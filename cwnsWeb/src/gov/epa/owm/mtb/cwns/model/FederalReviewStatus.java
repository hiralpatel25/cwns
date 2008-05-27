package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * FederalReviewStatus generated by hbm2java
 */
public class FederalReviewStatus implements java.io.Serializable {

	// Fields    

	private FederalReviewStatusId id;

	private DataAreaRef dataAreaRef;

	private Facility facility;

	private char errorFlag;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public FederalReviewStatus() {
	}

	/** full constructor */
	public FederalReviewStatus(FederalReviewStatusId id,
			DataAreaRef dataAreaRef, Facility facility, char errorFlag,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.id = id;
		this.dataAreaRef = dataAreaRef;
		this.facility = facility;
		this.errorFlag = errorFlag;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public FederalReviewStatusId getId() {
		return this.id;
	}

	public void setId(FederalReviewStatusId id) {
		this.id = id;
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

	public char getErrorFlag() {
		return this.errorFlag;
	}

	public void setErrorFlag(char errorFlag) {
		this.errorFlag = errorFlag;
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