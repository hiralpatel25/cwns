package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.OnsiteWastewaterTreatmentSystemInput;

import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;

public class NewIndividualWastewaterTreatmentAllCostInputProcessor extends OnsiteWastewaterTreatmentCostInputProcessor { 
	
	public CostCurveInput getCostInputData(Long facilityId){
		OnsiteWastewaterTreatmentSystemInput ci = new OnsiteWastewaterTreatmentSystemInput();
		
		GeographicAreaCounty gac= facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);		
		long countyId = gac.getCountyRef().getCountyId();	
		
		ci.setCountyId(countyId);		
		ci.setResidentialNumberOfHouses(populationService.getResidentialNewIndividualWastewaterTreatmentSystemAllHouses(facilityId));		
		ci.setNonResidentialNumberOfHouses(populationService.getNonResidentialNewIndividualWastewaterTreatmentSystemAllHouses(facilityId));
		
		ci.setResidentialPopulationPerHousehold(populationService.getResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemAll(facilityId));		
		ci.setNonResidentialPopulationPerHousehold(populationService.getNonResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemAll(facilityId));		
		
		return ci;
	}				
	
}
