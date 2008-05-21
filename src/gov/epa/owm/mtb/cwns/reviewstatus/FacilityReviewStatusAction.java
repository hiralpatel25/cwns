package gov.epa.owm.mtb.cwns.reviewstatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

public class FacilityReviewStatusAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		FacilityReviewStatusForm facilityReviewStatusForm = (FacilityReviewStatusForm) form;
		
		ArrayList a = new ArrayList();
		String currentReviewStatusId = "";
		String facilityFeedbackStatusId = "";
		ArrayList a1 = new ArrayList();
		String showfeedbackstatus = "N";
		Entity facilityFeedbackStatus = null;
		String isFacilitySmallCommunity = "N";
		String USFacilitiesInCorrectStatus="Y";
		String isFacilityInSewershed="N";
		
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		
        //	Get user privilages
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userrole = user.getCurrentRole();
		HashMap privileges = userrole.getPrivileges();
		
	   //get the facility ID from the page parameter - configured as input parameter in provider.xml
		String  facilityId = httpReq.getParameter("facilityId");
				
		String action = facilityReviewStatusForm.getFacilityReviewStatusAct();
		if ("save".equalsIgnoreCase(action)){
			if ("Y".equalsIgnoreCase(facilityReviewStatusForm.getIsUpdatable())){
			    saveReviewStatus(facilityReviewStatusForm.getFacilityId(),facilityReviewStatusForm.getCurrentReviewStatusId(),facilityReviewStatusForm.getSelectedReviewStatus(),user);
			    if(ReviewStatusRefService.RESTORE.equalsIgnoreCase(facilityReviewStatusForm.getSelectedReviewStatus()))
			    	facilityService.performPostSaveUpdates(new Long(facilityId), FacilityService.DATA_AREA_FACILITY, user);
			}    
			if ("Y".equalsIgnoreCase(facilityReviewStatusForm.getIsFeedbackStatusUpdatable())){
				reviewStatusRefService.saveFeedbackReviewStatus(facilityReviewStatusForm.getFacilityId(),facilityReviewStatusForm.getCurrentFeedbackStatusId(),facilityReviewStatusForm.getSelectedFeedbackStatus(),user);
				log.debug("current feedback status--"+facilityReviewStatusForm.getSelectedFeedbackStatus());
			}	
		}
		Entity currentReviewStatus = reviewStatusRefService.findFacilityReviewStatus(facilityId);
		HashMap reviewStatues = reviewStatusRefService.getAllReviewStatuses();		
		// check if feedback version exists for the facility
        boolean hasfeedbackversion = reviewStatusRefService.hasFeedbackVersion(facilityId);
        // if feedback version is updatable
		if(reviewStatusRefService.isFeedbackStatusUpdatable(user, new Long(facilityId))){
          showfeedbackstatus = "Y";
          facilityReviewStatusForm.setIsFeedbackStatusUpdatable("Y");
          //Get Feedback Status for the facility
          facilityFeedbackStatus = reviewStatusRefService.getFacilityFeedbackStatus(facilityId);
          if (facilityFeedbackStatus != null){
        	  facilityFeedbackStatusId = facilityFeedbackStatus.getKey();
        	  a1.add(facilityFeedbackStatus);
          }  
          else
        	  a1.add(new Entity("","")); 
          if ("".equalsIgnoreCase(facilityFeedbackStatusId)){
        	  a1.add(new Entity(ReviewStatusRefService.LOCAL_ASSIGNED,(String)reviewStatues.get(ReviewStatusRefService.LOCAL_ASSIGNED)));

          }
          else if (ReviewStatusRefService.LOCAL_ASSIGNED.equalsIgnoreCase(facilityFeedbackStatusId) || ReviewStatusRefService.LOCAL_IN_PROGRESS.equalsIgnoreCase(facilityFeedbackStatusId)){
        	  if (privileges.containsKey(new Long(6)) || privileges.containsKey(new Long(7))){
        		  a1.add(new Entity(ReviewStatusRefService.REVOKED,"Revoked"));
        	  }
        	  if (privileges.containsKey(CurrentUser.SUBMIT_FOR_STATE_REVIEW)){
        		  a1.add(new Entity(ReviewStatusRefService.STATE_REVIEW_REQUESTED,(String)reviewStatues.get(ReviewStatusRefService.STATE_REVIEW_REQUESTED)));
        	  }
          }else if (ReviewStatusRefService.STATE_REVIEW_REQUESTED.equalsIgnoreCase(facilityFeedbackStatusId)){
        	  if (privileges.containsKey(new Long(6))) {
        		  a1.add(new Entity(ReviewStatusRefService.STATE_APPLIED,(String)reviewStatues.get(ReviewStatusRefService.STATE_APPLIED)));
        		  a1.add(new Entity(ReviewStatusRefService.REVOKED,"Revoked"));
        		  a1.add(new Entity(ReviewStatusRefService.LOCAL_ASSIGNED,(String)reviewStatues.get(ReviewStatusRefService.LOCAL_ASSIGNED)));
        	  }   
        	  else
        		  if (privileges.containsKey(new Long(7))){
        			  a1.add(new Entity(ReviewStatusRefService.STATE_APPLIED,(String)reviewStatues.get(ReviewStatusRefService.STATE_APPLIED)));
        			  a1.add(new Entity(ReviewStatusRefService.REVOKED,"Revoked"));
        		  }
          }else
        	  if (ReviewStatusRefService.STATE_APPLIED.equalsIgnoreCase(facilityFeedbackStatusId)){
        		  if (privileges.containsKey(new Long(6))){
        			  //a1.add(new Entity(ReviewStatusRefService.REVOKED,"Revoked"));
        			  a1.add(new Entity(ReviewStatusRefService.LOCAL_ASSIGNED,(String)reviewStatues.get(ReviewStatusRefService.LOCAL_ASSIGNED)));
        		  }
        		  /* bug #930-change of business rule
        		  else
        			  if (privileges.containsKey(new Long(7))){
        				  a1.add(new Entity(ReviewStatusRefService.REVOKED,"Revoked"));
        			  }*/
        	  }
		}
		else if(hasfeedbackversion){ //if feedback version doesn't exists but facility has a feedback copy
			showfeedbackstatus = "Y";
			facilityReviewStatusForm.setIsFeedbackStatusUpdatable("N");
			//Get Feedback Status for the facility
			facilityFeedbackStatus = reviewStatusRefService.getFacilityFeedbackStatus(facilityId);
			if (facilityFeedbackStatus != null){
				facilityReviewStatusForm.setCurrentFeedbackStatus(facilityFeedbackStatus.getValue());
			}
		}
		else{ //feedback status is not updatable and no feedback copy exists
			showfeedbackstatus = "N";
			facilityReviewStatusForm.setIsFeedbackStatusUpdatable("N");
		}
				
		// Check if S facility's review status is updatable
		if(!reviewStatusRefService.isUpdatable(user, new Long(facilityId))){ //review status is not updatable
			if (currentReviewStatus != null){
				facilityReviewStatusForm.setCurrentReviewStatus(currentReviewStatus.getValue());
			}
            
	        
	        facilityReviewStatusForm.setIsUpdatable("N");
		}
        
		// review status is updatable 
		else {	
				
		     if (currentReviewStatus != null){
		        currentReviewStatusId = currentReviewStatus.getKey();
		     }    
				
		//Get FacilityEntryStatus objects where FacilityEntryStatus.errorFlag = Y for all applicable data areas
        Collection facilityEntryStatusWithErrors =  reviewStatusRefService.getFacilityEntryStatusWithErrors(facilityId);
		
        //Get FacilityReviewStatus objects where FacilityReviewStatus.errorFlag = Y for all applicable data areas 
        Collection federalReviewStatusWithErrors = reviewStatusRefService.getFederalReviewStatusWithErrors(facilityId);
        
        //Get Facility’s latest review comment timestamp
        Date reviewcomment_lastupdated = reviewStatusRefService.getLatestReviewCommentTs(facilityId);
        
        //Get Facility’s latest facility review status timestamp
        Date reviewstatus_lastupdated = reviewStatusRefService.getLatestReviewStatusTs(facilityId);
        
        //check if feedback version exists for the facility
        //boolean hasfeedbackversion = reviewStatusRefService.hasFeedbackVersion(facilityId);
        
        //check if Facility is a small community
        isFacilitySmallCommunity = reviewStatusRefService.isFacilitySmallCommunity(facilityId);
        // Check if facility is in a sewershed
        if(populationService.getRelatedSewerShedFacilities(facilityId)!=null && 
        		populationService.getRelatedSewerShedFacilities(facilityId).size()>1){
        	isFacilityInSewershed = "Y";
        }
                        
        // Get all upstream facilities if the selected facility is in a sewershed and check if all upstream
        // facilities are in correct status, i.e. 'FRR' or 'FRC' or 'FA'
        if("Y".equalsIgnoreCase(isFacilityInSewershed)){
           Collection upStreamFacilities = populationService.getUpstreamFacilityIds(facilityId);
           if(upStreamFacilities != null && !(upStreamFacilities.isEmpty())){
        	  Iterator iter = upStreamFacilities.iterator();
              while (iter.hasNext()){
        		String facId = (String)iter.next();
        		if(reviewStatusRefService.isFacilityInCorrectStatus(facId)==false){
        			USFacilitiesInCorrectStatus = "N";
        			break;
        		}
        	  }
        
           }
        }
               
        a.add(currentReviewStatus);
        if (ReviewStatusRefService.STATE_ASSIGNED.equalsIgnoreCase(currentReviewStatusId) || ReviewStatusRefService.STATE_IN_PROGRESS.equalsIgnoreCase(currentReviewStatusId)){ 
            if (privileges.containsKey(new Long(7))){
            	/*
            	if("Y".equalsIgnoreCase(isFacilityInSewershed)){
            	   if(facilityEntryStatusWithErrors.size()==0 && "Y".equalsIgnoreCase(USFacilitiesInCorrectStatus)){
        	        a.add(new Entity(ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED,(String)reviewStatues.get(ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED)));
        	       }
            	}else{*/
            		a.add(new Entity(ReviewStatusRefService.DELETED,(String)reviewStatues.get(ReviewStatusRefService.DELETED)));
            		if(facilityEntryStatusWithErrors.size()==0)
            			a.add(new Entity(ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED,(String)reviewStatues.get(ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED)));	
            	//}
            }
            else
            	if (privileges.containsKey(new Long(6)) && !("Y".equalsIgnoreCase(isFacilityInSewershed))){
            		a.add(new Entity(ReviewStatusRefService.DELETED,(String)reviewStatues.get(ReviewStatusRefService.DELETED)));
            	}
        }
        else
        if (ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED.equalsIgnoreCase(currentReviewStatusId) || ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION.equalsIgnoreCase(currentReviewStatusId)){
        	if (privileges.containsKey(new Long(3))){
        		a.add(new Entity(ReviewStatusRefService.STATE_REQUESTED_RETURN,"State Requested Return"));
        		if (federalReviewStatusWithErrors.size()>0 && reviewcomment_lastupdated != null &&
        				    reviewcomment_lastupdated.after(reviewstatus_lastupdated)){
        			a.add(new Entity(ReviewStatusRefService.STATE_CORRECTION_REQUESTED,(String)reviewStatues.get(ReviewStatusRefService.STATE_CORRECTION_REQUESTED)));
        			
        		}
        		else
        			if (federalReviewStatusWithErrors.size()==0){
        				a.add(new Entity(ReviewStatusRefService.FEDERAL_ACCEPTED,(String)reviewStatues.get(ReviewStatusRefService.FEDERAL_ACCEPTED)));
        				
            		}
        	}
        }
        else
        if (ReviewStatusRefService.FEDERAL_ACCEPTED.equalsIgnoreCase(currentReviewStatusId)){
        	if (privileges.containsKey(new Long(3))){
        		if (federalReviewStatusWithErrors.size()>0 && reviewcomment_lastupdated != null &&
        				    reviewcomment_lastupdated.after(reviewstatus_lastupdated)){
        			a.add(new Entity(ReviewStatusRefService.STATE_CORRECTION_REQUESTED,(String)reviewStatues.get(ReviewStatusRefService.STATE_CORRECTION_REQUESTED)));
        			
        		}
        	}
        }
        else
        	if (ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equalsIgnoreCase(currentReviewStatusId)){
        		if (privileges.containsKey(new Long(7))){
        			/*if("Y".equalsIgnoreCase(isFacilityInSewershed)){
        				if(facilityEntryStatusWithErrors.size()==0 && "Y".equalsIgnoreCase(USFacilitiesInCorrectStatus)){	
        					a.add(new Entity(ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION,(String)reviewStatues.get(ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION))); 
        				}
        			}
        			else*/
        				if(facilityEntryStatusWithErrors.size()==0) 
        			       a.add(new Entity(ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION,(String)reviewStatues.get(ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION)));
        			
        		}
        	}
        else
        if (ReviewStatusRefService.DELETED.equalsIgnoreCase(currentReviewStatusId)){
        	if (privileges.containsKey(new Long(6)) || privileges.containsKey(new Long(7))){
        		a.add(new Entity(ReviewStatusRefService.RESTORE,"Restore"));
        		
        	}
        }
        
	   } // end of review status updatable else 
		
		Collection facilities = populationService.getRelatedSewerShedFacilities(facilityId);
		// Checking for Data Area errors 
		boolean dataAreaErrors = false;
		if(facilities!=null && facilities.size()>1){
			Iterator iter = facilities.iterator();
			while(iter.hasNext()&&!dataAreaErrors){
				Collection facilityEntryStatusWithErrors =  reviewStatusRefService.getFacilityEntryStatusWithErrors((String)iter.next());
				if(facilityEntryStatusWithErrors!=null && facilityEntryStatusWithErrors.size()>0)
					dataAreaErrors = true;
			}
		}
		request.setAttribute("hasDataAreaErrors", dataAreaErrors?"Y":"N");
		
		// Checking for correction indicators
		boolean hasCorrectionIndicators = false;
		if(facilities!=null && facilities.size()>1){
			Iterator iter = facilities.iterator();
			while(iter.hasNext()&&!hasCorrectionIndicators){
				Collection federalReviewStatusWithErrors = reviewStatusRefService.getFederalReviewStatusWithErrors((String)iter.next());
				if(federalReviewStatusWithErrors!=null && federalReviewStatusWithErrors.size()>0)
					hasCorrectionIndicators = true;
			}
		}
		request.setAttribute("hasCorrectionIndicators", hasCorrectionIndicators?"Y":"N");
		
		//Check if all sewershed facilities are in state
		boolean outOfState = false;
		if(facilities!=null && facilities.size()>1){
			Iterator iter = facilities.iterator();
			while(iter.hasNext()&&!outOfState){
				outOfState = populationService.isOutOfState((String)iter.next(), facilityId);
			}
		}
				
		// get all small community facilities in the sewershed if exists
		String smallCommunityFacilities = reviewStatusRefService.getSmallcomunityFacilities(facilityId);
		  request.setAttribute("smallCommunityFacilities", smallCommunityFacilities);
		  
		// get facilities in the sewershed which feedback copy with review status of LAS or LIP  
		String facilitiesWithFeedbackCopy = reviewStatusRefService.getFacilitiesWithFeedbackCopy(facilityId);
		  request.setAttribute("facilitiesWithFeedbackCopy", facilitiesWithFeedbackCopy);
				
		request.setAttribute("facilitiesInState", outOfState?"N":"Y");
		request.setAttribute("isFacilityInSewershed", isFacilityInSewershed);
        facilityReviewStatusForm.setFacilityId(facilityId);
        facilityReviewStatusForm.setCurrentReviewStatusId(currentReviewStatusId);
        facilityReviewStatusForm.setReviewStatues(a);
        facilityReviewStatusForm.setCurrentFeedbackStatusId(facilityFeedbackStatusId);
        facilityReviewStatusForm.setFeedBackStatues(a1);
        facilityReviewStatusForm.setShowfeedbackstatus(showfeedbackstatus);
        facilityReviewStatusForm.setIsFacilitySmallCommunity(isFacilitySmallCommunity);
        facilityReviewStatusForm.setUserType(user.getCurrentRole().getLocationTypeId());
        request.setAttribute("facilityReviewStatus", facilityReviewStatusForm);
        
        	String key ="help."+user.getCurrentRole().getLocationTypeId()+".reviewstatus";
		    String defaultkey = "help.reviewstatus"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		request.setAttribute("helpKey", helpKey);
        return mapping.findForward("success");
	}
  
	private ActionErrors validateRulesForErrors(String facilityId, String currentReviewStatusId, String selectedReviewStatus, String user) {
		boolean dataAreaErrors = false;
		boolean hasCorrectionIndicator = false;
		if(UserServiceImpl.LOCATION_TYPE_ID_STATE.equals(user)){
		if (ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED.equalsIgnoreCase(selectedReviewStatus) 
				|| ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION.equalsIgnoreCase(selectedReviewStatus)){
			Collection facilities = populationService.getRelatedSewerShedFacilities(facilityId);
			if(facilities!=null && facilities.size()>1){
				Iterator iter = facilities.iterator();
				while(iter.hasNext()&&!dataAreaErrors){
					Collection facilityEntryStatusWithErrors =  reviewStatusRefService.getFacilityEntryStatusWithErrors(((Long)iter.next()).toString());
					if(facilityEntryStatusWithErrors!=null && facilityEntryStatusWithErrors.size()>0)
						dataAreaErrors = true;
				}
			}
		}
		}
		
		if(UserServiceImpl.LOCATION_TYPE_ID_FEDERAL.equals(user)){
			if (ReviewStatusRefService.FEDERAL_ACCEPTED.equalsIgnoreCase(selectedReviewStatus)){
				Collection facilities = populationService.getRelatedSewerShedFacilities(facilityId);
				if(facilities!=null && facilities.size()>1){
					Iterator iter = facilities.iterator();
					while(iter.hasNext()&&!hasCorrectionIndicator){
						Collection federalReviewStatusWithErrors = reviewStatusRefService.getFederalReviewStatusWithErrors(((Long)iter.next()).toString());
						if(federalReviewStatusWithErrors!=null && federalReviewStatusWithErrors.size()>0)
							hasCorrectionIndicator = true;
					}
				}
			}
			}
		
		return null;
	}
	
	private String validateRulesForConfirmation(String facilityId, String selectedReviewStatus, String user){
		boolean confirm1 = false;
		boolean confirm2 = false;
		if(UserServiceImpl.LOCATION_TYPE_ID_STATE.equals(user)){
			if (ReviewStatusRefService.FEDERAL_REVIEW_REQUESTED.equalsIgnoreCase(selectedReviewStatus) 
					|| ReviewStatusRefService.FEDERAL_REVIEW_CORRECTION.equalsIgnoreCase(selectedReviewStatus)){
				Collection facilities = populationService.getRelatedSewerShedFacilities(facilityId);
				if(facilities!=null && facilities.size()>1){
					Iterator iter = facilities.iterator();
					while(iter.hasNext()&&!confirm1){
						Collection facilityEntryStatusWithErrors =  reviewStatusRefService.getFacilityEntryStatusWithErrors(((Long)iter.next()).toString());
						if(facilityEntryStatusWithErrors==null || facilityEntryStatusWithErrors.size()==0)
							confirm1 = true;
					}
				}
			}	
		}
		
		if(UserServiceImpl.LOCATION_TYPE_ID_FEDERAL.equals(user)){
			if (ReviewStatusRefService.FEDERAL_ACCEPTED.equalsIgnoreCase(selectedReviewStatus)){
				Collection facilities = populationService.getRelatedSewerShedFacilities(facilityId);
				if(facilities!=null && facilities.size()>1){
					Iterator iter = facilities.iterator();
					while(iter.hasNext()&&!confirm2){
						Collection federalReviewStatusWithErrors = reviewStatusRefService.getFederalReviewStatusWithErrors(((Long)iter.next()).toString());
						if(federalReviewStatusWithErrors==null || federalReviewStatusWithErrors.size()==0)
							confirm2 = true;
					}
				}
			}
		}
		
	 return null;	
	}

	private void saveReviewStatus(String facilityId, String currentReviewStatusId, String selectedReviewStatus, CurrentUser user) {
		reviewStatusRefService.saveReviewStatus(facilityId, currentReviewStatusId, selectedReviewStatus, user);
		
	}

	private ReviewStatusRefService reviewStatusRefService;
	
	private PopulationService populationService;
	
	private FacilityService facilityService;

	public void setReviewStatusRefService(ReviewStatusRefService rsf) {
		reviewStatusRefService = rsf;
	}
	
    //  set the population service
    public void setPopulationService(PopulationService ps){
    	populationService = ps;    	
    } 
    
    /* set the facility service */
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }

}
