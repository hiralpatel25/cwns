package gov.epa.owm.mtb.cwns.service;

import java.util.Collection;

public interface EffluentService {
	
	public static final long EFFLUENT_TREATMENT_LEVEL_RAW_DISCHARGE_ID = 10;
	public static final long EFFLUENT_TREATMENT_LEVEL_PRIMARY_ID = 20;
	public static final long EFFLUENT_TREATMENT_LEVEL_ADAVANCE_PRIMARY_ID = 30;
	public static final long EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID = 40;
	public static final long EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID = 50;
	
	public static final char ADVANCED_TREATMENT_TYPE_PRESENT ='P';
	public static final char ADVANCED_TREATMENT_TYPE_PROJECTED ='F';
	public static final char EFFLUENT_LEVEL_TYPE_PRESENT ='P';
	public static final char EFFLUENT_LEVEL_TYPE_PROJECTED ='F';
	
	public static final long EFFLUENT_TREATMENT_INDICATOR_BOD =1;
	public static final long EFFLUENT_TREATMENT_INDICATOR_N =2;
	public static final long EFFLUENT_TREATMENT_INDICATOR_P =3;
	
	public long getPresentFacilityEffluentLevel(Long facilityId);
	
	public long getProjectedFacilityEffluentLevel(Long facilityId);
	
	public boolean isPresentdFacilityDisinfected(Long facilityId);
	
	public boolean isProjectedFacilityDisinfected(Long facilityId);

	public int getPresentProjectEffluentAdvancedTypeSubtype(Long facilityId, String projectCode);
	
	public int getProjectedProjectEffluentAdvancedTypeSubtype(Long facilityId, String projectCode);
	
	public Collection getEffluentTreatmentLevel();
	
	public Collection getAdvanceTreatmentTypes();
	
	public Collection getPresentAdvanceTreatmentTypes(Long facilityId);	

	public Collection getProjectedAdvanceTreatmentTypes(Long facilityId);
	
	public void saveEffluent(Long facilityId, long effluentTreatmentLevelId, String[] advanceTreamentIndicators, char presentProjectedCode, char disinfectionFlag, String userId);
	
	public int getTreatmentLevelOrder(long treatmentLevelId, Collection advanceIndicators);
	
	//public Collection getFacilityEffluents(Long facilityId);
	
}
