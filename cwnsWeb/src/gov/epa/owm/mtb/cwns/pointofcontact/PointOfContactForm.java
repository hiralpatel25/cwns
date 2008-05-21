/**
 * 
 */
package gov.epa.owm.mtb.cwns.pointofcontact;

import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityPointOfContact;
import gov.epa.owm.mtb.cwns.model.PointOfContact;

import java.util.ArrayList;
import java.util.Collection;

import oracle.security.jazn.util.FormattedWriter;

import org.apache.struts.action.ActionForm;

/**
 * @author Matt Connors
 *
 */
public class PointOfContactForm extends ActionForm {
	
	private String action ="";
	private String authorityName="";
	private String contactName="";
	private String title="";
	private String phone="";
	private String phoneFormated="";
	private String fax="";
	private String faxFormated="";
	private String address1="";
	private String address2="";
	private String city="";
	private String stateId="";
	private String countyName="";
	private String zip="";
	private String email="";
	private String tribe="N";
	private String responsibleEntity="";
	private String sourcedFromNpdes="";
	
	private String facilityId="";
	private long primaryPocId;
	private String editPocId="";

	private boolean superfundPossible;
	private String superfundResponsibleParty="";
	
	
	private String displayDetails="N";
	private boolean updateable;
	
	public boolean isUpdateable() {
		return updateable;
	}

	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}

	/**
	 * Clear all the form bean attributes except facilityId and stateId. 
	 *
	 */
	public void clear() {
		
		action ="";
		authorityName="";
		contactName="";
		title="";
		phone="";
		fax="";
		address1="";
		address2="";
		city="";
		countyName="";
		zip="";
		email="";
		tribe="N";
		superfundResponsibleParty="";
		responsibleEntity="";
		sourcedFromNpdes="";
		editPocId="";
		displayDetails="N";		
	}
	public String getEditPocId() {
		return editPocId;
	}

	public void setEditPocId(String editPocId) {
		this.editPocId = editPocId;
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

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyId) {
		this.countyName = countyId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
		setFaxFormated(formatPhoneNumber(fax));
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
		setPhoneFormated(formatPhoneNumber(phone));
	
	}

	/**
	 * If the phoneNumber is just a string of digits return it formatted.
	 * @param phone
	 * @return
	 */
	public String formatPhoneNumber(String phone) {

		String formattedPhone = phone;
		
		if (phone != null && 
			phone.trim().length()== 10 &&
			isStringAllDigits(phone)) {
			
			String p        = phone.trim();
			String areaCode = p.substring(0, 3);
			String exch     = p.substring(3,6);
			String num		= p.substring(6,10);
			
			formattedPhone = "("+areaCode+") "+exch+"-"+num;
		}		
		return formattedPhone;
	}
	
	
	/**
	 * Return true if the String contains only numeric values.
	 */
	private boolean isStringAllDigits(String s) {
		try	{
			Long.parseLong(s);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	
	public String getSuperfundResponsibleParty() {
		return superfundResponsibleParty;
	}

	public void setSuperfundResponsibleParty(String superfundResponsibleParty) {
		this.superfundResponsibleParty = superfundResponsibleParty;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTribe() {
		return tribe;
	}

	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getPrimaryPocId() {
		return primaryPocId;
	}

	public void setPrimaryPocId(long primaryPocId) {
		this.primaryPocId = primaryPocId;
	}

	public String getResponsibleEntity() {
		return responsibleEntity;
	}

	public void setResponsibleEntity(String responsibleEntity) {
		this.responsibleEntity = responsibleEntity;
	}

	public String getSourcedFromNpdes() {
		return sourcedFromNpdes;
	}

	public void setSourcedFromNpdes(String sourcedFromNpdes) {
		this.sourcedFromNpdes = sourcedFromNpdes;
	}

	public String getDisplayDetails() {
		return displayDetails;
	}

	public void setDisplayDetails(String displayDetails) {
		this.displayDetails = displayDetails;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getPhoneFormated() {
		return phoneFormated;
	}

	public void setPhoneFormated(String phoneFormated) {
		this.phoneFormated = phoneFormated;
	}

	public String getFaxFormated() {
		return faxFormated;
	}

	public void setFaxFormated(String faxFormated) {
		this.faxFormated = faxFormated;
	}

	public boolean isSuperfundPossible() {
		return superfundPossible;
	}

	public void setSuperfundPossible(boolean superfundPossible) {
		this.superfundPossible = superfundPossible;
	}

}
