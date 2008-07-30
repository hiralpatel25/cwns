package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.model.FacilityDischarge;
import gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public interface PopulationService {
	
	public static final int PRESENT_ONLY = 0;
	public static final int PROJECTED_ONLY = 1;
	public static final int PRESENT_AND_PROJECTED = 2;
	public static final int PRESENT_OR_PROJECTED = 3;
	
	public static final int DISCHARGE_TYPE_CSO  = 10;
	
	public static final int POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION  = 1;
	public static final int POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION  = 2;
		
	public static final int POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER  = 5;
	public static final int POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER   = 6;
	
	public static final int POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER  = 7;
	public static final int POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER   = 8;
	
	public static final int HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL = 11;
	public static final int HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL = 12;
	public static final int HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE = 13;
	public static final int HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE = 14;
	public static final int HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL = 15;
	public static final int HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL = 16;
	public static final int HOUSE_TYPE_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM = 17;
	public static final int HOUSE_TYPE_NON_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM = 18;

	public static final int HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL = 19;
	public static final int HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL = 20;
	public static final int HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE = 21;
	public static final int HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE = 22;
	public static final int HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL = 23;
	public static final int HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL = 24;
	public static final int HOUSE_TYPE_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM = 25;
	public static final int HOUSE_TYPE_NON_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM = 26;	

	public static final int POPULATION_TYPE_NEW_SEPARATE_SEWER_SYSTEM = 27;
	public static final int POPULATION_TYPE_REHAB_SEPARATE_SEWER_SYSTEM = 28;	

	
	public static final int NON_RESIDENTIAL_MULTIPLIER  = 2;
	
	public static final int FACILITY_TYPE_CLUSTERED_SYSTEM = 6;
	public static final int FACILITY_TYPE_ONSITE_WASTE_WATER_TREATMENT_SYSTEM = 5;
	
	public Collection getPopulationRefs(String number);
	
	public Collection getPopulationComments(String facNum);

	public boolean isOnlyFacilityInSewershed(String facilityId);
	
	public String isRecCollectionPresentEnabled(String facNum);

	public String isTreatmentPresentEnabled(String facNum);
	
	public String isTreatmentProjectedEnabled(String facNum);
	
	public String isRecCollectionProjectedEnabled(String facNum);

	public String isDecentralizedPresentEnabled(String facNum);

	public String isDecentralizedProjectedEnabled(String facNum);

	public String isISDSPresentEnabled(String facNum);

	public String isISDSProjectedEnabled(String facNum);
	
	public String isRecCollectionPresentResEnabled(String facNum);

	public HashMap getPresentPopulationByPopIdAndFacId(String facNum, String popId1, String popId2);
	
	public HashMap getProjectedPopulationByPopIdAndFacId(String facNum, String popId1, String popId2);
	
	public void savePopulationInfo(String sFacilityId, String user);

	/**
	 * @param facilityId the id string of the facility of interest
	 * @return an ArrayList object containing a list of unique facility IDs of all upstream facilities
	 *         if no such facilities exist, the function returns null 
	 */	
	public Collection getUpstreamFacilityIds(String facilityId);

	public Collection getUpstreamFacilitiesListByDischargeType(String facilityId, 
			int dischargeType);
	
	public void deletePopulationInfoIfExists(String facilityId, Long populationId, String userId);

	public void saveOrUpdatePopulationInfo(String facilityId, Object[] values, Long populationId, String userId);

	public void saveOrUpdateSmallCommunityFlag(String facNum, Character smallCommuFlag, String userId);
		
	public Collection getUpstreamFacilitiesForDisplay(String facilityId);
	
	/**
	 * @param facilityId the id string of the facility of interest
	 * @return a PopulationHelper object containing the four types of total population of all upstream facilities
	 *         if no upstream facilities exist, the function returns null 
	 */	
	public PopulationHelper getUpStreamFacilitiesPopulationTotal(String facilityId);

	/**
	 * @param facilityId the id string of the facility of interest
	 * @return an ArrayList object containing a list of unique facility IDs of all facilities in the sewershed
	 *         if no other facilities exist in the shewshed, the function returns a list containing the 
	 *         input facility ID
	 */		
	public Collection getRelatedSewerShedFacilities(String facilityId);

	/**
	 * Given a Long object that represents a facilityId return a Collection of 
	 * Long objects that represent the related sewer shed facilities. 
	 * @param facilityId
	 * @return
	 */
	public Collection getRelatedSewerShedFacilities(Long facilityId);
	
    public int getResidentialNewIndividualWastewaterTreatmentSystemAllHouses(Long facilityId);
    
    public float getResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemAll(Long facilityId);  
    
    public int getProjectedResidentialNewIndividualWastewaterTreatmentSystemAllHouses(Long facilityId);

    public int getNonResidentialNewIndividualWastewaterTreatmentSystemAllHouses(Long facilityId);
    
    public float getNonResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemAll(Long facilityId);
    
    public int getProjectedNonResidentialNewIndividualWastewaterTreatmentSystemAllHouses(Long facilityId);

    public int getResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(Long facilityId);

    public int getNonResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(Long facilityId);
    
    public int getResidentialNewIndividualWastewaterTreatmentSystemConventionalHouses(Long facilityId);

    public int getNonResidentialNewIndividualWastewaterTreatmentSystemConventionalHouses(Long facilityId);
    
    public int getResidentialNewClusterWastewaterTreatmentSystemHouses(Long facilityId);
    
    public int getProjectedResidentialNewClusterWastewaterTreatmentSystemHouses(Long facilityId);

    public int getNonResidentialNewClusterWastewaterTreatmentSystemHouses(Long facilityId);
    
    public int getProjectedNonResidentialNewClusterWastewaterTreatmentSystemHouses(Long facilityId);
    
    public int getResidentialRehabIndividualWastewaterTreatmentSystemAllHouses(Long facilityId);

    public int getNonResidentialRehabIndividualWastewaterTreatmentSystemAllHouses(Long facilityId);    
	
    public int getResidentialRehabIndividualWastewaterTreatmentSystemInnovativeHouses(Long facilityId);

    public int getNonResidentialRehabIndividualWastewaterTreatmentSystemInnovativeHouses(Long facilityId);    

    public int getResidentialRehabIndividualWastewaterTreatmentSystemConventionalHouses(Long facilityId);

    public int getResidentialRehabClusterWastewaterTreatmentSystemHouses(Long facilityId);

    public int getNonResidentialRehabClusterWastewaterTreatmentSystemHouses(Long facilityId);    
    
    public int getNonResidentialRehabIndividualWastewaterTreatmentSystemConventionalHouses(Long facilityId);    
    
    public int getPresentNewSeparatSewerSystemPopulation(Long facilityId); 
    
    public int getPresentRehabSeparateSewerSystemPopulation(Long facilityId);    
    
    public int getTotalPresentResidentialReceivingPopulationInSewershed(Long facilityId);	
	
	public int getPresentResidentialReceivingPopulation(Long facilityId);	
	
    /**
     * pop =
     * 	facility’s future residential receiving collection population
     * 	+ facility’s future residential individual sewage disposal system population
     * 	+ facility’s future residential decentralized system population
     * 	+ all upstream facilities’ future residential receiving collection population
     * 
     * @param facilityId
     * @return
     */
    public int getProjectedResPopulation(Long facilityId);
    
    public int getProjectedResidentialReceivingPopulation(Long facilityId);
    
    public int getProjectedNonResidentialReceivingPopulation(Long facilityId);  
    
    public int getProjectedUpstreamNonResidentialReceivingPopuliation(Long facilityId);
    
    public int getProjectedUpstreamResidentialReceivingPopuliation(Long facilityId);
	
	public int getFutureTotalResAndNonResPopulation(Long facilityId);
	
	public double getTotalProjectedReceivingPopulation (Long facilityId);
	
	public double getTotalPresentReceivingPopulation (Long facilityId);
	
	public Collection getPopulationByFacilityId(Long facilityId);
	
	
   /**
     * Total present receiving collection population
     * @param facilityId
     * @return population
     */	
    public int getTotalPresentReceivingCollectionPopulation(Long facilityId);

    /**
     * Total projected receiving collection population
     * @param facilityId
     * @return population
     */	
    public int getTotalProjectedReceivingCollectionPopulation(Long facilityId);
    
    /**
     * Total present individual sewage disposal system population
     * @param facilityId
     * @return population
     */	
    public int getTotalPresentIndividualSewageDisposalSystemPopulation(Long facilityId);
    
    /**
     * Total projected individual sewage disposal system population
     * @param facilityId
     * @return population
     */	    
    public int getTotalProjectedIndividualSewageDisposalSystemPopulation(Long facilityId);
    
    /**
     * Total present Decentralized Population
     * @param facilityId
     * @return population
     */	    
    public int getTotalPresentDecentralizedPopulation(Long facilityId);
    
    /**
     * Total projected Decentralized Population
     * @param facilityId
     * @return population
     */    
    public int getTotalProjectedDecentralizedPopulation(Long facilityId);
    
    /**
     * Residential present clusted Population
     * @param facilityId
     * @return population
     */
    public int getPresentResDecentralizedPopulation(Long facilityId);
    
    /**
     * Non-Residential present clusted Population
     * @param facilityId
     * @return population
     */    
    public int getPresentNonResDecentralizedPopulation(Long facilityId);
    
    /**
     * Residential projected clusted Population
     * @param facilityId
     * @return population
     */    
    public int getProjectedResDecentralizedPopulation(Long facilityId);

    /**
     * Non-Residential projected clusted Population
     * @param facilityId
     * @return population
     */        
    public int getProjectedNonResDecentralizedPopulation(Long facilityId);
    
    //onsite systems
    public int getPresentResIndividualSewageDisposalSystemPopulation(Long facilityId);
    public int getPresentNonResIndividualSewageDisposalSystemPopulation(Long facilityId);
    public int getProjectedResIndividualSewageDisposalSystemPopulation(Long facilityId);   
    public int getProjectedNonResIndividualSewageDisposalSystemPopulation(Long facilityId);
    
    //houses
    //Clustered
    public int getPresentResidentialClusteredWastewaterHouses(Long facilityId) ;
    public int getPresentNonResidentialClusteredWastewaterHouses(Long facilityId) ;
    public int getProjectedResidentialClusteredWastewaterHouses(Long facilityId);
    public int getProjectedNonResidentialClusteredWastewaterHouses(Long facilityId);
        
    //onsite
    public int getPresentResidentialOnsiteWastewaterHouses(Long facilityId);
    public int getPresentNonResidentialOnsiteWastewaterHouses(Long facilityId);
    public int getProjectedResidentialOnsiteWastewaterHouses(Long facilityId);
    public int getProjectedNonResidentialOnsiteWastewaterHouses(Long facilityId);
       
    public Collection getRelatedSewerShedFacilitiesLocations(String facilityId);
    
    public float getResidentialPopulationPerHouse(Long facilityId);
    public float getNonResidentialPopulationPerHouse(Long facilityId);
    
    public void savePopulationUnitsByPopIdAndFacId(String facNum, String popId, Object[] values, String userId);

	public void deleteFacilityHouseIfExists(String facNum, Long popId);
	
	public void setUpCostCurvesForRerun(ArrayList costCurveList, Long facilityId);
	
	public String isClusteredSystemExists(String facNum);
	
	public String isOWTSystemExists(String facNum);
	
	public long getSumDirectDownstreamFacilitiesPopulationByDischargeType(String facilityId, int dischargeType);
	
	public Collection getDownstreamFacilitiesListByDischargeType(String facilityId, 
			int dischargeType);
	
	public Collection getRelatedSewerShedFacilitiesByDischargeType(Long facilityId, int dischargeType);
	
	public void updatePopulationPerUnits(String facNum, int[] popIds, float populationPerUnit, String userId);

	public void updateFacilityPopulationCount(Long facilityId, ArrayList popIds, String userId);

	public float getResidentialOWTSPopulationPerHouse(Long facilityId);

	public float getNonResidentialOWTSPopulationPerHouse(Long facilityId);
	
	public boolean isFlowApportionmentCompleted(String facNum);
	
	public boolean isOutOfState(String dischargingFacilityId, String currentFacilityId);

	public float getNonResidentialPopulationPerHouseholdRehabClusterWastewaterTreatmentSystem(Long facilityId);

	public float getResidentialPopulationPerHouseholdRehabClusterWastewaterTreatmentSystem(Long facilityId);

	public float getNonResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemConventional(Long facilityId);

	public float getResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemConventional(Long facilityId);

	public float getNonResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemInnovative(Long facilityId);

	public float getResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemInnovative(Long facilityId);

	public float getNonResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemAll(Long facilityId);

	public float getResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemAll(Long facilityId);

	public float getNonResidentialPopulationPerHouseholdNewClustereWastewaterTreatmentSystem(Long facilityId);

	public float getResidentialPopulationPerHouseholdNewClusterWastewaterTreatmentSystem(Long facilityId);

	public float getNonResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemConventional(Long facilityId);

	public float getResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemConventional(Long facilityId);

	public float getNonResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemInnovative(Long facilityId);

	public float getResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemInnovative(Long facilityId); 
	
	public Collection getRelatedSewerShedFacilitiesForDisplay(Long facilityId, int dischargeType);
}