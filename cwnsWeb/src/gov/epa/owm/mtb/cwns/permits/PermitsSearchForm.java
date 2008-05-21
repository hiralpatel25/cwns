package gov.epa.owm.mtb.cwns.permits;

import org.apache.struts.action.ActionForm;

public class PermitsSearchForm extends ActionForm {

    private String isUpdatable = "N";
	private String action ="";
	private String searchType= "";
	private String stateId = "";
	private Long facilityId;
	private String keyword = "";
	
	private int listSize = 0;
	private int startIndex = 1;
	private int endIndex = 0;
	
	private String[] selectedPermits;

	public String[] getSelectedPermits() {
		return selectedPermits;
	}

	public void setSelectedPermits(String[] selectedPermits) {
		this.selectedPermits = selectedPermits;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public Long getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getIsUpdatable() {
		return isUpdatable;
	}

	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

}
