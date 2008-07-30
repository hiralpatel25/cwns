package gov.epa.owm.mtb.cwns.dao.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;

import gov.epa.owm.mtb.cwns.dao.PopulationDAO;
import gov.epa.owm.mtb.cwns.model.FacilityComment;

public class PopulationDAOImpl extends BaseDAOHibernate implements
		PopulationDAO {

	public Collection getPopulationComments(String facNum) {
		List results;
		try{
		    Session session = this.getDAOSession();
		    results = session.createCriteria(FacilityComment.class)
		                 .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN)
		                 .setProjection(Projections.property("description"))
		                 .add(Expression.eq("facility.facilityId", new Long(facNum)))
		                 .add(Expression.eq("d.name", "Population"))
		                 .list();
		                 
		} catch (HibernateException x) {
            throw getHibernateTemplate().convertHibernateAccessException(x);
        }
		return results;
	}
	
}
