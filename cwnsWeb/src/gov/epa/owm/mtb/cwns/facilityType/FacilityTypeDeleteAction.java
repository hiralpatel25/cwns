package gov.epa.owm.mtb.cwns.facilityType;

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

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

public class FacilityTypeDeleteAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//Delete facility Type
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
    
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);

		//get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(request.getParameter("facilityId")!=null){
			facilityId = new Long(request.getParameter("facilityId"));
		}
		log.debug("FacilityId=" + facilityId);
		if(facilityId.equals(new Long(0))){
			throw new ApplicationException("Facility Id is needed to delete the facility type");
		}
		
		//get the facilityType Id
		Long facilityTypeId = new Long(0);
		if(prr!=null && prr.getParameter("facilityTypeId")!=null) {
			facilityTypeId = new Long(prr.getParameter("facilityTypeId"));
		}else if(request.getParameter("facilityTypeId")!=null){
			facilityTypeId = new Long(request.getParameter("facilityTypeId"));
		}
		log.debug("FacilityTypeId=" + facilityTypeId);
		if(facilityTypeId == new Long(0)){
			throw new ApplicationException("Facility Id is needed to delete the facility Type");
		}
		
		//if feedback then toggle softdelete
		Facility f = facilityService.findByFacilityId(facilityId.toString());
		if(f!=null && f.getVersionCode()=='F'){
			//softdelete
			facilityTypeService.softDeleteFacilityType(facilityId, facilityTypeId, user.getUserId());
			return mapping.findForward("success");
		}
		
		//get confirmDelete
		String confirmDelete= request.getParameter("confirmDelete");
		
		if(confirmDelete==null){
			ArrayList messages = new ArrayList();
			boolean isDeleted = facilityTypeService.isDataDeleted(facilityId, facilityTypeId, messages, user.getUserId());
			if(isDeleted){
				DeleteDataAreaHelper ddah= new DeleteDataAreaHelper(messages, facilityTypeId);  
				request.setAttribute("warnDataAreaDelete", ddah);  //get confirmation
				return mapping.findForward("success");
			}
		}else if("N".equals(confirmDelete)){
			return mapping.findForward("success"); //do nothing
		}
		
		// delete facility type object
		ArrayList errors = new ArrayList();
		boolean success = facilityTypeService.deleteFacilityType(facilityId, facilityTypeId, errors, user.getUserId());
		if(!success && errors.size()>0){
		  request.setAttribute("errors", errors);
		}
		
		log.debug("Deleted facility Type Id:" + facilityTypeId);
		facilityService.performPostSaveUpdates(facilityId,FacilityService.DATA_AREA_FACILITY, user);
		
		return mapping.findForward("success");
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

}
