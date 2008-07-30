package gov.epa.owm.mtb.cwns.dao;

import java.util.Collection;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;

public interface AccessLevelRefDAO extends DAO {
	
	public static final long FEDERAL_USER_MANAGEMENT 	= 1;
	public static final long FEDERAL_REFERENCE_ADMIN 	= 2;
	public static final long FEDERAL_REVIEW				= 3;
	public static final long STATE_USER_MANAGEMENT		= 4;
	public static final long STATE_REFERENCE_ADMIN		= 5;
	public static final long FACILITY_UPDATE			= 6;
	public static final long SUBMIT_FOR_FEDERAL_REVIEW  = 7;
	public static final long LOCAL_USER_MANAGEMENT 		= 8;
	public static final long FACILITY_UPDATE_PARTIAL	= 9;
	public static final long FACILITY_UPDATE_FEEDBACK 	= 10;
	public static final long SUBMIT_FOR_STATE_REVIEW	= 11;
	public static final long VIEW						= 12;
	
	public Collection getAccessLevelRefs(CurrentUser user);
	
}
