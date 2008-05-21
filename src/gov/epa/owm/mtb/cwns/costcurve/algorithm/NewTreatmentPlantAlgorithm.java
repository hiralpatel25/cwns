package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import java.util.List;

import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.WastewaterTreatmentPlantInput;
import gov.epa.owm.mtb.cwns.costcurve.service.TreatmentPlantCostCurveService;

/**
 * New Treatment Plant
 * 
 * Category: 		I and II
 * 
 * 
 * 1. Get Regional Multipliers
 * 2. Compute Future GPCD (fgpcd)
 *		gpcd = fgpcd
 * 3. Compute Future Municipal Design Flow 
 *		Update future municipal design flow with fut_mun_flow_rate	
 *		Flow_rate = fut_mun_flow_rate
 * 4. Get Cost Curve Coefficient (based on future plant type and flow_rate)
 * 		Cost_base_amount  = coefficient_a_value * 1000 * (flow_rate ** coefficient_b_value)
 * 		If plant type = mechanical then do
 *			 Calculate Disinfection Costs 
 *			Cost_base_amount  = Cost_base_amount  + Disinfection_Cost_Amount
 *	If plant type = lagoon and effluent level = advanced then do
 *			 Calculate Filtration Costs  
 * 			Cost_base_amount  = Cost_base_amount  + Filtration_Cost_Amount
 * 5. Apply Treatment Multiplier and Split Cost
 *		Update Cat I & II costs
 * 
 */


public class NewTreatmentPlantAlgorithm extends
		WasteWaterTreatmentPlanAlgorithm {

	public CostCurveOutput execute(CostCurveInput costInput) throws CostCurveException {
		WastewaterTreatmentPlantInput ntpi = (WastewaterTreatmentPlantInput)costInput;
		
		long countyId = ntpi.getCountyId();
		double multiplier = treatmentPlantCostCurveService.getMuliplier(countyId);
		int fgpcd = treatmentPlantCostCurveService.computeProjectedGPCD(ntpi.getProjectedResidentialPopulation());
		float flowRate = treatmentPlantCostCurveService.computeFutureMunicipalDesignFlow(
				ntpi.getProjectedNonResidentialReceivingPopulation(),
				ntpi.getProjectedUpstreamNonResidentialReceivingPopulation(),
				ntpi.getProjectedResidentialReceivingPopulation(),
				ntpi.getProjectedUpstreamResidentialReceivingPopulation(),
				fgpcd
			);
		
		char projectedPlantType = ntpi.getProjectedPlantType();
		
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
		float coefficientAValue = treatmentPlantCostCurveService.getCoefficientAValue(
				treatmentCurveType, flowRate);
		float coefficientBValue = treatmentPlantCostCurveService.getCoefficientBValue(
				treatmentCurveType, flowRate);

		double cost_base_amount = treatmentPlantCostCurveService.computeCostByPlantType(
				projectedPlantType, flowRate, coefficientAValue, coefficientBValue, 
				projectedFacilityEffluentType);
		
		CostCurveOutput costCurveOuput = new CostCurveOutput();
		costCurveOuput.setFutureMunicipalFlowRate(flowRate);
				
		int catISplitPercentage = treatmentPlantCostCurveService.getCatISplitPercentage(projectedPlantType, 
				projectedFacilityEffluentType, projectedFacilityEffluenTypeAdvancedTreatmentSubtype);		

		//calculate costs
		List costOupts = treatmentPlantCostCurveService.applyMultiplierSplitCost(cost_base_amount, 
				multiplier, catISplitPercentage);

		costCurveOuput.setCostOutputs(costOupts);
		
		return costCurveOuput;
	}

}
