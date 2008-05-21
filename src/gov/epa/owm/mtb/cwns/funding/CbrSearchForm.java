package gov.epa.owm.mtb.cwns.funding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.apache.struts.action.ActionForm;


public class CbrSearchForm extends ActionForm{
	
	private static final long serialVersionUID = 1;
	
	private String borrower = "";
	private String trackingNumber = "";
	private String startDate = "";
	private String endDate = "";
	private String assocWithCwnsNumber = "";
	private String assocWithPermit = "";
	private String cbrAction = "";
	private String firstResult = "1";
	private String maxResult = "3";
	private String[] loanIds = {};
	private String[] categoryIds = {};	
	private String facilityId = "";
	private String isUpdatable = "N";
	
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	
	public String getIsUpdatable() {
		return isUpdatable;
	}

	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}
	public CbrSearchHelper popCbrSearchHelper(){
		
		CbrSearchHelper helper = new CbrSearchHelper();
		
		helper.setBorrower(this.getBorrower());
		helper.setTrackingNumber(this.getTrackingNumber());
		helper.setAssocWithCwnsNumber(this.getAssocWithCwnsNumber());
		helper.setAssocWithPermit(this.getAssocWithPermit());
		helper.setFacilityId(new Long(this.getFacilityId()).longValue());
		
		try {			
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			sdf.setLenient(true);		
			helper.setStartDate(sdf.parse(this.getStartDate()));
			helper.setEndDate(sdf.parse(this.getEndDate()));
			
			
		} catch (ParseException e) {			
			//if dates are invalid just continue with the search, but they should not because of js validation.
		} catch (java.lang.NullPointerException e){				
			//if dates are invalid just continue with the search.
		}		
		
		helper.setStartCount(Integer.parseInt(this.getFirstResult()));
		helper.setMaxCount(Integer.parseInt(this.getMaxResult()));
		
		
		helper.setLoanIds(new HashSet(Arrays.asList(this.loanIds)));
		//Collections.addAll(helper.getLoanIds(), this.loanIds);					
		
		
		return helper;
	}
	public String getCbrAction() {
		return cbrAction;
	}
	public void setCbrAction(String cbrAction) {
		this.cbrAction = cbrAction;
	}
	public String getFirstResult() {
		return firstResult;
	}
	public void setFirstResult(String firstResult) {
		this.firstResult = firstResult;
	}
	public String getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(String maxResult) {
		this.maxResult = maxResult;
	}
	public String[] getLoanIds() {
		return loanIds;
	}
	public void setLoanIds(String[] loanIds) {
		this.loanIds = loanIds;
	}
	public String[] getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(String[] categoryIds) {
		this.categoryIds = categoryIds;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	
	
}
