package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * CwnsUserSetting generated by hbm2java
 */
public class CwnsUserSetting implements java.io.Serializable {

	// Fields    

	private long cwnsUserSettingId;

	private String userAndRole;

	private String listType;

	private long facilityId;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public CwnsUserSetting() {
	}

	/** full constructor */
	public CwnsUserSetting(long cwnsUserSettingId, String userAndRole,
			String listType, long facilityId, String lastUpdateUserid,
			Date lastUpdateTs) {
		this.cwnsUserSettingId = cwnsUserSettingId;
		this.userAndRole = userAndRole;
		this.listType = listType;
		this.facilityId = facilityId;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public long getCwnsUserSettingId() {
		return this.cwnsUserSettingId;
	}

	public void setCwnsUserSettingId(long cwnsUserSettingId) {
		this.cwnsUserSettingId = cwnsUserSettingId;
	}

	public String getUserAndRole() {
		return this.userAndRole;
	}

	public void setUserAndRole(String userAndRole) {
		this.userAndRole = userAndRole;
	}

	public String getListType() {
		return this.listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public long getFacilityId() {
		return this.facilityId;
	}

	public void setFacilityId(long facilityId) {
		this.facilityId = facilityId;
	}

	public String getLastUpdateUserid() {
		return this.lastUpdateUserid;
	}

	public void setLastUpdateUserid(String lastUpdateUserid) {
		this.lastUpdateUserid = lastUpdateUserid;
	}

	public Date getLastUpdateTs() {
		return this.lastUpdateTs;
	}

	public void setLastUpdateTs(Date lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}

}
