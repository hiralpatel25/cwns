package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface FacilityTypeService {
	
	public static final Long FACILITY_TYPE_TREATMENT_PLANT = new Long(1);
	public static final Long FACILITY_TYPE_COMBINED_SEWER = new Long(2);
	public static final Long FACILITY_TYPE_SEPERATE_SEWER = new Long(3);	
	public static final Long FACILITY_TYPE_OTHER = new Long(77);
	public static final Long FACILITY_TYPE_MS4_I = new Long(8);
	public static final Long FACILITY_TYPE_MS4_II = new Long(9);
	public static final Long FACILITY_TYPE_MS4_NON = new Long(10);
	public static final Long FACILITY_TYPE_URBAN = new Long(14);
	public static final Long FACILITY_TYPE_STORAGE_TANKS = new Long(18);
	public static final Long FACILITY_TYPE_SANITARY_LANDFILL = new Long(19);
	public static final Long FACILITY_TYPE_INTERCEPTOR_SEWER = new Long(25);
	public static final Long FACILITY_TYPE_TMDL_PLAN_DEVELEOPMENT = new Long(30);
	public static final Long FACILITY_TYPE_WATERSHED_MANAGEMENT_PLAN_DEVELOPMENT = new Long(31);
	public static final Long FACILITY_TYPE_STORMWATER_PROJECT_PLAN_DEVELOPMENT = new Long(32);	
	
	public static final long FACILITY_OVERALL_TYPE_WASTEWATER = 1;
	public static final long FACILITY_OVERALL_TYPE_STROMWATER = 2;
	public static final long FACILITY_TYPE_OVERALL_TYPE_NPS=3;
	public static final long FACILITY_OVERALL_TYPE_DECENTRALIZED = 4;
	public static final long FACILITY_OVERALL_TYPE_PLANNING = 6;
	
	public static final Long CHANGE_TYPE_INCREASE_CAPACILITY = new Long(3);
	public static final Long CHANGE_TYPE_NO_CHANGE = new Long(1);
	public static final Long CHANGE_TYPE_ABANDONED = new Long(7);
	public static final Long CHANGE_TYPE_INCREASE_LEVEL_OF_TREATMENT = new Long(4);
	
	
	
	//Facility Type Status
	public static final String FACILITY_TYPE_STATUS_PRESENT = "P";
	public static final String FACILITY_TYPE_STATUS_PROJECTED = "F";
	public static final String FACILITY_TYPE_STATUS_BOTH = "B";
	
	public static final long FACILITY_TYPE_NONSOURCE_POINT = 3;
	


	
	public Collection getFacityType(Long facilityId);
	
	public Collection getAvailableFacityType(Long facilityId);
	
	public Collection getChangeTypes();
	
	public Collection getChangeTypesRules();
	
	public Collection getFacilityTypeChangeRules();
	
	public boolean deleteFacilityType(Long facilityId, Long facilityTypeId, Collection errors, String userId);
	
	public boolean isDataDeleted(Long facilityId, Long facilityTypeId, Collection messages, String userId); 
	
	public void addFacilityType(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String presentTPType, String projectedTPType, String npsStatus, String userId);
	
	public void updateFacilityType(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String presentTPType, String projectedTPType, String npsStatus, String userId);
	
	public FacilityType getFacilityType(Long facilityId, Long facilityTypeId);
	
	public FacilityTypeRef getFacilityTypeRef(Long facilityTypeId);
	
	public Collection getChangeType(Collection changeTypeIds);
	
	public boolean isFacilityTypeStatusProjected(Long facilityId);
	
	public boolean isFacilityTypeStatusPresent(Long facilityId);
	
	public boolean isFacilityTypeStatusPresentAndApplicableToDataArea(Long facilityId, Long dataAreaId);
	
	public boolean isFacilityTypeStatusProjectedAndApplicableToDataArea(Long facilityId, Long dataAreaId);
	
	public boolean isValidForDataArea(Long facilityTypeId, Long dataAreaId);
	
	public boolean isFacilityTypeValidForDataAreaAndHasChangeType(Long facilityId, Long dataAreaId, Long changeTypeId);
	
	public void setStandardDataAreas(Long facilityId, String userId);

	public boolean isFacilityNoChangeOrAbandoned(Long facilityId);
	
	public String getFacilityDataAreasQueryParam(Long facilityId);

	public boolean isFacilityTypeNoChangeOrAbandoned(Long facilityId, Long facilityTypeId);
	
	public boolean isFacilityTypeValid(Long facilityId, Long facilityType, String presentProjectedFlag);
	
	public boolean isUpdateFacilityTypeDeleteData(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String userId, Collection messages);
	
	public Collection getChangeTypeRefs(Collection selectedChangesList);

	public void softDeleteFacilityType(Long facilityId, Long facilityTypeId, String userId);

	public void updateFacilityTypeForFeedBack(Long facilityId, Long facilityTypeId, String status, Collection facilityChangeIds, String presentTPType, String projectedTPType, String npsStatus, String userId);
}
