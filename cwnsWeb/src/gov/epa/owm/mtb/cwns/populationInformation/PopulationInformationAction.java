package gov.epa.owm.mtb.cwns.populationInformation;

/**
 *
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PopulationInformationAction extends CWNSAction {

    private Logger log = Logger.getLogger(this.getClass());

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
    	    PortletRenderRequest prr = (PortletRenderRequest)
    		      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
   		    
    	    PopulationInformationForm populationInformationForm = (PopulationInformationForm)form;
    	    
            //Get user object
   			HttpServletRequest httpReq = (HttpServletRequest) request;
   			HttpSession httpSess = httpReq.getSession();
   			CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
   		
 		   //get the facility ID from the page parameter - configured as input parameter in provider.xml
 		   String facNum = prr.getParameter("facilityId");
 		  
 		   log.debug("facility number---"+facNum);	

 	       /*
 	         Enumeration keys = prr.getParameterNames();
 	        while (keys.hasMoreElements()) {
 	        	String name = (String)keys.nextElement();
 	    		log.debug("Para: " + name + " - " + prr.getParameter(name));
 	        }
 		   */

			boolean isSaveAction = false;
		      if(prr.getParameter("populationAct") != null && (prr.getParameter("populationAct")).equalsIgnoreCase("save"))
		      {
		    	  isSaveAction = true;
		    	  log.debug("isSaveAction - true");
		      }		      			
			
		    if(isSaveAction)  
		    {
		    	//save small community flag
		    	Character smallCommuFlag = new Character(populationInformationForm.getSmallCommunityExceptionFlag()=='Y'?'Y':'N');
		    	populationService.saveOrUpdateSmallCommunityFlag(facNum, smallCommuFlag, user.getUserId());
		    }

 		  	/* obtain the Facility object */
 	    	Facility facility = facilityService.findByFacilityId(facNum);   
 		  	   
    		    		
           // Check if feedback version exists 
    		Facility feedbackVersion = (Facility)facilityService.getFeedbackVersionOfFacility(facNum);	
    		if (feedbackVersion != null){
    			populationInformationForm.setHasFeedbackVersion("Y");
    		}
    	   	
    		String Sreviewstatustype = facility.getReviewStatusRef().getReviewStatusId();
    		String Freviewstatustype = feedbackVersion == null?"":feedbackVersion.getReviewStatusRef().getReviewStatusId();
    		if (("SAS".equalsIgnoreCase(Sreviewstatustype) || "SIP".equalsIgnoreCase(Sreviewstatustype) || "SCR".equalsIgnoreCase(Sreviewstatustype))
    			&& (feedbackVersion != null &&
    			   ("LAS".equalsIgnoreCase(Freviewstatustype) || "LIP".equalsIgnoreCase(Freviewstatustype)))) {
    			populationInformationForm.setShowWarningMessage("Y");	
    		}
    		
            // Check if facility has facility type that is present and valid for Receiving Collection Population
    		String enableRecCollectionPresent = populationService.isRecCollectionPresentEnabled(facNum);
            // Check if facility has facility type that is projected and valid for Receiving Collection Population
    		String enableRecCollectionProjected = populationService.isRecCollectionProjectedEnabled(facNum);
            // Check if facility has facility type that is present and valid for Decentralized System Population
    		String enableDecentralizedPresent = populationService.isDecentralizedPresentEnabled(facNum);
            // Check if facility has facility type that is projected and valid for Decentralized System Population
    		String enableDecentralizedProjected = populationService.isDecentralizedProjectedEnabled(facNum);
            // Check if facility has facility type that is present and valid for ISDS Population
    		String enableISDSPresent = populationService.isISDSPresentEnabled(facNum);
            // Check if facility has facility type that is projected and valid for ISDS Population
    		String enableISDSProjected = populationService.isISDSProjectedEnabled(facNum);
    		
   			populationInformationForm.setEnableRecCollectionPresent(enableRecCollectionPresent);
   			
   			populationInformationForm.setIsClusteredSystemExists(populationService.isClusteredSystemExists(facNum));
   			populationInformationForm.setIsOWTSystemExists(populationService.isOWTSystemExists(facNum));
   			/* sewershed check has been removed from the busines rules
   			if("Y".equalsIgnoreCase(enableRecCollectionPresent))
   			  populationInformationForm.setEnableRecCollectionResPresent(populationService.isRecCollectionPresentResEnabled(facNum));
   			else
     	      populationInformationForm.setEnableRecCollectionResPresent("N"); 				
			*/
   			populationInformationForm.setEnableRecCollectionResPresent(enableRecCollectionPresent);
   			populationInformationForm.setEnableRecCollectionProjected(enableRecCollectionProjected);
			populationInformationForm.setEnableDecentralizedPresent(enableDecentralizedPresent);
			populationInformationForm.setEnableDecentralizedProjected(enableDecentralizedProjected);
			populationInformationForm.setEnableISDSPresent(enableISDSPresent);
			populationInformationForm.setEnableISDSProjected(enableISDSProjected);
		      
		      populationInformationForm.setPopulationAct("");
		      
		    if("Y".equalsIgnoreCase(enableRecCollectionPresent) || "Y".equalsIgnoreCase(enableRecCollectionProjected))
		    {
	    	    log.debug("ready to handle RecCollection");
		    	
				int dbResRecPresentPopCnt=0, dbNonResRecPresentPopCnt=0,
				    dbResRecProjectedPopCnt=0, dbResRecProjectedYear=0, dbNonResRecProjectedPopCnt=0, dbResNonRecProjectedYear=0;
	    		// Get Resident and Non-Resident Receiving collection present population count
	  		      HashMap recPresentPopulationCount = populationService.getPresentPopulationByPopIdAndFacId(facNum,"1","2");
	  		      if (recPresentPopulationCount != null && !recPresentPopulationCount.isEmpty())
	  		      {
	  		    	 if (recPresentPopulationCount.containsKey("1"))
	  		    		dbResRecPresentPopCnt = ((Integer)recPresentPopulationCount.get("1")).intValue();	
			    	 if (recPresentPopulationCount.containsKey("2"))
			    		dbNonResRecPresentPopCnt = ((Integer)recPresentPopulationCount.get("2")).intValue();
	  		      }
	
			    // Get Resident and Non-Resident Receiving collection projected population count
			      HashMap recProjectedPopulationCount = populationService.getProjectedPopulationByPopIdAndFacId(facNum,"1","2");
			      if (recProjectedPopulationCount != null && !recProjectedPopulationCount.isEmpty())
			      {
	
			    	  if (recProjectedPopulationCount.containsKey("1")){
			    		  Object[] o = (Object[])recProjectedPopulationCount.get("1");
			    		  dbResRecProjectedPopCnt=((Integer)o[0]).intValue();
			    		  dbResRecProjectedYear=((Short)o[1]).intValue();
			    	  }    
	
			    	  if (recProjectedPopulationCount.containsKey("2")){
			    		  Object[] o = (Object[])recProjectedPopulationCount.get("2");
			    		  dbNonResRecProjectedPopCnt=((Integer)o[0]).intValue();
			    		  dbResNonRecProjectedYear=((Short)o[1]).intValue();
			    	  }    
			      }
			      
			      if(isSaveAction)
			      {
			    	  if(populationInformationForm.getResRecPresentPopCnt() !=0 || 
			    	     (populationInformationForm.getResRecProjectedPopCnt()!=0/* && 
			    	      populationInformationForm.getResRecProjectedYear() !=0 */))
			    	  {
			    		//saveOrUpdate  "1"
			    		  Object[] values = new Object[]
			    		    {new Integer(populationInformationForm.getResRecPresentPopCnt()),
			    			 new Integer(populationInformationForm.getResRecProjectedPopCnt()),
			    			 new Short((short)populationInformationForm.getResRecProjectedYear())};
			    		  populationService.saveOrUpdatePopulationInfo(facNum, values, new Long(1), user.getUserId());
			    		  		    		  
			    	  }
			    	  else
			    	  {
			    		  //delete "1"
			    		  populationService.deletePopulationInfoIfExists(facNum, new Long(1), user.getUserId());
			    	  }
			    	  
			    	  if(populationInformationForm.getNonResRecPresentPopCnt() !=0 || 
					    	     (populationInformationForm.getNonResRecProjectedPopCnt()!=0/* && 
					    	      populationInformationForm.getNonResRecProjectedYear() !=0 */))
					    	  {
					    		//saveOrUpdate  "2"
				    		  	Object[] values = new Object[]
				    		   	  {new Integer(populationInformationForm.getNonResRecPresentPopCnt()),
				    		   	   new Integer(populationInformationForm.getNonResRecProjectedPopCnt()),
				    		   	   new Short((short)populationInformationForm.getNonResRecProjectedYear())};
				    		   	populationService.saveOrUpdatePopulationInfo(facNum, values, new Long(2), user.getUserId());
					    	  }
					    	  else
					    	  {
					    		  //delete "2"
					    		  populationService.deletePopulationInfoIfExists(facNum, new Long(2), user.getUserId());
					    	  }
			    	  if (dbResRecPresentPopCnt!=populationInformationForm.getResRecPresentPopCnt()){
		    			  ArrayList costCurveIdList1 = new ArrayList();
		    			  costCurveIdList1.add(new Long(7));
		    			  Collection presentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facNum), populationService.PRESENT_ONLY);
		    			  if (presentSewershed!=null){
		  					Iterator iter2 = presentSewershed.iterator();
		  					while (iter2.hasNext()){
		  						populationService.setUpCostCurvesForRerun(costCurveIdList1, (Long)iter2.next());
		  					}
		  				}
		    		  }
			    	  if (dbResRecProjectedPopCnt!=populationInformationForm.getResRecProjectedPopCnt() || 
			    			  dbNonResRecProjectedPopCnt!=populationInformationForm.getNonResRecProjectedPopCnt()){
			    		ArrayList costCurveIdList1 = new ArrayList();
			  			costCurveIdList1.add(new Long(1));
			  			costCurveIdList1.add(new Long(2));
			  			costCurveIdList1.add(new Long(4));
			  			costCurveIdList1.add(new Long(5));
			  			costCurveIdList1.add(new Long(6));
			  			costCurveIdList1.add(new Long(10));
			  			Collection projectedDownStream = populationService.getDownstreamFacilitiesListByDischargeType(facNum, populationService.PROJECTED_ONLY);
			  			if (projectedDownStream!=null){
			  				Iterator iter1 = projectedDownStream.iterator();
							while (iter1.hasNext()){
								String obj = (String)iter1.next();
								populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(obj));
							}
			  			}
			    	  }
			      }
			      else
			      {
		  		      populationInformationForm.setResRecPresentPopCnt(dbResRecPresentPopCnt);
		  		      populationInformationForm.setNonResRecPresentPopCnt(dbNonResRecPresentPopCnt);
			    	  populationInformationForm.setResRecProjectedPopCnt(dbResRecProjectedPopCnt);
			          populationInformationForm.setNonResRecProjectedPopCnt(dbNonResRecProjectedPopCnt);
			          populationInformationForm.setResRecProjectedYear(dbResRecProjectedYear);
			          populationInformationForm.setNonResRecProjectedYear(dbResNonRecProjectedYear);			    	  
			      }
		    } 
		    else
		    {
	  		      populationInformationForm.setResRecPresentPopCnt(0);
	  		      populationInformationForm.setNonResRecPresentPopCnt(0);
		    	  populationInformationForm.setResRecProjectedPopCnt(0);
		          populationInformationForm.setNonResRecProjectedPopCnt(0);
		          populationInformationForm.setResRecProjectedYear(0);
		          populationInformationForm.setNonResRecProjectedYear(0);			    	  		    	
		    }

		    if("Y".equalsIgnoreCase(enableDecentralizedPresent) || "Y".equalsIgnoreCase(enableDecentralizedProjected))
		    {		
	    	    log.debug("ready to handle Decentralized");
		    	
		      int dbResDecPresentPopCnt=0,dbNonResDecPresentPopCnt=0,
		      dbResDecProjectedPopCnt=0,dbNonResDecProjectedPopCnt=0,dbResDecProjectedYear=0,dbNonResDecProjectedYear=0;
		    // Get Resident and Non-Resident Decentralized Systems present population count
		      HashMap decPresentPopulationCount = populationService.getPresentPopulationByPopIdAndFacId(facNum,"7","8");
		      if (decPresentPopulationCount != null && !decPresentPopulationCount.isEmpty()){

		    	  if (decPresentPopulationCount.containsKey("7"))
		    		  dbResDecPresentPopCnt=(((Integer)decPresentPopulationCount.get("7")).intValue());

		    	  if (decPresentPopulationCount.containsKey("8"))
		    		  dbNonResDecPresentPopCnt=(((Integer)decPresentPopulationCount.get("8")).intValue());

		      }
		
    		    // Get Resident and Non-Resident Decentralized Systems projected population count
    		      HashMap decProjectedPopulationCount = populationService.getProjectedPopulationByPopIdAndFacId(facNum,"7","8");
    		      if (decProjectedPopulationCount != null && !decProjectedPopulationCount.isEmpty())
    		      {

    		    	  if (decProjectedPopulationCount.containsKey("7")){
    		    		  Object[] o = (Object[])decProjectedPopulationCount.get("7");
    		    		  dbResDecProjectedPopCnt=(((Integer)o[0]).intValue());
    		    		  dbResDecProjectedYear=(((Short)o[1]).intValue());
    		    	  }    

    		    	  if (decProjectedPopulationCount.containsKey("8")){
    		    		  Object[] o = (Object[])decProjectedPopulationCount.get("8");
    		    		  dbNonResDecProjectedPopCnt=(((Integer)o[0]).intValue());
    		    		  dbNonResDecProjectedYear=(((Short)o[1]).intValue());
    		    	  }    
    		      }
    		        
			    	  populationInformationForm.setResDecPresentPopCnt(dbResDecPresentPopCnt);
			          populationInformationForm.setNonResDecPresentPopCnt(dbNonResDecPresentPopCnt);
    		    	  populationInformationForm.setResDecProjectedPopCnt(dbResDecProjectedPopCnt);
    		          populationInformationForm.setNonResDecProjectedPopCnt(dbNonResDecProjectedPopCnt);
    		          populationInformationForm.setResDecProjectedYear(dbResDecProjectedYear);
    		          populationInformationForm.setNonResDecProjectedYear(dbNonResDecProjectedYear);	    	  
			          		      
		    }
		    else
		    {
		    	  populationInformationForm.setResDecPresentPopCnt(0);
		          populationInformationForm.setNonResDecPresentPopCnt(0);
		    	  populationInformationForm.setResDecProjectedPopCnt(0);
		          populationInformationForm.setNonResDecProjectedPopCnt(0);
		          populationInformationForm.setResDecProjectedYear(0);
		          populationInformationForm.setNonResDecProjectedYear(0);	    	  		    	
		    }
		    
		    if("Y".equalsIgnoreCase(enableISDSPresent) || "Y".equalsIgnoreCase(enableISDSProjected))
		    {
	    	    log.debug("ready to handle ISDS");
		    	
  		      int dbResISDSPresentPopCnt=0,dbNonResISDSPresentPopCnt=0,
		      dbResISDSProjectedPopCnt=0,dbNonResISDSProjectedPopCnt=0,dbResISDSProjectedYear=0,dbNonResISDSProjectedYear=0;	
		    	// Get Resident and Non-Resident Individual Sewage Disposal Systems present population count
    		      HashMap ISDSPresentPopulationCount = populationService.getPresentPopulationByPopIdAndFacId(facNum,"5","6");
    		      if (ISDSPresentPopulationCount != null && !ISDSPresentPopulationCount.isEmpty()){

    		    	  if (ISDSPresentPopulationCount.containsKey("5"))
    		    		  dbResISDSPresentPopCnt=(((Integer)ISDSPresentPopulationCount.get("5")).intValue());

    		    	  if (ISDSPresentPopulationCount.containsKey("6"))
    		    		  dbNonResISDSPresentPopCnt=(((Integer)ISDSPresentPopulationCount.get("6")).intValue());
    		      }
    	
    		    // Get Resident and Non-Resident Individual Sewage Disposal Systems projected population count
    		      HashMap ISDSProjectedPopulationCount = populationService.getProjectedPopulationByPopIdAndFacId(facNum,"5","6");
    		      if (ISDSProjectedPopulationCount != null && !ISDSProjectedPopulationCount.isEmpty())
    		      {

    		    	  if (ISDSProjectedPopulationCount.containsKey("5")){
    		    		 Object[] o = (Object[])ISDSProjectedPopulationCount.get("5");
    		    		 dbResISDSProjectedPopCnt=(((Integer)o[0]).intValue());
    		    		 dbResISDSProjectedYear=(((Short)o[1]).intValue());
    		    	  }    

    		    	  if (ISDSProjectedPopulationCount.containsKey("6")){
    		    		  Object[] o = (Object[])ISDSProjectedPopulationCount.get("6");
    		    		  dbNonResISDSProjectedPopCnt=(((Integer)o[0]).intValue());
    		    		  dbNonResISDSProjectedYear=(((Short)o[1]).intValue());
    		    	  }    
    		      }
    		                	  
    		    	  populationInformationForm.setResISDSPresentPopCnt(dbResISDSPresentPopCnt);
    		          populationInformationForm.setNonResISDSPresentPopCnt(dbNonResISDSPresentPopCnt);
    		    	  populationInformationForm.setResISDSProjectedPopCnt(dbResISDSProjectedPopCnt);
    		          populationInformationForm.setNonResISDSProjectedPopCnt(dbNonResISDSProjectedPopCnt);
    		          populationInformationForm.setResISDSProjectedYear(dbResISDSProjectedYear);
    		          populationInformationForm.setNonResISDSProjectedYear(dbNonResISDSProjectedYear);   	  
			         		      
		    }
		    else
		    {
		    	  populationInformationForm.setResISDSPresentPopCnt(0);
		          populationInformationForm.setNonResISDSPresentPopCnt(0);
		    	  populationInformationForm.setResISDSProjectedPopCnt(0);
		          populationInformationForm.setNonResISDSProjectedPopCnt(0);
		          populationInformationForm.setResISDSProjectedYear(0);
		          populationInformationForm.setNonResISDSProjectedYear(0);   	  		    	
		    }

        	PopulationHelper ph = (PopulationHelper)populationService.getUpStreamFacilitiesPopulationTotal(facNum);
        	
        	if(ph!=null)
        	{
        	  populationInformationForm.setUpstreamPresentResPopulation(ph.getPresentResPopulation());
        	  populationInformationForm.setUpstreamPresentNonResPopulation(ph.getPresentNonResPopulation());
        	  populationInformationForm.setUpstreamProjectedResPopulation(ph.getProjectedResPopulation());
        	  populationInformationForm.setUpstreamProjectedNonResPopulation(ph.getProjectedNonResPopulation());  
        	  
        	  populationInformationForm.setHasUpstream("Y");
        	}
        	else
        	{
          	  populationInformationForm.setHasUpstream("N");        		
        	}
        	//otherwise all 0s
    		
            // Check if facility is updatable or not and set form attribute
   	     if (facilityService.isUpdatable(user, new Long(facNum))){
   	    	populationInformationForm.setIsUpdatable("Y");
   	    	log.debug("populationInformationForm: isUpdatable: Y");
   		  }
   	     else
   	     {
    	    	populationInformationForm.setIsUpdatable("N");   	    	 
   	     }
   	    if(isSaveAction){ 
   	    	 //fesManager.runValidation(new Long(facNum), FacilityService.DATA_AREA_POPULATION, user.getUserId());
   	    	facilityService.performPostSaveUpdates(new Long(facNum), FacilityService.DATA_AREA_POPULATION, user);
   	    }
   	    if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
   	      ArrayList errorMessages = new ArrayList();
   	      if (!populationService.isFlowApportionmentCompleted(facNum)){
            errorMessages.add("error.population.flowApportionment");
            request.setAttribute("errorMessages", errorMessages);
   	      }
   	    }  
    	populationInformationForm.setSmallCommunityExceptionFlag(facility.getSmallCommunityExceptionFlag());	    	
    	request.setAttribute("populationform", populationInformationForm);
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
