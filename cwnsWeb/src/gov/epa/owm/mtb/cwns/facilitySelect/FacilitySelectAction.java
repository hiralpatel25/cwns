package gov.epa.owm.mtb.cwns.facilitySelect;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.discharge.DischargeForm;

public class FacilitySelectAction extends CWNSAction {

	public static final String ACTION_SEARCH  = "search";
	public static final String SORT_COLUMN    = "id.facilityName";
	public static final int    MAX_RESULTS    = 200;
	public static final int    START_INDEX    = 0;
	
	
	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

   		PortletRenderRequest pRequest = (PortletRenderRequest)
			request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
		
		FacilitySelectForm fsForm = (FacilitySelectForm)form; 
		CurrentUser user = (CurrentUser) request.getSession()
											.getAttribute(CurrentUser.CWNS_USER);
		Search search=null;

		String action       = fsForm.getAction();
		String keyword      = fsForm.getKeyword().trim();
		String locationId   = fsForm.getLocationId();
		String facilityId   = pRequest.getParameter("facilityId");
		String formId   	 = pRequest.getParameter("formId");
		Collection states = new ArrayList();
		Facility facility = facilityService.findByFacilityId(facilityId);
        if ("".equals(action) && dischargeService.csoCostCurveInSewershed(facilityId)){
        	fsForm.setLocationId(facility.getLocationId());
        	StateRef state = facilityService.getStateByLocationId(facility.getLocationId());
        	if (state!=null){
        		states.add(new Entity(facility.getLocationId(),state.getName()));
        	}
        	fsForm.setThisStateOnly(true);	
        }
		
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
	   	ArrayList ar = new ArrayList();
    	ar.add(ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED);
    	ar.add(ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION);
    	ar.add(ReviewStatusRefService.FEDERAL_ACCEPTED);
    	ar.add(ReviewStatusRefService.DELETED);
	   	Collection facilityIds = facilityService.getFacilityIdByReviewStatus(locationId, ar);
	   	/* Matt's code
	   	// Get the other Facilities within this Sewarshed
	   	if(facilityId!=null && facilityId.compareToIgnoreCase("")!=0)
	   	{
	   		Collection relatedSewerShedListIds = 
	   			   populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facilityId), new Integer(DischargeForm.PRESENT).intValue());
	   	
	   		facilityListIds.removeAll(relatedSewerShedListIds);
	   	}
	   	*/
	   	facilityListIds.removeAll(facilityIds);
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

	    if(!fsForm.isThisStateOnly()){
	    	states = facilityService.getStates();
	    }	
	   	request.setAttribute("facilityList", facilityList);
		request.setAttribute("states", states);
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
