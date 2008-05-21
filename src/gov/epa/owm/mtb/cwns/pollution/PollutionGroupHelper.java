package gov.epa.owm.mtb.cwns.pollution;

public class PollutionGroupHelper {

	long pollutionGroupId = -999;
	
	String pollutionGroupName = "";

	/**
	 * @return the pollutionGroupId
	 */
	public long getPollutionGroupId() {
		return pollutionGroupId;
	}

	/**
	 * @param pollutionGroupId the pollutionGroupId to set
	 */
	public void setPollutionGroupId(long pollutionGroupId) {
		this.pollutionGroupId = pollutionGroupId;
	}

	/**
	 * @return the pollutionGroupName
	 */
	public String getPollutionGroupName() {
		return pollutionGroupName;
	}

	/**
	 * @param pollutionGroupName the pollutionGroupName to set
	 */
	public void setPollutionGroupName(String pollutionGroupName) {
		this.pollutionGroupName = pollutionGroupName;
	}

}