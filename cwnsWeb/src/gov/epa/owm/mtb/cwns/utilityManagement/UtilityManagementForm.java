package gov.epa.owm.mtb.cwns.utilityManagement;

import org.apache.struts.action.ActionForm;

public class UtilityManagementForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	public static final long INITIAL_LONG_VALUE = -999;
	
	private String utilityManagementAct = "";

	private String isUpdatable = "Y";

	/*
	 * this string should be passed back as a list that contains items in the format of
	 * utilMgmtPracticeId:utilMgmtImplmntStatusId:remainingCostToImplement:annualCostToOperate
	 */
	private String resultListString = "";

	private long utilityManagementFacilityId = INITIAL_LONG_VALUE;	
	
	public long getUtilityManagementFacilityId() {
		return utilityManagementFacilityId;
	}

	public void setUtilityManagementFacilityId(long utilityManagementFacilityId) {
		this.utilityManagementFacilityId = utilityManagementFacilityId;
	}

	public String getIsUpdatable() {
		return isUpdatable;
	}

	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	public String getResultListString() {
		return resultListString;
	}

	public void setResultListString(String resultListString) {
		this.resultListString = resultListString;
	}

	public String getUtilityManagementAct() {
		return utilityManagementAct;
	}

	public void setUtilityManagementAct(String utilityManagementAct) {
		this.utilityManagementAct = utilityManagementAct;
	}

}
