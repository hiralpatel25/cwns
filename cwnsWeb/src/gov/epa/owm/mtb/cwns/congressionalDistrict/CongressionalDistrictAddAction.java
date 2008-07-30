package gov.epa.owm.mtb.cwns.congressionalDistrict;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.county.FacilityCountyForm;

public class CongressionalDistrictAddAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		CongressionalDistrictForm conDistrictForm = (CongressionalDistrictForm)form;
		conDistrictForm.setConDistrictName("");
		conDistrictForm.setConDistrictId("");
		conDistrictForm.setPrimary('N');
		conDistrictForm.setMode("add");
        // save token
		saveToken(request);     
		return mapping.findForward("success");
	}

}
