package gov.epa.owm.mtb.cwns.reviewstatus;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class FacilityReviewStatusForm extends ActionForm {

	private String facilityId;

	private String facilityReviewStatusAct = "none";

	private String currentReviewStatusId = "";

	private String currentFeedbackStatusId = "";

	private Collection reviewStatues = null;

	private Collection feedBackStatues = null;
	
	private String showfeedbackstatus = "N";
	
	private String isFacilitySmallCommunity = "N";
	
	private String selectedReviewStatus;
	
	private String selectedFeedbackStatus;
	
	private String isUpdatable = "Y";
	
	private String currentReviewStatus = "";
	
	private String currentFeedbackStatus = "";
	
	private String userType = "";
	
	private String hasDataAreaErrors = "";
	
	private String isFeedbackStatusUpdatable = "";

	public String getIsFeedbackStatusUpdatable() {
		return isFeedbackStatusUpdatable;
	}

	public void setIsFeedbackStatusUpdatable(String isFeedbackStatusUpdatable) {
		this.isFeedbackStatusUpdatable = isFeedbackStatusUpdatable;
	}

	public String getHasDataAreaErrors() {
		return hasDataAreaErrors;
	}

	public void setHasDataAreaErrors(String hasDataAreaErrors) {
		this.hasDataAreaErrors = hasDataAreaErrors;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCurrentFeedbackStatus() {
		return currentFeedbackStatus;
	}

	public void setCurrentFeedbackStatus(String currentFeedbackStatus) {
		this.currentFeedbackStatus = currentFeedbackStatus;
	}

	public String getCurrentReviewStatus() {
		return currentReviewStatus;
	}

	public void setCurrentReviewStatus(String currentReviewStatus) {
		this.currentReviewStatus = currentReviewStatus;
	}

	public String getIsUpdatable() {
		return isUpdatable;
	}

	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	public String getCurrentFeedbackStatusId() {
		return currentFeedbackStatusId;
	}

	public void setCurrentFeedbackStatusId(String currentFeedbackStatusId) {
		this.currentFeedbackStatusId = currentFeedbackStatusId;
	}

	public String getCurrentReviewStatusId() {
		return currentReviewStatusId;
	}

	public void setCurrentReviewStatusId(String currentReviewStatusId) {
		this.currentReviewStatusId = currentReviewStatusId;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityReviewStatusAct() {
		return facilityReviewStatusAct;
	}

	public void setFacilityReviewStatusAct(String facilityReviewStatusAct) {
		this.facilityReviewStatusAct = facilityReviewStatusAct;
	}

	public Collection getFeedBackStatues() {
		return feedBackStatues;
	}

	public void setFeedBackStatues(Collection feedBackStatues) {
		this.feedBackStatues = feedBackStatues;
	}

	public Collection getReviewStatues() {
		return reviewStatues;
	}

	public void setReviewStatues(Collection reviewStatues) {
		this.reviewStatues = reviewStatues;
	}

	public String getShowfeedbackstatus() {
		return showfeedbackstatus;
	}

	public void setShowfeedbackstatus(String showfeedbackstatus) {
		this.showfeedbackstatus = showfeedbackstatus;
	}

	public String getIsFacilitySmallCommunity() {
		return isFacilitySmallCommunity;
	}

	public void setIsFacilitySmallCommunity(String isFacilitySmallCommunity) {
		this.isFacilitySmallCommunity = isFacilitySmallCommunity;
	}

	public String getSelectedReviewStatus() {
		return selectedReviewStatus;
	}

	public void setSelectedReviewStatus(String selectedReviewStatus) {
		this.selectedReviewStatus = selectedReviewStatus;
	}

	public String getSelectedFeedbackStatus() {
		return selectedFeedbackStatus;
	}

	public void setSelectedFeedbackStatus(String selectedFeedbackStatus) {
		this.selectedFeedbackStatus = selectedFeedbackStatus;
	}
}
