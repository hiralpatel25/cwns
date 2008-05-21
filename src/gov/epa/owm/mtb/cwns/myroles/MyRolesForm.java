/**
 * 
 */
package gov.epa.owm.mtb.cwns.myroles;

import org.apache.struts.action.ActionForm;

/**
 * @author jlaviset
 *
 */
public class MyRolesForm extends ActionForm {
	
	private String action ="";
	
	private String locationTypeId;
	private String locationId;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationTypeId() {
		return locationTypeId;
	}

	public void setLocationTypeId(String locationTypeId) {
		this.locationTypeId = locationTypeId;
	}


}
