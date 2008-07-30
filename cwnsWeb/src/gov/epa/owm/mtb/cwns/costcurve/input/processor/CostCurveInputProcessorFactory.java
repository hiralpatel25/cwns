package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.costcurve.algorithm.CostCurveAlgorithmBase;

/**
 * CWNS Related Data from Data base
 * @author hchen3
 *
 */

public class CostCurveInputProcessorFactory {
	
		
	public CostCurveInputProcessor getCostCurveInputProcessor(String costCurveId) {
		if (CostCurveAlgorithmBase.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_ALL.equalsIgnoreCase(costCurveId))
			return newIndividualWastewaterTreatmentAllCostInputProcessor;
		else if (CostCurveAlgorithmBase.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_INNOVATIVE.equalsIgnoreCase(costCurveId))
			return newIndividualWastewaterTreatmentInnovativeCostInputProcessor;
		else if (CostCurveAlgorithmBase.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_CONVENTIONAL.equalsIgnoreCase(costCurveId))
			return newIndividualWastewaterTreatmentConventionalCostInputProcessor;						
		else if (CostCurveAlgorithmBase.CODE_CLUSTER_WASTEWATER_TREATMENT.equalsIgnoreCase(costCurveId))
			return newClusterWastewaterTreatmentCostInputProcessor;						
		else if (CostCurveAlgorithmBase.CODE_REHAB_WASTEWATER_TREATMENT_ALL.equalsIgnoreCase(costCurveId))
			return rehabIndividualWastewaterTreatmentAllCostInputProcessor;	
		else if (CostCurveAlgorithmBase.CODE_REHAB_WASTEWATER_TREATMENT_INNOVATIVE.equalsIgnoreCase(costCurveId))
			return rehabIndividualWastewaterTreatmentInnovativeCostInputProcessor;
		else if (CostCurveAlgorithmBase.CODE_REHAB_WASTEWATER_TREATMENT_CONVENTIONAL.equalsIgnoreCase(costCurveId))
			return rehabIndividualWastewaterTreatmentConventionalCostInputProcessor;
		else if (CostCurveAlgorithmBase.CODE_REHAB_CLUSTER_WASTEWATER_TREATMENT.equalsIgnoreCase(costCurveId))
			return rehabClusterWastewaterTreatmenCostInputProcessor;
		else if (CostCurveAlgorithmBase.CODE_NEW_SEPERATE_SEWER_COLLECTOR.equalsIgnoreCase(costCurveId)
				|| CostCurveAlgorithmBase.CODE_NEW_SEPERATE_SEWER_INTERCEPTOR.equalsIgnoreCase(costCurveId)
				)
			return newSeparateSewerCostInputProcessor;		
		else if (CostCurveAlgorithmBase.CODE_SEPERATE_SEWER_REHAB.equalsIgnoreCase(costCurveId))
			return rehabSeparateSewerCostInputProcessor;
		else if (CostCurveAlgorithmBase.CODE_COMBINED_SEWER_OVERFLOW.equalsIgnoreCase(costCurveId))
			return combinedSewerOverflowCostInputProcessor;		
		else if ((CostCurveAlgorithmBase.CODE_NEW_TREATMENT_PLANT.equalsIgnoreCase(costCurveId))
		 || (CostCurveAlgorithmBase.CODE_INCREASE_CAPACITY.equalsIgnoreCase(costCurveId))
		 || (CostCurveAlgorithmBase.CODE_INCREASE_TREATMENT.equalsIgnoreCase(costCurveId))
		 || (CostCurveAlgorithmBase.CODE_INCREASE_CAPACITY_TREATMENT.equalsIgnoreCase(costCurveId))
		 || (CostCurveAlgorithmBase.CODE_REPLACE_TREATMENT_PLANT.equalsIgnoreCase(costCurveId))
		 || (CostCurveAlgorithmBase.CODE_DISINFECTION_ONLY.equalsIgnoreCase(costCurveId))
		 || (CostCurveAlgorithmBase.CODE_REPLACE_TREATMENT_PLANT.equalsIgnoreCase(costCurveId))
		 ) 	
			return wastewaterTreatmentPlantCostInputProcessor;
		return wastewaterTreatmentPlantCostInputProcessor;
	}

	private CostCurveInputProcessor newIndividualWastewaterTreatmentAllCostInputProcessor;
	public void setNewIndividualWastewaterTreatmentAllCostInputProcessor(CostCurveInputProcessor ntpcip) {
		newIndividualWastewaterTreatmentAllCostInputProcessor = ntpcip;
	}
	
	private CostCurveInputProcessor newIndividualWastewaterTreatmentInnovativeCostInputProcessor;
	public void setNewIndividualWastewaterTreatmentInnovativeCostInputProcessor(CostCurveInputProcessor ntpcip) {
		newIndividualWastewaterTreatmentInnovativeCostInputProcessor = ntpcip;
	}	
			
	private CostCurveInputProcessor newIndividualWastewaterTreatmentConventionalCostInputProcessor;
	public void setNewIndividualWastewaterTreatmentConventionalCostInputProcessor(CostCurveInputProcessor ntpcip) {
		newIndividualWastewaterTreatmentConventionalCostInputProcessor = ntpcip;
	}		

	private CostCurveInputProcessor newClusterWastewaterTreatmentCostInputProcessor;
	public void setNewClusterWastewaterTreatmentCostInputProcessor(CostCurveInputProcessor ntpcip) {
		newClusterWastewaterTreatmentCostInputProcessor = ntpcip;
	}		

	private CostCurveInputProcessor rehabIndividualWastewaterTreatmentAllCostInputProcessor;
	public void setRehabIndividualWastewaterTreatmentAllCostInputProcessor(CostCurveInputProcessor ntpcip) {
		rehabIndividualWastewaterTreatmentAllCostInputProcessor = ntpcip;
	}		

	private CostCurveInputProcessor rehabIndividualWastewaterTreatmentConventionalCostInputProcessor;
	public void setRehabIndividualWastewaterTreatmentConventionalCostInputProcessor(CostCurveInputProcessor ntpcip) {
		rehabIndividualWastewaterTreatmentConventionalCostInputProcessor = ntpcip;
	}			
	
	private CostCurveInputProcessor rehabIndividualWastewaterTreatmentInnovativeCostInputProcessor;
	public void setRehabIndividualWastewaterTreatmentInnovativeCostInputProcessor(CostCurveInputProcessor ntpcip) {
		rehabIndividualWastewaterTreatmentInnovativeCostInputProcessor = ntpcip;
	}			

	private CostCurveInputProcessor rehabClusterWastewaterTreatmenCostInputProcessor;
	public void setRehabClusterWastewaterTreatmenCostInputProcessor(CostCurveInputProcessor ntpcip) {
		rehabClusterWastewaterTreatmenCostInputProcessor = ntpcip;
	}				
	
	private CostCurveInputProcessor newSeparateSewerCostInputProcessor;
	public void setNewSeparateSewerCostInputProcessor(
			CostCurveInputProcessor icp) {
		newSeparateSewerCostInputProcessor = icp;
	}
	
	
	private CostCurveInputProcessor rehabSeparateSewerCostInputProcessor;
	public void setRehabSeparateSewerCostInputProcessor(
			CostCurveInputProcessor icp) {
		rehabSeparateSewerCostInputProcessor = icp;
	}
	

	private CostCurveInputProcessor combinedSewerOverflowCostInputProcessor;
	public void setCombinedSewerOverflowCostInputProcessor(
			CostCurveInputProcessor icp) {
		combinedSewerOverflowCostInputProcessor = icp;
	}
	
	
	private CostCurveInputProcessor wastewaterTreatmentPlantCostInputProcessor;
	public void setWastewaterTreatmentPlantCostInputProcessor(CostCurveInputProcessor ntpcip) {
		wastewaterTreatmentPlantCostInputProcessor = ntpcip;
	}
	

	
}
