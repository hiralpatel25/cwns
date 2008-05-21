package gov.epa.owm.mtb.cwns.facility;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

public class FacilityListHelper {

	private String facId;
	private String cwnsNbr;
	private String name;
	private String reviewStatus;
	private String localReviewStatus = "None";
	private String feedbackFacId;
	private String county ="County Not Found";
	private String authority = "";
	private String check_box = "no";
	
 	public String getCheck_box() {
		return check_box;
	}

	public void setCheck_box(String check_box) {
		this.check_box = check_box;
	}

	public FacilityListHelper() {
		super();
	}
/*	
	public FacilityListHelper(String facId, String cwnsNbr, String name, String reviewStatus) {
		this.facId = facId;
		this.cwnsNbr = cwnsNbr;
		this.name = name;
		this.reviewStatus = reviewStatus;
	}
*/	
	public FacilityListHelper(String facId, String cwnsNbr, String name, String reviewStatus, String county, String localReviewStatus, String authority, String feedbackFacId) {
		this.facId = facId;
		this.cwnsNbr = cwnsNbr;
		this.name = name;
		this.reviewStatus = reviewStatus;
		this.county = county;
		this.localReviewStatus = localReviewStatus;
		this.authority = authority;
		this.feedbackFacId=feedbackFacId;
	}

	public String getFacId() {
		return facId;
	}

	public void setFacId(String facId) {
		this.facId = facId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getLocalReviewStatus() {
		return localReviewStatus;
	}

	public void setLocalReviewStatus(String localReviewStatus) {
		this.localReviewStatus = localReviewStatus;
	}

	public String getCwnsNbr() {
		return cwnsNbr;
	}

	public void setCwnsNbr(String cwnsNbr) {
		this.cwnsNbr = cwnsNbr;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getFeedbackFacId() {
		return feedbackFacId;
	}

	public void setFeedbackFacId(String feedbackFacId) {
		this.feedbackFacId = feedbackFacId;
	}
	
}
