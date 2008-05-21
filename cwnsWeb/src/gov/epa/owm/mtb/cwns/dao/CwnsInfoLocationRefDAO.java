package gov.epa.owm.mtb.cwns.dao;

import gov.epa.owm.mtb.cwns.model.CwnsInfoLocationRef;

public interface CwnsInfoLocationRefDAO extends DAO{

	public CwnsInfoLocationRef getCwnsInfoLocationRefByLocationId(String locationId);
}
