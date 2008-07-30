package gov.epa.owm.mtb.cwns.facilityInformation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorActionForm;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityAddress;
import gov.epa.owm.mtb.cwns.model.GeographicArea;
import gov.epa.owm.mtb.cwns.model.GeographicAreaTypeRef;
import gov.epa.owm.mtb.cwns.model.HorizontalCllctnMethodRef;
import gov.epa.owm.mtb.cwns.model.HorizontalCoordntDatumRef;
import gov.epa.owm.mtb.cwns.model.LocationDescriptionRef;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

public class PointOfRecordSaveAction extends CWNSAction {

	private Logger log = Logger.getLogger(this.getClass());
	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    public ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	
        FacilityPORForm facilityPORForm = (FacilityPORForm)form;
        
        String sessionIdAndSave = facilityPORForm.getSessionId();
        
        String sessionId=null;
        String saveConfirm="Y";
        String [] strArr=sessionIdAndSave.split(":");
        if(strArr.length>0){
            sessionId=strArr[0];
            if(strArr.length>1){
                saveConfirm=strArr[1];
            }
        }
        if (isCancelled(request)){
        	  if(sessionId!=null && !"".equals(sessionId)){
      			facilityAddressService.invalidateWebRITSession(sessionId); 
        	  } 
			  return mapping.findForward("success");
		}
        
        //	Get user object
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpSession httpSess = httpReq.getSession();
		CurrentUser user = (CurrentUser) httpSess.getAttribute(CurrentUser.CWNS_USER);    
		
		if ('N' == facilityPORForm.getIsUpdatable()){
			facilityAddressService.saveGeographicArea(facilityPORForm.getFacilityId(),facilityPORForm.getIsInTribalTerritory(), user);
			//facilityAddressService.updateMeasureDate(facilityPORForm.getFacilityId(),facilityPORForm.getMeasureDate(),user);
			return mapping.findForward("success");
		}
		
		//ActionErrors errors = facilityPORForm.validate(mapping, request);	
		Collection a = facilityAddressService.getPORByLatitudeAndLongitude(facilityPORForm.getFacilityId(),facilityPORForm.getLatitude(),
				facilityPORForm.getLongitude());
		/*
		log.debug("POR size---"+a.size());
    	if (!errors.isEmpty()){
    		saveErrors(request,errors);
    			
    		Collection locationDescs = facilityAddressService.getLocationdesc();
        	Collection methods = facilityAddressService.getMethods();
        	Collection datums = facilityAddressService.getDatums();
        	if (locationDescs != null)
        	    facilityPORForm.setLocationDescs(locationDescs);
        	if (methods != null)
        		facilityPORForm.setMethods(methods);
        	if (datums != null)
        		facilityPORForm.setDatums(datums);
    		request.setAttribute("facilityPOR", facilityPORForm);
    		return new ActionForward(mapping.getInput());
    	}*/
		    	
    	// If all the fields are deleted, delete the AbsoluteLocationPoint record if it exists in the database.  
    	if (facilityPORForm.getDatumId()== 0 && facilityPORForm.getMethodId() == 0 && "".equals(facilityPORForm.getLocationDescId()) &&
    		 facilityPORForm.getLatitude() == 0 && facilityPORForm.getLongitude() == 0 && "".equals(facilityPORForm.getMeasureDate())
    		 && ("".equals(facilityPORForm.getCoordinateTypeCode()) || "S".equals(facilityPORForm.getCoordinateTypeCode())|| "P".equals(facilityPORForm.getCoordinateTypeCode()))){
			facilityAddressService.deleteFacilityCoordinates(facilityPORForm.getFacilityId(),facilityPORForm.getIsInTribalTerritory(),user);
    	}
    	else {
    		if (a != null && a.size() > 0){
  			  facilityPORForm.setConfirm('Y');
  		    } 	
    	   AbsoluteLocationPoint absoluteLocationPoint = facilityAddressService.getFacilityCoordinates(facilityPORForm.getFacilityId());
		   if (absoluteLocationPoint == null){
			  AbsoluteLocationPoint object = new AbsoluteLocationPoint();
			  if("C".equals(facilityPORForm.getCoordinateTypeCode()) || "W".equals(facilityPORForm.getCoordinateTypeCode())){
					//set county or watershed centroid based on the coordinate type
					setAbsoluteLocationBasedOncentroid(object, facilityPORForm);
					//remove exisitng points if any
					facilityAddressService.removeFeatures(facilityPORForm.getFacilityId());
			  }else{
				  LocationDescriptionRef locDescRef = new LocationDescriptionRef();
				  locDescRef.setLocationDescriptionId(facilityPORForm.getLocationDescId());
				  object.setLocationDescriptionRef(locDescRef);
				  HorizontalCoordntDatumRef horizontalCoordntDatumRef = new HorizontalCoordntDatumRef();
				  horizontalCoordntDatumRef.setHorizontalCoordntDatumId(facilityPORForm.getDatumId());
				  object.setHorizontalCoordntDatumRef(horizontalCoordntDatumRef);
				  HorizontalCllctnMethodRef horizontalCllctnMethodRef = new HorizontalCllctnMethodRef();
				  horizontalCllctnMethodRef.setHorizontalCllctnMethodId(facilityPORForm.getMethodId());
				  object.setHorizontalCllctnMethodRef(horizontalCllctnMethodRef);
				  object.setLatitudeDecimalDegree(new BigDecimal(facilityPORForm.getLatitude()));
				  object.setLongitudeDecimalDegree(new BigDecimal(facilityPORForm.getLongitude()));
				  object.setScale(facilityPORForm.getScale());
				  object.setLatitudeDirection(new Character(facilityPORForm.getLatitudeDirec()));
				  object.setLongitudeDirection(new Character(facilityPORForm.getLongitudeDirec()));
				  object.setSourceCd(facilityPORForm.getSource().charAt(0));
				  object.setMeasurementDate(new Date(facilityPORForm.getMeasureDate()));
				  if(facilityPORForm.getCoordinateTypeCode()!=null && !"".equals(facilityPORForm.getCoordinateTypeCode())){
					  object.setCoordinateTypeCode(new Character(facilityPORForm.getCoordinateTypeCode().charAt(0)));
				  }
			  }
			  object.setLastUpdateTs(new Date());
			  object.setLastUpdateUserid(user.getUserId());
			  facilityAddressService.saveFacilityCoordinates(object,facilityPORForm.getFacilityId(),facilityPORForm.getIsInTribalTerritory(),user);
			  absoluteLocationPoint=object;
		   }else{
				if("C".equals(facilityPORForm.getCoordinateTypeCode()) || "W".equals(facilityPORForm.getCoordinateTypeCode())){
					//set county or watershed centroid based on the coordinate type
					setAbsoluteLocationBasedOncentroid(absoluteLocationPoint, facilityPORForm);
				}else{
					if (absoluteLocationPoint.getSourceCd()== 'W'&& "M".equalsIgnoreCase(facilityPORForm.getSource()) && !isDataIdentical(absoluteLocationPoint,facilityPORForm)){
						  absoluteLocationPoint.setSourceCd('M');
					}else if (absoluteLocationPoint.getSourceCd()== 'M'){
						absoluteLocationPoint.setSourceCd(facilityPORForm.getSource().charAt(0));
					}
					LocationDescriptionRef locDescRef = new LocationDescriptionRef();
					locDescRef.setLocationDescriptionId(facilityPORForm.getLocationDescId());
					absoluteLocationPoint.setLocationDescriptionRef(locDescRef);
					HorizontalCoordntDatumRef horizontalCoordntDatumRef = new HorizontalCoordntDatumRef();
					horizontalCoordntDatumRef.setHorizontalCoordntDatumId(facilityPORForm.getDatumId());
					absoluteLocationPoint.setHorizontalCoordntDatumRef(horizontalCoordntDatumRef);
					HorizontalCllctnMethodRef horizontalCllctnMethodRef = new HorizontalCllctnMethodRef();
					horizontalCllctnMethodRef.setHorizontalCllctnMethodId(facilityPORForm.getMethodId());
					absoluteLocationPoint.setHorizontalCllctnMethodRef(horizontalCllctnMethodRef);
					absoluteLocationPoint.setLatitudeDecimalDegree(new BigDecimal(facilityPORForm.getLatitude()));
					absoluteLocationPoint.setLongitudeDecimalDegree(new BigDecimal(facilityPORForm.getLongitude()));
					absoluteLocationPoint.setLatitudeDirection(new Character(facilityPORForm.getLatitudeDirec()));
					absoluteLocationPoint.setLongitudeDirection(new Character(facilityPORForm.getLongitudeDirec()));
					absoluteLocationPoint.setMeasurementDate(new Date(facilityPORForm.getMeasureDate()));
					absoluteLocationPoint.setScale(facilityPORForm.getScale());														
					if(facilityPORForm.getCoordinateTypeCode()!=null && !"".equals(facilityPORForm.getCoordinateTypeCode())){
						absoluteLocationPoint.setCoordinateTypeCode(new Character(facilityPORForm.getCoordinateTypeCode().charAt(0)));
					}
				}
				absoluteLocationPoint.setLastUpdateTs(new Date());
				absoluteLocationPoint.setLastUpdateUserid(user.getUserId());
				facilityAddressService.saveFacilityCoordinates(absoluteLocationPoint,facilityPORForm.getFacilityId(),facilityPORForm.getIsInTribalTerritory(),user);
			}
		    if(absoluteLocationPoint.getSourceCd()=='M'){
		    	if(absoluteLocationPoint.getLatitudeDecimalDegree()!=null && absoluteLocationPoint.getLongitudeDecimalDegree()!=null){
					String latitude = (absoluteLocationPoint.getLatitudeDirection().charValue()=='N'?"+":"-")+absoluteLocationPoint.getLatitudeDecimalDegree().floatValue();
					String longitude = (absoluteLocationPoint.getLongitudeDirection().charValue()=='E'?"+":"-")+absoluteLocationPoint.getLongitudeDecimalDegree().floatValue();
					//remove exisitng points if any
					facilityAddressService.removeFeatures(facilityPORForm.getFacilityId());
					facilityAddressService.saveWebRITSession(latitude, longitude, facilityPORForm.getFacilityId());		    		
		    	}
		    }
		  }
    	
    	if(sessionId!=null && !"".equals(sessionId)){
        	//if the feature has already been saved delete before you save
        	// save data to pet and invalidate session
        	log.debug("Saving to Pet with SessionId " + sessionId + "  Confirm Flag: "+ saveConfirm);
        	if("Y".equals(saveConfirm)){
            	facilityAddressService.saveWebRITSession(sessionId, facilityPORForm.getFacilityId());
        	}
        	//invalidate session
    		facilityAddressService.invalidateWebRITSession(sessionId);    		
    	}
    	//fesManager.runValidation(facilityPORForm.getFacilityId(), FacilityService.DATA_AREA_GEOGRAPHIC, user.getUserId());
    	facilityService.performPostSaveUpdates(facilityPORForm.getFacilityId(), FacilityService.DATA_AREA_GEOGRAPHIC, user);
    	return mapping.findForward("success");
	}
    
    private void setAbsoluteLocationBasedOncentroid(AbsoluteLocationPoint absoluteLocationPoint, FacilityPORForm facilityPORForm){
		AbsoluteLocationPoint centroidlocPoint=null;
		if("C".equals(facilityPORForm.getCoordinateTypeCode())){
			//get primary county center coordinates
			centroidlocPoint = facilityAddressService.getPrimaryCountyCentroid(facilityPORForm.getFacilityId());
		}else if("W".equals(facilityPORForm.getCoordinateTypeCode())){
			//get primary watershed center coordinates
			centroidlocPoint =facilityAddressService.getPrimaryWatershedCentroid(facilityPORForm.getFacilityId());
		}
		if(centroidlocPoint!=null){
			absoluteLocationPoint.setLocationDescriptionRef(centroidlocPoint.getLocationDescriptionRef());
			absoluteLocationPoint.setHorizontalCoordntDatumRef(centroidlocPoint.getHorizontalCoordntDatumRef());
			absoluteLocationPoint.setHorizontalCllctnMethodRef(centroidlocPoint.getHorizontalCllctnMethodRef());
			absoluteLocationPoint.setLatitudeDecimalDegree(centroidlocPoint.getLatitudeDecimalDegree());
			absoluteLocationPoint.setLongitudeDecimalDegree(centroidlocPoint.getLongitudeDecimalDegree());
			absoluteLocationPoint.setLatitudeDirection(centroidlocPoint.getLatitudeDirection());
			absoluteLocationPoint.setLongitudeDirection(centroidlocPoint.getLongitudeDirection());
			absoluteLocationPoint.setMeasurementDate(centroidlocPoint.getMeasurementDate());
			absoluteLocationPoint.setScale(centroidlocPoint.getScale());
			absoluteLocationPoint.setSourceCd('W');
		}else{
			absoluteLocationPoint.setLocationDescriptionRef(null);
			absoluteLocationPoint.setHorizontalCoordntDatumRef(null);
			absoluteLocationPoint.setHorizontalCllctnMethodRef(null);
			absoluteLocationPoint.setLatitudeDecimalDegree(null);
			absoluteLocationPoint.setLongitudeDecimalDegree(null);
			absoluteLocationPoint.setLatitudeDirection(null);
			absoluteLocationPoint.setLongitudeDirection(null);
			absoluteLocationPoint.setMeasurementDate(null);
			absoluteLocationPoint.setScale(null);
			absoluteLocationPoint.setSourceCd('M');			
		}
		absoluteLocationPoint.setCoordinateTypeCode(new Character(facilityPORForm.getCoordinateTypeCode().charAt(0)));
    }
        
    private boolean isDataIdentical(AbsoluteLocationPoint absoluteLocationPoint,FacilityPORForm facilityPORForm){
    	boolean isIdentical = true;
    	if((facilityPORForm.getDatumId() != absoluteLocationPoint.getHorizontalCoordntDatumRef().getHorizontalCoordntDatumId())
    		|| (facilityPORForm.getMethodId() != absoluteLocationPoint.getHorizontalCllctnMethodRef().getHorizontalCllctnMethodId())
    		|| (facilityPORForm.getLatitude() != absoluteLocationPoint.getLatitudeDecimalDegree().floatValue())
    		|| (facilityPORForm.getLatitudeDirec() != absoluteLocationPoint.getLatitudeDirection().charValue())
    		|| (facilityPORForm.getLongitude() != absoluteLocationPoint.getLongitudeDecimalDegree().floatValue())
    		|| (facilityPORForm.getLongitudeDirec() != absoluteLocationPoint.getLongitudeDirection().charValue())
    		|| (absoluteLocationPoint.getCoordinateTypeCode()!=null && facilityPORForm.getCoordinateTypeCode().equals(absoluteLocationPoint.getCoordinateTypeCode().toString()))){
    	  isIdentical = false;
    	}
      
		return isIdentical;	
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
