package gov.epa.owm.mtb.cwns.facilityType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

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
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChange;
import gov.epa.owm.mtb.cwns.model.FacilityTypeRef;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

public class FacilityTypeEditAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//Delete facility Type
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
		
		//get the facilityType Id
		Long facilityTypeId = new Long(0);
		if(prr!=null && prr.getParameter("facilityTypeId")!=null) {
			facilityTypeId = new Long(prr.getParameter("facilityTypeId"));
		}else if(request.getParameter("facilityTypeId")!=null){
			facilityTypeId = new Long(request.getParameter("facilityTypeId"));
		}
		log.debug("FacilityTypeId=" + facilityTypeId);
		if(facilityTypeId == new Long(0)){
			throw new ApplicationException("Facility Id is needed to delete the facility Type");
		}
		
		FacilityType ft= facilityTypeService.getFacilityType(facilityId, facilityTypeId);
		
		// set the form varaibles
		DynaValidatorForm  facilityTypeForm = (DynaValidatorForm)form;
		facilityTypeForm.set("facilityType", ft.getId().getFacilityTypeId()+"");
		if(ft.getPresentFlag()=='Y' && ft.getProjectedFlag()=='Y'){
			facilityTypeForm.set("status", FacilityTypeService.FACILITY_TYPE_STATUS_BOTH);
		}else if(ft.getPresentFlag()=='Y'){
			facilityTypeForm.set("status", FacilityTypeService.FACILITY_TYPE_STATUS_PRESENT);
		}else{
			facilityTypeForm.set("status", FacilityTypeService.FACILITY_TYPE_STATUS_PROJECTED);
		}
		
		Facility facility = ft.getFacility();
		if(facilityTypeId.longValue()==FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT.longValue()){
			Character presentTPType = facility.getPresentTreatmentPlantType();
			Character projectedTPType = facility.getProjectedTreatmentPlantType();
			
			if(presentTPType!=null && !"".equals(presentTPType.toString())){
				facilityTypeForm.set("presentTPType", presentTPType.toString());
			}
			
			if(projectedTPType!=null && !"".equals(projectedTPType.toString())){
				facilityTypeForm.set("projectedTPType", projectedTPType.toString());
			}
		}
		
//		String npsCode = facility.getNpsTypeCode();
//		if(npsCode!=null && !"".equals(npsCode)){
//			facilityTypeForm.set("npsStatus", npsCode);
//		}
		
		
		//facilityTypeForm.get("availableChanges");
		facilityTypeForm.set("selectedChanges", getChangeTypes(ft.getFacilityTypeChanges()));
		facilityTypeForm.set("mode", "edit");
		
		//selected changes
		request.setAttribute("selectedChangeTypeRefs", getChangeTypeRef(ft.getFacilityTypeChanges()));
		request.setAttribute("selectedFacilityTypeId", facilityTypeId);
		
		//save token
		saveToken(request);
		
		return mapping.findForward("success");	
	}
	
	private String[] getChangeTypes(Set changes){
		String[] changeIds = new String[changes.size()];
		int i=0;
		for (Iterator iter = changes.iterator(); iter.hasNext();) {
			FacilityTypeChange ftc = (FacilityTypeChange) iter.next();
			changeIds[i] = ftc.getId().getChangeTypeId()+"";
			i++;			
		}
		return changeIds;		
	}
	
	private Collection getChangeTypeRef(Set changes){
		ArrayList ctr = new ArrayList();
		for (Iterator iter = changes.iterator(); iter.hasNext();) {
			FacilityTypeChange ftc = (FacilityTypeChange) iter.next();
			ctr.add( ftc.getChangeTypeRef());			
		}		
		return ctr;
	}
	
    /* set the facility service via Spring injection*/
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }	
    
    /* set the facility service via Spring injection*/
    private FacilityTypeService facilityTypeService;
    public void setFacilityTypeService(FacilityTypeService fts){
       facilityTypeService = fts;    	
    }	

}
