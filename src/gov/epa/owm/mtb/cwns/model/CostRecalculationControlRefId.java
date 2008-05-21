package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * CostRecalculationControlRefId generated by hbm2java
 */
public class CostRecalculationControlRefId implements java.io.Serializable {

	// Fields    

	private String locationId;

	private String recalculationType;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public CostRecalculationControlRefId() {
	}

	/** full constructor */
	public CostRecalculationControlRefId(String locationId,
			String recalculationType, Date lastUpdateTs) {
		this.locationId = locationId;
		this.recalculationType = recalculationType;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public String getLocationId() {
		return this.locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getRecalculationType() {
		return this.recalculationType;
	}

	public void setRecalculationType(String recalculationType) {
		this.recalculationType = recalculationType;
	}

	public Date getLastUpdateTs() {
		return this.lastUpdateTs;
	}

	public void setLastUpdateTs(Date lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CostRecalculationControlRefId))
			return false;
		CostRecalculationControlRefId castOther = (CostRecalculationControlRefId) other;

		return ((this.getLocationId() == castOther.getLocationId()) || (this
				.getLocationId() != null
				&& castOther.getLocationId() != null && this.getLocationId()
				.equals(castOther.getLocationId())))
				&& ((this.getRecalculationType() == castOther
						.getRecalculationType()) || (this
						.getRecalculationType() != null
						&& castOther.getRecalculationType() != null && this
						.getRecalculationType().equals(
								castOther.getRecalculationType())))
				&& ((this.getLastUpdateTs() == castOther.getLastUpdateTs()) || (this
						.getLastUpdateTs() != null
						&& castOther.getLastUpdateTs() != null && this
						.getLastUpdateTs().equals(castOther.getLastUpdateTs())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getLocationId() == null ? 0 : this.getLocationId()
						.hashCode());
		result = 37
				* result
				+ (getRecalculationType() == null ? 0 : this
						.getRecalculationType().hashCode());
		result = 37
				* result
				+ (getLastUpdateTs() == null ? 0 : this.getLastUpdateTs()
						.hashCode());
		return result;
	}

}
