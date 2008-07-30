package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * StateRef generated by hbm2java
 */
public class StateRef implements java.io.Serializable {

	// Fields    

	private String stateId;

	private String name;

	private byte epaRegionCode;

	private char federalReviewReqstFlag;

	private String locationId;

	private BigDecimal resPopulationPerUnit;

	private BigDecimal nonresPopulationPerUnit;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set rainfalls = new HashSet(0);

	// Constructors

	/** default constructor */
	public StateRef() {
	}

	/** minimal constructor */
	public StateRef(String stateId, String name, byte epaRegionCode,
			char federalReviewReqstFlag, String locationId,
			BigDecimal resPopulationPerUnit,
			BigDecimal nonresPopulationPerUnit, String lastUpdateUserid,
			Date lastUpdateTs) {
		this.stateId = stateId;
		this.name = name;
		this.epaRegionCode = epaRegionCode;
		this.federalReviewReqstFlag = federalReviewReqstFlag;
		this.locationId = locationId;
		this.resPopulationPerUnit = resPopulationPerUnit;
		this.nonresPopulationPerUnit = nonresPopulationPerUnit;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public StateRef(String stateId, String name, byte epaRegionCode,
			char federalReviewReqstFlag, String locationId,
			BigDecimal resPopulationPerUnit,
			BigDecimal nonresPopulationPerUnit, String lastUpdateUserid,
			Date lastUpdateTs, Set rainfalls) {
		this.stateId = stateId;
		this.name = name;
		this.epaRegionCode = epaRegionCode;
		this.federalReviewReqstFlag = federalReviewReqstFlag;
		this.locationId = locationId;
		this.resPopulationPerUnit = resPopulationPerUnit;
		this.nonresPopulationPerUnit = nonresPopulationPerUnit;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.rainfalls = rainfalls;
	}

	// Property accessors
	public String getStateId() {
		return this.stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getEpaRegionCode() {
		return this.epaRegionCode;
	}

	public void setEpaRegionCode(byte epaRegionCode) {
		this.epaRegionCode = epaRegionCode;
	}

	public char getFederalReviewReqstFlag() {
		return this.federalReviewReqstFlag;
	}

	public void setFederalReviewReqstFlag(char federalReviewReqstFlag) {
		this.federalReviewReqstFlag = federalReviewReqstFlag;
	}

	public String getLocationId() {
		return this.locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public BigDecimal getResPopulationPerUnit() {
		return this.resPopulationPerUnit;
	}

	public void setResPopulationPerUnit(BigDecimal resPopulationPerUnit) {
		this.resPopulationPerUnit = resPopulationPerUnit;
	}

	public BigDecimal getNonresPopulationPerUnit() {
		return this.nonresPopulationPerUnit;
	}

	public void setNonresPopulationPerUnit(BigDecimal nonresPopulationPerUnit) {
		this.nonresPopulationPerUnit = nonresPopulationPerUnit;
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

	public Set getRainfalls() {
		return this.rainfalls;
	}

	public void setRainfalls(Set rainfalls) {
		this.rainfalls = rainfalls;
	}

}
