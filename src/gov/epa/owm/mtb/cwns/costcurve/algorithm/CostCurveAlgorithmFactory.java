package gov.epa.owm.mtb.cwns.costcurve.algorithm;

/**
 * CWNS Related Data from Data base
 * @author hchen3
 *
 */

public class CostCurveAlgorithmFactory {
	
	
	public CostCurveAlgorithm getCostCurveAlgorithm(String costCurveId) {
		if ((CostCurveAlgorithmBase.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_ALL.equalsIgnoreCase(costCurveId)))
			return newIndividualWastewaterTreatmentAllAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_INNOVATIVE.equalsIgnoreCase(costCurveId)))
			return newIndividualWastewaterTreatmentInnovativeAlgorithm;		
		else if ((CostCurveAlgorithmBase.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_CONVENTIONAL.equalsIgnoreCase(costCurveId)))
			return newIndividualWastewaterTreatmentConventionalAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_CLUSTER_WASTEWATER_TREATMENT.equalsIgnoreCase(costCurveId)))
			return newClusterWastewaterTreatmentAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_REHAB_WASTEWATER_TREATMENT_ALL.equalsIgnoreCase(costCurveId)))
			return rehabIndividualWastewaterTreatmentAllAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_REHAB_WASTEWATER_TREATMENT_INNOVATIVE.equalsIgnoreCase(costCurveId)))
			return rehabIndividualWastewaterTreatmentInnovativeAlgorithm;	
		else if ((CostCurveAlgorithmBase.CODE_REHAB_WASTEWATER_TREATMENT_CONVENTIONAL.equalsIgnoreCase(costCurveId)))
			return rehabIndividualWastewaterTreatmentConventionalAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_REHAB_CLUSTER_WASTEWATER_TREATMENT.equalsIgnoreCase(costCurveId)))
			return rehabClusterWastewaterTreatmentAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_NEW_SEPERATE_SEWER_COLLECTOR.equalsIgnoreCase(costCurveId)))
			return newExpandSeparateSewerCollectorAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_NEW_SEPERATE_SEWER_INTERCEPTOR.equalsIgnoreCase(costCurveId)))
			return newExpandSeparateSewerInterceptorAlgorithm;		
		else if ((CostCurveAlgorithmBase.CODE_SEPERATE_SEWER_REHAB.equalsIgnoreCase(costCurveId)))
			return rehabSeparateSewerlAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_COMBINED_SEWER_OVERFLOW.equalsIgnoreCase(costCurveId)))
			return combinedSewerOverflowAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_NEW_TREATMENT_PLANT.equalsIgnoreCase(costCurveId)))
			return newTreatmentPlantAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_INCREASE_CAPACITY.equalsIgnoreCase(costCurveId)))
			return increaseCapacityAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_INCREASE_TREATMENT.equalsIgnoreCase(costCurveId)))
			return increaseTreatmentAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_INCREASE_CAPACITY_TREATMENT.equalsIgnoreCase(costCurveId)))
			return increaseTreatmentCapacityAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_REPLACE_TREATMENT_PLANT.equalsIgnoreCase(costCurveId)))
			return newTreatmentPlantAlgorithm;
		else if ((CostCurveAlgorithmBase.CODE_DISINFECTION_ONLY.equalsIgnoreCase(costCurveId)))
			return disinfectionOnlyAlgorithm;
		return newTreatmentPlantAlgorithm;
	}

	private CostCurveAlgorithm newIndividualWastewaterTreatmentAllAlgorithm;
	public void setNewIndividualWastewaterTreatmentAllAlgorithm(CostCurveAlgorithm cca) {
		this.newIndividualWastewaterTreatmentAllAlgorithm = cca;
	}		
	
	private CostCurveAlgorithm newIndividualWastewaterTreatmentInnovativeAlgorithm;
	public void setNewIndividualWastewaterTreatmentInnovativeAlgorithm(CostCurveAlgorithm cca) {
		this.newIndividualWastewaterTreatmentInnovativeAlgorithm = cca;
	}	
	
	private CostCurveAlgorithm newIndividualWastewaterTreatmentConventionalAlgorithm;
	public void setNewIndividualWastewaterTreatmentConventionalAlgorithm(CostCurveAlgorithm newIndividualWastewaterTreatmentAllAlgorithm) {
		this.newIndividualWastewaterTreatmentConventionalAlgorithm = newIndividualWastewaterTreatmentAllAlgorithm;
	}

	private CostCurveAlgorithm newClusterWastewaterTreatmentAlgorithm;
	public void setNewClusterWastewaterTreatmentAlgorithm(CostCurveAlgorithm cca) {
		this.newClusterWastewaterTreatmentAlgorithm = cca;
	}
	
	private CostCurveAlgorithm rehabIndividualWastewaterTreatmentAllAlgorithm;
	public void setRehabIndividualWastewaterTreatmentAllAlgorithm(CostCurveAlgorithm cca) {
		this.rehabIndividualWastewaterTreatmentAllAlgorithm = cca;
	}				
	
	private CostCurveAlgorithm rehabIndividualWastewaterTreatmentInnovativeAlgorithm;
	public void setRehabIndividualWastewaterTreatmentInnovativeAlgorithm(CostCurveAlgorithm cca) {
		this.rehabIndividualWastewaterTreatmentInnovativeAlgorithm = cca;
	}	
	
	private CostCurveAlgorithm rehabIndividualWastewaterTreatmentConventionalAlgorithm;
	public void setRehabIndividualWastewaterTreatmentConventionalAlgorithm(CostCurveAlgorithm cca) {
		this.rehabIndividualWastewaterTreatmentConventionalAlgorithm = cca;
	}				
		
	private CostCurveAlgorithm rehabClusterWastewaterTreatmentAlgorithm;
	public void setRehabClusterWastewaterTreatmentAlgorithm(CostCurveAlgorithm cca) {
		this.rehabClusterWastewaterTreatmentAlgorithm = cca;
	}					
		 
	private CostCurveAlgorithm newExpandSeparateSewerCollectorAlgorithm;
	public void setNewExpandSeparateSewerCollectorAlgorithm(CostCurveAlgorithm cca) {
		this.newExpandSeparateSewerCollectorAlgorithm = cca;
	}	
	
	private CostCurveAlgorithm newExpandSeparateSewerInterceptorAlgorithm;
	public void setNewExpandSeparateSewerInterceptorAlgorithm(
			CostCurveAlgorithm newSeparateSewerlInterceptorAlgorithm) {
		this.newExpandSeparateSewerInterceptorAlgorithm = newSeparateSewerlInterceptorAlgorithm;
	}
	
	private CostCurveAlgorithm rehabSeparateSewerlAlgorithm;
	public void setRehabSeparateSewerlAlgorithm(CostCurveAlgorithm cca) {
		this.rehabSeparateSewerlAlgorithm = cca;
	}	
	
	private CostCurveAlgorithm combinedSewerOverflowAlgorithm;
	public void setCombinedSewerOverflowAlgorithm(CostCurveAlgorithm combinedSewerOverflowAlgorithm) {
		this.combinedSewerOverflowAlgorithm = combinedSewerOverflowAlgorithm;
	}	
	
	private CostCurveAlgorithm newTreatmentPlantAlgorithm;
	public void setNewTreatmentPlantAlgorithm(CostCurveAlgorithm newTreatmentPlantAlgorithm) {
		this.newTreatmentPlantAlgorithm = newTreatmentPlantAlgorithm;
	}
	
	private CostCurveAlgorithm increaseCapacityAlgorithm;
	public void setIncreaseCapacityAlgorithm(CostCurveAlgorithm increaseCapacityAlgorithm) {
		this.increaseCapacityAlgorithm = increaseCapacityAlgorithm;
	}

	private CostCurveAlgorithm increaseTreatmentAlgorithm;
	public void setIncreaseLevelTreatmentAlgorithm(CostCurveAlgorithm increaseTreatmentAlgorithm) {
		this.increaseTreatmentAlgorithm = increaseTreatmentAlgorithm;
	}

	//increaseTreatmentCapacityAlgorithm
	private CostCurveAlgorithm increaseTreatmentCapacityAlgorithm;
	public void setIncreaseTreatmentCapacityAlgorithm(CostCurveAlgorithm increaseTreatmentCapacityAlgorithm) {
		this.increaseTreatmentCapacityAlgorithm = increaseTreatmentCapacityAlgorithm;
	}
		
	private CostCurveAlgorithm disinfectionOnlyAlgorithm;
	public void setDisinfectionOnlyAlgorithm(CostCurveAlgorithm cca) {
		this.disinfectionOnlyAlgorithm = cca;
	}		
	
}
