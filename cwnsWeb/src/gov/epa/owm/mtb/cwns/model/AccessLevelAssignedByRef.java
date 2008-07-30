package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * AccessLevelAssignedByRef generated by hbm2java
 */
public class AccessLevelAssignedByRef implements java.io.Serializable {

	// Fields    

	private AccessLevelAssignedByRefId id;

	private LocationTypeRef locationTypeRef;

	private AccessLevelRef accessLevelRef;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public AccessLevelAssignedByRef() {
	}

	/** full constructor */
	public AccessLevelAssignedByRef(AccessLevelAssignedByRefId id,
			LocationTypeRef locationTypeRef, AccessLevelRef accessLevelRef,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.id = id;
		this.locationTypeRef = locationTypeRef;
		this.accessLevelRef = accessLevelRef;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public AccessLevelAssignedByRefId getId() {
		return this.id;
	}

	public void setId(AccessLevelAssignedByRefId id) {
		this.id = id;
	}

	public LocationTypeRef getLocationTypeRef() {
		return this.locationTypeRef;
	}

	public void setLocationTypeRef(LocationTypeRef locationTypeRef) {
		this.locationTypeRef = locationTypeRef;
	}

	public AccessLevelRef getAccessLevelRef() {
		return this.accessLevelRef;
	}

	public void setAccessLevelRef(AccessLevelRef accessLevelRef) {
		this.accessLevelRef = accessLevelRef;
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
