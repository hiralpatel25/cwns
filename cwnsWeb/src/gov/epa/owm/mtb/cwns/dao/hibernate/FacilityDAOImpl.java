package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.facility.FacilityListDisplayAction;
import gov.epa.owm.mtb.cwns.facility.FacilityListForm;
import gov.epa.owm.mtb.cwns.facility.FacilityListHelper;
import gov.epa.owm.mtb.cwns.model.CountyRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityComment;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FederalReviewStatus;
import gov.epa.owm.mtb.cwns.model.ReviewComment;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.model.VFacility;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.portal.provider.v2.render.PortletRenderRequest;

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

public class FacilityDAOImpl extends BaseDAOHibernate implements FacilityDAO {

	public Facility findByFacilityNumber(String facilityNumber) {
		DetachedCriteria crit = DetachedCriteria.forClass(Facility.class);
		crit.add(Expression.eq("cwnsNbr", facilityNumber));
		List ls = getHibernateTemplate().findByCriteria(crit);
		Facility fs = null;
		for (Iterator iter = ls.iterator(); iter.hasNext();) {
			fs = (Facility) iter.next();
		}
		return fs;
	}
	
	public Facility getFacilityByCwnsNbr(String cwnsNbr) {
		SearchConditions scs  =  new SearchConditions(new SearchCondition("cwnsNbr", SearchCondition.OPERATOR_EQ, cwnsNbr));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("versionCode",SearchCondition.OPERATOR_EQ, "S"));
		return (Facility)searchDAO.getSearchObject(Facility.class, scs);
	}
	
	public Facility findByFacilityId(String facilityId) {
		DetachedCriteria crit = DetachedCriteria.forClass(Facility.class);
		crit.add(Expression.eq("facilityId", new Long(facilityId)));
		List ls = getHibernateTemplate().findByCriteria(crit);
		Facility fs = null;
		for (Iterator iter = ls.iterator(); iter.hasNext();) {
			fs = (Facility) iter.next();
		}
		return fs;
	}
	
	
	/**
	 * Finds all facility objects based on an Example facility Object
	 * The search is based on facility by example
	 * 
	 * @param facility
	 * 			 The facility object with the fields initialized to the search value
	 * @param firstResult 
	 * 			 This first result to return
	 * @param maxResults
	 * 			 The maximum number of results to return 
	 * @return A facility objects list
	 * @throws HibernateException
	 *             if hibernate has problems
	 */
	public List findFacility(Facility facility, int firstResult, int maxResults) {
		List facilities;
        try {
        	Example exampleFacility = Example.create(facility).ignoreCase()
			.enableLike(MatchMode.ANYWHERE).excludeProperty(
					"privateOwnedFlag").excludeProperty(
					"smallCommunityExceptionFlag");
        	
        	Session session = this.getDAOSession();
        	facilities = session.createCriteria(Facility.class)
			.add(exampleFacility)
			.addOrder(Order.asc("cwnsNbr"))
//			.addOrder(Order.asc("").ignoreCase())
			.setFirstResult(firstResult)
			.setMaxResults(maxResults)
			.list();
        } catch (HibernateException x) {
            throw getHibernateTemplate().convertHibernateAccessException(x);
        }
        
        return facilities;
	}
	
	public StateRef getStateByLocationId(String locationId) {
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		Object o= searchDAO.getSearchObject(StateRef.class, scs);
		return (StateRef)o;
	}


	/**
	 * Finds the number of facility objects based on an Example Facility Object.
	 * 
	 * @param hSession
	 *            an open hibernate session
	 * @param facility
	 *            The facility object with the fields initialized to the search
	 *            value
	 * @return The number of facilities in the database based on the Example
	 *         Facility Object.
	 * @throws HibernateException
	 */
	public int countFacilities(Facility facility) {
		Criteria criteria;
		try {
			Example exampleFacility = Example.create(facility).ignoreCase()
					.enableLike(MatchMode.ANYWHERE).excludeProperty(
							"privateOwnedFlag").excludeProperty(
							"smallCommunityExceptionFlag");
			Session session = this.getDAOSession();
			criteria = session.createCriteria(Facility.class).add(
					exampleFacility);
			criteria.setProjection(Projections.rowCount());
		} catch (HibernateException x) {
			throw getHibernateTemplate().convertHibernateAccessException(x);
		}
		return ((Integer) criteria.uniqueResult()).intValue();
	}	
	
	public Collection getFacilitiesByList(Collection facilityDisplayList, Collection sortCriteria, int startIndex, int maxResults){
		Criteria criteria;
		try {
			Session session = this.getDAOSession();
			criteria = session.createCriteria(VFacility.class);			
			Collection arr = splitCollection(facilityDisplayList);
			int icount =0;
			Criterion c =null;
			for (Iterator iter = arr.iterator(); iter.hasNext();) {
				Collection col = (Collection) iter.next();
				if(icount ==0){
					c = Expression.in("id.facilityId", col);	
				}else{
					c = (Expression.or(c, Expression.in("id.facilityId", col)));
				}
				icount++;
			}
			criteria.add(c);

			ProjectionList pl = Projections.projectionList();
			pl.add(Projections.property("id.facilityId"));
			pl.add(Projections.property("id.cwnsNbr"));			
			pl.add(Projections.property("id.facilityName"));
			pl.add(Projections.property("id.reviewStatusName"));
			pl.add(Projections.property("id.county"));
			pl.add(Projections.property("id.feedbackReviewStatusName"));
			pl.add(Projections.property("id.authority"));
	    	criteria.setProjection(pl);	    		    	
	    	if(maxResults>0){
	    		criteria.setFirstResult(startIndex);
	    		criteria.setMaxResults(maxResults);
	    	}
	    	if(sortCriteria!=null){
	    		setSortCriteria(criteria, sortCriteria);
	    	}    		    	
	    	Collection col = criteria.list();
			return col;
		} catch (HibernateException x) {
			throw getHibernateTemplate().convertHibernateAccessException(x);
		}
	}
	
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
	private Collection splitCollection(Collection facilityDisplayList){
		ArrayList arr = new ArrayList();
		if(facilityDisplayList.size()>990){
		int icount =0;
		ArrayList narr=new ArrayList();
		for (Iterator iter = facilityDisplayList.iterator(); iter.hasNext();) {
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
			arr.add(facilityDisplayList);	
		}
	    return arr;	
	}	
	
	public Collection getFacilityIdByNeeds(String locationId, String operator, int amount){
		
		String sql = "select f.facilityId from Facility f "+
					  "inner join f.facilityDocuments as fd "+
					  "inner join fd.costs as c "+
					  "where c.needTypeRef.needTypeId='F' and f.versionCode='S' and f.locationId =?"+
					  "group by f.facilityId having sum(c.adjustedAmount)"+operator+ amount;
		Session session = this.getDAOSession();
		Query q = session.createQuery(sql);
		q.setParameter(0, locationId);
		return q.list();		
	} 
	
	public Collection getFacilityIdWithNoErrors(String locationId){
		String sql =" select f.facilityId" +
					" from Facility as f" +
					" where f.facilityId  not in (select fes.id.facilityId" + 
                    " from FacilityEntryStatus as fes" +
                    " where fes.errorFlag='Y'" +
                    " group by fes.id.facilityId) " +
                    " and f.versionCode='S' and f.locationId = ?";                    
		Session session = this.getDAOSession();
		Query q = session.createQuery(sql);
		q.setParameter(0, locationId);		
		return q.list();
	}

	public Collection getFacilityComments(String facNum) {
		List results;
		try{
		    Session session = this.getDAOSession();
		    results = session.createCriteria(FacilityComment.class)
		                 .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN)
		                 .setProjection(Projections.property("description"))
		                 .add(Expression.eq("facility.facilityId", new Long(facNum)))
		                 .add(Expression.eq("d.name", "Facility"))
		                 .list();
		                 
		} catch (HibernateException x) {
            throw getHibernateTemplate().convertHibernateAccessException(x);
        }
		return results;
	}

	public Collection getFeedbackVersionOfFacility(String facNum) {
		String cwnsNbr = getCWNSNbrByFacilityId(facNum);
		List results;
		try{
		    Session session = this.getDAOSession();
		    results = session.createCriteria(Facility.class)
		                     .add(Expression.eq("cwnsNbr", cwnsNbr))
		                     .add(Expression.eq("versionCode", new Character('F')))
		                     .list();
		} catch (HibernateException x) {
            throw getHibernateTemplate().convertHibernateAccessException(x);
        }   
		return results;
	}
	
	public String getCWNSNbrByFacilityId(String facNum){
		String cwnsNbr = "";
		List results;
		try{
		    Session session = this.getDAOSession();
		    results = session.createCriteria(Facility.class)
		                     .setProjection(Projections.property("cwnsNbr"))
		                     .add(Expression.eq("facilityId", new Long(facNum)))
		                     .list();
		} catch (HibernateException x) {
            throw getHibernateTemplate().convertHibernateAccessException(x);
        } 
        if (results.iterator().hasNext()) 
        	cwnsNbr = (String)results.get(0);
		return cwnsNbr;
	}
	
	
	/**
	 * This method is used for testing purposes only.
	 */
	public Collection getFacilitiesByFunction(){
		try{
		    String sql="select * from TABLE(fetch_facilities('LIP,LAS'))";
		    Session session = this.getDAOSession();
		    PreparedStatement ps=  session.connection().prepareStatement(sql);
		    ResultSet s = ps.executeQuery();
		    while(s.next()){
			   String cwnsNumber=s.getString("CWNS_NBR");
			   log.debug("CWNS Number:" + cwnsNumber);
		    }
		} catch (HibernateException x) {
            throw getHibernateTemplate().convertHibernateAccessException(x);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Collection executeKeywordSearch(Search search){
		log.debug("Enter FacilityDAOImpl.executeKeywordSearch()");
		Collection facilityListHelpers = new ArrayList();
		
		try{
			String sql="SELECT * FROM TABLE(advance_search.get_facilities_by_keywords(?,?,?,?,?,?,?,?,?,?,?))";
			Session session = this.getDAOSession();
		    PreparedStatement ps=  session.connection().prepareStatement(sql);
		    Map queryProperties = search.getQueryProperties();
		    
		    
				log.debug("");
				log.debug("fromRow: " + search.getStartIndex()); 
				log.debug("MAX_RESULTS: " + search.getMaxResults());
				log.debug("LOCATION_ID: " + (String)queryProperties.get(FacilityDAO.LOCATION_ID));
				log.debug("REVIEW_STATUS: " + (String)queryProperties.get(FacilityDAO.REVIEW_STATUS));
				log.debug("FACILITY_NAME: " + (String)queryProperties.get(FacilityDAO.FACILITY_NAME));
				log.debug("FACILITY_DESCRIPTION: " + (String)queryProperties.get(FacilityDAO.FACILITY_DESCRIPTION));
				log.debug("CWNS_NUMBER: " + (String)queryProperties.get(FacilityDAO.CWNS_NUMBER));
				log.debug("COUNTY: " + (String)queryProperties.get(FacilityDAO.COUNTY));
				log.debug("AUTHORITY: " + (String)queryProperties.get(FacilityDAO.AUTHORITY));
				log.debug("FACILITY_ID: " + (String)queryProperties.get(FacilityDAO.FACILITY_ID));
				log.debug("SORT_COLUMN: " + (String)queryProperties.get(FacilityDAO.SORT_COLUMN));
				
			    ps.setInt(1, search.getStartIndex()); 													//p_from_row
			    ps.setInt(2, search.getMaxResults()); 												//p_number_of_row
			    ps.setString(3, (String)queryProperties.get(FacilityDAO.LOCATION_ID)); 			//location_id
			    ps.setString(4, (String)queryProperties.get(FacilityDAO.REVIEW_STATUS)); 		//review_status
			    ps.setString(5, (String)queryProperties.get(FacilityDAO.FACILITY_NAME)); 		//facilityName
			    ps.setString(6, (String)queryProperties.get(FacilityDAO.FACILITY_DESCRIPTION));	//facility_description
			    ps.setString(7, (String)queryProperties.get(FacilityDAO.CWNS_NUMBER));			//cwns_nbr
			    ps.setString(8, (String)queryProperties.get(FacilityDAO.COUNTY)); 				//county
			    ps.setString(9, (String)queryProperties.get(FacilityDAO.AUTHORITY)); 				//Authority
			    ps.setString(10, (String)queryProperties.get(FacilityDAO.FACILITY_ID)); 				//facilityId
			    ps.setString(11, (String)queryProperties.get(FacilityDAO.SORT_COLUMN));			//sort_column
			    
			    
			    ResultSet resultSet = ps.executeQuery();
			    while(resultSet.next()){
			    	
					FacilityListHelper FLH = new FacilityListHelper(
							new Long(resultSet.getLong("FACILITY_ID")).toString(),
									 resultSet.getString("CWNS_NBR"),
									 resultSet.getString("FACILITY_NAME"),
									 resultSet.getString("REVIEW_STATUS_NAME"),
									 resultSet.getString("COUNTY"),
									 resultSet.getString("FEEDBACK_REVIEW_STATUS_NAME"),
									 resultSet.getString("AUTHORITY"),
									 new Long(resultSet.getLong("FEEDBACK_FACILITY_ID")).toString());
			    
				facilityListHelpers.add(FLH);
				log.debug(resultSet.getString("FACILITY_NAME"));
				log.debug(new Long(resultSet.getLong("TOTAL_ROW")));
				search.setNumOfFacilities((resultSet.getInt("TOTAL_ROW")));
				
		    }
		    
		    
		   	}catch(Exception e){
		    	e.printStackTrace();
		    }
		   	log.debug("Exit FacilityDAOImpl.executeKeywordSearch()");
		    return facilityListHelpers;		
	}
	
	public Collection executeAdvancedSearch(Search search) {
		log.debug("Enter FacilityDAOImpl.executeAdvancedSearch()");
		Collection facilityListHelpers = new ArrayList();
		try{
			
			//TODO: must get schema from a config file
			
			String sql="SELECT * FROM TABLE(advance_search.get_facilities(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))";
			
			Session session = this.getDAOSession();
		    PreparedStatement ps=  session.connection().prepareStatement(sql);
		    Map queryProperties = search.getQueryProperties();
		    
		    
		    log.debug("fromRow: " + search.getStartIndex()); 
		    log.debug("MAX_RESULTS: " + search.getMaxResults());
		    log.debug("LOCATION_ID: " + (String)queryProperties.get(FacilityDAO.LOCATION_ID));
		    log.debug("currentUser.getUserId(): " + search.getCwnsUserId());
		    log.debug("REVIEW_STATUS: " + (String)queryProperties.get(FacilityDAO.REVIEW_STATUS));
		    log.debug("FACILITY_NAME: " + (String)queryProperties.get(FacilityDAO.FACILITY_NAME));
		    log.debug("OVERALL_NATURE_TYPE: " + (String)queryProperties.get(FacilityDAO.OVERALL_NATURE_TYPE));
		    log.debug("AUTHORITY: " + (String)queryProperties.get(FacilityDAO.AUTHORITY));
		    log.debug("SYSTEM_NAME: " + (String)queryProperties.get(FacilityDAO.SYSTEM_NAME));
		    log.debug("PERMIT_NUMBER: " + (String)queryProperties.get(FacilityDAO.PERMIT_NUMBER));
		    log.debug("COUNTY: " + (String)queryProperties.get(FacilityDAO.COUNTY));
		    log.debug("WATERSHED: " + (String)queryProperties.get(FacilityDAO.WATERSHED));
		    log.debug("NEEDS: " + (String)queryProperties.get(FacilityDAO.NEEDS));
		    log.debug("DOCUMENT_DATE: " + (String)queryProperties.get(FacilityDAO.DOCUMENT_DATE));
		    log.debug("DOCUMENT_TYPE: " + (String)queryProperties.get(FacilityDAO.DOCUMENT_TYPE));
		    log.debug("PRESENT_FLOW_COUNT: " + (String)queryProperties.get(FacilityDAO.PRESENT_FLOW_COUNT));
		    log.debug("PRESENT_POPULATION_COUNT:" +(String)queryProperties.get(FacilityDAO.PRESENT_POPULATION_COUNT));
		    log.debug("FACILTITY_NOT_CHANGED: " +(String)queryProperties.get(FacilityDAO.FACILTITY_NOT_CHANGED));
		    log.debug("ERROR_STATUS: " +(String)queryProperties.get(FacilityDAO.ERROR_STATUS));
		    log.debug("FACILITY_ID: " + (String)queryProperties.get(FacilityDAO.FACILITY_ID));
		    log.debug("SORT_COLUMN: " + (String)queryProperties.get(FacilityDAO.SORT_COLUMN));
		    
		    ps.setInt(1, search.getStartIndex()); 													//p_from_row
		    ps.setInt(2, search.getMaxResults()); 												//p_number_of_row
		    ps.setString(3, (String)queryProperties.get(FacilityDAO.LOCATION_ID)); 	//location_id
		    ps.setString(4, search.getCwnsUserId()); 								//userid
		    ps.setString(5, (String)queryProperties.get(FacilityDAO.REVIEW_STATUS)); 	//review_status
		    ps.setString(6, (String)queryProperties.get(FacilityDAO.FACILITY_NAME)); 	//facilityName
		    ps.setString(7, (String)queryProperties.get(FacilityDAO.OVERALL_NATURE_TYPE)); 	//facility_over_all
		    ps.setString(8, (String)queryProperties.get(FacilityDAO.AUTHORITY)); 	//authority
		    ps.setString(9, (String)queryProperties.get(FacilityDAO.SYSTEM_NAME)); 	//system_name
		    ps.setString(10, (String)queryProperties.get(FacilityDAO.PERMIT_NUMBER)); //permit_number
		    ps.setString(11, (String)queryProperties.get(FacilityDAO.COUNTY)); //county
		    ps.setString(12, (String)queryProperties.get(FacilityDAO.WATERSHED)); //watershed
		    ps.setString(13, (String)queryProperties.get(FacilityDAO.NEEDS)); //need
		    ps.setString(14, (String)queryProperties.get(FacilityDAO.DOCUMENT_DATE)); //doc_published_date
		    ps.setString(15, (String)queryProperties.get(FacilityDAO.DOCUMENT_TYPE)); //document_type_id
		    ps.setString(16, (String)queryProperties.get(FacilityDAO.PRESENT_FLOW_COUNT)); //flow
		    ps.setString(17, (String)queryProperties.get(FacilityDAO.PRESENT_POPULATION_COUNT)); //population
		    ps.setString(18, (String)queryProperties.get(FacilityDAO.FACILTITY_NOT_CHANGED)); //facility_change_date
		    ps.setString(19, (String)queryProperties.get(FacilityDAO.ERROR_STATUS)); //error_status
		    ps.setString(20, (String)queryProperties.get(FacilityDAO.FACILITY_ID)); //facilityId
		    ps.setString(21, (String)queryProperties.get(FacilityDAO.SORT_COLUMN)); //sort_column
		    
		    
		    /*ps.setInt(1, 1); 													//p_from_row
		    ps.setInt(2, 20); 												//p_number_of_row
		    ps.setString(3, ""); 	//location_id
		    ps.setString(4, ""); 								//userid
		    ps.setString(5, ""); 	//review_status
		    ps.setString(6, ""); 	//facilityName
		    ps.setString(7, ""); 	//facility_over_all
		    ps.setString(8, ""); 	//authority
		    ps.setString(9, ""); 	//system_name
		    ps.setString(10,""); //permit_number
		    ps.setString(11,""); //county
		    ps.setString(12,""); //watershed
		    ps.setString(13,""); //need
		    ps.setString(14,""); //doc_published_date
		    ps.setString(15,""); //document_type_id
		    ps.setString(16,""); //flow
		    ps.setString(17,""); //population
		    ps.setString(18,""); //facility_change_date
		    ps.setString(19,""); //error_status
		    ps.setString(20,"");*/
 
		    ResultSet resultSet = ps.executeQuery();
		    while(resultSet.next()){
		    	
				FacilityListHelper FLH = new FacilityListHelper(
						new Long(resultSet.getLong("FACILITY_ID")).toString(),
								 resultSet.getString("CWNS_NBR"),
								 resultSet.getString("FACILITY_NAME"),
								 resultSet.getString("REVIEW_STATUS_NAME"),
								 resultSet.getString("COUNTY"),
								 resultSet.getString("FEEDBACK_REVIEW_STATUS_NAME"),
								 resultSet.getString("AUTHORITY"),
								 new Long(resultSet.getLong("FEEDBACK_FACILITY_ID")).toString());		 				
										
				facilityListHelpers.add(FLH);
				log.debug(resultSet.getString("FACILITY_NAME"));
				log.debug(new Long(resultSet.getLong("TOTAL_ROW")));
				search.setNumOfFacilities((resultSet.getInt("TOTAL_ROW")));
				
		    }
		    
		    
		   	}catch(Exception e){
		    	e.printStackTrace();
		    }
		   	log.debug("Exit FacilityDAOImpl.executeAdvancedSearch()");
		    return facilityListHelpers;				
	}
	
	public Collection getCounties(Collection counties, String locationId){
		Session session = this.getDAOSession();
		Query q = session.getNamedQuery("countySearch");
		q.setParameterList("counties", counties);
		q.setParameter("location", locationId);
		List c = q.list();		
		if(c!=null && c.size()>0){
			ArrayList fs= new ArrayList();
			for (Iterator iter = c.iterator(); iter.hasNext();) {
				String fipsCode = (String) iter.next();
				fs.add(fipsCode);			
			}
			SortCriteria sc = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);
			ArrayList sortArr = new ArrayList();
			sortArr.add(sc);
			//go to the database and fetch the counties
			SearchConditions scs = new SearchConditions(new SearchCondition("fipsCode", SearchCondition.OPERATOR_IN, fs));
		    return searchDAO.getSearchList(CountyRef.class, scs, sortArr);
		}
		return null;
	}
	
	public List getFaciltiyEntryStatusesByFacilityId(String facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		return searchDAO.getSearchList(FacilityEntryStatus.class, scs);
		
	}
	
	public List getFederalReviewStatusesByFacilityId(String facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		return searchDAO.getSearchList(FederalReviewStatus.class, scs);
	}
	
	public List getReviewCommentByFacilityId(String facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		return searchDAO.getSearchList(ReviewComment.class, scs);
	}
	
	public List getFRRFacilityReviewStatusesByFacilityId(String facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("reviewStatusRef.reviewStatusId",SearchCondition.OPERATOR_EQ,"FRR"));
		return searchDAO.getSearchList(FacilityReviewStatus.class, scs);
	}
	
	public List getFRCFacilityReviewStatusesByFacilityId(String facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("reviewStatusRef.reviewStatusId",SearchCondition.OPERATOR_EQ,"FRC"));;
		return searchDAO.getSearchList(FacilityReviewStatus.class, scs);
	}
	
	
	
	public FacilityReviewStatus getLatestFRRorFRCFacilityReviewStatus(String facilityId){
		log.debug("--getLatestFRRorFRCFacilityReviewStatus--");
		List frrFacilityRevStatList = getFRRFacilityReviewStatusesByFacilityId(facilityId);
		List frcFacilityRevStatList = getFRCFacilityReviewStatusesByFacilityId(facilityId);
		List facilityRevStatList = new ArrayList();
		if(frrFacilityRevStatList != null && frrFacilityRevStatList.size() > 0){
			for(int I = 0; I < frrFacilityRevStatList.size(); I++){
				log.debug("frrFacilityRevStatList facilityDate = " + ((FacilityReviewStatus)frrFacilityRevStatList.get(I)).getId().getLastUpdateTs());
			}
			facilityRevStatList.addAll(frrFacilityRevStatList);
		}
		if(frcFacilityRevStatList != null && frcFacilityRevStatList.size() > 0){
			for(int I = 0; I < frcFacilityRevStatList.size(); I++){
				log.debug("frcFacilityRevStatList facilityDate = " + ((FacilityReviewStatus)frcFacilityRevStatList.get(I)).getId().getLastUpdateTs());
			}
			facilityRevStatList.addAll(frcFacilityRevStatList);
		}
		if(facilityRevStatList == null || facilityRevStatList.size() == 0){
			return null;
		}
		
		if(facilityRevStatList != null && facilityRevStatList.size() == 1){
			log.debug("only latestFacilityDate = " + ((FacilityReviewStatus)facilityRevStatList.get(0)).getId().getLastUpdateTs());
			return (FacilityReviewStatus)facilityRevStatList.get(0);
		}
		
		FacilityReviewStatus latestFacilityReviewStatus = (FacilityReviewStatus)facilityRevStatList.get(0);
		log.debug("Initial latestFacilityDate = " + latestFacilityReviewStatus.getId().getLastUpdateTs());
		log.debug("Initial latestFacilityDate type = " + latestFacilityReviewStatus.getReviewStatusRef().getReviewStatusId());
		log.debug("Initial latestFacilityDate user = " + latestFacilityReviewStatus.getLastUpdateUserid());
		Date latestFacilityDate;
		Date facilityDate;
		for(int I = 1; I < facilityRevStatList.size(); I++){
			latestFacilityDate = latestFacilityReviewStatus.getId().getLastUpdateTs();
			facilityDate = ((FacilityReviewStatus)facilityRevStatList.get(I)).getId().getLastUpdateTs();
			
			log.debug("latestFacilityDate = " + latestFacilityDate);
			log.debug("latestFacilityDate type = " + latestFacilityReviewStatus.getReviewStatusRef().getReviewStatusId());
			log.debug("latestFacilityDate user = " + latestFacilityReviewStatus.getLastUpdateUserid());
			log.debug("facilityDate = " + facilityDate);
			log.debug("facilityDate Type = " + ((FacilityReviewStatus)facilityRevStatList.get(I)).getReviewStatusRef().getReviewStatusId());
			log.debug("latestFacilityDate user = " + ((FacilityReviewStatus)facilityRevStatList.get(I)).getLastUpdateUserid());
			if(!latestFacilityDate.after(facilityDate)){
				log.debug("facilityDate = " + facilityDate + " > " + "latestFacilityDate = " + latestFacilityDate);
				latestFacilityReviewStatus = (FacilityReviewStatus)facilityRevStatList.get(I);
			}
		}
		
		return latestFacilityReviewStatus;
	}
	
	public ReviewComment getLatestReviewComment(String facilityId){
		log.debug("--getLatestReviewComment--");
		List revList = getReviewCommentByFacilityId(facilityId);
		ReviewComment latestReviewComment = null;
		Date latestReviewCommentDate;
		Date reviewCommentDate;
		if(revList != null && revList.size() == 1){
			return (ReviewComment)revList.get(0);
		}
		if(revList != null && revList.size() > 1){
			latestReviewComment = (ReviewComment)revList.get(0);
			for(int I = 0; I < revList.size(); I++){
				latestReviewCommentDate = latestReviewComment.getLastUpdateTs();
				reviewCommentDate = ((ReviewComment)revList.get(I)).getLastUpdateTs();
				log.debug("latestReviewCommentDate = " + latestReviewCommentDate);
				log.debug("reviewCommentDate = " + reviewCommentDate);
				if(!latestReviewCommentDate.after(reviewCommentDate)){
					latestReviewComment = (ReviewComment)revList.get(I);
				}
			}
			
		}
		return latestReviewComment;
	}
	
	public boolean isLastUpdateForReviewCommentLaterThanFacilityReviewStatus(String facilityId){
		boolean condition = false;
		try{
			FacilityReviewStatus facilityReviewStatus = getLatestFRRorFRCFacilityReviewStatus(facilityId);
			ReviewComment reviewComment = getLatestReviewComment(facilityId);
			if(facilityReviewStatus == null){
				return true;
			}else{
				if(reviewComment.getLastUpdateTs().after(facilityReviewStatus.getId().getLastUpdateTs())){
					return true;
				}
			}
		}catch(Exception e){
			log.error("ERROR when comparing latest date for ReviewComment and FacilityReviewStatus", e);
		}
		
		return condition;
	}
	
	public boolean isAllFacilityEntryStatusErrorFlagEqualNo(String facilityId){
		List entryStatuses = getFaciltiyEntryStatusesByFacilityId(facilityId);
		boolean condition = true;
		char errorFlag;
		for(int I = 0; I < entryStatuses.size(); I++){
			errorFlag = ((FacilityEntryStatus)entryStatuses.get(I)).getErrorFlag();
			log.debug("error flag = " + errorFlag);
			if(errorFlag == 'Y'){
				condition = false;
				return condition;
			}
		}
		return condition;
	}
	
	public boolean isAllFederalReviewStatusErrorFlagEqualNo(String facilityId){
		List reviewStatuses = getFederalReviewStatusesByFacilityId(facilityId);
		boolean condition = true;
		char errorFlag;
		for(int I = 0; I < reviewStatuses.size(); I++){
			errorFlag = ((FederalReviewStatus)reviewStatuses.get(I)).getErrorFlag();
			log.debug("error flag = " + errorFlag);
			if(errorFlag == 'Y'){
				condition = false;
				return condition;
			}
		}
		return condition;
	}
	
	
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}
	
	
	
}