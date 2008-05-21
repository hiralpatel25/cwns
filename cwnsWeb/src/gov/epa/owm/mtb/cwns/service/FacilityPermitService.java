package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.EfPermit;
import gov.epa.owm.mtb.cwns.model.FacilityPermit;

import java.util.Collection;
import java.util.List;

public interface FacilityPermitService {
	
	public final int MAX_RESULTS = 5;
	
	public Collection getPermitsByFacilityId(Long facilityId);

	public void deleteFacilityPermit(Long facilityId, Long permitId, CurrentUser user);
	
	public FacilityPermit getFacilityPermitByPrimaryKey(Long permitId, Long facilityId);
	
	public Collection getPermitTypes(boolean excludedPermitType);
	
	// Other	
	public void updateFacilityPermit(Long facilityId, long permitId, long permitTypeId, String permitNumber, 
			 CurrentUser user);
	
	// NPDES
	public void updateFacilityPermit(Long facilityId, long permitId, Character usedForPORFlag, CurrentUser user );

	public void addFacilityPermit(Long facilityId, long permitTypeId, String permitNumber, CurrentUser user);
	
	public boolean isPermitAlreadyUsed(Long facilityId, String permitNumber);
	
	public boolean isDuplicatePermit(Long facilityId, String permitNumber);

	public void updatePORAndFacilityAddress(Long facilityId, CurrentUser user);
	
	// Methods for permits search
	public List getPermitSearchList(
			Long facilityId,
			String locationId,
			String keyword,
			int startIndex, 
			int maxResults);

    public int getPermitSearchListCount(Long facilityId, String locationId, String keyword);

    public void associatePermits(Long facilityId, String[] permitNbrs, String userId);
    
    public EfPermit getNPDESPermitDetails(String permitNbr);
    
    public Collection facilitiesAssWithPermit(String permitNbr, Long facilityId);
    
    public void createOrUpdateInfo(Long facilityId, CurrentUser user, long permitId);
    
    public void updateUseDataNPDESPermitInfo(CurrentUser user, int start, int max);
    
    public void updateUseDataNPDESPermitInfo(CurrentUser user, Long facilityId);
       
}
