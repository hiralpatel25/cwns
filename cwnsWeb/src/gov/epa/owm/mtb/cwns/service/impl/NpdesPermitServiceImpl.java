package gov.epa.owm.mtb.cwns.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.EfDmrMeasurement;
import gov.epa.owm.mtb.cwns.model.EfEffluentLimit;
import gov.epa.owm.mtb.cwns.model.EfEffluentLimitQty;
import gov.epa.owm.mtb.cwns.model.EfPermit;
import gov.epa.owm.mtb.cwns.model.EfPipeSched;
import gov.epa.owm.mtb.cwns.model.FacilityPermit;
import gov.epa.owm.mtb.cwns.service.NpdesPermitService;

public class NpdesPermitServiceImpl extends CWNSService implements
		NpdesPermitService {
	
	private SearchDAO searchDAO;
	
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	
	public Collection getEfPipeScheds(Long facilityId){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("permit", "p", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("p.efPermit", "ef", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ef.efPipeScheds", "pipe", AliasCriteria.JOIN_INNER));
		ArrayList columns = new ArrayList();
		columns.add("pipe.id.efDischargeNum");
		columns.add("pipe.id.efReportDesig");
		columns.add("pipe.pipeDesc");
		SortCriteria sortCriteria = new SortCriteria("pipe.id.efDischargeNum", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria); 
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("usedForFacilityLocatnFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		Collection efPipeScheds = searchDAO.getSearchList(FacilityPermit.class, columns, scs, sortArray, aliasArray);
		return processEntitiesNameValue(efPipeScheds);
	}
	
	private Collection processEntitiesNameValue(Collection results) {
		Collection col = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			String arg1 = (String)obj[0]+","+(String)obj[1];
			String arg2 = (String)obj[0]+"-";
			if (obj[2]==null)
				arg2 = arg2+"";
			else
				arg2 = arg2+(String)obj[2];
			col.add(new Entity(arg1,arg2));
		}	
		return col;
	}    
	
	public Collection displayNpdesFlowData(String efNpdesPermitNumber, String efDischargeNum, String efReportDesig,
            Date startDate, Date endDate){
        //System.out.println("efNpdesPermitNumber--"+efNpdesPermitNumber+"efDischargeNum--"+efDischargeNum+"efReportDesig"+efReportDesig);
        ArrayList paramCode = new ArrayList();
        paramCode.add("50050");
        paramCode.add("82220");
        paramCode.add("00056");
        
		ArrayList dates = new ArrayList();
        dates.add(startDate);
        dates.add(endDate);
        
        SearchConditions scs = new SearchConditions(new SearchCondition("id.efNpdesPermitNumber", SearchCondition.OPERATOR_EQ, efNpdesPermitNumber.trim()));
        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.efDischargeNum", SearchCondition.OPERATOR_EQ, efDischargeNum.trim()));
        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.efReportDesig", SearchCondition.OPERATOR_EQ, efReportDesig.trim()));
        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.efPipeSetQualifier", SearchCondition.OPERATOR_EQ, new Character('9')));
        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.efParamCode", SearchCondition.OPERATOR_IN, paramCode));
        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.monitoringPeriodEndDate", SearchCondition.OPERATOR_BETWEEN, dates));
        Collection flowData = searchDAO.getSearchList(EfDmrMeasurement.class, scs);
        return flowData;
    }

	public String getEfNpdesPermitNbr(Long facilityId) {
		String efNpdesPermitNbr = "";
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("usedForFacilityLocatnFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		FacilityPermit facilityPermit = (FacilityPermit)searchDAO.getSearchObject(FacilityPermit.class, scs);
		if (facilityPermit != null){
			if (facilityPermit.getPermit() != null)
				if (facilityPermit.getPermit().getEfPermit() != null)
			      efNpdesPermitNbr = facilityPermit.getPermit().getEfPermit().getEfNpdesPermitNumber();
		}	
		return efNpdesPermitNbr;
	}
	
	public float getPermitDesignFlowRate(String efNpdesPermitNbr){
		float flowRate = 0;
		ArrayList columns = new ArrayList();
		columns.add("presentDesignFlowRate");
		SearchConditions scs = new SearchConditions(new SearchCondition("efNpdesPermitNumber", SearchCondition.OPERATOR_EQ, efNpdesPermitNbr));
		Collection c = searchDAO.getSearchList(EfPermit.class, columns, scs);
		if (!c.isEmpty())
			flowRate = ((BigDecimal)c.iterator().next())!=null?((BigDecimal)c.iterator().next()).floatValue():0;
		return flowRate;
	}

	public boolean IsUnitOfMeasureSame(Collection npdesFlowData) {
		boolean isSame = true;
		Iterator iter = npdesFlowData.iterator();
		EfDmrMeasurement efm = (EfDmrMeasurement)iter.next();
		String unitCode1 = nullToEmpty(efm.getConcentrationUnitCode()).trim();
		while (iter.hasNext()){
			efm = (EfDmrMeasurement)iter.next();
			String unitCode2 = nullToEmpty(efm.getConcentrationUnitCode()).trim();
			if(!unitCode2.equalsIgnoreCase(unitCode1))
			  return false;
		}
		return isSame;
	}
	
	private String nullToEmpty(Object obj){
    	if (obj == null)
    	   return "";
    	else
    	   return (String)obj;	
    }
	

}
