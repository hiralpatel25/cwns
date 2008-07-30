package gov.epa.owm.mtb.cwns.impairedWatersInformation;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

public class ImpairedWatersListHelper {

	private String listId;
	private String listUrl = "";
	private String waterBodyName = "";
	private String waterBodyDetailUrl = "";
	private String impairedWaterId;
	private String hasCost = "N";
	private String waterBodyType = "";
	private String feedbackDeleteFlag="";
	
	/**
	 * @return the hasCost
	 */
	public String getHasCost() {
		return hasCost;
	}
	/**
	 * @param hasCost the hasCost to set
	 */
	public void setHasCost(String hasCost) {
		this.hasCost = hasCost;
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
	public String getWaterBodyType() {
		return waterBodyType;
	}
	public void setWaterBodyType(String waterBodyType) {
		this.waterBodyType = waterBodyType;
	}
	public String getFeedbackDeleteFlag() {
		return feedbackDeleteFlag;
	}
	public void setFeedbackDeleteFlag(String feedbackDeleteFlag) {
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}


}
