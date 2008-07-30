package gov.epa.owm.mtb.cwns.pointofcontact;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PocService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction;
import gov.epa.owm.mtb.cwns.userlist.UserListAction;
import java.util.ArrayList;
import java.util.Collection;

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
public class PointOfContactSearchAction extends CWNSAction {

	public static final String ACTION_KEYWORD_SEARCH   	= "keyword_search";
	public static final String ACTION_DEFAULT   		= "poc_default_action";
	public static final String ACTION_NEXT 				= "next";
	public static final String ACTION_PREVIOUS			= "previous";
	public static final String ACTION_FIRST				= "first";
	public static final String ACTION_LAST				= "last";
	public static final String ACTION_ASSOCIATE_WITH_FACILITY
														= "associate_with_facility";
	
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

   		PortletRenderRequest prr = (PortletRenderRequest)
   				request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   		
   		PointOfContactSearchForm pocsForm = (PointOfContactSearchForm) form;

	    //TODO: Check if provider session works in the portal environment
   		//ProviderSession pSession = prr.getSession();
	    HttpSession pSession = request.getSession(false);
		//ProviderSession pSession = prr.getSession();	    
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }

	    /* Determine the action to take. */  		    
	    String action = null;
  		if (prr.getParameter("action") != null && !"".equals(prr.getParameter("action"))) {
   			action = prr.getParameter("action");
   		}else {
   			action=ACTION_DEFAULT;
   		}

  		log.debug("ACTION = "+action);
  		CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
  		
  		String facilityId = prr.getParameter("facilityId");
  		Facility facility = facilityService.findByFacilityId(facilityId);

  		pocsForm.setFacilityId(facilityId);
  		if ("".equals(pocsForm.getStateId())) {
  	  		pocsForm.setStateId(facility.getLocationId());
  		}

  		// Determine what part of the list needs to be displayed
  		if (ACTION_FIRST.equals(action)) {
  	  		pocsForm.setStartIndex(1);
  		} else if (ACTION_PREVIOUS.equals(action)) {
  			int startIndex = pocsForm.getStartIndex();
  			startIndex = startIndex - PocService.MAX_RESULTS;
  			if (startIndex < 1) {
  				startIndex = 1;
  			}
  	  		pocsForm.setStartIndex(startIndex);
  	  		
  		} else if (ACTION_NEXT.equals(action)) {
  			int startIndex = pocsForm.getStartIndex();
  			startIndex = startIndex + PocService.MAX_RESULTS;
  			if (startIndex > pocsForm.getListSize()) {
  				startIndex = pocsForm.getListSize();
  			}
  	  		pocsForm.setStartIndex(startIndex);
  			
  		} else if (ACTION_LAST.equals(action)) {
  			int startIndex;
   			int listSize = pocsForm.getListSize();
   			if (listSize % PocService.MAX_RESULTS == 0) {
   				startIndex = listSize - PocService.MAX_RESULTS +1;
   			}else {
   				startIndex = listSize - 
	    					(listSize % PocService.MAX_RESULTS) +1;
   			}
   			pocsForm.setStartIndex(startIndex);

  		} else if (ACTION_ASSOCIATE_WITH_FACILITY.equals(action)) {
  			pocService.associatePointOfContacts(facilityId,
  												pocsForm.getSelectedPocs(),
  												currentUser.getUserId());
  			facilityService.performPostSaveUpdates(new Long(facilityId), FacilityService.DATA_AREA_POC, currentUser);  			
  		}
  		
  		
  		Collection pocList = new ArrayList();
  		int listSize;
  		if (ACTION_KEYWORD_SEARCH.equals(action)) {
  		    pSession.setAttribute("POC_KEYWORD", pocsForm.getKeyword());
	   		pocsForm.setStartIndex(1);
  		} else {
  	  		// Get previous Search keyword
  		    String keyword = (String)pSession.getAttribute("POC_KEYWORD");
  		    if (keyword != null && keyword.trim().length() > 0 ) {
  	  			pocsForm.setKeyword(keyword);
  		    }
  		}
  		
  	  	pocList =   pocService.getPocSearchList(
	  				facility.getFacilityId(),
	  				pocsForm.getStateId(),
	  				pocsForm.getKeyword(),
	  				pocsForm.getStartIndex()-1, 
	  				PocService.MAX_RESULTS);
  	  	
	  	listSize = pocService.getPocSearchListCount(
	  				facility.getFacilityId(),
	  				pocsForm.getStateId(),
	  				pocsForm.getKeyword());

  		pocsForm.setListSize(listSize);
  		pocsForm.setSearchType(action);
		pocsForm.setSelectedPocs(null);
		
  		int endIndex = (pocsForm.getStartIndex() - 1) + pocList.size();
  		pocsForm.setEndIndex(endIndex);

		// Determine if the user has update privileges
  		pocsForm.setUpdateable(facilityService.isUpdatable(currentUser, new Long(facilityId)));

		request.setAttribute("pocsForm", pocsForm);
		request.setAttribute("pocList", pocList);

		// Create state list 
  		Collection states =   facilityService.getStates();
		request.setAttribute("states", states );
			String key ="help."+currentUser.getCurrentRole().getLocationTypeId()+".pointofcontactsearch";
		    String defaultkey = "help.pointofcontactsearch"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		request.setAttribute("helpKey", helpKey);
		
		return mapping.findForward("success");
	}

    /* set the facility service */
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }

    /* set the Point of Contact service */
    private PocService pocService;
    public void setPocService(PocService ps){
       pocService = ps;    	
    }

}


