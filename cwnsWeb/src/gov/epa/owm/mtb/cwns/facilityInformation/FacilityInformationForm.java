package gov.epa.owm.mtb.cwns.facilityInformation;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

import org.apache.struts.validator.ValidatorActionForm;

// import gov.epa.owm.mtb.cwns.model.Facility;

public class FacilityInformationForm extends ValidatorActionForm {


	private static final long serialVersionUID = 8860344500979728052L;

	private String cwnsNbr = "";
	
	private String facilityName = "";

	private String description = "";
	
	//private String hasFacilityComments = "N";

	//private String hasFeedbackVersion = "N";

	private String showWarningMessage = "N";

	private String isUpdatable = "N";

	private String surveyFacilityId;
	
	private String systemName;
	
	private String locationId;
	
	private String ownerCode = "PUB";
	
	private String showPrivate = "Y";
	
	private char militaryFlag='N';
	
	private String isFacility = "N";
	
	private String ownerCodeValue;
	
	private String isNpdesIconVisible = "N";
	
	private String npdesFacilityName = "";
	
	private String tmdlFlg = "N";
	
	private String sourceWaterProtectionFlg = "N";
	
	private String isLocalUser = "N";
	
	public String getIsLocalUser() {
		return isLocalUser;
	}

	public void setIsLocalUser(String isLocalUser) {
		this.isLocalUser = isLocalUser;
	}

	public String getSourceWaterProtectionFlg() {
		return sourceWaterProtectionFlg;
	}

	public void setSourceWaterProtectionFlg(String sourceWaterProtectionFlg) {
		this.sourceWaterProtectionFlg = sourceWaterProtectionFlg;
	}

	public String getTmdlFlg() {
		return tmdlFlg;
	}

	public void setTmdlFlg(String tmdlFlg) {
		this.tmdlFlg = tmdlFlg;
	}

	public String getIsNpdesIconVisible() {
		return isNpdesIconVisible;
	}

	public void setIsNpdesIconVisible(String isNpdesIconVisible) {
		this.isNpdesIconVisible = isNpdesIconVisible;
	}

	public String getNpdesFacilityName() {
		return npdesFacilityName;
	}

	public void setNpdesFacilityName(String npdesFacilityName) {
		this.npdesFacilityName = npdesFacilityName;
	}

	public String getIsFacility() {
		return isFacility;
	}

	public void setIsFacility(String isFacility) {
		this.isFacility = isFacility;
	}

	public char getMilitaryFlag() {
		return militaryFlag;
	}

	public void setMilitaryFlag(char militaryFlag) {
		this.militaryFlag = militaryFlag;
	}

	public String getIsUpdatable() {
		return isUpdatable;
	}

	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
   public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	/*
    public String getHasFacilityComments() {
		return this.hasFacilityComments;
	}
	
	public void setHasFacilityComments(String hasFacilityComments) {
		this.hasFacilityComments = hasFacilityComments;
	}

	public String getHasFeedbackVersion() {
		return this.hasFeedbackVersion;
	}

	public void setHasFeedbackVersion(String hasFeedbackVersion) {
		this.hasFeedbackVersion = hasFeedbackVersion;
	}
     */ 
	public String getShowWarningMessage() {
		return this.showWarningMessage;
	}

	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}

	public String getSurveyFacilityId() {
		return surveyFacilityId;
	}

	public void setSurveyFacilityId(String surveyFacilityId) {
		this.surveyFacilityId = surveyFacilityId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getCwnsNbr() {
		if(!"".equals(cwnsNbr)){
			return cwnsNbr.trim();
		}
		return cwnsNbr;
	}

	public void setCwnsNbr(String cwnsNbr) {
		this.cwnsNbr = cwnsNbr;
	}

	public String getOwnerCodeValue() {
		return ownerCodeValue;
	}

	public void setOwnerCodeValue(String ownerCodeValue) {
		this.ownerCodeValue = ownerCodeValue;
	}

	public String getShowPrivate() {
		return showPrivate;
	}

	public void setShowPrivate(String showPrivate) {
		this.showPrivate = showPrivate;
	}
}