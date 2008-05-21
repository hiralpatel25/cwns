package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.dao.CwnsUserStatusRefDAO;
import gov.epa.owm.mtb.cwns.dao.ReviewStatusRefDAO;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.model.CwnsUserStatusRef;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

public class CwnsUserStatusRefDAOImpl extends BaseDAOHibernate implements CwnsUserStatusRefDAO {

	public Collection getCwnsUserStatusRefs() {
	    Session session = this.getDAOSession();
		Criteria criteria = session.createCriteria(CwnsUserStatusRef.class)
								   .addOrder(Order.asc("name"));		
		return criteria.list();
	}

}
