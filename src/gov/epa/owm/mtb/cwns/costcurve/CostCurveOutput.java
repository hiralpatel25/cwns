package gov.epa.owm.mtb.cwns.costcurve;

import java.util.ArrayList;
import java.util.List;

/**
 * Container of Final Cost output
 * @author hchen3
 *
 */
public class CostCurveOutput {
	
	public List costOutputs = new ArrayList();
	
	public float futureMunicipalFlowRate;

	public List getCostOutputs() {
		return costOutputs;
	}

	public void addCostOutput(CostOutput costOutput) {
		costOutputs.add(costOutput);
	}		
	
	public void setCostOutputs(List costOutputs) {
		this.costOutputs = costOutputs;
	}

	public float getFutureMunicipalFlowRate() {
		return futureMunicipalFlowRate;
	}

	public void setFutureMunicipalFlowRate(float futureMunicipalFlowRate) {
		this.futureMunicipalFlowRate = futureMunicipalFlowRate;
	}
	
	
	

}
