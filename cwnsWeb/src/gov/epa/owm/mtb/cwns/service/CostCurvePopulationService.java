package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.costCurvePopulation.CostCurvePopulationForm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface CostCurvePopulationService {
	public void saveOrUpdateCostCurvePopulation(CostCurvePopulationForm costCurvePopulationForm, CurrentUser user);	
	
	public int getFacilityPopulationPresentPopulation(long facilityId, long populationId);
	public int getFacilityPopulationProjectedPopulation(long facilityId, long populationId);
	public int getFacilityPopulationHousesPresentPopulation(long facilityId, long populationId);
	public int getFacilityPopulationHousesProjectedPopulation(long facilityId, long populationId);
	
	public void setFacilityPopulationPresentPopulation(long facilityId, long populationId, long number);
	public void setFacilityPopulationProjectedPopulation(long facilityId, long populationId, long number);
	public void setFacilityPopulationHousesPresentPopulation(long facilityId, long populationId, long number);
	public void setFacilityPopulationHousesProjectedPopulation(long facilityId, long populationId, long number);

	public List getFacilityCostCurve(long facilityId, long costCurveId);
	public void setFacilityCostCurveRerun(long facilityId, long costCurveId, char yesOrNo);
	
	public void setDisplayObjectsInRequest(HttpServletRequest req, long facilityId, CostCurvePopulationForm costCurvePopulationForm, boolean resetDisplayValues);
	
	public double getPopulationPerHouse(long facilityId, int resOrNonRes);
	public String getPopulationPerHouseDisplayOnly(long facilityId);
	
	public void validateFormValues(CostCurvePopulationForm form, List errorsList, List warningsList);
	
}