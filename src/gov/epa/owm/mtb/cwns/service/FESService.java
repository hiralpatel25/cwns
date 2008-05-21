package gov.epa.owm.mtb.cwns.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;

public interface FESService {
	
	public FacilityEntryStatus getFacilityEntryStatus(Long facilityId, Long dataAreaId);

	public void deleteFacilityEntryStatusErrors(Collection fesErrors);

	public void createFacilityEntryStatusErrors(FacilityEntryStatus fes, ArrayList errors, String userId);


	public Collection getDataAreaFESRule(Long currentDataAreaId);
	
	public void updateFes(boolean validateRequired, char requiredFlag, char enteredFlag, char errorFlag, ArrayList errors, Long facilityId, Long dataAreaId, String userId);

	public Collection getDataAreaFESErrors(long facilityId, long dataAreaId);
	
	public void deleteFacilityEntryStatus(FacilityEntryStatus fes);
	
	public boolean isDataAreaApplicable(Long facilityId, Long dataAreaId);

}
