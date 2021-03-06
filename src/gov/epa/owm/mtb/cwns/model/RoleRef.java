package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * RoleRef generated by hbm2java
 */
public class RoleRef implements java.io.Serializable {

	// Fields    

	private RoleRefId id;

	private LocationTypeRef locationTypeRef;

	private LocationRef locationRef;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set cwnsUserLocations = new HashSet(0);

	// Constructors

	/** default constructor */
	public RoleRef() {
	}

	/** minimal constructor */
	public RoleRef(RoleRefId id, LocationTypeRef locationTypeRef,
			LocationRef locationRef, String lastUpdateUserid, Date lastUpdateTs) {
		this.id = id;
		this.locationTypeRef = locationTypeRef;
		this.locationRef = locationRef;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public RoleRef(RoleRefId id, LocationTypeRef locationTypeRef,
			LocationRef locationRef, String lastUpdateUserid,
			Date lastUpdateTs, Set cwnsUserLocations) {
		this.id = id;
		this.locationTypeRef = locationTypeRef;
		this.locationRef = locationRef;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.cwnsUserLocations = cwnsUserLocations;
	}

	// Property accessors
	public RoleRefId getId() {
		return this.id;
	}

	public void setId(RoleRefId id) {
		this.id = id;
	}

	public LocationTypeRef getLocationTypeRef() {
		return this.locationTypeRef;
	}

	public void setLocationTypeRef(LocationTypeRef locationTypeRef) {
		this.locationTypeRef = locationTypeRef;
	}

	public LocationRef getLocationRef() {
		return this.locationRef;
	}

	public void setLocationRef(LocationRef locationRef) {
		this.locationRef = locationRef;
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

	public Set getCwnsUserLocations() {
		return this.cwnsUserLocations;
	}

	public void setCwnsUserLocations(Set cwnsUserLocations) {
		this.cwnsUserLocations = cwnsUserLocations;
	}

}
