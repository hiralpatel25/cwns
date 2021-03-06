package gov.epa.owm.mtb.cwns.navigationBanner;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
import gov.epa.owm.mtb.cwns.common.CWNSAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class NavigationBannerAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return mapping.findForward("success");
	}

}
