package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.costcurve.input.CombinedSewerSystemInput;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;

import java.util.ArrayList;
import java.util.List;


public class CombinedSewerOverflowAlgorithm extends CostCurveAlgorithmBase {		
	
	public CostCurveOutput execute(CostCurveInput costInput) throws CostCurveException {
		CombinedSewerSystemInput ntpi = (CombinedSewerSystemInput)costInput;
		int pprrc = ntpi.getPresentResidentialPopulation();

		// pprrc_cso = pprrc + upstream present resident recollection population 
		// + SUM(present resident recollection population for all facilities directly downstream)
		
		int pprrc_cso = pprrc + ntpi.getUpstreamPresentResidentReceivingPopulation()
							 + (int)ntpi.getSumDirectDownstreamFacilitiesPopulationByDischargeType();	

		float pprrc_res; 
		if (pprrc_cso == 0) pprrc_res = (float)0.0;
		else pprrc_res = (float)pprrc / (float)pprrc_cso;
		
		// 1) Calculate Area-wieght Average Combined Sewer Area
		int cspop = ntpi.getCostcurvePopulationCount();
		int csarea = ntpi.getCostcurveAreaSqMilesMsr();
		float csimp = ntpi.getImperviousRatio();
		
		if (csimp < 0 && csarea>0) {
			csimp = (float)(9.6 * Math.pow(cspop/csarea, (0.573 - 0.0391 * (Math.log(cspop/csarea)/Math.log(10)))));
		}
		
		if (csimp > 100) csimp = 100;
		
		// 2) Calculate Wet-weather Treatment

		double fpdtot = ntpi.getPresentTotalFlowForFacility();
		double fpdtot_dis = ntpi.getPresentTotalFlowForNextDownstreamFacilities();
		
		double wwcap = 0.0;
		
		if (ntpi.isOnlyFacilityInSewershed()) {
			if (ntpi.isTerminatingFacility()) {
				wwcap = 0.5 * fpdtot; 
			}
		} else {
			if (ntpi.isTerminatingFacility()) {
				wwcap = 0.5 * (pprrc_res * fpdtot); 
			} else {
				wwcap = 0.5 * (fpdtot + (pprrc_res * fpdtot_dis));
			}
		}
		
		wwcap = (Math.round(wwcap * 10000)) /10000.0;
		
		// 3) Retrieve Rainfall
		double rainfall = ntpi.getRainfall();
		
		// 4) Calculate Runoff Volume (RV)
		//TODO is that possible rv < 0????
		double rv = 0.8 * (csarea * (0.05 + (csimp / 111.11)) * rainfall * 0.027156 - (wwcap / 24));
		
		double n_sed = rv / 20 + 1;
		n_sed = (Math.round(n_sed * 10000)) /10000;
		n_sed = n_sed + 0.5;
		int tem_number = (int)n_sed;
		n_sed = tem_number + 1;
		double sedim = 5460 * n_sed * Math.pow((rv / n_sed), 0.821);
		
		// 5) Calculate Chlorination Cost
		double rate = rv * 24 / 0.8;
		double  chlor = 327 *  Math.pow(rate, 0.496);
				
		long cost = Math.round((chlor + sedim) * 1000);		
		
		CostCurveOutput costCurveOuput = new CostCurveOutput();						
		List costOutputs = new ArrayList();		
		
		CostOutput costOutput1 = new CostOutput();

		costOutput1.setCost(cost);
		costOutput1.setCategoryID("V");
		costOutputs.add(costOutput1);

		costCurveOuput.setCostOutputs(costOutputs);

		return costCurveOuput;		

	}
}
