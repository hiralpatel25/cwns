package gov.epa.owm.mtb.cwns.discharge;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityDischarge;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.PopulationService;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Lockheed Martin CWNS Team
 * @version 1.0
 */
public class DischargeAction extends CWNSAction {
	
	public static final String ACTION_DEFAULT      = "default_discharge";
	public static final String ACTION_EDIT         = "edit_discharge";
	public static final String ACTION_PROCESS_EDIT = "process_edit_discharge";
	public static final String ACTION_ADD          = "add_discharge";
	public static final String ACTION_PROCESS_ADD  = "process_add_discharge";
	public static final String ACTION_DELETE       = "delete_discharge";
	
	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

   		PortletRenderRequest pRequest = (PortletRenderRequest)
   				request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
   		DischargeForm dForm = (DischargeForm) form;
        ActionErrors errors1 = null;
        ActionErrors errors2 = null;
        ActionErrors errors = new ActionErrors();
	    //TODO: Check if provider session works in the portal environment
   		//ProviderSession pSession = prr.getSession();
	    HttpSession pSession = request.getSession(false);
		//ProviderSession pSession = prr.getSession();	    
	    if(pSession==null){
	    	throw new ApplicationException("Unable to fetch provider session");	
	    }

	    /* Determine the action to take. */  		    
	    String action = null;
  		if (pRequest.getParameter("action") != null && !"".equals(pRequest.getParameter("action"))) {
   			action = pRequest.getParameter("action");
   		}else {
   			action=ACTION_DEFAULT;
   		}
  		log.debug("action = "+action);

	    CurrentUser currentUser = (CurrentUser) pSession.getAttribute(CurrentUser.CWNS_USER);
		String      facilityId  = pRequest.getParameter("facilityId");
		Long        facId       = new Long(facilityId);
				
		if (ACTION_PROCESS_ADD.equals(action)||ACTION_PROCESS_EDIT.equals(action)){
		  if(dForm.getMethodId().equals(DischargeService.DISCHARGE_TO_ANOTHER_FACILITY)&&
				!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())){
			char presentFlag = 'N';
			char projectedFlag = 'N';
			if (dForm.getStatus().equals(DischargeForm.PRESENT)){ 				
				presentFlag = 'Y';
			} else if (dForm.getStatus().equals(DischargeForm.PROJECTED)) {
				      projectedFlag = 'Y';
			       } else if (dForm.getStatus().equals(DischargeForm.PRESENT_AND_PROJECTED)) {
			    	         presentFlag = 'Y';
			    	         projectedFlag = 'Y';
							}
			if (ACTION_PROCESS_EDIT.equals(action)){
				FacilityDischarge fd = dischargeService.getFacilityDischarge(dForm.getDischargeId());
				long facilityIdDischargeTo = 0;
			    if (fd.getFacilityByFacilityIdDischargeTo()!=null)
				facilityIdDischargeTo = fd.getFacilityByFacilityIdDischargeTo().getFacilityId();
			    Facility facility = facilityService.getFacilityByCwnsNbr(dForm.getCwnsNumber());
			     
				if(facilityIdDischargeTo!=facility.getFacilityId() || (fd.getPresentFlag()!=presentFlag && (dForm.getStatus().trim().equalsIgnoreCase(DischargeForm.PRESENT)||dForm.getStatus().equals(DischargeForm.PRESENT_AND_PROJECTED))))
					errors1 = validateBusinessRules(dForm,"present");	
				if(facilityIdDischargeTo!=facility.getFacilityId() || (fd.getProjectedFlag()!=projectedFlag && (dForm.getStatus().trim().equalsIgnoreCase(DischargeForm.PROJECTED)||dForm.getStatus().equals(DischargeForm.PRESENT_AND_PROJECTED)))) 
					errors2 = validateBusinessRules(dForm,"projected");
			}    
			else{
				errors1 = validateBusinessRules(dForm,"present");
			    errors2 = validateBusinessRules(dForm,"projected");
			}
			
			if ((errors1!=null && errors1.size()>0)||(errors2!=null && errors2.size()>0)){
				if (errors1!=null && errors1.size()>0)
				  saveErrors(request, errors1);
				if (errors2!=null && errors2.size()>0)
					  saveErrors(request, errors2);
				
				dForm.setDisplayDetails("Y");
				dForm.setUpdateable(facilityService.isUpdatable(currentUser, new Long(facilityId)));
				dForm.setPresentFlowUpdatable(facilityTypeService.isFacilityTypeStatusPresent(new Long(facilityId)));
				dForm.setProjectedFlowUpdatable(facilityTypeService.isFacilityTypeStatusProjected(new Long(facilityId)));
				request.setAttribute("dForm", dForm);
				request.setAttribute("dischargeMethodHelpers", dischargeService.getDischargeMethodHelpers(facilityId,currentUser.getCurrentRole().getLocationTypeId()));
				request.setAttribute("facilityDischarges", dischargeService.getFacilityDischarges(facilityId));
				
				// for warning message
				ArrayList warnMessages = new ArrayList();
				if (dischargeService.isPresentEffluentLevel(new Long(facilityId)) && dischargeService.isPresentDischargeToSurfaceWaters(new Long(facilityId)))
					warnMessages.add("warn.effluent.presentDischarge");
				if (dischargeService.isProjectedEffluentLevel(new Long(facilityId)) && dischargeService.isProjectedDischargeToSurfaceWaters(new Long(facilityId)))
					warnMessages.add("warn.effluent.projectedDischarge");
				/* replaced by fes errors
				if (!dischargeService.isFacilityHasfaciliyTypeTreatmentPlant(new Long(facilityId)) && dischargeService.isPresentDischargeToSurfaceWaters(new Long(facilityId)))
					warnMessages.add("warn.discharge.presentTreatmentPlant");
				if (!dischargeService.isFacilityHasfaciliyTypeTreatmentPlant(new Long(facilityId)) && dischargeService.isProjectedDischargeToSurfaceWaters(new Long(facilityId)))
					warnMessages.add("warn.discharge.projectedTreatmentPlant");
				*/	
				request.setAttribute("warnMessages", warnMessages);
				request.setAttribute("faciliyTypeTreatmentPlant", ((dischargeService.isFacilityHasfaciliyTypeTreatmentPlant(new Long(facilityId)))?"Y":"N"));
				return mapping.findForward("success");
			}
		 }
		
		}
		
		if (ACTION_DEFAULT.equals(action)) {
			
			dForm.setDisplayDetails("N");
			
		} else if (ACTION_ADD.equals(action)) {
			dForm.loadForAdd(facilityTypeService.getFacityType(facId), 
							 dischargeService.getFacilityDischarges(facilityId));

		} else if (ACTION_PROCESS_ADD.equals(action)) {
			//if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())){
				//performSaveandPostSaveUpdates(dForm, currentUser.getUserId(), facilityId, action);
			//}
			//else
			    dischargeService.addDischargeMethod(dForm,currentUser);
			facilityService.performPostSaveUpdates(facId, FacilityService.DATA_AREA_DISCHARGE, currentUser);
			dForm.clear();

		} else if (ACTION_EDIT.equals(action)) {
			
  	  		dForm.loadForEdit(dischargeService.getFacilityDischarge(dForm.getDischargeId()));
  	  		
			dForm.setDisplayDetails("Y");

		} else if (ACTION_PROCESS_EDIT.equals(action)) {
			//if(!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())){
				//performSaveandPostSaveUpdates(dForm, currentUser.getUserId(), facilityId, action);
			//}
			//else
				dischargeService.updateFacilityDischarge(dForm,currentUser);
			facilityService.performPostSaveUpdates(facId, FacilityService.DATA_AREA_DISCHARGE, currentUser);
			dForm.clear();
			
			
		} else if (ACTION_DELETE.equals(action)) {
			if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())){
				dischargeService.deleteDischargeMethod(dForm.getDischargeId(),currentUser);
			}
			//log.debug("DELETE dischargeId "+dForm.getDischargeId());
			else{
			FacilityDischarge fd = dischargeService.getFacilityDischarge(dForm.getDischargeId());
			// set up cost curves for rerun if applicable
			if (fd.getDischargeMethodRef().getDischargeMethodId() == DischargeService.DISCHARGE_TO_ANOTHER_FAC 
					&& fd.getProjectedFlag()=='Y'){
				ArrayList costCurveIdList = new ArrayList();
    			costCurveIdList.add(new Long(1));
    			costCurveIdList.add(new Long(2));
    			costCurveIdList.add(new Long(4));
    			costCurveIdList.add(new Long(5));
    			costCurveIdList.add(new Long(6));
    			costCurveIdList.add(new Long(10));
    			dischargeService.setUpCostCurvesForRerun(costCurveIdList, facilityId);					
			}
			dischargeService.deleteDischargeMethod(dForm.getDischargeId(),currentUser);
			}
			facilityService.performPostSaveUpdates(facId, FacilityService.DATA_AREA_DISCHARGE, currentUser);
			dForm.clear();
		}
		if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())){
			dForm.setIsLocalUser("Y");
		}
		else
			dForm.setIsLocalUser("N");
		dForm.setUpdateable(facilityService.isUpdatable(currentUser, new Long(facilityId)));
		dForm.setPresentFlowUpdatable(facilityTypeService.isFacilityTypeStatusPresent(new Long(facilityId)));
		dForm.setProjectedFlowUpdatable(facilityTypeService.isFacilityTypeStatusProjected(new Long(facilityId)));
		request.setAttribute("dForm", dForm);
		request.setAttribute("dischargeMethodHelpers", dischargeService.getDischargeMethodHelpers(facilityId,currentUser.getCurrentRole().getLocationTypeId()));
		request.setAttribute("facilityDischarges", dischargeService.getFacilityDischarges(facilityId));
		
		// for warning message
		ArrayList warnMessages = new ArrayList();
		if (!UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())){
			if (dischargeService.isPresentEffluentLevel(new Long(facilityId)) && dischargeService.isPresentDischargeToSurfaceWaters(new Long(facilityId)))
				warnMessages.add("warn.effluent.presentDischarge");
			if (dischargeService.isProjectedEffluentLevel(new Long(facilityId)) && dischargeService.isProjectedDischargeToSurfaceWaters(new Long(facilityId)))
				warnMessages.add("warn.effluent.projectedDischarge");
			/* replaced by fes error
			if (!dischargeService.isFacilityHasfaciliyTypeTreatmentPlant(new Long(facilityId)) && dischargeService.isPresentDischargeToSurfaceWaters(new Long(facilityId)))
				warnMessages.add("warn.discharge.presentTreatmentPlant");
			if (!dischargeService.isFacilityHasfaciliyTypeTreatmentPlant(new Long(facilityId)) && dischargeService.isProjectedDischargeToSurfaceWaters(new Long(facilityId)))
				warnMessages.add("warn.discharge.projectedTreatmentPlant");
			*/	
		}
		
		//request.setAttribute("presentDischargeSurfaceWater", ((dischargeService.isPresentDischargeToSurfaceWaters(new Long(facilityId)))?"Y":"N"));
		//request.setAttribute("projectedDischargeSurfaceWater", ((dischargeService.isProjectedDischargeToSurfaceWaters(new Long(facilityId)))?"Y":"N"));
		//request.setAttribute("effluentLevelId", ((dischargeService.isEffluentLevelId(new Long(facilityId)))?"Y":"N"));
       else if (UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())){
    	       if (dischargeService.isPresentEffluentLevel(new Long(facilityId)) && dischargeService.isPresentDischargeToSurfaceWaters(new Long(facilityId)))
				  warnMessages.add("warn.effluent.presentDischarge");
			   if (dischargeService.isProjectedEffluentLevel(new Long(facilityId)) && dischargeService.isProjectedDischargeToSurfaceWaters(new Long(facilityId)))
				  warnMessages.add("warn.effluent.projectedDischarge");
		}
		request.setAttribute("warnMessages", warnMessages);
		request.setAttribute("faciliyTypeTreatmentPlant", ((dischargeService.isFacilityHasfaciliyTypeTreatmentPlant(new Long(facilityId)))?"Y":"N"));
		return mapping.findForward("success");
	}

	/**
	 * Perform business rule validation. 
	 * @param pocf
	 * @return
	 */
	private ActionErrors validateBusinessRules(DischargeForm dForm, String status) {
		
		ActionErrors errors = new ActionErrors();
		
			Facility facilityToDischargeTo = 
				facilityService.getFacilityByCwnsNbr(dForm.getCwnsNumber());
			if ("present".equalsIgnoreCase(status) && facilityToDischargeTo!=null){
				if (dForm.getStatus().trim().equalsIgnoreCase(DischargeForm.PRESENT)||dForm.getStatus().equals(DischargeForm.PRESENT_AND_PROJECTED)){
					boolean found = false;
					Collection facilities = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(dForm.getFacilityId()), 
					                             new Integer(populationService.PRESENT_ONLY).intValue());
					
					Iterator iter = facilities.iterator();
					while(iter.hasNext()&&!found){
						if (facilityToDischargeTo.getFacilityId()==((Long)iter.next()).longValue())
							found = true;
					}
					if (found)
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.discharge.facilityexistsinsewershed"));
					
					boolean presentFlag = facilityTypeService.isFacilityTypeStatusPresent(new Long(facilityToDischargeTo.getFacilityId()));
					if (!presentFlag)
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.discharge.invalidpresentdischarge"));
			    }
			}else if ("projected".equalsIgnoreCase(status) && facilityToDischargeTo!=null){
				if (dForm.getStatus().equals(DischargeForm.PROJECTED)||dForm.getStatus().equals(DischargeForm.PRESENT_AND_PROJECTED)){
					boolean found = false;
					Collection facilities = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(dForm.getFacilityId()), 
					                             new Integer(populationService.PROJECTED_ONLY).intValue());
					Iterator iter = facilities.iterator();
					while(iter.hasNext()&&!found){
						if (facilityToDischargeTo!=null && facilityToDischargeTo.getFacilityId()==((Long)iter.next()).longValue())
							found = true;
					}
					if (found)
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.discharge.facilityexistsinsewershed"));
					
					boolean projectedFlag = facilityTypeService.isFacilityTypeStatusProjected(new Long(facilityToDischargeTo.getFacilityId()));
					if (!projectedFlag)
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.discharge.invalidprojecteddischarge"));
			    }
			}     
        return errors;
	}
/*
	private void performSaveandPostSaveUpdates(DischargeForm dForm, String userId, String facilityId, String action){

		char presentFlag = 'N';
		char projectedFlag = 'N';
		
		if (dForm.getStatus().equals(DischargeForm.PRESENT)){ 				
			presentFlag = 'Y';
		} else if (dForm.getStatus().equals(DischargeForm.PROJECTED)) {
			projectedFlag = 'Y';
		} else if (dForm.getStatus().equals(DischargeForm.PRESENT_AND_PROJECTED)) {
			presentFlag = 'Y';
			projectedFlag = 'Y';
		}

		ArrayList costCurveIdList1 = new ArrayList();
		costCurveIdList1.add(new Long(1));
		costCurveIdList1.add(new Long(2));
		costCurveIdList1.add(new Long(4));
		costCurveIdList1.add(new Long(5));
		costCurveIdList1.add(new Long(6));
		costCurveIdList1.add(new Long(10));
		ArrayList costCurveIdList2 = new ArrayList();
		costCurveIdList2.add(new Long(7));
		boolean setProjectedCostCurveRerun = false;
		boolean setPresentCostCurveRerun = false;
		Collection oldDownStreamFacilities = null;
		Collection oldPresentSewershed = null;
		Collection newDownStreamFacilities = null;
		Collection newPresentSewershed = null;

		if (ACTION_PROCESS_EDIT.equals(action)){
			FacilityDischarge fd = dischargeService.getFacilityDischarge(dForm.getDischargeId());
			if (dForm.getMethodId().equals(DischargeService.DISCHARGE_TO_ANOTHER_FACILITY )){
				long facilityIdDischargeTo = 0;
				if (fd.getFacilityByFacilityIdDischargeTo()!=null)
					facilityIdDischargeTo = fd.getFacilityByFacilityIdDischargeTo().getFacilityId();
				Facility facility = facilityService.getFacilityByCwnsNbr(dForm.getCwnsNumber());
				
				oldDownStreamFacilities = populationService.getDownstreamFacilitiesListByDischargeType(facilityId, new Integer(populationService.PROJECTED_ONLY).intValue());
				oldPresentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facilityId), new Integer(populationService.PRESENT_ONLY).intValue());

				if (fd.getPresentFlag()!=presentFlag
						||fd.getProjectedFlag()!=projectedFlag || facilityIdDischargeTo!=facility.getFacilityId()){
					// add cost curve rerun
					setProjectedCostCurveRerun = true;
					setPresentCostCurveRerun = true;
				}
				else if (dForm.getStatus() == dForm.PROJECTED && fd.getProjectedFlowPortionPersent() != null &&
						fd.getProjectedFlowPortionPersent().toString()!=dForm.getProjectedFlow()){
					// add cost curve rerun
					setProjectedCostCurveRerun = true;
				}
				else if (fd.getPresentFlowPortionPersent()!= null && fd.getPresentFlowPortionPersent().toString()!=dForm.getPresentFlow()){
					// add cost curve rerun
					setPresentCostCurveRerun = true;
				}
			}
			// save data
			dischargeService.updateFacilityDischarge(dForm,userId);

			if (setProjectedCostCurveRerun){
				populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(facilityId));
				newDownStreamFacilities = populationService.getDownstreamFacilitiesListByDischargeType(facilityId, new Integer(populationService.PROJECTED_ONLY).intValue());
				if (newDownStreamFacilities!=null){
					Iterator iter1 = newDownStreamFacilities.iterator();
					while (iter1.hasNext()){
						String obj = (String)iter1.next();
						populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(obj));
					}
				}
				if (oldDownStreamFacilities!=null){
					Iterator iter1 = oldDownStreamFacilities.iterator();
					while (iter1.hasNext()){
						String obj = (String)iter1.next();
						populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(obj));
					}
				}
			}
			if (setPresentCostCurveRerun){
				newPresentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facilityId), new Integer(populationService.PRESENT_ONLY).intValue());
				if (newPresentSewershed!=null){
					Iterator iter2 = newPresentSewershed.iterator();
					while (iter2.hasNext()){
						populationService.setUpCostCurvesForRerun(costCurveIdList2, (Long)iter2.next());
					}
				}
				if (oldPresentSewershed!=null){
					Iterator iter2 = oldPresentSewershed.iterator();
					while (iter2.hasNext()){
						populationService.setUpCostCurvesForRerun(costCurveIdList2, (Long)iter2.next());
					}
				}
			}
		}
		else if (ACTION_PROCESS_ADD.equals(action)){
			oldDownStreamFacilities = populationService.getDownstreamFacilitiesListByDischargeType(facilityId, new Integer(populationService.PROJECTED_ONLY).intValue());
			oldPresentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facilityId), new Integer(populationService.PRESENT_ONLY).intValue());
			newDownStreamFacilities = null;
			newPresentSewershed = null;
			if (dForm.getMethodId().equals(DischargeService.DISCHARGE_TO_ANOTHER_FACILITY )){
				if (dForm.getStatus() == dForm.PROJECTED){
					// add cost curve rerun
					setProjectedCostCurveRerun = true;
				}
				else if (dForm.getStatus() == dForm.PRESENT){
					// add cost curve rerun
					setPresentCostCurveRerun = true;
				}
				else if (dForm.getStatus() == dForm.PRESENT_AND_PROJECTED){
					// add cost curve rerun
					setPresentCostCurveRerun = true;
					setProjectedCostCurveRerun = true;
				}
			}
			dischargeService.addDischargeMethod(dForm,userId);
			if (setProjectedCostCurveRerun){
				populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(facilityId));
				newDownStreamFacilities = populationService.getDownstreamFacilitiesListByDischargeType(facilityId, new Integer(populationService.PROJECTED_ONLY).intValue());
				if (newDownStreamFacilities!=null){
					Iterator iter1 = newDownStreamFacilities.iterator();
					while (iter1.hasNext()){
						String obj = (String)iter1.next();
						populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(obj));
					}
				}
				if (oldDownStreamFacilities!=null){
					Iterator iter1 = oldDownStreamFacilities.iterator();
					while (iter1.hasNext()){
						String obj = (String)iter1.next();
						populationService.setUpCostCurvesForRerun(costCurveIdList1, new Long(obj));
					}
				}
			}
			if (setPresentCostCurveRerun){
				newPresentSewershed = populationService.getRelatedSewerShedFacilitiesByDischargeType(new Long(facilityId), new Integer(populationService.PRESENT_ONLY).intValue());
				if (newPresentSewershed!=null){
					Iterator iter2 = newPresentSewershed.iterator();
					while (iter2.hasNext()){
						populationService.setUpCostCurvesForRerun(costCurveIdList2,  (Long)iter2.next());
					}
				}
				if (oldPresentSewershed!=null){
					Iterator iter2 = oldPresentSewershed.iterator();
					while (iter2.hasNext()){
						populationService.setUpCostCurvesForRerun(costCurveIdList2,  (Long)iter2.next());
					}
				}

			}
		}		
	}
	*/
	/* set the User service */
    private static UserService userService;
    public void setUserService(UserService us){
       userService = us;    	
    }

    /* set the discharge service */
    private DischargeService dischargeService;
    public void setDischargeService(DischargeService fs){
       dischargeService = fs;    	
    }


    /* set the facility service */
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
    
    private FESManager fesManager;
    public void setFesManager(FESManager fesm){
    	fesManager=fesm;
    }
    
    private FacilityTypeService facilityTypeService;
    public void setFacilityTypeService(FacilityTypeService fts){
       facilityTypeService = fts;    	
    }
    
//  set the population service
    private PopulationService populationService;
    public void setPopulationService(PopulationService ps){
    	populationService = ps;    	
    } 
}


