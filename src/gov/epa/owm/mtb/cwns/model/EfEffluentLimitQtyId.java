package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

/**
 * EfEffluentLimitQtyId generated by hbm2java
 */
public class EfEffluentLimitQtyId implements java.io.Serializable {

	// Fields

	private String efNpdesPermitNumber;

	private String efDischargeNum;

	private String efReportDesig;

	private char efPipeSetQualifier;

	private String efLimitType;

	private String efParamCode;

	private String efMonitoringLoc;

	private String efSeasonNum;

	private String efModifNum;

	// Constructors

	/** default constructor */
	public EfEffluentLimitQtyId() {
	}

	/** full constructor */
	public EfEffluentLimitQtyId(String efNpdesPermitNumber,
			String efDischargeNum, String efReportDesig,
			char efPipeSetQualifier, String efLimitType,
			String efParamCode, String efMonitoringLoc,
			String efSeasonNum, String efModifNum) {
		this.efNpdesPermitNumber = efNpdesPermitNumber;
		this.efDischargeNum = efDischargeNum;
		this.efReportDesig = efReportDesig;
		this.efPipeSetQualifier = efPipeSetQualifier;
		this.efLimitType = efLimitType;
		this.efParamCode = efParamCode;
		this.efMonitoringLoc = efMonitoringLoc;
		this.efSeasonNum = efSeasonNum;
		this.efModifNum = efModifNum;
	}

	// Property accessors
	public String getEfNpdesPermitNumber() {
		return this.efNpdesPermitNumber;
	}

	public void setEfNpdesPermitNumber(String efNpdesPermitNumber) {
		this.efNpdesPermitNumber = efNpdesPermitNumber;
	}

	public String getEfDischargeNum() {
		return this.efDischargeNum;
	}

	public void setEfDischargeNum(String efDischargeNum) {
		this.efDischargeNum = efDischargeNum;
	}

	public String getEfReportDesig() {
		return this.efReportDesig;
	}

	public void setEfReportDesig(String efReportDesig) {
		this.efReportDesig = efReportDesig;
	}

	public char getEfPipeSetQualifier() {
		return this.efPipeSetQualifier;
	}

	public void setEfPipeSetQualifier(char efPipeSetQualifier) {
		this.efPipeSetQualifier = efPipeSetQualifier;
	}

	public String getEfLimitType() {
		return this.efLimitType;
	}

	public void setEfLimitType(String efLimitType) {
		this.efLimitType = efLimitType;
	}

	public String getEfParamCode() {
		return this.efParamCode;
	}

	public void setEfParamCode(String efParamCode) {
		this.efParamCode = efParamCode;
	}

	public String getEfMonitoringLoc() {
		return this.efMonitoringLoc;
	}

	public void setEfMonitoringLoc(String efMonitoringLoc) {
		this.efMonitoringLoc = efMonitoringLoc;
	}

	public String getEfSeasonNum() {
		return this.efSeasonNum;
	}

	public void setEfSeasonNum(String efSeasonNum) {
		this.efSeasonNum = efSeasonNum;
	}

	public String getEfModifNum() {
		return this.efModifNum;
	}

	public void setEfModifNum(String efModifNum) {
		this.efModifNum = efModifNum;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EfEffluentLimitQtyId))
			return false;
		EfEffluentLimitQtyId castOther = (EfEffluentLimitQtyId) other;

		return ((this.getEfNpdesPermitNumber() == castOther
				.getEfNpdesPermitNumber()) || (this
				.getEfNpdesPermitNumber() != null
				&& castOther.getEfNpdesPermitNumber() != null && this
				.getEfNpdesPermitNumber().equals(
						castOther.getEfNpdesPermitNumber())))
				&& ((this.getEfDischargeNum() == castOther
						.getEfDischargeNum()) || (this.getEfDischargeNum() != null
						&& castOther.getEfDischargeNum() != null && this
						.getEfDischargeNum().equals(
								castOther.getEfDischargeNum())))
				&& ((this.getEfReportDesig() == castOther
						.getEfReportDesig()) || (this.getEfReportDesig() != null
						&& castOther.getEfReportDesig() != null && this
						.getEfReportDesig().equals(
								castOther.getEfReportDesig())))
				&& (this.getEfPipeSetQualifier() == castOther
						.getEfPipeSetQualifier())
				&& ((this.getEfLimitType() == castOther.getEfLimitType()) || (this
						.getEfLimitType() != null
						&& castOther.getEfLimitType() != null && this
						.getEfLimitType()
						.equals(castOther.getEfLimitType())))
				&& ((this.getEfParamCode() == castOther.getEfParamCode()) || (this
						.getEfParamCode() != null
						&& castOther.getEfParamCode() != null && this
						.getEfParamCode()
						.equals(castOther.getEfParamCode())))
				&& ((this.getEfMonitoringLoc() == castOther
						.getEfMonitoringLoc()) || (this
						.getEfMonitoringLoc() != null
						&& castOther.getEfMonitoringLoc() != null && this
						.getEfMonitoringLoc().equals(
								castOther.getEfMonitoringLoc())))
				&& ((this.getEfSeasonNum() == castOther.getEfSeasonNum()) || (this
						.getEfSeasonNum() != null
						&& castOther.getEfSeasonNum() != null && this
						.getEfSeasonNum()
						.equals(castOther.getEfSeasonNum())))
				&& ((this.getEfModifNum() == castOther.getEfModifNum()) || (this
						.getEfModifNum() != null
						&& castOther.getEfModifNum() != null && this
						.getEfModifNum().equals(castOther.getEfModifNum())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getEfNpdesPermitNumber() == null ? 0 : this
						.getEfNpdesPermitNumber().hashCode());
		result = 37
				* result
				+ (getEfDischargeNum() == null ? 0 : this
						.getEfDischargeNum().hashCode());
		result = 37
				* result
				+ (getEfReportDesig() == null ? 0 : this.getEfReportDesig()
						.hashCode());
		result = 37 * result + this.getEfPipeSetQualifier();
		result = 37
				* result
				+ (getEfLimitType() == null ? 0 : this.getEfLimitType()
						.hashCode());
		result = 37
				* result
				+ (getEfParamCode() == null ? 0 : this.getEfParamCode()
						.hashCode());
		result = 37
				* result
				+ (getEfMonitoringLoc() == null ? 0 : this
						.getEfMonitoringLoc().hashCode());
		result = 37
				* result
				+ (getEfSeasonNum() == null ? 0 : this.getEfSeasonNum()
						.hashCode());
		result = 37
				* result
				+ (getEfModifNum() == null ? 0 : this.getEfModifNum()
						.hashCode());
		return result;
	}

}
