package gov.epa.owm.mtb.cwns.service;

import java.math.BigDecimal;
import java.util.Collection;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.congressionalDistrict.ConDistrictHelper;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.CongressionalDistrictRef;
import gov.epa.owm.mtb.cwns.model.CountyRef;
import gov.epa.owm.mtb.cwns.model.FacilityAddress;
import gov.epa.owm.mtb.cwns.model.GeographicAreaBoundary;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCongDistrict;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.model.GeographicAreaWatershed;
import gov.epa.owm.mtb.cwns.model.WatershedRef;

public interface FacilityAddressService {
	
	public static final String LAYER_STATE="STATES";
	public static final String LAYER_COUNTY="COUNTIES";
	public static final String LAYER_ZIP="ZIPCODES";
	public static final String LAYER_CITY="CITIES";
	public static final String LAYER_CONGRESSIONAL_DISTRICT="CONGRESSIONAL_DISTRICTS";
	public static final String LAYER_HUC="HUC";
	
	// Get Facility/project Address 
	public FacilityAddress getFacilityAddress(Long facilityId);
	
	// Get states from statc table
	public Collection getStates();
	
	// Save Facility/project Address 
	public void saveFacilityAddress(FacilityAddress obj,Long facilityId,CurrentUser user);
	
	// check if Facility has at least 1 facility type with facility overall type is 3
	public boolean isNonPointSource(Long facilityId);
	
	// Get Facility/project Coordinates
    public AbsoluteLocationPoint getFacilityCoordinates(Long facilityId);
    
    // Get location descriptions from static table
    public Collection getLocationdesc();
    
    // Get Method names from static table
    public Collection getMethods();
    
    // Get Datum names from static table
    public Collection getDatums(boolean excludeUnknown);
    
    // Is facility with in Tribal Territory
    public boolean isInTribalTerritory(Long facilityId);
    
    public boolean areReqFieldsPopulated(AbsoluteLocationPoint absLocationPoint);
    
    public boolean areReqFieldsEmpty(AbsoluteLocationPoint absLocationPoint);

	public void saveFacilityCoordinates(AbsoluteLocationPoint absoluteLocationPoint, Long facilityId, char tribalTerritory, CurrentUser user);

	public void saveGeographicArea(Long facilityId, char isInTribalTerritory, CurrentUser user);

	public void deleteFacilityCoordinates(Long facilityId, char isInTribalTerritory, CurrentUser user);

	public Collection getPORByLatitudeAndLongitude(Long facilityId, float latitude, float longitude);
	
	public Collection getGeographicAreaCountyByFacilityId(Long facilityId);

	public void saveFacilityCounty(String fipsCode, Long facilityId, String countyName, char primary, String locationId, CurrentUser user);

	public GeographicAreaCounty getGACByCountyNameAndFacilityId(String fipsCode, String countyName, Long facilityId);

	public GeographicAreaCounty getGeographicAreaCounty(Long geographicAreaId, Long countyId);

	public void updateGeographicAreaCounty(Long facilityId, long geographicAreaId, long countyId, char primary, CurrentUser user);

	public void deleteGeographicAreaCounty(Long facilityId, Long geographicAreaId, Long countyId, CurrentUser user);
	
	public Collection getGeographicAreaConDistrictByFacilityId(Long facilityId);
	
	// Get Congressional Districts in the location Id
	public Collection getConDistrictListByLocation(String locationId);

	public GeographicAreaCongDistrict getGeographicAreaCongDistrict(String conDistrictId, Long facilityId);

	public void saveGeographicAreaCongDistrict(Long facilityId, String conDistrictId, char primary, String locationId, CurrentUser user);

	public void updateGeographicAreaCongDistrict(Long facilityId, long geographicAreaId, String conDistrictId, char primary, CurrentUser user);
	
	public GeographicAreaCongDistrict getGACDByPrimaryKey(String conDistrictId, Long geographicAreaId);
	
	public void deleteGeographicAreaCongDistrict(Long facilityId, Long geographicAreaId, String conDistrictId, CurrentUser user);
	
	//Get geographic area watersheds for the facility
	public Collection getGeographicAreaWatershedByFacilityId(Long facilityId);

	public GeographicAreaWatershed getGeographicAreaWatershed(String watershedId, Long facilityId);

	public void saveGeographicAreaWatershed(Long facilityId, String watershedId, char primary, String locationId, CurrentUser user);

	public void updateGeographicAreaWatershed(Long facilityId, long geographicAreaId, String watershedId, char primary, CurrentUser user);
	
	public GeographicAreaWatershed getGAWatershedByPrimaryKey(String watershedId, Long geographicAreaId);
	
	public void deleteGeographicAreaWatershed(Long facilityId, Long geographicAreaId, String watershedId, CurrentUser user);

	public Collection getGeographicAreaBoundaries(Long facilityId);
	
	public void saveGeographicAreaBoundary(Long facilityId, Long boundaryId, CurrentUser user);
	
	public GeographicAreaBoundary getGeographicAreaBoundary(Long boundaryId, Long facilityId);
	
	public GeographicAreaBoundary getGABoundaryByPrimaryKey(Long boundaryId, Long geographicAreaId);
	
	public void deleteGeographicAreaBoundary(Long facilityId, Long geographicAreaId, Long boundaryId, CurrentUser user);
	
    //  Get Boundary List
    public Collection getBoundaryListByLocation(String locationId);
    
    public GeographicAreaWatershed getPrimaryGeographicAreaWatershed(Long facilityId);
    
    public GeographicAreaCounty getPrimaryGeographicAreaCounty(Long facilityId);
    
    public ConDistrictHelper getPrimaryGeographicAreaCongDistrict(Long facilityId);

	public void deleteFacilityAddress(Long facilityId);
	
	public String getWebRITURL(Long facilityId);
	
	public String getWebritSession();
	
	public String getBoundingBox(BigDecimal latitude, BigDecimal longitude);

	public String getWebRITSessionInformation(String sessionId);

	public void invalidateWebRITSession(String sessionId);

	public void saveWebRITSession(String sessionId, Long facilityId);
	
	public void removeFeatures(Long facilityId);
	
	public boolean saveFeatureIds(String xmlRes, Long facilityId);
	
	public void saveWebRITSession(String latitude, String longitude, Long facilityId);

	public void updateMeasureDate(Long facilityId, String measureDate, CurrentUser user);
	
	public String getCountyByLatLong(double latitude, double longitude);

	public Collection getAdjacentCounties(Long facilityId);
	
	public Collection getAdjacentCounties(CountyRef county, Long facilityId);
	
	public CountyRef getPreferredCounty(Long facilityId);
	
	public CongressionalDistrictRef getFacilityCongressionalDistrictByLatLong(Long facilityId);
	
	public CongressionalDistrictRef getPreferredCongressionalDistrict(Long facilityId);
	
	public CongressionalDistrictRef getCongressionalDistrictByLatLong(double latitude, double longitude);
	
	public WatershedRef getWatershedByLatLong(double latitude, double longitude);
	
	public WatershedRef getPreferredWatershed(Long facilityId);
	
	public Collection getAdjacentWatersheds(Long facilityId);
	
	public Collection getConDistrictListByLocationObjects(String locationId);
	
	public Collection getAdjacentCongressionalDistricts(CongressionalDistrictRef conDis, Long facilityId);
	
	public Collection getAdjacentCongressionalDistricts(Long facilityId);

	public Collection getAdjacentWatersheds(WatershedRef ws, Long facilityId);

	public String getBoundingBox(Long facilityId);

	public void softDeleteGeographicAreaCounty(Long facilityId, Long geographicAreaId, Long countyId, CurrentUser user);

	public void softDeleteGeographicAreaCongDistrict(Long facilityId, Long geographicAreaId, String conDistrictId, CurrentUser user);

	public void softDeleteGeographicAreaWatershed(Long facilityId, Long geographicAreaId, String watershedId, CurrentUser user);

	public void softDeleteGeographicAreaBoundary(Long facilityId, Long geographicAreaId, Long boundaryId, CurrentUser user);
	
	public AbsoluteLocationPoint getPrimaryCountyCentroid(Long facilityId);
	
	public AbsoluteLocationPoint getPrimaryWatershedCentroid(Long facilityId);	
	
	public boolean areFieldsRequired(Long facilityId);
	
	public void deleteFacilityCoordinates(AbsoluteLocationPoint alp);
	
}
