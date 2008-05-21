package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * ReviewComment generated by hbm2java
 */
public class ReviewComment implements java.io.Serializable {

	// Fields    

	private long reviewCommentId;

	private Facility facility;

	private String description;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public ReviewComment() {
	}

	/** minimal constructor */
	public ReviewComment(long reviewCommentId, String description,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.reviewCommentId = reviewCommentId;
		this.description = description;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public ReviewComment(long reviewCommentId, Facility facility,
			String description, String lastUpdateUserid, Date lastUpdateTs) {
		this.reviewCommentId = reviewCommentId;
		this.facility = facility;
		this.description = description;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public long getReviewCommentId() {
		return this.reviewCommentId;
	}

	public void setReviewCommentId(long reviewCommentId) {
		this.reviewCommentId = reviewCommentId;
	}

	public Facility getFacility() {
		return this.facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLastUpdateUserid() {
		return this.lastUpdateUserid;
	}

	public void setLastUpdateUserid(String lastUpdateUserid) {
		this.lastUpdateUserid = lastUpdateUserid;
	}

	public Date getLastUpdateTs() {
		return this.lastUpdateTs;
	}

	public void setLastUpdateTs(Date lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}

}
