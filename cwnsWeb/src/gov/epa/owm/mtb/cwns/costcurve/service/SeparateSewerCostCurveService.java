package gov.epa.owm.mtb.cwns.costcurve.service;

/**
 * Algorythm ONLY Input Data 
 */

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CountyRef;

public class SeparateSewerCostCurveService {
	
	public float getSewerMuliplier(long countyId) {		
		SearchConditions scs = new SearchConditions(new SearchCondition("countyId", SearchCondition.OPERATOR_EQ, new Long(countyId)));		
		CountyRef c = (CountyRef) searchDAO.getSearchObject(CountyRef.class, scs);
	    if(c != null){
	    	return c.getSewerMultiplierRatio().floatValue();
	    }
		return 0;
	}			
	
	public int getCostPopulation(int population) {
		int costPopulation = 0;
		if (population < 500)
			costPopulation = 980;
		else if (population >=  500 && population < 2500)
			costPopulation = 1127; 
		else if (population >=  2500 && population < 5000)
			costPopulation = 1165; 
		else if (population >=  5000 && population < 15000)
			costPopulation = 1583; 

		return costPopulation;
		
	}
	
	public int getModelCoefficient(int population) {
		int modelCoefficient = 0;
		if (population < 500)
			modelCoefficient = 0;
		else if (population >=  500 && population < 2500)
			modelCoefficient = -73510; 
		else if (population >=  2500 && population < 5000)
			modelCoefficient = -167880; 
		else if (population >=  5000 && population < 15000)
			modelCoefficient = -2258987; 
		return modelCoefficient;
		
	}	
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	
	

}
