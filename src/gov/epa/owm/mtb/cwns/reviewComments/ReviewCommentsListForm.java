package gov.epa.owm.mtb.cwns.reviewComments;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

import org.apache.struts.action.ActionForm;

import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import java.util.*;


public class ReviewCommentsListForm extends ActionForm {

	public static final long INITIAL_LONG_VALUE = -999;
	public static final String TYPE_SURVEY = "S";
	public static final String TYPE_FACILITY = "F";
	public static final String STATE = UserServiceImpl.LOCATION_TYPE_ID_STATE;
		
	private String reviewCommentAct = "none";
	private int numOfComments = 0;
	private int fromReviewComments;
	private int toReviewComments;
	private int prevReviewCommentsToDisplay;
	private int nextReviewCommentsToDisplay;
	private long reviewCommentsFacilityId = INITIAL_LONG_VALUE;
	private Collection reviewCommentsHelpers = new ArrayList();
	private String inputComments;
	private String facilityNumber;
	private String facilityVersionCode; //legit values are F and S
	private long reviewCommentsFacilityIdF = INITIAL_LONG_VALUE;
	private int nextResult = -1;
	private String userType;
	private String isUpdatable = "N";	

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

	public String toString()
	{
		return 
		"\r\n numOfComments: " + numOfComments + 
		"\r\n fromReviewComments: " + fromReviewComments + 
		"\r\n toReviewComments: " + toReviewComments + 
		"\r\n prevReviewCommentsToDisplay: " + prevReviewCommentsToDisplay + 
		"\r\n nextReviewCommentsToDisplay: " + nextReviewCommentsToDisplay + 
		"\r\n reviewCommentsHelpers.size(): " + (reviewCommentsHelpers==null?-1:reviewCommentsHelpers.size()) + 
		"\r\n reviewCommentAct: " + reviewCommentAct +
		"\r\n inputComments: " + inputComments +
		"\r\n facilityVersionCode: " + facilityVersionCode +
		"\r\n facilityNumber: " + facilityNumber +
		"\r\n reviewCommentsFacilityId: " + reviewCommentsFacilityId + 
		"\r\n reviewCommentsFacilityIdF: " + reviewCommentsFacilityIdF + 
		"\r\n userType: " + userType + 
		"\r\n nextResult: " + nextResult;		
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
	 * @return the nextResult
	 */
	public int getNextResult() {
		return nextResult;
	}

	/**
	 * @param nextResult the nextResult to set
	 */
	public void setNextResult(int nextResult) {
		this.nextResult = nextResult;
	}

	/**
	 * @return the facilityVersionCode
	 */
	public String getFacilityVersionCode() {
		return facilityVersionCode;
	}

	/**
	 * @param facilityVersionCode the facilityVersionCode to set
	 */
	public void setFacilityVersionCode(String facilityVersionCode) {
		this.facilityVersionCode = facilityVersionCode;
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

	public int getFromReviewComments() {
		return fromReviewComments;
	}
	public void setFromReviewComments(int fromReviewComments) {
		this.fromReviewComments = fromReviewComments;
	}
	public int getNumOfComments() {
		return numOfComments;
	}
	public void setNumOfComments(int numOfComments) {
		this.numOfComments = numOfComments;
	}
	public int getNextReviewCommentsToDisplay() {
		return nextReviewCommentsToDisplay;
	}
	public void setNextReviewCommentsToDisplay(int nextReviewCommentsToDisplay) {
		this.nextReviewCommentsToDisplay = nextReviewCommentsToDisplay;
	}
	public int getToReviewComments() {
		return toReviewComments;
	}
	public void setToReviewComments(int toReviewComments) {
		this.toReviewComments = toReviewComments;
	}
	public int getPrevReviewCommentsToDisplay() {
		return prevReviewCommentsToDisplay;
	}
	public void setPrevReviewCommentsToDisplay(int prevReviewCommentsToDisplay) {
		this.prevReviewCommentsToDisplay = prevReviewCommentsToDisplay;
	}

	public Collection getReviewCommentsHelpers() {
		return reviewCommentsHelpers;
	}
	/**
	 * @return the reviewCommentAct
	 */
	public String getReviewCommentAct() {
		return reviewCommentAct;
	}

	/**
	 * @param reviewCommentAct the reviewCommentAct to set
	 */
	public void setReviewCommentAct(String reviewCommentAct) {
		this.reviewCommentAct = reviewCommentAct;
	}

	public void setReviewCommentsHelpers(Collection reviewCommentsHelpers) {
		this.reviewCommentsHelpers = reviewCommentsHelpers;
	}


	/**
	 * @return the reviewCommentsFacilityId
	 */
	public long getReviewCommentsFacilityId() {
		return reviewCommentsFacilityId;
	}

	/**
	 * @param reviewCommentsFacilityId the reviewCommentsFacilityId to set
	 */
	public void setReviewCommentsFacilityId(long revCommentsFacilityId) {
		this.reviewCommentsFacilityId = revCommentsFacilityId;
	}

	/**
	 * @return the facilityNumber
	 */
	public String getFacilityNumber() {
		return facilityNumber;
	}

	/**
	 * @param facilityNumber the facilityNumber to set
	 */
	public void setFacilityNumber(String facilityNumber) {
		this.facilityNumber = facilityNumber;
	}

	/**
	 * @return the reviewCommentsFacilityIdF
	 */
	public long getReviewCommentsFacilityIdF() {
		return reviewCommentsFacilityIdF;
	}

	/**
	 * @param reviewCommentsFacilityId2 the reviewCommentsFacilityId2 to set
	 */
	public void setReviewCommentsFacilityIdF(long reviewCommentsFacilityIdF) {
		this.reviewCommentsFacilityIdF = reviewCommentsFacilityIdF;
	}
}