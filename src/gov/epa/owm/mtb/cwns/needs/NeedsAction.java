package gov.epa.owm.mtb.cwns.needs;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.CapitalCostService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.NeedsService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class NeedsAction extends CWNSAction {

    private Logger log = Logger.getLogger(this.getClass());

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
    	    PortletRenderRequest prr = (PortletRenderRequest)
    		      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
   		    
    	    NeedsForm needsForm = (NeedsForm)form;
    	    
            //Get user object
   			HttpServletRequest httpReq = (HttpServletRequest) request;
   			HttpSession httpSess = httpReq.getSession();
   			CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
   		
 		   //get the facility ID from the page parameter - configured as input parameter in provider.xml
 		   String facNum = prr.getParameter("facilityId");
 		  
 		   if(facNum != null && facNum.length() > 0)
 		   {
	 		   log.debug("facility number---"+facNum);	
	 		   needsForm.setNeedsFacilityId(facNum);
 		   }
 		   else
 		   {
 			  facNum = needsForm.getNeedsFacilityId();
 		   }
 	       /*
 	         Enumeration keys = prr.getParameterNames();
 	        while (keys.hasMoreElements()) {
 	        	String name = (String)keys.nextElement();
 	    		log.debug("Para: " + name + " - " + prr.getParameter(name));
 	        }
 		   */

			boolean isSaveAction = false;
			boolean isDeleteAction = false;
			boolean isEditAction = false;
			boolean isNewAction = false;
			boolean isExpandCostAction = false;
			boolean isCancel = false;

		      if(prr.getParameter("needsAct") != null)
		      {
			      if(prr.getParameter("needsAct").equalsIgnoreCase("save"))
			      {
			    	  isSaveAction = true;
			    	  log.debug("isSaveAction - true");
			      }
			      else if(prr.getParameter("needsAct").equalsIgnoreCase("delete"))
			      {
			    	  isDeleteAction = true;
			      }
			      else if(prr.getParameter("needsAct").equalsIgnoreCase("edit"))
			      {
			    	  isEditAction = true;
			      }
			      else if(prr.getParameter("needsAct").equalsIgnoreCase("new"))
			      {
			    	  isNewAction = true;
			      }
			      else if(prr.getParameter("needsAct").equalsIgnoreCase("expandCost"))
			      {
			    	  isExpandCostAction = true;
			      }
			      else if(prr.getParameter("needsAct").equalsIgnoreCase("cancel"))
			      {
			    	  isCancel = true;
			      }
		      }

 		  	/* obtain the Facility object */
 	    	Facility facility = facilityService.findByFacilityId(facNum);
 	    	needsForm.setLocationId(facility.getLocationId());

		    needsForm.setNeedsAct("");
		      if(isCancel)
		      {
		    	  needsForm.setDocumentId("");  
		      }
		      else if(isSaveAction)
		      {
		    	  // validate base Month Year		    	  
		    	  if(needsService.isBaseMonthYearValid(needsForm.getBaseMonthYear()) == true)
		    	  {
			    	  needsService.saveOrUpdateNeedsInfo(facNum, needsForm, user.getUserId());
			    	  capitalCostService.updateDocumentedCosts(Long.parseLong(facNum), Long.parseLong(needsForm.getDocumentId()), user.getUserId());
			    	  facilityService.performPostSaveUpdates(new Long(facNum), FacilityService.DATA_AREA_NEEDS, user);
			    	  needsForm.setDocumentId("");
		    	  }
		    	  else
		    	  {
		    		  needsForm.setDetailEditExpand("Y");
		    		  needsForm.setIsBaseMonthYearValid("N");
		    		  needsForm.setShowCIPWarning("N");
		    		  needsForm.setShowIUPWarning("N");
		    		  needsForm.setShowFPWarning("N");
		    	  }
		    	  
		    	 httpSess.setAttribute(facNum + "_ActiveDocId", needsForm.getDocumentId());		    	  
		    	  
		      }
		      else if(isDeleteAction)
		      {   
		    	  //if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
		    		//  needsService.setFeedBackDeleteFlag(facNum, needsForm.getDocumentId(), needsForm.getFeedBackDeleteFlg()); 
		    	  //}
		    	  needsService.deleteFacilityDocument(facNum, needsForm.getDocumentId(), user);
		    	  needsForm.initForm();
	    		  needsForm.setShowCIPWarning("N");
	    		  needsForm.setShowIUPWarning("N");
	    		  needsForm.setShowFPWarning("N");	
	    		  httpSess.setAttribute(facNum + "_ActiveDocId", "");
	    		  facilityService.performPostSaveUpdates(new Long(facNum), FacilityService.DATA_AREA_NEEDS, user);
			  }
		      else if(isEditAction)
		      {
		    	  needsService.prepareFacilityDocument(facNum, needsForm.getDocumentId(), needsForm);
		    	  needsForm.setDatesUpdatable(needsService.datesUpdatable(needsForm.getDocumentId())==true?"Y":"N");
		    	  needsForm.setDetailEditExpand("Y");
	    		  needsForm.setShowCIPWarning("N");
	    		  needsForm.setShowIUPWarning("N");
	    		  needsForm.setShowFPWarning("N");	
	    		  httpSess.setAttribute(facNum + "_ActiveDocId", needsForm.getDocumentId());
		      }
		      else if(isNewAction)
		      {
		    	  needsForm.initForm();
		    	  needsForm.setDetailEditExpand("Y");
		    	  needsForm.setDocumentId("");
	    		  needsForm.setShowCIPWarning("N");
	    		  needsForm.setShowIUPWarning("N");
	    		  needsForm.setShowFPWarning("N");
	    		  
	    		  httpSess.setAttribute(facNum + "_ActiveDocId", "");
		      }
		      else if(isExpandCostAction)
		      {
		    	  needsForm.setDetailEditExpand("N");
	    		  needsForm.setShowCIPWarning("N");
	    		  needsForm.setShowIUPWarning("N");
	    		  needsForm.setShowFPWarning("N");
	    		  httpSess.setAttribute(facNum + "_ActiveDocId", needsForm.getDocumentId());
		      }
		      else // this is when the page is refreshed through other portlets on Needs page
		      {
		  		String docId = (String)httpSess.getAttribute(facNum + "_ActiveDocId");
				if(docId!=null && !docId.equals(""))
				{
					needsForm.setDocumentId(docId);
				}
		      }
            		        
			Collection needsInfoList = needsService.getNeedsInfo(facNum);
			
			Collection documentGroupList = needsService.getDocumentGroupInfo();
			
			Collection documentGroupTypeList = needsService.getDocumentGroupTypeInfo(facNum, needsForm.getDocumentId());

			// Check if facility is updatable or not and set form attribute
			if (facilityService.isUpdatable(user, new Long(facNum))){
				needsForm.setIsUpdatable("Y");
				log.debug("needsInformationForm: isUpdatable: Y");
	   	    	
	   	    	if(needsForm.getDocumentType().equals("11") || needsForm.getDocumentType().equals("98")){
	   	    		needsForm.setIsDetailUpdatable("N");
	   	    	}else{
	   	    		needsForm.setIsDetailUpdatable("Y");
	   	    	}
	   	    	//annotate
	   	    	if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
	   	    		needsForm.setIsAnnotate("N");
	   	    	}else{
	   	    		needsForm.setIsAnnotate("Y");
	   	    	}   	    	
	   	    }else {
    	    	needsForm.setIsUpdatable("N");
       	    	needsForm.setIsDetailUpdatable("N");
       	    	needsForm.setIsAnnotate("N");
       	    }
   	     
         ArrayList warnMessages = new ArrayList();
   	     if (needsService.existDuplicateCostCategory(facNum, "20"))
   	    	warnMessages.add("warn.needs.capitalimprovementplan");
   	     if (needsService.existDuplicateCostCategory(facNum, "01"))
 	    	warnMessages.add("warn.needs.intendeduseplan");
   	     if (needsService.existDuplicateCostCategory(facNum, "21"))
 	    	warnMessages.add("warn.needs.facilityplan");
   	     
   	     long federalAdjustedAmountTotal = 0, sseAdjustedAmountTotal = 0;
   	     
   	     if(needsInfoList!=null)
   	     {
	   	    	Iterator iter = needsInfoList.iterator();
	   	    	
	   	    	while ( iter.hasNext() ) {
	   	    		NeedsHelper nh = (NeedsHelper) iter.next();
	   	    		federalAdjustedAmountTotal += nh.getFederalAmount();
	   	    		sseAdjustedAmountTotal += nh.getSseAmount();
	   	    	}
   	     }
   	     
   	  if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
   		needsForm.setIsLocalUser("Y");
   		Facility feedbackCopy = facilityService.getFeedbackVersionOfFacility(facNum);
   		if(feedbackCopy!=null){
   	   		String reviewStatus = feedbackCopy.getReviewStatusRef()!=null?feedbackCopy.getReviewStatusRef().getReviewStatusId():"";
   	   		if (ReviewStatusRefService.LOCAL_ASSIGNED.equalsIgnoreCase(reviewStatus)||ReviewStatusRefService.LOCAL_IN_PROGRESS.equalsIgnoreCase(reviewStatus))
   	   			needsForm.setIsneedsStartYearUpdatable("Y");   			
   		}
   	  }		
   	     
   	  needsForm.setFederalAdjustedAmountTotal(federalAdjustedAmountTotal);
   	  needsForm.setSseAdjustedAmountTotal(sseAdjustedAmountTotal);
   	  
   	  request.setAttribute("warnMessages", warnMessages);
      request.setAttribute("needsform", needsForm);
      request.setAttribute("needsInfoList", needsInfoList);
      request.setAttribute("documentGroupList", documentGroupList);
      request.setAttribute("documentGroupTypeList", documentGroupTypeList);
      
      return mapping.findForward("success");
  }
   
    private FacilityService facilityService;   
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  

    private NeedsService needsService;    
    //  set the needs service
    public void setNeedsService(NeedsService ps){
       needsService = ps;    	
    }    
    
    private CapitalCostService capitalCostService;
	public void setCapitalCostService(CapitalCostService capitalCostService) {
		this.capitalCostService = capitalCostService;
	}    
}
