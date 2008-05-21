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
 * New/Expand Separate Sewer System - Collectors
 * 
 * Category: 		IV-A (63%) and IV-B (37%)
 * Algorithm: 		Cost ($) = 55492 * X0.6036 * Z
 * 
 */


public class NewExpandSeparateSewerCollectorAlgorithm extends
		SeparateSewerAlgorithm {

	public CostCurveOutput execute(CostCurveInput costInput) throws CostCurveException {
		SeparateSewerSystemInput ntpi = (SeparateSewerSystemInput)costInput;		

		int x = ntpi.getPresentPopulation();

		long countyId = ntpi.getCountyId(); 
		float z = separateSewerCostCurveService.getSewerMuliplier(countyId);		
			
		//calculate costs
		double cost = 55492 * Math.pow(x, 0.6036) * z  *  0.63; 
		CostCurveOutput costCurveOuput = new CostCurveOutput();
		
		CostOutput costCurveOutput1 = new CostOutput();
		long cost1 = Math.round(cost);
		costCurveOutput1.setCost(cost1);
		costCurveOutput1.setCategoryID("IV-A");
		costCurveOuput.addCostOutput(costCurveOutput1);
				
		return costCurveOuput;
	}
	

}
