package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveForm;

import java.util.ArrayList;
import java.util.Collection;

public interface ManageCostCurveService {
	public void saveOrUpdateCostCurve(ManageCostCurveForm manageCostCurveForm, CurrentUser user);
	public Collection getCostCurveList(long facilityId, long documentId);
	public Collection getAssignedCostCurveList(ArrayList ccList);
	public void deleteCostCurve(long facilityId, long costCurveId, CurrentUser user);
	public String atLeastOneEnabled(Collection cclist);
	public Collection getCatVCostForFacilitiesInSewershed(long facilityId);
	//public void runCostCurve(Long facilityId, String userId) throws CostCurveException;
}
