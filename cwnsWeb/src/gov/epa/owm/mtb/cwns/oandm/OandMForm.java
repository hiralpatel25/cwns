package gov.epa.owm.mtb.cwns.oandm;

import org.apache.struts.action.ActionForm;

public class OandMForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	public static final long INITIAL_LONG_VALUE = -999;
	
	private String oandMAct = "";

	private String isUpdatable = "Y";

	private long oandMId = INITIAL_LONG_VALUE;
	
	private long oandMFacilityId = INITIAL_LONG_VALUE;
	
	private String detailEditExpand = "N";
	
	private String isAddAction = "N";
	
	private long oldCategoryId = INITIAL_LONG_VALUE;

	private short year = 0;
	
	private long categoryId = INITIAL_LONG_VALUE;
	
	private String categoryName = "";

	private long plantCost = 0;
	
	private long collectionCost = 0;
	
	private long totalCost = 0;	
	
	private String isNPS = "";
	
	private long currentYear = 2008;
	
	/**
	 * @return the currentYear
	 */
	public long getCurrentYear() {
		return currentYear;
	}

	/**
	 * @param currentYear the currentYear to set
	 */
	public void setCurrentYear(long currentYear) {
		this.currentYear = currentYear;
	}

	/**
	 * @return the detailEditExpand
	 */
	
	public void initForm()
	{
		year = 0;
		plantCost = 0;
		collectionCost = 0;
		totalCost = 0;
		categoryId = INITIAL_LONG_VALUE;
	}
	
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
	 * @return the oandMAct
	 */
	public String getOandMAct() {
		return oandMAct;
	}

	/**
	 * @param oandMAct the oandMAct to set
	 */
	public void setOandMAct(String oandMAct) {
		this.oandMAct = oandMAct;
	}

	/**
	 * @return the oandMFacilityId
	 */
	public long getOandMFacilityId() {
		return oandMFacilityId;
	}

	/**
	 * @param oandMFacilityId the oandMFacilityId to set
	 */
	public void setOandMFacilityId(long oandMFacilityId) {
		this.oandMFacilityId = oandMFacilityId;
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
	 * @return the categoryId
	 */
	public long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the year
	 */
	public short getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(short year) {
		this.year = year;
	}

	/**
	 * @return the oldCategoryId
	 */
	public long getOldCategoryId() {
		return oldCategoryId;
	}

	/**
	 * @param oldCategoryId the oldCategoryId to set
	 */
	public void setOldCategoryId(long oldCategoryId) {
		this.oldCategoryId = oldCategoryId;
	}

	/**
	 * @return the isNPS
	 */
	public String getIsNPS() {
		return isNPS;
	}

	/**
	 * @param isNPS the isNPS to set
	 */
	public void setIsNPS(String isNPS) {
		this.isNPS = isNPS;
	}

}
