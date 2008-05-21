/**
 * 
 */
package gov.epa.owm.mtb.cwns.cso;

import java.util.Date;

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
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.CSOService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

/**
 * @author Raj Lingam  *
 */

public class CSOSaveAction extends CWNSAction{

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//save
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
		boolean costCurveRerun = false;
		//get and set the CSO object
		DynaValidatorForm  csoForm = (DynaValidatorForm)form;
		String ccArea = (String)csoForm.get("ccArea");
		String ccPopulation = (String)csoForm.get("ccPopulation");
		String statusId = (String)csoForm.get("statusId");
		if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
		if(cs!=null){
			if((cs.getCcAreaSquareMilesMsr()!=null && !"".equals(ccArea.trim())&&!(cs.getCcAreaSquareMilesMsr().toString()).equalsIgnoreCase(ccArea))
					||(cs.getCcPopulationCount()!=null &&!"".equals(ccPopulation.trim())&&!(cs.getCcPopulationCount().toString()).equalsIgnoreCase(ccPopulation))
					||!statusId.equalsIgnoreCase(cs.getCombinedSewerStatusRef().getCombinedSewerStatusId()))
			   
				 costCurveRerun = true;
		}
		else
			costCurveRerun = true;
	    }
		
		if(cs==null){
			cs = new CombinedSewer();
			cs.setFacilityId(facilityId.longValue());
			//also set the facilityObject
			Facility facility = facilityService.findByFacilityId(facilityId.toString());
			cs.setFacility(facility);
		}	
		
		String docArea = (String)csoForm.get("docArea");
		if(docArea !=null && !"".equals(docArea.trim())){
		  cs.setDocAreaSquareMilesMsr(new Integer(docArea));
		}else{
		  cs.setDocAreaSquareMilesMsr(null);
		}  
		
		String docPopulation = (String)csoForm.get("docPopulation");
		if(docPopulation !=null && !"".equals(docPopulation.trim())){
			cs.setDocPopulationCount(new Integer(docPopulation));
		}else{
			cs.setDocPopulationCount(null);
		}
		
		
		
		if(ccArea !=null && !"".equals(ccArea.trim())){
			cs.setCcAreaSquareMilesMsr(new Integer(ccArea));
		}else{
			cs.setCcAreaSquareMilesMsr(null);
		}
		
		
		if(ccPopulation !=null && !"".equals(ccPopulation.trim())){
			cs.setCcPopulationCount(new Integer(ccPopulation));
		}else{
			cs.setCcPopulationCount(null);
		}
		
		cs.setLastUpdateTs(new Date());
		cs.setLastUpdateUserid(user.getUserId());
		
		cs.setCombinedSewerStatusRef(csoService.getCSOStatusReferenceById(statusId));
		csoService.saveCSO(cs);
		if(costCurveRerun)
		  csoService.costCurveRerun(facilityId);
		//perform post save updates
		facilityService.performPostSaveUpdates(facilityId, FacilityService.DATA_AREA_CSO, user);	
		//on success invalidate the token
		resetToken(request);
		return mapping.findForward("success");
	}
	
    /* set the facility service via Spring injection*/
    private CSOService csoService;
    public void setCsoService(CSOService cso){
       csoService = cso;    	
    }
    
    /* set the facility service via Spring injection*/
    private FacilityService facilityService;
    public void setFacilityService(FacilityService f){
       facilityService = f;    	
    }  
}
