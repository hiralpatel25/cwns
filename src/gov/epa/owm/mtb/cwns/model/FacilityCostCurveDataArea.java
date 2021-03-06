package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * FacilityCostCurveDataArea generated by hbm2java
 */
public class FacilityCostCurveDataArea implements java.io.Serializable {

	// Fields    

	private FacilityCostCurveDataAreaId id;

	private DataAreaRef dataAreaRef;

	private FacilityCostCurve facilityCostCurve;

	private char errorFlag;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public FacilityCostCurveDataArea() {
	}

	/** full constructor */
	public FacilityCostCurveDataArea(FacilityCostCurveDataAreaId id,
			DataAreaRef dataAreaRef, FacilityCostCurve facilityCostCurve,
			char errorFlag, String lastUpdateUserid, Date lastUpdateTs) {
		this.id = id;
		this.dataAreaRef = dataAreaRef;
		this.facilityCostCurve = facilityCostCurve;
		this.errorFlag = errorFlag;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public FacilityCostCurveDataAreaId getId() {
		return this.id;
	}

	public void setId(FacilityCostCurveDataAreaId id) {
		this.id = id;
	}

	public DataAreaRef getDataAreaRef() {
		return this.dataAreaRef;
	}

	public void setDataAreaRef(DataAreaRef dataAreaRef) {
		this.dataAreaRef = dataAreaRef;
	}

	public FacilityCostCurve getFacilityCostCurve() {
		return this.facilityCostCurve;
	}

	public void setFacilityCostCurve(FacilityCostCurve facilityCostCurve) {
		this.facilityCostCurve = facilityCostCurve;
	}

	public char getErrorFlag() {
		return this.errorFlag;
	}

	public void setErrorFlag(char errorFlag) {
		this.errorFlag = errorFlag;
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
