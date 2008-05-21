package gov.epa.owm.mtb.cwns.funding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FundingAgencyRef;
import gov.epa.owm.mtb.cwns.model.FundingSource;
import gov.epa.owm.mtb.cwns.model.FundingSourceTypeRef;
import gov.epa.owm.mtb.cwns.model.LoanTypeRef;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class FundingForm extends ActionForm{
	
	private static final long serialVersionUID = 1;
	
	private String fundingId = "";
	private String facilityId = "";
	private String fundingAction = "";
	private String awardDate = "";
	private String fundingType = "";
	private String fundingAgency = "";
	private String loanNumber = "";
	private String fundingSource = "";
	private String fundingTotal = "";
	private String percentbyCwsrf = "";
	private String isUpdatable = "N";
	
	
	public String getFundingId() {
		return fundingId;
	}

	public void setFundingId(String fundingId) {
		this.fundingId = fundingId;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getFundingAction() {
		return fundingAction;
	}

	public void setFundingAction(String fundingAction) {
		this.fundingAction = fundingAction;
	}

	public String getAwardDate() {
		return awardDate;
	}

	public void setAwardDate(String awardDate) {
		this.awardDate = awardDate;
	}

	public String getFundingAgency() {
		return fundingAgency;
	}

	public void setFundingAgency(String fundingAgency) {
		this.fundingAgency = fundingAgency;
	}

	public String getFundingSource() {
		return fundingSource;
	}

	public void setFundingSource(String fundingSource) {
		this.fundingSource = fundingSource;
	}

	public String getFundingTotal() {
		return fundingTotal;
	}

	public void setFundingTotal(String fundingTotal) {
		this.fundingTotal = fundingTotal;
	}

	public String getFundingType() {
		return fundingType;
	}

	public void setFundingType(String fundingType) {
		this.fundingType = fundingType;
	}

	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public String getPercentbyCwsrf() {
		return percentbyCwsrf;
	}

	public void setPercentbyCwsrf(String percentbyCwsrf) {
		this.percentbyCwsrf = percentbyCwsrf;
	}
	
	public String getIsUpdatable() {
		return isUpdatable;
	}

	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	public void reset(ActionMapping pMapping, HttpServletRequest pRequest) {
	      super.reset(pMapping, pRequest);
	      
	      fundingId = "";
	      facilityId = "";
	      awardDate = "";
	      fundingType = "";
	      fundingAgency = "";
	      loanNumber = "";
	      fundingSource = "";
	      fundingTotal = "";
	      percentbyCwsrf = "";
	      fundingAction = "";
	      
	}
	public void reset() {	      
	      
	      fundingId = "";
	      awardDate = "";
	      fundingType = "";
	      fundingAgency = "";
	      loanNumber = "";
	      fundingSource = "";
	      fundingTotal = "";
	      percentbyCwsrf = "";
	      
	}
	public void popForm(FundingSource fs){
		if (fs == null) return;
		
		fundingId = "";
		facilityId = "";
		awardDate = "";
	    fundingType = "";
	    fundingAgency = "";
	    loanNumber = "";
	    fundingSource = "";
	    fundingTotal = "";
	    percentbyCwsrf = "";	    
	    
	    fundingId = "" + fs.getFundingSourceId();
	    
	    if(fs.getFacility() != null)
	    	facilityId = "" + fs.getFacility().getFacilityId();  
				
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");		
		
		if(fs.getAwardDate() != null)
			awardDate = sdf.format(fs.getAwardDate());
		
		if(fs.getLoanTypeRef() != null)
			fundingType = fs.getLoanTypeRef().getLoanTypeId();
	    
		if(fs.getFundingAgencyRef() != null)
			fundingAgency = fs.getFundingAgencyRef().getFundingAgencyId();
	    
		if(fs.getFundingSourceTypeRef() != null)
			fundingSource = fs.getFundingSourceTypeRef().getFundingSourceTypeId();
	    
		if(fs.getLoanNumber() != null)
	    loanNumber = fs.getLoanNumber();
		
		if(fs.getAwardedAmount() != null)
			fundingTotal = "" + fs.getAwardedAmount().longValue();
		
		if(fs.getPercentageFundedBySrf() != null)
			percentbyCwsrf = "" + fs.getPercentageFundedBySrf().longValue();
				
	}
	public FundingSource popFundingSource(String userid){
		
		FundingSource fs = new FundingSource();
		
		//fs.setLastUpdateUserid(userid);
		fs.setLastUpdateUserid("FPLATTEN");
		fs.setLastUpdateTs(new Date());
		
		if(!fundingId.equals(""))
			fs.setFundingSourceId(new Long(fundingId).longValue());	    
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");		
		
		try {
			if(!awardDate.equals(""))
				fs.setAwardDate(sdf.parse(awardDate));
		} catch (ParseException e) {
			// TODO: add error to log file
		}		
		
		if(!loanNumber.equals(""))
		    fs.setLoanNumber(loanNumber);
			
		if(!fundingTotal.equals(""))
			fs.setAwardedAmount(new Long(fundingTotal));
			
		if(!percentbyCwsrf.equals(""))
			fs.setPercentageFundedBySrf(new Short(percentbyCwsrf));		
		
		
		if(!facilityId.equals("")){
			Facility f = new Facility();
			f.setFacilityId(new Long(facilityId).longValue());
			fs.setFacility(f);
		}		
		
		if(!fundingType.equals("")){
			LoanTypeRef ltr = new LoanTypeRef();
			ltr.setLoanTypeId(fundingType);
			fs.setLoanTypeRef(ltr);
		}
		
		if(!fundingAgency.equals("")){
			FundingAgencyRef far = new FundingAgencyRef();
			far.setFundingAgencyId(fundingAgency);
			fs.setFundingAgencyRef(far);
		}	    
		
		if(!fundingSource.equals("")){
			FundingSourceTypeRef fstr = new FundingSourceTypeRef();
			fstr.setFundingSourceTypeId(fundingSource);
			fs.setFundingSourceTypeRef(fstr);
		}
		
		return fs;
	}
	
}
