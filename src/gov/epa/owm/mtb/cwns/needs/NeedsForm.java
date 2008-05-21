package gov.epa.owm.mtb.cwns.needs;

import org.apache.struts.action.ActionForm;

public class NeedsForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	
	private String hasFacilityComments = "N";

	private String hasFeedbackVersion = "N";
	
	private String showWarningMessage = "N";
	
	private String isUpdatable = "N";

	private String needsAct = "";
	
	private String needsFacilityId = "";
	
	private long federalAdjustedAmountTotal = 0;
	
	private long sseAdjustedAmountTotal = 0;
	
	private String detailEditExpand = "N";
	
	private String isDetailUpdatable = "N";
	
	private String documentId = "";
	private String locationId = "";
	private String baseMonthYear = "";
	private String needsStartYear = "";
	private String targetDesignYear = "";
	private String narrativeText = "";
	private String othersCheckbox = "N";
	private String ongoingDialogCheckbox = "N";
	private String fileName = "";
	private String author = "";
	private String title = "";
	private String publishedDate = "";
	private String documentType = "";
	private String documentTypeDesc = "";
	private String documentGroupType = "";
	
	private String isBaseMonthYearValid = "Y";
	
	private String datesUpdatable = "Y";
	
	private String showCIPWarning = "N";
	private String showIUPWarning = "N";
	private String showFPWarning = "N";
	
	private char feedBackDeleteFlg = 'N';
	private String isLocalUser = "N";
	private String isneedsStartYearUpdatable = "N";
	private String isAnnotate ="N";

	public String getIsneedsStartYearUpdatable() {
		return isneedsStartYearUpdatable;
	}

	public void setIsneedsStartYearUpdatable(String isneedsStartYearUpdatable) {
		this.isneedsStartYearUpdatable = isneedsStartYearUpdatable;
	}

	public char getFeedBackDeleteFlg() {
		return feedBackDeleteFlg;
	}

	public void setFeedBackDeleteFlg(char feedBackDeleteFlg) {
		this.feedBackDeleteFlg = feedBackDeleteFlg;
	}

	public String getIsLocalUser() {
		return isLocalUser;
	}

	public void setIsLocalUser(String isLocalUser) {
		this.isLocalUser = isLocalUser;
	}

	/**
	 * @return the isBaseMonthYearValid
	 */
	public String getIsBaseMonthYearValid() {
		return isBaseMonthYearValid;
	}

	/**
	 * @param isBaseMonthYearValid the isBaseMonthYearValid to set
	 */
	public void setIsBaseMonthYearValid(String isBaseMonthYearValid) {
		this.isBaseMonthYearValid = isBaseMonthYearValid;
	}

	public void initForm()
	{
		 //documentId = "";
		 locationId = "";
		 baseMonthYear = "";
		 needsStartYear = "";
		 targetDesignYear = "";
		 narrativeText = "";
		 othersCheckbox = "Y";
		 ongoingDialogCheckbox = "N";
		 fileName = "";
		 author = "";
		 title = "";
		 publishedDate = "";
		 documentType = "";
		 documentTypeDesc = "";
		 documentGroupType = "";
		 detailEditExpand = "N";
		 isDetailUpdatable = "N";
		 isUpdatable = "N";
		 isBaseMonthYearValid = "Y";
		 datesUpdatable = "Y";
		 isAnnotate ="N";
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

	public static String convertIntToString(int i)
	{
		if(i==0) 
			return "";
		else
			return Integer.toString(i);
	}

	/**
	 * @return the hasFacilityComments
	 */
	public String getHasFacilityComments() {
		return hasFacilityComments;
	}

	/**
	 * @param hasFacilityComments the hasFacilityComments to set
	 */
	public void setHasFacilityComments(String hasFacilityComments) {
		this.hasFacilityComments = hasFacilityComments;
	}

	/**
	 * @return the hasFeedbackVersion
	 */
	public String getHasFeedbackVersion() {
		return hasFeedbackVersion;
	}

	/**
	 * @param hasFeedbackVersion the hasFeedbackVersion to set
	 */
	public void setHasFeedbackVersion(String hasFeedbackVersion) {
		this.hasFeedbackVersion = hasFeedbackVersion;
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
	 * @return the showWarningMessage
	 */
	public String getShowWarningMessage() {
		return showWarningMessage;
	}

	/**
	 * @param showWarningMessage the showWarningMessage to set
	 */
	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}

	/**
	 * @return the needsAct
	 */
	public String getNeedsAct() {
		return needsAct;
	}

	/**
	 * @param needsAct the needsAct to set
	 */
	public void setNeedsAct(String needsAct) {
		this.needsAct = needsAct;
	}

	/**
	 * @return the federalAdjustedAmountTotal
	 */
	public long getFederalAdjustedAmountTotal() {
		return federalAdjustedAmountTotal;
	}

	/**
	 * @param federalAdjustedAmountTotal the federalAdjustedAmountTotal to set
	 */
	public void setFederalAdjustedAmountTotal(long federalAdjustedAmountTotal) {
		this.federalAdjustedAmountTotal = federalAdjustedAmountTotal;
	}

	/**
	 * @return the sseAdjustedAmountTotal
	 */
	public long getSseAdjustedAmountTotal() {
		return sseAdjustedAmountTotal;
	}

	/**
	 * @param sseAdjustedAmountTotal the sseAdjustedAmountTotal to set
	 */
	public void setSseAdjustedAmountTotal(long sseAdjustedAmountTotal) {
		this.sseAdjustedAmountTotal = sseAdjustedAmountTotal;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the baseMonthYear
	 */
	public String getBaseMonthYear() {
		return baseMonthYear;
	}

	/**
	 * @param baseMonthYear the baseMonthYear to set
	 */
	public void setBaseMonthYear(String baseMonthYear) {
		this.baseMonthYear = baseMonthYear;
	}

	/**
	 * @return the documentGroupType
	 */
	public String getDocumentGroupType() {
		return documentGroupType;
	}

	/**
	 * @param documentGroupType the documentGroupType to set
	 */
	public void setDocumentGroupType(String documentGroupType) {
		this.documentGroupType = documentGroupType;
	}

	/**
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType the documentType to set
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the narrativeText
	 */
	public String getNarrativeText() {
		return narrativeText;
	}

	/**
	 * @param narrativeText the narrativeText to set
	 */
	public void setNarrativeText(String narrativeText) {
		this.narrativeText = narrativeText;
	}

	/**
	 * @return the documentId
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the needsStartYear
	 */
	public String getNeedsStartYear() {
		return needsStartYear;
	}

	/**
	 * @param needsStartYear the needsStartYear to set
	 */
	public void setNeedsStartYear(String needsStartYear) {
		this.needsStartYear = needsStartYear;
	}

	/**
	 * @return the ongoingDialogCheckbox
	 */
	public String getOngoingDialogCheckbox() {
		return ongoingDialogCheckbox;
	}

	/**
	 * @param ongoingDialogCheckbox the ongoingDialogCheckbox to set
	 */
	public void setOngoingDialogCheckbox(String ongoingDialogCheckbox) {
		this.ongoingDialogCheckbox = ongoingDialogCheckbox;
	}

	/**
	 * @return the othersCheckbox
	 */
	public String getOthersCheckbox() {
		return othersCheckbox;
	}

	/**
	 * @param othersCheckbox the othersCheckbox to set
	 */
	public void setOthersCheckbox(String othersCheckbox) {
		this.othersCheckbox = othersCheckbox;
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
	 * @return the targetDesignYear
	 */
	public String getTargetDesignYear() {
		return targetDesignYear;
	}

	/**
	 * @param targetDesignYear the targetDesignYear to set
	 */
	public void setTargetDesignYear(String targetDesignYear) {
		this.targetDesignYear = targetDesignYear;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the locationId
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the detailEditExpand
	 */
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
	 * @return the isDetailUpdatable
	 */
	public String getIsDetailUpdatable() {
		return isDetailUpdatable;
	}

	/**
	 * @param isDetailUpdatable the isDetailUpdatable to set
	 */
	public void setIsDetailUpdatable(String isDetailUpdatable) {
		this.isDetailUpdatable = isDetailUpdatable;
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
	 * @return the datesUpdatable
	 */
	public String getDatesUpdatable() {
		return datesUpdatable;
	}

	/**
	 * @param datesUpdatable the datesUpdatable to set
	 */
	public void setDatesUpdatable(String datesUpdatable) {
		this.datesUpdatable = datesUpdatable;
	}

	/**
	 * @return the showCIPWarning
	 */
	public String getShowCIPWarning() {
		return showCIPWarning;
	}

	/**
	 * @param showCIPWarning the showCIPWarning to set
	 */
	public void setShowCIPWarning(String showCIPWarning) {
		this.showCIPWarning = showCIPWarning;
	}

	/**
	 * @return the showFPWarning
	 */
	public String getShowFPWarning() {
		return showFPWarning;
	}

	/**
	 * @param showFPWarning the showFPWarning to set
	 */
	public void setShowFPWarning(String showFPWarning) {
		this.showFPWarning = showFPWarning;
	}

	/**
	 * @return the showIUPWarning
	 */
	public String getShowIUPWarning() {
		return showIUPWarning;
	}

	/**
	 * @param showIUPWarning the showIUPWarning to set
	 */
	public void setShowIUPWarning(String showIUPWarning) {
		this.showIUPWarning = showIUPWarning;
	}

	public String getIsAnnotate() {
		return isAnnotate;
	}

	public void setIsAnnotate(String isAnnotate) {
		this.isAnnotate = isAnnotate;
	}

}
