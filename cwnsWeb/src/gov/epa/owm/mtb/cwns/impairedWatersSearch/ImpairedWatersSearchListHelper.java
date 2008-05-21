package gov.epa.owm.mtb.cwns.impairedWatersSearch;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

public class ImpairedWatersSearchListHelper {

	private String listId;
	private String listUrl = "";
	private String waterBodyName;
	private String waterBodyDetailUrl = "";
	private String impairedWaterId;
	private String endDate;
	private String checked;
	private String tmdlStatus = "";
	
	private String waterType = ""; //303D or 305B
	private double milesToLocation = -1.0;

	private String reportCategory = ""; //305B
	
	public String getReportCategory() {
		return reportCategory;
	}
	public void setReportCategory(String reportCategory) {
		this.reportCategory = reportCategory;
	}
	public String getWaterType() {
		return waterType;
	}
	public void setWaterType(String waterType) {
		this.waterType = waterType;
	}
	/**
	 * @return the tmdlStatus
	 */
	public String getTmdlStatus() {
		return tmdlStatus;
	}
	/**
	 * @param tmdlStatus the tmdlStatus to set
	 */
	public void setTmdlStatus(String tmdlStatus) {
		this.tmdlStatus = tmdlStatus;
	}
	/**
	 * @return the checked
	 */
	public String getChecked() {
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(String checked) {
		this.checked = checked;
	}
	/**
	 * @return the impairedWaterId
	 */
	public String getImpairedWaterId() {
		return impairedWaterId;
	}
	/**
	 * @param impairedWaterId the impairedWaterId to set
	 */
	public void setImpairedWaterId(String impairedWaterId) {
		this.impairedWaterId = impairedWaterId;
	}
	/**
	 * @return the listId
	 */
	public String getListId() {
		return listId;
	}
	/**
	 * @param listId the listId to set
	 */
	public void setListId(String listId) {
		this.listId = listId;
	}
	/**
	 * @return the listUrl
	 */
	public String getListUrl() {
		return listUrl;
	}
	/**
	 * @param listUrl the listUrl to set
	 */
	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}
	/**
	 * @return the waterBodyDetailUrl
	 */
	public String getWaterBodyDetailUrl() {
		return waterBodyDetailUrl;
	}
	/**
	 * @param waterBodyDetailUrl the waterBodyDetailUrl to set
	 */
	public void setWaterBodyDetailUrl(String waterBodyDetailUrl) {
		this.waterBodyDetailUrl = waterBodyDetailUrl;
	}
	/**
	 * @return the waterBodyName
	 */
	public String getWaterBodyName() {
		return waterBodyName;
	}
	/**
	 * @param waterBodyName the waterBodyName to set
	 */
	public void setWaterBodyName(String waterBodyName) {
		this.waterBodyName = waterBodyName;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public double getMilesToLocation() {
		return milesToLocation;
	}
	public void setMilesToLocation(double milesToLocation) {
		this.milesToLocation = milesToLocation;
	}


}
