package gov.epa.owm.mtb.cwns.needs;

public class NeedsDocumentGroupTypeHelper {

	private long groupId = 0;
	
	private int groupIndex = 0;
	
	private String groupTypeId = "";
	
	private String groupTypeDesc = "";

	/**
	 * @return the groupId
	 */
	public long getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupIndex
	 */
	public int getGroupIndex() {
		return groupIndex;
	}

	/**
	 * @param groupIndex the groupIndex to set
	 */
	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}

	/**
	 * @return the groupTypeDesc
	 */
	public String getGroupTypeDesc() {
		return groupTypeDesc;
	}

	/**
	 * @param groupTypeDesc the groupTypeDesc to set
	 */
	public void setGroupTypeDesc(String groupTypeDesc) {
		this.groupTypeDesc = groupTypeDesc;
	}

	/**
	 * @return the groupTypeId
	 */
	public String getGroupTypeId() {
		return groupTypeId;
	}

	/**
	 * @param groupTypeId the groupTypeId to set
	 */
	public void setGroupTypeId(String groupTypeId) {
		this.groupTypeId = groupTypeId;
	}

}
