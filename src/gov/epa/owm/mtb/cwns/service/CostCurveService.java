package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CostCurveService {
	
	// ONSITE WASTEWATER TREATMENT SYSTEMS
	public static final String CODE_INDIVIDUAL_WASTEWATER_TREATMENT_ALL = "NIA";
	public static final String CODE_INDIVIDUAL_WASTEWATER_TREATMENT_INNOVATIVE = "NII";
	public static final String CODE_INDIVIDUAL_WASTEWATER_TREATMENT_CONVENTIONAL = "NIC";
	public static final String CODE_CLUSTER_WASTEWATER_TREATMENT = "NCA";
	
	public static final String CODE_REHAB_WASTEWATER_TREATMENT_ALL = "RIA";
	public static final String CODE_REHAB_WASTEWATER_TREATMENT_INNOVATIVE = "RII";
	public static final String CODE_REHAB_WASTEWATER_TREATMENT_CONVENTIONAL = "RIC";
	public static final String CODE_REHAB_CLUSTER_WASTEWATER_TREATMENT = "RCA";

	// SEPARATE SEWER SYSTEMS
	public static final String CODE_NEW_SEPERATE_SEWER_INTERCEPTOR = "NSI";
	public static final String CODE_NEW_SEPERATE_SEWER_COLLECTOR = "NSS";
	public static final String CODE_SEPERATE_SEWER_REHAB = "RSS";
	
	// COMBINED SEWER SYSTEMS
	public static final String CODE_COMBINED_SEWER_OVERFLOW = "CSO";
	
	// WASTEWATER TREATMENT PLANT SYSTEM
	public static final String CODE_NEW_TREATMENT_PLANT = "NTP";
	public static final String CODE_INCREASE_FLOW_CAPACITY = "IFC";
	public static final String CODE_INCREASE_LEVEL_TREATMENT = "ILT";
	public static final String CODE_INCREASE_CAPACITY_TREATMENT = "IFL";
	public static final String CODE_REPLACE_TREATMENT_PLANT = "RTP";
	public static final String CODE_DISINFECTION_ONLY = "DIO";	
	
	public CostCurveOutput runCostCurve(String costCurveId, Long facilityId) throws CostCurveException;
	
	public Collection getFacilityCostCurves(Long facilityId);

	public Collection getCostCurveValidationDataAreas(Long facilityId);

	public void updateFacilityCostCurveDataArea(char errorFlag, Set errors, long facilityCostCurveId, Long dataAreaId, String userId);
	
	public Collection getFacilityCostCurvesWithRerunAndNoErrors(Long facilityId);
	
	public void runCostCurve(Long facilityId, String userId) throws CostCurveException;
	
	public long getCostCurveId(String costCurveCode);
	
	public Collection getCostCategories(long costCurveId);
}
