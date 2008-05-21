package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.service.CSOService;
import gov.epa.owm.mtb.cwns.service.CapitalCostService;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.NeedsService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CSOValidator extends FESValidator {

	public CSOValidator() {
		super(FacilityService.DATA_AREA_CSO);
	}

	public boolean isRequired(Long facilityId) {
		FacilityType ft = facilityTypeService.getFacilityType(facilityId, FacilityTypeService.FACILITY_TYPE_COMBINED_SEWER);
		if(ft!=null){
			return true;
		}
		return false;
	}

	public boolean isEntered(Long facilityId) {
		CombinedSewer cs = csoService.getFacilityCSOInfo(facilityId);
		if(cs!=null){
			return true;
		}
		return false;
	}

	public boolean isErrors(Long facilityId, Collection errors) {
		if(isRequired(facilityId)==true && isEntered(facilityId)==false){
			errors.add("error.cso.required");
			return true;
		}	
		
		int futurePop = populationService.getFutureTotalResAndNonResPopulation(facilityId);
		int csoPop = csoService.getTotalCSOPopulation(facilityId);
		boolean isError = false;
		if(futurePop < csoPop){
			errors.add("error.cso.futurePopLessThanCsoPop");
			isError=true;
		}
		
		CombinedSewer cs = csoService.getFacilityCSOInfo(facilityId);
		Collection facilityCostCurves = costCurveService.getFacilityCostCurves(facilityId);		
		if(cs!=null){
			if(cs.getCombinedSewerStatusRef().getCombinedSewerStatusId().charAt(0)==CSOService.COMBINED_SEWER_STATUS_REQUIRES_COST_CURVE||
					cs.getCombinedSewerStatusRef().getCombinedSewerStatusId().charAt(0)==CSOService.COMBINED_SEWER_STATUS_BOTH){
				//area of population =0
				if(cs.getCcPopulationCount().intValue()==0 || cs.getCcPopulationCount().intValue()==0){
					errors.add("error.cso.area_or_pop_zero");
					isError=true;
				}
				
				//cost curve is not assigned
				if(!isCSOCCAssigned(facilityCostCurves)){
					errors.add("error.cso.curve.notAssigned");
					isError=true;				
				}			
			}
			
			if(cs.getCombinedSewerStatusRef().getCombinedSewerStatusId().charAt(0)!=CSOService.COMBINED_SEWER_STATUS_REQUIRES_COST_CURVE &&
					cs.getCombinedSewerStatusRef().getCombinedSewerStatusId().charAt(0)!=CSOService.COMBINED_SEWER_STATUS_BOTH){
				//cost curve is not assigned
				if(isCSOCCAssigned(facilityCostCurves)){
					errors.add("error.cso.curve.status.noCostCurve.isAssigned");
					isError=true;				
				}			
			}		
			
			if(cs.getCombinedSewerStatusRef().getCombinedSewerStatusId().charAt(0)==CSOService.COMBINED_SEWER_STATUS_REQUIRES_COST_CURVE||
					cs.getCombinedSewerStatusRef().getCombinedSewerStatusId().charAt(0)==CSOService.COMBINED_SEWER_STATUS_NONE){
				//check if cat 5 documentum needs are assigned then error
				Collection costs=capitalCostService.getFacilityCosts(facilityId);
				for (Iterator iter = costs.iterator(); iter.hasNext();) {
					Cost cost = (Cost) iter.next();
					if(CapitalCostService.CATEGORY_ID_COMBINED_SEWER_OVERFLOW.equals(cost.getCategoryRef().getCategoryId()) && cost.getCostMethodCode()==CapitalCostService.COST_METHOD_CODE_DOCUMENTED) {
						errors.add("error.cso.curve.CatV.NA");
						isError=true;
						break;
					}
				}			
			}
		}else{
			if(isCSOCCAssigned(facilityCostCurves)){
				errors.add("error.cso.curve.CCAssigned.CSOInfo.notExists");
				isError=true;
			}			
		}
		
		//
		
		
		return isError;
	}
	
	private boolean isCSOCCAssigned(Collection fcc) {
		for (Iterator iter = fcc.iterator(); iter.hasNext();) {
			FacilityCostCurve cc = (FacilityCostCurve) iter.next();
			if(CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(cc.getCostCurveRef().getCode())){
				return true;
			}
		}
		return false;
	}
	
	protected FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService fts){
		facilityTypeService = fts;
	}
	
	protected CSOService csoService;
	public void setCsoService(CSOService cso){
		csoService = cso;
	}
	
	protected PopulationService populationService;
	public void setPopulationService(PopulationService ps){
		populationService = ps;
	}
	
	//Always required
	public void setFesService(FESService fes){
		fesService = fes;
	}
	
	
	private CapitalCostService capitalCostService;
	public void setCapitalCostService(CapitalCostService ccs) {
		this.capitalCostService = ccs;
	}
	
	private CostCurveService costCurveService;
	public void setCostCurveService(CostCurveService ccs) {
		this.costCurveService= ccs;
	}

}
