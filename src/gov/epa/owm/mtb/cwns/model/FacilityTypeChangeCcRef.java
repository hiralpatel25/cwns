package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * FacilityTypeChangeCcRef generated by hbm2java
 */
public class FacilityTypeChangeCcRef implements java.io.Serializable {

	// Fields    

	private FacilityTypeChangeCcRefId id;

	private FacilityTypeChangeRef facilityTypeChangeRef;

	private CostCurveRef costCurveRef;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public FacilityTypeChangeCcRef() {
	}

	/** full constructor */
	public FacilityTypeChangeCcRef(FacilityTypeChangeCcRefId id,
			FacilityTypeChangeRef facilityTypeChangeRef,
			CostCurveRef costCurveRef, String lastUpdateUserid,
			Date lastUpdateTs) {
		this.id = id;
		this.facilityTypeChangeRef = facilityTypeChangeRef;
		this.costCurveRef = costCurveRef;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public FacilityTypeChangeCcRefId getId() {
		return this.id;
	}

	public void setId(FacilityTypeChangeCcRefId id) {
		this.id = id;
	}

	public FacilityTypeChangeRef getFacilityTypeChangeRef() {
		return this.facilityTypeChangeRef;
	}

	public void setFacilityTypeChangeRef(
			FacilityTypeChangeRef facilityTypeChangeRef) {
		this.facilityTypeChangeRef = facilityTypeChangeRef;
	}

	public CostCurveRef getCostCurveRef() {
		return this.costCurveRef;
	}

	public void setCostCurveRef(CostCurveRef costCurveRef) {
		this.costCurveRef = costCurveRef;
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
