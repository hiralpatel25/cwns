package gov.epa.owm.mtb.cwns.pollution;

import org.apache.struts.action.ActionForm;

public class PollutionForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	public static final long INITIAL_LONG_VALUE = -999;
	
	private String pollutionAct = "";

	private String isUpdatable = "Y";

	private long pollutionFacilityId = INITIAL_LONG_VALUE;

	private String[] selectedFacilityPollutionProblemsIds = {};
	
	private String availPollutionProblemType = "";
	
	private String hiddenSelectedPolluProbType = "";
	
	private String pollutionGroupType = "";
	
	private String selectedPolluProbType = "";

	private String commaDilimitedPollutionProblemIds = "";
	
	/**
	 * @return the commaDilimitedPollutionProblemIds
	 */
	public String getCommaDilimitedPollutionProblemIds() {
		return commaDilimitedPollutionProblemIds;
	}

	/**
	 * @param commaDilimitedPollutionProblemIds the commaDilimitedPollutionProblemIds to set
	 */
	public void setCommaDilimitedPollutionProblemIds(
			String commaDilimitedPollutionProblemIds) {
		this.commaDilimitedPollutionProblemIds = commaDilimitedPollutionProblemIds;
	}

	/**
	 * @return the selectedPolluProbType
	 */
	public String getSelectedPolluProbType() {
		return selectedPolluProbType;
	}

	/**
	 * @param selectedPolluProbType the selectedPolluProbType to set
	 */
	public void setSelectedPolluProbType(String selectedPolluProbType) {
		this.selectedPolluProbType = selectedPolluProbType;
	}

	/**
	 * @return the pollutionGroupType
	 */
	public String getPollutionGroupType() {
		return pollutionGroupType;
	}

	/**
	 * @param pollutionGroupType the pollutionGroupType to set
	 */
	public void setPollutionGroupType(String pollutionGroupType) {
		this.pollutionGroupType = pollutionGroupType;
	}

	/**
	 * @return the selectedFacilityPollutionProblemsIds
	 */
	public String[] getSelectedFacilityPollutionProblemsIds() {
		return selectedFacilityPollutionProblemsIds;
	}

	/**
	 * @param selectedFacilityPollutionProblemsIds the selectedFacilityPollutionProblemsIds to set
	 */
	public void setSelectedFacilityPollutionProblemsIds(
			String[] selectedFacilityPollutionProblemsIds) {
		this.selectedFacilityPollutionProblemsIds = selectedFacilityPollutionProblemsIds;
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
	 * @return the pollutionAct
	 */
	public String getPollutionAct() {
		return pollutionAct;
	}

	/**
	 * @param pollutionAct the pollutionAct to set
	 */
	public void setPollutionAct(String pollutionAct) {
		this.pollutionAct = pollutionAct;
	}

	/**
	 * @return the pollutionFacilityId
	 */
	public long getPollutionFacilityId() {
		return pollutionFacilityId;
	}

	/**
	 * @param pollutionFacilityId the pollutionFacilityId to set
	 */
	public void setPollutionFacilityId(long pollutionFacilityId) {
		this.pollutionFacilityId = pollutionFacilityId;
	}

	/**
	 * @return the availPollutionProblemType
	 */
	public String getAvailPollutionProblemType() {
		return availPollutionProblemType;
	}

	/**
	 * @param availPollutionProblemType the availPollutionProblemType to set
	 */
	public void setAvailPollutionProblemType(String availPollutionProblemType) {
		this.availPollutionProblemType = availPollutionProblemType;
	}

	/**
	 * @return the hiddenSelectedPolluProbType
	 */
	public String getHiddenSelectedPolluProbType() {
		return hiddenSelectedPolluProbType;
	}

	/**
	 * @param hiddenSelectedPolluProbType the hiddenSelectedPolluProbType to set
	 */
	public void setHiddenSelectedPolluProbType(String hiddenSelectedPolluProbType) {
		this.hiddenSelectedPolluProbType = hiddenSelectedPolluProbType;
	}

}
