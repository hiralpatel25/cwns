package gov.epa.owm.mtb.cwns.populationInformation;


public class DischargeReceiveHelper {

	private FacilityPopulationInformation dischargingFacility;
	private FacilityPopulationInformation receivingFacility;
	private double presentResPopulationPersant = 0.0;
	private double presentNonResPopulationPersant = 0.0;
	private double projectedResPopulationPersant = 0.0;
	private double projectedNonResPopulationPersant = 0.0;
	private String combinedSewer;
	private String outOfState;

	/**
	 * @return the presentNonResPopulationPersant
	 */
	public double getPresentNonResPopulationPersant() {
		return presentNonResPopulationPersant;
	}
	/**
	 * @param presentNonResPopulationPersant the presentNonResPopulationPersant to set
	 */
	public void setPresentNonResPopulationPersant(
			double presentNonResPopulationPersant) {
		this.presentNonResPopulationPersant = presentNonResPopulationPersant;
	}
	/**
	 * @return the presentResPopulationPersant
	 */
	public double getPresentResPopulationPersant() {
		return presentResPopulationPersant;
	}
	/**
	 * @param presentResPopulationPersant the presentResPopulationPersant to set
	 */
	public void setPresentResPopulationPersant(double presentResPopulationPersant) {
		this.presentResPopulationPersant = presentResPopulationPersant;
	}
	/**
	 * @return the projectedNonResPopulationPersant
	 */
	public double getProjectedNonResPopulationPersant() {
		return projectedNonResPopulationPersant;
	}
	/**
	 * @param projectedNonResPopulationPersant the projectedNonResPopulationPersant to set
	 */
	public void setProjectedNonResPopulationPersant(
			double projectedNonResPopulationPersant) {
		this.projectedNonResPopulationPersant = projectedNonResPopulationPersant;
	}
	/**
	 * @return the projectedResPopulationPersant
	 */
	public double getProjectedResPopulationPersant() {
		return projectedResPopulationPersant;
	}
	/**
	 * @param projectedResPopulationPersant the projectedResPopulationPersant to set
	 */
	public void setProjectedResPopulationPersant(
			double projectedResPopulationPersant) {
		this.projectedResPopulationPersant = projectedResPopulationPersant;
	}
	/**
	 * @return the combinedSewer
	 */
	public String getCombinedSewer() {
		return combinedSewer;
	}
	/**
	 * @param combinedSewer the combinedSewer to set
	 */
	public void setCombinedSewer(String combinedSewer) {
		this.combinedSewer = combinedSewer;
	}
	/**
	 * @return the dischargingFacility
	 */
	public FacilityPopulationInformation getDischargingFacility() {
		return dischargingFacility;
	}
	/**
	 * @param dischargingFacility the dischargingFacility to set
	 */
	public void setDischargingFacility(
			FacilityPopulationInformation dischargingFacility) {
		this.dischargingFacility = dischargingFacility;
	}
	/**
	 * @return the outOfState
	 */
	public String getOutOfState() {
		return outOfState;
	}
	/**
	 * @param outOfState the outOfState to set
	 */
	public void setOutOfState(String outOfState) {
		this.outOfState = outOfState;
	}
	/**
	 * @return the receivingFacility
	 */
	public FacilityPopulationInformation getReceivingFacility() {
		return receivingFacility;
	}
	/**
	 * @param receivingFacility the receivingFacility to set
	 */
	public void setReceivingFacility(FacilityPopulationInformation receivingFacility) {
		this.receivingFacility = receivingFacility;
	}

}
