package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.service.FacilityService;

public class CostCurveValidatorFactory {
	
	public static CostCurveValidator createValidator(long dataAreaIdToPerform) {
		CostCurveValidator ccv =null;		
		if(dataAreaIdToPerform == FacilityService.DATA_AREA_FACILITY.longValue()){
			ccv = facilityCCValidator;	 
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_GEOGRAPHIC.longValue()){
			ccv = locationCCValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_POPULATION.longValue()){
			ccv = populationCCValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_FLOW.longValue()){
			ccv = flowCCValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_EFFLUENT.longValue()){
			ccv = effluentCCValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_DISCHARGE.longValue()){
			ccv = dischargeCCValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_CSO.longValue()){
			ccv = combinedSewerCCValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_NEEDS.longValue()){
			ccv = needsCCValidator;
		}		
		return ccv;
	}
	
	private static FacilityCCValidator facilityCCValidator;
	public void setFacilityCCValidator(FacilityCCValidator fccv){
		facilityCCValidator = fccv;
	}
	
	private static LocationCCValidator locationCCValidator;
	public void setLocationCCValidator(LocationCCValidator locCCV){
		locationCCValidator = locCCV;
	}
	
	private static PopulationCCValidator populationCCValidator;
	public void setPopulationCCValidator(PopulationCCValidator popCCV){
		populationCCValidator = popCCV;
	}
	
	private static FlowCCValidator flowCCValidator;
	public void setFlowCCValidator(FlowCCValidator floCCV){
		flowCCValidator = floCCV;
	}
	
	private static EffluentCCValidator effluentCCValidator;
	public void setEffluentCCValidator(EffluentCCValidator effCCV){
		effluentCCValidator = effCCV;
	}
	
	private static DischargeCCValidator dischargeCCValidator;
	public void setDischargeCCValidator(DischargeCCValidator disCCV){
		dischargeCCValidator = disCCV;
	}
	
	private static CombinedSewerCCValidator combinedSewerCCValidator;
	public void setCombinedSewerCCValidator(CombinedSewerCCValidator comCCV){
		combinedSewerCCValidator = comCCV;
	}		
	
	private static NeedsCCValidator needsCCValidator;
	public void setNeedsCCValidator(NeedsCCValidator nedCCV){
		needsCCValidator = nedCCV;
	}	
}
