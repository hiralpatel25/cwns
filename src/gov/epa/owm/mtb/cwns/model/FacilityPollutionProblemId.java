package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * FacilityPollutionProblemId generated by hbm2java
 */
public class FacilityPollutionProblemId implements java.io.Serializable {

	// Fields

	private long facilityId;

	private long pollutionProblemId;

	// Constructors

	/** default constructor */
	public FacilityPollutionProblemId() {
	}

	/** full constructor */
	public FacilityPollutionProblemId(long facilityId,
			long pollutionProblemId) {
		this.facilityId = facilityId;
		this.pollutionProblemId = pollutionProblemId;
	}

	// Property accessors
	public long getFacilityId() {
		return this.facilityId;
	}

	public void setFacilityId(long facilityId) {
		this.facilityId = facilityId;
	}

	public long getPollutionProblemId() {
		return this.pollutionProblemId;
	}

	public void setPollutionProblemId(long pollutionProblemId) {
		this.pollutionProblemId = pollutionProblemId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FacilityPollutionProblemId))
			return false;
		FacilityPollutionProblemId castOther = (FacilityPollutionProblemId) other;

		return (this.getFacilityId() == castOther.getFacilityId())
				&& (this.getPollutionProblemId() == castOther
						.getPollutionProblemId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getFacilityId();
		result = 37 * result + (int) this.getPollutionProblemId();
		return result;
	}

}
