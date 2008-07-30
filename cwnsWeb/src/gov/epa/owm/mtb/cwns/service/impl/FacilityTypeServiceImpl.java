package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.ChangeTypeRef;
import gov.epa.owm.mtb.cwns.model.ChangeTypeRuleRef;
import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.DataAreaRef;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityAdvancedTreatment;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataArea;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataAreaId;
import gov.epa.owm.mtb.cwns.model.FacilityDataAreaMessage;
import gov.epa.owm.mtb.cwns.model.FacilityDischarge;
import gov.epa.owm.mtb.cwns.model.FacilityDocument;
import gov.epa.owm.mtb.cwns.model.FacilityEffluent;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatusId;
import gov.epa.owm.mtb.cwns.model.FacilityFlow;
import gov.epa.owm.mtb.cwns.model.FacilityImpairedWater;
import gov.epa.owm.mtb.cwns.model.FacilityPointOfContact;
import gov.epa.owm.mtb.cwns.model.FacilityPollutionProblem;
import gov.epa.owm.mtb.cwns.model.FacilityPopulation;
import gov.epa.owm.mtb.cwns.model.FacilityPopulationUnit;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatusId;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeCategoryRef;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChange;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChangeCcRef;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChangeId;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChangeRef;
import gov.epa.owm.mtb.cwns.model.FacilityTypeDischargeRef;
import gov.epa.owm.mtb.cwns.model.FacilityTypeId;
import gov.epa.owm.mtb.cwns.model.FacilityTypeRef;
import gov.epa.owm.mtb.cwns.model.FacilityTypeRuleRef;
import gov.epa.owm.mtb.cwns.model.FacilityUnitProcess;
import gov.epa.owm.mtb.cwns.model.FederalReviewStatus;
import gov.epa.owm.mtb.cwns.model.FederalReviewStatusId;
import gov.epa.owm.mtb.cwns.model.OperationAndMaintenanceCost;
import gov.epa.owm.mtb.cwns.model.PopulationRef;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class FacilityTypeServiceImpl extends CWNSService implements FacilityTypeService {
	
	private SearchDAO searchDAO;
	private PopulationService populationService;
	private FESService fesService;
	
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	
	public void setFesService(FESService fes){
		fesService = fes;
	}

	public void setPopulationService(PopulationService popService){
		this.populationService = popService;
	}	
	
	public Collection getFacityType(Long facilityId) {
		
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftr", AliasCriteria.JOIN_LEFT);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);

		SortCriteria sortCriteria = new SortCriteria("ftr.sortSequence", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection results = searchDAO.getSearchList(FacilityType.class, scs, sortArray, aliasArray,0,0);
		return results;
	}
	
	public Collection getAvailableFacityType(Long facilityId) {
		//get the facility types
		Collection facilityTypes = getFacityType(facilityId);
		
		//if no facility types are assigned
		if(facilityTypes.size()<1){
			//get all the facility Type
			Collection allFacilityTypes = searchDAO.getObjects(FacilityTypeRef.class);
			return allFacilityTypes;
		}
		
		//if facility types are already assigned
		int icount=0;
		Collection col1=null;
		ArrayList facilityTypeIds = new ArrayList();
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType facilityType = (FacilityType) iter.next();
			long facilityTypeId = facilityType.getFacilityTypeRef().getFacilityTypeId();
			//save facility Type Ids
			facilityTypeIds.add(new Long(facilityTypeId));
			//get the business rules
			SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityTypeId1", SearchCondition.OPERATOR_EQ, new Long(facilityTypeId)));
			Collection facilityTypeRules = searchDAO.getSearchList(FacilityTypeRuleRef.class, scs);
			Collection col = getProcessFacilityTypes(facilityTypeRules);				
	    	if(icount==0){
	    		col1=col;
	    	}else{
		    	col1= CollectionUtils.intersection(col1, col);
	    	}
	    	icount++;
	    }
		//if no additional facility types are applicable.
		if(col1.size()< 1){
			return col1;
		}
		
		//remove all facilityTypeIds that have already been added
		col1.removeAll(facilityTypeIds);
		
		//get the facility Type objects and remove the ones already assigned and sort them
		SortCriteria sortCriteria = new SortCriteria("sortSequence", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityTypeId", SearchCondition.OPERATOR_IN, col1));
		Collection facilityType2 = searchDAO.getSearchList(FacilityTypeRef.class, scs, sortArray);
		return facilityType2;
	}
	
	private Collection getProcessFacilityTypes(Collection facilityTypeRules){
		ArrayList al = new ArrayList();
		for (Iterator iter = facilityTypeRules.iterator(); iter.hasNext();) {
			FacilityTypeRuleRef fr = (FacilityTypeRuleRef) iter.next();
			al.add(new Long(fr.getId().getFacilityTypeId2()));			
		}
		return al;
	}	
	
	public Collection getChangeTypes(){
		SortCriteria sortCriteria = new SortCriteria("sortSequence", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		Collection results = searchDAO.getSearchList(ChangeTypeRef.class, new SearchConditions(),sortArray);
		return results;		
	}

	public Collection getChangeTypesRules(){
		Collection results = searchDAO.getObjects(ChangeTypeRuleRef.class);
		return results;		
	}
	
	public Collection getFacilityTypeChangeRules(){
		Collection results = searchDAO.getObjects(FacilityTypeChangeRef.class);
		return results;		
	}
	
	public FacilityType getFacilityType(Long facilityId, Long facilityTypeId){	
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityTypeId", SearchCondition.OPERATOR_EQ, facilityTypeId));
		scs.setCondition(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		FacilityType ft = (FacilityType)searchDAO.getSearchObject(FacilityType.class, scs);
		return ft;
	}
	
	public void softDeleteFacilityType(Long facilityId, Long facilityTypeId, String userId) {
		FacilityType ft = getFacilityType(facilityId, facilityTypeId);		
        if(ft!=null){
        	char deleteFlag = ft.getFeedbackDeleteFlag()=='Y'?'N':'Y';
        	ft.setFeedbackDeleteFlag(deleteFlag);
        	searchDAO.saveObject(ft);
        }
		
	}

	public boolean deleteFacilityType(Long facilityId, Long facilityTypeId, Collection errors, String userId) {
		//	get the facility type object 
		FacilityType ft = getFacilityType(facilityId, facilityTypeId);		
        if(ft!=null){
        	// check upstream facility preventing deletion
    		if(checkUpstream(ft)){
    			int dischargeType =0;
    			if(ft.getPresentFlag()=='Y' && ft.getProjectedFlag()=='Y'){
    				dischargeType = PopulationService.PRESENT_AND_PROJECTED;
    			}else if(ft.getPresentFlag()=='Y'){
    				dischargeType = PopulationService.PRESENT_ONLY;
    			}else{
    				dischargeType = PopulationService.PROJECTED_ONLY;
    			}
    			if(isUpStreamfacilityPreventsDelete(facilityId, dischargeType)){
    				Entity error = new Entity("Error", "One or more upstream facilities are sumbitted for federal review.  Therefore the selected facility type cannot be deleted");
    				errors.add(error);
    				return false;
    			}			
    		}
    		
    		
    		
        	//delete data areas
    		Collection facilityTypes = removeFacilityType(facilityId, ft);
    		
    		//before deletion get upstream if applicable
        	ArrayList messages = new ArrayList();
    		deleteDischarge(facilityTypes, facilityId, userId, false, messages, errors);
    		Collection oldUpstream = new ArrayList();
    		Collection oldSewershed = new ArrayList();
    		if(isDishargeUpdated(messages)){
    			//get upstream facilitites before delete
    			oldUpstream = populationService.getUpstreamFacilityIds(facilityId.toString());
    			oldSewershed = populationService.getRelatedSewerShedFacilities(facilityId);
    		}
    		
    		messages = new ArrayList();
    		
    		//deleted dataarea information that is not applicable  
    		deleteData(facilityTypes, facilityId, userId, true, messages, errors);
    		
    	    //delete changes
    		deleteChanges(ft);
   
    		//delete facility Type object
    		searchDAO.removeObject(ft);
    		
    		//set rerun flag
    		setRerunFlag(facilityId, messages, oldUpstream, oldSewershed);
    		
    		//update fes records
    		updateFES(facilityId);
    		return true;    			    			
        }
		return false;
	}
	
	private boolean setRerunFlag(Long facilityId, ArrayList messages, Collection oldUpstream, Collection oldSewershed){		// cost curve rerun
    		ArrayList costCurveIdList1 = new ArrayList();
			costCurveIdList1.add(new Long(1));
			costCurveIdList1.add(new Long(2));
			costCurveIdList1.add(new Long(4));
			costCurveIdList1.add(new Long(5));
			costCurveIdList1.add(new Long(6));
			costCurveIdList1.add(new Long(10));
			ArrayList costCurveIdList2 = new ArrayList();
			costCurveIdList2.add(new Long(7));
			if(isPopulationUpdated(messages)||isFlowUpdated(messages)||isDishargeUpdated(messages)){
    			if(isPopulationUpdated(messages)){
    				ArrayList costCurveIdList = new ArrayList();
    				costCurveIdList.add(new Long(8));
    				costCurveIdList.add(new Long(9));
    				costCurveIdList.add(new Long(11));
    				costCurveIdList.add(new Long(12));
    				costCurveIdList.add(new Long(13));
    				costCurveIdList.add(new Long(14));
    				costCurveIdList.add(new Long(15));
    				costCurveIdList.add(new Long(16));
    				costCurveIdList.add(new Long(17));
    				costCurveIdList.add(new Long(18));
    			  populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);	
    			}
    			
    		    // get present upstream facilitites
    			Collection upstreamfacIds = populationService.getUpstreamFacilityIds(facilityId.toString());
    			if(upstreamfacIds!=null){
        			Iterator iter = upstreamfacIds.iterator();
                    while (iter.hasNext()) {
        				String facId = (String) iter.next(); 
        				populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(facId));
        			}
        			populationService.setUpCostCurvesForRerun(costCurveIdList1, facilityId);    				
    			}

    			// get present sewershed facilities
    			Collection facIds = populationService.getRelatedSewerShedFacilities(facilityId);
    			if(facIds!=null){
        			Iterator iter1 = facIds.iterator();
        			while (iter1.hasNext()) {
        				Long facId = (Long) iter1.next(); 
        				populationService.setUpCostCurvesForRerun(costCurveIdList2, facId);
        			}    				
    			}
    			// if discharge is updated set rerun flag for old upstream and old sewershed
    			if(isDishargeUpdated(messages)){
    				if(oldUpstream!=null){
        				Iterator iter2 = oldUpstream.iterator();
                        while (iter2.hasNext()) {
            				String facId = (String) iter2.next(); 
            				populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(facId));
            			}    					
    				}
    				
    				if(oldSewershed!=null){
                        Iterator iter3 = oldSewershed.iterator();
            			while (iter3.hasNext()) {
            				Long facId = (Long) iter3.next(); 
            				populationService.setUpCostCurvesForRerun(costCurveIdList2, facId);
            			}	
    				}
    			}
    		} else if(isEffluentUpdated(messages)){
                // get present upstream facilitites
    			Collection upstreamfacIds = populationService.getUpstreamFacilityIds(facilityId.toString());
    			if(upstreamfacIds!=null){
        			Iterator iter = upstreamfacIds.iterator();
                    while (iter.hasNext()) {
        				String facId = (String) iter.next(); 
        				populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(facId));
        			}
        			populationService.setUpCostCurvesForRerun(costCurveIdList1, facilityId);    				
    			}
    		}
			return true;
    		// end of cost curve of rerun)		
	}
	
	
	
	private boolean isPopulationUpdated(Collection messages){
		for (Iterator iter = messages.iterator(); iter.hasNext();) {
			String message = (String) iter.next();
			if(message.indexOf("Population")>-1){
				return true;
			}			
		}
		return false;
	}
	
	private boolean isDishargeUpdated(Collection messages){
		for (Iterator iter = messages.iterator(); iter.hasNext();) {
			String message = (String) iter.next();
			if( message.indexOf("Discharge")>-1){
				return true;
			}			
		}
		return false;
	}
	
	private boolean isFlowUpdated(Collection messages){
		for (Iterator iter = messages.iterator(); iter.hasNext();) {
			String message = (String) iter.next();
			if(message.indexOf("Flow")>-1){
				return true;
			}			
		}
		return false;
	}
	
	private boolean isEffluentUpdated(Collection messages){
		for (Iterator iter = messages.iterator(); iter.hasNext();) {
			String message = (String) iter.next();
			if(message.indexOf("Effluent")>-1){
				return true;
			}			
		}
		return false;
	}
	
	
	public boolean isDataDeleted(Long facilityId, Long facilityTypeId, Collection messages, String userId){
		FacilityType ft = getFacilityType(facilityId, facilityTypeId);
		Collection facilityTypes = removeFacilityType(facilityId, ft);
		ArrayList errors = new ArrayList();  //errors will not be throw because the confirmDelete is false
		deleteData(facilityTypes, facilityId, userId, false, messages, errors);
		if(messages.size()>0){
			return true;
		}
		return false;  
	}
	
	private Collection removeFacilityType(Long facilityId, FacilityType facilityType){
		ArrayList arr = new ArrayList();
		Collection fts = getFacityType(facilityId);
		for (Iterator iter = fts.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(ft.getId().getFacilityTypeId()!=facilityType.getId().getFacilityTypeId()){
				arr.add(ft);
			}
		}
		return arr;
	}
	
	private boolean deleteData(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isupdated = false;
		if(deletePollutionProblem(facilityTypes, facilityId, userId, confirmDelete, messages, errors)) isupdated=true;
		if(deleteImpairedWaters(facilityTypes, facilityId, userId, confirmDelete, messages, errors))isupdated=true;
		if(deleteCSO(facilityTypes, facilityId, userId, confirmDelete, messages, errors)) isupdated=true;
		if(deleteDischarge(facilityTypes, facilityId, userId, confirmDelete, messages, errors)) isupdated=true;
		if(deleteEffluent(facilityTypes, facilityId, userId, confirmDelete, messages, errors)) isupdated=true;
		if(deleteFlow(facilityTypes, facilityId, userId, confirmDelete, messages, errors)) isupdated=true;
		if(deletePopulation(facilityTypes, facilityId, userId, confirmDelete, messages, errors)) isupdated=true;
		if(deleteCost(facilityTypes, facilityId, userId, confirmDelete, messages, errors)) isupdated=true;
		if(deleteCostCurves(facilityTypes, facilityId, userId, confirmDelete, messages, errors)) isupdated=true;
		if(updateResponsibleEntryFlag(facilityTypes, facilityId, userId, confirmDelete, messages, errors))isupdated=true;
		if(deleteOAndMCosts(facilityTypes, facilityId, userId, confirmDelete, messages, errors))isupdated=true;
		if(deleteUnitProcess(facilityTypes, facilityId, userId, confirmDelete, messages, errors)) isupdated=true;
		return isupdated;
	}
	
	//delete unit process
	private boolean deleteUnitProcess(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		//TODO: check if it is a feedback copy
		boolean isUpdated = false;
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
        Collection fups = searchDAO.getSearchList(FacilityUnitProcess.class, scs);
        if(fups!=null && fups.size()>0){
        	boolean isDelete = false;
        	for (Iterator iter = fups.iterator(); iter.hasNext();) {
				FacilityUnitProcess fup = (FacilityUnitProcess) iter.next();
				
	        	if(!isValidOverFacilityType(facilityTypes, fup)){
	        		isDelete=true;
	    			if(confirmDelete){
	    				Collection fupc = fup.getFacilityUnitProcessChanges();
	    				if(fupc!=null && fupc.size()>0){
	    					searchDAO.deleteAll(fupc);	
	    				}
	    				searchDAO.removeObject(fup);
	    				isUpdated=true;
	    			}		
	        	}				
			}
        	if(isDelete){
        		messages.add("Unit Process");	
        	}
        }
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_UNIT_PROCESS, userId);
		}
		return isUpdated;
	}
	
	
	private boolean isValidOverFacilityType(Collection facilityTypes, FacilityUnitProcess fu) {
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(ft.getFacilityTypeRef().getFacilityOverallTypeRef().getFacilityOverallTypeId()==fu.getTreatmentTypeUnitprocessRef().getTreatmentTypeRef().getFacilityOverallTypeRef().getFacilityOverallTypeId()){
				return true;
			}			
		}
		return false;
	}

	//delete o&m Costs
	private boolean deleteOAndMCosts(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
        Collection omcs = searchDAO.getSearchList(OperationAndMaintenanceCost.class, scs);
        if(omcs!=null && omcs.size()>0 && facilityTypes.isEmpty()){
        	messages.add("Operation and Maintenanace Costs");
			if(confirmDelete){
				searchDAO.deleteAll(omcs);
				isUpdated=true;
			}
        }
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_NEEDS, userId);
		}
		return isUpdated;
	}
	
	
	//update responsible party flag
	private boolean updateResponsibleEntryFlag(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
		//get a list of facility point of contacts where superfundRespPartyFlag is Y
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("superfundRespPartyFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
        Collection fpocs = searchDAO.getSearchList(FacilityPointOfContact.class, scs);
        if(fpocs!=null && fpocs.size()>0){
        	if(!isValidForResponsiblePartyFlag(facilityTypes)){
        		messages.add("Update Superfund Responsible Party to No");
				if(confirmDelete){
					for (Iterator iter = fpocs.iterator(); iter.hasNext();) {
						FacilityPointOfContact fpoc = (FacilityPointOfContact) iter.next();
						fpoc.setSuperfundRespPartyFlag('N');
						searchDAO.saveObject(fpoc);
						isUpdated=true;
					}
				}
    			
    		}        	
        }
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_POC, userId);
		}
		return isUpdated;
	}	
	
	private boolean isValidForResponsiblePartyFlag(Collection facilityTypes){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ftr.getValidForRespnsblePartyFlag()=='Y'){
					return true;
				}
			}
		}
		return false;
	}
	
	
	//delete cost curves
	private boolean deleteCostCurves(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
		
		Collection fcc = getFacilityCostCurves(facilityId);
		if(fcc!=null && fcc.size()>0){
			for (Iterator iter = fcc.iterator(); iter.hasNext();) {
				FacilityCostCurve facilityCostCurve = (FacilityCostCurve) iter.next();
				if(!isValidCostCurve(facilityTypes, facilityCostCurve)){
					messages.add("CostCurve and Population for "+ facilityCostCurve.getCostCurveRef().getName());
					if(confirmDelete){
	    				//remove population
	    				FacilityPopulation fp= getFacilityPopulationByCostCurve(facilityId, new Long(facilityCostCurve.getCostCurveRef().getCostCurveId()));
	    				if(fp!=null){
	    					Collection popunits = fp.getPopulationRef().getFacilityPopulationUnits();
	    					if(popunits!=null && popunits.size()>0){
	    						searchDAO.deleteAll(popunits);
	    					}
	    					searchDAO.removeObject(fp);
	    				}
	    				Collection costs = facilityCostCurve.getCosts();
	    				if(costs!=null){
	    					searchDAO.deleteAll(costs);	
	    				}
	    				Collection cdas= facilityCostCurve.getFacilityCostCurveDataAreas();
	    				if(cdas!=null && !cdas.isEmpty()){
	    					for (Iterator iterator = cdas.iterator(); iterator.hasNext();) {
								FacilityCostCurveDataArea fccda = (FacilityCostCurveDataArea) iterator.next();
								deleteCostCurveMessages(fccda);
								searchDAO.removeObject(fccda);
							}
	    					//searchDAO.deleteAll(cdas);	
	    				}
	    				
	    				//delete facility cost curve
	    				searchDAO.removeObject(facilityCostCurve);
	    				isUpdated=true;
	    			}
				}	
			}	
		}
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_NEEDS, userId);
		}
		return isUpdated;
	}	
	
	//delete ccv messages
	private void deleteCostCurveMessages(FacilityCostCurveDataArea fccda){
		long facilityId = fccda.getFacilityCostCurve().getFacilityDocument().getId().getFacilityId();
		long dataAreaId= fccda.getId().getDataAreaId();
		Collection ccvErrors = getFacilityDataAreaMessages(new Long(facilityId), new Long(dataAreaId));
		if(ccvErrors!=null && !ccvErrors.isEmpty()){
			deleteFacilityDataAreaMessages(ccvErrors);	
		}
	}
	
	public Collection getFacilityDataAreaMessages(Long facilityId, Long dataAreaId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		scs.setCondition(new SearchCondition ("sourceCode", SearchCondition.OPERATOR_EQ, "ccv"));		
		return searchDAO.getSearchList(FacilityDataAreaMessage.class, scs);
	}
	
	public void deleteFacilityDataAreaMessages(Collection ccvErrors) {
		for (Iterator iter = ccvErrors.iterator(); iter.hasNext();) {
			FacilityDataAreaMessage ccvErr = (FacilityDataAreaMessage) iter.next();
			searchDAO.removeObject(ccvErr);
			//searchDAO.flushAndClearCache();
		}
	}
	
	private FacilityPopulation getFacilityPopulationByCostCurve(Long facilityId, Long costCurveId) {
		ArrayList aliasArray = new ArrayList();		
		aliasArray.add(new AliasCriteria("populationRef", "p", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("p.costCurveRef", "ccr", AliasCriteria.JOIN_INNER));		
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ccr.costCurveId", SearchCondition.OPERATOR_EQ, costCurveId));
        return (FacilityPopulation)searchDAO.getSearchObject(FacilityPopulation.class, scs, aliasArray);
	}

	private boolean isValidCostCurve(Collection facilityTypes, FacilityCostCurve facilityCostCurve) {
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType facilityType = (FacilityType) iter.next();
			Long facilityTypeId= new Long(facilityType.getId().getFacilityTypeId());
			Collection changes= facilityType.getFacilityTypeChanges();
			if(changes!=null && changes.size()>0){
				for (Iterator iterator = changes.iterator(); iterator.hasNext();) {
					FacilityTypeChange ftc = (FacilityTypeChange) iterator.next();
					Long changeTypeId=new Long(ftc.getChangeTypeRef().getChangeTypeId());
					//get FacilityTypeChangeCcRef
					SearchConditions scs = new SearchConditions(new SearchCondition("id.changeTypeId", SearchCondition.OPERATOR_EQ, changeTypeId));
					scs.setCondition(new SearchCondition("id.facilityTypeId", SearchCondition.OPERATOR_EQ, facilityTypeId));
					Collection ftcccrs = searchDAO.getSearchList(FacilityTypeChangeCcRef.class, scs);				
					for (Iterator iterator2 = ftcccrs.iterator(); iterator2	.hasNext();) {
						FacilityTypeChangeCcRef fcr = (FacilityTypeChangeCcRef) iterator2.next();
						if(fcr.getId().getCostCurveId()==facilityCostCurve.getCostCurveRef().getCostCurveId()){
							return true;
						}						
					}
				}
			}			
		}
		return false;
	}

	private Collection getFacilityCostCurves(Long facilityId){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));	    						
		SearchConditions scs = new SearchConditions(new SearchCondition("fd.id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection fcc = searchDAO.getSearchList(FacilityCostCurve.class, scs, new ArrayList(), aliasArray,0,0);		
		return fcc;
	}
	
	
	//delete Costs
	private boolean deleteCost(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
		Collection costs = getCosts(facilityId);
		if(costs!=null && costs.size()>0){
			for (Iterator iter = costs.iterator(); iter.hasNext();) {
				Cost cost = (Cost) iter.next();
				String categoryId = cost.getCategoryRef().getCategoryId();
				if(!isValidCostCategory(facilityTypes, categoryId)){
					messages.add("Category "+ categoryId+ " Costs");
					if(confirmDelete){
	    				searchDAO.removeObject(cost);
	    				isUpdated=true;
	    			}
					
				}								
			}			
		}
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_NEEDS, userId);
		}
		return isUpdated;
	}	
	
	private boolean isValidCostCategory(Collection facilityTypes, String categoryId){
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			Collection ftcs = ft.getFacilityTypeRef().getFacilityTypeCategoryRefs();
			for (Iterator iterator = ftcs.iterator(); iterator.hasNext();) {
				FacilityTypeCategoryRef ftcr = (FacilityTypeCategoryRef) iterator.next();
				if(ftcr.getCategoryRef().getCategoryId().equals(categoryId)){
					return true;
				}
			} 
		} 
		return false;
	}
	
	private Collection getCosts(Long facilityId) {
		ArrayList costs = new ArrayList();
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection fds = searchDAO.getSearchList(FacilityDocument.class, scs);
		if(fds !=null && fds.size()>0){
			for (Iterator iter = fds.iterator(); iter.hasNext();) {
				FacilityDocument fd = (FacilityDocument) iter.next();
				Collection c = fd.getCosts();
				if(c!=null && c.size()>0){
					costs.addAll(c);	
				}	
			}	
		}
		return costs;
	}
	
	
	
	

	private boolean deletePollutionProblem(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection fpp = searchDAO.getSearchList(FacilityPollutionProblem.class, scs);
		if(fpp!=null && fpp.size()>0){
			if(!isValidForPollution(facilityTypes)){
				messages.add("Pollution Problem");
    			if(confirmDelete){
    				searchDAO.deleteAll(fpp);
    				isUpdated=true;
    			}
			}			
		}	
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_POLLUTION, userId);
		}
		return isUpdated;
	}
	
	private boolean deleteImpairedWaters(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection fiws = searchDAO.getSearchList(FacilityImpairedWater.class, scs);
		if(fiws!=null && fiws.size()>0){
			if(!isValidForPollution(facilityTypes)){
				messages.add("Impaired Water");
    			if(confirmDelete){
    				for (Iterator iter = fiws.iterator(); iter.hasNext();) {
						FacilityImpairedWater fiw = (FacilityImpairedWater) iter.next();
	    				//remove any needs & costs associated with this water body
	    				FacilityDocument fd =  getImpairedWatersDocument(facilityId, fiw.getImpairedWater().getWaterBodyName(), fiw.getId().getListId());
	    				if(fd!=null){
	    					//remove associated costs
		    				Collection costs = fd.getCosts();
		    				if(costs!=null){
		    					searchDAO.deleteAll(costs);
		    				}
		    				//remove document if it has only one reference
		    				if(fd.getDocument().getFacilityDocuments().size()==1){
		    					searchDAO.removeObject(fd);
		    				}	    					
	    				}
	    				//remove impaired water
	    				searchDAO.removeObject(fiw);
	    				isUpdated=true;						
					}
    			}
			}			
		}	
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_POLLUTION, userId);
		}
		return isUpdated;
	}
	
	private FacilityDocument getImpairedWatersDocument(Long facilityId, String waterBodyName, String listId){
		String titleName = waterBodyName + " (" + listId + ")";
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("document", "d", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("d.documentTypeRef", "dtr", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("d.titleName", SearchCondition.OPERATOR_EQ, titleName));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("dtr.documentTypeId", SearchCondition.OPERATOR_EQ, "11"));
		FacilityDocument facilityDocument = (FacilityDocument)searchDAO.getSearchObject(FacilityDocument.class, scs, aliasArray);
		return facilityDocument;
	}
	
	private boolean isValidForPollution(Collection facilityTypes){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ftr.getValidForPollutionFlag()=='Y'){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean deleteCSO(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		CombinedSewer cs = (CombinedSewer)searchDAO.getSearchObject(CombinedSewer.class, scs);
		if(cs!=null){
			if(!isValidForCSO(facilityTypes)){
				messages.add("CSO");
    			if(confirmDelete){
    				//delete facility document
    				ArrayList aliasArray = new ArrayList();
    				aliasArray.add(new AliasCriteria("document", "d", AliasCriteria.JOIN_INNER));
    				aliasArray.add(new AliasCriteria("d.documentTypeRef", "dtr", AliasCriteria.JOIN_INNER));	    				
    				SearchConditions scsfd =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
    				scsfd.setCondition(new SearchCondition("dtr.documentTypeId", SearchCondition.OPERATOR_EQ, "98"));
    				FacilityDocument fd = (FacilityDocument)searchDAO.getSearchObject(FacilityDocument.class, scsfd,aliasArray);
    				if(fd!=null){
    					if(fd.getCosts()!=null){
    						searchDAO.deleteAll(fd.getCosts());	
    					}
    					Collection fccs = fd.getFacilityCostCurves();
    					if(fccs!=null){
    						for (Iterator iter = fccs.iterator(); iter.hasNext();) {
								FacilityCostCurve facilityCostCurve = (FacilityCostCurve) iter.next();
								Collection costs = facilityCostCurve.getCosts();
			    				if(costs!=null){
			    					searchDAO.deleteAll(costs);	
			    				}
			    				Collection cdas= facilityCostCurve.getFacilityCostCurveDataAreas();
			    				if(cdas!=null){
			    					for (Iterator iterator = cdas.iterator(); iterator.hasNext();) {
										FacilityCostCurveDataArea fccda = (FacilityCostCurveDataArea) iterator.next();
										deleteCostCurveMessages(fccda);
										searchDAO.removeObject(fccda);
									}
			    					//searchDAO.deleteAll(cdas);	
			    				}	
			    				//delete facility cost curve
			    				searchDAO.removeObject(facilityCostCurve);								
							}
    					}
    					searchDAO.removeObject(fd);
    					
    					//remove document
    					Document d = fd.getDocument();
    					Collection fdForDocument = d.getFacilityDocuments();
    					if(fdForDocument!=null && fdForDocument.size()==1){  //make sure that this document is not asscoaisted with more than one document        					
    						searchDAO.removeObject(d);
    					}
    				}
    				//remove cso data
    				searchDAO.removeObject(cs);
    				isUpdated=true;
    			}
			}			
		}	
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_CSO, userId);
		}
		return isUpdated;			
	}
	
	
	
	private boolean isValidForCSO(Collection facilityTypes){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ftr.getValidForCsoFlag()=='Y'){
					return true;
				}					
			}
		}
		return false;
	}
	
	
//	***************************************************
	//  Effluent delete functions
    //	***************************************************
	private boolean deleteEffluent(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
		//delete the effluent
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection facilityEffluent = (Collection)searchDAO.getSearchList(FacilityEffluent.class, scs);
		if(facilityEffluent!=null && facilityEffluent.size()>0){
			if(isValidForEffluent(facilityTypes)){
				if(!isValidForEffluent(facilityTypes, 'P')){
    				//delete present flow
    				messages.add("Present Effluent");
    				if(confirmDelete){
    					removeEffluent(facilityEffluent, 'P');
    				}
				}
				if(!isValidForEffluent(facilityTypes, 'F')){
    				//delete present flow
    				messages.add("Projected Effluent");
    				if(confirmDelete){
    					removeEffluent(facilityEffluent, 'F');
    				}
				}				
			}else{
    			messages.add("Effluent");
    			if(confirmDelete){
    				//2. loop and delete all the facility population
    				for (Iterator iter = facilityEffluent.iterator(); iter.hasNext();) {
						FacilityEffluent fe = (FacilityEffluent) iter.next();
						removeEffluent(fe);						
					}
    			}
			}
		}
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_EFFLUENT, userId);
		}
		return isUpdated;					
	}
	
	private void removeEffluent(FacilityEffluent fe){
		Set indicators = fe.getFacilityAdvancedTreatments();
		if(indicators !=null && indicators.size()>0){
				searchDAO.deleteAll(indicators);								
		}
		searchDAO.removeObject(fe);
	}
	
	private void removeEffluent(Collection facilityEffluent, char ppFlag){
		for (Iterator iter = facilityEffluent.iterator(); iter.hasNext();) {
			FacilityEffluent fe = (FacilityEffluent) iter.next();
			if(fe.getId().getPresentOrProjectedCode()==ppFlag){
				removeEffluent(fe);
			}
		}
	}
	
	private FacilityEffluent getFacilityEffluentLevel(Long facilityId, char projectCode){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition("id.presentOrProjectedCode", SearchCondition.OPERATOR_EQ, new Character(projectCode)));
		FacilityEffluent fe = (FacilityEffluent)searchDAO.getSearchObject(FacilityEffluent.class, scs);
		return fe;		
	}
	
	
	private boolean isValidForEffluent(Collection facilityTypes){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ftr.getValidForEffluentFlag()=='Y'){
					return true;
				}					
			}
		}
		return false;
	}
	
	private boolean isValidForEffluent(Collection facilityTypes, char ppFlag){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ppFlag=='P'){
					if(ft.getPresentFlag()=='Y' && ftr.getValidForEffluentFlag()=='Y'){
						return true;
					}					
				}else{
					if(ft.getProjectedFlag()=='Y' && ftr.getValidForEffluentFlag()=='Y'){
						return true;
					}
				}
			}
		}
	    return false;	
	}
	
	
	
//	***************************************************
	//  Flow delete functions
    //	***************************************************
	private boolean deleteDischarge(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
		Facility facility = getFacility(facilityId);
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityByFacilityId", SearchCondition.OPERATOR_EQ, facility));
        Collection facilityDischarge = searchDAO.getSearchList(FacilityDischarge.class, scs);
        if(facilityDischarge!=null && facilityDischarge.size()>0){        	        	
        	if(isValidForDischarge(facilityTypes)){
        		//	get facilityTypeDischargeRef
            	Collection facilityTypeDischargeRef = searchDAO.getObjects(FacilityTypeDischargeRef.class);
            	boolean updatePresentDischarge =false;
            	boolean updateProjectedDischarge =false;
            	
            	//delete present discharge 
            	for (Iterator iter = facilityDischarge.iterator(); iter.hasNext();) {
            		FacilityDischarge fd = (FacilityDischarge) iter.next();
            		if(!isFacilityTypeInAllowedDischarge(facilityTypes, facilityTypeDischargeRef,fd, fd.getDischargeMethodRef().getDischargeMethodId(), 'P')){
            			updatePresentDischarge =true;
            			if(confirmDelete){
            				//set the discharge methods 
            				fd.setPresentFlag('N');
            				fd.setPresentFlowPortionPersent(new Short("0"));
            				searchDAO.saveObject(fd);
            				isUpdated=true;
            			}
            		}					
				}
            	
            	if(updatePresentDischarge){
            		messages.add("Present Discharge");
            	}
            	
            	//delete projected discharge
            	for (Iterator iter = facilityDischarge.iterator(); iter.hasNext();) {
            		FacilityDischarge fd = (FacilityDischarge) iter.next();
            		if(!isFacilityTypeInAllowedDischarge(facilityTypes, facilityTypeDischargeRef, fd,fd.getDischargeMethodRef().getDischargeMethodId(), 'F')){
            			updateProjectedDischarge =true;
            			if(confirmDelete){
            				//set the discharge methods 
            				fd.setProjectedFlag('N');
            				fd.setProjectedFlowPortionPersent(new Short("0"));
            				searchDAO.saveObject(fd);
            				isUpdated=true;
            			}
            		}					
				}
            	if(updateProjectedDischarge){
            		messages.add("Projected Discharge");
            	}
            	
            	//delete the record if both present and projected status are set to N
            	if(confirmDelete){
            	  for (Iterator iter = facilityDischarge.iterator(); iter.hasNext();) {
            		FacilityDischarge fd = (FacilityDischarge) iter.next();
            		//set the discharge methods 
            		if(fd.getProjectedFlag()=='N' && fd.getPresentFlag()=='N'){
            			searchDAO.removeObject(fd);
            			isUpdated=true;
            		} 
            	  }					
            	}
            	
        	}else{
        		//delete all the discharge
    			messages.add("Discharge");
    			if(confirmDelete){
    				//2. loop and delete all the facility population
    		        for (Iterator iter = facilityDischarge.iterator(); iter.hasNext();) {
    					FacilityDischarge fd = (FacilityDischarge) iter.next();
    					searchDAO.removeObject(fd);
    					isUpdated=true;
    				}				
    			}
        	}
        }
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_DISCHARGE, userId);
		}
		return isUpdated;
	}
	
	private boolean isFacilityTypeInAllowedDischarge(Collection facilityTypes, Collection facilityTypeDischargeRef, FacilityDischarge fd, long facilityDischargeMethod, char ppFlag){
		for (Iterator iter = facilityTypes.iterator(); iter
				.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			boolean allowed = false;
			boolean isDischarge = true;
			if(ppFlag =='P'){
				allowed = (ft.getPresentFlag()=='Y')?true: false;
				isDischarge=(fd.getPresentFlag()=='Y')?true:false;
			}else{
				allowed = (ft.getProjectedFlag()=='Y')?true: false;
				isDischarge=(fd.getPresentFlag()=='Y')?true:false;
			}
			if(allowed){
				for (Iterator iterator = facilityTypeDischargeRef.iterator(); iterator.hasNext();) {
					FacilityTypeDischargeRef ftdr = (FacilityTypeDischargeRef) iterator.next();
					if(!isDischarge || ( (ftdr.getId().getFacilityTypeId()==ft.getId().getFacilityTypeId()) && ftdr.getId().getDischargeMethodId()==facilityDischargeMethod)){
						return true;
					}					
				}
			}					
		}
		return false;
	}
	
	private boolean isValidForDischarge(Collection facilityTypes){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ftr.getValidForDischargeFlag()=='Y'){
					return true;
				}					
			}
		}
		return false;
	}
	
	
	
	//	***************************************************
	//  Flow delete functions
    //	***************************************************
	private boolean deleteFlow(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
        SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
        Collection facilityFlow = searchDAO.getSearchList(FacilityFlow.class, scs);
        if( facilityFlow!=null &&  facilityFlow.size()>0){
    		if(isValidForFlow(facilityTypes)){
    			if(!isValidForFlow(facilityTypes, 'P')){
    				//delete present flow
    				messages.add("Present Flow");
    				if(confirmDelete){
    					for (Iterator iter = facilityFlow.iterator(); iter.hasNext();) {
        					FacilityFlow ff = (FacilityFlow) iter.next();
        					ff.setExistingFlowMsr(new BigDecimal(0.0));
        					ff.setPresentFlowMsr(new BigDecimal(0.0));
        					searchDAO.saveObject(ff);
        					isUpdated=true;
        				}	
    				}
    			}
    			
    			if(!isValidForFlow(facilityTypes, 'F')){
    				//delete present flow
    				messages.add("Projected Flow");
    				if(confirmDelete){
    					for (Iterator iter = facilityFlow.iterator(); iter.hasNext();) {
        					FacilityFlow ff = (FacilityFlow) iter.next();
        					ff.setProjectedFlowMsr(new BigDecimal(0.0));
        					searchDAO.saveObject(ff);
        					isUpdated=true;
        				}	
    				}
    			}
    			
    			//if flow values are zeros remove the record
				for (Iterator iter = facilityFlow.iterator(); iter.hasNext();) {
					FacilityFlow ff = (FacilityFlow) iter.next();
					if((ff.getExistingFlowMsr()!=null && ff.getExistingFlowMsr().floatValue()==0.0) && (ff.getExistingFlowMsr()!=null&& ff.getExistingFlowMsr().floatValue()==0.0) && (ff.getProjectedFlowMsr()!=null && ff.getProjectedFlowMsr().floatValue()==0.0)){
						searchDAO.removeObject(ff);
						log.debug("Delete facility" + ff.getFlowRef().getName() +" flow information");
					}
					isUpdated=true;
				}
    		}else{
    			//delete all the flow
    			messages.add("Flow");
    			if(confirmDelete){
    				//2. loop and delete all the facility population
    		        for (Iterator iter = facilityFlow.iterator(); iter.hasNext();) {
    					FacilityFlow ff = (FacilityFlow) iter.next();
    					searchDAO.removeObject(ff);
    					isUpdated=true;
    				}				
    			}	        				
    		}        	
        }		
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_FLOW, userId);
		}
		return isUpdated;
	}
	
	private boolean isValidForFlow(Collection facilityTypes){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ftr.getValidForFlowFlag()=='Y'){
					return true;
				}					
			}
		}
		return false;
	}
	
	private boolean isValidForFlow(Collection facilityTypes, char ppFlag){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ppFlag=='P'){
					if(ft.getPresentFlag()=='Y' && ftr.getValidForFlowFlag()=='Y'){
						return true;
					}					
				}else{
					if(ft.getProjectedFlag()=='Y' && ftr.getValidForFlowFlag()=='Y'){
						return true;
					}
				}
			}
		}
	    return false;	
	}
	
	
	
	//***************************************************
	//  Population delete functions
    //	***************************************************
	private boolean deletePopulation(Collection facilityTypes, Long facilityId, String userId, boolean confirmDelete, Collection messages, Collection errors){
		boolean isUpdated = false;
        SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
        Collection facilityPopulation = searchDAO.getSearchList(FacilityPopulation.class, scs);
        if(facilityPopulation!=null && facilityPopulation.size()>0){
    		if(isValidForPopulation(facilityTypes)){
    			FacilityPopulation fpRecResidential = getFacilityPopulation(facilityId, new Long(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION));
    			FacilityPopulation fpRecNonResidential = getFacilityPopulation(facilityId, new Long(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION));
    			if(fpRecResidential!=null || fpRecNonResidential!=null){
    				//check if valid for receiving collection
    				boolean presentPopRec = isValidForPopRecCollection(facilityTypes, 'P');
    				boolean projectedPopRec = isValidForPopRecCollection(facilityTypes, 'F');
    				if(!presentPopRec && !projectedPopRec){
    					//delete the rec pop record
    					messages.add("Receiving Population");
    					if(confirmDelete){
    						searchDAO.removeObject(fpRecResidential);
    						searchDAO.removeObject(fpRecNonResidential);
    						isUpdated=true;
    					}
    				}else{
    					//present
    					if(!presentPopRec){
        					//set present population rec to 0
    						if((fpRecResidential!=null && fpRecResidential.getPresentPopulationCount()!=null && fpRecResidential.getPresentPopulationCount().intValue()>0) ||
        							(fpRecNonResidential!=null && fpRecNonResidential.getPresentPopulationCount()!=null && fpRecNonResidential.getPresentPopulationCount().intValue()>0)){
            					messages.add("Present Receiving Population");
            					if(confirmDelete){
            						if(fpRecResidential!=null){
            							fpRecResidential.setPresentPopulationCount(new Integer(0));
            							searchDAO.saveObject(fpRecResidential);
            						}
            						if(fpRecNonResidential!=null){
            							fpRecNonResidential.setPresentPopulationCount(new Integer(0));
            							searchDAO.saveObject(fpRecNonResidential);							
            						}
            						isUpdated=true;
            					}    						
        					}
    					}
    					
    					//projected
    					if(!projectedPopRec){
        					//set present population rec to 0
        					if((fpRecResidential!=null && fpRecResidential.getProjectedPopulationCount()!=null && fpRecResidential.getProjectedPopulationCount().intValue()>0) ||
        							(fpRecNonResidential!=null && fpRecNonResidential.getProjectedPopulationCount()!=null && fpRecNonResidential.getProjectedPopulationCount().intValue()>0)){
            					messages.add("Projected Receiving Population");
            					if(confirmDelete){
            						if(fpRecResidential!=null){
            							fpRecResidential.setProjectedPopulationCount(new Integer(0));
            							fpRecResidential.setProjectionYear(null);
            							searchDAO.saveObject(fpRecResidential);							
            						}
            						if(fpRecNonResidential!=null){
            							fpRecNonResidential.setProjectedPopulationCount(new Integer(0));
            							fpRecNonResidential.setProjectionYear(null);
            							searchDAO.saveObject(fpRecNonResidential);							
            						}
            						isUpdated=true;
            					}
        					}
        				}
    					
    					if(confirmDelete){
        					if((fpRecResidential!=null && fpRecResidential.getPresentPopulationCount()!=null && fpRecResidential.getPresentPopulationCount().intValue()==0) &&
        							(fpRecNonResidential!=null && fpRecNonResidential.getPresentPopulationCount()!=null && fpRecNonResidential.getPresentPopulationCount().intValue()==0) &&
        							(fpRecResidential!=null && fpRecResidential.getProjectedPopulationCount()!=null && fpRecResidential.getProjectedPopulationCount().intValue()==0) &&
        							(fpRecNonResidential!=null && fpRecNonResidential.getProjectedPopulationCount()!=null && fpRecNonResidential.getProjectedPopulationCount().intValue()==0)){
        						searchDAO.removeObject(fpRecResidential);
        						searchDAO.removeObject(fpRecNonResidential);
        						
        					}	
    					}
    				}
    			}
    			
    			//	check if valid for clustered			
    			FacilityPopulation fpClusteredResidential = getFacilityPopulation(facilityId, new Long(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
    			FacilityPopulationUnit fpClusteredResidentialUnit = getFacilityPopulationUnits(facilityId, new Long(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
    			FacilityPopulation fpClusteredNonResidential = getFacilityPopulation(facilityId, new Long(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER));
    			FacilityPopulationUnit fpClusteredNonResidentialUnit = getFacilityPopulationUnits(facilityId, new Long(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER));
    			
    			if(fpClusteredResidential!=null || fpClusteredNonResidential!=null){
    				boolean presentPopClustered = isValidForPopClusteredCollection(facilityTypes, 'P');
    				boolean projectedPopClusted = isValidForPopClusteredCollection(facilityTypes, 'F');
    				if(!presentPopClustered && !projectedPopClusted){
    					//delete the clustered pop record
    					messages.add("Clusted Population");
    					if(confirmDelete){
    						searchDAO.removeObject(fpClusteredResidential);
    						if(fpClusteredResidentialUnit!=null){
    							searchDAO.removeObject(fpClusteredResidentialUnit);
    						}
    						searchDAO.removeObject(fpClusteredNonResidential);
    						if(fpClusteredNonResidentialUnit!=null){
    							searchDAO.removeObject(fpClusteredNonResidentialUnit);
    						}
    						isUpdated=true;
    					}
    				}else{
    					if(!presentPopClustered){
        					//set present clustered population to 0
        					messages.add("Present Clustered Population");
        					if(confirmDelete){
        						if(fpClusteredResidential!=null){
        							fpClusteredResidential.setPresentPopulationCount(new Integer(0));
        							searchDAO.saveObject(fpClusteredResidential);	
        							//set pop unit count to zero
        							if(fpClusteredResidentialUnit!=null){
            							fpClusteredResidentialUnit.setPresentUnitsCount(new Integer(0));
            							searchDAO.saveObject(fpClusteredResidentialUnit);    								
        							}
        						}
        						if(fpClusteredNonResidential!=null){
        							fpClusteredNonResidential.setPresentPopulationCount(new Integer(0));
        							searchDAO.saveObject(fpClusteredNonResidential);
        							if(fpClusteredNonResidentialUnit!=null){
            							fpClusteredNonResidentialUnit.setPresentUnitsCount(new Integer(0));
            							searchDAO.saveObject(fpClusteredNonResidentialUnit);
        							}
        						}	
        						isUpdated=true;
        				    }
    					}	
        					
    					if(!projectedPopClusted){
        					//set present clustered population to 0
        					messages.add("Projected Clustered Population");
        					if(confirmDelete){
        						if(fpClusteredResidential!=null){
        							fpClusteredResidential.setProjectedPopulationCount(new Integer(0));
        							fpClusteredResidential.setProjectionYear(null);
        							searchDAO.saveObject(fpClusteredResidential);
        							if(fpClusteredResidentialUnit!=null){
            							fpClusteredResidentialUnit.setProjectedUnitsCount(new Integer(0));
            							searchDAO.saveObject(fpClusteredResidentialUnit);    								
        							}
        							
        						}
        						if(fpClusteredNonResidential!=null){
        							fpClusteredNonResidential.setProjectedPopulationCount(new Integer(0));
        							fpClusteredNonResidential.setProjectionYear(null);
        							searchDAO.saveObject(fpClusteredNonResidential);
        							if(fpClusteredNonResidentialUnit!=null){
            							fpClusteredNonResidentialUnit.setProjectedUnitsCount(new Integer(0));
            							searchDAO.saveObject(fpClusteredNonResidentialUnit);
        							}
        						}
        						isUpdated=true;
        					}
        				}
    					
    					//check if both values are set to zero resident and non resident and delete in case of confirm delete
    					if(confirmDelete){
        					if(fpClusteredResidential!=null){
         					   if((fpClusteredResidential.getPresentPopulationCount()==null || fpClusteredResidential.getPresentPopulationCount().intValue()==0) 
         							   && (fpClusteredResidential.getProjectedPopulationCount()==null || fpClusteredResidential.getProjectedPopulationCount().intValue()==0)){
         						   //delete the record
         						   searchDAO.removeObject(fpClusteredResidential);
            						   if(fpClusteredResidentialUnit!=null){
            							 searchDAO.removeObject(fpClusteredResidentialUnit);
            						   }
         					   }
         					}
         					
         					// check if both values are set to zero non resident
         					if(fpClusteredNonResidential!=null){
         					   if((fpClusteredNonResidential.getPresentPopulationCount()==null || fpClusteredNonResidential.getPresentPopulationCount().intValue()==0) 
         							   && (fpClusteredNonResidential.getProjectedPopulationCount()==null || fpClusteredNonResidential.getProjectedPopulationCount().intValue()==0)){
         						   //delete the record
         						   searchDAO.removeObject(fpClusteredNonResidential);
            					   if(fpClusteredNonResidentialUnit!=null){
            						 searchDAO.removeObject(fpClusteredNonResidentialUnit);
            					   }
         					   }
         					}    						
    					}    					
    				} 
    								
    			}
    			
    			//	check if valid for OWTS
    			FacilityPopulation fpOWTSResidential = getFacilityPopulation(facilityId, new Long(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
    			FacilityPopulation fpOWTSNonResidential = getFacilityPopulation(facilityId, new Long(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
    			
    			FacilityPopulationUnit fpOWTSResidentialUnit = getFacilityPopulationUnits(facilityId, new Long(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
    			FacilityPopulationUnit fpOWTSNonResidentialUnit =getFacilityPopulationUnits(facilityId, new Long(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
    			
    			if(fpOWTSResidential!=null || fpOWTSNonResidential!=null){
    				boolean presentPopOWTS = isValidForPopOWTSCollection(facilityTypes, 'P');
    				boolean projectedPopOWTS = isValidForPopOWTSCollection(facilityTypes, 'F');
    				if(!presentPopOWTS && !projectedPopOWTS){
    					//delete the OWTS pop record
    					messages.add("Onsite Wastewater Treatment Systems Population");
    					if(confirmDelete){
    						searchDAO.removeObject(fpOWTSResidential);
    						searchDAO.removeObject(fpOWTSNonResidential);
    						isUpdated=true;
    						if(fpOWTSResidentialUnit!=null){
        						 searchDAO.removeObject(fpOWTSResidentialUnit);
        					}
    						if(fpOWTSNonResidentialUnit!=null){
         						 searchDAO.removeObject(fpOWTSNonResidentialUnit);
         					}
    						
    					}
    				}else{
    					if(!presentPopOWTS){
        					//set present OWTS population to 0
        					messages.add("Present Onsite Wastewater Treatment System Population");
        					if(confirmDelete){
        						if(fpOWTSResidential!=null){
        							fpOWTSResidential.setPresentPopulationCount(new Integer(0));
        							searchDAO.saveObject(fpOWTSResidential);
        							//set pop unit count to zero
        							if(fpOWTSResidentialUnit!=null){
            							fpOWTSResidentialUnit.setPresentUnitsCount(new Integer(0));
            							searchDAO.saveObject(fpOWTSResidentialUnit);    								
        							}
        						}
        						if(fpOWTSNonResidential!=null){
        							fpOWTSNonResidential.setPresentPopulationCount(new Integer(0));
        							searchDAO.saveObject(fpOWTSNonResidential);
        							if(fpOWTSNonResidentialUnit!=null){
            							fpOWTSNonResidentialUnit.setPresentUnitsCount(new Integer(0));
            							searchDAO.saveObject(fpOWTSNonResidentialUnit);
        							}					
        						}	
        						isUpdated=true;
        					}
    					}
    					
    					if(!projectedPopOWTS){
        					//set present OWTS population to 0
        					messages.add("Projected Onsite Wastewater Treatment System Population");
        					if(confirmDelete){
        						if(fpOWTSResidential!=null){
        							fpOWTSResidential.setProjectedPopulationCount(new Integer(0));
        							fpOWTSResidential.setProjectionYear(null);
        							searchDAO.saveObject(fpOWTSResidential);
        							if(fpOWTSResidentialUnit!=null){
            							fpOWTSResidentialUnit.setProjectedUnitsCount(new Integer(0));
            							searchDAO.saveObject(fpOWTSResidentialUnit);    								
        							}
        						}
        						if(fpOWTSNonResidential!=null){
        							fpOWTSNonResidential.setProjectedPopulationCount(new Integer(0));
        							fpOWTSNonResidential.setProjectionYear(null);
        							searchDAO.saveObject(fpOWTSNonResidential);
        							if(fpOWTSNonResidentialUnit!=null){
            							fpOWTSNonResidentialUnit.setProjectedUnitsCount(new Integer(0));
            							searchDAO.saveObject(fpOWTSNonResidentialUnit);
        							}
        						}
        						isUpdated=true;
        					}
        				}
    					
    					//check and delete if both the values are zero
    					
    					if(fpOWTSResidential!=null){
      					   if((fpOWTSResidential.getPresentPopulationCount()==null || fpOWTSResidential.getPresentPopulationCount().intValue()==0) 
      							   && (fpOWTSResidential.getProjectedPopulationCount()==null || fpOWTSResidential.getProjectedPopulationCount().intValue()==0)){
      						   //delete the record
      						   searchDAO.removeObject(fpOWTSResidential);
         					   if(fpOWTSResidentialUnit!=null){
         						 searchDAO.removeObject(fpOWTSResidentialUnit);
         					   }
      					   }
      					}
      					
      					// check if both values are set to zero non resident
      					if(fpOWTSNonResidential!=null){
      					   if((fpOWTSNonResidential.getPresentPopulationCount()==null || fpOWTSNonResidential.getPresentPopulationCount().intValue()==0) 
      							   && (fpOWTSNonResidential.getProjectedPopulationCount()==null || fpOWTSNonResidential.getProjectedPopulationCount().intValue()==0)){
      						   //delete the record
      						   searchDAO.removeObject(fpOWTSNonResidential);
         					   if(fpOWTSNonResidentialUnit!=null){
         						 searchDAO.removeObject(fpOWTSNonResidentialUnit);
         					   }
      					   }
      					}     					
    				} 				
    			}
    		}else{
    			//1. get all the facility population
    	        if( facilityPopulation!=null &&  facilityPopulation.size()>0){
    				//delete all the population
    				messages.add("Population");
    				if(confirmDelete){
    					//2. loop and delete all the facility population
    			        for (Iterator iter = facilityPopulation.iterator(); iter.hasNext();) {
    						FacilityPopulation fp = (FacilityPopulation) iter.next();
    						searchDAO.removeObject(fp);
    						isUpdated=true;
    					}				
    				}	        	
    	        }			
    		}        	
        }
		if(isUpdated){
			updateFESTimeStamps(facilityId, FacilityService.DATA_AREA_POPULATION, userId);	
		}
		//this belongs to calling method
		//updateReviewStatusAndFacilityTimeStamp(facilityId, userId);
		return isUpdated;
	}
	
	private FacilityPopulation  getFacilityPopulation(Long facilityId, Long populationId){
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, populationId));
         return (FacilityPopulation)searchDAO.getSearchObject(FacilityPopulation.class, scs);	
	}
	
	private FacilityPopulationUnit  getFacilityPopulationUnits(Long facilityId, Long populationId){		
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.populationId", SearchCondition.OPERATOR_EQ, populationId));
         return (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs);	
	}
	
	
	
    public void updateFESTimeStamps(Long facilityId, Long dataAreaId, String user) {
		
		SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ, dataAreaId));
		FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
		
		if(facilityEntryStatus != null)
		{
			facilityEntryStatus.setLastUpdateTs(new Date());
			searchDAO.saveObject(facilityEntryStatus);
		}
		else
		{
			//throw serious error
		}
    }
    
    public void updateReviewStatusAndFacilityTimeStamp(Long facilityId, String user) {
		Facility facility = (Facility)searchDAO.getObject(Facility.class, facilityId);
		
		if (facility != null){
		     if ("SAS".equalsIgnoreCase(facility.getReviewStatusRef().getReviewStatusId())){
		    	 FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
		    	 FacilityReviewStatusId id = new FacilityReviewStatusId(facilityId.longValue(), "SIP", new Date());
		    	 facilityreviewstatus.setId(id);
		    	 facilityreviewstatus.setLastUpdateUserid(user);
		    	 searchDAO.saveObject(facilityreviewstatus);
		    	 
		        ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
		        reviewStatusRef.setReviewStatusId("SIP");
		        facility.setReviewStatusRef(reviewStatusRef);
		     } 
		  
		  facility.setLastUpdateTs(new Date());
	      facility.setLastUpdateUserid(user);
		  searchDAO.saveObject(facility);  
		}
    }
	
	private boolean isValidForPopRecCollection(Collection facilityTypes, char ppFlag){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ppFlag=='P'){
					if(ft.getPresentFlag()=='Y' && ftr.getPopRecCollectionFlag()=='Y'){
						return true;
					}					
				}else{
					if(ft.getProjectedFlag()=='Y' && ftr.getPopRecCollectionFlag()=='Y'){
						return true;
					}
				}
			}
		}
	    return false;		
	}
	
	private boolean isValidForPopulation(Collection facilityTypes){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ftr.getValidForPopulationFlag()=='Y'){
					return true;
				}					
			}
		}
		return false;
	}
	
	private boolean isValidForPopClusteredCollection(Collection facilityTypes, char ppFlag){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ppFlag=='P'){
					if(ft.getPresentFlag()=='Y' && ftr.getPopClusteredFlag()=='Y'){
						return true;
					}					
				}else{
					if(ft.getProjectedFlag()=='Y' && ftr.getPopClusteredFlag()=='Y'){
						return true;
					}
				}
			}
		}
	    return false;		
	}
	
	private boolean isValidForPopOWTSCollection(Collection facilityTypes, char ppFlag){
		if(facilityTypes!=null){
			for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				FacilityTypeRef ftr = ft.getFacilityTypeRef();
				if(ppFlag=='P'){
					if(ft.getPresentFlag()=='Y' && ftr.getPopOwtsFlag()=='Y'){
						return true;
					}					
				}else{
					if(ft.getProjectedFlag()=='Y' && ftr.getPopOwtsFlag()=='Y'){
						return true;
					}
				}
			}
		}
	    return false;		
	}
	

	
	
	
	
	private void deleteChanges(FacilityType ft) {
		for (Iterator iter = ft.getFacilityTypeChanges().iterator(); iter.hasNext();) {
			FacilityTypeChange ftc = (FacilityTypeChange) iter.next();
			searchDAO.removeObject(ftc);			
		}
	}
	
	public void addFacilityType(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String presentTPType, String projectedTPType, String npsStatus,String userId){
		
		Facility facility = getFacility(facilityId);
		
		//create the object		
		FacilityType ft = new FacilityType();
		FacilityTypeId ftId = new FacilityTypeId();
		ftId.setFacilityId(facilityId.longValue());
		ftId.setFacilityTypeId(facilityTypeId.longValue());
		ft.setId(ftId);
		
		//set treatment plant type if facilityTypeId is treatment plant
		if(facilityTypeId.longValue()==FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT.longValue()){
			boolean savFac=false;
			if(presentTPType!=null && !"".equals(presentTPType)){
				facility.setPresentTreatmentPlantType(new Character(presentTPType.charAt(0)));
				savFac=true;
			}
			
			if(projectedTPType!=null && !"".equals(projectedTPType)){
				facility.setProjectedTreatmentPlantType(new Character(projectedTPType.charAt(0)));
				savFac=true;
			}
			if(savFac){
				searchDAO.saveObject(facility);
			}
		}
		//set status for non-point source
		//if(facilityTypeId.longValue() > 10 && facilityTypeId.longValue() < 22){
		//	if(npsStatus!=null&& !"".equals(npsStatus)){
		//		//nothing
		//	}
		//}
		
		if(FACILITY_TYPE_STATUS_PRESENT.equals(status) || FACILITY_TYPE_STATUS_BOTH.equals(status)){
			ft.setPresentFlag('Y');
		}else{
			ft.setPresentFlag('N');
		}
		if(FACILITY_TYPE_STATUS_PROJECTED.equals(status) || FACILITY_TYPE_STATUS_BOTH.equals(status)){
			ft.setProjectedFlag('Y');
		}else {
			ft.setProjectedFlag('N');
		}
		ft.setLastUpdateTs(new Date());
		ft.setLastUpdateUserid(userId);
		ft.setFacilityTypeChanges(getFacilityTypeChanges(facilityId, facilityTypeId, facilityChangeIds, userId));
		ft.setFacility(facility);
		ft.setFacilityTypeRef(getFacilityTypeRef(facilityTypeId));
		//ft.setFeedbackDeleteFlag();  what is the default value??
		
		searchDAO.saveObject(ft);	
		saveChanges(ft);
		
		//add fes and federal review records
		addFacilityTypeDataAreas(facilityId, ft, userId);
	}
	
	public void addFacilityTypeDataAreas(Long facilityId, FacilityType ft, String userId){
		setStandardDataAreas(facilityId, userId);
		
	    //discharge
		if(ft.getFacilityTypeRef().getValidForDischargeFlag()=='Y'){
			setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_DISCHARGE, userId);
			setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_DISCHARGE, userId);	
			setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_DISCHARGE, userId);
		}
		
		//Popullation
		if(ft.getFacilityTypeRef().getValidForPopulationFlag()=='Y'){
			setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_POPULATION, userId);
			setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_POPULATION, userId);
			setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_POPULATION, userId);
		}
		
		//FLOW
		if(ft.getFacilityTypeRef().getValidForFlowFlag()=='Y'){
			setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_FLOW, userId);
			setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_FLOW, userId);				
			setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_FLOW, userId);
		}
		
		//CSO
		if(ft.getFacilityTypeRef().getValidForCsoFlag()=='Y'){
			setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_CSO, userId);
			setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_CSO, userId);		
			setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_CSO, userId);
		}
		
		//Effluent
		if(ft.getFacilityTypeRef().getValidForEffluentFlag()=='Y'){
			setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_EFFLUENT, userId);
			setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_EFFLUENT, userId);
			setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_EFFLUENT, userId);
		}

		//Unit Process
		if(ft.getFacilityTypeRef().getValidForUnitProcessFlag()=='Y'){
			setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_UNIT_PROCESS, userId);
			setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_UNIT_PROCESS, userId);
			setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_UNIT_PROCESS, userId);
		}
		
		//Utility Management
		if(ft.getFacilityTypeRef().getValidForUtilMgmtFlag()=='Y'){
			setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_UTIL_MANAGEMENT, userId);
			setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_UTIL_MANAGEMENT, userId);
			setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_UTIL_MANAGEMENT, userId);
		}	
		
		//pollution
		if(ft.getFacilityTypeRef().getValidForPollutionFlag()=='Y'){
			setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_POLLUTION, userId);
			setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_POLLUTION, userId);
			setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_POLLUTION, userId);
		}
	}
	
	
	public void setStandardDataAreas(Long facilityId, String userId){
		//	facility 
		setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_FACILITY, userId);
		setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_FACILITY, userId);
		setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_FACILITY, userId);
		
		//Geographic
		setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_GEOGRAPHIC, userId);
		setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_GEOGRAPHIC, userId);
		setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_GEOGRAPHIC, userId);
		
		//POINT of contact
		setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_POC, userId);
		setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_POC, userId);
		setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_POC, userId);
		
		//Needs
		setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_NEEDS, userId);
		setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_NEEDS, userId);
		setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_NEEDS, userId);

		//Permit
		setFacilityEntryStatus(facilityId, FacilityService.DATA_AREA_PERMITS, userId);
		setFederalReviewStatus(facilityId, FacilityService.DATA_AREA_PERMITS, userId);
		setFacilityCostCurveDataArea(facilityId, FacilityService.DATA_AREA_PERMITS, userId);
		
	}
	
	//check valid data areas
	public void updateFES(Long facilityId){
		Collection fts = getFacityType(facilityId);
		ArrayList dataArr = new ArrayList();
		dataArr.add(FacilityService.DATA_AREA_DISCHARGE);
		dataArr.add(FacilityService.DATA_AREA_POPULATION);
		dataArr.add(FacilityService.DATA_AREA_POLLUTION);
		dataArr.add(FacilityService.DATA_AREA_FLOW);
		dataArr.add(FacilityService.DATA_AREA_EFFLUENT);
		dataArr.add(FacilityService.DATA_AREA_CSO);
		dataArr.add(FacilityService.DATA_AREA_UNIT_PROCESS);
		dataArr.add(FacilityService.DATA_AREA_UTIL_MANAGEMENT);
		
		Map validDataAreas = new HashMap();		
		for (Iterator iter = dataArr.iterator(); iter.hasNext();) {
			Long da = (Long) iter.next();
			validDataAreas.put(da, "N");			
		}

		for (Iterator iter = fts.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(ft.getFacilityTypeRef().getValidForPopulationFlag()=='Y'){
				validDataAreas.put(FacilityService.DATA_AREA_POPULATION, "Y");
			}
			if(ft.getFacilityTypeRef().getValidForPollutionFlag()=='Y'){
				validDataAreas.put(FacilityService.DATA_AREA_POLLUTION, "Y");
			}
			if(ft.getFacilityTypeRef().getValidForDischargeFlag()=='Y'){
				validDataAreas.put(FacilityService.DATA_AREA_DISCHARGE, "Y");
			}
			if(ft.getFacilityTypeRef().getValidForFlowFlag()=='Y'){
				validDataAreas.put(FacilityService.DATA_AREA_FLOW, "Y");
			}
			if(ft.getFacilityTypeRef().getValidForEffluentFlag()=='Y'){
				validDataAreas.put(FacilityService.DATA_AREA_EFFLUENT, "Y");
			}
			if(ft.getFacilityTypeRef().getValidForCsoFlag()=='Y'){
				validDataAreas.put(FacilityService.DATA_AREA_CSO, "Y");
			}
			if(ft.getFacilityTypeRef().getValidForUnitProcessFlag()=='Y'){
				validDataAreas.put(FacilityService.DATA_AREA_UNIT_PROCESS, "Y");
			}
			if(ft.getFacilityTypeRef().getValidForUtilMgmtFlag()=='Y'){
				validDataAreas.put(FacilityService.DATA_AREA_UTIL_MANAGEMENT, "Y");
			}
		}
		//delete non valid fes and frs records
		for (Iterator iter = dataArr.iterator(); iter.hasNext();) {
			Long da = (Long) iter.next();
			String val=(String)validDataAreas.get(da);
			if("N".equals(val)){
				FacilityEntryStatus fes = getFacilityEntryStatus(facilityId, da);
				if(fes!=null){
					fesService.deleteFacilityEntryStatus(fes);	
				}
				FederalReviewStatus frs = getFederalReviewStatus(facilityId, da);
				if(frs!=null){
					searchDAO.removeObject(frs);
				}
				
				//delete any messages also
				Collection fccdam = getFacilityCostCuveDataAreaMessages(facilityId, da);
				if(fccdam!=null){
					searchDAO.deleteAll(fccdam);
				}				
                //delete facility cost curve data area records
				Collection fccdas = getFacilityCostCurveDataArea(facilityId, da);
				if(fccdas!=null){
					searchDAO.deleteAll(fccdas);
				}
			}
		}
	}
	
	private Collection getFacilityCostCuveDataAreaMessages(Long facilityId, Long dataAreaId){		
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		scs.setCondition(new SearchCondition ("sourceCode", SearchCondition.OPERATOR_EQ, "ccv"));
		return searchDAO.getSearchList(FacilityDataAreaMessage.class, scs);
	}
	
	public boolean isUpdateFacilityTypeDeleteData(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String userId, Collection messages){
		//get updated facilityList
		ArrayList facilityTypes = getUpdatedFacilityTypeList(facilityId, facilityTypeId, status, facilityChangeIds, userId);
		//check if any data deleted
		ArrayList errors = new ArrayList();  //errors will not be throw because the confirmDelete is false
		deleteData(facilityTypes, facilityId, userId, false, messages, errors);
		if(messages.size()>0){
			return true;
		}
		return false; 
	}
	
	private ArrayList getUpdatedFacilityTypeList(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String userId){
		//		get the facility Type object and update the status and changes information
		FacilityType ft =  new FacilityType();
		FacilityTypeId ftId = new FacilityTypeId(facilityId.longValue(), facilityTypeId.longValue());
		ft.setId(ftId);
		ft.setFacilityTypeRef(getFacilityTypeRef(facilityTypeId));
		// update the status
		if(FACILITY_TYPE_STATUS_PRESENT.equals(status) || FACILITY_TYPE_STATUS_BOTH.equals(status)){
			ft.setPresentFlag('Y');
		}else {
			ft.setPresentFlag('N');
		}
		if(FACILITY_TYPE_STATUS_PROJECTED.equals(status) || FACILITY_TYPE_STATUS_BOTH.equals(status)){
			ft.setProjectedFlag('Y');
		}else {
			ft.setProjectedFlag('N');
		}
		//set the new changes
		ft.setFacilityTypeChanges(getFacilityTypeChanges(facilityId, facilityTypeId, facilityChangeIds, userId));
		
		//update the facilityTypes
		Collection fts = getFacityType(facilityId);
		ArrayList facilityTypes = new ArrayList();
		for (Iterator iter = fts.iterator(); iter.hasNext();) {
			FacilityType facilityType = (FacilityType) iter.next();
			if(facilityType.getId().getFacilityTypeId()==ft.getId().getFacilityTypeId()){
				facilityTypes.add(ft);
			}else{
				facilityTypes.add(facilityType);
			}			
		} 
		return facilityTypes;
	}	
	
	public void updateFacilityTypeForFeedBack(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String presentTPType, String projectedTPType, String npsStatus, String userId){
		updateFacilityTypeObject(facilityId, facilityTypeId, status, facilityChangeIds, presentTPType, projectedTPType, npsStatus, userId);		
	}
	
	
	
	public void updateFacilityType(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String presentTPType, String projectedTPType, String npsStatus, String userId){
		
		//get updated facilityList
		ArrayList facilityTypes = getUpdatedFacilityTypeList(facilityId, facilityTypeId, status, facilityChangeIds, userId);
		//check if any data deleted
		ArrayList errors = new ArrayList();  //errors will not be throw because the confirmDelete is false -- in future
		ArrayList messages = new ArrayList(); //not doing anything with it right now
		
		//getupstream and sewershed
		deleteDischarge(facilityTypes, facilityId, userId, false, messages, errors);
		Collection oldUpstream = new ArrayList();
		Collection oldSewershed = new ArrayList();
		if(isDishargeUpdated(messages)){
			//get upstream facilitites before delete
			oldUpstream = populationService.getUpstreamFacilityIds(facilityId.toString());
			oldSewershed = populationService.getRelatedSewerShedFacilities(facilityId);
		}
		
		//reset messages
		messages = new ArrayList();
		//delete any data
		deleteData(facilityTypes, facilityId, userId, true, messages, errors);
				
		//update facility Type
		updateFacilityTypeObject(facilityId, facilityTypeId, status, facilityChangeIds, presentTPType, projectedTPType, npsStatus, userId);
		
        //set rerun flag
		setRerunFlag(facilityId, messages, oldUpstream, oldSewershed);
	}
	
	
	private void updateFacilityTypeObject(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String presentTPType, String projectedTPType, String npsStatus, String userId){
		//update the facilityType
		Facility facility = getFacility(facilityId);
		//get the facility type object 
		FacilityType ft = getFacilityType(facilityId, facilityTypeId);
		
		ft.setLastUpdateTs(new Date());
		ft.setLastUpdateUserid(userId);		
		
		if(facilityTypeId.longValue()==FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT.longValue()){
			boolean savFac= false;
			if(presentTPType!=null && !"".equals(presentTPType)){
				if(facility.getPresentTreatmentPlantType()==null ||facility.getPresentTreatmentPlantType().charValue()!=presentTPType.charAt(0)){
				      //set rerun flag
					ArrayList ar = new ArrayList();
					ar.add(new Long(1));
					ar.add(new Long(2));
					ar.add(new Long(4));
					updateRerunFlags(facilityId,ar); 
				}
				facility.setPresentTreatmentPlantType(new Character(presentTPType.charAt(0)));
				savFac=true;
			}
			
			if(projectedTPType!=null && !"".equals(projectedTPType)){
				if(facility.getProjectedTreatmentPlantType()==null || facility.getProjectedTreatmentPlantType().charValue()!=projectedTPType.charAt(0)){
				      //set rerun flag 	
					ArrayList ar = new ArrayList();
					ar.add(new Long(1));
					ar.add(new Long(2));
					ar.add(new Long(4));
					ar.add(new Long(5));
					ar.add(new Long(6));
					updateRerunFlags(facilityId,ar); 

				}
				facility.setProjectedTreatmentPlantType(new Character(projectedTPType.charAt(0)));
				savFac=true;
			}
			if(savFac){
				searchDAO.saveObject(facility);
			}
		}
		
//		if(ft.getFacilityTypeRef().getFacilityOverallTypeRef().getFacilityOverallTypeId() == FacilityTypeService.FACILITY_TYPE_OVERALL_TYPE_NPS){
//			if(npsStatus!=null && !"".equals(npsStatus)){
//				 facility.setNpsTypeCode(npsStatus);	
//			}	
//		}
		ft.setFacility(facility);
		//update the status
		if(FACILITY_TYPE_STATUS_PRESENT.equals(status) || FACILITY_TYPE_STATUS_BOTH.equals(status)){
			ft.setPresentFlag('Y');
		}else {
			ft.setPresentFlag('N');
		}
		
		if(FACILITY_TYPE_STATUS_PROJECTED.equals(status) || FACILITY_TYPE_STATUS_BOTH.equals(status)){
			ft.setProjectedFlag('Y');
		}else {
			ft.setProjectedFlag('N');
		}		
		//remove the old changes
		deleteChanges(ft);
		ft.setFacilityTypeChanges(new HashSet());		
		searchDAO.saveObject(ft);
		searchDAO.flushAndClearCache();
		//refetch
		FacilityType ft2 = getFacilityType(facilityId, facilityTypeId);

		//add the new changes and save
		ft2.setFacilityTypeChanges(getFacilityTypeChanges(facilityId, facilityTypeId, facilityChangeIds, userId));
		saveChanges(ft2);		
		//update the Facility Type Object and changes
		searchDAO.saveObject(ft2);	
	}
	
	private void updateRerunFlags(Long facilityId, Collection costCurveIds){
		Collection fccs = getFacilityCostCurves(facilityId);
		for (Iterator iter = fccs.iterator(); iter.hasNext();) {
			FacilityCostCurve fcc = (FacilityCostCurve) iter.next();
			for (Iterator iterator = costCurveIds.iterator(); iterator.hasNext();) {
				Long costCurveId = (Long) iterator.next();
				if(fcc.getCostCurveRef().getCostCurveId()==costCurveId.longValue()){
					//update fcc
					fcc.setCurveRerunFlag('Y');
					searchDAO.saveObject(fcc);
				}				
			}						
		}
	}
	
	
	private void saveChanges(FacilityType ft) {
		for (Iterator iter = ft.getFacilityTypeChanges().iterator(); iter.hasNext();) {
			FacilityTypeChange ftc = (FacilityTypeChange) iter.next();
			searchDAO.saveObject(ftc);			
		}
	}

	public Set getFacilityTypeChanges(Long facilityId, Long facilityTypeId, Collection facilityChangeIds, String userId){
		Set set = new HashSet();
		for (Iterator iter = facilityChangeIds.iterator(); iter.hasNext();) {
			String facilityChangeIdStr = (String) iter.next();
			Long facilityChangeId = new Long(facilityChangeIdStr);
			FacilityTypeChange ctc = new FacilityTypeChange();
			FacilityTypeChangeId ctcId = new FacilityTypeChangeId();
			ctcId.setChangeTypeId(facilityChangeId.longValue());
			ctcId.setFacilityTypeId(facilityTypeId.longValue());
			ctcId.setFacilityId(facilityId.intValue());
			ctc.setId(ctcId);
			ctc.setLastUpdateTs(new Date());
			ctc.setLastUpdateUserid(userId);
			ctc.setChangeTypeRef(getChangeType(facilityChangeId));
			ctc.setFacilityType( getFacilityType(facilityId, facilityTypeId));
			set.add(ctc);
		}
		return set;
	}
	
	public Collection getChangeTypeRefs(Collection selectedChangesList){
		ArrayList arr = new ArrayList();
		for (Iterator iter = selectedChangesList.iterator(); iter.hasNext();) {
			String facilityChangeIdStr = (String) iter.next();
			Long facilityChangeId = new Long(facilityChangeIdStr);
			ChangeTypeRef ctr = getChangeType(facilityChangeId);
			arr.add(ctr);
		}
	    return arr;                    		
	}
	
	public ChangeTypeRef getChangeType(Long facilityChangeId){
		return (ChangeTypeRef) searchDAO.getObject(ChangeTypeRef.class, facilityChangeId);		
	}
	
	public FacilityTypeRef getFacilityTypeRef(Long facilityTypeId){
		return (FacilityTypeRef) searchDAO.getObject(FacilityTypeRef.class, facilityTypeId);		
	}
	
	public Facility getFacility(Long facilityId){
	  return (Facility) searchDAO.getObject(Facility.class, facilityId);	
	}
	
	public Collection getChangeType(Collection changeTypeIds){
	   SortCriteria sortCriteria = new SortCriteria("sortSequence", SortCriteria.ORDER_ASCENDING);	
	   ArrayList sortArray = new ArrayList();
	   sortArray.add(sortCriteria);	    	
	   SearchConditions scs =  new SearchConditions(new SearchCondition("changeTypeId", SearchCondition.OPERATOR_IN, changeTypeIds));
	   return (Collection) searchDAO.getSearchList(ChangeTypeRef.class, scs, sortArray);
	}
	
	public void setFacilityEntryStatus(Long facilityId, Long dataAreaId, String userId){
		//check if the dataArea record exists if not create a new one
		FacilityEntryStatus fesObj = getFacilityEntryStatus(facilityId, dataAreaId);
		if(fesObj == null){
			//create a new record
			fesObj = new FacilityEntryStatus();
			fesObj.setId(new FacilityEntryStatusId(facilityId.longValue(), dataAreaId.longValue()));
			fesObj.setDataAreaRef(getDataAreaRef(dataAreaId));
			fesObj.setFacility(getFacility(facilityId));
			fesObj.setErrorFlag('N');
			fesObj.setEnteredFlag('N');
			fesObj.setRequiredFlag('Y');
		}
		
		fesObj.setLastUpdateUserid(userId);
		fesObj.setLastUpdateTs(new Date());
		searchDAO.saveObject(fesObj);
	}	

	public void setFederalReviewStatus(Long facilityId, Long dataAreaId, String userId){
		//check if the dataArea record exists if not create a new one
		FederalReviewStatus frsObj =  getFederalReviewStatus(facilityId, dataAreaId);
		if(frsObj == null){
			//create a new record
			FederalReviewStatus frs = new FederalReviewStatus();
			frs.setId(new FederalReviewStatusId(facilityId.longValue(), dataAreaId.longValue()));
			frs.setDataAreaRef(getDataAreaRef(dataAreaId));
			frs.setFacility(getFacility(facilityId));
			frs.setLastUpdateUserid(userId);
			frs.setLastUpdateTs(new Date());
			frs.setErrorFlag('N');
			searchDAO.saveObject(frs);
		}
	}
	
	public void setFacilityCostCurveDataArea(Long facilityId, Long dataAreaId, String userId){
		//check if the dataArea record exists if not create a new one
		Collection fccdas =  getFacilityCostCurveDataArea(facilityId, dataAreaId);
		//get a list of facility Cost curves
		Collection fccs =  getFacilityCostCurves(facilityId);
		if((fccdas == null || fccdas.isEmpty()) && (fccs!=null && !fccs.isEmpty())){
			//loop through the cost curves and add the facilityCostCurveDataArea records for each
			for (Iterator iter = fccs.iterator(); iter.hasNext();) {
				FacilityCostCurve fcc = (FacilityCostCurve) iter.next();
				long facilityCostCurveId = fcc.getFacilityCostCurveId();
				FacilityCostCurveDataAreaId fccId = new FacilityCostCurveDataAreaId(facilityCostCurveId, dataAreaId.longValue());
				FacilityCostCurveDataArea fccda = new FacilityCostCurveDataArea();
				fccda.setId(fccId);
				fccda.setErrorFlag('N');
				fccda.setDataAreaRef(getDataAreaRef(dataAreaId));
				fccda.setFacilityCostCurve(fcc);
				fccda.setLastUpdateTs(new Date());
				fccda.setLastUpdateUserid(userId);
				searchDAO.saveObject(fccda);
			}
		}
	}
	
	private Collection getFacilityCostCurveDataArea(Long facilityId, Long dataAreaId){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityCostCurve", "fcc", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fcc.facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("fd.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		return searchDAO.getSearchList(FacilityCostCurveDataArea.class, new ArrayList(), scs, new ArrayList(), aliasArray);
	}

	public DataAreaRef getDataAreaRef(Long dataAreaId) {
		return (DataAreaRef) searchDAO.getObject(DataAreaRef.class, dataAreaId);
	}
	
	public Collection getDataAreaRef() {
		return searchDAO.getObjects(DataAreaRef.class);
	}
	
	public FederalReviewStatus getFederalReviewStatus(Long facilityId, Long dataAreaId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		return (FederalReviewStatus)searchDAO.getSearchObject(FederalReviewStatus.class, scs);		
	}
	
	public FacilityEntryStatus getFacilityEntryStatus(Long facilityId, Long dataAreaId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		FacilityEntryStatus fes = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs);
		return 	fes;
	}
	
	public boolean isUpStreamfacilityPreventsDelete(Long facilityId, int dischargeType){
		Collection reviewStatuses = new ArrayList();
		reviewStatuses.add(ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED);
		reviewStatuses.add(ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION);
		reviewStatuses.add(ReviewStatusRefService.FEDERAL_ACCEPTED);
		
		Collection facilities = populationService.getUpstreamFacilitiesListByDischargeType(facilityId.toString(), dischargeType);
		if(facilities!=null && facilities.size() > 0){
			Collection facilityIds = ConvertStringIdsToLongIds(facilities);
			AliasCriteria alias = new AliasCriteria("reviewStatusRef", "r", AliasCriteria.JOIN_LEFT);
			ArrayList aliasArray = new ArrayList();
			aliasArray.add(alias);
			SearchConditions scs = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_IN, facilityIds));
			scs.setCondition(new SearchCondition("r.reviewStatusId", SearchCondition.OPERATOR_IN, reviewStatuses));
			Collection rs = searchDAO.getSearchList(Facility.class, new ArrayList(), scs, new ArrayList(), aliasArray);
			if(rs.size()>0){
				return true;
			}			
		}
		return false;
	}
	
	private Collection ConvertStringIdsToLongIds(Collection facilities){
		ArrayList arr = new ArrayList();
		for (Iterator iter = facilities.iterator(); iter.hasNext();) {
			String facilityId = (String) iter.next();
			arr.add(new Long(facilityId));			
		}
		return arr;		
	}
	
	public boolean checkUpstream(FacilityType ft){
		Map mFacTypes = new HashMap();
		mFacTypes.put(FACILITY_TYPE_TREATMENT_PLANT, FACILITY_TYPE_TREATMENT_PLANT);
		mFacTypes.put(FACILITY_TYPE_COMBINED_SEWER, FACILITY_TYPE_COMBINED_SEWER);
		mFacTypes.put(FACILITY_TYPE_SEPERATE_SEWER , FACILITY_TYPE_SEPERATE_SEWER);
		mFacTypes.put(FACILITY_TYPE_OTHER , FACILITY_TYPE_OTHER);
				
		long facType = ft.getFacilityTypeRef().getFacilityTypeId();
		if(mFacTypes.get(new Long(facType))!=null){
			//check if other facility Types exists
			Long facId = new Long(ft.getFacility().getFacilityId());
			Collection fts = getFacityType(facId);
			int icount=0;
			for (Iterator iter = fts.iterator(); iter.hasNext();) {
			  FacilityType fType = (FacilityType) iter.next();
			  long ftId = fType.getFacilityTypeRef().getFacilityTypeId();
			  if(mFacTypes.get(new Long(ftId))!=null){
					icount=icount+1;
			  }			
			}
			if(icount > 1){
				return false;
			} 
			return true;
		}
		return false;
	}
	
	public boolean isFacilityTypeStatusPresent(Long facilityId){
		Collection type = getFacityType(facilityId);
		for (Iterator iter = type.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(ft.getPresentFlag()=='Y'){
				return true;
			}
			
		}
		return false;
	}
	
	public boolean isFacilityTypeStatusProjected(Long facilityId){
		Collection type = getFacityType(facilityId);
		for (Iterator iter = type.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(ft.getProjectedFlag()=='Y'){
				return true;
			}
			
		}
		return false;
	}
	
	public boolean isFacilityTypeStatusPresentAndApplicableToDataArea(Long facilityId, Long dataAreaId){
		Collection type = getFacityType(facilityId);
		for (Iterator iter = type.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();		
			if(ft.getPresentFlag()=='Y' && isValidForDataArea(ft.getFacilityTypeRef(), dataAreaId)){
				return true;
			}	
		}
		return false;	
	}
	
	public boolean isFacilityTypeStatusProjectedAndApplicableToDataArea(Long facilityId, Long dataAreaId){
		Collection type = getFacityType(facilityId);
		for (Iterator iter = type.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();		
			if(ft.getProjectedFlag()=='Y' && isValidForDataArea(ft.getFacilityTypeRef(), dataAreaId)){
				return true;
			}	
		}
		return false;	
	}	
	

	public boolean isFacilityTypeValidForDataAreaAndHasChangeType(Long facilityId, Long dataAreaId, Long changeTypeId){
		Collection type = getFacityType(facilityId);
		for (Iterator iter = type.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(isValidForDataArea(ft.getFacilityTypeRef(), dataAreaId)){
				Collection changeType = ft.getFacilityTypeChanges();
				for (Iterator iterator = changeType.iterator(); iterator
						.hasNext();) {
					FacilityTypeChange ftc = (FacilityTypeChange) iterator.next();
					if(ftc.getId().getChangeTypeId() == changeTypeId.longValue()){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isValidForDataArea(Long facilityTypeId, Long dataAreaId){
		FacilityTypeRef ftr= getFacilityTypeRef(facilityTypeId);
		return isValidForDataArea(ftr, dataAreaId);
	}
	
	private boolean isValidForDataArea(FacilityTypeRef ftr, Long dataAreaId){
		
		if(FacilityService.DATA_AREA_POPULATION.equals(dataAreaId)){
			if(ftr.getValidForPopulationFlag()!='Y'){
				return false;
			}
		}
		if(FacilityService.DATA_AREA_FLOW.equals(dataAreaId)){
			if(ftr.getValidForFlowFlag()!='Y'){
				return false;
			}
		}
		
		if(FacilityService.DATA_AREA_DISCHARGE.equals(dataAreaId)){
			if(ftr.getValidForDischargeFlag()!='Y'){
				return false;
			}
		}
		
		if(FacilityService.DATA_AREA_DISCHARGE.equals(dataAreaId)){
			if(ftr.getValidForDischargeFlag()!='Y'){
				return false;
			}
		}
		
		if(FacilityService.DATA_AREA_EFFLUENT.equals(dataAreaId)){
			if(ftr.getValidForEffluentFlag()!='Y'){
				return false;
			}
		}

		if(FacilityService.DATA_AREA_CSO.equals(dataAreaId)){
			if(ftr.getValidForCsoFlag()!='Y'){
				return false;
			}
		}

		if(FacilityService.DATA_AREA_UNIT_PROCESS.equals(dataAreaId)){
			if(ftr.getValidForUnitProcessFlag()!='Y'){
				return false;
			}
		}

		if(FacilityService.DATA_AREA_UTIL_MANAGEMENT.equals(dataAreaId)){
			if(ftr.getValidForUtilMgmtFlag()!='Y'){
				return false;
			}
		}		
		return true;
	}
	
	public String getFacilityDataAreasQueryParam(Long facilityId){
		String retStr=null;
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));		
		Collection areas = searchDAO.getSearchList(FacilityEntryStatus.class, scs);
		for (Iterator iter = areas.iterator(); iter.hasNext();) {
			FacilityEntryStatus fes = (FacilityEntryStatus) iter.next();
			String dataAreaName = fes.getDataAreaRef().getName();
			if(retStr==null){
				retStr="&" + dataAreaName.substring(0,3).toLowerCase()+"=1";	
			}else{
				retStr=retStr+"&" + dataAreaName.substring(0,3).toLowerCase()+"=1";
			}			
		}
		return retStr;
	}
	

	public boolean isFacilityNoChangeOrAbandoned(Long facilityId){
		boolean isNoChangeOrAbandoned = true;
		Collection fts = getFacityType(facilityId);
		for (Iterator iter = fts.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			Collection changeType = ft.getFacilityTypeChanges();
			for (Iterator iterator = changeType.iterator(); iterator
					.hasNext();) {
				FacilityTypeChange ftc = (FacilityTypeChange) iterator.next();
				if(ftc.getId().getChangeTypeId()!= FacilityTypeService.CHANGE_TYPE_NO_CHANGE.longValue() &&
						ftc.getId().getChangeTypeId()!= FacilityTypeService.CHANGE_TYPE_ABANDONED.longValue()){
					return false;
				}
			}			
		}
		return isNoChangeOrAbandoned;
	}
	
	public boolean isFacilityTypeNoChangeOrAbandoned(Long facilityId, Long facilityTypeId){
		FacilityType ft = getFacilityType(facilityId, facilityTypeId);
		Collection cftc = ft.getFacilityTypeChanges();
		for (Iterator iterator = cftc.iterator(); iterator.hasNext();) {
			FacilityTypeChange ftc = (FacilityTypeChange) iterator.next();
			if(ftc.getId().getChangeTypeId()!= FacilityTypeService.CHANGE_TYPE_NO_CHANGE.longValue() &&
				ftc.getId().getChangeTypeId()!= FacilityTypeService.CHANGE_TYPE_ABANDONED.longValue()){
				return false;
			}
		}
		return true;
	}
	
	public boolean isFacilityTypeValid(Long facilityId, Long facilityType, String presentProjectedFlag){
		FacilityType ft = getFacilityType(facilityId, facilityType);
		if(ft!=null){
			if("P".equals(presentProjectedFlag)){
				if(ft.getPresentFlag()=='Y'){
					return true;
				}
			}else{
				if(ft.getProjectedFlag()=='Y'){
					return true;
				}
			}		
		}
		return false;
	}	
}
