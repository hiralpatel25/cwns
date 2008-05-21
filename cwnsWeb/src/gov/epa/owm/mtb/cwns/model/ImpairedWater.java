package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ImpairedWater generated by hbm2java
 */
public class ImpairedWater implements java.io.Serializable {

	// Fields    

	private String listId;

	private String impairedWaterUrl;

	private String tmdlUrl;

	private String waterBodyName;

	private String sourceCode;

	private String entityId;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set facilityImpairedWaters = new HashSet(0);

	// Constructors

	/** default constructor */
	public ImpairedWater() {
	}

	/** minimal constructor */
	public ImpairedWater(String listId, String sourceCode, String entityId,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.listId = listId;
		this.sourceCode = sourceCode;
		this.entityId = entityId;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public ImpairedWater(String listId, String impairedWaterUrl,
			String tmdlUrl, String waterBodyName, String sourceCode,
			String entityId, String lastUpdateUserid, Date lastUpdateTs,
			Set facilityImpairedWaters) {
		this.listId = listId;
		this.impairedWaterUrl = impairedWaterUrl;
		this.tmdlUrl = tmdlUrl;
		this.waterBodyName = waterBodyName;
		this.sourceCode = sourceCode;
		this.entityId = entityId;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.facilityImpairedWaters = facilityImpairedWaters;
	}

	// Property accessors
	public String getListId() {
		return this.listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getImpairedWaterUrl() {
		return this.impairedWaterUrl;
	}

	public void setImpairedWaterUrl(String impairedWaterUrl) {
		this.impairedWaterUrl = impairedWaterUrl;
	}

	public String getTmdlUrl() {
		return this.tmdlUrl;
	}

	public void setTmdlUrl(String tmdlUrl) {
		this.tmdlUrl = tmdlUrl;
	}

	public String getWaterBodyName() {
		return this.waterBodyName;
	}

	public void setWaterBodyName(String waterBodyName) {
		this.waterBodyName = waterBodyName;
	}

	public String getSourceCode() {
		return this.sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getEntityId() {
		return this.entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
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

	public Set getFacilityImpairedWaters() {
		return this.facilityImpairedWaters;
	}

	public void setFacilityImpairedWaters(Set facilityImpairedWaters) {
		this.facilityImpairedWaters = facilityImpairedWaters;
	}

}
