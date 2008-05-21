package gov.epa.owm.mtb.cwns.dao.hibernate;

import java.util.List;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.CwnsUserLocationFacilityDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocationFacility;


public class CwnsUserLocationFacilityDAOImpl extends BaseDAOHibernate implements CwnsUserLocationFacilityDAO{

	public List getLocalCwnsUserLocationFacilityByFacilityId(String facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("cwnsUserLocation.id.locationTypeId", SearchCondition.OPERATOR_EQ, CwnsUserLocationFacilityDAO.LOCATION_TYPE_ID_LOCAL));
		return searchDAO.getSearchList(CwnsUserLocationFacility.class, scs);	
	}
	
	
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}
}
