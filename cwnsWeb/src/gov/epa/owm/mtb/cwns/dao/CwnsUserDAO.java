package gov.epa.owm.mtb.cwns.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;

import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.model.Facility;

public interface CwnsUserDAO extends DAO{

	
	public Collection getUserObjects(Collection userList, Search search);
	public void setSortCriteria(Criteria criteria, Collection sortCriteria);
	public void setSortCriteria(Criteria criteria, SortCriteria sortCriteria);
	
}
