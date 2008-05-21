package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import gov.epa.owm.mtb.cwns.costcurve.service.SeparateSewerCostCurveService;


public abstract class SeparateSewerAlgorithm extends
		CostCurveAlgorithmBase {
	
	protected SeparateSewerCostCurveService separateSewerCostCurveService;
	public void setSeparateSewerCostCurveService(
			SeparateSewerCostCurveService s) {
		this.separateSewerCostCurveService = s;
	}
}
