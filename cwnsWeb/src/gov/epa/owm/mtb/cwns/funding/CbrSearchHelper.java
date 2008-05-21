package gov.epa.owm.mtb.cwns.funding;

import java.util.Date;
import java.util.Set;

public class CbrSearchHelper {	
	
	private String borrower;
	private String trackingNumber;
	private Date startDate;
	private Date endDate;
	private String assocWithCwnsNumber;
	private String assocWithPermit;
	private int result = 0;
	private Set loanIds;
	private Set categoryAmountIds;
	private long facilityId;
	
	public static final int COUNT = 0;
	public static final int LIST = 1;
	
	
	private int startCount = 1;
	private int maxCount = 3;
	
	public String getAssocWithCwnsNumber() {
		return assocWithCwnsNumber;
	}
	public void setAssocWithCwnsNumber(String assocWithCwnsNumber) {
		this.assocWithCwnsNumber = assocWithCwnsNumber;
	}
	public String getAssocWithPermit() {
		return assocWithPermit;
	}
	public void setAssocWithPermit(String assocWithPermit) {
		this.assocWithPermit = assocWithPermit;
	}
	public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public int getStartCount() {
		return startCount;
	}
	public void setStartCount(int startCount) {
		this.startCount = startCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public Set getCategoryAmountIds() {
		return categoryAmountIds;
	}
	public void setCategoryAmountIds(Set categoryAmountIds) {
		this.categoryAmountIds = categoryAmountIds;
	}
	public Set getLoanIds() {
		return loanIds;
	}
	public void setLoanIds(Set loanIds) {
		this.loanIds = loanIds;
	}
	public long getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(long facilityId) {
		this.facilityId = facilityId;
	}
		
}
