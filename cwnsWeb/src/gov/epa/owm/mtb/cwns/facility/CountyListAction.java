package gov.epa.owm.mtb.cwns.facility;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.model.CountyRef;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class CountyListAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		//HttpServletRequest httpReq = (HttpServletRequest) req;
		//HttpSession httpSess = httpReq.getSession();
		//CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		//UserRole userrole = user.getCurrentRole();
		
		DynaValidatorForm countyListForm = (DynaValidatorForm)form;
		String filter = (String)countyListForm.get("filter");
		
		//get facilityId
		String facilityIdStr =req.getParameter("facilityId");
		if(facilityIdStr!=null && !"".equals(facilityIdStr)){
			countyListForm.set("facilityId", facilityIdStr);
		}else{
			facilityIdStr = (String)countyListForm.get("facilityId");	
		}		
		Collection countylist=null;
		if(facilityIdStr !=null && ! "".equals(facilityIdStr)&& !"0".equals(facilityIdStr)){
			req.setAttribute("facilityId", facilityIdStr);
			Long facilityId = new Long(facilityIdStr);
			String disablePreferred = "false";
			CountyRef county = facilityAddressService.getPreferredCounty(facilityId);
			Collection adjCo = null;
			if(county!=null){
				adjCo=facilityAddressService.getAdjacentCounties(county, facilityId);
				req.setAttribute("prefCountyId", county.getFipsCode());
				if (adjCo==null || adjCo.isEmpty()){
					disablePreferred = "true";
					filter = "A";
				}	
			}
			else{
				disablePreferred = "true";
				filter = "A";
			}
				
			req.setAttribute("disablePreferred", disablePreferred);
			countyListForm.set("filter", filter);
			if("P".equals(filter)){
				if(county!=null && adjCo!=null){
					countylist = adjCo;
				}
			}else{
				countylist = facilityService.getCountyListByLocation(req.getParameter("locationId"));
			}
		}else{
			countylist = facilityService.getCountyListByLocation(req.getParameter("locationId"));
		}
		if(countylist!=null){
			req.setAttribute("counties", countylist);
		}		
		return mapping.findForward("success");
	}
   
	/* set the facility service */
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }

    private FacilityAddressService facilityAddressService;
    public void setFacilityAddressService(FacilityAddressService fas){
       facilityAddressService = fas;    	
    }
    
    
}
