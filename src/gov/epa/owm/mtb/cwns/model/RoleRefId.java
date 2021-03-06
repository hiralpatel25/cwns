package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * RoleRefId generated by hbm2java
 */
public class RoleRefId implements java.io.Serializable {

	// Fields

	private String locationTypeId;

	private String locationId;

	// Constructors

	/** default constructor */
	public RoleRefId() {
	}

	/** full constructor */
	public RoleRefId(String locationTypeId, String locationId) {
		this.locationTypeId = locationTypeId;
		this.locationId = locationId;
	}

	// Property accessors
	public String getLocationTypeId() {
		return this.locationTypeId;
	}

	public void setLocationTypeId(String locationTypeId) {
		this.locationTypeId = locationTypeId;
	}

	public String getLocationId() {
		return this.locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RoleRefId))
			return false;
		RoleRefId castOther = (RoleRefId) other;

		return ((this.getLocationTypeId() == castOther.getLocationTypeId()) || (this
				.getLocationTypeId() != null
				&& castOther.getLocationTypeId() != null && this
				.getLocationTypeId().equals(castOther.getLocationTypeId())))
				&& ((this.getLocationId() == castOther.getLocationId()) || (this
						.getLocationId() != null
						&& castOther.getLocationId() != null && this
						.getLocationId().equals(castOther.getLocationId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getLocationTypeId() == null ? 0 : this
						.getLocationTypeId().hashCode());
		result = 37
				* result
				+ (getLocationId() == null ? 0 : this.getLocationId()
						.hashCode());
		return result;
	}

}
