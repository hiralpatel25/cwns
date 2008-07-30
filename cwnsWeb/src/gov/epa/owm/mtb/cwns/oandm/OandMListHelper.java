package gov.epa.owm.mtb.cwns.oandm;

public class OandMListHelper {

	private long oandMId;
	
	private long year = 0;
	
	private String categoryId = "";
	
	private String categoryName = "";

	private long plantCost = 0;
	
	private long collectionCost = 0;
	
	private long totalCost = 0;

	private String canDelete  = "N";
	
	private String hasDetailRecord = "Y";
	
	private String feedbackDeleteFlag = "N";

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
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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
	 * @return the collectionCost
	 */
	public long getCollectionCost() {
		return collectionCost;
	}

	/**
	 * @param collectionCost the collectionCost to set
	 */
	public void setCollectionCost(long collectionCost) {
		this.collectionCost = collectionCost;
	}

	/**
	 * @return the oandMId
	 */
	public long getOandMId() {
		return oandMId;
	}

	/**
	 * @param oandMId the oandMId to set
	 */
	public void setOandMId(long oandMId) {
		this.oandMId = oandMId;
	}

	/**
	 * @return the plantCost
	 */
	public long getPlantCost() {
		return plantCost;
	}

	/**
	 * @param plantCost the plantCost to set
	 */
	public void setPlantCost(long plantCost) {
		this.plantCost = plantCost;
	}

	/**
	 * @return the totalCost
	 */
	public long getTotalCost() {
		return totalCost;
	}

	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(long totalCost) {
		this.totalCost = totalCost;
	}

	/**
	 * @return the year
	 */
	public long getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(long year) {
		this.year = year;
	}

	/**
	 * @return the hasDetailRecord
	 */
	public String getHasDetailRecord() {
		return hasDetailRecord;
	}

	/**
	 * @param hasDetailRecord the hasDetailRecord to set
	 */
	public void setHasDetailRecord(String hasDetailRecord) {
		this.hasDetailRecord = hasDetailRecord;
	}

	public String getFeedbackDeleteFlag() {
		return feedbackDeleteFlag;
	}

	public void setFeedbackDeleteFlag(String feedbackDeleteFlag) {
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

}