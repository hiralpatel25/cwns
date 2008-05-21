package gov.epa.owm.mtb.cwns.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.CombinedSewerStatusRef;
import gov.epa.owm.mtb.cwns.model.CsoImperviousness;
import gov.epa.owm.mtb.cwns.service.CSOService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

public class CSOServiceImpl extends CWNSService implements CSOService {
	
	public CombinedSewer getFacilityCSOInfo(Long facilityId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		return (CombinedSewer)searchDAO.getSearchObject(CombinedSewer.class, scs);		
	}
	
	public Collection getCSOStatusReference() {
		return (Collection)searchDAO.getObjects(CombinedSewerStatusRef.class);		
	}
	
	public CombinedSewerStatusRef getCSOStatusReferenceById(String statusId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("combinedSewerStatusId", SearchCondition.OPERATOR_EQ, statusId));
		return (CombinedSewerStatusRef)searchDAO.getSearchObject(CombinedSewerStatusRef.class, scs);		
	}

	public CsoImperviousness getCSOImperviousnessByFacilityId(Long facilityId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		return (CsoImperviousness)searchDAO.getSearchObject(CsoImperviousness.class, scs);
	}
	
	public void saveCSO(CombinedSewer cs) {
		searchDAO.saveObject(cs);		
	}
	
	public int getTotalCSOPopulation(Long facilityId){
		CombinedSewer cs= getFacilityCSOInfo(facilityId);
		int total = 0;
		if(cs!=null){
			if(cs.getDocPopulationCount()!=null){
				total = total + cs.getDocPopulationCount().intValue();
			}
			if(cs.getCcPopulationCount()!=null){
				total = total + cs.getCcPopulationCount().intValue();
			}			
		}
		return total;
	}

	public void costCurveRerun(Long facilityId) {
		Collection sewershedList = populationService.getRelatedSewerShedFacilitiesByDischargeType(facilityId, 
				new Integer(PopulationService.PRESENT_ONLY).intValue());
		ArrayList costCurveIdList = new ArrayList();
		costCurveIdList.add(new Long(7));
		if (sewershedList!=null){
			Iterator iter = sewershedList.iterator();
			while(iter.hasNext()){
				Long facId = (Long)iter.next();
				populationService.setUpCostCurvesForRerun(costCurveIdList, facId);
			}
		}
		
	}	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	
   //  set the population service
    private PopulationService populationService;
    public void setPopulationService(PopulationService ps){
    	populationService = ps;    	
    }  

	
}
