package gov.epa.owm.mtb.cwns.reviewstatus;

public class ReviewStatusHelper {
	private String reviewStatusId;
	private String reviewStatusDesc;
	private String reviewStatusCount;

	public ReviewStatusHelper(String reviewStatusId, String reviewStatusDesc, String reviewStatusCount) {
		super();
		this.reviewStatusId = reviewStatusId;
		this.reviewStatusDesc = reviewStatusDesc;
		this.reviewStatusCount = reviewStatusCount;
	}
	

	public String getReviewStatusCount() {
		return reviewStatusCount;
	}

	public void setReviewStatusCount(String reviewStatusCount) {
		this.reviewStatusCount = reviewStatusCount;
	}

	public String getReviewStatusDesc() {
		return reviewStatusDesc;
	}

	public void setReviewStatusDesc(String reviewStatusDesc) {
		this.reviewStatusDesc = reviewStatusDesc;
	}

	public String getReviewStatusId() {
		return reviewStatusId;
	}

	public void setReviewStatusId(String reviewStatusId) {
		this.reviewStatusId = reviewStatusId;
	}

}
