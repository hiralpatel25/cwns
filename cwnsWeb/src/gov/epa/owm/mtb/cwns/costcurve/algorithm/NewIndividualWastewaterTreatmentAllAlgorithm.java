package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.costcurve.input.CombinedSewerSystemInput;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.OnsiteWastewaterTreatmentSystemInput;
import gov.epa.owm.mtb.cwns.costcurve.input.WastewaterTreatmentPlantInput;
import gov.epa.owm.mtb.cwns.costcurve.service.TreatmentPlantCostCurveService;

import java.util.ArrayList;
import java.util.List;

/**
 * New Onsite Wastewater Treatment System - all
 * 
 * Category: 		XII	
 * where 
 * X1 = Residential Number of Units:
 * 			Validation:  	must be <= Projected - Present Residential Onsite Onsite Number of Units
 * Y1 = Residential Population per household
 * X2 = Non-residential Number of Units:		User entered
 * 		Validation:  	must be <= Projected  - Present Non-residential Onsite Number of Units
 * Y2 = Non-residential Population per household
 * Z = Onsite Multiplier Ratio for primary County
 * 
 */


public class NewIndividualWastewaterTreatmentAllAlgorithm extends
	OnsiteWastewaterTreatmentAlgorithm {

	public CostCurveOutput execute(CostCurveInput costInput) throws CostCurveException {
		OnsiteWastewaterTreatmentSystemInput ntpi = (OnsiteWastewaterTreatmentSystemInput)costInput;
		

		int x1 = ntpi.getResidentialNumberOfHouses();
		int x2 = ntpi.getNonResidentialNumberOfHouses();

		long countyId = ntpi.getCountyId(); 
		float y1 = ntpi.getResidentialPopulationPerHousehold();
		float y2 = ntpi.getNonResidentialPopulationPerHousehold();
		float z = onsiteWastewaterTreatmentCostcurveService.getOnsiteMuliplier(countyId);
			
		//calculate costs

		CostOutput costCurveOutput1 = new CostOutput();
		long cost1 = Math.round(3751 * ((x1 * y1) + (x2 * y2)) * z);
		costCurveOutput1.setCost(cost1);
		costCurveOutput1.setCategoryID("XII");
		CostCurveOutput costCurveOuput = new CostCurveOutput();
		costCurveOuput.addCostOutput(costCurveOutput1);
		
		return costCurveOuput;
	}
	

}
