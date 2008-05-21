package gov.epa.owm.mtb.cwns.populationInformation;

import java.math.BigDecimal;
import java.util.ArrayList;

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
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

public class OWTSystemsDetailsAction extends CWNSAction {

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
	   			
//	   			check if the action is or duplicate request 
	   			if(isCancelled(request)){
	   				
	   			    return mapping.findForward("success");
	   			}
	   			
	   			boolean isSaveAction = false;
			    if(populationInformationForm.getOwtsAct()!= null && "save".equalsIgnoreCase(populationInformationForm.getOwtsAct()))
			    {
			    	  isSaveAction = true;
			    	  log.debug("isSaveAction - true");
			    }
			    
			    
			    if (isSaveAction){
			    	int presentResOWTSHouses = populationInformationForm.getPresentResOWTSHouses();
			    	int projectedResOWTSHouses = populationInformationForm.getProjectedResOWTSHouses();
			    	int presentNonResOWTSHouses = populationInformationForm.getPresentNonResOWTSHouses();
			    	int projectedNonResOWTSHouses = populationInformationForm.getProjectedNonResOWTSHouses();
			    	float resPopulationPerHouse = populationInformationForm.getResOWTSPopulationPerHouse();
			    	float nonResPopulationPerHouse = populationInformationForm.getNonResOWTSPopulationPerHouse();
			    	float dbresPopulationPerHouse = populationService.getResidentialOWTSPopulationPerHouse(facilityId);
			    	float dbNonResPopulationPerHouse = populationService.getNonResidentialOWTSPopulationPerHouse(facilityId);
			    	
			    	int dbPresentResOWTSHouses = populationService.getPresentResidentialOnsiteWastewaterHouses(facilityId);
			    	int dbProjectedResOWTSHouses = populationService.getProjectedResidentialOnsiteWastewaterHouses(facilityId);
			    	int dbPresentNonResOWTSHouses = populationService.getPresentNonResidentialOnsiteWastewaterHouses(facilityId);
			    	int dbProjectedNonResOWTSHouses = populationService.getProjectedNonResidentialOnsiteWastewaterHouses(facilityId);
			    	//int dbresOWTSProjectedPopCnt = populationService.getProjectedResIndividualSewageDisposalSystemPopulation(facilityId);
			    	//int dbResOWTSPresentPopCnt = populationService.getPresentResIndividualSewageDisposalSystemPopulation(facilityId);
			    	int resOWTSPresentPopCnt = Math.round(presentResOWTSHouses * resPopulationPerHouse);
			    	int resOWTSProjectedPopCnt = Math.round(projectedResOWTSHouses * resPopulationPerHouse);
                    
                    if( resOWTSPresentPopCnt!=0 || resOWTSProjectedPopCnt!=0){
			    	// 	saveOrUpdate  "5"
	    		  	Object[] values1 = new Object[]
	    		   	  {new Integer(resOWTSPresentPopCnt),
	    		   	   new Integer(resOWTSProjectedPopCnt),
	    		   	   new Short((short)populationInformationForm.getResISDSProjectedYear())};
	    		  	Object[] values2 = new Object[]
		    		  {new Integer(presentResOWTSHouses),
		    		   new Integer(projectedResOWTSHouses),
		    		   new BigDecimal(resPopulationPerHouse)};
	    		  	populationService.savePopulationUnitsByPopIdAndFacId(facilityId.toString(), "5", values2, user.getUserId());
	    		  	int[] popIds = {PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL,
	    		  			PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE,
	    		  			PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL,
	    		  			PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL,
	    		  			PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL,
	    		  			PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE};
	    		  	populationService.updatePopulationPerUnits(facilityId.toString(), popIds, resPopulationPerHouse, user.getUserId());
	    		   	populationService.saveOrUpdatePopulationInfo(facilityId.toString(), values1, new Long(5), user.getUserId());
	    		   	
                    }
                    else
			    	  {
			    		  //delete "5"
			    		  populationService.deletePopulationInfoIfExists(facilityId.toString(), new Long(5), user.getUserId());
			    		  populationService.deleteFacilityHouseIfExists(facilityId.toString(), new Long(5));
			    	  }
                           
                    
	    		   	int nonResOWTSPresentPopCnt = Math.round(presentNonResOWTSHouses * nonResPopulationPerHouse);
			    	int nonResOWTSProjectedPopCnt = Math.round(projectedNonResOWTSHouses * nonResPopulationPerHouse);
			    	if(nonResOWTSPresentPopCnt !=0 ||(nonResOWTSProjectedPopCnt!=0 && 
				    	      populationInformationForm.getNonResISDSProjectedYear() !=0 )){
	    		   	// saveOrUpdate  "6"
	    		  	Object[] values2 = new Object[]
	    		   	  {new Integer(nonResOWTSPresentPopCnt),
	    		   	   new Integer(nonResOWTSProjectedPopCnt),
	    		   	   new Short((short)populationInformationForm.getNonResISDSProjectedYear())};
	    		  	Object[] values3 = new Object[]
	    		  	  {new Integer(presentNonResOWTSHouses),
	    		  	   new Integer(projectedNonResOWTSHouses),
	    		  	   new BigDecimal(nonResPopulationPerHouse)};
	    		  	populationService.savePopulationUnitsByPopIdAndFacId(facilityId.toString(), "6", values3, user.getUserId());
	    		  	int[] popIds = {PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL,
	    		  			PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE,
	    		  			PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL,
	    		  			PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL,
	    		  			PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL,
	    		  			PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE};
	    		  	populationService.updatePopulationPerUnits(facilityId.toString(), popIds, resPopulationPerHouse, user.getUserId());
	    		  	populationService.saveOrUpdatePopulationInfo(facilityId.toString(), values2, new Long(6), user.getUserId());
	    		  	//facilityService.performPostSaveUpdates(facilityId, new Long(5), user);
			    	}
			    	else
			    	  {
			    		  //delete "6"
			    		  populationService.deletePopulationInfoIfExists(facilityId.toString(), new Long(6), user.getUserId());
			    		  populationService.deleteFacilityHouseIfExists(facilityId.toString(), new Long(6));
			    	  }
			    	ArrayList popIds = new ArrayList();
			    	popIds.add(new Long(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
			    	popIds.add(new Long(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
			    	popIds.add(new Long(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
			    	// Recalculate total population
			    	populationService.updateFacilityPopulationCount(facilityId, popIds, user.getUserId());
			    	if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
				    	// cost curve rerun
			    		if (dbProjectedResOWTSHouses != projectedResOWTSHouses || dbresPopulationPerHouse != resPopulationPerHouse){
	                    	System.out.println("rerun cost curve");
	    	    		   	ArrayList costCurveIdList = new ArrayList();
	    	    			costCurveIdList.add(new Long(1));
	    	    			costCurveIdList.add(new Long(2));
	    	    			costCurveIdList.add(new Long(4));
	    	    			costCurveIdList.add(new Long(5));
	    	    			costCurveIdList.add(new Long(6));
	    	    			costCurveIdList.add(new Long(10));
	    	    			costCurveIdList.add(new Long(11));
	    	    			costCurveIdList.add(new Long(12));
	    	    			costCurveIdList.add(new Long(13));
	    	    			costCurveIdList.add(new Long(15));
	    	    			costCurveIdList.add(new Long(16));
	    	    			costCurveIdList.add(new Long(17));
	    	    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
	                    }
	                    if (dbPresentResOWTSHouses != presentResOWTSHouses || dbresPopulationPerHouse != resPopulationPerHouse){
	                    	ArrayList costCurveIdList = new ArrayList();
	    	    			costCurveIdList.add(new Long(11));
	    	    			costCurveIdList.add(new Long(12));
	    	    			costCurveIdList.add(new Long(13));
	    	    			costCurveIdList.add(new Long(15));
	    	    			costCurveIdList.add(new Long(16));
	    	    			costCurveIdList.add(new Long(17));
	    	    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
	                    }
				    	if (dbProjectedNonResOWTSHouses != projectedNonResOWTSHouses || dbNonResPopulationPerHouse != nonResPopulationPerHouse){
	                    	ArrayList costCurveIdList = new ArrayList();
	                    	costCurveIdList.add(new Long(11));
	    	    			costCurveIdList.add(new Long(12));
	    	    			costCurveIdList.add(new Long(13));
	    	    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
	                    }
				    	if (dbPresentNonResOWTSHouses != presentNonResOWTSHouses || dbNonResPopulationPerHouse != nonResPopulationPerHouse){
	                    	ArrayList costCurveIdList = new ArrayList();
	                    	costCurveIdList.add(new Long(11));
	    	    			costCurveIdList.add(new Long(12));
	    	    			costCurveIdList.add(new Long(13));
	    	    			costCurveIdList.add(new Long(15));
	    	    			costCurveIdList.add(new Long(16));
	    	    			costCurveIdList.add(new Long(17));
	    	    			populationService.setUpCostCurvesForRerun(costCurveIdList, facilityId);
	                    }
				    	}
			    	facilityService.performPostSaveUpdates(facilityId, FacilityService.DATA_AREA_POPULATION, user);
	    		  	return mapping.findForward("success");
			    }
                // Check if facility has facility type that is present and valid for ISDS Population                
	    		String enableISDSPresent = populationService.isISDSPresentEnabled(facilityId.toString());
	            // Check if facility has facility type that is projected and valid for ISDS Population
	    		String enableISDSProjected = populationService.isISDSProjectedEnabled(facilityId.toString());
                // Check if facility has facility type that is valid for OWTS Population
	    		String isOWTSystemExists = populationService.isOWTSystemExists(facilityId.toString());
                
	    		//set Onsite Wastewater Treatment Systems Units
	   			int presentResOWTSHouses = populationService.getPresentResidentialOnsiteWastewaterHouses(facilityId);
	   			int projectedResOWTSHouses = populationService.getProjectedResidentialOnsiteWastewaterHouses(facilityId);
	   			int presentNonResOWTSHouses = populationService.getPresentNonResidentialOnsiteWastewaterHouses(facilityId);
	   			int projectedNonResOWTSHouses = populationService.getProjectedNonResidentialOnsiteWastewaterHouses(facilityId);
	   			
	   			float resOWTSPopulationPerHouse = populationService.getResidentialOWTSPopulationPerHouse(facilityId);
	   			float nonResOWTSPopulationPerHouse = populationService.getNonResidentialOWTSPopulationPerHouse(facilityId);
	   			
	   			populationInformationForm.setDisplayOWTSDetails("Y");
	   			populationInformationForm.setPresentResOWTSHouses(presentResOWTSHouses);
	   			populationInformationForm.setProjectedResOWTSHouses(projectedResOWTSHouses);
	   			populationInformationForm.setPresentNonResOWTSHouses(presentNonResOWTSHouses);
	   			populationInformationForm.setProjectedNonResOWTSHouses(projectedNonResOWTSHouses);
	   			populationInformationForm.setResOWTSPopulationPerHouse(resOWTSPopulationPerHouse);
	   			populationInformationForm.setNonResOWTSPopulationPerHouse(nonResOWTSPopulationPerHouse);
	   				   			
				populationInformationForm.setEnableISDSPresent(enableISDSPresent);
				populationInformationForm.setEnableISDSProjected(enableISDSProjected);
				populationInformationForm.setIsOWTSystemExists(isOWTSystemExists);
				
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
	
	 private PopulationService populationService;
	 private FacilityService facilityService;
	    
	    //  set the facility service
	    public void setFacilityService(FacilityService fs){
	       facilityService = fs;    	
	    }  
	 	
	 //  set the population service
	 public void setPopulationService(PopulationService ps){
	     populationService = ps;    	
	 }


}
