package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.FacilityAddress;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityCommentsService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GeographicValidator extends FESValidator {
	
	public GeographicValidator() {
		super(FacilityService.DATA_AREA_GEOGRAPHIC);
	}
	
	public boolean isRequired(Long facilityId){
		// Is always required
		return true;
	}
	
	public boolean isEntered(Long facilityId){
		// check if a Geographic area exist with type por
		Object o = facilityAddressService.getFacilityCoordinates(facilityId);
		if(o==null){
			return false;	
		}
		return true;
	}
	
	public boolean isErrors(Long facilityId, Collection errors){
		boolean isErr = false;
		//if facility Present /projected
		if(facilityTypeService.isFacilityTypeStatusPresent(facilityId)){
			
			Object o = facilityAddressService.getFacilityCoordinates(facilityId);
			if(o==null){
				errors.add("error.geographic.coordinates_required");
				isErr = true;
			}else{
				AbsoluteLocationPoint alp = (AbsoluteLocationPoint)o;
				if(alp.getSourceCd()!='N'){
					if((alp.getCoordinateTypeCode()!=null && alp.getCoordinateTypeCode().charValue()!='W' && alp.getCoordinateTypeCode().charValue()!='C')){
						if(alp.getLatitudeDecimalDegree()==null || alp.getLongitudeDecimalDegree()==null){
							errors.add("error.geographic.latlong_required");
							isErr = true;
						}else{
							//check other fields
							if(alp.getLatitudeDirection()==null){
								errors.add("error.geographic.latdir_required");
								isErr = true;
							}
							if(alp.getLongitudeDirection()==null){
								errors.add("error.geographic.longdir_required");
								isErr = true;
							}
							if(alp.getHorizontalCoordntDatumRef()==null){
								errors.add("error.geographic.datum_required");
								isErr = true;
							}else if(alp.getHorizontalCoordntDatumRef().getHorizontalCoordntDatumId()==12){
								errors.add("error.geographic.datum_no_unknown");
								isErr = true;
							}
							if(alp.getHorizontalCllctnMethodRef()==null){
								errors.add("error.geographic.method_required");
								isErr = true;
							}
							if(alp.getLocationDescriptionRef()==null){
								errors.add("error.geographic.locDesc_required");
								isErr = true;
							}
							if(alp.getMeasurementDate()==null){
								errors.add("error.geographic.measDate_required");
								isErr = true;
							}						
						}	
					}
				}
			}
			Object c = facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);
			if(c==null){
				errors.add("error.geographic.county_required");
				isErr = true;
			}
			Object w = facilityAddressService.getPrimaryGeographicAreaWatershed(facilityId);
			if(w==null){
				errors.add("error.geographic.watershed_required");
				isErr = true;
			}
			Object cd = facilityAddressService.getPrimaryGeographicAreaCongDistrict(facilityId);
			if(cd==null){
				errors.add("error.geographic.congDistrict_required");
				isErr = true;
			}
			
			//Present Facility Address is required only for facility
			if(isAddressRequired(facilityId)){
				FacilityAddress fa = facilityAddressService.getFacilityAddress(facilityId);
				if(fa!=null){
					if(fa.getSourcedFromNpdesFlag()=='N'){
						if(fa.getStreetAddress1()==null ||"".equals(fa.getStreetAddress1())){
							errors.add("error.geographic.streetaddress_required");
							isErr = true;
						}
						if(fa.getCity()==null ||"".equals(fa.getCity())){
							errors.add("error.geographic.city_required");
							isErr = true;
						}
						if(fa.getZip()==null ||"".equals(fa.getZip())){
							errors.add("error.geographic.zip_required");
							isErr = true;
						}					
					}
				}else{
					errors.add("error.geographic.address_required");
					isErr = true;				
				}				
			}			
		}else if(facilityTypeService.isFacilityTypeStatusProjected(facilityId)){
			Object c = facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);
			if(c==null){
				errors.add("error.geographic.county_required");
				isErr = true;
			}
			Object w = facilityAddressService.getPrimaryGeographicAreaWatershed(facilityId);
			if(w==null){
				errors.add("error.geographic.watershed_required");
				isErr = true;
			}
			Object cd = facilityAddressService.getPrimaryGeographicAreaCongDistrict(facilityId);
			if(cd==null){
				Collection cds = facilityAddressService.getGeographicAreaConDistrictByFacilityId(facilityId);
				if(cds.size()>0){
					errors.add("error.geographic.congDistrict_required");
					isErr = true;					
				}
			}
		}		
		
		//Error = Y if facility is both Wastewater and Decentralized and Comment for
		//Locaion is blank/not exist
		
		if(facilityService.isFacilityWasteWater(facilityId)&& facilityService.isFacilityDecentralized(facilityId)){
			List l = facilityCommentsService.findFacilityCommentsByFacilityIdAndDataAreaId(facilityId.longValue(), dataAreaId);
			if(l.size()==0){
				errors.add("error.geographic.decentralized_comments");
				isErr = true;
			}	
		}
		
		//if not sourced from pcs all the coordinate information need to be present
		//AbsoluteLocationPoint ap = facilityAddressService.getFacilityCoordinates(facilityId);
		//if(ap != null && ap.getSourceCd()){	
		//}
		
		return isErr;
	}
	
	
	private boolean isAddressRequired(Long facilityId){
//		If a present Facility Type exists, and its Overall Facility Type is
		//not NPS,STORMWATER, DECENSTRALIZED, PLANNING,
		Collection fts = facilityTypeService.getFacityType(facilityId);
		for (Iterator iter = fts.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(ft.getPresentFlag()=='Y'){
				long overallType=ft.getFacilityTypeRef().getFacilityOverallTypeRef().getFacilityOverallTypeId();
				long facilityTypeId=ft.getFacilityTypeRef().getFacilityTypeId();
				if(FacilityTypeService.FACILITY_TYPE_OVERALL_TYPE_NPS!=overallType &&
				   FacilityTypeService.FACILITY_OVERALL_TYPE_STROMWATER!=overallType &&
				   FacilityTypeService.FACILITY_OVERALL_TYPE_DECENTRALIZED!=overallType &&
				   FacilityTypeService.FACILITY_OVERALL_TYPE_PLANNING!=overallType &&
				   FacilityTypeService.FACILITY_TYPE_INTERCEPTOR_SEWER.longValue()!= facilityTypeId &&
				   FacilityTypeService.FACILITY_TYPE_COMBINED_SEWER.longValue()!= facilityTypeId &&
				   FacilityTypeService.FACILITY_TYPE_SEPERATE_SEWER.longValue()!= facilityTypeId){
					return true;
				}
			}			
		} 
		return false;
	}
	
	//Always required
	public void setFesService(FESService fes){
		fesService = fes;
	}
		
    //  set the facility address service
    private FacilityAddressService facilityAddressService;
	public void setFacilityAddressService(FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}

	protected FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService fts){
		facilityTypeService = fts;
	}	
	
	protected FacilityService facilityService;
	public void setFacilityService(FacilityService fs){
		facilityService = fs;
	}
	
	protected FacilityCommentsService facilityCommentsService;
	public void setFacilityCommentsService(FacilityCommentsService fcs){
		facilityCommentsService = fcs;
	}
}
