package gov.epa.owm.mtb.cwns.pointofcontact;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityPointOfContact;
import gov.epa.owm.mtb.cwns.model.PointOfContact;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PocService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction;
import gov.epa.owm.mtb.cwns.userlist.UserListAction;
import java.util.ArrayList;
import java.util.Collection;

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

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
public class PointOfContactAction extends CWNSAction {
	
	public static final String ACTION_DEFAULT_QUERY   = "default_query";
	public static final String ACTION_EDIT_POC        = "edit_poc";
	public static final String ACTION_UPDATE_POC      = "update_poc";
	public static final String ACTION_ADD_POC         = "add_poc";
	public static final String ACTION_PROCESS_NEW_POC = "process_new_poc";
	public static final String ACTION_DELETE          = "delete";
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

   		PortletRenderRequest prr = (PortletRenderRequest)
   				request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	    PointOfContactForm pocf = (PointOfContactForm) form;
        ActionErrors errors = new ActionErrors();

	    //TODO: Check if provider session works in the portal environment
   		//ProviderSession pSession = prr.getSession();
	    HttpSession pSession = request.getSession(false);
		//ProviderSession pSession = prr.getSession();	    
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }

	    /* Determine the action to take. */  		    
	    String action = null;
	    if (pocf.getAction().trim().length() > 0 ) {
	    	action = pocf.getAction();
	    } else {
   			action=ACTION_DEFAULT_QUERY;
	    }

	    log.debug("action = "+action);

  		CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);

		String facilityId = prr.getParameter("facilityId");
  		
  		// If this is the first time the user has landed on the page  
  		// set some default values in the form bean
  		if (pocf.getStateId().length() < 1) {
  			Facility facility = facilityService.findByFacilityId(facilityId);
  			pocf.setStateId(facility.getLocationId());
  			pocf.setFacilityId(facilityId);
  		}
  		
  		if (ACTION_EDIT_POC.equals(action)) {
  			
  	  		// Load the form bean with data
  	  		pocService.loadPocForm(facilityId, pocf);
			pocf.setDisplayDetails("Y");
  		} else if (ACTION_UPDATE_POC.equals(action)) {
  			FacilityPointOfContact fpoc = (FacilityPointOfContact)pocService.getFacilityPoc(facilityId, pocf.getEditPocId());
  			if(fpoc!=null && fpoc.getPointOfContact()!=null &&
  					(!pocf.getAuthorityName().trim().equalsIgnoreCase(fpoc.getPointOfContact().getAuthorityName())||
  					 !pocf.getContactName().trim().equalsIgnoreCase(fpoc.getPointOfContact().getName())||
  					 !pocf.getCity().trim().equalsIgnoreCase(fpoc.getPointOfContact().getCity())||
  					 !pocf.getStateId().trim().equalsIgnoreCase(fpoc.getPointOfContact().getStateId())))
  			   errors = validateBusinessRules(pocf);
			if (errors.isEmpty()) {
	  			pocService.updatePoc(pocf,currentUser.getUserId());
	  			pocf.clear();
	  			facilityService.performPostSaveUpdates(new Long(facilityId), FacilityService.DATA_AREA_POC, currentUser);
			} else {
				pocf.setDisplayDetails("Y");
			}
  		} else if (ACTION_ADD_POC.equals(action)) {
  			pocf.clear();
  			pocf.setSuperfundPossible(pocService.isResponsiblePartyEnabled(facilityId));
  			pocf.setAction(ACTION_ADD_POC);
			pocf.setDisplayDetails("Y");
		} else if (ACTION_PROCESS_NEW_POC.equals(action)) {

			errors = validateBusinessRules(pocf);
			if (errors.isEmpty()) {
				pocService.addNewPoc(pocf,currentUser.getUserId());
	  			facilityService.performPostSaveUpdates(new Long(facilityId), FacilityService.DATA_AREA_POC, currentUser);
			} else {
				pocf.setAction(ACTION_ADD_POC);
				pocf.setDisplayDetails("Y");
			}
		} else if (ACTION_DELETE.equals(action)){
			pocService.deletePoc(pocf, currentUser);
  			facilityService.performPostSaveUpdates(new Long(facilityId), FacilityService.DATA_AREA_POC, currentUser);
		} else {
			// default query
			pocf.setEditPocId("0"); 
		}
  		
		// Get data for jsp Select boxes
  		Collection states =   facilityService.getStates();
		request.setAttribute("states", states );
		
		// Get POC list 
		request.setAttribute("pocs", pocService.getPocListForFacility(facilityId));
		
		// Determine if the user has update privileges
		pocf.setUpdateable(facilityService.isUpdatable(currentUser, new Long(facilityId)));
		
		// Get the primary POC 
		pocf.setPrimaryPocId(pocService.getPirmaryPocIdForFacility(facilityId));
		
    	request.setAttribute("localUser", UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())?"true":"false");

		request.setAttribute("pocForm", pocf);
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		return mapping.findForward("success");
	}

	/**
	 * Perform business rule validation. 
	 * @param pocf
	 * @return
	 */
	private ActionErrors validateBusinessRules(PointOfContactForm pocf) {

		ActionErrors errors = new ActionErrors();

		//if (!ACTION_UPDATE_POC.equals(pocf.getAction())) {
	        if (!pocService.isAuthorityNameUniqueWithinState(pocf.getStateId(), 
														     pocf.getAuthorityName().trim(),pocf.getCity().trim(),
														     pocf.getContactName().trim())) {
	            // verify Authority Name is unique within the state
	        	ActionError error = new ActionError("error.poc.authority");
	        	errors.add(ActionErrors.GLOBAL_ERROR,error);
	        }    
		//}
		
		if (pocf.getCountyName().trim().length() > 0) {
	        if (!pocService.doesCountyExist(pocf.getStateId(), 
	        		                        pocf.getCountyName())) {
	        	ActionError error = new ActionError("error.poc.county");
	        	errors.add(ActionErrors.GLOBAL_ERROR,error);
	        }
		}
        
        return errors;
	}

	
    /* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
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

    private FESManager fesManager;
    public void setFesManager(FESManager fesm){
    	fesManager=fesm;
    }
    
}


