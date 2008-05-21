package gov.epa.owm.mtb.cwns.flowInformation;

import org.apache.struts.action.ActionForm;

public class FlowInformationForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String showWarningMessage = "N";
	
	private String flowAct = "none";
	
	private String surveyFacilityId;
			
	private String isPreFlowUpdatable;
	
	private String isProFlowUpdatable;
	
	private String totalFlowState = "disable";
		
	private double existMunicipalFlow;
	
	private double preMunicipalFlow;
	
    private double proMunicipalFlow;
    
    private double existIndustrialFlow;
	
	private double preIndustrialFlow;
	
    private double proIndustrialFlow;
    
    private double existInfiltrationFlow;
	
	private double preInfiltrationFlow;
	
    private double proInfiltrationFlow;
    
    private double existWWPFlow;
	
	private double preWWPFlow;
	
    private double proWWPFlow;
    
    private double existTotalFlow;
	
	private double preTotalFlow;
	
    private double proTotalFlow;
    
    private String isUpdatable = "N";
    
    private double present;
    
    private double projected;
    
    private int low;
    
    private int high;
    
    private float preDesMultiplier;
    
    private String isPreDesignFlowUpdatable;
    
    private String isProMunicipalFlowUpdatable = "Y";
    
    private String isTotalFlowUpdatable = "Y";
    
	public String getIsProMunicipalFlowUpdatable() {
		return isProMunicipalFlowUpdatable;
	}

	public void setIsProMunicipalFlowUpdatable(String isProMunicipalFlowUpdatable) {
		this.isProMunicipalFlowUpdatable = isProMunicipalFlowUpdatable;
	}

	public String getIsTotalFlowUpdatable() {
		return isTotalFlowUpdatable;
	}

	public void setIsTotalFlowUpdatable(String isTotalFlowUpdatable) {
		this.isTotalFlowUpdatable = isTotalFlowUpdatable;
	}

	public static String convertFloatToString(double i)
	{
		if(i==0.0) 
			return "";
		else
			return Double.toString(i);
	}
    
	public float getPreDesMultiplier() {
		return preDesMultiplier;
	}

	public void setPreDesMultiplier(float preDesMultiplier) {
		this.preDesMultiplier = preDesMultiplier;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public double getPresent() {
		return present;
	}

	public void setPresent(double present) {
		this.present = present;
	}

	public double getProjected() {
		return projected;
	}

	public void setProjected(double projected) {
		this.projected = projected;
	}

	public String getIsUpdatable() {
		return isUpdatable;
	}

	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	public String getTotalFlowState() {
		return totalFlowState;
	}

	public void setTotalFlowState(String totalFlowState) {
		this.totalFlowState = totalFlowState;
	}
	
	public double getExistIndustrialFlow() {
		return existIndustrialFlow;
	}

	public void setExistIndustrialFlow(double existIndustrialFlow) {
		this.existIndustrialFlow = existIndustrialFlow;
	}

	public double getExistInfiltrationFlow() {
		return existInfiltrationFlow;
	}

	public void setExistInfiltrationFlow(double existInfiltrationFlow) {
		this.existInfiltrationFlow = existInfiltrationFlow;
	}

	public double getExistMunicipalFlow() {
		return existMunicipalFlow;
	}

	public void setExistMunicipalFlow(double existMunicipalFlow) {
		this.existMunicipalFlow = existMunicipalFlow;
	}

	public double getPreIndustrialFlow() {
		return preIndustrialFlow;
	}

	public void setPreIndustrialFlow(double preIndustrialFlow) {
		this.preIndustrialFlow = preIndustrialFlow;
	}

	public double getPreInfiltrationFlow() {
		return preInfiltrationFlow;
	}

	public void setPreInfiltrationFlow(double preInfiltrationFlow) {
		this.preInfiltrationFlow = preInfiltrationFlow;
	}

	public double getPreMunicipalFlow() {
		return preMunicipalFlow;
	}

	public void setPreMunicipalFlow(double preMunicipalFlow) {
		this.preMunicipalFlow = preMunicipalFlow;
	}

	public double getProIndustrialFlow() {
		return proIndustrialFlow;
	}

	public void setProIndustrialFlow(double proIndustrialFlow) {
		this.proIndustrialFlow = proIndustrialFlow;
	}

	public double getProInfiltrationFlow() {
		return proInfiltrationFlow;
	}

	public void setProInfiltrationFlow(double proInfiltrationFlow) {
		this.proInfiltrationFlow = proInfiltrationFlow;
	}

	public double getProMunicipalFlow() {
		return proMunicipalFlow;
	}

	public void setProMunicipalFlow(double proMunicipalFlow) {
		this.proMunicipalFlow = proMunicipalFlow;
	}

	public String getIsPreFlowUpdatable() {
		return isPreFlowUpdatable;
	}

	public void setIsPreFlowUpdatable(String isPreFlowUpdatable) {
		this.isPreFlowUpdatable = isPreFlowUpdatable;
	}

	public String getIsProFlowUpdatable() {
		return isProFlowUpdatable;
	}

	public void setIsProFlowUpdatable(String isProFlowUpdatable) {
		this.isProFlowUpdatable = isProFlowUpdatable;
	}

	public double getExistTotalFlow() {
		return existTotalFlow;
	}

	public void setExistTotalFlow(double existTotalFlow) {
		this.existTotalFlow = existTotalFlow;
	}

	public double getPreTotalFlow() {
		return preTotalFlow;
	}

	public void setPreTotalFlow(double preTotalFlow) {
		this.preTotalFlow = preTotalFlow;
	}

	public double getProTotalFlow() {
		return proTotalFlow;
	}

	public void setProTotalFlow(double proTotalFlow) {
		this.proTotalFlow = proTotalFlow;
	}

	public String getShowWarningMessage() {
		return showWarningMessage;
	}

	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}

	public String getFlowAct() {
		return flowAct;
	}

	public void setFlowAct(String flowAct) {
		this.flowAct = flowAct;
	}

	public String getSurveyFacilityId() {
		return surveyFacilityId;
	}

	public void setSurveyFacilityId(String surveyFacilityId) {
		this.surveyFacilityId = surveyFacilityId;
	}

	public String getIsPreDesignFlowUpdatable() {
		return isPreDesignFlowUpdatable;
	}

	public void setIsPreDesignFlowUpdatable(String isPreDesignFlowUpdatable) {
		this.isPreDesignFlowUpdatable = isPreDesignFlowUpdatable;
	}

	public double getExistWWPFlow() {
		return existWWPFlow;
	}

	public void setExistWWPFlow(double existWWPFlow) {
		this.existWWPFlow = existWWPFlow;
	}

	public double getPreWWPFlow() {
		return preWWPFlow;
	}

	public void setPreWWPFlow(double preWWPFlow) {
		this.preWWPFlow = preWWPFlow;
	}

	public double getProWWPFlow() {
		return proWWPFlow;
	}

	public void setProWWPFlow(double proWWPFlow) {
		this.proWWPFlow = proWWPFlow;
	}
}
