package gov.epa.owm.mtb.cwns.manageCostCurve;

public class ManageCostCurveViewAllCatVListHelper {

	private String cwnsNumber = "";
	
	private String facilityName = "";

	private long adjustedAmount = 0;

	/**
	 * @return the adjustedAmount
	 */
	public long getAdjustedAmount() {
		return adjustedAmount;
	}

	/**
	 * @param adjustedAmount the adjustedAmount to set
	 */
	public void setAdjustedAmount(long adjustedAmount) {
		this.adjustedAmount = adjustedAmount;
	}

	/**
	 * @return the cwnsNumber
	 */
	public String getCwnsNumber() {
		return cwnsNumber;
	}

	/**
	 * @param cwnsNumber the cwnsNumber to set
	 */
	public void setCwnsNumber(String cwnsNumber) {
		this.cwnsNumber = cwnsNumber;
	}

	/**
	 * @return the facilityName
	 */
	public String getFacilityName() {
		return facilityName;
	}

	/**
	 * @param facilityName the facilityName to set
	 */
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}


}