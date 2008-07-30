package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.facility.FacilityListHelper;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeRef;
import gov.epa.owm.mtb.cwns.model.StateRef;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FacilityService {	

	/* Maximum number of results to return from a query */ 
	public final int	MAX_RESULTS = 20;
	public final char VERSION_SURVEY = 'S';
	public final char VERSION_FEEDBACK = 'F';
	public static final String FACILITY_LOCATION_HEADQUARTER = "HQ";
	
	public static final Long DATA_AREA_FACILITY = new Long(1);
	public static final Long DATA_AREA_GEOGRAPHIC = new Long(2);
	public static final Long DATA_AREA_POC = new Long(3);
	public static final Long DATA_AREA_DISCHARGE = new Long(4);
	public static final Long DATA_AREA_POPULATION = new Long(5);
	public static final Long DATA_AREA_FLOW = new Long(6);
	public static final Long DATA_AREA_CSO = new Long(8);
	public static final Long DATA_AREA_EFFLUENT = new Long(9);
	public static final Long DATA_AREA_NEEDS = new Long(10);
	public static final Long DATA_AREA_PERMITS= new Long(11);
	public static final Long DATA_AREA_POLLUTION = new Long(12);
	public static final Long DATA_AREA_UNIT_PROCESS = new Long(13);		
	public static final Long DATA_AREA_UTIL_MANAGEMENT = new Long(14);
	
	public static final String FACILITY_OWNER_PUBLIC = "PUB";
	public static final String FACILITY_OWNER_PRIVATE = "PRI";

//	public Collection getFacilityDereferencedSearchResults(Facility searchFacility, int startIndex, int maxResultSize);

	
	public List findFacility(Facility facility, int firstResult, int maxResults);

	public int countFacilities(Facility searchFacility);
	public Set statusChange(Set facilities, String newStatus);

	public Facility findByFacilityNumber(String facNum);
	public Facility findByFacilityId(String facId);
	
//	public Collection getFacilities(String searchKeyword, int startIndex, int maxResultSize, String locationId);
	
	public Collection getFacilityIdsByKeyword(String searchKeyword, String locationId);

	public int countFacilities(String keyword, String locationId);

	public Collection getFacilities(Collection facilityList, Search search);

	public Collection getFacilityByLocation(String locationId);

	public Facility getFacilityByCwnsNbr(String cwnsNbr);

	public Search getDefaultSearch(CurrentUser currentUser, SortCriteria sortCriteria, int startIndex, int maxResults);
	
	public Search getImportSearch(String key, SortCriteria sortCriteria, int startIndex, int maxResults);

	public Collection getFacilitiesIds(Search search);
	
	public Collection getFacilitiesIds(Search search, boolean andResults);
	
	public int getFacilitiesCount(Search search);

	public Search getSimpleSearch(String keyword, String locationId, SortCriteria sc, int startIndex, int max_results);	
	
    public void updateCwnsUserSetting(String cwnsUserSettingId,Set masterSelectedList);
    
    public Collection getCwnsUserSetting(String userAndRole, String listType);

    public Collection getCountyListByLocation(String locationId);
    
    public Collection getSystemNameListByLocation(String locationId);
    
    public Collection getWatershedListByLocation(String locationId);
    
    public Collection getWatershedListByLocationObjects(String locationId);
    
    public Collection getStates();
    
    public Collection getOverAllNatureTypes();
    
    public StateRef getStateByLocationId(String locationId);
    
    public String [] getFacilityOverallTypeIds(String name);
    
    public Collection getReviewStatusRefs();
    
	public Collection getFacilityObjects(Collection facilityIds, Search search);

	public Search getAdvanceSearch(Map queryProperties, String locationId, SortCriteria sc, int startIndex, int max_results2);

	//public FacilityListHelper getFacility(String facNum);
	
	public Collection getFacilityComments(String facNum);

	public Facility getFeedbackVersionOfFacility(String facNum);
	
	
	/**
	 * Determines if a user has previlages to update a facility
	 * @param user  user object
	 * @param facilityId  facility id
	 * @return boolean indicating whether a user can update the facility data
	 */
	public boolean isUpdatable(CurrentUser user, Long facilityId);

	public Facility saveFacilityInfo(String sFacilityId, String facilityName, String description, String sysName, String ownerCode, char militaryFlag, CurrentUser user, String cwnsNbr, String locationId,
			                            String tmdlFlg, String sourceWaterProtectionFlg);
    
	public Collection getFacilityByNameAndState(String facilityName, String locationId, String cwnsNbr);

	public boolean hasFederalNeeds(Long facilityId);
	
	public boolean facilityTypesAllowsFederalNeeds(Long facilityId);
	
	public boolean isFacility(Long facilityId);  
	
	public boolean isDataAreaViewable(CurrentUser user, Long facilityId, Long dataArea);
	
	public boolean warnIfFeedBackOutOfSync(Long facilityId);
	
	public void performPostSaveUpdates(Long facilityId, Long dataAreaId,  CurrentUser user);
	
	public boolean isFacilityLargeCommunity(Long facilityId);

	public Search getFacilitySelectAdvanceSearch(Map queryProperties, String locationId, SortCriteria sortCriteria, int startIndex, int maxResults);
	
	public boolean isNpdesIconVisible(Long facilityId);
	
	public String getNpdesFacilityName(Long facilityId);
	
	public Collection getFacilityIdByReviewStatus(String locationId, Collection reviewStatus);
	
	public boolean isSmallCommunity(Long facilityId);
	
	public boolean isFacilityWasteWater(Long facilityId);
	
	public boolean isFacilityDecentralized(Long facilityId);
	
	public boolean hasFeedbackFacilityWithSRR(Long facilityId);

	public boolean statusChange(String facilityId, String newStatusId, CurrentUser user); 
	
	public boolean isFacilityReviewStatusFoundInList(String facilityId, List reviewStatusIdList);
	
	public Set changeStatusToFederalReviewRequested(Set facilities, CurrentUser user);
	
	public Set changeStatusToFederalReviewCorrection(Set facilities, CurrentUser user);
	
	public Set changeStatusToFederalAccepted(Set facilities, CurrentUser user);
	
	public Set changeStatusToStateCorrectionRequested(Set facilities, CurrentUser user);
	
	public Set changeStatusToStateAssigned(Set facilities, CurrentUser user);
	
	public Set changeStatusToLocalAssigned(Set facilities, CurrentUser user);
	
	public boolean isCwnsUserAccessLevel(String cwnsUserId, long accessLevel);
	
	public boolean isFacilityHasCwnsLocalUserWithUpdateFeebackAccessLevel(String facilityId);
	
	public boolean isCwnsUserHasAccessLevel(CurrentUser user, long accessLevel);
	
	public boolean isCurrentDateAfterDataEntryEndDate(CurrentUser user);
	
	public boolean isFeedBack(Long facilityId, CurrentUser user);

	public boolean isFacilityStormwater(Long facilityId);

	public boolean runCCValidationByLocationId(String locationId, String userId);

	public boolean requestAccess(Set masterSelectedList, CurrentUser currentUser);
	
	public boolean isUpdatable(CurrentUser user, Long facilityId, Long dataAreaId);
	
	public String getCWNSNbrByFacilityId(String facilityId);
	
	public void runAllCostCurveValidation(String userId);
	
	public void runAllCostCurveValidation(Long facilityId, String userId);
	
	public void syncCcDataAreas(Long facilityId, String userId);
	
	public void runAllCostCurveValidation(String locationId, String userId);
	
	public Collection getStateIdsForRegion(String regionCode);
	
	public void updateImportFacilitiesInDatabase(Set facilityNbrs, String userAndRole);
	
	public Set changeStatusToDeleted(Set facilities, CurrentUser user);
	
	public Set changeStatusToStateRequestedReturn(Set facilities, CurrentUser user);
}

