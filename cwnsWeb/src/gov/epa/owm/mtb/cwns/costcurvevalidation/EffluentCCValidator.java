package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.EffluentService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.util.Collection;
import java.util.Set;

public class EffluentCCValidator extends CostCurveValidator{
	public EffluentCCValidator(){
		super(FacilityService.DATA_AREA_EFFLUENT);
	}
	
	public boolean isCostCurveApplicable(String costCurveCode) {
		boolean isApplicable = false;
		if(CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
			CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
			CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
			CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
			CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode)){
			isApplicable=true;
		}
		return isApplicable;
	}
	
	public boolean isErrors(Long facilityId, String costCurveCode, Set errors) {
		boolean isError=false;
		Facility facility = facilityService.findByFacilityId(facilityId.toString());
		Character present_treatment_plant_type = facility.getPresentTreatmentPlantType();
		Character projected_treatment_plant_type = facility.getProjectedTreatmentPlantType();		
		long presentEffluentLevel = effluentService.getPresentFacilityEffluentLevel(facilityId);		
		long projectedEffluentLevel = effluentService.getProjectedFacilityEffluentLevel(facilityId);
		
		//case 1: Present Effluent must be Secondary or Advanced
		//case 3: If present Plant Type = Mechanical: At least one Present Advanced Treatment 
		//Indicator must be Y if Present Effluent is Advanced
		if(CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode)){
			//case 1: implementation
			if (!((presentEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID)
					|| (presentEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID))){
				errors.add("error.ccv.effluent.present_effluent_not_secondary_or_advanced");
				isError=true;
			}
			
			//case 3: implementation
			if (facility!=null && present_treatment_plant_type!=null && 
					(present_treatment_plant_type.charValue()==Facility.TREATMENT_PLANT_TYPE_MECHANICAL)){
				if(projectedEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
					Collection presentAdvTreatmentTypes = effluentService.getPresentAdvanceTreatmentTypes(facilityId);
					if (presentEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
						if (!(presentAdvTreatmentTypes!=null && presentAdvTreatmentTypes.size()>0)){
							errors.add("error.ccv.effluent.pre_plant_type_M_and_pre_eff_advanced_and_adv_treat_indicator_not_Y");
							isError=true;							
						}
					}					
				}
			}
		}		
		
		//case 2: Projected Effluent must be Secondary or Advanced		
		//case 4: If projected Plant Type = Mechanical:	At least one Projected Advanced Treatment 
		//Indicator must be Y if Projected Effluent is Advanced
		if(CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode)){			
			//case 2: implementation
			if (!((projectedEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID)
					|| (projectedEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID))){
				errors.add("error.ccv.effluent.projected_effluent_not_secondary_or_advanced");
				isError=true;
			}			
			
			//case 4: implementation
			if (facility!=null && projected_treatment_plant_type!=null && 
					(projected_treatment_plant_type.charValue()==Facility.TREATMENT_PLANT_TYPE_MECHANICAL)){
				if (projectedEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
					Collection projectedAdvTreatmentTypes = effluentService.getProjectedAdvanceTreatmentTypes(facilityId);
					if (projectedEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
						if (!(projectedAdvTreatmentTypes!=null && projectedAdvTreatmentTypes.size()>0)){
							errors.add("error.ccv.effluent.fut_plant_type_M_and_fut_eff_advanced_and_adv_treat_indicator_not_Y");
							isError=true;
						}
					}				
				}
			}			
		}		
		
		//case 5: If projected Treatment Plant Type = Lagoon, Present and Projected effluent 
		//must be secondary and advanced respectively
		//case 6: If projected Treatment Plant Type = Mechanical, Projected Effluent must be 'higher' than Present, 
		//in the following sequence: 
		//Primary or Advanced Primary Secondary
		//Advanced with no BOD and no Nitrogen or Phosphorus
		//Advanced with no BOD and with Nitrogen or Phosphorus
		//Advanced with no BOD and with Nitrogen or Phosphorus
		//Advanced with BOD and no Nitrogen or Phosphorus
		//Advanced with BOD and with Nitrogen or Phosphorus
		//Advanced with BOD and with Nitrogen or Phosphorus		
		if(CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode)){		
			if (facility!=null && present_treatment_plant_type!=null && 
					(present_treatment_plant_type.charValue()==Facility.TREATMENT_PLANT_TYPE_LAGOON)){
				if(!((presentEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID)&&
						(projectedEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID))){
					errors.add("error.ccv.effluent.fut_plant_type_L_and_pre_level_not_sec_or_pro_level_not_adv");
					isError=true;
				}
			}
			
			//case 6: implementation
			if (facility!=null && projected_treatment_plant_type!=null && 
					(projected_treatment_plant_type.charValue()==Facility.TREATMENT_PLANT_TYPE_MECHANICAL)){
				Collection projectedAdvTreatmentIndicators = effluentService.getProjectedAdvanceTreatmentTypes(facilityId);
				Collection presentAdvTreatmentIndicators = effluentService.getPresentAdvanceTreatmentTypes(facilityId);			
				int projectedEffOrder = effluentService.getTreatmentLevelOrder(projectedEffluentLevel, projectedAdvTreatmentIndicators);
				int pesentEffOrder = effluentService.getTreatmentLevelOrder(presentEffluentLevel, presentAdvTreatmentIndicators);

				if (!(projectedEffOrder>pesentEffOrder)){
					errors.add("error.ccv.effluent.fut_plant_type_M_fut_eff_lte_present");
					isError=true;
				}
			}
		}
		return isError;
	}
	
	private EffluentService effluentService;
	public void setEffluentService(EffluentService effluentService) {
		this.effluentService = effluentService;
	}
	
	private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService){
		this.facilityService = facilityService;
	}
}
