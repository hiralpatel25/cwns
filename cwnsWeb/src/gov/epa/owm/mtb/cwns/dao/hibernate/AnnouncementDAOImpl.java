package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.dao.AnnouncementDAO;
import gov.epa.owm.mtb.cwns.model.Announcement;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class AnnouncementDAOImpl extends BaseDAOHibernate implements AnnouncementDAO{

	public Collection findAnnouncementsByLocation(String locationId) {
		List result;		
	    try {
	    	Session session = this.getDAOSession();
	    	Criteria crit =   session.createCriteria(Announcement.class)
	    	                .createAlias("administrativeMessageType", "amt", Criteria.INNER_JOIN);
	    	result = crit.setProjection(Projections.projectionList()
	    	                            .add(Projections.property("id.lastUpdateTs"))
	    	                            .add(Projections.property("description"))
	    	                            .add(Projections.property("amt.description"))		
	    	                            )
	    	               .add(Expression.or(Expression.eq("locationId", locationId), Expression.eq("locationId", "US")))
	    	               .addOrder(Order.desc("id.lastUpdateTs"))
	    	               .setFirstResult(0)
	    	               .setMaxResults(10)
	    	               .list(); 
	    	log.debug("test1 "+result.size());
	    		    	
	    } catch (HibernateException he) {
	    	throw getHibernateTemplate().convertHibernateAccessException(he);
	    }
	     return result;
	}

}
