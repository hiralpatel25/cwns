package gov.epa.owm.mtb.cwns.permits;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FacilityPermitDisplayAction extends CWNSAction {
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	        throws Exception {
	    	
	    	PortletRenderRequest prr = (PortletRenderRequest)
		      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
	    	
	    	FacilityPermitForm facilityPermitForm = (FacilityPermitForm)form;
	    	
            // 	Get user object
			HttpServletRequest httpReq = (HttpServletRequest) request;
			HttpSession httpSess = httpReq.getSession();
			CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
			
	        //	get the facility ID from the page parameter - configured as input parameter in provider.xml
			Long facilityId = new Long(0);
			if(prr.getParameter("facilityId")!=null) facilityId = new Long(prr.getParameter("facilityId"));
			            
	    	Collection permits = facilityPermitService.getPermitsByFacilityId(facilityId);
	    	facilityPermitForm.setPermits(permits);
	    	//	 Check if facility id updatable or not and set form attribute
		    if (facilityService.isUpdatable(user, facilityId)){
		       facilityPermitForm.setIsUpdatable("Y");
			}
		    String mode = (String)request.getAttribute("save_mode");
			if(mode!=null && !"errors".equals(mode)){			
				facilityPermitForm.setMode("list");	
				facilityPermitForm.setPermitId(0);
			}
			if(mode!=null && "errors".equals(mode)){
			  if("edit".equals(facilityPermitForm.getMode())){
				boolean excludePermitType = false;
				if (facilityPermitForm.getPermitTypeId() == 40)
					excludePermitType = true;
				Collection permitTypes = facilityPermitService.getPermitTypes(excludePermitType);
				facilityPermitForm.setPermitTypes(permitTypes);
			  }
			  if("add".equals(facilityPermitForm.getMode())){
				Collection permitTypes = facilityPermitService.getPermitTypes(true);
				facilityPermitForm.setPermitTypes(permitTypes);
			  }
			}
	    	request.setAttribute("isNonLocalUser", user.isNonLocalUser()?"true":"false");
	    	request.setAttribute("facilityPermitForm", facilityPermitForm);
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
