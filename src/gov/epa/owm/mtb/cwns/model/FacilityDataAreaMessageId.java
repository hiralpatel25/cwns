package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * FacilityDataAreaMessageId generated by hbm2java
 */
public class FacilityDataAreaMessageId implements java.io.Serializable {

	// Fields

	private long facilityId;

	private long dataAreaId;

	private String errorMessageKey;

	// Constructors

	/** default constructor */
	public FacilityDataAreaMessageId() {
	}

	/** full constructor */
	public FacilityDataAreaMessageId(long facilityId, long dataAreaId,
			String errorMessageKey) {
		this.facilityId = facilityId;
		this.dataAreaId = dataAreaId;
		this.errorMessageKey = errorMessageKey;
	}

	// Property accessors
	public long getFacilityId() {
		return this.facilityId;
	}

	public void setFacilityId(long facilityId) {
		this.facilityId = facilityId;
	}

	public long getDataAreaId() {
		return this.dataAreaId;
	}

	public void setDataAreaId(long dataAreaId) {
		this.dataAreaId = dataAreaId;
	}

	public String getErrorMessageKey() {
		return this.errorMessageKey;
	}

	public void setErrorMessageKey(String errorMessageKey) {
		this.errorMessageKey = errorMessageKey;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FacilityDataAreaMessageId))
			return false;
		FacilityDataAreaMessageId castOther = (FacilityDataAreaMessageId) other;

		return (this.getFacilityId() == castOther.getFacilityId())
				&& (this.getDataAreaId() == castOther.getDataAreaId())
				&& ((this.getErrorMessageKey() == castOther
						.getErrorMessageKey()) || (this.getErrorMessageKey() != null
						&& castOther.getErrorMessageKey() != null && this
						.getErrorMessageKey().equals(
								castOther.getErrorMessageKey())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getFacilityId();
		result = 37 * result + (int) this.getDataAreaId();
		result = 37
				* result
				+ (getErrorMessageKey() == null ? 0 : this.getErrorMessageKey()
						.hashCode());
		return result;
	}

}
