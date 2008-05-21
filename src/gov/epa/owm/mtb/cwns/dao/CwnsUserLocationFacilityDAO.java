package gov.epa.owm.mtb.cwns.dao;

import java.util.List;

import gov.epa.owm.mtb.cwns.model.CwnsUserLocationFacility;

public interface CwnsUserLocationFacilityDAO extends DAO{
	
	/* Location Type Ids */
	public static final String LOCATION_TYPE_ID_FEDERAL 			= "Federal";
	public static final String LOCATION_TYPE_ID_REGIONAL 			= "Regional";
	public static final String LOCATION_TYPE_ID_STATE 				= "State";
	public static final String LOCATION_TYPE_ID_LOCAL 				= "Local";
	
	public List getLocalCwnsUserLocationFacilityByFacilityId(String facilityId);

}
