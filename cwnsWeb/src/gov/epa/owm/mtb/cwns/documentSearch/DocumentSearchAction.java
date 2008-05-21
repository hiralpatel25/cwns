package gov.epa.owm.mtb.cwns.documentSearch;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.needs.NeedsHelper;
import gov.epa.owm.mtb.cwns.service.DocumentService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.NeedsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DocumentSearchAction extends CWNSAction {		
	
	
	private Logger log = Logger.getLogger(this.getClass());

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	
    	
    	PortletRenderRequest prr = (PortletRenderRequest)
    		      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST); 
   		    
    	DocumentSearchForm docForm = (DocumentSearchForm)form;
    	    
        //Get user object
   		HttpServletRequest httpReq = (HttpServletRequest) request;
   		HttpSession httpSess = httpReq.getSession();
   		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
   		
 		//get the facility ID from the page parameter - configured as input parameter in provider.xml
 		String facNum = prr.getParameter("facilityId");
 		String facilityLocation = "";
 		Collection documentGroupTypeList = needsService.getDocumentGroupTypeInfo(facNum, "-9999");
 		request.setAttribute("documentGroupTypeList", documentGroupTypeList);
 		if(facNum != null && facNum.length() > 0)
		   {
	 		  log.debug("facility number---"+facNum);	
	 		  docForm.setNeedsFacilityId(facNum);
	 		  Facility f = facilityService.findByFacilityId(facNum);
	 		  facilityLocation = f.getLocationId();
		   }
		   else
		   {
			  facNum = docForm.getNeedsFacilityId();
		   }
 		
 		//get search results
 		Collection searchResults = new ArrayList();
 		Integer countDocTotal = new Integer(0);
 		request.setAttribute("countDocTotal", new Integer(-1));
 		if(docForm.getDocSearchAct().equals("submit") || docForm.getDocSearchAct().equals("add")){
 			
 			//if(isTokenValid(request, true)){ 			
 			if( docForm.getDocSearchAct().equals("add")){ 				
 				try {
 					boolean result = needsService.associateDocumentWithFacility(facNum, docForm.getDocumentIds(), user);
 					 					
 					if(!result){/*display message on web page that docs not associated*/}
 					else{
 						//if one of the documents assigned is of type 98:
 						//1. reload the doctype popup, without docType 98
 						String[] docIds = docForm.getDocumentIds();
 						ArrayList documentIdList = null;
 						if (docIds!=null){
 							documentIdList = new ArrayList();
 							for(int i=0; i<docIds.length; i++){
 								documentIdList.add(new Long(docIds[i]));
 							}
 							
 	 						//if one of the documents assigned is of type 98: 							
 	 						if (needsService.containsCSOCostCurveDocType(documentIdList)){
 	 							//1. reload the doctype popup, without docType 98
 	 							documentGroupTypeList = needsService.getDocumentGroupTypeInfo(facNum, "-9999");
 	 							request.setAttribute("documentGroupTypeList", documentGroupTypeList);
 	 							docForm.setDocType(""); 							
 	 						} 							
 						}
 					} 					 					
 				} catch (gov.epa.owm.mtb.cwns.common.exceptions.CWNSRuntimeException e) {
 					//TODO: add error message to display on web page
 					e.printStackTrace();
 				}
 			}
 	 				
 			countDocTotal = new Integer(needsService.countDocumentsByKeywords(docForm, facilityLocation));
 			
 				log.debug("DocType - " + docForm.getDocType());
 				log.debug("DateFrom - " + docForm.getDateFrom());
 				log.debug("DateTo - " + docForm.getDateTo());
 				log.debug("Keywords - " + docForm.getKeywords());
 				
 				String [] here = docForm.getDocumentIds();
 				
 				log.debug("DocumentIds - " + here.toString());
 	 			
 	 			for(int i = 0; i < here.length; ++i){
 	 				log.debug("Selected id = " + here[i]);
 	 			}
 	    	
 				if( countDocTotal != null && countDocTotal.intValue() > 0){
 					searchResults = needsService.findDocumentsByKeywords(docForm, facilityLocation, Integer.parseInt(docForm.getFirstResult()) - 1, Integer.parseInt(docForm.getMaxResult())); 					
 				}	
 	    	
 				resetToken(request);
	 			docForm.setDocSearchAct("");
	 	 		request.setAttribute("countDocTotal", countDocTotal);
 			//}
 			//else{
 			//	saveToken(request);
 			//}
 			
 		}
 		

 		
 		if( docForm.getDocSearchAct().equals("add")){
 			
 			String [] here = docForm.getDocumentIds();
 			
 			for(int i = 0; i < here.length; ++i){
 				log.debug("Selected id = " + here[i]);
 			} 			
 		}
 		
 		
 		if (facilityService.isUpdatable(user, new Long(facNum))){
 			docForm.setIsUpdatable("Y");
    	}else{
    		docForm.setIsUpdatable("N");
    	} 		
 		
 		request.setAttribute("docForm", docForm);
 		request.setAttribute("searchResults", searchResults);
 		
 		//if the facility already has a type 98 doc, do not display check box for type 98 docs
 		request.setAttribute("hasCSOCostCurveNeeds", "N");
 		if(facNum != null && facNum.length() > 0){
 			if (needsService.facilityHasDocumentType(new Long(facNum), DocumentService.DOCUMENT_TYPE_CSO_COST_CURVE_NEEDS)){
 				request.setAttribute("hasCSOCostCurveNeeds", "Y");
 			}
 		}
 		 		 		
 		//saveToken(request);
 		
 		//ActionForward af = new ActionForward(docForm.getForwardUrl(), true);
 	    
 	    //return af;
 		
			String key ="help."+user.getCurrentRole().getLocationTypeId()+".documentsearch";
		    String defaultkey = "help.documentsearch"; 
		    String helpKey = CWNSProperties.getKey(key, defaultkey); 	
		request.setAttribute("helpKey", helpKey);
 		
        return mapping.findForward("success");
  }
    
    private String[] getDocumentIdList(Collection searchResults) {
		//Set documentIds = new HashSet();
		String [] documentIds = new String[searchResults.size()];		
		Iterator iter = searchResults.iterator();
		for (int i = 0; iter.hasNext(); ++i) {
			NeedsHelper document = (NeedsHelper) iter.next();
			documentIds[i] = "" + document.getDocumentId();		
		}
		return documentIds;
	}    
    
	private NeedsService needsService; 
    //  set the needs service
    public void setNeedsService(NeedsService ps){
       needsService = ps;    	
    }
    
    private FacilityService facilityService;    
    //  set the facility service
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }

}
