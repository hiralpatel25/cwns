package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CbrLoanInformation generated by hbm2java
 */
public class CbrLoanInformation implements java.io.Serializable {

	// Fields    

	private String cbrLoanInformationId;

	private CbrBorrower cbrBorrower;

	private LoanTypeRef loanTypeRef;

	private int loanRecordNumber;

	private String locationId;

	private Date loanDate;

	private String loanNumber;

	private BigDecimal loanAmount;

	private Character finalAmountFlag;

	private BigDecimal percentFundedByCwsrf;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set cbrProjectInformations = new HashSet(0);

	// Constructors

	/** default constructor */
	public CbrLoanInformation() {
	}

	/** minimal constructor */
	public CbrLoanInformation(String cbrLoanInformationId,
			int loanRecordNumber, String locationId, String loanNumber,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.cbrLoanInformationId = cbrLoanInformationId;
		this.loanRecordNumber = loanRecordNumber;
		this.locationId = locationId;
		this.loanNumber = loanNumber;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public CbrLoanInformation(String cbrLoanInformationId,
			CbrBorrower cbrBorrower, LoanTypeRef loanTypeRef,
			int loanRecordNumber, String locationId, Date loanDate,
			String loanNumber, BigDecimal loanAmount,
			Character finalAmountFlag, BigDecimal percentFundedByCwsrf,
			String lastUpdateUserid, Date lastUpdateTs,
			Set cbrProjectInformations) {
		this.cbrLoanInformationId = cbrLoanInformationId;
		this.cbrBorrower = cbrBorrower;
		this.loanTypeRef = loanTypeRef;
		this.loanRecordNumber = loanRecordNumber;
		this.locationId = locationId;
		this.loanDate = loanDate;
		this.loanNumber = loanNumber;
		this.loanAmount = loanAmount;
		this.finalAmountFlag = finalAmountFlag;
		this.percentFundedByCwsrf = percentFundedByCwsrf;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.cbrProjectInformations = cbrProjectInformations;
	}

	// Property accessors
	public String getCbrLoanInformationId() {
		return this.cbrLoanInformationId;
	}

	public void setCbrLoanInformationId(String cbrLoanInformationId) {
		this.cbrLoanInformationId = cbrLoanInformationId;
	}

	public CbrBorrower getCbrBorrower() {
		return this.cbrBorrower;
	}

	public void setCbrBorrower(CbrBorrower cbrBorrower) {
		this.cbrBorrower = cbrBorrower;
	}

	public LoanTypeRef getLoanTypeRef() {
		return this.loanTypeRef;
	}

	public void setLoanTypeRef(LoanTypeRef loanTypeRef) {
		this.loanTypeRef = loanTypeRef;
	}

	public int getLoanRecordNumber() {
		return this.loanRecordNumber;
	}

	public void setLoanRecordNumber(int loanRecordNumber) {
		this.loanRecordNumber = loanRecordNumber;
	}

	public String getLocationId() {
		return this.locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public Date getLoanDate() {
		return this.loanDate;
	}

	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}

	public String getLoanNumber() {
		return this.loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public BigDecimal getLoanAmount() {
		return this.loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Character getFinalAmountFlag() {
		return this.finalAmountFlag;
	}

	public void setFinalAmountFlag(Character finalAmountFlag) {
		this.finalAmountFlag = finalAmountFlag;
	}

	public BigDecimal getPercentFundedByCwsrf() {
		return this.percentFundedByCwsrf;
	}

	public void setPercentFundedByCwsrf(BigDecimal percentFundedByCwsrf) {
		this.percentFundedByCwsrf = percentFundedByCwsrf;
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

	public Set getCbrProjectInformations() {
		return this.cbrProjectInformations;
	}

	public void setCbrProjectInformations(Set cbrProjectInformations) {
		this.cbrProjectInformations = cbrProjectInformations;
	}

}