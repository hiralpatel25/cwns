package gov.epa.owm.mtb.cwns.facilityInformation;

import java.util.Collection;

import org.apache.struts.validator.ValidatorActionForm;

public class FacilityAddressForm extends ValidatorActionForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long facilityId;
	private String address1;
	private String address2;
	private String city;
	private String state = "";
	private String zipCode;
	private Collection states;
	private char isSourcedFromNPDES;
	private char isUpdatable = 'N';
	private String showWarningMessage = "N";
		
	public char getIsUpdatable() {
		return isUpdatable;
	}
	public void setIsUpdatable(char isUpdatable) {
		this.isUpdatable = isUpdatable;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public char getIsSourcedFromNPDES() {
		return isSourcedFromNPDES;
	}
	public void setIsSourcedFromNPDES(char isSourcedFromNPDES) {
		this.isSourcedFromNPDES = isSourcedFromNPDES;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public Collection getStates() {
		return states;
	}
	public void setStates(Collection states) {
		this.states = states;
	}
	public Long getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}
	public String getShowWarningMessage() {
		return showWarningMessage;
	}
	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}
	

}
