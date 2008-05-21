package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.CwnsUserDAO;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.common.search.Search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

public class CwnsUserDAOImpl extends BaseDAOHibernate implements CwnsUserDAO {

/**
 * 
 */
public Collection getUserObjects(Collection userList, Search search) {
		Criteria criteria;
		try {
			Session session = this.getDAOSession();
			
			criteria = session.createCriteria(CwnsUser.class);			
			Collection arr = splitCollection(userList);
			int icount =0;
			Criterion c =null;
			for (Iterator iter = arr.iterator(); iter.hasNext();) {
				Collection col = (Collection) iter.next();
				if(icount ==0){
					c = Expression.in("cwnsUserId", col);	
				}else{
					c = (Expression.or(c, Expression.in("cwnsUserId", col)));
				}
				icount++;
			}
			criteria.add(c);

			//TODO: must get Role information (locationId/type)

			if(search.getMaxResults()>0){
	    		criteria.setFirstResult(search.getStartIndex());
	    		criteria.setMaxResults(search.getMaxResults());
	    	}
	    	if(search.getSortCriteria()!=null){
	    		setSortCriteria(criteria, search.getSortCriteria());
	    	}    		    	
	    	Collection col = criteria.list();
			return col;
			
		} catch (HibernateException x) {
			throw getHibernateTemplate().convertHibernateAccessException(x);
		}

}
		
/*	
public Collection getUsers(Collection userList, Search search) {
	Collection results = new ArrayList();
	return results;
}
*/	


	public void setSortCriteria(Criteria criteria, Collection sortCriteria) {		
        //	set sort order based on sort criteria collection
		if (sortCriteria.size()>0) {
			for (Iterator iter = sortCriteria.iterator(); iter.hasNext();) {
				SortCriteria sc = (SortCriteria) iter.next();
				setSortCriteria(criteria, sc);				
			}
		}
	}
	
	public void setSortCriteria(Criteria criteria, SortCriteria sortCriteria){
		Order order = null;
        //		set sort order
		if (sortCriteria != null) {
			if (sortCriteria.getOrder() == SortCriteria.ORDER_ASCENDING) {
				order = Order.asc(sortCriteria.getColumn());
			} else {
				order = Order.desc(sortCriteria.getColumn());
			}
			criteria.addOrder(order);
		}
	}	
	private Collection splitCollection(Collection userList){
		ArrayList arr = new ArrayList();
		if(userList.size()>990){
		int icount =0;
		ArrayList narr=new ArrayList();
		for (Iterator iter = userList.iterator(); iter.hasNext();) {
			Long facilityId = (Long) iter.next();
			narr.add(facilityId);
			icount++;
			if(icount > 990){
				arr.add(narr);
				icount=0;
				narr=new ArrayList();
			}
		}
		if(narr.size()>0)arr.add(narr);
		}else{
			arr.add(userList);	
		}
	    return arr;	
	}	
	
}