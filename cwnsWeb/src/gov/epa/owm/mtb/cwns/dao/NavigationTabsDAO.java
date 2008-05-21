package gov.epa.owm.mtb.cwns.dao;

import java.util.List;

public interface NavigationTabsDAO extends DAO {

    public List findDataAreasByFacilityId(String facilityNumber);

	
}
