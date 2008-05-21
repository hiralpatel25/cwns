/**
 * 
 */
package gov.epa.owm.mtb.cwns.userlist;

import gov.epa.owm.mtb.cwns.common.search.SortCriteria;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author jlaviset
 *
 */
public class UserListForm extends ActionForm {
	
	private String action ="badbadboy";
	private String searchDescription;
	private int numOfUsers;
	private int frmUser=1;
	private int toUser;
	private int prevUserToDisplay;
	private int nextUserToDisplay;
	private Collection ulHelpers = new ArrayList();
	private String userSortColumn = "name";
	private String currentSelection = "";

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getFrmUser() {
		return frmUser;
	}

	public void setFrmUser(int frmUser) {
		this.frmUser = frmUser;
	}

	public int getNextUserToDisplay() {
		return nextUserToDisplay;
	}

	public void setNextUserToDisplay(int nextUserToDisplay) {
		this.nextUserToDisplay = nextUserToDisplay;
	}

	public int getNumOfUsers() {
		return numOfUsers;
	}

	public void setNumOfUsers(int numOfUsers) {
		this.numOfUsers = numOfUsers;
	}

	public int getPrevUserToDisplay() {
		return prevUserToDisplay;
	}

	public void setPrevUserToDisplay(int prevUserToDisplay) {
		this.prevUserToDisplay = prevUserToDisplay;
	}

	public String getSearchDescription() {
		return searchDescription;
	}

	public void setSearchDescription(String searchDescription) {
		this.searchDescription = searchDescription;
	}

	public String getUserSortColumn() {
		return userSortColumn;
	}

	public void setUserSortColumn(String userSortColumn) {
		this.userSortColumn = userSortColumn;
	}

	public int getToUser() {
		return toUser;
	}

	public void setToUser(int toUser) {
		this.toUser = toUser;
	}

	public Collection getUlHelpers() {
		return ulHelpers;
	}

	public void setUlHelpers(Collection helpers) {
		ulHelpers = helpers;
	}

	public String getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(String cwnsUserId) {
		this.currentSelection = cwnsUserId;
	}

}
