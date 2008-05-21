package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class FacilityCCValidator extends CostCurveValidator {

	public FacilityCCValidator() {
		super(FacilityService.DATA_AREA_FACILITY);
	}

	public boolean isCostCurveApplicable(String costCurveCode) {
		if(CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode)||CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) 
				|| CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) 
				|| CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) 
				|| CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode)
				|| CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){
			return true;
		}

		return false;
	}

	public boolean isErrors(Long facilityId, String costCurveCode, Set errors) {
		Facility f = facilityService.findByFacilityId(facilityId.toString());
		boolean isError = false;
		char presentTreatmentPlantType= (f.getPresentTreatmentPlantType()!=null)?f.getPresentTreatmentPlantType().charValue():'N';
		char projectedTreatmentPlantType= (f.getProjectedTreatmentPlantType()!=null)?f.getProjectedTreatmentPlantType().charValue():'N';
		
		//case 1: Present Plant Type (Mechanical/Lagoon)
		if(CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) || CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode)
			|| CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode)){
			if(presentTreatmentPlantType!= Facility.TREATMENT_PLANT_TYPE_MECHANICAL &&
					presentTreatmentPlantType!=Facility.TREATMENT_PLANT_TYPE_LAGOON){
				errors.add("error.ccv.present.notMechOrLagoon");
				isError = true;
			}			
		}

		//case 2: Projected Plant Type (Mechanical/Lagoon)
		if(CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode)||CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) || CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode)
				|| CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) || CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode)){
			if(projectedTreatmentPlantType!=Facility.TREATMENT_PLANT_TYPE_MECHANICAL 
					&& projectedTreatmentPlantType!=Facility.TREATMENT_PLANT_TYPE_LAGOON){
				errors.add("error.ccv.projected.notMechOrLagoon");
				isError = true;
			}			
		}

		//case 3: Present and Projected Plant Type must be same
		if(CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) 
				|| CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode)
				|| CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode)){	
			if(presentTreatmentPlantType != projectedTreatmentPlantType){
				errors.add("error.ccv.projected.notSame");
				isError = true;
			}			
		}
		
		//case 4: All the terminating facilities in the present Sewershed must be a present Treatment Plant
		if(CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){
			Collection otherFacilitiesInSewerShed = 
				populationService.getRelatedSewerShedFacilitiesByDischargeType(facilityId, PopulationService.PRESENT_ONLY);			
			
			if(otherFacilitiesInSewerShed!=null && otherFacilitiesInSewerShed.size()>0){
				Iterator facIter =  otherFacilitiesInSewerShed.iterator();
				while(facIter.hasNext()){
					Long curFacId = (Long)facIter.next();
					if (dischargeService.isTerminatingFacility(curFacId)){
						FacilityType facilityType = facilityTypeService.getFacilityType(curFacId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT);
						if (!(facilityType!=null && facilityType.getPresentFlag()=='Y')){
							errors.add("error.ccv.facility.terminating_facilities_in_sewershed_pre_treatment_plant");
							isError=true;
							break;							
						}
					}						
				}
			}
		}	
		return isError;
	}

	private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
	private FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService facilityTypeService) {
		this.facilityTypeService = facilityTypeService;
	}
	
	private PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		this.populationService = populationService;
	}
	
	public DischargeService dischargeService;
	public void setDischargeService(DischargeService dischargeService) {
		this.dischargeService = dischargeService;
	}
	
	public FlowService flowService;
	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}	
}
