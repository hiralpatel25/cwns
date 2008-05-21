package gov.epa.owm.mtb.cwns.dao;

import gov.epa.owm.mtb.cwns.funding.CbrSearchHelper;
import gov.epa.owm.mtb.cwns.model.CbrLoanInformation;

import java.util.Collection;


public interface FundingDAO extends DAO {
	Collection getFundingInfoAndType(String facNum );
	Collection listLoanTypes();
	Collection listFundingSourceTypes();
	Collection listFundingAgencyTypes();
	CbrLoanInformation getCbrLoanDetails(String id);
	CbrLoanInformation findCbrLoanByLoanNumber(String loannumber);
	Collection listCbrAmountsByLoanId(String id);
	Collection listCbrSearchResults(CbrSearchHelper helper, String locationId);
	CbrLoanInformation getCbrLoanFromFundingSourceId(long fsid);
	Collection listLoanIdsAssociatedWithNpdesPermit(long facNum);
	Collection listLoanIdsAssociatedWithCwnsNumberFacility(long facNum);
	Collection listLoanIdsToExclude(long facNum, String locationId);
}
