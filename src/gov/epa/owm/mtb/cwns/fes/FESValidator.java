package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.service.FESService;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

public abstract class FESValidator {
	
	protected Logger log;
	protected Long dataAreaId;	
	protected FESService fesService;
	
	
	public FESValidator(Long daId) {
		log = Logger.getLogger(this.getClass());
		dataAreaId = daId;
	}

	public void validate(Long facilityId, boolean validateRequired, String userId){
		if(fesService.isDataAreaApplicable(facilityId, dataAreaId)){
			char requiredFlag =' ';
			//Check on required flag
			if(validateRequired){
				requiredFlag = (isRequired(facilityId)==true)?'Y':'N';
			}
			
			//check on entered flag
			char enteredFlag = (isEntered(facilityId)==true)?'Y':'N';
			
			//check on error flag
			ArrayList errors = new ArrayList();
			char errorFlag = (isErrors(facilityId, errors)==true)?'Y':'N';
			log.debug("Updating FES for facility:" + facilityId + " dataarea " + dataAreaId + " Falgs R-E-ERR " + validateRequired+requiredFlag+enteredFlag);
			fesService.updateFes(validateRequired, requiredFlag, enteredFlag, errorFlag, errors, facilityId, dataAreaId, userId);			
		}
	}
	
	public abstract boolean isRequired(Long facilityId);
	
	public abstract boolean isEntered(Long facilityId);

	public abstract boolean isErrors(Long facilityId, Collection errors);
	
	public abstract void setFesService(FESService fes);
}
