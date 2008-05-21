package gov.epa.owm.mtb.cwns.county;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;

public class FacilityCountyAddAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		FacilityCountyForm facilityCountyForm = (FacilityCountyForm)form;
		facilityCountyForm.setCountyName("");
		facilityCountyForm.setFipsCode("");
		facilityCountyForm.setPrimary('N');
		facilityCountyForm.setMode("add");
        // save token
		saveToken(request);     
		return mapping.findForward("success");
	}

}
