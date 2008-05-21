package gov.epa.owm.mtb.cwns.service.impl;


/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */

import java.text.MessageFormat;

import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.service.MailService;
import gov.epa.owm.mtb.cwns.service.UserService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;


public class MailServiceImpl  extends CWNSService implements MailService {
	
    private static Log log 		= LogFactory.getLog(MailServiceImpl.class);
	private String enabled 		= CWNSProperties.getProperty("mail.enable");
	private boolean mailEnabled = (enabled != null && "true".equalsIgnoreCase(enabled));
    
    public boolean notifyUserOfApproval(String cwnsUserId, boolean portalAccountCreated) {    

    	CwnsUser cwnsUser = userService.findUserByUserId(cwnsUserId);
    	
    	if (portalAccountCreated) {
    		sendApprovalWithPortalAccount(cwnsUser);
    		
    	} else {
    		sendApprovalWithoutPortalAccount(cwnsUser);
    	}
    	
    	return true;
    }
	
    /**
     * Send and email to the user notifying them we have received their request 
     * to access the CWNS system.
     */
    public boolean notifyUserRequestReceived(String cwnsUserId) {

    	CwnsUser cwnsUser = userService.findUserByUserId(cwnsUserId);
    	
    	// Subject line
    	String subj = CWNSProperties.getProperty("mail.notify.user.subj");

    	/* Build the message text */
    	String text = CWNSProperties.getProperty("mail.notify.user.text");
        
        // Days to review
    	//String daysToReview = CWNSProperties.getProperty("mail.notify.days.to.review");
        //Object daysToReviewArgs[] = {daysToReview}; 
        //String completeText = MessageFormat.format(text, daysToReviewArgs);

        return sendMail(cwnsUser, subj, text);
    }
    
    /**
     * Send an email to the user notifying them that they now have access to the 
     * CWNS System. This method is used when a Portal account had to be created.
     * @param cwnsUser
     * @return
     */
	private boolean sendApprovalWithPortalAccount(CwnsUser cwnsUser) {

		// Subject
    	String subj = CWNSProperties.getProperty("mail.approve.user.subj");

    	// Opening text
    	String text = CWNSProperties.getProperty("mail.approve.user.new.portal.account.text.opening");

    	// UserId
    	text += CWNSProperties.getProperty("mail.approve.user.new.portal.account.userid");
    	text += " " + cwnsUser.getOidUserid() +"\n";
    	
    	// Password
    	text += CWNSProperties.getProperty("mail.approve.user.new.portal.account.password");
    	text += " " + cwnsUser.getInitialPassword() +"\n\n";
    	
    	// Main text
    	text += CWNSProperties.getProperty("mail.approve.user.new.portal.account.text.main");
    	
    	// Closing text
    	text += CWNSProperties.getProperty("mail.approve.user.new.portal.account.text.closing");
    	
        return sendMail(cwnsUser, subj, text);
	}
    
    
	/**
     * Send an email to the user notifying them that they now have access to the 
     * CWNS System. This method is used when a Portal account was not created.
	 * 
	 * @param cwnsUser
	 * @return
	 */
	private boolean sendApprovalWithoutPortalAccount(CwnsUser cwnsUser) {

		// Subject
    	String subj = CWNSProperties.getProperty("mail.approve.user.subj");

    	// Opening text
    	String text = CWNSProperties.getProperty("mail.approve.user.no.portal.account.text.opening");
    	
    	// Closing text
    	text += CWNSProperties.getProperty("mail.approve.user.no.portal.account.text.closing");
    	
        return sendMail(cwnsUser, subj, text);
	}
    
    
	/**
	 * Notify a person that their request for access to the CWNS account was denied.
	 */
    public boolean notifyUserDenied(String cwnsUserId){

    	CwnsUser cwnsUser = userService.findUserByUserId(cwnsUserId);
    	
    	String subj = CWNSProperties.getProperty("mail.deny.user.subj");
    	String text = CWNSProperties.getProperty("mail.deny.user.text");

        return sendMail(cwnsUser, subj, text);
    }
    
   
    /**
     * Build and send the SMTP email.
     * @param cwnsUser
     * @param subject
     * @param text
     * @return
     */
    private boolean sendMail(CwnsUser cwnsUser,String subject, String text) {

    	String userName = cwnsUser.getFirstName() + " " +  cwnsUser.getLastName();
    	String to		= CWNSProperties.getProperty("mail.to");  // for testing only
    	String from    	= CWNSProperties.getProperty("mail.from"); 
    	
        //Create a thread safe "sandbox" of the message
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage(this.message);
        
    	// To
        if (to != null && to.length() > 0) {
        	simpleMailMessage.setTo(to);
        } else {
        	simpleMailMessage.setTo(cwnsUser.getEmailAddress());
        }
    	
    	// From
    	simpleMailMessage.setFrom(from);
    	
    	// Subject
    	simpleMailMessage.setSubject(subject);

    	// Greeting
    	String salutation = CWNSProperties.getProperty("mail.greeting");
        Object[] arguments = {userName}; 
        String greeting = MessageFormat.format(salutation, arguments);

        
        // Email signature
        String signature = CWNSProperties.getProperty("mail.signature");
        
        // Put it all together
        simpleMailMessage.setText(greeting + text+signature);

        // Send it!
        if (mailEnabled) {
	        try{
	            mailSender.send(simpleMailMessage);
	        }
	        catch(MailException ex) {
	            //log it and go on
	        	// TODO: what do we do here ???????
	            System.err.println(ex.getMessage());            
	        }
        }
    	return true;
    }
    
    
    public MailServiceImpl() {
    }

    private MailSender mailSender;
	public void setMailSender(MailSender mailSender) {
			this.mailSender = mailSender;
	}
	
    private SimpleMailMessage message;
    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }
	
    /* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }
    
    
	
	
}
