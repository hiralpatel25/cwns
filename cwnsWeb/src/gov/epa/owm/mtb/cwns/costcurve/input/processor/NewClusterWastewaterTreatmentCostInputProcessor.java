package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.OnsiteWastewaterTreatmentSystemInput;

import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;

public class NewClusterWastewaterTreatmentCostInputProcessor extends OnsiteWastewaterTreatmentCostInputProcessor { 
	
	public CostCurveInput getCostInputData(Long facilityId){
		OnsiteWastewaterTreatmentSystemInput ci = new OnsiteWastewaterTreatmentSystemInput();
		
		GeographicAreaCounty gac= facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);		
		long countyId = gac.getCountyRef().getCountyId();	
		
		ci.setCountyId(countyId);		
		ci.setResidentialNumberOfHouses(populationService.getResidentialNewClusterWastewaterTreatmentSystemHouses(facilityId));
		ci.setNonResidentialNumberOfHouses(populationService.getNonResidentialNewClusterWastewaterTreatmentSystemHouses(facilityId));
		
		ci.setResidentialPopulationPerHousehold(populationService.getResidentialPopulationPerHouseholdNewClusterWastewaterTreatmentSystem(facilityId));		
		ci.setNonResidentialPopulationPerHousehold(populationService.getNonResidentialPopulationPerHouseholdNewClustereWastewaterTreatmentSystem(facilityId));				
		
		return ci;
	}				
	
}
