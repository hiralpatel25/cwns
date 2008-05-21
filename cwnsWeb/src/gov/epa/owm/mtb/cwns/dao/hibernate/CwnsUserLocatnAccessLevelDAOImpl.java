package gov.epa.owm.mtb.cwns.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.CwnsUserLocatnAccessLevelDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocationFacility;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocatnAccessLevel;



public class CwnsUserLocatnAccessLevelDAOImpl extends BaseDAOHibernate implements CwnsUserLocatnAccessLevelDAO {

	public void save(CwnsUserLocatnAccessLevel cwnsUserLocatnAccessLevel) {
    	saveObject(cwnsUserLocatnAccessLevel);	
	}

	
	// Delete a Collection of CwnsUserLocatnAccessLevel objects
	public void deleteObjects(Collection cwnsUserLocatnAccessLevels) {
		Session session = this.getDAOSession();

		Iterator iter = cwnsUserLocatnAccessLevels.iterator();
		while (iter.hasNext()) {
			CwnsUserLocatnAccessLevel culal = (CwnsUserLocatnAccessLevel) iter.next();
			session.delete(culal);
		}
		
	}
	
	//TODO: This should not be done at a particular DAO Level - refactor.
	public void flushAndClearCache() {		
		Session session = this.getDAOSession();
		session.flush();
		session.clear();
	}
	
	public List getCwnsUserLocationAccessLevelByCwnsUserId(String cwnsUserId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.cwnsUserId", SearchCondition.OPERATOR_EQ, cwnsUserId));
		return searchDAO.getSearchList(CwnsUserLocatnAccessLevel.class, scs);
	}
	
	public List getCwnsUserLocationAccessLevel(String cwnsUserId, String locationTypeId, String locationId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.cwnsUserId", SearchCondition.OPERATOR_EQ, cwnsUserId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.locationTypeId",SearchCondition.OPERATOR_EQ,locationTypeId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.locationId",SearchCondition.OPERATOR_EQ,locationId));
		return searchDAO.getSearchList(CwnsUserLocatnAccessLevel.class, scs);
	}
	
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}
}