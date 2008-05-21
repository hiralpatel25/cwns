package gov.epa.owm.mtb.cwns.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import gov.epa.owm.mtb.cwns.dao.NavigationTabsDAO;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;

public class NavigationTabsDAOImpl extends BaseDAOHibernate implements NavigationTabsDAO {

    /**
     * Finds the Data Areas by Facility Number
     * @param hSession
     * @param facilityNumber
     * @return Data Areas results
     * @throws HibernateException
     */
    public List findDataAreasByFacilityId(String facilityNumber) {
	
    	List result;
		
    	
    try {
    	Session session = this.getDAOSession();	
    	Criteria crit = session.createCriteria(FacilityEntryStatus.class)
                                .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN)
                                .createAlias("facility", "f", Criteria.INNER_JOIN);
    	result = crit.setProjection(Projections.projectionList()
    			                    .add(Projections.property("d.name").as("da_name"))
    			                    .add(Projections.property("d.dataAreaId").as("da_dataAreaTypeId"))
    			                    )
    	              .add(Expression.eq("f.facilityId", new Long(facilityNumber)))
    	              .addOrder(Order.asc("d.sortSequence"))
    	              .list();
    	
    	log.debug("test1 - " + result.size());
    		    	
    } catch (HibernateException he) {
    	throw getHibernateTemplate().convertHibernateAccessException(he);
    }
     return result;
    }
	
}
