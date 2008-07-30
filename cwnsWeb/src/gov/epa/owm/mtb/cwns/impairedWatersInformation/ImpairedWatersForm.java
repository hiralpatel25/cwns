package gov.epa.owm.mtb.cwns.impairedWatersInformation;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class ImpairedWatersForm extends ActionForm {
	
	private static final long serialVersionUID = 8860344500979728052L;
	
	private String isUpdatable = "N";

	private String impairedWaterAct = "";
	
	private String impairedWaterFacilityId = "";

	private String listId = "";
	
	private String waterBodyName = "";
	
	private Collection impairedWatersListHelpers = new ArrayList();	

	/**
	 * @return the impairedWatersListHelpers
	 */
	public Collection getImpairedWatersListHelpers() {
		return impairedWatersListHelpers;
	}

	/**
	 * @param impairedWatersListHelpers the impairedWatersListHelpers to set
	 */
	public void setImpairedWatersListHelpers(Collection impairedWatersListHelpers) {
		this.impairedWatersListHelpers = impairedWatersListHelpers;
	}

	/**
	 * @return the listId
	 */
	public String getListId() {
		return listId;
	}

	/**
	 * @param listId the listId to set
	 */
	public void setListId(String listId) {
		this.listId = listId;
	}

	/**
	 * @return the impairedWaterAct
	 */
	public String getImpairedWaterAct() {
		return impairedWaterAct;
	}

	/**
	 * @param impairedWaterAct the impairedWaterAct to set
	 */
	public void setImpairedWaterAct(String impairedWaterAct) {
		this.impairedWaterAct = impairedWaterAct;
	}

	/**
	 * @return the impairedWaterFacilityId
	 */
	public String getImpairedWaterFacilityId() {
		return impairedWaterFacilityId;
	}

	/**
	 * @param impairedWaterFacilityId the impairedWaterFacilityId to set
	 */
	public void setImpairedWaterFacilityId(String impairedWaterFacilityId) {
		this.impairedWaterFacilityId = impairedWaterFacilityId;
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
	 * @return the waterBodyName
	 */
	public String getWaterBodyName() {
		return waterBodyName;
	}

	/**
	 * @param waterBodyName the waterBodyName to set
	 */
	public void setWaterBodyName(String waterBodyName) {
		this.waterBodyName = waterBodyName;
	}


}
