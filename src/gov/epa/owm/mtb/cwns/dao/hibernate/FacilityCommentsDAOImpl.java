package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.dao.FacilityCommentsDAO;
import gov.epa.owm.mtb.cwns.model.DataAreaRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityComment;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class FacilityCommentsDAOImpl extends BaseDAOHibernate implements FacilityCommentsDAO {



	public FacilityReviewStatus getLatestFacilityReviewStatus(String facilityId) {
		List result;

		try{
		    Session session = this.getDAOSession();
		    Criteria crit = session.createCriteria(FacilityReviewStatus.class);
		    result = crit.setProjection(Projections.property("id.lastUpdateTs"))
		    		     .add(Expression.eq("facility.facilityId", new Long(facilityId)))
		    		     .addOrder(Order.desc("id.lastUpdateTs"))
		                 .list();
		if (result.iterator().hasNext()){ 
			return (FacilityReviewStatus)result.iterator().next();
		}	
		}catch (HibernateException he) {
	    	throw getHibernateTemplate().convertHibernateAccessException(he);
	    }
		return null;
	}	
	
	public boolean isFeedbackVersionValid(long facilityId, String dataAreaName)
	{
	   	 Collection resultList = null;
	   	 
	   	 Date fesDate = null, frsDate = null;

		 log.debug("*****facilityId/dataAreaName*****: " + facilityId + "/" + dataAreaName);
		
	     try {
	     	Session session = this.getDAOSession();	
	    	Criteria crit = session.createCriteria(FacilityEntryStatus.class, "fes")
	                                .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN)
	                                .createAlias("facility", "f", Criteria.INNER_JOIN);
	    	
	    	crit = crit.setProjection(Projections.projectionList()
			                .add(Projections.property("fes.lastUpdateTs").as("lastUpdateTs"))
	    			                    )
	    	              .add(Expression.eq("f.facilityId", new Long(facilityId)))
	    	              .add(Expression.eq("d.name", getDataAreaNameFirstPart(dataAreaName)));
	    	
	    	resultList = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

	     } catch (HibernateException he) {
	      	 throw getHibernateTemplate().convertHibernateAccessException(he);
	     }

	     if(resultList == null)
	    	 return false;
	     
		 log.debug("FacilityEntryStatus*****: " + resultList.size());    
	     
		Iterator iter = resultList.iterator();
		
		if(iter.hasNext()) {

    	    Map map = (Map) iter.next();
    	    fesDate = (Date)map.get("lastUpdateTs");

			 log.debug("fesDate*****: " + fesDate.toString());    
			
		}
		else
			return false;
		
		resultList = null;
		
	     try {
		     	Session session = this.getDAOSession();	
		    	Criteria crit = session.createCriteria(FacilityReviewStatus.class, "frs")
		                                .createAlias("reviewStatusRef", "r", Criteria.INNER_JOIN)
		                                .createAlias("facility", "f", Criteria.INNER_JOIN);
		    	
		    	crit = crit.setProjection(Projections.projectionList()
		    			                 .add( Projections.max("frs.id.lastUpdateTs").as("maxLastUpdateTs") )
		    			                    )
		    	              .add(Expression.eq("f.facilityId", new Long(facilityId)))
		    	              .add(Expression.eq("r.reviewStatusId", "LAS"));
		    	
		    	resultList = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

		     } catch (HibernateException he) {
		      	 throw getHibernateTemplate().convertHibernateAccessException(he);
		     }
		 
		     if(resultList == null)
		    	 return false;

			 log.debug("FacilityReviewStatus*****: " + resultList.size());    		     
		     
			iter = resultList.iterator();

			if(iter.hasNext()) {

	    	    Map map = (Map) iter.next();
	    	    frsDate = (Date)map.get("maxLastUpdateTs");

				 log.debug("frsDate*****: " + frsDate.toString());    
	    	    
			}
			else
				return false;

			if(frsDate == null || frsDate == null)
				return false;
		
			if(fesDate.after(frsDate))
				return true;
			
			return false;
	 }
	
    public List findFacilityCommentsByFacilityIdAndDataAreaName(long facilityId, String dataAreaName)
    {
   	 List result;

	 log.debug("*****facilityId/dataAreaName*****: " + facilityId + "/" + dataAreaName);
	
     try {
     	Session session = this.getDAOSession();	
    	Criteria crit = session.createCriteria(FacilityComment.class, "fc")
                                .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN)
                                .createAlias("facility", "f", Criteria.INNER_JOIN);
    	
    	crit = crit.setProjection(Projections.projectionList()
		                .add(Projections.property("fc.facilityCommentId").as("facilityCommentId"))
		                .add(Projections.property("fc.description").as("description"))
		                .add(Projections.property("fc.lastUpdateUserid").as("lastUpdateUserid"))
		                .add(Projections.property("fc.lastUpdateTs").as("lastUpdateTs"))
    			                    )
    	              .add(Expression.eq("f.facilityId", new Long(facilityId)))
    	              .add(Expression.eq("d.name", getDataAreaNameFirstPart(dataAreaName)));
    	
   	    result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
    	
    	log.debug("test1 - " + result.size());

     } catch (HibernateException he) {
      	 throw getHibernateTemplate().convertHibernateAccessException(he);
     }
       return result;    	
    }
	
	public int countFacilityComments(long facilityId, String dataAreaName)
	{

		log.debug("*****facilityId/dataAreaName*****: " + facilityId + "/" + dataAreaName);
		
		Criteria crit;
		try {

			Session session = this.getDAOSession();
			
	    	crit = session.createCriteria(FacilityComment.class, "fc")
            						.createAlias("dataAreaRef", "d", Criteria.INNER_JOIN)
            						.createAlias("facility", "f", Criteria.INNER_JOIN);

	    	String dataAreaNameFirstPart = dataAreaName;
	    	
	    	int firstBlankIndex = dataAreaName.indexOf(" ");
	    	if(firstBlankIndex > 0)
	    		dataAreaNameFirstPart = dataAreaName.substring(0, firstBlankIndex);

	    	log.debug("dataAreaNameFirstPart - " + dataAreaNameFirstPart);    	
	    	
            crit = crit.add(Expression.eq("f.facilityId", new Long(facilityId)))
	    	              .add(Expression.eq("d.name", getDataAreaNameFirstPart(dataAreaName)));
             
             crit.setProjection(Projections.rowCount());
			
		} catch (HibernateException x) {
			throw getHibernateTemplate().convertHibernateAccessException(x);
		}
		return ((Integer) crit.uniqueResult()).intValue();

	}
	public void addFacilityComments(long facilityId, String dataAreaName, String comments, String User)
	{

		log.debug("*****facilityId/dataAreaName/User*****: " + facilityId + "/" + dataAreaName + "/" + User);
		
		try
    	{
    		FacilityComment facCom = new FacilityComment();
	    	
	    	Facility f = new Facility();
	    	f.setFacilityId(facilityId);

	    	facCom.setFacility(f);
	    	facCom.setDataAreaRef(getDataAreaRefFromName(dataAreaName));
	    	facCom.setDescription(comments);
	    	facCom.setLastUpdateTs(new Date());
	    	facCom.setLastUpdateUserid(User);
	    	
	    	saveObject(facCom);
    	}
    	catch (HibernateException he) {
         	 throw getHibernateTemplate().convertHibernateAccessException(he);
        }
	}
	
	public void updateFacilityComments(long facilityCommentId, String comments, String User)
	{
		log.debug("*****facilityCommentId/comments/User*****: " + facilityCommentId + "/" + comments + "/" + User);
		
		try
    	{
    		FacilityComment facCom = (FacilityComment)getObject(FacilityComment.class, new Long(facilityCommentId));

	    	facCom.setDescription(comments);
	    	facCom.setLastUpdateTs(new Date());
	    	facCom.setLastUpdateUserid(User);
	    	
	    	saveObject(facCom);
    	}
    	catch (HibernateException he) {
         	 throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		
	}

	public void deleteFacilityComments(long facilityId, String dataAreaName)
	{
		
	}
	
	public void deleteFacilityComments(long facilityCommentId)
	{
		log.debug("*****facilityCommentId*****: " + facilityCommentId);
		
		try
    	{
    		removeObject(FacilityComment.class, new Long(facilityCommentId));
    	}
    	catch (HibernateException he) {
         	 throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		
	}
	
	private String getDataAreaNameFirstPart(String dataAreaName)
	{
    	String dataAreaNameFirstPart = dataAreaName;
    	
    	int firstBlankIndex = dataAreaName.indexOf(" &");
    	if(firstBlankIndex > 0)
    		dataAreaNameFirstPart = dataAreaName.substring(0, firstBlankIndex);

		log.debug("*****dataAreaNameFirstPart*****: " + dataAreaNameFirstPart);
    	
    	return dataAreaNameFirstPart;
	}
	
	public DataAreaRef getDataAreaRefFromName(String dataAreaName)
	{
		log.debug("*****dataAreaName*****: " + dataAreaName);
		
		Object result;
		
		try {

			Session session = this.getDAOSession();
			
	    	result = session.createCriteria(DataAreaRef.class, "da")
	    	             .add(Expression.eq("da.name", getDataAreaNameFirstPart(dataAreaName)))
	    	             .uniqueResult();
			
		} catch (HibernateException x) {
			throw getHibernateTemplate().convertHibernateAccessException(x);
		}
		
		return (DataAreaRef)result;
	}

	public List findFacilityCommentsByFacilityIdAndDataAreaName(long facilityId, Long dataAreaId) {
		List result;

		 log.debug("*****facilityId/dataAreaID*****: " + facilityId + "/" + dataAreaId.toString());
		
	     try {
	     	Session session = this.getDAOSession();	
	    	Criteria crit = session.createCriteria(FacilityComment.class, "fc")
	                                .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN)
	                                .createAlias("facility", "f", Criteria.INNER_JOIN);
	    	
	    	crit = crit.setProjection(Projections.projectionList()
			                .add(Projections.property("fc.facilityCommentId").as("facilityCommentId"))
			                .add(Projections.property("fc.description").as("description"))
			                .add(Projections.property("fc.lastUpdateUserid").as("lastUpdateUserid"))
			                .add(Projections.property("fc.lastUpdateTs").as("lastUpdateTs"))
	    			                    )
	    	              .add(Expression.eq("f.facilityId", new Long(facilityId)))
	    	              .add(Expression.eq("d.dataAreaId", dataAreaId));
	    	
	   	    result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
	    	
	    	log.debug("test1 - " + result.size());

	     } catch (HibernateException he) {
	      	 throw getHibernateTemplate().convertHibernateAccessException(he);
	     }
	       return result;    	
	}

}
