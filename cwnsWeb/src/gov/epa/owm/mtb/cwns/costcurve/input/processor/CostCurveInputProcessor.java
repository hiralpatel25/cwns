package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;

public interface CostCurveInputProcessor  {
	
	public CostCurveInput getCostInputData(Long facilityId);
		 
	

}
