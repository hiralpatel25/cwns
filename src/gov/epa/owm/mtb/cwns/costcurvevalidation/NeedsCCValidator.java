package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.model.CostCurveCategoryRef;
import gov.epa.owm.mtb.cwns.needs.NeedsHelper;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.NeedsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class NeedsCCValidator extends CostCurveValidator{
	public NeedsCCValidator() {
		super(FacilityService.DATA_AREA_NEEDS);
	}

	public boolean isCostCurveApplicable(String costCurveCode) {
		boolean isApplicable = false;
		if(CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_DISINFECTION_ONLY.equals(costCurveCode) ||
				CostCurveService.CODE_SEPERATE_SEWER_REHAB.equals(costCurveCode) ||			
				CostCurveService.CODE_NEW_SEPERATE_SEWER_INTERCEPTOR.equals(costCurveCode) ||
				CostCurveService.CODE_NEW_SEPERATE_SEWER_COLLECTOR.equals(costCurveCode) ||			
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_ALL.equals(costCurveCode) ||			
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode) ||			
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode) ||			
				CostCurveService.CODE_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode) ||			
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_ALL.equals(costCurveCode) ||			
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){
			isApplicable = true;
		}			
		return isApplicable;
	}

	public boolean isErrors(Long facilityId, String costCurveCode, Set errors) {		
		boolean isError = false;	
		
		//case 1: Documented Cost for the same category already exist - error condition
		if(CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_DISINFECTION_ONLY.equals(costCurveCode) ||
				CostCurveService.CODE_SEPERATE_SEWER_REHAB.equals(costCurveCode) ||			
				CostCurveService.CODE_NEW_SEPERATE_SEWER_INTERCEPTOR.equals(costCurveCode) ||
				CostCurveService.CODE_NEW_SEPERATE_SEWER_COLLECTOR.equals(costCurveCode) ||			
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_ALL.equals(costCurveCode) ||			
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode) ||			
				CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode) ||			
				CostCurveService.CODE_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode) ||			
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_ALL.equals(costCurveCode) ||			
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode) ||
				CostCurveService.CODE_REHAB_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){	
				
				long costCurveId = costCurveService.getCostCurveId(costCurveCode);
				Collection costCategoryList = costCurveService.getCostCategories(costCurveId);
				ArrayList ccCatList = null;
				
				if (costCategoryList!=null && costCategoryList.size()>0){
					ccCatList = new ArrayList();
					Iterator catIter = costCategoryList.iterator();
					while(catIter.hasNext()){
						CostCurveCategoryRef ccCatRef = (CostCurveCategoryRef)catIter.next();
						ccCatList.add(ccCatRef.getId().getCategoryId());
					}
				}
				
				Collection needsInfo = needsService.getNeedsInfo(facilityId.toString());
				if (needsInfo!=null && needsInfo.size()>0){
					Iterator needsInfoIter = needsInfo.iterator();
					while (needsInfoIter.hasNext()){
						NeedsHelper nh = (NeedsHelper)needsInfoIter.next();
						Long documentId = new Long(nh.getDocumentId());						
						if (needsService.documentedCostExists(facilityId, documentId, ccCatList)){
							isError = true;
							errors.add("error.ccv.needs.doc_cost_exists");
							break;
						}
					}
				}
		}
		return isError;
	}
	
	private NeedsService needsService;
	public void setNeedsService(NeedsService needsService) {
		this.needsService = needsService;
	}	
}
