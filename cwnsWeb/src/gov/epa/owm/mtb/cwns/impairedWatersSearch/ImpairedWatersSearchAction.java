package gov.epa.owm.mtb.cwns.impairedWatersSearch;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.GeographicAreaWatershed;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ImpairedWatersService;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ImpairedWatersSearchAction extends CWNSAction {
	
	public static final String ACTION_NEXT="next";
	public static final String ACTION_PREVIOUS="previous";
	public static final String ACTION_FIRST="first";
	public static final String ACTION_LAST="last";	
	public static final String ACTION_TMDL_FILTER="tmdl_filter";

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		boolean moreThan1000 = false;
    	ImpairedWatersSearchListForm impairedWatersSearchListForm = (ImpairedWatersSearchListForm) form;
    	
		log.debug(" IN - impairedWatersListForm--> " + impairedWatersSearchListForm.toString());

        //Get user privilages
		HttpServletRequest httpReq = (HttpServletRequest) req;

	    PortletRenderRequest prr = (PortletRenderRequest)
	      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
	
		    /* determine what needs to be done */
		String action = (impairedWatersSearchListForm.getImpairedWatersSearchAct() != null) ? 
				impairedWatersSearchListForm.getImpairedWatersSearchAct() : "";
	
		log.debug("Action: " + action);
	  
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));
	
		log.debug("req.getParameter(facilityId): " + facId);
	
		// if facId is available in the form, use it instead
		if(impairedWatersSearchListForm.getImpairedWatersSearchFacilityId() != ImpairedWatersSearchListForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facId = impairedWatersSearchListForm.getImpairedWatersSearchFacilityId();			
		}
			
	    HttpSession pSession = httpReq.getSession(false);
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }		

		CurrentUser user = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
		//UserRole userRole = user.getCurrentRole();

	    // masterSelectedList is a set of List Ids (String)
	    
	    Set masterSelectedList = (Set) pSession.getAttribute(facId + "_ImpairedWaterSearchMasterSelectedList");
	    
	    if(masterSelectedList == null)
	    {
	    	masterSelectedList = new HashSet();	    	
	    }

	    log.debug("Obtaining " + facId + "_ImpairedWaterSearchMasterSelectedList: size " + masterSelectedList.size());
	    
	    Collection masterList = (Collection) pSession.getAttribute(facId + "_ImpairedWaterSearchMasterList");
	    
	    if(masterList == null)
	    {
	    	masterList = new ArrayList();	    	
	    }

	    log.debug("Obtaining " + facId + "_ImpairedWaterSearchMasterList: size " + masterList.size());	    
	    
	    log.debug("NextResult: " + impairedWatersSearchListForm.getNextResult());	    
	    
	    int nextResultToDisplay = -1;

	    ActionForward actionFwd = mapping.findForward("success");  		
		
		Collection impairedWatersSearchDisplayList = new ArrayList();
		
		Collection qualifiedImpairedWaterList = null;
		
		if (action.equals("add"))
		{	
	    	masterSelectedList = updateMasterSelectedList(impairedWatersSearchListForm, facId, pSession);

	    	nextResultToDisplay = 0;
	    		    	
			if(masterSelectedList!=null && masterSelectedList.size()>0)
			{
		    	log.debug("masterSelectedList.size: " + masterSelectedList.size());
		    	/*
				if(isTokenValid(req, true))
				{
				*/
					log.debug("ready to addImpairedWaters");
					
				   if(false == addImpairedWaters(facId, masterSelectedList, masterList, user.getUserId()))
				   {
					   masterSelectedList.clear();
					   resetCache(facId, pSession);
					   					   
					   throw new ApplicationException("Unable to add selected impaired waters!");	
				   }
				   else
				   {		   
					   masterSelectedList.clear();
				   }
				/*   
				  resetToken(req);
				}
				
				saveToken(req);	
				*/
			}
			
			// if nothing is selected, it will simply pass the impairedWatersSearchListForm.getNextResult() back to form

		}
		else if(action.equals("search"))
		{
			req.setAttribute("isValidSearch", "N");
			req.setAttribute("hasNoRecords", "Y");
			req.setAttribute("exceeds1000Results", "N");
			
			//radiusMile can also be huc-8 when searchType is Watershed
			String radiusMile = impairedWatersSearchListForm.getRadiusMiles();
			
			Collection searchResults = null;
			
			if(impairedWatersSearchListForm.getWebServiceMethod() == impairedWatersService.BY_LAT_LONG_RADIUS)
			{
				int rMileInt = -999;
				
				try
				{
					rMileInt = Integer.parseInt(radiusMile);
				}
				catch(NumberFormatException e)
				{
					rMileInt = 0;
				}
				
				if(rMileInt == 0)
				{
					radiusMile = "0";
					searchResults = new ArrayList();
				}
				else 					//searchResults should be an ArrayList of ImpairedWatersSearchListHelper
				try{
				searchResults = impairedWatersService.getImpairedWaterEntities(facId, 
																			   impairedWatersService.BY_LAT_LONG_RADIUS, 
																			   Integer.parseInt(radiusMile), 
																			   "", 
																			   impairedWatersSearchListForm.getTmdlOption(),
																			   impairedWatersSearchListForm.getKeyword());
				}catch (Exception e){
					String strMessage = "";
					if (e instanceof UndeclaredThrowableException){
						UndeclaredThrowableException u = (UndeclaredThrowableException)e;
						Throwable t = u.getUndeclaredThrowable();
						if (t!=null)
							strMessage = t.getMessage();
					}else if(e.getMessage()!=null){
						strMessage = e.getLocalizedMessage();
					}
					if(strMessage.indexOf("returns more than")>=0 &&
							(strMessage.indexOf("1000")>=0 ||
									strMessage.indexOf("1,000")>=0)){
						req.setAttribute("recordsFound", "Y");
						req.setAttribute("exceeds1000Results", "Y");
						moreThan1000 = true;
					}
				}			
			}
			else if(impairedWatersSearchListForm.getWebServiceMethod() == impairedWatersService.BY_HUC8)
			{
				try{
				//searchResults should be an ArrayList of ImpairedWatersSearchListHelper
				searchResults = impairedWatersService.getImpairedWaterEntities(facId, 
																			   impairedWatersService.BY_HUC8, 
																			   0, 
																			   radiusMile, 
																			   impairedWatersSearchListForm.getTmdlOption(),
																			   impairedWatersSearchListForm.getKeyword());
				}catch (Exception e){
					String strMessage = "";
					if (e instanceof UndeclaredThrowableException){
						UndeclaredThrowableException u = (UndeclaredThrowableException)e;
						Throwable t = u.getUndeclaredThrowable();
						if (t!=null)
							strMessage = t.getMessage();
					}else if(e.getMessage()!=null){
						strMessage = e.getLocalizedMessage();
					}
					if(strMessage.indexOf("returns more than")>=0 &&
							(strMessage.indexOf("1000")>=0 ||
									strMessage.indexOf("1,000")>=0)){
						req.setAttribute("recordsFound", "Y");
						req.setAttribute("exceeds1000Results", "Y");
						moreThan1000 = true;
					}
				}		
			}
			else if(impairedWatersSearchListForm.getWebServiceMethod() == impairedWatersService.BY_KEYWORD)
			{
				try{
				//searchResults should be an ArrayList of ImpairedWatersSearchListHelper
				searchResults = impairedWatersService.getImpairedWaterEntities(facId, 
																			   impairedWatersService.BY_KEYWORD, 
																			   0, 
																			   "", 
																			   impairedWatersSearchListForm.getTmdlOption(),
																			   impairedWatersSearchListForm.getKeyword());
				}catch (Exception e){
					String strMessage = "";
					if (e instanceof UndeclaredThrowableException){
						UndeclaredThrowableException u = (UndeclaredThrowableException)e;
						Throwable t = u.getUndeclaredThrowable();
						if (t!=null)
							strMessage = t.getMessage();
					}else if(e.getMessage()!=null){
						strMessage = e.getLocalizedMessage();
					}
					if(strMessage.indexOf("returns more than")>=0 &&
							(strMessage.indexOf("1000")>=0 ||
									strMessage.indexOf("1,000")>=0)){
						req.setAttribute("recordsFound", "Y");
						req.setAttribute("exceeds1000Results", "Y");
						moreThan1000 = true;
					}
				}
			}

			if (impairedWatersSearchListForm.getRadiusMiles().trim().length()>0 ||
					impairedWatersSearchListForm.getKeyword().trim().length()>0){
				req.setAttribute("isValidSearch", "Y");
			}
			
			req.setAttribute("hasNoRecords", "Y");
			if ((searchResults!=null && searchResults.size()>0) || moreThan1000)
				req.setAttribute("hasNoRecords", "N");
		
			pSession.setAttribute(facId + "_ImpairedWaterSearchMasterList", searchResults);
			pSession.setAttribute(facId + "_ImpairedWaterSearchRadiusMile", radiusMile);
			pSession.setAttribute(facId + "_ImpairedWaterSearchWebServiceMethod", new Integer(impairedWatersSearchListForm.getWebServiceMethod())); 
			pSession.setAttribute(facId + "_ImpairedWaterSearchTMDLOption", new Integer(impairedWatersSearchListForm.getTmdlOption())); 
			
			masterList = searchResults;
			
			nextResultToDisplay = 0;
		   
		    masterSelectedList.clear();
	
			if (moreThan1000)
				req.setAttribute("exceeds1000Results", "Y");
		}
		else if (ACTION_NEXT.equals(action)) 
		{
	    	masterSelectedList = updateMasterSelectedList(impairedWatersSearchListForm, facId, pSession);
	    	nextResultToDisplay=impairedWatersSearchListForm.getNextResult() - 1;
	    	 
	    } 
		else if (ACTION_PREVIOUS.equals(action)) 
		{
		    	masterSelectedList = updateMasterSelectedList(impairedWatersSearchListForm, facId, pSession);
		    	nextResultToDisplay=impairedWatersSearchListForm.getNextResult() - 1;
		} 
		else if (ACTION_FIRST.equals(action)) 
		{
		    	masterSelectedList = updateMasterSelectedList(impairedWatersSearchListForm, facId, pSession);
		    	nextResultToDisplay = 0;	
		} 
		else if (ACTION_LAST.equals(action)) 
		{
		    	masterSelectedList = updateMasterSelectedList(impairedWatersSearchListForm, facId, pSession);
		    	
	   			int totalIWs = ((Integer) pSession.getAttribute(facId + "_ImpairedWaterSearchQualifiedListSize")).intValue();
	   			
	   			if(totalIWs <= 0)
	   				nextResultToDisplay = 0;
	   			else if (totalIWs % impairedWatersService.MAX_RESULTS == 0) 
	   			{
	   				nextResultToDisplay = totalIWs - impairedWatersService.MAX_RESULTS;
	   			}
	   			else 
	   			{
	   				nextResultToDisplay = impairedWatersService.MAX_RESULTS * (int)(totalIWs / impairedWatersService.MAX_RESULTS);
	   			}
		}/*
		else if(ACTION_TMDL_FILTER.equals(action))
		{
			masterSelectedList.clear();
			nextResultToDisplay = 0;
			pSession.setAttribute(facId + "_ImpairedWaterSearchTMDLOption", new Integer(impairedWatersSearchListForm.getTmdlOption())); 
		}*/
		else
		{
			if(masterList!=null && masterList.size() > 0) // this is the returning senario
			{
				nextResultToDisplay = 0;
							}
			else
			{
				// this is the new case, clear all cache and instruct JSP not to display anything.
				resetCache(facId, pSession);
				nextResultToDisplay = -1;
			}
		}
		//else we may want to clean up the cache when user enters this portlet fresh

		   //impairedWatersSearchDisplayList is an arrayList of ImpairedWatersSearchListHelper (size 5)
		   // that is the subset of the master list
		
		Integer intTmdlOption = (Integer)pSession.getAttribute(facId + "_ImpairedWaterSearchTMDLOption");
		impairedWatersSearchListForm.setTmdlOption(intTmdlOption==null?0:intTmdlOption.intValue());
		
		if (masterList!=null){
		qualifiedImpairedWaterList = 
			     impairedWatersService.getQualifiedImpairedWaterList(masterList, 
				                                                     facId, 
				                                                     impairedWatersSearchListForm.getTmdlOption());

		   log.debug("getSearchDisplayList: " + qualifiedImpairedWaterList.size() + " - " + nextResultToDisplay);
		   
		   if (qualifiedImpairedWaterList!=null)
		   impairedWatersSearchDisplayList = impairedWatersService.getSearchDisplayList(qualifiedImpairedWaterList, nextResultToDisplay);		
		
		   pSession.setAttribute(facId + "_ImpairedWaterSearchQualifiedListSize", new Integer(qualifiedImpairedWaterList.size()));	   
		   pSession.setAttribute(facId + "_ImpairedWaterSearchMasterSelectedList", masterSelectedList);
		   
		   saveDisplayListAsSetToSession(impairedWatersSearchDisplayList, facId, pSession);
		
	   // prepare for display
	   
	   impairedWatersSearchListForm.setStateId(facilityService.findByFacilityId(new Long(facId).toString()).getLocationId());
	   
	   if(pSession.getAttribute(facId + "_ImpairedWaterSearchRadiusMile")!=null)
	   	   impairedWatersSearchListForm.setRadiusMiles((String)pSession.getAttribute(facId + "_ImpairedWaterSearchRadiusMile"));
	   else
		   impairedWatersSearchListForm.setRadiusMiles("");
	   
	   impairedWatersSearchListForm.setImpairedWatersSelectedNumber(masterSelectedList.size());
	   
	   impairedWatersSearchListForm.setImpairedWatersSearchAct("");

	   impairedWatersSearchListForm.setListIds((String[])masterSelectedList.toArray(new String[0]));
	   
   	   impairedWatersSearchListForm.setNumOfImpairedWaters(qualifiedImpairedWaterList==null?0:qualifiedImpairedWaterList.size());

   	   log.debug("qualifiedImpairedWatersCount: " + impairedWatersSearchListForm.getNumOfImpairedWaters());

		/* Set values in the Struts Form Bean */
   	   GeographicAreaWatershed primaryGAWaterShed = facilityAddressService.getPrimaryGeographicAreaWatershed(new Long(facId));
   	   
   		impairedWatersSearchListForm.setPrimaryHUC(primaryGAWaterShed==null?"":primaryGAWaterShed.getWatershedRef().getWatershedId());   		
		impairedWatersSearchListForm.setFromImpairedWatersSearch(nextResultToDisplay<0?0:(nextResultToDisplay+1));
		impairedWatersSearchListForm.setToImpairedWatersSearch(nextResultToDisplay<0?0:(nextResultToDisplay+impairedWatersSearchDisplayList.size()));
		
		Integer intWSMethod = (Integer)pSession.getAttribute(facId + "_ImpairedWaterSearchWebServiceMethod");
		impairedWatersSearchListForm.setWebServiceMethod(intWSMethod==null?0:intWSMethod.intValue());				

		log.debug("getWebServiceMethod: " + impairedWatersSearchListForm.getNumOfImpairedWaters());
				
		if(nextResultToDisplay+impairedWatersSearchDisplayList.size()+1 > impairedWatersSearchListForm.getNumOfImpairedWaters())
			impairedWatersSearchListForm.setNextImpairedWatersSearchToDisplay(-1); //no link
		else
			impairedWatersSearchListForm.setNextImpairedWatersSearchToDisplay(nextResultToDisplay+impairedWatersSearchDisplayList.size()+1);
		
		if(nextResultToDisplay >= impairedWatersService.MAX_RESULTS )
			impairedWatersSearchListForm.setPrevImpairedWatersSearchToDisplay(nextResultToDisplay - impairedWatersService.MAX_RESULTS + 1);
		else
			impairedWatersSearchListForm.setPrevImpairedWatersSearchToDisplay(-1); //no link
		
		impairedWatersSearchListForm.setImpairedWatersSearchHelpers(impairedWatersSearchDisplayList);
		
		}

        // Check if facility is updatable or not and set form attribute
 	     if (facilityService.isUpdatable(user, new Long(facId))){
 	    	impairedWatersSearchListForm.setIsUpdatable("Y");
 	    	log.debug("facilityCommentsForm: isUpdatable: Y");
 		  }
 	     else
 	     {
 	    	impairedWatersSearchListForm.setIsUpdatable("N");
 	    	log.debug("facilityCommentsForm: isUpdatable: N");
 	     }
		
		req.setAttribute("impairedWatersSearchListForm", impairedWatersSearchListForm);
		req.setAttribute("hasCoordinateInfo", "N");
		AbsoluteLocationPoint alp = facilityAddressService.getFacilityCoordinates(new Long(facId));
		if (alp!=null && alp.getLatitudeDecimalDegree()!=null && alp.getLongitudeDecimalDegree()!=null){			
			req.setAttribute("hasCoordinateInfo", "Y");
		}
		
		if (impairedWatersSearchListForm.getRadiusMiles().trim().length()>0 ||
						impairedWatersSearchListForm.getKeyword().trim().length()>0){
			req.setAttribute("isValidSearch", "Y");
		}
		
		
			String key ="help."+user.getCurrentRole().getLocationTypeId()+".impairedwaterssearch";
		    String defaultkey = "help.impairedwaterssearch"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		req.setAttribute("helpKey", helpKey);
		return actionFwd;
	}
	
    private boolean addImpairedWaters(long facilityId, Set listIds, Collection mstList, String User) {
		return impairedWatersService.addImpairedWaters(facilityId, listIds, mstList, User);
    }	

/*
	private boolean addToMasterSelectedList(Set masterSelectedList, String[] listIds)
	{
		if(listIds!=null && listIds.length > 0)
			masterSelectedList.addAll(Arrays.asList(listIds));
		
		return true;
	}
*/
	/**
	 * Clear the Master list of Selected IWs in the session object. 
	 * @param pSession
	 * @return
	 * 		An empty Set
	 */
	private Set clearMasterSelectedList(long facId, HttpSession pSession) {
		Set masterSelectedList = new HashSet(); 
		pSession.setAttribute(facId + "_ImpairedWaterSearchMasterSelectedList", masterSelectedList);
		return masterSelectedList;
	}	
	
	/**
	 * Update the Master list of Selected Facilities in the session object. 
	 * @param facilityListForm
	 * @param pSession
	 * @return
	 * 		The updated master list
	 */
	private Set updateMasterSelectedList(ImpairedWatersSearchListForm impairedWatersSearchListForm, long facId, HttpSession pSession) {
		
		// IW just displayed 
		Set currentDisplayList = (Set) pSession.getAttribute(facId + "_ImpairedWaterSearchDisplaySet");
		
		// KLUDGE - if the currentDisplayList 
		//          is null the session was lost 
		if ( currentDisplayList == null) {
			return clearMasterSelectedList(facId, pSession);
		}

		// IWs previously selected
		Set masterSelectedList = (Set) pSession.getAttribute(facId + "_ImpairedWaterSearchMasterSelectedList");

		// Facilities just selected
		String[] selectedIWs = impairedWatersSearchListForm.getListIds();
		
		// Remove facilities just displayed to the user from the Master Selected List 
		masterSelectedList.removeAll(currentDisplayList);

		// Add facilities the user just selected to the Master Selected List 
		masterSelectedList.addAll(Arrays.asList(selectedIWs));
		return masterSelectedList;
	}
	

	private void saveDisplayListAsSetToSession(Collection impairedWatersSearchDisplayList, long facId, HttpSession pSession)
	{
		HashSet iwDisplaySet = new HashSet();
		
		if(impairedWatersSearchDisplayList != null)
		{
	    	Iterator iter = impairedWatersSearchDisplayList.iterator();
	    	while ( iter.hasNext() ) {
	    		ImpairedWatersSearchListHelper iwSearchHelper = (ImpairedWatersSearchListHelper) iter.next();
	    		iwDisplaySet.add(iwSearchHelper.getListId());
	    	}
		}
		
		pSession.setAttribute(facId + "_ImpairedWaterSearchDisplaySet", iwDisplaySet);    	
	}
	
	private void resetCache(long facId, HttpSession pSession)
	{
		   pSession.setAttribute(facId + "_ImpairedWaterSearchQualifiedListSize", new Integer(0));	   
		   pSession.setAttribute(facId + "_ImpairedWaterSearchMasterSelectedList", new HashSet());	
		   pSession.setAttribute(facId + "_ImpairedWaterSearchRadiusMile", "");
		   pSession.setAttribute(facId + "_ImpairedWaterSearchMasterList", new ArrayList());
		   pSession.setAttribute(facId + "_ImpairedWaterSearchDisplaySet", new HashSet()); 

		   pSession.setAttribute(facId + "_ImpairedWaterSearchWebServiceMethod", new Integer(0)); 
		   pSession.setAttribute(facId + "_ImpairedWaterSearchTMDLOption", new Integer(0)); 
		   
	}
	
	private ImpairedWatersService impairedWatersService;
	private FacilityAddressService facilityAddressService;
	private FacilityService facilityService;

	public void setFacilityAddressService(FacilityAddressService ss) {
		facilityAddressService = ss;
	}

	public void setFacilityService(FacilityService ss) {
		facilityService = ss;
	}
	
	public void setImpairedWatersService(ImpairedWatersService ss) {
		impairedWatersService = ss;
	}
	
}
