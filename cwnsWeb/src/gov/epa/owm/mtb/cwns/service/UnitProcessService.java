package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.unitProcess.UnitProcessForm;

import java.util.Collection;

public interface UnitProcessService {

	public Collection getUnitProcessList(long facilityId);
	
	public Collection getGlobalUnitProcessChangeTypes();
	public Collection getFacilityUnitProcessChangeTypes(long facId, long treatmentTypeId, long unitProcessListId);
	public Collection getAvailUnitProcessChangeTypes(Collection unitProcessChangeTypeList, Collection selectedUnitProcessChangeTypes, int presentProjectedFlag);
	
	public void updateUnitProcessChangeTypeRecords(String facilityId, String commaDelimitedString, long treatmentTypeId, long unitProcessListId, CurrentUser user);
		
	public Collection getUnitProcessDropdownList(long facilityId, long treatmentTypeId);
	
	public void saveOrUpdateUnitProcess(UnitProcessForm form, CurrentUser user);	
	public void deleteFacilityUnitProcess(UnitProcessForm form, CurrentUser user);	
	public void prepareFacilityUnitProcess(UnitProcessForm form);
	
	public String warnStateUSerAboutLocal(long facilityId, CurrentUser user);
	public void moveUnitProcess(long facilityId, long treatmentTypeId, long unitProcessRadioId, int index, CurrentUser user);
	public Collection getUnitProcessListByFacility(Long facilityId);
}
	