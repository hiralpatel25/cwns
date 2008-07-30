package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityDischarge;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChange;
import gov.epa.owm.mtb.cwns.model.FacilityTypeRef;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.EffluentService;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityCommentsService;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DischargeValidator extends FESValidator {
	public DischargeValidator() {
		super(FacilityService.DATA_AREA_DISCHARGE);		
	}	
	public boolean isRequired(Long facilityId) {
		Collection facilityTypes =facilityTypeService.getFacityType(facilityId);
		return isValidType(facilityTypes, null);
	}
	
	public boolean isEntered(Long facilityId) {
		Collection c = dischargeService.getFacilityDischarges(facilityId.toString());
		if(c!=null && c.size()>0){
			return true;
		}
		return false;
	}	

	public boolean isErrors(Long facilityId, Collection errors) {
		
		if(!isRequired(facilityId))return false;
		
		if(isRequired(facilityId)&&!isEntered(facilityId)){
			errors.add("error.discharge.required");
			return true;
		}
		
		//if required and not entered
		boolean isError=false;
		Collection facilityTypes =facilityTypeService.getFacityType(facilityId);
		Collection discharges = dischargeService.getFacilityDischarges(facilityId.toString());
		
		
		//present facility of type treatment plant, combined sewer, or seperate sewer exists and  not discharge is specifed
		if(isValidType(facilityTypes, "P")){
			if(!dischargeService.isPresentDischargeSpecified(facilityId)){
				errors.add("error.discharge.present.required");
				isError=true;
			}
		}
		
		//projected facility of type treatment plant, combined sewer, or seperate sewer exists and  not discharge is specifed
		if(isValidType(facilityTypes, "F")){
			if(!dischargeService.isProjectedDischargeSpecified(facilityId)){
				errors.add("error.discharge.projected.required");
				isError=true;
			}
		}
				
		//if no change and no discharge 
		if(isAllFacilityTypesNoChange(facilityTypes)&& !isDischargePresentAndProjected(discharges)){
			errors.add("error.discharge.noChange.discharge.required");
			isError=true;
		}
		
		//if present treatment plant present discharge which is not CSO discharge must exists 
		//check treatmentPlant Discharge
		if(isFacilityTypeValid(facilityId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT, "P")){
			if(!isDischargeOtherThanCSO(discharges, "P")){
				errors.add("error.discharge.treatment.notCSO.present");
				isError= true;
			}
		}else{
			//
			if(isFacilityTypeValid(facilityId, FacilityTypeService.FACILITY_TYPE_COMBINED_SEWER, "P")){
				if(!isDischargeCSOOrAnotherFacility(discharges, "P")){
					errors.add("error.discharge.cso.notCSOORAnother.present");
					isError= true;					
				}
			}
			if(isFacilityTypeValid(facilityId, FacilityTypeService.FACILITY_TYPE_SEPERATE_SEWER, "P")){
				if(!isDischargeOtherThanCSOORGroundWater(discharges, "P")){
					errors.add("error.discharge.ssewer.notCSOAndGW.present");
					isError= true;					
				}
			}	
		}		
		
		//	projected treatment plant present discharge which is not CSO discharge must exists 
		//check treatmentPlant Discharge
		if(isFacilityTypeValid(facilityId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT, "F")){
			if(!isDischargeOtherThanCSO(discharges, "F")){
				errors.add("error.discharge.treatment.notCSO.projected");
				isError= true;
			}
		}else{
			//
			if(isFacilityTypeValid(facilityId, FacilityTypeService.FACILITY_TYPE_COMBINED_SEWER, "F")){
				if(!isDischargeCSOOrAnotherFacility(discharges, "F")){
					errors.add("error.discharge.cso.notCSOORAnother.projected");
					isError= true;					
				}
			}
			if(isFacilityTypeValid(facilityId, FacilityTypeService.FACILITY_TYPE_SEPERATE_SEWER, "F")){
				if(!isDischargeOtherThanCSOORGroundWater(discharges, "F")){
					errors.add("error.discharge.ssewer.notCSOAndGW.projected");
					isError= true;					
				}
			}	
		}
		
		//if a present facility type is valid for effluent and the discharge is present and is 
		//(overland flow or no dicharge or evaporation) present effluent mush be secondary or higher
		if(facilityTypeService.isFacilityTypeStatusPresentAndApplicableToDataArea(facilityId, FacilityService.DATA_AREA_EFFLUENT)){
			long presentEffluentTreatmentLevel = effluentService.getPresentFacilityEffluentLevel(facilityId);
			if(presentEffluentTreatmentLevel !=0 && presentEffluentTreatmentLevel < EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID && isOverLandFlowNoDischargeOrEvaporation(discharges, "P")){
				errors.add("error.discharge.effluent.secondary.present");
				isError= true;
			}			
		}

		
		//if a projected facility type is valid for effluent and the discharge is present and is 
		//(overland flow or no dicharge or evaporation) present effluent mush be secondary or higher
		if(facilityTypeService.isFacilityTypeStatusProjectedAndApplicableToDataArea(facilityId, FacilityService.DATA_AREA_EFFLUENT)){
			long projectedEffluentTreatmentLevel = effluentService.getProjectedFacilityEffluentLevel(facilityId);
			if(projectedEffluentTreatmentLevel !=0 && projectedEffluentTreatmentLevel < EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID && isOverLandFlowNoDischargeOrEvaporation(discharges, "F")){
				errors.add("error.discharge.effluent.secondary.projected");
				isError= true;
			}			
		}	

		//if a discharge of No Discharge, unknow is assigned then error
		if(isDischargeMethodNoDischargeUnknowSpecified(discharges)){
			errors.add("error.discharge.noDischargepUnknown");
			isError= true;
		}
		
		List comments = facilityCommentsService.findFacilityCommentsByFacilityIdAndDataAreaId(facilityId.longValue(), dataAreaId);
		//Discharge other and no comments
		if(isDischargeMethodOtherSpecified(discharges)&& comments.size()==0 ){			
			errors.add("error.discharge.other.comments");
			isError= true;
		}
		
		//if present flow does not add up to 100%
		int presentFlowPercent = getPresentFlowPercent(discharges);
		int projectedFlowPercent = getProjectedFlowPercent(discharges);
		if(presentFlowPercent!=0 && presentFlowPercent !=100){
			errors.add("error.discharge.present.flowPercent.0Or100");
			isError= true;
		}
		
		if(projectedFlowPercent!=0 && projectedFlowPercent !=100){
			errors.add("error.discharge.projected.flowPercent.0Or100");
			isError= true;
		}
		
		//
		FacilityDischarge presentFacilityDischargeToAnother= getPresentDischargeMethodToAnotherFacility(discharges);
		FacilityDischarge projectedFacilityDischargeToAnother= getProjectedDischargeMethodToAnotherFacility(discharges);
		
		if(presentFacilityDischargeToAnother !=null){
			if(presentFlowPercent !=100){
				errors.add("error.discharge.another.pres.flowPercent.not100");
				isError= true;
			}
		}
		
		if(projectedFacilityDischargeToAnother !=null){
			if(projectedFlowPercent !=100){
				errors.add("error.discharge.another.proj.flowPercent.not100");
				isError= true;
			}
		}
		
		if(isDischargeMethodToAnotherFacilityWithNoFacility(discharges)){
			errors.add("error.discharge.another.with.noLoction");
			isError= true;
		}else{
			if(presentFacilityDischargeToAnother !=null){
				Facility f = presentFacilityDischargeToAnother.getFacilityByFacilityIdDischargeTo();
				if(!isValidType(f.getFacilityTypes(), "P")){
					errors.add("error.discharge.another.facility.invalid.present");
					isError= true;
				}
			}
			
			if(projectedFacilityDischargeToAnother !=null){
				Facility f = projectedFacilityDischargeToAnother.getFacilityByFacilityIdDischargeTo();
				if(!isValidType(f.getFacilityTypes(), "F")){
					errors.add("error.discharge.another.facility.invalid.projected");
					isError= true;	
				}
			}			
		}
		
		if(isDuplicateLocations(discharges)){
			errors.add("error.discharge.another.facility.dupLocations");
			isError= true;
		}
		//If a present Discharge method of Ocean Discharge, Outfall to Surface
		//Waters, or Overland Flow with Discharge exist and a present Facility Type of
		//Treatment Plant is not associated with the facility.
		if (!dischargeService.isFacilityHasPresentFacilityTypeTreatmentPlant(facilityId) && dischargeService.isPresentDischargeToSurfaceWaters(facilityId)){
			errors.add("warn.discharge.presentTreatmentPlant");
			isError=true;
		}
		//If a projected Discharge method of Ocean Discharge, Outfall to Surface
		//Waters, or Overland Flow with Discharge exist and a projected Facility Type of
		//Treatment Plant is not associated with the facility.
		if (!dischargeService.isFacilityHasProjectedFacilityTypeTreatmentPlant(facilityId) && dischargeService.isProjectedDischargeToSurfaceWaters(facilityId)){
			errors.add("warn.discharge.projectedTreatmentPlant");
			isError=true;
		}		
		if(isError){
			return true;
		}		
		return false;
	}
	
	private boolean isDischargePresentAndProjected(Collection discharges) {
        if(discharges!=null && discharges.size()>0){
        	//proceed with the logic below
        }else{
        	return false;
        }
		for (Iterator iter = discharges.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			if(fd.getProjectedFlag()!='Y' ||fd.getPresentFlag()!='Y'){
			  return false;
			}
		}
		return true;
	}
	private boolean isDuplicateLocations(Collection discharges){
		//check if all the present and future discharge to another location unique
		Map location = new HashMap();
		for (Iterator iter = discharges.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			if(fd.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_ANOTHER_FAC){
				if(fd.getPresentFlag()=='Y'){
					Facility  disFac = fd.getFacilityByFacilityIdDischargeTo();
					if(disFac!=null){
						long disFacId = disFac.getFacilityId();
						String keyP ="P_" + disFacId; 
						Object op= location.get(keyP);
						if(op!=null){
							return true;
						}else{
							location.put(keyP, "1");
						}
						
					}
					
				}
				//future
				if(fd.getProjectedFlag()=='Y'){
					Facility  disFac = fd.getFacilityByFacilityIdDischargeTo();
					if(disFac!=null){
						long disFacId = disFac.getFacilityId();
						String keyF ="F_" + disFacId; 
						Object op= location.get(keyF);
						if(op!=null){
							return true;
						}else{
							location.put(keyF, "1");
						}
					}	
				}
			}	
		}
		return false;
	}
	
	private int getPresentFlowPercent(Collection discharges){
		int flowPercent =0;
		for (Iterator iter = discharges.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			int cflow = (fd.getPresentFlowPortionPersent()!=null?fd.getPresentFlowPortionPersent().intValue():0);
			flowPercent = flowPercent+ cflow;	
		}
		return flowPercent;		
	}
	
	private int getProjectedFlowPercent(Collection discharges){
		int flowPercent =0;
		for (Iterator iter = discharges.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			int cflow = (fd.getProjectedFlowPortionPersent()!=null?fd.getProjectedFlowPortionPersent().intValue():0);
			flowPercent = flowPercent+ cflow;
		}
		return flowPercent;		
	}
	
	private boolean isFacilityTypeValid(Long facilityId, Long facilityType, String presentProjectedFlag){
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
	
	private boolean isOverLandFlowNoDischargeOrEvaporation(Collection ds, String presentProjectedFlag){
		if(ds==null)return false;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {						
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if("P".equals(presentProjectedFlag)){
				if(fs.getPresentFlag()=='Y' && 
						(fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_OVERLAND_FLOW_NO_DISCHARGE ||
						fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_EVAPORATION)){
					return true;
				}
			}else{
				if(fs.getProjectedFlag()=='Y' && 
						(fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_OVERLAND_FLOW_NO_DISCHARGE ||
						fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_EVAPORATION)){
					return true;
				}
			}	
		}
		return false;		
	}
	
	private boolean isDischargeOtherThanCSO(Collection ds, String presentProjectedFlag){
		//Collection ds = dischargeService.getFacilityDischarges(facilityId.toString());
		if(ds==null)return false;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {						
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if("P".equals(presentProjectedFlag)){
				if(fs.getPresentFlag()=='Y' && fs.getDischargeMethodRef().getDischargeMethodId()!=DischargeService.DISCHARGE_CSO){
					return true;
				}
			}else{
				if(fs.getProjectedFlag()=='Y' && fs.getDischargeMethodRef().getDischargeMethodId()!=DischargeService.DISCHARGE_CSO){
					return true;
				}
			}	
		}
		return false;
	}
	
	private boolean isDischargeCSOOrAnotherFacility(Collection ds, String presentProjectedFlag){
		//Collection ds = dischargeService.getFacilityDischarges(facilityId.toString());
		if(ds==null)return false;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {						
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if("P".equals(presentProjectedFlag)){
				if(fs.getPresentFlag()=='Y' && (fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_CSO ||
						fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_ANOTHER_FAC)){
					return true;
				}
			}else{
				if(fs.getProjectedFlag()=='Y' && (fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_CSO ||
						fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_ANOTHER_FAC)){
					return true;
				}
			}	
		}
		return false;
	}
	
	
	
	private boolean isDischargeMethodNoDischargeUnknowSpecified(Collection ds){
		if(ds==null)return false;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {						
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if(fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_NO_DISCHARGE_UNKNOWN){
				return true;
			}	
		}
		return false;
	}
	
	private FacilityDischarge getPresentDischargeMethodToAnotherFacility(Collection ds){
		if(ds==null)return null;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {						
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if(fs.getPresentFlag()=='Y' && fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_ANOTHER_FAC){
				return fs;
			}	
		}
		return null;
	}
	
	private FacilityDischarge getProjectedDischargeMethodToAnotherFacility(Collection ds){
		if(ds==null)return null;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {						
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if(fs.getProjectedFlag()=='Y' && fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_ANOTHER_FAC){
				return fs;
			}	
		}
		return null;
	}
	
	private boolean isDischargeMethodToAnotherFacilityWithNoFacility(Collection ds){
		if(ds==null)return false;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if(fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_TO_ANOTHER_FAC && fs.getFacilityByFacilityIdDischargeTo()==null){
				return true;
			}
		}
		return false;
	}
	
	private boolean isDischargeMethodOtherSpecified(Collection ds){
		if(ds==null)return false;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {						
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if(fs.getDischargeMethodRef().getDischargeMethodId()==DischargeService.DISCHARGE_OTHER){
				return true;
			}	
		}
		return false;
	}
	
	private boolean isDischargeOtherThanCSOORGroundWater(Collection ds, String presentProjectedFlag){
		//Collection ds = dischargeService.getFacilityDischarges(facilityId.toString());
		if(ds==null)return false;
		for (Iterator iter = ds.iterator(); iter.hasNext();) {						
			FacilityDischarge fs = (FacilityDischarge) iter.next();
			if("P".equals(presentProjectedFlag)){
				if(fs.getPresentFlag()=='Y' && (fs.getDischargeMethodRef().getDischargeMethodId()!=DischargeService.DISCHARGE_CSO &&
												fs.getDischargeMethodRef().getDischargeMethodId()!=DischargeService.DISCHARGE_GROUNDWATER)){
					return true;
				}
			}else{
				if(fs.getProjectedFlag()=='Y' && (fs.getDischargeMethodRef().getDischargeMethodId()!=DischargeService.DISCHARGE_CSO &&
												 fs.getDischargeMethodRef().getDischargeMethodId()!=DischargeService.DISCHARGE_GROUNDWATER)){
					return true;
				}
			}	
		}
		return false;
	}	
	
	
	private boolean isAllFacilityTypesNoChange(Collection types){
		if(types==null)return false;
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			FacilityType type = (FacilityType) iter.next();
			Collection changes= type.getFacilityTypeChanges();
			for (Iterator iterator = changes.iterator(); iterator.hasNext();) {
				FacilityTypeChange ftc = (FacilityTypeChange) iterator.next();
				if(ftc.getChangeTypeRef().getChangeTypeId()!= FacilityTypeService.CHANGE_TYPE_NO_CHANGE.longValue()){
					return false;
				}
			}
		}
		return true;
	}

	
	private boolean isValidType(Collection types, String presentProjected){		
		//Collection types =facilityTypeService.getFacityType(facilityId);
		if(types==null)return false;
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			FacilityType type = (FacilityType) iter.next();
			FacilityTypeRef ftr = type.getFacilityTypeRef();
			if (ftr.getFacilityTypeId() == FacilityTypeService.FACILITY_TYPE_SEPERATE_SEWER.longValue()||
					ftr.getFacilityTypeId() == FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT.longValue() ||
					ftr.getFacilityTypeId() == FacilityTypeService.FACILITY_TYPE_COMBINED_SEWER.longValue()) {
				if(presentProjected==null)return true;
				if("P".equals(presentProjected)){
					if(type.getPresentFlag()=='Y'){
						return true;
					}
				}else if("F".equals(presentProjected)){
					if(type.getProjectedFlag()=='Y'){
						return true;
					}
				}
			}
		}
		return false;
	}

	public void setFesService(FESService fes) {
		fesService = fes;
	}
	
	public FacilityPermitService facilityPermitService;
	public void setFacilityPermitService(FacilityPermitService facilityPermitService) {
		this.facilityPermitService = facilityPermitService;
	}
	
	protected FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService fts){
		facilityTypeService = fts;
	}
	
	protected DischargeService dischargeService;
	public void setDischargeService(DischargeService dischargeService) {
		this.dischargeService = dischargeService;
	}
	
	protected FacilityService facilityService;
	public void setFacilityService(FacilityService fs){
		facilityService = fs;
	}	
	
	protected EffluentService effluentService;
	public void setEffluentService(EffluentService es){
		effluentService = es;
	}
	
	protected FacilityCommentsService facilityCommentsService;
	public void setFacilityCommentsService(
			FacilityCommentsService facilityCommentsService) {
		this.facilityCommentsService = facilityCommentsService;
	}
	
}
