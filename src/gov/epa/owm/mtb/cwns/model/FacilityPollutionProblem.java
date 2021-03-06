package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * FacilityPollutionProblem generated by hbm2java
 */
public class FacilityPollutionProblem implements java.io.Serializable {

	// Fields    

	private FacilityPollutionProblemId id;

	private PollutionProblemRef pollutionProblemRef;

	private Facility facility;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public FacilityPollutionProblem() {
	}

	/** full constructor */
	public FacilityPollutionProblem(FacilityPollutionProblemId id,
			PollutionProblemRef pollutionProblemRef, Facility facility,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.id = id;
		this.pollutionProblemRef = pollutionProblemRef;
		this.facility = facility;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public FacilityPollutionProblemId getId() {
		return this.id;
	}

	public void setId(FacilityPollutionProblemId id) {
		this.id = id;
	}

	public PollutionProblemRef getPollutionProblemRef() {
		return this.pollutionProblemRef;
	}

	public void setPollutionProblemRef(PollutionProblemRef pollutionProblemRef) {
		this.pollutionProblemRef = pollutionProblemRef;
	}

	public Facility getFacility() {
		return this.facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
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
