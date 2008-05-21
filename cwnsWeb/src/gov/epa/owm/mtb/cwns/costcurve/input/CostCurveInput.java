package gov.epa.owm.mtb.cwns.costcurve.input;

import java.util.HashMap;
import java.util.Map;

public class CostCurveInput {
	
	private Map dataMap = new HashMap();
	
	public Object getData(Object key){
		return dataMap.get(key);		
	}
	
	public void setData(Object key, Object value){
		dataMap.put(key, value);		
	}
}
