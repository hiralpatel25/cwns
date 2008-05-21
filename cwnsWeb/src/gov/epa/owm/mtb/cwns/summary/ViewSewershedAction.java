package gov.epa.owm.mtb.cwns.summary;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

public class ViewSewershedAction extends CWNSAction {
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		DynaActionForm  sewershedListForm = (DynaActionForm)form;
	
	String hasPresentSewershed = "false";
	String hasProjectedSewershed = "false";
	int dischargeType;
	long facId = -1;
	// this is from portal page parameter		
	if(req.getParameter("facilityId")!=null && req.getParameter("facilityId").length()>0)
		facId = Long.parseLong(req.getParameter("facilityId"));
				
    Collection presentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facId), PopulationService.PRESENT_ONLY);
    Collection projectedSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facId), PopulationService.PROJECTED_ONLY);
	if(presentSewershed!=null && presentSewershed.size()>1)
		hasPresentSewershed = "true";
	if(projectedSewershed!=null && projectedSewershed.size()>1)
		hasProjectedSewershed = "true";
	if(sewershedListForm.get("dischargeType")==null || "".equals(sewershedListForm.get("dischargeType"))){
		if("true".equals(hasPresentSewershed))
			dischargeType = PopulationService.PRESENT_ONLY;
		else
			dischargeType = PopulationService.PROJECTED_ONLY;
	}
	else
		dischargeType = Integer.parseInt((String)sewershedListForm.get("dischargeType"));
	
	ArrayList populationHelperList;
	populationHelperList = (ArrayList)populationService.getRelatedSewerShedFacilitiesForDisplay(new Long(facId), dischargeType);
	
	sewershedListForm.set("dischargeType", Integer.toString(dischargeType));
    req.setAttribute("populationHelperList", populationHelperList);
    req.setAttribute("hasPresentSewershed", hasPresentSewershed);
    req.setAttribute("hasProjectedSewershed", hasProjectedSewershed);
		return mapping.findForward("success");
	}
	
    private PopulationService populationService;
    
    //  set the population service
    public void setPopulationService(PopulationService ps){
       populationService = ps;    	
    }  

}
