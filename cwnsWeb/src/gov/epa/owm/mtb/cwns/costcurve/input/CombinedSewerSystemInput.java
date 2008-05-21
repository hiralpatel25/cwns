package gov.epa.owm.mtb.cwns.costcurve.input;

public class CombinedSewerSystemInput extends CostCurveInput {

	private int presentResidentialPopulation;
	private int upstreamPresentResidentReceivingPopulation = 0;
	private long sumDirectDownstreamFacilitiesPopulationByDischargeType;
	private int totalPresentResidentialReceivingPopulationInSewershed; //@deprecated
	private int costcurvePopulationCount;
	private int costcurveAreaSqMilesMsr;
	private short imperviousRatio;
	
	private double presentTotalFlowForFacility;
	private double presentTotalFlowForNextDownstreamFacilities;
	private boolean isOnlyFacilityInSewershed;
	private boolean isTerminatingFacility;
			
	private double rainfall;		
	
	public long getSumDirectDownstreamFacilitiesPopulationByDischargeType() {
		return sumDirectDownstreamFacilitiesPopulationByDischargeType;
	}

	public void setSumDirectDownstreamFacilitiesPopulationByDischargeType(
			long sumDirectDownstreamFacilitiesPopulationByDischargeType) {
		this.sumDirectDownstreamFacilitiesPopulationByDischargeType = sumDirectDownstreamFacilitiesPopulationByDischargeType;
	}

	public double getRainfall() {
		return rainfall;
	}

	public void setRainfall(double rainfall) {
		this.rainfall = rainfall;
	}

	public boolean isTerminatingFacility() {
		return isTerminatingFacility;
	}

	public void setTerminatingFacility(boolean isTerminatingFacility) {
		this.isTerminatingFacility = isTerminatingFacility;
	}

	public boolean isOnlyFacilityInSewershed() {
		return isOnlyFacilityInSewershed;
	}

	public void setOnlyFacilityInSewershed(boolean isOnlyFacilityInSewershed) {
		this.isOnlyFacilityInSewershed = isOnlyFacilityInSewershed;
	}

	public double getPresentTotalFlowForNextDownstreamFacilities() {
		return presentTotalFlowForNextDownstreamFacilities;
	}

	public void setPresentTotalFlowForNextDownstreamFacilities(
			double presentTotalFlowForNextDownstreamFacilities) {
		this.presentTotalFlowForNextDownstreamFacilities = presentTotalFlowForNextDownstreamFacilities;
	}

	public double getPresentTotalFlowForFacility() {
		return presentTotalFlowForFacility;
	}

	public void setPresentTotalFlowForFacility(double presentTotalFlow) {
		this.presentTotalFlowForFacility = presentTotalFlow;
	}

	public short getImperviousRatio() {
		return imperviousRatio;
	}

	public void setImperviousRatio(short imperviousRatio) {
		this.imperviousRatio = imperviousRatio;
	}

	public int getCostcurveAreaSqMilesMsr() {
		return costcurveAreaSqMilesMsr;
	}

	public void setCostcurveAreaSqMilesMsr(int costcurveAreaSqMilesMsr) {
		this.costcurveAreaSqMilesMsr = costcurveAreaSqMilesMsr;
	}

	public int getCostcurvePopulationCount() {
		return costcurvePopulationCount;
	}

	public void setCostcurvePopulationCount(int costcurvePopulationCount) {
		this.costcurvePopulationCount = costcurvePopulationCount;
	}

	public int getTotalPresentResidentialReceivingPopulationInSewershed() {
		return totalPresentResidentialReceivingPopulationInSewershed;
	}

	public void setTotalPresentResidentialReceivingPopulationInSewershed(
			int totalPresentResidentialReceivingPopulationInSewershed) {
		this.totalPresentResidentialReceivingPopulationInSewershed = totalPresentResidentialReceivingPopulationInSewershed;
	}

	public int getPresentResidentialPopulation() {
		return presentResidentialPopulation;
	}

	public void setPresentResidentialPopulation(int projectedPopulation) {
		this.presentResidentialPopulation = projectedPopulation;
	}

	public int getUpstreamPresentResidentReceivingPopulation() {
		return upstreamPresentResidentReceivingPopulation;
	}

	public void setUpstreamPresentResidentReceivingPopulation(
			int upstreamPresentResidentReceivingPopulation) {
		this.upstreamPresentResidentReceivingPopulation = upstreamPresentResidentReceivingPopulation;
	}

	
	
	
	
}
