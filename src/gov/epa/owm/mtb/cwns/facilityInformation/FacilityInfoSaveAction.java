package gov.epa.owm.mtb.cwns.facilityInformation;

import java.util.Collection;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

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

public class FacilityInfoSaveAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	        throws Exception {
		  ActionErrors actionerrors = new ActionErrors();
		  FacilityInformationForm facilityinfoform = (FacilityInformationForm)form;
		  PortletRenderRequest prr = (PortletRenderRequest)
		  request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		  		  
		  if (isCancelled(request)){
			  return mapping.findForward("success");
		  }
           //Get user object
			HttpServletRequest httpReq = (HttpServletRequest) request;
			HttpSession httpSess = httpReq.getSession();
			CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
			String cwnsNbr = "";
			boolean isNewFacility = false;
			if("0".equals(facilityinfoform.getSurveyFacilityId().trim()))isNewFacility = true;
			//check is a facility with the same name exists in the same location
			if(isNewFacility){
				Facility ff = facilityService.findByFacilityNumber(facilityinfoform.getCwnsNbr());
				if(ff!=null){
					actionerrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.facilityinfo.facilityNumberexists"));
				}
				//cwnsNbr = facilityinfoform.getCwnsNbr();
			}
			else
				cwnsNbr = facilityService.getCWNSNbrByFacilityId(facilityinfoform.getSurveyFacilityId());
			//check for facility name
			Collection facilities = facilityService.getFacilityByNameAndState(facilityinfoform.getFacilityName(),facilityinfoform.getLocationId(), cwnsNbr);
	
			if (facilities!=null && !facilities.isEmpty()){
				actionerrors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.facilityinfo.nameexists"));
			}
			if(actionerrors.size()>0){
				//get stateId
				StateRef s = facilityService.getStateByLocationId(facilityinfoform.getLocationId());
				if(s!= null){
				     log.debug("State Id" + s.getStateId());
					 request.setAttribute("stateId", s.getStateId());
				 }else{
					 log.error("Error: unable to retrive state for locationId:" + facilityinfoform.getLocationId());
				}
				request.setAttribute("facilityinfo", facilityinfoform); 
				saveErrors(request,actionerrors);
				return new ActionForward(mapping.getInput());
			}
			
			Facility f = facilityService.saveFacilityInfo(facilityinfoform.getSurveyFacilityId(),facilityinfoform.getFacilityName(),
					                  facilityinfoform.getDescription(),facilityinfoform.getSystemName(),
					                  facilityinfoform.getOwnerCode(),facilityinfoform.getMilitaryFlag(),user, facilityinfoform.getCwnsNbr(),facilityinfoform.getLocationId(),
					                  facilityinfoform.getTmdlFlg(), facilityinfoform.getSourceWaterProtectionFlg());

			if(isNewFacility){
				facilityTypeService.setStandardDataAreas(new Long(f.getFacilityId()),user.getUserId());
				request.setAttribute("facility", f); 
				facilityService.performPostSaveUpdates(new Long(f.getFacilityId()), FacilityService.DATA_AREA_FACILITY, user);
				//fesManager.runValidation(new Long(f.getFacilityId()), FacilityService.DATA_AREA_FACILITY, user.getUserId());
				return mapping.findForward("newSuccess");
			}
			facilityService.performPostSaveUpdates(new Long(f.getFacilityId()), FacilityService.DATA_AREA_FACILITY, user);
			//fesManager.runValidation(new Long(f.getFacilityId()), FacilityService.DATA_AREA_FACILITY, user.getUserId());
			return mapping.findForward("success");				
	}
	
	private FESManager fesManager;
    public void setFesManager(FESManager fesm){
    	fesManager=fesm;
    }		
   
	// set the facility service
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
    
    /**
     * 
     */
    public FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService facilityTypeService) {
		this.facilityTypeService = facilityTypeService;
	}
    

}
