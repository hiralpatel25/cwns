package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;

import java.util.Collection;

public interface NavigationTabsService {

	public Collection findDataAreasByFacilityId(String facNum, String navigationTabType, boolean hasNeedsLinks, CurrentUser user);
	public Collection findDataAreaIdsByFacilityId(String facNum);
	public Collection findValidatedDataAreasByFacilityId(String facNum, String navigationTabType, boolean hasNeedsLinks, CurrentUser user);
	
}
