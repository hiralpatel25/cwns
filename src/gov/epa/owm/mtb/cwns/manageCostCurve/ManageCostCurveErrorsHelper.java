package gov.epa.owm.mtb.cwns.manageCostCurve;

import gov.epa.owm.mtb.cwns.navigationTabs.NavigationTabsListHelper;

public class ManageCostCurveErrorsHelper {

	private String errorDataAreaName = "";
	
	private String navTabText = "";
	
	private String active = "";

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @return the navTabText
	 */
	public String getNavTabText() {
		return navTabText;
	}

	/**
	 * @param navTabText the navTabText to set
	 */
	public void setNavTabText(String navTabText) {
		this.navTabText = navTabText;
	}

	/**
	 * @return the errorDataAreaName
	 */
	public String getErrorDataAreaName() {
		return errorDataAreaName;
	}

	/**
	 * @param errorDataAreaName the errorDataAreaName to set
	 */
	public void setErrorDataAreaName(String errorDataAreaName) {
		this.errorDataAreaName = errorDataAreaName;
	}

}