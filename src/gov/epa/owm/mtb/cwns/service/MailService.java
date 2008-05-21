package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.model.CwnsUser;


/**
 * This class provides business functionality related to user management. 
 * @author Matt Connors
 *
 */
public interface MailService {	

    public boolean notifyUserRequestReceived(String cwnsUserId);    
    
    public boolean notifyUserOfApproval(String cwnsUserId,boolean approvePortalAccount);

    public boolean notifyUserDenied(String cwnsUserId);    
}
