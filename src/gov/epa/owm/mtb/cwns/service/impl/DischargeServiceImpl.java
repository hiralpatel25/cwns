package gov.epa.owm.mtb.cwns.service.impl;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.discharge.DischargeForm;
import gov.epa.owm.mtb.cwns.discharge.DischargeMethodHelper;
import gov.epa.owm.mtb.cwns.model.DischargeMethodRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityDischarge;
import gov.epa.owm.mtb.cwns.model.FacilityEffluent;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeDischargeRef;
import gov.epa.owm.mtb.cwns.model.ReviewComment;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.EffluentService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;


public class DischargeServiceImpl extends CWNSService implements DischargeService {	

	/**
	 * Return a Collection of DischargeMethodRef objects based on the facilityId.
	 */
	public Collection getDischargeMethodRefs(String facilityId) {
		
		Facility facility = facilityDAO.findByFacilityId(facilityId);
		
		// Get the FacilityType objects
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility", SearchCondition.OPERATOR_EQ, facility));
		Collection facilityTypes = searchDAO.getSearchList(FacilityType.class, scs);
		
		// Get the FacilityTypeDischargeRef objects
		SearchConditions scs1 = new SearchConditions();
		Iterator ftIter = facilityTypes.iterator();
		while (ftIter.hasNext()) {
			FacilityType ft = (FacilityType) ftIter.next();
			scs1.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("facilityTypeRef", SearchCondition.OPERATOR_EQ, ft.getFacilityTypeRef()));
		}
		Collection facilityTypeDischargeRefs = searchDAO.getSearchList(FacilityTypeDischargeRef.class, scs1);

		// Create a Set of DischargeMethod ids
		Set dischargeMethodIds = new HashSet();
		Iterator ftdrIter = facilityTypeDischargeRefs.iterator();		
		while (ftdrIter.hasNext()) {
			FacilityTypeDischargeRef ftdr = (FacilityTypeDischargeRef) ftdrIter.next();
			long dischargeMethodId = ftdr.getDischargeMethodRef().getDischargeMethodId();
			// Do not add "No Discharge, Unknown"
			if (dischargeMethodId != NO_DISCHARGE_UNKNOWN){
				dischargeMethodIds.add(new Long(dischargeMethodId));
			}
		}
		
		// Get the list of DischargeMethodRefs in sorted order
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("dischargeMethodId", SearchCondition.OPERATOR_IN, dischargeMethodIds));
		Collection dischargeMethodRefs = searchDAO.getSearchList(DischargeMethodRef.class, new ArrayList(), scs2, sortArray );
		
//		filterOutExistingDischargeMethods(dischargeMethodRefs,facilityId);
		return dischargeMethodRefs;
	}	
	
	public boolean isTerminatingFacility(Long facilityId) {
		Facility facility = facilityDAO.findByFacilityId(facilityId.toString());
		
		// Get the FacilityDischarge objects
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityByFacilityId.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		
		DischargeMethodRef dischargeMethodRef = getDischargeMethodRef(DischargeService.DISCHARGE_TO_ANOTHER_FACILITY);
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("dischargeMethodRef.dischargeMethodId", SearchCondition.OPERATOR_EQ, new Long(DischargeService.DISCHARGE_TO_ANOTHER_FACILITY)));
		Collection nextDownstreamfacilityDischarges = searchDAO.getSearchList(FacilityDischarge.class, scs);
		//System.out.println("terminating  "+facilityId.longValue()+"size "+nextDownstreamfacilityDischarges.size());
		return nextDownstreamfacilityDischarges.size() == 0 ? true : false;
		
	}
	
	// Get the list of next downsream facilities 
	public double getPresentTotalFlowForNextDownstreamFacilities(Long facilityId) {
		double total = 0.0;
		
		Facility facility = facilityDAO.findByFacilityId(facilityId.toString());
		
		// Get the FacilityDischarge objects
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityByFacilityId", SearchCondition.OPERATOR_EQ, facility));
		
		DischargeMethodRef dischargeMethodRef = getDischargeMethodRef(DischargeService.DISCHARGE_TO_ANOTHER_FACILITY);
		scs.setCondition(new SearchCondition("dischargeMethodRef", SearchCondition.OPERATOR_EQ, dischargeMethodRef));
		Collection nextDownstreamfacilityDischarges = searchDAO.getSearchList(FacilityDischarge.class, scs);

		Iterator fdIter = nextDownstreamfacilityDischarges.iterator();
		
		while (fdIter.hasNext()) {
			FacilityDischarge fd = (FacilityDischarge)fdIter.next();
			Short portion = fd.getPresentFlowPortionPersent();
			short portionValue = (portion==null)?0:portion.shortValue();
			long facilityId2 = fd.getFacilityByFacilityIdDischargeTo().getFacilityId();
//			total = total + (flowService.getPresentTotalFlow(new Long(facilityId2))) * portionValue / 100 ;
			total = total + flowService.getPresentTotalFlow(new Long(facilityId2));
		}
		return total;
	}
	
	/**
	 * Given a Collection of DischargeMethodRef objects remove any that are already 
	 * associate with the Facility. 
	 * 
	 * The one exception to this is the method DISCHARGE_TO_ANOTHER_FACILITY.
	 * Since this method can be associated to a facility multiple times is should 
	 * not be filtered out. 
	 * 
	 * @param dischargeMethodRefs
	 * @return
	 */
	private Collection filterOutExistingDischargeMethods(Collection dischargeMethodRefs,String facilityId) {
		
		// Filter out all discharge methods, other than "Discharge to Another  Facility", that are
		// already assoicated with this Facility.  
		Collection dischargeMethodRefsToRemove = new ArrayList();
		Collection facilityDischarges = getFacilityDischarges(facilityId);
		Iterator fdIter = facilityDischarges.iterator();
		while (fdIter.hasNext()) {
			FacilityDischarge fd = (FacilityDischarge)fdIter.next();
			Iterator dmrIter = dischargeMethodRefs.iterator();
			while (dmrIter.hasNext()) {
				DischargeMethodRef dmr = (DischargeMethodRef)dmrIter.next();
				if (dmr.getDischargeMethodId() == fd.getDischargeMethodRef().getDischargeMethodId() &&
					dmr.getDischargeMethodId() != DISCHARGE_TO_ANOTHER_FAC) {
					dischargeMethodRefsToRemove.add(dmr);
				}
			}
		}
		dischargeMethodRefs.removeAll(dischargeMethodRefsToRemove);
		
		return  dischargeMethodRefs;
		
	}
	
	
	
	
	
	/**
	 * Return a Collection of DischargeMethodHelpers objects based on the facilityId.
	 */
	public Collection getDischargeMethodHelpers(String facilityId, String locationType) {
	    
		Collection dischargeMethodRefs = null;
		if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(locationType)){
            // Get the list of DischargeMethodRefs in sorted order
			SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
			ArrayList sortArray = new ArrayList();
			sortArray.add(sortCriteria);		
			
			//SearchConditions scs2 =  new SearchConditions(new SearchCondition("dischargeMethodId", SearchCondition.OPERATOR_IN, dischargeMethodIds));
			dischargeMethodRefs = searchDAO.getSearchList(DischargeMethodRef.class, new SearchConditions(), sortArray );
		}
		else{
		 dischargeMethodRefs    = getDischargeMethodRefs(facilityId);
		}
		HashMap dischargeMethodHelpers = new LinkedHashMap();
		
		// Loop through the Collection of DischargeMethodRef objects and 
		// create a Hashmap of DischargeMethodHelper objects
		Iterator dmrIter = dischargeMethodRefs.iterator();
		while (dmrIter.hasNext()) {
			DischargeMethodRef dmr = (DischargeMethodRef) dmrIter.next();
			DischargeMethodHelper dmh = new DischargeMethodHelper(
											new Long(dmr.getDischargeMethodId()).toString(),
											dmr.getName());
			dischargeMethodHelpers.put(dmh.getId(), dmh);
		}
		
		if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(locationType)){
		// Get the FacilityType objects for this Facility
		
		Facility facility = facilityDAO.findByFacilityId(facilityId);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility", SearchCondition.OPERATOR_EQ, facility));
		Collection facilityTypes = searchDAO.getSearchList(FacilityType.class, scs);
		
		Iterator ftIter = facilityTypes.iterator();
		while (ftIter.hasNext()) {
			// For Each FacilityType get the list of FacilityTypeDischargeRef objects
			FacilityType ft = (FacilityType) ftIter.next();
			SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityTypeRef", SearchCondition.OPERATOR_EQ, ft.getFacilityTypeRef()));
			Collection facilityTypeDischargeRefs = searchDAO.getSearchList(FacilityTypeDischargeRef.class, scs1);

			// For Each FacilityTypeDischargeRef object get the associated DischargeMethodRef object
			Iterator ftdrIter = facilityTypeDischargeRefs.iterator();		
			while (ftdrIter.hasNext()) {
				FacilityTypeDischargeRef ftdr = (FacilityTypeDischargeRef) ftdrIter.next();
				DischargeMethodRef dmr = ftdr.getDischargeMethodRef();
				
				// Do not include "No Discharge, Unknown" as it is no longer used
				if (dmr.getDischargeMethodId()!= NO_DISCHARGE_UNKNOWN){

					// Update the DischargeMethodHelp object's Present and Projected attributes 
					// based on what this FacilityType allows.
					DischargeMethodHelper dmh = (DischargeMethodHelper)dischargeMethodHelpers.get(new Long (dmr.getDischargeMethodId()).toString());
					if (ft.getPresentFlag() == 'Y') {
						dmh.setPresent("Y");
					}
/* MNC */					
					if (ft.getProjectedFlag() == 'Y') {
						dmh.setProjected("Y");
					}
/* */						
				}
			}
			
		}
		}
		
		// Remove from the Collection of DischargeMethodHelpers any discharge method, other than 
		// "Discharge to Another  Facility", that is already assoicated with this Facility.
		Collection facilityDischarges = getFacilityDischarges(facilityId);
		Iterator fdIter = facilityDischarges.iterator();
		while (fdIter.hasNext()) {
			FacilityDischarge fd   = (FacilityDischarge)fdIter.next();
			DischargeMethodRef dmr = fd.getDischargeMethodRef();
			String dmId = new Long (dmr.getDischargeMethodId()).toString();
			DischargeMethodHelper dmh = (DischargeMethodHelper)dischargeMethodHelpers.get(dmId);
			if (dmh != null && 
				dmr.getDischargeMethodId() != DISCHARGE_TO_ANOTHER_FAC) {
				dischargeMethodHelpers.remove(dmId);
			}
		}

		// Convert the HashMap to a Collection and return
		return  dischargeMethodHelpers.values();
	}
	
	/**
	 * Get the list of FACTILITY_DISCHARGE objects that are associated with the facilityId. 
	 * @param facilityId
	 * @return
	 */
	public Collection getFacilityDischarges(String facilityId) {
		
		Facility facility = facilityDAO.findByFacilityId(facilityId);

		AliasCriteria alias = new AliasCriteria("dischargeMethodRef", "dm", AliasCriteria.JOIN_LEFT);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		
		SortCriteria sortCriteria = new SortCriteria("dm.name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityByFacilityId", SearchCondition.OPERATOR_EQ, facility));
		Collection facilityDischarges = searchDAO.getSearchList(FacilityDischarge.class, new ArrayList(), scs, new ArrayList(), aliasArray);

		return  facilityDischarges;
	}
	
	/**
	 * Add a Discharge Method to the Facility. 
	 * @param dForm
	 */
	public void addDischargeMethod(DischargeForm dForm, CurrentUser user) {
		Collection oldDownStreamFacilities = null;
		Collection oldPresentSewershed = null;
		Collection newDownStreamFacilities = null;
		Collection newPresentSewershed = null;
		boolean setProjectedCostCurveRerun = false;
		boolean setPresentCostCurveRerun = false;
		ArrayList costCurveIdList1 = new ArrayList();
		costCurveIdList1.add(new Long(1));
		costCurveIdList1.add(new Long(2));
		costCurveIdList1.add(new Long(4));
		costCurveIdList1.add(new Long(5));
		costCurveIdList1.add(new Long(6));
		costCurveIdList1.add(new Long(10));
		ArrayList costCurveIdList2 = new ArrayList();
		costCurveIdList2.add(new Long(7));

		// Get the Facility object
		String facId = new Long(dForm.getFacilityId()).toString();
		Facility facility = facilityDAO.findByFacilityId(facId);
		Facility facilityToDischargeTo = null;
		// Get the DischargeMethodRef object
		String dischargeMethodId = dForm.getMethodId();
		DischargeMethodRef dischargeMethodRef = getDischargeMethodRef(dischargeMethodId);

		// Create and initialize the FacilityDischarge object
		FacilityDischarge fd = new FacilityDischarge();
		fd.setDischargeMethodRef(dischargeMethodRef);
		fd.setFacilityByFacilityId(facility);

		if (dischargeMethodRef.getDischargeMethodId() == DISCHARGE_TO_ANOTHER_FAC ) {

			facilityToDischargeTo = 
				facilityDAO.getFacilityByCwnsNbr(dForm.getCwnsNumber());
			
			if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
				String reviewStatus = facility.getReviewStatusRef().getReviewStatusId();
				if (reviewStatus != facilityToDischargeTo.getReviewStatusRef().getReviewStatusId()){
					setStatusForSewershed(facility.getFacilityId(),facilityToDischargeTo.getReviewStatusRef().getReviewStatusId(),user.getUserId());
				}

				oldDownStreamFacilities = populationService.getDownstreamFacilitiesListByDischargeType(facId, new Integer(populationService.PROJECTED_ONLY).intValue());
				oldPresentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facId), new Integer(populationService.PRESENT_ONLY).intValue());

				if (dForm.getStatus() == dForm.PROJECTED){
					// add cost curve rerun
					setProjectedCostCurveRerun = true;
				}
				else if (dForm.getStatus() == dForm.PRESENT){
					// add cost curve rerun
					setPresentCostCurveRerun = true;
				}
				else if (dForm.getStatus() == dForm.PRESENT_AND_PROJECTED){
					// add cost curve rerun
					setPresentCostCurveRerun = true;
					setProjectedCostCurveRerun = true;
				}

			}
			fd.setFacilityByFacilityIdDischargeTo(facilityToDischargeTo);
		}
		fd.setFeedbackDeleteFlag('N');
		updateFacilityDischargeStatusAndFlow(fd, dForm);

		fd.setLastUpdateUserid(user.getUserId());
		fd.setLastUpdateTs(new Date());
		searchDAO.saveObject(fd);

		if (setProjectedCostCurveRerun){
			populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(facId));
			newDownStreamFacilities = populationService.getDownstreamFacilitiesListByDischargeType(facId, new Integer(populationService.PROJECTED_ONLY).intValue());
			if (newDownStreamFacilities!=null){
				Iterator iter1 = newDownStreamFacilities.iterator();
				while (iter1.hasNext()){
					String obj = (String)iter1.next();
					populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(obj));
				}
			}
			if (oldDownStreamFacilities!=null){
				Iterator iter1 = oldDownStreamFacilities.iterator();
				while (iter1.hasNext()){
					String obj = (String)iter1.next();
					populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(obj));
				}
			}
		}
		if (setPresentCostCurveRerun){
			newPresentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facId), new Integer(populationService.PRESENT_ONLY).intValue());
			if (newPresentSewershed!=null){
				Iterator iter2 = newPresentSewershed.iterator();
				while (iter2.hasNext()){
					populationService.setUpCostCurvesForRerun(costCurveIdList2,  (Long)iter2.next());
				}
			}
			if (oldPresentSewershed!=null){
				Iterator iter2 = oldPresentSewershed.iterator();
				while (iter2.hasNext()){
					populationService.setUpCostCurvesForRerun(costCurveIdList2,  (Long)iter2.next());
				}
			}

		}

	}

	
	private void setStatusForSewershed(long facilityId, String reviewStatusId, String userId) {
		Collection sewershed = populationService.getRelatedSewerShedFacilities(new Long(facilityId));
		String reviewStatus = reviewStatusId.equalsIgnoreCase(ReviewStatusRefService.STATE_CORRECTION_REQUESTED)?
				               ReviewStatusRefService.STATE_CORRECTION_REQUESTED:ReviewStatusRefService.STATE_IN_PROGRESS;
		SearchConditions scs =  new SearchConditions(new SearchCondition("reviewStatusId", SearchCondition.OPERATOR_EQ, reviewStatus));
		ReviewStatusRef reviewStatusRef = (ReviewStatusRef)searchDAO.getSearchObject(ReviewStatusRef.class, scs);
		if (sewershed!=null){
			Iterator iter = sewershed.iterator();
			while (iter.hasNext()){
				Long facId = (Long)iter.next();
				Facility f = facilityDAO.findByFacilityId(facId.toString());
				if (f != null){
					f.setReviewStatusRef(reviewStatusRef);
					f.setLastUpdateTs(new Date());
					f.setLastUpdateUserid(userId);
					searchDAO.saveObject(f);
				}
				if (ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equalsIgnoreCase(reviewStatusRef.getReviewStatusId())){
					ReviewComment reviewComment = new ReviewComment();
					reviewComment.setDescription("Not reviewed yet, but status changed due to facility being in a sewershed that needs to be reviewed.");
					reviewComment.setFacility(f);
					reviewComment.setLastUpdateTs(new Date());
					reviewComment.setLastUpdateUserid(userId);
					searchDAO.saveObject(reviewComment);
				}
			}
		}
		
	}

	/**
	 * Update a FacilityDischage object
	 * @param dForm
	 * @param userId
	 */
	public void updateFacilityDischarge(DischargeForm dForm, CurrentUser user) {

		char presentFlag = 'N';
		char projectedFlag = 'N';

		if (dForm.getStatus().equals(DischargeForm.PRESENT)){ 				
			presentFlag = 'Y';
		} else if (dForm.getStatus().equals(DischargeForm.PROJECTED)) {
			projectedFlag = 'Y';
		} else if (dForm.getStatus().equals(DischargeForm.PRESENT_AND_PROJECTED)) {
			presentFlag = 'Y';
			projectedFlag = 'Y';
		}

		ArrayList costCurveIdList1 = new ArrayList();
		costCurveIdList1.add(new Long(1));
		costCurveIdList1.add(new Long(2));
		costCurveIdList1.add(new Long(4));
		costCurveIdList1.add(new Long(5));
		costCurveIdList1.add(new Long(6));
		costCurveIdList1.add(new Long(10));
		ArrayList costCurveIdList2 = new ArrayList();
		costCurveIdList2.add(new Long(7));
		boolean setProjectedCostCurveRerun = false;
		boolean setPresentCostCurveRerun = false;
		Collection oldDownStreamFacilities = null;
		Collection oldPresentSewershed = null;
		Collection newDownStreamFacilities = null;
		Collection newPresentSewershed = null;

		// Get the FacilityDischarge object
		FacilityDischarge fd = getFacilityDischarge(dForm.getDischargeId());
		Facility facility = fd.getFacilityByFacilityId();
		// Set the selected DischargeMethodRef object
		String dischargeMethodId = dForm.getMethodId();
		DischargeMethodRef dischargeMethodRef = getDischargeMethodRef(dischargeMethodId);

		if (dischargeMethodRef.getDischargeMethodId() == DISCHARGE_TO_ANOTHER_FAC ) {
			long facilityIdDischargeTo = 0;
			if (fd.getFacilityByFacilityIdDischargeTo()!=null)
				facilityIdDischargeTo = fd.getFacilityByFacilityIdDischargeTo().getFacilityId();
			Facility facilityToDischargeTo = 
				facilityDAO.getFacilityByCwnsNbr(dForm.getCwnsNumber());
			
			if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
				String reviewStatus = facility.getReviewStatusRef().getReviewStatusId();
				
				if (reviewStatus != facilityToDischargeTo.getReviewStatusRef().getReviewStatusId()){
					setStatusForSewershed(facility.getFacilityId(),facilityToDischargeTo.getReviewStatusRef().getReviewStatusId(),user.getUserId());
				}
				oldDownStreamFacilities = populationService.getDownstreamFacilitiesListByDischargeType(Long.toString(facility.getFacilityId()), new Integer(populationService.PROJECTED_ONLY).intValue());
				oldPresentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facility.getFacilityId()), new Integer(populationService.PRESENT_ONLY).intValue());
				
				if (fd.getPresentFlag()!=presentFlag
						||fd.getProjectedFlag()!=projectedFlag || facilityIdDischargeTo!=facilityToDischargeTo.getFacilityId()){
					// add cost curve rerun
					setProjectedCostCurveRerun = true;
					setPresentCostCurveRerun = true;
					
				}
				else if (dForm.getStatus() == dForm.PROJECTED && fd.getProjectedFlowPortionPersent() != null &&
						fd.getProjectedFlowPortionPersent().toString()!=dForm.getProjectedFlow()){
					// add cost curve rerun
					setProjectedCostCurveRerun = true;
				}
				else if (fd.getPresentFlowPortionPersent()!= null && fd.getPresentFlowPortionPersent().toString()!=dForm.getPresentFlow()){
					// add cost curve rerun
					setPresentCostCurveRerun = true;
				}
			}
			fd.setFacilityByFacilityIdDischargeTo(facilityToDischargeTo);
		}

		updateFacilityDischargeStatusAndFlow(fd, dForm);

		fd.setLastUpdateUserid(user.getUserId());
		fd.setLastUpdateTs(new Date());
		searchDAO.saveObject(fd);

		if (setProjectedCostCurveRerun){
			
			populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(facility.getFacilityId()));
			newDownStreamFacilities = populationService.getDownstreamFacilitiesListByDischargeType(Long.toString(facility.getFacilityId()), new Integer(populationService.PROJECTED_ONLY).intValue());
			if (newDownStreamFacilities!=null){
				
				Iterator iter1 = newDownStreamFacilities.iterator();
				while (iter1.hasNext()){
					String obj = (String)iter1.next();
					log.debug("newdown"+obj);
					populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(obj));
				}
			}
			if (oldDownStreamFacilities!=null){
				
				Iterator iter1 = oldDownStreamFacilities.iterator();
				while (iter1.hasNext()){
					String obj = (String)iter1.next();
					log.debug("olddown"+obj);
					populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(obj));
				}
			}
		}
		if (setPresentCostCurveRerun){
			newPresentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facility.getFacilityId()), new Integer(populationService.PRESENT_ONLY).intValue());
			if (newPresentSewershed!=null){
				Iterator iter2 = newPresentSewershed.iterator();
				while (iter2.hasNext()){
					populationService.setUpCostCurvesForRerun(costCurveIdList2, (Long)iter2.next());
				}
			}
			if (oldPresentSewershed!=null){
				Iterator iter2 = oldPresentSewershed.iterator();
				while (iter2.hasNext()){
					populationService.setUpCostCurvesForRerun(costCurveIdList2, (Long)iter2.next());
				}
			}
		}

	}

	/**
	 * Using the DischargeForm update the status and Flow of the given 
	 * FacilityDischarge object.
	 * @param fd
	 * @param dForm
	 * @param status
	 */
	private void updateFacilityDischargeStatusAndFlow(FacilityDischarge fd, 
													  DischargeForm dForm) {
		
		String status = dForm.getStatus();
		
		if (status.equals(DischargeForm.PRESENT)) {
			
			fd.setPresentFlag('Y');
			if (!"".equals(dForm.getPresentFlow().trim())) {
				fd.setPresentFlowPortionPersent(new Short(dForm.getPresentFlow().trim()));
			} else {
				fd.setPresentFlowPortionPersent(null);
			}

			fd.setProjectedFlag('N');
			//fd.setProjectedFlowPortionPersent(new Short("0"));

		} else if (status.equals(DischargeForm.PROJECTED)) {

			fd.setPresentFlag('N');
			fd.setPresentFlowPortionPersent(null);

			fd.setProjectedFlag('Y');
			if (!"".equals(dForm.getProjectedFlow().trim())) {
				fd.setProjectedFlowPortionPersent(new Short(dForm.getProjectedFlow().trim()));
			} else {
				fd.setProjectedFlowPortionPersent(null);
			}

		} else if (status.equals(DischargeForm.PRESENT_AND_PROJECTED)) {

			fd.setPresentFlag('Y');
			if (!"".equals(dForm.getPresentFlow().trim())) {
				fd.setPresentFlowPortionPersent(new Short(dForm.getPresentFlow().trim()));
			} else {
				fd.setPresentFlowPortionPersent(null);
			}

			fd.setProjectedFlag('Y');
			if (!"".equals(dForm.getProjectedFlow().trim())) {
				fd.setProjectedFlowPortionPersent(new Short(dForm.getProjectedFlow().trim()));
			} else {
				fd.setProjectedFlowPortionPersent(null);
			}
		}
	}

	
	public void deleteDischargeMethod(long dischargeId, CurrentUser currentUser) {

		FacilityDischarge fd = getFacilityDischarge(dischargeId);
        // if local user mark as deleted
		if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())){
			if (fd.getFeedbackDeleteFlag()=='Y') {
				fd.setFeedbackDeleteFlag('N');
   		    }else{
   		    	fd.setFeedbackDeleteFlag('Y');
   		     }   	    		
   		  searchDAO.saveObject(fd);
		}
		else{
		   searchDAO.removeObject(fd);
		}   
	}
	
	private DischargeMethodRef getDischargeMethodRef(String dischargeMethodId) {
		
		SearchConditions scs  =  new SearchConditions(new SearchCondition("dischargeMethodId", SearchCondition.OPERATOR_EQ, new Long(dischargeMethodId)));
		return (DischargeMethodRef)searchDAO.getSearchObject(DischargeMethodRef.class, scs);
	}

	/**
	 * Given a facilityId determine if there are any other facilities in this facilities' 
	 * sewershed that have a CSO Cost Curve.
	 * @param facilityId
	 * @return
	 */
	public boolean csoCostCurveInSewershed(String facilityId) {
		boolean justThisState = false;
		
		//Collection facilityIds = populationService.getRelatedSewerShedFacilities("1175830");
        // Get the other Facilities within this Sewarshed
		Facility f = facilityDAO.findByFacilityId(facilityId);
	   	Collection facilityIds = 
	   			   populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facilityId), new Integer(populationService.PRESENT_ONLY).intValue());
		Iterator iter = facilityIds.iterator();
		while (iter.hasNext()) {
			Long facId = (Long) iter.next();
			ArrayList aliasArray = new ArrayList();
			aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));			
			aliasArray.add(new AliasCriteria("costCurveRef", "c", AliasCriteria.JOIN_INNER));			
			aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
			SearchConditions scs1 =  new SearchConditions(new SearchCondition("c.costCurveId", SearchCondition.OPERATOR_EQ, new Long(7)));
			scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, facId));
			Collection objects = searchDAO.getSearchList(FacilityCostCurve.class, new ArrayList(), scs1, new ArrayList(), aliasArray);
			if (!objects.isEmpty()){
				justThisState = true;
				return justThisState;
			}
				
		}
		
		return justThisState;
		
	}
	
	
	/**
	 * Get the FacilityDischarge object based on the dischargeId
	 */
	public FacilityDischarge getFacilityDischarge(long dischargeId) {
		return (FacilityDischarge) searchDAO.getObject(FacilityDischarge.class, new Long(dischargeId));
	}
	
	
	private static SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}


    /* set the facility service */
    private FacilityDAO facilityDAO;
    public void setFacilityDAO(FacilityDAO fdao){
       facilityDAO = fdao;    	
    }

    //  set the population service
    private PopulationService populationService;
    public void setPopulationService(PopulationService ps){
    	populationService = ps;    	
    }  
 
    private FlowService flowService;
    public void setFlowService(FlowService fs){
    	flowService = fs;    	
    }
    
    private FacilityTypeService facilityTypeService;
    public void setFacilityTypeService(FacilityTypeService fts){
    	facilityTypeService = fts;    	
    }
   
    private EffluentService effluentService;
    public void setEffluentService(EffluentService es){
    	effluentService = es;
    }
    
	public boolean isPresentDischargeToSurfaceWaters(Long facilityId){
		Collection fds = getFacilityDischarges(facilityId.toString());
		for (Iterator iter = fds.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			if(fd.getPresentFlag()=='Y'){
				if(fd.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_OCEAN ||
				   fd.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_OVERLAND_FLOW_WITH_DISCHARGE ||
				   fd.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_SURFACE_WATERS){
					return true;
				}				   						
			}
			
		}
		return false;
	}

	public boolean isProjectedDischargeToSurfaceWaters(Long facilityId){
		Collection fds = getFacilityDischarges(facilityId.toString());
		for (Iterator iter = fds.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			if(fd.getProjectedFlag()=='Y'){
				if(fd.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_OCEAN ||
				   fd.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_OVERLAND_FLOW_WITH_DISCHARGE ||
				   fd.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_SURFACE_WATERS){
					return true;
				}				   						
			}
			
		}
		return false;
	}
	
	public boolean isPresentEffluentLevel(Long facilityId){
		long effluentLevel = effluentService.getPresentFacilityEffluentLevel(facilityId);
		if(effluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_RAW_DISCHARGE_ID || effluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_PRIMARY_ID ||
				effluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_ADAVANCE_PRIMARY_ID){
			return true;
		}
		else
			return false;
	}
	
	public boolean isProjectedEffluentLevel(Long facilityId){
		long effluentLevel = effluentService.getProjectedFacilityEffluentLevel(facilityId);
		if(effluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_RAW_DISCHARGE_ID || effluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_PRIMARY_ID ||
				effluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_ADAVANCE_PRIMARY_ID){
	    	return true;
		}				   						
		else
		    return false;
	}
	
	public Collection getFacilityEffluents(Long facilityId){
		return null;
	}
	
	public boolean isPresentDischargeSpecified(Long facilityId){
		Collection fds = getFacilityDischarges(facilityId.toString());
		for (Iterator iter = fds.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			if(fd.getPresentFlag()=='Y'){
					return true;
			}
		}
		return false;
	}
	
	public boolean isProjectedDischargeSpecified(Long facilityId){
		Collection fds = getFacilityDischarges(facilityId.toString());
		if(fds==null)return false;
		for (Iterator iter = fds.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			if(fd.getProjectedFlag()=='Y'){
					return true;
			}
		}
		return false;
	}
	
	public boolean isOverLandFlowNoDischargeOrEvaporation(Long facilityId, String presentProjectedFlag){
		Collection ds = getFacilityDischarges(facilityId.toString());
		if(ds==null)return false;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {						
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if("P".equals(presentProjectedFlag)){
				if(fs.getPresentFlag()=='Y' && 
						(fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_OVERLAND_FLOW_NO_DISCHARGE ||
						fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_EVAPORATION)){
					return true;
				}
			}else{
				if(fs.getProjectedFlag()=='Y' && 
						(fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_OVERLAND_FLOW_NO_DISCHARGE ||
						fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_EVAPORATION)){
					return true;
				}
			}	
		}
		return false;		
	}

	public boolean isFacilityHasfaciliyTypeTreatmentPlant(Long facilityId) {
		FacilityType ft = facilityTypeService.getFacilityType(facilityId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT);
		if (ft != null)
			return true;
		else
		return false;
	}
	
	public boolean isFacilityHasPresentFacilityTypeTreatmentPlant(Long facilityId) {
		FacilityType ft = facilityTypeService.getFacilityType(facilityId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT);
		if (ft != null){
			if(ft.getPresentFlag()=='Y'){
				return true;	
			}	
		}
		return false;
	}
	
	public boolean isFacilityHasProjectedFacilityTypeTreatmentPlant(Long facilityId) {
		FacilityType ft = facilityTypeService.getFacilityType(facilityId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT);
		if (ft != null){
			if(ft.getProjectedFlag()=='Y'){
				return true;	
			}	
		}
		return false;
	}
	

	public void setUpCostCurvesForRerun(ArrayList costCurveIdList, String facilityId) {
		    ArrayList aliasArray = new ArrayList();
			aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));			
			aliasArray.add(new AliasCriteria("costCurveRef", "c", AliasCriteria.JOIN_INNER));			
			aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
			SearchConditions scs1 =  new SearchConditions(new SearchCondition("c.costCurveId", SearchCondition.OPERATOR_IN, costCurveIdList));
			scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
			Collection objects = searchDAO.getSearchList(FacilityCostCurve.class, new ArrayList(), scs1, new ArrayList(), aliasArray);
			Iterator iter = objects.iterator();
			while (iter.hasNext()){
				FacilityCostCurve facilityCostCurve = (FacilityCostCurve)iter.next();
				facilityCostCurve.setCurveRerunFlag('Y');
				searchDAO.saveObject(facilityCostCurve);
			}		
	}
	
	public void setUpCostCurveCodesForRerun(ArrayList costCurveCodeList, String facilityId) {
	    ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("costCurveRef", "c", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("c.code", SearchCondition.OPERATOR_IN, costCurveCodeList));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		Collection objects = searchDAO.getSearchList(FacilityCostCurve.class, new ArrayList(), scs1, new ArrayList(), aliasArray);
		Iterator iter = objects.iterator();
		while (iter.hasNext()){
			FacilityCostCurve facilityCostCurve = (FacilityCostCurve)iter.next();
			facilityCostCurve.setCurveRerunFlag('Y');
			searchDAO.saveObject(facilityCostCurve);
		}
	
	}

	public void setFeedBackDeleteFlag(long dischargeId, char feedBackDeleteFlg) {
//		 Get the FacilityDischarge object
		FacilityDischarge fd = getFacilityDischarge(dischargeId);
		if (fd != null){
			fd.setFeedbackDeleteFlag(feedBackDeleteFlg);
			searchDAO.saveObject(fd);
		}
		
	}

	

}
