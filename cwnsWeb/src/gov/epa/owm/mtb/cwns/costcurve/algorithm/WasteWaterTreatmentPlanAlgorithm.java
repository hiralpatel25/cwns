package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.WastewaterTreatmentPlantInput;
import gov.epa.owm.mtb.cwns.costcurve.service.TreatmentPlantCostCurveService;


public abstract class WasteWaterTreatmentPlanAlgorithm extends CostCurveAlgorithmBase {		
	
	protected TreatmentPlantCostCurveService treatmentPlantCostCurveService;

	public void setTreatmentPlantCostCurveService(
			TreatmentPlantCostCurveService treatmentPlantCostCurveService) {
		this.treatmentPlantCostCurveService = treatmentPlantCostCurveService;
	}
}
