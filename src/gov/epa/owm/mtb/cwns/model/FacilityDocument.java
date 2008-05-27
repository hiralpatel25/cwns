package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * FacilityDocument generated by hbm2java
 */
public class FacilityDocument implements java.io.Serializable {

	// Fields    

	private FacilityDocumentId id;

	private Document document;

	private Facility facility;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private String feedbackProjectDescription;

	private char feedbackDeleteFlag;

	private Set costs = new HashSet(0);

	private Set facilityCostCurves = new HashSet(0);

	// Constructors

	/** default constructor */
	public FacilityDocument() {
	}

	/** minimal constructor */
	public FacilityDocument(FacilityDocumentId id, Document document,
			Facility facility, String lastUpdateUserid, Date lastUpdateTs,
			char feedbackDeleteFlag) {
		this.id = id;
		this.document = document;
		this.facility = facility;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

	/** full constructor */
	public FacilityDocument(FacilityDocumentId id, Document document,
			Facility facility, String lastUpdateUserid, Date lastUpdateTs,
			String feedbackProjectDescription, char feedbackDeleteFlag,
			Set costs, Set facilityCostCurves) {
		this.id = id;
		this.document = document;
		this.facility = facility;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.feedbackProjectDescription = feedbackProjectDescription;
		this.feedbackDeleteFlag = feedbackDeleteFlag;
		this.costs = costs;
		this.facilityCostCurves = facilityCostCurves;
	}

	// Property accessors
	public FacilityDocumentId getId() {
		return this.id;
	}

	public void setId(FacilityDocumentId id) {
		this.id = id;
	}

	public Document getDocument() {
		return this.document;
	}

	public void setDocument(Document document) {
		this.document = document;
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

	public Date getLastUpdateTs() {
		return this.lastUpdateTs;
	}

	public void setLastUpdateTs(Date lastUpdateTs) {
		this.lastUpdateTs = lastUpdateTs;
	}

	public String getFeedbackProjectDescription() {
		return this.feedbackProjectDescription;
	}

	public void setFeedbackProjectDescription(String feedbackProjectDescription) {
		this.feedbackProjectDescription = feedbackProjectDescription;
	}

	public char getFeedbackDeleteFlag() {
		return this.feedbackDeleteFlag;
	}

	public void setFeedbackDeleteFlag(char feedbackDeleteFlag) {
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

	public Set getCosts() {
		return this.costs;
	}

	public void setCosts(Set costs) {
		this.costs = costs;
	}

	public Set getFacilityCostCurves() {
		return this.facilityCostCurves;
	}

	public void setFacilityCostCurves(Set facilityCostCurves) {
		this.facilityCostCurves = facilityCostCurves;
	}

}