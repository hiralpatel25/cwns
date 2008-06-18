package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.ReviewStatusRefDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CwnsInfoLocationRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatusId;
import gov.epa.owm.mtb.cwns.model.LocationRef;
import gov.epa.owm.mtb.cwns.model.ReviewComment;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper;
import gov.epa.owm.mtb.cwns.reviewstatus.ReviewStatusHelper;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class ReviewStatusRefServiceImpl extends CWNSService implements ReviewStatusRefService {

	
	private ReviewStatusRefDAO reviewStatusRefDAO;
	private SearchDAO searchDAO;
	private PopulationService populationService;
	private FacilityAddressService facilityAddressService; 
	
	public void setReviewStatusRefDAO(ReviewStatusRefDAO dao){
		reviewStatusRefDAO = dao;
	}
	
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	
    // set the population service
    public void setPopulationService(PopulationService ps){
       populationService = ps;    	
    }
    
    public void setFacilityAddressService(FacilityAddressService fas){
    	facilityAddressService = fas;    	
     }

	public Collection findFacilityCountByReviewStatusRef(Collection locationIds) {	
		return reviewStatusRefDAO.findFacilityCountByReviewStatusRef(locationIds);
	}
	
	public Collection findReviewStatusFacilityCountAndSort(String locationTypeId,String locationId) {
		Collection locationIds = new ArrayList();
		if (locationId.equalsIgnoreCase("HQ")) {
			if(UserServiceImpl.LOCATION_TYPE_ID_FEDERAL.equalsIgnoreCase(locationTypeId))
				locationIds = getStates();
			else if(UserServiceImpl.LOCATION_TYPE_ID_REGIONAL.equalsIgnoreCase(locationTypeId))
				locationIds = getStateIdsForRegion(locationId);
		}
		else if(locationId.indexOf("0")!=-1)
			locationIds = getStateIdsForRegion(locationId);
		else
			locationIds.add(locationId);
		
		Collection reviewstatues = findFacilityCountByReviewStatusRef(locationIds);

		Collection sortedList = new ArrayList();
		int count = 0;
		boolean addTotal = false;
		Iterator iter = null;
		if (reviewstatues != null)
			 iter = reviewstatues.iterator();
		 		      
		// HashMap h = new HashMap();
		while (iter.hasNext()) {
			
			Object[] obj = (Object[]) iter.next();

			if (((Byte) obj[2]).intValue() < SURVEY_ENDVALUE) {
				count = count + ((Long) obj[3]).intValue();
			} else {
				if (((Byte) obj[2]).intValue() == FEEDBACK_STARTVALUE)
					sortedList.add(new ReviewStatusHelper("","Feedback Status",""));
				if (!addTotal) {
					sortedList.add(new ReviewStatusHelper("", "Total", (new Long(count)).toString()));
					addTotal = true;
				}
			}
			sortedList.add(new ReviewStatusHelper((String) obj[0], (String) obj[1], ((Long) obj[3]).toString()));
		}
		
		return sortedList;
	}
	
	public ReviewStatusRef findByReviewStatusRefId(String reviewStatusRefId) {
		return reviewStatusRefDAO.getReviewStatusRefByCode(reviewStatusRefId);	
	}
	
	public Entity findFacilityReviewStatus(String facilityId) {
		Collection results = reviewStatusRefDAO.getFacilityReviewStatus(facilityId);
		Iterator iter = results.iterator();
		Entity e = null;
		if (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			e = new Entity(((String)obj[0]),(String)obj[1]);
		}
		return e;
	}

	public Collection getFacilityEntryStatusWithErrors(String facilityId) {
		return reviewStatusRefDAO.getFacilityEntryStatusWithErrors(facilityId);
	}

	public Collection getFederalReviewStatusWithErrors(String facilityId) {
		return reviewStatusRefDAO.getFederalReviewStatusWithErrors(facilityId);
	}

	public Date getLatestReviewCommentTs(String facilityId) {
		return reviewStatusRefDAO.getLatestReviewCommentTs(facilityId);
	}

	public Date getLatestReviewStatusTs(String facilityId) {
		return reviewStatusRefDAO.getLatestReviewStatusTs(facilityId);
	}

	public Entity getFacilityFeedbackStatus(String facilityId) {
		String cwnsNbr = getCWNSNbrByFacilityId(facilityId);
		Collection results = reviewStatusRefDAO.getFacilityFeedbackStatus(cwnsNbr);
		Iterator iter = results.iterator();
		Entity e = null;
		if (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			e = new Entity(((String)obj[0]),(String)obj[1]);
		}
		return e;
	}
   
	public String getCWNSNbrByFacilityId(String facilityId){
		return reviewStatusRefDAO.getCWNSNbrByFacilityId(facilityId);
	}
	
	public boolean hasFeedbackVersion(String facilityId) {
		String cwnsNbr = getCWNSNbrByFacilityId(facilityId);
		return reviewStatusRefDAO.hasFeedbackVersion(cwnsNbr);
	}

	public HashMap getAllReviewStatuses() {
		
		ArrayList columns = new ArrayList();
		columns.add("reviewStatusId");
		columns.add("name");
		//SearchConditions scs = new SearchConditions(new SearchCondition("sortSequence", SearchCondition.OPERATOR_LT, new Byte("55")));
		Collection results = searchDAO.getSearchList(ReviewStatusRef.class, columns, new SearchConditions());
		Iterator iter = results.iterator();
		HashMap map = new HashMap();
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			map.put(obj[0], obj[1]);
		}
		return map;
	}

	/*
	public String isFacilitySmallCommunity(String facilityId) {
		ArrayList columns = new ArrayList();
		columns.add("fd.facility.facilityId");
		SearchConditions scs =  new SearchConditions(new SearchCondition("d.documentTypeRef.documentTypeId", SearchCondition.OPERATOR_EQ, "71"));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facilityId",SearchCondition.OPERATOR_EQ, facilityId));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocuments", "fd", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fd.document", "d", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, new ArrayList(), aliasArray);
		if (results.isEmpty())
		    return "N";
		else
			return "Y";
	}
      */
	public boolean isUpdatable(CurrentUser user, Long facilityId){
		log.debug("user type--"+user.getCurrentRole().getLocationTypeId());
		boolean updatable = false;
		//get the facilityObject
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		if(f==null){
			return false;
		}
		CwnsInfoLocationRef cwnsInfo = (CwnsInfoLocationRef)searchDAO.getSearchObject(CwnsInfoLocationRef.class, new SearchConditions(new SearchCondition("locationId", f.getLocationId())));
		if(cwnsInfo==null){
			log.error("Unable to find cwns info for locationId: " + f.getLocationId());
			return false;
		}
		//get the review status
		ReviewStatusRef rst= f.getReviewStatusRef();
		if(rst ==null){
			return false;
		}
		UserRole ur = user.getCurrentRole();
		if(ur==null){
			return false;
		}
		
			if(UserServiceImpl.LOCATION_TYPE_ID_STATE.equals(ur.getLocationTypeId())){
				if(ur.getLocationId().equals(f.getLocationId())){
				   if(user.isAuth(CurrentUser.FACILITY_UPDATE )|| user.isAuth(CurrentUser.SUBMIT_FOR_FEDERAL_REVIEW)){
					  if(ReviewStatusRefService.STATE_IN_PROGRESS.equals(rst.getReviewStatusId()) ||
					       ReviewStatusRefService.STATE_ASSIGNED.equals(rst.getReviewStatusId()) ||		
					       ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equals(rst.getReviewStatusId()) ||
					       ReviewStatusRefService.DELETED.equals(rst.getReviewStatusId())){
						    Date now = new Date();
						   	if(now.before(cwnsInfo.getDataEntryEndDate())){
						   		updatable = true;
						   	}  
					  }
				   }
					
				}
			}
			else 
				if(UserServiceImpl.LOCATION_TYPE_ID_FEDERAL.equals(ur.getLocationTypeId())){
					Date now = new Date();
					if(user.isAuth(CurrentUser.FACILITY_UPDATE )|| user.isAuth(CurrentUser.SUBMIT_FOR_FEDERAL_REVIEW)
							&&(ReviewStatusRefService.STATE_IN_PROGRESS.equals(rst.getReviewStatusId()) ||
						       ReviewStatusRefService.STATE_ASSIGNED.equals(rst.getReviewStatusId()) ||		
						       ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equals(rst.getReviewStatusId()) ||
						       ReviewStatusRefService.DELETED.equals(rst.getReviewStatusId()))
						       && now.before(cwnsInfo.getDataEntryEndDate())){
							    
					      updatable = true;
					}
					else
						if(user.isAuth(CurrentUser.FEDERAL_REVIEW)){
							if(ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION.equals(rst.getReviewStatusId()) ||
							     ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED.equals(rst.getReviewStatusId()) ||		
							      ReviewStatusRefService.FEDERAL_ACCEPTED.equals(rst.getReviewStatusId())){
								updatable = true;
							 }
						}
								
				}
					
		return updatable;
	}	
	
	public boolean isFeedbackStatusUpdatable(CurrentUser user, Long facilityId){
		boolean updatable = false;
		//get the facilityObject
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		if(f==null){
			return false;
		}
		CwnsInfoLocationRef cwnsInfo = (CwnsInfoLocationRef)searchDAO.getSearchObject(CwnsInfoLocationRef.class, new SearchConditions(new SearchCondition("locationId", f.getLocationId())));
		if(cwnsInfo==null){
			log.error("Unable to find cwns info for locationId: " + f.getLocationId());
			return false;
		}
		//get the review status
		ReviewStatusRef rst= f.getReviewStatusRef();
		if(rst ==null){
			return false;
		}
		UserRole ur = user.getCurrentRole();
		if(ur==null){
			return false;
		}
		
			if(UserServiceImpl.LOCATION_TYPE_ID_STATE.equals(ur.getLocationTypeId())){
				if(ur.getLocationId().equals(f.getLocationId())){
				   if(user.isAuth(CurrentUser.FACILITY_UPDATE )){
					  if(ReviewStatusRefService.STATE_IN_PROGRESS.equals(rst.getReviewStatusId()) ||
					       ReviewStatusRefService.STATE_ASSIGNED.equals(rst.getReviewStatusId()) ||		
					       ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equals(rst.getReviewStatusId())){
						    Date now = new Date();
						   	if(now.before(cwnsInfo.getDataEntryEndDate())){
						   		updatable = true;
						   	}  
					  }
				   }
					
				}
			}
			else 
				if(UserServiceImpl.LOCATION_TYPE_ID_FEDERAL.equals(ur.getLocationTypeId())){
					if(user.isAuth(CurrentUser.FACILITY_UPDATE )){
						 if(ReviewStatusRefService.STATE_IN_PROGRESS.equals(rst.getReviewStatusId()) ||
						     ReviewStatusRefService.STATE_ASSIGNED.equals(rst.getReviewStatusId()) ||		
						      ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equals(rst.getReviewStatusId())){
							    Date now = new Date();
							   	if(now.before(cwnsInfo.getDataEntryEndDate())){
							   		updatable = true;
							   	} 
						 }
					}
				}
				else
					if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(ur.getLocationTypeId())){
						if(user.isAuth(CurrentUser.FACILITY_FEEDBACK)&&f.getVersionCode()=='F'){
							 if((ReviewStatusRefService.LOCAL_IN_PROGRESS.equals(rst.getReviewStatusId()) ||
							     ReviewStatusRefService.LOCAL_ASSIGNED.equals(rst.getReviewStatusId()))){
								    updatable = true;
							 }
						}
					}
					
		return updatable;
	}
	
	public String isFacilitySmallCommunity(String facilityId) {
		int upstreamPresentResPopulation = 0;
		int upstreamPresentNonResPopulation = 0;
		double totalPopulation = 0;
		PopulationHelper ph = (PopulationHelper)populationService.getUpStreamFacilitiesPopulationTotal(facilityId);
    	if(ph!=null)
    	{
    	  upstreamPresentResPopulation = ph.getPresentResPopulation();
    	  upstreamPresentNonResPopulation = ph.getPresentNonResPopulation();
    	}
    	double presentReceivingPopulation = populationService.getTotalPresentReceivingPopulation(new Long(facilityId));
    	double presentTotalClusteredPop = populationService.getTotalPresentDecentralizedPopulation(new Long(facilityId));
		double presentTotalOWTSPop = populationService.getTotalPresentIndividualSewageDisposalSystemPopulation(new Long(facilityId));
    	totalPopulation = presentReceivingPopulation+upstreamPresentResPopulation+upstreamPresentNonResPopulation+presentTotalClusteredPop+presentTotalOWTSPop;
		if (totalPopulation>0 && totalPopulation<10000)
		    return "Y";
		else
			return "N";
	}
	
	public boolean isFacilityInCorrectStatus(String facilityId){
		boolean status = false;
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		Facility f = (Facility)searchDAO.getSearchObject(Facility.class, scs);
		if (f!=null){
			if (FEDERAL_REVIEW_REQUESTED.equalsIgnoreCase(f.getReviewStatusRef().getReviewStatusId()) || 
				  FEDERAL_REVIEW_CORRECTION.equalsIgnoreCase(f.getReviewStatusRef().getReviewStatusId()) ||
				  FEDERAL_ACCEPTED.equalsIgnoreCase(f.getReviewStatusRef().getReviewStatusId())){
				status = true;
			}
		}
		return status;
	}
	
	public void saveReviewStatus(String facilityId, String currentReviewStatusId, String selectedReviewStatus, CurrentUser user) {
		if (ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED.equalsIgnoreCase(currentReviewStatusId) 
				&& ReviewStatusRefService.STATE_REQUESTED_RETURN.equalsIgnoreCase(selectedReviewStatus)){
			Date SIP_lastUpdateTs = null;
			Date SAS_lastUpdateTS = null;
			ArrayList columns = new ArrayList();
			columns.add("id.lastUpdateTs");
			SortCriteria sortCriteria = new SortCriteria("id.lastUpdateTs", SortCriteria.ORDER_DECENDING);
			ArrayList sortArray = new ArrayList();
			sortArray.add(sortCriteria);	
			SearchConditions scs =  new SearchConditions(new SearchCondition("reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_EQ, "SIP"));
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId",SearchCondition.OPERATOR_EQ, new Long(facilityId)));
			SearchConditions scs1 =  new SearchConditions(new SearchCondition("reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_EQ, "SAS"));
			scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId",SearchCondition.OPERATOR_EQ, new Long(facilityId)));
			Collection results = searchDAO.getSearchList(FacilityReviewStatus.class, columns, scs, sortArray);
			Collection results1 = searchDAO.getSearchList(FacilityReviewStatus.class, columns, scs1, sortArray);
			Iterator iter = results.iterator();
			Iterator iter1 = results1.iterator();
			if (iter.hasNext() && iter1.hasNext()){
				SIP_lastUpdateTs = (Date)iter.next();
				SAS_lastUpdateTS = (Date)iter1.next();
				if (SIP_lastUpdateTs.after(SAS_lastUpdateTS))
					selectedReviewStatus = "SIP";
				else
					selectedReviewStatus = "SAS";
			}
			else {
				if (iter.hasNext())
					selectedReviewStatus = "SIP";
				else if (iter1.hasNext())
					selectedReviewStatus = "SAS";
			}	
			//Create new status
			createNewStatus(facilityId,selectedReviewStatus,user.getUserId());


		}
		else
			if (ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION.equalsIgnoreCase(currentReviewStatusId)
					&& ReviewStatusRefService.STATE_REQUESTED_RETURN.equalsIgnoreCase(selectedReviewStatus)){
				selectedReviewStatus = "SCR";
				//Create new status
				createNewStatus(facilityId,selectedReviewStatus,user.getUserId());
			}
			else
				if (ReviewStatusRefService.DELETED.equalsIgnoreCase(currentReviewStatusId)
						&& ReviewStatusRefService.RESTORE.equalsIgnoreCase(selectedReviewStatus)){
					/*
					ArrayList columns = new ArrayList();
					columns.add("reviewStatusRef.reviewStatusId");
					SortCriteria sortCriteria = new SortCriteria("id.lastUpdateTs", SortCriteria.ORDER_DECENDING);
					ArrayList sortArray = new ArrayList();
					sortArray.add(sortCriteria);
					Long facNum = new Long(facilityId);
					SearchConditions scs =  new SearchConditions(new SearchCondition("reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_NOT_EQ, "DE"));
					scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId",SearchCondition.OPERATOR_EQ, facNum));
					Collection results = searchDAO.getSearchList(FacilityReviewStatus.class, columns, scs, sortArray);
					 */
					Collection results = reviewStatusRefDAO.getFacilityReviewStatusHistory(facilityId);

					Iterator iter = results.iterator();
					if (iter.hasNext())
						selectedReviewStatus = (String)iter.next();

					//System.out.println("reviewstatus size---"+results.size());
					//Create new status
					createNewStatus(facilityId,selectedReviewStatus,user.getUserId());
				}
				else if(ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED.equalsIgnoreCase(selectedReviewStatus)||
						ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION.equalsIgnoreCase(selectedReviewStatus)){
					// if facility in the sewershed, change the review status for all the facilities in the sewershed
					Collection facilities = populationService.getRelatedSewerShedFacilities(new Long(facilityId)); 
					if(facilities!=null && facilities.size()>1){
						Collection facIds = getSurveyFacilitiesOnly(facilities);
						if(facIds!=null){
							Iterator iter = facIds.iterator();
							while(iter.hasNext()){
								String facNum = Long.toString(((Facility)iter.next()).getFacilityId());
								createNewStatus(facNum,selectedReviewStatus,user.getUserId());

							}
						}
					}
					else //if facility is not in the sewershed, change the status for current facility
						createNewStatus(facilityId,selectedReviewStatus,user.getUserId());
				}
				else if(UserServiceImpl.LOCATION_TYPE_ID_FEDERAL.equals(user.getCurrentRole().getLocationTypeId())){
					if(ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equalsIgnoreCase(selectedReviewStatus)){
						// if facility in the sewershed, change the review status for all the facilities in the sewershed
						Collection facilities = populationService.getRelatedSewerShedFacilities(new Long(facilityId)); 
						if(facilities!=null && facilities.size()>1){
							Collection facIds = getSurveyFacilitiesOnly(facilities);
							if(facIds!=null){
								Iterator iter = facIds.iterator();
								while(iter.hasNext()){
									String facNum = Long.toString(((Facility)iter.next()).getFacilityId()); 
									createNewStatus(facNum,selectedReviewStatus,user.getUserId());
									// if no correction needed for the facility, create a facility review comment
									if (getFederalReviewStatusWithErrors(facNum)==null || getFederalReviewStatusWithErrors(facNum).size()==0 ){
										ReviewComment reviewComment = new ReviewComment();
										SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
										Facility f = (Facility)searchDAO.getSearchObject(Facility.class, scs);
										reviewComment.setDescription("This facility does not require a correction. The status has changed to State " +
										"Correction Requested due to another facility(s) in the sewershed (present or projected) requiring a correction." );
										reviewComment.setFacility(f);
										reviewComment.setLastUpdateTs(new Date());
										reviewComment.setLastUpdateUserid(user.getUserId());
										searchDAO.saveObject(reviewComment);
									}
								}
							}
						}
						else //if facility is not in the sewershed, change the status for current facility
							createNewStatus(facilityId,selectedReviewStatus,user.getUserId());
					}
					else if(ReviewStatusRefService.FEDERAL_ACCEPTED.equalsIgnoreCase(selectedReviewStatus)){
//						if facility in the sewershed, change the review status for all the facilities in the sewershed
						Collection facilities = populationService.getRelatedSewerShedFacilities(new Long(facilityId)); 
						if(facilities!=null && facilities.size()>1){
							Collection facIds = getSurveyFacilitiesOnly(facilities);
							if(facIds!=null){
								Iterator iter = facIds.iterator();
								while(iter.hasNext()){
									String facNum = Long.toString(((Facility)iter.next()).getFacilityId());
									createNewStatus(facNum,selectedReviewStatus,user.getUserId());
								}
							}
						}
						else //if facility is not in the sewershed, change the status for current facility
							createNewStatus(facilityId,selectedReviewStatus,user.getUserId());
					}
					else 
						if (!currentReviewStatusId.equalsIgnoreCase(selectedReviewStatus)){
							// Create new status
							createNewStatus(facilityId,selectedReviewStatus,user.getUserId());
						}
				}
				else 
					if (!currentReviewStatusId.equalsIgnoreCase(selectedReviewStatus)){
						// Create new status
						createNewStatus(facilityId,selectedReviewStatus,user.getUserId());
					}

	}
	
	private void createNewStatus(String facilityId, String selectedReviewStatus, String user){
		FacilityReviewStatus facilityReviewStatus = new FacilityReviewStatus();
		FacilityReviewStatusId id = new FacilityReviewStatusId(new Long(facilityId).longValue(), selectedReviewStatus, new Date());
		facilityReviewStatus.setId(id);
		facilityReviewStatus.setLastUpdateUserid(user);
		reviewStatusRefDAO.saveObject(facilityReviewStatus);
		//SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
	    Facility f  = (Facility)searchDAO.getObject(Facility.class, new Long(facilityId)); 
	    if (f != null){
	    	ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
	    	reviewStatusRef.setReviewStatusId(selectedReviewStatus);
	    	f.setReviewStatusRef(reviewStatusRef);
	    	f.setLastUpdateTs(new Date());
	    	f.setLastUpdateUserid(user);
	    	reviewStatusRefDAO.saveObject(f);
	    }
	}
	
	public String getSmallcomunityFacilities(String facilityId){
		String smallComunityfacilities = "";
		Collection facilities = populationService.getRelatedSewerShedFacilities(new Long(facilityId));
		if (facilities != null && facilities.size()>1){
			//Get S Facilities only
			SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_IN, facilities));
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character('S')));
			facilities  = searchDAO.getSearchList(Facility.class, scs);	
		Iterator iter = facilities.iterator();
		while(iter.hasNext()){
			String facNum = Long.toString(((Facility)iter.next()).getFacilityId());
			if ("Y".equalsIgnoreCase(isFacilitySmallCommunity(facNum))){
				SearchConditions scs1 =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
				Facility f = (Facility)searchDAO.getSearchObject(Facility.class, scs1);
				smallComunityfacilities = smallComunityfacilities+f.getCwnsNbr()+" - "+f.getName()+"\\n";
			}
		}
		}
		return smallComunityfacilities;
	}
	
	public String getFacilitiesWithFeedbackCopy(String facilityId){
		String facilitiesWithFeedbackCopy = "";
		Collection facilities = populationService.getRelatedSewerShedFacilities(new Long(facilityId));
		if (facilities != null && facilities.size()>1){
			//Get S Facilities only
			SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_IN, facilities));
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character('S')));
			facilities  = searchDAO.getSearchList(Facility.class, scs);	
			
		Iterator iter = facilities.iterator();
		while(iter.hasNext()){
			String facNum = Long.toString(((Facility)iter.next()).getFacilityId());
			if (hasFeedbackVersion(facNum)){
				String cwnsNbr = getCWNSNbrByFacilityId(facNum);
				SearchConditions scs1 =  new SearchConditions(new SearchCondition("cwnsNbr", SearchCondition.OPERATOR_EQ, cwnsNbr));
				scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character('F')));
				Facility f = (Facility)searchDAO.getSearchObject(Facility.class, scs1);
				if(ReviewStatusRefService.LOCAL_ASSIGNED.equalsIgnoreCase(f.getReviewStatusRef().getReviewStatusId())
						|| ReviewStatusRefService.LOCAL_IN_PROGRESS.equalsIgnoreCase(f.getReviewStatusRef().getReviewStatusId()))
				  facilitiesWithFeedbackCopy = facilitiesWithFeedbackCopy+f.getCwnsNbr()+" - "+f.getName()+"\\n";
			}
		}
		}
		return facilitiesWithFeedbackCopy;
	}

	public void saveFeedbackReviewStatus(String facilityId, String currentReviewStatusId, String selectedReviewStatus, CurrentUser user) {
		String cwnsNbr = getCWNSNbrByFacilityId(facilityId);
		SearchConditions scs =  new SearchConditions(new SearchCondition("cwnsNbr", SearchCondition.OPERATOR_EQ, cwnsNbr));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character('F')));
		Facility f = (Facility)searchDAO.getSearchObject(Facility.class, scs);
		if("".equalsIgnoreCase(currentReviewStatusId) && "LAS".equalsIgnoreCase(selectedReviewStatus)){
			createFeedbackFacility(facilityId,user.getUserId());
		}
		else if(("LAS".equalsIgnoreCase(currentReviewStatusId) || "LIP".equalsIgnoreCase(currentReviewStatusId) ||
				"SRR".equalsIgnoreCase(currentReviewStatusId)|| "SAP".equalsIgnoreCase(currentReviewStatusId)) &&
				ReviewStatusRefService.REVOKED.equalsIgnoreCase(selectedReviewStatus)){
			if(f!=null){
				//remove the lat/long from the WLV
				facilityAddressService.removeFeatures(new Long(f.getFacilityId()));
				f.setVersionCode('A');
			    searchDAO.saveObject(f);
			}    
		}else if((ReviewStatusRefService.STATE_REVIEW_REQUESTED.equalsIgnoreCase(currentReviewStatusId) || ReviewStatusRefService.STATE_APPLIED.equalsIgnoreCase(currentReviewStatusId)) &&
				ReviewStatusRefService.LOCAL_ASSIGNED.equalsIgnoreCase(selectedReviewStatus)){
			f.setVersionCode('A');
			f.setLastUpdateTs(new Date());
			f.setLastUpdateUserid(user.getUserId());
		    searchDAO.saveObject(f);
		    searchDAO.flushAndClearCache();
		    createFeedbackFacility(facilityId,user.getUserId());
		}
		else {
			if (!currentReviewStatusId.equalsIgnoreCase(selectedReviewStatus)){
				  // Create new status
				  createNewStatus(Long.toString(f.getFacilityId()),selectedReviewStatus,user.getUserId());
				}
		}
		
	}

	private void createFeedbackFacility(String facilityId, String userId) {
		reviewStatusRefDAO.createFeedbackFacility(facilityId, userId);
		
	}
	private Collection getSurveyFacilitiesOnly(Collection facIds){
			//Get S Facilities only
			SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_IN, facIds));
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character('S')));
			return searchDAO.getSearchList(Facility.class, scs);
	}
	
	/**
	 * Return a list of user states based on the Location and
	 * LocationType of the CurrentUser object passed in.
	 */
	public Collection getLocationRefs(CurrentUser adminUser) {

		String userType = adminUser.getCurrentRole().getLocationTypeId();
		String userLocationId = adminUser.getCurrentRole().getLocationId();

		SortCriteria sortCriteria = new SortCriteria("name",
				SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		
		ArrayList columns = new ArrayList();
		columns.add("locationId");
		columns.add("name");

		SearchConditions scs = null;
		Class implClass = null;
		if ("Regional".equals(userType.trim())) {
			scs = new SearchConditions(new SearchCondition("epaRegionCode",
					SearchCondition.OPERATOR_EQ, new Byte(userLocationId)));
			implClass = StateRef.class;

		} else { // Federal
			scs = new SearchConditions(new SearchCondition("locationId",
					SearchCondition.OPERATOR_NOT_EQ, "HQ"));
			implClass = LocationRef.class;
		}

		Collection locationRefs = searchDAO.getSearchList(implClass,
				columns, scs, sortArray);
		ArrayList states = new ArrayList(); 
        if(locationRefs!=null){
          Iterator iter = locationRefs.iterator();
           	while(iter.hasNext()){
    			Object[] obj = (Object[])iter.next();
    			Entity e = new Entity(((String)obj[0]),(String)obj[1]);
    			states.add(e);
    		}
        }	
		return states;
	}
	
	public Collection getDefaultSearchConditions(String locationTypeId,String locationId) {
		Collection states = new ArrayList();
		
		if (UserService.LOCATION_TYPE_ID_REGIONAL.equals(locationTypeId)) {
			states = getStateIdsForRegion(locationId);
		} else if (UserService.LOCATION_TYPE_ID_STATE.equals(locationTypeId) ||
				   UserService.LOCATION_TYPE_ID_LOCAL.equals(locationTypeId)) {
			states.add(locationId);
		} else { 
			states.add("DC");
		}
		return states;
	}
	
	/** 
	 * Return the Collection of states (locationIds) associated with the given region.  
	 * @param regionCode
	 * @return Collection of locationIds associated with the given region code.
	 */
	private Collection getStateIdsForRegion(String regionCode){
		ArrayList columns = new ArrayList();
		columns.add("locationId");
		SearchConditions scs =  new SearchConditions(new SearchCondition("epaRegionCode", SearchCondition.OPERATOR_EQ, new Byte(regionCode)));
		return (Collection)searchDAO.getSearchList(StateRef.class, columns, scs);
	}
	
	private Collection getStates(){
		ArrayList columns = new ArrayList();
		columns.add("locationId");
		return (Collection)searchDAO.getSearchList(StateRef.class, columns, new SearchConditions());
		
    }
}
