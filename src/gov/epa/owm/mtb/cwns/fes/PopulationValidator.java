package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityCommentsService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.util.Collection;
import java.util.HashMap;

public class PopulationValidator extends FESValidator {

	public PopulationValidator() {
		super(FacilityService.DATA_AREA_POPULATION);
	}

	public boolean isRequired(Long facilityId) {
		if (populationService.isRecCollectionPresentEnabled(facilityId.toString()).equals("Y")
			|| populationService.isRecCollectionProjectedEnabled( facilityId.toString()).equals("Y")
			|| populationService.isDecentralizedPresentEnabled( facilityId.toString()).equals("Y")
			|| populationService.isDecentralizedProjectedEnabled(facilityId.toString()).equals("Y")
			|| populationService.isISDSPresentEnabled(facilityId.toString()).equals("Y")
			|| populationService.isISDSProjectedEnabled(facilityId.toString()).equals("Y")) {
			return true;
		}
		return false;
	}
	
	public boolean isEntered(Long facilityId) {
		Collection pop = populationService.getPopulationByFacilityId(facilityId);
		if(pop.size()>0){
			return true;
		}
		return false;
	}

	public boolean isErrors(Long facilityId, Collection errors) {
		boolean isErr = false; 
		if(populationService.isRecCollectionPresentEnabled(facilityId.toString()).equals("Y")){
			if(populationService.getTotalPresentReceivingCollectionPopulation(facilityId)<=0){
				errors.add("error.population.present.receivingColl");
				isErr = true;				
			}			
		}
		
		if(populationService.isRecCollectionProjectedEnabled(facilityId.toString()).equals("Y")){
			if(populationService.getTotalProjectedReceivingCollectionPopulation(facilityId)<=0){
				errors.add("error.population.projected.receivingColl");
				isErr = true;				
			}			
		}
		
		if(populationService.isISDSPresentEnabled(facilityId.toString()).equals("Y")){
			if(populationService.getTotalPresentIndividualSewageDisposalSystemPopulation(facilityId)<=0){
				errors.add("error.population.present.ISDS");
				isErr = true;				
			}			
		}
		
		if(populationService.isISDSProjectedEnabled(facilityId.toString()).equals("Y")){
			if(populationService.getTotalProjectedIndividualSewageDisposalSystemPopulation(facilityId)<=0){
				errors.add("error.population.projected.ISDS");
				isErr = true;				
			}			
		}
		
		if(populationService.isDecentralizedPresentEnabled(facilityId.toString()).equals("Y")){
			if(populationService.getTotalPresentDecentralizedPopulation(facilityId)<=0){
				errors.add("error.population.present.decentralized");
				isErr = true;				
			}			
		}
		
		if(populationService.isDecentralizedProjectedEnabled(facilityId.toString()).equals("Y")){
			if(populationService.getTotalProjectedDecentralizedPopulation(facilityId)<=0){
				errors.add("error.population.projected.decentralized");
				isErr = true;				
			}			
		}
		
		//receiving treatment
		if(populationService.isTreatmentPresentEnabled(facilityId.toString()).equals("Y")){
			if(populationService.getTotalPresentReceivingPopulation(facilityId)<=0){
				errors.add("error.population.present.receivingTreat");
				isErr = true;				
			}			
		}
		
		//receiving treatment
		if(populationService.isTreatmentProjectedEnabled(facilityId.toString()).equals("Y")){
			if(populationService.getTotalProjectedReceivingPopulation(facilityId)<=0){
				errors.add("error.population.projected.receivingTreat");
				isErr = true;				
			}			
		}
		
		//Clustered households
//		Clustered
		int presentResClusteredPopulation = populationService.getPresentResidentialClusteredWastewaterHouses(facilityId) ;
		int projectedResClusteredPopulation = populationService.getProjectedResidentialClusteredWastewaterHouses(facilityId) ;
		int presentNonResClusteredPopulation = populationService.getPresentNonResidentialClusteredWastewaterHouses(facilityId);
		int projectedNonResClusteredPopulation = populationService.getProjectedNonResidentialClusteredWastewaterHouses(facilityId);
		
		int ccClusteredResHouses = populationService.getResidentialNewClusterWastewaterTreatmentSystemHouses(facilityId);
		int ccClusteredNonResHouses = populationService.getNonResidentialNewClusterWastewaterTreatmentSystemHouses(facilityId);
		
		//Resident
		if(projectedResClusteredPopulation>presentResClusteredPopulation){
			if((ccClusteredResHouses > (projectedResClusteredPopulation-presentResClusteredPopulation)) &&
					(facilityCommentsService.findFacilityCommentsByFacilityIdAndDataAreaId(facilityId.longValue(), FacilityService.DATA_AREA_POPULATION)).size()<1){
				errors.add("error.population.res.cluster.houses.invalid");
				isErr=true;
				
			}
		}
		//nonResident 
		if(projectedNonResClusteredPopulation>presentNonResClusteredPopulation){
			if((ccClusteredNonResHouses > (projectedNonResClusteredPopulation-presentNonResClusteredPopulation)) &&
					(facilityCommentsService.findFacilityCommentsByFacilityIdAndDataAreaId(facilityId.longValue(), FacilityService.DATA_AREA_POPULATION)).size()<1){
				errors.add("error.population.nonRes.cluster.houses.invalid");
				isErr=true;		
			}
		}
		
		//On-site
	    //onsite
		int presentResOnsitePopulation = populationService.getPresentResidentialOnsiteWastewaterHouses(facilityId);
		int presentNonResOnsitePopulation = populationService.getPresentNonResidentialOnsiteWastewaterHouses(facilityId);
		int projectedResOnsitePopulation = populationService.getProjectedResidentialOnsiteWastewaterHouses(facilityId);
		int projectedNonResOnsitePopulation = populationService.getProjectedNonResidentialOnsiteWastewaterHouses(facilityId);
	    
		
		int ccOnsiteResHouses = populationService.getResidentialNewIndividualWastewaterTreatmentSystemAllHouses(facilityId)
				+ populationService.getResidentialNewIndividualWastewaterTreatmentSystemConventionalHouses(facilityId)
				+ populationService.getResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(facilityId);

		int ccOnsiteNonResHouses = populationService.getNonResidentialNewIndividualWastewaterTreatmentSystemAllHouses(facilityId)
				+ populationService.getNonResidentialNewIndividualWastewaterTreatmentSystemConventionalHouses(facilityId)
				+ populationService.getNonResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(facilityId);

		//Resident
		if(projectedResOnsitePopulation>presentResOnsitePopulation){
			if((ccOnsiteResHouses > (projectedResOnsitePopulation-presentResOnsitePopulation)) &&
					(facilityCommentsService.findFacilityCommentsByFacilityIdAndDataAreaId(facilityId.longValue(), FacilityService.DATA_AREA_POPULATION)).size()<1){
				errors.add("error.population.res.onsite.houses.invalid");
				isErr=true;
				
			}
		}
		//nonResident 
		if(projectedNonResOnsitePopulation>presentNonResOnsitePopulation){
			if((ccOnsiteNonResHouses > (projectedNonResOnsitePopulation-presentNonResOnsitePopulation)) &&
					(facilityCommentsService.findFacilityCommentsByFacilityIdAndDataAreaId(facilityId.longValue(), FacilityService.DATA_AREA_POPULATION)).size()<1){
				errors.add("error.population.nonRes.onsite.houses.invalid");
				isErr=true;		
			}
		}		
		
		
		

		return isErr;
	}

	public void setFesService(FESService fes) {
		fesService = fes;
	}
	
	protected PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		this.populationService = populationService;
	}
	
	protected FacilityCommentsService facilityCommentsService;
	public void setFacilityCommentsService(
			FacilityCommentsService facilityCommentsService) {
		this.facilityCommentsService = facilityCommentsService;
	}

}
