package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import gov.epa.owm.mtb.cwns.costcurve.service.OnsiteWastewaterTreatmentCostcurveService;


public abstract class OnsiteWastewaterTreatmentAlgorithm extends
		CostCurveAlgorithmBase {
	
	protected OnsiteWastewaterTreatmentCostcurveService onsiteWastewaterTreatmentCostcurveService;
	public void setOnsiteWastewaterTreatmentCostcurveService(
			OnsiteWastewaterTreatmentCostcurveService treatmentPlantCostCurveService) {
		this.onsiteWastewaterTreatmentCostcurveService = treatmentPlantCostCurveService;
	}
}
