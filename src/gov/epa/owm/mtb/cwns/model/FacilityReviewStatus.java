package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * FacilityReviewStatus generated by hbm2java
 */
public class FacilityReviewStatus implements java.io.Serializable {

	// Fields    

	private FacilityReviewStatusId id;

	private ReviewStatusRef reviewStatusRef;

	private Facility facility;

	private String lastUpdateUserid;

	// Constructors

	/** default constructor */
	public FacilityReviewStatus() {
	}

	/** full constructor */
	public FacilityReviewStatus(FacilityReviewStatusId id,
			ReviewStatusRef reviewStatusRef, Facility facility,
			String lastUpdateUserid) {
		this.id = id;
		this.reviewStatusRef = reviewStatusRef;
		this.facility = facility;
		this.lastUpdateUserid = lastUpdateUserid;
	}

	// Property accessors
	public FacilityReviewStatusId getId() {
		return this.id;
	}

	public void setId(FacilityReviewStatusId id) {
		this.id = id;
	}

	public ReviewStatusRef getReviewStatusRef() {
		return this.reviewStatusRef;
	}

	public void setReviewStatusRef(ReviewStatusRef reviewStatusRef) {
		this.reviewStatusRef = reviewStatusRef;
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

}
