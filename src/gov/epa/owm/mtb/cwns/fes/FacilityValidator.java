package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityCommentsService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

import java.util.Collection;
import java.util.List;

public class FacilityValidator extends FESValidator {
	
	public FacilityValidator() {
		super(FacilityService.DATA_AREA_FACILITY);
	}
	
	public boolean isRequired(Long facilityId){
		// facility is always required
		return true;
	}
	
	public boolean isEntered(Long facilityId){
		// alway true
		return true;
	}
	
	public boolean isErrors(Long facilityId, Collection errors){
		//check if facility Type exists
		Collection ft = facilityTypeService.getFacityType(facilityId);
		if(ft!=null && ft.size()!=0){
			//check if comment exists facility Type other
			FacilityType facilityType = facilityTypeService.getFacilityType(facilityId, FacilityTypeService.FACILITY_TYPE_OTHER);
			if(facilityType!=null){
				//check if comment exists
				List l = facilityCommentsService.findFacilityCommentsByFacilityIdAndDataAreaId(facilityId.longValue(), dataAreaId);
				if(l.size()==0){
					errors.add("error.facility.noOtherTypeComment");
					return true;
				}
			}
		}else{	
			errors.add("error.facility.nofacilityTypeAssigned");
			return true;
		}
		return false;
	}
	
	//Always required
	public void setFesService(FESService fes){
		fesService = fes;
	}
		
	protected FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService fts){
		facilityTypeService = fts;
	}

	protected FacilityCommentsService facilityCommentsService;
	public void setFacilityCommentsService(FacilityCommentsService fcs){
		facilityCommentsService = fcs;
	}		
}
