package gov.epa.owm.mtb.cwns.facility;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

import org.apache.struts.action.ActionForm;

import gov.epa.owm.mtb.cwns.common.search.SortCriteria;

import java.util.*;
import org.apache.struts.upload.FormFile;


public class FacilityListForm extends ActionForm {

	private String act = "";
	private String searchDescription;
	private int numOfFacilities;
	private int fromFacility=1;
	private int toFacility;
	private int prevFacilityToDisplay;
	private int nextFacilityToDisplay;
	private Collection facHelpers = new ArrayList();
	private String[] facilityIds = {}; 
	private FormFile importFile;
	private String sortColumn = "name";
	private int sortOrder = SortCriteria.ORDER_ASCENDING;
	
	private String facilities;
	private String selectedFacilities;
	private String importExportKey ="";
	private String newStatus;
	private String sortColumnDescription ="";
	private String sortOrderDescription ="";
	
	public String getSortColumnDescription() {
		return sortColumnDescription;
	}
	public void setSortColumnDescription(String sortColumnDescription) {
		this.sortColumnDescription = sortColumnDescription;
	}
	public String getSortOrderDescription() {
		return sortOrderDescription;
	}
	public void setSortOrderDescription(String sortOrderDescription) {
		this.sortOrderDescription = sortOrderDescription;
	}
	public String getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	public FormFile getImportFile() {
		return importFile;
	}
	public void setImportFile(FormFile importFile) {
		this.importFile = importFile;
	}
	public int getFromFacility() {
		return fromFacility;
	}
	public void setFromFacility(int fromFacility) {
		this.fromFacility = fromFacility;
	}
	public int getNumOfFacilities() {
		return numOfFacilities;
	}
	public void setNumOfFacilities(int numOfFacilities) {
		this.numOfFacilities = numOfFacilities;
	}
	public int getNextFacilityToDisplay() {
		return nextFacilityToDisplay;
	}
	public void setNextFacilityToDisplay(int nextFacilityToDisplay) {
		this.nextFacilityToDisplay = nextFacilityToDisplay;
	}
	public int getToFacility() {
		return toFacility;
	}
	public void setToFacility(int toFacility) {
		this.toFacility = toFacility;
	}
	public int getPrevFacilityToDisplay() {
		return prevFacilityToDisplay;
	}
	public void setPrevFacilityToDisplay(int prevFacilityToDisplay) {
		this.prevFacilityToDisplay = prevFacilityToDisplay;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public Collection getFacHelpers() {
		return facHelpers;
	}
	public void setFacHelpers(Collection facHelpers) {
		this.facHelpers = facHelpers;
	}
	public String[] getFacilityIds() {
		return facilityIds;
	}
	public void setFacilityIds(String[] facilityIds) {
		this.facilityIds = facilityIds;
	}
	
	public String getFacilities() {
		return facilities;
	}
	public void setFacilities(String facilities) {
		this.facilities = facilities;
	}
	public String getSelectedFacilities() {
		return selectedFacilities;
	}
	public void setSelectedFacilities(String selectedFacilities) {
		this.selectedFacilities = selectedFacilities;
	}
	public String getImportExportKey() {
		return importExportKey;
	}
	public void setImportExportKey(String importExportKey) {
		this.importExportKey = importExportKey;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getSearchDescription() {
		return searchDescription;
	}
	public void setSearchDescription(String searchDescription) {
		this.searchDescription = searchDescription;
	}

}