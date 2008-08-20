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
 * 
 * 1. Get Regional Multipliers
 * 2. Compute Future GPCD (fgpcd)
 *		gpcd = fgpcd
 * 3. Compute Future Municipal Design Flow 
 * 		Error trap: if fut_mun_flow_rate is less than present municipal design flow rate 
 * or if after update, the total future design flow rate would be less than the total 
 * present design flow rate, then do not proceed, spit out an error 
 * message "Computed future design flow is less than present design flow, cannot proceed".
 * 		Update future municipal design flow with fut_mun_flow_rate
 * 
 * 4. Compute initial costs
 * 	Flow_rate = fut_mun_flow_rate
 * 	Get Cost Curve Coefficient (based on future plant type and flow_rate)
 * 		Cost_base_amount  = coefficient_a_value * 1000 * (flow_rate ** coefficient_b_value)
 * 		If plant type = mechanical then do
 * 			Calculate Disinfection Costs
 * 			Cost_base_amount  = Cost_base_amount  + Disinfection_Cost_Amount
 * 		If plant type = lagoon and effluent level = advanced then do
 * 			Calculate Filtration Costs
 * 			Cost_base_amount  = Cost_base_amount  + Filtration_Cost_Amount
 * 
 * 5.Deductions
 * 		Flow_rate = present municipal design flow rate
 * 		Get Cost Curve Coefficient (based on present plant type and flow_rate)
 * 			Deduction_amount  = coefficient_a_value * 1000 * (flow_rate ** coefficient_b_value)
 * 			If plant type = mechanical then do
 * 				Calculate Disinfection Deduction
 * 			Deduction_amount  = Deduction_amount  + Disinfection_Cost_Amount
 * 		If plant type = lagoon and effluent level = advanced then do
 * 				Calculate Filtration Costs
 * 				Deduction_amount  = Deduction_amount  + Filtration_Cost_Amount
 * 
 * 6. Cost_base_amount  = Cost_base_amount - Deduction_amount
 * 		Apply Treatment Multiplier and Split Cost
 * 		Update Cat I & II costs


 * 
 */


public class IncreaseCapacityAlgorithm extends
		WasteWaterTreatmentPlanAlgorithm {

	public CostCurveOutput execute(CostCurveInput costInput) throws CostCurveException {

		WastewaterTreatmentPlantInput ntpi = (WastewaterTreatmentPlantInput)costInput;
		
		long countyId = ntpi.getCountyId();
		double multiplier = treatmentPlantCostCurveService.getMuliplier(countyId);
		int fgpcd = treatmentPlantCostCurveService.computeProjectedGPCD(ntpi.getProjectedResidentialPopulation());
		float presentFlowRate = ntpi.getPresentFlowRate();
		
		float futureMunicipalFlowRate = treatmentPlantCostCurveService.computeFutureMunicipalDesignFlow(
				ntpi.getProjectedNonResidentialReceivingPopulation(),
				ntpi.getProjectedUpstreamNonResidentialReceivingPopulation(),
				ntpi.getProjectedResidentialReceivingPopulation(),
				ntpi.getProjectedUpstreamResidentialReceivingPopulation(),
				fgpcd
			);
		
		if (futureMunicipalFlowRate < presentFlowRate)
			throw new CostCurveException("Computed future design flow is less than present design flow, cannot proceed");
		
		float flowRate = futureMunicipalFlowRate;
		
		char projectedPlantType = ntpi.getProjectedPlantType();
		char presentPlantType = ntpi.getPresentPlantType(); 
	
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
		
		String treatmentCurveType = treatmentPlantCostCurveService.getTreatmentCurveType(projectedPlantType,
								projectedFacilityEffluentType, projectedFacilityEffluenTypeAdvancedTreatmentSubtype);
		float projectedCoefficientAValue = treatmentPlantCostCurveService.getCoefficientAValue(
				treatmentCurveType, flowRate);
		float projectedCoefficientBValue = treatmentPlantCostCurveService.getCoefficientBValue(
				treatmentCurveType, flowRate);

		double initial_cost_base_amount = treatmentPlantCostCurveService.computeCostByPlantType(
				projectedPlantType, flowRate, projectedCoefficientAValue, projectedCoefficientBValue, 
				projectedFacilityEffluentType);
						
		float presentCoefficientAValue = treatmentPlantCostCurveService.getCoefficientAValue(
				treatmentCurveType, presentFlowRate);
		float presentCoefficientBValue = treatmentPlantCostCurveService.getCoefficientBValue(
				treatmentCurveType, presentFlowRate);
				
		double deductions = treatmentPlantCostCurveService.computeCostByPlantType(
				presentPlantType, presentFlowRate, presentCoefficientAValue, presentCoefficientBValue, projectedFacilityEffluentType);
		
		double deduction_amount = Math.min(deductions, 0.85 * initial_cost_base_amount);
		double cost_base_amount = initial_cost_base_amount - deduction_amount;
			
		int catISplitPercentage = treatmentPlantCostCurveService.getCatISplitPercentage(
				projectedPlantType, projectedFacilityEffluentType, projectedFacilityEffluenTypeAdvancedTreatmentSubtype);		

		CostCurveOutput costCurveOuput = new CostCurveOutput();		
		costCurveOuput.setFutureMunicipalFlowRate(futureMunicipalFlowRate);
		
		//calculate costs
		List costOupts = treatmentPlantCostCurveService.applyMultiplierSplitCost(cost_base_amount, 
				multiplier, catISplitPercentage);

		costCurveOuput.setCostOutputs(costOupts);

		return costCurveOuput;		
		
	}
	
	

}
