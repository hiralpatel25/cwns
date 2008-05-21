package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.FundingDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.funding.CbrSearchHelper;
import gov.epa.owm.mtb.cwns.funding.CbrSearchResult;
import gov.epa.owm.mtb.cwns.model.CbrAmountInformation;
import gov.epa.owm.mtb.cwns.model.CbrAmountInformationId;
import gov.epa.owm.mtb.cwns.model.CbrLoanInformation;
import gov.epa.owm.mtb.cwns.model.CbrProjectInformation;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FundingAgencyRef;
import gov.epa.owm.mtb.cwns.model.FundingSource;
import gov.epa.owm.mtb.cwns.model.FundingSourceTypeRef;
import gov.epa.owm.mtb.cwns.service.FundingService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

public class FundingServiceImpl extends CWNSService implements FundingService{
	
	private FundingDAO fundingDAO;
	private SearchDAO searchDAO;		
	
	public FundingDAO getFundingDAO() {
		return fundingDAO;
	}

	public void setFundingDAO(FundingDAO fundingDAO) {
		this.fundingDAO = fundingDAO;
	}

	public SearchDAO getSearchDAO() {
		return searchDAO;
	}
	
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}
	
	public Collection getFundingByFacilityId(String facNum){
		
		Collection list = fundingDAO.getFundingInfoAndType(facNum);	
		
		log.debug("DEBUG");		
		
		return list;
	}	
	public Collection listLoanTypes(){
		return fundingDAO.listLoanTypes();
	}
	public Collection listFundingSourceTypes(){
		return fundingDAO.listFundingSourceTypes();
	}
	public Collection listFundingAgencyTypes(){
		return fundingDAO.listFundingAgencyTypes();
	}
	public FundingSource getFundingSourceById(long id){
		
		SearchConditions scs1 = new SearchConditions(new SearchCondition("fundingSourceId", SearchCondition.OPERATOR_EQ, new Long(id)));
		FundingSource fs = (FundingSource) searchDAO.getSearchObject(FundingSource.class, scs1);
		
		return fs;
	}
	
	public boolean saveOrUpdateFundingSource(FundingSource fs, CurrentUser user){		
		
		log.debug("FundingServiceImpl funding ID fs.getFacility().getFacilityId(): " + new Long(fs.getFacility().getFacilityId()).longValue() );
				
		Facility f;
		
		try {
			
			SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(fs.getFacility().getFacilityId())));
			f = (Facility) searchDAO.getSearchObject(Facility.class, scs1);		
			fs.setLocationId(f.getLocationId());			
			searchDAO.saveObject(fs);
			
			//update ts in facility_entry_status
			updateFacilityEntryStatusTS(f.getFacilityId(), 10, user.getUserId());			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			log.error("Threw a RuntimeException");
			e.printStackTrace();
		}catch (Exception e) {
			log.error("Threw a Exception");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	public boolean deleteFundingSource(long id, CurrentUser user){
		
		SearchConditions scs1 = new SearchConditions(new SearchCondition("fundingSourceId", SearchCondition.OPERATOR_EQ, new Long(id)));
		FundingSource fs = (FundingSource) searchDAO.getSearchObject(FundingSource.class, scs1);		
		searchDAO.removeObject(fs);		
	
		//update ts in facility_entry_status
		updateFacilityEntryStatusTS(fs.getFacility().getFacilityId(), 10, user.getUserId());
		
		return true;
	}
	public CbrLoanInformation getCbrLoanDetail(String id){
		
		return fundingDAO.getCbrLoanDetails(id);
	}
	public Collection getCbrAmountInfoByLoanId(String id){
						
		return fundingDAO.listCbrAmountsByLoanId(id);
	}
	public Collection getCbrSearchResults(CbrSearchHelper helper, String locationId){
		Collection lstCbrSearchResult = null;
		Collection lstLoanInfo = fundingDAO.listCbrSearchResults(helper, locationId);
		
		if (helper.getResult()==CbrSearchHelper.LIST){
			lstCbrSearchResult = new ArrayList();
			if (lstLoanInfo!=null && lstLoanInfo.size()>0){
				Iterator lstLoanInfoIter = lstLoanInfo.iterator();
				while(lstLoanInfoIter.hasNext()){
					CbrLoanInformation cbrLoanInformation = (CbrLoanInformation)lstLoanInfoIter.next();
					CbrSearchResult cbrSearchResult = new CbrSearchResult();				
					cbrSearchResult.setCbrLoanInformation(cbrLoanInformation);				
					if (isInconsistentCbrFundingSource(cbrLoanInformation.getLoanNumber())){
						cbrSearchResult.setDisplayLink("N");
					}else{
						cbrSearchResult.setDisplayLink("Y");
					}
					lstCbrSearchResult.add(cbrSearchResult);
				}
			}
		}else{
			lstCbrSearchResult = lstLoanInfo;
		}
		return lstCbrSearchResult;
	}
	
	private boolean isInconsistentCbrFundingSource(String loanNumber){
		boolean isCbrFundingSource = false;
		SearchConditions scs = new SearchConditions(new SearchCondition("loanNumber", SearchCondition.OPERATOR_EQ, loanNumber));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("sourceCd", SearchCondition.OPERATOR_EQ, new Character('C')));
		scs.setCondition(SearchCondition.OPERATOR_AND,
				new SearchCondition("consistentWithCbrCode", SearchCondition.OPERATOR_EQ, new Character('D')));
		Collection fsList =  searchDAO.getSearchList(FundingSource.class, scs);
		if (fsList!=null && fsList.size()>0) isCbrFundingSource=true;			
		return isCbrFundingSource;
	}
	
	public boolean associateLoansWithFacility(String facilityId, String[] loanids, CurrentUser user){
		Facility f;		
		try {			
			SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
			f = (Facility) searchDAO.getSearchObject(Facility.class, scs1);			
			
			for(int i = 0; i < loanids.length; ++i){				
				Collection list = fundingDAO.listCbrAmountsByLoanId(loanids[i]);				
				Iterator iter = list.iterator();				
				CbrLoanInformation cbr = (CbrLoanInformation) iter.next();
				
				FundingSource fs = popfundingSourceFromCbrLoan(new FundingSource(), cbr);			
				fs.setFacility(f);
				fs.setLocationId(f.getLocationId());
				
				//fs.setLastUpdateUserid(userid);
				fs.setLastUpdateUserid(user.getUserId());
				fs.setLastUpdateTs(new Date());
				fs.setConsistentWithCbrCode(new Character('C'));
				fs.setSourceCd('C');				
				fs.setFeedbackDeleteFlag('N');
				saveOrUpdateFundingSource(fs, user);				
			}			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			log.error("Threw a RuntimeException");
			e.printStackTrace();
		}catch (Exception e) {
			log.error("Threw a Exception");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;		
	}
	
	
	public boolean associateCategoriesWithFacility(String facilityId, String[] categoryIds, CurrentUser user){
		
				
		Facility f;
		
		try {
			
			SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
			f = (Facility) searchDAO.getSearchObject(Facility.class, scs1);			
			
			for(int i = 0; i < categoryIds.length; ++i){
				
				
				CbrAmountInformationId catid = popCbrAmountInformationId(categoryIds[i]);				
				
				Collection list = fundingDAO.listCbrAmountsByLoanId(catid.getCbrLoanInformationId());
				
				Iterator iter = list.iterator();
				
				CbrLoanInformation cbr = (CbrLoanInformation) iter.next();								
				
				FundingSource fs = popfundingSourceFromCbrCategory(new FundingSource(), cbr, catid);
				
				fs.setFacility(f);
				fs.setLocationId(f.getLocationId());
				
				//fs.setLastUpdateUserid(userid);
				fs.setLastUpdateUserid(user.getUserId());
				fs.setLastUpdateTs(new Date());
				fs.setConsistentWithCbrCode(new Character('C'));
				fs.setSourceCd('C');
				fs.setFeedbackDeleteFlag('N');
				saveOrUpdateFundingSource(fs, user);
				
			}
			
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			log.debug("Threw a RuntimeException");
			e.printStackTrace();
		}catch (Exception e) {
			log.debug("Threw a Exception");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	public boolean refreshFundingSourceFromCbr(String facilityId, long id, CurrentUser user){		
		
		FundingSource fs = getFundingSourceById(id);
		
		long fid = fs.getFundingSourceId();
		
		Facility f;
		
		try {
		
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		f = (Facility) searchDAO.getSearchObject(Facility.class, scs1);			
		
		
		if(fs.getCbrAmountInformation() != null && fs.getCbrAmountInformation().getId() != null){
			
			
			if(fs.getCbrAmountInformation().getId().getCbrCategoryId() != null && fs.getCbrAmountInformation().getId().getCbrProjectInformationId() != null){
				
				CbrLoanInformation cbr = fundingDAO.getCbrLoanDetails(fs.getCbrAmountInformation().getId().getCbrLoanInformationId());
				fs = this.popfundingSourceFromCbrCategory(fs, cbr, fs.getCbrAmountInformation().getId());
			}
		}
		else{				
			CbrLoanInformation cbr = fundingDAO.getCbrLoanFromFundingSourceId(fs.getFundingSourceId());
			fs = this.popfundingSourceFromCbrLoan(fs, cbr);
		}
			
			fs.setFacility(f);
			fs.setLocationId(f.getLocationId());
			
			fs.setLastUpdateUserid(user.getUserId());
			fs.setLastUpdateTs(new Date());
			fs.setConsistentWithCbrCode(new Character('C'));
			fs.setSourceCd('C');			
			saveOrUpdateFundingSource(fs, user);		
			
		
		
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			log.debug("Threw a RuntimeException");
			e.printStackTrace();
		}catch (Exception e) {
			log.debug("Threw a Exception");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	public Collection listLoanIdsAssociatedWithNpdesPermit(long facNum){
		return fundingDAO.listLoanIdsAssociatedWithNpdesPermit(facNum);				
	}
	public Collection listLoanIdsAssociatedWithCwnsNumberFacility(long facNum){
		return fundingDAO.listLoanIdsAssociatedWithCwnsNumberFacility(facNum);				
	}
	public CbrLoanInformation getCbrLoanFromFundingSourceId(long fsid){		
		return fundingDAO.getCbrLoanFromFundingSourceId(fsid);	
	}	
	private FundingSource popfundingSourceFromCbrLoan(FundingSource fs, CbrLoanInformation cbr){
		
		//FundingSource fs = new FundingSource();			
		
		fs.setAwardDate(cbr.getLoanDate());			
		
		fs.setLoanNumber(cbr.getLoanNumber());
			
		fs.setAwardedAmount(new Long(cbr.getLoanAmount().longValue()));
			
		fs.setPercentageFundedBySrf(new Short(cbr.getPercentFundedByCwsrf().shortValue()));
		
		fs.setLoanTypeRef(cbr.getLoanTypeRef());
		
		FundingAgencyRef far = new FundingAgencyRef();
		far.setFundingAgencyId("S");
		fs.setFundingAgencyRef(far);		

		FundingSourceTypeRef fstr = new FundingSourceTypeRef();
		fstr.setFundingSourceTypeId("B");
		fs.setFundingSourceTypeRef(fstr);
		
		
		CbrAmountInformationId id = new CbrAmountInformationId();
		
		id.setCbrLoanInformationId(cbr.getCbrLoanInformationId());
		
		CbrAmountInformation amount = new CbrAmountInformation();
		
		amount.setId(id);
		
		fs.setCbrAmountInformation(amount);
		
		
		return fs;	
	}	
	private FundingSource popfundingSourceFromCbrCategory(FundingSource fs, CbrLoanInformation cbr, CbrAmountInformationId catid){
		
		//FundingSource fs = new FundingSource();			
		
		fs.setAwardDate(cbr.getLoanDate());			
		
		fs.setLoanNumber(cbr.getLoanNumber());
			
		fs.setAwardedAmount(new Long(cbr.getLoanAmount().longValue()));
		
		//this is not included when assoc'ing with cat
		//fs.setPercentageFundedBySrf(new Short(cbr.getPercentFundedByCwsrf().shortValue()));
		
		fs.setLoanTypeRef(cbr.getLoanTypeRef());
		
		FundingAgencyRef far = new FundingAgencyRef();
		far.setFundingAgencyId("S");
		fs.setFundingAgencyRef(far);		

		FundingSourceTypeRef fstr = new FundingSourceTypeRef();
		fstr.setFundingSourceTypeId("B");
		fs.setFundingSourceTypeRef(fstr);
		
		Iterator iter = cbr.getCbrProjectInformations().iterator();
		while(iter.hasNext()){
			
			CbrProjectInformation project = (CbrProjectInformation) iter.next();
			
			if(project.getId().getCbrProjectInformationId().equals(catid.getCbrProjectInformationId())){								
				
				fs.setCbrProjectNumber(new Integer(project.getProjectNumber()));
				
				Iterator iterator = project.getCbrAmountInformations().iterator();
				while(iterator.hasNext()){
					
					CbrAmountInformation amount = (CbrAmountInformation) iterator.next();
					
					if(amount.getId().equals(catid)){
						
						fs.setCbrAmountInformation(amount);						
						fs.setAwardedAmount(new Long(amount.getAmount().longValue()));						
					}					
				}				
			}			
		}		
		
		return fs;	
	}
	private CbrAmountInformationId popCbrAmountInformationId(String id) {
		
		CbrAmountInformationId catid = new CbrAmountInformationId();

		if (id.length() > 0) {
			StringTokenizer st = new StringTokenizer(id, ";");

			if (st.countTokens() == 3) {			
					
					catid.setCbrLoanInformationId((String) st.nextElement());
					catid.setCbrProjectInformationId((String) st.nextElement());
					catid.setCbrCategoryId((String) st.nextElement());					
			} 
		}
		return catid;
	}
	
	private void updateFacilityEntryStatusTS(long facilityId, long dataAreaId, String userId){
		SearchConditions fes_scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));			
		fes_scs.setCondition(new SearchCondition("id.dataAreaId", SearchCondition.OPERATOR_EQ, new Long(dataAreaId)));
		Object objFes = searchDAO.getSearchObject(FacilityEntryStatus.class, fes_scs);
		if (objFes!=null){
			FacilityEntryStatus fes = (FacilityEntryStatus)objFes;
			fes.setDataAreaLastUpdateTs(new Date());
			fes.setLastUpdateUserid(userId);
			searchDAO.saveObject(fes);
		}		
	}
}
