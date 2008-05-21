package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * GeographicAreaTypeRef generated by hbm2java
 */
public class GeographicAreaTypeRef implements java.io.Serializable {

	// Fields    

	private long geographicAreaTypeId;

	private String name;

	private char facilityAreaFlag;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set geographicAreas = new HashSet(0);

	// Constructors

	/** default constructor */
	public GeographicAreaTypeRef() {
	}

	/** minimal constructor */
	public GeographicAreaTypeRef(long geographicAreaTypeId, String name,
			char facilityAreaFlag, String lastUpdateUserid, Date lastUpdateTs) {
		this.geographicAreaTypeId = geographicAreaTypeId;
		this.name = name;
		this.facilityAreaFlag = facilityAreaFlag;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public GeographicAreaTypeRef(long geographicAreaTypeId, String name,
			char facilityAreaFlag, String lastUpdateUserid, Date lastUpdateTs,
			Set geographicAreas) {
		this.geographicAreaTypeId = geographicAreaTypeId;
		this.name = name;
		this.facilityAreaFlag = facilityAreaFlag;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.geographicAreas = geographicAreas;
	}

	// Property accessors
	public long getGeographicAreaTypeId() {
		return this.geographicAreaTypeId;
	}

	public void setGeographicAreaTypeId(long geographicAreaTypeId) {
		this.geographicAreaTypeId = geographicAreaTypeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getFacilityAreaFlag() {
		return this.facilityAreaFlag;
	}

	public void setFacilityAreaFlag(char facilityAreaFlag) {
		this.facilityAreaFlag = facilityAreaFlag;
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

	public Set getGeographicAreas() {
		return this.geographicAreas;
	}

	public void setGeographicAreas(Set geographicAreas) {
		this.geographicAreas = geographicAreas;
	}

}
