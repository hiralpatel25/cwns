package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurvevalidation.CCValidationManager;
import gov.epa.owm.mtb.cwns.dao.AccessLevelRefDAO;
import gov.epa.owm.mtb.cwns.dao.CwnsInfoLocationRefDAO;
import gov.epa.owm.mtb.cwns.dao.CwnsUserLocationFacilityDAO;
import gov.epa.owm.mtb.cwns.dao.CwnsUserLocatnAccessLevelDAO;
import gov.epa.owm.mtb.cwns.dao.CwnsUserSettingDAO;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.dao.ReviewCommentsDAO;
import gov.epa.owm.mtb.cwns.dao.ReviewStatusRefDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.facility.FacilityListDisplayAction;
import gov.epa.owm.mtb.cwns.facility.FacilityListHelper;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.AdministrativeMessageRef;
import gov.epa.owm.mtb.cwns.model.Announcement;
import gov.epa.owm.mtb.cwns.model.AnnouncementId;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.CountyRef;
import gov.epa.owm.mtb.cwns.model.CwnsInfoLocationRef;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocation;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocationFacility;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocatnAccessLevel;
import gov.epa.owm.mtb.cwns.model.CwnsUserSetting;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataArea;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataAreaId;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityFlow;
import gov.epa.owm.mtb.cwns.model.FacilityOverallTypeRef;
import gov.epa.owm.mtb.cwns.model.FacilityPermit;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatusId;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.model.System;
import gov.epa.owm.mtb.cwns.model.WatershedRef;
import gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper;
import gov.epa.owm.mtb.cwns.service.AnnouncementService;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author rlingam
 *
 */
public class FacilityServiceImpl extends CWNSService implements FacilityService {	

	private FacilityDAO facilityDAO;
	private ReviewStatusRefDAO reviewStatusRefDAO;
	private SearchDAO searchDAO;
	private CwnsUserSettingDAO cwnsUserSettingDAO;
	private FacilityTypeService facilityTypeService;
	
	
	private CwnsInfoLocationRefDAO cwnsInfoLocationRefDAO;
	public void setCwnsInfoLocationRefDAO(
			CwnsInfoLocationRefDAO cwnsInfoLocationRefDAO) {
		this.cwnsInfoLocationRefDAO = cwnsInfoLocationRefDAO;
	}
	
	private ReviewCommentsDAO reviewCommentsDAO;
	public void setReviewCommentsDAO(ReviewCommentsDAO reviewCommentsDAO) {
		this.reviewCommentsDAO = reviewCommentsDAO;
	}
	
	private CwnsUserLocationFacilityDAO cwnsUserLocationFacilityDAO;
	public void setCwnsUserLocationFacilityDAO(
			CwnsUserLocationFacilityDAO cwnsUserLocationFacilityDAO) {
		this.cwnsUserLocationFacilityDAO = cwnsUserLocationFacilityDAO;
	}

	private CwnsUserLocatnAccessLevelDAO cwnsUserLocatnAccessLevelDAO;
	public void setCwnsUserLocatnAccessLevelDAO(
			CwnsUserLocatnAccessLevelDAO cwnsUserLocatnAccessLevelDAO) {
		this.cwnsUserLocatnAccessLevelDAO = cwnsUserLocatnAccessLevelDAO;
	}
	
	private UserService userService;	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}	
    
	//set the reviewStatusRef service
    private ReviewStatusRefService reviewStatusRefService;
    public void setReviewStatusRefService(ReviewStatusRefService rsts){
    	reviewStatusRefService = rsts;
    }
    
    private FESManager fesManager;
    public void setFesManager(FESManager fesm){
    	fesManager=fesm;
    } 
    
    private CostCurveService costCurveService;
	public void setCostCurveService(CostCurveService costCurveService) {
		this.costCurveService = costCurveService;
	}	    
    
	private CCValidationManager ccValidationManager;
	public void setCcValidationManager(CCValidationManager cvm) {
		this.ccValidationManager = cvm;
	}	
    
	private PopulationService populationService;
	public void setPopulationService(PopulationService populationService){
		this.populationService=populationService;
	}
	

	public void setCwnsUserSettingDAO(CwnsUserSettingDAO dao) {
		this.cwnsUserSettingDAO = dao;
	}

	public void setFacilityDAO(FacilityDAO dao){
		facilityDAO = dao;
	}	
	
	public void setReviewStatusRefDAO(ReviewStatusRefDAO dao){
		reviewStatusRefDAO = dao;
	}
	
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	
	public void setFacilityTypeService (FacilityTypeService fts){
		facilityTypeService = fts;
	}
	
	public List findFacility(Facility searchFacility, int firstResult, int maxResults){
		return facilityDAO.findFacility(searchFacility, firstResult, maxResults);
	}
/*	
	public Collection getFacilityDereferencedSearchResults(Facility searchFacility, int startIndex, int maxResultSize){
		
		Collection facilities = findFacility(searchFacility, startIndex, maxResultSize);		
		Collection facilityListHelpers = new ArrayList();
		Iterator iter = facilities.iterator();
		while (iter.hasNext()) {
			Facility f = (Facility) iter.next();
			FacilityListHelper FLH = new FacilityListHelper(
											new Long(f.getFacilityId()).toString(),
											f.getCwnsNbr(),
					                        f.getName(),
					                        f.getReviewStatusRef().getName());

			facilityListHelpers.add(FLH);
		}		
		return facilityListHelpers;	
	}
*/
	public int countFacilities(Facility searchFacility) {
		return facilityDAO.countFacilities(searchFacility);
	}

	
	   /**
     * Change the ReviewStatusRef for one or more Facilities.
     * @param facilities 
     * 			a Set containing facilityIds whoes 
     *          status should be changed.
     * @param newStatusId 
     * 			A REVIEW_STATUS_TYPE.REVIEW_STATUS_TYPE_ID value. This value will 
     * 			be used to find a ReviewStatusRef object. The Facility objects will be
     * 			updated with this object.
     * @return
     * 			A Set of facilityIds whoes status could not be changed because they failed 
     * 			business rules validation.  
     */

	public Set statusChange(Set facilities, String newStatusId) {
		
    	Set facilityIdsNotUpdated = new HashSet();
    	
    	ReviewStatusRef revStatus =  reviewStatusRefService.findByReviewStatusRefId(newStatusId);
    	Iterator iter = facilities.iterator();
    	while (iter.hasNext()) {
    		String facilityId = (String)iter.next();
			Facility facility = facilityDAO.findByFacilityId(facilityId);
			
    		if (validateStatusChange(facility, newStatusId)) {
    			facility.setReviewStatusRef(revStatus);
    		} else {
    			facilityIdsNotUpdated.add(facilityId);
    		}
    	}
    	return facilityIdsNotUpdated;
    }

	public Facility findByFacilityNumber(String facNum) {
		return facilityDAO.findByFacilityNumber(facNum);
	}

	public Facility findByFacilityId(String facilityId) {
		return facilityDAO.findByFacilityId(facilityId);
	}

/*	
	public Collection getFacilities(String searchKeyword, int startIndex, int maxResultSize, String locationId) {
		//construct the simple search
		//keyword conditions
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs = getKeyWordSearchCondition(searchKeyword, locationId);
		Collection results = searchDAO.getSearchList(Facility.class, scs, sortArray, startIndex, maxResultSize);
		return processFacilityList(results);
	} 

	public Collection processFacilityList(Collection facilities){
		Collection facilityListHelpers = new ArrayList();
		Iterator iter = facilities.iterator();
		while (iter.hasNext()) {
			Facility f = (Facility) iter.next();
			FacilityListHelper FLH = new FacilityListHelper(
											new Long(f.getFacilityId()).toString(),
											f.getCwnsNbr(),
					                        f.getName(),
					                        f.getReviewStatusRef().getName());

			facilityListHelpers.add(FLH);
		}		
		return facilityListHelpers;			
	}
*/	

	public int countFacilities(String searchKeyword, String locationId) {
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs = getKeyWordSearchCondition(searchKeyword, locationId);
		int count = searchDAO.getCount(Facility.class, scs);
		return count;
	}

	public Collection getFacilityIdsByKeyword(String searchKeyword, String locationId) {
		//construct the simple search
		//keyword conditions
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		
		AliasCriteria alias = new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_LEFT);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		
		SearchConditions scs = getKeyWordSearchCondition(searchKeyword, locationId);
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, sortArray, aliasArray);
		return processFacilityNbrList(results);
	}

	private Collection processFacilityNbrList(Collection results) {
		ArrayList arr = new ArrayList();
		for (Iterator iter = results.iterator(); iter.hasNext();) {
			Long nbr = (Long)iter.next();
			arr.add(nbr);
		}
		return arr;
	}



	public Collection getFacilityByLocation(String locationId) {
		ArrayList columns = new ArrayList();
		columns.add("facilityId");		
		SearchConditions scs = getLocationSearchCondition(locationId);
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs);
		return processFacilityNbrList(results);		
	}
	
	/**
	 * Given a Collection of Facility Ids return a Collection of Facility objects based on the 
	 * criteria in the search object
	 */
	public Collection getFacilityObjects(Collection facilityIds, Search search) {
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_IN, facilityIds));
		Collection results = searchDAO.getSearchList(Facility.class, scs, sortArray, search.getStartIndex(), search.getMaxResults());
		return results;
		
	}
	
	public Collection getFacilities(Collection facilityList, Search search) {
		Collection facilities = facilityDAO.getFacilitiesByList(facilityList, search.getSortCriteria(), search.getStartIndex(), search.getMaxResults());
		return processDisplayFacilityList(facilities);
	}	
	
	public Collection processDisplayFacilityList(Collection facilities){
		Collection facilityListHelpers = new ArrayList();
		Iterator iter = facilities.iterator();
		while (iter.hasNext()) {
			Object[] obj = (Object[])iter.next();
			FacilityListHelper FLH = new FacilityListHelper(
											((Long)obj[0]).toString(),
											(String)obj[1],
											(String)obj[2],
											(String)obj[3],
											(String)obj[4],
											(String)obj[5],
											(obj[6] == null) ? "" :(String)obj[6],"");

			facilityListHelpers.add(FLH);
		}		
		return facilityListHelpers;			
	}	
	
	public Search getDefaultSearch(CurrentUser currentUser, SortCriteria sortCriteria, int startIndex, int maxResults){
		String locationId  	  = currentUser.getCurrentRole().getLocationId(); 
		String locationTypeId = currentUser.getCurrentRole().getLocationTypeId();
		Search s = new Search();	
		// Set name and desc
		s.setName(FacilityListDisplayAction.QUERY_TYPE_DEFAULT);
		s.setDescription(FacilityListDisplayAction.QUERY_TYPE_DEFAULT_DESC);

		// Set Search Conditions 
		s.setSearchConditions(getDefaultSearchConditions(locationTypeId,locationId));
		
		// Set Sort Criteria		
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		s.setSortCriteria(sortArray);
		
		// Set the number of results
		s.setStartIndex(startIndex);
		s.setMaxResults(maxResults);
		return s;
	}

	
	public Search getImportSearch(String key, SortCriteria sortCriteria, int startIndex, int maxResults){
		Search s = new Search();	
		//set name and desc
		s.setName(FacilityListDisplayAction.QUERY_TYPE_IMPORT);
		s.setDescription(FacilityListDisplayAction.QUERY_TYPE_IMPORT_DESC);
		//set search Conditions
		s.setSearchConditions(getImportSearchCondition(key));
		//set sort Cr		
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		s.setSortCriteria(sortArray);		
		//set the number of results
		s.setStartIndex(startIndex);
		s.setMaxResults(maxResults);
		return s;
	}
	
	
	private SearchConditions getDefaultSearchConditions(String locationTypeId,String locationId) {
		SearchConditions scs = null;
		
		if (UserService.LOCATION_TYPE_ID_REGIONAL.equals(locationTypeId)) {
			Collection states = getStateIdsForRegion(locationId);
			scs =  new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_IN, states));
			scs.setCondition(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));		
			
		} else if (UserService.LOCATION_TYPE_ID_STATE.equals(locationTypeId) ||
				   UserService.LOCATION_TYPE_ID_LOCAL.equals(locationTypeId)) {
			
			scs =  new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
			scs.setCondition(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));		
			
		} else { 
           // Nothing to do because Federal users get to see everything !
			scs =  new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, "DC"));
//			scs =  new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
//			scs.setCondition(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));		
		}
		return scs;
	}

	//default search condition
	private SearchConditions getLocationSearchCondition(String locationId){
		SearchConditions scs3 =  new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs3.setCondition(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));		
		return scs3;
	}
	
	// Import search conditions
	public SearchConditions getImportSearchCondition(String key){
		// Get list of facilityIds from the CwnsUserSetting table
		Collection facilityIds = getCwnsUserSetting(key,CwnsUserSettingDAO.IMPORT_LIST_TYPE);
		SearchConditions scs3 =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_IN, facilityIds));
		scs3.setCondition(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));		
		return scs3;
	}

	/**
	 * Return a Collection of facilityIds obtained by "and"ing the search 
	 * criteria.
	 */
	public Collection getFacilitiesIds(Search search) {
		return getFacilitiesIds(search, true);
	}
	
	/**
	 * Return a Collection of facilityIds based on the Search object passed in.
	 * @param search
	 * @param andResults
	 * @return+
	 * 		If (andResults = true) return an intersection of facilityIds based on the search criteria. 
	 * 		If (andResults = false) return a union of facilityIds based on the search criteria. 
	 */
	public Collection getFacilitiesIds(Search search, boolean andResults) {
		ArrayList columns = new ArrayList();
		Collection results;
		if(FacilityListDisplayAction.QUERY_TYPE_ADVANCE.equals(search.getName())){
			Collection coll = search.getQueryParameters();
			Object[] o= coll.toArray();
			Map parameters = (Map)o[0];
			results = getFacilitiesByAdvanceSearch(parameters,andResults);
		}else{
			if(!search.isNamedQuery()){
				columns.add("facilityId");
				Collection alias = search.getAlias();
				if(alias==null){
					alias= new ArrayList();
				}
				results = searchDAO.getSearchList(Facility.class, columns, search.getSearchConditions(), new ArrayList(),alias, 0, 0);
			}else{
				results = searchDAO.getNamedQueryList(search.getQueryName(), search.getQueryParameters());
			}
			return processFacilityNbrList(results);
		}
		return results;	
	}	

	/**
	 * 
	 * @param search
	 * @return
	 */
	public Collection getUserIds(Search search) {
		ArrayList  columns = new ArrayList();
		Collection results = new ArrayList();

		if(!search.isNamedQuery()){
			columns.add("cwnsUserId");
			Collection alias = search.getAlias();
			if(alias==null){
				alias= new ArrayList();
			}
			results = searchDAO.getSearchList(CwnsUser.class, columns, search.getSearchConditions(), new ArrayList(),alias, 0, 0);
//		}else{
//			results = searchDAO.getNamedQueryList(search.getQueryName(), search.getQueryParameters());
		}
		return processFacilityNbrList(results);
	}	
	
	private Collection getFacilitiesByAdvanceSearch(Map parameters, boolean andResults) {		
		//get locationId first
		String locationId= (String)parameters.get("state");
		ArrayList results = new ArrayList();
		
		String[] reviewStatus  = (String[])parameters.get("reviewStatus");
	    if(reviewStatus!=null && reviewStatus.length > 0){
	    	Collection rs = removeEmptyStrings(reviewStatus);
	    	if(!rs.isEmpty()){
		    	Collection c = getFacilityIdByReviewStatus(locationId, rs);
		    	if(c.isEmpty() && andResults)return c;
		    	results.add(processFacilityNbrList(c));	    		
	    	}
	    }
	    
	    String facName = (String)parameters.get("facName");	
	    if(facName!=null && !"".equals(facName)){
	    	Collection c = getFacilityIdByFacilityName(locationId, facName);
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }
	    
	    String[] overAllType  = (String[]) parameters.get("overAllType");
	    if(overAllType!=null && overAllType.length>0){
	    	Collection ovt = convertStringToLong(overAllType);
	    	if(!ovt.isEmpty()){
		    	Collection c = getFacilityIdByOverallType(locationId, ovt);
		    	if(c.isEmpty() && andResults)return c;
		    	results.add(processFacilityNbrList(c));	    		
	    	}
	    }

	    String authority = (String)parameters.get("authority");	
	    if(authority!=null && !"".equals(authority)){
	    	Collection c = getFacilityIdByAuthority(locationId, authority);
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }
	    
	    String sysname = (String)parameters.get("sysname");	
	    if(sysname!=null && !"".equals(sysname)){
	    	Collection c = getFacilityIdBySystemName(locationId, sysname);
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }

	    String county = (String)parameters.get("county");	
	    if(county!=null && !"".equals(county)){
	    	Collection c = getFacilityIdByCounty(locationId, county);
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }
	    
	    String watershed = (String)parameters.get("watershed");	
	    if(watershed!=null && !"".equals(watershed)){
	    	Collection c = getFacilityIdByWaterShed(locationId, watershed);
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }	    
	    
	    Date olderdoc = (Date)parameters.get("olderdoc");	
	    if(olderdoc!=null){
	    	Collection c = getFacilityIdByDocOlder(locationId, olderdoc);
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }
	    
	    Date notChanged = (Date)parameters.get("notChanged");	
	    if(notChanged!=null){
	    	Collection c = getFacilityIdByNotChanged(locationId, notChanged);
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }
	    
	    String permitNumber = (String)parameters.get("permitNumber");	
	    if(permitNumber!=null && !"".equals(permitNumber)){
	    	Collection c = getFacilityIdByPermitNumber(locationId, permitNumber);
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }

	    String errorStatus = (String)parameters.get("errorStatus");	
	    if(errorStatus!=null && !"".equals(errorStatus)){
	    	Collection c = getFacilityIdByErrorStatus(locationId, errorStatus);
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }
	    	    
	    Object[] needs = (Object[])parameters.get("need");	
	    if(needs!=null){
	    	Collection c = facilityDAO.getFacilityIdByNeeds(locationId,(String)needs[1], ((Integer)needs[0]).intValue());
	    	if(c.isEmpty() && andResults)return c;
	    	results.add(processFacilityNbrList(c));
	    }
	    
	    if(results.size()==0){
	    	Collection c =  getFacilityIdByLocation(locationId);
	    	results.add(processFacilityNbrList(c));	    	
	    }
	    	
	    if(results.size()==1){
	    	return (Collection)results.get(0);
	    }
	    
	    Collection facilityIds;
	    if (andResults) {
	    	facilityIds =  processAndResults(results);
	    } else { 
	    	facilityIds =  processOrResults(results);
	    }
	    return facilityIds;
	}
	
	
	/**
	 * Return a Collection of FacilityIds obtained by "and"ing each 
	 * of the Collections contained in the ArrayList. 
	 * @param results
	 * @return
	 */
	private Collection processAndResults(ArrayList results) {
		if(results.isEmpty())return new ArrayList();
		int icount=0;
		Collection col1=null;
	    for (Iterator iter = results.iterator(); iter.hasNext();) {
	    	Collection col = (Collection) iter.next();
	    	if(icount==0){
	    		col1=col;
	    	}else{
		    	col1= CollectionUtils.intersection(col1, col);	    		
	    	}
	    	icount++;
	    }	
		return col1;		
	}
	
	/**
	 * Return a Collection of FacilityIds obtained by "or"ing each 
	 * of the Collections contained in the ArrayList. 
	 * @param results
	 * @return
	 */
	private Collection processOrResults(ArrayList results) {
		if(results.isEmpty())return new ArrayList();
		Set facilityIds = new HashSet();
	    for (Iterator iter = results.iterator(); iter.hasNext();) {
	    	Collection col = (Collection) iter.next();
	    	facilityIds.addAll(col);
	    }	
		return facilityIds;		
	}

	public Collection getFacilityIdByReviewStatus(String locationId, Collection reviewStatus) {
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		SearchConditions scs =  new SearchConditions(new SearchCondition("reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_IN, reviewStatus));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs);
		return results;
	}
	
	private Collection getFacilityIdByFacilityName(String locationId, String facilityName) {
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		SearchConditions scs =  new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_LIKE, facilityName));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs);
		return results;
	}
	
	private Collection getFacilityIdByOverallType(String locationId, Collection overallType) {
		//select nt.* from facility f, facility_nature fn, nature_type nt
		//where f.facility_id=fn.facility_id and fn.nature_type_id = nt.nature_type_id and nt.nature_overall_type_id in (3)
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		SearchConditions scs =  new SearchConditions(new SearchCondition("novt.facilityOverallTypeId", SearchCondition.OPERATOR_IN, overallType));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityTypes", "fnt", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fnt.facilityTypeRef", "nt", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("nt.facilityOverallTypeRef", "novt", AliasCriteria.JOIN_INNER));
		
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, new ArrayList(), aliasArray );
		return results;
	}	
	
	private Collection getFacilityIdByAuthority(String locationId, String authority) {
		//select p.* from point_of_contact p, facility_point_of_contact fp 
		//where p.point_of_contact_id = fp.point_of_contact_id and lower(authority_Name) like '%town%'
		
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		SearchConditions scs =  new SearchConditions(new SearchCondition("pc.authorityName", SearchCondition.OPERATOR_LIKE, authority));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityPointOfContacts", "fpc", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fpc.pointOfContact", "pc", AliasCriteria.JOIN_INNER));		
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, new ArrayList(), aliasArray );
		return results;
	}		
	
	private Collection getFacilityIdBySystemName(String locationId, String systemName) {
		//Select f.facility_id, s.name from facility f, system s 
		//where f.system_id = s.system_id and lower(s.name) like '%county%'
		//system.name
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		SearchConditions scs =  new SearchConditions(new SearchCondition("s.name", SearchCondition.OPERATOR_LIKE, systemName));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("system", "s", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, new ArrayList(), aliasArray );
		return results;
	}		
	
	
	private Collection getFacilityIdByCounty(String locationId, String county) {
		ArrayList columns = new ArrayList();
		columns.add("facilityId");				
		SearchConditions scs =  new SearchConditions(new SearchCondition("c.name", SearchCondition.OPERATOR_LIKE, county));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("gat.name", SearchCondition.OPERATOR_LIKE, "Point of Record"));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicAreas", "ga", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ga.geographicAreaTypeRef", "gat", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ga.geographicAreaCounties", "gac", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("gac.countyRef", "c", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, new ArrayList(), aliasArray );
		return results;
	}
	
	private Collection getFacilityIdByWaterShed(String locationId, String watershed) {
		ArrayList columns = new ArrayList();
		columns.add("facilityId");				
		SearchConditions scs =  new SearchConditions(new SearchCondition("w.name", SearchCondition.OPERATOR_LIKE, watershed));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("gat.name", SearchCondition.OPERATOR_LIKE, "Point of Record"));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicAreas", "ga", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ga.geographicAreaTypeRef", "gat", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ga.geographicAreaWatersheds", "gaw", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("gaw.watershedRef", "w", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, new ArrayList(), aliasArray );
		return results;
	}
	
	private Collection getFacilityIdByDocOlder(String locationId, Date publishDate) {
		//select * from facility f, facility_Document fd, document d 
		//where f.facility_Id = fd.facility_Id and d.document_id = fd.document_id
	    //and d.published_date < to_date('10-2-2002','MM-DD-YYYY')		
		ArrayList columns = new ArrayList();
		columns.add("facilityId");				
		SearchConditions scs =  new SearchConditions(new SearchCondition("d.publishedDate", SearchCondition.OPERATOR_LT, publishDate));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocuments", "fd", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fd.document", "d", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, new ArrayList(), aliasArray );
		return results;
	}
	
	private Collection getFacilityIdByNotChanged(String locationId, Date notChangedDate) {
		//select * from facility where last_update_ts > to_date('10-2-2005','MM-DD-YYYY')
		ArrayList columns = new ArrayList();
		columns.add("facilityId");				
		SearchConditions scs =  new SearchConditions(new SearchCondition("lastUpdateTs", SearchCondition.OPERATOR_LT, notChangedDate));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs);
		return results;
	}	
	
	private Collection getFacilityIdByPermitNumber(String locationId, String permitNumber) {
		//select fp.facility_id, p.* 
		//from facility_permit fp, permit p 
		//where fp.permit_id = p.permit_id and lower(permit_nbr) like '%ok%';

		ArrayList columns = new ArrayList();
		columns.add("facilityId");				
		SearchConditions scs =  new SearchConditions(new SearchCondition("p.permitNbr", SearchCondition.OPERATOR_LIKE, permitNumber));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityPermits", "fp", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fp.permit", "p", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, new ArrayList(), aliasArray );
		return results;
	}
	
	private Collection getFacilityIdByErrorStatus(String locationId, String errorStatus) {
		//select * from facility_ENTRY_STATUS where error_flag='Y'
      if("Y".equals(errorStatus)){
		ArrayList columns = new ArrayList();
		columns.add("facilityId");				
		SearchConditions scs =  new SearchConditions(new SearchCondition("fes.errorFlag", SearchCondition.OPERATOR_LIKE, errorStatus));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityEntryStatuses", "fes", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs, new ArrayList(), aliasArray );
		Set sh =new HashSet(results);
		return sh;
      }else{
    	  return facilityDAO.getFacilityIdWithNoErrors(locationId);
      }	
	}
	
	private Collection getFacilityIdByLocation(String locationId){
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		SearchConditions scs = getLocationSearchCondition(locationId);
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs);		
		return results;		
	}
	
	
	
	
	private Collection convertStringToLong(String[] s){
		ArrayList arr = new ArrayList();
		for (int i = 0; i < s.length; i++) {
			if(!"".equals(s[i])){
				arr.add(new Long(Integer.parseInt(s[i])));				
			}
		}
		return arr;		
	}
	
	private Collection removeEmptyStrings(String[] s){
		ArrayList arr = new ArrayList();
		for (int i = 0; i < s.length; i++) {
			if(!"".equals(s[i])){
				arr.add(s[i]);				
			}
		}
		return arr;	
	}

	public int getFacilitiesCount(Search search) {
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		//TODO: Handle Alias
		return searchDAO.getCount(Facility.class, search.getSearchConditions());
	}

	public Search getSimpleSearch(String keyword, String locationId, SortCriteria sortCriteria, int startIndex, int maxResults) {
		Search s = new Search();	
		//set name and desc
		s.setName(FacilityListDisplayAction.QUERY_TYPE_SIMPLE);
		s.setDescription(FacilityListDisplayAction.QUERY_TYPE_SIMPLE_DESC+ keyword);
		if(!"".equals(keyword)){
			s.setNamedQuery(true);
			s.setQueryName("keywordsearch");
			ArrayList parameters = new ArrayList();
			parameters.add("%"+ keyword.toLowerCase() + "%");
			parameters.add("%"+ keyword.toLowerCase() + "%");
			parameters.add("%"+ keyword.toLowerCase() + "%");
			parameters.add("%"+ keyword.toLowerCase() + "%");
			parameters.add("%"+ keyword.toLowerCase() + "%");
			parameters.add(locationId.toUpperCase());
			s.setQueryParameters(parameters);
			//s.setSearchConditions(getKeyWordSearchCondition(keyword, locationId));
		}else{
			s.setSearchConditions(getLocationSearchCondition(locationId));
		}		
		//set search Conditions
		//set sort Cr		
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		s.setSortCriteria(sortArray);		
		
		//set the number of results
		s.setStartIndex(startIndex);
		s.setMaxResults(maxResults);
		return s;
	}

	//simple search condition
	public SearchConditions getKeyWordSearchCondition(String searchKeyword, String locationId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_LIKE, searchKeyword));
		scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("description", SearchCondition.OPERATOR_LIKE, searchKeyword));
		scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("cwnsNbr", SearchCondition.OPERATOR_LIKE, searchKeyword));
		scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("r.name", SearchCondition.OPERATOR_LIKE, searchKeyword));
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("c.name", SearchCondition.OPERATOR_LIKE, searchKeyword));		
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("gat.name", SearchCondition.OPERATOR_LIKE, "Point of Record"));
		scs.setCondition(SearchCondition.OPERATOR_OR, scs2);		
		SearchConditions scs3 =  new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs3.setCondition(SearchCondition.OPERATOR_AND, scs);
		scs3.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
		return scs3;
	}
	
	/**
	 * This method will use business rules to validate whether a facility's status can be changed
	 * to the new status. 
	 * @param facility
	 * 			The facility to examen
	 * @param newStatusId
	 * 	
	 * @return
	 * 			True if it's OK to change the facility to the new status, false otherwise.
	 */
	private boolean validateStatusChange(Facility facility, String newStatusId) {
		boolean valid = true;
		/*
		//TODO: Real validation code must be added here

		if (newStatusId.equals(facility.getReviewStatusRef().getReviewStatusId())) {
			// (for testing purposes only) return false if the facility already has the
			// requested new status.
			valid = false;
		}
		*/
		// Real validation code
		if(newStatusId.equalsIgnoreCase(FacilityDAO.LOCAL_ASSIGNED)){
			if (facility!=null && !ReviewStatusRefService.STATE_APPLIED.equalsIgnoreCase(facility.getReviewStatusRef().getReviewStatusId()))
				valid = false;
		}
		
		
		return valid;
	}
	
	/**
	 * Update MY_FACILITIES in the database
	 */
	public void updateCwnsUserSetting(String userAndRole,Set masterSelectedList) {
		cwnsUserSettingDAO.updateCwnsUserSetting(userAndRole, masterSelectedList);
	}
	
	/**
	 * Get a list of facilityIds from the MY_FACILITIES table 
	 * @param 	userAndRole
	 * @return
	 * 			A Collection of facilityIds
	 */
	public Collection getCwnsUserSetting(String userAndRole,String listType) {
		return cwnsUserSettingDAO.getCwnsUserSetting(userAndRole, listType);
	}
	
    // Get County List
    public Collection getCountyListByLocation(String locationId) {
    	ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);	
		//ArrayList columns = new ArrayList();
		//columns.add("fipsCode");
		//columns.add("name");
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));;
		Collection results = searchDAO.getSearchList(CountyRef.class, scs, sortArray);
		
		//Collection counties = new ArrayList();
		//Iterator iter = results.iterator();	
		//while (iter.hasNext()){
		//	Object[] obj = (Object[])iter.next();
		//	counties.add(new Entity(((String)obj[0]),(String)obj[1]));
		//}	
		return results;
		
		
	}
    
    //  Get System Names
    public Collection getSystemNameListByLocation(String locationId) {
    	ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);
    	ArrayList columns = new ArrayList();
		columns.add("systemId");
		columns.add("name");
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));;
		Collection results = searchDAO.getSearchList(System.class, columns, scs, sortArray);
		Collection sysnames = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			sysnames.add(new Entity(((Long)obj[0]).toString(),(String)obj[1]));
		}	
		return sysnames;
    }	
		
    //  Get Watershed List
    public Collection getWatershedListByLocation(String locationId) {
    	ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);	
		ArrayList columns = new ArrayList();
		columns.add("watershedId");
		columns.add("name");
		SearchConditions scs = new SearchConditions(new SearchCondition("wsl.id.locationId", SearchCondition.OPERATOR_EQ, locationId));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("watershedLocationRefs", "wsl", AliasCriteria.JOIN_INNER));		
		Collection results = searchDAO.getSearchList(WatershedRef.class, columns, scs, sortArray, aliasArray);
		Collection watersheds = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			watersheds.add(new Entity(((Long)obj[0]).toString(),(String)obj[1]));
		}	
		return watersheds;
    }
    
    public Collection getWatershedListByLocationObjects(String locationId) {
    	ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);	
		SearchConditions scs = new SearchConditions(new SearchCondition("wsl.id.locationId", SearchCondition.OPERATOR_EQ, locationId));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("watershedLocationRefs", "wsl", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(WatershedRef.class,  scs, sortArray, aliasArray,0,0);
		return results;
    }
    
    public Collection getOverAllNatureTypes(){
		ArrayList columns = new ArrayList();
		columns.add("facilityOverallTypeId");
		columns.add("name");
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		Collection results = searchDAO.getSearchList(FacilityOverallTypeRef.class, columns, new SearchConditions(), sortArray);
		return processEntitiesIdValue(results);	
    }

    /**
     * 
     * @param name
     * @return
     */
    public String [] getFacilityOverallTypeIds(String name ){
    	
		ArrayList columns = new ArrayList();
		columns.add("facilityOverallTypeId");

    	SearchConditions scs = new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_LIKE, name));
		Collection ids = searchDAO.getSearchList(FacilityOverallTypeRef.class, columns, scs);
    	if (ids.size() == 0) {
    		return null;
    	}
    	
		Collection results = new ArrayList();
		Iterator iter = ids.iterator();
		while (iter.hasNext()) {
			Long id = (Long) iter.next();
			results.add(id.toString());
		}
		return (String []) results.toArray(new String[0]);	
    }

    public Collection getReviewStatusRefs(){
		ArrayList columns = new ArrayList();
		columns.add("reviewStatusId");
		columns.add("name");
		//SearchConditions scs = new SearchConditions(new SearchCondition("sortSequence", SearchCondition.OPERATOR_LT, new Integer(50)));
		SortCriteria sortCriteria = new SortCriteria("sortSequence", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		Collection results = searchDAO.getSearchList(ReviewStatusRef.class, columns, new SearchConditions(), sortArray);
		return processEntitiesNameValue(results);	
    }

    public Collection getStates(){
		ArrayList columns = new ArrayList();
		columns.add("locationId");
		columns.add("name");
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		Collection results = searchDAO.getSearchList(StateRef.class, columns, new SearchConditions(), sortArray);
		return processEntitiesNameValue(results);	
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
    
	private Collection processEntitiesIdValue(Collection results) {
		Collection col = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			col.add(new Entity(((Long)obj[0]).toString(),(String)obj[1]));
		}	
		return col;
	}


	public Search getFacilitySelectAdvanceSearch(Map queryProperties, String locationId, SortCriteria sortCriteria, int startIndex, int maxResults) {
		Search search = getAdvanceSearch(queryProperties, locationId, sortCriteria, startIndex, maxResults);
    	SearchConditions scs = search.getSearchConditions();
    	
    	if (scs != null) {
    		scs.setCondition(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
    		//scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_NOT_IN, ar));
    	}
    	else {
			scs =  new SearchConditions(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, "S"));
			//scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_NOT_IN, ar));
    	}
        //search.setSearchConditions(scs);
		return search;
	}
	
	public Search getAdvanceSearch(Map queryProperties, String locationId, SortCriteria sortCriteria, int startIndex, int maxResults) {
		Search s = new Search();	
		//set name and desc
		s.setName(FacilityListDisplayAction.QUERY_TYPE_ADVANCE);
		s.setDescription(FacilityListDisplayAction.QUERY_TYPE_ADVANCE_DESC);
		ArrayList parameters = new ArrayList();
		//for now used the existing queryparameters
		parameters.add(queryProperties);
		s.setQueryParameters(parameters);
		
		//set search Conditions
		//set sort Cr		
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		s.setSortCriteria(sortArray);		
		
		//set the number of results
		s.setStartIndex(startIndex);
		s.setMaxResults(maxResults);
		return s;
	}
    /*
	// Get facility by facilityid
	public FacilityListHelper getFacility(String facNum){
		FacilityListHelper FLH = null;
		  Facility f = (Facility)facilityDAO.findByFacilityId(facNum);
		  if (f != null){
			  FLH =  new FacilityListHelper(
					        new Long(f.getFacilityId()).toString(),
					        f.getCwnsNbr(),
					        f.getDescription(),
					        f.getReviewStatusType().getReviewStatusTypeId(),
					        f.getSystem().getName());
			  FLH.setDescription(f.getDescription());
					       
		  }
		return FLH;
	}
	*/
	// Get facility comments 
	public Collection getFacilityComments(String facNum) {
		/*
		ArrayList columns = new ArrayList();
		columns.add("description");
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("d.name", SearchCondition.OPERATOR_EQ, "Facility"));
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("dataAreaType", "d", AliasCriteria.JOIN_INNER));
		Collection results = searchDAO.getSearchList(FacilityComment.class, columns, scs, new ArrayList(), aliasArray);
		return results;*/
		return facilityDAO.getFacilityComments(facNum);
	}
    /*
	public Facility getFeedbackVersionOfFacility(String facNum) {
		String cwnsNbr = getCWNSNbrByFacilityId(facNum);
		SearchConditions scs = new SearchConditions(new SearchCondition("cwnsNbr", SearchCondition.OPERATOR_EQ, cwnsNbr));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character('F')));
		Collection results = searchDAO.getSearchList(Facility.class, scs);
		Facility fs = null;
		for (Iterator iter = results.iterator(); iter.hasNext();) {
			fs = (Facility) iter.next();
		}
		return fs;
	}

	public String getCWNSNbrByFacilityId(String facilityId){
		String cwnsNbr = "";
		ArrayList columns = new ArrayList();
		columns.add("cwnsNbr");
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(1144312)));
		
		Collection results = searchDAO.getSearchList(Facility.class, columns, scs);
		if (results.iterator().hasNext()){
			Object[] obj = (Object[])results.iterator().next();
			cwnsNbr = (String)obj[0];
		}
		return cwnsNbr;
	}
	*/
	public Facility getFeedbackVersionOfFacility(String facNum) {
		Collection results = facilityDAO.getFeedbackVersionOfFacility(facNum);
		Facility fs = null;
		for (Iterator iter = results.iterator(); iter.hasNext();) {
			fs = (Facility) iter.next();
		}
		return fs;
	}	

	public Facility saveFacilityInfo(String sFacilityId, String facilityName, String description, String sysName, String ownerCode, char militaryFlag, CurrentUser user, String cwnsNbr, String locationId,
			                             String tmdlFlg, String sourceWaterProtectionFlg) {
		/*		
		SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(sFacilityId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ,new Long(1)));
		FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
		if (facilityEntryStatus != null){
		  facilityEntryStatus.setLastUpdateTs(new Date());
		  facilityEntryStatus.setDataAreaLastUpdateTs(new Date());
		  facilityEntryStatus.setLastUpdateUserid(user);
		  searchDAO.saveObject(facilityEntryStatus);
		}
		*/
		boolean isNewFacility = false;
		Facility facility = new Facility();
		if("0".equals((sFacilityId).trim())){
			facility.setCwnsNbr(cwnsNbr);
			facility.setLocationId(locationId);
			ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
			reviewStatusRef.setReviewStatusId("SIP");
			facility.setReviewStatusRef(reviewStatusRef);
			//new facilitites can only created by the state user
			facility.setVersionCode(FacilityService.VERSION_SURVEY);
			facility.setSmallCommunityExceptionFlag('N');
			facility.setLocalUserDoesUnitProcFlag('N');
			facility.setLocalUserDoesUtilMgmtFlag('N');			
			isNewFacility=true;
		}else{
			facility =facilityDAO.findByFacilityId(sFacilityId);
		}	
		if (facility != null) {
			facility.setDescription(description);
			facility.setName(facilityName);
			facility.setMilitaryFlag(militaryFlag);
			if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
			  facility.setOwnerCode(ownerCode);
			  facility.setSourceWaterProtectionFlag("Y".equalsIgnoreCase(sourceWaterProtectionFlg)?new Character('Y'):new Character('N'));
			  facility.setTmdlFlag("Y".equalsIgnoreCase(tmdlFlg)?new Character('Y'):new Character('N'));
			}
			setSystemName(sysName, facility, user);
			facility.setLastUpdateTs(new Date());
			facility.setLastUpdateUserid(user.getUserId());			
			/*
			ReviewStatusRef rsr = facility.getReviewStatusRef();
			if ("SAS".equalsIgnoreCase(rsr.getReviewStatusId())) {
				FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
				FacilityReviewStatusId id = new FacilityReviewStatusId(
						new Long(sFacilityId).longValue(), "SIP", new Date());
				facilityreviewstatus.setId(id);
				facilityreviewstatus.setLastUpdateUserid(user);
				facilityDAO.saveObject(facilityreviewstatus);
				ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
				reviewStatusRef.setReviewStatusId("SIP");
				facility.setReviewStatusRef(reviewStatusRef);
			}*/
			facilityDAO.saveObject(facility);
			//for a new facility save facility review record
			if(isNewFacility){
				log.debug("New facility Id:" +facility.getFacilityId());
				FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
				FacilityReviewStatusId id = new FacilityReviewStatusId(facility.getFacilityId(), "SIP", new Date());
				facilityreviewstatus.setId(id);
				facilityreviewstatus.setLastUpdateUserid(user.getUserId());
				facilityDAO.saveObject(facilityreviewstatus);
				//TODO:add dataAreas
			}
			
		}else{
			//error condition			
			log.error("Unable to save facility information for facility:" + cwnsNbr);
		}
		return facility;
		
	}
	
	private void setSystemName(String sysName, Facility facility, CurrentUser user){
		if (!("".equalsIgnoreCase(sysName))){
			  if (facility.getSystem() != null){
				  if (!(facility.getSystem().getName().equalsIgnoreCase(sysName))){
					  SearchConditions scs = new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_EQ, sysName));
					  scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("locationId",SearchCondition.OPERATOR_EQ,facility.getLocationId()));
					  System obj = (System)searchDAO.getSearchObject(System.class, scs); 
					  if (obj == null){
						  obj = new System();
						  obj.setName(sysName);
						  obj.setLastUpdateTs(new Date());
						  obj.setLastUpdateUserid(user.getUserId());
						  obj.setLocationId(facility.getLocationId());
						  facilityDAO.saveObject(obj);
					  }
					  SearchConditions scs1 = new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_EQ, facility.getSystem().getName()));
					  scs1.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("locationId",SearchCondition.OPERATOR_EQ,facility.getLocationId()));
					  System obj1 = (System)searchDAO.getSearchObject(System.class, scs1); 
					  if (obj1 != null){
						 if(obj1.getFacilities().size()==1){	
						     facilityDAO.removeObject(System.class, new Long(obj1.getSystemId()));
						 }
					  }	 
					  facility.setSystem(obj);
						  
				  }
			  }
			  else{
				  SearchConditions scs = new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_EQ, sysName));
				  scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("locationId",SearchCondition.OPERATOR_EQ,facility.getLocationId()));
				  System obj = (System)searchDAO.getSearchObject(System.class, scs); 
				  if (obj == null){
					  obj = new System();
					  obj.setName(sysName);
					  obj.setLastUpdateTs(new Date());
					  obj.setLastUpdateUserid(user.getUserId());
					  obj.setLocationId(facility.getLocationId());
					  facilityDAO.saveObject(obj);
				  }
				  facility.setSystem(obj);
			  }
		  }
		  else{
			  if (facility.getSystem() != null){
				  String name = facility.getSystem().getName();
				  	  SearchConditions scs = new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_EQ, name));
					  scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("locationId",SearchCondition.OPERATOR_EQ,facility.getLocationId()));
				  	  System obj = (System)searchDAO.getSearchObject(System.class, scs); 
					  if (obj != null){
						 if(obj.getFacilities().size()==1){	
						     facility.setSystem(null);
							 facilityDAO.removeObject(System.class, new Long(obj.getSystemId()));
						 }
						 else
							 facility.setSystem(null);
					  }
			  }
		  }
	}
	
	
	/**
	 * Determines if a user has previlages to update a facility
	 * @param user  user object
	 * @param facilityId  facility id
	 * @return boolean indicating whether a user can update the facility data
	 */	
	public boolean isUpdatable(CurrentUser user, Long facilityId){
		return isUpdatable(user, facilityId, null);
	}
	
	/**
	 * Determines if a user has previlages to update a facility
	 * @param user  user object
	 * @param facilityId  facility id
	 * @param dataAreaId  data area Id
	 * @return boolean indicating whether a user can update the facility data
	 */		
	public boolean isUpdatable(CurrentUser user, Long facilityId, Long dataAreaId){
		boolean updatable = false;
		//get the facilityObject
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		if(f==null){
			return false;
		}
		CwnsInfoLocationRef cwnsInfo = (CwnsInfoLocationRef)searchDAO.getSearchObject(CwnsInfoLocationRef.class, new SearchConditions(new SearchCondition("locationId", f.getLocationId())));
		if(cwnsInfo==null){
			log.error("Unable to find cwns info for locationId: " + f.getLocationId());
			return false;
		}
		//get the review status
		ReviewStatusRef rst= f.getReviewStatusRef();
		if(rst ==null){
			return false;
		}		
		UserRole ur = user.getCurrentRole();
		if(ur==null){
			return false;
		}
		
		Long sFacId= facilityId;
		if('F' == f.getVersionCode()){
			Facility sFacility = getFacilityByCwnsNbr(f.getCwnsNbr());
			if(sFacility!=null){
				sFacId= new Long(sFacility.getFacilityId());	
			}
		}
		
		//check if the facility is authorised on survey facility Id
		if(!isUserAssociatedWithFacility(user, ur, sFacId)){
			return false;
		}
		
		if('F' == f.getVersionCode()){ //Local
			if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(ur.getLocationTypeId())){
				if(user.isAuth(CurrentUser.FACILITY_FEEDBACK)){
					if(ReviewStatusRefService.LOCAL_ASSIGNED.equals(rst.getReviewStatusId()) ||
					    ReviewStatusRefService.LOCAL_IN_PROGRESS.equals(rst.getReviewStatusId()) ){
						updatable = true;
					}
				}					
			}
		}else{
		    //applies to all users for unit process and util management dataareas
			//Important:  the version code should be survey for unit process and util management
			//make sure the facility is in the same location as user
			if(ur.getLocationId().equals(f.getLocationId()) || ur.getLocationId().equals(FacilityService.FACILITY_LOCATION_HEADQUARTER)){
				if(dataAreaId!=null && (dataAreaId.longValue()==FacilityService.DATA_AREA_UNIT_PROCESS.longValue() ||
							dataAreaId.longValue()==FacilityService.DATA_AREA_UTIL_MANAGEMENT.longValue())){
						if(user.isAuth(CurrentUser.FACILITY_UPDATE) || user.isAuth(CurrentUser.FACILITY_UPDATE_PARTIAL)){
							if(!isFacilityLargeCommunity(facilityId)){
								if(!ReviewStatusRefService.DELETED.equals(rst.getReviewStatusId())){
									updatable = true;
								}
							}else{
								if(!(ReviewStatusRefService.FEDERAL_ACCEPTED.equals(rst.getReviewStatusId()) ||
									       ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION.equals(rst.getReviewStatusId()) ||
									       ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED.equals(rst.getReviewStatusId()))){
									updatable = true;
									
								}					
							}
						}	
			    }else{
				    	if(UserServiceImpl.LOCATION_TYPE_ID_STATE.equals(ur.getLocationTypeId()) ||  
					 			   UserServiceImpl.LOCATION_TYPE_ID_FEDERAL.equals(ur.getLocationTypeId())){
				    		if(user.isAuth(CurrentUser.FACILITY_UPDATE)){
				    			if(ReviewStatusRefService.STATE_IN_PROGRESS.equals(rst.getReviewStatusId()) ||
						 			       ReviewStatusRefService.STATE_ASSIGNED.equals(rst.getReviewStatusId()) ||
						 			       ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equals(rst.getReviewStatusId())){
						 					Date now = new Date();
						 					if(now.before(cwnsInfo.getDataEntryEndDate())){
						 				   		updatable = true;
						 				   	}  
						 		}
					 		}
					 	}									
				}				
			}
		}		
		return updatable;
	}
	
	
	
	public boolean isUserAssociatedWithFacility(CurrentUser user, UserRole ur,Long facilityId){
		//get the facility restriction information
		CwnsUserLocation cul = userService.getCwnsUserLocation(user.getUserId(), user.getCurrentRole().getLocationTypeId(), user.getCurrentRole().getLocationId());
		if(cul.getLimitedFacilitiesFlag()!='Y'){
			return true;
		}
		//get the list of facilities
		Collection restrictedFacilities = userService.getUserLocationRestrictedFacilitiesId (facilityId, user.getUserId(), ur.getLocationTypeId(), ur.getLocationId());
		//check if the facility is listed in the list of
		if(restrictedFacilities.contains(facilityId)){
			return true;
		}
		return false; 		
	}
	
	/**
	 * Determines if the facility is a large community
	 * Criteria: Facility_flow.present_flow_msr >= 10 where flow_id_fk = 5
	 * @param facilityId
	 * @return if large community returns true else false
	 */
	public boolean isFacilityLargeCommunity(Long facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.flowTypeId", SearchCondition.OPERATOR_EQ, FlowService.FLOW_TYPE_TOTAL));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		FacilityFlow ff = (FacilityFlow)searchDAO.getSearchObject(FacilityFlow.class, scs);
		if(ff!=null && ff.getPresentFlowMsr()!=null &&  ff.getPresentFlowMsr().intValue()>10){
			return true;
		}
		return false;
	}
	
	public boolean isFacilitySmallCommunity(Long facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.flowTypeId", SearchCondition.OPERATOR_EQ, FlowService.FLOW_TYPE_TOTAL));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		FacilityFlow ff = (FacilityFlow)searchDAO.getSearchObject(FacilityFlow.class, scs);
		if(ff!=null && ff.getPresentFlowMsr()!=null &&  ff.getPresentFlowMsr().intValue()>10){
			return true;
		}
		return false;
	}

	/**
	 * Determines if a facilityId belongs to a facility of a project
	 * @param facilityId  facility id
	 * @return boolean indicating whether it is a facility or a project
	 */	
	
	public boolean isFacility(Long facilityId) {
		//check facility Overall type 
		//if nonpoint source return false
		//else return true
		//fetch the facility
		Collection facilityTypes = facilityTypeService.getFacityType(facilityId);
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(FacilityTypeService.FACILITY_TYPE_NONSOURCE_POINT==ft.getFacilityTypeRef().getFacilityOverallTypeRef().getFacilityOverallTypeId()){
				return false;
			}
		}
		return true;
	}
	
	public boolean isFacilityDecentralized(Long facilityId) {
		Collection facilityTypes = facilityTypeService.getFacityType(facilityId);
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(FacilityTypeService.FACILITY_OVERALL_TYPE_DECENTRALIZED==ft.getFacilityTypeRef().getFacilityOverallTypeRef().getFacilityOverallTypeId()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isFacilityStormwater(Long facilityId) {
		Collection facilityTypes = facilityTypeService.getFacityType(facilityId);
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(FacilityTypeService.FACILITY_OVERALL_TYPE_STROMWATER==ft.getFacilityTypeRef().getFacilityOverallTypeRef().getFacilityOverallTypeId()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isFacilityWasteWater(Long facilityId) {
		Collection facilityTypes = facilityTypeService.getFacityType(facilityId);
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(FacilityTypeService.FACILITY_OVERALL_TYPE_WASTEWATER==ft.getFacilityTypeRef().getFacilityOverallTypeRef().getFacilityOverallTypeId()){
				return true;
			}
		}
		return false;
	}
	

	public Collection getFacilityByNameAndState(String facilityName, String locationId, String cwnsNbr) {
		//String cwnsNbr = getCWNSNbrByFacilityId(facilityId);
		SearchConditions scs =  new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_EQ, facilityName.trim()));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId",SearchCondition.OPERATOR_EQ,locationId));
		//scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facilityId",SearchCondition.OPERATOR_NOT_EQ,new Long(facilityId)));
		if(!"".equalsIgnoreCase(cwnsNbr))
		   scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("cwnsNbr",SearchCondition.OPERATOR_NOT_EQ,cwnsNbr));
		Collection facilities = searchDAO.getSearchList(Facility.class, scs);
		return facilities;
	}
    
	public String getCWNSNbrByFacilityId(String facilityId){
		return facilityDAO.getCWNSNbrByFacilityId(facilityId);
	}
	
	/**
	 * Determines if a facility has federal Needs
	 * @param facilityId  facility id
	 * @return boolean indicating whether a facility has federal needs or not
	 */		
	public boolean hasFederalNeeds(Long facilityId) {
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("fd.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("needTypeRef.needTypeId",SearchCondition.OPERATOR_LIKE,"F"));
        Collection c = searchDAO.getSearchList(Cost.class, new ArrayList(), scs, new ArrayList(), aliasArray);
        if (c != null && !(c.isEmpty()))
           return true;
        else
		   return false;
	}
	
	
	/**
	 * Check none of the facility's Facility Types allows for Private facilities to have Federal Needs 
     * @param facilityId  facility id
     * @return boolean
	 */
	public boolean facilityTypesAllowsFederalNeeds(Long facilityId){
		Collection fts =facilityTypeService.getFacityType(facilityId);
		Iterator iter = fts.iterator();
		boolean allowsFederalNeeds = false;
		while (iter.hasNext()){
			FacilityType ft = (FacilityType)iter.next();
			if (ft.getFacilityTypeRef().getFederalNeedsForPrivateFlag()=='Y'){
				allowsFederalNeeds = true;
				return allowsFederalNeeds;
			}
		}
		return allowsFederalNeeds;
	}
	/**
	 * Determines if the NPDES icon should be shown in the Facility
	 * Information portlet based on if facility has a permit with  
	 * Use Data Flag is checked
	 * @param facilityId
	 * @return boolean
	 */
	public boolean isNpdesIconVisible(Long facilityId){
		boolean isVisible = true;
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("usedForFacilityLocatnFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		FacilityPermit obj = (FacilityPermit)searchDAO.getSearchObject(FacilityPermit.class, scs1);
		if (obj == null)
			isVisible = false;
		return isVisible;
	}
	
	public String getNpdesFacilityName(Long facilityId){
		String facilityName = "";
		ArrayList columns = new ArrayList();
		columns.add("ef.facilityName");
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("permit", "p", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("p.efPermit", "ef", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("usedForFacilityLocatnFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		Collection c = searchDAO.getSearchList(FacilityPermit.class, columns, scs, new ArrayList(), aliasArray);
		Iterator iter = c.iterator();
		if (iter.hasNext()){
			facilityName = (String)iter.next();
		}		
		return facilityName;
	}

	public boolean isDataAreaViewable(CurrentUser user, Long facilityId, Long dataArea) {
		boolean isViewable = true;
		
		UserRole cur = user.getCurrentRole();
		
		//get the facilityObject
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		if(f==null){
			return false;
		}
		
		Long facId= facilityId;
		if('F' == f.getVersionCode()){
			Facility sFacility = getFacilityByCwnsNbr(f.getCwnsNbr());
			if(sFacility!=null){
				facId= new Long(sFacility.getFacilityId());	
			}else{
				return false;
			}
		}
		
		//Right Now the rules only apply to the needs area
		if(FacilityService.DATA_AREA_NEEDS.equals(dataArea)){
			if(UserService.LOCATION_TYPE_ID_STATE.equals(cur.getLocationTypeId())){//State
				//If the state user location is not the same as the facility location
				if(!cur.getLocationId().equals(f.getLocationId())){
					isViewable = false;
				}
			}else if(UserService.LOCATION_TYPE_ID_REGIONAL.equals(cur.getLocationTypeId())){//Regional
				//get the states in the region
				if(!isLocationinRegion(cur.getLocationId(), f.getLocationId())){
					isViewable = false;
				}
			}else if(UserService.LOCATION_TYPE_ID_LOCAL.equals(cur.getLocationTypeId())){//Local
				//check if assigned facility else return false;  
				if(!isUserAssociatedWithFacility(user, cur, facId)){
					isViewable = false;
				}
			}
			
		}
		return isViewable;
	}

	/** 
	 * Return the Collection of states (locationIds) associated with the given region.  
	 * @param regionCode
	 * @return Collection of locationIds associated with the given region code.
	 */
	public Collection getStateIdsForRegion(String regionCode){
		ArrayList columns = new ArrayList();
		columns.add("locationId");
		SearchConditions scs =  new SearchConditions(new SearchCondition("epaRegionCode", SearchCondition.OPERATOR_EQ, new Byte(regionCode)));
		return (Collection)searchDAO.getSearchList(StateRef.class, columns, scs);
	}
	
	public boolean isLocationinRegion(String regionCode, String locationId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("epaRegionCode", SearchCondition.OPERATOR_EQ, new Byte(regionCode)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId",SearchCondition.OPERATOR_EQ, locationId));
		Object o= searchDAO.getSearchObject(StateRef.class, scs);
		if(o!= null){
			return true;
		}
		return false;
	}
	
	public StateRef getStateByLocationId(String locationId) {
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		Object o= searchDAO.getSearchObject(StateRef.class, scs);
		return (StateRef)o;
	}
	
	//generic method to provide warning when feedback copy exists
	public boolean warnIfFeedBackOutOfSync(Long facilityId){
		Facility facility = findByFacilityId(facilityId.toString());
		Facility feedbackVersion = getFeedbackVersionOfFacility(facilityId.toString());
		if(feedbackVersion!=null){
			String Sreviewstatustype = facility.getReviewStatusRef().getReviewStatusId();
			String Freviewstatustype = feedbackVersion.getReviewStatusRef().getReviewStatusId();
			
			
			if ((ReviewStatusRefService.STATE_ASSIGNED.equalsIgnoreCase(Sreviewstatustype) || ReviewStatusRefService.STATE_IN_PROGRESS.equalsIgnoreCase(Sreviewstatustype) || ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equalsIgnoreCase(Sreviewstatustype))
				&& ((ReviewStatusRefService.LOCAL_ASSIGNED.equalsIgnoreCase(Freviewstatustype) || ReviewStatusRefService.LOCAL_IN_PROGRESS.equalsIgnoreCase(Freviewstatustype)))) {
			return true;
			}	
		}
		return false;
	}
	
	
	//performs postsaveUpdates	
	public void performPostSaveUpdates(Long facilityId, Long dataAreaId,  CurrentUser user){
		updateFacilityEntryStatus(facilityId, user, dataAreaId);
		updateFacilityAndReviewStatus(facilityId, user);
		if(!isFeedBack(facilityId, user)){			
			ccValidationManager.runValidation(facilityId, dataAreaId, user.getUserId());
			searchDAO.flushAndClearCache();
			//run cost curves
			try {
				costCurveService.runCostCurve(facilityId, user.getUserId());
			} catch (CostCurveException e) {
				log.error("Errors occured while executing cost curves" + e.getMessage());
			}
			fesManager.runValidation(facilityId, dataAreaId, user.getUserId());
			syncCcDataAreas(facilityId,user.getUserId());
		}	
	}
	
	public void syncCcDataAreas(Long facilityId, String userId){
		
		Collection fes = null;
		ArrayList columns = new ArrayList();
		columns.add("dataAreaRef.dataAreaId");
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		fes = searchDAO.getSearchList(FacilityEntryStatus.class,columns,scs);
		
		ArrayList columns1 = new ArrayList();
		columns1.add("facilityCostCurveId");
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		SearchConditions scs1 = new SearchConditions(new SearchCondition("fd.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection c = (ArrayList)searchDAO.getSearchList(FacilityCostCurve.class, columns1, scs1, new ArrayList(), aliasArray);
		if(c!=null){
			for (Iterator iter = c.iterator(); iter.hasNext();) {
				Collection cc = null;
				ArrayList addList = new ArrayList();
				ArrayList removeList = new ArrayList();
				Long facilityCostCurveId = (Long)(iter.next());
				ArrayList columns2 = new ArrayList();
				columns2.add("dataAreaRef.dataAreaId");
				SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityCostCurveId", SearchCondition.OPERATOR_EQ, facilityCostCurveId));
				cc = searchDAO.getSearchList(FacilityCostCurveDataArea.class,columns2,scs2);
				if(fes!=null){
				for (Iterator iter1 = fes.iterator(); iter1.hasNext();) {
					Long fesDataAreaId = (Long)iter1.next();
					if(cc==null || !cc.contains(fesDataAreaId)){
						addList.add(fesDataAreaId);
					}
				}
				}
				if(cc!=null){
				for (Iterator iter2 = cc.iterator(); iter2.hasNext();) {
					Long ccDataAreaId = (Long)iter2.next();
					if(fes!=null && !fes.contains(ccDataAreaId)){
						removeList.add(ccDataAreaId);
					}
				}
				}
			  if(addList.size()>0)
				  addDataAreas(addList,facilityCostCurveId,userId);
			  if(removeList.size()>0)
				  removeDataAreas(removeList,facilityCostCurveId);
			}
		}
		searchDAO.flushAndClearCache();
		
	}
	
	private void addDataAreas(Collection addList, Long facilityCostCurveId, String userId){
		for (Iterator iter = addList.iterator(); iter.hasNext();) {
			Long dataAreaId = ((Long)iter.next());
			log.debug("data area ids--"+dataAreaId.longValue()+" "+facilityCostCurveId.longValue());
			FacilityCostCurveDataAreaId id = new FacilityCostCurveDataAreaId();
			id.setDataAreaId(dataAreaId.longValue());
			id.setFacilityCostCurveId(facilityCostCurveId.longValue());
			FacilityCostCurveDataArea facilityCostCurveDataArea = new FacilityCostCurveDataArea();
			facilityCostCurveDataArea.setId(id);
			facilityCostCurveDataArea.setErrorFlag('N');
			facilityCostCurveDataArea.setLastUpdateTs(new Date());
			facilityCostCurveDataArea.setLastUpdateUserid(userId);
			searchDAO.saveObject(facilityCostCurveDataArea);
		}
		
	}
	
    private void removeDataAreas(Collection removeList, Long facilityCostCurveId){
    	for (Iterator iter = removeList.iterator(); iter.hasNext();) {
    		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityCostCurveId", SearchCondition.OPERATOR_EQ, facilityCostCurveId));
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.dataAreaId", SearchCondition.OPERATOR_EQ, (Long)iter.next()));
    		FacilityCostCurveDataArea obj = (FacilityCostCurveDataArea)searchDAO.getSearchObject(FacilityCostCurveDataArea.class,scs);
			if(obj!=null)
			  searchDAO.removeObject(obj);
		}
		
	}
    
	public void runAllCostCurveValidation(String locationId, String userId){
		java.lang.System.out.println("Called");
		//get a list of all the facilities
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		SearchConditions scs = new SearchConditions(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character('S')));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		Collection c = searchDAO.getSearchList(Facility.class,columns,scs);
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			Long facilityId = (Long) iter.next();
			java.lang.System.out.println("Processing facility" + facilityId.longValue());
			fesManager.runValidation(facilityId, FacilityService.DATA_AREA_FACILITY, userId);
			ccValidationManager.runValidation(facilityId, FacilityService.DATA_AREA_FACILITY, userId);
			searchDAO.flushAndClearCache();
			/*
			try {
				costCurveService.runCostCurve(facilityId, userId);
			} catch (CostCurveException e) {
				log.error("Errors occured while executing cost curves" + e.getMessage());
			}
			*/	
		} 				
	}    
	
	public void runAllCostCurveValidation(String userId){
		java.lang.System.out.println("Called");
		//get a list of all the facilities
		ArrayList columns = new ArrayList();
		columns.add("facilityId");
		//sort by column
		ArrayList sortArray = new ArrayList();
    	SortCriteria sortCriteria = new SortCriteria("facilityId", SortCriteria.ORDER_ASCENDING);	
		sortArray.add(sortCriteria);
		
		SearchConditions scs = new SearchConditions(new SearchCondition("versionCode", SearchCondition.OPERATOR_EQ, new Character('S')));
		Collection c = searchDAO.getSearchList(Facility.class,columns,scs,sortArray, new ArrayList(), 30001, 10000);
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			Long facilityId = (Long) iter.next();
			java.lang.System.out.println("Processing facility " + facilityId.longValue());
			fesManager.runValidation(facilityId, FacilityService.DATA_AREA_FACILITY, userId);
			searchDAO.flushAndClearCache();
			//ccValidationManager.runValidation(facilityId, FacilityService.DATA_AREA_FACILITY, userId);
			//try {
			//	costCurveService.runCostCurve(facilityId, userId);
			//	searchDAO.flushAndClearCache();
			//} catch (CostCurveException e) {
			//	log.error("Errors occured while executing cost curves" + e.getMessage());
			//}
		} 				
	}
	
	public void runAllCostCurveValidation(Long facilityId, String userId){
			java.lang.System.out.println("Processing facility" + facilityId.longValue());
			//fesManager.runValidation(facilityId, FacilityService.DATA_AREA_FACILITY, userId);
			//ccValidationManager.runValidation(facilityId, FacilityService.DATA_AREA_FACILITY, userId);
			try {
				costCurveService.runCostCurve(facilityId, userId);
			} catch (CostCurveException e) {
				log.error("Errors occured while executing cost curves" + e.getMessage());
			}
	}		

	
	public boolean runCCValidationByLocationId(String locationId, String userId){
		ArrayList aliasArray = new ArrayList();
		ArrayList revArr = new ArrayList();
		revArr.add("SAS");
		revArr.add("SIP");
		revArr.add("SCR");
		revArr.add("DE");
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("f.reviewStatusRef", "r", AliasCriteria.JOIN_INNER));
		SearchConditions scs = new SearchConditions(new SearchCondition("r.reviewStatusId", SearchCondition.OPERATOR_IN, revArr));
		scs.setCondition(new SearchCondition("curveRerunFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		Collection rerunFacilityCostCurves = searchDAO.getSearchList(FacilityCostCurve.class, scs, new ArrayList(), aliasArray,0,0);
		//check if each cost curve check if any error is set to Y  -- need to be optimized
		HashSet m = new HashSet();
		for (Iterator iter = rerunFacilityCostCurves.iterator(); iter.hasNext();) {
			FacilityCostCurve fcc = (FacilityCostCurve) iter.next();
			long facilityId = fcc.getFacilityDocument().getFacility().getFacilityId();
			m.add(new Long(facilityId));
		}
		//facilityId
		for (Iterator iter = m.iterator(); iter.hasNext();) {
			Long fId = (Long) iter.next();
			log.debug("Running Validation for: " + fId.toString());
			ccValidationManager.runValidation(fId, FacilityService.DATA_AREA_FACILITY, userId);			
		}
		return true;		
	}	
	
	
	private void updateFacilityEntryStatus(Long facilityId, CurrentUser user, Long dataAreaId){
		SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ, dataAreaId));
		FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
		if (facilityEntryStatus != null){
			facilityEntryStatus.setLastUpdateTs(new Date());
			facilityEntryStatus.setDataAreaLastUpdateTs(new Date());
			facilityEntryStatus.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(facilityEntryStatus);
		}		
	}
	
	private void updateFacilityAndReviewStatus(Long facilityId, CurrentUser user){
		//	get the facilityObject
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		UserRole ur = user.getCurrentRole();
		if (f != null){
			//update facility time stamp
			f.setLastUpdateTs(new Date());
			f.setLastUpdateUserid(user.getUserId());
		    if(UserServiceImpl.LOCATION_TYPE_ID_STATE.equals(ur.getLocationTypeId())){ //State view
		    	if (f.getReviewStatusRef()!=null){
		    		if (ReviewStatusRefService.STATE_ASSIGNED.equals(f.getReviewStatusRef().getReviewStatusId())){
		    			createFacilityReviewStatus(f, ReviewStatusRefService.STATE_IN_PROGRESS, user.getUserId());
		    		}
		    	}
		    }
		    else
		    	if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(ur.getLocationTypeId())){ //Local view
		    		if (f.getReviewStatusRef()!=null){
			    		if (ReviewStatusRefService.LOCAL_ASSIGNED.equals(f.getReviewStatusRef().getReviewStatusId())){
			    			createFacilityReviewStatus(f, ReviewStatusRefService.LOCAL_IN_PROGRESS, user.getUserId());
			    		}
			    	}
		    	}
		    searchDAO.saveObject(f);
		}		
	}
	
	
	private void createFacilityReviewStatus(Facility f, String reviewStatusRefId, String userId){
		FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
    	FacilityReviewStatusId id = new FacilityReviewStatusId(f.getFacilityId(), reviewStatusRefId, new Date());
    	facilityreviewstatus.setId(id);
    	facilityreviewstatus.setLastUpdateUserid(userId);
    	ReviewStatusRef rsr = reviewStatusRefService.findByReviewStatusRefId(reviewStatusRefId);
    	f.setReviewStatusRef(rsr);
    	searchDAO.saveObject(facilityreviewstatus);	
	}

	/**
	 * Given a cwns number return the associated Facility object.
	 * @param cwnsNbr
	 * @return
	 */
	public Facility getFacilityByCwnsNbr(String cwnsNbr) {
		SearchConditions scs  =  new SearchConditions(new SearchCondition("cwnsNbr", SearchCondition.OPERATOR_EQ, cwnsNbr));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("versionCode",SearchCondition.OPERATOR_EQ, "S"));
		return (Facility)searchDAO.getSearchObject(Facility.class, scs);
	}
	
	public boolean isSmallCommunity(Long facilityId){
		boolean isSmallCommunity = false;
		PopulationHelper popHelper = populationService.getUpStreamFacilitiesPopulationTotal(facilityId.toString());
		int presentUpstrNonResPop = 0;
		int presentUpstrResPop = 0;		
		if (popHelper!=null){
			presentUpstrNonResPop = popHelper.getPresentNonResPopulation();
			presentUpstrResPop = popHelper.getPresentResPopulation();
		}
		
		double presentTotalRecPop = populationService.getTotalPresentReceivingPopulation(facilityId);
		double presentTotalClusteredPop = populationService.getTotalPresentDecentralizedPopulation(facilityId);
		double presentTotalOWTSPop = populationService.getTotalPresentIndividualSewageDisposalSystemPopulation(facilityId);
		double totalPopulation = presentTotalRecPop + presentUpstrNonResPop + presentUpstrResPop + presentTotalClusteredPop + presentTotalOWTSPop;
		
		if (totalPopulation>0 && totalPopulation<10000){
			isSmallCommunity = true;
		}		
		return isSmallCommunity;
	}		
	
	public boolean hasFeedbackFacilityWithSRR(Long facilityId){
		boolean hasFeedbackFacility = false;
		Facility s_facility = facilityDAO.findByFacilityId(facilityId.toString());
		
		if (s_facility.getVersionCode()=='S'){
			String cwnsNbr = facilityDAO.getCWNSNbrByFacilityId(facilityId.toString());

			SearchConditions scs  =  new SearchConditions(new SearchCondition("cwnsNbr", SearchCondition.OPERATOR_EQ, cwnsNbr));
			scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("versionCode",SearchCondition.OPERATOR_EQ, "F"));
			Object objFacility = searchDAO.getSearchObject(Facility.class, scs);
			Facility f_facility = null;		
			f_facility = objFacility!=null?(Facility)objFacility:null;

			if (f_facility!=null  &&
					(f_facility.getReviewStatusRef().getReviewStatusId().equals(ReviewStatusRefService.STATE_REVIEW_REQUESTED))){
				hasFeedbackFacility = true;
			}
		}
		return hasFeedbackFacility;
	}

	
	public boolean statusChange(String facilityId, String newStatusId, CurrentUser user) {
		
		boolean changed = true;
		Facility facility = null;
		ReviewStatusRef revStatus =  reviewStatusRefService.findByReviewStatusRefId(newStatusId);
		if (newStatusId.equalsIgnoreCase(FacilityDAO.LOCAL_ASSIGNED)){
		    Collection f = facilityDAO.getFeedbackVersionOfFacility(facilityId);
		    if (f!=null && f.size()>0){
		    	for (Iterator iter = f.iterator(); iter.hasNext();) {
		    		facility = (Facility) iter.next();
				}
			   
		    }
		}
		else
			  facility = facilityDAO.findByFacilityId(facilityId);
		
		Long facId = new Long (facilityId);
        // create copy if LAS
		if(newStatusId.equalsIgnoreCase(FacilityDAO.LOCAL_ASSIGNED)){
			//facility = facilityDAO.findByFacilityId(facilityId);
			reviewStatusRefService.saveFeedbackReviewStatus(facilityId, facility==null?"":facility.getReviewStatusRef().getReviewStatusId(), "LAS", user);
		}
		else{
			FacilityReviewStatusId facilityReviewStatusId = new FacilityReviewStatusId();
			facilityReviewStatusId.setFacilityId(facId.longValue());
			facilityReviewStatusId.setLastUpdateTs(new Date());
			facilityReviewStatusId.setReviewStatusId(newStatusId);

			FacilityReviewStatus facilityReviewStatus = new FacilityReviewStatus();
			facilityReviewStatus.setFacility(facility);
			facilityReviewStatus.setId(facilityReviewStatusId );
			facilityReviewStatus.setLastUpdateUserid(user.getUserId());
			facilityReviewStatus.setReviewStatusRef(revStatus);

			//create faciltiy_review_status entry 
			searchDAO.getDAOSession().saveOrUpdate(facilityReviewStatus);
			//update facility 
			facility.setReviewStatusRef(revStatus);
			facility.setLastUpdateTs(new Date());
			facility.setLastUpdateUserid(user.getUserId());
		}
				
		return changed;
	}
	
	
	
	public Set changeStatusToFederalReviewRequested(Set facilities, CurrentUser user) {
		
    	Set facilityIdsNotUpdated = new HashSet();
    	List reviewStatusIdList = new ArrayList();
    	
    	reviewStatusIdList.add(FacilityDAO.STATE_ASSIGNED);
    	reviewStatusIdList.add(FacilityDAO.STATE_IN_PROGRESS);
    	
    	Iterator iter = facilities.iterator();
    	while (iter.hasNext()) {
    		String facilityId = (String)iter.next();
			
    		//if current Review_status is SAS or SIP and Facility_Entry_Status.Error_Flag = N for all data_areas
    		//update facility_review_status to FRR
    		//create facility_review_status FRR
    		
    		if(facilityDAO.isAllFacilityEntryStatusErrorFlagEqualNo(facilityId) 
					&& isFacilityReviewStatusFoundInList(facilityId,reviewStatusIdList)){
				if(!statusChange(facilityId,FacilityDAO.FEDERAL_REVIEW_REQUESTED,user)){
					facilityIdsNotUpdated.add(facilityId); //status not change
				}
			}else{
				facilityIdsNotUpdated.add(facilityId); //condition not met
			}
    	}
    	return facilityIdsNotUpdated;
    }
	
	public Set changeStatusToStateRequestedReturn(Set facilities, CurrentUser user) {
		Set facilityIdsNotUpdated = new HashSet();
		List reviewStatusIdList = new ArrayList();
		
		reviewStatusIdList.add(FacilityDAO.FEDERAL_REVIEW_REQUESTED);
		reviewStatusIdList.add(FacilityDAO.FEDERAL_REVIEW_CORRECTION);
		
		Iterator iter = facilities.iterator();
		while (iter.hasNext()) {
			String facilityId = (String)iter.next();
			
			//Check If facility.review_status = FRR or FRC
    		if(isFacilityReviewStatusFoundInList(facilityId,reviewStatusIdList)){
			//Get the review status before FRR or FRC (which is current)
    			String previousReviewStatus=getPreviousReviewStatus(facilityId);
			//Create Facility_review_status and Update facility to new status
    			if(previousReviewStatus==null || !statusChange(facilityId,previousReviewStatus,user)){
					facilityIdsNotUpdated.add(facilityId); //status not change
				}
    		}else{
    			facilityIdsNotUpdated.add(facilityId); //condition not met
    		}
		}
		return facilityIdsNotUpdated;
    }
	
	
	public String getPreviousReviewStatus(String facilityId){
		ArrayList columns = new ArrayList();
		columns.add("id.reviewStatusId");
		//columns.add("id.lastUpdateTs");
		SortCriteria sortCriteria = new SortCriteria("id.lastUpdateTs", SortCriteria.ORDER_DECENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId",SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		Collection results = searchDAO.getSearchList(FacilityReviewStatus.class, columns, scs, sortArray);
		Iterator iter = results.iterator();
		//should be the current review status
		if (iter.hasNext()){
			String currentReviewStatus=(String)iter.next();
			log.debug("Current Review Status: " + currentReviewStatus);
		}    			
		String previousReviewStatus=null;
		if (iter.hasNext()){
			previousReviewStatus=(String)iter.next();
		}
		return previousReviewStatus;
	}
	
	public Set changeStatusToDeleted(Set facilities, CurrentUser user) {
		
    	Set facilityIdsNotUpdated = new HashSet();
    	List reviewStatusIdList = new ArrayList();
    	
    	reviewStatusIdList.add(FacilityDAO.STATE_ASSIGNED);
    	reviewStatusIdList.add(FacilityDAO.STATE_IN_PROGRESS);
    	reviewStatusIdList.add(FacilityDAO.STATE_CORRECTION_REQUESTED);
    	
    	Iterator iter = facilities.iterator();
    	while (iter.hasNext()) {
    		String facilityId = (String)iter.next();
    		//If Facility.review_status_type_id is  ‘SAS’, ‘SIP’  or ‘SCR’
    		//Create Facility_review_status, linked to Review_status_type of ‘DE’
    		//Update facility: move transfer link to Review_Status_Type to ‘DE’    		
    		if(isFacilityReviewStatusFoundInList(facilityId,reviewStatusIdList)){
				if(!statusChange(facilityId,ReviewStatusRefService.DELETED,user)){
					facilityIdsNotUpdated.add(facilityId); //status not change
				}
			}else{
				facilityIdsNotUpdated.add(facilityId); //condition not met
			}
    	}
    	return facilityIdsNotUpdated;
    }
	
	public Set changeStatusToFederalReviewCorrection(Set facilities, CurrentUser user){
		Set facilityIdsNotUpdated = new HashSet();
		List reviewStatusIdList = new ArrayList();
		
		reviewStatusIdList.add(FacilityDAO.STATE_CORRECTION_REQUESTED);
		
		Iterator iter = facilities.iterator();
		while (iter.hasNext()) {
			String facilityId = (String)iter.next();
			
			//if current Review_status is SCR and Facility_Entry_status.error_flag = N for all State Correction request
			//update facility_review_status to FRC
			
			if(facilityDAO.isAllFacilityEntryStatusErrorFlagEqualNo(facilityId) 
					&& isFacilityReviewStatusFoundInList(facilityId,reviewStatusIdList)){
				if(!statusChange(facilityId,FacilityDAO.FEDERAL_REVIEW_CORRECTION,user)){
					facilityIdsNotUpdated.add(facilityId); //status not change
				}
			}else{
				facilityIdsNotUpdated.add(facilityId); //condition not met
			}
		}
		return facilityIdsNotUpdated;
	}
	
	public Set changeStatusToFederalAccepted(Set facilities, CurrentUser user){
		Set facilityIdsNotUpdated = new HashSet();
		List reviewStatusIdList = new ArrayList();
		
		reviewStatusIdList.add(FacilityDAO.FEDERAL_REVIEW_REQUESTED);
		reviewStatusIdList.add(FacilityDAO.FEDERAL_REVIEW_CORRECTION);
		
		Iterator iter = facilities.iterator();
		while (iter.hasNext()) {
			String facilityId = (String)iter.next();
			
			//if Facility.review_status_type_id is FRR or FRC and all federal_review_status.error_flag = N
			//create facility_review_status FA
			//change status to FA
			
			if(facilityDAO.isAllFederalReviewStatusErrorFlagEqualNo(facilityId) 
					&& isFacilityReviewStatusFoundInList(facilityId,reviewStatusIdList)){
				if(!statusChange(facilityId,FacilityDAO.FEDERAL_ACCEPTED,user)){
					facilityIdsNotUpdated.add(facilityId); //status not change
				}
			}else{
				facilityIdsNotUpdated.add(facilityId); //condition not met
			}
		}
		
		
		return facilityIdsNotUpdated;
	}
	
	public Set changeStatusToStateCorrectionRequested(Set facilities, CurrentUser user){
		Set facilityIdsNotUpdated = new HashSet();
		List reviewStatusIdList = new ArrayList();
		
		reviewStatusIdList.add(FacilityDAO.FEDERAL_REVIEW_REQUESTED);
		reviewStatusIdList.add(FacilityDAO.FEDERAL_REVIEW_CORRECTION);
		
		Iterator iter = facilities.iterator();
		while (iter.hasNext()) {
			String facilityId = (String)iter.next();
			
			//If Facility.review_status_type is FRR or FRC and At least 1 federal_review_status.error_flag = Y for facility
			//And lates review_comment for facility has last_update date > 
			//latest facility_review_status.last_update_ts where facility_review_status.review_status_type_is FRR or FRC
			//create facility_review_status SCR
			//update status to SCR
			
			if(!facilityDAO.isAllFederalReviewStatusErrorFlagEqualNo(facilityId)
					&& facilityDAO.isLastUpdateForReviewCommentLaterThanFacilityReviewStatus(facilityId)){
				if(!statusChange(facilityId,FacilityDAO.STATE_CORRECTION_REQUESTED,user)){
					facilityIdsNotUpdated.add(facilityId); //status not change
				}
			}else{
				facilityIdsNotUpdated.add(facilityId); //condition not met
			}
		}
		
		return facilityIdsNotUpdated;
	}
	
	public Set changeStatusToStateAssigned(Set facilities, CurrentUser user){
		Set facilityIdsNotUpdated = new HashSet();
		List reviewStatusIdList = new ArrayList();
		
		reviewStatusIdList.add(FacilityDAO.FEDERAL_ACCEPTED);
		
		Iterator iter = facilities.iterator();
		while (iter.hasNext()) {
			String facilityId = (String)iter.next();

			//if Facility.review_status_type_id is FA
			//create facility_review_status SAS
			//update review_status_type to SAS

			if(isFacilityReviewStatusFoundInList(facilityId,reviewStatusIdList)){
				if(!statusChange(facilityId,FacilityDAO.STATE_ASSIGNED,user)){
					facilityIdsNotUpdated.add(facilityId); //status not change
				}
			}else{
				facilityIdsNotUpdated.add(facilityId); //condition not met
			}
		}
		
		return facilityIdsNotUpdated;
	}
	
	public Set changeStatusToLocalAssigned(Set facilities, CurrentUser user){
		Set facilityIdsNotUpdated = new HashSet();
		List reviewStatusIdList = new ArrayList();
		Facility facility;
		Facility feedbackCopy = null;
		
		reviewStatusIdList.add(FacilityDAO.STATE_ASSIGNED);
		reviewStatusIdList.add(FacilityDAO.STATE_IN_PROGRESS);
		reviewStatusIdList.add(FacilityDAO.STATE_CORRECTION_REQUESTED);
		
		Iterator iter = facilities.iterator();
		while (iter.hasNext()) {
			String facilityId = (String)iter.next();
			Collection f = facilityDAO.getFeedbackVersionOfFacility(facilityId);
		    if (f!=null && f.size()>0){
		    	for (Iterator iter1 = f.iterator(); iter1.hasNext();) {
		    		feedbackCopy = (Facility) iter1.next();
				}
			   
		    }
			//if Facility.review_status_type_id is SAS or SIP or SCR 
			//And Facility has any user_location_facility, And that user_location_facility has user_location
			//that has Location_access_level of update-feedback
			//Create Facility_review_status of LAS
			//change status to LAS
			if(isFacilityHasCwnsLocalUserWithUpdateFeebackAccessLevel(facilityId) 
					&& isFacilityReviewStatusFoundInList(facilityId,reviewStatusIdList)
					&& (feedbackCopy==null || (feedbackCopy!=null 
							&& ReviewStatusRefService.STATE_APPLIED.equalsIgnoreCase(feedbackCopy.getReviewStatusRef().getReviewStatusId())))){
				
				if(!statusChange(facilityId,FacilityDAO.LOCAL_ASSIGNED,user)){
					facilityIdsNotUpdated.add(facilityId); //status not change
				  }
			}else{
				facilityIdsNotUpdated.add(facilityId); //condition not met
			}
		}
		
		return facilityIdsNotUpdated;
	}
	
	
	
	public boolean isFacilityReviewStatusFoundInList(String facilityId, List reviewStatusIdList){
		boolean condition = false;
		Facility facility = facilityDAO.findByFacilityId(facilityId);
		String reviewStatusId;
		String facilityReviewStatusId;
		
		for(int I = 0; I < reviewStatusIdList.size(); I++){
			reviewStatusId = (String) reviewStatusIdList.get(I);
			facilityReviewStatusId = facility.getReviewStatusRef().getReviewStatusId();
			if(reviewStatusId.equalsIgnoreCase(facilityReviewStatusId)){
				condition = true;
				return condition;
			}
		}
		return condition;
	}

	public boolean isCwnsUserAccessLevel(String cwnsUserId, long accessLevel){
		boolean condition = false;
		List accessList = cwnsUserLocatnAccessLevelDAO.getCwnsUserLocationAccessLevelByCwnsUserId(cwnsUserId);
		long accessId;
		for(int I = 0; I < accessList.size(); I++){
			accessId = ((CwnsUserLocatnAccessLevel)accessList.get(I)).getId().getAccessLevelId();
			if(accessId == accessLevel){
				condition = true;
				return condition;
			}
		}
		
		return condition;
	}
	
	public boolean isFacilityHasCwnsLocalUserWithUpdateFeebackAccessLevel(String facilityId){
		boolean condition = false;
		List locFacList = cwnsUserLocationFacilityDAO.getLocalCwnsUserLocationFacilityByFacilityId(facilityId);
		
		String cwnsUserId;
		for(int I = 0; I < locFacList.size(); I++){
			cwnsUserId = ((CwnsUserLocationFacility)locFacList.get(I)).getId().getCwnsUserId();
			if(isCwnsUserAccessLevel(cwnsUserId,AccessLevelRefDAO.FACILITY_UPDATE_FEEDBACK)){
				condition = true;
			}
		}
		return condition;
	}
	
	public boolean isCwnsUserHasAccessLevel(CurrentUser user, long accessLevel){
		boolean condition = false;
		List userAccessLevels = cwnsUserLocatnAccessLevelDAO.getCwnsUserLocationAccessLevel(user.getUserId(), user.getCurrentRole().getLocationTypeId(), user.getCurrentRole().getLocationId());
		long accessLevelId;
		for(int I = 0; I < userAccessLevels.size(); I++){
			accessLevelId = ((CwnsUserLocatnAccessLevel)userAccessLevels.get(I)).getId().getAccessLevelId();
			if(accessLevelId == accessLevel){
				return true;
			}
		}
		return condition;
	}
	
	public boolean isCurrentDateAfterDataEntryEndDate(CurrentUser user){
		boolean condition = false;
		CwnsInfoLocationRef cwnsInfoLocationRef = cwnsInfoLocationRefDAO.getCwnsInfoLocationRefByLocationId(user.getCurrentRole().getLocationId());
		Date endDate = cwnsInfoLocationRef.getDataEntryEndDate();
		Date currentDate = new Date();
		if(currentDate.after(endDate)){
			return true;
		}
		return condition;
	}
	
	public boolean isFeedBack(Long facilityId, CurrentUser user) {
		Facility facility = facilityDAO.findByFacilityId(facilityId.toString());
		if(facility.getVersionCode()==VERSION_FEEDBACK)return true;
		return false;
	}


	public boolean requestAccess(Set facilities, CurrentUser user) {    	
    	if(facilities!=null && !facilities.isEmpty() && user!=null){
        	StringBuffer sb = new StringBuffer();    		
        	Iterator iter = facilities.iterator();
            sb.append(user.getFirstName() + " " + user.getLastName() + " has requested access to: ");    		
        	int size=facilities.size();
        	int i=0;
        	while (iter.hasNext()) {
        		String facilityId = (String)iter.next();
        		//construct a string and added a note to announcements
        		Facility f = facilityDAO.findByFacilityId(facilityId);
        		if(f!=null){
        		   if(i==size-1){
        			   sb.append(f.getCwnsNbr());   
        		   }else{
        			   sb.append(f.getCwnsNbr()+",");
        		   }
        		   
        		}
        		i++;
        	}
        	sb.append(" facilities/projects");
        	String message = sb.toString();
        	String desc= (message.length()>200)?message.substring(0,199):message;
        	//insert a announcement
        	Announcement a = new Announcement();
    		Date now = new Date();
    		AnnouncementId aId = new AnnouncementId(AnnouncementService.ADMIN_MESSAGE_ID,now);
    		a.setId(aId);
    		a.setLastUpdateUserid(user.getUserId());
    		UserRole ur = user.getCurrentRole();
    		a.setLocationId(ur.getLocationId());
    		a.setDescription(desc);
    		AdministrativeMessageRef administrativeMessageRef = (AdministrativeMessageRef) searchDAO.getObject(AdministrativeMessageRef.class, new Long(AnnouncementService.ADMIN_MESSAGE_ID));
    		a.setAdministrativeMessageRef(administrativeMessageRef);
    		searchDAO.saveObject(a);		
    	}
		return false;
  }
	
	/**
	 * Insert a record into the MY_FACILITIES table to store the imported
	 * list of facilities. Return the MY_FACILITIES_ID that identifies the 
	 * record just saved.
	 * @param facilities
	 * 		A blank delimeted string of facility numbers
	 * @return
	 */
	public void updateImportFacilitiesInDatabase(Set facilityNbrs, String userAndRole){
			SearchConditions scs  =  new SearchConditions(new SearchCondition("userAndRole", SearchCondition.OPERATOR_EQ, userAndRole));
			scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("listType",SearchCondition.OPERATOR_EQ, CwnsUserSettingDAO.IMPORT_LIST_TYPE));
			Collection cwnsUserSettings = searchDAO.getSearchList(CwnsUserSetting.class, scs);
			if(cwnsUserSettings!=null){
				for(Iterator iter=cwnsUserSettings.iterator();iter.hasNext();){
					CwnsUserSetting cwnsUserSetting = (CwnsUserSetting)iter.next();
					searchDAO.removeObject(cwnsUserSetting);
				}
			}
			
	        // Save facilityIds to the database
	        Iterator iter = facilityNbrs.iterator();
			while (iter.hasNext()) {
				String facilityNumber = (String) iter.next();

				// Get the FacilityId for this Facility number
							
				ArrayList columns = new ArrayList();
				columns.add("facilityId");
				SearchConditions scs1  =  new SearchConditions(new SearchCondition("cwnsNbr", SearchCondition.OPERATOR_EQ, facilityNumber));
				Collection facilityIds = searchDAO.getSearchList(Facility.class, columns, scs1);
				
				//TODO: What if there is more than one FACILITY record found?
				if(facilityIds!=null){
					for(Iterator iter1=facilityIds.iterator();iter1.hasNext();){
                        // Get the facility number
						long facilityId = ((Long)iter1.next()).longValue();
						CwnsUserSetting cwnsUserSetting = new CwnsUserSetting();
						cwnsUserSetting.setFacilityId(facilityId);
						cwnsUserSetting.setLastUpdateTs(new Date());
						cwnsUserSetting.setLastUpdateUserid(userAndRole);
						cwnsUserSetting.setListType(CwnsUserSettingDAO.IMPORT_LIST_TYPE);
						cwnsUserSetting.setUserAndRole(userAndRole);
						searchDAO.saveObject(cwnsUserSetting);
					}	
				}
			}
	}

}