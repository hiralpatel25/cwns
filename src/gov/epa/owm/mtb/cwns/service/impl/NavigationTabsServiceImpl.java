package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.dao.NavigationTabsDAO;
import gov.epa.owm.mtb.cwns.navigationTabs.NavigationTabsListHelper;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.NavigationTabsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

 public class NavigationTabsServiceImpl extends CWNSService implements NavigationTabsService {

	 
		private NavigationTabsDAO navigationTabsDAO;
		
		public void setNavigationTabsDAO(NavigationTabsDAO dao){
			navigationTabsDAO = dao;
		}		 

		private FacilityService facilityService;

		public void setFacilityService(FacilityService fs) {
			facilityService = fs;
		}
				
	public Collection findDataAreaIdsByFacilityId(String facNum)
	{
		Collection dataArea = navigationTabsDAO.findDataAreasByFacilityId(facNum);
		
		Iterator iter = dataArea.iterator();
		
		Object[] s = null;
		
		ArrayList daIdList = new ArrayList();
		
		while (iter.hasNext()) {

    	    s = (Object[]) iter.next();

    	    daIdList.add((Long)s[1]);
		}
		
		return daIdList;
	}
		
	public Collection findDataAreasByFacilityId(String facNum, String navigationTabType, boolean hasNeedsLinks, CurrentUser user ) {

		Collection dataArea = navigationTabsDAO.findDataAreasByFacilityId(facNum);
		Collection navigationTabsListHelper = new ArrayList();
		TreeSet ts = new TreeSet();		
		Iterator iter = dataArea.iterator();		
		Object[] s = null;
		
		while (iter.hasNext()) {
			s = (Object[]) iter.next();	    
    	    if ((String)s[0]!=null){
    	    	ts.add((String)s[0]);
    	    }
		}

		if(ts.contains("Facility")) 
			navigationTabsListHelper.add(addTab("Facility",navigationTabType));

		if(ts.contains("Location")) 
			navigationTabsListHelper.add(addTab("Location",navigationTabType));		
		
		if(ts.contains("Needs")) 
		{
			NavigationTabsListHelper needsNavTabHelper = addTab("Needs",navigationTabType);
			
			if(hasNeedsLinks)
			{
				needsNavTabHelper.setActive("true");
			}
			
			if(user!=null && !facilityService.isDataAreaViewable(user, new Long(facNum), FacilityService.DATA_AREA_NEEDS))
			{
				needsNavTabHelper.setActive("disabled"); // to utilize the JSP logic		
			}
			
			navigationTabsListHelper.add(needsNavTabHelper);
		}

		if(ts.contains("Population") && ts.contains("Flow"))
			navigationTabsListHelper.add(addTab("Population & Flow",navigationTabType));
		else if(ts.contains("Population"))
				navigationTabsListHelper.add(addTab("Population",navigationTabType));

		if(ts.contains("Discharge") && ts.contains("Effluent"))
			navigationTabsListHelper.add(addTab("Discharge & Effluent",navigationTabType));
		else if(ts.contains("Discharge"))
				navigationTabsListHelper.add(addTab("Discharge",navigationTabType));

		if(ts.contains("Pollution")) 
			navigationTabsListHelper.add(addTab("Pollution",navigationTabType));

		if(ts.contains("Combined Sewer")) 
			navigationTabsListHelper.add(addTab("Combined Sewer",navigationTabType));

		if(ts.contains("Unit Process")) 
			navigationTabsListHelper.add(addTab("Unit Process",navigationTabType));

		if(ts.contains("Utility Management")) 
			navigationTabsListHelper.add(addTab("Utility Management",navigationTabType));

		return navigationTabsListHelper;
	}
	
	public Collection findValidatedDataAreasByFacilityId(String facNum, String navigationTabType, boolean hasNeedsLinks, CurrentUser user){
		Collection dataArea = navigationTabsDAO.findDataAreasByFacilityId(facNum);
		Collection navigationTabsListHelper = new ArrayList();
		TreeSet ts = new TreeSet();		
		Iterator iter = dataArea.iterator();		
		Object[] s = null;
		
		while (iter.hasNext()) {
    	    s = (Object[]) iter.next();    	    
    	    String dataAreaName = (String)s[0];    	        	    
   	    	ts.add(dataAreaName);
		}

		if(ts.contains("Facility")) 
			navigationTabsListHelper.add(addTab("Facility",navigationTabType));

		if(ts.contains("Location")) 
			navigationTabsListHelper.add(addTab("Location",navigationTabType));		
		
		if(ts.contains("Needs")) 
		{
			NavigationTabsListHelper needsNavTabHelper = addTab("Needs",navigationTabType);			
			if(hasNeedsLinks){
				needsNavTabHelper.setActive("true");
			}			
			if(user!=null && !facilityService.isDataAreaViewable(user, new Long(facNum), FacilityService.DATA_AREA_NEEDS)){
				needsNavTabHelper.setActive("disabled"); // to utilize the JSP logic		
			}			
			navigationTabsListHelper.add(needsNavTabHelper);
		}

		if(ts.contains("Population") && ts.contains("Flow"))
			navigationTabsListHelper.add(addTab("Population & Flow",navigationTabType));
		else if(ts.contains("Population"))
				navigationTabsListHelper.add(addTab("Population",navigationTabType));

		if(ts.contains("Discharge") && ts.contains("Effluent"))
			navigationTabsListHelper.add(addTab("Discharge & Effluent",navigationTabType));
		else if(ts.contains("Discharge"))
				navigationTabsListHelper.add(addTab("Discharge",navigationTabType));

		if(ts.contains("Pollution")) 
			navigationTabsListHelper.add(addTab("Pollution",navigationTabType));

		if(ts.contains("Combined Sewer")) 
			navigationTabsListHelper.add(addTab("Combined Sewer",navigationTabType));

		if(ts.contains("Unit Process")) 
			navigationTabsListHelper.add(addTab("Unit Process",navigationTabType));
		
		if(ts.contains("Utility Management")) 
			navigationTabsListHelper.add(addTab("Utility Management",navigationTabType));
		return navigationTabsListHelper;		
	}
	
	private NavigationTabsListHelper addTab(String str, String navigationTabType)
	{
		NavigationTabsListHelper navTapHelper = new NavigationTabsListHelper();
		
		if(str.equals(navigationTabType))
		{
			navTapHelper.setActive("true");
		}
		else
		{
			navTapHelper.setActive("false");
		}
		
		navTapHelper.setTabText(str);
			
		return navTapHelper;
	}	
}
