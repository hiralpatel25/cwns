package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.ChangeTypeRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityUnitProcess;
import gov.epa.owm.mtb.cwns.model.FacilityUnitProcessChange;
import gov.epa.owm.mtb.cwns.model.FacilityUnitProcessChangeId;
import gov.epa.owm.mtb.cwns.model.FacilityUnitProcessId;
import gov.epa.owm.mtb.cwns.model.TreatmentTypeRef;
import gov.epa.owm.mtb.cwns.model.TreatmentTypeUnitprocessRef;
import gov.epa.owm.mtb.cwns.model.TreatmentTypeUnitprocessRefId;
import gov.epa.owm.mtb.cwns.model.UnitProcessRef;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.UnitProcessService;
import gov.epa.owm.mtb.cwns.unitProcess.UnitProcessChangeHelper;
import gov.epa.owm.mtb.cwns.unitProcess.UnitProcessForm;
import gov.epa.owm.mtb.cwns.unitProcess.UnitProcessHelper;
import gov.epa.owm.mtb.cwns.unitProcess.UnitProcessListHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

public class UnitProcessServiceImpl extends CWNSService implements UnitProcessService {
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	private FacilityService faclityService;

	public void setFacilityService(FacilityService fs) {
		faclityService = fs;
	}
	
	public String warnStateUSerAboutLocal(long facilityId, CurrentUser user)
	{
		String result = "N";
		
		if(user.getCurrentRole().getLocationTypeId().equalsIgnoreCase("State"))
		{
			Facility f = (Facility)faclityService.findByFacilityId((new Long(facilityId)).toString());
		
			if(f.getLocalUserDoesUnitProcFlag() == 'Y')
				result = "Y";
		}
		
		return result;
	}
	
	public void moveUnitProcess(long facilityId, long treatmentTypeId, long unitProcessRadioId, int index, CurrentUser user)
	{
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("treatmentTypeUnitprocessRef", "ttup", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ttup.treatmentTypeRef", "tt", AliasCriteria.JOIN_INNER));

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("tt.treatmentTypeId", SearchCondition.OPERATOR_EQ, new Long(treatmentTypeId)));    			

		ArrayList sortArrayCat = new ArrayList();	  		
	    sortArrayCat.add(new SortCriteria("sortSequence", (index>0)?SortCriteria.ORDER_DECENDING:SortCriteria.ORDER_ASCENDING));	  		

		Collection upList = searchDAO.getSearchList(FacilityUnitProcess.class, scs, sortArrayCat, aliasArray, 0, 0);

		Iterator iter = upList.iterator();
		
		boolean nextReady = false;
		
		int count = (upList==null?0:upList.size());
		
		int countIndex = 0;
		
		boolean dataDirty = false;
		
		while ( iter.hasNext() ) 
		{
			countIndex++;
			
			FacilityUnitProcess obj = (FacilityUnitProcess)iter.next();
    		
			if(nextReady)
			{
		   		obj.setSortSequence(new Byte((byte)((obj.getSortSequence()==null?0:obj.getSortSequence().byteValue()) + index)));				
				searchDAO.saveObject(obj);
				dataDirty = true;
				break;
			}
				
    		if(obj.getId().getUnitProcessId()!=unitProcessRadioId || countIndex >= count ) // if reach the edge, quit
    			continue;
    		
    		obj.setSortSequence(new Byte((byte)((obj.getSortSequence()==null?0:obj.getSortSequence().byteValue()) - index)));
    		
    		searchDAO.saveObject(obj);
    		dataDirty = true;
    		
    		nextReady = true;
    		
		}
		
		if(dataDirty)
			faclityService.performPostSaveUpdates(new Long(facilityId), faclityService.DATA_AREA_UNIT_PROCESS, user);			
		

	}
	
	public void prepareFacilityUnitProcess(UnitProcessForm form)
	{
		FacilityUnitProcessId fupId = new FacilityUnitProcessId(form.getUnitProcessFacilityId(), form.getTreatmentTypeId(), form.getUnitProcessListId());
		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, fupId));
        FacilityUnitProcess facilityUnitProcess = (FacilityUnitProcess)searchDAO.getSearchObject(FacilityUnitProcess.class, scs);
        
        if(facilityUnitProcess !=null)
        {
        	form.setBackupFlag(facilityUnitProcess.getBackupFlag());
        	form.setDescription(facilityUnitProcess.getDescription());
        	form.setPlanYear(facilityUnitProcess.getPlannedYear()==null?0:facilityUnitProcess.getPlannedYear().shortValue());
        	if(facilityUnitProcess.getPresentFlag() == 'Y' && facilityUnitProcess.getProjectedFlag() == 'Y')
        		form.setPresentProjectedFlag(3);
        	else if(facilityUnitProcess.getPresentFlag() == 'Y')
        		form.setPresentProjectedFlag(1);
        	else if(facilityUnitProcess.getProjectedFlag() == 'Y')
        		form.setPresentProjectedFlag(2);
        	else
        		form.setPresentProjectedFlag(0); // nothing is selected
        	
        	UnitProcessRef upr = (UnitProcessRef)searchDAO.getObject(UnitProcessRef.class, new Long(form.getUnitProcessListId()));
        	
        	if(upr != null)
        	{
        		form.setUnitProcessFormId(upr.getUnitProcessId());
        		form.setUnitProcessFormName(upr.getName());
        	}
        }
	
	}
	
	public void saveOrUpdateUnitProcess(UnitProcessForm form, CurrentUser user)
	{
		FacilityUnitProcessId fupId = new FacilityUnitProcessId(form.getUnitProcessFacilityId(), 
				form.getTreatmentTypeId(), form.getUnitProcessFormId());
		
		TreatmentTypeUnitprocessRefId ttUpId = new TreatmentTypeUnitprocessRefId(form.getTreatmentTypeId(), 
								form.getUnitProcessFormId());
		
        SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, fupId));

        FacilityUnitProcess facilityUnitProcess = (FacilityUnitProcess)searchDAO.getSearchObject(FacilityUnitProcess.class, scs);

        char presentChar='N', projectedChar='N';
        
        if(form.getPresentProjectedFlag()==1)
        	presentChar = 'Y';
        else if(form.getPresentProjectedFlag()==2)
        	projectedChar = 'Y';
        else if(form.getPresentProjectedFlag()==3)
        {
        	presentChar = 'Y';
        	projectedChar = 'Y';
        }
        
        if (facilityUnitProcess==null){
        
        	facilityUnitProcess = new FacilityUnitProcess();
        	
        	facilityUnitProcess.setId(fupId);	
        	
        	facilityUnitProcess.setFacility(faclityService.findByFacilityId(new Long(form.getUnitProcessFacilityId()).toString()));
        	//facilityUnitProcess.setFacilityUnitProcessChanges(facilityUnitProcessChanges);
        	
        	SearchConditions scs0 = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, ttUpId));
        	TreatmentTypeUnitprocessRef ttUp = 
        		(TreatmentTypeUnitprocessRef)searchDAO.getSearchObject(TreatmentTypeUnitprocessRef.class, scs0);
        	
        	facilityUnitProcess.setTreatmentTypeUnitprocessRef(ttUp);
        	
        	facilityUnitProcess.setSortSequence(new Byte((byte)(1+getMaxSortSequence(form.getUnitProcessFacilityId(), 
    				form.getTreatmentTypeId(), true))));
        }
        
    	facilityUnitProcess.setBackupFlag(form.getBackupFlag());
    	facilityUnitProcess.setDescription(form.getDescription());
    	facilityUnitProcess.setPlannedYear(form.getPlanYear()==0?null:(new Short(form.getPlanYear())));
    	facilityUnitProcess.setPresentFlag(presentChar);
    	facilityUnitProcess.setProjectedFlag(projectedChar);
    	
        facilityUnitProcess.setLastUpdateTs(new Date());
        facilityUnitProcess.setLastUpdateUserid(user.getUserId());
        
        searchDAO.saveObject(facilityUnitProcess);
        
    	updateUnitProcessChangeTypeRecords(new Long(form.getUnitProcessFacilityId()).toString(),
    			form.getCommaDilimitedChangeTypeIds(), form.getTreatmentTypeId(), form.getUnitProcessFormId(), user);
    	
		if(user.getCurrentRole().getLocationTypeId().equalsIgnoreCase("Local"))
		{
			Facility f = (Facility)faclityService.findByFacilityId((new Long(form.getUnitProcessFacilityId())).toString());
		
			if(f !=null )
			{
				f.setLocalUserDoesUnitProcFlag('Y');
				searchDAO.saveObject(f);
			}
		}

    		faclityService.performPostSaveUpdates(new Long(form.getUnitProcessFacilityId()), faclityService.DATA_AREA_UNIT_PROCESS, user);
}

	private byte getMaxSortSequence(long facilityId, long treatmentTypeId, boolean getMax)
	{
		ArrayList columns = new ArrayList();
		columns.add("sortSequence");		//l
		columns.add("lastUpdateTs");		//l		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("treatmentTypeUnitprocessRef", "ttup", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ttup.treatmentTypeRef", "tt", AliasCriteria.JOIN_INNER));

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("tt.treatmentTypeId", SearchCondition.OPERATOR_EQ, new Long(treatmentTypeId)));    			

		ArrayList sortArrayCat = new ArrayList();	  		
	    sortArrayCat.add(new SortCriteria("sortSequence", getMax?SortCriteria.ORDER_DECENDING:SortCriteria.ORDER_ASCENDING));	  		

		Collection upList = searchDAO.getSearchList(FacilityUnitProcess.class, columns, scs, sortArrayCat, aliasArray, 0, 0, true);

		Iterator iter = upList.iterator();
		
		if ( iter.hasNext() ) 
		{
    		Object[] obj = (Object[])iter.next();
    		return (byte)(obj[0]==null?0:((Byte)obj[0]).byteValue());
		}
		else
		{
			return (byte)0;
		}
	}
	
	public Collection getUnitProcessDropdownList(long facilityId, long treatmentTypeId)
	{
		ArrayList columns = new ArrayList();
		columns.add("up.unitProcessId");		//l
	
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("treatmentTypeUnitprocessRef", "ttup", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ttup.treatmentTypeRef", "tt", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ttup.unitProcessRef", "up", AliasCriteria.JOIN_INNER));	

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("tt.treatmentTypeId", SearchCondition.OPERATOR_EQ, new Long(treatmentTypeId)));    			

	    //ArrayList sortArrayCat = new ArrayList();	  		
	    //sortArrayCat.add(new SortCriteria("up.name", SortCriteria.ORDER_ASCENDING));	  		

		Collection upList = searchDAO.getSearchList(FacilityUnitProcess.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);
		
		// now treatmentTypeUnitProcessRef
		columns = new ArrayList();
		columns.add("up.unitProcessId");
		columns.add("up.name");
		
		aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("unitProcessRef", "up", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("treatmentTypeRef", "tt", AliasCriteria.JOIN_INNER));			

		scs = new SearchConditions(new SearchCondition("tt.treatmentTypeId", SearchCondition.OPERATOR_EQ, 
				new Long(treatmentTypeId)));
		
		if(upList!=null && upList.size() > 0)
			scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("up.unitProcessId", SearchCondition.OPERATOR_NOT_IN, upList));    			
		
	    ArrayList sortArrayCat = new ArrayList();	  		
	    sortArrayCat.add(new SortCriteria("up.name", SortCriteria.ORDER_ASCENDING));	  		
		
		Collection ttUpList = searchDAO.getSearchList(TreatmentTypeUnitprocessRef.class, columns, scs, sortArrayCat, aliasArray, 0, 0, true);
	    
		Iterator iter = ttUpList.iterator();
		
		ArrayList unitProcessList = new ArrayList();
		
		while ( iter.hasNext() ) 
		{
    		Object[] objCT = (Object[])iter.next();
    		UnitProcessHelper uph = new UnitProcessHelper();
    		
    		uph.setUnitProcessId(((Long)objCT[0]).longValue());
    		uph.setUnitProcessName((String)objCT[1]);
    		
    		unitProcessList.add(uph);
		}	    
		
		return unitProcessList;
		
	}
	
	public void updateUnitProcessChangeTypeRecords(String facilityId, String commaDelimitedString, long treatmentTypeId, long unitProcessListId, CurrentUser user)
	{
		String userId = user.getUserId();
		
		ArrayList existingCTList = 
			(ArrayList)getFacilityUnitProcessChangeTypes((new Long(facilityId).longValue()), treatmentTypeId, unitProcessListId);

		boolean dataDirtyFlag = false;
		
		if(commaDelimitedString!=null)
		{
		     StringTokenizer tokenizer = new StringTokenizer(commaDelimitedString, ",");
		     
		     while (tokenizer.hasMoreTokens()) {

				String str = tokenizer.nextToken();
		    	
				Iterator iter = existingCTList.iterator();
				
				boolean matchExisting = false;
				
		    	while ( iter.hasNext() ) {
		    		
		    		UnitProcessChangeHelper pp = (UnitProcessChangeHelper)iter.next();
		    		
		    		if((new Long(str)).longValue() == pp.getChangeTypeId())
		    		{
		    			//one of the existing - no touch
		    			matchExisting = true;
		    			existingCTList.remove(pp);
		    			break;
		    		}
		    	}				
		    	
		    	if(!matchExisting)
		    	{
		    		//insert this problemId
		    		insertFacilityUnitProcessChangeTypes((new Long(facilityId)).longValue(), treatmentTypeId, unitProcessListId, (new Long(str)).longValue(), userId);
		    		dataDirtyFlag = true;
		    	}
			}
		}
		
		// now the remaining ones in the existingPPList should be deleted

		Iterator iter = existingCTList.iterator();

    	while ( iter.hasNext() ) {	
    		UnitProcessChangeHelper pp = (UnitProcessChangeHelper)iter.next();
    		
    		// delete pp
    		deleteFacilityUnitProcessChangeType((new Long(facilityId)).longValue(), treatmentTypeId, unitProcessListId, pp.getChangeTypeId());
    		dataDirtyFlag = true;
    	}		
		
		if(dataDirtyFlag)
			faclityService.performPostSaveUpdates(new Long(facilityId), faclityService.DATA_AREA_UNIT_PROCESS, user);			
    	
	}	
	
	private void insertFacilityUnitProcessChangeTypes(long facilityId,  
			long treatmentTypeId, long unitProcessId, long changeTypeId, String userId)
	{
		FacilityUnitProcessChange fupc = new FacilityUnitProcessChange();
		
        SearchConditions scs = new SearchConditions(new SearchCondition("changeTypeId", SearchCondition.OPERATOR_EQ, new Long(changeTypeId)));
        ChangeTypeRef ctr = (ChangeTypeRef)searchDAO.getSearchObject(ChangeTypeRef.class, scs);		
		
		fupc.setChangeTypeRef(ctr);
		
		FacilityUnitProcessId fupId = new FacilityUnitProcessId(facilityId, treatmentTypeId, unitProcessId);
        scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, fupId));
        FacilityUnitProcess facilityUnitProcess = (FacilityUnitProcess)searchDAO.getSearchObject(FacilityUnitProcess.class, scs);
				
		fupc.setFacilityUnitProcess(facilityUnitProcess);
		
		fupc.setId(new FacilityUnitProcessChangeId(facilityId, treatmentTypeId, unitProcessId, changeTypeId));
		
		fupc.setLastUpdateTs(new Date());
		fupc.setLastUpdateUserid(userId);
		
		searchDAO.saveObject(fupc);
	}
	
	private void  deleteFacilityUnitProcessChangeType(
			long facilityId,  long treatmentTypeId, long unitProcessId, long changeTypeId)
	{
		FacilityUnitProcessChangeId fupcId = new FacilityUnitProcessChangeId(facilityId, treatmentTypeId, unitProcessId, changeTypeId);

		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, fupcId));
		FacilityUnitProcessChange fupc = (FacilityUnitProcessChange)searchDAO.getSearchObject(FacilityUnitProcessChange.class, scs);
		
		searchDAO.removeObject(fupc);
	}

	public void deleteFacilityUnitProcess(UnitProcessForm form, CurrentUser user)
	{
		ArrayList aliasArray = new ArrayList();
		
		aliasArray.add(new AliasCriteria("facilityUnitProcess", "fup", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("fup.facility", "f", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("fup.treatmentTypeUnitprocessRef", "ttup", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ttup.treatmentTypeRef", "tt", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ttup.unitProcessRef", "up", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs1 = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(form.getUnitProcessFacilityId())));				
		scs1.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("tt.treatmentTypeId", SearchCondition.OPERATOR_EQ, new Long(form.getTreatmentTypeId())));    			
		scs1.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("up.unitProcessId", SearchCondition.OPERATOR_EQ, new Long(form.getUnitProcessListId())));    			
		
		Collection changeList = searchDAO.getSearchList(FacilityUnitProcessChange.class, scs1, new ArrayList(), aliasArray, 0, 0);
		
		if(changeList != null && changeList.size() > 0)
			searchDAO.deleteAll(changeList);
		
		FacilityUnitProcessId fupId = new FacilityUnitProcessId(form.getUnitProcessFacilityId(), 
				form.getTreatmentTypeId(), form.getUnitProcessListId());
		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, fupId));
		
        FacilityUnitProcess facilityUnitProcess = (FacilityUnitProcess)searchDAO.getSearchObject(FacilityUnitProcess.class, scs);
        
        byte sortSequence = 0;
        
        if(facilityUnitProcess!=null)
        {
        	sortSequence =  facilityUnitProcess.getSortSequence().byteValue();
        	
        	searchDAO.removeObject(facilityUnitProcess);
        }
        else
        {   // should throw serious error
        	return;
        }
        
    	shiftFacilityUnitProcessSortSequence(form.getUnitProcessFacilityId(), 
    							form.getTreatmentTypeId(), sortSequence);

        
		faclityService.performPostSaveUpdates(new Long(form.getUnitProcessFacilityId()), faclityService.DATA_AREA_UNIT_PROCESS, user);
        
	}
	
	private void shiftFacilityUnitProcessSortSequence(long facilityId, long treatmentTypeId, byte sortSequence)
	{
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("treatmentTypeUnitprocessRef", "ttup", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ttup.treatmentTypeRef", "tt", AliasCriteria.JOIN_INNER));

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("tt.treatmentTypeId", SearchCondition.OPERATOR_EQ, new Long(treatmentTypeId)));    			
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("sortSequence", SearchCondition.OPERATOR_GT, new Byte(sortSequence)));    			

		Collection upList = searchDAO.getSearchList(FacilityUnitProcess.class, scs,  new ArrayList(), aliasArray, 0, 0);

		Iterator iter = upList.iterator();
		
		while ( iter.hasNext() ) 
		{
			FacilityUnitProcess obj = (FacilityUnitProcess)iter.next();
			obj.setSortSequence(new Byte((byte)(obj.getSortSequence().byteValue()-1)));
		}
		
	}
	
	public Collection getGlobalUnitProcessChangeTypes()
	{
		ArrayList globalUnitProcessChangeTypes = new ArrayList();
		
	    ArrayList sortArrayCat = new ArrayList();
	    sortArrayCat.add(new SortCriteria("sortSequence", SortCriteria.ORDER_ASCENDING));	  			  		

		Iterator iter0 = searchDAO.getSearchList(ChangeTypeRef.class, new SearchConditions(), sortArrayCat).iterator();
		
		while ( iter0.hasNext() ) 
		{
			ChangeTypeRef obj = (ChangeTypeRef)iter0.next();
    		
    		UnitProcessChangeHelper upch = new UnitProcessChangeHelper();
    		
    		upch.setChangeTypeId(obj.getChangeTypeId());
    		upch.setChangeTypeName(obj.getName());
    		
    		globalUnitProcessChangeTypes.add(upch);
		}
		
		return globalUnitProcessChangeTypes;

	}
	
	public Collection getFacilityUnitProcessChangeTypes(long facId, long treatmentTypeId, long unitProcessListId)
	{
		ArrayList columns = new ArrayList();
		columns.add("ctr.changeTypeId");		//l
		columns.add("ctr.name");		//S
		columns.add("ctr.sortSequence");		//S
	
		ArrayList aliasArray = new ArrayList();
		
		aliasArray.add(new AliasCriteria("facilityUnitProcess", "fup", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("fup.facility", "f", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("fup.treatmentTypeUnitprocessRef", "ttup", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ttup.treatmentTypeRef", "tt", AliasCriteria.JOIN_INNER));
		
		if(unitProcessListId > 0)
			aliasArray.add(new AliasCriteria("ttup.unitProcessRef", "up", AliasCriteria.JOIN_INNER));
		
		aliasArray.add(new AliasCriteria("changeTypeRef", "ctr", AliasCriteria.JOIN_INNER));	
		
		SearchConditions scs1 = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facId)));				
		scs1.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("tt.treatmentTypeId", SearchCondition.OPERATOR_EQ, new Long(treatmentTypeId)));    			
		
		if(unitProcessListId > 0)
		scs1.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("up.unitProcessId", SearchCondition.OPERATOR_EQ, new Long(unitProcessListId)));    			
		
		ArrayList sortArrayCat = new ArrayList();	  		
	    sortArrayCat.add(new SortCriteria("ctr.sortSequence", SortCriteria.ORDER_ASCENDING));	  		
	    
		Collection changeTypeList = searchDAO.getSearchList(FacilityUnitProcessChange.class, columns, scs1, sortArrayCat, aliasArray, 0, 0, true);
		
		Iterator iterCT = changeTypeList.iterator();
		
		ArrayList ctList = new ArrayList();
		
		while ( iterCT.hasNext() ) 
		{
    		Object[] objCT = (Object[])iterCT.next();
    		UnitProcessChangeHelper upch = new UnitProcessChangeHelper();
    		
    		upch.setChangeTypeId(((Long)objCT[0]).longValue());
    		upch.setChangeTypeName((String)objCT[1]);
    		
    		ctList.add(upch);
		}	    
		
		return ctList;
	}
	
	public Collection getAvailUnitProcessChangeTypes(Collection unitProcessChangeGlobalTypeList, Collection selectedUnitProcessChangeTypes, int presentProjectedFlag)
	{
		ArrayList availList = new ArrayList();
		
		Iterator iterCT = unitProcessChangeGlobalTypeList.iterator();

		while ( iterCT.hasNext() ) 
		{
    		UnitProcessChangeHelper upch = (UnitProcessChangeHelper)iterCT.next();
    		
    		boolean found = false;
    		
    		Iterator iterSel = selectedUnitProcessChangeTypes.iterator();

    		while ( iterSel.hasNext() )
    		{
    			UnitProcessChangeHelper upchSel = (UnitProcessChangeHelper)iterSel.next();
    			if(upchSel.getChangeTypeId() == upch.getChangeTypeId())
    			{
    				found = true;
    				break;
    			}	
			}
    		
    		if(!found)
    		{
    			if(presentProjectedFlag == 1 && upch.getChangeTypeId() == 7) // present
    				{
    					availList.add(upch);
    					break;
    				}
    			else if(presentProjectedFlag == 2 && upch.getChangeTypeId() == 2) // projected
    				{
	    				availList.add(upch);
	    				break;
    				}
    			else if(presentProjectedFlag == 3 && upch.getChangeTypeId() != 7 && upch.getChangeTypeId() != 2) // present & projected
    				availList.add(upch);
    		}
		}
		
		return availList;
		
	}
	
	private Collection getFacilityOverallType(long facilityId)
	{
		ArrayList columns = new ArrayList();
		columns.add("fot.facilityOverallTypeId");		
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("facilityTypeRef", "ft", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ft.facilityOverallTypeRef", "fot", AliasCriteria.JOIN_INNER));	
		
		SearchConditions scs0 = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));

		return searchDAO.getSearchList(FacilityType.class, columns, scs0, new ArrayList(), aliasArray);			

	}
	
	public Collection getUnitProcessList(long facilityId)
	{
		
		ArrayList unitProcessList = new ArrayList();
				
		ArrayList columns = new ArrayList();
		columns.add("treatmentTypeId");		
		columns.add("name");
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityOverallTypeRef", "fot", AliasCriteria.JOIN_INNER));	
		
		SearchConditions scs0 = new SearchConditions(new SearchCondition("fot.facilityOverallTypeId", 
				SearchCondition.OPERATOR_IN, getFacilityOverallType(facilityId)));

	    ArrayList sortArrayCat = new ArrayList();
	    sortArrayCat.add(new SortCriteria("sortSequence", SortCriteria.ORDER_ASCENDING));	  			  		

		Collection ttList = searchDAO.getSearchList(TreatmentTypeRef.class, columns, scs0, sortArrayCat, aliasArray);			

		Iterator iter0 = ttList.iterator();
		
		while ( iter0.hasNext() ) 
		{
    		Object[] objTT = (Object[])iter0.next();
    		
    		UnitProcessListHelper uplh = new UnitProcessListHelper();
    		uplh.setTreatmentTypeId(((Long)objTT[0]).longValue());
    		uplh.setTreatmentTypeName((String)objTT[1]);
    		
			//------ beginning - unit process --------------
			columns = new ArrayList();
			columns.add("up.unitProcessId");		//l
			columns.add("up.name");		//S
			columns.add("presentFlag");	//c	
			columns.add("projectedFlag");	//c
			columns.add("backupFlag");		//c
			columns.add("plannedYear");		//short
			columns.add("description");		//S
			columns.add("sortSequence");		//S
		
			aliasArray = new ArrayList();
			aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));			
			aliasArray.add(new AliasCriteria("treatmentTypeUnitprocessRef", "ttup", AliasCriteria.JOIN_INNER));	
			aliasArray.add(new AliasCriteria("ttup.treatmentTypeRef", "tt", AliasCriteria.JOIN_INNER));
			aliasArray.add(new AliasCriteria("ttup.unitProcessRef", "up", AliasCriteria.JOIN_INNER));	
	
			SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
					new Long(facilityId)));
			scs.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("tt.treatmentTypeId", SearchCondition.OPERATOR_EQ, new Long(uplh.getTreatmentTypeId())));    			
	
		    sortArrayCat = new ArrayList();	  		
		    sortArrayCat.add(new SortCriteria("sortSequence", SortCriteria.ORDER_ASCENDING));	  		
	
			Collection upList = searchDAO.getSearchList(FacilityUnitProcess.class, columns, scs, sortArrayCat, aliasArray, 0, 0, true);
	
			Iterator iter = upList.iterator();
			
			ArrayList uphList = new ArrayList();
			
			int indexChecked = 0;
			
			while ( iter.hasNext() ) 
			{
	    		Object[] obj = (Object[])iter.next();
	    		UnitProcessHelper uph = new UnitProcessHelper();

	    		uph.setUnitProcessId(((Long)obj[0]).longValue());
	    		uph.setUnitProcessName((String)obj[1]);
	    		uph.setPresentFlag(((Character)obj[2]).charValue());
	    		uph.setProjectedFlag(((Character)obj[3]).charValue());
	    		uph.setBackupFlag(((Character)obj[4]).charValue());
	    		uph.setPlannedYear(obj[5]==null?0:((Short)obj[5]).shortValue());
	    		uph.setDescription(obj[6]==null?"":(String)obj[6]);
	    		if(indexChecked == 0)
	    		{
	    			indexChecked = 1;
	    			uph.setChecked("checked");
	    		}

				uph.setChangeTypeList((ArrayList)getFacilityUnitProcessChangeTypes(facilityId, uplh.getTreatmentTypeId(), uph.getUnitProcessId()));
				
	    		uphList.add(uph);
			}
			//------ ending - unit process --------------
			
			uplh.setUnitProcessList(uphList);
			uplh.setNumberOfUnitProcesses(uphList.size());
			
			unitProcessList.add(uplh);
		}
		
		return unitProcessList;
	}
	
	public Collection getUnitProcessListByFacility(Long facilityId){	
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId)); 
		return searchDAO.getSearchList(FacilityUnitProcess.class, scs);	
	}

	public SearchDAO getSearchDAO() {
		return searchDAO;
	}
	
}