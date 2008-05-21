package gov.epa.owm.mtb.cwns.utilityManagement;

import org.apache.struts.action.ActionForm;

public class UtilityManagementStatusHelper extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	public static final long INITIAL_LONG_VALUE = -999;

	private long utilMgmtStatusId = INITIAL_LONG_VALUE;
	
	private String utilMgmtStatusName = "";

	public long getUtilMgmtStatusId() {
		return utilMgmtStatusId;
	}

	public void setUtilMgmtStatusId(long utilMgmtStatusId) {
		this.utilMgmtStatusId = utilMgmtStatusId;
	}

	public String getUtilMgmtStatusName() {
		return utilMgmtStatusName;
	}

	public void setUtilMgmtStatusName(String utilMgmtStatusName) {
		this.utilMgmtStatusName = utilMgmtStatusName;
	}

}
