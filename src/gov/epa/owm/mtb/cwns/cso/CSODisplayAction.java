package gov.epa.owm.mtb.cwns.cso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.service.CSOService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

public class CSODisplayAction extends CWNSAction{

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);

		//get the facility Id
		Long facilityId = new Long(0);
		if(prr !=null && prr.getParameter("facilityId")!=null){
			facilityId = new Long(prr.getParameter("facilityId"));
		}else if(request.getParameter("facilityId")!=null){
			facilityId = new Long(request.getParameter("facilityId"));
		}
		log.debug("FacilityId=" + facilityId);
		if(facilityId.equals(new Long(0))){
			throw new ApplicationException("Facility Id is needed to delete the facility type");
		}
		
		CombinedSewer cs= csoService.getFacilityCSOInfo(facilityId);
		if(cs!=null){
			DynaValidatorForm  csoForm = (DynaValidatorForm)form;
			csoForm.set("statusId", cs.getCombinedSewerStatusRef().getCombinedSewerStatusId());
			request.setAttribute("statusLabel", cs.getCombinedSewerStatusRef().getName());
			if(cs.getDocAreaSquareMilesMsr()!=null){
				csoForm.set("docArea", cs.getDocAreaSquareMilesMsr().toString());
			}
			if(cs.getDocPopulationCount()!=null){
				csoForm.set("docPopulation", cs.getDocPopulationCount().toString());	
			}
			
			if(cs.getCcAreaSquareMilesMsr()!=null){
			  csoForm.set("ccArea", cs.getCcAreaSquareMilesMsr().toString());
			}  
			
			if(cs.getCcPopulationCount()!=null){
				csoForm.set("ccPopulation", cs.getCcPopulationCount().toString());
			}	
		}
		boolean updatable= facilityService.isUpdatable(user, facilityId);
		request.setAttribute("updatable", (updatable==true)?"true":"false");
		
		request.setAttribute("csoStatusReferences", csoService.getCSOStatusReference());
		
		//save token
		saveToken(request);
		
		return mapping.findForward("success");
	}
	
    /* set the facility service via Spring injection*/
    private CSOService csoService;
    public void setCsoService(CSOService cso){
       csoService = cso;    	
    }
    
    private FacilityService facilityService;
    public void setFacilityService(FacilityService f){
    	facilityService = f;    	
    }
    
    

}
