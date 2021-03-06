package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * BoundaryLocationRef generated by hbm2java
 */
public class BoundaryLocationRef implements java.io.Serializable {

	// Fields    

	private BoundaryLocationRefId id;

	private BoundaryRef boundaryRef;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public BoundaryLocationRef() {
	}

	/** full constructor */
	public BoundaryLocationRef(BoundaryLocationRefId id,
			BoundaryRef boundaryRef, String lastUpdateUserid, Date lastUpdateTs) {
		this.id = id;
		this.boundaryRef = boundaryRef;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public BoundaryLocationRefId getId() {
		return this.id;
	}

	public void setId(BoundaryLocationRefId id) {
		this.id = id;
	}

	public BoundaryRef getBoundaryRef() {
		return this.boundaryRef;
	}

	public void setBoundaryRef(BoundaryRef boundaryRef) {
		this.boundaryRef = boundaryRef;
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
