package gov.epa.owm.mtb.cwns.flowInformation;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.CwnsInfoRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FlowInformationAction extends CWNSAction {

	private Logger log = Logger.getLogger(this.getClass());
	
    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
	    		
        FlowInformationForm flowInfoForm = (FlowInformationForm)form;
                
        int resPresentPopCnt=0,nonResPresentPopCnt=0,resPresentUpstreamCnt=0,nonResPresentUpstreamCnt=0;
        int resProjectedPopCnt=0,nonResProjectedPopCnt=0,resProjectedUpstreamCnt=0,nonResProjectedUpstreamCnt=0;
        DecimalFormat df = new DecimalFormat("#0.000");
        
        //Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
	
        //get the facility ID from the page parameter - configured as input parameter in provider.xml
        String facNum = prr.getParameter("facilityId");
 	     log.debug("facility number---"+facNum);	
 	    Long facilityId = new Long(facNum);
 	    
 	   /* determine what needs to be done */
		//String action = flowInfoForm.getFlowAct();
		/*		
		if (action.equals("save")){ 
			HashMap map = new HashMap(); 
			  if ("disable".equalsIgnoreCase(flowInfoForm.getTotalFlowState())){
				  if (flowInfoForm.getExistMunicipalFlow()!=0 || flowInfoForm.getPreMunicipalFlow()!=0 
						  || flowInfoForm.getProMunicipalFlow()!=0){
				  float[] municipalflow = new float[3];
				  municipalflow[0] = flowInfoForm.getExistMunicipalFlow();
				  municipalflow[1] = flowInfoForm.getPreMunicipalFlow();
				  municipalflow[2] = flowInfoForm.getProMunicipalFlow();
				  map.put("1", municipalflow);
				  }
				  if (flowInfoForm.getExistIndustrialFlow()!=0 || flowInfoForm.getPreIndustrialFlow()!=0
						  || flowInfoForm.getProIndustrialFlow() !=0){
				  float[] industrialflow = new float[3];
				  industrialflow[0] = flowInfoForm.getExistIndustrialFlow();
				  industrialflow[1] = flowInfoForm.getPreIndustrialFlow();
				  industrialflow[2] = flowInfoForm.getProIndustrialFlow();
				  map.put("2", industrialflow);
				  }
				  if (flowInfoForm.getExistInfiltrationFlow()!=0 || flowInfoForm.getPreInfiltrationFlow()!=0
						  || flowInfoForm.getProInfiltrationFlow()!=0){
				  float[] infiltrationflow = new float[3];
				  infiltrationflow[0] = flowInfoForm.getExistInfiltrationFlow();
				  infiltrationflow[1] = flowInfoForm.getPreInfiltrationFlow();
				  infiltrationflow[2] = flowInfoForm.getProInfiltrationFlow();
				  map.put("3", infiltrationflow);
				  }
				  saveIndividualFlowInfo(flowInfoForm.getSurveyFacilityId(), map, user.getUserId());
			  }
			  else {
				  float[] totalflow = new float[3];
				  totalflow[0] = flowInfoForm.getExistTotalFlow();
				  totalflow[1] = flowInfoForm.getPreTotalFlow();
				  totalflow[2] = flowInfoForm.getProTotalFlow();
				  map.put("5", totalflow);
				  saveTotalFlowInfo(flowInfoForm.getSurveyFacilityId(), map, user.getUserId());
			  }
			  
			  if (flowInfoForm.getExistWWPFlow()!=0 || flowInfoForm.getPreWWPFlow()!=0 
					  || flowInfoForm.getProWWPFlow()!=0){
			     float[] wwpFlow = new float[3];
			     wwpFlow[0] = flowInfoForm.getExistWWPFlow();
			     wwpFlow[1] = flowInfoForm.getPreWWPFlow();
			     wwpFlow[2] = flowInfoForm.getProWWPFlow();
			     saveWWPFlow(flowInfoForm.getSurveyFacilityId(), wwpFlow, user.getUserId());
			  }   
		}
 	    */
 	    /* obtain the Survey Facility object */
	    Facility facility = facilityService.findByFacilityId(facNum);
	    	
 	    // 	 obtain feedback version for the facility 
		Facility feedbackVersion = (Facility)facilityService.getFeedbackVersionOfFacility(facNum);	
		
		// check for warning message	   	
		String Sreviewstatustype = facility.getReviewStatusRef().getReviewStatusId();
		String Freviewstatustype = feedbackVersion == null?"":feedbackVersion.getReviewStatusRef().getReviewStatusId();
		if (("SAS".equalsIgnoreCase(Sreviewstatustype) || "SIP".equalsIgnoreCase(Sreviewstatustype) || "SCR".equalsIgnoreCase(Sreviewstatustype))
			&& (feedbackVersion != null &&
			   ("LAS".equalsIgnoreCase(Freviewstatustype) || "LIP".equalsIgnoreCase(Freviewstatustype)))) {
			flowInfoForm.setShowWarningMessage("Y");	
		}
 	   	  
         // Check if facility is updatable or not and set form attribute
	     if (facilityService.isUpdatable(user, new Long(facNum))){
	    	 flowInfoForm.setIsUpdatable("Y");
		  }
 	    
	    CwnsInfoRef cwnsInfo = flowService.getCwnsInfoRef();
	    float nonResMulti = ((BigDecimal)cwnsInfo.getFlowRatioNonResMultiplier()).floatValue();
	    float preDesMultiplier = ((BigDecimal)cwnsInfo.getPresentDesignFlowMultiplier()).floatValue();
	    int low = cwnsInfo.getFlowLowRatio();
	    int high = cwnsInfo.getFlowHighRatio();
	    
	    // Get upstream facilities population total for a selected facility
	    PopulationHelper PH = populationService.getUpStreamFacilitiesPopulationTotal(facNum);
	    if (PH != null){
	       resPresentUpstreamCnt = PH.getPresentResPopulation();
	       nonResPresentUpstreamCnt = PH.getPresentNonResPopulation();
	       resProjectedUpstreamCnt = PH.getProjectedResPopulation();
	       nonResProjectedUpstreamCnt = PH.getProjectedNonResPopulation();
	       log.debug("Upstream collection--"+resPresentUpstreamCnt+" "+nonResPresentUpstreamCnt+" "+resProjectedUpstreamCnt+" "+nonResProjectedUpstreamCnt);
	    }
	    
	    HashMap recPresentPopulationCount = populationService.getPresentPopulationByPopIdAndFacId(facNum, "1", "2");
	    if (recPresentPopulationCount != null && !recPresentPopulationCount.isEmpty()){
		    if (recPresentPopulationCount.containsKey("1")){
		        resPresentPopCnt=((Integer)recPresentPopulationCount.get("1")).intValue();
		    }    
		    if (recPresentPopulationCount.containsKey("2")){
		        nonResPresentPopCnt = ((Integer)recPresentPopulationCount.get("2")).intValue();
		    }    
		 }
	    HashMap recProjectedPopulationCount = populationService.getProjectedPopulationByPopIdAndFacId(facNum, "1", "2");
	    if (recProjectedPopulationCount != null && !recProjectedPopulationCount.isEmpty()){
		    if (recProjectedPopulationCount.containsKey("1")){
		    	Object[] o = (Object[])recProjectedPopulationCount.get("1");
		        resProjectedPopCnt=((Integer)o[0]).intValue();
		    }    
		    if (recProjectedPopulationCount.containsKey("2")){
		    	Object[] o = (Object[])recProjectedPopulationCount.get("2");
		        nonResProjectedPopCnt = ((Integer)o[0]).intValue();
		    }    
		 }
	    
	    HashMap wwpFlow = flowService.getWWPFlowValues(facilityId);
	    	if (wwpFlow != null && !(wwpFlow.isEmpty())){
	    		if (wwpFlow.get("Existingflow") != null)
	    			flowInfoForm.setExistWWPFlow(((BigDecimal)wwpFlow.get("Existingflow")).doubleValue());
	    		if (wwpFlow.get("Presentflow") != null)
	    			flowInfoForm.setPreWWPFlow(((BigDecimal)wwpFlow.get("Presentflow")).doubleValue());
	    		if (wwpFlow.get("Projectedflow") != null)
	    			flowInfoForm.setProWWPFlow(((BigDecimal)wwpFlow.get("Projectedflow")).doubleValue());
	    	}	
	    
 	    HashMap individualFlowValues = flowService.getIndividualFlowValues(facilityId);
 	    if (individualFlowValues!=null && !(individualFlowValues.isEmpty())){
 	    	double existTotalFlow=0.0,preTotalFlow=0.0,proTotalFlow=0.0;
 	    	if (individualFlowValues.containsKey("1")){
 	    		
 	    		Object[] values = (Object[])individualFlowValues.get("1");
 	    		if (values[0]!=null){ 	    					
 	    		   flowInfoForm.setExistMunicipalFlow(((BigDecimal)values[0]).doubleValue());
 	    		  existTotalFlow = existTotalFlow+((BigDecimal)values[0]).doubleValue();
 	    		}   
 	    		if (values[1] != null){
 	    		   flowInfoForm.setPreMunicipalFlow(((BigDecimal)values[1]).doubleValue());
 	    		   preTotalFlow = preTotalFlow+((BigDecimal)values[1]).doubleValue();
 	    		}   
 	    		if (values[2] != null){
  	    		   flowInfoForm.setProMunicipalFlow(((BigDecimal)values[2]).doubleValue());
  	    		   proTotalFlow = proTotalFlow+((BigDecimal)values[2]).doubleValue();
 	    		}   
 	    	}
 	    	if (individualFlowValues.containsKey("2")){
 	    		Object[] values = (Object[])individualFlowValues.get("2");
 	    		if (values[0]!=null){ 	    					
 	    		   flowInfoForm.setExistIndustrialFlow(((BigDecimal)values[0]).doubleValue());
 	    		  existTotalFlow = existTotalFlow+((BigDecimal)values[0]).doubleValue();
 	    		}   
 	    		if (values[1] != null){
 	    		   flowInfoForm.setPreIndustrialFlow(((BigDecimal)values[1]).doubleValue());
 	    		   preTotalFlow = preTotalFlow+((BigDecimal)values[1]).doubleValue();
 	    		}   
 	    		if (values[2] != null){
  	    		   flowInfoForm.setProIndustrialFlow(((BigDecimal)values[2]).doubleValue());
  	    		   proTotalFlow = proTotalFlow+((BigDecimal)values[2]).doubleValue();
 	    		}   
 	    	}
 	    	if (individualFlowValues.containsKey("3")){
 	    		Object[] values = (Object[])individualFlowValues.get("3");
 	    		if (values[0]!=null){ 	    					
 	    		   flowInfoForm.setExistInfiltrationFlow(((BigDecimal)values[0]).doubleValue());
 	    		  existTotalFlow = existTotalFlow+((BigDecimal)values[0]).doubleValue();
 	    		}   
 	    		if (values[1] != null){
 	    		   flowInfoForm.setPreInfiltrationFlow(((BigDecimal)values[1]).doubleValue());
 	    		   preTotalFlow = preTotalFlow+((BigDecimal)values[1]).doubleValue();
 	    		}   
 	    		if (values[2] != null){
  	    		   flowInfoForm.setProInfiltrationFlow(((BigDecimal)values[2]).doubleValue());
  	    		   proTotalFlow = proTotalFlow+((BigDecimal)values[2]).doubleValue();
 	    		}   
 	    	}
 	      	
 	      flowInfoForm.setExistTotalFlow(new Double(df.format(existTotalFlow)).doubleValue());
 	      flowInfoForm.setPreTotalFlow(new Double(df.format(preTotalFlow)).doubleValue());
 	      flowInfoForm.setProTotalFlow(new Double(df.format(proTotalFlow)).doubleValue());
 	      flowInfoForm.setTotalFlowState("disable");
 	    }
 	    else{
 	    	
 	    	HashMap totalFlow = flowService.getTotalFlowValues(facilityId);
 	    	if (totalFlow != null && !(totalFlow.isEmpty())){
 	    		flowInfoForm.setTotalFlowState("enable");
 	    		if (totalFlow.get("Existingflow") != null)
 	    			flowInfoForm.setExistTotalFlow(((BigDecimal)totalFlow.get("Existingflow")).doubleValue());
 	    		if (totalFlow.get("Presentflow") != null)
 	    			flowInfoForm.setPreTotalFlow(((BigDecimal)totalFlow.get("Presentflow")).doubleValue());
 	    		if (totalFlow.get("Projectedflow") != null)
 	    			flowInfoForm.setProTotalFlow(((BigDecimal)totalFlow.get("Projectedflow")).doubleValue());
 	    	}
 	    }
 	    /*
 	    // Check if facility is in a sewershed
 	    Collection c = populationService.getRelatedSewerShedFacilities(facNum);
 	    //if facility is in a sewershed check if the present sedign flow is updatable (Only for state user)
 	    if (c!=null && !(c.isEmpty())){
 	    	
 	    	boolean updatable = false;
 	    	Iterator iter = c.iterator();
 	    	while (iter.hasNext()){
 	    		if(flowService.isPreDesignFlowUpdatable((String)iter.next())){
 	    			updatable = true;
 	    			break;
 	    		}
 	    	}
 	    	log.debug("sewershed size---"+updatable);
 	    	if(!updatable) flowInfoForm.setIsPreDesignFlowUpdatable("N");
 	    }
 	    */
 	    double present = resPresentPopCnt+resPresentUpstreamCnt+(nonResMulti * (nonResPresentPopCnt+nonResPresentUpstreamCnt));
 	    double projected = resProjectedPopCnt+resProjectedUpstreamCnt+(nonResMulti * (nonResProjectedPopCnt+nonResProjectedUpstreamCnt));
 	    flowInfoForm.setPresent(present);
 	    flowInfoForm.setProjected(projected);
 	    flowInfoForm.setHigh(high);
 	    flowInfoForm.setLow(low);
 	    flowInfoForm.setPreDesMultiplier(preDesMultiplier);
 	    flowInfoForm.setIsPreFlowUpdatable(flowService.isPreFlowUpdatable(facilityId));
 	    flowInfoForm.setIsProFlowUpdatable(flowService.isProFlowUpdatable(facilityId));
 	    flowInfoForm.setSurveyFacilityId(new Long(facility.getFacilityId()).toString());
 	   if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
 	    flowInfoForm.setIsProMunicipalFlowUpdatable(flowService.isProMunicipalFlowUpdatable(facilityId)?"Y":"N");
 	   flowInfoForm.setIsTotalFlowUpdatable(flowService.isTotalFlowUpdatable(facilityId)?"Y":"N");
 	    //flowInfoForm.setTotalFlowState("disable");
 	   }
       // for warning message
		ArrayList warnMessages = new ArrayList(); 	    
      if (flowInfoForm.getExistTotalFlow() > preDesMultiplier * flowInfoForm.getPreTotalFlow()){
    	  warnMessages.add("Total existing flow is more than "+preDesMultiplier+" times design flow");
      }  
 	  if(flowInfoForm.getPreTotalFlow()>=10){
 		  warnMessages.add("This is a Large Community-Unit Process data has to be entered before submission for Federal review");
 		  
 	  }	   
 	   request.setAttribute("warnMessages", warnMessages); 
 	   request.setAttribute("flowInfo", flowInfoForm);
 	    return mapping.findForward("success");
    }
    /*
    private void saveWWPFlow(String surveyFacilityId, float[] wwpFlow, String userId) {
		flowService.saveWWPFlow(surveyFacilityId, wwpFlow, userId);
	}

	private void saveTotalFlowInfo(String surveyFacilityId, HashMap map, String userId) {
		flowService.saveTotalFlowInfo(surveyFacilityId, map, userId);
	}

	private void saveIndividualFlowInfo(String surveyFacilityId, HashMap map, String userId) {
		flowService.saveIndividualFlowInfo(surveyFacilityId, map, userId);
	}
    */
	private FlowService flowService;
    private FacilityService facilityService;
    private PopulationService populationService;
        
    //  set the flow service
    public void setFlowService(FlowService fls){
       flowService = fls;    	
    }  
    
    // set facility service
    public void setFacilityService(FacilityService fs){
    	facilityService = fs;
    }
    
    // set population service
    public void setPopulationService(PopulationService ps){
    	populationService = ps;
    }

}
