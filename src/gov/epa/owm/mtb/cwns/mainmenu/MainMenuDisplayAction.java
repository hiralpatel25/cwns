package gov.epa.owm.mtb.cwns.mainmenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;

public class MainMenuDisplayAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//display new facility link
		HttpSession pSession = request.getSession(false);
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }
	    
	    /* User Information  */
	    CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
	    String updateAccess = (currentUser!=null && !"Federal".equalsIgnoreCase(currentUser.getCurrentRole().getLocationTypeId()) && currentUser.isAuth(CurrentUser.FACILITY_UPDATE))?"Y":"N";
	    pSession.setAttribute("updateAccess", updateAccess);
	    	    
		String key ="help."+currentUser.getCurrentRole().getLocationTypeId()+".mainmenu";
		String defaultkey = "help.mainmenu"; 
		String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		request.setAttribute("helpKey", helpKey);
	    return mapping.findForward("success");
	}

}
