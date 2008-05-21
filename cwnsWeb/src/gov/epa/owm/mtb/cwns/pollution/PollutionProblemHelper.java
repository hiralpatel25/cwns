package gov.epa.owm.mtb.cwns.pollution;

public class PollutionProblemHelper {

	long pollutionProblemId = -999;
	
	String pollutionProblemName = "";

	/**
	 * @return the pollutionProblemId
	 */
	public long getPollutionProblemId() {
		return pollutionProblemId;
	}

	/**
	 * @param pollutionProblemId the pollutionProblemId to set
	 */
	public void setPollutionProblemId(long pollutionProblemId) {
		this.pollutionProblemId = pollutionProblemId;
	}

	/**
	 * @return the pollutionProblemName
	 */
	public String getPollutionProblemName() {
		return pollutionProblemName;
	}

	/**
	 * @param pollutionProblemName the pollutionProblemName to set
	 */
	public void setPollutionProblemName(String pollutionProblemName) {
		this.pollutionProblemName = pollutionProblemName;
	}

}