package gov.epa.owm.mtb.cwns.unitProcess;

import java.util.List;

public class UnitProcessListHelper {
	
	private long treatmentTypeId = -999;

	private String treatmentTypeName = "";
	
	private int numberOfUnitProcesses = 0;
	
	private List unitProcessList = null;

	public long getTreatmentTypeId() {
		return treatmentTypeId;
	}

	public void setTreatmentTypeId(long treatmentTypeId) {
		this.treatmentTypeId = treatmentTypeId;
	}

	public String getTreatmentTypeName() {
		return treatmentTypeName;
	}

	public void setTreatmentTypeName(String treatmentTypeName) {
		this.treatmentTypeName = treatmentTypeName;
	}

	public List getUnitProcessList() {
		return unitProcessList;
	}

	public void setUnitProcessList(List unitProcessList) {
		this.unitProcessList = unitProcessList;
	}

	public int getNumberOfUnitProcesses() {
		return numberOfUnitProcesses;
	}

	public void setNumberOfUnitProcesses(int numberOfUnitProcesses) {
		this.numberOfUnitProcesses = numberOfUnitProcesses;
	}
	
}
