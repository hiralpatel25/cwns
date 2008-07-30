package gov.epa.owm.mtb.cwns.facility;

/**
 * This class prepares, and processes, the Facility List Portlet information.
 * @author Matt Connors
 * @version 1.0
 */
import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.AccessLevelRefDAO;
import gov.epa.owm.mtb.cwns.dao.CwnsUserSettingDAO;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.UserService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.criterion.Projections;


public class FacilityListDisplayAction extends CWNSAction {

	public static final String ACTION_SIMPLE_QUERY="simple";
	public static final String ACTION_ADVANCE_QUERY="advance";
	public static final String ACTION_IMPORT="import"; 
	public static final String ACTION_DISPLAY="display";
	public static final String ACTION_MY_FACILITIES="my_facilities";

	public static final String ACTION_SELECT_ALL="selectAll"; 
	public static final String ACTION_CLEAR_ALL="clearAll";
	public static final String ACTION_SET_REVIEW_STATUS="reviewStatus";
	public static final String ACTION_REQUEST_ACCESS="requestAccess";
	
	public static final String ACTION_NEXT="next";
	public static final String ACTION_PREVIOUS="previous";
	public static final String ACTION_FIRST="first";
	public static final String ACTION_LAST="last";
	public static final String ACTION_SORT="sort";
	public static final String ACTION_DEFAULT_QUERY="default";

	public static final String QUERY_TYPE_SIMPLE="simple";
	public static final String QUERY_TYPE_ADVANCE="advance";
	public static final String QUERY_TYPE_IMPORT="import"; 
	public static final String QUERY_TYPE_DEFAULT="default";
	public static final String QUERY_MY_FACILITIES="my_facilities";

	public static final String QUERY_TYPE_DEFAULT_DESC="All Facilities";
	public static final String QUERY_TYPE_SIMPLE_DESC="Facilities based on search keyword: ";
	public static final String QUERY_TYPE_ADVANCE_DESC="Facilities based on advance search";	
	public static final String QUERY_TYPE_IMPORT_DESC ="Imported Facilities";
	public static final String QUERY_MY_FACILITIES_DESC ="My Facilities";

	public static final String SORT_COLUMN_CWNS_NUMBER ="id.cwnsNbr";
	public static final String SORT_COLUMN_NAME ="id.facilityName";
	public static final String SORT_COLUMN_REVIEW_STATUS ="id.reviewStatusName";
	public static final String SORT_COLUMN_COUNTY ="id.county";
	public static final String SORT_COLUMN_LOCAL_REVIEW_STATUS ="id.feedbackReviewStatusName";
	public static final String SORT_AUTHORITY ="id.authority";
	public static final String SORT_COLUMN_SELECTED ="selected";
	
	public static final String SORT_DESC_CWNS_NUMBER 			= "CWNS Number";
	public static final String SORT_DESC_FACILITY_NAME			= "Facility/Project Name";
	public static final String SORT_DESC_REVIEW_STATUS_NAME 	= "Status";
	public static final String SORT_DESC_COUNTY 				= "County";
	public static final String SORT_DESC_FEEDBACK				= "Feedback Status";
	public static final String SORT_DESC_AUTHORITY 				= "Authority";
	public static final String SORT_ORDER_DESC_ASCENDING		= "Ascending";
	public static final String SORT_ORDER_DESC_DESCENDING 		= "Descending";
	
	public static final String DISPLAY_FRR		= "displayFRR";
	public static final String DISPLAY_LA		= "displayLA";
	public static final String DISPLAY_FA		= "displayFA";
	public static final String DISPLAY_SCR		= "displaySCR";
	public static final String DISPLAY_SA		= "displaySA";
	public static final String DISPLAY_SRRT		= "displaySRRT";
	public static final String DISPLAY_DE		= "displayDE";
	public static final String DISPLAY_REQUEST_ACCESS = "requestAccess";

	// used for import and export
	public static final String IMPORT_EXPORT_KEY="import_export_key";	
	public static final String DEFAULT_SORT_COLUMN="id.facilityName";
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	
		/** get action form **/
		FacilityListForm facilityListForm = (FacilityListForm) form;
        ActionErrors errors = new ActionErrors();
		
    	//try {   	
    		PortletRenderRequest prr = (PortletRenderRequest)
    		      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   		    //TODO: Check if provider session works in the portal environment
    		//ProviderSession pSession = prr.getSession();
   		    HttpSession pSession = request.getSession(false);
   		    if(pSession==null){
   		    	throw new ApplicationException("Unable to fetch provider session");	
   		    }
       		
   		    /* Determine the action to take. First check the form bean
   		     * and then (if necessary) check the request object. */  		    
   		    String action = null;
	    	if(facilityListForm.getAct() != null && !"".equals(facilityListForm.getAct())){
	    		action=facilityListForm.getAct().trim();
	    	}else {
	    		if (prr.getParameter("action") != null && !"".equals(prr.getParameter("action"))) {
	    			action = prr.getParameter("action");
	    		}else {
	    			action=ACTION_DISPLAY;
	    		}
	    	}

   		    if (pSession.getAttribute("switchRole") != null &&
   		    	"switchRole".equals((String)pSession.getAttribute("switchRole"))) {
   		    	// The user switch their role so revert back to the default querry
   		    	
   		    	action = ACTION_DEFAULT_QUERY;
   		    }
   		    
   		    pSession.setAttribute("switchRole", null);

   		    log.debug("action = "+action);  		    
   		    
   		    /* User Information  */
			CurrentUser currentUser = (CurrentUser) request.getSession().getAttribute(CurrentUser.CWNS_USER);
			String cwnsUserId = currentUser.getUserId();
		    String locationId=currentUser.getCurrentRole().getLocationId();
		    String locationTypeId=currentUser.getCurrentRole().getLocationTypeId();
   			
		    request.setAttribute("currentRole", currentUser.getCurrentRole());
   		   	//request.setAttribute("accessLevel", currentUser.getCurrentRole().);
   		    
            //   get the list query Type
   		    String queryType = (String) pSession.getAttribute("queryType");
   		    //set sort criteria
   		    SortCriteria sc = new SortCriteria(DEFAULT_SORT_COLUMN, SortCriteria.ORDER_ASCENDING);
   		    
   		    // get the start index from the session
   		    Integer strtIndx =(Integer)pSession.getAttribute("startIndex"); 
   		    int startIndex = (strtIndx != null) ? strtIndx.intValue() : 1;  
   		    
   		    Search search=null;
   		    Set masterSelectedList = (Set) pSession.getAttribute("masterSelectedList");
   		    Collection facilityListHelpers = new ArrayList();
   		    
   		    /* Determine the query criteria */
   		    if (ACTION_SIMPLE_QUERY.equals(action)) {
   		    	masterSelectedList = clearMasterSelectedList(pSession); 
   		    	//get the parameters
   		    	String keyword = prr.getParameter("keyword");
   		    	if(keyword==null){
   		    		throw new ApplicationException("keyword is null");
   		    	}
   		    	keyword=keyword.trim();
   		    	startIndex = 1;
   		    	search = new Search();
   		    	search.setStartIndex(startIndex);
   		    	search.setMaxResults(FacilityDAO.MAX_RESULTS);
   		    	search.setSearchType(Search.SEARCH_TYPE_STORE_FUNCTION_KEYWORD_SEARCH);
   		    	search.setQueryProperties(getSimpleQueryProperties(currentUser, keyword));
   		    	search.setName(QUERY_TYPE_SIMPLE);
   		    	search.setDescription(QUERY_TYPE_SIMPLE_DESC + keyword);
   	   		    
   		    	pSession.setAttribute("queryType", QUERY_TYPE_SIMPLE);

   		    }else if (ACTION_ADVANCE_QUERY.equals(action)) {
   		    	
   		    	log.debug("ACTION_ADVANCE_QUERY");
   		    	masterSelectedList  = clearMasterSelectedList(pSession); 
   		    	//Map queryPropertiesOld = getQueryPropertiesOld(prr);
 		    	//Map queryPropertiesNew = getQueryProperties(prr);
   		    	//pSession.setAttribute("queryProperties", queryPropertiesNew);
   		    	startIndex = 1;
   		    	search = new Search();
   		    	search.setStartIndex(startIndex);
   		    	search.setName(QUERY_TYPE_ADVANCE);
   		    	search.setDescription(QUERY_TYPE_ADVANCE_DESC);
   		    	search.setQueryProperties(getAdvanceQueryProperties(prr));
   		    	search.setMaxResults(FacilityDAO.MAX_RESULTS);
   		    	search.setSearchType(Search.SEARCH_TYPE_STORE_FUNCTION_ADVANCE_SEARCH);
   		    	
   		    	//facilityListHelpers = facilityDAO.executeAdvancedSearch(currentUser, search, queryPropertiesNew, startIndex);
   	   		    //search= facilityService.getAdvanceSearch(queryPropertiesOld, locationId, sc, startIndex-1, FacilityService.MAX_RESULTS);
	    		
		    } else if (ACTION_IMPORT.equals(action)) { 		
		    	Collection facilityIds = facilityService.getCwnsUserSetting(currentUser.getUserAndRole(),CwnsUserSettingDAO.IMPORT_LIST_TYPE);
				if (facilityIds.size() > 0 ) {
					masterSelectedList = clearMasterSelectedList(pSession); 
					pSession.setAttribute("masterSelectedList", new HashSet());
					startIndex = 1;
					search = facilityService.getImportSearch(currentUser.getUserAndRole(),sc,startIndex -1,FacilityService.MAX_RESULTS);
					search.setSearchType(Search.SEARCH_TYPE_CODED_SEARCH);
					pSession.setAttribute("queryType", QUERY_TYPE_IMPORT);
				} else {
					// The import contained no valid CWNS numbers
					ActionError error = new ActionError("error.import.noCWNSRecordsFound");
					errors.add(ActionErrors.GLOBAL_ERROR,error);
					search = (Search) pSession.getAttribute("search");
				}
				
		    } else if (ACTION_DEFAULT_QUERY.equals(action) || queryType == null) {
		    	log.debug("ACTION_DEFAULT_QUERY");
		    	startIndex = 1;
		    	masterSelectedList = clearMasterSelectedList(pSession); 
				search = new Search();
				search.setName(QUERY_TYPE_DEFAULT);
   		    	search.setDescription(QUERY_TYPE_DEFAULT_DESC);
   		    	search.setMaxResults(FacilityDAO.MAX_RESULTS);
		    	search.setStartIndex(startIndex);
		    	search.setQueryProperties(getDefaultQueryProperties(currentUser));
		    	search.setCwnsUserId(currentUser.getUserId()+","+currentUser.getCurrentRole().getLocationTypeId()+","+locationId);
		    	search.setSearchType(Search.SEARCH_TYPE_STORE_FUNCTION_ADVANCE_SEARCH);
		    	pSession.setAttribute("queryType", QUERY_TYPE_DEFAULT_DESC);
				
			}
		    else {
				search = (Search) pSession.getAttribute("search");
			}   	
   		       		    
   		    if(search == null){
   		    	throw new ApplicationException("Unable to find search/construct search condition");
   		    }
   		    //save search for future
			pSession.setAttribute("search", search);
   		    
   		    //determine the list properties
   		    if (ACTION_NEXT.equals(action)) {
   		    	masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
   		    	startIndex=facilityListForm.getNextFacilityToDisplay();
   		    	log.debug("ACTION_NEXT = " + startIndex);
   		    	if(search.getSearchType().equalsIgnoreCase(Search.SEARCH_TYPE_CODED_SEARCH)){
   		    		search.setStartIndex(startIndex-1);
   		    	}
   		    	else{
   		    		search.setStartIndex(startIndex);
   		    	}
   		    	
   		    	
   		    } else if (ACTION_PREVIOUS.equals(action)) {
   		    	masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
   		    	startIndex=facilityListForm.getPrevFacilityToDisplay();
   		    	log.debug("ACTION_PREVIOUS = " + startIndex);
   		    	if(search.getSearchType().equalsIgnoreCase(Search.SEARCH_TYPE_CODED_SEARCH)){
   		    		search.setStartIndex(startIndex-1);
   		    	}
   		    	else{
   		    		search.setStartIndex(startIndex);
   		    	}
   		    	
   		    } else if (ACTION_FIRST.equals(action)) {
   		    	masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
   		    	startIndex=1;
   		    	log.debug("ACTION_FIRST = " + startIndex);
   		    	if(search.getSearchType().equalsIgnoreCase(Search.SEARCH_TYPE_CODED_SEARCH)){
   		    		search.setStartIndex(startIndex-1);
   		    	}
   		    	else{
   		    		search.setStartIndex(startIndex);
   		    	}
   		    	
   		    } else if (ACTION_LAST.equals(action)) {
   		    	masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
   	   			int totalFacilities = ((Integer) pSession.getAttribute("totalFacilities")).intValue();
   	   			if (totalFacilities % FacilityService.MAX_RESULTS == 0) {
   	   				startIndex = totalFacilities - FacilityService.MAX_RESULTS +1;
   	   			}else {
   	   				startIndex = totalFacilities - 
   		    					(totalFacilities % FacilityService.MAX_RESULTS - 1);
   	   			}
   	   			log.debug("ACTION_LAST = " + startIndex);
   	   			if(search.getSearchType().equalsIgnoreCase(Search.SEARCH_TYPE_CODED_SEARCH)){
		    		search.setStartIndex(startIndex-1);
		    	}
		    	else{
		    		search.setStartIndex(startIndex);
		    	}
   		    	
   		    } else if (ACTION_SORT.equals(action)) {
   		    	masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
   		    	
   		    	String sortColumn = "";
   		    	String sortOrder = "";
		    	String sortColumnDesc = "";
		    	String sortOrderDesc = "";
   		    	
		    	if(facilityListForm.getSortColumn().equalsIgnoreCase(SORT_COLUMN_CWNS_NUMBER)){
		    			sortColumn = FacilityDAO.SORT_ADVANCE_SEARCH_CWNS_NUMBER;
		    			sortColumnDesc = SORT_DESC_CWNS_NUMBER;
		    		}else if(facilityListForm.getSortColumn().equalsIgnoreCase(SORT_COLUMN_NAME)){
		    			sortColumn = FacilityDAO.SORT_ADVANCE_SEARCH_FACILITY_NAME;
		    			sortColumnDesc = SORT_DESC_FACILITY_NAME;
		    		}else if(facilityListForm.getSortColumn().equalsIgnoreCase(SORT_COLUMN_REVIEW_STATUS)){
		    			sortColumn = FacilityDAO.SORT_ADVANCE_SEARCH_REVIEW_STATUS_NAME;
		    			sortColumnDesc = SORT_DESC_REVIEW_STATUS_NAME;
		    		}else if(facilityListForm.getSortColumn().equalsIgnoreCase(SORT_COLUMN_COUNTY)){
		    			sortColumn = FacilityDAO.SORT_ADVANCE_SEARCH_COUNTY;
		    			sortColumnDesc = SORT_DESC_COUNTY;
		    		}else if(facilityListForm.getSortColumn().equalsIgnoreCase(SORT_COLUMN_LOCAL_REVIEW_STATUS)){
		    			sortColumn = FacilityDAO.SORT_ADVANCE_SEARCH_FEEDBACK;
		    			sortColumnDesc = SORT_DESC_FEEDBACK;
		    		}else if(facilityListForm.getSortColumn().equalsIgnoreCase(SORT_AUTHORITY)){
		    			sortColumn = FacilityDAO.SORT_ADVANCE_SEARCH_AUTHORITY;
		    			sortColumnDesc = SORT_DESC_AUTHORITY;
		    		}
		    		if(search.getSortOrder().equalsIgnoreCase(FacilityDAO.SORT_ORDER_ADVANCE_SEARCH_ASCENDING))
		    		{
		    			sortOrder = FacilityDAO.SORT_ORDER_ADVANCE_SEARCH_DESCENDING;
		    			sortOrderDesc = SORT_ORDER_DESC_DESCENDING;
		    		}else{
		    			sortOrder = FacilityDAO.SORT_ORDER_ADVANCE_SEARCH_ASCENDING;
		    			sortOrderDesc = SORT_ORDER_DESC_ASCENDING;
		    		}
		    		
		    		if(search.getSearchType().equalsIgnoreCase(Search.SEARCH_TYPE_CODED_SEARCH)){
   		    	
   		    		sc = new SortCriteria(facilityListForm.getSortColumn(), facilityListForm.getSortOrder());

   		    		// Get the previous sort criteria 
   		    		Collection scs = search.getSortCriteria();
   		    		Iterator iter = scs.iterator();
   		    		SortCriteria oldSc = (SortCriteria)iter.next();

   		    		// See if the user changed the sort column
   		    		if (sc.getColumn().equals(oldSc.getColumn())) {
   		    			// If the user did a refresh don't change the sort order
   		    			if (isTokenValid(request)) {
   		    				ArrayList a = new ArrayList();
   		    				oldSc.changeOrder();
   		    				a.add(oldSc);   		    			
   		    				search.setSortCriteria(a);
   		    				startIndex = 1;
   		    				search.setStartIndex(startIndex -1);
   		    				search.setSortColumnDescription(sortColumnDesc);
   		    				search.setSortOrderDescription(sortOrderDesc);
   		    			}
   		    		} else {
   		    			// Change the sort column
   		    			ArrayList a = new ArrayList();
   		    			sortOrderDesc = SORT_ORDER_DESC_ASCENDING;
   		    			sc.setOrder(SortCriteria.ORDER_ASCENDING);
   		    			a.add(sc);
   		    			search.setSortCriteria(a);
   		    			startIndex = 1;
   		    			search.setStartIndex(startIndex -1);
   		    			search.setSortColumnDescription(sortColumnDesc);
		    			search.setSortOrderDescription(sortOrderDesc);
   		    		}
   		    	}else{
   		    		Map queryProperties = search.getQueryProperties();
   		    		String sort = "";
   		    		
   		    		if(search.getSortColumn().equalsIgnoreCase(sortColumn)){
   		    			if (isTokenValid(request)) {
   		    				sort = search.getSortColumn() + " " + sortOrder;
   		    				search.setSortOrder(sortOrder);
   		    				search.setSortColumnDescription(sortColumnDesc);
   		    				search.setSortOrderDescription(sortOrderDesc);
   		    				log.debug("Column sort = " + sort);
   		    				queryProperties.put(FacilityDAO.SORT_COLUMN,sort);
   		    			}
   		    		}else{
   		    			sortOrder = FacilityDAO.SORT_ORDER_ADVANCE_SEARCH_ASCENDING;
   		    			sortOrderDesc = SORT_ORDER_DESC_ASCENDING;
   		    			search.setSortColumn(sortColumn);
   		    			search.setSortOrder(sortOrder);
   		    			search.setSortColumnDescription(sortColumnDesc);
		    			search.setSortOrderDescription(sortOrderDesc);
   		    			sort =sortColumn + " " + sortOrder;
   		    			log.debug("Init sort = " + sort);
   		    			queryProperties.put(FacilityDAO.SORT_COLUMN,sort);
   		    		}
   		    			    		
   		    		
   		    	}

   		    	
		    }else if (ACTION_CLEAR_ALL.equals(action)) {
		      masterSelectedList = clearMasterSelectedList(pSession);
 		      facilityListForm.setFacilityIds(new String[0]);
 		      
		   } else if (ACTION_SELECT_ALL.equals(action)){
			   int maxResults = search.getMaxResults();
			   
			   Collection  facilityIdList;
			   log.debug("Select All = " + search.getName());
			   if(search.getSearchType().equalsIgnoreCase(Search.SEARCH_TYPE_CODED_SEARCH)){
				   log.debug("Select All = " + QUERY_TYPE_SIMPLE);
				   search.setStartIndex(0);
				   search.setMaxResults(0);
				   // Get all facilityIds based on the search Criteria. 
				   facilityIdList = facilityService.getFacilitiesIds(search);
				   masterSelectedList = new HashSet(convertLongToString(facilityIdList));
				   pSession.setAttribute("masterSelectedList", masterSelectedList);
				   // Reset startIndex & maxResults back to their original values
				   search.setStartIndex(startIndex-1);
			   }
			   else{
				   log.debug("Select All Other");
				   search.setStartIndex(1);
				   search.setMaxResults(search.getNumOfFacilities());
				   facilityIdList = facilityDAO.executeAdvancedSearch(search);
				   masterSelectedList = new HashSet(getFacilityId(facilityIdList));
				   pSession.setAttribute("masterSelectedList", masterSelectedList);
				   search.setStartIndex(startIndex);
				   log.debug("-----------------------------------------");
			   }
			   
			   search.setMaxResults(maxResults);
 		      
		   } else if(facilityDAO.FEDERAL_REVIEW_REQUESTED.equalsIgnoreCase(action)){
			   masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
			   Set facilityIdsNotUpdated = facilityService.changeStatusToFederalReviewRequested(masterSelectedList, currentUser);
			   setFacilityNotChangeAttribute(facilityIdsNotUpdated,masterSelectedList,request);
			   masterSelectedList = clearMasterSelectedList(pSession);
			   if(facilityIdsNotUpdated != null && facilityIdsNotUpdated.size() > 0){
				   masterSelectedList = facilityIdsNotUpdated;
				   pSession.setAttribute("masterSelectedList", masterSelectedList);
			   }
			   
		   } else if(facilityDAO.LOCAL_ASSIGNED.equalsIgnoreCase(action)){
			   masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
			   Set facilityIdsNotUpdated = facilityService.changeStatusToLocalAssigned(masterSelectedList, currentUser);
			   setFacilityNotChangeAttribute(facilityIdsNotUpdated,masterSelectedList,request);
			   masterSelectedList = clearMasterSelectedList(pSession);
			   if(facilityIdsNotUpdated != null && facilityIdsNotUpdated.size() > 0){
				   masterSelectedList = facilityIdsNotUpdated;
				   pSession.setAttribute("masterSelectedList", masterSelectedList);
			   }
		   }else if(facilityDAO.FEDERAL_ACCEPTED.equalsIgnoreCase(action)){
			   masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
			   Set facilityIdsNotUpdated = facilityService.changeStatusToFederalAccepted(masterSelectedList, currentUser);
			   setFacilityNotChangeAttribute(facilityIdsNotUpdated,masterSelectedList,request);
			   masterSelectedList = clearMasterSelectedList(pSession);
			   if(facilityIdsNotUpdated != null && facilityIdsNotUpdated.size() > 0){
				   masterSelectedList = facilityIdsNotUpdated;
				   pSession.setAttribute("masterSelectedList", masterSelectedList);
			   }
		   }else if(facilityDAO.STATE_CORRECTION_REQUESTED.equalsIgnoreCase(action)){
			   masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
			   Set facilityIdsNotUpdated = facilityService.changeStatusToStateCorrectionRequested(masterSelectedList, currentUser);
			   setFacilityNotChangeAttribute(facilityIdsNotUpdated,masterSelectedList,request);
			   masterSelectedList = clearMasterSelectedList(pSession);
			   if(facilityIdsNotUpdated != null && facilityIdsNotUpdated.size() > 0){
				   masterSelectedList = facilityIdsNotUpdated;
				   pSession.setAttribute("masterSelectedList", masterSelectedList);
			   }
		   }else if(facilityDAO.STATE_ASSIGNED.equalsIgnoreCase(action)){
			   masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
			   Set facilityIdsNotUpdated = facilityService.changeStatusToStateAssigned(masterSelectedList, currentUser);
			   setFacilityNotChangeAttribute(facilityIdsNotUpdated,masterSelectedList,request);
			   masterSelectedList = clearMasterSelectedList(pSession);
			   if(facilityIdsNotUpdated != null && facilityIdsNotUpdated.size() > 0){
				   masterSelectedList = facilityIdsNotUpdated;
				   pSession.setAttribute("masterSelectedList", masterSelectedList);
			   }
		   }else if(ReviewStatusRefService.STATE_REQUESTED_RETURN.equalsIgnoreCase(action)){
			   masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
			   Set facilityIdsNotUpdated = facilityService.changeStatusToStateRequestedReturn(masterSelectedList, currentUser);
			   setFacilityNotChangeAttribute(facilityIdsNotUpdated,masterSelectedList,request);
			   masterSelectedList = clearMasterSelectedList(pSession);
			   if(facilityIdsNotUpdated != null && facilityIdsNotUpdated.size() > 0){
				   masterSelectedList = facilityIdsNotUpdated;
				   pSession.setAttribute("masterSelectedList", masterSelectedList);
			   }
		   }else if(ReviewStatusRefService.DELETED.equalsIgnoreCase(action)){
			   masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
			   Set facilityIdsNotUpdated = facilityService.changeStatusToDeleted(masterSelectedList, currentUser);
			   setFacilityNotChangeAttribute(facilityIdsNotUpdated,masterSelectedList,request);
			   masterSelectedList = clearMasterSelectedList(pSession);
			   if(facilityIdsNotUpdated != null && facilityIdsNotUpdated.size() > 0){
				   masterSelectedList = facilityIdsNotUpdated;
				   pSession.setAttribute("masterSelectedList", masterSelectedList);
			   }
		   }else if (ACTION_REQUEST_ACCESS.equalsIgnoreCase(action)){
			   masterSelectedList = updateMasterSelectedList(facilityListForm, pSession);
			   boolean isSuccess = facilityService.requestAccess(masterSelectedList, currentUser);
			   masterSelectedList = clearMasterSelectedList(pSession);
			   if(!isSuccess){
				   if(currentUser!=null){
					   log.error(currentUser.getFirstName() + " " + currentUser.getLastName() + " could not request access to facilities");   
				   }else{
					   log.error("A unknown user could not be given access to facilities");
				   }				   
			   }
		   }
   		    
		   /* Execute the search */
   		   
   		 log.debug("search.getSearchType()= " + search.getSearchType());
		 
   		if(search.getSearchType().equalsIgnoreCase(Search.SEARCH_TYPE_CODED_SEARCH)){
   		 
   			 log.debug("QUERY_TYPE_SIMPLE = " +  QUERY_TYPE_SIMPLE);
   			 Collection  facilityList = facilityService.getFacilitiesIds(search);

   			 if(facilityList==null){
   				 throw new ApplicationException("search on null or empty facility list");
   			 }

   			 // Store total facilities in session for use by ACTION_LAST
   			 pSession.setAttribute("totalFacilities", new Integer(facilityList.size()));

   			 //fetch the columns

   			 if (facilityListHelpers.isEmpty()) {
   				 // we only get here if the new search method was NOT used.
   				 if(facilityList.size()!=0){


   					 ////////////////////////////////////////////////////////////////////////
   					 //TEMPORARY CODE - This code should be removed once the Facility Search 
   					 //                 PL/SQL Function is working. This code limits the 
   					 //                  result set to 4000.
   					 int maxSize = 500;
   					 if (facilityList.size() > maxSize && 
   							 (currentUser.getCurrentRole().getLocationTypeId().equals("Federal") ||
   									 currentUser.getCurrentRole().getLocationTypeId().equals("Regional"))) {

   						 Collection shortList = new ArrayList();
   						 Iterator iter = facilityList.iterator();
   						 for (int i = 0; i < maxSize; i++) {
   							 Long fac = (Long)iter.next();
   							 shortList.add(fac);
   						 }
   						 facilityList = shortList;
   					 }
   					 ///////////////////////////
   					 // TEMPORARY CODE - END  //
   					 ///////////////////////////

   					 facilityListHelpers = facilityService.getFacilities(facilityList, search);

   					 //set number of facilities according to facility List search
   					 facilityListForm.setNumOfFacilities(facilityList.size());

   				 }
   			 }
   		 }
   		 
   		 else if(search.getSearchType().equalsIgnoreCase(Search.SEARCH_TYPE_STORE_FUNCTION_ADVANCE_SEARCH)){
   			 log.debug("QUERY_TYPE = " + search.getQueryName());
   			 facilityListHelpers = facilityDAO.executeAdvancedSearch(search);
   			 facilityListForm.setNumOfFacilities(search.getNumOfFacilities());
   			 pSession.setAttribute("totalFacilities", new Integer(search.getNumOfFacilities()));
   		 }
   		 else if(search.getSearchType().equalsIgnoreCase(Search.SEARCH_TYPE_STORE_FUNCTION_KEYWORD_SEARCH)){
   			 log.debug("QUERY_TYPE = " + search.getQueryName());
  			 facilityListHelpers = facilityDAO.executeKeywordSearch(search);
  			 facilityListForm.setNumOfFacilities(search.getNumOfFacilities());
  			 pSession.setAttribute("totalFacilities", new Integer(search.getNumOfFacilities()));
   		 }
   		 else{
   			 log.debug("Empty Search");
   			 facilityListHelpers = new ArrayList();
   		 }
   		 
   		   
		   
		   
   		   // The following line is for testing purposes only
   		   
   		   // Save the Master Selected List to the database
   		   facilityService.updateCwnsUserSetting(currentUser.getUserAndRole(),masterSelectedList);
 		   
    	   /* Set values in the Struts Form Bean */
   		   facilityListForm.setSearchDescription(search.getDescription());
    	   facilityListForm.setFromFacility(startIndex);
    	   facilityListForm.setToFacility(startIndex+facilityListHelpers.size()-1);
    	   facilityListForm.setNextFacilityToDisplay(startIndex+facilityListHelpers.size());
    	   facilityListForm.setPrevFacilityToDisplay(startIndex-FacilityService.MAX_RESULTS);
    	   //facilityListForm.setNumOfFacilities(new Long(facilityList.size()));
    	   facilityListForm.setFacHelpers(facilityListHelpers);
    	   facilityListForm.setImportExportKey(currentUser.getUserAndRole());
    	   facilityListForm.setFacilityIds((String[]) masterSelectedList.toArray(new String[0]));
    	   facilityListForm.setAct("");
    	   facilityListForm.setSortColumnDescription(search.getSortColumnDescription());
    	   facilityListForm.setSortOrderDescription(search.getSortOrderDescription());
    	   
    	   /* set Session values */
    	   pSession.setAttribute("currentDisplayList", getFacilityIdList(facilityListHelpers));
    	   pSession.setAttribute("startIndex", new Integer(startIndex));
    	//} catch (Exception e) {
        //	throw e;
        //}    	
    	
    	   
    	   if(locationTypeId.equalsIgnoreCase(userService.LOCATION_TYPE_ID_STATE)
    			 &&	facilityService.isCwnsUserHasAccessLevel(currentUser, AccessLevelRefDAO.SUBMIT_FOR_FEDERAL_REVIEW)
    			 && !facilityService.isCurrentDateAfterDataEntryEndDate(currentUser)){
    		   request.setAttribute(DISPLAY_FRR, DISPLAY_FRR);
    	   }
    	   
    	   if(locationTypeId.equalsIgnoreCase(userService.LOCATION_TYPE_ID_STATE)
    			   && (facilityService.isCwnsUserHasAccessLevel(currentUser, AccessLevelRefDAO.FACILITY_UPDATE) 
    					   || facilityService.isCwnsUserHasAccessLevel(currentUser, AccessLevelRefDAO.SUBMIT_FOR_STATE_REVIEW))){
    		   request.setAttribute(DISPLAY_LA, DISPLAY_LA);
    		   request.setAttribute(DISPLAY_DE, DISPLAY_DE);
    	   }
    	   
    	   if(locationTypeId.equalsIgnoreCase(userService.LOCATION_TYPE_ID_FEDERAL)
    			   && facilityService.isCwnsUserHasAccessLevel(currentUser, AccessLevelRefDAO.FEDERAL_REVIEW)){
    		   request.setAttribute(DISPLAY_FA, DISPLAY_FA);
    		   request.setAttribute(DISPLAY_SCR, DISPLAY_SCR);
    		   request.setAttribute(DISPLAY_SA, DISPLAY_SA);
    		   request.setAttribute(DISPLAY_SRRT, DISPLAY_SRRT);
    	   }
    	   
    	   if(locationTypeId.equalsIgnoreCase(userService.LOCATION_TYPE_ID_LOCAL)){
    		   request.setAttribute(DISPLAY_REQUEST_ACCESS, DISPLAY_REQUEST_ACCESS);
    	   }
    		   
    	   
    	saveToken(request);
    
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
			String key ="help."+currentUser.getCurrentRole().getLocationTypeId()+".facilitylist";
		    String defaultkey = "help.facilitylist"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		request.setAttribute("helpKey", helpKey);
		
        return mapping.findForward("success");
  }


	/**
	 * Update the Master list of Selected Facilities in the session object. 
	 * @param facilityListForm
	 * @param pSession
	 * @return
	 * 		The updated master list
	 */
	private Set updateMasterSelectedList(FacilityListForm facilityListForm, HttpSession pSession) {
		
	    //TODO:might loose selected facilities on a page if not submitted (i.e. URL is used)
		
		// Facilities just displayed 
		Set currentDisplayList = (Set) pSession.getAttribute("currentDisplayList");
		
		// KLUDGE - if the currentDisplayList 
		//          is null the session was lost 
		if ( currentDisplayList == null) {
			return clearMasterSelectedList(pSession);
		}

		// Facilities previously selected
		Set masterSelectedList = (Set) pSession.getAttribute("masterSelectedList");

		// Facilities just selected
		String[] selectedFacilities = facilityListForm.getFacilityIds();
		
		// Remove facilities just displayed to the user from the Master Selected List 
		masterSelectedList.removeAll(currentDisplayList);

		// Add facilities the user just selected to the Master Selected List 
		masterSelectedList.addAll(Arrays.asList(selectedFacilities));
		return masterSelectedList;
	}

	/**
	 * Clear the Master list of Selected Facilities in the session object. 
	 * @param pSession
	 * @return
	 * 		An empty Set
	 */
	private Set clearMasterSelectedList(HttpSession pSession) {
		Set masterSelectedList = new HashSet(); 
		pSession.setAttribute("masterSelectedList", masterSelectedList);
		return masterSelectedList;
	}
	/**
	 * Iterate through a Collection of facilityListHelper objects and return a 
	 * Set of facilityIds.
	 *  
	 * @param facilityListHelpers
	 * @return
	 */
	private Set getFacilityIdList(Collection facilityListHelpers) {
		Set facilityIds = new HashSet();
		for (Iterator iter = facilityListHelpers.iterator(); iter.hasNext();) {
			FacilityListHelper facility = (FacilityListHelper) iter.next();
			facilityIds.add(facility.getFacId());			
		}
		return facilityIds;
	}

   
    
    
    /**
     * 
     * @param FacilityList
     * @return
     */
	private Set convertLongToString(Collection FacilityList){
		Set arr  = new HashSet();
		for (Iterator iter = FacilityList.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			arr.add(id.toString());			
		}
		return arr;		
	}
	
	
	private Set getFacilityId(Collection FacilityList)
	{
		Set arr = new HashSet();
		for (Iterator iter = FacilityList.iterator(); iter.hasNext();) {
			String id = ((FacilityListHelper)iter.next()).getFacId();
			arr.add(id);
		}
		return arr;
	}
	
	private Map getSimpleQueryProperties(CurrentUser currentUser, String keyword)
	{
		log.debug("getSimpleQueryProperties()");
		
		Map queryProperties =  new HashMap();
		String qrReviewStatus = keyword;
		String qrFacName = keyword;
		String qrCounty = keyword;
		String qrState = "";
		String qrSortColumn = "";
		String qrFacDesc = keyword;
		String qrCWNSNumber = keyword;
		String qrAuthority = keyword;
		String qrFacilityId = "";
		
		String locationId = currentUser.getCurrentRole().getLocationId();
		String locationType = currentUser.getCurrentRole().getLocationTypeId();
		
		if(locationType.equalsIgnoreCase(UserService.LOCATION_TYPE_ID_FEDERAL)){
			qrState = "HQ";
		}
		else{
			qrState = locationId;
		}
		
		
		queryProperties.put(FacilityDAO.REVIEW_STATUS,qrReviewStatus);
		queryProperties.put(FacilityDAO.FACILITY_NAME, qrFacName);
		queryProperties.put(FacilityDAO.COUNTY,qrCounty);
		queryProperties.put(FacilityDAO.LOCATION_ID, qrState.trim());
		queryProperties.put(FacilityDAO.SORT_COLUMN,qrSortColumn);
		queryProperties.put(FacilityDAO.CWNS_NUMBER,qrCWNSNumber);
		queryProperties.put(FacilityDAO.FACILITY_DESCRIPTION, qrFacDesc);
		queryProperties.put(FacilityDAO.AUTHORITY, qrAuthority);
		queryProperties.put(FacilityDAO.FACILITY_ID,qrFacilityId);
		
		return queryProperties;
	}
	
	private Map getDefaultQueryProperties(CurrentUser currentUser)
	{
		log.debug("getDefaultQueryProperties()");
		Map queryProperties =  new HashMap();
		String qrReviewStatus = "";
		String qrFacName = "";
		String qrOverAllType = "";
		String qrAuthority = "";
		String qrSysname = "";
		String qrCounty = "";
		String qrWatershed = "";
		String qrNeedTotal = "";
		String qrNotChanged = "";
		String qrPermitNumber = "";
		String qrState = "";
		String qrErrorStatus = "";
		String qrDocDate = "";
		String qrPresFlowCount = "";
		String qrPresPopulationCount = "";
		String qrDocType = "";
		String qrSortColumn = "";
		String qrFacilityId="";
		
		
		String locationId = currentUser.getCurrentRole().getLocationId();
		String locationType = currentUser.getCurrentRole().getLocationTypeId();
		
		if(locationType.equalsIgnoreCase(UserService.LOCATION_TYPE_ID_FEDERAL)){
			qrState = "DC";
		}
		else{
			qrState = locationId;
		}
		
		/*if(locationType.equalsIgnoreCase(UserService.LOCATION_TYPE_ID_LOCAL)){
			if(!currentUser.getCurrentRole().isLimited()){
				qrReviewStatus = "LAS,LIP,SRR,SAP";
			}			
		}*/
		
		
		queryProperties.put(FacilityDAO.REVIEW_STATUS,qrReviewStatus);
		queryProperties.put(FacilityDAO.FACILITY_NAME, qrFacName);
		queryProperties.put(FacilityDAO.OVERALL_NATURE_TYPE,qrOverAllType);
		queryProperties.put(FacilityDAO.AUTHORITY, qrAuthority);
		queryProperties.put(FacilityDAO.SYSTEM_NAME, qrSysname);
		queryProperties.put(FacilityDAO.COUNTY,qrCounty);
		queryProperties.put(FacilityDAO.WATERSHED, qrWatershed);
		queryProperties.put(FacilityDAO.NEEDS, qrNeedTotal);
		queryProperties.put(FacilityDAO.FACILTITY_NOT_CHANGED,qrNotChanged);
		queryProperties.put(FacilityDAO.PERMIT_NUMBER, qrPermitNumber.trim());
		queryProperties.put(FacilityDAO.LOCATION_ID, qrState.trim());
		queryProperties.put(FacilityDAO.ERROR_STATUS, qrErrorStatus.trim());	
		queryProperties.put(FacilityDAO.DOCUMENT_DATE,qrDocDate);
		queryProperties.put(FacilityDAO.PRESENT_FLOW_COUNT,qrPresFlowCount);
		queryProperties.put(FacilityDAO.PRESENT_POPULATION_COUNT,qrPresPopulationCount);
		queryProperties.put(FacilityDAO.SORT_COLUMN,qrSortColumn);
		queryProperties.put(FacilityDAO.DOCUMENT_TYPE, qrDocType);
		queryProperties.put(FacilityDAO.FACILITY_ID,qrFacilityId);		
		return queryProperties;
	}
	
	
	/**
	 * Create a HashMap of all the Advanced Search properties 
	 * @param prr
	 * @param userId
	 * @return
	 */
	private Map getAdvanceQueryProperties(PortletRenderRequest prr) {
		log.debug("getAdvanceQueryProperties");
		Map queryProperties =  new HashMap();
    	
		String qrReviewStatus = "";
		String qrFacName = "";
		String qrOverAllType = "";
		String qrAuthority = "";
		String qrSysname = "";
		String qrCounty = "";
		String qrWatershed = "";
		String qrNeedTotal = "";
		String qrNotChanged = "";
		String qrPermitNumber = "";
		String qrState = "";
		String qrErrorStatus = "";
		String qrDocDate = "";
		String qrPresFlowCount = "";
		String qrPresPopulationCount = "";
		String qrDocType = "";
		String qrFacilityId = "";
		String qrSortColumn = ""; // By default, FACILITY_NAME ASC
		
		//facilityId
		queryProperties.put(FacilityDAO.FACILITY_ID,qrFacilityId);
		
		// Review Status
        String[] reviewStatus  = prr.getParameterValues(FacilityDAO.REVIEW_STATUS);		        
	    if(reviewStatus!=null && reviewStatus.length>0){
	    	qrReviewStatus = "";
	    	for (int i=0;i < reviewStatus.length; i++) {
	    		if (qrReviewStatus.length()>0) qrReviewStatus += ",";
	    		qrReviewStatus += reviewStatus[i].trim();
	    	}
	    }
	    queryProperties.put(FacilityDAO.REVIEW_STATUS,qrReviewStatus);
	    
    	// Facility Name
    	String facName = prr.getParameter(FacilityDAO.FACILITY_NAME);
    	if(facName!=null && !"".equals(facName)){
    		qrFacName = facName.trim();
    	}
    	queryProperties.put(FacilityDAO.FACILITY_NAME, qrFacName);
    	
    	
    	// Overall Facility Nature Type
    	String[] overAllType  = prr.getParameterValues(FacilityDAO.OVERALL_NATURE_TYPE);
    	if(overAllType!=null && overAllType.length>0){
    		qrOverAllType = "";
    		for (int i=0; i < overAllType.length; i++) {
    			if (qrOverAllType.length()>0) qrOverAllType += ",";
    			qrOverAllType += overAllType[i];
    		}	
    	}
    	queryProperties.put(FacilityDAO.OVERALL_NATURE_TYPE,qrOverAllType);
    	
    	// Authority
    	String authority  = prr.getParameter(FacilityDAO.AUTHORITY);
    	if(authority!=null && !"".equals(authority)){
    		qrAuthority  = authority.trim();
    	}
    	queryProperties.put(FacilityDAO.AUTHORITY, qrAuthority);	
    	
    	
    	// System Name
    	String sysname = prr.getParameter(FacilityDAO.SYSTEM_NAME);
    	if(sysname !=null && !"".equals(sysname )){
    		qrSysname  = sysname.trim();
    	}
    	queryProperties.put(FacilityDAO.SYSTEM_NAME, qrSysname);	
    	
	
    	// County
    	String county    = prr.getParameter(FacilityDAO.COUNTY);
    	if(county!=null && !"".equals(county)){
    		qrCounty  = county.trim();
    	}
    	queryProperties.put(FacilityDAO.COUNTY,qrCounty);	
    	
    	
    	// Watershed
    	String watershed    = prr.getParameter(FacilityDAO.WATERSHED);
    	if(watershed!=null && !"".equals(watershed)){
    		qrWatershed  = watershed.trim();
    	}
    	queryProperties.put(FacilityDAO.WATERSHED, qrWatershed);	
    	
    	
    	// TODO: must speak to Ganish regarding needs
    	// Needs
    	String needTotal      = prr.getParameter(FacilityDAO.NEEDS);
    	String needOperator = prr.getParameter(FacilityDAO.NEEDS_OPER);
    	if(needTotal!=null && !"".equals(needTotal)){
    		log.debug("needTotal - " + needTotal);
    		if(needOperator!=null && !"".equals(needOperator)){
    			if("gt".equals(needOperator)){
    				qrNeedTotal=">" + needTotal.trim();

    			}else if("lt".equals(needOperator)){
    				qrNeedTotal="<" + needTotal.trim();

    			}else{
    				qrNeedTotal="=" + needTotal.trim();	
    			}
    		}
    	}
    	queryProperties.put(FacilityDAO.NEEDS, qrNeedTotal);



    	// Facility not changed
    	String notChanged = prr.getParameter(FacilityDAO.FACILTITY_NOT_CHANGED);
    	String facProjectOperator  = prr.getParameter(FacilityDAO.FACILITY_PROJECT_OPER);
    	if(notChanged!=null && !"".equals(notChanged)){

    		try{
    			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    			Date notChangedDate = (Date)formatter.parse(notChanged.trim());
    			if(facProjectOperator!=null && !"".equals(facProjectOperator)){
    				if("gt".equals(facProjectOperator)){
    					qrNotChanged  = ">" + notChanged.trim();
    				}
    				else if("lt".equals(facProjectOperator)){
    					qrNotChanged  = "<" + notChanged.trim();
    				}
    				else{
    					qrNotChanged  = "=" + notChanged.trim();
    				}
    			}
    				
    		}catch(ParseException pe){
    			log.error("Invalid facility not changed since date  :" + notChanged, pe);
    		}

    	}
    	queryProperties.put(FacilityDAO.FACILTITY_NOT_CHANGED,qrNotChanged);
    	
    	// Permit Number
    	String permitNumber = prr.getParameter(FacilityDAO.PERMIT_NUMBER);
    	if(permitNumber !=null && !"".equals(permitNumber)){
    		qrPermitNumber  = permitNumber.trim();
    	}
    	queryProperties.put(FacilityDAO.PERMIT_NUMBER, qrPermitNumber.trim());	
    	
    	
    	// LocationId 	
    	String state = prr.getParameter(FacilityDAO.LOCATION_ID);
    	if(state !=null && !"".equals(state)){
    		qrState  = state.trim();
    	}
    	queryProperties.put(FacilityDAO.LOCATION_ID, qrState.trim());	
    	
    	
    	// Error status   		    	
    	String errorStatus = prr.getParameter(FacilityDAO.ERROR_STATUS);
    	if(errorStatus !=null && !"".equals(errorStatus)){
    		qrErrorStatus  = errorStatus.trim();
    	}
    	queryProperties.put(FacilityDAO.ERROR_STATUS, qrErrorStatus.trim());	
    	 		    	

    	// document Date
    	String docDate  = prr.getParameter(FacilityDAO.DOCUMENT_DATE);
    	String docOperator = prr.getParameter(FacilityDAO.DOCUMENT_OPER);
    	if(docDate!=null && !"".equals(docDate)){
    		try{
	    		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	    		Date documentDate = (Date)formatter.parse(docDate.trim());
	    		if(docOperator!=null && !"".equals(docOperator)){
	    			if("gt".equals(docOperator)){
	    				qrDocDate = ">" + docDate.trim();
    				}
    				else if("lt".equals(docOperator)){
    					qrDocDate = "<" + docDate.trim();
    				}
    				else{
    					qrDocDate = "=" + docDate.trim();
    				}
	    		}
	    		queryProperties.put(FacilityDAO.DOCUMENT_DATE,docDate);	
    		}catch(ParseException pe){
    			log.error("Invalid document publish date  :" + docDate, pe);
    		}
    	}
    	queryProperties.put(FacilityDAO.DOCUMENT_DATE,qrDocDate);
    	
    	/*Present Population
    	String presPopulationOperator = prr.getParameter(FacilityDAO.PRESENT_POPULATION_OPER);
    	String presPopulationCount = prr.getParameter(FacilityDAO.PRESENT_POPULATION_COUNT);
    	if(presPopulationOperator!=null && !"".equalsIgnoreCase(presPopulationOperator.trim())){
    		if(presPopulationCount!=null && !"".equalsIgnoreCase(presPopulationCount.trim())){
    			
    		}
    	}
    	
    	*/
    	
    	
    	String presPopulationType = prr.getParameter(FacilityDAO.PRESENT_POPULATION_TYPE);
    	String presPopulationCount = prr.getParameter(FacilityDAO.PRESENT_POPULATION_COUNT);
    	String presPopulationOperator = prr.getParameter(FacilityDAO.PRESENT_POPULATION_OPER);
    	if(presPopulationOperator!=null && !"".equalsIgnoreCase(presPopulationOperator.trim())){
    		if(presPopulationCount!=null && !"".equalsIgnoreCase(presPopulationCount.trim())){
    			if(presPopulationType!=null && !"".equalsIgnoreCase(presPopulationType.trim())){
    				if("gt".equals(presPopulationOperator)){
    					presPopulationOperator = ">";
    				}
    				else if("lt".equals(presPopulationOperator)){
    					presPopulationOperator = "<";
    				}
    				else{
    					presPopulationOperator = "=";
    				}
    				qrPresPopulationCount =  presPopulationType.trim() + presPopulationOperator.trim() + presPopulationCount.trim();
    			}
    		}
    	}
    	queryProperties.put(FacilityDAO.PRESENT_POPULATION_COUNT,qrPresPopulationCount);	
    			
    			
    			
    	String presFlowType = prr.getParameter(FacilityDAO.PRESENT_FLOW_TYPE);
    	String presFlowCount = prr.getParameter(FacilityDAO.PRESENT_FLOW_COUNT);
    	String presFlowOperator = prr.getParameter(FacilityDAO.PRESENT_FLOW_OPER);
    	if(presFlowOperator!=null && !"".equalsIgnoreCase(presFlowOperator.trim())){
    		if(presFlowCount!=null && !"".equalsIgnoreCase(presFlowCount.trim())){
    			if(presFlowType!=null && !"".equalsIgnoreCase(presFlowType.trim())){
    				if("gt".equals(presFlowOperator)){
    					presFlowOperator = ">";
    				}
    				else if("lt".equals(presFlowOperator)){
    					presFlowOperator = "<";
    				}
    				else{
    					presFlowOperator = "=";
    				}
    				qrPresFlowCount =  presFlowType.trim() + presFlowOperator.trim() + presFlowCount.trim();
    			}
    		}
    	}	
    	queryProperties.put(FacilityDAO.PRESENT_FLOW_COUNT,qrPresFlowCount);
    	
    	
    	
    	// Document Type
    	String docType = prr.getParameter(FacilityDAO.DOCUMENT_TYPE);
    	if(docType !=null && !"".equals(docType)){
    		qrDocType  = docType.trim();
    	}
    	queryProperties.put(FacilityDAO.DOCUMENT_TYPE, qrDocType);
    	
    	
    	queryProperties.put(FacilityDAO.SORT_COLUMN,qrSortColumn);
    	
    	return queryProperties;
	}
	
	public void setFacilityNotChangeAttribute(Set facilityIdsNotUpdated, Set masterSelectedList,  HttpServletRequest request){
		String errorMessage;
		int totalProcessedFacilities = masterSelectedList.size() - facilityIdsNotUpdated.size();
		errorMessage = "<strong>Total Processed Facilities:</strong> " + totalProcessedFacilities + "<br><br>";
		errorMessage = errorMessage + "<strong>Total Error Facilities:</strong> " +  facilityIdsNotUpdated.size() + "<br><br>";
		errorMessage = errorMessage + "<b>The error facilities are listed by their CWNS Number:<b> <br><br>";
		if(facilityIdsNotUpdated.size() > 0){
			Iterator iter = facilityIdsNotUpdated.iterator();
			Facility facility;
			String cwnsNumber;
			while (iter.hasNext()) {
				facility = facilityDAO.findByFacilityId((String)iter.next());
				cwnsNumber = facility.getCwnsNbr();
				errorMessage = errorMessage + cwnsNumber + "<br>";
			}
			request.setAttribute("facilityIdsNotUpdated", errorMessage);
			
		}
			
		
	}
	
	/* set the facility service */
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }

    /* Set the FacilityDAO */
	private FacilityDAO facilityDAO;
	public void setFacilityDAO(FacilityDAO dao){
		facilityDAO = dao;
	}	
	
	private UserService userService;	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}