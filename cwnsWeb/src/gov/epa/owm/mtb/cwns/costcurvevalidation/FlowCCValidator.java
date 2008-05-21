package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class FlowCCValidator extends CostCurveValidator {
	public FlowCCValidator(){
		super(FacilityService.DATA_AREA_FLOW);
	}
	
	public boolean isCostCurveApplicable(String costCurveCode) {
		boolean isApplicable = false;
		if(CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
		CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
		CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
		CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
		CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode) ||
		CostCurveService.CODE_DISINFECTION_ONLY.equals(costCurveCode) ||
		CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){
			isApplicable = true;
		}
		return isApplicable;
	}

	public boolean isErrors(Long facilityId, String costCurveCode, Set errors) {
		boolean isError=false;
		
		//case 1: Present Design Municipal Flow > 0 
		if(CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode)){
			if(flowService.getMunicipalPresentFlowRate(facilityId)<=0){
				errors.add("error.ccv.flow.present_municipal_flow_lte_zero");
				isError=true;
			}
		}
		
		//case 2: Calculated Projected Municipal Flow must be < 5
		if(CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_DISINFECTION_ONLY.equals(costCurveCode)){
			if (getProjectedMunicipalFlowRate(facilityId)>=5){
				errors.add("error.ccv.flow.calculated_projected_municipal_flow_gte_five");
				isError=true;
			}
		}		
		
		//case 3: Projected Municipal Flow must be >= present Municipal Flow
		//AND Total Projected Flow must be >= Total Present Flow
		if(CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode)){
			
			HashMap totalFlows = flowService.getTotalFlowValues(facilityId);			
			BigDecimal projectedTotalFlow = totalFlows.get("Projectedflow")!=null?(BigDecimal)totalFlows.get("Projectedflow"):new BigDecimal(0.0);
			BigDecimal presentTotalFlow = totalFlows.get("Presentflow")!=null?(BigDecimal)totalFlows.get("Presentflow"):new BigDecimal(0.0);

			if(!((flowService.getMunicipalProjectedFlowRate(facilityId) >= flowService.getMunicipalPresentFlowRate(facilityId))
					&&(projectedTotalFlow.compareTo(presentTotalFlow)>=0))){
				errors.add("error.ccv.flow.proj_municipal_lt_pre_municipal_or_pro_total_lt__pre_total");
				isError=true;
			}			
		}
			
		//case 4: Cannot have Total flow and no individual Flow
		if(CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode)){
		
			//total flows
			HashMap totalFlows = flowService.getTotalFlowValues(facilityId);
			boolean hasExistingTotalFlow = totalFlows.get("Existingflow")!=null?true:false;
			boolean hasPresentTotalFlow = totalFlows.get("Presentflow")!=null?true:false;
			boolean hasProjectedTotalFlow = totalFlows.get("Projectedflow")!=null?true:false;
			
			//individual flows
			boolean hasIndiExistingFlow = false;
			boolean hasIndiPresentFlow = false;
			boolean hasIndiProjectedFlow = false;			
			HashMap individualFlows = flowService.getIndividualFlowValues(facilityId);
			Set flowIdSet = individualFlows.keySet();			
			if (flowIdSet!=null){
				Iterator flowIdSetIter = flowIdSet.iterator();				
				while (flowIdSetIter.hasNext()){
					Object[] individualFlowVals = (Object[])individualFlows.get(flowIdSetIter.next());
					if (individualFlowVals[0]!=null) hasIndiExistingFlow=true;
					if (individualFlowVals[1]!=null) hasIndiPresentFlow=true;
					if (individualFlowVals[2]!=null) hasIndiProjectedFlow=true;
				}				
			}		
			
			if((hasExistingTotalFlow && !hasIndiExistingFlow)||
					(hasPresentTotalFlow && !hasIndiPresentFlow) ||
					(hasProjectedTotalFlow && !hasIndiProjectedFlow)){
				errors.add("error.ccv.flow.total_flow_and_no_individual_flow");
				isError=true;	
			}
		}
		
		//case 5: All terminating facilities in the present Sewershed must have 
		//present design Total flow > 0 (i.e. must be a Treatment Plant)
		if(CostCurveService.CODE_COMBINED_SEWER_OVERFLOW.equals(costCurveCode)){
			Collection otherFacilitiesInSewerShed = 
				populationService.getRelatedSewerShedFacilitiesByDischargeType(facilityId, PopulationService.PRESENT_ONLY);			
			
			if(otherFacilitiesInSewerShed!=null && otherFacilitiesInSewerShed.size()>0){
				Iterator facIter =  otherFacilitiesInSewerShed.iterator();
				while(facIter.hasNext()){
					Long curFacId = (Long)facIter.next();
					if (dischargeService.isTerminatingFacility(curFacId)){
						if (flowService.getPresentTotalFlow(curFacId)<=0){
							errors.add("error.ccv.flow.terminating_facilities_in_sewershed_pre_total_flow_lte_zero");
							isError=true;
							break;
						}
					}						
				}
			}
		}
		
		//case 6: Compute Projected Municipal Design Flow
		//- nrpop = facility's non-residential projected receiving collection population
		//- upnrpop = upstream facilities' non-residential projected receiving collection population
		//- rpop = facility’s residential projected receiving collection population
		//- uprpop = upstream facilities' residential projected receiving collection population
		//fut_mun_flow_rate (ROUNDED to 1/1000) = (((nrpop + upnrpop) * 0.6) + rpop + uprpop) * gpcd / 1,000,000
		//Error if fut_mun_flow_rate is less than or equal to present municipal design flow rate
		if(CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode)){			
			double projected_municipal_flow_rate = getProjectedMunicipalFlowRate(facilityId);			
			double present_municipal_flow_rate = flowService.getMunicipalPresentFlowRate(facilityId);
			
			if (projected_municipal_flow_rate <= present_municipal_flow_rate){
				isError=true;
				errors.add("error.ccv.flow.calc_proj_mun_flow_rate_lt_pre_mun_flow_rate");
			}
		}
		return isError;
	}
	
	private double getProjectedMunicipalFlowRate(Long facilityId){		
		int nrpop = populationService.getProjectedNonResidentialReceivingPopulation(facilityId);
		int upnrpop = populationService.getProjectedUpstreamNonResidentialReceivingPopuliation(facilityId);			
		int rpop = populationService.getProjectedResidentialReceivingPopulation(facilityId);
		int uprpop = populationService.getProjectedUpstreamResidentialReceivingPopuliation(facilityId);
		float gpcd = getGPCD(facilityId);
		double projected_municipal_flow_rate = 
			new BigDecimal((((nrpop+upnrpop)*0.6)+rpop + uprpop)*gpcd/1000000).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		return projected_municipal_flow_rate;
	}
	
	private int getGPCD(Long facilityId){
		int gpcd = 0;
		int projectedResPop = populationService.getProjectedResidentialReceivingPopulation(facilityId);
		int projectedResOnsiteWTSPop = populationService.getProjectedResIndividualSewageDisposalSystemPopulation(facilityId);
		int projectedResClusteredWTSPop = populationService.getProjectedResDecentralizedPopulation(facilityId);
		int uprpop = populationService.getProjectedUpstreamResidentialReceivingPopuliation(facilityId);
		
		//pop = facility's projected residential receiving collection population 
		//		+ facility's projected residential Onsite wastewater treatment system population
		//		+ facility's projected residential clustered wastewater treatment system population
		//		+ all upstream facilities' projected residential receiving collection population
		long pop = projectedResPop + projectedResOnsiteWTSPop + projectedResClusteredWTSPop + uprpop;
		if (pop<5000){
			gpcd=95;
		}else if(pop>=5000 && pop<10000){
			gpcd=105;
		}else if(pop>=10000){
			gpcd=115;
		}		
		return gpcd;
	}
	
	private FlowService flowService;
	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}
	
	private PopulationService populationService;
	public void setPopulationService(PopulationService populationService){
		this.populationService = populationService;
	}
	
	private DischargeService dischargeService;
	public void setDischargeService(DischargeService dischargeService){
		this.dischargeService = dischargeService;
	}
}
