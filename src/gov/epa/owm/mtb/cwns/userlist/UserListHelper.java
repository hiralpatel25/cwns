/**
 * 
 */
package gov.epa.owm.mtb.cwns.userlist;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mconnors
 *
 */
public class UserListHelper{
	
	private String name;
	private String status;
	private String oidUserId;
	private String cwnsUserId; 
	private Date lastModifiedDate;
	
	public UserListHelper(String name, String cwnsUserId, String oidUserId, String status, Date lastModifiedDate) {
		super();
		this.name = name;
		this.cwnsUserId = cwnsUserId;
		this.oidUserId = oidUserId;
		this.status =status;
		this.lastModifiedDate=lastModifiedDate;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getCwnsUserId() {
		return cwnsUserId;
	}

	public void setCwnsUserId(String cwnsUserId) {
		this.cwnsUserId = cwnsUserId;
	}

	public String getOidUserId() {
		return oidUserId;
	}

	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	
	public String getLastModifiedDateFormatted() {
		String lmd = "";
		if(lastModifiedDate!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); 
			lmd=sdf.format(lastModifiedDate);
		}
		return lmd;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


}
