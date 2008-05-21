package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import java.util.List;

import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.WastewaterTreatmentPlantInput;
import gov.epa.owm.mtb.cwns.costcurve.service.TreatmentPlantCostCurveService;

/**
 * Disinfection Only
 * Category: 		I and II
 *  
 */


public class DisinfectionOnlyAlgorithm extends
		WasteWaterTreatmentPlanAlgorithm {

	public CostCurveOutput execute(CostCurveInput costInput) throws CostCurveException {
		WastewaterTreatmentPlantInput ntpi = (WastewaterTreatmentPlantInput)costInput;
		
		long countyId = ntpi.getCountyId();
		
		// Get Regional Multipliers
		double multiplier = treatmentPlantCostCurveService.getMuliplier(countyId);
		
		// Compute Projected GPCD (fgpcd)
		int fgpcd = treatmentPlantCostCurveService.computeProjectedGPCD(ntpi.getProjectedResidentialPopulation());
		
		// Compute Projected Municipal Design Flow
		float flowRate = treatmentPlantCostCurveService.computeFutureMunicipalDesignFlow(
				ntpi.getProjectedNonResidentialReceivingPopulation(),
				ntpi.getProjectedUpstreamNonResidentialReceivingPopulation(),
				ntpi.getProjectedResidentialReceivingPopulation(),
				ntpi.getProjectedUpstreamResidentialReceivingPopulation(),
				fgpcd
			);		
		
		// Calculate Disinfection Costs
		double cost_base_amount = treatmentPlantCostCurveService.computeDisinfectionCosts(flowRate);
							
		CostCurveOutput costCurveOuput = new CostCurveOutput();
		costCurveOuput.setFutureMunicipalFlowRate(flowRate);
				
		//calculate costs
		List costOupts = treatmentPlantCostCurveService.applyMultiplierSplitCost(cost_base_amount, 
				multiplier, 100);

		costCurveOuput.setCostOutputs(costOupts);
		
		return costCurveOuput;
	}

}
