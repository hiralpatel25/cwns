package gov.epa.owm.mtb.cwns.costcurve.input;

public class OnsiteWastewaterTreatmentSystemInput extends CostCurveInput {

	private long countyId;
	
	private int residentialNumberOfHouses;
	private int nonResidentialNumberOfHouses;
	
	private float residentialPopulationPerHousehold;
	private float nonResidentialPopulationPerHousehold;
	
	public long getCountyId() {
		return countyId;
	}
	public void setCountyId(long countyId) {
		this.countyId = countyId;
	}
	public int getNonResidentialNumberOfHouses() {
		return nonResidentialNumberOfHouses;
	}
	public void setNonResidentialNumberOfHouses(int nonResidentialNumberOfHouses) {
		this.nonResidentialNumberOfHouses = nonResidentialNumberOfHouses;
	}
	public int getResidentialNumberOfHouses() {
		return residentialNumberOfHouses;
	}
	public void setResidentialNumberOfHouses(int residentialNumberOfHouses) {
		this.residentialNumberOfHouses = residentialNumberOfHouses;
	}
	public float getNonResidentialPopulationPerHousehold() {
		return nonResidentialPopulationPerHousehold;
	}
	public void setNonResidentialPopulationPerHousehold(
			float nonResidentialPopulationPerHousehold) {
		this.nonResidentialPopulationPerHousehold = nonResidentialPopulationPerHousehold;
	}
	public float getResidentialPopulationPerHousehold() {
		return residentialPopulationPerHousehold;
	}
	public void setResidentialPopulationPerHousehold(
			float residentialPopulationPerHousehold) {
		this.residentialPopulationPerHousehold = residentialPopulationPerHousehold;
	}

	
	
	
	
	
}
