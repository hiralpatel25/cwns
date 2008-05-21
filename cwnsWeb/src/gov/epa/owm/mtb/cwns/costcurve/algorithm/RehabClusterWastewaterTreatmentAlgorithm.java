package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.OnsiteWastewaterTreatmentSystemInput;
import gov.epa.owm.mtb.cwns.costcurve.input.WastewaterTreatmentPlantInput;
import gov.epa.owm.mtb.cwns.costcurve.service.TreatmentPlantCostCurveService;

import java.util.ArrayList;
import java.util.List;

/**
 * Rehab Individual Wastewater Treament System - Innovative
 * 
 * Category: 		XII
 * 
 */


public class RehabClusterWastewaterTreatmentAlgorithm extends
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
		long cost1 = Math.round(3742 * ((x1 * y1) + (x2 * y2)) * z);		

		costCurveOutput1.setCost(cost1);
		costCurveOutput1.setCategoryID("XII");
		
		CostCurveOutput costCurveOuput = new CostCurveOutput();
		costCurveOuput.addCostOutput(costCurveOutput1);
		
		return costCurveOuput;
	}
	

}
