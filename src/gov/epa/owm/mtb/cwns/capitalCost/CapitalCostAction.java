package gov.epa.owm.mtb.cwns.capitalCost;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.service.CapitalCostService;
import gov.epa.owm.mtb.cwns.service.DocumentService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.NeedsService;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CapitalCostAction extends CWNSAction {

	public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

    	CapitalCostForm capitalCostForm = (CapitalCostForm) form;
    	
		log.debug(" IN - capitalCostForm--> " + capitalCostForm.toString());

        //Get user privilages
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		UserRole userRole = user.getCurrentRole();
		
		//this should be commented out during deployment
		//userRole.setType(UserRole.LOCAL);
		
		String userType = userRole.getLocationTypeId();

	    PortletRenderRequest prr = (PortletRenderRequest)
		      req.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
		
		    /* determine what needs to be done */
		String action = (capitalCostForm.getCapitalCostAct() != null) ? 
				capitalCostForm.getCapitalCostAct() : "None";

		log.debug("Action: " + action);
        
		//This facilityId comes from portlet parameter
		long facId = -1;
		if(req.getParameter("facilityId")!=null)
			facId = Long.parseLong(req.getParameter("facilityId"));

		log.debug("req.getParameter(facilityId): " + facId);

		// if facId is available in the form, use it instead
		if(capitalCostForm.getCapitalCostFacilityId() != CapitalCostForm.INITIAL_LONG_VALUE)
		{
			//get Parameter from the form instead
			facId = capitalCostForm.getCapitalCostFacilityId();			
		}
		
		capitalCostForm.setCapitalCostFacilityId(facId);

		ActionForward actionFwd = mapping.findForward("success");
	    
		ArrayList categoryClassificationList = null;

		ArrayList costList = new ArrayList();
		
		String docId = "";
		
		if(capitalCostForm.getDocumentId()<=0)
			docId = (String)httpSess.getAttribute(facId + "_ActiveDocId");
		else
			docId = new Long(capitalCostForm.getDocumentId()).toString();
		
		log.debug("docId: " + docId);
		
		if(docId != null && !docId.equals(""))
		{
			capitalCostForm.setDocumentId(new Long(docId).longValue());
		}
	
		if (action.equals("save"))
		{	
			if(isTokenValid(req, true))
			{
				log.debug("save cost for docId: " + docId);				
				capitalCostService.saveOrUpdateCost(capitalCostForm, 
		                user);				
			   resetToken(req);
			}		
			
			capitalCostForm.setIsAddAction("N");	
			capitalCostForm.setCapitalCostId(-999);
		}
		else if(action.equals("edit"))
		{
			Cost costEdit = capitalCostService.prepareFacilityDocument(capitalCostForm.getCapitalCostId());
			if(costEdit!=null)
			{
				capitalCostForm.setAdjustedAmount(costEdit.getAdjustedAmount());
				capitalCostForm.setBaseAmount(costEdit.getBaseAmount());
				capitalCostForm.setClassification(costEdit.getClassificationRef()==null?"":costEdit.getClassificationRef().getClassificationId());
				capitalCostForm.setCostCategory(costEdit.getCategoryRef()==null?"":costEdit.getCategoryRef().getCategoryId());
				capitalCostForm.setCostCategoryName(costEdit.getCategoryRef()==null?"":(costEdit.getCategoryRef()).getName());				
				capitalCostForm.setCostMethodCode(new Character(costEdit.getCostMethodCode()).toString());
				capitalCostForm.setCostNeedType(costEdit.getNeedTypeRef()==null?"":costEdit.getNeedTypeRef().getNeedTypeId());

				capitalCostForm.setSrfEligible(costEdit.getSrfEligiblePercentage()==null?"":costEdit.getSrfEligiblePercentage().toString());
				capitalCostForm.setSso(costEdit.getSsoFlag()==null?"":costEdit.getSsoFlag().toString());				
				capitalCostForm.setCategoryValidForSSO(costEdit.getCategoryRef().getValidForSsoFlag()+"");
				
				if(capitalCostForm.getCategoryValidForSSO().equals("N"))
				{
					capitalCostForm.setSso("");
				}

				Collection classificationList  = capitalCostService.getClassificationList(capitalCostForm.getCostCategory());				
				capitalCostForm.setCategoryNoClassification((classificationList==null||classificationList.size()<1)?"Y":"N");
				capitalCostForm.setDetailEditExpand("Y");
				capitalCostForm.setIsAddAction("N");				
			}					
		}
		else if(action.equals("delete") || action.equals("mark delete"))
		{
			capitalCostService.deleteCost(capitalCostForm.getCapitalCostFacilityId(), capitalCostForm.getCapitalCostId(), user);
			capitalCostForm.initForm();
			capitalCostForm.setIsAddAction("N");	
		}		
		else if(action.equals("new"))
		{
			capitalCostForm.initForm();			
			capitalCostForm.setDetailEditExpand("Y");
			capitalCostForm.setIsAddAction("Y");			
		}

		if(action.equals("new") || action.equals("edit"))
		{
			categoryClassificationList = (ArrayList)capitalCostService.getCategoryClassificationList(facId, new Long(docId).longValue(), user.isNonLocalUser());	
			capitalCostForm.setMonthlyBaseCostIndexAmt(capitalCostService.getMonthlyCostIndexAmt(0, 'Y'));
			capitalCostForm.setMonthlyCostIndexAmt(capitalCostService.getMonthlyCostIndexAmt(capitalCostForm.getDocumentId(), 'N'));
		}
		
		saveToken(req);			
		
		if(docId != null && !docId.equals(""))
		{
			capitalCostForm.setCostIndexBaseDate(capitalCostService.getMonthlyCostIndexDate(capitalCostForm.getDocumentId()));
			
			Document docObj = capitalCostService.getDocument(new Long(docId).longValue());

			//if(docObj!=null && docObj.getDocumentTypeRef()!=null && docObj.getDocumentTypeRef().getNeedTypeRef()!=null)
			//{
			//	capitalCostForm.setDocumentTypeNeedType(docObj.getDocumentTypeRef().getNeedTypeRef().getNeedTypeId());
			//	capitalCostForm.setCostNeedType(docObj.getDocumentTypeRef().getNeedTypeRef().getNeedTypeId());
			//	capitalCostForm.setValidForCost(docObj.getDocumentTypeRef().getAprovedForCostsFlag()+"");
			//}
			
			costList = (ArrayList)capitalCostService.getCostList(facId, Long.parseLong(docId));
			
			log.debug("costList: " + (costList==null?0:costList.size()));
		}

		capitalCostForm.setPrivateAllowFederal(capitalCostService.getFacilityPrivateAllowFederal(facId));		
		
		if(capitalCostForm.getPrivateAllowFederal().equals("N"))
		{
			capitalCostForm.setCostNeedType("S");
		}		
		
        // Check if facility is updatable or not and set form attribute
 	     if (facilityService.isUpdatable(user, new Long(facId))){
 	    	capitalCostForm.setIsUpdatable("Y");
 		  }else{
 	    	capitalCostForm.setIsUpdatable("N");
 	     }
 	     
		if (docId!=null && docId.length()>0 && capitalCostService.canAddNewCostToDocument(new Long(docId))){
			capitalCostForm.setValidForCost("Y");
		}else{
			capitalCostForm.setValidForCost("N");
		}
		
		if (facId!=-1 && capitalCostService.facilityHasFacilityTypesAssigned(new Long(facId))){
			req.setAttribute("canNotAddCost", "N");
		}else{
			req.setAttribute("canNotAddCost", "Y");
		}
		
		if (facId!=-1 && capitalCostService.assignedTypesAreAllNoChange(new Long(facId))){
			req.setAttribute("allTypesNoChange", "Y");
		}else{
			req.setAttribute("allTypesNoChange", "N");
		}
 	     	    
 	    req.setAttribute("categoryClassificationList", categoryClassificationList);
 	    req.setAttribute("capitalCostList", costList);
		req.setAttribute("capitalCostForm", capitalCostForm);		
		
		//add warning messages
		Collection warningMsgList = getWarningMessages((new Long(facId)).toString());
		if (warningMsgList!=null && warningMsgList.size()>0) 
		req.setAttribute("warnings", warningMsgList);
		
    	String remote = CWNSProperties.getProperty("ajax.remote");
    	req.setAttribute("remote", remote);
    	req.setAttribute("stateUser", user.isNonLocalUser()?"true":"false");    	
		return actionFwd;
	}
	
	//checks for duplicate CIP, IUP, Facility Plan
	private Collection getWarningMessages(String strFacilityId){
		ArrayList lstCapitalCostWarnings = new ArrayList();
		boolean duplicateCIP = needsService.existDuplicateCostCategory(strFacilityId, DocumentService.DOCUMENT_TYPE_CAPITAL_IMPROVEMENT_PLAN);
		boolean duplicateIUP = needsService.existDuplicateCostCategory(strFacilityId, DocumentService.DOCUMENT_TYPE_INTENDED_USE_PLAN);
		boolean duplicateFacilityPlan = needsService.existDuplicateCostCategory(strFacilityId, DocumentService.DOCUMENT_TYPE_FACILITY_PLAN);
		
		if (duplicateCIP) lstCapitalCostWarnings.add("warn.needs.capitalimprovementplan");		
		if (duplicateIUP) lstCapitalCostWarnings.add("warn.needs.intendeduseplan");
		if (duplicateFacilityPlan) lstCapitalCostWarnings.add("warn.needs.facilityplan");
		
		if (lstCapitalCostWarnings.size()<=0) lstCapitalCostWarnings = null;
		return lstCapitalCostWarnings;
	}

	private CapitalCostService capitalCostService;
	public void setCapitalCostService(CapitalCostService ss) {
		capitalCostService = ss;
	}
	
    private FacilityService facilityService;    
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
    
    private NeedsService needsService;
	public void setNeedsService(NeedsService needsService) {
		this.needsService = needsService;
	}
    
}
