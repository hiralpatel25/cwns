package gov.epa.owm.mtb.cwns.permits;

import java.util.Collection;
import java.util.Iterator;

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
import gov.epa.owm.mtb.cwns.model.FacilityPermit;
import gov.epa.owm.mtb.cwns.model.Permit;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.common.Entity;

public class FacilityPermitEditAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		FacilityPermitForm facilityPermitForm = (FacilityPermitForm)form;
    	
        // 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
		boolean excludePermitType = true;
		//get the permit Id
		Long permitId = new Long(0);
		if(prr !=null && prr.getParameter("permitId")!=null){
			permitId = new Long(prr.getParameter("permitId"));
		}else if(request.getParameter("permitId")!=null){
			permitId = new Long(request.getParameter("permitId"));
		}
		if(permitId.equals(new Long(0))){
			throw new ApplicationException("Permit Id is needed to edit the Facility Permit");
		}
				
		//get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(facilityPermitForm.getFacilityId()!=null){
			facilityId = facilityPermitForm.getFacilityId();
		}
		boolean permitUsedByAnotherFac = false;				
		FacilityPermit facilityPermit = facilityPermitService.getFacilityPermitByPrimaryKey(permitId, facilityId);
		if (facilityPermit != null){
			facilityPermitForm.setUseData(facilityPermit.getUsedForFacilityLocatnFlag());
			Permit permit = facilityPermit.getPermit();
			if (permit!=null){
				facilityPermitForm.setPermitId(permit.getPermitId());
				facilityPermitForm.setPermitNumber(permit.getPermitNumber());
				facilityPermitForm.setCurrentPermitNbr(permit.getPermitNumber());
				permitUsedByAnotherFac = facilityPermitService.isPermitAlreadyUsed(facilityId, permit.getPermitNumber());
				if (permitUsedByAnotherFac){
				   facilityPermitForm.setConfirm('Y');
				}  
				if (permit.getPermitTypeRef()!=null){
					facilityPermitForm.setPermitType(facilityPermit.getPermit().getPermitTypeRef().getName());
					facilityPermitForm.setPermitTypeId(facilityPermit.getPermit().getPermitTypeRef().getPermitTypeId());
					facilityPermitForm.setType_NPDES(facilityPermit.getPermit().getPermitTypeRef().getNpdesFlag());
				}
			}
		}
		if (facilityPermitForm.getPermitTypeId() == 40)
			excludePermitType = false;
		Collection permitTypes = facilityPermitService.getPermitTypes(excludePermitType);
		facilityPermitForm.setPermitTypes(permitTypes);
		facilityPermitForm.setMode("edit");
		
        //	save token
		saveToken(request);
		
		return mapping.findForward("success");
	 }
	
    //  set the facility address service
    private FacilityPermitService facilityPermitService;
            
    public void setFacilityPermitService(FacilityPermitService facilityPermitService) {
		this.facilityPermitService = facilityPermitService;
	}

}
