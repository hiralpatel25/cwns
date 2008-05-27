package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * GeographicAreaWatershedId generated by hbm2java
 */
public class GeographicAreaWatershedId implements java.io.Serializable {

	// Fields

	private long geographicAreaId;

	private String watershedId;

	// Constructors

	/** default constructor */
	public GeographicAreaWatershedId() {
	}

	/** full constructor */
	public GeographicAreaWatershedId(long geographicAreaId, String watershedId) {
		this.geographicAreaId = geographicAreaId;
		this.watershedId = watershedId;
	}

	// Property accessors
	public long getGeographicAreaId() {
		return this.geographicAreaId;
	}

	public void setGeographicAreaId(long geographicAreaId) {
		this.geographicAreaId = geographicAreaId;
	}

	public String getWatershedId() {
		return this.watershedId;
	}

	public void setWatershedId(String watershedId) {
		this.watershedId = watershedId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof GeographicAreaWatershedId))
			return false;
		GeographicAreaWatershedId castOther = (GeographicAreaWatershedId) other;

		return (this.getGeographicAreaId() == castOther
				.getGeographicAreaId())
				&& (this.getWatershedId() == castOther.getWatershedId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getGeographicAreaId();
		result = 37 * result + getWatershedId() == null ? 0 : this.getWatershedId().hashCode();
		return result;
	}

}