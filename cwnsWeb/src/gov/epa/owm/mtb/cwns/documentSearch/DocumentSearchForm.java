package gov.epa.owm.mtb.cwns.documentSearch;

import org.apache.struts.action.ActionForm;

public class DocumentSearchForm extends ActionForm{
	
	private static final long serialVersionUID = 1;
	
	private String docType = "";
	private String dateFrom = "";
	private String dateTo = "";
	private String keywords = "";
	private String facilityId = "";
	private String firstResult = "1";
	private String maxResult = "5";
	private String needsFacilityId = "";
	private String forwardUrl = "";
	private String[] documentIds = {};
	private String isUpdatable = "N";
	
	private String docSearchAct = "";
	
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getFirstResult() {
		return firstResult;
	}
	public void setFirstResult(String firstResult) {
		this.firstResult = firstResult;
	}
	public String getDocSearchAct() {
		return docSearchAct;
	}
	public void setDocSearchAct(String docSearchAct) {
		this.docSearchAct = docSearchAct;
	}	
	/**
	 * @return the needsFacilityId
	 */
	public String getNeedsFacilityId() {
		return needsFacilityId;
	}
	/**
	 * @param needsFacilityId the needsFacilityId to set
	 */
	public void setNeedsFacilityId(String needsFacilityId) {
		this.needsFacilityId = needsFacilityId;
	}
	public String getForwardUrl() {
		return forwardUrl;
	}
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
	public String getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(String maxResult) {
		this.maxResult = maxResult;
	}
	public String[] getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(String[] documentIds) {
		this.documentIds = documentIds;
	}	
	public String getIsUpdatable() {
		return isUpdatable;
	}
	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}	
}
