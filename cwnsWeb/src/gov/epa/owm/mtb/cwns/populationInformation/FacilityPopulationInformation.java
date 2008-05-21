package gov.epa.owm.mtb.cwns.populationInformation;

import java.util.Collection;

public class FacilityPopulationInformation {

	private String facilityId;
	private String facilityCwnsNbr;
	private String facilityName;
	private int actualPresentResPopulation = 0;
	private int actualPresentNonResPopulation = 0;
	private int actualProjectedResPopulation = 0;
	private int actualProjectedNonResPopulation = 0;
	private double presentResPopulationPersant = 0.0;
	private double presentNonResPopulationPersant = 0.0;
	private double projectedResPopulationPersant = 0.0;
	private double projectedNonResPopulationPersant = 0.0;
	Collection upStreamFacilites = null;
	
	
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

	public FacilityPopulationInformation() {
		super();
	}
	
	public FacilityPopulationInformation shallowCopy() {
	
		FacilityPopulationInformation copyFP = new FacilityPopulationInformation();
		copyFP.setActualPresentNonResPopulation(actualPresentNonResPopulation);
		copyFP.setActualPresentResPopulation(actualPresentResPopulation);
		copyFP.setActualProjectedNonResPopulation(actualProjectedNonResPopulation);
		copyFP.setActualProjectedResPopulation(actualProjectedResPopulation);
		copyFP.setFacilityCwnsNbr(facilityCwnsNbr);
		copyFP.setFacilityId(facilityId);
		copyFP.setFacilityName(facilityName);
		copyFP.setPresentNonResPopulationPersant(presentNonResPopulationPersant);
		copyFP.setPresentResPopulationPersant(presentResPopulationPersant);
		copyFP.setProjectedNonResPopulationPersant(projectedNonResPopulationPersant);
		copyFP.setProjectedResPopulationPersant(projectedResPopulationPersant);

		return copyFP;
	}
	
	public Collection getUpStreamFacilites() {
		return upStreamFacilites;
	}

	public void setUpStreamFacilites(Collection upStreamFacilites) {
		this.upStreamFacilites = upStreamFacilites;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityCwnsNbr() {
		return facilityCwnsNbr;
	}

	public void setFacilityCwnsNbr(String facilityCwnsNbr) {
		this.facilityCwnsNbr = facilityCwnsNbr;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	/**
	 * @return the actualPresentNonResPopulation
	 */
	public int getActualPresentNonResPopulation() {
		return actualPresentNonResPopulation;
	}

	/**
	 * @param actualPresentNonResPopulation the actualPresentNonResPopulation to set
	 */
	public void setActualPresentNonResPopulation(int actualPresentNonResPopulation) {
		this.actualPresentNonResPopulation = actualPresentNonResPopulation;
	}

	/**
	 * @return the actualPresentResPopulation
	 */
	public int getActualPresentResPopulation() {
		return actualPresentResPopulation;
	}

	/**
	 * @param actualPresentResPopulation the actualPresentResPopulation to set
	 */
	public void setActualPresentResPopulation(int actualPresentResPopulation) {
		this.actualPresentResPopulation = actualPresentResPopulation;
	}

	/**
	 * @return the actualProjectedNonResPopulation
	 */
	public int getActualProjectedNonResPopulation() {
		return actualProjectedNonResPopulation;
	}

	/**
	 * @param actualProjectedNonResPopulation the actualProjectedNonResPopulation to set
	 */
	public void setActualProjectedNonResPopulation(
			int actualProjectedNonResPopulation) {
		this.actualProjectedNonResPopulation = actualProjectedNonResPopulation;
	}

	/**
	 * @return the actualProjectedResPopulation
	 */
	public int getActualProjectedResPopulation() {
		return actualProjectedResPopulation;
	}

	/**
	 * @param actualProjectedResPopulation the actualProjectedResPopulation to set
	 */
	public void setActualProjectedResPopulation(int actualProjectedResPopulation) {
		this.actualProjectedResPopulation = actualProjectedResPopulation;
	}


}

