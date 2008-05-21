/**
 * 
 */
package gov.epa.owm.mtb.cwns.facilitySelect;

import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityDischarge;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts.action.ActionForm;

/**
 * @author Matt Connors
 *
 */
public class UMFacilitySelectForm extends ActionForm {
	
	private String action ="";
	private String locationId="";
	private String keyword="";
	private boolean thisStateOnly=false;
	private int    listSize;
	private int    displaySize;
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public int getDisplaySize() {
		return displaySize;
	}
	public void setDisplaySize(int displaySize) {
		this.displaySize = displaySize;
	}
	public int getListSize() {
		return listSize;
	}
	public void setListSize(int listSize) {
		this.listSize = listSize;
	}
	public boolean isThisStateOnly() {
		return thisStateOnly;
	}
	public void setThisStateOnly(boolean thisStateOnly) {
		this.thisStateOnly = thisStateOnly;
	}

}
