package gov.epa.owm.mtb.cwns.facilityType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

public class FacilityTypeAddAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//		 set the form varaibles
		DynaValidatorForm  facilityTypeForm = (DynaValidatorForm)form;
		facilityTypeForm.set("facilityType", "");
		facilityTypeForm.set("status", FacilityTypeService.FACILITY_TYPE_STATUS_BOTH);
		facilityTypeForm.set("selectedChanges", new String[0]);
		facilityTypeForm.set("availableChanges", new String[0]);
		facilityTypeForm.set("mode", "add");
		//save token
		saveToken(request);
		
		return mapping.findForward("success");
	}
}
