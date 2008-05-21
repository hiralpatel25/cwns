package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * CwnsUserLocationFacility generated by hbm2java
 */
public class CwnsUserLocationFacility implements java.io.Serializable {

	// Fields    

	private CwnsUserLocationFacilityId id;

	private CwnsUserLocation cwnsUserLocation;

	private Facility facility;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public CwnsUserLocationFacility() {
	}

	/** full constructor */
	public CwnsUserLocationFacility(CwnsUserLocationFacilityId id,
			CwnsUserLocation cwnsUserLocation, Facility facility,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.id = id;
		this.cwnsUserLocation = cwnsUserLocation;
		this.facility = facility;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public CwnsUserLocationFacilityId getId() {
		return this.id;
	}

	public void setId(CwnsUserLocationFacilityId id) {
		this.id = id;
	}

	public CwnsUserLocation getCwnsUserLocation() {
		return this.cwnsUserLocation;
	}

	public void setCwnsUserLocation(CwnsUserLocation cwnsUserLocation) {
		this.cwnsUserLocation = cwnsUserLocation;
	}

	public Facility getFacility() {
		return this.facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
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
