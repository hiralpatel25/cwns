package gov.epa.owm.mtb.cwns.userlist;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
public class UserListAction extends CWNSAction {

	public static final String ACTION_SIMPLE_QUERY="simple";
	public static final String ACTION_DISPLAY="display";
	
	public static final String ACTION_NEXT="next";
	public static final String ACTION_PREVIOUS="previous";
	public static final String ACTION_FIRST="first";
	public static final String ACTION_LAST="last";
	public static final String ACTION_SORT="sort";
	public static final String ACTION_DEFAULT_QUERY="default";
	public static final String ACTION_USER_DEFAULT_SEARCH = "default user search";
	public static final String ACTION_PENDING_USERS="pending_users";

	public static final String ACTION_SEARCH   = "search";
	
	public static final String QUERY_TYPE_SEARCH="simple";
	public static final String QUERY_TYPE_DEFAULT="default";

	public static final String QUERY_TYPE_DEFAULT_DESC="Users";
	public static final String QUERY_TYPE_PENDING_DESC="Pending Users";
	public static final String QUERY_TYPE_SEARCH_DESC="List based on User Search citeria";

	public static final String SORT_COLUMN_NAME ="lastName";
	public static final String SORT_COLUMN_ROLE ="id.cwnsNbr";
	public static final String SORT_COLUMN_STATUS ="id.cwnsNbr";
	
	public static final String DEFAULT_SORT_COLUMN=SORT_COLUMN_NAME;

	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserListForm ulForm = (UserListForm)form;

    	try {   	
    		PortletRenderRequest prr = (PortletRenderRequest)
    		      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   		    //TODO: Check if provider session works in the portal environment
    		//ProviderSession pSession = prr.getSession();
   		    HttpSession pSession = request.getSession(false);
   		    if(pSession==null){
   		    	throw new ApplicationException("Unable to fetch provider session");	
   		    }
 
   		    /* User Information  */
   		    CurrentUser user = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
  		    
            //   get the user query Type
   		    String userQueryType = (String) pSession.getAttribute("userQueryType");
   		    
   		    //set sort criteria
   		    SortCriteria sc = new SortCriteria(DEFAULT_SORT_COLUMN, SortCriteria.ORDER_ASCENDING);
   		    
   		    // get the start index from the session
   		    Integer strtIndx =(Integer)pSession.getAttribute("userStartIndex"); 
   		    int startIndex = (strtIndx != null) ? strtIndx.intValue() : 1;  
   		        		
   		    /* Determine the action to take. First check the form bean
   		     * and then (if necessary) check the request object. */  		    
   		    String action = null;
    		if (prr.getParameter("action") != null && !"".equals(prr.getParameter("action"))) {
    			action = prr.getParameter("action");
    		}else {
    			action=ACTION_DISPLAY;
	    	}

   		    log.debug("action = "+action);  		    
   		    Search search=null;
   		    
   		    /* Determine the query criteria */
   		    if (ACTION_SEARCH.equals(action)) {

  		    	//reset the list properties
   		    	startIndex=1;
   		    	ulForm.setCurrentSelection("");

   		    	//get the parameters and construct the hashmap
   	   		    Map queryProperties =  new HashMap();
		        log.debug("User Search Parameters");   		    	
		        
		        // Keyword
   		    	String userKeyword = prr.getParameter("keyword");
	    		queryProperties.put("userKeyword", userKeyword.trim());	
	    		log.debug("userKeyword = " + userKeyword);
 		        
   		    	// Location
   		    	String locId = prr.getParameter("locationId");
	    		queryProperties.put("locationId", locId.trim());	
	    		log.debug("Location Id = " + locId);

   		    	// Type
   		    	String userType = prr.getParameter("userType");
   		    	if ("all".equalsIgnoreCase(userType)) {
   		    		queryProperties.put("locationTypeId", "");	
   		    	} else {
   		    		queryProperties.put("locationTypeId", userType.trim());	
   		    	}
	    		log.debug("userType = " + userType);
   		    	
   		    	// Status
   		    	String userStatus = prr.getParameter("userStatus");
	    		queryProperties.put("userStatus", userStatus.trim());	
	    		log.debug("userStatus = " + userStatus);
  		    	
   		    	// Access Levels
   		        String[] accessLevels  = prr.getParameterValues("AccessLevel");		          		    	
	    		queryProperties.put("accessLevels",accessLevels);	
	    		//log.debug("accessLevels - " + accessLevels.toString());
   		    	
	   		    search = userService.getUserSearch(user,queryProperties, sc,0,UserService.MAX_RESULTS);
   				pSession.setAttribute("userQueryType", QUERY_TYPE_SEARCH);
   				pSession.setAttribute("selectedUser", "");

   		    } else if (ACTION_DEFAULT_QUERY.equals(action) || 
   		    		   UserListAction.ACTION_USER_DEFAULT_SEARCH.equals(action) ||
   		    		   userQueryType == null) {

   		    	startIndex = 1;
   		    	pSession.setAttribute("selectedUser", "");
   		    	
				search = userService.getDefaultSearch(user, sc, startIndex -1, UserService.MAX_RESULTS);
				
				pSession.setAttribute("userQueryType", QUERY_TYPE_DEFAULT);
				
   			} else if (UserDetailsAction.ACTION_DISPLAY_SELECTED_USER_INFO.equals(action)) {
   				 String selectedUser = prr.getParameter("cwnsUserId");
   				 pSession.setAttribute("selectedUser", selectedUser);
  				 search = (Search) pSession.getAttribute("userSearch");
   			} else { // use previously created Search object
   				search = (Search) pSession.getAttribute("userSearch");
   				
   			}   	
   		       		    
   		    if(search == null){
   		    	throw new ApplicationException("Unable to find search/construct user search condition");
   		    }
   		    // Save the search 
			pSession.setAttribute("userSearch", search);
   		    
   		    //determine the list properties
   		    if (ACTION_NEXT.equals(action)) {
   		    	startIndex=ulForm.getNextUserToDisplay();
   		    	search.setStartIndex(startIndex-1);

   		    } else if (ACTION_PREVIOUS.equals(action)) {
   		    	startIndex=ulForm.getPrevUserToDisplay();
   		    	search.setStartIndex(startIndex-1);
   		    	
   		    } else if (ACTION_FIRST.equals(action)) {
   		    	startIndex=1;
   		    	search.setStartIndex(startIndex-1);
   		    	
   		    } else if (ACTION_LAST.equals(action)) {
   	   			int totalUsers = ((Integer) pSession.getAttribute("totalUsers")).intValue();
   	   			if (totalUsers % FacilityService.MAX_RESULTS == 0) {
   	   				startIndex = totalUsers - UserService.MAX_RESULTS +1;
   	   			}else {
   	   				startIndex = totalUsers - 
   		    					(totalUsers % UserService.MAX_RESULTS - 1);
   	   			}
   		    	search.setStartIndex(startIndex -1);
   		    	
   		    } else if (ACTION_SORT.equals(action)) {
   		    	sc = new SortCriteria(ulForm.getUserSortColumn(),SortCriteria.ORDER_ASCENDING);

   		    	// Get the previous sort criteria 
   		    	Collection scs = search.getSortCriteria();
   		    	Iterator iter = scs.iterator();
   		    	SortCriteria oldSc = (SortCriteria)iter.next();

   		    	// See if the user changed the sort column
   		    	if (sc.getColumn().equals(oldSc.getColumn())) {
   		    		// Do not change the sort order if the user did a refresh.
   		    		if (isTokenValid(request)) {
   		   		    	ArrayList a = new ArrayList();
   		    			oldSc.changeOrder();
   		    			a.add(oldSc);   		    			
   		   		    	search.setSortCriteria(a);
   	   	   		    	startIndex = 1;
   	   	   		    	search.setStartIndex(startIndex -1);
   		    		}
   		    	} else {
   		    		// Change the sort column
   	   		    	ArrayList a = new ArrayList();
   		    		sc.setOrder(SortCriteria.ORDER_ASCENDING);
   	   		    	a.add(sc);
   	   		    	search.setSortCriteria(a);
   	   		    	startIndex = 1;
   	   		    	search.setStartIndex(startIndex -1);
   	   		    	ulForm.setCurrentSelection("");
   		    	}
   		    	pSession.setAttribute("selectedUser", "");
		    }
 		    
	    	String selectedUser = (String)pSession.getAttribute("selectedUser");
	    	// This will cause the user information to be highlighted 
	    	// in the User List portlet.
	    	if (selectedUser != null && selectedUser.length() > 0) {
	    		ulForm.setCurrentSelection(selectedUser);
	    	}
	    	
	    	if (!QUERY_TYPE_SEARCH_DESC.equals(search.getDescription())) {
	    		// This is a default search so we must recreate the search object to 
	    		// determine whether to display Active & Pending users or just Pending
	    		// users.
				search = userService.getDefaultSearch(user, sc, startIndex -1, UserService.MAX_RESULTS);
				pSession.setAttribute("userQueryType", QUERY_TYPE_DEFAULT);
	    	} 
	    	
			/* Execute the search  to obtain a list of User Ids*/
	   		Collection  userList = userService.getUserIds(search);
	   		   
	   		// Store total total users in session for use by ACTION_LAST
	   		pSession.setAttribute("totalUsers", new Integer(userList.size()));

	   		
	   		   
  		   //fetch the columns
   		   Collection userListHelpers = new ArrayList();;
   		   if(userList.size()!=0){
   			  userListHelpers = userService.getUserListHelpers(userList, search);
   		   }	   

		   
    	   /* Set values in the Form Bean */
   		   ulForm.setSearchDescription(search.getDescription());
   		   ulForm.setFrmUser(startIndex);
   		   ulForm.setToUser(startIndex+userListHelpers.size()-1);
   		   ulForm.setNextUserToDisplay(startIndex+userListHelpers.size());
   		   ulForm.setPrevUserToDisplay(startIndex-UserService.MAX_RESULTS);
   		   ulForm.setNumOfUsers(userList.size());
   		   ulForm.setUlHelpers(userListHelpers);
 
   		   prr.setAttribute("ulForm", ulForm);
   		   
    	   /* set Session values */
    	   pSession.setAttribute("userStartIndex", new Integer(startIndex));
		   pSession.setAttribute("totalUsers", new Integer(userList.size()));
       		
		   		String key ="help."+user.getCurrentRole().getLocationTypeId()+".userlist";
			    String defaultkey = "help.userlist"; 
			    String helpKey = CWNSProperties.getKey(key, defaultkey); 		
		   request.setAttribute("helpKey", helpKey);	
		   saveToken(request);

       	} catch (Exception e) {
        	throw e;
        }    	
       	    
   		return mapping.findForward("success");
	}
	
    /* set the User service */
    private UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }
	

}


