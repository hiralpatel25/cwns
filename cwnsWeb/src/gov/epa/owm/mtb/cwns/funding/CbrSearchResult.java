package gov.epa.owm.mtb.cwns.funding;

import gov.epa.owm.mtb.cwns.model.CbrLoanInformation;

public class CbrSearchResult {
	private CbrLoanInformation cbrLoanInformation;
	private String displayLink;
		
	public CbrLoanInformation getCbrLoanInformation() {
		return cbrLoanInformation;
	}
	
	public void setCbrLoanInformation(CbrLoanInformation cbrLoanInformation) {
		this.cbrLoanInformation = cbrLoanInformation;
	}

	public String getDisplayLink() {
		return displayLink;
	}

	public void setDisplayLink(String displayLink) {
		this.displayLink = displayLink;
	}
	

}
