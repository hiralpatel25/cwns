package gov.epa.owm.mtb.cwns.capitalCost;

public class CapitalCostListHelper {

	private long capitalCostId;
	
	private String category = "";
	
	private String categoryName = "";

	private String classification = "";

	private char costMethodCode;
	
	private String costCurveName;
	
	private long baseAmount = 0;
	
	private long adjustedAmount = 0;
	
	private String srfEligible = "";
	
	private String costNeedType = "";
	
	private String sso = "N";

	private String canDelete  = "N";
	
	private String categoryValidForSSO = "";
	
	private String feedbackDeleteFlag = "";

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
	 * @return the canDelete
	 */
	public String getCanDelete() {
		return canDelete;
	}

	/**
	 * @param canDelete the canDelete to set
	 */
	public void setCanDelete(String canDelete) {
		this.canDelete = canDelete;
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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
	 * @return the costMethodCode
	 */
	public char getCostMethodCode() {
		return costMethodCode;
	}

	/**
	 * @param costMethodCode the costMethodCode to set
	 */
	public void setCostMethodCode(char costMethodCode) {
		this.costMethodCode = costMethodCode;
	}
	
	public String getCostCurveName() {
		return costCurveName;
	}

	public void setCostCurveName(String costCurveName) {
		this.costCurveName = costCurveName;
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
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public String getFeedbackDeleteFlag() {
		return feedbackDeleteFlag;
	}

	public void setFeedbackDeleteFlag(String feedbackDeleteFlag) {
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

}