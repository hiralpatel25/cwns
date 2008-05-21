package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.CwnsUserSettingDAO;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.dao.ReviewCommentsDAO;
import gov.epa.owm.mtb.cwns.dao.ReviewStatusRefDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CwnsInfoLocationRef;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocation;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListHelper;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.ReviewCommentsService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.UserService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class ReviewCommentsServiceImpl extends CWNSService implements ReviewCommentsService {
	
	public static final String DATEFORMAT =  "MM/dd/yyyy HH:mm";
	
	private ReviewCommentsDAO reviewCommentsDAO;
	
	public void setReviewCommentsDAO(ReviewCommentsDAO dao){
		reviewCommentsDAO = dao;
	}	

	private UserService userService;

    public void setUserService(UserService us) {
		userService = us;
	}

	private SearchDAO searchDAO;

	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
    
    
	public Collection findReviewCommentsByFacilityId(long facilityId, int startIndex, int maxResultSize) 
	{	
		return reviewCommentsDAO.findReviewCommentsByFacilityId(facilityId, startIndex, maxResultSize);
	}
	
    public long findFacilityIdByFacilityNumberAndVersionCode(String facilityNumber, String facilityVersionCode)
    {
		return reviewCommentsDAO.findFacilityIdByFacilityNumberAndVersionCode(facilityNumber, facilityVersionCode);    	
    }
	

	public int countReviewComments(long facilityId) {
		return reviewCommentsDAO.countReviewComments(facilityId);
	}

	public void addComments(long facilityId, String comments, String User)
	{
		reviewCommentsDAO.addComments(facilityId, comments, User);		
	}

	public long fetchFacilityIdByVersionCode(long facilityId, String srcVersionCode, String targetVersionCode)
	{
		return reviewCommentsDAO.fetchFacilityIdByVersionCode(facilityId, srcVersionCode, targetVersionCode);
	}
	
	public Collection getReviewCommentsDereferencedResults(long facilityId, int startIndex, int maxResultSize){

		Collection reviewcomments = findReviewCommentsByFacilityId(facilityId, startIndex, maxResultSize);
		Collection reviewCommentsListHelpers = new ArrayList();
    	Iterator iter = reviewcomments.iterator();
    	while ( iter.hasNext() ) {
    	    Map map = (Map) iter.next();
            String shortComment = (String) map.get("description");
            
            if(shortComment != null)
            {
            	shortComment = org.apache.commons.lang.StringEscapeUtils.escapeHtml(shortComment);
            }

            String longComment = shortComment;

            if(shortComment != null && shortComment.length() > ReviewCommentsService.SHORT_COMMENT_LENGTH)
            	{
            		shortComment = shortComment.substring(0, ReviewCommentsService.SHORT_COMMENT_LENGTH - 1);
            	}
            else
            	longComment = null;
            
            SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
            
            String lastUpdateStr = df.format((Timestamp) map.get("lastUpdateTs"));
            
    	    ReviewCommentsListHelper revCom = new ReviewCommentsListHelper(
    	    		shortComment,
    	    		longComment,
    	    		((Long) map.get("reviewCommentId")).toString(),
    	    		userService.findUserNameByUserId((String) map.get("lastUpdateUserid")),
    	    		lastUpdateStr);

			reviewCommentsListHelpers.add(revCom);
		}		
		return reviewCommentsListHelpers;	
	}

	public boolean isUpdatable(CurrentUser user, Long facilityId){
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
		
		if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(ur.getLocationTypeId())){
			//check if the facility is authorised on survey facility Id
			if(!isUserAssociatedWithFacility(user, ur, facilityId)){
				return false;
			}
			else
				if(user.isAuth(CurrentUser.FACILITY_FEEDBACK)||user.isAuth(CurrentUser.SUBMIT_FOR_STATE_REVIEW)){
					if(ReviewStatusRefService.LOCAL_ASSIGNED.equals(rst.getReviewStatusId()) ||
							ReviewStatusRefService.LOCAL_IN_PROGRESS.equals(rst.getReviewStatusId()) ){
						Date now = new Date();
						if(now.before(cwnsInfo.getDataEntryEndDate())){
						    updatable = true;
						}
					}
				}					
		}
		else
			if(UserServiceImpl.LOCATION_TYPE_ID_STATE.equals(ur.getLocationTypeId())){
				if(ur.getLocationId().equals(f.getLocationId())){
					if(user.isAuth(CurrentUser.FACILITY_UPDATE )|| user.isAuth(CurrentUser.SUBMIT_FOR_FEDERAL_REVIEW)){
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
					if(user.isAuth(CurrentUser.FEDERAL_REVIEW)){
						updatable = true;
					}
					else if(user.isAuth(CurrentUser.FACILITY_UPDATE )|| user.isAuth(CurrentUser.SUBMIT_FOR_FEDERAL_REVIEW)){
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
					
		return updatable;
	}
	
	public boolean isUserAssociatedWithFacility(CurrentUser user, UserRole ur,Long facilityId){
		//get the facility restriction information
		CwnsUserLocation cul = userService.getCwnsUserLocation(user.getUserId(), user.getCurrentRole().getLocationTypeId(), user.getCurrentRole().getLocationId());
		if(cul.getLimitedFacilitiesFlag()!='Y'){
			return true;
		}
		//get the list of facilities
		Collection restrictedFacilities = userService.getUserLocationRestrictedFacilitiesId (facilityId, user.getUserId(), ur.getLocationTypeId(), ur.getLocationId());
		//check if the facility is listed in the list of
		if(restrictedFacilities.contains(facilityId)){
			return true;
		}
		return false; 		
	}
}
