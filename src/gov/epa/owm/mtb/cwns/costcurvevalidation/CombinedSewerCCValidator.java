package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.CombinedSewerStatusRef;
import gov.epa.owm.mtb.cwns.service.CSOService;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.util.Set;

public class CombinedSewerCCValidator extends CostCurveValidator {
	public CombinedSewerCCValidator(){
		super(FacilityService.DATA_AREA_CSO);
	}
	
	public boolean isCostCurveApplicable(String costCurveCode) {
		boolean isApplicable = false;
		if (CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){
			isApplicable=true;
		}
		return isApplicable;
	}
	
	public boolean isErrors(Long facilityId, String costCurveCode, Set errors) {
		boolean isError=false;
		
		//case 1: Current facility's Combined Sewer Status must be Requires a Cost Curve 
		//or Both documented and Cost Curve Needs
		//case 2: Combined Sewer CC Population AND Combined Sewer CC Area must be > 0
		if (CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){
			//case 1 implementation
			CombinedSewer facilityCombinedSewer = csoService.getFacilityCSOInfo(facilityId);
			boolean case1Error = true;
			if (facilityCombinedSewer!=null){
				CombinedSewerStatusRef facilityCombinedSewerStatusRef = facilityCombinedSewer.getCombinedSewerStatusRef();
				if (facilityCombinedSewerStatusRef!=null){
					String combinedSewerStatusId = facilityCombinedSewerStatusRef.getCombinedSewerStatusId();
					if(combinedSewerStatusId.charAt(0)==CSOService.COMBINED_SEWER_STATUS_REQUIRES_COST_CURVE ||
							combinedSewerStatusId.charAt(0)==CSOService.COMBINED_SEWER_STATUS_BOTH){
						case1Error=false;
					}
				}
			}
			
			if (case1Error){
				isError = true;
				errors.add("error.ccv.combined_sewer.status_not_req_cc_and_not_both_doc_cc_needs");
			}			
			
			//case 2 implementation			
			int ccPopulationCount = facilityCombinedSewer.getCcPopulationCount()!=null?facilityCombinedSewer.getCcPopulationCount().intValue():0;
			int ccArea = facilityCombinedSewer.getCcAreaSquareMilesMsr()!=null?facilityCombinedSewer.getCcAreaSquareMilesMsr().intValue():0;
			if (!(ccPopulationCount>0 && ccArea > 0)){
				isError = true;
				errors.add("error.ccv.combined_sewer.cc_population_or_area_not_positive");
			}			
		}		
		return isError;
	}	
	
	private CSOService csoService;
	public void setCsoService(CSOService csoService) {
		this.csoService = csoService;
	}		
}
