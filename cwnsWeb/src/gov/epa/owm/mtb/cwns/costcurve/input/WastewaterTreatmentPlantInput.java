package gov.epa.owm.mtb.cwns.costcurve.input;

public class WastewaterTreatmentPlantInput extends CostCurveInput {
		
	private long countyId;

	private int projectedNonResidentialReceivingPopulation;
	private int projectedUpstreamNonResidentialReceivingPopulation;
	private int projectedResidentialReceivingPopulation;
	private int projectedUpstreamResidentialReceivingPopulation;
	
	private int projectedResidentialPopulation;
	private char projectedPlantType;
	
	private int projectedFacilityEffluenType;
	private int projectedFacilityEffluenTypeAdvancedTreatmentSubtype;	
	
	private char presentPlantType;
	private float presentFlowRate;
	private int presentFacilityEffluenType;
	private int presentFacilityEffluenTypeAdvancedTreatmentSubtype;

	
		
	public int getPresentFacilityEffluenType() {
		return presentFacilityEffluenType;
	}

	public void setPresentFacilityEffluenType(int presentFacilityEffluenType) {
		this.presentFacilityEffluenType = presentFacilityEffluenType;
	}

	public int getPresentFacilityEffluenTypeAdvancedTreatmentSubtype() {
		return presentFacilityEffluenTypeAdvancedTreatmentSubtype;
	}

	public void setPresentFacilityEffluenTypeAdvancedTreatmentSubtype(
			int presentFacilityEffluenTypeAdvancedTreatmentSubtype) {
		this.presentFacilityEffluenTypeAdvancedTreatmentSubtype = presentFacilityEffluenTypeAdvancedTreatmentSubtype;
	}

	public int getProjectedFacilityEffluenTypeAdvancedTreatmentSubtype() {
		return projectedFacilityEffluenTypeAdvancedTreatmentSubtype;
	}

	public void setProjectedFacilityEffluenTypeAdvancedTreatmentSubtype(
			int projectedFacilityEffluenTypeAdvancedTreatmentSubtype) {
		this.projectedFacilityEffluenTypeAdvancedTreatmentSubtype = projectedFacilityEffluenTypeAdvancedTreatmentSubtype;
	}

	public float getPresentFlowRate() {
		return presentFlowRate;
	}

	public void setPresentFlowRate(float presentFlowRate) {
		this.presentFlowRate = presentFlowRate;
	}

	public char getPresentPlantType() {
		return presentPlantType;
	}

	public void setPresentPlantType(char presentPlantType) {
		this.presentPlantType = presentPlantType;
	}

	public int getProjectedFacilityEffluenType() {
		return projectedFacilityEffluenType;
	}

	public void setProjectedFacilityEffluenType(int projectedFacilityEffluenType) {
		this.projectedFacilityEffluenType = projectedFacilityEffluenType;
	}

	public long getCountyId() {
		return countyId;
	}

	public void setCountyId(long countyId) {
		this.countyId = countyId;
	}
	
	public int getProjectedNonResidentialReceivingPopulation() {
		return projectedNonResidentialReceivingPopulation;
	}

	public void setProjectedNonResidentialReceivingPopulation(
			int projectedNonResidentialReceivingPopulation) {
		this.projectedNonResidentialReceivingPopulation = projectedNonResidentialReceivingPopulation;
	}

	public int getProjectedResidentialReceivingPopulation() {
		return projectedResidentialReceivingPopulation;
	}

	public void setProjectedResidentialReceivingPopulation(
			int projectedResidentialReceivingPopulation) {
		this.projectedResidentialReceivingPopulation = projectedResidentialReceivingPopulation;
	}

	public int getProjectedUpstreamNonResidentialReceivingPopulation() {
		return projectedUpstreamNonResidentialReceivingPopulation;
	}

	public void setProjectedUpstreamNonResidentialReceivingPopulation(
			int projectedUpstreamNonResidentialReceivingPopulation) {
		this.projectedUpstreamNonResidentialReceivingPopulation = projectedUpstreamNonResidentialReceivingPopulation;
	}

	public int getProjectedUpstreamResidentialReceivingPopulation() {
		return projectedUpstreamResidentialReceivingPopulation;
	}

	public void setProjectedUpstreamResidentialReceivingPopulation(
			int projectedUpstreamResidentialReceivingPopulation) {
		this.projectedUpstreamResidentialReceivingPopulation = projectedUpstreamResidentialReceivingPopulation;
	}

	public int getProjectedResidentialPopulation() {
		return projectedResidentialPopulation;
	}

	public void setProjectedResidentialPopulation(int projectedPopulation) {
		this.projectedResidentialPopulation = projectedPopulation;
	}

	public char getProjectedPlantType() {
		return projectedPlantType;
	}

	public void setProjectedPlantType(char projectedPlantType) {
		this.projectedPlantType = projectedPlantType;
	}
	
	
	
	
}
