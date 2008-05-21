package gov.epa.owm.mtb.cwns.facilityInformation;

import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.provider.v2.render.PortletRenderRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.model.GeographicAreaWatershed;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ReviewStatusRefService;

public class PointOfRecordDisplayAction extends CWNSAction {

	private Logger log = Logger.getLogger(this.getClass());

    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	
    	FacilityPORForm facilityPORForm = (FacilityPORForm)form;
    	PortletRenderRequest prr = (PortletRenderRequest)
	      request.getAttribute(HttpCommonConstants.PORTLET_RENDER_REQUEST);
    	String Sreviewstatustype ="";
    	String Freviewstatustype ="";
    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    	
        // 	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);
		
        //	get the facility ID from the page parameter - configured as input parameter in provider.xml
		Long facilityId = new Long(0);
		if(prr.getParameter("facilityId")!=null) facilityId = new Long(prr.getParameter("facilityId"));
		facilityPORForm.setFacilityId(facilityId);
		
        //  Get Survey Facility Object
        Facility f = facilityService.findByFacilityId(facilityId.toString());
        
        //  Get feedback copy 
		Facility feedbackVersion = (Facility)facilityService.getFeedbackVersionOfFacility(facilityId.toString());	
		 
		if (f != null && f.getReviewStatusRef() != null)
		   Sreviewstatustype = f.getReviewStatusRef().getReviewStatusId();
		if (feedbackVersion != null && feedbackVersion.getReviewStatusRef() != null)
		   Freviewstatustype = feedbackVersion.getReviewStatusRef().getReviewStatusId();
		if ((ReviewStatusRefService.STATE_ASSIGNED.equalsIgnoreCase(Sreviewstatustype) || ReviewStatusRefService.STATE_IN_PROGRESS.equalsIgnoreCase(Sreviewstatustype) || 
				ReviewStatusRefService.STATE_CORRECTION_REQUESTED.equalsIgnoreCase(Sreviewstatustype)) && (feedbackVersion != null &&
			   (ReviewStatusRefService.LOCAL_ASSIGNED.equalsIgnoreCase(Freviewstatustype) || ReviewStatusRefService.LOCAL_IN_PROGRESS.equalsIgnoreCase(Freviewstatustype)))) {
			facilityPORForm.setShowWarningMessage("Y");	
		}
		
    	AbsoluteLocationPoint absLocationPoint = facilityAddressService.getFacilityCoordinates(facilityId);
    	if (absLocationPoint != null){
    	   facilityPORForm.setLocationDescId(absLocationPoint.getLocationDescriptionRef()==null?"":absLocationPoint.getLocationDescriptionRef().getLocationDescriptionId());
    	   facilityPORForm.setLocationDesc(absLocationPoint.getLocationDescriptionRef()==null?"":absLocationPoint.getLocationDescriptionRef().getName());
    	   if (absLocationPoint.getHorizontalCllctnMethodRef()!=null){
    	      facilityPORForm.setMethodId(absLocationPoint.getHorizontalCllctnMethodRef().getHorizontalCllctnMethodId());
    	      facilityPORForm.setMethodDesc(absLocationPoint.getHorizontalCllctnMethodRef().getName());
    	   }
    	   if (absLocationPoint.getHorizontalCoordntDatumRef()!=null){
    	      facilityPORForm.setDatumId(absLocationPoint.getHorizontalCoordntDatumRef().getHorizontalCoordntDatumId());
    	      facilityPORForm.setDatumDesc(absLocationPoint.getHorizontalCoordntDatumRef().getName());
    	   }   
    	   if (absLocationPoint.getLatitudeDecimalDegree()!=null)
    	      facilityPORForm.setLatitude(absLocationPoint.getLatitudeDecimalDegree().floatValue());
    	   if (absLocationPoint.getLongitudeDecimalDegree()!=null)
    	      facilityPORForm.setLongitude(absLocationPoint.getLongitudeDecimalDegree().floatValue());
    	   if (absLocationPoint.getLatitudeDirection()!=null)
    	      facilityPORForm.setLatitudeDirec(absLocationPoint.getLatitudeDirection().charValue());
    	   if (absLocationPoint.getLongitudeDirection()!=null)
    	      facilityPORForm.setLongitudeDirec(absLocationPoint.getLongitudeDirection().charValue());
    	   facilityPORForm.setSource(absLocationPoint.getSourceCd()== 'N'?"E":absLocationPoint.getSourceCd()== 'W'?"W":"M");
    	   facilityPORForm.setSourceDesc(absLocationPoint.getSourceCd()== 'N'?"Envirofacts":absLocationPoint.getSourceCd()== 'W'?"Waters Light Viewer":"Manual");
    	   if (absLocationPoint.getMeasurementDate()!=null)
    	      facilityPORForm.setMeasureDate(df.format(absLocationPoint.getMeasurementDate()));
		   facilityPORForm.setScale(absLocationPoint.getScale());
		   if (absLocationPoint.getGeographicArea().getTribeFlag()!=null)
		      facilityPORForm.setIsInTribalTerritory(absLocationPoint.getGeographicArea().getTribeFlag().charValue());
		   if (facilityService.isUpdatable(user, facilityId)){
			    if("M".equalsIgnoreCase((new Character(absLocationPoint.getSourceCd())).toString()) ||
			    	  "W".equalsIgnoreCase((new Character(absLocationPoint.getSourceCd())).toString())){
			    	facilityPORForm.setIsUpdatable('Y');
			    }
			    facilityPORForm.setIsTriTerritoryUpdatable('Y');
			    facilityPORForm.setIsMeasureDateUpdatable('Y');
	    	}
	    }else if (facilityService.isUpdatable(user, facilityId)){
	    		facilityPORForm.setIsUpdatable('Y');
	    		facilityPORForm.setIsTriTerritoryUpdatable('Y');
	    		facilityPORForm.setIsMeasureDateUpdatable('Y');
	    }	
    	Collection locationDescs = facilityAddressService.getLocationdesc();
    	Collection methods = facilityAddressService.getMethods();
    	
    	Collection datums = null;
    	if (facilityPORForm.getDatumId()==12)
    		datums = facilityAddressService.getDatums(false);
    	else
    		datums = facilityAddressService.getDatums(true);
    	if (locationDescs != null)
    	    facilityPORForm.setLocationDescs(locationDescs);
    	if (methods != null)
    		facilityPORForm.setMethods(methods);
    	if (datums != null)
    		facilityPORForm.setDatums(datums);
    	request.setAttribute("facilityPOR", facilityPORForm);
    	
    	String remote = CWNSProperties.getProperty("ajax.remote");
    	request.setAttribute("remote", remote);
    	
    	if(isCoordinatesTypeValid(facilityId)){
    		if(absLocationPoint!=null && absLocationPoint.getCoordinateTypeCode()!=null){
    			facilityPORForm.setCoordinateTypeCode(absLocationPoint.getCoordinateTypeCode().toString());	
    		}else{
    			if(facilityService.isFacility(facilityId)){
    				facilityPORForm.setCoordinateTypeCode("S");	
    			}else{
    				facilityPORForm.setCoordinateTypeCode("P");
    			}    			
    		}
    		request.setAttribute("displayCoordinatesType", "Y");
    		//check if primary county or watershed exists
    		GeographicAreaCounty gac =facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);
    		request.setAttribute("isPrimaryCounty",(gac!=null)?"Y":"N");
    		GeographicAreaWatershed gaw = facilityAddressService.getPrimaryGeographicAreaWatershed(facilityId);
    		request.setAttribute("isPrimaryWatershed",(gaw!=null)?"Y":"N");
    	}
    	
		return mapping.findForward("success");
	}
    
    
	public boolean isCoordinatesTypeValid(Long facilityId){
		//if the facility is a non-point sources
		if(!facilityService.isFacility(facilityId)){
			return true;
		}
		//if a facility has a overall nature of owts and not a waste water
		if(!facilityService.isFacilityWasteWater(facilityId)){
			if(facilityService.isFacilityDecentralized(facilityId)||facilityService.isFacilityStormwater(facilityId)){
				return true;	
			}
		}
		return false;
	}
    
    //  set the facility address service
    private FacilityAddressService facilityAddressService;
    
    //  set the facility service
    private FacilityService facilityService;
    
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }  

	public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}	
}
