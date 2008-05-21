package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.model.CwnsInfoRef;
import gov.epa.owm.mtb.cwns.model.FacilityFlow;
import java.util.Collection;

import java.util.HashMap;

public interface FlowService {
	
	public static final Long FLOW_TYPE_MUNICIPAL = new Long(1);
	public static final Long FLOW_TYPE_INDUSTRIAL = new Long(2);
	public static final Long FLOW_TYPE_INFILTRATION = new Long(3);
	public static final Long FLOW_TYPE_WET_WEATHER_PEAK = new Long(4);	
	public static final Long FLOW_TYPE_TOTAL = new Long(5);
	
	public Collection getFlowRefs();
	
	public String isPreFlowUpdatable(Long facilityId);
	
	public String isProFlowUpdatable(Long facilityId);
	
	public HashMap getIndividualFlowValues(Long facilityId);
	
	public double getPresentTotalFlow(Long facilityId);
	
	public HashMap getTotalFlowValues(Long facilityId);
	
	public float getMunicipalPresentFlowRate(Long facilityId);
	
	public float getMunicipalProjectedFlowRate(Long facilityId);

	public CwnsInfoRef getCwnsInfoRef();

	public void saveTotalFlowInfo(String surveyFacilityId, HashMap map, String userId);

	public void saveIndividualFlowInfo(String surveyFacilityId, HashMap map, String userId);

	public boolean isPreDesignFlowUpdatable(String facilityId);

	public void saveWWPFlow(String surveyFacilityId, double[] wwpFlow, String userId);

	public HashMap getWWPFlowValues(Long facilityId);
	
	//checks if the flow information exists for this facility
	public boolean isFlowExist(Long facilityId);
	
	//get the flow for a given flow type and facility
	public FacilityFlow getFacilityFlow(Long facilityId, Long flowTypeId);
	
	public boolean isFlowToRationValid(Long facilityId);
	
	public boolean isProMunicipalFlowUpdatable(Long facilityId);
	
	public boolean isTotalFlowUpdatable(Long facilityId);
}
