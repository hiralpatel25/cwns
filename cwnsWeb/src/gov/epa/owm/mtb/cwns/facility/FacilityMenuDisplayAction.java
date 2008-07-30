package gov.epa.owm.mtb.cwns.facility;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.NeedsService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FacilityMenuDisplayAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		PortletRenderRequest prr = (PortletRenderRequest)
		request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    
		//Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser currentUser = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);

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
		
		PopulationHelper pop = populationService.getUpStreamFacilitiesPopulationTotal(facilityId.toString());
		if(pop!=null){
			request.setAttribute("population", pop);	
		}else{
			log.debug("Upstream population is null");
		}
		
		//get dataareas query parameter
		String daqp=facilityTypeService.getFacilityDataAreasQueryParam(facilityId);
		if(daqp!=null){
			request.setAttribute("dataAreaQueryParameters", daqp);	
		}else{
			log.debug("data area parameters are null");
		}
		
		//set isSmallCommunity
		if (facilityService.isSmallCommunity(facilityId)){
			request.setAttribute("isSmallCommunity", "true");			
		}else{
			request.setAttribute("isSmallCommunity", "false");
		}
		
		Object objFFacility = facilityService.getFeedbackVersionOfFacility(facilityId.toString());
		Facility f_facility = null;
		if (objFFacility!=null){
			f_facility = (Facility)objFFacility;
		}
		
		//set isFeedbackFacility
		if (facilityService.hasFeedbackFacilityWithSRR(facilityId) || facilityService.isFeedBack(facilityId, currentUser)){
			String f_facilityId = Long.toString(f_facility.getFacilityId());
			request.setAttribute("isFeedbackFacility", "true");			
			request.setAttribute("f_facilityId", Long.toString(f_facility.getFacilityId()));
			
			//get population for the F facility
			PopulationHelper f_population = populationService.getUpStreamFacilitiesPopulationTotal(f_facilityId);
			if(f_population!=null){
				request.setAttribute("f_population", f_population);	
			}else{
				log.debug("Upstream population is null for the feedback facility");
			}
			
			//get data areas query parameter for the F facility
			String f_daqp=facilityTypeService.getFacilityDataAreasQueryParam(new Long(f_facilityId));
			if(f_daqp!=null){
				request.setAttribute("f_dataAreaQueryParameters", f_daqp);	
			}else{
				log.debug("data area parameters are null for the feedback facility");
			}			
		}else{
			request.setAttribute("isFeedbackFacility", "false");
		}
		
		if(!facilityService.isDataAreaViewable(currentUser, facilityId, FacilityService.DATA_AREA_NEEDS)){
			request.setAttribute("showReports", "N");
		}else{
			request.setAttribute("showReports", "Y");
		}
		
		request.setAttribute("expiredDocsSize", new Integer(needsService.getExpiredNeedsDocumentsSize(facilityId)));
		return mapping.findForward("success");
	}
	
	private PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		this.populationService = populationService;
	}
	
	private FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService facilityTypeService) {
		this.facilityTypeService = facilityTypeService;
	}
	
	private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
	private NeedsService needsService;
	public void setNeedsService(NeedsService needsService) {
		this.needsService = needsService;
	}
}
