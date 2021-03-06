package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * FacilityTypeRuleRefId generated by hbm2java
 */
public class FacilityTypeRuleRefId implements java.io.Serializable {

	// Fields

	private long facilityTypeId1;

	private long facilityTypeId2;

	// Constructors

	/** default constructor */
	public FacilityTypeRuleRefId() {
	}

	/** full constructor */
	public FacilityTypeRuleRefId(long facilityTypeId1, long facilityTypeId2) {
		this.facilityTypeId1 = facilityTypeId1;
		this.facilityTypeId2 = facilityTypeId2;
	}

	// Property accessors
	public long getFacilityTypeId1() {
		return this.facilityTypeId1;
	}

	public void setFacilityTypeId1(long facilityTypeId1) {
		this.facilityTypeId1 = facilityTypeId1;
	}

	public long getFacilityTypeId2() {
		return this.facilityTypeId2;
	}

	public void setFacilityTypeId2(long facilityTypeId2) {
		this.facilityTypeId2 = facilityTypeId2;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FacilityTypeRuleRefId))
			return false;
		FacilityTypeRuleRefId castOther = (FacilityTypeRuleRefId) other;

		return (this.getFacilityTypeId1() == castOther.getFacilityTypeId1())
				&& (this.getFacilityTypeId2() == castOther
						.getFacilityTypeId2());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getFacilityTypeId1();
		result = 37 * result + (int) this.getFacilityTypeId2();
		return result;
	}

}
