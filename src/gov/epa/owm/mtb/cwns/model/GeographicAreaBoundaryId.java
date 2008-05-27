package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * GeographicAreaBoundaryId generated by hbm2java
 */
public class GeographicAreaBoundaryId implements java.io.Serializable {

	// Fields

	private long geographicAreaId;

	private long boundaryId;

	// Constructors

	/** default constructor */
	public GeographicAreaBoundaryId() {
	}

	/** full constructor */
	public GeographicAreaBoundaryId(long geographicAreaId, long boundaryId) {
		this.geographicAreaId = geographicAreaId;
		this.boundaryId = boundaryId;
	}

	// Property accessors
	public long getGeographicAreaId() {
		return this.geographicAreaId;
	}

	public void setGeographicAreaId(long geographicAreaId) {
		this.geographicAreaId = geographicAreaId;
	}

	public long getBoundaryId() {
		return this.boundaryId;
	}

	public void setBoundaryId(long boundaryId) {
		this.boundaryId = boundaryId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof GeographicAreaBoundaryId))
			return false;
		GeographicAreaBoundaryId castOther = (GeographicAreaBoundaryId) other;

		return (this.getGeographicAreaId() == castOther
				.getGeographicAreaId())
				&& (this.getBoundaryId() == castOther.getBoundaryId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getGeographicAreaId();
		result = 37 * result + (int) this.getBoundaryId();
		return result;
	}

}