package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * WatershedRef generated by hbm2java
 */
public class WatershedRef implements java.io.Serializable {

	// Fields    

	private String watershedId;

	private String name;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set geographicAreaWatersheds = new HashSet(0);

	private Set watershedLocationRefs = new HashSet(0);

	// Constructors

	/** default constructor */
	public WatershedRef() {
	}

	/** minimal constructor */
	public WatershedRef(String watershedId, String name, String lastUpdateUserid,
			Date lastUpdateTs) {
		this.watershedId = watershedId;
		this.name = name;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public WatershedRef(String watershedId, String name, String lastUpdateUserid,
			Date lastUpdateTs, Set geographicAreaWatersheds,
			Set watershedLocationRefs) {
		this.watershedId = watershedId;
		this.name = name;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.geographicAreaWatersheds = geographicAreaWatersheds;
		this.watershedLocationRefs = watershedLocationRefs;
	}

	// Property accessors
	public String getWatershedId() {
		return this.watershedId;
	}

	public void setWatershedId(String watershedId) {
		this.watershedId = watershedId;
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

	public Set getGeographicAreaWatersheds() {
		return this.geographicAreaWatersheds;
	}

	public void setGeographicAreaWatersheds(Set geographicAreaWatersheds) {
		this.geographicAreaWatersheds = geographicAreaWatersheds;
	}

	public Set getWatershedLocationRefs() {
		return this.watershedLocationRefs;
	}

	public void setWatershedLocationRefs(Set watershedLocationRefs) {
		this.watershedLocationRefs = watershedLocationRefs;
	}

}
