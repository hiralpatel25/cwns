package gov.epa.owm.mtb.cwns.impairedWatersInformation;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ImpairedWatersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ImpairedWatersAction extends CWNSAction {

    private Logger log = Logger.getLogger(this.getClass());

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
	    PortletRenderRequest prr = (PortletRenderRequest)
		      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		    
	    ImpairedWatersForm impairedWatersForm = (ImpairedWatersForm)form;

        //Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		//get the facility ID from the page parameter - configured as input parameter in provider.xml
		String facNum = prr.getParameter("facilityId");
		  
		if(facNum != null && facNum.length() > 0) {
 		   log.debug("facility number---"+facNum);	
 		   impairedWatersForm.setImpairedWaterFacilityId(facNum);
		} else {
			  facNum = impairedWatersForm.getImpairedWaterFacilityId();
		}
		boolean isDeleteAction = false;
        if(prr.getParameter("impairedWatersAct") != null
		    		  && prr.getParameter("impairedWatersAct").equalsIgnoreCase("delete")){
		    isDeleteAction = true;
		}

        //check if feedback copy
 		boolean isFeedback = facilityService.isFeedBack(new Long(facNum), user);
 	  	request.setAttribute("isFeedback",(isFeedback? "true" : "false"));

 		/* obtain the Facility object */
		impairedWatersForm.setImpairedWaterAct("");
		    
		if(isDeleteAction) {
	    	//	if feedback then toggle softdelete
			if(isFeedback){
				impairedWatersService.softDeleteFacilityType(new Long(facNum), impairedWatersForm.getWaterBodyName(), impairedWatersForm.getListId(), user);
			}else{
		    	if(!impairedWatersService.costExistsForFacilityDocument(new Long(facNum).longValue(), 
		    			impairedWatersForm.getWaterBodyName(), 
		    			impairedWatersForm.getListId()) 
		    			&& !impairedWatersService.costCurvesExistsForFacilityDocument(new Long(facNum).longValue(), 
				    			impairedWatersForm.getWaterBodyName(), 
				    			impairedWatersForm.getListId())){
		    		impairedWatersService.delete(new Long(facNum).longValue(), 
		    			impairedWatersForm.getWaterBodyName(), 
		    			impairedWatersForm.getListId(), user.getUserId());
		    	}					
			}	
		 }		    

		 impairedWatersForm.setImpairedWatersListHelpers(impairedWatersService.getFacilityImpairedWaterList(new Long(facNum).longValue()));   
		 impairedWatersForm.setListId("");
		 impairedWatersForm.setWaterBodyName("");
		    
        // Check if facility is updatable or not and set form attribute
 	     if (facilityService.isUpdatable(user, new Long(facNum))){
 	    	impairedWatersForm.setIsUpdatable("Y");
 	    	log.debug("facilityCommentsForm: isUpdatable: Y");
		 } else {
	 	    	impairedWatersForm.setIsUpdatable("N");
	 	    	log.debug("facilityCommentsForm: isUpdatable: N");
 	     }			    
	     request.setAttribute("impairedWatersform", impairedWatersForm);

      return mapping.findForward("success");
  }
    
    
	
    private ImpairedWatersService impairedWatersService;
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
    //  set the impairedWaters service
    public void setImpairedWatersService(ImpairedWatersService ps){
       impairedWatersService = ps;    	
    }  
    
}
