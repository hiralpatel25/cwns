package gov.epa.owm.mtb.cwns.unitProcess;

import org.apache.struts.action.ActionForm;

public class UnitProcessForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	public static final long INITIAL_LONG_VALUE = -999;
	
	private String unitProcessAct = "";

	private String isUpdatable = "Y";

	private String detailEditExpand = "N";
	
	private String isAddAction = "N";
	
	private short currentYear = 2008;
	
	private long unitProcessFacilityId = INITIAL_LONG_VALUE;	
	
	private long treatmentTypeId = INITIAL_LONG_VALUE;
	
	private long unitProcessListId = INITIAL_LONG_VALUE;
	
	private long changeTypeListId = INITIAL_LONG_VALUE;
	
	private long unitProcessRadioId = INITIAL_LONG_VALUE;
	
	//form
	//---------------------------------------------------
	private long unitProcessFormId = INITIAL_LONG_VALUE;
	private String unitProcessFormName = "";
	
	private String[] selectedChangeType = {};
	
	
	private String warnStateUser = "N";
	
	private String[] availChangeType = {};
	
	private String commaDilimitedChangeTypeIds = "";
	
	private String description = "";
	
	//1 - present; 2 - projected; 3 - present & projected
	private int presentProjectedFlag = 3;
	
	private short planYear = 0;
	
	private char backupFlag = 'N';

	//---------------------------------------------------

	public void initForm() {
		
		unitProcessFormId = INITIAL_LONG_VALUE;
		unitProcessRadioId = INITIAL_LONG_VALUE;
		description = "";
		presentProjectedFlag = 3;
		planYear = 0;
		backupFlag = 'N';
		unitProcessFormName = "";
		detailEditExpand = "N";
		isAddAction = "N";		
		warnStateUser = "N";
	}

	public char getBackupFlag() {
		return backupFlag;
	}

	public void setBackupFlag(char backupFlag) {
		this.backupFlag = backupFlag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public short getPlanYear() {
		return planYear;
	}

	public void setPlanYear(short planYear) {
		this.planYear = planYear;
	}

	public int getPresentProjectedFlag() {
		return presentProjectedFlag;
	}

	public void setPresentProjectedFlag(int presentProjectedFlag) {
		this.presentProjectedFlag = presentProjectedFlag;
	}

	public long getChangeTypeListId() {
		return changeTypeListId;
	}


	public void setChangeTypeListId(long changeTypeListId) {
		this.changeTypeListId = changeTypeListId;
	}


	public String getDetailEditExpand() {
		return detailEditExpand;
	}


	public void setDetailEditExpand(String detailEditExpand) {
		this.detailEditExpand = detailEditExpand;
	}


	public String getIsAddAction() {
		return isAddAction;
	}


	public void setIsAddAction(String isAddAction) {
		this.isAddAction = isAddAction;
	}


	public String getIsUpdatable() {
		return isUpdatable;
	}


	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}


	public long getTreatmentTypeId() {
		return treatmentTypeId;
	}


	public void setTreatmentTypeId(long treatmentTypeId) {
		this.treatmentTypeId = treatmentTypeId;
	}


	public String getUnitProcessAct() {
		return unitProcessAct;
	}


	public void setUnitProcessAct(String unitProcessAct) {
		this.unitProcessAct = unitProcessAct;
	}


	public long getUnitProcessFacilityId() {
		return unitProcessFacilityId;
	}


	public void setUnitProcessFacilityId(long unitProcessFacilityId) {
		this.unitProcessFacilityId = unitProcessFacilityId;
	}


	public long getUnitProcessFormId() {
		return unitProcessFormId;
	}


	public void setUnitProcessFormId(long unitProcessFormId) {
		this.unitProcessFormId = unitProcessFormId;
	}


	public long getUnitProcessListId() {
		return unitProcessListId;
	}


	public void setUnitProcessListId(long unitProcessListId) {
		this.unitProcessListId = unitProcessListId;
	}

	public long getUnitProcessRadioId() {
		return unitProcessRadioId;
	}

	public void setUnitProcessRadioId(long unitProcessRadioId) {
		this.unitProcessRadioId = unitProcessRadioId;
	}

	public String getUnitProcessFormName() {
		return unitProcessFormName;
	}

	public void setUnitProcessFormName(String unitProcessFormName) {
		this.unitProcessFormName = unitProcessFormName;
	}

	public String[] getSelectedChangeType() {
		return selectedChangeType;
	}

	public void setSelectedChangeType(String[] selectedChangeType) {
		this.selectedChangeType = selectedChangeType;
	}

	public String[] getAvailChangeType() {
		return availChangeType;
	}

	public void setAvailChangeType(String[] availChangeType) {
		this.availChangeType = availChangeType;
	}

	public String getCommaDilimitedChangeTypeIds() {
		return commaDilimitedChangeTypeIds;
	}

	public void setCommaDilimitedChangeTypeIds(String commaDilimitedChangeTypeIds) {
		this.commaDilimitedChangeTypeIds = commaDilimitedChangeTypeIds;
	}

	public short getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(short currentYear) {
		this.currentYear = currentYear;
	}

	public String getWarnStateUser() {
		return warnStateUser;
	}

	public void setWarnStateUser(String warnStateUser) {
		this.warnStateUser = warnStateUser;
	}

	
	
}
