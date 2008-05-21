package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.SeparateSewerSystemInput;

import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;

public class NewSeparateSewerCostInputProcessor extends SeparateSewerCostInputProcessor { 
	
	public CostCurveInput getCostInputData(Long facilityId){
		SeparateSewerSystemInput ci = new SeparateSewerSystemInput();
		
		GeographicAreaCounty gac= facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);		
		long countyId = gac.getCountyRef().getCountyId();	
		
		ci.setCountyId(countyId);		
		ci.setPresentPopulation(populationService.getPresentNewSeparatSewerSystemPopulation(facilityId));
		
		return ci;
	}				
	
}
