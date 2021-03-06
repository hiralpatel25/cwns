package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * DataAreaFesRuleRef generated by hbm2java
 */
public class DataAreaFesRuleRef implements java.io.Serializable {

	// Fields

	private DataAreaFesRuleRefId id;

	private DataAreaRef dataAreaRefByDataAreaIdToPerform;

	private DataAreaRef dataAreaRefByDataAreaIdCurrent;

	private char performRequiredCheckFlag;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	// Constructors

	/** default constructor */
	public DataAreaFesRuleRef() {
	}

	/** full constructor */
	public DataAreaFesRuleRef(DataAreaFesRuleRefId id,
			DataAreaRef dataAreaRefByDataAreaIdToPerform,
			DataAreaRef dataAreaRefByDataAreaIdCurrent,
			char performRequiredCheckFlag, String lastUpdateUserid,
			Date lastUpdateTs) {
		this.id = id;
		this.dataAreaRefByDataAreaIdToPerform = dataAreaRefByDataAreaIdToPerform;
		this.dataAreaRefByDataAreaIdCurrent = dataAreaRefByDataAreaIdCurrent;
		this.performRequiredCheckFlag = performRequiredCheckFlag;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	// Property accessors
	public DataAreaFesRuleRefId getId() {
		return this.id;
	}

	public void setId(DataAreaFesRuleRefId id) {
		this.id = id;
	}

	public DataAreaRef getDataAreaRefByDataAreaIdToPerform() {
		return this.dataAreaRefByDataAreaIdToPerform;
	}

	public void setDataAreaRefByDataAreaIdToPerform(
			DataAreaRef dataAreaRefByDataAreaIdToPerform) {
		this.dataAreaRefByDataAreaIdToPerform = dataAreaRefByDataAreaIdToPerform;
	}

	public DataAreaRef getDataAreaRefByDataAreaIdCurrent() {
		return this.dataAreaRefByDataAreaIdCurrent;
	}

	public void setDataAreaRefByDataAreaIdCurrent(
			DataAreaRef dataAreaRefByDataAreaIdCurrent) {
		this.dataAreaRefByDataAreaIdCurrent = dataAreaRefByDataAreaIdCurrent;
	}

	public char getPerformRequiredCheckFlag() {
		return this.performRequiredCheckFlag;
	}

	public void setPerformRequiredCheckFlag(char performRequiredCheckFlag) {
		this.performRequiredCheckFlag = performRequiredCheckFlag;
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
