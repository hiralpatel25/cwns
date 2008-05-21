package gov.epa.owm.mtb.cwns.congressionalDistrict;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.model.CongressionalDistrictRef;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class ConDistrictListAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
        //	get the location ID from the page parameter - configured as input parameter in provider.xml
		String locationId = "";
		if(prr.getParameter("locationId")!=null) locationId = prr.getParameter("locationId");
		
		DynaValidatorForm conDistListForm = (DynaValidatorForm)form;
		String filter = (String)conDistListForm.get("filter");
		
		//get facilityId
		String facilityIdStr =request.getParameter("facilityId");
		if(facilityIdStr!=null && !"".equals(facilityIdStr)){
			conDistListForm.set("facilityId", facilityIdStr);
		}else{
			facilityIdStr = (String)conDistListForm.get("facilityId");	
		}		
		Collection conDistlist=null;
		if(facilityIdStr !=null && ! "".equals(facilityIdStr)&& !"0".equals(facilityIdStr)){
			request.setAttribute("facilityId", facilityIdStr);
			Long facilityId = new Long(facilityIdStr);
			String disablePreferred = "false";
			CongressionalDistrictRef cr = facilityAddressService.getPreferredCongressionalDistrict(facilityId);
			Collection adjCD = null;
			if(cr!=null){
				adjCD = facilityAddressService.getAdjacentCongressionalDistricts(cr,facilityId);
				request.setAttribute("prefCongressionDistrictId", cr.getCongressionalDistrictId());
				if (adjCD==null || adjCD.isEmpty()){
					disablePreferred = "true";
					filter = "A";
				}	
			}else{
				disablePreferred = "true";
				filter = "A";
			}
			request.setAttribute("disablePreferred", disablePreferred);
			conDistListForm.set("filter", filter);
			if("P".equals(filter)){
				if(cr!=null && adjCD != null){
					conDistlist = adjCD;
				}
			}else{
				conDistlist  = facilityAddressService.getConDistrictListByLocationObjects(locationId);
			}
		}else{		
			conDistlist  = facilityAddressService.getConDistrictListByLocationObjects(locationId);		
		}
		request.setAttribute("conDistricts", conDistlist);
		return mapping.findForward("success");
	}
	
      //  set the facility address service
	  private FacilityAddressService facilityAddressService;
	  public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		 this.facilityAddressService = facilityAddressService;
	  }    

}
