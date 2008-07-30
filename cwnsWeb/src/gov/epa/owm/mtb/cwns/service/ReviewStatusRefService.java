package gov.epa.owm.mtb.cwns.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;

public interface ReviewStatusRefService {

	public static final String FEDERAL_REVIEW_REQUESTED = "FRR";
	public static final String STATE_ASSIGNED = "SAS";
	public static final String STATE_IN_PROGRESS = "SIP";
	public static final String STATE_CORRECTION_REQUESTED = "SCR";
	public static final String FEDERAL_REVIEW_CORRECTION ="FRC";
	public static final String FEDERAL_ACCEPTED = "FA";
	public static final String DELETED = "DE";
	public static final String STATE_REVIEW_REQUESTED = "SRR";
	public static final String STATE_APPLIED = "SAP";
	public static final String STATE_REQUESTED_RETURN = "SRRT";
	public static final String REVOKED = "REV";
	public static final String RESTORE = "RES";
	
	public static final String LOCAL_ASSIGNED           = "LAS";
	public static final String LOCAL_IN_PROGRESS        = "LIP";
	
	public int SURVEY_ENDVALUE = 49;
	public int FEEDBACK_STARTVALUE =50;
	
	public Collection findFacilityCountByReviewStatusRef(Collection locationIds);
	public Collection findReviewStatusFacilityCountAndSort(String locationTypeId,String locationId);
	public Collection getLocationRefs(CurrentUser adminUser);
	public Collection getDefaultSearchConditions(String locationTypeId,String locationId);
	
	
	public ReviewStatusRef findByReviewStatusRefId(String reviewStatusRefId);
	public boolean isUpdatable(CurrentUser user, Long facilityId);
	public Entity findFacilityReviewStatus(String facilityId);
	public Collection getFacilityEntryStatusWithErrors(String facilityId);
	public Collection getFederalReviewStatusWithErrors(String facilityId);
	public Date getLatestReviewCommentTs(String facilityId);
	public Date getLatestReviewStatusTs(String facilityId);
	public Entity getFacilityFeedbackStatus(String facilityId);
	public boolean hasFeedbackVersion(String facilityId);
	public HashMap getAllReviewStatuses();
	public String isFacilitySmallCommunity(String facilityId);
	public boolean isFacilityInCorrectStatus(String facilityId);
	public void saveReviewStatus(String facilityId, String currentReviewStatusId, String selectedReviewStatus, CurrentUser user);
	public String getCWNSNbrByFacilityId(String facilityId);
	// Get Small Community Facilities in the Sewershed
	public String getSmallcomunityFacilities(String facilityId);
    // Get facilities in the Sewershed which has feedback copy with review status of LAS or LIP
	public String getFacilitiesWithFeedbackCopy(String facilityId);
	public boolean isFeedbackStatusUpdatable(CurrentUser user, Long facilityId);
	public void saveFeedbackReviewStatus(String facilityId, String currentReviewStatusId, String selectedReviewStatus, CurrentUser user);
}
