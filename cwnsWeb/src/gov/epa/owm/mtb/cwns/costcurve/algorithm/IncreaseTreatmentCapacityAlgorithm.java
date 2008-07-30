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
 * Increate Capacity
 * 
 * Category: 		I and II
 * 
 * - If present plant type = lagoon and future plant type = mechanical, 
 *   goto Replace Treatment Plant  and skip the rest of this section
 *   
 * - Error trap: If present plant type <> future plant type, then send user
 *   error message “Cannot use cost curves to upgrade specified plant types”
 *   
 * - Error trap: If future plant type = lagoon then present and future effluent needs 
 * 	 to be secondary and advanced, respectively, else send user error message 
 *   "Cannot use cost curve to upgrade this lagoon"
 *   
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
 * 3. Compute Future Municipal Design Flow 
 *      Error trap: if fut_mun_flow_rate is less than present municipal design flow rate 
 *      or if after update, the total future design flow rate would be less than the 
 *      total present design flow rate, then do not proceed, spit out an error message 
 *      "Computed future design flow is less than present design flow, cannot proceed". 
 *     
 *     Flow_rate = fut_mun_flow_rate    
 *
 * For Lagoons
 * If plant type = lagoon then do
 * 4. Compute initial costs
 *    a) Get Cost Curve Coefficient (based on future plant type and flow_rate)
 *       Cost_base_amount  = coefficient_a_value * 1000 * (flow_rate ** coefficient_b_value)
 *    b) Calculate Filtration Costs
 *       Cost_base_amount  = Cost_base_amount  + Filtration_Cost_Amount
 * 5. Deductions
 * Flow_rate = present municipal design flow rate
 *    a) Get Cost Curve Coefficient (based on present plant type and flow_rate)
 *       Deduction_amount  = coefficient_a_value * 1000 * (flow_rate ** coefficient_b_value)
 *    b) Cost_base_amount  = Cost_base_amount - Deduction_amount
 *    	Cat II = Filtration_Cost_Amount
 *    	Cat I = Cost_base_amount - Filtration_Cost_Amount
 *    Error trap: make sure Cat I and II are >= 0
 *  Skip rest
 *  
 * For Mechanical
 *  If plant type = mechanical then
 *  5a. Get Future and Salvage Treatment Curve Types
 *  6a. Compute initial costs
 *      Flow_rate = fun_mun_flow_rate
 *  	Get Cost Curve Coefficient based on Future Treatment Curve Type and flow_rate
 *  	Cost_base_amount  = coefficient_a_value * 1000 * (flow_rate ** coefficient_b_value)
 *  6a. Deductions
 *  	Flow_rate = present municipal design flow rate
 *  	Get Cost Curve Coefficient based on Salvage Treatment Curve Type and flow_rate
 *  	Deduction_amount  = coefficient_a_value * 1000 * (flow_rate ** coefficient_b_value)
 *  	Cost_base_amount  = Cost_base_amount - Deduction_amount
 *  7a. Get Upgrade split 
 * 		Apply Treatment Multiplier and Split Cost
 */

public class IncreaseTreatmentCapacityAlgorithm extends
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
		
		if (futureMunicipalFlowRate < presentFlowRate)
			throw new CostCurveException("Computed future design flow is less than present design flow, cannot proceed");
		
		
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

		int projectedFacilityEffluenType = ntpi.getProjectedFacilityEffluenType();
		
		CostCurveOutput costCurveOuput = new CostCurveOutput();
		costCurveOuput.setFutureMunicipalFlowRate(futureMunicipalFlowRate);
		
		double cost_base_amount;		
		if (projectedPlantType == TreatmentPlantCostCurveService.PLANT_TYPE_LOGOON) {
			
			String projectedTreatmentCurveType = treatmentPlantCostCurveService.getTreatmentCurveType(projectedPlantType,
					projectedFacilityEffluenType, projectedFacilityEffluenTypeAdvancedTreatmentSubtype);
			float projectedCoefficientAValue = treatmentPlantCostCurveService.getCoefficientAValue(
						projectedTreatmentCurveType, flowRate);
			float projectedCoefficientBValue = treatmentPlantCostCurveService.getCoefficientBValue(
						projectedTreatmentCurveType, flowRate);			
			
			double initial_cost_base_amount = treatmentPlantCostCurveService.computeBasicCost(
				flowRate, projectedCoefficientAValue, projectedCoefficientBValue);
			double filtrationCosts = treatmentPlantCostCurveService.computeFiltrationCosts(flowRate);
			initial_cost_base_amount = initial_cost_base_amount + filtrationCosts; 

			flowRate = presentFlowRate;
			String presentTreatmentCurveType = treatmentPlantCostCurveService.getTreatmentCurveType(presentPlantType,
					presentFacilityEffluentType, presentFacilityEffluenTypeAdvancedTreatmentSubtype);
			float presentCoefficientAValue = treatmentPlantCostCurveService.getCoefficientAValue(
					presentTreatmentCurveType, flowRate);
			float presentCoefficientBValue = treatmentPlantCostCurveService.getCoefficientBValue(
					presentTreatmentCurveType, flowRate);			
			
			double deductions = treatmentPlantCostCurveService.computeBasicCost
					(flowRate, presentCoefficientAValue, presentCoefficientBValue);
			cost_base_amount = initial_cost_base_amount - deductions; 
			
			List costOutputs = new ArrayList();

			CostOutput costOutput1 = new CostOutput();						
			
//			long cost1 = (long)cost_base_amount - Math.round(filtrationCosts * 1000 * multiplier);
			long cost1 = Math.round((cost_base_amount - filtrationCosts) * 1000 * multiplier);
			costOutput1.setCost(cost1);
			costOutput1.setCategoryID("I");
			costOutputs.add(costOutput1);

			CostOutput costOutput2 = new CostOutput();
			costOutput2.setCost(Math.round(filtrationCosts * 1000 * multiplier));
			costOutput2.setCategoryID("II");
			costOutputs.add(costOutput2);
						
			costCurveOuput.setCostOutputs(costOutputs);
			return costCurveOuput;	
			
		} else if (projectedPlantType == TreatmentPlantCostCurveService.PLANT_TYPE_MECHANICAL) {
			
			
			String futureTreatmentCurveType = treatmentPlantCostCurveService.getFutureTreatmentCurveType
					(presentFacilityEffluentType, presentFacilityEffluenTypeAdvancedTreatmentSubtype,
							projectedFacilityEffluentType, projectedFacilityEffluenTypeAdvancedTreatmentSubtype);
			
			// compute initial costs
			flowRate = futureMunicipalFlowRate;
			
			float coefficientAValue = treatmentPlantCostCurveService.getCoefficientAValue(
					futureTreatmentCurveType, flowRate);
			float coefficientBValue = treatmentPlantCostCurveService.getCoefficientBValue(
					futureTreatmentCurveType, flowRate);

			cost_base_amount = treatmentPlantCostCurveService.computeBasicCost(
								flowRate, coefficientAValue, coefficientBValue);
			
			// deductions
			flowRate = presentFlowRate;
			String salvageTreatmentCurveType = treatmentPlantCostCurveService.getSalvageTreatmentCurveType
				(presentFacilityEffluentType, presentFacilityEffluenTypeAdvancedTreatmentSubtype,
					projectedFacilityEffluentType, projectedFacilityEffluenTypeAdvancedTreatmentSubtype);
			coefficientAValue = treatmentPlantCostCurveService.getCoefficientAValue(
					salvageTreatmentCurveType, flowRate);
			coefficientBValue = treatmentPlantCostCurveService.getCoefficientBValue(
					salvageTreatmentCurveType, flowRate);

			double deduction_amount = treatmentPlantCostCurveService.computeBasicCost(
				flowRate, coefficientAValue, coefficientBValue);
			
			cost_base_amount =  cost_base_amount - deduction_amount;
			
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
