package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.service.FacilityService;

public class FESValidatorFactory {
	
	public static FESValidator createValidator(long dataAreaIdToPerform) {
		FESValidator fesv =null;
		if(dataAreaIdToPerform == FacilityService.DATA_AREA_FACILITY.longValue()){
		  fesv = facilityValidator;	 
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_GEOGRAPHIC.longValue()){
			fesv = 	geographicValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_POC.longValue()){
			fesv = 	pocValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_POLLUTION.longValue()){
			fesv = 	pollutionValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_CSO.longValue()){
			fesv = 	csoValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_FLOW.longValue()){
			fesv = 	flowValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_POPULATION.longValue()){
			fesv = 	populationValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_EFFLUENT.longValue()){
			fesv = 	effluentValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_PERMITS.longValue()){
			fesv = 	permitValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_DISCHARGE.longValue()){
			fesv = 	dischargeValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_NEEDS.longValue()){
			fesv = 	needsValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_UNIT_PROCESS.longValue()){
			fesv = 	unitProcessValidator;
		}else if(dataAreaIdToPerform == FacilityService.DATA_AREA_UTIL_MANAGEMENT.longValue()){
			fesv = 	utilityManagementValidator;
		}				
		return fesv;
	}
	
	private static FacilityValidator facilityValidator;
	public void setFacilityValidator(FacilityValidator fs){
		facilityValidator = fs;
	}
	
	private static PointOfContactValidator pocValidator;
	public void setPocValidator(PointOfContactValidator pocv){
		pocValidator = pocv;
	}

	private static GeographicValidator geographicValidator;
	public void setGeographicValidator(GeographicValidator gv){
		geographicValidator = gv;
	}

	private static PollutionValidator pollutionValidator;
	public void setPollutionValidator(PollutionValidator pv){
		pollutionValidator = pv;
	}

	private static CSOValidator csoValidator;
	public void setCsoValidator(CSOValidator cv){
		csoValidator = cv;
	}
	
	private static FlowValidator flowValidator;
	public void setFlowValidator(FlowValidator fv){
		flowValidator = fv;
	}
	
	private static PermitValidator permitValidator;
	public void setPermitValidator(PermitValidator pv){
		permitValidator = pv;
	}
	
	private static PopulationValidator populationValidator;
	public void setPopulationValidator(PopulationValidator pv) {
		populationValidator = pv;
	}
	
	private static DischargeValidator dischargeValidator;
	public void setDischargeValidator(DischargeValidator dv) {
		dischargeValidator = dv;
	}
	
	private static NeedsValidator needsValidator;
	public void setNeedsValidator(NeedsValidator nv) {
		needsValidator = nv;
	}
	
	private static UnitProcessValidator unitProcessValidator;
	public void setUnitProcessValidator(UnitProcessValidator upv) {
		unitProcessValidator= upv;
	}
	
	private static UtilityManagementValidator utilityManagementValidator;
	public void setUtilityManagementValidator(UtilityManagementValidator umv) {
		utilityManagementValidator= umv;
	}
	
	private static EffluentValidator effluentValidator;
	public void setEffluentValidator(EffluentValidator ev) {
		effluentValidator= ev;
	}

}
