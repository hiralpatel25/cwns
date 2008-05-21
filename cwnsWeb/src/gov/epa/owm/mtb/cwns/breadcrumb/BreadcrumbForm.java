package gov.epa.owm.mtb.cwns.breadcrumb;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

// import gov.epa.owm.mtb.cwns.model.Facility;

public class BreadcrumbForm extends ActionForm {

	private String cwnsNbr = "";
	
	private String facilityName = "";

	/**
	 * @return the cwnsNbr
	 */
	public String getCwnsNbr() {
		return cwnsNbr;
	}

	/**
	 * @param cwnsNbr the cwnsNbr to set
	 */
	public void setCwnsNbr(String cwnsNbr) {
		this.cwnsNbr = cwnsNbr;
	}

	/**
	 * @return the facilityName
	 */
	public String getFacilityName() {
		return facilityName;
	}

	/**
	 * @param facilityName the facilityName to set
	 */
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

}