package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.math.BigDecimal;
import java.util.Date;

/**
 * Rainfall generated by hbm2java
 */
public class Rainfall implements java.io.Serializable {

	// Fields    

	private long rainfallId;

	private StateRef stateRef;

	private Facility facility;

	private Character nationalDefualtFlag;

	private BigDecimal rain85CptMsr;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public Rainfall() {
	}

	/** minimal constructor */
	public Rainfall(long rainfallId, String lastUpdateUserid, Date lastUpdateTs) {
		this.rainfallId = rainfallId;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public Rainfall(long rainfallId, StateRef stateRef, Facility facility,
			Character nationalDefualtFlag, BigDecimal rain85CptMsr,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.rainfallId = rainfallId;
		this.stateRef = stateRef;
		this.facility = facility;
		this.nationalDefualtFlag = nationalDefualtFlag;
		this.rain85CptMsr = rain85CptMsr;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public long getRainfallId() {
		return this.rainfallId;
	}

	public void setRainfallId(long rainfallId) {
		this.rainfallId = rainfallId;
	}

	public StateRef getStateRef() {
		return this.stateRef;
	}

	public void setStateRef(StateRef stateRef) {
		this.stateRef = stateRef;
	}

	public Facility getFacility() {
		return this.facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

	public Character getNationalDefualtFlag() {
		return this.nationalDefualtFlag;
	}

	public void setNationalDefualtFlag(Character nationalDefualtFlag) {
		this.nationalDefualtFlag = nationalDefualtFlag;
	}

	public BigDecimal getRain85CptMsr() {
		return this.rain85CptMsr;
	}

	public void setRain85CptMsr(BigDecimal rain85CptMsr) {
		this.rain85CptMsr = rain85CptMsr;
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