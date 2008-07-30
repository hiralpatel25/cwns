package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PocService;

import java.util.Collection;

public class PointOfContactValidator extends FESValidator {

	public PointOfContactValidator() {
		super(FacilityService.DATA_AREA_POC);
	}
	
	public boolean isRequired(Long facilityId) {
		return true;
	}
	
	public boolean isEntered(Long facilityId) {
		Collection c = pocService.getPocListForFacility(facilityId.toString());
		if (c!=null && c.size()>0){
			return true;
		}
		return false;
	}

	public boolean isErrors(Long facilityId, Collection errors) {		
		if(!isEntered(facilityId)){
			errors.add("error.poc.required");
			return true;
		}
		
		if(pocService.getPirmaryPocIdForFacility(facilityId.toString())==0){
			errors.add("error.poc.noPrimary");
			return true;
		}
		
		return false;
	}
	
    //  set the poc service
    private PocService pocService;
	public void setPocService(PocService ps) {
		pocService = ps;
	}
	
	//Always required
	public void setFesService(FESService fes){
		fesService = fes;
	}
	
	
	
	


}
