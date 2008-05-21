package gov.epa.owm.mtb.cwns.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface SummaryService {
	
	// Set facility version code
	public final String facVerCode = "S";
	public static final String DATEFORMAT =  "MM/dd/yyyy";
	
	public Collection findSummaryByFacilityId(String facNum, String reviewstatustype, String showChanged);
	public String findReviewStatusRefByFacilityIdAndVersionCode(String facNum, String facilityVersionCode);
	//public long findFacilityIdByCWNSNumber(String cwnsNbr);
	public Collection findCommentsByFacilityId(String facNum);
	public void save(String facilityId, String[] dataAreaTypes);
	public Collection findFacilityCostcurveByFacilityId(String facNum);
	public Collection findCostCurveErrorsByFacilityId(String facNum, String reviewstatustype); 
	public HashMap findLastUpdatedUserIdAndDate(String facilityId);
	public HashMap findLastReviewedUserIdAndDate(String facilityId);
}
