package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.model.DataAreaFesRuleRef;
import gov.epa.owm.mtb.cwns.service.FESService;


import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class FESManager {
	
	private Logger log =null;
	public FESManager(){
		log = Logger.getLogger(this.getClass());
	}	
	
	public void runValidation(Long facilityId, Long currentDataAreaId, String userId){
		//figure out which data areas apply based on the current DataArea
		Collection dataAreas = fesService.getDataAreaFESRule(currentDataAreaId);
		for (Iterator iter = dataAreas.iterator(); iter.hasNext();) {
			DataAreaFesRuleRef dafrr = (DataAreaFesRuleRef) iter.next();
			FESValidator fesv = FESValidatorFactory.createValidator(dafrr.getId().getDataAreaIdToPerform());
			if(fesv!=null){
				boolean validateRequired = (dafrr.getPerformRequiredCheckFlag()=='Y'? true : false);
				fesv.validate(facilityId, validateRequired, userId);
			}else{
				log.debug("Unable to find validator for dataArea: " + dafrr.getDataAreaRefByDataAreaIdToPerform().getName());
			}
		}
	}
	
	private FESService fesService;
	public void setFesService(FESService fes){
		fesService = fes;
	}

}
