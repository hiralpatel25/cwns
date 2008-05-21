package gov.epa.owm.mtb.cwns.reviewComments;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.service.ReviewCommentsService;

import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ReviewCommentsViewAllAction extends CWNSAction {

	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	ReviewCommentsListForm reviewCommentsListForm = (ReviewCommentsListForm) form;
    	
    	log.debug(" ReviewCommentsViewAllAction: Token: " + generateToken(req));
		log.debug(" ReviewCommentsViewAllAction: IN - reviewCommentsListForm--> " + reviewCommentsListForm.toString());

	    //PortletRenderRequest prr = (PortletRenderRequest)
		//      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		
		long facId = -1;

		log.debug(" parameter: facilityId: (" + req.getParameter("facilityId") + ")");
		
		// this is from portal page parameter		
		if(req.getParameter("facilityId")!=null && req.getParameter("facilityId").length()>0)
			facId = Long.parseLong(req.getParameter("facilityId"));
		
        Enumeration keys = req.getParameterNames();
        while (keys.hasMoreElements()) {
        	String name = (String)keys.nextElement();
    		log.debug("Para: " + name + " - " + req.getParameter(name));
        }

		
		ActionForward actionFwd = mapping.findForward("success");
	    
		Collection reviewCommentsListHelpers = 
			reviewCommentsService.getReviewCommentsDereferencedResults(facId,
					                                             0, 
					                                             -1);

		reviewCommentsListForm.setReviewCommentsHelpers(reviewCommentsListHelpers);
		reviewCommentsListForm.setReviewCommentsFacilityId(facId);

		req.setAttribute("reviewCommentsForm", reviewCommentsListForm);
		
		log.debug(" ReviewCommentsViewAllAction: Out - reviewCommentsListForm--> " + reviewCommentsListForm.toString());
		
		return actionFwd;
	}

	private ReviewCommentsService reviewCommentsService;

	public void setReviewCommentsService(ReviewCommentsService ss) {
		reviewCommentsService = ss;
	}

}
