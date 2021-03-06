package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * HorizontalCoordntDatumRef generated by hbm2java
 */
public class HorizontalCoordntDatumRef implements java.io.Serializable {

	// Fields    

	private long horizontalCoordntDatumId;

	private String code;

	private String name;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Integer geodeticCode;

	private Set efPermits = new HashSet(0);

	private Set absoluteLocationPoints = new HashSet(0);

	// Constructors

	/** default constructor */
	public HorizontalCoordntDatumRef() {
	}

	/** minimal constructor */
	public HorizontalCoordntDatumRef(long horizontalCoordntDatumId,
			String code, String name, String lastUpdateUserid, Date lastUpdateTs) {
		this.horizontalCoordntDatumId = horizontalCoordntDatumId;
		this.code = code;
		this.name = name;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public HorizontalCoordntDatumRef(long horizontalCoordntDatumId,
			String code, String name, String lastUpdateUserid,
			Date lastUpdateTs, Integer geodeticCode, Set efPermits,
			Set absoluteLocationPoints) {
		this.horizontalCoordntDatumId = horizontalCoordntDatumId;
		this.code = code;
		this.name = name;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.geodeticCode = geodeticCode;
		this.efPermits = efPermits;
		this.absoluteLocationPoints = absoluteLocationPoints;
	}

	// Property accessors
	public long getHorizontalCoordntDatumId() {
		return this.horizontalCoordntDatumId;
	}

	public void setHorizontalCoordntDatumId(long horizontalCoordntDatumId) {
		this.horizontalCoordntDatumId = horizontalCoordntDatumId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Integer getGeodeticCode() {
		return this.geodeticCode;
	}

	public void setGeodeticCode(Integer geodeticCode) {
		this.geodeticCode = geodeticCode;
	}

	public Set getEfPermits() {
		return this.efPermits;
	}

	public void setEfPermits(Set efPermits) {
		this.efPermits = efPermits;
	}

	public Set getAbsoluteLocationPoints() {
		return this.absoluteLocationPoints;
	}

	public void setAbsoluteLocationPoints(Set absoluteLocationPoints) {
		this.absoluteLocationPoints = absoluteLocationPoints;
	}

}
