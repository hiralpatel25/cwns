package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;


/**
 * This class is a generic implementation of search data access object based on hibernate framework.
 *    
 * Note: This class was developed for the CWNS project.  We encourage you to 
 * update/modify/improve this class. We will appreciate if you can share any 
 * updates with us. 
 *    
 * @author raj Lingam
 * raj.lingam@lmco.com
 */
public class SearchDAOImpl extends BaseDAOHibernate implements SearchDAO {
    
	public static final int MAX_IN =990;

	/**
	 * Searches for an object based on class and search conditions  
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @return  an object based on search
	 */
	public Object getSearchObject(Class implClass, SearchConditions searchConditions) {
		return getSearchObject(implClass, searchConditions, new ArrayList());
	}
	
	/**
	 * Searches for an object based on class and search conditions  
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @param aliasCriteria  alias
	 * @return  an object based on search
	 */
	public Object getSearchObject(Class implClass, SearchConditions searchConditions, Collection aliasCriteria) {
		Session session = getDAOSession();
		Object o = null;
		try{
			Criteria criteria = session.createCriteria(implClass);
			setAlias(criteria, aliasCriteria);
			if(searchConditions.getConditions().size()>0){
				criteria.add(getConditionExpression(searchConditions));
			}	
			o=criteria.uniqueResult();
		} catch (HibernateException x) {
			throw getHibernateTemplate().convertHibernateAccessException(x);
		}
		return o;
	}
	
	/**
	 * Searches for a list of objects based on class and search conditions
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @return  list of objects based on search
	 */
	public List getSearchList(Class implClass, SearchConditions searchConditions) {
		return getSearchList(implClass, searchConditions, new ArrayList(), new ArrayList(), 0, 0);
	}
	
	/**
	 * Searches for a list of objects based on class and search conditions and sorts the results 
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @return  list of objects based on search
	 */
    public List getSearchList(Class implClass, SearchConditions searchConditions, Collection sortCriteria) {
        return getSearchList(implClass, searchConditions, sortCriteria, new ArrayList(), 0, 0);
	}	
	
	
	/**
	 * Searches for a list of objects based on class and search conditions and sorts the results. The results are restricted to max result size. 
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param startIndex start index of the result set
	 * @param maxResults  number of results from the start index
	 * @return list of objects based on search 
	 */
    public List getSearchList(Class implClass, SearchConditions searchConditions, Collection sortCriteria, int startIndex, int maxResults) {
        return getSearchList(implClass, searchConditions, sortCriteria, new ArrayList(), startIndex, maxResults);
	}

	/**
	 * Searches for a list of objects based on class and search conditions and sorts the results. The results are restricted to max result size. 
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param aliasCriteria a collection of alias criteria
	 * @param startIndex start index of the result set
	 * @param maxResults  number of results from the start index
	 * @return list of objects based on search
	 */
	public List getSearchList(Class implClass, SearchConditions searchConditions, Collection sortCriteria, Collection aliasCriteria, int startIndex, int maxResults) {
		return getSearchList(implClass, new ArrayList(), searchConditions, sortCriteria, aliasCriteria, startIndex, maxResults);
	}
	
	/**
	 * searches for specified columns based on search conditions and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @return an object array of specified columns
	 */
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions) {
		return getSearchList(implClass, columns, searchConditions, new ArrayList()); 		
	}
	
	/**
	 * searches based on search conditions and sorts and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @param sortCriteria sort criteria
	 * @return an object array of specified columns
	 */
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions, Collection sortCriteria) {
		return getSearchList(implClass, columns, searchConditions, sortCriteria, new ArrayList()); 		
	}
	
	/**
	 * 
	 * searches based on search conditions and sorts and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param aliasCriteria a collection of alias criteria 
	 * @return an object array of specified columns
	 */
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions, Collection sortCriteria, Collection aliasCriteria) {
		return getSearchList(implClass, columns, searchConditions, sortCriteria, aliasCriteria, 0, 0); 		
	}
	
	
	/**
	 * searches based on search conditions and sorts and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param aliasCriteria a collection of alias criteria 
  	 * @param startIndex start index of the result set
	 * @param maxResults  number of results from the start index
	 * @return an object array of specified columns
	 */
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions, Collection sortCriteria, Collection aliasCriteria, int startIndex, int maxResults) {
		return getSearchList(implClass, columns, searchConditions, sortCriteria, aliasCriteria, startIndex, maxResults, false);
	}

	/**
	 * searches based on search conditions and sorts and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param aliasCriteria a collection of alias criteria 
  	 * @param startIndex start index of the result set
	 * @param maxResults  number of results from the start index
	 * @param isDistinct  whether the result set should be distinct; default false
	 * @return an object array of specified columns
	 */	
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions, Collection sortCriteria, Collection aliasCriteria, int startIndex, int maxResults, boolean isDistinct) {
		Session session = getDAOSession();
		try{
			Criteria criteria = session.createCriteria(implClass);
			setColumns(criteria, columns, isDistinct);
			setAlias(criteria, aliasCriteria);
			setSortCriteria(criteria, sortCriteria);
			setMaxResults(criteria, startIndex, maxResults);
			if(searchConditions.getConditions()!=null && searchConditions.getConditions().size()>0){
				criteria.add(getConditionExpression(searchConditions));
			}	
			
			List l = criteria.list();
			return l;
		} catch (HibernateException x) {
			throw getHibernateTemplate().convertHibernateAccessException(x);
		}		
	}
	
	
	/**
	 * gets a count of search results based on the searc condition 
	 * @param implClass search object class
	 * @param searchConditions search conditions
	 * @return a count of the results
	 */
	public int getCount(Class implClass, SearchConditions searchConditions){
		Session session = getDAOSession();
		try{
			Criteria criteria = session.createCriteria(implClass);
			if(searchConditions.getConditions()!=null && searchConditions.getConditions().size()>0){
				criteria.add(getConditionExpression(searchConditions));
			}	
			criteria.setProjection(Projections.rowCount());
			return ((Integer) criteria.uniqueResult()).intValue();
			
		} catch (HibernateException x) {
			throw getHibernateTemplate().convertHibernateAccessException(x);
		}		
	}	
	
	
	//helper methods	
	/**
	 * Constructs a hibernate criteria based on search conditions
	 * @param searchConditions  search conditions
	 * @return hibernate criteria
	 */
	private Criterion getConditionExpression(SearchConditions searchConditions){
		List conditions = searchConditions.getConditions();
		Criterion crit1=null; 
		int index =0;
		for (Iterator iter = conditions.iterator(); iter.hasNext();) { 
			Object sc = iter.next();
			Criterion c;
			if(sc instanceof SearchConditions){
				c=getConditionExpression((SearchConditions)sc);  //recursion		
			}else if(sc instanceof SearchCondition){
				c=getConditionExpression((SearchCondition)sc);
			}else{
				throw new ApplicationException("Invalid searchcontion");
			}				
			if(index==0){
				 crit1= c; 
			} else {
				Criterion crit2 = c;
				String logicalOperator = searchConditions.getLogicalOperator(index);
				if (SearchCondition.OPERATOR_AND.equals(logicalOperator)) {
					crit1 = Expression.and(crit1, crit2);
				} else if (SearchCondition.OPERATOR_OR.equals(logicalOperator)) {
					crit1 = Expression.or(crit1, crit2);
				}else{
					log.error("No local operator specified");
				}
			}	
			index=index+1;			
		}
		return crit1;
	}
	

	/**
	 * Constructs a hibernate criterion based on search condition
	 * @param searchCondition  search condition
	 * @return hibernate criterion
	 * @throws ApplicationException
	 */
	private Criterion getConditionExpression(SearchCondition searchCondition) throws ApplicationException {
		String name=searchCondition.getName();
		Object value=searchCondition.getValue();
		String operator =searchCondition.getOperator();
		
		if(SearchCondition.OPERATOR_IS_NULL.equals(operator)){
			return Expression.isNull(name);
		}else if(SearchCondition.OPERATOR_IS_NOT_NULL.equals(operator)){
			return Expression.isNotNull(name);
		}else if(SearchCondition.OPERATOR_EQ.equals(operator)){
			if(value instanceof String){
				   return Expression.eq(name, ((String)value).trim()).ignoreCase();
				}else{
					return Expression.eq(name, value);	
				}      
		}else if(SearchCondition.OPERATOR_NOT_EQ.equals(operator)){
			return Expression.ne(name, value);
		}else if(SearchCondition.OPERATOR_GT.equals(operator)){
			return Expression.gt(name, value);
		}else if(SearchCondition.OPERATOR_LT.equals(operator)){
			return Expression.lt(name, value);
		}else if(SearchCondition.OPERATOR_IN.equals(operator)){
			if(value instanceof Collection){
				if(((Collection)value).size()>MAX_IN){
					return splitCollectionAndSetInAndNotInCriteria(name, (Collection)value, false);
				}else{
					return Expression.in(name, (Collection)value);
				}
			}else{
				throw new ApplicationException("Error In operator requires Value of instance type Collection");
			}
		}else if(SearchCondition.OPERATOR_NOT_IN.equals(operator)){
			if(value instanceof Collection){
				if(((Collection)value).size()>MAX_IN){
					return splitCollectionAndSetInAndNotInCriteria(name, (Collection)value, true);
				}else{
					return Expression.not(Expression.in(name, (Collection)value));
				}
			}else{
				throw new ApplicationException("Error Not In operator requires Value of instance type Collection");
			}
		}else if(SearchCondition.OPERATOR_LIKE.equals(operator)){
			log.debug("setting the like condition");
			if(value instanceof String){
			   return Expression.like(name, (String)value, MatchMode.ANYWHERE).ignoreCase();
			}else{
				throw new ApplicationException("Error value is not an instance of String for Like condition");	
			}   
		}else if(SearchCondition.OPERATOR_BETWEEN.equals(operator)){
			if(value instanceof Collection && ((Collection)value).size()== 2){
			   Collection c  = (Collection) value;
			   Object[] oArray = c.toArray();
			   return Expression.between(name, oArray[0], oArray[1]);
			}else{
				throw new ApplicationException("Error In operator requires Value of instance type Collection with two objects");
			}   
		}else{
			throw new ApplicationException("Unsupported Operator: " + operator);
		}
	}
	
	/** function to split In Criteria collection into bins of less than 1000
	 * 
	 */
	private Collection splitCollection(Collection facilityDisplayList){
		ArrayList arr = new ArrayList();
		if(facilityDisplayList.size()>990){
		int icount =0;
		ArrayList narr=new ArrayList();
		for (Iterator iter = facilityDisplayList.iterator(); iter.hasNext();) {
			Long facilityId = (Long) iter.next();
			narr.add(facilityId);
			icount++;
			if(icount > MAX_IN){
				arr.add(narr);
				icount=0;
				narr=new ArrayList();
			}
		}
		if(narr.size()>0)arr.add(narr);
		}else{
			arr.add(facilityDisplayList);	
		}
	    return arr;	
	}		

	/**
	 * Processes a collection sort criteria
	 * @param criteria hibernate criteria
	 * @param sortCriteria  collection of sortcriteria
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
	
	/**
	 * sets the sort order on the hibernate criteria
	 * @param criteria search criteria
	 * @param sortCriteria sort criteria
	 */
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
	
	/**
	 * Set the max results on the critetia
	 * @param criteria hibernate search criteria
	 * @param startIndex start index
	 * @param maxResults max number of results
	 */
	public void setMaxResults(Criteria criteria, int startIndex, int maxResults) {
		if(maxResults > 0){
			  criteria.setFirstResult(startIndex);
			  criteria.setMaxResults(maxResults);
		}
	}
	
	public void deleteAll(Collection objects)
	{
		removeObjects(objects);
	}

	/**
	 * processes a collection of alias
	 * @param criteria search criteria
	 * @param aliasCriteria a collection of alias criteria
	 */
	private void setAlias(Criteria criteria, Collection aliasCriteria) {
		if (aliasCriteria.size()>0) {
			for (Iterator iter = aliasCriteria.iterator(); iter.hasNext();) {
				AliasCriteria ac = (AliasCriteria) iter.next();
				setAliasCriteria(criteria, ac);				
			}
		}		
	}

	/**
	 * sets alias
	 * @param criteria search criteria
	 * @param aliasCriteria alias criteria
	 */
	private void setAliasCriteria(Criteria criteria, AliasCriteria aliasCriteria) {
		if (aliasCriteria != null) {
			if(!"".equals(aliasCriteria.getJoin())){
				if(AliasCriteria.JOIN_INNER.equals(aliasCriteria.getJoin())){
				   criteria.createAlias(aliasCriteria.getChildObjectName(), aliasCriteria.getAlias(), Criteria.INNER_JOIN);
				}else if(AliasCriteria.JOIN_FULL.equals(aliasCriteria.getJoin())){
				   criteria.createAlias(aliasCriteria.getChildObjectName(), aliasCriteria.getAlias(), Criteria.FULL_JOIN);
				}else if(AliasCriteria.JOIN_LEFT.equals(aliasCriteria.getJoin())){
				   criteria.createAlias(aliasCriteria.getChildObjectName(), aliasCriteria.getAlias(), Criteria.LEFT_JOIN);
				}
			}else{
				criteria.createAlias(aliasCriteria.getChildObjectName(), aliasCriteria.getAlias());
			}
		}
	}
	
	/**
	 * sets search columns
	 * @param criteria search criteria
	 * @param aliasCriteria a collection of columns
	 */
	private void setColumns(Criteria criteria, Collection columns, boolean isDistinct){		
       if(columns.size()>0){
            //set projecttions
    	   ProjectionList pl = Projections.projectionList();
    	   for (Iterator iter = columns.iterator(); iter.hasNext();) {
			String col = (String) iter.next();
			pl.add(Projections.property(col));
		   }
    	   
    	   if(isDistinct)
    		   criteria.setProjection(Projections.distinct(pl));    	
    	   else
    		   criteria.setProjection(pl);  
       }			
	}

	public Collection getNamedQueryList(String queryName, Collection queryParameters) {
		Session session = getDAOSession();
		Query q = session.getNamedQuery(queryName);
		int index =0;
		for (Iterator iter = queryParameters.iterator(); iter.hasNext();) {
			Object param = (Object) iter.next();
			q.setParameter(index, param);
			index++;
		}
		
		return q.list();
	}
	
	public void flushAndClearCache() {		
		Session session = this.getDAOSession();
		session.flush();
		session.clear();
	}
	
	private Criterion splitCollectionAndSetInAndNotInCriteria(String name, Collection value, boolean isNotIn){
		Collection arr = splitCollection((Collection)value);
		int icount =0;
		Criterion c =null;
		for (Iterator iter = arr.iterator(); iter.hasNext();) {
			Collection col = (Collection) iter.next();
			if(icount ==0){
					c = Expression.in(name, col); 						
				if(isNotIn){
					c = Expression.not(c);
				}
			}else{
				Criterion nc = Expression.in(name, col);
				if(isNotIn){
					nc = Expression.not(nc);
				}
				c = (Expression.or(c, nc));
			}
			icount++;
		}
		return c;		
		
	}
}
