package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

public abstract class SeparateSewerCostInputProcessor implements CostCurveInputProcessor { 
	
    protected PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		this.populationService = populationService;
	}	
	
	protected FacilityAddressService facilityAddressService;
	public void setFacilityAddressService(FacilityAddressService fas) {
		facilityAddressService = fas;
	}	
	
}
