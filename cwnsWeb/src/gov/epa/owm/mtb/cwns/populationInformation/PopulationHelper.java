package gov.epa.owm.mtb.cwns.populationInformation;

public class PopulationHelper {

	private String dischargingFacilityId;
	private String disFacilityCwnsNbr;
	private String disFacilityName;
	private String recFacilityCwnsNbr;
	private String recFacilityName;
	private int presentResPopulation = 0;
	private int presentNonResPopulation = 0;
	private int projectedResPopulation = 0;
	private int projectedNonResPopulation = 0;
	
	
	public PopulationHelper() {
		super();
	}
	
	public PopulationHelper(String dischargingFacilityId, String disFacilityCwnsNbr, String disFacilityName, String recFacilityCwnsNbr, String recFacilityName, int presentResPopulation, int presentNonResPopulation, int projectedResPopulation, int projectedNonResPopulation) {
		super();
		this.dischargingFacilityId = dischargingFacilityId;
		this.disFacilityCwnsNbr = disFacilityCwnsNbr;
		this.disFacilityName = disFacilityName;
		this.recFacilityCwnsNbr = recFacilityCwnsNbr;
		this.recFacilityName = recFacilityName;
		this.presentResPopulation = presentResPopulation;
		this.presentNonResPopulation = presentNonResPopulation;
		this.projectedResPopulation = projectedResPopulation;
		this.projectedNonResPopulation = projectedNonResPopulation;
	}

	public String getDischargingFacilityId() {
		return dischargingFacilityId;
	}
	public void setDischargingFacilityId(String dischargingFacilityId) {
		this.dischargingFacilityId = dischargingFacilityId;
	}
	public String getDisFacilityCwnsNbr() {
		return disFacilityCwnsNbr;
	}
	public void setDisFacilityCwnsNbr(String disFacilityCwnsNbr) {
		this.disFacilityCwnsNbr = disFacilityCwnsNbr;
	}
	public String getDisFacilityName() {
		return disFacilityName;
	}
	public void setDisFacilityName(String disFacilityName) {
		this.disFacilityName = disFacilityName;
	}
	public int getPresentNonResPopulation() {
		return presentNonResPopulation;
	}
	public void setPresentNonResPopulation(int presentNonResPopulation) {
		this.presentNonResPopulation = presentNonResPopulation;
	}
	public int getPresentResPopulation() {
		return presentResPopulation;
	}
	public void setPresentResPopulation(int presentResPopulation) {
		this.presentResPopulation = presentResPopulation;
	}
	public int getProjectedNonResPopulation() {
		return projectedNonResPopulation;
	}
	public void setProjectedNonResPopulation(int projectedNonResPopulation) {
		this.projectedNonResPopulation = projectedNonResPopulation;
	}
	public int getProjectedResPopulation() {
		return projectedResPopulation;
	}
	public void setProjectedResPopulation(int projectedResPopulation) {
		this.projectedResPopulation = projectedResPopulation;
	}
	public String getRecFacilityCwnsNbr() {
		return recFacilityCwnsNbr;
	}
	public void setRecFacilityCwnsNbr(String recFacilityCwnsNbr) {
		this.recFacilityCwnsNbr = recFacilityCwnsNbr;
	}
	public String getRecFacilityName() {
		return recFacilityName;
	}
	public void setRecFacilityName(String recFacilityName) {
		this.recFacilityName = recFacilityName;
	}
}
