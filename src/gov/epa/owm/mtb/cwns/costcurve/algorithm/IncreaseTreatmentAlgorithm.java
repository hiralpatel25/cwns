package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.WastewaterTreatmentPlantInput;
import gov.epa.owm.mtb.cwns.costcurve.service.TreatmentPlantCostCurveService;

import java.util.ArrayList;
import java.util.List;

/**
 * Increate Treatment
 * 
 * Category: 		I and II
 * 
 * - If present plant type = lagoon and future plant type = mechanical, 
 *   goto Replace Treatment Plant  and skip the rest of this section
 * - Error trap: If present plant type <> future plant type, then send user
 *   error message “Cannot use cost curves to upgrade specified plant types”
 * - Error trap: If future plant type = lagoon then present and future effluent needs 
 * 	 to be secondary and advanced, respectively, else send user error message 
 *   "Cannot use cost curve to upgrade this lagoon"
 * - Error trap: If future plant type = mechanical then present and future effluent 
 *   levels must be one of the following such that future effluent is further down the list 
 *   than the present effluent (if any other combination then send user error message 
 *   “cannot use cost curve to upgrade this mechanical treatment plant”)
 * 			Effluent Level (note N/P removal flags)
 *			Primary or Advanced Primary
 *			Secondary
 *			Advanced (and no N/P removal)
 *			Advanced with nitrogen or phosphorus removal
 *			Advanced with nitrogen and phosphorus removal
 * 
 * 1. Get Regional Multipliers
 * 2. Compute Future GPCD (fgpcd)
 *		gpcd = fgpcd
 *      Flow_rate = fut_mun_flow_rate    
 *
 * For Lagoons
 * If plant type = lagoon then do
 * 3. Calculate Filtration Costs
 * 		Cost_base_amount  = Filtration_Cost_Amount
 * 		Set Category I and II split to 0 and 100, respectively
 * 4. Apply Treatment Multiplier and Split Cost
 * 	Skip the rest
 * 
 * For Mechanical
 *  If plant type = mechanical then
 *  3a. Get Future and Salvage Treatment Curve Types
 *  4a. Compute initial costs
 *  	Get Cost Curve Coefficient based on Future Treatment Curve Type and flow_rate
 *  	Cost_base_amount  = coefficient_a_value * 1000 * (flow_rate ** coefficient_b_value)
 *  5a. Deductions
 *  	Get Cost Curve Coefficient based on Salvage Treatment Curve Type and flow_rate
 *  	Deduction_amount  = coefficient_a_value * 1000 * (flow_rate ** coefficient_b_value)
 *  	Cost_base_amount  = Cost_base_amount - Deduction_amount
 *  6a. Get Upgrade split 
 * 		Apply Treatment Multiplier and Split Cost
 */

public class IncreaseTreatmentAlgorithm extends
		WasteWaterTreatmentPlanAlgorithm {

	public CostCurveOutput execute(CostCurveInput costInput) throws CostCurveException {

		WastewaterTreatmentPlantInput ntpi = (WastewaterTreatmentPlantInput)costInput;
		
		long countyId = ntpi.getCountyId();
		double multiplier = treatmentPlantCostCurveService.getMuliplier(countyId);
		int fgpcd = treatmentPlantCostCurveService.computeProjectedGPCD(ntpi.getProjectedResidentialPopulation());
		float futureMunicipalFlowRate = treatmentPlantCostCurveService.computeFutureMunicipalDesignFlow(
				ntpi.getProjectedNonResidentialReceivingPopulation(),
				ntpi.getProjectedUpstreamNonResidentialReceivingPopulation(),
				ntpi.getProjectedResidentialReceivingPopulation(),
				ntpi.getProjectedUpstreamResidentialReceivingPopulation(),
				fgpcd
			);
		float presentFlowRate = ntpi.getPresentFlowRate();
		
		float flowRate = futureMunicipalFlowRate;
		
		char projectedPlantType = ntpi.getProjectedPlantType();
		char presentPlantType = ntpi.getPresentPlantType();
		
		if (presentPlantType == TreatmentPlantCostCurveService.PLANT_TYPE_LOGOON 
		   && projectedPlantType == TreatmentPlantCostCurveService.PLANT_TYPE_MECHANICAL ) {
			throw new CostCurveException("Please Call Replace Treatment Plant Algorithm");
		}
		
		if (presentPlantType != projectedPlantType ) {
			throw new CostCurveException("Cannot use cost curves to upgrade specified plant types");
		}		
		
		
		int projectedFacilityEffluentType = ntpi.getProjectedFacilityEffluenType();
		int projectedFacilityEffluenTypeAdvancedTreatmentSubtype = 0;
		if (projectedFacilityEffluentType == TreatmentPlantCostCurveService.EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT) {
			projectedFacilityEffluenTypeAdvancedTreatmentSubtype = ntpi.getProjectedFacilityEffluenTypeAdvancedTreatmentSubtype();
		}
		
		int presentFacilityEffluentType = ntpi.getPresentFacilityEffluenType();
		int presentFacilityEffluenTypeAdvancedTreatmentSubtype = 0;
		if (presentFacilityEffluentType == TreatmentPlantCostCurveService.EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT) {
			presentFacilityEffluenTypeAdvancedTreatmentSubtype = ntpi.getPresentFacilityEffluenTypeAdvancedTreatmentSubtype();
		}		
				

		CostCurveOutput costCurveOuput = new CostCurveOutput();
//		costCurveOuput.setFutureMunicipalFlowRate(0);
		
		double cost_base_amount;		
		if (projectedPlantType == TreatmentPlantCostCurveService.PLANT_TYPE_LOGOON) {					
			
			cost_base_amount = treatmentPlantCostCurveService.computeFiltrationCosts(flowRate);
		
			//calculate costs
			List costOupts = treatmentPlantCostCurveService.applyMultiplierSplitCost(cost_base_amount, 
					multiplier, 0);
			costCurveOuput.setCostOutputs(costOupts);
			return costCurveOuput;	
			
		} else if (projectedPlantType == TreatmentPlantCostCurveService.PLANT_TYPE_MECHANICAL) {
			String futureTreatmentCurveType = treatmentPlantCostCurveService.getFutureTreatmentCurveType
				(presentFacilityEffluentType, presentFacilityEffluenTypeAdvancedTreatmentSubtype,
					projectedFacilityEffluentType, projectedFacilityEffluenTypeAdvancedTreatmentSubtype);
			float coefficientAValue = treatmentPlantCostCurveService.getCoefficientAValue(
					futureTreatmentCurveType, flowRate);
			float coefficientBValue = treatmentPlantCostCurveService.getCoefficientBValue(
					futureTreatmentCurveType, flowRate);

			cost_base_amount = treatmentPlantCostCurveService.computeBasicCost(
								flowRate, coefficientAValue, coefficientBValue);
			
			//flowRate = presentFlowRate;
			String salvageTreatmentCurveType = treatmentPlantCostCurveService.getSalvageTreatmentCurveType
				(presentFacilityEffluentType, presentFacilityEffluenTypeAdvancedTreatmentSubtype,
					projectedFacilityEffluentType, projectedFacilityEffluenTypeAdvancedTreatmentSubtype);
			coefficientAValue = treatmentPlantCostCurveService.getCoefficientAValue(
					salvageTreatmentCurveType, flowRate);
			coefficientBValue = treatmentPlantCostCurveService.getCoefficientBValue(
					salvageTreatmentCurveType, flowRate);

			double deduction_amount = treatmentPlantCostCurveService.computeBasicCost(
				flowRate, coefficientAValue, coefficientBValue);
			
			//make sure deduction_amount is no greater than 85% of the cost_base_amount
			
			double deduction = Math.min(deduction_amount, 0.85 * cost_base_amount);			
			cost_base_amount =  cost_base_amount - deduction;
			
			int catISplitPercentage = treatmentPlantCostCurveService.getUpgradeCatISplitPercentage
				(presentFacilityEffluentType, presentFacilityEffluenTypeAdvancedTreatmentSubtype,
					projectedFacilityEffluentType, projectedFacilityEffluenTypeAdvancedTreatmentSubtype);			
			
			//calculate costs
			List costOupts = treatmentPlantCostCurveService.applyMultiplierSplitCost(cost_base_amount, 
					multiplier, catISplitPercentage);

			costCurveOuput.setCostOutputs(costOupts);

			return costCurveOuput;		
		}
		
		return costCurveOuput;
	}
	
	

}
