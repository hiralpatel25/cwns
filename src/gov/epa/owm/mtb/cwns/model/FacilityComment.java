package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * FacilityComment generated by hbm2java
 */
public class FacilityComment implements java.io.Serializable {

	// Fields    

	private long facilityCommentId;

	private DataAreaRef dataAreaRef;

	private Facility facility;

	private String description;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public FacilityComment() {
	}

	/** full constructor */
	public FacilityComment(long facilityCommentId, DataAreaRef dataAreaRef,
			Facility facility, String description, String lastUpdateUserid,
			Date lastUpdateTs) {
		this.facilityCommentId = facilityCommentId;
		this.dataAreaRef = dataAreaRef;
		this.facility = facility;
		this.description = description;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public long getFacilityCommentId() {
		return this.facilityCommentId;
	}

	public void setFacilityCommentId(long facilityCommentId) {
		this.facilityCommentId = facilityCommentId;
	}

	public DataAreaRef getDataAreaRef() {
		return this.dataAreaRef;
	}

	public void setDataAreaRef(DataAreaRef dataAreaRef) {
		this.dataAreaRef = dataAreaRef;
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