package gov.epa.owm.mtb.cwns.costcurve.input.processor;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.costcurve.input.CombinedSewerSystemInput;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.CsoImperviousness;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.Rainfall;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper;
import gov.epa.owm.mtb.cwns.service.CSOService;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

public class CombinedSewerOverflowCostInputProcessor implements CostCurveInputProcessor { 
	
	public CostCurveInput getCostInputData(Long facilityId){
		CombinedSewerSystemInput ci = new CombinedSewerSystemInput();
				
		ci.setPresentResidentialPopulation(populationService.getPresentResidentialReceivingPopulation(facilityId));
		PopulationHelper upStreamFacilitiesPopulationTotal = populationService.getUpStreamFacilitiesPopulationTotal(facilityId.toString());
		if (upStreamFacilitiesPopulationTotal != null)
			ci.setUpstreamPresentResidentReceivingPopulation(upStreamFacilitiesPopulationTotal.getPresentResPopulation());
		ci.setSumDirectDownstreamFacilitiesPopulationByDischargeType(populationService.getSumDirectDownstreamFacilitiesPopulationByDischargeType(facilityId.toString(), PopulationService.DISCHARGE_TYPE_CSO));		
//		ci.setTotalPresentResidentialReceivingPopulationInSewershed(populationService.getTotalPresentResidentialReceivingPopulationInSewershed(facilityId));
		CombinedSewer combinedSewer = csoService.getFacilityCSOInfo(facilityId);
		Integer ccPopulationCount = combinedSewer == null ? null :	combinedSewer.getCcPopulationCount();
		ci.setCostcurvePopulationCount(ccPopulationCount == null ? 0 : ccPopulationCount.intValue());
		Integer ccAreaSquareMilesMsr = combinedSewer == null ? null :	combinedSewer.getCcAreaSquareMilesMsr();
		ci.setCostcurveAreaSqMilesMsr(ccAreaSquareMilesMsr == null ? 0 : ccAreaSquareMilesMsr.intValue());
		ci.setImperviousRatio(getImperviousRatio(facilityId));
		
		ci.setPresentTotalFlowForFacility(flowService.getPresentTotalFlow(facilityId));
		ci.setPresentTotalFlowForNextDownstreamFacilities(dischargeService.getPresentTotalFlowForNextDownstreamFacilities(facilityId));
		ci.setOnlyFacilityInSewershed(populationService.isOnlyFacilityInSewershed(facilityId.toString()));
		ci.setTerminatingFacility(dischargeService.isTerminatingFacility(facilityId));
		ci.setRainfall(getRainfall(facilityId));
		
		return ci;
	}
	
	public short getImperviousRatio(Long facilityId) {
		short imperviousRatio;
		CsoImperviousness imperviousness = csoService.getCSOImperviousnessByFacilityId(facilityId);
		if (imperviousness != null)
			imperviousRatio = imperviousness.getImperviousRatio();
		else {
			imperviousRatio= -1;
		}					
		return imperviousRatio;
	}
	
	public double getRainfall(Long facilityId) {
		double rain_85_capture = 0.0;
		
		Facility facility = facilityDAO.findByFacilityId(facilityId.toString());
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facility", SearchCondition.OPERATOR_EQ, facility));		
		Rainfall rainfall = (Rainfall) searchDAO.getSearchObject(Rainfall.class, scs);
		
	    if(rainfall != null){
	    	return rainfall.getRain85CptMsr().doubleValue();
	    } else {
	    	String locationId = facility.getLocationId();
	    	StateRef stateRef = facilityDAO.getStateByLocationId(locationId);
	    	scs = new SearchConditions(new SearchCondition("stateRef", SearchCondition.OPERATOR_EQ, stateRef));
	    	rainfall = (Rainfall) searchDAO.getSearchObject(Rainfall.class, scs);
	    	if (rainfall != null)
	    		return rainfall.getRain85CptMsr().doubleValue();
	    	else {
	    		scs = new SearchConditions(new SearchCondition("nationalDefualtFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
	    		rainfall = (Rainfall) searchDAO.getSearchObject(Rainfall.class, scs);
	    		rain_85_capture = rainfall.getRain85CptMsr().doubleValue();
	    	}
	    }
	    
		return rain_85_capture;
	}
		
	
    private PopulationService populationService;
	public void setPopulationService(PopulationService populationService) {
		this.populationService = populationService;
	}

    private DischargeService dischargeService;
	public void setDischargeService(DischargeService fs){
    	dischargeService = fs;
    }	
	
	private FlowService flowService;
    public void setFlowService(FlowService fls){
       flowService = fls;    	
    }  	
    
    private CSOService csoService;
    public void setCsoService(CSOService cso){
       csoService = cso;    	
    }
    
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}    

    private FacilityDAO facilityDAO;
    public void setFacilityDAO(FacilityDAO fdao){
    	facilityDAO = fdao;    	
    }
	
	
}
