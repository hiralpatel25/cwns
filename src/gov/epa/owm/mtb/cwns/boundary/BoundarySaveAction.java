package gov.epa.owm.mtb.cwns.boundary;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.GeographicAreaBoundary;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

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

public class BoundarySaveAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		BoundaryForm boundaryForm = (BoundaryForm)form;
        // check if the action is or duplicate request 
		if(isCancelled(request) || !isTokenValid(request, true)){
			log.debug("in cancel---");
			boundaryForm.setBoundaryType("");
			boundaryForm.setBoundaryName("");
			boundaryForm.setMode("list");
			request.setAttribute("save_mode", "cancel");
			return mapping.findForward("success");
					
		} 
		
		//	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
      //	get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(boundaryForm.getFacilityId()!=null){
			facilityId = boundaryForm.getFacilityId();
		}
				
		long  boundaryId = boundaryForm.getBoundaryId();
			
		// check for duplicate boundary
		GeographicAreaBoundary g = facilityAddressService.getGeographicAreaBoundary(new Long(boundaryId),facilityId);
		if (g != null){
			ActionErrors errors = new ActionErrors();
		    errors.add("boundary", new ActionError("error.geographicareaboundary.boundaryexists"));
		    saveErrors(request, errors);
		    request.setAttribute("save_mode", "errors");
		    saveToken(request);
		    return mapping.findForward("success");
		}
		
		facilityAddressService.saveGeographicAreaBoundary(facilityId, new Long(boundaryId), user);
				
		resetToken(request);
		boundaryForm.setMode("list");
		request.setAttribute("save_mode", "success");
		//fesManager.runValidation(facilityId, FacilityService.DATA_AREA_GEOGRAPHIC, user.getUserId());
		facilityService.performPostSaveUpdates(facilityId, FacilityService.DATA_AREA_GEOGRAPHIC, user);
		return mapping.findForward("success");
	}
	
      //  set the facility address service
	  private FacilityAddressService facilityAddressService;
	  
	  public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	  }
	  
        //  set the facility service
	    private FacilityService facilityService;
	    
	    public void setFacilityService(FacilityService fs){
	       facilityService = fs;    	
	    } 

}
