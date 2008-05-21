package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.model.FacilityFlow;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChange;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityCommentsService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.FlowService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FlowValidator extends FESValidator {
	
	public FlowValidator() {
		super(FacilityService.DATA_AREA_FLOW);
	}
	
	public boolean isRequired(Long facilityId){
		FacilityType ft = facilityTypeService.getFacilityType(facilityId, FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT);
		if(ft!=null){
			return true;
		}
		return false;
	}
	
	public boolean isEntered(Long facilityId){
		return flowService.isFlowExist(facilityId);
	}
	
	public boolean isErrors(Long facilityId, Collection errors){
		boolean isErr = false;
		if(isRequired(facilityId)==true && isEntered(facilityId)==false){
			errors.add("error.flow.required");
			return true;
		}
		
		FacilityFlow ff = flowService.getFacilityFlow(facilityId, FlowService.FLOW_TYPE_TOTAL);
		if(ff !=null){
			
			//present flow
			if(facilityTypeService.isFacilityTypeStatusPresentAndApplicableToDataArea(facilityId, dataAreaId)){
				//check present design flow 
				if(ff.getPresentFlowMsr()==null || (ff.getPresentFlowMsr()!=null && ff.getPresentFlowMsr().doubleValue()<= 0)){
					errors.add("error.flow.present_lt_zero");
					isErr = true;
				}
				
				//check existing design flow
				if(ff.getExistingFlowMsr()==null || (ff.getExistingFlowMsr()!=null && ff.getExistingFlowMsr().doubleValue()<= 0)){
					errors.add("error.flow.existing_lt_zero");
					isErr = true;
				}
			}
			
			//projected flow
			if(facilityTypeService.isFacilityTypeStatusProjectedAndApplicableToDataArea(facilityId, dataAreaId)){
				if(ff.getProjectedFlowMsr()==null || (ff.getProjectedFlowMsr()!=null && ff.getProjectedFlowMsr().doubleValue()<= 0)){
					errors.add("error.flow.projected_lt_zero");
					isErr = true;
				}
			}
			
			//increase capacility
			if(increasedCapacity(facilityId)){
				if(ff.getProjectedFlowMsr()!=null && (ff.getPresentFlowMsr()!=null )){
					if(ff.getProjectedFlowMsr().doubleValue()<=ff.getPresentFlowMsr().doubleValue()){
						errors.add("error.flow.projected_lt_present");
						isErr = true;
					}
				}
			}else{
				if(isValidForFlowCheck(facilityId)){
					if(ff.getProjectedFlowMsr()!=null && (ff.getPresentFlowMsr()!=null )){
						if(ff.getProjectedFlowMsr().doubleValue()>ff.getPresentFlowMsr().doubleValue()){
							errors.add("error.flow.present_gt_projected");
							isErr = true;
						}
					}
				}
			}
						
			
			//flow to population ratio comments
			if(!flowService.isFlowToRationValid(facilityId)){
				List l = facilityCommentsService.findFacilityCommentsByFacilityIdAndDataAreaId(facilityId.longValue(), dataAreaId);
				if(l.size()==0){
					errors.add("error.flow.missing_popflowratio_comment");
					isErr = true;
				}				
			}
			return isErr;
		}else{
			log.error("Error: Facility Flow should not be null");
		}		
		return false;
	}
	
	private boolean increasedCapacity(Long facilityId){
		Collection type = facilityTypeService.getFacityType(facilityId);
		for (Iterator iter = type.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(ft.getFacilityTypeRef().getValidForFlowFlag()=='Y' & ft.getPresentFlag()=='Y' & ft.getProjectedFlag()=='Y'){
				Collection changeType = ft.getFacilityTypeChanges();
				for (Iterator iterator = changeType.iterator(); iterator.hasNext();) {
					FacilityTypeChange ftc = (FacilityTypeChange) iterator.next();
					if(ftc.getId().getChangeTypeId() == FacilityTypeService.CHANGE_TYPE_INCREASE_CAPACILITY.longValue()){
						return true;
					}
				}
			}
		}
		return false;		
	}
	
	private boolean isValidForFlowCheck(Long facilityId){
		Collection type = facilityTypeService.getFacityType(facilityId);
		for (Iterator iter = type.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(ft.getFacilityTypeRef().getValidForFlowFlag()=='Y' & ft.getPresentFlag()=='Y' & ft.getProjectedFlag()=='Y'){
				return true;
			}
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

	protected FlowService flowService;
	public void setFlowService(FlowService flow){
		flowService = flow;
	}
	
	protected FacilityCommentsService facilityCommentsService;
	public void setFacilityCommentsService(FacilityCommentsService fcs){
		facilityCommentsService = fcs;
	}
	
}
