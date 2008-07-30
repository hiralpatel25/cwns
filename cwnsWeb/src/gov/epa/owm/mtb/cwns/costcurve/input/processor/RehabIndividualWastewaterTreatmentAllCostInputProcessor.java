package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.OnsiteWastewaterTreatmentSystemInput;

import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;

public class RehabIndividualWastewaterTreatmentAllCostInputProcessor extends OnsiteWastewaterTreatmentCostInputProcessor { 
	
	public CostCurveInput getCostInputData(Long facilityId){
		OnsiteWastewaterTreatmentSystemInput ci = new OnsiteWastewaterTreatmentSystemInput();
		
		GeographicAreaCounty gac= facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);		
		long countyId = gac.getCountyRef().getCountyId();	
		
		ci.setCountyId(countyId);		
		ci.setResidentialNumberOfHouses(populationService.getResidentialRehabIndividualWastewaterTreatmentSystemAllHouses(facilityId));
		ci.setNonResidentialNumberOfHouses(populationService.getNonResidentialRehabIndividualWastewaterTreatmentSystemAllHouses(facilityId));
		
		ci.setResidentialPopulationPerHousehold(populationService.getResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemAll(facilityId));		
		ci.setNonResidentialPopulationPerHousehold(populationService.getNonResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemAll(facilityId));				
		
		return ci;
	}				
	
}
