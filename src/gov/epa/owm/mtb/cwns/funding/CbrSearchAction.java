package gov.epa.owm.mtb.cwns.funding;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.model.CbrAmountInformation;
import gov.epa.owm.mtb.cwns.model.FundingSource;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FundingService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CbrSearchAction extends CWNSAction{
	
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	        throws Exception {
		
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
	    
		CbrSearchForm cbrSearchForm = (CbrSearchForm)form;
  
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
		
		//get the facility ID from the page parameter - configured as input parameter in provider.xml
 		String facNum = prr.getParameter("facilityId");
 		
 		if(facNum != null && facNum.length() > 0)
		   {
	 		   log.debug("facility number---"+facNum);	
	 		  cbrSearchForm.setFacilityId(facNum);
		   }
		   else
		   {
			  facNum = cbrSearchForm.getFacilityId();
		   }
		
		
		Collection searchResults = new ArrayList();
		Collection catIds = new ArrayList();
		Integer countSearchTotal = new Integer(0);
		
		
		if(cbrSearchForm.getCbrAction().equals("add")){
			
			//needsService.associateDocumentWithFacility(facNum, docForm.getDocumentIds());	
			boolean result = false;			
			
			if(cbrSearchForm.getLoanIds().length > 0)
				result = fundingService.associateLoansWithFacility(facNum, cbrSearchForm.getLoanIds(), user);			
			
			
			if(cbrSearchForm.getCategoryIds().length > 0)
				result = fundingService.associateCategoriesWithFacility(facNum, cbrSearchForm.getCategoryIds(), user);
			
			
			cbrSearchForm.setLoanIds(new String[]{""});
			cbrSearchForm.setCategoryIds(new String[]{""});
			
		}
		
		
		if(cbrSearchForm.getCbrAction().equals("search") || cbrSearchForm.getCbrAction().equals("add")){			
						
			
			CbrSearchHelper helper = cbrSearchForm.popCbrSearchHelper();
			
			helper.setResult(CbrSearchHelper.COUNT);
			
			
			String facilityLocation = facilityDAO.findByFacilityId(facNum).getLocationId();
			countSearchTotal = new Integer(fundingService.getCbrSearchResults(helper, facilityLocation).size());
			
			if(countSearchTotal.intValue() > 0 ){
				
				helper.setResult(CbrSearchHelper.LIST);
				searchResults = fundingService.getCbrSearchResults(helper, facilityLocation);				
				
				Collection fundinglist = fundingService.getFundingByFacilityId("" + helper.getFacilityId());
				
				Iterator iterator = fundinglist.iterator();
				
				while(iterator.hasNext()){
					
					FundingSource fs = (FundingSource) iterator.next();
					
					CbrAmountInformation amount = fs.getCbrAmountInformation();
					
					if(amount != null && amount.getId() != null){				
						
						catIds.add(amount.getId());				
					}			
				}				
			}						
		}		
		
		
		if (facilityService.isUpdatable(user, new Long(facNum))){
			cbrSearchForm.setIsUpdatable("Y");
    	}
		
		
		request.setAttribute("countSearchTotal", countSearchTotal);
		request.setAttribute("cbrSearchForm", cbrSearchForm); 		
 		request.setAttribute("searchResults", searchResults);
 		request.setAttribute("catIds", catIds);
 		
 		//request.setAttribute("countDocTotal", countDocTotal);
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
