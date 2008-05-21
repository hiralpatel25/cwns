package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.dao.NeedsDAO;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.CwnsInfoRef;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.model.DocumentGroupRef;
import gov.epa.owm.mtb.cwns.model.DocumentTypeGroupRef;
import gov.epa.owm.mtb.cwns.model.DocumentTypeRef;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityDocument;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.needs.NeedsHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class NeedsDAOImpl extends BaseDAOHibernate implements NeedsDAO {

	public Collection getDocumentGroupInfo()
	{
		List result;

		try {
			
			Session session = this.getDAOSession();
			
			Criteria crit = session.createCriteria(DocumentGroupRef.class);
			
			crit = crit.setProjection(
					Projections.projectionList()
					            .add(Projections.property("documentGroupId").as("documentGroupId"))
							    .add(Projections.property("name").as("name"))
							    )
			                .addOrder(Order.asc("documentGroupId"));

      	    result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
		
		return result;		
	}
	
	public Collection getDocFacByRES(String documentId)
	{
		List result;

		try {
			
			Session session = this.getDAOSession();
			
			Criteria crit = session.createCriteria(FacilityDocument.class)
					.createAlias("facility", "f", Criteria.INNER_JOIN)
					.createAlias("f.reviewStatusRef", "res", Criteria.INNER_JOIN);
			
			crit = crit.setProjection(
					Projections.projectionList()
					            .add(Projections.property("f.facilityId").as("facilityId"))
							    )
							.add(Expression.eq("id.documentId", new Long(documentId)))
							.add(Expression.or(Expression.or(Expression.eq("res.reviewStatusId", "FRR"),
									                         Expression.eq("res.reviewStatusId", "FA")),
									           Expression.eq("res.reviewStatusId", "FRC")
									          )
								);
			
      	    result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
			log.debug("test1 - " + result.size());

		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
		
		return result;
	}
	
	public boolean existDuplicateCostCategory(String facilityId, String documentType)
	{
		boolean resultExist = false;
		
		if((facilityId == null || facilityId.equals("")) || 
		   (documentType == null || documentType.equals("")))
			return resultExist;	
		
		List result;
		
		try {
			
			Session session = this.getDAOSession();
			
			Criteria crit = session.createCriteria(FacilityDocument.class)
								.createAlias("document", "d", Criteria.INNER_JOIN)
								.createAlias("facility", "f", Criteria.INNER_JOIN)								
								.createAlias("d.documentTypeRef", "dt", Criteria.INNER_JOIN);
			
			crit = crit.setProjection(Projections.rowCount())
			                .add(Expression.eq("f.facilityId", new Long(facilityId)))
			                .add(Expression.eq("dt.documentTypeId", documentType));

			if(((Integer) crit.uniqueResult()).intValue() <= 1)
				return false;
			
			crit = session.createCriteria(Cost.class)
			.createAlias("categoryRef", "cr", Criteria.INNER_JOIN)
			.createAlias("facilityDocument", "fd", Criteria.INNER_JOIN)
			.createAlias("fd.facility", "f", Criteria.INNER_JOIN)			
			.createAlias("fd.document", "d", Criteria.INNER_JOIN)
			.createAlias("d.documentTypeRef", "dt", Criteria.INNER_JOIN);

			crit.setProjection(Projections.distinct(
					Projections.projectionList()
					            .add(Projections.property("cr.categoryId").as("categoryId"))
							    .add(Projections.property("fd.id.facilityId").as("facilityId"))
							    .add(Projections.property("fd.id.documentId").as("documentId"))
							    ))
			                .add(Expression.eq("f.facilityId", new Long(facilityId)))
			                .add(Expression.eq("dt.documentTypeId", documentType))							    
			                .addOrder(Order.asc("categoryId"));		

			result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
			String catIdTmp = "";
			
	    	Iterator iter = result.iterator();
	    	
	    	while ( iter.hasNext() ) {
	    	    Map map = (Map) iter.next();
	    	    
	    	    if(!catIdTmp.equalsIgnoreCase((String)map.get("categoryId")))
	    	    {
	    	    	catIdTmp = (String)map.get("categoryId");
	    	    }
	    	    else
	    	    {
	    	    	return true;
	    	    }
	    	}
		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
		return resultExist;
	}
	
	private void restrictDocType(Criteria critToAddExpr, String facilityId, String documentId)
	{
		if((facilityId == null || facilityId.equals("")) && (documentId == null || documentId.equals("")))
				return;

		try {
			
			Session session = this.getDAOSession();
			
			//check facilty PRI and federalNeedsForPrivateFlag
			Criteria crit = session.createCriteria(FacilityType.class)
								.createAlias("facility", "f", Criteria.INNER_JOIN)
								.createAlias("facilityTypeRef", "ftr", Criteria.INNER_JOIN);
			
			crit = crit.setProjection(Projections.rowCount())
							   .add(Expression.eq("f.facilityId", new Long(facilityId)))
							   .add(Expression.eq("f.ownerCode", "PRI"))
							   .add(Expression.eq("ftr.federalNeedsForPrivateFlag", new Character('N')));

			//if(((Integer) crit.uniqueResult()).intValue() > 0)
			//{
			//	critToAddExpr.add(Expression.ne("ntr.needTypeId", "F"));
			//}

			if((documentId == null || documentId.equals("")))
				return;
			
			// check facility cost curve
			crit = session.createCriteria(FacilityCostCurve.class)
			.createAlias("facilityDocument", "fd", Criteria.INNER_JOIN)
			.createAlias("fd.document", "d", Criteria.INNER_JOIN);

			crit = crit.setProjection(Projections.rowCount())
		   .add(Expression.eq("d.documentId", new Long(documentId)));
		
			if(((Integer) crit.uniqueResult()).intValue() > 0)
			{
				critToAddExpr.add(Expression.ne("dt.aprovedForNeedsFlag", "N"));
			}

			// check cost method
			crit = session.createCriteria(Cost.class)
			.createAlias("facilityDocument", "fd", Criteria.INNER_JOIN)
			.createAlias("fd.document", "d", Criteria.INNER_JOIN);

			crit = crit.setProjection(Projections.rowCount())
		   .add(Expression.eq("d.documentId", new Long(documentId)))
		   .add(Expression.eq("costMethodCode", new Character('D')));
		
			if(((Integer) crit.uniqueResult()).intValue() > 0)
			{
				critToAddExpr.add(Expression.ne("dt.aprovedForCostsFlag", "N"));
			}
			
		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
				
	}
	
	public Collection getDocumentGroupTypeInfo(String facilityId, String documentId)
	{
		List result;

		try {
			
			Session session = this.getDAOSession();
			
			Criteria crit = session.createCriteria(DocumentTypeGroupRef.class)
								.createAlias("documentTypeRef", "dt", Criteria.INNER_JOIN);
								//.createAlias("dt.needTypeRef", "ntr", Criteria.INNER_JOIN);
			
			crit = crit.setProjection(Projections.distinct(
					Projections.projectionList()
					            .add(Projections.property("id.documentGroupId").as("documentGroupId"))
							    .add(Projections.property("id.documentTypeId").as("documentTypeId"))
							    .add(Projections.property("dt.name").as("documentTypeName"))
							    ))
			                .addOrder(Order.asc("documentGroupId"))
					        .addOrder(Order.asc("documentTypeId"));
             // Has been removed from the business rules - Jyothi
			//restrictDocType(crit, facilityId, documentId);
			
      	    result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
		
		return result;			
	}

	
	public Collection getDistinctDocumentTypeInfo(String facilityId, String documentId)
	{
		List result;

		try {
			
			Session session = this.getDAOSession();
			/*
			Criteria crit = session.createCriteria(DocumentTypeRef.class)
								.createAlias("documentTypeRef", "dt", Criteria.INNER_JOIN)
								.createAlias("dt.needTypeRef", "ntr", Criteria.INNER_JOIN);
			
			crit = crit.setProjection(
					Projections.distinct(Projections.projectionList()
							    .add(Projections.property("id.documentTypeId").as("documentTypeId"))
							    .add(Projections.property("dt.name").as("documentTypeName"))
							    ))
					        .addOrder(Order.asc("documentTypeId"));
			*/

			Criteria crit = session.createCriteria(DocumentTypeRef.class, "dt");
								//.createAlias("needTypeRef", "ntr", Criteria.INNER_JOIN);

			crit = crit.setProjection(
					Projections.distinct(Projections.projectionList()
							    .add(Projections.property("dt.documentTypeId").as("documentTypeId"))
							    .add(Projections.property("dt.name").as("documentTypeName"))
							    ))
					        .addOrder(Order.asc("documentTypeId"));
			
			restrictDocType(crit, facilityId, documentId);
			
      	    result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
		
		return result;			
	}
		
	
	public Collection getDocumentAndType(String facilityNumber) {

		List result;

		try {
			
			Session session = this.getDAOSession();
			
			Criteria crit = session.createCriteria(FacilityDocument.class)
					.createAlias("facility", "f", Criteria.INNER_JOIN)
					.createAlias("document", "d", Criteria.INNER_JOIN)
					.createAlias("d.documentTypeRef", "dt", Criteria.INNER_JOIN);
			
			crit = crit.setProjection(
					Projections.projectionList()
					            .add(Projections.property("d.documentId").as("d_documentId"))
							    .add(Projections.property("d.titleName").as("d_titleName"))
							    .add(Projections.property("d.authorName").as("d_authorName"))
							    .add(Projections.property("d.publishedDate").as("d_publishedDate"))
							    .add(Projections.property("d.outdatedDocCertificatnFlag").as("d_outdatedDocCertificatnFlag"))
							    .add(Projections.property("d.repositoryId").as("d_repositoryId"))
							    .add(Projections.property("dt.documentTypeId").as("dt_documentTypeRefId"))
							    .add(Projections.property("dt.name").as("dt_documentTypeRefName"))
							    .add(Projections.property("dt.allowFootnoteFlag").as("dt_allowFootnoteFlag"))
							    .add(Projections.property("feedbackDeleteFlag").as("feedbackDeleteFlag"))
							    )
							.add(Expression.eq("f.facilityId", new Long(facilityNumber)))
			                .addOrder(Order.asc("d.titleName"));

      	    result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
			log.debug("test1 - " + result.size());

		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
		
		return result;
	}

	public Collection getAdjustedAmounts(String facilityNumber, Long documentId)
	{

		List result;

		try {
			
			Session session = this.getDAOSession();
			
			Criteria crit = session.createCriteria(Cost.class)
					.createAlias("facilityDocument", "fd", Criteria.INNER_JOIN);
			
			crit = crit.setProjection(
					Projections.projectionList()
							    .add(Projections.sum("adjustedAmount").as("adjust_amount_sum"))
							    .add(Projections.groupProperty("needTypeRef.needTypeId"), "needTypeId")
							    )
							.add(Expression.eq("fd.document.documentId", documentId))
							.add(Expression.eq("fd.facility.facilityId", new Long(facilityNumber)));

      	    result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
			log.debug("test2 - " + result.size());

		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
		
		return result;
	}
	
	public CwnsInfoRef getCwnsInfoRef()
	{
		List result;
		
		try {
		
		Session session = this.getDAOSession();
		
		result = session.createCriteria(CwnsInfoRef.class).list();
		
		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
		
		return (CwnsInfoRef)result.iterator().next();		
	}
	public Collection getDocumentByQuery(String facilityNumber, String facilityLocation, NeedsHelper nh, int first, int max){
		
		List result = new ArrayList();
		
		/*list of search rules:
		 * do not return docs already in facility
		 */
		
		try {
			
			Session session = this.getDAOSession();
			
			Criteria crit1 = session.createCriteria(FacilityDocument.class)
			.createAlias("facility", "f", Criteria.INNER_JOIN)
			.createAlias("document", "d", Criteria.INNER_JOIN);
	
			crit1 = crit1.setProjection(
					Projections.projectionList()
			            .add(Projections.property("d.documentId").as("d_documentId")));
	
	
			crit1.add(Expression.eq("f.facilityId", new Long(facilityNumber)));
	
			Collection docids = crit1.list();
			
			
			Criteria crit = session.createCriteria(Document.class)					
					.createAlias("documentTypeRef", "dt", Criteria.INNER_JOIN);			
			
			
			//DetachedCriteria facilityList = DetachedCriteria.forClass(Facility.class))
		    //.SetProjection( Projections.projectionList().add(arg0) );
			
			crit = crit.setProjection(
					Projections.projectionList()
					            .add(Projections.property("documentId").as("d_documentId"))
							    .add(Projections.property("titleName").as("d_titleName"))
							    .add(Projections.property("authorName").as("d_authorName"))
							    .add(Projections.property("publishedDate").as("d_publishedDate"))
							    .add(Projections.property("outdatedDocCertificatnFlag").as("d_outdatedDocCertificatnFlag"))
							    .add(Projections.property("repositoryId").as("d_repositoryId"))
							    .add(Projections.property("dt.documentTypeId").as("dt_documentTypeRefId"))
							    .add(Projections.property("dt.name").as("dt_documentTypeRefName"))
							    .add(Projections.property("dt.allowFootnoteFlag").as("dt_allowFootnoteFlag"))
							    )							
			                .addOrder(Order.asc("titleName"));	
			
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				sdf.setLenient(true);			
				Date dtfrom = sdf.parse(nh.getPublishDateFrom());
				Date dtto = sdf.parse(nh.getPublishDateTo());
				
				crit.add(Expression.between("publishedDate", dtfrom, dtto ));
				crit.add(Expression.eq("locationId", facilityLocation));
				
				
			} catch (ParseException e) {			
				//if dates are invalid just continue with the search.
			} catch (java.lang.NullPointerException e){				
				//if dates are invalid just continue with the search.
			}			
			
			if(nh.getKeywords() != null && nh.getKeywords().length()> 0){
				
				StringTokenizer tokenizer = new StringTokenizer(nh.getKeywords(), " ");			
				
				for(int i =0; i <= tokenizer.countTokens(); ++i){
					
					String str = tokenizer.nextToken();
					
					crit.add(Expression.or(Expression.like("authorName", "%" + str + "%" ).ignoreCase(), Expression.like("titleName", "%" + str + "%" ).ignoreCase()));										
					
				}
				
			}
			
			
			if(docids != null && docids.size() > 0)				
				crit.add(Expression.not(Expression.in("documentId", docids )));
			
			if(nh.getDocumentTypeId() != null && nh.getDocumentTypeId().length() > 0)
				crit.add(Expression.eq("documentTypeRef.documentTypeId", nh.getDocumentTypeId() ));
			
			crit.setMaxResults(max);
			crit.setFirstResult(first);

	  	    result = crit.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			
			log.debug("test1 - " + result.size());
			
			} catch (HibernateException he) {
				throw getHibernateTemplate().convertHibernateAccessException(he);
			}
		
		
		return result;
	}
	public int countDocumentByQuery(String facilityNumber, String facilityLocation, NeedsHelper nh){
		
		int result = 0;
		
		try {
			
			Session session = this.getDAOSession();
			
			Criteria crit1 = session.createCriteria(FacilityDocument.class)
					.createAlias("facility", "f", Criteria.INNER_JOIN)
					.createAlias("document", "d", Criteria.INNER_JOIN);
			
			crit1 = crit1.setProjection(
					Projections.projectionList()
					            .add(Projections.property("d.documentId").as("d_documentId")));			
			
			crit1.add(Expression.eq("f.facilityId", new Long(facilityNumber)));
			
			Collection docids = crit1.list();
			
			
			Criteria crit = session.createCriteria(Document.class);
			
			crit = crit.setProjection(
					Projections.projectionList()
					            .add(Projections.rowCount(), "s_count")
							    );
			
			
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				sdf.setLenient(true);			
				Date dtfrom = sdf.parse(nh.getPublishDateFrom());
				Date dtto = sdf.parse(nh.getPublishDateTo());
				
				crit.add(Expression.between("publishedDate", dtfrom, dtto ));
				crit.add(Expression.eq("locationId", facilityLocation));
				
				
			} catch (ParseException e) {			
				//if dates are invalid just continue with the search.
			} catch (java.lang.NullPointerException e){				
				//if dates are invalid just continue with the search.
			}
			
			if(docids != null && docids.size() > 0)				
				crit.add(Expression.not(Expression.in("documentId", docids )));
			
			if(nh.getDocumentTypeId() != null && nh.getDocumentTypeId().length() > 0)
				crit.add(Expression.eq("documentTypeRef.documentTypeId", nh.getDocumentTypeId() ));
			
			
			if(nh.getKeywords() != null && nh.getKeywords().length()> 0){
				
				StringTokenizer tokenizer = new StringTokenizer(nh.getKeywords(), " ");			
				
				for(int i =0; i <= tokenizer.countTokens(); ++i){
					
					String str = tokenizer.nextToken();
					
					crit.add(Expression.or(Expression.like("authorName", "%" + str + "%" ).ignoreCase(), Expression.like("titleName", "%" + str + "%" ).ignoreCase()));										
					
				}
				
			}
			
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
			result = ((Integer) crit.uniqueResult()).intValue();
			
			} catch (HibernateException he) {
				throw getHibernateTemplate().convertHibernateAccessException(he);
			}		
		
		return result;
	}
	
	
}
