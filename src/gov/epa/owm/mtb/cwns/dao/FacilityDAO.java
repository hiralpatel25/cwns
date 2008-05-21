package gov.epa.owm.mtb.cwns.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.facility.FacilityListForm;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.ReviewComment;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.common.search.Search;

public interface FacilityDAO extends DAO{
	
	public final int	MAX_RESULTS = 20;
	
	
	public static final String FEDERAL_REVIEW_REQUESTED 		= "FRR";
	public static final String FEDERAL_REVIEW_CORRECTION 		= "FRC";
	public static final String FEDERAL_ACCEPTED          		= "FA";
	public static final String STATE_CORRECTION_REQUESTED 		= "SCR";
	public static final String STATE_IN_PROGRESS				= "SIP";
	public static final String STATE_ASSIGNED 					= "SAS";
	public static final String LOCAL_ASSIGNED           		= "LAS";
	

	public static final String REVIEW_STATUS			= "reviewStatus";
    public static final String FACILITY_NAME			= "facName";
    public static final String FACILITY_DESCRIPTION 	= "facDesc";
    public static final String OVERALL_NATURE_TYPE      = "overAllType"; 
    public static final String AUTHORITY				= "authority";
    public static final String FACILITY_ID				= "facilityId";
    public static final String SYSTEM_NAME				= "sysname"; 	
    public static final String COUNTY					= "county";
    public static final String WATERSHED				= "watershed"; 	
    public static final String NEEDS					= "needTotal";	
    
    public static final String NEEDS_OPER				= "needOperator";
    public static final String FACILTITY_NOT_CHANGED	= "notChanged"; 	
    public static final String PERMIT_NUMBER			= "permitNumber"; 	
    public static final String LOCATION_ID				= "state";	
    public static final String ERROR_STATUS				= "errorStatus";
    public static final String SORT_COLUMN				= "sortColumn";
    public static final String DOCUMENT_DATE			= "docDate";	
    public static final String DOCUMENT_OPER			= "docOperator";
    public static final String PRESENT_POPULATION_OPER	= "presPopulationOperator";
    public static final String PRESENT_POPULATION_COUNT	= "presPopulationCount";
    public static final String PRESENT_POPULATION_TYPE	= "presPopulationType";
    public static final String PRESENT_FLOW_OPER		= "presFlowOperator";
    public static final String PRESENT_FLOW_COUNT		= "presFlowCount";
    public static final String PRESENT_FLOW_TYPE		= "presFlowType";
    public static final String FACILITY_PROJECT_OPER	= "facProjectOperator";
    public static final String DOCUMENT_TYPE 			= "docType";
    public static final String CWNS_USER_ID				= "cwnsUserId";
    public static final String CWNS_NUMBER				= "cwnsNumber";
    
    public static final String SORT_ADVANCE_SEARCH_CWNS_NUMBER 			= "CWNS_NBR";
	public static final String SORT_ADVANCE_SEARCH_FACILITY_NAME		= "FACILITY_NAME";
	public static final String SORT_ADVANCE_SEARCH_REVIEW_STATUS_NAME 	= "REVIEW_STATUS_NAME";
	public static final String SORT_ADVANCE_SEARCH_COUNTY 				= "COUNTY";
	public static final String SORT_ADVANCE_SEARCH_FEEDBACK				= "FEEDBACK_REVIEW_STATUS_NAME";
	public static final String SORT_ADVANCE_SEARCH_AUTHORITY 			= "AUTHORITY";
	public static final String SORT_ORDER_ADVANCE_SEARCH_ASCENDING		= "ASC";
	public static final String SORT_ORDER_ADVANCE_SEARCH_DESCENDING 	= "DESC";
	
	public List findFacility(Facility searchFacility, int firstResult, int maxResults);

	public Facility findByFacilityNumber(String nbr);
	
	public Facility findByFacilityId(String facilityId);
	
	public int countFacilities(Facility facility);
	
	public Collection getFacilitiesByList(Collection facilityList, Collection sortCriteria, int startIndex, int maxResults);
	
	public Collection getFacilityIdByNeeds(String locationId, String operator, int amount);
	
	public Collection getFacilityIdWithNoErrors(String locationId);
	
	public Collection getFacilityComments(String facNum);

	public Collection getFeedbackVersionOfFacility(String facNum);
	
	public Collection getFacilitiesByFunction();
	
	public Collection executeKeywordSearch(Search search);
	
	public Collection executeAdvancedSearch(Search search);
	
	public StateRef getStateByLocationId(String locationId);
	
	public Facility getFacilityByCwnsNbr(String cwnsNumber);
	
	public Collection getCounties(Collection counties, String locationId);

	public String getCWNSNbrByFacilityId(String facilityId);
	
	public List getFederalReviewStatusesByFacilityId(String facilityId);
	
	public List getFaciltiyEntryStatusesByFacilityId(String facilityId);
	
	public boolean isAllFacilityEntryStatusErrorFlagEqualNo(String facilityId);
	
	public boolean isAllFederalReviewStatusErrorFlagEqualNo(String facilityId);
	
	public FacilityReviewStatus getLatestFRRorFRCFacilityReviewStatus(String facilityId);
	
	public ReviewComment getLatestReviewComment(String facilityId);
	
	public boolean isLastUpdateForReviewCommentLaterThanFacilityReviewStatus(String facilityId);
		
}
