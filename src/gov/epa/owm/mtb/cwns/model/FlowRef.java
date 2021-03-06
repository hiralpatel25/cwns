package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * FlowRef generated by hbm2java
 */
public class FlowRef implements java.io.Serializable {

	// Fields    

	private long flowId;

	private String name;

	private String measureUnitCode;

	private String measureQualifierCode;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set facilityFlows = new HashSet(0);

	// Constructors

	/** default constructor */
	public FlowRef() {
	}

	/** minimal constructor */
	public FlowRef(long flowId, String name, String measureUnitCode,
			String measureQualifierCode, String lastUpdateUserid,
			Date lastUpdateTs) {
		this.flowId = flowId;
		this.name = name;
		this.measureUnitCode = measureUnitCode;
		this.measureQualifierCode = measureQualifierCode;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public FlowRef(long flowId, String name, String measureUnitCode,
			String measureQualifierCode, String lastUpdateUserid,
			Date lastUpdateTs, Set facilityFlows) {
		this.flowId = flowId;
		this.name = name;
		this.measureUnitCode = measureUnitCode;
		this.measureQualifierCode = measureQualifierCode;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.facilityFlows = facilityFlows;
	}

	// Property accessors
	public long getFlowId() {
		return this.flowId;
	}

	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeasureUnitCode() {
		return this.measureUnitCode;
	}

	public void setMeasureUnitCode(String measureUnitCode) {
		this.measureUnitCode = measureUnitCode;
	}

	public String getMeasureQualifierCode() {
		return this.measureQualifierCode;
	}

	public void setMeasureQualifierCode(String measureQualifierCode) {
		this.measureQualifierCode = measureQualifierCode;
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

	public Set getFacilityFlows() {
		return this.facilityFlows;
	}

	public void setFacilityFlows(Set facilityFlows) {
		this.facilityFlows = facilityFlows;
	}

}
