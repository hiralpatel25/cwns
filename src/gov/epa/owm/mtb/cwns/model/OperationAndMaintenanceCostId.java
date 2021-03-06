package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * OperationAndMaintenanceCostId generated by hbm2java
 */
public class OperationAndMaintenanceCostId implements java.io.Serializable {

	// Fields

	private long facilityId;

	private long operAndMaintCategoryId;

	private short costYear;

	// Constructors

	/** default constructor */
	public OperationAndMaintenanceCostId() {
	}

	/** full constructor */
	public OperationAndMaintenanceCostId(long facilityId,
			long operAndMaintCategoryId, short costYear) {
		this.facilityId = facilityId;
		this.operAndMaintCategoryId = operAndMaintCategoryId;
		this.costYear = costYear;
	}

	// Property accessors
	public long getFacilityId() {
		return this.facilityId;
	}

	public void setFacilityId(long facilityId) {
		this.facilityId = facilityId;
	}

	public long getOperAndMaintCategoryId() {
		return this.operAndMaintCategoryId;
	}

	public void setOperAndMaintCategoryId(long operAndMaintCategoryId) {
		this.operAndMaintCategoryId = operAndMaintCategoryId;
	}

	public short getCostYear() {
		return this.costYear;
	}

	public void setCostYear(short costYear) {
		this.costYear = costYear;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OperationAndMaintenanceCostId))
			return false;
		OperationAndMaintenanceCostId castOther = (OperationAndMaintenanceCostId) other;

		return (this.getFacilityId() == castOther.getFacilityId())
				&& (this.getOperAndMaintCategoryId() == castOther
						.getOperAndMaintCategoryId())
				&& (this.getCostYear() == castOther.getCostYear());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getFacilityId();
		result = 37 * result + (int) this.getOperAndMaintCategoryId();
		result = 37 * result + this.getCostYear();
		return result;
	}

}
