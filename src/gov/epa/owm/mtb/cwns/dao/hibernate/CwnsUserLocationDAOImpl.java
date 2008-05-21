package gov.epa.owm.mtb.cwns.dao.hibernate;

import java.util.List;

import org.hibernate.Session;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.CwnsUserLocationDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocation;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocationFacility;

public class CwnsUserLocationDAOImpl extends BaseDAOHibernate implements CwnsUserLocationDAO {

	public void save(CwnsUserLocation cwnsUserLocation) {
    	saveObject(cwnsUserLocation);	
	}

	
	public void deleteObject(CwnsUserLocation cwnsUserLocation) {
		Session session = this.getDAOSession();
		session.delete(cwnsUserLocation);
	}
	
	public List getCwnsUserLocationByCwnsUserId(String cwnsUserId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.cwnsUserId", SearchCondition.OPERATOR_EQ, cwnsUserId));
		return searchDAO.getSearchList(CwnsUserLocation.class, scs);
	}
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}
	
}