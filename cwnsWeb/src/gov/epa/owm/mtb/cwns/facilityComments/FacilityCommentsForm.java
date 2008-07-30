package gov.epa.owm.mtb.cwns.facilityComments;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

import gov.epa.owm.mtb.cwns.model.Facility;

import org.apache.struts.action.ActionForm;


public class FacilityCommentsForm extends ActionForm {

	public static final long INITIAL_LONG_VALUE = -999;
//	public static final String STATE = UserServiceImpl.LOCATION_TYPE_ID_STATE;
		
	private String facilityCommentAct = "none";
	private int numOfComments = 0;
	private String inputComments;
	private long facilityId;
	private long facilityCommentId = INITIAL_LONG_VALUE;
	private String dataAreaName;
	private long dataTypeId = INITIAL_LONG_VALUE;
	private String userType;
	private boolean isFacility = true;
	private String feedbackVersionFacilityId = null;
	private String userId = null;
	private String lastUpdatedTs = null;
	private String isUpdatable = "N";

	/**
	 * @return the isFacility
	 */
	public boolean isFacility() {
		return isFacility;
	}

	/**
	 * @param isFacility the isFacility to set
	 */
	public void setFacility(boolean isFacility) {
		this.isFacility = isFacility;
	}

	public String toString()
	{
		return 
		"\r\n numOfComments: " + numOfComments + 
		"\r\n facilityCommentAct: " + facilityCommentAct +
		"\r\n inputComments: " + inputComments + 
		"\r\n facilityId: " + facilityId + 
		"\r\n facilityCommentId: " + facilityCommentId + 
		"\r\n dataTypeName: " + dataAreaName + 
		"\r\n dataTypeId: " + dataTypeId + 
		"\r\n userType: " + userType +
		"\r\n isFacility: " + isFacility + 
		"\r\n feedbackVersionFacilityId: " + feedbackVersionFacilityId;		
 	}

	/**
	 * @return the dataTypeId
	 */
	public long getDataTypeId() {
		return dataTypeId;
	}

	/**
	 * @param dataTypeId the dataTypeId to set
	 */
	public void setDataTypeId(long dataTypeId) {
		this.dataTypeId = dataTypeId;
	}


	/**
	 * @return the dataAreaName
	 */
	public String getDataAreaName() {
		return dataAreaName;
	}

	/**
	 * @param dataAreaName the dataAreaName to set
	 */
	public void setDataAreaName(String dataAreaName) {
		this.dataAreaName = dataAreaName;
	}

	/**
	 * @return the facilityCommentAct
	 */
	public String getFacilityCommentAct() {
		return facilityCommentAct;
	}

	/**
	 * @param facilityCommentAct the facilityCommentAct to set
	 */
	public void setFacilityCommentAct(String facilityCommentAct) {
		this.facilityCommentAct = facilityCommentAct;
	}

	/**
	 * @return the facilityId
	 */
	public long getFacilityId() {
		return facilityId;
	}

	/**
	 * @param facilityId the facilityId to set
	 */
	public void setFacilityId(long facilityId) {
		this.facilityId = facilityId;
	}

	/**
	 * @return the inputComments
	 */
	public String getInputComments() {
		return inputComments;
	}

	/**
	 * @param inputComments the inputComments to set
	 */
	public void setInputComments(String inputComments) {
		this.inputComments = inputComments;
	}

	/**
	 * @return the numOfComments
	 */
	public int getNumOfComments() {
		return numOfComments;
	}

	/**
	 * @param numOfComments the numOfComments to set
	 */
	public void setNumOfComments(int numOfComments) {
		this.numOfComments = numOfComments;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return the facilityCommentId
	 */
	public long getFacilityCommentId() {
		return facilityCommentId;
	}

	/**
	 * @param facilityCommentId the facilityCommentId to set
	 */
	public void setFacilityCommentId(long facilityCommentId) {
		this.facilityCommentId = facilityCommentId;
	}

	/**
	 * @return the feedbackVersionFacilityId
	 */
	public String getFeedbackVersionFacilityId() {
		return feedbackVersionFacilityId;
	}

	/**
	 * @param feedbackVersionFacilityId the feedbackVersionFacilityId to set
	 */
	public void setFeedbackVersionFacilityId(String feedbackVersionFacilityId) {
		this.feedbackVersionFacilityId = feedbackVersionFacilityId;
	}

	/**
	 * @return the lastUpdatedTs
	 */
	public String getLastUpdatedTs() {
		return lastUpdatedTs;
	}

	/**
	 * @param lastUpdatedTs the lastUpdatedTs to set
	 */
	public void setLastUpdatedTs(String lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the isUpdatable
	 */
	public String getIsUpdatable() {
		return isUpdatable;
	}

	/**
	 * @param isUpdatable the isUpdatable to set
	 */
	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}
	
}