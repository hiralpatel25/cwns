package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityUtilMgmtPractice;
import gov.epa.owm.mtb.cwns.model.FacilityUtilMgmtPracticeId;
import gov.epa.owm.mtb.cwns.model.UtilMgmtImplmntStatusRef;
import gov.epa.owm.mtb.cwns.model.UtilMgmtPracticeRef;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.UtilityManagementService;
import gov.epa.owm.mtb.cwns.utilityManagement.UtilityManagementForm;
import gov.epa.owm.mtb.cwns.utilityManagement.UtilityManagementHelper;
import gov.epa.owm.mtb.cwns.utilityManagement.UtilityManagementStatusHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

public class UtilityManagementServiceImpl extends CWNSService implements UtilityManagementService {
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	private FacilityService faclityService;

	public void setFacilityService(FacilityService fs) {
		faclityService = fs;
	}
	
	public Collection getUtilityManagementList(long facilityId)
	{
		ArrayList umHelperList = new ArrayList();
		
		ArrayList sortArrayCat = new ArrayList();	  		
	    sortArrayCat.add(new SortCriteria("sortSequence", SortCriteria.ORDER_ASCENDING));	  		

		Collection umList = searchDAO.getSearchList(UtilMgmtPracticeRef.class, new SearchConditions(), sortArrayCat);

		Iterator iter = umList.iterator();

		while ( iter.hasNext() ) 
		{
			UtilMgmtPracticeRef obj = (UtilMgmtPracticeRef)iter.next();
			
			UtilityManagementHelper umh = new UtilityManagementHelper();
			
			umh.setUtilMgmtPracticeId(obj.getUtilMgmtPracticeId());
			umh.setUtilMgmtPracticeName(obj.getName());
			
			// noew check FacilityUnitlityManagemnt
			ArrayList columns = new ArrayList();
			columns.add("umis.utilMgmtImplmntStatusId");		
			columns.add("umis.name");		
			columns.add("annualCostToOperate");		
			columns.add("remainingCostToImplement");		

			ArrayList aliasArray = new ArrayList();
			aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));			
			aliasArray.add(new AliasCriteria("utilMgmtPracticeRef", "ump", AliasCriteria.JOIN_INNER));	
			aliasArray.add(new AliasCriteria("utilMgmtImplmntStatusRef", "umis", AliasCriteria.JOIN_INNER));	

			SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
					new Long(facilityId)));
			scs.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("ump.utilMgmtPracticeId", SearchCondition.OPERATOR_EQ, new Long(obj.getUtilMgmtPracticeId())));    			

			Collection fumpList = searchDAO.getSearchList(FacilityUtilMgmtPractice.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);

			Iterator iterFump = fumpList.iterator();
			
			if( iterFump.hasNext() ) 
			{
	    		Object[] objFump = (Object[])iterFump.next();
	    		umh.setUtilMgmtImplmntStatusId(objFump[0]==null?-999:((Long)objFump[0]).longValue());
	    		umh.setUtilMgmtImplmntStatusName(objFump[1]==null?"":(String)objFump[1]);	    		
	    		umh.setAnnualCostToOperate(objFump[2]==null?-999:((Long)objFump[2]).longValue());
	    		umh.setRemainingCostToImplement(objFump[3]==null?-999:((Long)objFump[3]).longValue());
	    		
	    		if(umh.getUtilMgmtImplmntStatusId()==2 || umh.getUtilMgmtImplmntStatusId()==3)
		    		umh.setIsRemainingCostDisabled("N");

	    		if(umh.getUtilMgmtImplmntStatusId()==1 || umh.getUtilMgmtImplmntStatusId()==2 || 
	    				umh.getUtilMgmtImplmntStatusId()==3)
	    			umh.setIsAnnualCostToOperatetDisabled("N");
			}
			
			umHelperList.add(umh);
		}
		
		return umHelperList;
	}
	
	public Collection getUtilityManagementByFacility(Long facilityId){		
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection fumpList = searchDAO.getSearchList(FacilityUtilMgmtPractice.class, scs);
		return fumpList;		
	}
	
	public Collection getUtilityManagementStatusList()
	{
		ArrayList umStatusHelperList = new ArrayList();

		ArrayList sortArrayCat = new ArrayList();	  		
	    sortArrayCat.add(new SortCriteria("utilMgmtImplmntStatusId", SortCriteria.ORDER_ASCENDING));	  		

		Collection umList = searchDAO.getSearchList(UtilMgmtImplmntStatusRef.class, new SearchConditions(), sortArrayCat);

		Iterator iter = umList.iterator();

		while ( iter.hasNext() ) 
		{
			UtilMgmtImplmntStatusRef obj = (UtilMgmtImplmntStatusRef)iter.next();
			
			UtilityManagementStatusHelper ums = new UtilityManagementStatusHelper();
			
			ums.setUtilMgmtStatusId(obj.getUtilMgmtImplmntStatusId());
			ums.setUtilMgmtStatusName(obj.getName());
			
			umStatusHelperList.add(ums);
		}
		
		return umStatusHelperList;
	}
	
	/*
	 * the resultStr should be passed back as a list that contains items in the format of
	 * utilMgmtPracticeId:utilMgmtImplmntStatusId:remainingCostToImplement:annualCostToOperate;
	 */
	public void saveOrUpdateUtilityManagement(long facilityId, String resultStr, CurrentUser user)
	{
		if(resultStr==null || resultStr.length() == 0)
			return;
	     
	   StringTokenizer t = new StringTokenizer(resultStr, ";");
	   
	     while (t.hasMoreTokens()) 
	     {
			 String str = t.nextToken();
		     
		     StringTokenizer tokenizer = new StringTokenizer(str, ":");
		     
			long utilMgmtPracticeId = UtilityManagementForm.INITIAL_LONG_VALUE;
			long utilMgmtImplmntStatusId = UtilityManagementForm.INITIAL_LONG_VALUE;
			long remainingCostToImplement = UtilityManagementForm.INITIAL_LONG_VALUE;
			long annualCostToOperate = UtilityManagementForm.INITIAL_LONG_VALUE;
				
			try
			{
				utilMgmtPracticeId = Long.parseLong(tokenizer.nextToken());
				utilMgmtImplmntStatusId = Long.parseLong(tokenizer.nextToken());
				remainingCostToImplement = Long.parseLong(tokenizer.nextToken());
				annualCostToOperate = Long.parseLong(tokenizer.nextToken());
			}
			catch(NumberFormatException e)
			{
				return;
			}
			
			if(utilMgmtPracticeId < 0)
				continue;
			
			//try to get the FUM obj
			FacilityUtilMgmtPracticeId fumpId = new FacilityUtilMgmtPracticeId(facilityId, utilMgmtPracticeId);
			
			SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, fumpId));
			FacilityUtilMgmtPractice fump = (FacilityUtilMgmtPractice)searchDAO.getSearchObject(FacilityUtilMgmtPractice.class, scs);
	        
			if(utilMgmtImplmntStatusId < 0)
			{
				// try to delete the record if FUM exists
				if(fump != null)
					searchDAO.removeObject(fump);
				
				continue;
			}
			
			//if FUM obj is not null, update status Id, else create new one
			if(fump == null)
			{
				fump = new FacilityUtilMgmtPractice();
				fump.setId(fumpId);
				fump.setFacility(faclityService.findByFacilityId(new Long(facilityId).toString()));
				fump.setUtilMgmtPracticeRef((UtilMgmtPracticeRef)searchDAO.getObject(UtilMgmtPracticeRef.class, new Long(utilMgmtPracticeId)));
			}
	
			fump.setUtilMgmtImplmntStatusRef((UtilMgmtImplmntStatusRef)searchDAO.getObject(UtilMgmtImplmntStatusRef.class, new Long(utilMgmtImplmntStatusId)));

			if(remainingCostToImplement <= 0)
			{
				//set to null
				fump.setRemainingCostToImplement(null);
			}
			else
			{
				// update
				fump.setRemainingCostToImplement(new Long(remainingCostToImplement));
			}
	
			if(annualCostToOperate <= 0)
			{
				//set to null
				fump.setAnnualCostToOperate(null);
			}
			else
			{
				// update
				fump.setAnnualCostToOperate(new Long(annualCostToOperate));
			}
			
			fump.setLastUpdateTs(new Date());
			fump.setLastUpdateUserid(user.getUserId());
			
			searchDAO.saveObject(fump);
	     } // while ;
	     
		if(user.getCurrentRole().getLocationTypeId().equalsIgnoreCase("Local"))
		{
			Facility f = (Facility)faclityService.findByFacilityId((new Long(facilityId)).toString());
		
			if(f !=null )
			{
				f.setLocalUserDoesUtilMgmtFlag('Y');
				searchDAO.saveObject(f);
			}
		}
     
 		faclityService.performPostSaveUpdates(new Long(facilityId), faclityService.DATA_AREA_UTIL_MANAGEMENT, user);
	     
	}// function
	
	public String warnStateUSerAboutLocal(long facilityId, CurrentUser user)
	{
		String result = "N";
		
		if(user.getCurrentRole().getLocationTypeId().equalsIgnoreCase("State"))
		{
			Facility f = (Facility)faclityService.findByFacilityId((new Long(facilityId)).toString());
		
			if(f.getLocalUserDoesUtilMgmtFlag() == 'Y')
				result = "Y";
		}
		
		return result;
	}
}