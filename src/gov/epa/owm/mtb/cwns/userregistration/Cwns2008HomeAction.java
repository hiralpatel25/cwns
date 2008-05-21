package gov.epa.owm.mtb.cwns.userregistration;

import gov.epa.owm.mtb.cwns.common.CWNSAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class Cwns2008HomeAction extends CWNSAction {
	
	public static final String ACTION_PROCESS_PRELIMINARY  = "process_preliminary";
	public static final String ACTION_DISPLAY_PRELIMINARY  = "display_preliminary";
	
	
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {

//		request.getSession().setAttribute("user_mode", "PUBLIC_USER");
		RegistrationForm rForm = (RegistrationForm) form; 
		   
		// This is a self service request to create a new CWNS account. 
		// Forward the requestion on to the Preliminary user Registration screen.

//		request.setAttribute("mode", "public");
		return mapping.findForward("success");
	}
	  
}