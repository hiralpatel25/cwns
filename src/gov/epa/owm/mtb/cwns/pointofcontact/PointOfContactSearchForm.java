/**
 * 
 */
package gov.epa.owm.mtb.cwns.pointofcontact;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * @author Matt Connors
 *
 */
public class PointOfContactSearchForm extends ActionForm {
	
	private String action ="";
	private String searchType= "";
	private String stateId = "";
	private String facilityId= "";
	private String keyword = "";
	
	private int listSize = 0;
	private int startIndex = 1;
	private int endIndex = 0;
	
	private long[] selectedPocs;
	
	private boolean updateable;

	public long[] getSelectedPocs() {
		return selectedPocs;
	}

	public void setSelectedPocs(long[] selectedPocs) {
		this.selectedPocs = selectedPocs;
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

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
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

	public boolean isUpdateable() {
		return updateable;
	}

	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}

}
