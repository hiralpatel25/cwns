package gov.epa.owm.mtb.cwns.reviewComments;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ReviewCommentsService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ReviewCommentsAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	ReviewCommentsListForm reviewCommentsListForm = (ReviewCommentsListForm) form;
    	
		log.debug(" IN - reviewCommentsListForm--> " + reviewCommentsListForm.toString());

        //Get user privilages
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userRole = user.getCurrentRole();
		
		//this should be commented out during deployment
		//userRole.setType(UserRole.LOCAL);
		
		String userType = userRole.getLocationTypeId();
		
		
		
		reviewCommentsListForm.setUserType(userType);
		
	    PortletRenderRequest prr = (PortletRenderRequest)
		      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		
		    /* determine what needs to be done */
		String action = (reviewCommentsListForm.getReviewCommentAct() != null) ? 
				reviewCommentsListForm.getReviewCommentAct() : "None";
		ActionForward actionFwd = mapping.findForward("success");
		log.debug("Action: " + action);
		        
		//This facilityId comes from portlet parameter, always for type "S" -- "Survey" -- not true
		long facIdS = -1;
		if(req.getParameter("facilityId")!=null)
			facIdS = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facIdS);
        /*
		// if facId is available in the form, use it instead
		if(reviewCommentsListForm.getReviewCommentsFacilityId() != ReviewCommentsListForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facIdS = reviewCommentsListForm.getReviewCommentsFacilityId();			
		}*/
		
		//set the help key
		String key ="help."+user.getCurrentRole().getLocationTypeId()+".reviewcomments";
	    String defaultkey = "help.reviewcomments"; 
	    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
	    req.setAttribute("helpKey", helpKey);		
		
		//check if the facIdS is infact from a feedback copy
		Facility f = facilityService.findByFacilityId(String.valueOf(facIdS));
		if (UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(userType)){
			if(f.getVersionCode()!='F'){
				httpReq.setAttribute("feedbackMessage", "Feedback copy does not exist");
				return actionFwd;
			}
		}

		long facIdF = -1; // this is the ID for the facility type record
		if(reviewCommentsListForm.getReviewCommentsFacilityIdF() != ReviewCommentsListForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form
			facIdF = reviewCommentsListForm.getReviewCommentsFacilityIdF();			
		}
		else
		{
			//try to fetch the Id from the database for a state and local user
			if(userType.equals(UserServiceImpl.LOCATION_TYPE_ID_STATE) || userType.equals(UserServiceImpl.LOCATION_TYPE_ID_LOCAL))
				if(f.getVersionCode()=='F'){
					//switch facilityids
					facIdF = facIdS;
					facIdS = fetchFacilityIdByVersionCode(facIdF, "F", "S");	
				}else{
					facIdF = fetchFacilityIdByVersionCode(facIdS, "S", "F");
				}
	
			  
		}
		
		log.debug("facIdS + facIdF: " + facIdS + "+" + facIdF);

		if(reviewCommentsListForm.getFacilityVersionCode() == null || reviewCommentsListForm.getFacilityVersionCode().equals("")){
			reviewCommentsListForm.setFacilityVersionCode("S");
		}

		if(userType.equals(UserServiceImpl.LOCATION_TYPE_ID_FEDERAL) || userType.equals(UserServiceImpl.LOCATION_TYPE_ID_REGIONAL)){
			reviewCommentsListForm.setFacilityVersionCode("S");			
		}else if(userType.equals(UserServiceImpl.LOCATION_TYPE_ID_LOCAL)){
			reviewCommentsListForm.setFacilityVersionCode("F");			
		}
		
		long facId = -1;

		if(reviewCommentsListForm.getFacilityVersionCode().equals(ReviewCommentsListForm.TYPE_SURVEY)){
			   facId = facIdS;
		}else if(reviewCommentsListForm.getFacilityVersionCode().equals(ReviewCommentsListForm.TYPE_FACILITY)){
			   facId = facIdF;
		}
		
		//if (action.equals("mark_as_read"));

	    int nextResultToDisplay = 1;
	    int maxResultToDisplay = ReviewCommentsService.MAX_RESULTS;
			    
		if (action.equals("add") && 
			reviewCommentsListForm.getInputComments()!=null && 
			reviewCommentsListForm.getInputComments().trim().length()!=0 )
		{	
			if(isTokenValid(req, true))
			{
			   addComments(facId, reviewCommentsListForm.getInputComments(), 
		                user.getUserId());
			   resetToken(req);
			}
		}
		
		//else
		{
			saveToken(req);			
		}
					
		/* Determine the result sub-set that should be displayed */
		String nextRes = prr.getQualifiedParameter("nextResult");
		
		if (nextRes != null) {
			nextResultToDisplay = Integer.parseInt(nextRes);
		}

		int  commentsCount = reviewCommentsService.countReviewComments(facId);

   	    log.debug("commentsCount: " + commentsCount);

   	    reviewCommentsListForm.setNumOfComments(commentsCount);
		reviewCommentsListForm.setReviewCommentsFacilityIdF(facIdF);
        reviewCommentsListForm.setReviewCommentsFacilityId(facIdS);

		Collection reviewCommentsListHelpers = 
			reviewCommentsService.getReviewCommentsDereferencedResults(facId, 
					                                             nextResultToDisplay -1, 
					                                             maxResultToDisplay);

		/* Set values in the Struts Form Bean */
		reviewCommentsListForm.setFromReviewComments(nextResultToDisplay);
		reviewCommentsListForm.setToReviewComments(nextResultToDisplay+reviewCommentsListHelpers.size()-1);
		reviewCommentsListForm.setNextReviewCommentsToDisplay(nextResultToDisplay+reviewCommentsListHelpers.size());
		reviewCommentsListForm.setPrevReviewCommentsToDisplay(nextResultToDisplay - ReviewCommentsService.MAX_RESULTS);
		reviewCommentsListForm.setReviewCommentsHelpers(reviewCommentsListHelpers);
		//reviewCommentsListForm.setFacilityVersionCode(facType);
		reviewCommentsListForm.setReviewCommentAct("");
		reviewCommentsListForm.setInputComments("");

        // Check if facility is updatable or not and set form attribute
 	     if (reviewCommentsService.isUpdatable(user, new Long(f.getVersionCode()=='F'?facIdF:facIdS))){
 	    	reviewCommentsListForm.setIsUpdatable("Y");
 	    	log.debug("facilityCommentsForm: isUpdatable: Y");
 		  }
 	     else
 	     {
 	    	reviewCommentsListForm.setIsUpdatable("N");
 	    	log.debug("facilityCommentsForm: isUpdatable: N");
 	     }
		
		
		req.setAttribute("reviewCommentsForm", reviewCommentsListForm);
		
		log.debug(" Out - reviewCommentsListForm--> " + reviewCommentsListForm.toString());

		
		return actionFwd;
	}

    private void addComments(long facilityId, String comments, String User) {
		reviewCommentsService.addComments(facilityId, comments, User);
    }	

    private long fetchFacilityIdByVersionCode(long facilityId, String srcVersionCode, String targetVersionCode) {
    	log.debug("fetchFacilityIdByVersionCode(" + facilityId + " " + srcVersionCode + " " + targetVersionCode);
    	
		return reviewCommentsService.fetchFacilityIdByVersionCode(facilityId, srcVersionCode, targetVersionCode);
    }	

	private ReviewCommentsService reviewCommentsService;

	public void setReviewCommentsService(ReviewCommentsService ss) {
		reviewCommentsService = ss;
	}
	
	private FacilityService facilityService;

	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}

}
