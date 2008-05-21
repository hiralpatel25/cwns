package gov.epa.owm.mtb.cwns.announcements;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.service.AnnouncementService;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AnnouncementsAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Get user locationId
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userrole = user.getCurrentRole();

		// obtain Announcements user locationID
		//Collection announcements = announcementService.findAnnouncementsByLocationAndFormat(userrole.getLocation());
		Collection announcements = announcementService.findAnnouncementsByLocationAndFormat(userrole.getLocationId(),"US",0,10);
		request.setAttribute("announce", announcements);
		/*
		String helpKey = "help."+user.getCurrentRole().getLocationTypeId()+".announcements";
		    if (!messages.isPresent(helpKey)) {
				helpKey = "help.announcements";
			}
		*/
		String key ="help."+user.getCurrentRole().getLocationTypeId()+".announcements";
	    String defaultkey = "help.announcements"; 
	    String helpKey = CWNSProperties.getKey(key, defaultkey); 
		request.setAttribute("helpKey", helpKey);
		return mapping.findForward("success");
	}

	private AnnouncementService announcementService;

	public void setAnnouncementService(AnnouncementService as) {
		announcementService = as;
	}

}
