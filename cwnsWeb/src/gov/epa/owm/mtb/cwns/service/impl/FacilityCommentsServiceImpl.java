package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.FacilityCommentsDAO;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.DataAreaRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatusId;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.service.FacilityCommentsService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.UserService;

import java.util.Date;
import java.util.List;

public class FacilityCommentsServiceImpl extends CWNSService implements FacilityCommentsService {
	
	public static final String DATEFORMAT =  "dd-MMM-yy HH:mm";
	
	private FacilityCommentsDAO facilityCommentsDAO;
	
	public void setFacilityCommentsDAO(FacilityCommentsDAO dao){
		facilityCommentsDAO = dao;
	}	
	
    public List findFacilityCommentsByFacilityIdAndDataAreaName(long facilityId, String dataAreaName)
    {
    	return facilityCommentsDAO.findFacilityCommentsByFacilityIdAndDataAreaName(facilityId, dataAreaName);
    }
    
	public int countFacilityComments(long facilityId, String dataAreaName)
	{
		return facilityCommentsDAO.countFacilityComments(facilityId, dataAreaName);
	}
	
	public void addFacilityComments(long facilityId, String dataAreaName, String comments, String User)
	{
		facilityCommentsDAO.addFacilityComments(facilityId, dataAreaName, comments, User);
	}
	
	public void updateFacilityComments(long facilityCommentId, String comments, String User)
	{
		facilityCommentsDAO.updateFacilityComments(facilityCommentId, comments, User);
	}

	public void deleteFacilityComments(long facilityId, String dataAreaName)
	{
		facilityCommentsDAO.deleteFacilityComments(facilityId, dataAreaName);
	}

	public void deleteFacilityComments(long facilityCommentId)
	{
		facilityCommentsDAO.deleteFacilityComments(facilityCommentId);
	}
	
	public boolean isFeedbackVersionValid(long facilityId, String dataAreaName)
	{
			return facilityCommentsDAO.isFeedbackVersionValid(facilityId, dataAreaName);
	}

	public List findFacilityCommentsByFacilityIdAndDataAreaId(long facilityId, Long dataAreaId) {
		return facilityCommentsDAO.findFacilityCommentsByFacilityIdAndDataAreaName(facilityId, dataAreaId);
	}
	
	public DataAreaRef getDataAreaRefFromName(String dataAreaName){
		return facilityCommentsDAO.getDataAreaRefFromName(dataAreaName);
	}
	
	public boolean displayFeedbackFacilitySatisfied(long facilityId, long dataAreaId, CurrentUser user){
		boolean rulesSatisfied = false;
		Facility s_facility = facilityDAO.findByFacilityId(Long.toString(facilityId));
		Facility f_facility = null;
		
		if (s_facility!=null && s_facility.getVersionCode()=='S'){		
			//User .access = 'Facility Update' or 'Submit for Review'
			if(user.isAuth(CurrentUser.FACILITY_UPDATE) || user.isAuth(CurrentUser.SUBMIT_FOR_FEDERAL_REVIEW)){
				//AND (user.location_type = State and facility.location_id = user.location OR user.location_type = Federal )
				String userLocationTypeId = user.getCurrentRole().getLocationTypeId();
				String userLocationId = user.getCurrentRole().getLocationId();
				
				if(userLocationTypeId.equals(UserService.LOCATION_TYPE_ID_STATE) &&	
						(s_facility!=null && s_facility.getLocationId().equals(userLocationId)) ||
						userLocationTypeId.equals(UserService.LOCATION_TYPE_ID_FEDERAL)){
					String cwnsNbr = s_facility.getCwnsNbr();
					f_facility = getFacility(cwnsNbr, 'F');
					
					//'F' version of the 'S' facility exist AND	'F' facility.review_status = 'SRR'
					if (f_facility!=null &&
							f_facility.getReviewStatusRef().getReviewStatusId().equals(ReviewStatusRefService.STATE_REVIEW_REQUESTED)){				
						//F Facility_entry_status.data_area_last_update_ts (where data_area = 1) > 
						//latest F facility_review_status.last_update_ts where review_status_type = Local Assigned
						long f_facilityId = f_facility.getFacilityId();
						SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
								new FacilityEntryStatusId(f_facilityId, dataAreaId)));
						Object obj = searchDAO.getSearchObject(FacilityEntryStatus.class, scs);
						if (obj !=null){
							FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)obj;
							Date f_dataAreaLastUpdated = facilityEntryStatus.getDataAreaLastUpdateTs();

							//latest F facility_review_status.last_update_ts where review_status_type = Local Assigned
							Date f_reviewStatusLastUpdate = null;
							SearchConditions scs1 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, 
														new Long(f_facilityId)));
							scs1.setCondition(new SearchCondition("id.reviewStatusId", SearchCondition.OPERATOR_EQ, 
														ReviewStatusRefService.LOCAL_ASSIGNED));							
							Object objFacilityReviewStatus = searchDAO.getSearchObject(FacilityReviewStatus.class, scs1);
							if (objFacilityReviewStatus!=null){
								f_reviewStatusLastUpdate = ((FacilityReviewStatus)objFacilityReviewStatus).getId().getLastUpdateTs();
							}
							
							if(f_reviewStatusLastUpdate!=null){			
								if (f_dataAreaLastUpdated!=null && f_dataAreaLastUpdated.after(f_reviewStatusLastUpdate)){									
									//AND S facility.review_status = 'SAS' or 'SIP' or 'SCR'
									ReviewStatusRef s_reviewStatusRef = s_facility.getReviewStatusRef();
									if (s_reviewStatusRef!=null){
										String s_reviewStatusId = s_reviewStatusRef.getReviewStatusId();
										if(s_reviewStatusId.equals(ReviewStatusRefService.STATE_ASSIGNED) ||
											s_reviewStatusId.equals(ReviewStatusRefService.STATE_IN_PROGRESS) ||
											s_reviewStatusId.equals(ReviewStatusRefService.STATE_CORRECTION_REQUESTED)){
											rulesSatisfied = true;
										}
									}
								}
							}
						}
					}
				}			
			}
		}
		return rulesSatisfied;
	}
	
	private Facility getFacility(String cwnsNbr, char versionCode){
		Facility f_facility = null;
		SearchConditions scs = new SearchConditions(new SearchCondition("cwnsNbr", SearchCondition.OPERATOR_EQ, cwnsNbr));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character(versionCode)));
		Object objFeedbackFacility = searchDAO.getSearchObject(Facility.class, scs);
		if (objFeedbackFacility!=null){
			f_facility = (Facility)objFeedbackFacility;
		}
		return f_facility;
	}
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	
	private FacilityDAO facilityDAO;
	public void setFacilityDAO(FacilityDAO facilityDAO) {
		this.facilityDAO = facilityDAO;
	}	
}
