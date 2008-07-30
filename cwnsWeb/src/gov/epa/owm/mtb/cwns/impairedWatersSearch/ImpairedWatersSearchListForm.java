package gov.epa.owm.mtb.cwns.impairedWatersSearch;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

import org.apache.struts.action.ActionForm;

import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import java.util.*;


public class ImpairedWatersSearchListForm extends ActionForm {

	public static final long INITIAL_LONG_VALUE = -999;
		
	private String impairedWatersSearchAct = "none";
	private int numOfImpairedWaters = 0;
	private int fromImpairedWatersSearch;
	private int toImpairedWatersSearch;
	private int prevImpairedWatersSearchToDisplay;
	private int nextImpairedWatersSearchToDisplay;
	private long impairedWatersSearchFacilityId = INITIAL_LONG_VALUE;
	private Collection impairedWatersSearchHelpers = new ArrayList();
	private int nextResult = 0;
	private String isUpdatable = "N";
	private String[] listIds = {}; 
	private String radiusMiles = "";
	private int impairedWatersSelectedNumber = 0;
	private int webServiceMethod = 0;
	private int tmdlOption = 0;
	private String primaryHUC = "";
	private String stateId = "";
	private String keyword = "";
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	/**
	 * @return the stateId
	 */
	public String getStateId() {
		return stateId;
	}
	/**
	 * @param stateId the stateId to set
	 */
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	/**
	 * @return the tmdlOption
	 */
	public int getTmdlOption() {
		return tmdlOption;
	}
	/**
	 * @param tmdlOption the tmdlOption to set
	 */
	public void setTmdlOption(int tmdlOption) {
		this.tmdlOption = tmdlOption;
	}
	/**
	 * @return the webServiceMethod
	 */
	public int getWebServiceMethod() {
		return webServiceMethod;
	}
	/**
	 * @param webServiceMethod the webServiceMethod to set
	 */
	public void setWebServiceMethod(int webServiceMethod) {
		this.webServiceMethod = webServiceMethod;
	}
	/**
	 * @return the impairedWatersSelectedNumber
	 */
	public int getImpairedWatersSelectedNumber() {
		return impairedWatersSelectedNumber;
	}
	/**
	 * @param impairedWatersSelectedNumber the impairedWatersSelectedNumber to set
	 */
	public void setImpairedWatersSelectedNumber(int impairedWatersSelectedNumber) {
		this.impairedWatersSelectedNumber = impairedWatersSelectedNumber;
	}

	/**
	 * @return the radiusMiles
	 */
	public String getRadiusMiles() {
		return radiusMiles;
	}
	/**
	 * @param radiusMiles the radiusMiles to set
	 */
	public void setRadiusMiles(String radiusMiles) {
		this.radiusMiles = radiusMiles;
	}
	/**
	 * @return the listIds
	 */
	public String[] getListIds() {
		return listIds;
	}
	/**
	 * @param listIds the listIds to set
	 */
	public void setListIds(String[] listIds) {
		this.listIds = listIds;
	}
	/**
	 * @return the fromImpairedWatersSearch
	 */
	public int getFromImpairedWatersSearch() {
		return fromImpairedWatersSearch;
	}
	/**
	 * @param fromImpairedWatersSearch the fromImpairedWatersSearch to set
	 */
	public void setFromImpairedWatersSearch(int fromImpairedWatersSearch) {
		this.fromImpairedWatersSearch = fromImpairedWatersSearch;
	}
	/**
	 * @return the impairedWatersSearchAct
	 */
	public String getImpairedWatersSearchAct() {
		return impairedWatersSearchAct;
	}
	/**
	 * @param impairedWatersSearchAct the impairedWatersSearchAct to set
	 */
	public void setImpairedWatersSearchAct(String impairedWatersSearchAct) {
		this.impairedWatersSearchAct = impairedWatersSearchAct;
	}
	/**
	 * @return the impairedWatersSearchFacilityId
	 */
	public long getImpairedWatersSearchFacilityId() {
		return impairedWatersSearchFacilityId;
	}
	/**
	 * @param impairedWatersSearchFacilityId the impairedWatersSearchFacilityId to set
	 */
	public void setImpairedWatersSearchFacilityId(
			long impairedWatersSearchFacilityId) {
		this.impairedWatersSearchFacilityId = impairedWatersSearchFacilityId;
	}
	/**
	 * @return the impairedWatersSearchHelpers
	 */
	public Collection getImpairedWatersSearchHelpers() {
		return impairedWatersSearchHelpers;
	}
	/**
	 * @param impairedWatersSearchHelpers the impairedWatersSearchHelpers to set
	 */
	public void setImpairedWatersSearchHelpers(
			Collection impairedWatersSearchHelpers) {
		this.impairedWatersSearchHelpers = impairedWatersSearchHelpers;
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
	 * @return the nextImpairedWatersSearchToDisplay
	 */
	public int getNextImpairedWatersSearchToDisplay() {
		return nextImpairedWatersSearchToDisplay;
	}
	/**
	 * @param nextImpairedWatersSearchToDisplay the nextImpairedWatersSearchToDisplay to set
	 */
	public void setNextImpairedWatersSearchToDisplay(
			int nextImpairedWatersSearchToDisplay) {
		this.nextImpairedWatersSearchToDisplay = nextImpairedWatersSearchToDisplay;
	}
	/**
	 * @return the nextResult
	 */
	public int getNextResult() {
		return nextResult;
	}
	/**
	 * @param nextResult the nextResult to set
	 */
	public void setNextResult(int nextResult) {
		this.nextResult = nextResult;
	}
	/**
	 * @return the numOfImpairedWaters
	 */
	public int getNumOfImpairedWaters() {
		return numOfImpairedWaters;
	}
	/**
	 * @param numOfImpairedWaters the numOfImpairedWaters to set
	 */
	public void setNumOfImpairedWaters(int numOfImpairedWaters) {
		this.numOfImpairedWaters = numOfImpairedWaters;
	}
	/**
	 * @return the prevImpairedWatersSearchToDisplay
	 */
	public int getPrevImpairedWatersSearchToDisplay() {
		return prevImpairedWatersSearchToDisplay;
	}
	/**
	 * @param prevImpairedWatersSearchToDisplay the prevImpairedWatersSearchToDisplay to set
	 */
	public void setPrevImpairedWatersSearchToDisplay(
			int prevImpairedWatersSearchToDisplay) {
		this.prevImpairedWatersSearchToDisplay = prevImpairedWatersSearchToDisplay;
	}
	/**
	 * @return the toImpairedWatersSearch
	 */
	public int getToImpairedWatersSearch() {
		return toImpairedWatersSearch;
	}
	/**
	 * @param toImpairedWatersSearch the toImpairedWatersSearch to set
	 */
	public void setToImpairedWatersSearch(int toImpairedWatersSearch) {
		this.toImpairedWatersSearch = toImpairedWatersSearch;
	}

	/**
	 * @return the primaryHUC
	 */
	public String getPrimaryHUC() {
		return primaryHUC;
	}
	/**
	 * @param primaryHUC the primaryHUC to set
	 */
	public void setPrimaryHUC(String primaryHUC) {
		this.primaryHUC = primaryHUC;
	}	

}