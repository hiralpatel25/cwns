package gov.epa.owm.mtb.cwns.dao;

import java.util.Collection;
import java.util.List;

import gov.epa.owm.mtb.cwns.model.CwnsUserLocatnAccessLevel;

/**
 * 
 * @author mnconnor
 *
 */
public interface CwnsUserLocatnAccessLevelDAO extends DAO{
	
	
	
	public void save(CwnsUserLocatnAccessLevel cwnsUserLocatnAccessLevel);
	public void deleteObjects(Collection cwnsUserLocatnAccessLevels);
	public void flushAndClearCache();
	public List getCwnsUserLocationAccessLevelByCwnsUserId(String cwnsUserId);
	public List getCwnsUserLocationAccessLevel(String cwnsUserId, String locationTypeId, String locationId);
}
