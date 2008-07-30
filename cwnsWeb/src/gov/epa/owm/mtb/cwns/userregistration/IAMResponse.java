/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
package gov.epa.owm.mtb.cwns.userregistration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class holds response information from IAM. All IAM response information is placed in this 
 * class so that there is a uniform way for the applicaton to handle it.
 * @author mnconnor
 *
 */
public class IAMResponse {

	protected boolean success = false;
	protected String portalUserId;
	protected String requestId;
	protected Collection messages;
	protected String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Clear out all the data in the object.
	 */
	public void reset() {
		success = false;
		portalUserId = "";
		requestId = "";
		messages = new ArrayList();
	}
	
	public IAMResponse() {
		super();
		reset();
	}
	public Collection getMessages() {
		return messages;
	}
	public void setMessages(Collection messages) {
		this.messages = messages;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getPortalUserId() {
		return portalUserId;
	}
	public void setPortalUserId(String portalUserId) {
		this.portalUserId = portalUserId;
	}

}
