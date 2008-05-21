package gov.epa.owm.mtb.cwns.dao;

import java.util.List;

import gov.epa.owm.mtb.cwns.model.CwnsUserLocation;

/**
 * 
 * @author mnconnor
 *
 */
public interface CwnsUserLocationDAO extends DAO{

	public void save(CwnsUserLocation cwnsUserLocation);
	public void deleteObject(CwnsUserLocation cwnsUserLocation);
	public List getCwnsUserLocationByCwnsUserId(String cwnsUserId);
}
