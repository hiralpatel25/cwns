package gov.epa.owm.mtb.cwns.costcurve.algorithm;

import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;

import java.util.List;

public interface CostCurveAlgorithm {
  
	public CostCurveOutput execute(CostCurveInput costInput) throws CostCurveException; 

}
