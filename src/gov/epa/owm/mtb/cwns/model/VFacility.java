package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * VFacility generated by hbm2java
 */
public class VFacility implements java.io.Serializable {

	// Fields    

	private VFacilityId id;

	// Constructors

	/** default constructor */
	public VFacility() {
	}

	/** full constructor */
	public VFacility(VFacilityId id) {
		this.id = id;
	}

	// Property accessors
	public VFacilityId getId() {
		return this.id;
	}

	public void setId(VFacilityId id) {
		this.id = id;
	}

}
