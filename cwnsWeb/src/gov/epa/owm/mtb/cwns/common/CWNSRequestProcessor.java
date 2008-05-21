/*
 * Created on Feb 2, 2004
 *
 * Enter comment here
 */
package gov.epa.owm.mtb.cwns.common;

import gov.epa.owm.mtb.cwns.service.UserService;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;
import java.util.Enumeration;


/**
 * @author Matt Connors
 */
public class CWNSRequestProcessor extends RequestProcessor {

    protected Logger log = null;
   

    /**
     * Constructor for RequestProcessor.
     */
    public CWNSRequestProcessor() {
        super();
        log = Logger.getLogger(CWNSRequestProcessor.class);
    }


    /**
     * @see org.apache.struts.action.RequestProcessor#processRoles(HttpServletRequest, HttpServletResponse, ActionMapping)
     */
    protected boolean processRoles( HttpServletRequest request, HttpServletResponse response,
    								ActionMapping mapping)
        throws IOException, ServletException {
    	
    	 
//        ServletContext sc = servlet.getServletContext();
 
        HttpSession httpSess = request.getSession(false);
        
        // When the user logs into the Portal the Portal will send the app 
        // server an Http request. This is not the same as the Http request
        // that gets generated for each Portlet. This Http request does not have a "_username" 
        // associated with it and that is how we identify it. The application server processes
        // this request but it does not make it to the application code. 
        // Because of this we do not initialize the User object when we receive this 
        // request. (MNC)
        
        boolean applicationRequest =  request.getParameter("_username")!= null 
        							? true : false;
        
       
        PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
                
        String userLocation = null;
        String userType = null;
        
        //Get Event page parameters
        
        if(prr!=null){
            userLocation = prr.getParameter("userLocation");
            userType = prr.getParameter("userType");        	
        }
        
        //System.out.println(request.getRequestURI());
        
        
        if (applicationRequest) {
			try {
				CurrentUser currentUser = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
				if (currentUser == null) {
					
		        	// If necessary, create a CurrentUser object and attached it to the HTTP session.
					
		        	if (!isPublicUrl(request)) {
		        		currentUser = createUserObject(request);
		        		if (currentUser == null && !isIndependentUrl(request)) {
		        			
							// The user is not recognized by CWNS - send them to an error page.
							RequestDispatcher rd = request.getRequestDispatcher("/noCwnsUserRecordFound.do");
							rd.forward(request,response);
							return false;				        			
		        		}
		        		
		        	}
				} 
				if(currentUser != null){
					
					//If currentUser Object exist
					//verify whether the user has switch role
					if(userLocation != null 
							&& userLocation.length() > 0
							&& userType != null 
							&& userType.length() > 0
							&& (!userLocation.equalsIgnoreCase(currentUser.getCurrentRole().getLocationId())
							|| !userType.equalsIgnoreCase(currentUser.getCurrentRole().getLocationTypeId()))){
						
//						 Set a Session attribute indicating the user role has been switched.
				        // This is required so the Facility List Portlet knows to create a new search.
				        httpSess.setAttribute("switchRole", "switchRole");
				        
				        
				        
				        //Switch the user role
				        userService.switchRole(currentUser, userType, userLocation);
				        
				   }
				} 
				
			} catch (Exception ex) {
				log.error("Unable to obtain User Infomation", ex);
			}
		}
        
        return true;
    }
        
    private boolean isPublicUrl(HttpServletRequest request) {
    	boolean publicUrl = false;

    	String reqURL = request.getRequestURI();
		String pUrls  = CWNSProperties.getProperty("cwns.2008.public.urls").trim();

		String[] publicUrls = pUrls.split(";");
//		log.debug("reqURL = " + reqURL);
    	
		for (int i = 0; i < publicUrls.length; i++) {
			if (reqURL.matches(".*"+publicUrls[i]+".*")) {
				publicUrl = true;
				log.debug("Public Url");
			}
		}
		
    	return publicUrl;
    }

    private boolean isIndependentUrl(HttpServletRequest request){
    	boolean independentUrl = false;
    	
    	String reqURL = request.getRequestURI();
    	String inpUrls  = CWNSProperties.getProperty("cwns.2008.independent.urls").trim();
    	
    	String[] InpUrls = inpUrls.split(";");
    	for (int i = 0; i < InpUrls.length; i++) {
			if (reqURL.matches(".*"+InpUrls[i]+".*")) {
				independentUrl = true;
				log.debug("Independent Url");
			}
		}
    	
    	return independentUrl;
    }
    
    /**
     * Ensure a User object is attached to the session.
     * 
     * If a User object is found in the http session use it, otherwise create one
     * and attach it to the session.
     * 
     * Note: This a synchronized method.
     * 
     * @param httpReq
     * @return
     * @throws Exception
     */
        private synchronized CurrentUser createUserObject(HttpServletRequest request) 
    						throws Exception {
    		
            String username = request.getParameter("_username");
            CurrentUser user = (CurrentUser) request.getSession().getAttribute(CurrentUser.CWNS_USER);
    		
    		if (user == null) {
    			user = userService.createUserObject(username);
    			request.getSession().setAttribute(CurrentUser.CWNS_USER, user);
    		} 
    		
    		return user;
    	}
        
    /* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }  

}
