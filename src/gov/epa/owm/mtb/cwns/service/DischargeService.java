package gov.epa.owm.mtb.cwns.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.discharge.DischargeForm;
import gov.epa.owm.mtb.cwns.model.DischargeMethodRef;
import gov.epa.owm.mtb.cwns.model.FacilityDischarge;

/**
 * This class provides business functionality related to the Point of Contact. 
 * @author Matt Connors
 *
 */
public interface DischargeService {	

	public static String DISCHARGE_TO_ANOTHER_FACILITY = "10";
	//public static String DISCHARGE_OTHER 			   = "12";
	public static long   DISCHARGE_TO_ANOTHER_FAC      =  10;
	public static String SELECT_DISCHARGE_METHOD       = "xx";
	public static long NO_DISCHARGE_UNKNOWN		  = 14;
	public static long DISCHARGE_TO_OCEAN = 1;
	public static long DISCHARGE_TO_SURFACE_WATERS = 2;
	public static long DISCHARGE_TO_OVERLAND_FLOW_NO_DISCHARGE = 8;
	public static long DISCHARGE_TO_OVERLAND_FLOW_WITH_DISCHARGE = 9;
	public static long DISCHARGE_CSO = 11;
	public static long DISCHARGE_GROUNDWATER = 13;
	public static long DISCHARGE_NO_DISCHARGE_UNKNOWN = 14;
	public static long DISCHARGE_EVAPORATION = 6;
	public static long DISCHARGE_OTHER = 12;
	
	
	public boolean isTerminatingFacility(Long facilityId);
	
	public Collection getDischargeMethodRefs(String facilityId);
	
	public Collection getFacilityDischarges(String facilityId);

	public Collection getDischargeMethodHelpers(String facilityId, String locationType);
	
	public FacilityDischarge getFacilityDischarge(long dischargeId);	

	public void addDischargeMethod(DischargeForm dForm, CurrentUser currentUser);
	
	public double getPresentTotalFlowForNextDownstreamFacilities(Long facilityId);	
	
	public void deleteDischargeMethod(long dischargeId, CurrentUser currentUser);
	
	public void updateFacilityDischarge(DischargeForm dForm, CurrentUser currentUser);
	
	public boolean csoCostCurveInSewershed(String facilityId);
	
	public boolean isPresentDischargeToSurfaceWaters(Long facilityId);
	
	public boolean isProjectedDischargeToSurfaceWaters(Long facilityId);
	
	public boolean isOverLandFlowNoDischargeOrEvaporation(Long facilityId, String presentProjectedFlag);

	public boolean isPresentDischargeSpecified(Long facilityId);
	
	public boolean isProjectedDischargeSpecified(Long facilityId);

	public boolean isFacilityHasfaciliyTypeTreatmentPlant(Long facilityId);
	public boolean isFacilityHasPresentFacilityTypeTreatmentPlant(Long facilityId);
	public boolean isFacilityHasProjectedFacilityTypeTreatmentPlant(Long facilityId);
	
	public void setUpCostCurvesForRerun(ArrayList costCurveIdList, String facilityId);
	
	public void setUpCostCurveCodesForRerun(ArrayList costCurveCodeList, String facilityId);
	
	public boolean isPresentEffluentLevel(Long facilityId);
	
	public boolean isProjectedEffluentLevel(Long facilityId);

	public void setFeedBackDeleteFlag(long dischargeId, char feedBackDeleteFlg);
	

}
