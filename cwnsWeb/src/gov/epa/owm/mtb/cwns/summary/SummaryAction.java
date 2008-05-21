package gov.epa.owm.mtb.cwns.summary;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.SummaryService;
import gov.epa.owm.mtb.cwns.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SummaryAction extends CWNSAction {

	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
        		
		SummaryForm summaryForm = (SummaryForm) form;
		
		String facNum = summaryForm.getFacilityId();
		if (facNum == null){		
		  //get the facility ID from the page parameter - configured as input parameter in provider.xml
		   facNum = req.getParameter("facilityId");
		}	
		
		 /* determine what needs to be done */
		String action = summaryForm.getSummaryAct();
		log.debug("Action------"+action);
		if (action.equals("save"))
		{
           			
			saveCorrections(summaryForm.getFacilityId(), summaryForm.getDataAreaTypes());
             // get Parameter from the form instead
			 //facNum = summaryForm.getCWNSNbr();
		}
					
		log.debug("show cost curve---"+summaryForm.getCostCurve());				
		log.debug("Facility Number "+facNum);
		
        //Get user privilages
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userrole = user.getCurrentRole();
		HashMap privileges = userrole.getPrivileges();
		Collection summaryHelpers = null;
		
		
        // Check facility Overall type 
	       if (facilityService.isFacility(new Long(facNum))){
	    	   summaryForm.setIsFacility("Y");
	       }
	 if(user.isNonLocalUser()){      
           // Get the facility review status type
			String reviewstatustype = summaryService.findReviewStatusRefByFacilityIdAndVersionCode(facNum, summaryService.facVerCode);
			log.debug("reviewstatustype "+reviewstatustype);
		// Determine if the correction column needs to be displayed
		if (ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED.equalsIgnoreCase(reviewstatustype) || 
			reviewstatustype.equalsIgnoreCase("FRC") || 
			reviewstatustype.equalsIgnoreCase("SCR") || reviewstatustype.equalsIgnoreCase("FA"))
		    summaryForm.setShowCorrection("Y");
		
		// Determine if 'Changed' needs to be shown in the column heading instead of Error
		if (privileges.containsKey(CurrentUser.FEDERAL_REVIEW) &&	reviewstatustype.equalsIgnoreCase("FRC"))
			summaryForm.setShowChanged("Y");
							   	
		// obtain the summary objects
		summaryHelpers = summaryService.findSummaryByFacilityId(facNum,reviewstatustype,summaryForm.getShowChanged());
		
		//check if the correction column is updatable 
		if (privileges.containsKey(CurrentUser.FEDERAL_REVIEW) && (reviewstatustype.equalsIgnoreCase("FRR") || reviewstatustype.equalsIgnoreCase("FRC") || 
				 reviewstatustype.equalsIgnoreCase("FA"))) {
			
			summaryForm.setCheck_box("Yes");
			
			Iterator it = summaryHelpers.iterator();
			ArrayList al = new ArrayList();
						
			while (it.hasNext()) {
				SummaryHelper SH = (SummaryHelper)it.next();
				if (SH.getCorrection().equalsIgnoreCase("Y")) {
					al.add(SH.getName());
				}
			}
						
			String s[] = (String[])(al.toArray(new String[al.size()]));
						
			summaryForm.setDataAreaTypes(s);
		}
		
		Collection facCostCurves = summaryService.findFacilityCostcurveByFacilityId(facNum);
		// set values in the summaryFormBean
		if (facCostCurves.size()>0){
	       summaryForm.setShowCostCurve("Y");		
		}
	 }
	 else
		 summaryHelpers = summaryService.findSummaryByFacilityId(facNum,"","");	 
		//long facilityId = summaryService.findFacilityIdByCWNSNumber(facNum);
	    
		// get facility comments
		Collection facCommentHelpers = summaryService.findCommentsByFacilityId(facNum);
				
		// get Last updated userId and time stamp
		HashMap map1 = summaryService.findLastUpdatedUserIdAndDate(facNum);
		// get Last updated username
		log.debug("user id-----"+map1.get("lastUpdateUserid"));
		String username = userService.findUserNameByUserId((String)(map1.get("lastUpdateUserid")));
		
		// get last updated TS and userId of the last federal user to review facility
		HashMap map2 = summaryService.findLastReviewedUserIdAndDate(facNum);
        // get first and last name of last Federal user to review facility
		String reviewername = (map2.isEmpty())?"":userService.findUserNameByUserId((String)(map2.get("lastUpdateUserid")));
		log.debug("reviewer id-----"+reviewername);
		
		summaryForm.setLastUpdatedUserName(username);
		summaryForm.setlastUpdateTS((String)map1.get("lastUpdateTs"));
		summaryForm.setReviewerName(reviewername);
		summaryForm.setLastReviewedTS((String)map2.get("lastUpdateTs"));
		summaryForm.setFacilityId(facNum);
		//summaryForm.setCWNSNbr(facNum);
		summaryForm.setSummHelpers(summaryHelpers);
		summaryForm.setFacCommentHelpers(facCommentHelpers);
        // Check if facility is in a sewershed
        if(populationService.getRelatedSewerShedFacilities(facNum)!=null && 
        		populationService.getRelatedSewerShedFacilities(facNum).size()>1){
        	summaryForm.setIsFacilityInSewershed("Y");
        }
		req.setAttribute("isNonLocalUser", user.isNonLocalUser()?"true":"false");
		req.setAttribute("summary", summaryForm);
			String key ="help."+user.getCurrentRole().getLocationTypeId()+".summary";
		    String defaultkey = "help.summary"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		req.setAttribute("helpKey", helpKey);
		
		return mapping.findForward("success");
	}
	
	private void saveCorrections(String facilityId, String[] dataAreaTypes) {
		summaryService.save(facilityId,dataAreaTypes);
		
	}

	private SummaryService summaryService;
    private UserService userService;
    private FacilityService facilityService;
    private PopulationService populationService;
	
	public void setSummaryService(SummaryService ss) {
		summaryService = ss;
	}
	public void setUserService(UserService us) {
		userService = us;
	}
	public void setFacilityService(FacilityService fs) {
		facilityService = fs;
	}
    //  set the population service
    public void setPopulationService(PopulationService ps){
    	populationService = ps;    	
    }  

}
