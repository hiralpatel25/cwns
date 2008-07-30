package gov.epa.owm.mtb.cwns.utilityManagement;

import org.apache.struts.action.ActionForm;

public class UtilityManagementHelper extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	public static final long INITIAL_LONG_VALUE = -999;

	private long utilMgmtPracticeId = INITIAL_LONG_VALUE;
	
	private String utilMgmtPracticeName = "";

	private long utilMgmtImplmntStatusId = INITIAL_LONG_VALUE ;

	private String utilMgmtImplmntStatusName = "" ;

	private long remainingCostToImplement = INITIAL_LONG_VALUE ;

	private long annualCostToOperate = INITIAL_LONG_VALUE ;
	
	private String isRemainingCostDisabled = "Y";
	private String isAnnualCostToOperatetDisabled = "Y";
	

	public String getIsAnnualCostToOperatetDisabled() {
		return isAnnualCostToOperatetDisabled;
	}

	public void setIsAnnualCostToOperatetDisabled(
			String isAnnualCostToOperatetDisabled) {
		this.isAnnualCostToOperatetDisabled = isAnnualCostToOperatetDisabled;
	}

	public String getIsRemainingCostDisabled() {
		return isRemainingCostDisabled;
	}

	public void setIsRemainingCostDisabled(String isRemainingCostDisabled) {
		this.isRemainingCostDisabled = isRemainingCostDisabled;
	}

	public long getAnnualCostToOperate() {
		return annualCostToOperate;
	}

	public void setAnnualCostToOperate(long annualCostToOperate) {
		this.annualCostToOperate = annualCostToOperate;
	}

	public long getRemainingCostToImplement() {
		return remainingCostToImplement;
	}

	public void setRemainingCostToImplement(long remainingCostToImplement) {
		this.remainingCostToImplement = remainingCostToImplement;
	}

	public long getUtilMgmtImplmntStatusId() {
		return utilMgmtImplmntStatusId;
	}

	public void setUtilMgmtImplmntStatusId(long utilMgmtImplmntStatusId) {
		this.utilMgmtImplmntStatusId = utilMgmtImplmntStatusId;
	}

	public String getUtilMgmtImplmntStatusName() {
		return utilMgmtImplmntStatusName;
	}

	public void setUtilMgmtImplmntStatusName(String utilMgmtImplmntStatusName) {
		this.utilMgmtImplmntStatusName = utilMgmtImplmntStatusName;
	}

	public long getUtilMgmtPracticeId() {
		return utilMgmtPracticeId;
	}

	public void setUtilMgmtPracticeId(long utilMgmtPracticeId) {
		this.utilMgmtPracticeId = utilMgmtPracticeId;
	}

	public String getUtilMgmtPracticeName() {
		return utilMgmtPracticeName;
	}

	public void setUtilMgmtPracticeName(String utilMgmtPracticeName) {
		this.utilMgmtPracticeName = utilMgmtPracticeName;
	}
	


}
