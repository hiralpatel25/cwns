package gov.epa.owm.mtb.cwns.boundary;

import gov.epa.owm.mtb.cwns.common.CWNSAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class BoundaryAddAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		BoundaryForm boundaryForm = (BoundaryForm)form;
		boundaryForm.setBoundaryName("");
		boundaryForm.setBoundaryType("");
		boundaryForm.setMode("add");
        // save token
		saveToken(request);     
		return mapping.findForward("success");
	}

}
