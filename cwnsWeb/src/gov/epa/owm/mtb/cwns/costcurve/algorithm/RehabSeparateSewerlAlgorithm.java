package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.costcurve.input.CombinedSewerSystemInput;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.OnsiteWastewaterTreatmentSystemInput;
import gov.epa.owm.mtb.cwns.costcurve.input.SeparateSewerSystemInput;
import gov.epa.owm.mtb.cwns.costcurve.input.WastewaterTreatmentPlantInput;
import gov.epa.owm.mtb.cwns.costcurve.service.TreatmentPlantCostCurveService;

import java.util.ArrayList;
import java.util.List;

/**
 * Rehab Separate Sewer System
 * 
 * 
 */


public class RehabSeparateSewerlAlgorithm extends
		SeparateSewerAlgorithm {

	public CostCurveOutput execute(CostCurveInput costInput) throws CostCurveException {
		SeparateSewerSystemInput ntpi = (SeparateSewerSystemInput)costInput;		

		int x = ntpi.getPresentPopulation();
		
		int y = separateSewerCostCurveService.getCostPopulation(x);
		int b = separateSewerCostCurveService.getModelCoefficient(x);

		long countyId = ntpi.getCountyId(); 
		float z = separateSewerCostCurveService.getSewerMuliplier(countyId);		
			
		//calculate costs
	
		CostCurveOutput costCurveOuput = new CostCurveOutput();		
		
		CostOutput costCurveOutput2 = new CostOutput();
		long cost = (long)Math.round((((x * y) + b) * z)); 
		costCurveOutput2.setCost(cost);
		costCurveOutput2.setCategoryID("III-B");
		costCurveOuput.addCostOutput(costCurveOutput2);

		
		return costCurveOuput;
	}
	

}
