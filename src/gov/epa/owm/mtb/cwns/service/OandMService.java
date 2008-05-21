package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.OperationAndMaintenanceCost;
import gov.epa.owm.mtb.cwns.oandm.OandMForm;

import java.util.Collection;

public interface OandMService {
	public static final long OPER_AND_MAINT_CAT_TOTAL = 99;
	
	// Get Facility CSO Info
	public void saveOrUpdateOandM(OandMForm oandMForm, CurrentUser user);
	public Collection getOandMList(long facilityId);
	public void deleteOandM(long facilityId, long oandMCatId, long year, CurrentUser user, boolean runPostSave, boolean isCatTypeSwitch);
	public OperationAndMaintenanceCost prepareOperationAndMaintenance(long facilityId, long oandMCatId, long year);
	public Collection getOandMCategoryList(boolean existing, long facilityId, long year, long catIdAlwaysAvail);
	public Collection generateExcludedList(long facilityId);
	
}