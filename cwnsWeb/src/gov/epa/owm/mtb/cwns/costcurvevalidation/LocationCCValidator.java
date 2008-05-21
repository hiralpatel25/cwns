package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.GeographicArea;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class LocationCCValidator extends CostCurveValidator {

	public LocationCCValidator() {
		super(FacilityService.DATA_AREA_GEOGRAPHIC);
	}

	public boolean isCostCurveApplicable(String costCurveCode) {
		boolean isApplicable = false;
		if(CostCurveService.CODE_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_DISINFECTION_ONLY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				
				CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_ALL.equals(costCurveCode) ||
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode) ||
				
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode) ||
				CostCurveService.CODE_NEW_SEPERATE_SEWER_INTERCEPTOR.equals(costCurveCode) ||
				CostCurveService.CODE_NEW_SEPERATE_SEWER_COLLECTOR.equals(costCurveCode) ||
				CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode)||
				
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_ALL.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_SEPERATE_SEWER_REHAB.equals(costCurveCode)){
			isApplicable = true;
		}			
		return isApplicable;
	}

	public boolean isErrors(Long facilityId, String costCurveCode, Set errors) {		
		boolean isError = false;
		if(CostCurveService.CODE_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_DISINFECTION_ONLY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				
				CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_ALL.equals(costCurveCode) ||
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode) ||
				
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode) ||
				CostCurveService.CODE_NEW_SEPERATE_SEWER_INTERCEPTOR.equals(costCurveCode) ||
				CostCurveService.CODE_NEW_SEPERATE_SEWER_COLLECTOR.equals(costCurveCode) ||
				CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode)||
				
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_ALL.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_SEPERATE_SEWER_REHAB.equals(costCurveCode)){
			
				if (!hasPrimaryCounty(facilityId)){
					errors.add("error.ccv.location.primary_county_required");
					isError = true;
				}			
		}
		return isError;
	}

	private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
	private boolean hasPrimaryCounty(Long facilityId){
		boolean hasPrimaryCounty = false;		
		Facility f = facilityService.findByFacilityId(facilityId.toString());
		Collection geographicAreas = f.getGeographicAreas();				
		if (geographicAreas!=null){
			Iterator iterGeographicAreas=geographicAreas.iterator();
			while(iterGeographicAreas.hasNext()){
				Collection gaCounties = ((GeographicArea)iterGeographicAreas.next()).getGeographicAreaCounties();		
				if (gaCounties!=null){
					Iterator iterCounties = gaCounties.iterator();
					while(iterCounties.hasNext()){
						GeographicAreaCounty gaCounty = (GeographicAreaCounty)iterCounties.next();
						if(gaCounty.getPrimaryFlag()=='Y'){
							hasPrimaryCounty = true;
							break;
						}
					}
				}			
			}
		}
		return hasPrimaryCounty;
	}

}
