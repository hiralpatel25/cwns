package gov.epa.owm.mtb.cwns.populationInformation;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class UpstreamCollectionAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
//	    PortletRenderRequest prr = (PortletRenderRequest)
//	      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 

//    PopulationInformationForm populationInformationForm = (PopulationInformationForm)form;
	    
	long facId = -1;
	// this is from portal page parameter		
	if(req.getParameter("facilityId")!=null && req.getParameter("facilityId").length()>0)
		facId = Long.parseLong(req.getParameter("facilityId"));
    // Get user object
	HttpServletRequest httpReq = (HttpServletRequest) req;
	HttpSession httpSess = httpReq.getSession();
	CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
    
	ArrayList dischargeReceiveHelperList;
	dischargeReceiveHelperList = (ArrayList)populationService.getUpstreamFacilitiesForDisplay(Long.toString(facId));
	if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
 	      ArrayList errorMessages = new ArrayList();
 	      if (!populationService.isFlowApportionmentCompleted(Long.toString(facId))){
          errorMessages.add("error.population.flowApportionment");
          req.setAttribute("errorMessages", errorMessages);
 	      }
 	}  
    req.setAttribute("dischargeReceiveHelperList", dischargeReceiveHelperList);
    //ArrayList sewerShedList = (ArrayList)populationService.getRelatedSewerShedFacilities(Long.toString(facId));
    
   // req.setAttribute("sewerShedList", sewerShedList);
    
    //populationService.getUpstreamFacilitiesListByDischargeType(new Long(facId).toString(), populationService.PRESENT_ONLY);
    
		return mapping.findForward("success");
	}
	
    private PopulationService populationService;
    private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
    //  set the population service
    public void setPopulationService(PopulationService ps){
       populationService = ps;    	
    }  
	
}
