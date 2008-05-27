package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Permit generated by hbm2java
 */
public class Permit implements java.io.Serializable {

	// Fields    

	private long permitId;

	private PermitTypeRef permitTypeRef;

	private EfPermit efPermit;

	private String permitNumber;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set facilityPermits = new HashSet(0);

	// Constructors

	/** default constructor */
	public Permit() {
	}

	/** minimal constructor */
	public Permit(long permitId, String permitNumber, String lastUpdateUserid,
			Date lastUpdateTs) {
		this.permitId = permitId;
		this.permitNumber = permitNumber;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public Permit(long permitId, PermitTypeRef permitTypeRef,
			EfPermit efPermit, String permitNumber, String lastUpdateUserid,
			Date lastUpdateTs, Set facilityPermits) {
		this.permitId = permitId;
		this.permitTypeRef = permitTypeRef;
		this.efPermit = efPermit;
		this.permitNumber = permitNumber;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.facilityPermits = facilityPermits;
	}

	// Property accessors
	public long getPermitId() {
		return this.permitId;
	}

	public void setPermitId(long permitId) {
		this.permitId = permitId;
	}

	public PermitTypeRef getPermitTypeRef() {
		return this.permitTypeRef;
	}

	public void setPermitTypeRef(PermitTypeRef permitTypeRef) {
		this.permitTypeRef = permitTypeRef;
	}

	public EfPermit getEfPermit() {
		return this.efPermit;
	}

	public void setEfPermit(EfPermit efPermit) {
		this.efPermit = efPermit;
	}

	public String getPermitNumber() {
		return this.permitNumber;
	}

	public void setPermitNumber(String permitNumber) {
		this.permitNumber = permitNumber;
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

	public Set getFacilityPermits() {
		return this.facilityPermits;
	}

	public void setFacilityPermits(Set facilityPermits) {
		this.facilityPermits = facilityPermits;
	}

}