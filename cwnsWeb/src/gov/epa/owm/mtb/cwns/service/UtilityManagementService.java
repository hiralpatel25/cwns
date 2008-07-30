package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;

import java.util.Collection;

public interface UtilityManagementService {

	public void saveOrUpdateUtilityManagement(long facilityId, String resultStr, CurrentUser user);
	
	public Collection getUtilityManagementList(long facilityId);
	
	public Collection getUtilityManagementStatusList();
	
	public Collection getUtilityManagementByFacility(Long facilityId);
	
	public String warnStateUSerAboutLocal(long facilityId, CurrentUser user);
}
	