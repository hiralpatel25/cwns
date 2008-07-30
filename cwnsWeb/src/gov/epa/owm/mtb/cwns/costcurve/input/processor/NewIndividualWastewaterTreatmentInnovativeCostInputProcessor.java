package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.OnsiteWastewaterTreatmentSystemInput;

import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;

public class NewIndividualWastewaterTreatmentInnovativeCostInputProcessor extends OnsiteWastewaterTreatmentCostInputProcessor { 
	
	public CostCurveInput getCostInputData(Long facilityId){
		OnsiteWastewaterTreatmentSystemInput ci = new OnsiteWastewaterTreatmentSystemInput();
		
		GeographicAreaCounty gac= facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);		
		long countyId = gac.getCountyRef().getCountyId();	
		
		ci.setCountyId(countyId);		
		ci.setResidentialNumberOfHouses(populationService.getResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(facilityId));
		ci.setNonResidentialNumberOfHouses(populationService.getNonResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(facilityId));
		
		ci.setResidentialPopulationPerHousehold(populationService.getResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemInnovative(facilityId));		
		ci.setNonResidentialPopulationPerHousehold(populationService.getNonResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemInnovative(facilityId));		
		
		
		return ci;
	}				
	
}
