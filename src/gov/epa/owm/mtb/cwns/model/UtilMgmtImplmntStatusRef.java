package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * UtilMgmtImplmntStatusRef generated by hbm2java
 */
public class UtilMgmtImplmntStatusRef implements java.io.Serializable {

	// Fields    

	private long utilMgmtImplmntStatusId;

	private String name;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set facilityUtilMgmtPractices = new HashSet(0);

	// Constructors

	/** default constructor */
	public UtilMgmtImplmntStatusRef() {
	}

	/** minimal constructor */
	public UtilMgmtImplmntStatusRef(long utilMgmtImplmntStatusId, String name,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.utilMgmtImplmntStatusId = utilMgmtImplmntStatusId;
		this.name = name;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public UtilMgmtImplmntStatusRef(long utilMgmtImplmntStatusId, String name,
			String lastUpdateUserid, Date lastUpdateTs,
			Set facilityUtilMgmtPractices) {
		this.utilMgmtImplmntStatusId = utilMgmtImplmntStatusId;
		this.name = name;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.facilityUtilMgmtPractices = facilityUtilMgmtPractices;
	}

	// Property accessors
	public long getUtilMgmtImplmntStatusId() {
		return this.utilMgmtImplmntStatusId;
	}

	public void setUtilMgmtImplmntStatusId(long utilMgmtImplmntStatusId) {
		this.utilMgmtImplmntStatusId = utilMgmtImplmntStatusId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set getFacilityUtilMgmtPractices() {
		return this.facilityUtilMgmtPractices;
	}

	public void setFacilityUtilMgmtPractices(Set facilityUtilMgmtPractices) {
		this.facilityUtilMgmtPractices = facilityUtilMgmtPractices;
	}

}
