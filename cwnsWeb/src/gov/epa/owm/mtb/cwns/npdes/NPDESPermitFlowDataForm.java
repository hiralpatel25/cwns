package gov.epa.owm.mtb.cwns.npdes;

import java.util.Date;

import org.apache.struts.validator.ValidatorActionForm;

public class NPDESPermitFlowDataForm extends ValidatorActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long facilityId;
	private String startDate = "";
	private String endDate = (new Date()).toString();
	private String efPipeSchedId = "";
	private String efNpdesPermitNumber = "";
	private float preDesignFlowRate;
	private String action = "";
	private String search = "enable";
	private boolean displayOnly = false;
	private String isUnitOfMeasureSame = "";
	
	public String getIsUnitOfMeasureSame() {
		return isUnitOfMeasureSame;
	}
	public void setIsUnitOfMeasureSame(String isUnitOfMeasureSame) {
		this.isUnitOfMeasureSame = isUnitOfMeasureSame;
	}
	public boolean isDisplayOnly() {
		return displayOnly;
	}
	public void setDisplayOnly(boolean displayOnly) {
		this.displayOnly = displayOnly;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public float getPreDesignFlowRate() {
		return preDesignFlowRate;
	}
	public void setPreDesignFlowRate(float preDesignFlowRate) {
		this.preDesignFlowRate = preDesignFlowRate;
	}
	public String getEfNpdesPermitNumber() {
		return efNpdesPermitNumber;
	}
	public void setEfNpdesPermitNumber(String efNpdesPermitNumber) {
		this.efNpdesPermitNumber = efNpdesPermitNumber;
	}
	public String getEfPipeSchedId() {
		return efPipeSchedId;
	}
	public void setEfPipeSchedId(String efPipeSchedId) {
		this.efPipeSchedId = efPipeSchedId;
	}
		public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Long getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}
