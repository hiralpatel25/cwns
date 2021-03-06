package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * CostTempId generated by hbm2java
 */
public class CostTempId implements java.io.Serializable {

	// Fields

	private long costId;

	private char costMethodCode;

	private Long baseAmount;

	private Long adjustedAmount;

	private String costAdjustmentAreaId;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Character needTypeId;

	private String categoryId;

	private Long documentId;

	private Long facilityId;

	private Long facilityCostCurveId;

	private Long ncgId;

	private String classificationId;

	private Character ssoFlag;

	// Constructors

	/** default constructor */
	public CostTempId() {
	}

	/** minimal constructor */
	public CostTempId(long costId, char costMethodCode) {
		this.costId = costId;
		this.costMethodCode = costMethodCode;
	}

	/** full constructor */
	public CostTempId(long costId, char costMethodCode, Long baseAmount,
			Long adjustedAmount, String costAdjustmentAreaId,
			String lastUpdateUserid, Date lastUpdateTs, Character needTypeId,
			String categoryId, Long documentId, Long facilityId,
			Long facilityCostCurveId, Long ncgId, String classificationId,
			Character ssoFlag) {
		this.costId = costId;
		this.costMethodCode = costMethodCode;
		this.baseAmount = baseAmount;
		this.adjustedAmount = adjustedAmount;
		this.costAdjustmentAreaId = costAdjustmentAreaId;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.needTypeId = needTypeId;
		this.categoryId = categoryId;
		this.documentId = documentId;
		this.facilityId = facilityId;
		this.facilityCostCurveId = facilityCostCurveId;
		this.ncgId = ncgId;
		this.classificationId = classificationId;
		this.ssoFlag = ssoFlag;
	}

	// Property accessors
	public long getCostId() {
		return this.costId;
	}

	public void setCostId(long costId) {
		this.costId = costId;
	}

	public char getCostMethodCode() {
		return this.costMethodCode;
	}

	public void setCostMethodCode(char costMethodCode) {
		this.costMethodCode = costMethodCode;
	}

	public Long getBaseAmount() {
		return this.baseAmount;
	}

	public void setBaseAmount(Long baseAmount) {
		this.baseAmount = baseAmount;
	}

	public Long getAdjustedAmount() {
		return this.adjustedAmount;
	}

	public void setAdjustedAmount(Long adjustedAmount) {
		this.adjustedAmount = adjustedAmount;
	}

	public String getCostAdjustmentAreaId() {
		return this.costAdjustmentAreaId;
	}

	public void setCostAdjustmentAreaId(String costAdjustmentAreaId) {
		this.costAdjustmentAreaId = costAdjustmentAreaId;
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

	public Character getNeedTypeId() {
		return this.needTypeId;
	}

	public void setNeedTypeId(Character needTypeId) {
		this.needTypeId = needTypeId;
	}

	public String getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Long getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Long getFacilityId() {
		return this.facilityId;
	}

	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}

	public Long getFacilityCostCurveId() {
		return this.facilityCostCurveId;
	}

	public void setFacilityCostCurveId(Long facilityCostCurveId) {
		this.facilityCostCurveId = facilityCostCurveId;
	}

	public Long getNcgId() {
		return this.ncgId;
	}

	public void setNcgId(Long ncgId) {
		this.ncgId = ncgId;
	}

	public String getClassificationId() {
		return this.classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

	public Character getSsoFlag() {
		return this.ssoFlag;
	}

	public void setSsoFlag(Character ssoFlag) {
		this.ssoFlag = ssoFlag;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CostTempId))
			return false;
		CostTempId castOther = (CostTempId) other;

		return (this.getCostId() == castOther.getCostId())
				&& (this.getCostMethodCode() == castOther.getCostMethodCode())
				&& ((this.getBaseAmount() == castOther.getBaseAmount()) || (this
						.getBaseAmount() != null
						&& castOther.getBaseAmount() != null && this
						.getBaseAmount().equals(castOther.getBaseAmount())))
				&& ((this.getAdjustedAmount() == castOther.getAdjustedAmount()) || (this
						.getAdjustedAmount() != null
						&& castOther.getAdjustedAmount() != null && this
						.getAdjustedAmount().equals(
								castOther.getAdjustedAmount())))
				&& ((this.getCostAdjustmentAreaId() == castOther
						.getCostAdjustmentAreaId()) || (this
						.getCostAdjustmentAreaId() != null
						&& castOther.getCostAdjustmentAreaId() != null && this
						.getCostAdjustmentAreaId().equals(
								castOther.getCostAdjustmentAreaId())))
				&& ((this.getLastUpdateUserid() == castOther
						.getLastUpdateUserid()) || (this.getLastUpdateUserid() != null
						&& castOther.getLastUpdateUserid() != null && this
						.getLastUpdateUserid().equals(
								castOther.getLastUpdateUserid())))
				&& ((this.getLastUpdateTs() == castOther.getLastUpdateTs()) || (this
						.getLastUpdateTs() != null
						&& castOther.getLastUpdateTs() != null && this
						.getLastUpdateTs().equals(castOther.getLastUpdateTs())))
				&& ((this.getNeedTypeId() == castOther.getNeedTypeId()) || (this
						.getNeedTypeId() != null
						&& castOther.getNeedTypeId() != null && this
						.getNeedTypeId().equals(castOther.getNeedTypeId())))
				&& ((this.getCategoryId() == castOther.getCategoryId()) || (this
						.getCategoryId() != null
						&& castOther.getCategoryId() != null && this
						.getCategoryId().equals(castOther.getCategoryId())))
				&& ((this.getDocumentId() == castOther.getDocumentId()) || (this
						.getDocumentId() != null
						&& castOther.getDocumentId() != null && this
						.getDocumentId().equals(castOther.getDocumentId())))
				&& ((this.getFacilityId() == castOther.getFacilityId()) || (this
						.getFacilityId() != null
						&& castOther.getFacilityId() != null && this
						.getFacilityId().equals(castOther.getFacilityId())))
				&& ((this.getFacilityCostCurveId() == castOther
						.getFacilityCostCurveId()) || (this
						.getFacilityCostCurveId() != null
						&& castOther.getFacilityCostCurveId() != null && this
						.getFacilityCostCurveId().equals(
								castOther.getFacilityCostCurveId())))
				&& ((this.getNcgId() == castOther.getNcgId()) || (this
						.getNcgId() != null
						&& castOther.getNcgId() != null && this.getNcgId()
						.equals(castOther.getNcgId())))
				&& ((this.getClassificationId() == castOther
						.getClassificationId()) || (this
						.getClassificationId() != null
						&& castOther.getClassificationId() != null && this
						.getClassificationId().equals(
								castOther.getClassificationId())))
				&& ((this.getSsoFlag() == castOther.getSsoFlag()) || (this
						.getSsoFlag() != null
						&& castOther.getSsoFlag() != null && this.getSsoFlag()
						.equals(castOther.getSsoFlag())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getCostId();
		result = 37 * result + this.getCostMethodCode();
		result = 37
				* result
				+ (getBaseAmount() == null ? 0 : this.getBaseAmount()
						.hashCode());
		result = 37
				* result
				+ (getAdjustedAmount() == null ? 0 : this.getAdjustedAmount()
						.hashCode());
		result = 37
				* result
				+ (getCostAdjustmentAreaId() == null ? 0 : this
						.getCostAdjustmentAreaId().hashCode());
		result = 37
				* result
				+ (getLastUpdateUserid() == null ? 0 : this
						.getLastUpdateUserid().hashCode());
		result = 37
				* result
				+ (getLastUpdateTs() == null ? 0 : this.getLastUpdateTs()
						.hashCode());
		result = 37
				* result
				+ (getNeedTypeId() == null ? 0 : this.getNeedTypeId()
						.hashCode());
		result = 37
				* result
				+ (getCategoryId() == null ? 0 : this.getCategoryId()
						.hashCode());
		result = 37
				* result
				+ (getDocumentId() == null ? 0 : this.getDocumentId()
						.hashCode());
		result = 37
				* result
				+ (getFacilityId() == null ? 0 : this.getFacilityId()
						.hashCode());
		result = 37
				* result
				+ (getFacilityCostCurveId() == null ? 0 : this
						.getFacilityCostCurveId().hashCode());
		result = 37 * result
				+ (getNcgId() == null ? 0 : this.getNcgId().hashCode());
		result = 37
				* result
				+ (getClassificationId() == null ? 0 : this
						.getClassificationId().hashCode());
		result = 37 * result
				+ (getSsoFlag() == null ? 0 : this.getSsoFlag().hashCode());
		return result;
	}

}
