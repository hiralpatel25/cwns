/**
 * 
 */
package gov.epa.owm.mtb.cwns.effluent;

import gov.epa.owm.mtb.cwns.model.Facility;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * @author Matt Connors
 *
 */
public class EffluentForm extends ActionForm {
	
	private String action ="";

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


}
