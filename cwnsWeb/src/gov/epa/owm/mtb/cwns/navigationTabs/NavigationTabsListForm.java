package gov.epa.owm.mtb.cwns.navigationTabs;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;


public class NavigationTabsListForm extends ActionForm {

	private Collection navigationTabsHelpers = new ArrayList();
	private String UserType;
	private long facilityID;
	private boolean isFacility = true;
	
	private String linkTabParent = "Needs"; // currently hard code the name, but a place holder fo rthe future use
	private String linkText = "";
	private String hasLink = "N";
	
	/**
	 * @return the hasLink
	 */
	public String getHasLink() {
		return hasLink;
	}

	/**
	 * @param hasLink the hasLink to set
	 */
	public void setHasLink(String hasLink) {
		this.hasLink = hasLink;
	}

	/**
	 * @return the isFacility
	 */
	public boolean isFacility() {
		return isFacility;
	}

	/**
	 * @param isFacility the isFacility to set
	 */
	public void setFacility(boolean isFacility) {
		this.isFacility = isFacility;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return UserType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		UserType = userType;
	}

	public String toString()
	{
		return "\r\n facilityID: " + facilityID + 
		"\r\n navigationTabsHelpers.size(): " + (navigationTabsHelpers==null?-1:navigationTabsHelpers.size());		
 	}

	/**
	 * @return the navigationTabsHelpers
	 */
	public Collection getNavigationTabsHelpers() {
		return navigationTabsHelpers;
	}

	/**
	 * @return the facilityID
	 */
	public long getFacilityID() {
		return facilityID;
	}

	/**
	 * @param facilityID the facilityID to set
	 */
	public void setFacilityID(long facilityID) {
		this.facilityID = facilityID;
	}

	/**
	 * @param navigationTabsHelpers the navigationTabsHelpers to set
	 */
	public void setNavigationTabsHelpers(Collection navigationTabsHelpers) {
		this.navigationTabsHelpers = navigationTabsHelpers;
	}

	/**
	 * @return the linkTabParent
	 */
	public String getLinkTabParent() {
		return linkTabParent;
	}

	/**
	 * @param linkTabParent the linkTabParent to set
	 */
	public void setLinkTabParent(String linkTabParent) {
		this.linkTabParent = linkTabParent;
	}

	/**
	 * @return the linkText
	 */
	public String getLinkText() {
		return linkText;
	}

	/**
	 * @param linkText the linkText to set
	 */
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

}