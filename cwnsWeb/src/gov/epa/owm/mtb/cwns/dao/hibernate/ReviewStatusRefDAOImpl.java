package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.dao.ReviewStatusRefDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityDocument;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatusId;
import gov.epa.owm.mtb.cwns.model.FederalReviewStatus;
import gov.epa.owm.mtb.cwns.model.ReviewComment;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class ReviewStatusRefDAOImpl extends BaseDAOHibernate implements ReviewStatusRefDAO {

	public ReviewStatusRef getReviewStatusRefByCode(String statusCode) {
		 
		return (ReviewStatusRef) getHibernateTemplate().get(ReviewStatusRef.class, statusCode);
	}

	public Collection findFacilityCountByReviewStatusRef(Collection locationIds) {
		try{
		    Session session = this.getDAOSession();
		    Query q = session.getNamedQuery("reviewstatistics");
		    q.setParameterList("location", locationIds);
		    return q.list();
		}
	    catch (HibernateException he) {
    	  throw getHibernateTemplate().convertHibernateAccessException(he);
        } 
	}
	
	public Collection getFacilityReviewStatus(String facilityId) {
		List result;
		try {
	    	Session session = this.getDAOSession();	
	    	Criteria crit = session.createCriteria(Facility.class)
	    	                       .createAlias("reviewStatusRef", "r", Criteria.INNER_JOIN);
	                                
	    	result = crit.setProjection(Projections.projectionList()
	    			                     .add(Projections.property("r.reviewStatusId"))
	    			                     .add(Projections.property("r.name"))
	    			                    ) 
	    	              .add(Expression.eq("facilityId", new Long(facilityId)))
	    	              .list();
	    		    		    	
	    } catch (HibernateException he) {
	    	throw getHibernateTemplate().convertHibernateAccessException(he);
	    }
		return result;
	}
		
	public Collection getFacilityEntryStatusWithErrors(String facilityId) {
        List result;
		
	    try {
	    	Session session = this.getDAOSession();	
	    	Criteria crit = session.createCriteria(FacilityEntryStatus.class);
	                                
	    	result = crit.add(Expression.eq("facility.facilityId", new Long(facilityId)))
	    	              .add(Expression.eq("errorFlag", new Character('Y')))
	    	              .list();
	    	
	    } catch (HibernateException he) {
	    	throw getHibernateTemplate().convertHibernateAccessException(he);
	    }
	     return result;
	}

	public Collection getFederalReviewStatusWithErrors(String facilityId) {
       List result;
		
	    try {
	    	Session session = this.getDAOSession();	
	    	Criteria crit = session.createCriteria(FederalReviewStatus.class);
	                                
	    	result = crit.add(Expression.eq("facility.facilityId", new Long(facilityId)))
	    	              .add(Expression.eq("errorFlag", new Character('Y')))
	    	              .list();
	    	
	    } catch (HibernateException he) {
	    	throw getHibernateTemplate().convertHibernateAccessException(he);
	    }
	     return result;
	}

	public Date getLatestReviewCommentTs(String facilityId) {
		List result;
		Date lastupdatedts = null;
		try{
		    Session session = this.getDAOSession();
		    Criteria crit = session.createCriteria(ReviewComment.class);
		    result = crit.setProjection(Projections.property("lastUpdateTs"))
		    		     .add(Expression.eq("facility.facilityId", new Long(facilityId)))
		                 .addOrder(Order.desc("lastUpdateTs"))
		                 .list();
		if (result.iterator().hasNext()) 
			lastupdatedts = (Date)result.get(0);
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		return lastupdatedts;
	}

	public Date getLatestReviewStatusTs(String facilityId) {
		List result;
		Date lastupdatedts = null;
		try{
		    Session session = this.getDAOSession();
		    Criteria crit = session.createCriteria(FacilityReviewStatus.class);
		    result = crit.setProjection(Projections.property("id.lastUpdateTs"))
		    		     .add(Expression.eq("facility.facilityId", new Long(facilityId)))
		    		     .addOrder(Order.desc("id.lastUpdateTs"))
		                 .list();
		if (result.iterator().hasNext()){ 
			lastupdatedts = (Date)result.get(0);
		}	
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		return lastupdatedts;
	}

	public Collection getFacilityFeedbackStatus(String cwnsNbr) {
		List result;
		
		try{
		    Session session = this.getDAOSession();
            	    
		    Criteria crit = session.createCriteria(Facility.class)
		                           .createAlias("reviewStatusRef", "r");
		    result = crit.setProjection(Projections.projectionList()
		    		                               .add(Projections.property("r.reviewStatusId"))
		    		                               .add(Projections.property("r.name"))
		    		                    )           
		    		     .add(Expression.eq("cwnsNbr", cwnsNbr))
		    		     .add(Expression.eq("versionCode", new Character('F')))
		    		     .list();
		
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		return result;
	}

	public boolean hasFeedbackVersion(String cwnsNbr) {
		List result;
		boolean hasfeedbackversion = false;
		try{
		    Session session = this.getDAOSession();
            	    
		    Criteria crit = session.createCriteria(Facility.class);
		                           
		    result = crit.add(Expression.eq("cwnsNbr", cwnsNbr))
		    		     .add(Expression.eq("versionCode", new Character('F')))
		    		     .list();
		if (result.iterator().hasNext()) 
			hasfeedbackversion = true;
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		return hasfeedbackversion;
	}

	public String getCWNSNbrByFacilityId(String facilityId) {
		List result;
		String cwnsNbr ="";
		try{
		    Session session = this.getDAOSession();
            // Get CWNSNBR for facility
	    	Criteria criteria = session.createCriteria(Facility.class);
	    	result = criteria.setProjection(Projections.property("cwnsNbr"))
		                     .add(Expression.eq("facilityId", new Long(facilityId)))
		                     .list();		 
		    if (result.iterator().hasNext()) 
		    	cwnsNbr = (String)result.get(0);
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        } 
		return cwnsNbr;
	}

	public Collection getAllReviewStatues() {
		
		
		return null;
	}

	public Collection getFacilityDocumentByType(String facilityId) {
		List result;
		try{
		    Session session = this.getDAOSession();
            Criteria criteria = session.createCriteria(FacilityDocument.class)
                                        .createAlias("document", "d", Criteria.INNER_JOIN); 
            result = criteria.setProjection(Projections.property("facility.facilityId"))
                             .add(Expression.eq("facility.facilityId", new Long(facilityId)))
                             .add(Expression.eq("d.documentTypeRef.documentTypeId", "71"))
                             .list();
		   }catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
           }   
		return result;
	}

	public Collection getFacilityReviewStatusHistory(String facilityId) {
		List result;
		try{
			Session session = this.getDAOSession();
            Criteria criteria = session.createCriteria(FacilityReviewStatus.class);
            result = criteria.setProjection(Projections.property("reviewStatusRef.reviewStatusId"))
                             .add(Expression.eq("facility.facilityId", new Long(facilityId))) 
		                     .add(Expression.ne("reviewStatusRef.reviewStatusId", "DE"))
		                     .addOrder(Order.desc("id.lastUpdateTs"))
		                     .list();
	       }catch (HibernateException he) {
    	    throw getHibernateTemplate().convertHibernateAccessException(he);
           } 
		return result;
	}

	public void createFeedbackFacility(String facilityId, String userId) {
		try{
			String ff_status="";
			String ff_status_msg="";
			long FF_NEW_Fid=0;
		    Session session = this.getDAOSession();
		    PreparedStatement ps=  session.connection().prepareStatement("{call PKG_FACILITY_FEEDBACK.CREATE_FEEDBACK_FACILITY(?,false,?,?,?,?)}");
		    ps.setLong(1, (new Long(facilityId)).longValue());
		    ps.setString(2, userId);
		    ps.setLong(3, FF_NEW_Fid);
		    ps.setString(4, ff_status);
		    ps.setString(5, ff_status_msg);
		    ResultSet resultSet = ps.executeQuery();
		    
		} catch (HibernateException x) {
            throw getHibernateTemplate().convertHibernateAccessException(x);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    
}
