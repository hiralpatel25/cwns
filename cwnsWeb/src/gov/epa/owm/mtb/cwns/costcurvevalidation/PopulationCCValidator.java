package gov.epa.owm.mtb.cwns.costcurvevalidation;

import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.util.Set;

public class PopulationCCValidator extends CostCurveValidator {
	
	public PopulationCCValidator(){
		super(FacilityService.DATA_AREA_POPULATION);
	}

	public boolean isCostCurveApplicable(String costCurveCode) {
		boolean isApplicable = false;
		if (CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
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
			CostCurveService.CODE_REHAB_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode)){
			isApplicable = true;
		}
		return isApplicable;
	}
	
	public boolean isErrors(Long facilityId, String costCurveCode, Set errors) {
		boolean isError = false;		
		long projectedRecColPop =	populationService.getTotalProjectedReceivingCollectionPopulation(facilityId);
		long presentRecColPop = populationService.getTotalPresentReceivingCollectionPopulation(facilityId);
		long newWTSHouses = populationService.getPresentNonResidentialOnsiteWastewaterHouses(facilityId) +
								populationService.getPresentResidentialOnsiteWastewaterHouses(facilityId);		
		long projectedNewWTSHouses = populationService.getProjectedNonResidentialOnsiteWastewaterHouses(facilityId) +
										populationService.getProjectedResidentialOnsiteWastewaterHouses(facilityId);

		//long projectedNewWTSAllHouses = populationService.getNonResidentialNewIndividualWastewaterTreatmentSystemAllHouses(facilityId) +
		//									populationService.getResidentialNewIndividualWastewaterTreatmentSystemAllHouses(facilityId);

		//case 1: Projected Receiving Collection Population (residential + non-residential)
		//+ Projected Upstream receiving Collection (residential + non-residential) > 0
		if (CostCurveService.CODE_NEW_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_FLOW_CAPACITY.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_LEVEL_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_INCREASE_CAPACITY_TREATMENT.equals(costCurveCode) ||
				CostCurveService.CODE_REPLACE_TREATMENT_PLANT.equals(costCurveCode) ||
				CostCurveService.CODE_DISINFECTION_ONLY.equals(costCurveCode)){
			    
				long proUpsRecColPop =
					populationService.getProjectedUpstreamNonResidentialReceivingPopuliation(facilityId) +
					populationService.getProjectedUpstreamResidentialReceivingPopuliation(facilityId);
			
				if ((projectedRecColPop + proUpsRecColPop)<=0) {
					errors.add("error.ccv.population.projected_receiving_lte_zero");
					isError=true;
				}				
		}


		//case 2: Separate Sewer Rehabilitation/Replacement Population > 0 and < 15,000 
		//and < Present Receiving Collection (residential + non-residential)
		if (CostCurveService.CODE_SEPERATE_SEWER_REHAB.equals(costCurveCode)){
			//************
			long sewerRehabPop = populationService.getPresentRehabSeparateSewerSystemPopulation(facilityId);
					
			if(!((sewerRehabPop>0 && sewerRehabPop<15000) && 
					(sewerRehabPop<presentRecColPop))){
				errors.add("error.ccv.population.sewer_rehab_replace_lte_zero_or_gt_present_rec");
				isError=true;
			}
		}
		
		//case 3: New Separate Sewer Population > 0 and < 15,000 and < 
		//Projected - Present Receiving Collection (residential + non-residential)
		if (CostCurveService.CODE_NEW_SEPERATE_SEWER_INTERCEPTOR.equals(costCurveCode) ||
			CostCurveService.CODE_NEW_SEPERATE_SEWER_COLLECTOR.equals(costCurveCode)){
			long newSepSewerPop = populationService.getPresentNewSeparatSewerSystemPopulation(facilityId);
			if(!((newSepSewerPop>0 && newSepSewerPop<15000) && 
					(newSepSewerPop<(projectedRecColPop - presentRecColPop)))){
				errors.add("error.ccv.population.separate_sewer_lte_zero_or_gt_present_rec");
				isError=true;
			}			
		}
		
		//case 4: (residential + non-residential) Houses with New Individual Wastewater Treatment Systems – all Houses > 0 
		//AND <= Projected Houses (Residential + Non-residential) with Individual Wastewater Treatment Systems
		if (CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_ALL.equals(costCurveCode)){
			long newWTSAllHouses = populationService.getNonResidentialNewIndividualWastewaterTreatmentSystemAllHouses(facilityId) +
										populationService.getResidentialNewIndividualWastewaterTreatmentSystemAllHouses(facilityId);			
			if (!(newWTSAllHouses> 0 && newWTSAllHouses<= projectedNewWTSHouses)){
				errors.add("error.ccv.population.indi_WTS_houses_lte_zero_or_gt_projected_indi_WTS_houses");
				isError=true;
			}
		}

		//case 5: (residential + non-residential) Houses with New Individual Wastewater Treatment Systems – Innovative > 0 
		//and <= Projected Houses (Residential + Non-residential) with Individual Wastewater Treatment Systems
		if (CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode)){
			long newWTSInnovative = populationService.getNonResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(facilityId)
										+ populationService.getResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(facilityId);
			
			if(!(newWTSInnovative>0 && newWTSInnovative<=projectedNewWTSHouses)){
				errors.add("error.ccv.population.indi_WTS_innovative_lte_zero_or_gt_projected_indi_WTS_houses");
				isError=true;
			}
		}
		
		//case 6: (residential + non-residential) Houses with New Individual Wastewater Treatment Systems – 
		//Conventional > 0 and <= Projected Houses (Residential + Non-residential) with Individual Wastewater Treatment Systems
		if (CostCurveService.CODE_INDIVIDUAL_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode)){
			long newWTSConventional = populationService.getResidentialNewIndividualWastewaterTreatmentSystemConventionalHouses(facilityId) +
										populationService.getNonResidentialNewIndividualWastewaterTreatmentSystemConventionalHouses(facilityId);
			
			if(!(newWTSConventional>0 && newWTSConventional<=projectedNewWTSHouses)){
				errors.add("error.ccv.population.indi_WTS_conventioal_lte_zero_or_gt_projected_indi_WTS_houses");
				isError=true;
			}
		}
		
		//case 7: (residential + non-residential) Houses with New Clustered Wastewater Treatment Systems > 0 
		//and <= Projected Houses (Residential + Non-residential) with Cluster Wastewater Treatment Systems
		if (CostCurveService.CODE_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode)){
			long clusterWTSHouses = populationService.getResidentialNewClusterWastewaterTreatmentSystemHouses(facilityId) +
										populationService.getNonResidentialNewClusterWastewaterTreatmentSystemHouses(facilityId);
			
			long projectedClusterWTSHouses = populationService.getProjectedNonResidentialClusteredWastewaterHouses(facilityId) +
												populationService.getProjectedResidentialClusteredWastewaterHouses(facilityId);
			
			if(!(clusterWTSHouses>0 && clusterWTSHouses<=projectedClusterWTSHouses)){
				errors.add("error.ccv.population.cluster_WTS_lte_zero_or_gt_projected_cluster_WTS_houses");
				isError=true;
			}
		}
		
		//case 8: (residential + non-residential) Houses with Rehab Individual Wastewater Treatment Systems – all > 0 
		//and <= Present Houses (Residential + Non-residential) with Individual Wastewater Treatment Systems 
		if (CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_ALL.equals(costCurveCode)){
			long rehabWTSHouses = populationService.getNonResidentialRehabIndividualWastewaterTreatmentSystemAllHouses(facilityId) +
									populationService.getResidentialRehabIndividualWastewaterTreatmentSystemAllHouses(facilityId);
			
			if(!(rehabWTSHouses>0 && rehabWTSHouses<=newWTSHouses)){
				errors.add("error.ccv.population.rehab_WTS_houses_lte_zero_or_gt_indi_WTS_houses");
				isError=true;
			}		
		}
		
		//case 9: (residential + non-residential) Houses with Rehab Individual Wastewater Treatment Systems – Innovative > 0 
		//and <= Present Houses (Residential + Non-residential) with Individual Wastewater Treatment Systems
		if(CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_INNOVATIVE.equals(costCurveCode)){
			long rehabWTSInnovativeHouses = populationService.getResidentialRehabIndividualWastewaterTreatmentSystemInnovativeHouses(facilityId)
							+ populationService.getNonResidentialRehabIndividualWastewaterTreatmentSystemInnovativeHouses(facilityId);

			if(!(rehabWTSInnovativeHouses>0 && rehabWTSInnovativeHouses<= newWTSHouses)){
				errors.add("error.ccv.population.rehab_WTS_innovative_houses_lte_zero_or_gt_indi_WTS_houses");
				isError=true;
			}					
		}		
		
		//case 10: (residential + non-residential) Houses with Rehab Individual Wastewater Treatment Systems – Conventional > 0 
		//and <= Present Houses (Residential + Non-residential) with Individual Wastewater Treatment Systems
		if (CostCurveService.CODE_REHAB_WASTEWATER_TREATMENT_CONVENTIONAL.equals(costCurveCode)){
			long rehabWTSConventionalHouses = populationService.getResidentialRehabIndividualWastewaterTreatmentSystemConventionalHouses(facilityId)
								+ populationService.getNonResidentialRehabIndividualWastewaterTreatmentSystemConventionalHouses(facilityId);
			
			if(!(rehabWTSConventionalHouses>0 && rehabWTSConventionalHouses<= newWTSHouses)){
				errors.add("error.ccv.population.rehab_WTS_conventional_houses_lte_zero_or_gt_indi_WTS_houses");
				isError=true;
			}			
		}		
		
		//case 11: (residential + non-residential) Houses with Rehab Clustered Wastewater Treatment Systems > 0 
		//and <= Present Houses (Residential + Non-residential) with Clustered Wastewater Treatment Systems
		if(CostCurveService.CODE_REHAB_CLUSTER_WASTEWATER_TREATMENT.equals(costCurveCode)){
			long rehabClusterWTSHouses = populationService.getResidentialRehabClusterWastewaterTreatmentSystemHouses(facilityId) +
											populationService.getNonResidentialRehabClusterWastewaterTreatmentSystemHouses(facilityId);
			
			long presentClusterWTSHouses = populationService.getPresentNonResidentialClusteredWastewaterHouses(facilityId) +
											populationService.getPresentResidentialClusteredWastewaterHouses(facilityId);
			
			if(!(rehabClusterWTSHouses>0 && rehabClusterWTSHouses<= presentClusterWTSHouses)){
				errors.add("error.ccv.population.rehab_clustered_WTS_houses_lte_zero_or_gt_clustered_WTS_houses");
				isError=true;
			}			
		}		
		
		return isError;
	}
	
	private PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		this.populationService = populationService;
	}	
}
