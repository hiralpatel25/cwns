package gov.epa.owm.mtb.cwns.permits;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.validator.ValidatorActionForm;

public class FacilityPermitForm extends ValidatorActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String isUpdatable = "N";
	private Long facilityId;
	private long permitId;
	private long permitTypeId;
	private String permitType="";
	private String mode = "list";
	private char useData = 'N';
	private String permitNumber="";
	private char type_NPDES = 'N';
	private char confirm='N';
	private String autoPopulate="N";
	private Collection permitTypes = new ArrayList();
	private String currentPermitNbr="";
	
	private Collection permits;

	public Collection getPermits() {
		return permits;
	}

	public void setPermits(Collection permits) {
		this.permits = permits;
	}

	public Long getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}

	public String getIsUpdatable() {
		return isUpdatable;
	}

	public void setIsUpdatable(String isUpdatable) {
		this.isUpdatable = isUpdatable;
	}

	public long getPermitId() {
		return permitId;
	}

	public void setPermitId(long permitId) {
		this.permitId = permitId;
	}

	public String getPermitType() {
		return permitType;
	}

	public void setPermitType(String permitType) {
		this.permitType = permitType;
	}

	public long getPermitTypeId() {
		return permitTypeId;
	}

	public void setPermitTypeId(long permitTypeId) {
		this.permitTypeId = permitTypeId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Collection getPermitTypes() {
		return permitTypes;
	}

	public void setPermitTypes(Collection permitTypes) {
		this.permitTypes = permitTypes;
	}

	public char getUseData() {
		return useData;
	}

	public void setUseData(char useData) {
		this.useData = useData;
	}

	public String getPermitNumber() {
		return permitNumber;
	}

	public void setPermitNumber(String permitNumber) {
		this.permitNumber = permitNumber;
	}

	public char getType_NPDES() {
		return type_NPDES;
	}

	public void setType_NPDES(char type_NPDES) {
		this.type_NPDES = type_NPDES;
	}

	public char getConfirm() {
		return confirm;
	}

	public void setConfirm(char confirm) {
		this.confirm = confirm;
	}

	public String getAutoPopulate() {
		return autoPopulate;
	}

	public void setAutoPopulate(String autoPopulate) {
		this.autoPopulate = autoPopulate;
	}

	public String getCurrentPermitNbr() {
		return currentPermitNbr;
	}

	public void setCurrentPermitNbr(String currentPermitNbr) {
		this.currentPermitNbr = currentPermitNbr;
	}
	
}
