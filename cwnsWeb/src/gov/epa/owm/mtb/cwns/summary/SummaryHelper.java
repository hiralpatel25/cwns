package gov.epa.owm.mtb.cwns.summary;

public class SummaryHelper {
	
	private String name;
	private String required = "";
	private String entered = "";
	private String error = "";
	private String correction = "";
	private String ccerror = "";
	
	public String getCcerror() {
		return ccerror;
	}

	public void setCcerror(String ccerror) {
		this.ccerror = ccerror;
	}

	public SummaryHelper() {
		super();
	}
	
	public SummaryHelper(String name, String required, String entered, String error) {
		this.name = name;
		this.required = required;
		this.entered = entered;
		this.error = error;
		//this.ccerror = ccerror;
	}
	
 	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEntered() {
		return entered;
	}
	public void setEntered(String entered) {
		this.entered = entered;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getCorrection() {
		return correction;
	}
	public void setCorrection(String correction) {
		this.correction = correction;
	}

}
