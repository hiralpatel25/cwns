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
import gov.epa.owm.mtb.cwns.model.WatershedRef;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class WatershedListAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userrole = user.getCurrentRole();

		//System.out.println("locationid from watershed look up--"+req.getParameter("locationId"));
		
		DynaValidatorForm watershedListForm = (DynaValidatorForm)form;
		String filter = (String)watershedListForm.get("filter");
		
		//get facilityId
		String facilityIdStr =req.getParameter("facilityId");
		if(facilityIdStr!=null && !"".equals(facilityIdStr)){
			watershedListForm.set("facilityId", facilityIdStr);
		}else{
			facilityIdStr = (String)watershedListForm.get("facilityId");	
		}		
		Collection watershedlist=null;
		if(facilityIdStr !=null && ! "".equals(facilityIdStr)&& !"0".equals(facilityIdStr)){
			req.setAttribute("facilityId", facilityIdStr);
			Long facilityId = new Long(facilityIdStr);
			String disablePreferred = "false";
			WatershedRef watershed = facilityAddressService.getPreferredWatershed(facilityId);
			Collection adjWS=null;
			if(watershed!=null){
				adjWS=facilityAddressService.getAdjacentWatersheds(watershed, facilityId);
				req.setAttribute("prefWatershedId", new Long(watershed.getWatershedId()));
				if (adjWS==null || adjWS.isEmpty()){
					disablePreferred = "true";
					filter = "A";
				}	
			}
			else{
				disablePreferred = "true";
				filter = "A";
			}
			req.setAttribute("disablePreferred", disablePreferred);
			watershedListForm.set("filter", filter);
			if("P".equals(filter)){
				if(watershed!=null && adjWS!=null){
					watershedlist = adjWS;
				}
			}else{
				watershedlist = facilityService.getWatershedListByLocationObjects(req.getParameter("locationId"));
			}
		}else{
			watershedlist = facilityService.getWatershedListByLocationObjects(req.getParameter("locationId"));
		}
		req.setAttribute("watersheds", watershedlist);
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
