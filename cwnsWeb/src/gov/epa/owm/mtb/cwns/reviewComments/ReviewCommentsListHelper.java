package gov.epa.owm.mtb.cwns.reviewComments;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

public class ReviewCommentsListHelper {

	private String shortComments;
	private String longComments;
	private String userName;
	private String updateDate;
	private String reviewCommentId;


	/**
	 * @return the reviewCommentId
	 */
	public String getReviewCommentId() {
		return reviewCommentId;
	}

	/**
	 * @param reviewCommentId the reviewCommentId to set
	 */
	public void setReviewCommentId(String reviewCommentId) {
		this.reviewCommentId = reviewCommentId;
	}

	public ReviewCommentsListHelper(String shortComments, String longComments, String reviewCommentId, String userName, String updateDate) {
		this.shortComments = shortComments;
		this.longComments = longComments;
		this.reviewCommentId = reviewCommentId;
		this.userName = userName;
		this.updateDate = updateDate;		
	}

	/**
	 * @return the longComments
	 */
	public String getLongComments() {
		return longComments;
	}

	/**
	 * @param longComments the longComments to set
	 */
	public void setLongComments(String longComments) {
		this.longComments = longComments;
	}

	/**
	 * @return the shortComments
	 */
	public String getShortComments() {
		return shortComments;
	}

	/**
	 * @param shortComments the shortComments to set
	 */
	public void setShortComments(String shortComments) {
		this.shortComments = shortComments;
	}

	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
