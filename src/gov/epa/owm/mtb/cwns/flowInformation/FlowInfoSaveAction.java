package gov.epa.owm.mtb.cwns.flowInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

public class FlowInfoSaveAction extends CWNSAction {

private Logger log = Logger.getLogger(this.getClass());
	
    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	FlowInformationForm flowInfoForm = (FlowInformationForm)form;
    	PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
    	/*
    	if (isCancelled(request)){
			return mapping.findForward("success");
		  } */
    	
        // Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		double dbPreMunicipalFlow = flowService.getMunicipalPresentFlowRate(new Long(flowInfoForm.getSurveyFacilityId()));
		double preMunicipalFlow = flowInfoForm.getPreMunicipalFlow();
		double dbProMunicipalFlow = flowService.getMunicipalProjectedFlowRate(new Long(flowInfoForm.getSurveyFacilityId()));
		double proMunicipalFlow = flowInfoForm.getProMunicipalFlow();
		double dbPreTotalFlow = flowService.getPresentTotalFlow(new Long(flowInfoForm.getSurveyFacilityId()));
		double preTotalFlow = flowInfoForm.getPreTotalFlow(); 
        HashMap map = new HashMap(); 
		  if ("disable".equalsIgnoreCase(flowInfoForm.getTotalFlowState())) {
			if (flowInfoForm.getExistMunicipalFlow() != 0
					|| flowInfoForm.getPreMunicipalFlow() != 0
					|| flowInfoForm.getProMunicipalFlow() != 0) {
				double[] municipalflow = new double[3];
				municipalflow[0] = flowInfoForm.getExistMunicipalFlow();
				municipalflow[1] = flowInfoForm.getPreMunicipalFlow();
				municipalflow[2] = flowInfoForm.getProMunicipalFlow();
				map.put("1", municipalflow);
			}
			if (flowInfoForm.getExistIndustrialFlow() != 0
					|| flowInfoForm.getPreIndustrialFlow() != 0
					|| flowInfoForm.getProIndustrialFlow() != 0) {
				double[] industrialflow = new double[3];
				industrialflow[0] = flowInfoForm.getExistIndustrialFlow();
				industrialflow[1] = flowInfoForm.getPreIndustrialFlow();
				industrialflow[2] = flowInfoForm.getProIndustrialFlow();
				map.put("2", industrialflow);
			}
			if (flowInfoForm.getExistInfiltrationFlow() != 0
					|| flowInfoForm.getPreInfiltrationFlow() != 0
					|| flowInfoForm.getProInfiltrationFlow() != 0) {
				double[] infiltrationflow = new double[3];
				infiltrationflow[0] = flowInfoForm.getExistInfiltrationFlow();
				infiltrationflow[1] = flowInfoForm.getPreInfiltrationFlow();
				infiltrationflow[2] = flowInfoForm.getProInfiltrationFlow();
				map.put("3", infiltrationflow);
			}
			log.debug("Total flow---" + flowInfoForm.getExistTotalFlow());
			if (flowInfoForm.getExistTotalFlow() != 0
					|| flowInfoForm.getPreTotalFlow() != 0
					|| flowInfoForm.getProTotalFlow() != 0) {

				double[] totalflow = new double[3];
				totalflow[0] = flowInfoForm.getExistTotalFlow();
				totalflow[1] = flowInfoForm.getPreTotalFlow();
				totalflow[2] = flowInfoForm.getProTotalFlow();
				map.put("5", totalflow);
			}
			saveIndividualFlowInfo(flowInfoForm.getSurveyFacilityId(), map,	user.getUserId());
		} else {
			double[] totalflow = new double[3];
			totalflow[0] = flowInfoForm.getExistTotalFlow();
			totalflow[1] = flowInfoForm.getPreTotalFlow();
			totalflow[2] = flowInfoForm.getProTotalFlow();
			map.put("5", totalflow);
			saveTotalFlowInfo(flowInfoForm.getSurveyFacilityId(), map, user.getUserId());
		}

		/*
		 * if (flowInfoForm.getExistWWPFlow()!=0 ||
		 * flowInfoForm.getPreWWPFlow()!=0 || flowInfoForm.getProWWPFlow()!=0){
		 */
		double[] wwpFlow = new double[3];
		wwpFlow[0] = flowInfoForm.getExistWWPFlow();
		wwpFlow[1] = flowInfoForm.getPreWWPFlow();
		wwpFlow[2] = flowInfoForm.getProWWPFlow();
		saveWWPFlow(flowInfoForm.getSurveyFacilityId(), wwpFlow, user.getUserId());
		Long facilityId = new Long(flowInfoForm.getSurveyFacilityId());
		//cost curve rerun
		if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
			if(dbPreMunicipalFlow!=preMunicipalFlow){
				ArrayList costCurveIdList = new ArrayList();
    			costCurveIdList.add(new Long(1));
    			costCurveIdList.add(new Long(2));
    			costCurveIdList.add(new Long(4));
    			costCurveIdList.add(new Long(6));
    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
			}
			if(dbProMunicipalFlow!=proMunicipalFlow){
				ArrayList costCurveIdList = new ArrayList();
    			costCurveIdList.add(new Long(1));
    			costCurveIdList.add(new Long(2));
    			costCurveIdList.add(new Long(4));
    			costCurveIdList.add(new Long(5));
    			costCurveIdList.add(new Long(6));
    			costCurveIdList.add(new Long(10));
    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
			}
			if(dbPreTotalFlow!=preTotalFlow){
				ArrayList costCurveIdList = new ArrayList();
    			costCurveIdList.add(new Long(1));
    			costCurveIdList.add(new Long(2));
    			costCurveIdList.add(new Long(4));
    			costCurveIdList.add(new Long(6));
    			//costCurveIdList.add(new Long(7));
    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
    			Collection facilities = populationService.getRelatedSewerShedFacilitiesByDischargeType(facilityId, PopulationService.PRESENT_ONLY);
    			Iterator iter = facilities.iterator();
    			ArrayList costCurveList = new ArrayList();
    			costCurveList.add(new Long(7));
    			while (iter.hasNext()){
    				populationService.setUpCostCurvesForRerun(costCurveList, (Long)iter.next());
    			}
			}
			
		}
		facilityService.performPostSaveUpdates(facilityId, FacilityService.DATA_AREA_FLOW, user);
		// }
		return mapping.findForward("success");	 
    }
    
    private void saveWWPFlow(String surveyFacilityId, double[] wwpFlow, String userId) {
		flowService.saveWWPFlow(surveyFacilityId, wwpFlow, userId);
	}

	private void saveTotalFlowInfo(String surveyFacilityId, HashMap map, String userId) {
		flowService.saveTotalFlowInfo(surveyFacilityId, map, userId);
	}

	private void saveIndividualFlowInfo(String surveyFacilityId, HashMap map, String userId) {
		flowService.saveIndividualFlowInfo(surveyFacilityId, map, userId);
	}
    
	private FlowService flowService;
            
    //  set the flow service
    public void setFlowService(FlowService fls){
       flowService = fls;    	
    }  
    
    private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	private PopulationService populationService;
 	//  set the population service
	public void setPopulationService(PopulationService ps){
	    populationService = ps;    	
	}
        
}


