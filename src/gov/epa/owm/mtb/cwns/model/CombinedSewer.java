package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * CombinedSewer generated by hbm2java
 */
public class CombinedSewer implements java.io.Serializable {

	// Fields

	private long facilityId;

	private CombinedSewerStatusRef combinedSewerStatusRef;

	private Facility facility;

	private Integer docAreaSquareMilesMsr;

	private Integer docPopulationCount;

	private Integer ccAreaSquareMilesMsr;

	private Integer ccPopulationCount;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public CombinedSewer() {
	}

	/** minimal constructor */
	public CombinedSewer(long facilityId, Facility facility,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.facilityId = facilityId;
		this.facility = facility;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public CombinedSewer(long facilityId,
			CombinedSewerStatusRef combinedSewerStatusRef, Facility facility,
			Integer docAreaSquareMilesMsr, Integer docPopulationCount,
			Integer ccAreaSquareMilesMsr, Integer ccPopulationCount,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.facilityId = facilityId;
		this.combinedSewerStatusRef = combinedSewerStatusRef;
		this.facility = facility;
		this.docAreaSquareMilesMsr = docAreaSquareMilesMsr;
		this.docPopulationCount = docPopulationCount;
		this.ccAreaSquareMilesMsr = ccAreaSquareMilesMsr;
		this.ccPopulationCount = ccPopulationCount;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public long getFacilityId() {
		return this.facilityId;
	}

	public void setFacilityId(long facilityId) {
		this.facilityId = facilityId;
	}

	public CombinedSewerStatusRef getCombinedSewerStatusRef() {
		return this.combinedSewerStatusRef;
	}

	public void setCombinedSewerStatusRef(
			CombinedSewerStatusRef combinedSewerStatusRef) {
		this.combinedSewerStatusRef = combinedSewerStatusRef;
	}

	public Facility getFacility() {
		return this.facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

	public Integer getDocAreaSquareMilesMsr() {
		return this.docAreaSquareMilesMsr;
	}

	public void setDocAreaSquareMilesMsr(Integer docAreaSquareMilesMsr) {
		this.docAreaSquareMilesMsr = docAreaSquareMilesMsr;
	}

	public Integer getDocPopulationCount() {
		return this.docPopulationCount;
	}

	public void setDocPopulationCount(Integer docPopulationCount) {
		this.docPopulationCount = docPopulationCount;
	}

	public Integer getCcAreaSquareMilesMsr() {
		return this.ccAreaSquareMilesMsr;
	}

	public void setCcAreaSquareMilesMsr(Integer ccAreaSquareMilesMsr) {
		this.ccAreaSquareMilesMsr = ccAreaSquareMilesMsr;
	}

	public Integer getCcPopulationCount() {
		return this.ccPopulationCount;
	}

	public void setCcPopulationCount(Integer ccPopulationCount) {
		this.ccPopulationCount = ccPopulationCount;
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
