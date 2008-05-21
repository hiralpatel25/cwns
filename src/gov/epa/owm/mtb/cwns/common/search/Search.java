package gov.epa.owm.mtb.cwns.common.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.facility.FacilityListDisplayAction;


public class Search implements Serializable{
	public  static final String SEARCH_TYPE_STORE_FUNCTION_ADVANCE_SEARCH = "FUNCTION_ADVANCE_SEARCH";
	public 	static final String SEARCH_TYPE_STORE_FUNCTION_KEYWORD_SEARCH = "FUNCTION_KEYWORD_SEARCH";
	public  static final String SEARCH_TYPE_CODED_SEARCH = "CODED_SEARCH";
	
	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private SearchConditions searchConditions;
	private Collection sortCriteria;
	private Collection alias;
	private int startIndex;
	private int maxResults;
	private boolean isNamedQuery;
	private String queryName;
	private Collection queryParameters;
	private Map queryProperties;
	private int numOfFacilities;
	private String sortColumn = FacilityDAO.SORT_ADVANCE_SEARCH_FACILITY_NAME; //by default 
	private String sortOrder = FacilityDAO.SORT_ORDER_ADVANCE_SEARCH_ASCENDING;
	private String sortColumnDescription =FacilityListDisplayAction.SORT_DESC_FACILITY_NAME;
	private String sortOrderDescription =FacilityListDisplayAction.SORT_ORDER_DESC_ASCENDING;
	private String searchType;
	private String cwnsUserId = "";
	
	
	public String getCwnsUserId() {
		return cwnsUserId;
	}
	public void setCwnsUserId(String cwnsUserId) {
		this.cwnsUserId = cwnsUserId;
	}
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
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public int getNumOfFacilities() {
		return numOfFacilities;
	}
	public void setNumOfFacilities(int numOfFacilities) {
		this.numOfFacilities = numOfFacilities;
	}
	public Map getQueryProperties() {
		return queryProperties;
	}
	public void setQueryProperties(Map queryProperties) {
		this.queryProperties = queryProperties;
	}
	public Collection getAlias() {
		return alias;
	}
	public void setAlias(Collection alias) {
		this.alias = alias;
	}
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public Collection getSortCriteria() {
		return sortCriteria;
	}
	public void setSortCriteria(Collection sortCriteria) {
		this.sortCriteria = sortCriteria;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public SearchConditions getSearchConditions() {
		return searchConditions;
	}
	public void setSearchConditions(SearchConditions searchConditions) {
		this.searchConditions = searchConditions;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Collection getQueryParameters() {
		return queryParameters;
	}
	public void setQueryParameters(Collection queryParameters) {
		this.queryParameters = queryParameters;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public boolean isNamedQuery() {
		return isNamedQuery;
	}
	public void setNamedQuery(boolean isNamedQuery) {
		this.isNamedQuery = isNamedQuery;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
}
