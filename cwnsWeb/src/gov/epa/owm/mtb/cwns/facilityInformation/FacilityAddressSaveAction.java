package gov.epa.owm.mtb.cwns.facilityInformation;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.FacilityAddress;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class FacilityAddressSaveAction extends CWNSAction {

	private Logger log = Logger.getLogger(this.getClass());

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	FacilityAddressForm facilityAddressForm = (FacilityAddressForm)form;
    	
        //	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);    
        /*
		// If all the fields are deleted, delete the Facility Address record if it exists in the database.  
    	if ("".equalsIgnoreCase(facilityAddressForm.getAddress1())&&"".equalsIgnoreCase(facilityAddressForm.getAddress2())&&
    			"".equalsIgnoreCase(facilityAddressForm.getCity())&&"".equalsIgnoreCase(facilityAddressForm.getState())&&   
    			  "".equalsIgnoreCase(facilityAddressForm.getZipCode())){
    		
			facilityAddressService.deleteFacilityAddress(facilityAddressForm.getFacilityId()); 
    	}*/
    			
    	FacilityAddress facAddress = facilityAddressService.getFacilityAddress(facilityAddressForm.getFacilityId());
		if (facAddress == null){
			FacilityAddress object = new FacilityAddress();
			object.setFacilityId((facilityAddressForm.getFacilityId()).longValue());
			object.setStreetAddress1(facilityAddressForm.getAddress1());
			object.setStreetAddress2(facilityAddressForm.getAddress2());
			object.setCity(facilityAddressForm.getCity());
			object.setStateId(facilityAddressForm.getState());
			object.setZip(facilityAddressForm.getZipCode());
			object.setSourcedFromNpdesFlag('N');
			object.setLastUpdateTs(new Date());
			object.setLastUpdateUserid(user.getUserId());
			facilityAddressService.saveFacilityAddress(object,facilityAddressForm.getFacilityId(),user);
		}
		else{
			if (facAddress.getSourcedFromNpdesFlag()== 'N'){
				facAddress.setStreetAddress1(facilityAddressForm.getAddress1());
				facAddress.setStreetAddress2(facilityAddressForm.getAddress2());
				facAddress.setCity(facilityAddressForm.getCity());
				facAddress.setStateId(facilityAddressForm.getState());
				facAddress.setZip(facilityAddressForm.getZipCode());
				facAddress.setLastUpdateTs(new Date());
				facAddress.setLastUpdateUserid(user.getUserId());
				facilityAddressService.saveFacilityAddress(facAddress,facilityAddressForm.getFacilityId(),user);
			}
			/*
			else{
				   if (!facilityAddressForm.getAddress1().equalsIgnoreCase(nullToEmpty(facAddress.getStreetAddress1())) ||
					  !facilityAddressForm.getCity().equalsIgnoreCase(nullToEmpty(facAddress.getCity())) ||
					    !facilityAddressForm.getState().equalsIgnoreCase(nullToEmpty(facAddress.getStateId())) ||
					      !facilityAddressForm.getZipCode().equalsIgnoreCase(nullToEmpty(facAddress.getZip()))) {
					facAddress.setStreetAddress1(facilityAddressForm.getAddress1());
					facAddress.setStreetAddress2(facilityAddressForm.getAddress2());
					facAddress.setCity(facilityAddressForm.getCity());
					facAddress.setStateId(facilityAddressForm.getState());
					facAddress.setZip(facilityAddressForm.getZipCode());
					facAddress.setSourcedFromNpdesFlag('N');
					facAddress.setLastUpdateTs(new Date());
					facAddress.setLastUpdateUserid(user.getUserId());
					facilityAddressService.saveFacilityAddress(facAddress,facilityAddressForm.getFacilityId(),user);
				}
			}*/
		}	
		
		//fesManager.runValidation(facilityAddressForm.getFacilityId(), FacilityService.DATA_AREA_GEOGRAPHIC, user.getUserId());
		facilityService.performPostSaveUpdates(facilityAddressForm.getFacilityId(), FacilityService.DATA_AREA_GEOGRAPHIC, user);
		return mapping.findForward("success");
	}
    
    
    
    private String nullToEmpty(Object obj){
    	if (obj == null)
    	   return "";
    	else
    	   return (String)obj;	
    }
    
    //  set the facility address service
    private FacilityAddressService facilityAddressService;

	public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}
	
    //  set the facility service
    private FacilityService facilityService;
    
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    } 

}
