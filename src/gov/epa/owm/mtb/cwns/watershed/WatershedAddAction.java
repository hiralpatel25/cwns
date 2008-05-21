package gov.epa.owm.mtb.cwns.watershed;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.congressionalDistrict.CongressionalDistrictForm;

public class WatershedAddAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		WatershedForm watershedForm = (WatershedForm)form;
		watershedForm.setWatershedName("");
		watershedForm.setWatershedId("");
		watershedForm.setPrimary('N');
		watershedForm.setMode("add");
        // save token
		saveToken(request);     
		return mapping.findForward("success");
	}

}
