package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.FESService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

public abstract class CostCurveValidator {
	
	protected Logger log;
	protected Long costCurveId;	
	protected CostCurveService costCurveService;
	protected Long dataAreaId;
	
	protected FESService fesService;
	
	public CostCurveValidator(Long daId) {
		dataAreaId=daId;
	}
	
	public void validate(Long facilityId, String userId, Collection facilityCostCurves){
		boolean error = false;
		Set errors = new HashSet();
		//loop over the list of cost curves
		for (Iterator iter = facilityCostCurves.iterator(); iter.hasNext();) {
			FacilityCostCurve fcc = (FacilityCostCurve) iter.next();
			//get cost curve id
			String costCurveCode = fcc.getCostCurveRef().getCode();
			long facilityCostCurveId = fcc.getFacilityCostCurveId();
			//check if the cost curver is applicable to the data area
			if(isCostCurveApplicable(costCurveCode)){
				char errorFlag ='N';
				if(isErrors(facilityId,costCurveCode, errors)==true){
					errorFlag = 'Y';
					error =true;
				}
				costCurveService.updateFacilityCostCurveDataArea(errorFlag, errors, facilityCostCurveId, dataAreaId, userId);
			}						
		}
		if(error){
			//save the errors into the data area messages table
			//fesService.
		}
		
	}
	
	public void setCostCurveService(CostCurveService ccs){
		costCurveService=ccs;		
	}
	
	public abstract boolean isCostCurveApplicable(String costCurveCode);

	public abstract boolean isErrors(Long facilityId, String costCurveCode, Set errors);
	
}
