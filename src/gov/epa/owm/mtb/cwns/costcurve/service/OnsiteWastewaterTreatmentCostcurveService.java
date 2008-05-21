package gov.epa.owm.mtb.cwns.costcurve.service;

/**
 * Algorythm ONLY Input Data 
 */

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CountyRef;

public class OnsiteWastewaterTreatmentCostcurveService {
	

	public float getResidentialPopulationPerHouse(long countyId) {
		
		SearchConditions scs = new SearchConditions(new SearchCondition("countyId", SearchCondition.OPERATOR_EQ, new Long(countyId)));		
		CountyRef c = (CountyRef) searchDAO.getSearchObject(CountyRef.class, scs);
	    if(c != null){
	    	return c.getResPopulationPerUnit().floatValue();
	    }
		return 0;
	}

	public float getNonResidentialPopulationPerHouse(long countyId) {
		
		SearchConditions scs = new SearchConditions(new SearchCondition("countyId", SearchCondition.OPERATOR_EQ, new Long(countyId)));		
		CountyRef c = (CountyRef) searchDAO.getSearchObject(CountyRef.class, scs);
	    if(c != null){
	    	return c.getNonresPopulationPerUnit().floatValue();
	    }
		return 0;
	}
	
	public float getOnsiteMuliplier(long countyId) {
		
		SearchConditions scs = new SearchConditions(new SearchCondition("countyId", SearchCondition.OPERATOR_EQ, new Long(countyId)));		
		CountyRef c = (CountyRef) searchDAO.getSearchObject(CountyRef.class, scs);
	    if(c != null){
	    	return c.getOnsiteMultiplierRatio().floatValue();
	    }
		return 0;
	}			
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	
	

}
