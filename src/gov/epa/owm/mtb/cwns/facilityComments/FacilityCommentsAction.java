package gov.epa.owm.mtb.cwns.facilityComments;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.model.DataAreaRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityCommentsService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.UserService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FacilityCommentsAction extends CWNSAction {

	public static final String DATEFORMAT =  "MM/dd/yyyy hh:mm a";
		
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
    	FacilityCommentsForm facilityCommentsForm = (FacilityCommentsForm) form;
    	
        //Get user privilages
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userRole = user.getCurrentRole();
		
		//this should be commented out during deployment
		//userRole.setType(UserRole.LOCAL);
		
		String userType = userRole.getLocationTypeId();

		facilityCommentsForm.setUserType(userType);
		
	    PortletRenderRequest prr = (PortletRenderRequest)
		      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		
		/* determine what needs to be done */
		String action = (facilityCommentsForm.getFacilityCommentAct() != null) ? 
				facilityCommentsForm.getFacilityCommentAct() : "None";

		log.debug("Action: " + action);
        
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));

		String dataAreaName = "";
		if(req.getParameter("dataAreaType")!=null)
			dataAreaName = (String)req.getParameter("dataAreaType");
		
		String dataAreaHeader = "";
		if(req.getParameter("dataAreaHeader")!=null)
			dataAreaHeader = (String)req.getParameter("dataAreaHeader");
		
		log.debug("req.getParameter(facilityId/dataAreaType): " + facId + "/" + dataAreaName);
		
		//get dataarea object
		DataAreaRef dar = facilityCommentsService.getDataAreaRefFromName(dataAreaName);

		// if facId is available in the form, use it instead
		if(facilityCommentsForm.getFacilityId() != FacilityCommentsForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facId = facilityCommentsForm.getFacilityId();			
		}
		
		ActionForward actionFwd = mapping.findForward("success");

		int  commentsCount = facilityCommentsService.countFacilityComments(facId, dataAreaName);

   	    log.debug("commentsCount: " + commentsCount);
		
		if (action.equals("add") && 
				facilityCommentsForm.getInputComments()!=null && 
				facilityCommentsForm.getInputComments().trim().length() > 0)
		{		
			if(isTokenValid(req, true))
			{
				if(facilityCommentsForm.getFacilityCommentId() > 0 && commentsCount > 0)
				{
					log.debug("user.getUserId(): " + user.getUserId());
					updateFacilityComments(facilityCommentsForm.getFacilityCommentId(), 
											//"User: " + user.getUserId() + "     Date: " + (new Date()) + "\r\n" + 
											//filterUserDate(facilityCommentsForm.getInputComments()),
							                facilityCommentsForm.getInputComments(),
							                user.getUserId());
					resetToken(req);
				}
				else if(commentsCount <= 0)
				{
					log.debug("user.getUserId(): " + user.getUserId());
					
				    addFacilityComments(facId, dataAreaName, 
							//"User: " + user.getUserId() + "     Date: " + (new Date()) + "\r\n" + 
							facilityCommentsForm.getInputComments(),
			                user.getUserId());
				    resetToken(req);
				}
				facilityService.performPostSaveUpdates(new Long(facId), new Long(dar.getDataAreaId()), user);
			}
			
		}
		else if (action.equals("delete") && commentsCount > 0)
		{		
			if(isTokenValid(req, true))
			{
				deleteFacilityComments(facilityCommentsForm.getFacilityCommentId());
				facilityService.performPostSaveUpdates(new Long(facId), new Long(dar.getDataAreaId()), user);
				resetToken(req);
			}
		}
		//else
		
		{
			saveToken(req);
		}
		
		facilityCommentsForm.setFacilityCommentId(FacilityCommentsForm.INITIAL_LONG_VALUE);
		facilityCommentsForm.setInputComments("");
		
		Collection facilitycomments = findFacilityCommentsByFacilityIdAndDataAreaName(facId, dataAreaName);

    	Iterator iter = facilitycomments.iterator();
    	
    	if ( iter.hasNext() ) {
    	    Map map = (Map) iter.next();
            String comment = (String) map.get("description");
            
    		facilityCommentsForm.setInputComments(comment);
            
            SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
            String lastUpdateStr = df.format((Timestamp) map.get("lastUpdateTs"));
            String lastUpdateUserid = (String) map.get("lastUpdateUserid");
            
            facilityCommentsForm.setUserId(userService.findUserNameByUserId(lastUpdateUserid));
            facilityCommentsForm.setLastUpdatedTs(lastUpdateStr);
            
            facilityCommentsForm.setFacilityCommentId(((Long) map.get("facilityCommentId")).longValue());
            
       	    log.debug("facilityCommentId: " + facilityCommentsForm.getFacilityCommentId());            
		}		
   	    
   	    facilityCommentsForm.setNumOfComments(commentsCount);
		facilityCommentsForm.setFacilityId(facId);
		facilityCommentsForm.setDataAreaName(dataAreaName);
		facilityCommentsForm.setFacilityCommentAct("");
		
		facilityCommentsForm.setFacility(facilityService.isFacility(new Long(facId)));

		//check for feedback facility rules
		
		
		
		Facility feedbackF = facilityService.getFeedbackVersionOfFacility((new Long(facId)).toString());
		
		if(feedbackF != null)		
			log.debug("getFeedbackVersionOfFacility --> " + feedbackF.getFacilityId());
			
		if( 
		  feedbackF != null && feedbackF.getReviewStatusRef() != null &&
		  feedbackF.getReviewStatusRef().getReviewStatusId().equalsIgnoreCase("SRR") &&
		  facilityCommentsService.isFeedbackVersionValid(feedbackF.getFacilityId(), dataAreaName) &&
		  isFacilityReviewStatusValidForFeedback(new Long(facId).toString()) &&
		  userRole!=null &&
		  (userRole.getPrivileges()!=null && userRole.getPrivileges().containsKey(CurrentUser.SUBMIT_FOR_FEDERAL_REVIEW)) &&
		  (userRole.getLocationTypeId().equalsIgnoreCase("Federal") ||
			(userRole.getLocationTypeId().equalsIgnoreCase("State") &&
			  userRole.getLocationId().equalsIgnoreCase(facilityService.findByFacilityId(new Long(facId).toString()).getLocationId())))
		  )
		{
			facilityCommentsForm.setFeedbackVersionFacilityId(new Long(feedbackF.getFacilityId()).toString());
		}
		else
		{
			facilityCommentsForm.setFeedbackVersionFacilityId(null);
		}		

        // Check if facility is updatable or not and set form attribute
  	     if (facilityService.isUpdatable(user, new Long(facId))){
  	    	facilityCommentsForm.setIsUpdatable("Y");
  	    	log.debug("facilityCommentsForm: isUpdatable: Y");
  		  }
  	     else
  	     {
  	    	facilityCommentsForm.setIsUpdatable("N");
  	    	log.debug("facilityCommentsForm: isUpdatable: N");
  	     }
		
		req.setAttribute("facilityCommentsForm", facilityCommentsForm);
		
		log.debug(" Out - facilityCommentsForm--> " + facilityCommentsForm.toString());
		
		//get a data area errors
		Collection fesErrors = fesService.getDataAreaFESErrors(facId, dar.getDataAreaId());
		if(fesErrors.size()>0){
			req.setAttribute("fesErrors", fesErrors);
		}
		if(dar.getDataAreaId()!=FacilityService.DATA_AREA_UNIT_PROCESS.longValue() &&
				dar.getDataAreaId()!=FacilityService.DATA_AREA_UTIL_MANAGEMENT.longValue()){
		     //warn local copy
		     if(facilityService.warnIfFeedBackOutOfSync(new Long(facId))){
			   req.setAttribute("warnFeedBack", "Y");
		     }
		}
		
		//set display Facility Feedback Form Link
		if(dar!=null && facilityCommentsService.displayFeedbackFacilitySatisfied(facId, dar.getDataAreaId(), user)){
			req.setAttribute("displayFeedbackLink", "true");
			
			String feedBackFacilityId = Long.toString(feedbackF.getFacilityId());
			req.setAttribute("feedbackFacilityId", feedBackFacilityId);
			
			//get poppulation & data area info
			PopulationHelper pop = populationService.getUpStreamFacilitiesPopulationTotal(feedBackFacilityId);
			if(pop!=null){
				req.setAttribute("population", pop);	
			}else{
				log.debug("Upstream population is null");
			}
			
			//get dataareas query parameter
			String daqp = facilityTypeService.getFacilityDataAreasQueryParam(new Long(feedbackF.getFacilityId()));
			if(daqp!=null){
				req.setAttribute("dataAreaQueryParameters", daqp);	
			}else{
				log.debug("data area parameters are null");
			}		
		}
		String dataAreaHeaderJS = dataAreaHeader.replaceAll(" ", "_");
		
			String key ="help."+user.getCurrentRole().getLocationTypeId()+"."+dataAreaHeaderJS;
		    String defaultkey = "help."+dataAreaHeaderJS; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 
			
		req.setAttribute("helpKey", helpKey);
		return actionFwd;
	}
/*
	private String filterUserDate(String inputComments)
	{
		if(inputComments != null && 
				inputComments.indexOf("\r\n") > 0) 
		{
		
			int firstLineIndex = inputComments.indexOf("\r\n");

			log.debug(" firstLineIndex --> " + firstLineIndex);
			
			String tmpStr = inputComments.substring(0, firstLineIndex);
			
			if(tmpStr.indexOf("User:") >= 0 && 
					tmpStr.indexOf("Date:") > 0 && 
					tmpStr.indexOf("User:") < tmpStr.indexOf("Date:"))
			{
				return inputComments.substring(firstLineIndex + 2, inputComments.length());
			}
		}
		
		return inputComments;

	}
*/	
    private void addFacilityComments(long facilityId, String dataAreaName, String comments, String User) {
		
    	log.debug("facilityId//dataAreaName/comments/User: " + facilityId+"/"+dataAreaName+"/"+comments+"/"+User);
		
    	facilityCommentsService.addFacilityComments(facilityId, dataAreaName, comments, User);
    }	

    //private void deleteFacilityComments(long facilityId, String dataAreaName) {
	//	facilityCommentsService.deleteFacilityComments(facilityId, dataAreaName);
    //}	

    private void deleteFacilityComments(long facilityCommentId) {
		facilityCommentsService.deleteFacilityComments(facilityCommentId);
    }	
    
    private Collection findFacilityCommentsByFacilityIdAndDataAreaName(long facilityId, String dataAreaName)
    {
    	return facilityCommentsService.findFacilityCommentsByFacilityIdAndDataAreaName(facilityId, dataAreaName);
    }
    
    private void updateFacilityComments(long facilityCommentId, String comments, String User)
    {
    	facilityCommentsService.updateFacilityComments(facilityCommentId, comments, User);
    }
    
    private boolean isFacilityReviewStatusValidForFeedback(String facilityId)
    {
    	Facility f = (Facility)facilityService.findByFacilityId(facilityId);
    	
    	if(f!=null && 
    	   f.getVersionCode() == 'S' &&
    	   f.getReviewStatusRef() !=null &&
	       ("SAS".equalsIgnoreCase(f.getReviewStatusRef().getReviewStatusId()) ||
	    		   "SIP".equalsIgnoreCase(f.getReviewStatusRef().getReviewStatusId()) ||
	    		   "SCR".equalsIgnoreCase(f.getReviewStatusRef().getReviewStatusId())))
	    		   {
    					return true;
	    		   }
    	
    	return false;
    }
    
	private FacilityCommentsService facilityCommentsService;
	public void setFacilityCommentsService(
			FacilityCommentsService facilityCommentsService) {
		this.facilityCommentsService = facilityCommentsService;
	}
	
	private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
	private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private FESService fesService;
	public void setFesService(FESService fesService) {
		this.fesService = fesService;
	}
	
	private PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		this.populationService = populationService;
	}
	
	private FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService facilityTypeService) {
		this.facilityTypeService = facilityTypeService;
	}	
}
