package gov.epa.owm.mtb.cwns.common;
 
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.webdb.provider.v2.struts.action.PDKAction;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/** 
 * Wraps the usual Struts Action class so that all the log, session, and
 * hibernate session handling is taken care of.
 * 
 */
abstract public class CWNSAction extends PDKAction {

    /**
     * A log instance for the class.
     */	
    protected Logger log;

	public CWNSAction() {
		super();
		log = Logger.getLogger(this.getClass());
	} 
	    

    /**
     * Wraps the CWNSExecute method so that all the log, session, and hibernate
     * session handling is taken care of.
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param req
     *            The HTTP request we are processing
     * @param res
     *            The HTTP response we are creating
     * @throws Exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest req, HttpServletResponse res) throws Exception {
    	
  	  HttpSession httpSess = req.getSession();
	  boolean isTheFirstPortlet = false;
	  long outerGateWaitTime=(new Long(CWNSProperties.getProperty("outer.gate.wait.time", "10"))).longValue();
	  long innerGateWaitTime=(new Long(CWNSProperties.getProperty("inner.gate.wait.time", "50"))).longValue();

 	  if(req.getParameter("strutsAction") != null && req.getParameter("strutsAction").length()>0)
 	  {
 		 isTheFirstPortlet = true;
 		 httpSess.setAttribute("cwnsExecutionGate", "Closed");
 	  }
 	  else
 	  {
 		 isTheFirstPortlet = false;
 		 
 		  // start 1 second waiting and checking 
	    	for(int j=0; j < 45; j++)
 	    	{
	    		Thread.sleep(outerGateWaitTime);
	    		
	    		Object cwnsExecutionGate = httpSess.getAttribute("cwnsExecutionGate");
	    		
	    		if(cwnsExecutionGate != null && ((String)cwnsExecutionGate).equalsIgnoreCase("Closed") )
	    		{
	    			// OK, someone submitted a form, start a (3 second / Lifted) wait
	    			
	    			// kill the outter loop first
	    			j = 10000;
	    			
	    			for(int k=0; k < 60; k++)
	    			{
	    				Object cwnsExecutionGateNew = httpSess.getAttribute("cwnsExecutionGate");
	    				if(cwnsExecutionGateNew == null || ((String)cwnsExecutionGate).equalsIgnoreCase("Lifted"))
	    				{
	    					// let's go
	    					break;
	    				}
	    				
	    				Thread.sleep(innerGateWaitTime);
	    			}
	    			
	    		}
 	    	}
 	  }

    	
    	ActionForward af = cwnsExecute(mapping, form, req, res);

		if(isTheFirstPortlet == true)
		{
			httpSess.setAttribute("cwnsExecutionGate", "Lifted");
		}
		else
		{
			// any one of the rest should remove this attribute
			httpSess.removeAttribute("cwnsExecutionGate");
		}

    	return af;
	
    }

    /**
     * This method is implemented in the concreate class that extends this class.
     * 
     * @param mapping
     * @param form
     * @param req
     * @param res
     * @return
     * @throws Exception
     */
    abstract protected ActionForward cwnsExecute(ActionMapping mapping,
            ActionForm form, HttpServletRequest req, HttpServletResponse res)
            throws Exception;

}