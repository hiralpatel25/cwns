package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.OperAndMaintCategoryRef;
import gov.epa.owm.mtb.cwns.model.OperationAndMaintenanceCost;
import gov.epa.owm.mtb.cwns.model.OperationAndMaintenanceCostId;
import gov.epa.owm.mtb.cwns.oandm.OandMCategoryHelper;
import gov.epa.owm.mtb.cwns.oandm.OandMForm;
import gov.epa.owm.mtb.cwns.oandm.OandMListHelper;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.OandMService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class OandMServiceImpl extends CWNSService implements OandMService {

	public Collection generateExcludedList(long facilityId)
	{
		ArrayList exList = new ArrayList();

		ArrayList columns = new ArrayList();
		columns.add("id.costYear");				
		columns.add("id.operAndMaintCategoryId");				
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));      

		ArrayList sortArrayCat = new ArrayList();
	    sortArrayCat.add(new SortCriteria("id.costYear", SortCriteria.ORDER_DECENDING));	  

		List existingList = searchDAO.getSearchList(OperationAndMaintenanceCost.class, columns, scs, sortArrayCat);
		
    	Iterator iter = existingList.iterator();
      	
    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    		exList.add((Short)obj[0] + ":" +(Long)obj[1]);
    	}
    	
		return exList;
	}
	
	public Collection getOandMCategoryList(boolean existing, long facilityId, long year, long catIdAlwaysAvail)
	{
		boolean isNonPointSource = !facilityService.isFacility(new Long(facilityId));		
		ArrayList catList = new ArrayList();		
		ArrayList columns = new ArrayList();
		columns.add("operAndMaintCategoryId");		
		columns.add("name");

	    ArrayList sortArrayCat = new ArrayList();
	    sortArrayCat.add(new SortCriteria("name", SortCriteria.ORDER_ASCENDING));
	    
		SearchConditions scs = null;
		
		if(existing){
			scs = new SearchConditions(catAvailList(facilityId, year, catIdAlwaysAvail));
		}else{
			scs = new SearchConditions();
		}
		
		if (isNonPointSource){
			scs.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("operAndMaintCategoryId", SearchCondition.OPERATOR_EQ, 
													new Long(OandMService.OPER_AND_MAINT_CAT_TOTAL)));
		}
	    
	    List costList = searchDAO.getSearchList(OperAndMaintCategoryRef.class, columns, scs, sortArrayCat);	    
    	Iterator iter = costList.iterator();
  	
    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    		OandMCategoryHelper omh = new OandMCategoryHelper();    		
    		omh.setCategoryId(((Long)obj[0]).toString());
    		omh.setCategoryName((String)obj[1]);
    		catList.add(omh);
    	}
    	
		return catList;
	}
	
	//this function is only called when a total is present (or during edit)
	private SearchCondition catAvailList(long facilityId, long year, long catIdAlwaysAvail)
	{
		ArrayList columns = new ArrayList();
		columns.add("id.operAndMaintCategoryId");				
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));      
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("id.costYear", SearchCondition.OPERATOR_EQ, new Short((short)year)));	 
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("id.operAndMaintCategoryId", SearchCondition.OPERATOR_NOT_EQ, new Long(99)));	
		
		int count = searchDAO.getCount(OperationAndMaintenanceCost.class, scs);
		
		if(count == 0) //then it should be just total
		{
			return new SearchCondition("operAndMaintCategoryId", SearchCondition.OPERATOR_EQ, new Long(99));
		}
		else
		{
			scs.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("id.operAndMaintCategoryId", SearchCondition.OPERATOR_NOT_EQ, new Long(catIdAlwaysAvail)));	

			List existingList = searchDAO.getSearchList(OperationAndMaintenanceCost.class, columns, scs);
			
			if(existingList!=null)
			{
				existingList.add(new Long(99));
			}
			
			return new SearchCondition("operAndMaintCategoryId", SearchCondition.OPERATOR_NOT_IN, existingList);
		}
	}
	
	public OperationAndMaintenanceCost prepareOperationAndMaintenance(long facilityId, long oandMCatId, long year)
	{
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("operAndMaintCategoryRef", "omc", AliasCriteria.JOIN_LEFT));			
		
		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("id", SearchCondition.OPERATOR_EQ, new OperationAndMaintenanceCostId(facilityId, oandMCatId, (short)year)));
				
		return (OperationAndMaintenanceCost)searchDAO.getSearchObject(OperationAndMaintenanceCost.class, scs4, aliasArray);
	}
	
	public void deleteOandM(long facilityId, long oandMCatId, long year, CurrentUser user, boolean runPostSave, boolean isCatTypeSwitch)
	{
		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("id", SearchCondition.OPERATOR_EQ, new OperationAndMaintenanceCostId(facilityId, oandMCatId, (short)year)));
		
		OperationAndMaintenanceCost oandM = (OperationAndMaintenanceCost)searchDAO.getSearchObject(OperationAndMaintenanceCost.class, scs4);
	    
   	    if(oandM!=null)
   	    {
   	    	if (user.isNonLocalUser()){
   	    		searchDAO.removeObject(oandM);
   	    	}
        	else{	    					
				if (oandM.getFeedbackDeleteFlag()=='N'){
					oandM.setFeedbackDeleteFlag('Y');
				}else{
					oandM.setFeedbackDeleteFlag('N');
				}
			}
        	
        	if(oandMCatId!=99 && !isCatTypeSwitch)
        	{
        		createOrUpdateTotal(facilityId, (short)year, user.getUserId());        		
        		
        		// did we just deleted the last detail record?
        		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, 
    					new Long(facilityId)));      
	    		scs.setCondition(SearchCondition.OPERATOR_AND, 
	    				new SearchCondition("id.costYear", SearchCondition.OPERATOR_EQ, new Short((short)year)));	  
	    		int count = searchDAO.getCount(OperationAndMaintenanceCost.class, scs);
	    		
	    		if(count == 1)
	    		{
		    		scs.setCondition(SearchCondition.OPERATOR_AND, 
		    				new SearchCondition("id.operAndMaintCategoryId", SearchCondition.OPERATOR_EQ, new Long(99)));	
		    		
	    			OperationAndMaintenanceCost oandMTotal = (OperationAndMaintenanceCost)searchDAO.getSearchObject(OperationAndMaintenanceCost.class, scs);
	    			
	    			if(oandMTotal!=null)
	    			{	    				
	    				searchDAO.removeObject(oandMTotal);	    			
	    			}
	    		}
        	}
        	
            if(runPostSave && user != null)
            	facilityService.performPostSaveUpdates(new Long(facilityId), facilityService.DATA_AREA_NEEDS, user);
   	    }

		return;
	}
	
	public void saveOrUpdateOandM(OandMForm form, CurrentUser cUser)
	{
		String user = cUser.getUserId();

		OperationAndMaintenanceCost oandM = null;

		if(form.getCategoryId() != form.getOldCategoryId())
		{
			// delete the old record
			deleteOandM(form.getOandMFacilityId(), form.getOldCategoryId(), form.getYear(), cUser, false, true);			
		}
		
		SearchConditions scs = new SearchConditions(
			    new SearchCondition("id", SearchCondition.OPERATOR_EQ, new OperationAndMaintenanceCostId(form.getOandMFacilityId(), 
			    		form.getCategoryId(), (short)form.getYear())));
				
		oandM = (OperationAndMaintenanceCost)searchDAO.getSearchObject(OperationAndMaintenanceCost.class, scs);
    
        if (oandM==null)
        {
        	oandM = new OperationAndMaintenanceCost();	        	
        }
		
        Facility objFac = facilityService.findByFacilityId(new Long(form.getOandMFacilityId()).toString());
        
        oandM.setCollectionOrMaintenanceCost(new Integer((int)form.getCollectionCost()));
        oandM.setFacility(objFac);
        oandM.setOperAndMaintCategoryRef((OperAndMaintCategoryRef)searchDAO.getObject(OperAndMaintCategoryRef.class, new Long(form.getCategoryId())));
        oandM.setPlantOrMonitoringCost(new Integer((int)form.getPlantCost()));
        oandM.setId(new OperationAndMaintenanceCostId(form.getOandMFacilityId(), 
	    		form.getCategoryId(), (short)form.getYear()));
        
        oandM.setFeedbackDeleteFlag('N');
        oandM.setLastUpdateTs(new Date());
        oandM.setLastUpdateUserid(user);

        searchDAO.saveObject(oandM);
        
        if(form.getCategoryId() != 99)
        {
        	createOrUpdateTotal(form.getOandMFacilityId(), (short)form.getYear(), user);
        }
        
        facilityService.performPostSaveUpdates(new Long(form.getOandMFacilityId()), facilityService.DATA_AREA_NEEDS, cUser);

	}

	private void createOrUpdateTotal(long facilityId, long year, String user)
	{
    	int[] intI = getTotalCost(facilityId, year);
    	
    	SearchConditions scs = new SearchConditions(
			    new SearchCondition("id", SearchCondition.OPERATOR_EQ, new OperationAndMaintenanceCostId(facilityId, 
			    		99, (short)year)));
				
    	OperationAndMaintenanceCost oandM = (OperationAndMaintenanceCost)searchDAO.getSearchObject(OperationAndMaintenanceCost.class, scs);
    
        if (oandM==null)
        {
        	oandM = new OperationAndMaintenanceCost();	        	
        }
		
        Facility objFac = facilityService.findByFacilityId(new Long(facilityId).toString());
                
        oandM.setCollectionOrMaintenanceCost(new Integer(intI[1]));
        oandM.setFacility(objFac);
        oandM.setOperAndMaintCategoryRef((OperAndMaintCategoryRef)searchDAO.getObject(OperAndMaintCategoryRef.class, new Long(99)));
        oandM.setPlantOrMonitoringCost(new Integer(intI[0]));
        oandM.setId(new OperationAndMaintenanceCostId(facilityId, 99, (short)year));
        
        oandM.setFeedbackDeleteFlag('N');
        oandM.setLastUpdateTs(new Date());
        oandM.setLastUpdateUserid(user);

        searchDAO.saveObject(oandM);     		
	}
	
	private int[] getTotalCost(long facilityId, long year)
	{
		int[] intI = new int[2];
		
		ArrayList columns = new ArrayList();
		columns.add("plantOrMonitoringCost");		
		columns.add("collectionOrMaintenanceCost");		

		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("id.costYear", SearchCondition.OPERATOR_EQ, new Short((short)year)));	
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("id.operAndMaintCategoryId", SearchCondition.OPERATOR_NOT_EQ, new Long(99)));	
	    
		List costList = searchDAO.getSearchList(OperationAndMaintenanceCost.class, columns, scs);

		Iterator iter = costList.iterator();
      			
    	while( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    		intI[0] = intI[0] + (obj[0]==null?0:(((Integer)obj[0]).intValue()));  
    		intI[1] = intI[1] + (obj[1]==null?0:(((Integer)obj[1]).intValue()));  
    		
    	}
    	
    	return intI; 
	    
	}
	
	// return a OandMListHelper list
	public Collection getOandMList(long facilityId) {
		
		ArrayList oandMList = new ArrayList();

		ArrayList columns = new ArrayList();
		columns.add("omcr.operAndMaintCategoryId");		
		columns.add("omcr.name");		
		columns.add("id.costYear");		
		columns.add("plantOrMonitoringCost");		
		columns.add("collectionOrMaintenanceCost");
		columns.add("feedbackDeleteFlag");
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("operAndMaintCategoryRef", "omcr", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
			    					new Long(facilityId)));
		
	    ArrayList sortArrayCat = new ArrayList();
	    sortArrayCat.add(new SortCriteria("id.costYear", SortCriteria.ORDER_DECENDING));	  
	    sortArrayCat.add(new SortCriteria("omcr.sortSequence", SortCriteria.ORDER_ASCENDING));	  
	    
	    List costList = searchDAO.getSearchList(OperationAndMaintenanceCost.class, columns, scs, sortArrayCat, aliasArray);
    	Iterator iter = costList.iterator();
  	
    	long currentYear = -999;
    	
    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    	    
    	    OandMListHelper ch = new OandMListHelper();
    	    
    	    ch.setCategoryId(((Long)obj[0]).toString());
    	    ch.setCategoryName((String)obj[1]);
    	    ch.setYear(((Short)obj[2]).longValue());
    	    ch.setPlantCost(obj[3]==null?0:((Integer)obj[3]).longValue());
    	    ch.setCollectionCost(obj[4]==null?0:((Integer)obj[4]).longValue());
    	    ch.setTotalCost(ch.getPlantCost() + ch.getCollectionCost());
    	    ch.setFeedbackDeleteFlag(obj[4]==null?"":((Character)obj[5]).toString());
    	    
    	    if(currentYear != ch.getYear())
    	    {
    	    	// this is a new year
    	    	currentYear = ch.getYear();
    	    	
        	    if(Long.parseLong(ch.getCategoryId()) == 99)
        	    	ch.setHasDetailRecord("N");
    	    }
    	    
  	      oandMList.add(ch);
        	
    	} // while oandM query
    	
		return oandMList;
	}
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	

	private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
}