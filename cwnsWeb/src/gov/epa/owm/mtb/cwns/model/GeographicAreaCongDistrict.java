package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * GeographicAreaCongDistrict generated by hbm2java
 */
public class GeographicAreaCongDistrict implements java.io.Serializable {

	// Fields    

	private GeographicAreaCongDistrictId id;

	private CongressionalDistrictRef congressionalDistrictRef;

	private GeographicArea geographicArea;

	private char primaryFlag;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private char feedbackDeleteFlag;

	// Constructors

	/** default constructor */
	public GeographicAreaCongDistrict() {
	}

	/** full constructor */
	public GeographicAreaCongDistrict(GeographicAreaCongDistrictId id,
			CongressionalDistrictRef congressionalDistrictRef,
			GeographicArea geographicArea, char primaryFlag,
			String lastUpdateUserid, Date lastUpdateTs, char feedbackDeleteFlag) {
		this.id = id;
		this.congressionalDistrictRef = congressionalDistrictRef;
		this.geographicArea = geographicArea;
		this.primaryFlag = primaryFlag;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

	// Property accessors
	public GeographicAreaCongDistrictId getId() {
		return this.id;
	}

	public void setId(GeographicAreaCongDistrictId id) {
		this.id = id;
	}

	public CongressionalDistrictRef getCongressionalDistrictRef() {
		return this.congressionalDistrictRef;
	}

	public void setCongressionalDistrictRef(
			CongressionalDistrictRef congressionalDistrictRef) {
		this.congressionalDistrictRef = congressionalDistrictRef;
	}

	public GeographicArea getGeographicArea() {
		return this.geographicArea;
	}

	public void setGeographicArea(GeographicArea geographicArea) {
		this.geographicArea = geographicArea;
	}

	public char getPrimaryFlag() {
		return this.primaryFlag;
	}

	public void setPrimaryFlag(char primaryFlag) {
		this.primaryFlag = primaryFlag;
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

	public char getFeedbackDeleteFlag() {
		return this.feedbackDeleteFlag;
	}

	public void setFeedbackDeleteFlag(char feedbackDeleteFlag) {
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

}
