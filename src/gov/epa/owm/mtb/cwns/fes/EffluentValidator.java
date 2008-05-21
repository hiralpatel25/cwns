package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.model.AdvancedTreatmentTypeRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityAdvancedTreatment;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.EffluentService;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

import java.util.Collection;
import java.util.Iterator;

public class EffluentValidator extends FESValidator {

	public EffluentValidator() {
		super(FacilityService.DATA_AREA_EFFLUENT);
	}

	public boolean isRequired(Long facilityId) {
		if((isFacilityTypePresentOrProjected(facilityId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT, "P") && dischargeService.isPresentDischargeToSurfaceWaters(facilityId))||
				(isFacilityTypePresentOrProjected(facilityId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT, "F") && dischargeService.isProjectedDischargeToSurfaceWaters(facilityId))
		  ){
		  return true;	
			
		}
		return false;
	}

	public boolean isEntered(Long facilityId) {
		long pEL =effluentService.getPresentFacilityEffluentLevel(facilityId);
		long fEL = effluentService.getProjectedFacilityEffluentLevel(facilityId);
		if(pEL !=0 || fEL!=0){
			return true; 
		}
		return false;
	}

	public boolean isErrors(Long facilityId, Collection errors) {
		boolean isError = false;
		if(isRequired(facilityId)&&!isEntered(facilityId)){
			errors.add("error.effluent.required");
			return true;
		}
		long presentEffluentLevel=effluentService.getPresentFacilityEffluentLevel(facilityId);
		long projectedEffluentLevel=effluentService.getProjectedFacilityEffluentLevel(facilityId);
		Collection cP = effluentService.getPresentAdvanceTreatmentTypes(facilityId);
		Collection cF = effluentService.getProjectedAdvanceTreatmentTypes(facilityId);
		
		
		//Present Effluent treatment level should not be less than secondary for a discharge of Overland Flow with no discharge or Evaporation
		if(facilityTypeService.isFacilityTypeStatusPresentAndApplicableToDataArea(facilityId, dataAreaId) &&
				dischargeService.isOverLandFlowNoDischargeOrEvaporation(facilityId, "P")  &&
				(presentEffluentLevel < EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID)){
			errors.add("error.effluent.presentEvapo.LT.Secondary");
			isError= true;			
		}
		//Projected Effluent treatment level should not be less than secondary for a discharge of Overland Flow with no discharge or Evaporation
		if(facilityTypeService.isFacilityTypeStatusProjectedAndApplicableToDataArea(facilityId, dataAreaId) &&
				dischargeService.isOverLandFlowNoDischargeOrEvaporation(facilityId, "F")  &&
				(projectedEffluentLevel < EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID)){
			errors.add("error.effluent.projectedEvapo.LT.Secondary");
			isError= true;			
		}
		//facility with increase level of treatment
		if(facilityTypeService.isFacilityTypeValidForDataAreaAndHasChangeType(facilityId, dataAreaId, FacilityTypeService.CHANGE_TYPE_INCREASE_LEVEL_OF_TREATMENT)){
			if(presentEffluentLevel>projectedEffluentLevel){
				errors.add("error.effluent.inCap.PEffLvl.GT.FEffLvl");
				isError=true;
			}else if(presentEffluentLevel==projectedEffluentLevel && presentEffluentLevel != EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
				errors.add("error.effluent.inCap.notAdvance");
				isError=true;
			}else if(presentEffluentLevel==projectedEffluentLevel && presentEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
				//make sure atleast one indicator is selected
				if(cP.size() > cF.size()){
					errors.add("error.effluent.Advance.inCap.moreIndicators");
					isError=true;
				}
			}					
		}
		//facility with no increased level of treatment
		if(!facilityTypeService.isFacilityTypeValidForDataAreaAndHasChangeType(facilityId, dataAreaId, FacilityTypeService.CHANGE_TYPE_INCREASE_LEVEL_OF_TREATMENT) &&
				presentEffluentLevel>0 && projectedEffluentLevel>0){
			if(presentEffluentLevel!=projectedEffluentLevel){
				errors.add("error.effluent.noinCap.PEffLvl.EQ.FEffLvl");
				isError=true;
			}else if(presentEffluentLevel==projectedEffluentLevel && presentEffluentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
				//make sure atleast one indicator is selected
				if(cP.size() != cF.size()){
					errors.add("error.effluent.Advance.noinCap.equalIndicators");
					isError=true;
				}else{
					//check if the same indicators are selected
					boolean match = false;
					for (Iterator iter = cP.iterator(); iter.hasNext();) {
						FacilityAdvancedTreatment pAttr = (FacilityAdvancedTreatment) iter.next();
						for (Iterator iterator = cF.iterator(); iterator.hasNext();) {
							FacilityAdvancedTreatment fAttr = (FacilityAdvancedTreatment) iterator.next();
							if(fAttr.getId().getAdvancedTreatmentTypeId()==pAttr.getId().getAdvancedTreatmentTypeId()){
								match=true;
							}						
						}
						if(!match){
							errors.add("error.effluent.Advance.noinCap.equalIndicators");
							isError= true;
							break;
						}
						match=false;
					}					
				}				
			}	
			//if present or projected is advanced the treatment indicators should be selected
			if(presentEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
				if(cP.size()==0){
					errors.add("error.effluent.present.noAdvanceIndicators");
					isError= true;
				}
			}
			
			//if present or projected is advanced the treatment indicators should be selected
			if(projectedEffluentLevel==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
				if(cF.size()==0){
					errors.add("error.effluent.projected.noAdvanceIndicators");
					isError= true;
				}
			}
			
			//get facility
			FacilityType ft = facilityTypeService.getFacilityType(facilityId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT);
			if(ft!=null && ft.getPresentFlag()=='Y' && ft.getProjectedFlag()=='Y'){
				Facility f = ft.getFacility();
				if(f.getPresentTreatmentPlantType()!=null && f.getPresentTreatmentPlantType().charValue()=='L' && f.getProjectedTreatmentPlantType()!=null && f.getProjectedTreatmentPlantType().charValue()=='M'){
					int pLevel = getTreatmentLevelOrder(presentEffluentLevel, cP);
					int fLevel = getTreatmentLevelOrder(presentEffluentLevel, cF);
					if(pLevel > fLevel){
						errors.add("error.effluent.fMech.fgtpLevel");
						isError= true;
					}
				}

			}
		}
		
		//set FES error-flag = Y for Effluent if Advanced Treatment of Nutrient Removal
		//is assigned to the facility.
		if(isEffluentFlagSet(cP)||isEffluentFlagSet(cF)){
			errors.add("error.effluent.nutRemoval");
			isError= true;
		}

		return isError;
	}
	
	private int getTreatmentLevelOrder(long treatmentLevelId, Collection advanceIndicators){
		if(treatmentLevelId==EffluentService.EFFLUENT_TREATMENT_LEVEL_PRIMARY_ID || treatmentLevelId==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADAVANCE_PRIMARY_ID){
			return 1;
		}else if(treatmentLevelId==EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID){
			return 2;			
		}else if(treatmentLevelId==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
			boolean isBOD = false;
			boolean isNitrogen = false;
			boolean isPhosphorous = false;			
			for (Iterator iter = advanceIndicators.iterator(); iter.hasNext();) {
				FacilityAdvancedTreatment fAttr = (FacilityAdvancedTreatment) iter.next();
				if(EffluentService.EFFLUENT_TREATMENT_INDICATOR_BOD == fAttr.getId().getAdvancedTreatmentTypeId()){
					isBOD=true;
				}else if(EffluentService.EFFLUENT_TREATMENT_INDICATOR_N == fAttr.getId().getAdvancedTreatmentTypeId()){
					isNitrogen=true;
				}else if(EffluentService.EFFLUENT_TREATMENT_INDICATOR_P == fAttr.getId().getAdvancedTreatmentTypeId()){
					isPhosphorous=true;
				}
			}
			if(!isBOD && !isNitrogen && !isPhosphorous){
				return 3;
			}else if(!isBOD && (isNitrogen && isPhosphorous)){
				return 5;
			}else if(!isBOD && (isNitrogen || isPhosphorous)){
				return 4;
			}else if(isBOD && !isNitrogen && !isPhosphorous){
				return 6;
			}else if(isBOD && (isNitrogen && isPhosphorous)){
				return 8;
			}else if(isBOD && (isNitrogen || isPhosphorous)){
				return 7;
			}
		}
		return 0;
	}
	
	private boolean isEffluentFlagSet(Collection  advanceIndicators){
		if( advanceIndicators!=null && ! advanceIndicators.isEmpty()){
			for (Iterator iter =  advanceIndicators.iterator(); iter.hasNext();) {
				FacilityAdvancedTreatment at = (FacilityAdvancedTreatment) iter.next();
				if(at!=null && at.getId().getAdvancedTreatmentTypeId()==10){
					return true;
				}
			}			
		}
		return false;
	}
	
	private boolean isFacilityTypePresentOrProjected(Long facilityId, Long facilityType, String presentProjectedFlag){
		FacilityType ft = facilityTypeService.getFacilityType(facilityId, facilityType);
		if(ft!=null){
			if("P".equals(presentProjectedFlag)){
				if(ft.getPresentFlag()=='Y'){
					return true;
				}
			}else{
				if(ft.getProjectedFlag()=='Y'){
					return true;
				}
			}		
		}
		return false;
	}
	/*
	private boolean isTreatmentPlantPresentLagoonFutureMechanical(Long facilityId, Long facilityType){
		FacilityType ft = facilityTypeService.getFacilityType(facilityId, facilityType);
		if(ft!=null){
			ft.get	
		}		
		return false;
	} */
	

	public void setFesService(FESService fes) {
		fesService = fes;
	}
	
	private EffluentService effluentService;
	public void setEffluentService(EffluentService effluentService) {
		this.effluentService = effluentService;
	}
	
	private FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService facilityTypeService) {
		this.facilityTypeService = facilityTypeService;
	}
	
	private DischargeService dischargeService;
	public void setDischargeService(DischargeService ds) {
		this.dischargeService = ds;
	}
	

}
