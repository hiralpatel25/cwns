package gov.epa.owm.mtb.cwns.permits;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FacilityPermitDeleteAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
     		
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		FacilityPermitForm facilityPermitForm = (FacilityPermitForm)form;
    	
        // 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
		//get the permit Id
		Long permitId = new Long(0);
		if(prr !=null && prr.getParameter("permitId")!=null){
			permitId = new Long(prr.getParameter("permitId"));
		}else if(request.getParameter("permitId")!=null){
			permitId = new Long(request.getParameter("permitId"));
		}
		if(permitId.equals(new Long(0))){
			throw new ApplicationException("Permit Id is needed to delete the Facility Permit");
		}
				
		//get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(facilityPermitForm.getFacilityId()!=null){
			facilityId = facilityPermitForm.getFacilityId();
		}
		log.debug("permitid---"+permitId);		
		facilityPermitService.deleteFacilityPermit(facilityId, permitId, user);
		facilityService.performPostSaveUpdates(facilityId, new Long(11), user);
		
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
