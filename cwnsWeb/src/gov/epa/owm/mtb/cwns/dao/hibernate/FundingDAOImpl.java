package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.dao.FundingDAO;
import gov.epa.owm.mtb.cwns.funding.CbrSearchHelper;
import gov.epa.owm.mtb.cwns.model.CbrLoanInformation;
import gov.epa.owm.mtb.cwns.model.FundingAgencyRef;
import gov.epa.owm.mtb.cwns.model.FundingSource;
import gov.epa.owm.mtb.cwns.model.FundingSourceTypeRef;
import gov.epa.owm.mtb.cwns.model.LoanTypeRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class FundingDAOImpl extends BaseDAOHibernate implements FundingDAO {	
	
	
	public Collection getFundingInfoAndType(String facNum){
				
		String query = "select distinct fs from FundingSource as fs"
			+ "	inner join fetch fs.fundingSourceTypeRef" 
			+ "	inner join fetch fs.loanTypeRef"
			+ "	inner join fetch fs.fundingAgencyRef"
		    + " where fs.facility.facilityId=:facNum"
		    + " order by fs.loanNumber";		 	
		
		Session session = this.getDAOSession();		
				
		Query funQuery = session.createQuery(query);		
				
		
		funQuery.setLong("facNum", new Long(facNum).longValue());
				
		//funQuery.setCacheMode(arg0);
		//funQuery.setCacheable(arg0)		
		
		List funList = funQuery.list();		
		
		
		
		Iterator iter = funList.iterator();
		while(iter.hasNext()){
			
			FundingSource fs = (FundingSource) iter.next();
			
			//session.load(FundingSource.class, fs);
			
			if(fs.getFundingSourceTypeRef().getName() == null) {				
			
				FundingSourceTypeRef fstr = (FundingSourceTypeRef) session.load(FundingSourceTypeRef.class, fs.getFundingSourceTypeRef().getFundingSourceTypeId());
				fs.getFundingSourceTypeRef().setName(fstr.getName());
			
				FundingAgencyRef far = (FundingAgencyRef) session.load(FundingAgencyRef.class, fs.getFundingAgencyRef().getFundingAgencyId());
				fs.getFundingAgencyRef().setName(far.getName());
			
				LoanTypeRef ltr = (LoanTypeRef) session.load(LoanTypeRef.class, fs.getLoanTypeRef().getLoanTypeId());
				fs.getLoanTypeRef().setName(ltr.getName());
			
			}
			
			log.debug("ffgf " + fs.getFundingAgencyRef().getFundingAgencyId());
			log.debug(fs.getFundingSourceTypeRef().getFundingSourceTypeId());
			log.debug(fs.getLoanTypeRef().getLoanTypeId());
			
			log.debug("ffgf " + fs.getFundingAgencyRef().getName());			
			log.debug(fs.getFundingSourceTypeRef().getName());
			log.debug(fs.getLoanTypeRef().getName()); 
		}
		
		
		return funList;
	}
	public Collection listLoanTypes(){		
		String query = "from LoanTypeRef";			
		Session session = this.getDAOSession();			
		return session.createQuery(query).list();		
	}
	public Collection listFundingSourceTypes(){
		String query = "from FundingSourceTypeRef";			
		Session session = this.getDAOSession();			
		return session.createQuery(query).list();		
	}
	public Collection listFundingAgencyTypes(){
		String query = "from FundingAgencyRef";			
		Session session = this.getDAOSession();			
		return session.createQuery(query).list();
	}
	public CbrLoanInformation getCbrLoanDetails(String id){		
		
		String query = "from CbrLoanInformation as cbr"
			+ "	left join fetch cbr.cbrBorrower" 
			+ "	left join fetch cbr.loanTypeRef"
			+ "	left join fetch cbr.cbrProjectInformations as proj"
			+ "	left join fetch proj.cbrAmountInformations"
		    + " where cbr.cbrLoanInformationId=:id";
		
		Session session = this.getDAOSession();		
		
		Query cbrQuery = session.createQuery(query);				
		
		cbrQuery.setString("id", id);				
		
		List cbrList = cbrQuery.list();
		
		if(cbrList.size() > 0)
			return (CbrLoanInformation) cbrList.get(0);				
		
		return null;
	}
	public Collection listCbrAmountsByLoanId(String id){
		
		
		String query = "from CbrLoanInformation as cbr"
			+ "	left join fetch cbr.cbrBorrower" 
			+ "	left join fetch cbr.loanTypeRef"
			+ "	left join fetch cbr.cbrProjectInformations as proj"
			+ "	left join fetch proj.cbrAmountInformations"
		    + " where cbr.cbrLoanInformationId=:id";
		
				
		/*String query = "from CbrAmountInformation as cai"			
		    + " where cai.id=:loanid";
		
		CbrAmountInformationId cbrid = new CbrAmountInformationId();
		
		cbrid.setCbrLoanInformationId(id);
		*/		
		
		Session session = this.getDAOSession();
		
		Query cbrQuery = session.createQuery(query);
		
		cbrQuery.setString("id", id);
		
		List cbrList = cbrQuery.list();
		
		return cbrList;
	}
	public Collection listCbrSearchResults(CbrSearchHelper helper, String locationId){
		
		
		Session session = this.getDAOSession();
		
		Criteria crit1 = session.createCriteria(CbrLoanInformation.class)
		.createAlias("cbrBorrower", "bwr", Criteria.INNER_JOIN);
		
		crit1 = crit1.setProjection(
				Projections.projectionList()
		            .add(Projections.property("cbrLoanInformationId").as("cbr_id")));
		
		crit1.add(Expression.ilike("locationId", locationId));
		
		
		if(helper.getBorrower() != null && !helper.getBorrower().equals(""))
			crit1.add(Expression.like("bwr.name", "%" + helper.getBorrower().trim() + "%" ).ignoreCase());
		
		if(helper.getTrackingNumber() != null && !helper.getTrackingNumber().equals(""))
			crit1.add(Expression.like("loanNumber", "%" + helper.getTrackingNumber().trim() + "%" ).ignoreCase());
		
		if(helper.getStartDate() != null && helper.getEndDate() != null)
			crit1.add(Expression.between("loanDate", helper.getStartDate(), helper.getEndDate() ));
		
		if(helper.getAssocWithPermit() != null && helper.getAssocWithPermit().equals("Y")){
					
			Collection plist = listLoanIdsAssociatedWithNpdesPermit(helper.getFacilityId());
				
			if(plist != null && plist.size() > 0)				
				crit1.add(Expression.in("cbrLoanInformationId", plist ));
			else
				return new ArrayList();			
		}		
		
		Collection exclude = listLoanIdsToExclude(helper.getFacilityId(), locationId);
		
		if(exclude != null && exclude.size() > 0)				
			crit1.add(Expression.not(Expression.in("cbrLoanInformationId", exclude )));		
		
		
		if(helper.getAssocWithCwnsNumber() != null && helper.getAssocWithCwnsNumber().equals("Y")){
			
			Collection plist = listLoanIdsAssociatedWithCwnsNumberFacility(helper.getFacilityId());
				
			if(plist != null && plist.size() > 0)				
				crit1.add(Expression.in("cbrLoanInformationId", plist ));
			else
				return new ArrayList();			
		}
		
		
		if(helper.getResult() == CbrSearchHelper.LIST){
			crit1.setFirstResult(helper.getStartCount() - 1);
			crit1.setMaxResults(helper.getMaxCount());			
		}		
		
		crit1.addOrder(Order.asc("loanNumber"));
		
		crit1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);        
		
		Collection loanids = crit1.list();
		
		if(loanids == null || loanids.size() == 0 ) return new ArrayList();
		
		if(helper.getResult() == CbrSearchHelper.COUNT)
			return loanids;
		
		Criteria crit = session.createCriteria(CbrLoanInformation.class)
			.createAlias("cbrBorrower", "bwr", Criteria.INNER_JOIN);		
		
		if(loanids != null && loanids.size() > 0)				
			crit.add(Expression.in("cbrLoanInformationId", loanids ));		
		
		
		//crit.setFetchMode("cbrBorrower", FetchMode.JOIN);
		crit.setFetchMode("loanTypeRef", FetchMode.JOIN);
		crit.setFetchMode("cbrProjectInformations", FetchMode.JOIN);
		crit.setFetchMode("cbrProjectInformations.cbrAmountInformations", FetchMode.JOIN);
		
		
		crit.addOrder(Order.asc("bwr.name"));
		
		
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);		
		
		List cbrList = crit.list();		
		
		
		return cbrList;
	}
	public CbrLoanInformation getCbrLoanFromFundingSourceId(long fsid){
		
		
		//have to use sql query because of composite key
		String query = "select cbr_loan_information_id_fk from funding_source"
			+ "	 where funding_source_id = :id";
			
		
		Session session = this.getDAOSession();		
		
		Query cbrQuery = session.createSQLQuery(query);				
		
		cbrQuery.setLong("id", fsid);				
		
		List cbrList = cbrQuery.list();
		
		if(cbrList.size() > 0){
			String id =  (String) cbrList.get(0);
			return getCbrLoanDetails(id);
		}
		
		return null;
	}
	public CbrLoanInformation findCbrLoanByLoanNumber(String loannumber){		
		
		String query = "from CbrLoanInformation as cbr"
			+ "	inner join fetch cbr.cbrBorrower" 
			+ "	inner join fetch cbr.loanTypeRef"
			+ "	inner join fetch cbr.cbrProjectInformations as proj"
			+ "	inner join fetch proj.cbrAmountInformations"
		    + " where cbr.loanNumber=:numb";
		
		Session session = this.getDAOSession();		
		
		Query cbrQuery = session.createQuery(query);				
		
		cbrQuery.setString("numb", loannumber);				
		
		List cbrList = cbrQuery.list();
		
		if(cbrList.size() > 0)
			return (CbrLoanInformation) cbrList.get(0);				
		
		return null;
	}
	public Collection listLoanIdsAssociatedWithCwnsNumberFacility(long facNum){
		
		String query = "select distinct cbr_loan_information_id_fk" 
			+ " from cbr_project_information pr, facility f, cbr_loan_information loan"			
			+ " where pr.cwns_number like '%' || LTRIM(TO_CHAR(f.cwns_nbr),'0')  ||  '%'"
			+ " and pr.cbr_loan_information_id_fk = loan.cbr_loan_information_id "
			+ " and f.facility_id = :id" 
			+ " and loan.location_id = f.location_id ";
		
		Session session = this.getDAOSession();		
		
		Query cbrQuery = session.createSQLQuery(query);
		
		cbrQuery.setLong("id", facNum);				
		
		List cbrList = cbrQuery.list();
		
		if(cbrList.size() > 0){
			return cbrList;
		}
		
		return null;
	}
	public Collection listLoanIdsAssociatedWithNpdesPermit(long facNum){
		
		String query = "select distinct pr.cbr_loan_information_id_fk" 
			+ " from cbr_project_information pr inner join permit pe on pr.npdes_permit_number = pe.permit_number"
			+ " inner join permit_type_ref pt on pe.permit_type_id_fk = pt.permit_type_id inner join facility_permit"
			+ " fp on pe.permit_id=fp.permit_id_fk inner join facility f on fp.facility_id_fk=f.facility_id" 
			+ " inner join cbr_loan_information loan on loan.location_id = f.location_id"
			+ " where pt.npdes_flag='Y' and f.facility_id = :id";			
		
		Session session = this.getDAOSession();		
		
		Query cbrQuery = session.createSQLQuery(query);				
		
		cbrQuery.setLong("id", facNum);				
		
		List cbrList = cbrQuery.list();
		
		if(cbrList.size() > 0){
			return cbrList;
		}
		
		return null;
		
	}
	public Collection listLoanIdsToExclude(long facNum, String locationId){
		
		
		//I don't know if this can be converted to a Criteria because of composite keys
		//this query returns list of all loan ids that have had all categories selected
		String query = "select distinct cbr_loan_information_id_fk"
		 + " from funding_source "
		 + " where facility_id_fk = :id" 
		 + " and (not cbr_loan_information_id_fk in (SELECT cbr_loan_information_id_fk"
		 + " FROM cbr_amount_information cbr"
		 + " WHERE cbr.cbr_loan_information_id_fk in ("
		 + " select cbr_loan_information_id_fk from funding_source where facility_id_fk = :id)"
		 + " AND NOT EXISTS ("
		 + " SELECT fs.*"
		 + " FROM funding_source fs"
		 + " WHERE not fs.cbr_loan_information_id_fk is null"
		 + " AND cbr.cbr_loan_information_id_fk = fs.cbr_loan_information_id_fk"
		 + " AND cbr.CBR_CATEGORY_ID = fs.CBR_CATEGORY_ID_FK"
		 + " AND cbr.cbr_project_information_id = fs.cbr_project_information_id_fk"
		 + " and fs.facility_id_fk = :id)) and not cbr_loan_information_id_fk is null or (" 
		 + " not cbr_loan_information_id_fk is null and CBR_CATEGORY_ID_FK is null))";
		
		Session session = this.getDAOSession();
		
		Query cbrQuery = session.createSQLQuery(query);
		
		cbrQuery.setLong("id", facNum);
		
		List cbrList = cbrQuery.list();
		
		if(cbrList.size() > 0){
			return cbrList;
		}
		
		return null;
	}
}
