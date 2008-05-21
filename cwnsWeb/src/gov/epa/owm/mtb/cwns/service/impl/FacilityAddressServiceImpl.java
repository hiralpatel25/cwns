package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.boundary.BoundaryListHelper;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.congressionalDistrict.ConDistrictDisplayHelper;
import gov.epa.owm.mtb.cwns.congressionalDistrict.ConDistrictHelper;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.BoundaryRef;
import gov.epa.owm.mtb.cwns.model.CongressionalDistrictRef;
import gov.epa.owm.mtb.cwns.model.CountyRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityAddress;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatusId;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.GeographicArea;
import gov.epa.owm.mtb.cwns.model.GeographicAreaBoundary;
import gov.epa.owm.mtb.cwns.model.GeographicAreaBoundaryId;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCongDistrict;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCongDistrictId;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCountyId;
import gov.epa.owm.mtb.cwns.model.GeographicAreaTypeRef;
import gov.epa.owm.mtb.cwns.model.GeographicAreaWatershed;
import gov.epa.owm.mtb.cwns.model.GeographicAreaWatershedId;
import gov.epa.owm.mtb.cwns.model.HorizontalCllctnMethodRef;
import gov.epa.owm.mtb.cwns.model.HorizontalCoordntDatumRef;
import gov.epa.owm.mtb.cwns.model.LocationDescriptionRef;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.model.WatershedRef;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import CreateList_Service.waters.rti.org.Waters_Create_List_Service;
import CreateList_Service.waters.rti.org.Waters_Create_List_ServiceLocator;
import CreateList_Service.waters.rti.org.Waters_Create_List_ServiceSoap;
import Indexing_Service.waters.rti.org.Waters_Indexing_ServiceLocator;
import Indexing_Service.waters.rti.org.Waters_Indexing_ServiceSoap;
import ZoomService.waters.rti.org.WATERS_Zoom_ServiceLocator;
import ZoomService.waters.rti.org.WATERS_Zoom_ServiceSoap;

public class FacilityAddressServiceImpl extends CWNSService implements FacilityAddressService {

	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}
	
	private FacilityDAO facilityDAO;
	public void setFacilityDAO(FacilityDAO facilityDAO) {
		this.facilityDAO = facilityDAO;
	}
	
	public FacilityAddress getFacilityAddress(Long facilityId){
		FacilityAddress facAddress = null;
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		facAddress = (FacilityAddress)searchDAO.getSearchObject(FacilityAddress.class, scs);
		  return facAddress;	
	}
	
	public Collection getStates(){
		ArrayList columns = new ArrayList();
		columns.add("locationId");
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		Collection results = searchDAO.getSearchList(StateRef.class, columns, new SearchConditions(), sortArray);
		return results;	
    }
	
	public  StateRef getStateByLocation(String locationId){
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		StateRef state = (StateRef)searchDAO.getSearchObject(StateRef.class, scs);
		return state;	
    }
	
	public void saveFacilityAddress(FacilityAddress obj, Long facilityId, CurrentUser user){
		
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		        		
		if (obj != null){
			searchDAO.saveObject(obj);
		}
		
	}
	
	public void deleteFacilityAddress(Long facilityId) {
		FacilityAddress facAddress = null;
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		facAddress = (FacilityAddress)searchDAO.getSearchObject(FacilityAddress.class, scs);
		if (facAddress != null)
			searchDAO.removeObject(facAddress);
	}
	
	public boolean isNonPointSource(Long facilityId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection facilityTypes = searchDAO.getSearchList(FacilityType.class, scs);
		if (facilityTypes!=null && facilityTypes.size()>0){
		   ArrayList overallType = new ArrayList();
		   overallType.add(new Long(3));
		   Collection c = getFacilityTypesByOverAllTypes(facilityId,overallType);
			if (c!=null && c.size() > 0 && c.size() == facilityTypes.size())
			  return true;
		    else return false;
		}
		else return false;
	}
	
	private Collection getFacilityTypesByOverAllTypes(Long facilityId, Collection overallType){
		ArrayList facilityType = new ArrayList();
		facilityType.add(new Long(2));
		facilityType.add(new Long(3));
		facilityType.add(new Long(25));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityTypeRef", "ft", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ft.facilityOverallTypeRef", "fot", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("fot.facilityOverallTypeId",SearchCondition.OPERATOR_IN,overallType));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ft.facilityTypeId",SearchCondition.OPERATOR_NOT_IN,facilityType));
		return searchDAO.getSearchList(FacilityType.class, new ArrayList(), scs, new ArrayList(), aliasArray);
		
	}
	
	public boolean areFieldsRequired(Long facilityId){
		ArrayList overallType = new ArrayList();
		overallType.add(new Long(1));
		overallType.add(new Long(5));
		Collection c = getFacilityTypesByOverAllTypes(facilityId,overallType);
		//System.out.println("collection size---"+c.isEmpty());
		if (c!=null && c.size() > 0)
			return true;
		else return false;
	}

	public AbsoluteLocationPoint getFacilityCoordinates(Long facilityId) {
		AbsoluteLocationPoint obj = null;
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		obj = (AbsoluteLocationPoint)searchDAO.getSearchObject(AbsoluteLocationPoint.class, scs, aliasArray);
		return obj;
	}
	
	public void deleteFacilityCoordinates(AbsoluteLocationPoint alp){
		if(alp!=null){
			searchDAO.removeObject(alp);
		}		
	}
	
	public String getWebRITURL(Long facilityId){
		AbsoluteLocationPoint alp= getFacilityCoordinates(facilityId);
    	BigDecimal latitude = alp.getLatitudeDecimalDegree();
    	BigDecimal longitude = alp.getLongitudeDecimalDegree();
    	if(latitude!=null && longitude!=null){
        	if(alp.getLatitudeDirection()!=null && alp.getLatitudeDirection().charValue()=='S'){
        		latitude = latitude.negate();
        	}
        	if(alp.getLongitudeDirection()!=null && alp.getLongitudeDirection().charValue()=='W'){
        		longitude = longitude.negate();
        	}
        	log.debug("Lat: (" + latitude.toString() + ")  Long: (" + longitude.toString() + ")");
        	//get session
        	String sessionId =getWebritSession();
        	String getBoundingBox = getBoundingBox(latitude, longitude);
        	String viewerUrl = CWNSProperties.getProperty("webrit.viewer.url");
        	String viewerType = CWNSProperties.getProperty("webrit.viewer.type");
        	String viewerProperties = CWNSProperties.getProperty("webrit.viewer.boundingbox.properties");
 
        	String url = viewerUrl+"?Type="+viewerType+"&SessionID="+sessionId+"&OffNHDLat="+ latitude.toString() +"&OffNHDLon="+  longitude.toString() +"&BBOX="+getBoundingBox+viewerProperties;
        	log.debug("WebRIT Viewer URL" + url);
        	return url;    		
    	}else{
    		//getzip and its bounding box
    		//get county and its bounding box
    		//get state location and state bounding box
    		
    	}
		return null;
	}
	
	public String getWebritSession(){
		String sessionId=null;
		try {
		    String registrationKey = CWNSProperties.getProperty("webrit.registration.key");	
		    Waters_Indexing_ServiceSoap waterIndexingService = getWaterIndexingService();	    	
	    	String sessionIdXML =waterIndexingService.createSession(registrationKey);
	    	sessionId =getSession(sessionIdXML);
			log.debug("End water Indexing Services call...");
		}catch (Exception e) {
			log.error("Error Calling water index service", e);
		}
		return sessionId;
	}
	
	public String getCountyByLatLong(double latitude, double longitude){
		String county = null;
		WATERS_Zoom_ServiceSoap wzs = getZoomService();
		String responseXml=null;
		try {
			responseXml=wzs.findFeatureByPoint(LAYER_COUNTY, latitude+"", longitude+"");
			Document doc = null;
			if (responseXml!=null)
				doc = getXMLDocument(responseXml);
			if(doc!=null){
				county= getNodeValue(doc, "COUNTYNAME");	
			}
		} catch (RemoteException e) {
			log.error("Error Calling find feature by point", e);
		}
		return county;
	}
	
	public CountyRef getFacilityCounty(Long facilityId){
		CountyRef county=null;
		AbsoluteLocationPoint alp= getFacilityCoordinates(facilityId);
		if(alp!=null){
	    	BigDecimal latitude = alp.getLatitudeDecimalDegree();
	    	BigDecimal longitude = alp.getLongitudeDecimalDegree();
	    	if(latitude!=null && longitude!=null){
	        	if(alp.getLatitudeDirection()!=null && alp.getLatitudeDirection().charValue()=='S'){
	        		latitude = latitude.negate();
	        	}
	        	if(alp.getLongitudeDirection()!=null && alp.getLongitudeDirection().charValue()=='W'){
	        		longitude = longitude.negate();
	        	}
	        	log.debug("Lat: (" + latitude.toString() + ")  Long: (" + longitude.toString() + ")");
	        	String countyName = getCountyByLatLong(latitude.doubleValue(), longitude.doubleValue());
	        	if(countyName!=null && !"".equals(countyName)){
	        		String locationId = alp.getGeographicArea().getFacility().getLocationId();
	               	county=getCountyByName(countyName, locationId);
	        	} 
	    	}	
		}
		return county;
	}
	
	private CountyRef getCountyByName(String countyName, String locationId) {
		//fetch the county from the county table / state??
		SearchConditions scs = new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_EQ, countyName));
		scs.setCondition(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		return (CountyRef)searchDAO.getSearchObject(CountyRef.class, scs);
	}
	
	public CountyRef getPreferredCounty(Long facilityId){
		CountyRef county = getFacilityCounty(facilityId);
		if(county==null){
			//get the primary county
			GeographicAreaCounty  gac = getPrimaryGeographicAreaCounty(facilityId);
			if(gac!=null){
				county = gac.getCountyRef();
			}
		}		
		return county;
	}
	
	public AbsoluteLocationPoint getPrimaryCountyCentroid(Long facilityId){
		AbsoluteLocationPoint alp = null;
		//get primary county
		GeographicAreaCounty  gac = getPrimaryGeographicAreaCounty(facilityId);
		if(gac!=null){
			new AbsoluteLocationPoint();
			Facility f = gac.getGeographicArea().getFacility();
			CountyRef county = gac.getCountyRef();
			WATERS_Zoom_ServiceSoap wzs = getZoomService();
			Waters_Create_List_ServiceSoap wcls = getWaterCreateListService();
			String responseXml=null;
			try {
				//get a unique ID
				responseXml=wcls.createList(LAYER_COUNTY, county.getName(),f.getLocationId(), "", "");
				Document doc = getXMLDocument(responseXml);
				if(doc!=null){
					String uniqueId= getNodeValue(doc, "UNIQUE_ID");
					if(uniqueId!=null && !"".equals(uniqueId)){
						//based on unique Id get adjacent facilitites
						responseXml=wzs.getBBox(uniqueId, LAYER_COUNTY, "", "", "");
						doc = getXMLDocument(responseXml);
						if(doc!=null){
							String sminx = getNodeValue(doc, "MINX");
							String sminy = getNodeValue(doc, "MINY");
							String smaxx = getNodeValue(doc, "MAXX");
							String smaxy = getNodeValue(doc, "MAXY");
							
							double minx = (sminx!=null && !"".equals(sminx))? (new Double(sminx)).doubleValue():0.0;
							double miny = (sminy!=null && !"".equals(sminy))? (new Double(sminy)).doubleValue():0.0;
							double maxx = (smaxx!=null && !"".equals(smaxx))? (new Double(smaxx)).doubleValue():0.0;
							double maxy = (smaxy!=null && !"".equals(smaxy))? (new Double(smaxy)).doubleValue():0.0;
							alp = new AbsoluteLocationPoint();
							double x = (minx+maxx)/2;
							double y = (miny+maxy)/2;
							if(x < 0){
								alp.setLongitudeDecimalDegree(new BigDecimal(x*-1));
								alp.setLongitudeDirection(new Character('W'));
							}else{
								alp.setLongitudeDecimalDegree(new BigDecimal(x));
								alp.setLongitudeDirection(new Character('E'));
							}
							if(y < 0){
								alp.setLatitudeDecimalDegree(new BigDecimal(y*-1));
								alp.setLatitudeDirection(new Character('S'));
							}else{
								alp.setLatitudeDecimalDegree(new BigDecimal(y));
								alp.setLatitudeDirection(new Character('N'));
							}							
						}
						LocationDescriptionRef locDescRef = new LocationDescriptionRef();
						locDescRef.setLocationDescriptionId("102");
						alp.setLocationDescriptionRef(locDescRef);
						
						HorizontalCoordntDatumRef horizontalCoordntDatumRef = new HorizontalCoordntDatumRef();
						horizontalCoordntDatumRef.setHorizontalCoordntDatumId(4);
						alp.setHorizontalCoordntDatumRef(horizontalCoordntDatumRef);
						
						HorizontalCllctnMethodRef horizontalCllctnMethodRef = new HorizontalCllctnMethodRef();
						horizontalCllctnMethodRef.setHorizontalCllctnMethodId(22);
						alp.setHorizontalCllctnMethodRef(horizontalCllctnMethodRef);
						alp.setMeasurementDate(new Date());
						alp.setScale("10000");
						alp.setSourceCd('W');						
					}
				}
			}catch (RemoteException e) {
				log.error("Error while fetching adjacent counties for facility" + facilityId, e);
			}	
		}
		return alp;
	}
	
	
	public Collection getAdjacentCounties(Long facilityId){
		CountyRef county = getFacilityCounty(facilityId);
		return getAdjacentCounties(county, facilityId);
	}

	public Collection getAdjacentCounties(CountyRef county, Long facilityId){
		Facility f = facilityDAO.findByFacilityId(facilityId.toString());
		Collection adjCounties=null;
		if(county==null) return null;
		WATERS_Zoom_ServiceSoap wzs = getZoomService();
		Waters_Create_List_ServiceSoap wcls = getWaterCreateListService();
		String responseXml=null;
		try {
			//get a unique ID
			responseXml=wcls.createList(LAYER_COUNTY, county.getName(),f.getLocationId(), "", "");
			Document doc = getXMLDocument(responseXml);
			if(doc!=null){
				String uniqueId= getNodeValue(doc, "UNIQUE_ID");
				if(uniqueId!=null && !"".equals(uniqueId)){
					//based on unique Id get adjacent facilitites
					responseXml=wzs.findAdjacentFeatures(uniqueId, LAYER_COUNTY, "5");
					doc = getXMLDocument(responseXml);
					if(doc!=null){
						//parse the document and get multiple counties from the county list fetch the counties from the database
						Collection countyArr = getCountyAndStateValue(doc);
						if(countyArr!=null){
							countyArr.add(county.getName().toUpperCase() + "-" + county.getLocationId().toUpperCase());
							adjCounties =facilityDAO.getCounties(countyArr, f.getLocationId());
							//debug
							if(adjCounties!=null){
								for (Iterator iter = adjCounties.iterator(); iter.hasNext();) {
									CountyRef ct = (CountyRef) iter.next();
									log.debug((ct.getName()+ ","+ ct.getLocationId()));			
								}			
							}
						}						
					}
				}
			}
		} catch (RemoteException e) {
			log.error("Error while fetching adjacent counties for facility" + facilityId, e);
		}		
		return adjCounties;
	}
	
	//Get preffred congression districts
	
	public Collection getAdjacentCongressionalDistricts(Long facilityId){
		CongressionalDistrictRef conDis = getPreferredCongressionalDistrict(facilityId);
		return getAdjacentCongressionalDistricts(conDis, facilityId);
	}
	
	public Collection getAdjacentCongressionalDistricts(CongressionalDistrictRef conDis, Long facilityId){
		ArrayList retConHelper=new ArrayList();
		Facility f = facilityDAO.findByFacilityId(facilityId.toString());
		Collection adjConDis=null;
		if(conDis==null) return null;
		WATERS_Zoom_ServiceSoap wzs = getZoomService();
		Waters_Create_List_ServiceSoap wcls = getWaterCreateListService();
		String responseXml=null;
		try {
			//get a unique ID
			System.out.println("Search string for congressional district: "+conDis.getCongressionalDistrictId().substring(2) + " ->"+conDis.getCongressionalDistrictId());
			responseXml=wcls.createList(LAYER_CONGRESSIONAL_DISTRICT,(conDis.getCongressionalDistrictId().substring(2)),f.getLocationId(), "", "");
			Document doc = getXMLDocument(responseXml);
			if(doc!=null){
				String uniqueId= getNodeValue(doc, "UNIQUE_ID");
				if(uniqueId!=null && !"".equals(uniqueId)){
					//based on unique Id get adjacent facilitites
					responseXml=wzs.findAdjacentFeatures(uniqueId, LAYER_CONGRESSIONAL_DISTRICT, "5");
					doc = getXMLDocument(responseXml);
					if(doc!=null){
						//parse the document and get multiple counties from the county list fetch the counties from the database
						Collection conDisArr = getConDisAndStateValue(doc);
						if(conDisArr!=null){
							conDisArr.add(conDis.getCongressionalDistrictId());
							//facilityDAO.getConDistricts(conDisArr)
							adjConDis =  getCongressionalDistrictById(conDisArr, f.getLocationId());
							if(adjConDis!=null){
								for (Iterator iter = adjConDis.iterator(); iter.hasNext();) {
									CongressionalDistrictRef cdr = (CongressionalDistrictRef) iter.next();
									ConDistrictDisplayHelper ch = new ConDistrictDisplayHelper();
									ch.setCongressionalDistrictId(cdr.getCongressionalDistrictId());
									ch.setLocationId(cdr.getLocationId());
									retConHelper.add(ch);
									log.debug((ch.getCongressionalDistrictId()+ ","+ ch.getLocationId()));			
								}	
							}
						}						
					}
				}
			}
		} catch (RemoteException e) {
			log.error("Error while fetching adjacent counties for facility" + facilityId, e);
		}
		return retConHelper;
	}
	
	public CongressionalDistrictRef getPreferredCongressionalDistrict(Long facilityId){
		CongressionalDistrictRef conDis = getFacilityCongressionalDistrictByLatLong(facilityId);
		if(conDis==null){
			//get the primary congressional district
			GeographicAreaCongDistrict GC = getPrimaryGeographicAreaConDistrictByFacilityId(facilityId);
			if (GC!=null)
			conDis = GC.getCongressionalDistrictRef();
		}		
		return conDis;
	}	
	
	public CongressionalDistrictRef getFacilityCongressionalDistrictByLatLong(Long facilityId){
		CongressionalDistrictRef congressionalDistrict=null;
		AbsoluteLocationPoint alp= getFacilityCoordinates(facilityId);
		if(alp!=null){
	    	BigDecimal latitude = alp.getLatitudeDecimalDegree();
	    	BigDecimal longitude = alp.getLongitudeDecimalDegree();
	    	if(latitude!=null && longitude!=null){
	        	if(alp.getLatitudeDirection()!=null && alp.getLatitudeDirection().charValue()=='S'){
	        		latitude = latitude.negate();
	        	}
	        	if(alp.getLongitudeDirection()!=null && alp.getLongitudeDirection().charValue()=='W'){
	        		longitude = longitude.negate();
	        	}
	        	log.debug("Lat: (" + latitude.toString() + ")  Long: (" + longitude.toString() + ")");
	        	congressionalDistrict = getCongressionalDistrictByLatLong(latitude.doubleValue(), longitude.doubleValue());
	    	}			
		}
		return congressionalDistrict;
	}
	
	public CongressionalDistrictRef getCongressionalDistrictByLatLong(double latitude, double longitude){
		String congressionalDistrict = null;
		String locationId = null;
		WATERS_Zoom_ServiceSoap wzs = getZoomService();
		String responseXml=null;
		try {
			responseXml=wzs.findFeatureByPoint(LAYER_CONGRESSIONAL_DISTRICT, latitude+"", longitude+"");
			Document doc = getXMLDocument(responseXml);
			if(doc!=null){
				congressionalDistrict= getNodeValue(doc, "DISTRICT");
				locationId = getNodeValue(doc, "STATEABBR");
			}
		} catch (RemoteException e) {
			log.error("Error Calling find feature by point", e);
			return null;
		}
		//get the congression district
		//1. first get the state fipscode
		StateRef s = null;
		if (locationId!=null)
			s = getStateByLocation(locationId);
		
		String stateCode = null;
		if (s!=null)
		 stateCode = s.getStateId();
		
		String congressionalDistrictId = null;
		if (stateCode!=null && congressionalDistrict!=null)
			congressionalDistrictId = stateCode +  congressionalDistrict;		
		return getCongressionalDistrictById(congressionalDistrictId);
	}
	
	private CongressionalDistrictRef getCongressionalDistrictById(String congressionalDistrictId) {
		if (congressionalDistrictId!=null){
		//fetch the county from the county table / state??
		SearchConditions scs = new SearchConditions(new SearchCondition("congressionalDistrictId", SearchCondition.OPERATOR_EQ, congressionalDistrictId));
			return (CongressionalDistrictRef)searchDAO.getSearchObject(CongressionalDistrictRef.class, scs);
		}else{
			return null;
		}
	}
	
	private Collection getCongressionalDistrictById(Collection congressionalDistrictIds, String locationId) {
		//fetch the county from the county table / state??
		SearchConditions scs = new SearchConditions(new SearchCondition("congressionalDistrictId", SearchCondition.OPERATOR_IN, congressionalDistrictIds));
		scs.setCondition(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		return searchDAO.getSearchList(CongressionalDistrictRef.class, scs);
	}
	
	public AbsoluteLocationPoint getPrimaryWatershedCentroid(Long facilityId){
		AbsoluteLocationPoint alp = null;
		//get primary watershed
		GeographicAreaWatershed gaws = getPrimaryGeographicAreaWatershed(facilityId);
		if(gaws!=null){
			new AbsoluteLocationPoint();
			Facility f = gaws.getGeographicArea().getFacility();
			WatershedRef ws=gaws.getWatershedRef();
			WATERS_Zoom_ServiceSoap wzs = getZoomService();
			Waters_Create_List_ServiceSoap wcls = getWaterCreateListService();
			String responseXml=null;
			try {
				//get a unique ID
				responseXml=wcls.createList(LAYER_HUC,ws.getWatershedId(),"", "", "");
				Document doc = getXMLDocument(responseXml);
				if(doc!=null){
					String uniqueId= getNodeValue(doc, "UNIQUE_ID");
					if(uniqueId!=null && !"".equals(uniqueId)){
						//based on unique Id get adjacent facilitites
						responseXml=wzs.getBBox(uniqueId, LAYER_HUC, "", "", "");
						doc = getXMLDocument(responseXml);
						if(doc!=null){
							String sminx = getNodeValue(doc, "MINX");
							String sminy = getNodeValue(doc, "MINY");
							String smaxx = getNodeValue(doc, "MAXX");
							String smaxy = getNodeValue(doc, "MAXY");
							
							double minx = (sminx!=null && !"".equals(sminx))? (new Double(sminx)).doubleValue():0.0;
							double miny = (sminy!=null && !"".equals(sminy))? (new Double(sminy)).doubleValue():0.0;
							double maxx = (smaxx!=null && !"".equals(smaxx))? (new Double(smaxx)).doubleValue():0.0;
							double maxy = (smaxy!=null && !"".equals(smaxy))? (new Double(smaxy)).doubleValue():0.0;
							alp = new AbsoluteLocationPoint();
							double x = (minx+maxx)/2;
							double y = (miny+maxy)/2;
							if(x < 0){
								alp.setLongitudeDecimalDegree(new BigDecimal(x*-1));
								alp.setLongitudeDirection(new Character('W'));
							}else{
								alp.setLongitudeDecimalDegree(new BigDecimal(x));
								alp.setLongitudeDirection(new Character('E'));
							}
							if(y < 0){
								alp.setLatitudeDecimalDegree(new BigDecimal(y*-1));
								alp.setLatitudeDirection(new Character('S'));
							}else{
								alp.setLatitudeDecimalDegree(new BigDecimal(y));
								alp.setLatitudeDirection(new Character('N'));
							}							
						}
						LocationDescriptionRef locDescRef = new LocationDescriptionRef();
						locDescRef.setLocationDescriptionId("102");
						alp.setLocationDescriptionRef(locDescRef);
						
						HorizontalCoordntDatumRef horizontalCoordntDatumRef = new HorizontalCoordntDatumRef();
						horizontalCoordntDatumRef.setHorizontalCoordntDatumId(4);
						alp.setHorizontalCoordntDatumRef(horizontalCoordntDatumRef);
						
						HorizontalCllctnMethodRef horizontalCllctnMethodRef = new HorizontalCllctnMethodRef();
						horizontalCllctnMethodRef.setHorizontalCllctnMethodId(22);
						alp.setHorizontalCllctnMethodRef(horizontalCllctnMethodRef);
						alp.setMeasurementDate(new Date());
						alp.setScale("10000");
						alp.setSourceCd('W');
					}
				}
			}catch (RemoteException e) {
				log.error("Error while fetching adjacent counties for facility" + facilityId, e);
			}	
		}
		return alp;
	}	
	
	//get Adjacent WaterSheds
	public Collection getAdjacentWatersheds(Long facilityId){
		WatershedRef ws = getPreferredWatershed(facilityId);
		return getAdjacentWatersheds(ws, facilityId);
	}
	
	public Collection getAdjacentWatersheds(WatershedRef ws, Long facilityId){
		Facility f = facilityDAO.findByFacilityId(facilityId.toString());
		Collection adjWatersheds=null;
		if(ws==null) return null;
		WATERS_Zoom_ServiceSoap wzs = getZoomService();
		Waters_Create_List_ServiceSoap wcls = getWaterCreateListService();
		String responseXml=null;
		try {
			//get a unique ID
			responseXml=wcls.createList(LAYER_HUC,(ws.getWatershedId()),"", "", "");
			Document doc = getXMLDocument(responseXml);
			if(doc!=null){
				String uniqueId= getNodeValue(doc, "UNIQUE_ID");
				if(uniqueId!=null && !"".equals(uniqueId)){
					//based on unique Id get adjacent facilitites
					responseXml=wzs.findAdjacentFeatures(uniqueId, LAYER_HUC, "5");
					doc = getXMLDocument(responseXml);
					if(doc!=null){
						//parse the document and get multiple counties from the county list fetch the counties from the database
						Collection hucArr = getNodeValues(doc, "UNIQUE_ID");
						ArrayList hucList = new ArrayList();
						for (Iterator iter = hucArr.iterator(); iter.hasNext();) {
							String huc = (String) iter.next();
							hucList.add(huc);							
						}
						if(hucList!=null){
							//facilityDAO.getConDistricts(conDisArr)
							hucList.add(ws.getWatershedId());
							adjWatersheds =  getWatershedListByWatershedAndLocation(hucList,f.getLocationId());
							if(adjWatersheds!=null){
								for (Iterator iter = adjWatersheds.iterator(); iter.hasNext();) {
									WatershedRef wsr = (WatershedRef) iter.next();
									log.debug((wsr.getName()+ ","+ wsr.getWatershedId()));			
								}
							}
						}						
					}
				}
			}
		} catch (RemoteException e) {
			log.error("Error while fetching adjacent watershed for facility" + facilityId, e);
		}
		return adjWatersheds;
	}
	
	
	public WatershedRef getPreferredWatershed(Long facilityId){
		WatershedRef ws = getFacilityWatershedByLatLong(facilityId);
		if(ws==null){
			//get the primary county
			GeographicAreaWatershed gaws = getPrimaryGeographicAreaWatershed(facilityId);
			if(gaws!=null){
				ws=gaws.getWatershedRef();	
			}
		}		
		return ws;
	}	
	
	public WatershedRef getFacilityWatershedByLatLong(Long facilityId){
		WatershedRef ws=null;
		AbsoluteLocationPoint alp= getFacilityCoordinates(facilityId);
		if(alp!=null){
	    	BigDecimal latitude = alp.getLatitudeDecimalDegree();
	    	BigDecimal longitude = alp.getLongitudeDecimalDegree();
	    	if(latitude!=null && longitude!=null){
	        	if(alp.getLatitudeDirection()!=null && alp.getLatitudeDirection().charValue()=='S'){
	        		latitude = latitude.negate();
	        	}
	        	if(alp.getLongitudeDirection()!=null && alp.getLongitudeDirection().charValue()=='W'){
	        		longitude = longitude.negate();
	        	}
	        	log.debug("Lat: (" + latitude.toString() + ")  Long: (" + longitude.toString() + ")");
	        	ws = getWatershedByLatLong(latitude.doubleValue(), longitude.doubleValue());
	    	}
		}
		return ws;
	}
	
	public WatershedRef getWatershedByLatLong(double latitude, double longitude){
		String watershed = null;
		WATERS_Zoom_ServiceSoap wzs = getZoomService();
		String responseXml=null;
		try {
			responseXml=wzs.findFeatureByPoint(LAYER_HUC, String.valueOf(latitude), String.valueOf(longitude));
			log.debug(responseXml);
			
			Document doc = null;
			if (responseXml!=null)
				doc = getXMLDocument(responseXml);
			
			if(doc!=null){
				watershed= getNodeValue(doc, "UNIQUE_ID");
			}
		} catch (RemoteException e) {
			log.error("Error Calling find feature by point for watershed", e);
			return null;
		}
		return getWatershedById(watershed);
	}
	
	private WatershedRef getWatershedById(String watershedId) {
		if (watershedId!=null){
			//fetch the county from the county table / state??
			SearchConditions scs = new SearchConditions(new SearchCondition("watershedId", SearchCondition.OPERATOR_EQ, watershedId));
			return (WatershedRef)searchDAO.getSearchObject(WatershedRef.class, scs);
		} else {
			return null;
		}	
	}

	
    public Collection getWatershedListByWatershedAndLocation(Collection watershedIds, String locationId) {
    	ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);	
		SearchConditions scs = new SearchConditions(new SearchCondition("wsl.id.locationId", SearchCondition.OPERATOR_EQ, locationId));
		scs.setCondition(new SearchCondition("watershedId", SearchCondition.OPERATOR_IN, watershedIds));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("watershedLocationRefs", "wsl", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(WatershedRef.class,  scs, sortArray, aliasArray,0,0);
		return results;
    }
	
	
	
	
	
	private Document getXMLDocument(String responseXml){		
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        sb.append(responseXml);
        ByteArrayInputStream bais = new ByteArrayInputStream(sb.toString().getBytes());

		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc =null;
		try {
			builder = factory.newDocumentBuilder();
	        doc = builder.parse(bais);
		} catch (ParserConfigurationException e1) {
			log.error("Parser configuration error in parsing xml response=" + responseXml, e1);
		} catch (SAXException e) {
			log.error("SAX exception in parsing xml response=" + responseXml, e);
		} catch (IOException e) {
			log.error("IO expection in parsing xml response=" + responseXml, e);
		}          
        return doc;		
	}	
	
	private String getSession(String responseXml){		
		Document doc = getXMLDocument(responseXml);
		if(doc!=null){
	        return getNodeValue(doc, "session_id");
		}    
        return null;		
	}	
	
	private String getBBox(String responseXml){		
	    Document doc = getXMLDocument(responseXml);
	    if(doc!=null){
	    	return getNodeValue(doc, "MINX")+","+getNodeValue(doc, "MINY")+","+getNodeValue(doc, "MAXX")+","+getNodeValue(doc, "MAXY");
	    }	        
        return null;		
	}
	
	
	
	private String getNodeValue(Document doc, String nodeName){
		String retStr = null;
		NodeList list = doc.getElementsByTagName(nodeName);
        if(list !=null && list.getLength()>0){
        	Node e = (Node) list.item(0);
            if(e!=null && e.hasChildNodes()){
                NodeList nodes = e.getChildNodes();
                if (nodes!=null){
                	for (int i=0; i<nodes.getLength(); i++) {
                		if(nodes.item(i) != null && nodes.item(i).getNodeType() == Node.TEXT_NODE)
                			return nodes.item(i).getNodeValue();
                	}
                }
            }
        }		
        return retStr;
	}
	
	private Collection getCountyAndStateValue(Document doc){
		ArrayList retArr = new ArrayList();
		NodeList list = doc.getElementsByTagName("Table");
		if(list !=null && list.getLength()>0){
			log.debug("number of nodes" + list.getLength());
	       	for (int i=0; i<list.getLength(); i++) {
	       		Node e = (Node) list.item(i);
	       		if(e.hasChildNodes()){
	       			NodeList nodes = e.getChildNodes();
	       			String county=null;
	       			String state=null;
	       			for (int j=0; j<nodes.getLength(); j++) {
	       				if(nodes.item(j) != null && "COUNTYNAME".equals(nodes.item(j).getNodeName())){
	       					county=getValue(nodes.item(j));
	       				}
	       				if(nodes.item(j) != null && "STATEABBR".equals(nodes.item(j).getNodeName())){
	       					state=getValue(nodes.item(j));
	       				}
	       			}
	       			if(county!=null && state!=null){
	       				retArr.add(county.toUpperCase() + "-" + state.toUpperCase());
	       			}
	       		}
			}
	    }		
        return retArr;
	}
	
	private Collection getConDisAndStateValue(Document doc){
		Map stateMap  = new HashMap();
		ArrayList retArr = new ArrayList();
		NodeList list = doc.getElementsByTagName("Table");
		if(list !=null && list.getLength()>0){
			log.debug("number of nodes" + list.getLength());
	       	for (int i=0; i<list.getLength(); i++) {
	       		Node e = (Node) list.item(i);
	       		if(e.hasChildNodes()){
	       			NodeList nodes = e.getChildNodes();
	       			String county=null;
	       			String state=null;
	       			for (int j=0; j<nodes.getLength(); j++) {
	       				if(nodes.item(j) != null && "DISTRICT".equals(nodes.item(j).getNodeName())){
	       					county=getValue(nodes.item(j));
	       				}
	       				if(nodes.item(j) != null && "STATEABBR".equals(nodes.item(j).getNodeName())){
	       					state=getValue(nodes.item(j));
	       				}
	       			}
	       			if(county!=null && state!=null){
	       				StateRef s = (StateRef)stateMap.get(state);
	       				if(s==null){
	       					s=getStateByLocation(state);
	       					if(s!=null){
	       						stateMap.put(state, s);
	       					}
	       				}
	       				if(s!=null){
		       				retArr.add(s.getStateId()+county);	
	       				}
	       			}
	       		}
			}
	    }		
        return retArr;
	}
	
	private String getValue(Node e){
		String value=null;
		if(e.hasChildNodes()){
            NodeList nodes = e.getChildNodes();
            for (int j=0; j<nodes.getLength(); j++) {
              if(nodes.item(j) != null && nodes.item(j).getNodeType() == Node.TEXT_NODE)
            	  value=nodes.item(j).getNodeValue();
            }
        }
		return value;
	}
	
	private Collection getNodeValues(Document doc, String nodeName){
		ArrayList retArr = new ArrayList();
		NodeList list = doc.getElementsByTagName(nodeName);
	    if(list !=null && list.getLength()>0){
	    	log.debug("number of nodes" + list.getLength());
	       	for (int i=0; i<list.getLength(); i++) {
	       		Node e = (Node) list.item(i);
	            if(e.hasChildNodes()){
	                NodeList nodes = e.getChildNodes();     
	                for (int j=0; j<nodes.getLength(); j++) {
	                  if(nodes.item(j) != null && nodes.item(j).getNodeType() == Node.TEXT_NODE)
	                	  retArr.add(nodes.item(j).getNodeValue());
	                }             
	            }	       			       		
			}
	    }
	    return retArr;		
	}
	
	
	private Document getDocument(String xml) {
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        sb.append(xml);
        ByteArrayInputStream bais = new ByteArrayInputStream(sb.toString().getBytes());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(bais);
	        return doc;
		} catch (ParserConfigurationException e1) {
			log.error(e1);
		} catch (SAXException se) {
			log.error(se);
		} catch (IOException e) {
			log.error(e);
		}
        return null;
	}

	public String getBoundingBox(BigDecimal latitude, BigDecimal longitude){
		String boundingbox=null;
		try {
		    String radiusStr = CWNSProperties.getProperty("webrit.zoom.radius","5");
		    WATERS_Zoom_ServiceSoap zoomService = getZoomService();
	    	String boundingboxStr=zoomService.getPtBBox(latitude.doubleValue()+"", longitude.doubleValue()+"", radiusStr);
	    	boundingbox= getBBox(boundingboxStr);
	    	log.debug("boundingbox:" + boundingbox);
			log.debug("End water Zoom Services call...");
		}catch (Exception e) {
			log.error("Error Calling water zoom service", e);
		}
		return boundingbox;
	}
	
	public String getBoundingBox(String layer, String key, String filter){
		String boundingbox=null;
		try {
		    String radiusStr = CWNSProperties.getProperty("webrit.zoom.radius","5");
		    WATERS_Zoom_ServiceSoap zoomService = getZoomService();
		    Waters_Create_List_ServiceSoap listService = getWaterCreateListService();
			String responseXml=listService.createList(layer,key,filter, "", "");
			Document doc = getXMLDocument(responseXml);
			if(doc!=null){
				String uniqueId= getNodeValue(doc, "UNIQUE_ID");
				if(uniqueId!=null && !"".equals(uniqueId)){
					String boundingboxStr=zoomService.getBBox(uniqueId, layer , radiusStr,"","");
			    	boundingbox= getBBox(boundingboxStr);
			    	log.debug("boundingbox:" + boundingbox);
					log.debug("End water Zoom Services call...");					
				}
			}
		}catch (Exception e) {
			log.error("Error Calling water zoom service", e);
		}
		return boundingbox;
	}
	
	public String getBoundingBox(Long facilityId){
		AbsoluteLocationPoint alp= getFacilityCoordinates(facilityId);
		BigDecimal latitude =null;
		BigDecimal longitude =null;
		if(alp!=null){
			latitude = alp.getLatitudeDecimalDegree();
			longitude = alp.getLongitudeDecimalDegree();			
		}
		if(latitude!=null && longitude!=null){
		    if(alp.getLatitudeDirection()!=null && alp.getLatitudeDirection().charValue()=='S'){
		    	latitude = latitude.negate();
		    }
		    if(alp.getLongitudeDirection()!=null && alp.getLongitudeDirection().charValue()=='W'){
		    	longitude = longitude.negate();
		    }
		    log.debug("Lat for facility: (" + latitude.toString() + ")  Long: (" + longitude.toString() + ")");
		    return getBoundingBox(latitude, longitude);
		}else{
			//try zip
			FacilityAddress fa = getFacilityAddress(facilityId);
			if(fa!=null){
				String zipCode = fa.getZip();
				if(zipCode!=null && "".equals(zipCode)){
					String state= fa.getStateId();
					if(state==null)state="";
					return getBoundingBox(FacilityAddressService.LAYER_ZIP, zipCode, state);
				}
			}
			//try county
			GeographicAreaCounty gac= getPrimaryGeographicAreaCounty(facilityId);
			if(gac!=null){
				CountyRef county=gac.getCountyRef();
				return getBoundingBox(FacilityAddressService.LAYER_COUNTY, county.getName(), county.getLocationId());
			}
			//try state
			Facility f = facilityDAO.findByFacilityId(facilityId.toString());
			String locationId = f.getLocationId();
			return getBoundingBox(FacilityAddressService.LAYER_STATE, locationId, "");
		}
	}
	
	

	public Collection getDatums(boolean excludeUnknown) {
		ArrayList columns = new ArrayList();
		columns.add("horizontalCoordntDatumId");
		columns.add("name");
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		Collection results = searchDAO.getSearchList(HorizontalCoordntDatumRef.class, columns, new SearchConditions(), sortArray);
		return processEntitiesIdValueDatum(results,excludeUnknown);
	}

	public Collection getLocationdesc() {
		ArrayList columns = new ArrayList();
		columns.add("locationDescriptionId");
		columns.add("name");
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		Collection results = searchDAO.getSearchList(LocationDescriptionRef.class, columns, new SearchConditions(), sortArray);
		return processEntitiesNameValue(results);
	}

	public Collection getMethods() {
		ArrayList columns = new ArrayList();
		columns.add("horizontalCllctnMethodId");
		columns.add("name");
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		Collection results = searchDAO.getSearchList(HorizontalCllctnMethodRef.class, columns, new SearchConditions(), sortArray);
		return processEntitiesIdValueMethods(results);
	}
			
	 
	public boolean isInTribalTerritory(Long facilityId) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean areReqFieldsPopulated(AbsoluteLocationPoint absLocationPoint){
		 
	   return false; 	
	}
	
	public boolean areReqFieldsEmpty(AbsoluteLocationPoint absLocationPoint){
		 
		   return false; 	
	}

	public void saveFacilityCoordinates(AbsoluteLocationPoint absoluteLocationPoint, Long facilityId, char tribalTerritory, CurrentUser user) {
		
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		// Get Geographic Area object
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		GeographicArea geographicArea = (GeographicArea)searchDAO.getSearchObject(GeographicArea.class, scs);
		
		if (absoluteLocationPoint != null){
			if (geographicArea == null){
				geographicArea = new GeographicArea();
				Facility f = new Facility();
				f.setFacilityId(facilityId.longValue());
				GeographicAreaTypeRef geographicAreaTypeRef = new GeographicAreaTypeRef();
				geographicAreaTypeRef.setGeographicAreaTypeId(7);
				geographicArea.setFacility(f);
				geographicArea.setGeographicAreaTypeRef(geographicAreaTypeRef);
			}
			geographicArea.setLastUpdateUserid(user.getUserId());
			geographicArea.setLastUpdateTs(new Date());
			geographicArea.setTribeFlag(new Character(tribalTerritory));
			searchDAO.saveObject(geographicArea);
			absoluteLocationPoint.setGeographicArea(geographicArea);
			searchDAO.saveObject(absoluteLocationPoint);
		}
		
	}
	
	public void saveGeographicArea(Long facilityId, char isInTribalTerritory, CurrentUser user) {
        // Get Geographic Area object
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		GeographicArea geographicArea = (GeographicArea)searchDAO.getSearchObject(GeographicArea.class, scs);
		if (geographicArea == null){
			geographicArea = new GeographicArea();
			Facility f = new Facility();
			f.setFacilityId(facilityId.longValue());
			GeographicAreaTypeRef geographicAreaTypeRef = new GeographicAreaTypeRef();
			geographicAreaTypeRef.setGeographicAreaTypeId(7);
			geographicArea.setFacility(f);
			geographicArea.setGeographicAreaTypeRef(geographicAreaTypeRef);
		}
		geographicArea.setLastUpdateUserid(user.getUserId());
		geographicArea.setLastUpdateTs(new Date());
		geographicArea.setTribeFlag(new Character(isInTribalTerritory));
		searchDAO.saveObject(geographicArea);
		
	}
	
	private Collection processEntitiesNameValue(Collection results) {
		Collection col = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			col.add(new Entity(((String)obj[0]),(String)obj[1]));
		}	
		return col;
	}    
	
	private Collection processEntitiesIdValueDatum(Collection results, boolean excludeUnknown) {
		Collection col = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			if (excludeUnknown){
			  if(((Long)obj[0]).intValue()!=12){			
				col.add(new Entity(((Long)obj[0]).toString(),(String)obj[1]));
			  }
			}
			else
				col.add(new Entity(((Long)obj[0]).toString(),(String)obj[1]));
		}	
		return col;
	}
	
	private Collection processEntitiesIdValueMethods(Collection results) {
		Collection col = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			if(((Long)obj[0]).intValue()!=61 && ((Long)obj[0]).intValue()!=62){
				col.add(new Entity(((Long)obj[0]).toString(),(String)obj[1]));				
			}

		}	
		return col;
	}
   
	private void updateFacilityEntryStatus(Long facilityId, CurrentUser user){
		SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ,new Long(2)));
		FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
		if (facilityEntryStatus != null){
		facilityEntryStatus.setLastUpdateTs(new Date());
		facilityEntryStatus.setDataAreaLastUpdateTs(new Date());
		facilityEntryStatus.setLastUpdateUserid(user.getUserId());
		searchDAO.saveObject(facilityEntryStatus);
		}
		
	}
	
	private void performFacilityLevelSave(Long facilityId, CurrentUser user){
//		get the facilityObject
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		UserRole ur = user.getCurrentRole();
		if (f != null){
			f.setLastUpdateTs(new Date());
			f.setLastUpdateUserid(user.getUserId());
		    if(UserServiceImpl.LOCATION_TYPE_ID_STATE.equals(ur.getLocationTypeId())){ //State view
		    	if (f.getReviewStatusRef()!=null){
		    		if (ReviewStatusRefService.STATE_ASSIGNED.equals(f.getReviewStatusRef().getReviewStatusId())){
		    			FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
				    	 FacilityReviewStatusId id = new FacilityReviewStatusId(facilityId.longValue(), ReviewStatusRefService.STATE_IN_PROGRESS, new Date());
				    	 facilityreviewstatus.setId(id);
				    	 facilityreviewstatus.setLastUpdateUserid(user.getUserId());
				    	 searchDAO.saveObject(facilityreviewstatus);
				        ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
				        reviewStatusRef.setReviewStatusId(ReviewStatusRefService.STATE_IN_PROGRESS);
				        f.setReviewStatusRef(reviewStatusRef);
		    		}
		    			
		    	}
		    }
		    else
		    	if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(ur.getLocationTypeId())){ //Local view
		    		if (f.getReviewStatusRef()!=null){
			    		if (ReviewStatusRefService.LOCAL_ASSIGNED.equals(f.getReviewStatusRef().getReviewStatusId())){
			    			FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
					    	 FacilityReviewStatusId id = new FacilityReviewStatusId(facilityId.longValue(), ReviewStatusRefService.LOCAL_IN_PROGRESS, new Date());
					    	 facilityreviewstatus.setId(id);
					    	 facilityreviewstatus.setLastUpdateUserid(user.getUserId());
					    	 searchDAO.saveObject(facilityreviewstatus);
					        ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
					        reviewStatusRef.setReviewStatusId(ReviewStatusRefService.LOCAL_IN_PROGRESS);
					        f.setReviewStatusRef(reviewStatusRef);
			    		}
			    			
			    	}
		    		
		    	}
		    searchDAO.saveObject(f);
		}
		
	}

	public void deleteFacilityCoordinates(Long facilityId, char isInTribalTerritory, CurrentUser user) {
		
		AbsoluteLocationPoint obj = null;
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		obj = (AbsoluteLocationPoint)searchDAO.getSearchObject(AbsoluteLocationPoint.class, scs, aliasArray);
		if (obj != null)
			searchDAO.removeObject(obj);
		this.saveGeographicArea(facilityId, isInTribalTerritory, user);
		
	}

	public Collection getPORByLatitudeAndLongitude(Long facilityId, float latitude, float longitude) {
			
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_NOT_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("latitudeDecimalDegree",SearchCondition.OPERATOR_EQ,new BigDecimal((new Float(latitude)).toString())));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("longitudeDecimalDegree",SearchCondition.OPERATOR_EQ,new BigDecimal((new Float(longitude)).toString())));
		Collection c = searchDAO.getSearchList(AbsoluteLocationPoint.class, new ArrayList(), scs, new ArrayList(), aliasArray);
		return c;
	}

	public Collection getGeographicAreaCountyByFacilityId(Long facilityId){
		/*
		ArrayList columns = new ArrayList();
		columns.add("c.name");
		columns.add("primaryFlag");
		columns.add("c.fipsCode");*/
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("countyRef", "c", AliasCriteria.JOIN_INNER));
		SortCriteria sortCriteria = new SortCriteria("c.name", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		Collection c = searchDAO.getSearchList(GeographicAreaCounty.class, new ArrayList(), scs, sortArray, aliasArray);
		return c;
	}
	/*
	private Collection processCountyList(Collection results){
		Collection facilityCountyHelpers = new ArrayList();
		Iterator iter = results.iterator();
		while (iter.hasNext()) {
			Object[] o = (Object[]) iter.next();
			FacilityCountyHelper FCH = new FacilityCountyHelper((String)o[0], (String)o[2], ((Character)o[1]).charValue());
			facilityCountyHelpers.add(FCH);
		}		
		return facilityCountyHelpers;			
	}*/

	public void saveFacilityCounty(String fipsCode, Long facilityId, String countyName, char primary, String locationId, CurrentUser user) {
		
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		
		SearchConditions scs =  new SearchConditions(new SearchCondition("fipsCode", SearchCondition.OPERATOR_EQ, fipsCode));
		CountyRef county = (CountyRef)searchDAO.getSearchObject(CountyRef.class, scs);
		
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		GeographicArea geographicArea = (GeographicArea)searchDAO.getSearchObject(GeographicArea.class, scs1);
		if (geographicArea == null){
			geographicArea = new GeographicArea();
			// Get facility object			
			Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
			// Get GeographicAreaTypeRef object			
			GeographicAreaTypeRef g = (GeographicAreaTypeRef)searchDAO.getObject(GeographicAreaTypeRef.class, new Long(7));
			geographicArea.setFacility(f);
			geographicArea.setGeographicAreaTypeRef(g);
			geographicArea.setTribeFlag(new Character('N'));
			geographicArea.setLastUpdateTs(new Date());
			geographicArea.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(geographicArea);
		}
		long geographicAreaId = geographicArea.getGeographicAreaId();
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, new Long(geographicAreaId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("primaryFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		GeographicAreaCounty obj1 = (GeographicAreaCounty)searchDAO.getSearchObject(GeographicAreaCounty.class, scs2);
		if ((primary == 'Y') && (obj1 != null)){
			obj1.setPrimaryFlag('N');
			obj1.setLastUpdateTs(new Date());
			obj1.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(obj1);
		}
				
		GeographicAreaCountyId id = new GeographicAreaCountyId(geographicArea.getGeographicAreaId(),county.getCountyId());
		GeographicAreaCounty geographicAreaCounty = new GeographicAreaCounty();
		geographicAreaCounty.setId(id);
		geographicAreaCounty.setCountyRef(county);
		geographicAreaCounty.setGeographicArea(geographicArea);
		geographicAreaCounty.setLastUpdateTs(new Date());
		geographicAreaCounty.setLastUpdateUserid(user.getUserId());
		geographicAreaCounty.setPrimaryFlag(primary);
		searchDAO.saveObject(geographicAreaCounty);
		
	}

	public GeographicAreaCounty getGACByCountyNameAndFacilityId(String fipsCode, String countyName, Long facilityId) {
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("countyRef", "c", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("c.fipsCode",SearchCondition.OPERATOR_EQ,fipsCode));
		GeographicAreaCounty obj = (GeographicAreaCounty)searchDAO.getSearchObject(GeographicAreaCounty.class, scs, aliasArray);
		return obj;
	}

	public GeographicAreaCounty getGeographicAreaCounty(Long geographicAreaId, Long countyId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, geographicAreaId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.countyId",SearchCondition.OPERATOR_EQ,countyId));
		GeographicAreaCounty obj = (GeographicAreaCounty)searchDAO.getSearchObject(GeographicAreaCounty.class, scs);
		return obj;
	}

	public void updateGeographicAreaCounty(Long facilityId, long geographicAreaId, long countyId, char primary, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, new Long(geographicAreaId)));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("primaryFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		GeographicAreaCounty obj1 = (GeographicAreaCounty)searchDAO.getSearchObject(GeographicAreaCounty.class, scs1);
		
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, new Long(geographicAreaId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.countyId",SearchCondition.OPERATOR_EQ,new Long(countyId)));
		GeographicAreaCounty obj2 = (GeographicAreaCounty)searchDAO.getSearchObject(GeographicAreaCounty.class, scs2);
		if(obj2 != null){
			if ((primary == 'Y') && (obj1 != null)){
				obj1.setPrimaryFlag('N');
				obj1.setLastUpdateTs(new Date());
				obj1.setLastUpdateUserid(user.getUserId());
				searchDAO.saveObject(obj1);
			}
		  obj2.setPrimaryFlag(primary);
		  obj2.setLastUpdateTs(new Date());
		  obj2.setLastUpdateUserid(user.getUserId());
		  searchDAO.saveObject(obj2);
		}	
	}

	public void deleteGeographicAreaCounty(Long facilityId, Long geographicAreaId, Long countyId, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		
		GeographicAreaCounty gac = getGeographicAreaCounty(geographicAreaId, countyId);
		if (gac != null)
			searchDAO.removeObject(gac);
		
	}
		
	public void softDeleteGeographicAreaCounty(Long facilityId, Long geographicAreaId, Long countyId, CurrentUser user) {
		GeographicAreaCounty gac = getGeographicAreaCounty(geographicAreaId, countyId);
		if (gac != null){
			char delete = gac.getFeedbackDeleteFlag()=='Y'?'N':'Y';
			gac.setFeedbackDeleteFlag(delete);
			searchDAO.saveObject(gac);	
		}	
	}
	
	public Collection getGeographicAreaConDistrictByFacilityId(Long facilityId){
		ArrayList columns = new ArrayList();
		columns.add("c.congressionalDistrictId");
		columns.add("primaryFlag");
		columns.add("ga.geographicAreaId");
		columns.add("feedbackDeleteFlag");
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("congressionalDistrictRef", "c", AliasCriteria.JOIN_INNER));
		SortCriteria sortCriteria = new SortCriteria("c.congressionalDistrictId", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		Collection results = searchDAO.getSearchList(GeographicAreaCongDistrict.class, columns, scs, sortArray, aliasArray);
		return processConDistrictList(results);
	}
	
	public GeographicAreaCongDistrict getPrimaryGeographicAreaConDistrictByFacilityId(Long facilityId){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("congressionalDistrictRef", "c", AliasCriteria.JOIN_INNER));
		SortCriteria sortCriteria = new SortCriteria("c.congressionalDistrictId", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("primaryFlag",new Character('Y')));
		return (GeographicAreaCongDistrict)searchDAO.getSearchObject(GeographicAreaCongDistrict.class, scs, aliasArray); 
	}	
	
	
    //	 Get Congressional District List
    public Collection getConDistrictListByLocation(String locationId) {
    	ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("congressionalDistrictId", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);
    	ArrayList columns = new ArrayList();
		columns.add("congressionalDistrictId");
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		Collection results = searchDAO.getSearchList(CongressionalDistrictRef.class, columns, scs, sortArray);
		
		Collection conDistricts = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			String conDistrictName="";
			String obj = (String)iter.next();
			String lastTwoDigits=(obj).substring(2);
			if("01".equals(lastTwoDigits))
				conDistrictName = lastTwoDigits.substring(1)+"st Congressional District";
			else if ("02".equals(lastTwoDigits))
			   conDistrictName = lastTwoDigits.substring(1)+"nd Congressional District";
			else if("03".equals(lastTwoDigits))
			  	conDistrictName = lastTwoDigits.substring(1)+"rd Congressional District";
			else conDistrictName = lastTwoDigits.charAt(0)=='0'?lastTwoDigits.substring(1)+"th Congressional District"
					                                                 :lastTwoDigits+"th Congressional District";
			conDistricts.add(new Entity(obj,conDistrictName));
		}	
		return conDistricts;
	}
    
    public Collection getConDistrictListByLocationObjects(String locationId) {
    	ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("congressionalDistrictId", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		Collection results = searchDAO.getSearchList(CongressionalDistrictRef.class, scs, sortArray);
		Collection conDistricts = new ArrayList();
		for (Iterator iter = results.iterator(); iter.hasNext();) {
			CongressionalDistrictRef cdr = (CongressionalDistrictRef) iter.next();
			ConDistrictDisplayHelper ch = new ConDistrictDisplayHelper();
			ch.setCongressionalDistrictId(cdr.getCongressionalDistrictId());
			ch.setLocationId(cdr.getLocationId());
			conDistricts.add(ch);
		}
		return conDistricts;
	}
    
    
    
	
	private Collection processConDistrictList(Collection results){
		Collection conDistrictHelpers = new ArrayList();
		Iterator iter = results.iterator();
		while (iter.hasNext()) {
			Object[] o = (Object[]) iter.next();
			ConDistrictHelper CDH = new ConDistrictHelper();
			CDH.setConDistrictId((String)o[0]);
			CDH.setPrimary(((Character)o[1]).charValue());
			CDH.setGeographicAreaId(((Long)o[2]).longValue());
			CDH.setFeedbackDeleteFlag(((Character)o[3]).charValue());
			String lastTwoDigits=((String)o[0]).substring(2);
			if("01".equals(lastTwoDigits))
				CDH.setConDistrictName(lastTwoDigits.substring(1)+"st Congressional District");
			else if ("02".equals(lastTwoDigits))
			    CDH.setConDistrictName(lastTwoDigits.substring(1)+"nd Congressional District");
			else if("03".equals(lastTwoDigits))
			  	CDH.setConDistrictName(lastTwoDigits.substring(1)+"rd Congressional District");
			else CDH.setConDistrictName(lastTwoDigits.charAt(0)=='0'?lastTwoDigits.substring(1)+"th Congressional District"
					                                                 :lastTwoDigits+"th Congressional District");
			conDistrictHelpers.add(CDH);
		}		
		return conDistrictHelpers;
	}

	public GeographicAreaCongDistrict getGeographicAreaCongDistrict(String conDistrictId, Long facilityId) {
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("congressionalDistrictRef.congressionalDistrictId",SearchCondition.OPERATOR_EQ,conDistrictId));
		GeographicAreaCongDistrict obj = (GeographicAreaCongDistrict)searchDAO.getSearchObject(GeographicAreaCongDistrict.class, scs, aliasArray);
		return obj;
	}

	public void saveGeographicAreaCongDistrict(Long facilityId, String conDistrictId, char primary, String locationId, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		
		SearchConditions scs =  new SearchConditions(new SearchCondition("congressionalDistrictId", SearchCondition.OPERATOR_EQ, conDistrictId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		CongressionalDistrictRef conDistrict = (CongressionalDistrictRef)searchDAO.getSearchObject(CongressionalDistrictRef.class, scs);
		
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		GeographicArea geographicArea = (GeographicArea)searchDAO.getSearchObject(GeographicArea.class, scs1);
		if (geographicArea == null){
			geographicArea = new GeographicArea();
			// Get facility object			
			Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
			// Get GeographicAreaTypeRef object			
			GeographicAreaTypeRef g = (GeographicAreaTypeRef)searchDAO.getObject(GeographicAreaTypeRef.class, new Long(7));
			geographicArea.setFacility(f);
			geographicArea.setGeographicAreaTypeRef(g);
			geographicArea.setTribeFlag(new Character('N'));
			geographicArea.setLastUpdateTs(new Date());
			geographicArea.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(geographicArea);
		}
		long geographicAreaId = geographicArea.getGeographicAreaId();
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, new Long(geographicAreaId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("primaryFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		GeographicAreaCongDistrict obj1 = (GeographicAreaCongDistrict)searchDAO.getSearchObject(GeographicAreaCongDistrict.class, scs2);
		if ((primary == 'Y') && (obj1 != null)){
			obj1.setPrimaryFlag('N');
			obj1.setLastUpdateTs(new Date());
			obj1.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(obj1);
		}
				
		GeographicAreaCongDistrictId id = new GeographicAreaCongDistrictId(geographicArea.getGeographicAreaId(),conDistrictId);
		GeographicAreaCongDistrict geographicAreaCongDistrict = new GeographicAreaCongDistrict();
		geographicAreaCongDistrict.setId(id);
		geographicAreaCongDistrict.setCongressionalDistrictRef(conDistrict);
		geographicAreaCongDistrict.setGeographicArea(geographicArea);
		geographicAreaCongDistrict.setLastUpdateTs(new Date());
		geographicAreaCongDistrict.setLastUpdateUserid(user.getUserId());
		geographicAreaCongDistrict.setPrimaryFlag(primary);
		searchDAO.saveObject(geographicAreaCongDistrict);
		
	}

	public void updateGeographicAreaCongDistrict(Long facilityId, long geographicAreaId, String conDistrictId, char primary, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, new Long(geographicAreaId)));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("primaryFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		GeographicAreaCongDistrict obj1 = (GeographicAreaCongDistrict)searchDAO.getSearchObject(GeographicAreaCongDistrict.class, scs1);
		
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, new Long(geographicAreaId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.congressionalDistrictId",SearchCondition.OPERATOR_EQ,conDistrictId));
		GeographicAreaCongDistrict obj2 = (GeographicAreaCongDistrict)searchDAO.getSearchObject(GeographicAreaCongDistrict.class, scs2);
		if(obj2 != null){
			if ((primary == 'Y') && (obj1 != null)){
				obj1.setPrimaryFlag('N');
				obj1.setLastUpdateTs(new Date());
				obj1.setLastUpdateUserid(user.getUserId());
				searchDAO.saveObject(obj1);
			}
		  obj2.setPrimaryFlag(primary);
		  obj2.setLastUpdateTs(new Date());
		  obj2.setLastUpdateUserid(user.getUserId());
		  searchDAO.saveObject(obj2);
		}
		
	}
	
	public GeographicAreaCongDistrict getGACDByPrimaryKey(String conDistrictId, Long geographicAreaId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("geographicArea.geographicAreaId", SearchCondition.OPERATOR_EQ, geographicAreaId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("congressionalDistrictRef.congressionalDistrictId",SearchCondition.OPERATOR_EQ,conDistrictId));
		GeographicAreaCongDistrict obj = (GeographicAreaCongDistrict)searchDAO.getSearchObject(GeographicAreaCongDistrict.class, scs);
		return obj;
	}

	public void softDeleteGeographicAreaCongDistrict(Long facilityId, Long geographicAreaId, String conDistrictId, CurrentUser user) {
		GeographicAreaCongDistrict gacd = getGACDByPrimaryKey(conDistrictId,geographicAreaId);
		if (gacd != null){
			char delete = gacd.getFeedbackDeleteFlag()=='Y'?'N':'Y';
			gacd.setFeedbackDeleteFlag(delete);
			searchDAO.saveObject(gacd);
		}			
	}	
	
	public void deleteGeographicAreaCongDistrict(Long facilityId, Long geographicAreaId, String conDistrictId, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		
		GeographicAreaCongDistrict gacd = getGACDByPrimaryKey(conDistrictId,geographicAreaId);
		if (gacd != null)
			searchDAO.removeObject(gacd);
		
	}
	
	public Collection getGeographicAreaWatershedByFacilityId(Long facilityId){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("watershedRef", "w", AliasCriteria.JOIN_INNER));
		SortCriteria sortCriteria = new SortCriteria("w.name", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		Collection c = searchDAO.getSearchList(GeographicAreaWatershed.class, new ArrayList(), scs, sortArray, aliasArray);
		return c;
	}

	public GeographicAreaWatershed getGeographicAreaWatershed(String watershedId, Long facilityId) {
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("watershedRef.watershedId",SearchCondition.OPERATOR_EQ,watershedId));
		GeographicAreaWatershed obj = (GeographicAreaWatershed)searchDAO.getSearchObject(GeographicAreaWatershed.class, scs, aliasArray);
		return obj;
	}

	public void saveGeographicAreaWatershed(Long facilityId, String watershedId, char primary, String locationId, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
				
		SearchConditions scs =  new SearchConditions(new SearchCondition("watershedId", SearchCondition.OPERATOR_EQ, watershedId));
		WatershedRef watershed = (WatershedRef)searchDAO.getSearchObject(WatershedRef.class, scs);
		
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		GeographicArea geographicArea = (GeographicArea)searchDAO.getSearchObject(GeographicArea.class, scs1);
		if (geographicArea == null){
			geographicArea = new GeographicArea();
			// Get facility object			
			Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
			// Get GeographicAreaTypeRef object			
			GeographicAreaTypeRef g = (GeographicAreaTypeRef)searchDAO.getObject(GeographicAreaTypeRef.class, new Long(7));
			geographicArea.setFacility(f);
			geographicArea.setGeographicAreaTypeRef(g);
			geographicArea.setTribeFlag(new Character('N'));
			geographicArea.setLastUpdateTs(new Date());
			geographicArea.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(geographicArea);
		}
		long geographicAreaId = geographicArea.getGeographicAreaId();
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, new Long(geographicAreaId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("primaryFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		GeographicAreaWatershed obj1 = (GeographicAreaWatershed)searchDAO.getSearchObject(GeographicAreaWatershed.class, scs2);
		if ((primary == 'Y') && (obj1 != null)){
			obj1.setPrimaryFlag('N');
			obj1.setLastUpdateTs(new Date());
			obj1.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(obj1);
		}
				
		GeographicAreaWatershedId id = new GeographicAreaWatershedId(geographicArea.getGeographicAreaId(),watershedId);
		GeographicAreaWatershed geographicAreaWatershed = new GeographicAreaWatershed();
		geographicAreaWatershed.setId(id);
		geographicAreaWatershed.setWatershedRef(watershed);
		geographicAreaWatershed.setGeographicArea(geographicArea);
		geographicAreaWatershed.setLastUpdateTs(new Date());
		geographicAreaWatershed.setLastUpdateUserid(user.getUserId());
		geographicAreaWatershed.setPrimaryFlag(primary);
		searchDAO.saveObject(geographicAreaWatershed);
		
	}

	public void updateGeographicAreaWatershed(Long facilityId, long geographicAreaId, String watershedId, char primary, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, new Long(geographicAreaId)));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("primaryFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		GeographicAreaWatershed obj1 = (GeographicAreaWatershed)searchDAO.getSearchObject(GeographicAreaWatershed.class, scs1);
		
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("id.geographicAreaId", SearchCondition.OPERATOR_EQ, new Long(geographicAreaId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.watershedId",SearchCondition.OPERATOR_EQ,watershedId));
		GeographicAreaWatershed obj2 = (GeographicAreaWatershed)searchDAO.getSearchObject(GeographicAreaWatershed.class, scs2);
		if(obj2 != null){
			if ((primary == 'Y') && (obj1 != null)){
				obj1.setPrimaryFlag('N');
				obj1.setLastUpdateTs(new Date());
				obj1.setLastUpdateUserid(user.getUserId());
				searchDAO.saveObject(obj1);
			}
		  obj2.setPrimaryFlag(primary);
		  obj2.setLastUpdateTs(new Date());
		  obj2.setLastUpdateUserid(user.getUserId());
		  searchDAO.saveObject(obj2);
		}
		
	}
	
	public GeographicAreaWatershed getGAWatershedByPrimaryKey(String watershedId, Long geographicAreaId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("geographicArea.geographicAreaId", SearchCondition.OPERATOR_EQ, geographicAreaId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("watershedRef.watershedId",SearchCondition.OPERATOR_EQ,watershedId));
		GeographicAreaWatershed obj = (GeographicAreaWatershed)searchDAO.getSearchObject(GeographicAreaWatershed.class, scs);
		return obj;
	}
	
	public void softDeleteGeographicAreaWatershed(Long facilityId, Long geographicAreaId, String watershedId, CurrentUser user) {
		GeographicAreaWatershed gaw = getGAWatershedByPrimaryKey(watershedId,geographicAreaId);
		if (gaw != null){
			char delete = gaw.getFeedbackDeleteFlag()=='Y'?'N':'Y';
			gaw.setFeedbackDeleteFlag(delete);
			searchDAO.saveObject(gaw);
		}
		
	}	
	
	public void deleteGeographicAreaWatershed(Long facilityId, Long geographicAreaId, String watershedId, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		
		GeographicAreaWatershed gaw = getGAWatershedByPrimaryKey(watershedId,geographicAreaId);
		if (gaw != null)
			searchDAO.removeObject(gaw);
		
	}

	public Collection getGeographicAreaBoundaries(Long facilityId) {
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("boundaryRef", "b", AliasCriteria.JOIN_INNER));
		SortCriteria sortCriteria = new SortCriteria("b.name", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		Collection c = searchDAO.getSearchList(GeographicAreaBoundary.class, new ArrayList(), scs, sortArray, aliasArray);
		return c;
	}
	
	public GeographicAreaBoundary getGeographicAreaBoundary(Long boundaryId, Long facilityId) {
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("boundaryRef.boundaryId",SearchCondition.OPERATOR_EQ,boundaryId));
		GeographicAreaBoundary obj = (GeographicAreaBoundary)searchDAO.getSearchObject(GeographicAreaBoundary.class, scs, aliasArray);
		return obj;
	}
	
	public GeographicAreaBoundary getGABoundaryByPrimaryKey(Long boundaryId, Long geographicAreaId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("geographicArea.geographicAreaId", SearchCondition.OPERATOR_EQ, geographicAreaId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("boundaryRef.boundaryId",SearchCondition.OPERATOR_EQ,boundaryId));
		GeographicAreaBoundary obj = (GeographicAreaBoundary)searchDAO.getSearchObject(GeographicAreaBoundary.class, scs);
		return obj;
	}
	
	
	public void softDeleteGeographicAreaBoundary(Long facilityId, Long geographicAreaId, Long boundaryId, CurrentUser user) {
		GeographicAreaBoundary gab = getGABoundaryByPrimaryKey(boundaryId,geographicAreaId);
		if (gab != null){
			char delete = gab.getFeedbackDeleteFlag()=='Y'?'N':'Y';
			gab.setFeedbackDeleteFlag(delete);
			searchDAO.saveObject(gab);
		}
		
	}
	
	public void deleteGeographicAreaBoundary(Long facilityId, Long geographicAreaId, Long boundaryId, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
		
		GeographicAreaBoundary gab = getGABoundaryByPrimaryKey(boundaryId,geographicAreaId);
		if (gab != null)
			searchDAO.removeObject(gab);
		
	}
	
	public void saveGeographicAreaBoundary(Long facilityId, Long boundaryId, CurrentUser user) {
		//updateFacilityEntryStatus(facilityId,user);
		//performFacilityLevelSave(facilityId,user);
				
		SearchConditions scs =  new SearchConditions(new SearchCondition("boundaryId", SearchCondition.OPERATOR_EQ, boundaryId));
		BoundaryRef boundaryRef = (BoundaryRef)searchDAO.getSearchObject(BoundaryRef.class, scs);
		
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		GeographicArea geographicArea = (GeographicArea)searchDAO.getSearchObject(GeographicArea.class, scs1);
		if (geographicArea == null){
			geographicArea = new GeographicArea();
			// Get facility object			
			Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
			// Get GeographicAreaTypeRef object			
			GeographicAreaTypeRef g = (GeographicAreaTypeRef)searchDAO.getObject(GeographicAreaTypeRef.class, new Long(7));
			geographicArea.setFacility(f);
			geographicArea.setGeographicAreaTypeRef(g);
			geographicArea.setTribeFlag(new Character('N'));
			geographicArea.setLastUpdateTs(new Date());
			geographicArea.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(geographicArea);
		}
										
		GeographicAreaBoundaryId id = new GeographicAreaBoundaryId(geographicArea.getGeographicAreaId(),boundaryId.longValue());
		GeographicAreaBoundary geographicAreaBoundary = new GeographicAreaBoundary();
		geographicAreaBoundary.setId(id);
		geographicAreaBoundary.setBoundaryRef(boundaryRef);
		geographicAreaBoundary.setGeographicArea(geographicArea);
		geographicAreaBoundary.setLastUpdateTs(new Date());
		geographicAreaBoundary.setLastUpdateUserid(user.getUserId());
		searchDAO.saveObject(geographicAreaBoundary);
		
	}
	
    //  Get Boundary List
    public Collection getBoundaryListByLocation(String locationId) {
    	ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("bt.name", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);	
		ArrayList columns = new ArrayList();
		columns.add("boundaryId");
		columns.add("name");
		columns.add("bt.name");
		SearchConditions scs = new SearchConditions(new SearchCondition("bl.id.locationId", SearchCondition.OPERATOR_EQ, locationId));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("boundaryLocationRefs", "bl", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("boundaryTypeRef", "bt", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(BoundaryRef.class, columns, scs, sortArray, aliasArray);
		Collection boundaries = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			boundaries.add(new BoundaryListHelper(((Long)obj[0]).longValue(),(String)obj[2],(String)obj[1]));
		}	
		return boundaries;
    }	
    
    
    public ConDistrictHelper getPrimaryGeographicAreaCongDistrict(Long facilityId){
    	Collection gacd = getGeographicAreaConDistrictByFacilityId(facilityId);
    	for (Iterator iter = gacd.iterator(); iter.hasNext();) {
    		ConDistrictHelper cd = (ConDistrictHelper) iter.next();
			if(cd.getPrimary()=='Y'){
				return cd;
			}			
		}
    	return null;
    }
    
    public GeographicAreaCounty getPrimaryGeographicAreaCounty(Long facilityId){
    	Collection gac =  getGeographicAreaCountyByFacilityId(facilityId);
    	for (Iterator iter = gac.iterator(); iter.hasNext();) {
			GeographicAreaCounty c = (GeographicAreaCounty) iter.next();
			if(c.getPrimaryFlag()=='Y'){
				return c;
			}
			
		}
    	return null;
    }
    
    public GeographicAreaWatershed getPrimaryGeographicAreaWatershed(Long facilityId){
    	Collection gaw = getGeographicAreaWatershedByFacilityId(facilityId);
    	for (Iterator iter = gaw.iterator(); iter.hasNext();) {
			GeographicAreaWatershed w = (GeographicAreaWatershed) iter.next();
			if(w.getPrimaryFlag()=='Y'){
				return w;
			}			
		}
    	return null;
    }

	public String getWebRITSessionInformation(String sessionId) {
		String sessionLatLonXML=null;
		try {
			String registrationKey = CWNSProperties.getProperty("webrit.registration.key");	
			Waters_Indexing_ServiceSoap waterIndexingService=getWaterIndexingService();
	    	sessionLatLonXML =waterIndexingService.retrieveSketches(sessionId, registrationKey);
			log.debug("End water Indexing Services retrieve session lat and log call ..." + sessionLatLonXML);
		}catch (Exception e) {
			log.error("Error retrieving lat and lon", e);
			throw new ApplicationException("Error retrieving lat and lon", e);
		}		
		return sessionLatLonXML;
	}

	public void invalidateWebRITSession(String sessionId) {
		try {
			String registrationKey = CWNSProperties.getProperty("webrit.registration.key");	
			Waters_Indexing_ServiceSoap waterIndexingService=getWaterIndexingService();
	    	String clearSessionStatus =waterIndexingService.deleteSession(sessionId, registrationKey);
			log.debug("End water Indexing Services clear session call..." + clearSessionStatus);
		}catch (Exception e) {
			log.error("Error Calling water index service clear session", e);
			throw new ApplicationException("Error Calling water index service clear session", e);
		}
	}
	
	private Waters_Indexing_ServiceSoap getWaterIndexingService(){
    	String indexWebServicesEndPoint = CWNSProperties.getProperty("webrit.index.webservices.endpoint");
    	log.debug("indexWebServicesEndPoint: (" + indexWebServicesEndPoint + ")");
    	//if https
    	String protocol = CWNSProperties.getProperty("webrit.index.service.protocol", "http");
    	if ("https".equalsIgnoreCase(protocol)){
    		setCertificateProperties(); 
        }
    	Waters_Indexing_ServiceLocator factory = new Waters_Indexing_ServiceLocator();
    	factory.setWaters_Indexing_ServiceSoapEndpointAddress(indexWebServicesEndPoint);
    	Waters_Indexing_ServiceSoap waterIndexingService=null;
		try {
			waterIndexingService = factory.getWaters_Indexing_ServiceSoap();
		} catch (ServiceException e) {
			log.error("Error Calling water index service", e);
			throw new ApplicationException("Error Calling water index service", e);
		}
		return waterIndexingService;
	}
	
	private void setCertificateProperties(){
		//Use Sun's reference implementation of a URL handler for the "https" URL protocol type.
		String pkgs=CWNSProperties.getProperty("ssl.protocol.handler.pkgs","com.sun.net.ssl.internal.www.protocol");
        System.setProperty("java.protocol.handler.pkgs",pkgs);
        
        //Location of the client keystore.
        String key_ssl_store=CWNSProperties.getProperty("ssl.trustStore","");
        if(!"".equals(key_ssl_store)){
        	System.setProperty("javax.net.ssl.trustStore", key_ssl_store);	
        }
        String key_ssl_store_password=CWNSProperties.getProperty("ssl.trustStorePassword","");
        if(!"".equals(key_ssl_store_password)){
        	System.setProperty("javax.net.ssl.trustStorePassword", key_ssl_store_password);	
        }
        
        // dynamically register sun's ssl provider
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	}
	
	private Waters_Create_List_ServiceSoap getWaterCreateListService(){	
    	String listWebServicesEndPoint = CWNSProperties.getProperty("webrit.list.webservices.endpoint");
    	log.debug("listWebServicesEndPoint: (" + listWebServicesEndPoint + ")");
    	String protocol = CWNSProperties.getProperty("webrit.list.service.protocol", "http");
    	if ("https".equalsIgnoreCase(protocol)){
    		setCertificateProperties(); 
        }    	
    	Waters_Create_List_ServiceLocator factory = new Waters_Create_List_ServiceLocator();
    	factory.setWaters_Create_List_ServiceSoapEndpointAddress(listWebServicesEndPoint);
    	Waters_Create_List_ServiceSoap waterListService=null;
		try {
			waterListService = ((Waters_Create_List_Service)factory).getWaters_Create_List_ServiceSoap();
		} catch (ServiceException e) {
			log.error("Error Calling water index service", e);
			throw new ApplicationException("Error Calling water index service", e);
		}
		return waterListService;
	}
	
	private WATERS_Zoom_ServiceSoap getZoomService(){
		WATERS_Zoom_ServiceSoap zoomService =null;
    	String zoomWebServicesEndPoint = CWNSProperties.getProperty("webrit.zoom.webservices.endpoint");
    	log.debug("Zoom WebServicesEndPoint: (" + zoomWebServicesEndPoint + ")");
    	String protocol = CWNSProperties.getProperty("webrit.zoom.service.protocol", "http");
    	if ("https".equalsIgnoreCase(protocol)){
    		setCertificateProperties(); 
        }
    	WATERS_Zoom_ServiceLocator factory = new WATERS_Zoom_ServiceLocator();
    	factory.setWATERS_Zoom_ServiceSoapEndpointAddress(zoomWebServicesEndPoint);
		try {
	    	zoomService =factory.getWATERS_Zoom_ServiceSoap();
		} catch (ServiceException e) {
			log.error("Error Calling water index service", e);
			throw new ApplicationException("Error Calling water index service", e);
		}
		return zoomService;
	}
	

	public void saveWebRITSession(String sessionId, Long facilityId) {
		//		remove any old features associated with this facility
		removeFeatures(facilityId);
		
        //		save new features
		try { 
			String registrationKey = CWNSProperties.getProperty("webrit.registration.key");	
			Waters_Indexing_ServiceSoap waterIndexingService=getWaterIndexingService();
			Facility f = facilityDAO.findByFacilityId(facilityId.toString());
			String cwnsNbr = (f.getVersionCode()=='F')?f.getCwnsNbr()+"-Feedback":f.getCwnsNbr();
	    	String status =waterIndexingService.savePendingIndexFeatures(sessionId,cwnsNbr,registrationKey);
	    	//save new features
	    	boolean success = saveFeatureIds(status, facilityId);	    	
			log.debug("End water Indexing Services saving session call..." + status);
		}catch (Exception e) {
			log.error("Error Calling water index service saving session", e);
			throw new ApplicationException("Error Calling water index service saving  session", e);
		}	
	}
	
	public boolean saveFeatureIds(String xmlRes, Long facilityId) {
		//get all the featureIds
		String featureIds = getFeatureIdsFromXML(xmlRes, "Feature", "id");
		log.debug("FeatureIds:"+featureIds);
		//update the absolute point with the feature Ids
		if(featureIds !=null){
			AbsoluteLocationPoint alp= getFacilityCoordinates(facilityId);
			if(alp!=null){
				alp.setFeatureId(featureIds);
				searchDAO.saveObject(alp);
			}
			return true;
		}
		return false;
	}

	public void removeFeatures(Long facilityId){
		Collection featureIds = getFeatureIds(facilityId);
		if(featureIds!=null && featureIds.size()>0){
			String registrationKey = CWNSProperties.getProperty("webrit.registration.key");
			Waters_Indexing_ServiceSoap waterIndexingService=getWaterIndexingService();
			try {
				for (Iterator iter = featureIds.iterator(); iter.hasNext();) {
					String featureId = (String) iter.next();
					String status = waterIndexingService.removePendingIndexFeature(featureId, registrationKey);
					log.debug("remove status--"+status);
					log.debug("Removed Feature" + featureId);
				}
			} catch (RemoteException e) {
				log.error("Error removing feature" + featureIds, e);
				//throw new ApplicationException("Error removing feature" + featureIds, e);
			}			
		}				
	}
	
	public Collection getFeatureIds(Long facilityId){
		AbsoluteLocationPoint alp= getFacilityCoordinates(facilityId);
		if(alp==null)return null;
		String featureStr= alp.getFeatureId();
		if(featureStr==null)return new ArrayList();
		String[] features = featureStr.split(",");
		if(features==null){
			ArrayList arr = new ArrayList();
			arr.add(featureStr);
			return arr;
		}		
		return Arrays.asList(features);
	}
	
	private String getFeatureIdsFromXML(String xml, String nodeName, String attributeName){
		Document doc = getDocument(xml);
		String retStr = null;
		NodeList list = doc.getElementsByTagName(nodeName);
        if(list !=null && list.getLength()>0){
        	for (int i=0; i<list.getLength(); i++) {
				Element e = (Element) list.item(i);
				if(retStr == null){
					retStr = ((Element)e).getAttribute(attributeName);
				}else{
					retStr= retStr+ "," + ((Element)e).getAttribute(attributeName);
				}
			}
        }
        return retStr;
	}
	
	public void saveAllToWebRIT(){
        //		get a list of all the facilities
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		//sort by column
		ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("facilityId", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);
		SearchConditions scs = new SearchConditions(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character('S')));
		Collection c = searchDAO.getSearchList(Facility.class,columns,scs,sortArray, new ArrayList(), 35873, 10000);
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			Long facilityId = (Long) iter.next();
			//get the lat/long
			java.lang.System.out.println("Processing facility " + facilityId.longValue());
			
			AbsoluteLocationPoint absoluteLocationPoint = getFacilityCoordinates(facilityId);
			if(absoluteLocationPoint!=null && absoluteLocationPoint.getLatitudeDirection()!=null && absoluteLocationPoint.getLatitudeDecimalDegree()!=null &&
					absoluteLocationPoint.getLongitudeDirection()!=null && absoluteLocationPoint.getLongitudeDecimalDegree()!=null &&
					absoluteLocationPoint.getLatitudeDecimalDegree().floatValue()!=0.0 && absoluteLocationPoint.getLongitudeDecimalDegree().floatValue()!=0.0){
				String latitude = (absoluteLocationPoint.getLatitudeDirection().charValue()=='N'?"+":"-")+absoluteLocationPoint.getLatitudeDecimalDegree().floatValue();
				String longitude = (absoluteLocationPoint.getLongitudeDirection().charValue()=='E'?"+":"-")+absoluteLocationPoint.getLongitudeDecimalDegree().floatValue();
				removeFeatures(facilityId);
				saveWebRITSession(latitude, longitude, facilityId);
			}			
			searchDAO.flushAndClearCache();
		}
		//get the coordinates 
		//make sure they are not null
		
	}
	
	
	
	public void saveWebRITSession(String latitude, String longitude, Long facilityId){
		try { 
			String registrationKey = CWNSProperties.getProperty("webrit.registration.key");	
			Waters_Indexing_ServiceSoap waterIndexingService=getWaterIndexingService();
			String sessionId = getWebritSession();
			String xmlRes = waterIndexingService.savePointToSession(sessionId, latitude+","+longitude, registrationKey);
			log.debug("Reponse of save point to session:"+ xmlRes);
			Facility f = facilityDAO.findByFacilityId(facilityId.toString());
			String cwnsNbr = (f.getVersionCode()=='F')?f.getCwnsNbr()+"-Feedback":f.getCwnsNbr();
			String status =waterIndexingService.savePendingIndexFeatures(sessionId,cwnsNbr,registrationKey);
	    	log.debug("status:"+ status);
	    	//save new features
	    	boolean success = saveFeatureIds(status, facilityId);	    	
			log.debug("End water Indexing Services saving session call..." + status);
		}catch (Exception e) {
			log.error("Error Calling water index service saving session", e);
			throw new ApplicationException("Error Calling water index service saving  session", e);
		}	
		
	}

	public void updateMeasureDate(Long facilityId, String measureDate, CurrentUser user) {
		AbsoluteLocationPoint absoluteLocationPoint = getFacilityCoordinates(facilityId);
		if (absoluteLocationPoint!=null){
			absoluteLocationPoint.setMeasurementDate(new Date(measureDate));
			absoluteLocationPoint.setLastUpdateTs(new Date());
			absoluteLocationPoint.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(absoluteLocationPoint);
		}
		
	}


}
