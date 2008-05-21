package gov.epa.owm.mtb.cwns.dao.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Order;

import gov.epa.owm.mtb.cwns.dao.SummaryDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityComment;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataArea;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatusId;
import gov.epa.owm.mtb.cwns.model.FederalReviewStatus;

public class SummaryDAOImpl extends BaseDAOHibernate implements SummaryDAO {
    
    /**
     * Finds the summary elements by Facility Number
     * @param hSession
     * @param facilityNumber
     * @return summary results
     * @throws HibernateException
     */
    public List findSummaryByFacilityId(String facilityNumber) {
	List result;
		
    try {
    	Session session = this.getDAOSession();	
    	Criteria crit = session.createCriteria(FacilityEntryStatus.class)
                                .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN)
                                .createAlias("facility", "f", Criteria.INNER_JOIN);
    	result = crit.setProjection(Projections.projectionList()
    			                    .add(Projections.property("d.name"))
    			                    .add(Projections.property("requiredFlag"))
    			                    .add(Projections.property("enteredFlag"))
    			                    .add(Projections.property("errorFlag"))
    	                            )
    	              .add(Expression.eq("f.facilityId", new Long(facilityNumber)))
    	              .addOrder(Order.asc("d.sortSequence"))
    	              .list();
    	log.debug("test1 "+result.size());
    		    	
    } catch (HibernateException he) {
    	throw getHibernateTemplate().convertHibernateAccessException(he);
    }
     return result;
    }
    
    // Finds the values for correction column by dataarea type and facility number
    public String findCorrectionsByFacilityId(String facilityNumber, String dataarea) {
    	List result;
    	//String[] status = {"FRR","FRC","SCR"};
        try {
        	Session session = this.getDAOSession();	
        	Criteria crit = session.createCriteria(FederalReviewStatus.class)
                                    .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN)
                                    .createAlias("facility", "f", Criteria.INNER_JOIN);
        	result = crit.setProjection(Projections.projectionList()
        			                    
        			                    .add(Projections.property("errorFlag"))
        			                    //.add(Projections.property("f.reviewStatusRef.reviewStatusRefId"))
        	                            )
        	                        
        	              .add(Expression.eq("f.facilityId", new Long(facilityNumber)))
        	              .add(Expression.eq("d.name", dataarea))
        	              .list();
        	
        		    	
        } catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
        Iterator iter = result.iterator();
        if (iter.hasNext()) 
          return ((Character)iter.next()).toString();
        else
        	return "";
    }

    // find review status type by CWNS number and VersionCode
    public String findReviewStatusRefByFacilityIdAndVersionCode(String facNum, String facilityVersionCode) {
		List result;
		//String reviewstatustype = "";
		try {
			Session session = this.getDAOSession();
			Criteria crit = session.createCriteria(Facility.class);
			result = crit.setProjection(Projections.property("reviewStatusRef.reviewStatusId"))
			             .add(Expression.eq("facilityId", new Long(facNum)))
			             .add(Expression.eq("versionCode", facilityVersionCode))
       	                 .list();			
		} catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		return (String)result.get(0);
	}
	/*
	// finds facility Id by CWNS number
	public long findFacilityIdByCWNSNumber(String cwnsNbr) 
    {
	 long facilityId = -1;
	 List result;

	 log.debug("*****facilityNumber **: " + cwnsNbr);
	
     try {
    	 Session session = this.getDAOSession();	
    	 Criteria crit = session.createCriteria(Facility.class);
    	 
    	 crit = crit.setProjection(Projections.projectionList()
    			                    .add(Projections.property("facilityId").as("facilityId"))
    	                            )
    	              .add(Expression.eq("cwnsNbr", cwnsNbr));
       
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
    */
    
	// update FederalReviewStatus object
	public void save(String facId, String[] dataAreaTypes) {
		Session session = this.getDAOSession();
		List result;
		log.debug("cwnsnbr----"+facId);
		result = session.createCriteria(FederalReviewStatus.class)
                               .add(Expression.eq("facility.facilityId", new Long(facId)))
	                           .list();
		Iterator it = result.iterator();
		FederalReviewStatus federalreviewstatus;
		while (it.hasNext()) {
		   federalreviewstatus = (FederalReviewStatus) it.next();
					
				boolean matched = false;
				for (int index2=0; index2<dataAreaTypes.length; index2++){
					if ((federalreviewstatus.getDataAreaRef().getName())
							.equalsIgnoreCase(dataAreaTypes[index2])) {
						federalreviewstatus.setErrorFlag('Y');
						matched = true;
						break;
					}
			    }
				if (!matched) {
					federalreviewstatus.setErrorFlag('N');
				}
				saveObject(federalreviewstatus);	
			}
				
	}
    
	// find facility comments for each dataarea type by facility Id
	public List findCommentsByFacilityId(String facilityId) {
		List result;
		try {
			Session session = this.getDAOSession();
			Criteria crit = session.createCriteria(FacilityComment.class)
			                       .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN);
			result = crit.setProjection(Projections.projectionList()
					                    .add(Projections.property("d.name").as("DataAreaTypeName"))
					                    .add(Projections.property("lastUpdateTs").as("lastUpdateTs"))
					                    .add(Projections.property("description").as("description"))
					                    )
					      .add(Expression.eq("facility.facilityId", new Long(facilityId))) 
					      .addOrder(Order.asc("d.sortSequence"))
					      .list();
		} catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
			
		return result;
	}
	
	// find FacilityCostCurve object by facility Id
	public List findFacilityCostcurveByFacilityId(String facilityId){
		List result;
		try{
			Session session = this.getDAOSession();
			Criteria crit = session.createCriteria(FacilityCostCurve.class)
			                       .createAlias("facilityDocument", "fd", Criteria.INNER_JOIN);
			result = crit.setProjection(Projections.property("facilityCostCurveId"))
			             .add(Expression.eq("fd.facility.facilityId", new Long(facilityId)))
			             .list();
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		
		return result;
	}
    /*
	public Collection findCostCurveErrorsByFacilityId(String facNum) {
		List result;
		try{
			Session session = this.getDAOSession();
			Criteria crit = session.createCriteria(FacilityCostCurveDataArea.class)
			                       .createAlias("facilityCostCurve", "fc", Criteria.INNER_JOIN)
			                       .createAlias("fc.facilityDocument", "fd", Criteria.INNER_JOIN)
			                       .createAlias("dataAreaType", "d", Criteria.INNER_JOIN);
			                       
			result = crit.setProjection(Projections.projectionList()
					                    .add(Projections.property("d.name"))
					                    .add(Projections.property("errorFlag"))
					                   )
			             .add(Expression.eq("fd.facility.facilityId", new Long(facNum)))
			             .addOrder(Order.asc("d.sortSequence"))
			             .list();
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		
		return result;
	}
    */
	public Collection findCostCurveErrorsByFacilityId(String facNum) {
		List result;
		try{
			Session session = this.getDAOSession();
			Criteria crit = session.createCriteria(FacilityCostCurveDataArea.class)
			                       .createAlias("facilityCostCurve", "fc", Criteria.INNER_JOIN)
			                       .createAlias("fc.facilityDocument", "fd", Criteria.INNER_JOIN)
			                       .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN);
			                       
			result = crit.setProjection(Projections.projectionList()
					                    .add(Projections.groupProperty("d.name"))
					                    .add(Projections.max("errorFlag"))
					                    .add(Projections.groupProperty("d.sortSequence"))
					                   )
			             .add(Expression.eq("fd.facility.facilityId", new Long(facNum)))
			             .addOrder(Order.asc("d.sortSequence"))
			             .list();
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		
		return result;
	}
	
	public Date findFacilityEntryStatusLastupdatedts(String facNum, String dataAreaType) {
		
		List result;
		Date lastupdatets = null;
        try {
        	Session session = this.getDAOSession();	
        	Criteria crit = session.createCriteria(FacilityEntryStatus.class)
                                    .createAlias("dataAreaRef", "d", Criteria.INNER_JOIN);
                                    
        	result = crit.setProjection(Projections.projectionList()
        			                     .add(Projections.property("dataAreaLastUpdateTs"))
        			                   )
        	              .add(Expression.eq("facility.facilityId", new Long(facNum)))
        	              .add(Expression.eq("d.name", dataAreaType))
        	              .list();
        	if (result.iterator().hasNext()) 
    			lastupdatets = (Date)result.get(0);	
        		    	
        } catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
         return lastupdatets;
	}

	public Date findLatestFacilityReviewStatus(String facNum, String reviewstatustype) {
		List result;
		Date lastupdatets = null;
		try{
		    Session session = this.getDAOSession();
		    Criteria crit = session.createCriteria(FacilityReviewStatus.class);
		    result = crit.setProjection(Projections.projectionList()
		    		                     .add(Projections.max("id.lastUpdateTs"))
		    		                     .add(Projections.groupProperty("facility.facilityId"))
		    		                   )
		                 .add(Expression.eq("facility.facilityId", new Long(facNum)))
		                 .add(Expression.eq("reviewStatusRef.reviewStatusId", reviewstatustype))
		                 .list();
		  if (result.iterator().hasNext()){ 
			Object[] obj = (Object[])result.iterator().next();
			lastupdatets = (Date)(obj[0]);
		  }	
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }
		return lastupdatets;
	}

	public Collection findLastUpdatedUserIdAndDate(String facilityId) {
		List result;
		try{
		    Session session = this.getDAOSession();
		    Criteria crit = session.createCriteria(Facility.class);
		    result = crit.setProjection(Projections.projectionList()
		    		                     .add(Projections.property("lastUpdateUserid"))
		    		                     .add(Projections.property("lastUpdateTs"))
		                               )
		                  .add(Expression.eq("facilityId", new Long(facilityId)))
		                  .list();
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        }    
		return result;
	}

	public Object[] findLastReviewedUserIdAndDate(String facilityId) {
		Object[] obj = null;
		List result;
		try{
		    Session session = this.getDAOSession();
		    Criteria crit = session.createCriteria(FacilityReviewStatus.class);
		    result = crit.setProjection(Projections.projectionList()
		    		                     .add(Projections.property("lastUpdateUserid"))
		    		                     .add(Projections.property("id.lastUpdateTs"))
		                               )
		                  .add(Expression.eq("facility.facilityId", new Long(facilityId)))
		                  .add(Expression.or(Expression.eq("reviewStatusRef.reviewStatusId", "FA"), Expression.eq("reviewStatusRef.reviewStatusId", "SCR")))
		                  .addOrder(Order.desc("id.lastUpdateTs"))
		                  .list();
		}catch (HibernateException he) {
        	throw getHibernateTemplate().convertHibernateAccessException(he);
        } 
		if (result.iterator().hasNext())
			obj = (Object[])result.iterator().next();
		return obj;
	}
}
