package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.UnitProcessService;

import java.util.Collection;

public class UnitProcessValidator extends FESValidator {

	public UnitProcessValidator() {
		super(FacilityService.DATA_AREA_UNIT_PROCESS);
	}
	
	public boolean isRequired(Long facilityId) {
		if(facilityService.isFacilityLargeCommunity(facilityId)){
			return true;
		}
		return false;
	}

	public boolean isEntered(Long facilityId) {
		Collection c = unitProcessService.getUnitProcessListByFacility(facilityId);
		if(c!=null && c.size()>0){
			return true;
		}
		return false;
	}

	public boolean isErrors(Long facilityId, Collection errors) {
		if(isRequired(facilityId) && !isEntered(facilityId)){
			errors.add("error.unitProcess.required");
			return true;
		}
		return false;
	}


	private UnitProcessService unitProcessService;
	public void setUnitProcessService(UnitProcessService unitProcessService) {
		this.unitProcessService = unitProcessService;
	}	
	
	public void setFesService(FESService fes) {
		fesService = fes;
	}
	
	private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
	

}
