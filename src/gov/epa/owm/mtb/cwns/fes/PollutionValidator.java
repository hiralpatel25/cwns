package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ImpairedWatersService;
import gov.epa.owm.mtb.cwns.service.PocService;
import gov.epa.owm.mtb.cwns.service.PollutionService;

import java.util.Collection;

public class PollutionValidator extends FESValidator {

	public PollutionValidator() {
		super(FacilityService.DATA_AREA_POLLUTION);
	}
	
	public boolean isRequired(Long facilityId) {
		return false;
	}	

	public boolean isEntered(Long facilityId) {
		Collection c =impairedWatersService.getFacilityImpairedWaterList(facilityId.longValue());
		Collection pc = pollutionService.getFacilityPollutionProblems(facilityId.toString());
		if((c!=null && c.size()>0)|| (pc!=null && pc.size()>0)){
			return true;
		}
		
		
		return false;
	}

	public boolean isErrors(Long facilityId, Collection errors) {
		return false;
	}	
	  //  set the poc service
    private ImpairedWatersService impairedWatersService;
	public void setImpairedWatersService(ImpairedWatersService iws) {
		impairedWatersService = iws;
	}
	
	//Always required
	public void setFesService(FESService fes){
		fesService = fes;
	}
	
	/**
	 * 
	 */
	public PollutionService pollutionService;
	public void setPollutionService(PollutionService ps) {
		this.pollutionService = ps;
	}
	
	
}
