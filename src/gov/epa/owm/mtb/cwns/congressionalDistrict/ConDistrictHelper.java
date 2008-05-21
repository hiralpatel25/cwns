package gov.epa.owm.mtb.cwns.congressionalDistrict;

public class ConDistrictHelper {
	
	private String conDistrictName;
	private char primary;
	private String conDistrictId;
	private long geographicAreaId;
	private char feedbackDeleteFlag='N';
	
	public String getConDistrictId() {
		return conDistrictId;
	}
	public void setConDistrictId(String conDistrictId) {
		this.conDistrictId = conDistrictId;
	}
	public String getConDistrictName() {
		return conDistrictName;
	}
	public void setConDistrictName(String conDistrictName) {
		this.conDistrictName = conDistrictName;
	}
	public long getGeographicAreaId() {
		return geographicAreaId;
	}
	public void setGeographicAreaId(long geographicAreaId) {
		this.geographicAreaId = geographicAreaId;
	}
	public char getPrimary() {
		return primary;
	}
	public void setPrimary(char primary) {
		this.primary = primary;
	}
	public char getFeedbackDeleteFlag() {
		return feedbackDeleteFlag;
	}
	public void setFeedbackDeleteFlag(char feedbackDeleteFlag) {
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

}
