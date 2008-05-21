package gov.epa.owm.mtb.cwns.summary;

public class FacilityCommentHelper {
	
	private String dataAreaTypeName;
	private String comments = "";
	private String lastUpdateTS = "";
		
	public FacilityCommentHelper() {
		super();
	}
	
	public FacilityCommentHelper(String dataAreaTypeName, String lastUpdateTS, String comments) {
		this.dataAreaTypeName = dataAreaTypeName;
		this.comments = comments;
		this.lastUpdateTS = lastUpdateTS;
	}	
	
 	
	public String getDataAreaTypeName() {
		return dataAreaTypeName;
	}
	public void setdataAreaTypeName(String dataAreaTypeName) {
		this.dataAreaTypeName = dataAreaTypeName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getLastUpdateTS() {
		return lastUpdateTS;
	}
	public void setLastUpdateTS(String lastUpdateTS) {
		this.lastUpdateTS = lastUpdateTS;
	}

}
