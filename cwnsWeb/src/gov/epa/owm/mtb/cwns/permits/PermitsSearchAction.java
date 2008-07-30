package gov.epa.owm.mtb.cwns.permits;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

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

public class PermitsSearchAction extends CWNSAction {

	public static final String ACTION_DEFAULT       	= "default";
	public static final String ACTION_KEYWORD_SEARCH   	= "keyword_search";
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
   		
   		PermitsSearchForm permitsSearchForm = (PermitsSearchForm) form;
	    
	    HttpSession httpSess = request.getSession(false);
		        
  		/* Determine the action to take. */ 
	    String action = null;
  		if (permitsSearchForm.getAction()!=null && !"".equals(permitsSearchForm.getAction())){
  			action = permitsSearchForm.getAction();
  		}else {
  			action=ACTION_DEFAULT;
  		}
  		log.debug("ACTION = "+action);
  		CurrentUser currentUser = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
  		
        // get the facility ID from the page parameter - configured as input parameter in provider.xml
		Long facilityId = new Long(0);
		if(prr.getParameter("facilityId")!=null) facilityId = new Long(prr.getParameter("facilityId"));
  		  		
  		Facility facility = facilityService.findByFacilityId(facilityId.toString());
  		
  		// User has arrived at the page for the first time
		httpSess.setAttribute("PERMIT_KEYWORD", null);
  		permitsSearchForm.setFacilityId(facilityId);
  		permitsSearchForm.setStateId(facility.getLocationId());
  		//permitsSearchForm.setStartIndex(1);
  		
  		// Determine what part of the list needs to be displayed
  		if (ACTION_FIRST.equals(action)) {
  			permitsSearchForm.setStartIndex(1);
  		} else if (ACTION_PREVIOUS.equals(action)) {
  			int startIndex = permitsSearchForm.getStartIndex();
  			startIndex = startIndex - FacilityPermitService.MAX_RESULTS;
  			if (startIndex < 1) {
  				startIndex = 1;
  			}
  			permitsSearchForm.setStartIndex(startIndex);
  	  		
  		} else if (ACTION_NEXT.equals(action)) {
  			int startIndex = permitsSearchForm.getStartIndex();
  			startIndex = startIndex + FacilityPermitService.MAX_RESULTS;
  			if (startIndex > permitsSearchForm.getListSize()) {
  				startIndex = permitsSearchForm.getListSize();
  			}
  			permitsSearchForm.setStartIndex(startIndex);
  			
  		} else if (ACTION_LAST.equals(action)) {
  			int startIndex;
   			int listSize = permitsSearchForm.getListSize();
   			if (listSize % FacilityPermitService.MAX_RESULTS == 0) {
   				startIndex = listSize - FacilityPermitService.MAX_RESULTS +1;
   			}else {
   				startIndex = listSize - 
	    					(listSize % FacilityPermitService.MAX_RESULTS) +1;
   			}
   			permitsSearchForm.setStartIndex(startIndex);

  		} else if (ACTION_ASSOCIATE_WITH_FACILITY.equals(action)) {
  			facilityPermitService.associatePermits(facilityId,
  					permitsSearchForm.getSelectedPermits(),
  												currentUser.getUserId());
  			facilityService.performPostSaveUpdates(facilityId, new Long(11), currentUser);
  		}
  		
  		
  		Collection permitsList = new ArrayList();
  		int listSize;
  		  		
  		if (ACTION_KEYWORD_SEARCH.equals(action)) {
  			log.debug("keyword---"+permitsSearchForm.getKeyword());
  			httpSess.setAttribute("PERMIT_KEYWORD", permitsSearchForm.getKeyword());
  		    permitsSearchForm.setStartIndex(1);
  		} else {
  	  		// Get previous Search keyword
  			String keyword = (String)httpSess.getAttribute("PERMIT_KEYWORD");
  			if (keyword != null && keyword.trim().length() > 0 ) {
  			permitsSearchForm.setKeyword(keyword);
  			}
  		}
  		log.debug("facilityId = "+facilityId+" "+permitsSearchForm.getStateId()+" "+permitsSearchForm.getKeyword()+" "+permitsSearchForm.getStartIndex()+" "+FacilityPermitService.MAX_RESULTS);
  	  	permitsList =   facilityPermitService.getPermitSearchList(
  	  				facilityId,
  	  			    permitsSearchForm.getStateId(),
	  				permitsSearchForm.getKeyword(),
	  				permitsSearchForm.getStartIndex()-1, 
	  				FacilityPermitService.MAX_RESULTS);
  	  	
	  	listSize = facilityPermitService.getPermitSearchListCount(
	  				facilityId,
	  				permitsSearchForm.getStateId(),
	  				permitsSearchForm.getKeyword());

	  	permitsSearchForm.setListSize(listSize);
	  	permitsSearchForm.setSearchType(action);
	  	permitsSearchForm.setSelectedPermits(null);
		
  		int endIndex = (permitsSearchForm.getStartIndex() - 1) + permitsList.size();
  		permitsSearchForm.setEndIndex(endIndex);

        // Check if facility id updatable or not and set form attribute
	    if (facilityService.isUpdatable(currentUser, facilityId)){
	    	permitsSearchForm.setIsUpdatable("Y");
		}
		request.setAttribute("permitsSearchForm", permitsSearchForm);
		request.setAttribute("permitsList", permitsList);
		
			String key ="help."+currentUser.getCurrentRole().getLocationTypeId()+".NPDESpermitsearch";
		    String defaultkey = "help.NPDESpermitsearch"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		request.setAttribute("helpKey", helpKey);
				
		return mapping.findForward("success");
	}

    /* set the facility service */
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }

   //  set the facility address service
    private FacilityPermitService facilityPermitService;
    public void setFacilityPermitService(FacilityPermitService facilityPermitService) {
		this.facilityPermitService = facilityPermitService;
	}

}
