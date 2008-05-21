package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.OnsiteWastewaterTreatmentSystemInput;

import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;

public class RehabClusterWastewaterTreatmenCostInputProcessor extends OnsiteWastewaterTreatmentCostInputProcessor { 
	
	public CostCurveInput getCostInputData(Long facilityId){
		OnsiteWastewaterTreatmentSystemInput ci = new OnsiteWastewaterTreatmentSystemInput();
		
		GeographicAreaCounty gac= facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);		
		long countyId = gac.getCountyRef().getCountyId();	
		
		ci.setCountyId(countyId);		
		ci.setResidentialNumberOfHouses(populationService.getResidentialRehabClusterWastewaterTreatmentSystemHouses(facilityId));
		ci.setNonResidentialNumberOfHouses(populationService.getNonResidentialRehabClusterWastewaterTreatmentSystemHouses(facilityId));
		
		ci.setResidentialPopulationPerHousehold(populationService.getResidentialPopulationPerHouseholdRehabClusterWastewaterTreatmentSystem(facilityId));		
		ci.setNonResidentialPopulationPerHousehold(populationService.getNonResidentialPopulationPerHouseholdRehabClusterWastewaterTreatmentSystem(facilityId));				
		
		return ci;
	}					
}
