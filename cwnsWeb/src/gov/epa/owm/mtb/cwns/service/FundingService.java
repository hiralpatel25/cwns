package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.funding.CbrSearchHelper;
import gov.epa.owm.mtb.cwns.model.CbrLoanInformation;
import gov.epa.owm.mtb.cwns.model.FundingSource;

import java.util.Collection;

public interface FundingService {
	
	Collection getFundingByFacilityId(String facNum);
	FundingSource getFundingSourceById(long id);
	Collection listLoanTypes();
	Collection listFundingSourceTypes();
	Collection listFundingAgencyTypes();
	boolean saveOrUpdateFundingSource(FundingSource fs, CurrentUser user);
	boolean deleteFundingSource(long id, CurrentUser user);
	CbrLoanInformation getCbrLoanDetail(String id);
	Collection getCbrAmountInfoByLoanId(String id);
	Collection getCbrSearchResults(CbrSearchHelper helper, String locationId);
	boolean associateLoansWithFacility(String facilityId, String[] loanids, CurrentUser user);
	boolean associateCategoriesWithFacility(String facilityId, String[] categoryIds, CurrentUser user);
	boolean refreshFundingSourceFromCbr(String facilityId, long id, CurrentUser user);
	Collection listLoanIdsAssociatedWithNpdesPermit(long facNum);
	Collection listLoanIdsAssociatedWithCwnsNumberFacility(long facNum);
	CbrLoanInformation getCbrLoanFromFundingSourceId(long fsid);
}
