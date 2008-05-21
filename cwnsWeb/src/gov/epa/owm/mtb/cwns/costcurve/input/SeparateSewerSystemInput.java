package gov.epa.owm.mtb.cwns.costcurve.input;

public class SeparateSewerSystemInput extends CostCurveInput {

	private long countyId;
	
	private int presentPopulation;	
	
	public long getCountyId() {
		return countyId;
	}

	public void setCountyId(long countyId) {
		this.countyId = countyId;
	}

	public int getPresentPopulation() {
		return presentPopulation;
	}
	public void setPresentPopulation(int residentialNumberOfHouses) {
		this.presentPopulation = residentialNumberOfHouses;
	}

	
	
	
	
}
