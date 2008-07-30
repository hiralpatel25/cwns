package gov.epa.owm.mtb.cwns.facilityType;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import org.apache.struts.validator.DynaValidatorForm;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.ChangeTypeRef;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

public class FacilityTypeSaveAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// get the form varaibles
		DynaValidatorForm  facilityTypeForm = (DynaValidatorForm)form;
		String mode = (String)facilityTypeForm.get("mode");
		
		//check if the action is or duplicate request 
		if(isCancelled(request) || !isTokenValid(request, true)){
			facilityTypeForm.set("facilityType", "");
			facilityTypeForm.set("mode", "list");
			facilityTypeForm.set("status", FacilityTypeService.FACILITY_TYPE_STATUS_BOTH);
			facilityTypeForm.set("selectedChanges", new String[0]);
			facilityTypeForm.set("availableChanges", new String[0]);
			facilityTypeForm.set("presentTPType", "N");
			facilityTypeForm.set("projectedTPType", "N");
			facilityTypeForm.set("npsStatus", "");
			
		    request.setAttribute("save_mode", "cancel");
			return mapping.findForward("success");
		}
		
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
    
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
		//		get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(request.getParameter("facilityId")!=null){
			facilityId = new Long(request.getParameter("facilityId"));
		}
		log.debug("FacilityId=" + facilityId);
		
		String facilityTypeId = (String) facilityTypeForm.get("facilityType");
		String status = (String) facilityTypeForm.get("status");
		String[] availableChanges = (String[]) facilityTypeForm.get("availableChanges");
		String[] selectedChanges = (String[]) facilityTypeForm.get("selectedChanges");
		String presentTPType= (String) facilityTypeForm.get("presentTPType");
		String projectedTPType=(String) facilityTypeForm.get("projectedTPType");
		String npsStatus=(String) facilityTypeForm.get("npsStatus");
		
		log.debug("FacilityTypeId: " + facilityTypeId);
		log.debug("status: " + status);
		log.debug("selected Changes: " + selectedChanges.length);
		log.debug("available Changes: " + availableChanges.length);
		if(selectedChanges.length==0){
			ActionErrors errors = new ActionErrors();
		    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("prompt.selectedChanges"));
		    saveErrors(request, errors);
		    request.setAttribute("save_mode", "errors");
		    return mapping.findForward("success");
		}
		
		List selectedChangesList =  Arrays.asList(selectedChanges);
		
		if("add".equals(mode)){
			//add facility Type
			facilityTypeService.addFacilityType(facilityId, new Long(facilityTypeId), status, selectedChangesList,presentTPType,projectedTPType,npsStatus, user.getUserId());
			//check if coordate type is still valid
			AbsoluteLocationPoint alp= facilityAddressService.getFacilityCoordinates(facilityId);
			if(alp!=null){
				if(alp.getCoordinateTypeCode()!=null &&  alp.getCoordinateTypeCode().charValue()!='S' && !isCoordinatesTypeValid(facilityId)){
					facilityAddressService.deleteFacilityCoordinates(alp);
				}
			}
			
		}else{
			//if feedback perform a save without any prompts
			if(facilityService.isFeedBack(facilityId, user)){
				facilityTypeService.updateFacilityTypeForFeedBack(facilityId, new Long(facilityTypeId), status, selectedChangesList, presentTPType,projectedTPType,npsStatus, user.getUserId());
				facilityTypeForm.set("mode", "edit");
				request.setAttribute("save_mode", "success");
				return mapping.findForward("success");				
			}
			
			
			//in edit mode check if any data area information is deleted if so then warn the user
			String confirmDelete = (String) facilityTypeForm.get("confirmDelete");
			if(confirmDelete==null || "".equals(confirmDelete)){
				//check if any dataarea needs to be delete
				ArrayList messages = new ArrayList();
				boolean isDelete = facilityTypeService.isUpdateFacilityTypeDeleteData(facilityId, new Long(facilityTypeId), status, selectedChangesList, user.getUserId(), messages);
				if(isDelete){
					DeleteDataAreaHelper ddah= new DeleteDataAreaHelper(messages, new Long(facilityTypeId));
					//if yes set warnDataAreaDeleteOnEdit and return 
					request.setAttribute("warnDataAreaDeleteOnEdit", ddah);  //get confirmation
					resetToken(request);
					//reset the form	
					facilityTypeForm.set("mode", "edit");
					//selected changes 
					request.setAttribute("selectedChangeTypeRefs", facilityTypeService.getChangeTypeRefs(selectedChangesList));
					request.setAttribute("selectedFacilityTypeId", availableChanges);
					//save token
					saveToken(request);
					return mapping.findForward("success");
				}
				
			}
			//if Yes update or no errors proceed and update the data
			if(!"N".equals(confirmDelete)){
				facilityTypeService.updateFacilityType(facilityId, new Long(facilityTypeId), status, selectedChangesList, presentTPType,projectedTPType,npsStatus, user.getUserId());
			}
			
		}
		
		resetToken(request);

		DynaValidatorForm  facilityTypeFrm = (DynaValidatorForm)form;
		facilityTypeFrm.set("status", "X");
		facilityTypeFrm.set("mode", "list");
		log.debug("set the mode to!!!!:" + facilityTypeFrm.get("mode"));
		request.setAttribute("save_mode", "success");
		request.setAttribute("form", facilityTypeFrm );
		facilityService.performPostSaveUpdates(facilityId,FacilityService.DATA_AREA_FACILITY, user);
		return mapping.findForward("success");
	}
	
	public boolean isCoordinatesTypeValid(Long facilityId){
		//if the facility is a non-point sources
		if(!facilityService.isFacility(facilityId)){
			return true;
		}
		//if a facility has a overall nature of owts and not a waste water
		if(!facilityService.isFacilityWasteWater(facilityId)){
			if(facilityService.isFacilityDecentralized(facilityId)||facilityService.isFacilityStormwater(facilityId)){
				return true;	
			}
		}
		return false;
	}

	
	   
    /* set the facility service via Spring injection*/
    private FacilityTypeService facilityTypeService;
    public void setFacilityTypeService(FacilityTypeService fts){
       facilityTypeService = fts;    	
    }
    
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
    
    private FacilityAddressService facilityAddressService;
	public void setFacilityAddressService(FacilityAddressService fas) {
		facilityAddressService = fas;
	}
    

}
