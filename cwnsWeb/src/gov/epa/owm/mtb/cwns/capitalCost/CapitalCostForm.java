package gov.epa.owm.mtb.cwns.capitalCost;

import org.apache.struts.action.ActionForm;

public class CapitalCostForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	public static final long INITIAL_LONG_VALUE = -999;
	
	private String capitalCostAct = "";

	private String isUpdatable = "Y";

	private long capitalCostId = INITIAL_LONG_VALUE;
	
	private long capitalCostFacilityId = INITIAL_LONG_VALUE;
	
	private String detailEditExpand = "N";
	
	private String isAddAction = "N";
	
	private String costCategory = "";
	
	private String costCategoryName = "";
	
	private String costMethodCode = "";
	
	private String classification = "";

	private String costNeedType = "";
	
	private long baseAmount = 0;
	
	private long adjustedAmount = 0;
	
	private String srfEligible = "";
	
	private String sso = "";
	
	private long documentId = INITIAL_LONG_VALUE;
	
	private double monthlyBaseCostIndexAmt = 1.0;
	
	private double monthlyCostIndexAmt = 1.0;
	
	private String costIndexBaseDate = "";
	
	private String validForCost = "Y";
	
	private String documentTypeNeedType = "";
	
	// If Facility is Privately Owned, 
	// Federal Costs are only allowed 
	// if Facility has a Facility Type 
	// that allows Federal Needs for 
	// Private Facilities.
	private String privateAllowFederal = "Y";
	
	private String categoryValidForSSO = "";
	
	private String categoryNoClassification = "";
	
	private String categoryNoFacilityTypeChange = "";
	
	/**
	 * @return the categoryNoFacilityTypeChange
	 */
	public String getCategoryNoFacilityTypeChange() {
		return categoryNoFacilityTypeChange;
	}

	/**
	 * @param categoryNoFacilityTypeChange the categoryNoFacilityTypeChange to set
	 */
	public void setCategoryNoFacilityTypeChange(String categoryNoFacilityTypeChange) {
		this.categoryNoFacilityTypeChange = categoryNoFacilityTypeChange;
	}

	/**
	 * @return the categoryNoClassification
	 */
	public String getCategoryNoClassification() {
		return categoryNoClassification;
	}

	/**
	 * @param categoryNoClassification the categoryNoClassification to set
	 */
	public void setCategoryNoClassification(String categoryNoClassification) {
		this.categoryNoClassification = categoryNoClassification;
	}

	/**
	 * @return the categoryValidForSSO
	 */
	public String getCategoryValidForSSO() {
		return categoryValidForSSO;
	}

	/**
	 * @param categoryValidForSSO the categoryValidForSSO to set
	 */
	public void setCategoryValidForSSO(String categoryValidForSSO) {
		this.categoryValidForSSO = categoryValidForSSO;
	}

	public void initForm()
	{
		  capitalCostAct = "";
		  isUpdatable = "Y";
		  detailEditExpand = "N";
		  isAddAction = "N";
		  costCategory = "";
		  costMethodCode = "";
		  classification = "";
		  costNeedType = "";
		  baseAmount = 0;
		  adjustedAmount = 0;
		  srfEligible = "";
		  sso = "";		
		  monthlyBaseCostIndexAmt = 1.0;
		  monthlyCostIndexAmt = 1.0;
		  costIndexBaseDate = "";
		  validForCost = "Y";
		  documentTypeNeedType = "";
		  privateAllowFederal = "";
	}
	
	/**
	 * @return the adjustedAmount
	 */
	public long getAdjustedAmount() {
		return adjustedAmount;
	}
	/**
	 * @param adjustedAmount the adjustedAmount to set
	 */
	public void setAdjustedAmount(long adjustedAmount) {
		this.adjustedAmount = adjustedAmount;
	}
	/**
	 * @return the baseAmount
	 */
	public long getBaseAmount() {
		return baseAmount;
	}
	/**
	 * @param baseAmount the baseAmount to set
	 */
	public void setBaseAmount(long baseAmount) {
		this.baseAmount = baseAmount;
	}
	/**
	 * @return the capitalCostAct
	 */
	public String getCapitalCostAct() {
		return capitalCostAct;
	}
	/**
	 * @param capitalCostAct the capitalCostAct to set
	 */
	public void setCapitalCostAct(String capitalCostAct) {
		this.capitalCostAct = capitalCostAct;
	}

	/**
	 * @return the capitalCostFacilityId
	 */
	public long getCapitalCostFacilityId() {
		return capitalCostFacilityId;
	}
	/**
	 * @param capitalCostFacilityId the capitalCostFacilityId to set
	 */
	public void setCapitalCostFacilityId(long capitalCostFacilityId) {
		this.capitalCostFacilityId = capitalCostFacilityId;
	}
	/**
	 * @return the capitalCostId
	 */
	public long getCapitalCostId() {
		return capitalCostId;
	}
	/**
	 * @param capitalCostId the capitalCostId to set
	 */
	public void setCapitalCostId(long capitalCostId) {
		this.capitalCostId = capitalCostId;
	}
	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}
	/**
	 * @param classification the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}
	/**
	 * @return the costCategory
	 */
	public String getCostCategory() {
		return costCategory;
	}
	/**
	 * @param costCategory the costCategory to set
	 */
	public void setCostCategory(String costCategory) {
		this.costCategory = costCategory;
	}

	/**
	 * @return the costNeedType
	 */
	public String getCostNeedType() {
		return costNeedType;
	}
	/**
	 * @param costNeedType the costNeedType to set
	 */
	public void setCostNeedType(String costNeedType) {
		this.costNeedType = costNeedType;
	}

	/**
	 * @return the detailEditExpand
	 */
	public String getDetailEditExpand() {
		return detailEditExpand;
	}
	/**
	 * @param detailEditExpand the detailEditExpand to set
	 */
	public void setDetailEditExpand(String detailEditExpand) {
		this.detailEditExpand = detailEditExpand;
	}
	/**
	 * @return the isAddAction
	 */
	public String getIsAddAction() {
		return isAddAction;
	}
	/**
	 * @param isAddAction the isAddAction to set
	 */
	public void setIsAddAction(String isAddAction) {
		this.isAddAction = isAddAction;
	}
	/**
	 * @return the isUpdatable
	 */
	public String getIsUpdatable() {
		return isUpdatable;
	}
	/**
	 * @param isUpdatable the isUpdatable to set
	 */
	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	/**
	 * @return the srfEligible
	 */
	public String getSrfEligible() {
		return srfEligible;
	}

	/**
	 * @param srfEligible the srfEligible to set
	 */
	public void setSrfEligible(String srfEligible) {
		this.srfEligible = srfEligible;
	}

	/**
	 * @return the sso
	 */
	public String getSso() {
		return sso;
	}
	/**
	 * @param sso the sso to set
	 */
	public void setSso(String sso) {
		this.sso = sso;
	}
	/**
	 * @return the costMethodCode
	 */
	public String getCostMethodCode() {
		return costMethodCode;
	}
	/**
	 * @param costMethodCode the costMethodCode to set
	 */
	public void setCostMethodCode(String costMethodCode) {
		this.costMethodCode = costMethodCode;
	}

	/**
	 * @return the documentId
	 */
	public long getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the monthlyBaseCostIndexAmt
	 */
	public double getMonthlyBaseCostIndexAmt() {
		return monthlyBaseCostIndexAmt;
	}

	/**
	 * @param monthlyBaseCostIndexAmt the monthlyBaseCostIndexAmt to set
	 */
	public void setMonthlyBaseCostIndexAmt(double monthlyBaseCostIndexAmt) {
		this.monthlyBaseCostIndexAmt = monthlyBaseCostIndexAmt;
	}

	/**
	 * @return the monthlyCostIndexAmt
	 */
	public double getMonthlyCostIndexAmt() {
		return monthlyCostIndexAmt;
	}

	/**
	 * @param monthlyCostIndexAmt the monthlyCostIndexAmt to set
	 */
	public void setMonthlyCostIndexAmt(double monthlyCostIndexAmt) {
		this.monthlyCostIndexAmt = monthlyCostIndexAmt;
	}

	/**
	 * @return the costCategoryName
	 */
	public String getCostCategoryName() {
		return costCategoryName;
	}

	/**
	 * @param costCategoryName the costCategoryName to set
	 */
	public void setCostCategoryName(String costCategoryName) {
		this.costCategoryName = costCategoryName;
	}

	/**
	 * @return the costIndexBaseDate
	 */
	public String getCostIndexBaseDate() {
		return costIndexBaseDate;
	}

	/**
	 * @param costIndexBaseDate the costIndexBaseDate to set
	 */
	public void setCostIndexBaseDate(String costIndexBaseDate) {
		this.costIndexBaseDate = costIndexBaseDate;
	}

	/**
	 * @return the validForCost
	 */
	public String getValidForCost() {
		return validForCost;
	}

	/**
	 * @param validForCost the validForCost to set
	 */
	public void setValidForCost(String validForCost) {
		this.validForCost = validForCost;
	}

	/**
	 * @return the documentTypeNeedType
	 */
	public String getDocumentTypeNeedType() {
		return documentTypeNeedType;
	}

	/**
	 * @param documentTypeNeedType the documentTypeNeedType to set
	 */
	public void setDocumentTypeNeedType(String documentTypeNeedType) {
		this.documentTypeNeedType = documentTypeNeedType;
	}

	/**
	 * @return the privateAllowFederal
	 */
	public String getPrivateAllowFederal() {
		return privateAllowFederal;
	}

	/**
	 * @param privateAllowFederal the privateAllowFederal to set
	 */
	public void setPrivateAllowFederal(String privateAllowFederal) {
		this.privateAllowFederal = privateAllowFederal;
	}
}
