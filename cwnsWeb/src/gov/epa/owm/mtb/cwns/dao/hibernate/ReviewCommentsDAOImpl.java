package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.ReviewCommentsDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FederalReviewStatus;
import gov.epa.owm.mtb.cwns.model.ReviewComment;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class ReviewCommentsDAOImpl extends BaseDAOHibernate implements ReviewCommentsDAO {
    
    /**
     * Finds the review comments elements by Facility Id
     * @param facilityId
     * @return review comments results
     */
    public List findReviewCommentsByFacilityId(long facilityId, int firstResult, int maxResults) 
    {
	 List result;

	 log.debug("*****facilityId***** " + facilityId);
	
     try {
    	 Session session = this.getDAOSession();	
    	 Criteria crit = session.createCriteria(ReviewComment.class, "r")
                                .createAlias("facility", "f", Criteria.INNER_JOIN);
    	 
    	 crit = crit.setProjection(Projections.projectionList()
    			                    .add(Projections.property("r.reviewCommentId").as("reviewCommentId"))
    			                    .add(Projections.property("r.description").as("description"))
    			                    .add(Projections.property("r.lastUpdateUserid").as("lastUpdateUserid"))
    			                    .add(Projections.property("r.lastUpdateTs").as("lastUpdateTs"))
    	                            )
    	              .add(Expression.eq("f.facilityId", new Long(facilityId)))
    	              .addOrder(Order.desc("r.lastUpdateTs"))
    	              .setFirstResult(firstResult);
    	 
    	 if(maxResults > 0)
    		 crit = crit.setMaxResults(maxResults); 
    	              
    	 result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

     } catch (HibernateException he) {
      	 throw getHibernateTemplate().convertHibernateAccessException(he);
     }
       return result;
    }

    /**
     * Finds the review comments elements by Facility Number
     * @param facilityNumber
     * @return facilityVersionCode version code
     */
    public long findFacilityIdByFacilityNumberAndVersionCode(String facilityNumber, String facilityVersionCode) 
    {
	 long facilityId = -1;
	 List result;

	 log.debug("*****facilityNumber + facilityVersionCode*****: " + facilityNumber + " + " + facilityVersionCode);
	
     try {
    	 Session session = this.getDAOSession();	
    	 Criteria crit = session.createCriteria(Facility.class);
    	 
    	 crit = crit.setProjection(Projections.projectionList()
    			                    .add(Projections.property("facilityId").as("facilityId"))
    	                            )
    	              .add(Expression.eq("cwnsNbr", facilityNumber))
    	              .add(Expression.eq("versionCode", facilityVersionCode));
       
    	 result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
    	 
     	Iterator iter = result.iterator();
    	while ( iter.hasNext() ) {
    	    Map map = (Map) iter.next();
            facilityId = ((Long)map.get("facilityId")).longValue();
            break;
    	}

     } catch (HibernateException he) {
      	 throw getHibernateTemplate().convertHibernateAccessException(he);
     }
	 log.debug("*****facilityId*****: " + facilityId);
     return facilityId;
    }
    
    public long fetchFacilityIdByVersionCode(long srcFacilityId, String srcVersionCode, String targetVersionCode)
    {
   	 long targetFacilityId = gov.epa.owm.mtb.cwns.reviewComments.ReviewCommentsListForm.INITIAL_LONG_VALUE;
	 //List result;

	 log.debug("*****srcFacilityId + srcVersionCode + targetVersionCode*****: " + 
			           srcFacilityId + " + " + 
			           srcVersionCode + "+" + 
			           targetVersionCode);
	
     try {
    	 Session session = this.getDAOSession();	
    	 /*
    	 Criteria crit = session.createCriteria("Facility", "A")
    	                        .createAlias("Facility", "B")
    	                        .add(Expression.eq("B.facilityId", new Long(srcFacilityId)))
    	                        .add(Expression.eq("A.versionCode", targetVersionCode))
    	                        .add(Expression.eq("B.versionCode", srcVersionCode))    	                        
    	                        .add(Restrictions.eqProperty("A.cwnsNbr", "B.cwnsNbr"));
    	 
    	 crit = crit.setProjection(Projections.projectionList()
    			                    .add(Projections.property("A.facilityId").as("facId"))
    	                            );
       
    	 result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
    	 
     	Iterator iter = result.iterator();
    	while ( iter.hasNext() ) {
    	    Map map = (Map) iter.next();
    	    targetFacilityId = ((Long)map.get("facId")).longValue();
            break;
    	}
        */
    	 
         String facHql = " select A.facilityId from Facility A, Facility B " + 
                         " where A.cwnsNbr = B.cwnsNbr and " + 
                         "       A.versionCode = :targetVerCode and " + 
                         "       B.facilityId =  :srcFacId and " +
                         "       B.versionCode = :srcVerCode ";
                         
         Query facQuery = session.createQuery(facHql);
         facQuery.setLong("srcFacId", srcFacilityId);
         facQuery.setString("targetVerCode", targetVersionCode);
         facQuery.setString("srcVerCode", srcVersionCode);

         List facList = facQuery.list();

         if (facList.size() > 0)
         {
        	 targetFacilityId = ((Long)facList.get(0)).longValue();
         }
        	 
     } catch (HibernateException he) {
      	 throw getHibernateTemplate().convertHibernateAccessException(he);
     }
     
	 log.debug("*****targetFacilityId*****: " + targetFacilityId);
     return targetFacilityId;    
 }
    
    public void addComments(long facilityId, String comments, String User)
    {
    	try
    	{
	    	ReviewComment revCom = new ReviewComment();
	    	
	    	Facility f = new Facility();
	    	f.setFacilityId(facilityId);
	    	
	    	revCom.setFacility(f);
	    	revCom.setDescription(comments);
	    	revCom.setLastUpdateTs(new Date());
	    	revCom.setLastUpdateUserid(User);
	    	
	    	saveObject(revCom);
    	}
    	catch (HibernateException he) {
         	 throw getHibernateTemplate().convertHibernateAccessException(he);
        }
    }
    
	public int countReviewComments(long facilityId) {
		Criteria crit;
		try {

			Session session = this.getDAOSession();
			
	    	 crit = session.createCriteria(ReviewComment.class, "r")
                                          .createAlias("facility", "f", Criteria.INNER_JOIN);

             crit = crit.add(Expression.eq("f.facilityId", new Long(facilityId)));
             
             crit.setProjection(Projections.rowCount());
			
		} catch (HibernateException x) {
			throw getHibernateTemplate().convertHibernateAccessException(x);
		}
		return ((Integer) crit.uniqueResult()).intValue();
	}
	
	public List getReviewCommentsByFacilityId(String facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		return searchDAO.getSearchList(ReviewComment.class, scs);
	}
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}
}
