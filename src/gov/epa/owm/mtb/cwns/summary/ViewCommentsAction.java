package gov.epa.owm.mtb.cwns.summary;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.service.SummaryService;

public class ViewCommentsAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
	
		SummaryForm summaryForm = (SummaryForm) form;
		String facNum = "";
       //	get the facility ID from the page parameter - configured as input parameter in provider.xml
		if(req.getParameter("facilityId")!=null)	
		    facNum = req.getParameter("facilityId");
		
          // get facility comments
		  Collection facCommentHelpers = summaryService.findCommentsByFacilityId(facNum);
          summaryForm.setFacCommentHelpers(facCommentHelpers);
		
		req.setAttribute("summary", summaryForm);
		
		return mapping.findForward("success");
	}
	
	private SummaryService summaryService;

	public void setSummaryService(SummaryService ss) {
		summaryService = ss;
	}

}
