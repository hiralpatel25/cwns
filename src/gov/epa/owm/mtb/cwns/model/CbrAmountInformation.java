package gov.epa.owm.mtb.cwns.model;

// Generated Oct 31, 2007 10:35:21 AM by Hibernate Tools 3.2.0.beta8

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CbrAmountInformation generated by hbm2java
 */
public class CbrAmountInformation implements java.io.Serializable {

	// Fields    

	private CbrAmountInformationId id;

	private CbrProjectInformation cbrProjectInformation;

	private BigDecimal amount;

	private String lastUpdateUserid;

	private Date lastUpdateTs;

	private Set fundingSources = new HashSet(0);

	// Constructors

	/** default constructor */
	public CbrAmountInformation() {
	}

	/** minimal constructor */
	public CbrAmountInformation(CbrAmountInformationId id,
			CbrProjectInformation cbrProjectInformation, BigDecimal amount,
			String lastUpdateUserid, Date lastUpdateTs) {
		this.id = id;
		this.cbrProjectInformation = cbrProjectInformation;
		this.amount = amount;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
	}

	/** full constructor */
	public CbrAmountInformation(CbrAmountInformationId id,
			CbrProjectInformation cbrProjectInformation, BigDecimal amount,
			String lastUpdateUserid, Date lastUpdateTs, Set fundingSources) {
		this.id = id;
		this.cbrProjectInformation = cbrProjectInformation;
		this.amount = amount;
		this.lastUpdateUserid = lastUpdateUserid;
		this.lastUpdateTs = lastUpdateTs;
		this.fundingSources = fundingSources;
	}

	// Property accessors
	public CbrAmountInformationId getId() {
		return this.id;
	}

	public void setId(CbrAmountInformationId id) {
		this.id = id;
	}

	public CbrProjectInformation getCbrProjectInformation() {
		return this.cbrProjectInformation;
	}

	public void setCbrProjectInformation(
			CbrProjectInformation cbrProjectInformation) {
		this.cbrProjectInformation = cbrProjectInformation;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public Set getFundingSources() {
		return this.fundingSources;
	}

	public void setFundingSources(Set fundingSources) {
		this.fundingSources = fundingSources;
	}

}