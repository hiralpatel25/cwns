package gov.epa.owm.mtb.cwns.funding;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.CbrLoanInformation;
import gov.epa.owm.mtb.cwns.service.FundingService;

public class CbrLoanDetailAction extends CWNSAction{
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	        throws Exception {
		
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		
		
		String loanId = prr.getParameter("loanId");
		
	    
		CbrLoanDetailForm loanDetailForm = (CbrLoanDetailForm)form;
		
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
		
		CbrLoanInformation loanDetail = fundingService.getCbrLoanDetail(loanId);
		
		if(loanDetail == null){			
			
			try {
				loanDetail = fundingService.getCbrLoanFromFundingSourceId(new Long(loanId).longValue());
			} catch (RuntimeException e) {
				// No details			
			}
			
		}
		
			
		request.setAttribute("loanDetail", loanDetail);
		
		
		return mapping.findForward("success");
	}
	
	
	private FundingService fundingService; 
    //  set the needs service
    public void setFundingService(FundingService fs){
    	fundingService = fs;     
	}

}
