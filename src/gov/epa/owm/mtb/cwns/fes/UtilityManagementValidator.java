package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.UtilityManagementService;

import java.util.Collection;

public class UtilityManagementValidator extends FESValidator {

	public UtilityManagementValidator() {
		super(FacilityService.DATA_AREA_UTIL_MANAGEMENT);
	}

	public boolean isEntered(Long facilityId) {
		Collection c = utilityManagementService.getUtilityManagementByFacility(facilityId);
		if(c!=null && c.size()>0){
			return true;
		}
		return false;		
	}

	public boolean isRequired(Long facilityId) {
		return false;
	}


	public boolean isErrors(Long facilityId, Collection errors) {
		return false;
	}
	
	private UtilityManagementService utilityManagementService; 
	public void setUtilityManagementService(
			UtilityManagementService utilityManagementService) {
		this.utilityManagementService = utilityManagementService;
	}

	public void setFesService(FESService fes) {
		fesService = fes;
	}

}
