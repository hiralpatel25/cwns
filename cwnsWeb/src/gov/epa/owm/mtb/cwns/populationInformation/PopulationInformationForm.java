package gov.epa.owm.mtb.cwns.populationInformation;

import org.apache.struts.action.ActionForm;

public class PopulationInformationForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	
	private String hasFacilityComments = "N";

	private String hasFeedbackVersion = "N";
	
	private String showWarningMessage = "N";
	
	private String isUpdatable = "N";

	private String hasUpstream = "N";
	
	private String surveyFacilityId;

	private String populationAct = "none";
		
	private String enableRecCollectionResPresent = "N";	// Sewer
	
	private String enableRecCollectionPresent;
	
	private String enableRecCollectionProjected;
	
	private String enableDecentralizedPresent;
	
	private String enableDecentralizedProjected;
	
	private String enableISDSPresent;
	
	private String enableISDSProjected;
			
	private int resRecPresentPopCnt = 0;
	
	private int nonResRecPresentPopCnt = 0;
		
    private int resRecProjectedPopCnt = 0;
        	
	private int nonResRecProjectedPopCnt = 0;
	
    private int resRecProjectedYear;
	
	private int nonResRecProjectedYear;
	
	private int upstreamPresentResPopulation = 0;
	
	private int upstreamPresentNonResPopulation = 0;
	
	private int upstreamProjectedResPopulation = 0;
	
	private int upstreamProjectedNonResPopulation = 0;
	
	private int resDecPresentPopCnt = 0;
	
	private int nonResDecPresentPopCnt = 0;
	
	private int resDecProjectedPopCnt = 0;
	
	private int nonResDecProjectedPopCnt = 0;
	
    private int resDecProjectedYear = 0;
	
	private int nonResDecProjectedYear = 0;
	
	private int resISDSPresentPopCnt = 0;
	
	private int nonResISDSPresentPopCnt = 0;
	
	private int resISDSProjectedPopCnt = 0;
	
	private int nonResISDSProjectedPopCnt = 0;
	
    private int resISDSProjectedYear = 0;
	
	private int nonResISDSProjectedYear = 0;
	
	private char smallCommunityExceptionFlag;
	
	private int presentResClusteredHouses = 0;
	
	private int projectedResClusteredHouses = 0;
	
	private int presentNonResClusteredHouses = 0;
	
	private int projectedNonResClusteredHouses = 0;
	
	private String displayClusteredDetails = "N";
	
	private float resPopulationPerHouse;
	
	private float nonResPopulationPerHouse;
	
	private float resOWTSPopulationPerHouse;
	
	private float nonResOWTSPopulationPerHouse;
	
	private String clusteredAct = "none";
	
	private int presentResOWTSHouses;
	
	private int projectedResOWTSHouses;
	
	private int presentNonResOWTSHouses;
	
	private int projectedNonResOWTSHouses;
	
	private String isClusteredSystemExists = "N";
		
	private String isOWTSystemExists = "N";	
	
	private String displayOWTSDetails = "N";
	
	private String owtsAct = "none";
			
	public int getPresentNonResOWTSHouses() {
		return presentNonResOWTSHouses;
	}

	public void setPresentNonResOWTSHouses(int presentNonResOWTSHouses) {
		this.presentNonResOWTSHouses = presentNonResOWTSHouses;
	}

	public int getPresentResOWTSHouses() {
		return presentResOWTSHouses;
	}

	public void setPresentResOWTSHouses(int presentResOWTSHouses) {
		this.presentResOWTSHouses = presentResOWTSHouses;
	}

	public int getProjectedNonResOWTSHouses() {
		return projectedNonResOWTSHouses;
	}

	public void setProjectedNonResOWTSHouses(int projectedNonResOWTSHouses) {
		this.projectedNonResOWTSHouses = projectedNonResOWTSHouses;
	}

	public int getProjectedResOWTSHouses() {
		return projectedResOWTSHouses;
	}

	public void setProjectedResOWTSHouses(int projectedResOWTSHouses) {
		this.projectedResOWTSHouses = projectedResOWTSHouses;
	}

	public float getNonResPopulationPerHouse() {
		return nonResPopulationPerHouse;
	}

	public void setNonResPopulationPerHouse(float nonResPopulationPerHouse) {
		this.nonResPopulationPerHouse = nonResPopulationPerHouse;
	}

	public float getResPopulationPerHouse() {
		return resPopulationPerHouse;
	}

	public void setResPopulationPerHouse(float resPopulationPerHouse) {
		this.resPopulationPerHouse = resPopulationPerHouse;
	}

	public String getDisplayClusteredDetails() {
		return displayClusteredDetails;
	}

	public void setDisplayClusteredDetails(String displayClusteredDetails) {
		this.displayClusteredDetails = displayClusteredDetails;
	}

	public int getPresentNonResClusteredHouses() {
		return presentNonResClusteredHouses;
	}

	public void setPresentNonResClusteredHouses(int presentNonResClusteredHouses) {
		this.presentNonResClusteredHouses = presentNonResClusteredHouses;
	}

	public int getPresentResClusteredHouses() {
		return presentResClusteredHouses;
	}

	public void setPresentResClusteredHouses(int presentResClusteredHouses) {
		this.presentResClusteredHouses = presentResClusteredHouses;
	}

	public int getProjectedNonResClusteredHouses() {
		return projectedNonResClusteredHouses;
	}

	public void setProjectedNonResClusteredHouses(int projectedNonResClusteredHouses) {
		this.projectedNonResClusteredHouses = projectedNonResClusteredHouses;
	}

	public int getProjectedResClusteredHouses() {
		return projectedResClusteredHouses;
	}

	public void setProjectedResClusteredHouses(int projectedResClusteredHouses) {
		this.projectedResClusteredHouses = projectedResClusteredHouses;
	}

	public static String convertIntToString(int i)
	{
		if(i==0) 
			return "";
		else
			return Integer.toString(i);
	}
	
	public String getIsUpdatable() {
		return isUpdatable;
	}

	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	public String getHasFacilityComments() {
		return hasFacilityComments;
	}

	public void setHasFacilityComments(String hasFacilityComments) {
		this.hasFacilityComments = hasFacilityComments;
	}

	public String getHasFeedbackVersion() {
		return hasFeedbackVersion;
	}

	public void setHasFeedbackVersion(String hasFeedbackVersion) {
		this.hasFeedbackVersion = hasFeedbackVersion;
	}

	public String getPopulationAct() {
		return populationAct;
	}

	public void setPopulationAct(String populationAct) {
		this.populationAct = populationAct;
	}

	public String getSurveyFacilityId() {
		return surveyFacilityId;
	}

	public void setSurveyFacilityId(String surveyFacilityId) {
		this.surveyFacilityId = surveyFacilityId;
	}

	public String getEnableDecentralizedPresent() {
		return enableDecentralizedPresent;
	}

	public void setEnableDecentralizedPresent(String enableDecentralizedPresent) {
		this.enableDecentralizedPresent = enableDecentralizedPresent;
	}

	public String getEnableDecentralizedProjected() {
		return enableDecentralizedProjected;
	}

	public void setEnableDecentralizedProjected(String enableDecentralizedProjected) {
		this.enableDecentralizedProjected = enableDecentralizedProjected;
	}

	public String getEnableISDSPresent() {
		return enableISDSPresent;
	}

	public void setEnableISDSPresent(String enableISDSPresent) {
		this.enableISDSPresent = enableISDSPresent;
	}

	public String getEnableISDSProjected() {
		return enableISDSProjected;
	}

	public void setEnableISDSProjected(String enableISDSProjected) {
		this.enableISDSProjected = enableISDSProjected;
	}

	public String getEnableRecCollectionPresent() {
		return enableRecCollectionPresent;
	}

	public void setEnableRecCollectionPresent(String enableRecCollectionPresent) {
		this.enableRecCollectionPresent = enableRecCollectionPresent;
	}

	public String getEnableRecCollectionProjected() {
		return enableRecCollectionProjected;
	}

	public void setEnableRecCollectionProjected(String enableRecCollectionProjected) {
		this.enableRecCollectionProjected = enableRecCollectionProjected;
	}

	public int getNonResDecProjectedPopCnt() {
		return nonResDecProjectedPopCnt;
	}

	public void setNonResDecProjectedPopCnt(int nonResDecProjectedPopCnt) {
		this.nonResDecProjectedPopCnt = nonResDecProjectedPopCnt;
	}

	public int getNonResISDSProjectedPopCnt() {
		return nonResISDSProjectedPopCnt;
	}

	public void setNonResISDSProjectedPopCnt(int nonResISDSProjectedPopCnt) {
		this.nonResISDSProjectedPopCnt = nonResISDSProjectedPopCnt;
	}

	public int getNonResRecPresentPopCnt() {
		return nonResRecPresentPopCnt;
	}

	public void setNonResRecPresentPopCnt(int nonResRecPresentPopCnt) {
		this.nonResRecPresentPopCnt = nonResRecPresentPopCnt;
	}

	public int getNonResRecProjectedPopCnt() {
		return nonResRecProjectedPopCnt;
	}

	public void setNonResRecProjectedPopCnt(int nonResRecProjectedPopCnt) {
		this.nonResRecProjectedPopCnt = nonResRecProjectedPopCnt;
	}

	public int getResDecPresentPopCnt() {
		return resDecPresentPopCnt;
	}

	public void setResDecPresentPopCnt(int resDecPresentPopCnt) {
		this.resDecPresentPopCnt = resDecPresentPopCnt;
	}

	public int getResISDSPresentPopCnt() {
		return resISDSPresentPopCnt;
	}

	public void setResISDSPresentPopCnt(int resISDSPresentPopCnt) {
		this.resISDSPresentPopCnt = resISDSPresentPopCnt;
	}

	public int getResRecPresentPopCnt() {
		return resRecPresentPopCnt;
	}

	public void setResRecPresentPopCnt(int resRecPresentPopCnt) {
		this.resRecPresentPopCnt = resRecPresentPopCnt;
	}

	public int getResRecProjectedPopCnt() {
		return resRecProjectedPopCnt;
	}

	public void setResRecProjectedPopCnt(int resRecProjectedPopCnt) {
		this.resRecProjectedPopCnt = resRecProjectedPopCnt;
	}

	public int getNonResDecPresentPopCnt() {
		return nonResDecPresentPopCnt;
	}

	public void setNonResDecPresentPopCnt(int nonResDecPresentPopCnt) {
		this.nonResDecPresentPopCnt = nonResDecPresentPopCnt;
	}

	public int getNonResISDSPresentPopCnt() {
		return nonResISDSPresentPopCnt;
	}

	public void setNonResISDSPresentPopCnt(int nonResISDSPresentPopCnt) {
		this.nonResISDSPresentPopCnt = nonResISDSPresentPopCnt;
	}

	public int getResDecProjectedPopCnt() {
		return resDecProjectedPopCnt;
	}

	public void setResDecProjectedPopCnt(int resDecProjectedPopCnt) {
		this.resDecProjectedPopCnt = resDecProjectedPopCnt;
	}

	public int getResISDSProjectedPopCnt() {
		return resISDSProjectedPopCnt;
	}

	public void setResISDSProjectedPopCnt(int resISDSProjectedPopCnt) {
		this.resISDSProjectedPopCnt = resISDSProjectedPopCnt;
	}

	public char getSmallCommunityExceptionFlag() {
		return smallCommunityExceptionFlag;
	}

	public void setSmallCommunityExceptionFlag(char smallCommunityExceptionFlag) {
		this.smallCommunityExceptionFlag = smallCommunityExceptionFlag;
	}

	/**
	 * @return the nonResDecProjectedYear
	 */
	public int getNonResDecProjectedYear() {
		return nonResDecProjectedYear;
	}

	/**
	 * @param nonResDecProjectedYear the nonResDecProjectedYear to set
	 */
	public void setNonResDecProjectedYear(int nonResDecProjectedYear) {
		this.nonResDecProjectedYear = nonResDecProjectedYear;
	}

	/**
	 * @return the nonResISDSProjectedYear
	 */
	public int getNonResISDSProjectedYear() {
		return nonResISDSProjectedYear;
	}

	/**
	 * @param nonResISDSProjectedYear the nonResISDSProjectedYear to set
	 */
	public void setNonResISDSProjectedYear(int nonResISDSProjectedYear) {
		this.nonResISDSProjectedYear = nonResISDSProjectedYear;
	}

	/**
	 * @return the nonResRecProjectedYear
	 */
	public int getNonResRecProjectedYear() {
		return nonResRecProjectedYear;
	}

	/**
	 * @param nonResRecProjectedYear the nonResRecProjectedYear to set
	 */
	public void setNonResRecProjectedYear(int nonResRecProjectedYear) {
		this.nonResRecProjectedYear = nonResRecProjectedYear;
	}

	/**
	 * @return the resDecProjectedYear
	 */
	public int getResDecProjectedYear() {
		return resDecProjectedYear;
	}

	/**
	 * @param resDecProjectedYear the resDecProjectedYear to set
	 */
	public void setResDecProjectedYear(int resDecProjectedYear) {
		this.resDecProjectedYear = resDecProjectedYear;
	}

	/**
	 * @return the resISDSProjectedYear
	 */
	public int getResISDSProjectedYear() {
		return resISDSProjectedYear;
	}

	/**
	 * @param resISDSProjectedYear the resISDSProjectedYear to set
	 */
	public void setResISDSProjectedYear(int resISDSProjectedYear) {
		this.resISDSProjectedYear = resISDSProjectedYear;
	}

	/**
	 * @return the resRecProjectedYear
	 */
	public int getResRecProjectedYear() {
		return resRecProjectedYear;
	}

	/**
	 * @param resRecProjectedYear the resRecProjectedYear to set
	 */
	public void setResRecProjectedYear(int resRecProjectedYear) {
		this.resRecProjectedYear = resRecProjectedYear;
	}

	public String getShowWarningMessage() {
		return showWarningMessage;
	}

	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}

	/**
	 * @return the upstreamPresentNonResPopulation
	 */
	public int getUpstreamPresentNonResPopulation() {
		return upstreamPresentNonResPopulation;
	}

	/**
	 * @param upstreamPresentNonResPopulation the upstreamPresentNonResPopulation to set
	 */
	public void setUpstreamPresentNonResPopulation(
			int upstreamPresentNonResPopulation) {
		this.upstreamPresentNonResPopulation = upstreamPresentNonResPopulation;
	}

	/**
	 * @return the upstreamPresentResPopulation
	 */
	public int getUpstreamPresentResPopulation() {
		return upstreamPresentResPopulation;
	}

	/**
	 * @param upstreamPresentResPopulation the upstreamPresentResPopulation to set
	 */
	public void setUpstreamPresentResPopulation(int upstreamPresentResPopulation) {
		this.upstreamPresentResPopulation = upstreamPresentResPopulation;
	}

	/**
	 * @return the upstreamProjectedNonResPopulation
	 */
	public int getUpstreamProjectedNonResPopulation() {
		return upstreamProjectedNonResPopulation;
	}

	/**
	 * @param upstreamProjectedNonResPopulation the upstreamProjectedNonResPopulation to set
	 */
	public void setUpstreamProjectedNonResPopulation(
			int upstreamProjectedNonResPopulation) {
		this.upstreamProjectedNonResPopulation = upstreamProjectedNonResPopulation;
	}

	/**
	 * @return the upstreamProjectedResPopulation
	 */
	public int getUpstreamProjectedResPopulation() {
		return upstreamProjectedResPopulation;
	}

	/**
	 * @param upstreamProjectedResPopulation the upstreamProjectedResPopulation to set
	 */
	public void setUpstreamProjectedResPopulation(int upstreamProjectedResPopulation) {
		this.upstreamProjectedResPopulation = upstreamProjectedResPopulation;
	}

	/**
	 * @return the enableRecCollectionResPresent
	 */
	public String getEnableRecCollectionResPresent() {
		return enableRecCollectionResPresent;
	}

	/**
	 * @param enableRecCollectionResPresent the enableRecCollectionResPresent to set
	 */
	public void setEnableRecCollectionResPresent(
			String enableRecCollectionResPresent) {
		this.enableRecCollectionResPresent = enableRecCollectionResPresent;
	}

	/**
	 * @return the hasUpstream
	 */
	public String getHasUpstream() {
		return hasUpstream;
	}

	/**
	 * @param hasUpstream the hasUpstream to set
	 */
	public void setHasUpstream(String hasUpstream) {
		this.hasUpstream = hasUpstream;
	}

	public String getClusteredAct() {
		return clusteredAct;
	}

	public void setClusteredAct(String clusteredAct) {
		this.clusteredAct = clusteredAct;
	}

	public String getDisplayOWTSDetails() {
		return displayOWTSDetails;
	}

	public void setDisplayOWTSDetails(String displayOWTSDetails) {
		this.displayOWTSDetails = displayOWTSDetails;
	}

	public String getOwtsAct() {
		return owtsAct;
	}

	public void setOwtsAct(String owtsAct) {
		this.owtsAct = owtsAct;
	}

	public String getIsClusteredSystemExists() {
		return isClusteredSystemExists;
	}

	public void setIsClusteredSystemExists(String isClusteredSystemExists) {
		this.isClusteredSystemExists = isClusteredSystemExists;
	}

	public String getIsOWTSystemExists() {
		return isOWTSystemExists;
	}

	public void setIsOWTSystemExists(String isOWTSystemExists) {
		this.isOWTSystemExists = isOWTSystemExists;
	}

	public float getNonResOWTSPopulationPerHouse() {
		return nonResOWTSPopulationPerHouse;
	}

	public void setNonResOWTSPopulationPerHouse(float nonResOWTSPopulationPerHouse) {
		this.nonResOWTSPopulationPerHouse = nonResOWTSPopulationPerHouse;
	}

	public float getResOWTSPopulationPerHouse() {
		return resOWTSPopulationPerHouse;
	}

	public void setResOWTSPopulationPerHouse(float resOWTSPopulationPerHouse) {
		this.resOWTSPopulationPerHouse = resOWTSPopulationPerHouse;
	}

}
