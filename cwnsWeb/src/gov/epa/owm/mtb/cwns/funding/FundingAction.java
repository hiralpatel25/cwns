package gov.epa.owm.mtb.cwns.funding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.model.FundingSource;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FundingService;

public class FundingAction extends CWNSAction{
	
	private Logger log = Logger.getLogger(this.getClass());

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	
    	PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
	    
    	FundingForm fundingForm = (FundingForm)form;
    	
    	//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
    	
		request.setAttribute("searchResults", null);		
    	  
    	String facNum = prr.getParameter("facilityId");
    	if(fundingForm.getFacilityId().equals(""))fundingForm.setFacilityId(facNum);
    	
    	
    	//determine whether to load the lookup tables
    	if( fundingForm.getFundingAction().equals("add") || fundingForm.getFundingAction().equals("edit")){
    		request.setAttribute("loanTypes", fundingService.listLoanTypes());
        	request.setAttribute("fundingSourceTypes", fundingService.listFundingSourceTypes());
        	request.setAttribute("fundingAgencyTypes", fundingService.listFundingAgencyTypes()); 
        	
    	}
    	else{
    		request.setAttribute("loanTypes", new ArrayList());
        	request.setAttribute("fundingSourceTypes", new ArrayList());
        	request.setAttribute("fundingAgencyTypes", new ArrayList());
    	}
    	
    	//process the request action
    	if( fundingForm.getFundingAction().equals("add")){
    		
    		fundingForm.reset();
    		
    	}else if(fundingForm.getFundingAction().equals("save")){
    		
    		if(isTokenValid(request, true)){
    		    		    		
    			FundingSource fs = fundingForm.popFundingSource(user.getOidUserId());    		
    			fs.setSourceCd('M');
    			fs.setFeedbackDeleteFlag('N');
    			fs.setLastUpdateUserid(user.getUserId());
    			boolean result = fundingService.saveOrUpdateFundingSource(fs, user);    		
    			fundingForm.reset();
    			resetToken(request);
    		}    		
    	}
    	else if(fundingForm.getFundingAction().equals("edit")){
    		
    		FundingSource fs = fundingService.getFundingSourceById(new Long(fundingForm.getFundingId()).longValue());
    		fundingForm.popForm(fs);
    	}else if(!user.isNonLocalUser() && fundingForm.getFundingAction().equals("mark delete")){
    		FundingSource fs = fundingService.getFundingSourceById(new Long(fundingForm.getFundingId()).longValue());
    		if(fs.getFeedbackDeleteFlag()=='N')
    			fs.setFeedbackDeleteFlag('Y');
    		else
    			fs.setFeedbackDeleteFlag('N');
    		
    		fs.setLastUpdateUserid(user.getUserId());
    		fs.setLastUpdateTs(new Date());
			boolean result = fundingService.saveOrUpdateFundingSource(fs, user);    		
			fundingForm.reset();
			resetToken(request);    		
    	}else if(fundingForm.getFundingAction().equals("delete")){    		
    		if(isTokenValid(request, true)){
    			boolean result = fundingService.deleteFundingSource(new Long(fundingForm.getFundingId()).longValue(), user);
    			fundingForm.reset(); 
    		}
    	}
    	else if(fundingForm.getFundingAction().equals("refresh")){
    		
    		if(isTokenValid(request, true)){
    			boolean result = fundingService.refreshFundingSourceFromCbr(facNum, new Long(fundingForm.getFundingId()).longValue(), user);
    			fundingForm.reset(); 
    		}
    	}   	
    	

    	
    	if (facilityService.isUpdatable(user, new Long(facNum))){    		
    		fundingForm.setIsUpdatable("Y");
    	}
    	
    	//warn if feedback copy exists
    	request.setAttribute("feedbackCopyExists", "N");
    	if (facilityDAO.findByFacilityId(facNum).getVersionCode()=='S' && 
    			facilityService.getFeedbackVersionOfFacility(facNum)!=null){
    		request.setAttribute("feedbackCopyExists", "Y");
    	}
    	
    	saveToken(request);
    	
    	//get the search results after any save or edit is done.
    	Collection searchResults = null;
    	searchResults = fundingService.getFundingByFacilityId(facNum);
    	
    	request.setAttribute("fundingForm", fundingForm);
    	request.setAttribute("searchResults", searchResults);
    	request.setAttribute("stateUser", user.isNonLocalUser()?"true":"false");    	    	
    	return mapping.findForward("success");
    }
    
    private FundingService fundingService; 
    //  set the needs service
    public void setFundingService(FundingService fs){
    	fundingService = fs;    	
    }
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;
    }

    private FacilityDAO facilityDAO;
	public void setFacilityDAO(FacilityDAO facilityDAO) {
		this.facilityDAO = facilityDAO;
	}
    
}
