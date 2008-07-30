package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityDischarge;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class DischargeCCValidator extends CostCurveValidator{
	public DischargeCCValidator(){
		super(FacilityService.DATA_AREA_DISCHARGE);
	}
	
	public boolean isCostCurveApplicable(String costCurveCode) {
		boolean isApplicable = false;
		if(CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){
			isApplicable=true;
		}		
		return isApplicable;
	}

	public boolean isErrors(Long facilityId, String costCurveCode, Set errors) {
		boolean isError=false;
		
		if(CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){
			//case 1: All facilities in the present Sewershed must be in state.
			Collection presentSewerShedFacilities = 
				populationService.getRelatedSewerShedFacilitiesByDischargeType(facilityId, PopulationService.PRESENT_ONLY);
			if (presentSewerShedFacilities!=null && presentSewerShedFacilities.size()>0){
				Iterator preSSFacilitiesIter = presentSewerShedFacilities.iterator();
				while(preSSFacilitiesIter.hasNext()){
					String strFacilityId = ((Long)preSSFacilitiesIter.next()).toString();
					if(populationService.isOutOfState(strFacilityId, facilityId.toString())){
						errors.add("error.ccv.discharge.present_sewer_shed_facilities_not_in_same_state");
						isError=true;
						break;
					}
				}					
			}
						
			//case 2: For each non-terminating facility in present Sewershed: Upstream facilities' discharge flow % must add up to 100
			//(This is already an FES error for the actual facility, but not for the downstream facility) 
		    //This would ensure the downstream facility cannot run the CC and can only be sent to Review if the Cost Curve is not used
			if (presentSewerShedFacilities!=null && presentSewerShedFacilities.size()>0){
				Iterator preSSFacilitiesIter = presentSewerShedFacilities.iterator();
				while(preSSFacilitiesIter.hasNext()){
					Long facId = (Long)preSSFacilitiesIter.next(); 
					String strFacilityId = facId.toString();
					//if(!dischargeService.isTerminatingFacility(facilityId)){
					if(!dischargeService.isTerminatingFacility(facId)){
						if (!dischargeFlowPercentAddsTo100(strFacilityId)){
							errors.add("error.ccv.discharge.non_terminating_ss_facility_discharge_flow_ne_100");
							isError=true;
							break;
						}
					}
				}
			}
		}
		return isError;
	}
	
	private boolean dischargeFlowPercentAddsTo100(String facilityId){
		boolean addsTo100 = true;
		Facility facility = facilityService.findByFacilityId(facilityId);		
		Collection facDischarges = facility.getFacilityDischargesForFacilityId();
		double flowPercent = 0;
		
		if (facDischarges!=null && facDischarges.size()>0){
			Iterator facDischargesIter = facDischarges.iterator();
			while(facDischargesIter.hasNext()){
				FacilityDischarge fc = (FacilityDischarge)facDischargesIter.next();
				//if(fc.getDischargeMethodRef().getDischargeMethodId()== DischargeService.DISCHARGE_TO_ANOTHER_FAC){
				    if(fc.getPresentFlowPortionPersent()!=null){
				    	flowPercent += fc.getPresentFlowPortionPersent().doubleValue();	
				    }
				//}
			}
		}
		
		if (flowPercent!=100) addsTo100=false;
		return addsTo100;
	}
	
	private DischargeService dischargeService;
	public void setDischargeService(DischargeService dischargeService) {
		this.dischargeService = dischargeService;
	}
	
	private PopulationService populationService;
	public void setPopulationService(PopulationService populationService){
		this.populationService = populationService;
	}
	
	public FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService){
		this.facilityService = facilityService;
	}
}