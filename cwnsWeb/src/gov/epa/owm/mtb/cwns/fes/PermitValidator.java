package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.model.EfPermit;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityPermit;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeRef;
import gov.epa.owm.mtb.cwns.model.Permit;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.impl.ReviewStatusRefServiceImpl;

import java.util.Collection;
import java.util.Iterator;

public class PermitValidator extends FESValidator {
	public PermitValidator() {
		super(FacilityService.DATA_AREA_PERMITS);		
	}	
	
	public boolean isRequired(Long facilityId) {
		if(isMS4(facilityId))return true;
		if(isTreatmentOrOtherWithSurfaceWaterDischarge(facilityId))return true;
		return false;
	}
	
	public boolean isEntered(Long facilityId) {
		Collection p= facilityPermitService.getPermitsByFacilityId(facilityId);
		if(p!=null && p.size()>0){
			return true;
		}
		return false;
	}	

	public boolean isErrors(Long facilityId, Collection errors) {
		//if required and not entered
		if(isRequired(facilityId)&&!isEntered(facilityId)){
			errors.add("error.permit.required");
			return true;
		}		
		if(isTreatmentOrOtherWithSurfaceWaterDischarge(facilityId)&&!isNPDESExists(facilityId)&&!isSkipBatch(facilityId)){
			errors.add("error.permit.NPDES.required");
			return true;
		}
		
		if(isValidNPDESExists(facilityId)&&!isNPDESUseDataExists(facilityId)){
			errors.add("error.permit.validNPDES.notUsed");
			return true;
		}		
		return false;
	}
	
	private boolean isSkipBatch(Long facilityId){
		Facility facility = facilityService.findByFacilityId(facilityId.toString());
		String rsId =facility.getReviewStatusRef().getReviewStatusId(); 
		if(rsId!=null){
			if(ReviewStatusRefService.FEDERAL_ACCEPTED.equals(rsId)|| 
					ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED.equals(rsId)||
					ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION.equals(rsId)||
					ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equals(rsId)){
				Collection facilityPermits = facilityPermitService.getPermitsByFacilityId(facilityId);
				if(facilityPermits.size()>0){
					return true;
				}				
			}		
		}	
		return false;
	}
	
	private boolean isNPDESExists(Long facilityId){
		//get permits
		Collection facilityPermits = facilityPermitService.getPermitsByFacilityId(facilityId);
		if (facilityPermits!=null){
			for (Iterator iter = facilityPermits.iterator(); iter.hasNext();) {
				FacilityPermit facilityPermit = (FacilityPermit) iter.next();
				if(facilityPermit.getPermit().getPermitTypeRef().getNpdesFlag()=='Y' || facilityPermit.getPermit().getPermitTypeRef().getCode().equals("OTH")){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isValidNPDESExists(Long facilityId){
		Collection facilityPermits = facilityPermitService.getPermitsByFacilityId(facilityId);
		for (Iterator iter = facilityPermits.iterator(); iter.hasNext();) {
			FacilityPermit facilityPermit = (FacilityPermit) iter.next();
			//get the permit number
			Permit p = facilityPermit.getPermit();
			if(p!=null && p.getPermitTypeRef().getNpdesFlag()=='Y'){
				EfPermit ep= p.getEfPermit();
				//check if LAt/long are present
				if(ep!=null && ep.getLatitudeDecimalDegree()!=null && ep.getLongitudeDecimalDegree()!=null){
					return true;
				}	
			}	
		}
		return false;
	}
	
	private boolean isNPDESUseDataExists(Long facilityId){
		//get permits
		Collection facilityPermits = facilityPermitService.getPermitsByFacilityId(facilityId);
		if(facilityPermits!=null){
			for (Iterator iter = facilityPermits.iterator(); iter.hasNext();) {
				FacilityPermit facilityPermit = (FacilityPermit) iter.next();
				if(facilityPermit.getUsedForFacilityLocatnFlag()=='Y'){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isMS4(Long facilityId){		
		Collection types =facilityTypeService.getFacityType(facilityId);
		if(types==null)return false;
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			FacilityType type = (FacilityType) iter.next();
			FacilityTypeRef ftr = type.getFacilityTypeRef();
			if (ftr.getFacilityTypeId() == FacilityTypeService.FACILITY_TYPE_MS4_I.longValue() ||
					ftr.getFacilityTypeId() == FacilityTypeService.FACILITY_TYPE_MS4_II.longValue() ||
					ftr.getFacilityTypeId() == FacilityTypeService.FACILITY_TYPE_MS4_NON.longValue()
					) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isTreatmentOrOtherWithSurfaceWaterDischarge(Long facilityId){
		Collection types =facilityTypeService.getFacityType(facilityId);
		if(types==null)return false;
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			FacilityType type = (FacilityType) iter.next();
			FacilityTypeRef ftr = type.getFacilityTypeRef();
			if(ftr.getFacilityTypeId()== FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT.longValue()){
				//if facility type is treatment plant or other and surface discharge is surface water
				if(type.getPresentFlag()=='Y'){
					//check if surface discharge exists
					if(dischargeService.isPresentDischargeToSurfaceWaters(facilityId)){
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
}
