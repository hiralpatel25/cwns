package gov.epa.owm.mtb.cwns.needs;

public class NeedsHelper {

	private long documentId;
	
	private String documentTitle = "";

	private String documentUrl = "";

	private String documentTypeId = "";
	
	private String documentTypeDesc = "";
	
	private String publishedDate = "";
	
	private String authorName = "";
	
	private String hasFederalAmount = "N";

	private String hasSSEAmount  = "N";
	
	private String hasCost = "N";

	private long federalAmount  = 0;

	private long sseAmount  = 0;

	private String submitFlag = "N";

	private String footnoteableFlag  = "N";
	
	private String outdatedDocCertificatnFlag = "N";

	private String documentEditableFlag = "N";
	
	private String isDocumentUpdatable = "N";
	
	private String repositoryId = "";
	
	//the below fields are added for document search
	private String publishDateTo = "";
	
	private String publishDateFrom = "";
	
	private String keywords = "";
	
	private String isDocumentReload = "Y";
	
	private String feedbackDeleteFlag = "N";
	

	public String getFeedbackDeleteFlag() {
		return feedbackDeleteFlag;
	}

	public void setFeedbackDeleteFlag(String feedbackDeleteFlag) {
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

	public String getIsDocumentReload() {
		return isDocumentReload;
	}

	public void setIsDocumentReload(String isDocumentReload) {
		this.isDocumentReload = isDocumentReload;
	}

	/**
	 * @return the isDocumentUpdatable
	 */
	public String getIsDocumentUpdatable() {
		return isDocumentUpdatable;
	}

	/**
	 * @param isDocumentUpdatable the isDocumentUpdatable to set
	 */
	public void setIsDocumentUpdatable(String isDocumentUpdatable) {
		this.isDocumentUpdatable = isDocumentUpdatable;
	}

	public NeedsHelper() {
		super();
	}

	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * @param authorName the authorName to set
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * @return the documentEditableFlag
	 */
	public String getDocumentEditableFlag() {
		return documentEditableFlag;
	}

	/**
	 * @param documentEditableFlag the documentEditableFlag to set
	 */
	public void setDocumentEditableFlag(String documentEditableFlag) {
		this.documentEditableFlag = documentEditableFlag;
	}

	/**
	 * @return the documentId
	 */
	public long getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the documentTitle
	 */
	public String getDocumentTitle() {
		return documentTitle;
	}

	/**
	 * @param documentTitle the documentTitle to set
	 */
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	/**
	 * @return the documentTypeDesc
	 */
	public String getDocumentTypeDesc() {
		return documentTypeDesc;
	}

	/**
	 * @param documentTypeDesc the documentTypeDesc to set
	 */
	public void setDocumentTypeDesc(String documentTypeDesc) {
		this.documentTypeDesc = documentTypeDesc;
	}

	/**
	 * @return the documentTypeId
	 */
	public String getDocumentTypeId() {
		return documentTypeId;
	}

	/**
	 * @param documentTypeId the documentTypeId to set
	 */
	public void setDocumentTypeId(String documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	/**
	 * @return the documentUrl
	 */
	public String getDocumentUrl() {
		return documentUrl;
	}

	/**
	 * @param documentUrl the documentUrl to set
	 */
	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	/**
	 * @return the federalAmount
	 */
	public long getFederalAmount() {
		return federalAmount;
	}

	/**
	 * @param federalAmount the federalAmount to set
	 */
	public void setFederalAmount(long federalAmount) {
		this.federalAmount = federalAmount;
	}

	/**
	 * @return the footnoteableFlag
	 */
	public String getFootnoteableFlag() {
		return footnoteableFlag;
	}

	/**
	 * @param footnoteableFlag the footnoteableFlag to set
	 */
	public void setFootnoteableFlag(String footnoteableFlag) {
		this.footnoteableFlag = footnoteableFlag;
	}

	/**
	 * @return the hasFederalAmount
	 */
	public String getHasFederalAmount() {
		return hasFederalAmount;
	}

	/**
	 * @param hasFederalAmount the hasFederalAmount to set
	 */
	public void setHasFederalAmount(String hasFederalAmount) {
		this.hasFederalAmount = hasFederalAmount;
	}

	/**
	 * @return the hasSSEAmount
	 */
	public String getHasSSEAmount() {
		return hasSSEAmount;
	}

	/**
	 * @param hasSSEAmount the hasSSEAmount to set
	 */
	public void setHasSSEAmount(String hasSSEAmount) {
		this.hasSSEAmount = hasSSEAmount;
	}

	/**
	 * @return the publishedDate
	 */
	public String getPublishedDate() {
		return publishedDate;
	}

	/**
	 * @param publishedDate the publishedDate to set
	 */
	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	/**
	 * @return the sseAmount
	 */
	public long getSseAmount() {
		return sseAmount;
	}

	/**
	 * @param sseAmount the sseAmount to set
	 */
	public void setSseAmount(long sseAmount) {
		this.sseAmount = sseAmount;
	}

	/**
	 * @return the submitFlag
	 */
	public String getSubmitFlag() {
		return submitFlag;
	}

	/**
	 * @param submitFlag the submitFlag to set
	 */
	public void setSubmitFlag(String submitFlag) {
		this.submitFlag = submitFlag;
	}

	/**
	 * @return the outdatedDocCertificatnFlag
	 */
	public String getOutdatedDocCertificatnFlag() {
		return outdatedDocCertificatnFlag;
	}

	/**
	 * @param outdatedDocCertificatnFlag the outdatedDocCertificatnFlag to set
	 */
	public void setOutdatedDocCertificatnFlag(String outdatedDocCertificatnFlag) {
		this.outdatedDocCertificatnFlag = outdatedDocCertificatnFlag;
	}

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

	public String getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(String repositoryId) {
		if(repositoryId == null)return;
		this.repositoryId = repositoryId;
	}

	public String getPublishDateFrom() {
		return publishDateFrom;
	}

	public void setPublishDateFrom(String publishDateFrom) {
		this.publishDateFrom = publishDateFrom;
	}

	public String getPublishDateTo() {
		return publishDateTo;
	}

	public void setPublishDateTo(String publishDateTo) {
		this.publishDateTo = publishDateTo;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
