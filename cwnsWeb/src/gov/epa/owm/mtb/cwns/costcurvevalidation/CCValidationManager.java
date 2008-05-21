package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.model.DataAreaRef;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataArea;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class CCValidationManager {
	
	private Logger log =null;
	public CCValidationManager(){
		log = Logger.getLogger(this.getClass());
	}	
	
	public void runValidation(Long facilityId, Long currentDataAreaId, String userId){
		//run the cc for the current facility
		runValidation(facilityId, userId);
		
		//check if this facility belongs to sewershed and run the cc validation on the facilities in the sewershed
		Collection sewershedFacilities = populationService.getRelatedSewerShedFacilities(facilityId);
		if(sewershedFacilities!=null && !sewershedFacilities.isEmpty()){
			for (Iterator iter = sewershedFacilities.iterator(); iter.hasNext();) {
				Long ssFacId = (Long) iter.next();
				if(ssFacId.longValue()!=facilityId.longValue()){ //don't run on the same facility twice
					runValidation(ssFacId, userId);
				}
			}
		}
	}
	
	public void runValidation(Long facilityId, String userId){
		//get the list of cost curves associated with the facility
		Collection cc = costCurveService.getFacilityCostCurves(facilityId);
		//don't do any validation if there are no cost curves associated with the facility
		if(cc!=null && cc.size()>0){
			Collection dataAreas = costCurveService.getCostCurveValidationDataAreas(facilityId);
			for (Iterator iter = dataAreas.iterator(); iter.hasNext();) {
				DataAreaRef dataArea = (DataAreaRef) iter.next();
				CostCurveValidator ccv = CostCurveValidatorFactory.createValidator(dataArea.getDataAreaId());
				if(ccv!=null){
					ccv.validate(facilityId, userId, cc);
				}else{
					log.debug("Unable to find validator for dataArea: " + dataArea.getName());
				}
			}			
		}
	}
	
	
	
	private CostCurveService costCurveService;
	public void setCostCurveService(CostCurveService ccs) {
		costCurveService = ccs;
	}
	
	private PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		this.populationService = populationService;
	}
	
}
