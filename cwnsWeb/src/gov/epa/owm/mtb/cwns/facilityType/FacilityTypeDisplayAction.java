package gov.epa.owm.mtb.cwns.facilityType;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityTypeRef;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class FacilityTypeDisplayAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		PortletRenderRequest prr = (PortletRenderRequest)
				request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		    
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
		
		
		//get the facility Id
		Long facilityId = new Long(0);
		if(prr.getParameter("facilityId")!=null) facilityId = new Long(prr.getParameter("facilityId"));
		log.debug("req.getParameter(facilityId): " + prr.getParameter("facilityId"));
		log.debug("FacilityId=" + facilityId);	
		
		//fetch the collection of Facility Types Associated with the facility and Display
		Collection facilityTypes=facilityTypeService.getFacityType(facilityId);
		
		boolean updatable = facilityService.isUpdatable(user, facilityId);
		boolean isFacility = facilityService.isFacility(facilityId);
		request.setAttribute("facilityTypes", facilityTypes);
		request.setAttribute("updatable",(updatable? "true" : "false"));
		request.setAttribute("isFacility",(isFacility? "true" : "false"));
		Facility f = facilityService.findByFacilityId(facilityId.toString());
		request.setAttribute("isFeedback",(f.getVersionCode()=='F'? "true" : "false"));
		
		log.debug("Facility Types: " + facilityTypes.size());
		log.debug("updatable: " + (updatable? "true" : "false"));
		
		//get a collections of other types that can be added
		Collection availablefacilityTypes=facilityTypeService.getAvailableFacityType(facilityId);
		request.setAttribute("availablefacilityTypes", availablefacilityTypes);
		
		//get a collections of all the changes
		Collection changeTypes=facilityTypeService.getChangeTypes();
		request.setAttribute("changeTypes", changeTypes);
		
		//set the change Type rules
		Collection changeTypeRules=facilityTypeService.getChangeTypesRules();
		request.setAttribute("changeTypeRules", changeTypeRules);
		
		//	set the facilityType Change rules
		Collection facilityTypeChangeRules=facilityTypeService.getFacilityTypeChangeRules();
		request.setAttribute("facilityTypeChangeRules", facilityTypeChangeRules);
		
		String mode = (String)request.getAttribute("save_mode");
		DynaValidatorForm  facilityTypeForm = (DynaValidatorForm)form;
		if(mode!=null && !"errors".equals(mode)){			
			facilityTypeForm.set("mode", "list");			
		}
		if("edit".equals(facilityTypeForm.get("mode"))){
			String facilityTypeId= (String) facilityTypeForm.get("facilityType");
			FacilityTypeRef facilityTypeRef=facilityTypeService.getFacilityTypeRef(new Long(facilityTypeId));
			request.setAttribute("facilityTypeRef", facilityTypeRef);
		}
		log.debug("The mode is set to!!!: "+facilityTypeForm.get("mode"));
		//log.debug("The mode is set to!!!: "+request.getAttribute("mode"));
		//log.debug("The status is set to!!!: "+facilityTypeForm.get("status"));
		
		return mapping.findForward("success");
	}
	
    /* set the facility service via Spring injection*/
    private FacilityTypeService facilityTypeService;
    public void setFacilityTypeService(FacilityTypeService fts){
       facilityTypeService = fts;    	
    }	
    
    /* set the facility service via Spring injection*/
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }	
    
}
