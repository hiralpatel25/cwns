package gov.epa.owm.mtb.cwns.manageCostCurve;

import java.util.Collection;

public class ManageCostCurveListHelper {

	private long facilityCostCurveId = -999;
	
	private long documentId = -999;
	
	private long costCurveId = -999;
	
	private long costId = -999;
	
	private String costCurveCode = "";
	
	private String costCurveName = "";
	
	private String costAllocated = "";
	
	private String assignedOrRunCode = "";
	
	private String curveRerunFlag = "";
	
	private String disableCostCurve = "N";
	
	private Collection errorDataAreaNames = null;

	/**
	 * @return the costAllocated
	 */
	public String getCostAllocated() {
		return costAllocated;
	}

	/**
	 * @param costAllocated the costAllocated to set
	 */
	public void setCostAllocated(String costAllocated) {
		this.costAllocated = costAllocated;
	}

	/**
	 * @return the facilityCostCurveId
	 */
	public long getFacilityCostCurveId() {
		return facilityCostCurveId;
	}

	/**
	 * @param facilityCostCurveId the facilityCostCurveId to set
	 */
	public void setFacilityCostCurveId(long facilityCostCurveId) {
		this.facilityCostCurveId = facilityCostCurveId;
	}

	/**
	 * @return the costCurveName
	 */
	public String getCostCurveName() {
		return costCurveName;
	}

	/**
	 * @param costCurveName the costCurveName to set
	 */
	public void setCostCurveName(String costCurveName) {
		this.costCurveName = costCurveName;
	}

	/**
	 * @return the errorDataAreaNames
	 */
	public Collection getErrorDataAreaNames() {
		return errorDataAreaNames;
	}

	/**
	 * @param errorDataAreaNames the errorDataAreaNames to set
	 */
	public void setErrorDataAreaNames(Collection errorDataAreaNames) {
		this.errorDataAreaNames = errorDataAreaNames;
	}

	/**
	 * @return the disableCostCurve
	 */
	public String getDisableCostCurve() {
		return disableCostCurve;
	}

	/**
	 * @param disableCostCurve the disableCostCurve to set
	 */
	public void setDisableCostCurve(String disableCostCurve) {
		this.disableCostCurve = disableCostCurve;
	}

	/**
	 * @return the costCurveCode
	 */
	public String getCostCurveCode() {
		return costCurveCode;
	}

	/**
	 * @param costCurveCode the costCurveCode to set
	 */
	public void setCostCurveCode(String costCurveCode) {
		this.costCurveCode = costCurveCode;
	}

	/**
	 * @return the costCurveId
	 */
	public long getCostCurveId() {
		return costCurveId;
	}

	/**
	 * @param costCurveId the costCurveId to set
	 */
	public void setCostCurveId(long costCurveId) {
		this.costCurveId = costCurveId;
	}

	/**
	 * @return the documentId
	 */
	public long getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the assignedOrRunCode
	 */
	public String getAssignedOrRunCode() {
		return assignedOrRunCode;
	}

	/**
	 * @param assignedOrRunCode the assignedOrRunCode to set
	 */
	public void setAssignedOrRunCode(String assignedOrRunCode) {
		this.assignedOrRunCode = assignedOrRunCode;
	}

	/**
	 * @return the curveRerunFlag
	 */
	public String getCurveRerunFlag() {
		return curveRerunFlag;
	}

	/**
	 * @param curveRerunFlag the curveRerunFlag to set
	 */
	public void setCurveRerunFlag(String curveRerunFlag) {
		this.curveRerunFlag = curveRerunFlag;
	}

	/**
	 * @return the costId
	 */
	public long getCostId() {
		return costId;
	}

	/**
	 * @param costId the costId to set
	 */
	public void setCostId(long costId) {
		this.costId = costId;
	}
	

}