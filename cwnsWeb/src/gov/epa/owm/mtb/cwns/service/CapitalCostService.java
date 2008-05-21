package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.capitalCost.CapitalCostForm;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.CategoryRef;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.Document;
import java.util.Collection;

public interface CapitalCostService {
	
	public static final String CATEGORY_ID_ADVANCE_TREATMENT = "II";
	public static final String CATEGORY_ID_COMBINED_SEWER_OVERFLOW	 = "V";
	
	public static final char COST_METHOD_CODE_DOCUMENTED = 'D';
	public static final char COST_METHOD_CODE_COST_CURVE = 'C';
	
	// Get Facility CSO Info
	public long saveOrUpdateCost(CapitalCostForm capitalCostForm, CurrentUser user);
	public Collection getCostList(long facilityId, long documentId);
	public Collection  getCategoryClassificationList(long facilityId, long docId, boolean isStateUser);
	public void deleteCost(long facilityId, long costId, CurrentUser user);
	public Cost prepareFacilityDocument(long costId);
	public double getMonthlyCostIndexAmt(long documentId, char baseMonthFlag);
	public String getMonthlyCostIndexDate(long documentId);	
	public Document getDocument(long documentId);
	public String getFacilityPrivateAllowFederal(long facilityId);
	public boolean categoryNoFacilityTypeChange(long facilityId, String categoryId, boolean isStateUser);	
	public Collection getClassificationList(String catId);	
	public boolean checkUniqueCombination(String str);	
	public Collection getCategoryRefByFacilityType(Long facilityTypeId);
	public Collection getCostsByCategory(Long facilityId, String categoryId);
	public Collection getFacilityCosts(Long facilityId);	
	public CategoryRef getCategoryRef(String categoryId);	
	public boolean canAddNewCostToDocument(Long documentId);
	public boolean facilityHasFacilityTypesAssigned(Long facilityId);
	public boolean assignedTypesAreAllNoChange(Long facilityId);
	public void updateDocumentedCosts(long facilityId, long documentId, String userId);
}