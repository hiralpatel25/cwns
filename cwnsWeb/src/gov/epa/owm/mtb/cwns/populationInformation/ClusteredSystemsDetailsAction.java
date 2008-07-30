package gov.epa.owm.mtb.cwns.populationInformation;

import java.math.BigDecimal;
import java.util.ArrayList;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ClusteredSystemsDetailsAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
	        HttpServletRequest request, HttpServletResponse response)
	        throws Exception {
	        
	    	    PortletRenderRequest prr = (PortletRenderRequest)
	    		      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
	   		    
	    	    PopulationInformationForm populationInformationForm = (PopulationInformationForm)form;
	    	    
	            // Get user object
	   			HttpServletRequest httpReq = (HttpServletRequest) request;
	   			HttpSession httpSess = httpReq.getSession();
	   			CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
                // Get the facility Id
	   			Long facilityId = new Long(0);
	   			
	   			if(prr !=null && prr.getParameter("facilityId")!=null){
	   				facilityId = new Long(prr.getParameter("facilityId"));
	   			}
	   			/* obtain the Facility object */
	 	    	Facility facility = facilityService.findByFacilityId(facilityId.toString());     
                
	 	    	// check if the action is or duplicate request 
	   			if(isCancelled(request)){
	   				
	   			    return mapping.findForward("success");
	   			}
	   			
	   			boolean isSaveAction = false;
			    if(populationInformationForm.getClusteredAct()!= null && "save".equalsIgnoreCase(populationInformationForm.getClusteredAct()))
			    {
			    	  isSaveAction = true;
			    	  log.debug("isSaveAction - true");
			    }
			    
			    
			    if (isSaveAction){
			    	int presentResClusteredHouses = populationInformationForm.getPresentResClusteredHouses();
			    	int projectedResClusteredHouses = populationInformationForm.getProjectedResClusteredHouses();
			    	int presentNonResClusteredHouses = populationInformationForm.getPresentNonResClusteredHouses();
			    	int projectedNonResClusteredHouses = populationInformationForm.getProjectedNonResClusteredHouses();
			    	float resPopulationPerHouse = populationInformationForm.getResPopulationPerHouse();
			    	float nonResPopulationPerHouse = populationInformationForm.getNonResPopulationPerHouse();
			    	float dbresPopulationPerHouse = populationService.getResidentialPopulationPerHouse(facilityId);
			    	float dbNonResPopulationPerHouse = populationService.getNonResidentialPopulationPerHouse(facilityId);
			    	
			    	int dbresDecProjectedPopCnt = populationService.getProjectedResDecentralizedPopulation(facilityId);
			    	int dbResDecPresentPopCnt = populationService.getPresentResDecentralizedPopulation(facilityId);
			    	int resDecPresentPopCnt = Math.round(presentResClusteredHouses * resPopulationPerHouse);
			    	int resDecProjectedPopCnt = Math.round(projectedResClusteredHouses * resPopulationPerHouse);
                    System.out.println("counts---"+resDecPresentPopCnt+"--"+resDecProjectedPopCnt);
                    if( resDecPresentPopCnt!=0 || resDecProjectedPopCnt!=0){
			    	// 	saveOrUpdate  "7"
	    		  	Object[] values1 = new Object[]
	    		   	  {new Integer(resDecPresentPopCnt),
	    		   	   new Integer(resDecProjectedPopCnt),
	    		   	   new Short((short)populationInformationForm.getResDecProjectedYear())};
	    		  	Object[] values2 = new Object[]
	    		  	      	    		   	  {new Integer(presentResClusteredHouses),
	    		  	      	    		   	   new Integer(projectedResClusteredHouses),
	    		  	      	    		   	   new BigDecimal(resPopulationPerHouse)};
	    		  	populationService.savePopulationUnitsByPopIdAndFacId(facilityId.toString(), "7", values2, user.getUserId());
	    		  	int[] popIds = {PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM,
	    		  			PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM};
	    		  	populationService.updatePopulationPerUnits(facilityId.toString(), popIds, resPopulationPerHouse, user.getUserId());
	    		   	populationService.saveOrUpdatePopulationInfo(facilityId.toString(), values1, new Long(7), user.getUserId());
	    		   	
                    }
                    else
			    	  {
			    		  //delete "7"
			    		  populationService.deletePopulationInfoIfExists(facilityId.toString(), new Long(7), user.getUserId());
			    		  populationService.deleteFacilityHouseIfExists(facilityId.toString(), new Long(7));
			    	  }
                        
                    
                    int dbNonResDecProjectedPopCnt = populationService.getProjectedNonResDecentralizedPopulation(facilityId);
                    int dbNonResDecPresentPopCnt = populationService.getPresentNonResDecentralizedPopulation(facilityId);
	    		   	int nonResDecPresentPopCnt = Math.round(presentNonResClusteredHouses * nonResPopulationPerHouse);
			    	int nonResDecProjectedPopCnt = Math.round(projectedNonResClusteredHouses * nonResPopulationPerHouse);
			    	if(nonResDecPresentPopCnt !=0 ||(nonResDecProjectedPopCnt!=0 && 
				    	      populationInformationForm.getNonResDecProjectedYear() !=0 )){
	    		   	// saveOrUpdate  "8"
	    		  	Object[] values2 = new Object[]
	    		   	  {new Integer(nonResDecPresentPopCnt),
	    		   	   new Integer(nonResDecProjectedPopCnt),
	    		   	   new Short((short)populationInformationForm.getNonResDecProjectedYear())};
	    		  	Object[] values3 = new Object[]
		    		  {new Integer(presentNonResClusteredHouses),
		    		   new Integer(projectedNonResClusteredHouses),
		    		   new BigDecimal(nonResPopulationPerHouse)};
	    		  	populationService.savePopulationUnitsByPopIdAndFacId(facilityId.toString(), "8", values3, user.getUserId());
	    		  	int[] popIds = {PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM,
	    		  			PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM};
	    		  	populationService.updatePopulationPerUnits(facilityId.toString(), popIds, nonResPopulationPerHouse, user.getUserId());
	    		  	populationService.saveOrUpdatePopulationInfo(facilityId.toString(), values2, new Long(8), user.getUserId());
	    		  	
			    	}
			    	else
			    	  {
			    		  //delete "8"
			    		  populationService.deletePopulationInfoIfExists(facilityId.toString(), new Long(8), user.getUserId());
			    		  populationService.deleteFacilityHouseIfExists(facilityId.toString(), new Long(8));
			    	  }
			    	ArrayList popIds = new ArrayList();
			    	popIds.add(new Long(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
			    	popIds.add(new Long(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
			    	// Recalculate total population
			    	populationService.updateFacilityPopulationCount(facilityId, popIds, user.getUserId());
			    	if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
			    	// cost curve rerun
			    	if (dbresDecProjectedPopCnt != resDecProjectedPopCnt || dbresPopulationPerHouse != resPopulationPerHouse){
                    	ArrayList costCurveIdList = new ArrayList();
    	    			costCurveIdList.add(new Long(1));
    	    			costCurveIdList.add(new Long(2));
    	    			costCurveIdList.add(new Long(4));
    	    			costCurveIdList.add(new Long(5));
    	    			costCurveIdList.add(new Long(6));
    	    			costCurveIdList.add(new Long(10));
    	    			costCurveIdList.add(new Long(14));
    	    			costCurveIdList.add(new Long(18));
    	    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
                    }
                    if (dbResDecPresentPopCnt != resDecPresentPopCnt || dbresPopulationPerHouse != resPopulationPerHouse){
                    	ArrayList costCurveIdList = new ArrayList();
    	    			costCurveIdList.add(new Long(14));
    	    			costCurveIdList.add(new Long(18));
    	    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
                    }
			    	if (dbNonResDecProjectedPopCnt != nonResDecProjectedPopCnt || dbNonResPopulationPerHouse != nonResPopulationPerHouse){
                    	ArrayList costCurveIdList = new ArrayList();
    	    			costCurveIdList.add(new Long(14));
    	    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
                    }
			    	if (dbNonResDecPresentPopCnt != nonResDecPresentPopCnt || dbNonResPopulationPerHouse != nonResPopulationPerHouse){
                    	ArrayList costCurveIdList = new ArrayList();
    	    			costCurveIdList.add(new Long(14));
    	    			costCurveIdList.add(new Long(18));
    	    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
                    }
			    	}
			    	facilityService.performPostSaveUpdates(facilityId, FacilityService.DATA_AREA_POPULATION, user);	
	    		  	return mapping.findForward("success");
			    }
	   			
                // Check if facility has facility type that is present and valid for Decentralized System Population
	    		String enableDecentralizedPresent = populationService.isDecentralizedPresentEnabled(facilityId.toString());
	            // Check if facility has facility type that is projected and valid for Decentralized System Population
	    		String enableDecentralizedProjected = populationService.isDecentralizedProjectedEnabled(facilityId.toString());
	            // Check if facility has facility type that is present and valid for ISDS Population
	    		String enableISDSPresent = populationService.isISDSPresentEnabled(facilityId.toString());
	            // Check if facility has facility type that is projected and valid for ISDS Population
	    		String enableISDSProjected = populationService.isISDSProjectedEnabled(facilityId.toString());
                // Check if facility has facility type that is valid for Clustered System Population
	    		String isClusteredSystemExists = populationService.isClusteredSystemExists(facilityId.toString());
	            	    		
                //Clustered
	   			int presentResClusteredHouses = populationService.getPresentResidentialClusteredWastewaterHouses(facilityId) ;
	   			int projectedResClusteredHouses = populationService.getProjectedResidentialClusteredWastewaterHouses(facilityId);
	   			int presentNonResClusteredHouses = populationService.getPresentNonResidentialClusteredWastewaterHouses(facilityId) ;
	   			int projectedNonResClusteredHouses = populationService.getProjectedNonResidentialClusteredWastewaterHouses(facilityId);
	   			
	   			float resPopulationPerHouse = populationService.getResidentialPopulationPerHouse(facilityId);
	   			float nonResPopulationPerHouse = populationService.getNonResidentialPopulationPerHouse(facilityId);
	   			
	   			populationInformationForm.setDisplayClusteredDetails("Y");
	   			populationInformationForm.setPresentResClusteredHouses(presentResClusteredHouses);
	   			populationInformationForm.setProjectedResClusteredHouses(projectedResClusteredHouses);
	   			populationInformationForm.setPresentNonResClusteredHouses(presentNonResClusteredHouses);
	   			populationInformationForm.setProjectedNonResClusteredHouses(projectedNonResClusteredHouses);
	   			populationInformationForm.setResPopulationPerHouse(resPopulationPerHouse);
	   			populationInformationForm.setNonResPopulationPerHouse(nonResPopulationPerHouse);
	   			
	   			populationInformationForm.setEnableDecentralizedPresent(enableDecentralizedPresent);
				populationInformationForm.setEnableDecentralizedProjected(enableDecentralizedProjected);
				populationInformationForm.setEnableISDSPresent(enableISDSPresent);
				populationInformationForm.setEnableISDSProjected(enableISDSProjected);
				populationInformationForm.setIsClusteredSystemExists(isClusteredSystemExists);
	   			
				 // Check if facility is updatable or not and set form attribute
		   	     if (facilityService.isUpdatable(user, facilityId)){
		   	    	populationInformationForm.setIsUpdatable("Y");
		   	    	log.debug("populationInformationForm: isUpdatable: Y");
		   		  }
		   	     else
		   	     {
		    	    	populationInformationForm.setIsUpdatable("N");   	    	 
		   	     }
	   			return mapping.findForward("success");
	}
	
   private FacilityService facilityService;
    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  
	 private PopulationService populationService;
	 	
	 //  set the population service
	 public void setPopulationService(PopulationService ps){
	     populationService = ps;    	
	 }

}
