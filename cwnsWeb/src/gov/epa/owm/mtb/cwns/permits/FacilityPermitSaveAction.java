package gov.epa.owm.mtb.cwns.permits;

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

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class FacilityPermitSaveAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		FacilityPermitForm facilityPermitForm = (FacilityPermitForm)form;
		
      //	check if the action is or duplicate request 
		if(isCancelled(request) || !isTokenValid(request, true)){
			facilityPermitForm.setMode("list");
			request.setAttribute("save_mode", "cancel");
		    return mapping.findForward("success");
		} 
		
		//	 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
      //	get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(facilityPermitForm.getFacilityId()!=null){
			facilityId = facilityPermitForm.getFacilityId();
		}
		String permitNumber = facilityPermitForm.getPermitNumber().trim();
		char usedForFacilityLoca = facilityPermitForm.getUseData();
		String mode = facilityPermitForm.getMode();
		long permitId = facilityPermitForm.getPermitId();
		long permitTypeId = facilityPermitForm.getPermitTypeId();
		char type_NPDES = facilityPermitForm.getType_NPDES();
		String useLocaData = facilityPermitForm.getAutoPopulate();
		String currentPermitNbr = facilityPermitForm.getCurrentPermitNbr();
		
		 if("add".equalsIgnoreCase(mode)){
                if (facilityPermitService.isDuplicatePermit(facilityId, permitNumber)){
					ActionErrors errors = new ActionErrors();
				    errors.add("permit", new ActionError("error.facilitypermit.permitexists"));
				    saveErrors(request, errors);
				    request.setAttribute("save_mode", "errors");
				    saveToken(request);
				    return mapping.findForward("success");
				    //return new ActionForward(mapping.getInput());
				}
			 facilityPermitService.addFacilityPermit(facilityId, permitTypeId, permitNumber, user);
		 }
		 else{
		     if (type_NPDES == 'Y'){
			   // if permit type is NPDES update facility permit for usedForFacilityLocation Flag
			   facilityPermitService.updateFacilityPermit(facilityId, permitId, new Character(usedForFacilityLoca), user);
		       if ("Y".equalsIgnoreCase(useLocaData)){
		    	 facilityPermitService.createOrUpdateInfo(facilityId, user, permitId);
		       }else{
		    	   facilityPermitService.updatePORAndFacilityAddress(facilityId, user);
		       }
		     } else{
		    	 if(!permitNumber.equalsIgnoreCase(currentPermitNbr)){
		    	 if (facilityPermitService.isDuplicatePermit(facilityId, permitNumber)){
		    		    ActionErrors errors = new ActionErrors();
					    errors.add("permit", new ActionError("error.facilitypermit.permitexists"));
					    saveErrors(request, errors);
					    request.setAttribute("save_mode", "errors");
					    saveToken(request);
					    return mapping.findForward("success");
					    //return new ActionForward(mapping.getInput());
				 }
		    	 }
			  // if permit type is Other update facility permit
		      facilityPermitService.updateFacilityPermit(facilityId, permitId, permitTypeId, permitNumber, user);
		     } 
		 }    
		facilityService.performPostSaveUpdates(facilityId, FacilityService.DATA_AREA_PERMITS, user);
		resetToken(request);
		facilityPermitForm.setMode("list");
		request.setAttribute("save_mode", "success");
		return mapping.findForward("success");
	}
	
    //  set the facility address service
    private FacilityPermitService facilityPermitService;
//  set the facility service
    private FacilityService facilityService;
    
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
            
    public void setFacilityPermitService(FacilityPermitService facilityPermitService) {
		this.facilityPermitService = facilityPermitService;
	}

}
