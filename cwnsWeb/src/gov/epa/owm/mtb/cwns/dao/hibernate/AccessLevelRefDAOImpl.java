package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.dao.AccessLevelRefDAO;
import gov.epa.owm.mtb.cwns.dao.ReviewStatusRefDAO;
import gov.epa.owm.mtb.cwns.model.AccessLevelRef;
import gov.epa.owm.mtb.cwns.model.CwnsUserStatusRef;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

public class AccessLevelRefDAOImpl extends BaseDAOHibernate implements AccessLevelRefDAO {
	
	public Collection getAccessLevelRefs(CurrentUser user) {
		
		//TODO: Must filter by user type
	    Session session = this.getDAOSession();
		Criteria criteria = session.createCriteria(AccessLevelRef.class)
								   .addOrder(Order.asc("name"));		
		return criteria.list();		
	}
    
}
