package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.CwnsInfoLocationRefDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CwnsInfoLocationRef;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;

public class CwnsInfoLocationRefDAOImpl extends BaseDAOHibernate implements CwnsInfoLocationRefDAO{
	
	public CwnsInfoLocationRef getCwnsInfoLocationRefByLocationId(String locationId){
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		return (CwnsInfoLocationRef)searchDAO.getSearchObject(CwnsInfoLocationRef.class, scs);
	}
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}
}
