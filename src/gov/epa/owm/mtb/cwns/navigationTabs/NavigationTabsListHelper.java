package gov.epa.owm.mtb.cwns.navigationTabs;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

public class NavigationTabsListHelper {

	private String active = "false";
	private String tabText;
	private int tabLevel = 1;	
	private boolean isNewLine = false;
	private boolean isSeperator = false;

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
	 * @return the isNewLine
	 */
	public boolean isNewLine() {
		return isNewLine;
	}
	/**
	 * @param isNewLine the isNewLine to set
	 */
	public void setNewLine(boolean isNewLine) {
		this.isNewLine = isNewLine;
	}
	/**
	 * @return the isSeperator
	 */
	public boolean isSeperator() {
		return isSeperator;
	}
	/**
	 * @param isSeperator the isSeperator to set
	 */
	public void setSeperator(boolean isSeperator) {
		this.isSeperator = isSeperator;
	}
	/**
	 * @return the tabLevel
	 */
	public int getTabLevel() {
		return tabLevel;
	}
	/**
	 * @param tabLevel the tabLevel to set
	 */
	public void setTabLevel(int tabLevel) {
		this.tabLevel = tabLevel;
	}
	/**
	 * @return the tabText
	 */
	public String getTabText() {
		return tabText;
	}
	/**
	 * @param tabText the tabText to set
	 */
	public void setTabText(String tabText) {
		this.tabText = tabText;
	}

}
