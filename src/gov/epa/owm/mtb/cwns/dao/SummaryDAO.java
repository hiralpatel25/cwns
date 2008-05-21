package gov.epa.owm.mtb.cwns.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface SummaryDAO extends DAO {

	public List findSummaryByFacilityId(String facilityNumber);
	
	public String findCorrectionsByFacilityId(String facilityNumber, String string);
		
	//public long findFacilityIdByCWNSNumber(String cwnsNbr);
	
	public List findCommentsByFacilityId(String facilityId);

	public void save(String facId, String[] dataAreaTypes);	
	
	public List findFacilityCostcurveByFacilityId(String facilityId);

	public String findReviewStatusRefByFacilityIdAndVersionCode(String facNum, String facilityVersionCode);

	public Collection findCostCurveErrorsByFacilityId(String facNum);

	public Date findLatestFacilityReviewStatus(String facNum, String reviewstatustype);

	public Date findFacilityEntryStatusLastupdatedts(String facNum, String string);
	
	public Collection findLastUpdatedUserIdAndDate(String facilityId);

	public Object[] findLastReviewedUserIdAndDate(String facilityId);
}
