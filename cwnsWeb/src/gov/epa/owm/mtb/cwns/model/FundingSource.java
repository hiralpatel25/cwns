package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.util.Date;

/**
 * FundingSource generated by hbm2java
 */
public class FundingSource implements java.io.Serializable {

	// Fields    

	private long fundingSourceId;

	private FundingSourceTypeRef fundingSourceTypeRef;

	private CbrAmountInformation cbrAmountInformation;

	private FundingAgencyRef fundingAgencyRef;

	private LoanTypeRef loanTypeRef;

	private Facility facility;

	private String locationId;

	private char sourceCd;

	private String loanNumber;

	private Date awardDate;

	private Long awardedAmount;

	private Short percentageFundedBySrf;

	private Integer cbrProjectNumber;

	private Character consistentWithCbrCode;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private char feedbackDeleteFlag;

	// Constructors

	/** default constructor */
	public FundingSource() {
	}

	/** minimal constructor */
	public FundingSource(long fundingSourceId, Facility facility,
			String locationId, char sourceCd, Date awardDate,
			String lastUpdateUserid, Date lastUpdateTs, char feedbackDeleteFlag) {
		this.fundingSourceId = fundingSourceId;
		this.facility = facility;
		this.locationId = locationId;
		this.sourceCd = sourceCd;
		this.awardDate = awardDate;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

	/** full constructor */
	public FundingSource(long fundingSourceId,
			FundingSourceTypeRef fundingSourceTypeRef,
			CbrAmountInformation cbrAmountInformation,
			FundingAgencyRef fundingAgencyRef, LoanTypeRef loanTypeRef,
			Facility facility, String locationId, char sourceCd,
			String loanNumber, Date awardDate, Long awardedAmount,
			Short percentageFundedBySrf, Integer cbrProjectNumber,
			Character consistentWithCbrCode, String lastUpdateUserid,
			Date lastUpdateTs, char feedbackDeleteFlag) {
		this.fundingSourceId = fundingSourceId;
		this.fundingSourceTypeRef = fundingSourceTypeRef;
		this.cbrAmountInformation = cbrAmountInformation;
		this.fundingAgencyRef = fundingAgencyRef;
		this.loanTypeRef = loanTypeRef;
		this.facility = facility;
		this.locationId = locationId;
		this.sourceCd = sourceCd;
		this.loanNumber = loanNumber;
		this.awardDate = awardDate;
		this.awardedAmount = awardedAmount;
		this.percentageFundedBySrf = percentageFundedBySrf;
		this.cbrProjectNumber = cbrProjectNumber;
		this.consistentWithCbrCode = consistentWithCbrCode;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.feedbackDeleteFlag = feedbackDeleteFlag;
	}

	// Property accessors
	public long getFundingSourceId() {
		return this.fundingSourceId;
	}

	public void setFundingSourceId(long fundingSourceId) {
		this.fundingSourceId = fundingSourceId;
	}

	public FundingSourceTypeRef getFundingSourceTypeRef() {
		return this.fundingSourceTypeRef;
	}

	public void setFundingSourceTypeRef(
			FundingSourceTypeRef fundingSourceTypeRef) {
		this.fundingSourceTypeRef = fundingSourceTypeRef;
	}

	public CbrAmountInformation getCbrAmountInformation() {
		return this.cbrAmountInformation;
	}

	public void setCbrAmountInformation(
			CbrAmountInformation cbrAmountInformation) {
		this.cbrAmountInformation = cbrAmountInformation;
	}

	public FundingAgencyRef getFundingAgencyRef() {
		return this.fundingAgencyRef;
	}

	public void setFundingAgencyRef(FundingAgencyRef fundingAgencyRef) {
		this.fundingAgencyRef = fundingAgencyRef;
	}

	public LoanTypeRef getLoanTypeRef() {
		return this.loanTypeRef;
	}

	public void setLoanTypeRef(LoanTypeRef loanTypeRef) {
		this.loanTypeRef = loanTypeRef;
	}

	public Facility getFacility() {
		return this.facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

	public String getLocationId() {
		return this.locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public char getSourceCd() {
		return this.sourceCd;
	}

	public void setSourceCd(char sourceCd) {
		this.sourceCd = sourceCd;
	}

	public String getLoanNumber() {
		return this.loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public Date getAwardDate() {
		return this.awardDate;
	}

	public void setAwardDate(Date awardDate) {
		this.awardDate = awardDate;
	}

	public Long getAwardedAmount() {
		return this.awardedAmount;
	}

	public void setAwardedAmount(Long awardedAmount) {
		this.awardedAmount = awardedAmount;
	}

	public Short getPercentageFundedBySrf() {
		return this.percentageFundedBySrf;
	}

	public void setPercentageFundedBySrf(Short percentageFundedBySrf) {
		this.percentageFundedBySrf = percentageFundedBySrf;
	}

	public Integer getCbrProjectNumber() {
		return this.cbrProjectNumber;
	}

	public void setCbrProjectNumber(Integer cbrProjectNumber) {
		this.cbrProjectNumber = cbrProjectNumber;
	}

	public Character getConsistentWithCbrCode() {
		return this.consistentWithCbrCode;
	}

	public void setConsistentWithCbrCode(Character consistentWithCbrCode) {
		this.consistentWithCbrCode = consistentWithCbrCode;
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
