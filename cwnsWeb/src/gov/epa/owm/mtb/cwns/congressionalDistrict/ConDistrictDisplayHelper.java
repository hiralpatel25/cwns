package gov.epa.owm.mtb.cwns.congressionalDistrict;

public class ConDistrictDisplayHelper {
	
	private String congressionalDistrictId;
	private String locationId;
	private String name;
	private char feedbackDeleteFlag='N';
	
	public String getCongressionalDistrictId() {
		return congressionalDistrictId;
	}
	public void setCongressionalDistrictId(String congressionalDistrictId) {
		this.congressionalDistrictId = congressionalDistrictId;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getName() {
		String lastTwoDigits=congressionalDistrictId.substring(2);
		if("01".equals(lastTwoDigits))
			name = lastTwoDigits.substring(1)+"st Congressional District";
		else if ("02".equals(lastTwoDigits))
		   name = lastTwoDigits.substring(1)+"nd Congressional District";
		else if("03".equals(lastTwoDigits))
		  	name = lastTwoDigits.substring(1)+"rd Congressional District";
		else name = lastTwoDigits.charAt(0)=='0'?lastTwoDigits.substring(1)+"th Congressional District"
				                                                 :lastTwoDigits+"th Congressional District";
		
		return name;
	}
	public char getFeedbackDeleteFlag() {
		return feedbackDeleteFlag;
	}
	public void setFeedbackDeleteFlag(char feedbackDeleteFlag) {
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}
	

}
