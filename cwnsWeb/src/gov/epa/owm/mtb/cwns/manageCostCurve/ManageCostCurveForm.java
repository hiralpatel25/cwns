package gov.epa.owm.mtb.cwns.manageCostCurve;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class ManageCostCurveForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	public static final long INITIAL_LONG_VALUE = -999;
	
	private String manageCostCurveAct = "";

	private String isUpdatable = "Y";

	private String atLeastOneEnabled = "";
	
	private long facilityCostCurveId = INITIAL_LONG_VALUE;

	private String costCurveName = "";
	
	private long manageCostCurveFacilityId = INITIAL_LONG_VALUE;

	private long documentId = INITIAL_LONG_VALUE;

	private Collection ccCatVHelpers = new ArrayList();	
	
	private String[] assignedFacilityCostCurveIds = {}; 	
	
	private String[] updatedFacilityCostCurveIds = {};
	/**
	 * @return the updatedFacilityCostCurveIds
	 */
	public String[] getUpdatedFacilityCostCurveIds() {
		return updatedFacilityCostCurveIds;
	}

	/**
	 * @param updatedFacilityCostCurveIds the updatedFacilityCostCurveIds to set
	 */
	public void setUpdatedFacilityCostCurveIds(String[] updatedFacilityCostCurveIds) {
		this.updatedFacilityCostCurveIds = updatedFacilityCostCurveIds;
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
	 * @return the isUpdatable
	 */
	public String getIsUpdatable() {
		return isUpdatable;
	}

	/**
	 * @param isUpdatable the isUpdatable to set
	 */
	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	/**
	 * @return the manageCostCurveAct
	 */
	public String getManageCostCurveAct() {
		return manageCostCurveAct;
	}

	/**
	 * @param manageCostCurveAct the manageCostCurveAct to set
	 */
	public void setManageCostCurveAct(String manageCostCurveAct) {
		this.manageCostCurveAct = manageCostCurveAct;
	}

	/**
	 * @return the manageCostCurveFacilityId
	 */
	public long getManageCostCurveFacilityId() {
		return manageCostCurveFacilityId;
	}

	/**
	 * @param manageCostCurveFacilityId the manageCostCurveFacilityId to set
	 */
	public void setManageCostCurveFacilityId(long manageCostCurveFacilityId) {
		this.manageCostCurveFacilityId = manageCostCurveFacilityId;
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
	 * @return the assignedFacilityCostCurveIds
	 */
	public String[] getAssignedFacilityCostCurveIds() {
		return assignedFacilityCostCurveIds;
	}

	/**
	 * @param assignedFacilityCostCurveIds the assignedFacilityCostCurveIds to set
	 */
	public void setAssignedFacilityCostCurveIds(
			String[] assignedFacilityCostCurveIds) {
		this.assignedFacilityCostCurveIds = assignedFacilityCostCurveIds;
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
	 * @return the atLeastOneEnabled
	 */
	public String getAtLeastOneEnabled() {
		return atLeastOneEnabled;
	}

	/**
	 * @param atLeastOneEnabled the atLeastOneEnabled to set
	 */
	public void setAtLeastOneEnabled(String atLeastOneEnabled) {
		this.atLeastOneEnabled = atLeastOneEnabled;
	}

	/**
	 * @return the ccCatVHelpers
	 */
	public Collection getCcCatVHelpers() {
		return ccCatVHelpers;
	}

	/**
	 * @param ccCatVHelpers the ccCatVHelpers to set
	 */
	public void setCcCatVHelpers(Collection ccCatVHelpers) {
		this.ccCatVHelpers = ccCatVHelpers;
	}
	
}
