package gov.epa.owm.mtb.cwns.npdes;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.permits.FacilityPermitForm;
import gov.epa.owm.mtb.cwns.service.NpdesPermitService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import gov.epa.owm.mtb.cwns.common.Entity;

public class NPDESPermitFlowDataAction extends CWNSAction {
	public static final String DATEFORMAT =  "MM/dd/yyyy";

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
		NPDESPermitFlowDataForm permitFlowDataForm = (NPDESPermitFlowDataForm)form;
		SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
				
        // Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		String startDate ="";
        //	get the facility ID from the page parameter - configured as input parameter in provider.xml
		Long facilityId = new Long(0);
		if(prr.getParameter("facilityId")!=null) facilityId = new Long(prr.getParameter("facilityId"));
		
		// Get available EfPipeScheds
		Collection efPipes = npdesPermitService.getEfPipeScheds(facilityId);
		// Default action
		if ("".equals(permitFlowDataForm.getAction())){
			//startDate = "01/01/1900";	
			permitFlowDataForm.setEndDate(df.format(new Date()));
			Iterator iter = efPipes.iterator();
			if (iter.hasNext()){
				Entity obj = (Entity)iter.next();
				permitFlowDataForm.setEfPipeSchedId(obj.getKey());
			}
		}
		
		startDate = permitFlowDataForm.getStartDate();
		if("".equalsIgnoreCase(startDate)||startDate==null)
			startDate = "01/01/1900";	
		
		// get EfNpdesPermitNbr for facility's primary permit and set form attribute
		String efNpdesPermitNumber = npdesPermitService.getEfNpdesPermitNbr(facilityId);
		permitFlowDataForm.setEfNpdesPermitNumber(efNpdesPermitNumber);
				
		String endDate = permitFlowDataForm.getEndDate();
		String selectedPipeSchedId = permitFlowDataForm.getEfPipeSchedId();
		
		//get permit's present Design Flow Rate and set form attribute
		float preDesignFlowRate = npdesPermitService.getPermitDesignFlowRate(efNpdesPermitNumber);
		permitFlowDataForm.setPreDesignFlowRate(preDesignFlowRate);
		//Get NPDES flow data
		Collection npdesFlowData = null;
		if (!"".equals(selectedPipeSchedId)){
		  String[] args = selectedPipeSchedId.split(",");
		  npdesFlowData = npdesPermitService.displayNpdesFlowData(efNpdesPermitNumber, args[0], args[1], new Date(startDate), new Date(endDate));
		}
		// Check if Unit of Measure is same for all the quantities
		if(npdesFlowData!=null && npdesFlowData.size()>1)
		  permitFlowDataForm.setIsUnitOfMeasureSame(npdesPermitService.IsUnitOfMeasureSame(npdesFlowData)?"Y":"N");
			
		if (efPipes.size()==0)
		  permitFlowDataForm.setSearch("disable");
		else
			if (efPipes.size()==1)
			  permitFlowDataForm.setDisplayOnly(true);
		request.setAttribute("permitFlowDataForm", permitFlowDataForm);
		request.setAttribute("permitFlowData", npdesFlowData);
		request.setAttribute("efPipesData", efPipes);
				
			String key ="help."+user.getCurrentRole().getLocationTypeId()+".npdespermitflowdata";
		    String defaultkey = "help.npdespermitflowdata"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		request.setAttribute("helpKey", helpKey);
		return mapping.findForward("success");
	}
	
	private NpdesPermitService npdesPermitService;
            
    //  set the flow service
    public void setNpdesPermitService(NpdesPermitService nps){
    	npdesPermitService = nps;    	
    }  

}
