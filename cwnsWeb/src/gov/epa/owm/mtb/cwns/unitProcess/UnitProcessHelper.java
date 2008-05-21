package gov.epa.owm.mtb.cwns.unitProcess;

import java.util.List;


public class UnitProcessHelper {
	
	private long unitProcessId = -999;

	private String unitProcessName = "";
	
	private char presentFlag;

	private char projectedFlag;

	private char backupFlag;

	private short plannedYear = -999;

	private String description = " ";	
	
	private List changeTypeList = null;
	
	private String checked = "";
	
	
	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public List getChangeTypeList() {
		return changeTypeList;
	}

	public void setChangeTypeList(List changeTypeList) {
		this.changeTypeList = changeTypeList;
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



	public short getPlannedYear() {
		return plannedYear;
	}

	public void setPlannedYear(short plannedYear) {
		this.plannedYear = plannedYear;
	}

	public char getPresentFlag() {
		return presentFlag;
	}

	public void setPresentFlag(char presentFlag) {
		this.presentFlag = presentFlag;
	}

	public char getProjectedFlag() {
		return projectedFlag;
	}

	public void setProjectedFlag(char projectedFlag) {
		this.projectedFlag = projectedFlag;
	}

	public long getUnitProcessId() {
		return unitProcessId;
	}

	public void setUnitProcessId(long unitProcessId) {
		this.unitProcessId = unitProcessId;
	}

	public String getUnitProcessName() {
		return unitProcessName;
	}

	public void setUnitProcessName(String unitProcessName) {
		this.unitProcessName = unitProcessName;
	}
	
}
