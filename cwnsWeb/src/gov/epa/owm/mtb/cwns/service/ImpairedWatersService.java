package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;

import java.util.Collection;
import java.util.Set;

public interface ImpairedWatersService {
	
	public static final int MAX_RESULTS = 5;
	public static final int BY_LAT_LONG_RADIUS = 0;
	public static final int BY_HUC8 = 1;
	public static final int BY_KEYWORD = 2;
	public static final int TMDL_ALL = 0;
	public static final int TMDL_COMPLETED = 1;
	public static final int TMDL_NEEDED = 2;
	public static final String TEXT_TMDL_COMPLETED = "TMDL Completed";
	public static final String TEXT_TMDL_NEEDED = "TMDL Needed";
	

	public Collection getImpairedWaterEntities(long facilityId, 
			int webServiceMethod, 
			int radiusMile, 
			String huc8, 
			int tmdlOption,
			String keyword) throws Exception;
	public boolean addImpairedWaters(long facilityId, Set listIds, Collection masterList, String user);
	public Collection getSearchDisplayList(Collection qualifiedImpairedWaterList, int startIndex);
	public Collection getQualifiedImpairedWaterList(Collection masterList, long facilityId, int tmdlOption);
	public boolean isUpdatable(CurrentUser user, Long facilityId);
	public void deleteDocumentIfNoReferences(long facilityId, String waterBodyName, String listId);
	public void deleteFacilityDocumentIfExists(long facilityId,  String waterBodyName, String listId);
	public void deleteImpairedWaterIfNoReferences(long facilityId, String listId);
	public void deleteFacilityImpairedWaterIfExists(long facilityId, String listId);
	public long saveDocument(long facilityId, String waterBodyName, String ListId, String endDate, String userId);
	public void saveFacilityDocument(long facilityId, long documentId, String userId);
	public String saveImpairedWater(String waterBodyName, String ListId, String TMDLReportUrl, String NTTSUrl, String waterBodyType, String userId);
	public void saveFacilityImpairedWater(long facilityId, String listId, String userId);
    public Collection getFacilityImpairedWaterList(long facilityId);
    public boolean costExistsForFacilityDocument(long facilityId, String waterBodyName, String listId);
    public void delete(long facilityId, String waterBodyName, String listId, String userId);
	public boolean costCurvesExistsForFacilityDocument(long facilityId, String waterBodyName, String listId);
	public void softDeleteFacilityType(Long facilityId, String waterBodyName, String listId, CurrentUser user);
}