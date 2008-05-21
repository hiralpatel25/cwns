package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.WastewaterTreatmentPlantInput;
import gov.epa.owm.mtb.cwns.costcurve.service.TreatmentPlantCostCurveService;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.service.EffluentService;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

public class WastewaterTreatmentPlantCostInputProcessor implements CostCurveInputProcessor { 
	
	public CostCurveInput getCostInputData(Long facilityId){
		WastewaterTreatmentPlantInput ci = new WastewaterTreatmentPlantInput();
		
		GeographicAreaCounty gac= facilityAddressService.getPrimaryGeographicAreaCounty(facilityId);
		long countyId = 0;
		if (gac != null) 
			countyId = gac.getCountyRef().getCountyId();	
		
		int projectedResidentialPopulation = populationService.getProjectedResPopulation(facilityId);
		
		ci.setCountyId(countyId);
		ci.setProjectedNonResidentialReceivingPopulation(populationService.getProjectedNonResidentialReceivingPopulation(facilityId));
		ci.setProjectedUpstreamNonResidentialReceivingPopulation(populationService.getProjectedUpstreamNonResidentialReceivingPopuliation(facilityId));
		ci.setProjectedResidentialReceivingPopulation(populationService.getProjectedResidentialReceivingPopulation(facilityId));
		ci.setProjectedUpstreamResidentialReceivingPopulation(populationService.getProjectedUpstreamResidentialReceivingPopuliation(facilityId));		
		ci.setProjectedResidentialPopulation(projectedResidentialPopulation);
		ci.setProjectedPlantType(getProjectedPlantType(facilityId));
		
		int projectedEffunetType = getProjectedEffuentTreatmentLevel(facilityId);
		ci.setProjectedFacilityEffluenType(projectedEffunetType);
		if (projectedEffunetType == TreatmentPlantCostCurveService.EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT) {
			ci.setProjectedFacilityEffluenTypeAdvancedTreatmentSubtype(getProjectedFacilityEffluenTypeAdvancedTreatmentSubtype(facilityId));
		}

		ci.setPresentPlantType(getPresentPlantType(facilityId));	
		ci.setPresentFlowRate(flowService.getMunicipalPresentFlowRate(facilityId));

		int presentEffunetType = getPresentEffuentTreatmentLevel(facilityId);
		ci.setPresentFacilityEffluenType(presentEffunetType);
		if (presentEffunetType == TreatmentPlantCostCurveService.EFFLUENT_TREATMENT_LEVEL_ADVANCED_TREATMENT) {
			ci.setPresentFacilityEffluenTypeAdvancedTreatmentSubtype(getPresentFacilityEffluenTypeAdvancedTreatmentSubtype(facilityId));
		}
		
		//set other parameters
		
		return ci;
	}
	
	public char getProjectedPlantType(Long facilityId) {
		
		Facility c = facilityDAO.findByFacilityId(facilityId.toString());
	    if(c != null){
	    	Character projectedTreatmentPlantType = c.getProjectedTreatmentPlantType();
	    	if (projectedTreatmentPlantType != null)
	    		return projectedTreatmentPlantType.charValue();
	    }
		// TODO decide if nothing is found
	    return TreatmentPlantCostCurveService.PLANT_TYPE_MECHANICAL;
	}

	public char getPresentPlantType(Long facilityId) {
		Facility c = facilityDAO.findByFacilityId(facilityId.toString());
	    if(c != null){
	    	Character presentTreatmentPlantType = c.getPresentTreatmentPlantType();
	    	if (presentTreatmentPlantType != null)
	    		return presentTreatmentPlantType.charValue();
	    }
		// TODO decide if nothing is found
	    return TreatmentPlantCostCurveService.PLANT_TYPE_MECHANICAL;
	}
	
	// TODO: need to integrate Project Advance Treatment Service 
	// At Facility Advanced Treatment Table, if Advanced_Treatment_Type_Id is 2 or 3, 
	// and projected_flag == yes or nutrient removal flag is Y
	public boolean isNutrientRemoval(Long facilityId) {
		return true;  
	}	
		
	public int getProjectedEffuentTreatmentLevel(Long facilityId) {
		return (int)effluentService.getProjectedFacilityEffluentLevel(facilityId);
	}
	
	public int getProjectedFacilityEffluenTypeAdvancedTreatmentSubtype(Long facilityId) {
		return effluentService.getProjectedProjectEffluentAdvancedTypeSubtype(facilityId, "F");
	}
	
	public int getPresentEffuentTreatmentLevel(Long facilityId) {
		return (int)effluentService.getPresentFacilityEffluentLevel(facilityId);
	}

	public int getPresentFacilityEffluenTypeAdvancedTreatmentSubtype(Long facilityId) {
		return effluentService.getPresentProjectEffluentAdvancedTypeSubtype(facilityId, "P");
	}
	
	
    private EffluentService effluentService;
	public void setEffluentService(EffluentService fas) {
		effluentService = fas;
	}
	
    //  set the facility address service
    private FacilityAddressService facilityAddressService;
	public void setFacilityAddressService(FacilityAddressService fas) {
		facilityAddressService = fas;
	}
	
    private PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		this.populationService = populationService;
	}

    private FacilityDAO facilityDAO;
	public void setFacilityDAO(FacilityDAO fsdao){
    	facilityDAO = fsdao;
    }	
	
	private FlowService flowService;
    public void setFlowService(FlowService fls){
       flowService = fls;    	
    }  	
	
}
