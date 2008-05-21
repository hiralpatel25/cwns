package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * UnitProcessRef generated by hbm2java
 */
public class UnitProcessRef implements java.io.Serializable {

	// Fields    

	private long unitProcessId;

	private String name;

	private Date lastUpdateTs;

	private String lastUpdateUserid;

	private Set treatmentTypeUnitprocessRefs = new HashSet(0);

	// Constructors

	/** default constructor */
	public UnitProcessRef() {
	}

	/** minimal constructor */
	public UnitProcessRef(long unitProcessId, String name, Date lastUpdateTs,
			String lastUpdateUserid) {
		this.unitProcessId = unitProcessId;
		this.name = name;
		this.lastUpdateTs = lastUpdateTs;
		this.lastUpdateUserid = lastUpdateUserid;
	}

	/** full constructor */
	public UnitProcessRef(long unitProcessId, String name, Date lastUpdateTs,
			String lastUpdateUserid, Set treatmentTypeUnitprocessRefs) {
		this.unitProcessId = unitProcessId;
		this.name = name;
		this.lastUpdateTs = lastUpdateTs;
		this.lastUpdateUserid = lastUpdateUserid;
		this.treatmentTypeUnitprocessRefs = treatmentTypeUnitprocessRefs;
	}

	// Property accessors
	public long getUnitProcessId() {
		return this.unitProcessId;
	}

	public void setUnitProcessId(long unitProcessId) {
		this.unitProcessId = unitProcessId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastUpdateTs() {
		return this.lastUpdateTs;
	}

	public void setLastUpdateTs(Date lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}

	public String getLastUpdateUserid() {
		return this.lastUpdateUserid;
	}

	public void setLastUpdateUserid(String lastUpdateUserid) {
		this.lastUpdateUserid = lastUpdateUserid;
	}

	public Set getTreatmentTypeUnitprocessRefs() {
		return this.treatmentTypeUnitprocessRefs;
	}

	public void setTreatmentTypeUnitprocessRefs(Set treatmentTypeUnitprocessRefs) {
		this.treatmentTypeUnitprocessRefs = treatmentTypeUnitprocessRefs;
	}

}
