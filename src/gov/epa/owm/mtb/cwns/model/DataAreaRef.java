package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * DataAreaRef generated by hbm2java
 */
public class DataAreaRef implements java.io.Serializable {

	// Fields

	private long dataAreaId;

	private String name;

	private byte sortSequence;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set dataAreaFesRuleRefsForDataAreaIdToPerform = new HashSet(0);

	private Set facilityComments = new HashSet(0);

	private Set federalReviewStatuses = new HashSet(0);

	private Set dataAreaFesRuleRefsForDataAreaIdCurrent = new HashSet(0);

	private Set facilityEntryStatuses = new HashSet(0);

	private Set facilityCostCurveDataAreas = new HashSet(0);

	// Constructors

	/** default constructor */
	public DataAreaRef() {
	}

	/** minimal constructor */
	public DataAreaRef(long dataAreaId, String name, byte sortSequence,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.dataAreaId = dataAreaId;
		this.name = name;
		this.sortSequence = sortSequence;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public DataAreaRef(long dataAreaId, String name, byte sortSequence,
			String lastUpdateUserid, Date lastUpdateTs,
			Set dataAreaFesRuleRefsForDataAreaIdToPerform,
			Set facilityComments, Set federalReviewStatuses,
			Set dataAreaFesRuleRefsForDataAreaIdCurrent,
			Set facilityEntryStatuses, Set facilityCostCurveDataAreas) {
		this.dataAreaId = dataAreaId;
		this.name = name;
		this.sortSequence = sortSequence;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.dataAreaFesRuleRefsForDataAreaIdToPerform = dataAreaFesRuleRefsForDataAreaIdToPerform;
		this.facilityComments = facilityComments;
		this.federalReviewStatuses = federalReviewStatuses;
		this.dataAreaFesRuleRefsForDataAreaIdCurrent = dataAreaFesRuleRefsForDataAreaIdCurrent;
		this.facilityEntryStatuses = facilityEntryStatuses;
		this.facilityCostCurveDataAreas = facilityCostCurveDataAreas;
	}

	// Property accessors
	public long getDataAreaId() {
		return this.dataAreaId;
	}

	public void setDataAreaId(long dataAreaId) {
		this.dataAreaId = dataAreaId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getSortSequence() {
		return this.sortSequence;
	}

	public void setSortSequence(byte sortSequence) {
		this.sortSequence = sortSequence;
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

	public Set getDataAreaFesRuleRefsForDataAreaIdToPerform() {
		return this.dataAreaFesRuleRefsForDataAreaIdToPerform;
	}

	public void setDataAreaFesRuleRefsForDataAreaIdToPerform(
			Set dataAreaFesRuleRefsForDataAreaIdToPerform) {
		this.dataAreaFesRuleRefsForDataAreaIdToPerform = dataAreaFesRuleRefsForDataAreaIdToPerform;
	}

	public Set getFacilityComments() {
		return this.facilityComments;
	}

	public void setFacilityComments(Set facilityComments) {
		this.facilityComments = facilityComments;
	}

	public Set getFederalReviewStatuses() {
		return this.federalReviewStatuses;
	}

	public void setFederalReviewStatuses(Set federalReviewStatuses) {
		this.federalReviewStatuses = federalReviewStatuses;
	}

	public Set getDataAreaFesRuleRefsForDataAreaIdCurrent() {
		return this.dataAreaFesRuleRefsForDataAreaIdCurrent;
	}

	public void setDataAreaFesRuleRefsForDataAreaIdCurrent(
			Set dataAreaFesRuleRefsForDataAreaIdCurrent) {
		this.dataAreaFesRuleRefsForDataAreaIdCurrent = dataAreaFesRuleRefsForDataAreaIdCurrent;
	}

	public Set getFacilityEntryStatuses() {
		return this.facilityEntryStatuses;
	}

	public void setFacilityEntryStatuses(Set facilityEntryStatuses) {
		this.facilityEntryStatuses = facilityEntryStatuses;
	}

	public Set getFacilityCostCurveDataAreas() {
		return this.facilityCostCurveDataAreas;
	}

	public void setFacilityCostCurveDataAreas(Set facilityCostCurveDataAreas) {
		this.facilityCostCurveDataAreas = facilityCostCurveDataAreas;
	}

}