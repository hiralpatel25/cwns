package gov.epa.owm.mtb.cwns.facilitySelect;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;

public class UMFacilitySelectAction extends CWNSAction {

	public static final String ACTION_SEARCH  = "search";
	public static final String SORT_COLUMN    = "id.facilityName";
	public static final int    MAX_RESULTS    = 200;
	public static final int    START_INDEX    = 0;
	
	
	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

   		PortletRenderRequest pRequest = (PortletRenderRequest)
			request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
   		UMFacilitySelectForm fsForm = (UMFacilitySelectForm)form; 
		CurrentUser user = (CurrentUser) request.getSession()
											.getAttribute(CurrentUser.CWNS_USER);
		Search search=null;

		String action       = fsForm.getAction();
		String keyword      = fsForm.getKeyword().trim();
		String locationId   = fsForm.getLocationId();
		String facilityId   = pRequest.getParameter("facilityId");

		String formId   	 = pRequest.getParameter("formId");

 		Map queryProperties =  new HashMap();
	    queryProperties.put("state", locationId);	

    	if (ACTION_SEARCH.equals(action) && keyword.length() > 0 ) {

   		    // Facility Name 
    		queryProperties.put("facName", keyword);	

    		// Overall Facility Nature Type
    		String[] overallTypeIds = facilityService.getFacilityOverallTypeIds(keyword);
    		if (overallTypeIds != null) {
    			queryProperties.put("overAllType",overallTypeIds); 
    		}
    	
	    	// County
    		queryProperties.put("county", keyword);	

	    	// Watershed
    		queryProperties.put("watershed", keyword);
    	}

    	// Initialize the Search criteria
	    SortCriteria sc = new SortCriteria(SORT_COLUMN, SortCriteria.ORDER_ASCENDING);
	    search= facilityService.getFacilitySelectAdvanceSearch(queryProperties, locationId, sc, START_INDEX, MAX_RESULTS);
    	// Execute the search 
	   	Collection  facilityListIds = facilityService.getFacilitiesIds(search,false);
	   	
	   	// Get the other Facilities within this Sewarshed
	   	if(facilityId!=null && facilityId.compareToIgnoreCase("")!=0)
	   	{
	   		Collection relatedSewerShedListIds = 
	   			   populationService.getRelatedSewerShedFacilities(new Long(facilityId));
	   	
	   		facilityListIds.removeAll(relatedSewerShedListIds);
	   	}
	   	
	    //check if the current users is restricted by facilities
	    //if yes  remove any facility not in the list  i.e. get intersection
	   	if(facilityListIds!=null && !facilityListIds.isEmpty()){
	   		UserRole currentUserRole = user.getCurrentRole();
		   	if(currentUserRole.isLimited()){
		   		Map resFacilities = currentUserRole.getFacilities();
		   		if(!resFacilities.keySet().isEmpty()){
			   		ArrayList removeFac = new ArrayList();
			   		//loop thru facilityIds and get a list of facilityIds to be removed
			   		for (Iterator iter = facilityListIds.iterator(); iter.hasNext();) {
						Long fId = (Long) iter.next();
						if(resFacilities.get(fId)==null){
							removeFac.add(fId);
						}
					}
			   		if(!removeFac.isEmpty()){
			   			facilityListIds.removeAll(removeFac);
			   		}
		   		}else{
		   			facilityListIds = new ArrayList();
		   		}
		   	}	   		
	   	}	   		   	
	   
	   	fsForm.setListSize(facilityListIds.size());
	    // Set the display size
	    if (facilityListIds.size() > MAX_RESULTS) {
	    	fsForm.setDisplaySize(MAX_RESULTS);
	    } else {
	    	fsForm.setDisplaySize(facilityListIds.size());
	    }
	   
	    // Fetch the columns
	    Collection facilityList;
	    if(facilityListIds.size()!=0){
	    	facilityList = facilityService.getFacilityObjects(facilityListIds, search);
	    }else{
	    	facilityList = new ArrayList();
	    }	   

//		fsForm.setThisStateOnly(dischargeService.csoCostCurveInSewershed(facilityId));
	    fsForm.setThisStateOnly(false);    // just for testing
	    
	   	request.setAttribute("facilityList", facilityList);
		request.setAttribute("states", facilityService.getStates());
		request.setAttribute("fsForm", fsForm);
		
		return mapping.findForward("success");
	}
   
	/* set the facility service */
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
    
    private DischargeService dischargeService;
    public void setDischargeService(DischargeService fs){
       dischargeService = fs;    	
    }
	
    //  set the population service
	private PopulationService populationService;
    public void setPopulationService(PopulationService ps){
    	populationService = ps;    	
    }  
    
}
