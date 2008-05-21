package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.PopulationDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.CountyRef;
import gov.epa.owm.mtb.cwns.model.CwnsInfoRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityDischarge;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityPopulation;
import gov.epa.owm.mtb.cwns.model.FacilityPopulationId;
import gov.epa.owm.mtb.cwns.model.FacilityPopulationUnit;
import gov.epa.owm.mtb.cwns.model.FacilityPopulationUnitId;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatusId;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.model.PopulationRef;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.populationInformation.DischargeReceiveHelper;
import gov.epa.owm.mtb.cwns.populationInformation.FacilityPopulationInformation;
import gov.epa.owm.mtb.cwns.populationInformation.PopulationHelper;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PopulationServiceImpl extends CWNSService implements PopulationService {
	
	private PopulationDAO populationDAO;
	private SearchDAO searchDAO;
	//private FacilityService facilityService;
	
	public void setPopulationDAO(PopulationDAO dao){
		populationDAO = dao;
	}
	
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	/*	    
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }*/

	/**
	 * Return a list of user PopulationRef objects with populationId < parameter populationId
	 */
	public Collection getPopulationRefs(String populationId)
	{
		SortCriteria sortCriteria = new SortCriteria("name",
				SortCriteria.ORDER_DECENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs = new SearchConditions(new SearchCondition("populationId",SearchCondition.OPERATOR_LT,new Long(populationId)));
		Collection populationRefs = searchDAO.getSearchList(PopulationRef.class,new ArrayList(), scs, sortArray);
		return populationRefs;
	}
	
	public Collection getPopulationComments(String facNum) {
		return populationDAO.getPopulationComments(facNum);
	}

	public void saveOrUpdateSmallCommunityFlag(String facNum, Character smallCommuFlag, String userId)
	{
		Facility f = (Facility) searchDAO.getObject(Facility.class, new Long(facNum));
		
		if(f == null || f.getSmallCommunityExceptionFlag() == smallCommuFlag.charValue())
			return;
		
		f.setSmallCommunityExceptionFlag(smallCommuFlag.charValue());
		
		searchDAO.saveObject(f);

		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, new Long(1)));
		FacilityEntryStatus fesObj = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs);

		if(fesObj == null){
			//throw serious error
			return;
		}
		else
		{
			fesObj.setLastUpdateUserid(userId);
			fesObj.setLastUpdateTs(new Date());
			searchDAO.saveObject(fesObj);					
		}

	}
	
	public void saveOrUpdatePopulationInfo(String facilityId, Object[] values, Long populationId, String userId)
	{
        SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));

        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, populationId));

        FacilityPopulation facilityPopulation = (FacilityPopulation)searchDAO.getSearchObject(FacilityPopulation.class, scs);

        if (facilityPopulation!=null){

              facilityPopulation.setPresentPopulationCount((Integer)(values[0]));

              facilityPopulation.setProjectedPopulationCount((Integer)(values[1]));

              if(values[2] == null || ((Short)values[2]).shortValue() == 0)
            	  facilityPopulation.setProjectionYear(null);
              else
            	  facilityPopulation.setProjectionYear((Short)(values[2]));

              facilityPopulation.setLastUpdateTs(new Date());

              facilityPopulation.setLastUpdateUserid(userId);

              searchDAO.saveObject(facilityPopulation);
        }
        else {

              facilityPopulation = new FacilityPopulation();

              FacilityPopulationId id = new FacilityPopulationId(new Long(facilityId).longValue(), populationId.longValue());

              facilityPopulation.setId(id);

              facilityPopulation.setPresentPopulationCount((Integer)(values[0]));

              facilityPopulation.setProjectedPopulationCount((Integer)(values[1]));

              if(values[2] == null || ((Short)values[2]).shortValue() == 0)
            	  facilityPopulation.setProjectionYear(null);
              else
            	  facilityPopulation.setProjectionYear((Short)(values[2]));

              facilityPopulation.setLastUpdateTs(new Date());

              facilityPopulation.setLastUpdateUserid(userId);

              searchDAO.saveObject(facilityPopulation);
        }
        
        //savePopulationInfo(facilityId, userId);
        
	}
	
	public void deletePopulationInfoIfExists(String facilityId, Long populationId, String userId)
	{
        SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));

        scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, populationId));

        FacilityPopulation facilityPopulation = (FacilityPopulation)searchDAO.getSearchObject(FacilityPopulation.class, scs);

        if (facilityPopulation != null){
           searchDAO.removeObject(facilityPopulation);
        }		
        
        //savePopulationInfo(facilityId, userId);
	}
	
	public String isRecCollectionPresentResEnabled(String facNum) //sewershed check
	{
		ArrayList sewerShedFacilities = (ArrayList) getRelatedSewerShedFacilities(facNum);

		Iterator iter0 = sewerShedFacilities.iterator();
		while (iter0.hasNext()){
			String facId = (String)iter0.next();

			ArrayList columns = new ArrayList();
			columns.add("assignedOrRunCode");

			AliasCriteria alias1 = new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER);
			AliasCriteria alias2 = new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER);
			ArrayList aliasArray = new ArrayList();
			
			aliasArray.add(alias1);
			aliasArray.add(alias2);
			
			SearchConditions scs1 =  new SearchConditions(new SearchCondition("costCurveRef.costCurveId", SearchCondition.OPERATOR_EQ, new Long(7)));

			scs1.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("assignedOrRunCode", SearchCondition.OPERATOR_LIKE, "Y"));
			scs1.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facId)));

			SearchConditions  scs2 = new SearchConditions(new SearchCondition("f.reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_LIKE, "FA"));
			scs2.setCondition(SearchCondition.OPERATOR_OR,new SearchCondition("f.reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_LIKE,"FRR")); 
			scs2.setCondition(SearchCondition.OPERATOR_OR,new SearchCondition("f.reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_LIKE,"FRC")); 
			
			scs1.setCondition(SearchCondition.OPERATOR_AND,scs2);

			log.debug("ready to check sewer: " + facId);		
			
			Collection results= searchDAO.getSearchList(FacilityCostCurve.class, columns, scs1, new ArrayList(), aliasArray);

			if(results.iterator().hasNext())
			   return "N";
			
			log.debug("check sewer list: " + facId + " is clean...");
			
		}

		log.debug("all sewer for " + facNum + " is not clean, returning N");
		
		return "Y";
	}

	
	public String isRecCollectionPresentEnabled(String facNum) {
		ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.popRecCollectionFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("presentFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}

	public String isRecCollectionProjectedEnabled(String facNum) {
		ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.popRecCollectionFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("projectedFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}
	
	public String isTreatmentPresentEnabled(String facNum) {
		ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.popTreatmentFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("presentFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}
	
	public String isTreatmentProjectedEnabled(String facNum) {
		ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.popTreatmentFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("projectedFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}

	public String isDecentralizedPresentEnabled(String facNum) {
		ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.popClusteredFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("presentFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);				
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}

	public String isDecentralizedProjectedEnabled(String facNum) {
		ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.popClusteredFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("projectedFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}

	public String isISDSPresentEnabled(String facNum) {
		ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.popOwtsFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("presentFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);		
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
		

		
	}

	public String isISDSProjectedEnabled(String facNum) {
		ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.popOwtsFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("projectedFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}
	
	public HashMap getPresentPopulationByPopIdAndFacId(String facNum, String popId1, String popId2) {
		HashMap map = null;
		ArrayList columns = new ArrayList();
		columns.add("populationRef.populationId");
		columns.add("presentPopulationCount");
		SearchConditions scs = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popId1)));
		if(popId2!=null){
			scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popId2)));			
		}
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs1.setCondition(SearchCondition.OPERATOR_AND, scs);
		Collection results = searchDAO.getSearchList(FacilityPopulation.class, columns, scs1);
		Iterator iter = results.iterator();
		map = new HashMap();
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			map.put(((Long)obj[0]).toString(), obj[1]==null?new Integer(0):obj[1]);
			
			log.debug(" getPresentPopulationByPopIdAndFacId: " + ((Long)obj[0]).toString() + "-" + obj[1] + "\r\n");			
		}		
		
		return map;
	}

	private int getPresentHouseCountByPopIdAndFacId(String facNum, String popId1) {
		//SearchConditions scs = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popId1)));
		//SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		//scs1.setCondition(SearchCondition.OPERATOR_AND, scs);
		
		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
				new FacilityPopulationUnitId(Long.parseLong(facNum), Long.parseLong(popId1))));		
		FacilityPopulationUnit result = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs);				
		return result == null ? 0 : ((result.getPresentUnitsCount()==null)?0: result.getPresentUnitsCount().intValue());
	}
	
	private int getProjectedHouseCountByPopIdAndFacId(String facNum, String popId1) {
		//SearchConditions scs = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popId1)));
		//SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		//scs1.setCondition(SearchCondition.OPERATOR_AND, scs);
		
		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
								new FacilityPopulationUnitId(Long.parseLong(facNum), Long.parseLong(popId1))));
		FacilityPopulationUnit result = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs);
		return result == null ? 0 : ((result.getProjectedUnitsCount()==null)?0: result.getProjectedUnitsCount().intValue());
	}
	
	public float getPresentPopulationPerUnitByPopIdAndFacId(String facNum, String popId1) {
		//SearchConditions scs = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popId1)));
		//SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		//scs1.setCondition(SearchCondition.OPERATOR_AND, scs);	
		//FacilityPopulationUnit result = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs1);
		
		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
				new FacilityPopulationUnitId(Long.parseLong(facNum), Long.parseLong(popId1))));
		FacilityPopulationUnit result = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs);
		return result == null ? 0 : ((result.getPopulationPerUnit()==null)?0: result.getPopulationPerUnit().floatValue());
	}	
	
	
	public HashMap getProjectedPopulationByPopIdAndFacId(String facNum, String popId1, String popId2) {
		HashMap map = null;
		ArrayList columns = new ArrayList();
		columns.add("populationRef.populationId");
		columns.add("projectedPopulationCount");
		columns.add("projectionYear");
		SearchConditions scs = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popId1)));
		if (popId2 != null) {
			scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popId2)));
		}	
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs1.setCondition(SearchCondition.OPERATOR_AND, scs);
		Collection results = searchDAO.getSearchList(FacilityPopulation.class, columns, scs1);
		Iterator iter = results.iterator();
		map = new HashMap();
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			Object[] o = new Object[2];
			o[0] = obj[1]==null?new Integer(0):obj[1];
			o[1] = obj[2]==null?new Short("0"):obj[2];
			map.put(((Long)obj[0]).toString(), o);

			log.debug(" getProjectedPopulationByPopIdAndFacId: " + ((Long)obj[0]).toString() + ":" + o[0] + "-" + o[1]+ "\r\n");			
			
		}		
		
		return map;
	}

	public void getAllUpStreamFacilitiesAsCollection(FacilityPopulationInformation fpi, Collection facilities){
        Collection upfacs = fpi.getUpStreamFacilites();
        if(upfacs.size()>0){
            facilities.add(getPopulationHelpers(fpi,upfacs));
            for (Iterator iter = upfacs.iterator(); iter.hasNext();) {
             	 FacilityPopulationInformation cph = (FacilityPopulationInformation) iter.next();
                 getAllUpStreamFacilitiesAsCollection(cph, facilities);
            }                    
        }                 
   }
		
	private Collection getPopulationHelpers(FacilityPopulationInformation fpi, Collection upfacs) {
		ArrayList arr = new ArrayList();
		for (Iterator iter = upfacs.iterator(); iter.hasNext();) {
			FacilityPopulationInformation cfpi = (FacilityPopulationInformation) iter.next();
			PopulationHelper ph = new PopulationHelper();
			ph.setDischargingFacilityId(cfpi.getFacilityId());
			ph.setDisFacilityCwnsNbr(cfpi.getFacilityCwnsNbr());
			ph.setDisFacilityName(cfpi.getFacilityName());
			ph.setRecFacilityName(fpi.getFacilityName());
			ph.setRecFacilityCwnsNbr(fpi.getFacilityCwnsNbr());
			//ph.setPresentNonResPopulation(cfpi.getPresentNonResPopulation());
			//ph.setPresentResPopulation(cfpi.getPresentResPopulation());
			//ph.setProjectedNonResPopulation(cfpi.getProjectedNonResPopulation());
			//ph.setProjectedResPopulation(cfpi.getProjectedResPopulation());
			arr.add(ph);
		}		
		return arr;
	}
/*
	public void setUpStreamFacilities(FacilityPopulationInformation ph){
		Collection upStreamFacilities = getUpstreamFacilities(ph.getFacilityId());
		if(upStreamFacilities.size()>0){
			ph.setUpStreamFacilites(upStreamFacilities);
			for (Iterator iter = upStreamFacilities.iterator(); iter.hasNext();) {
				FacilityPopulationInformation cph = (FacilityPopulationInformation) iter.next();
				setUpStreamFacilities(cph);
			}
		}
	}
*/
	private FacilityPopulationInformation getFPByFacilityID(String facilityId, Collection dischargingFPList)
	{
		if(dischargingFPList == null || facilityId ==null)
			return null;

		//log.debug(" getFPByFacilityID: facilityId-dischargingFPList: " + facilityId + "-" + dischargingFPList.size() + "\r\n");
		
		Iterator iter = dischargingFPList.iterator();
		while (iter.hasNext()){
			FacilityPopulationInformation fp = (FacilityPopulationInformation)iter.next();

			log.debug(" getFPByFacilityID: fp.getFacilityId(): " + (fp==null?"null":fp.getFacilityId()) + "\r\n");			
			
			if(facilityId.equals(fp.getFacilityId()))
				return fp;
		}
		return null;
	}

	private DischargeReceiveHelper lookUpExistingDischargeReceiveHelper(String dischargeFacilityId, 
			String receiveFacilityId, 
			Collection disRcvHelperList)
	{

		if(disRcvHelperList == null || dischargeFacilityId == null || receiveFacilityId == null)
			return null;

		Iterator iter = disRcvHelperList.iterator();
		while (iter.hasNext()){
			DischargeReceiveHelper drh = (DischargeReceiveHelper)iter.next();

			if(drh.getDischargingFacility()!=null && drh.getDischargingFacility().getFacilityId().equals(dischargeFacilityId) &&
					drh.getReceivingFacility()!=null && drh.getReceivingFacility().getFacilityId().equals(receiveFacilityId))
			{
				return drh;
			}
		}
		
		return null;
	}
	
	public Collection getUpstreamFacilitiesForDisplay(String facilityId)
	{
		ArrayList dischargeReceiveHelperList = new ArrayList();
		getUpstreamFacilities(facilityId, new ArrayList(), dischargeReceiveHelperList);
		return dischargeReceiveHelperList;
	}
	
	public Collection getUpstreamFacilityIds(String facilityId)
	{
		ArrayList dischargeReceiveHelperList = new ArrayList();
		getUpstreamFacilities(facilityId, new ArrayList(), dischargeReceiveHelperList);
		
		ArrayList upstreamFacilities = null;
		
		if(dischargeReceiveHelperList!=null && dischargeReceiveHelperList.size() > 0)
		{			
			upstreamFacilities = new ArrayList();
	
			Iterator iter = dischargeReceiveHelperList.iterator();
			while (iter.hasNext()){
				DischargeReceiveHelper o = (DischargeReceiveHelper)iter.next();
				String facId = o.getDischargingFacility().getFacilityId();
				
				if(!upstreamFacilities.contains(facId))
					upstreamFacilities.add(facId);
			}
		}
		
		return upstreamFacilities;
	}

	public PopulationHelper getUpStreamFacilitiesPopulationTotal(String facilityId)
	{
		ArrayList dischargeReceiveHelperList = new ArrayList();		
		getUpstreamFacilities(facilityId, new ArrayList(), dischargeReceiveHelperList);

		PopulationHelper ph = null;
		
		if(dischargeReceiveHelperList!=null && dischargeReceiveHelperList.size() > 0)
		{	
			ph = new PopulationHelper();
			
			Iterator iter = dischargeReceiveHelperList.iterator();
			while (iter.hasNext()){
				
				DischargeReceiveHelper o = (DischargeReceiveHelper)iter.next();
				
				ph.setPresentResPopulation(ph.getPresentResPopulation() + 
						      (int)(0.5+o.getDischargingFacility().getPresentResPopulationPersant()*
						      o.getDischargingFacility().getActualPresentResPopulation()));

				ph.setPresentNonResPopulation(ph.getPresentNonResPopulation() + 
					      (int)(0.5+o.getDischargingFacility().getPresentNonResPopulationPersant()*
					      o.getDischargingFacility().getActualPresentNonResPopulation()));

				ph.setProjectedResPopulation(ph.getProjectedResPopulation() + 
					      (int)(0.5+o.getDischargingFacility().getProjectedResPopulationPersant()*
					      o.getDischargingFacility().getActualProjectedResPopulation()));

				ph.setProjectedNonResPopulation(ph.getProjectedNonResPopulation() + 
					      (int)(0.5+o.getDischargingFacility().getProjectedNonResPopulationPersant()*
					      o.getDischargingFacility().getActualProjectedNonResPopulation()));

			}
		}
		
		return ph;
	}

	public Collection getUpstreamFacilitiesListByDischargeType(String facilityId, 
			int dischargeType){
		
		ArrayList returnList = new ArrayList();
		
		getUpstreamFacilitiesByDischargeType(facilityId, dischargeType, returnList);
		
		return returnList;
	}
	
	private void getUpstreamFacilitiesByDischargeType(String facilityId, 
			int dischargeType,
			ArrayList resultList){
		
		ArrayList columns = new ArrayList();
		columns.add("facilityByFacilityId.facilityId");
		columns.add("presentFlag"); 
		columns.add("projectedFlag");
		columns.add("presentFlowPortionPersent");
		columns.add("projectedFlowPortionPersent");
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
				  SearchCondition.OPERATOR_EQ, new Long(facilityId)));

		SearchConditions scs0 = new SearchConditions();

		if(dischargeType == PopulationService.PRESENT_ONLY)
		{
			scs0.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PROJECTED_ONLY)
		{
			scs0.setCondition(new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PRESENT_AND_PROJECTED)
		{
			scs0.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
			scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PRESENT_OR_PROJECTED)
		{
			scs0.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
			scs0.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}

		scs.setCondition(SearchCondition.OPERATOR_AND, scs0);

		Collection results = searchDAO.getSearchList(FacilityDischarge.class, columns, scs);
		Iterator iter = results.iterator();
		while (iter.hasNext()){
			Object[] o = (Object[])iter.next();
			String tmpFacId = ((Long)o[0]).toString();

			//Pre-Order Traverse
			log.debug(" getUpstreamFacilitiesByDischargeType:visiting" + tmpFacId + "-" + facilityId + "\r\n");
			
			resultList.add(tmpFacId);
			
			getUpstreamFacilitiesByDischargeType(tmpFacId, dischargeType, resultList);
		}
	}	
	
	public long getSumDirectDownstreamFacilitiesPopulationByDischargeType(String facilityId, 
			int dischargeType){
		long sum = 0;
		List list = getDirectDownstreamFacilitiesListByDischargeType(facilityId, dischargeType);

		for (int i = 0; i < list.size(); i++) {
			FacilityDischarge fd = (FacilityDischarge)list.get(i);
			long disChargeFacilityId = fd.getFacilityByFacilityIdDischargeTo().getFacilityId();
			sum = sum + getPresentResidentialReceivingPopulation(new Long(disChargeFacilityId));
			
		}
		return sum;
	}

	public List getDirectDownstreamFacilitiesListByDischargeType(String facilityId, 
			int dischargeType){
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityByFacilityId.facilityId", 
				SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(new SearchCondition("dischargeMethodRef.dischargeMethodId", 
				SearchCondition.OPERATOR_EQ, new Long(dischargeType)));
		List results = searchDAO.getSearchList(FacilityDischarge.class, scs);		
		return results;
	}
	
	public Collection getDownstreamFacilitiesListByDischargeType(String facilityId,	int dischargeType){
		
		ArrayList returnList = new ArrayList();
		
		getDownstreamFacilitiesByDischargeType(facilityId, dischargeType, returnList);
		
		return returnList;
	}	
	
	
	private void getDownstreamFacilitiesByDischargeType(String facilityId, 
			int dischargeType,
			ArrayList resultList){
		
		ArrayList columns = new ArrayList();
		columns.add("facilityByFacilityIdDischargeTo.facilityId");
		columns.add("presentFlag"); 
		columns.add("projectedFlag");
		columns.add("presentFlowPortionPersent");
		columns.add("projectedFlowPortionPersent");
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityByFacilityId.facilityId", 
				SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		
		SearchConditions scs0 = new SearchConditions();

		if(dischargeType == PopulationService.PRESENT_ONLY)
		{
			scs0.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PROJECTED_ONLY)
		{
			scs0.setCondition(new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PRESENT_AND_PROJECTED)
		{
			scs0.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
			scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PRESENT_OR_PROJECTED)
		{
			scs0.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
			scs0.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}

		scs.setCondition(SearchCondition.OPERATOR_AND, scs0);

		Collection results = searchDAO.getSearchList(FacilityDischarge.class, columns, scs);
		Iterator iter = results.iterator();
		while (iter.hasNext()){
			Object[] o = (Object[])iter.next();
			if (o[0]!=null){
			String tmpFacId = ((Long)o[0]).toString();
			//Pre-Order Traverse
			log.debug(" getDownstreamFacilitiesByDischargeType:visiting" + tmpFacId + "-" + facilityId + "\r\n");
			
			resultList.add(tmpFacId);
			
			getDownstreamFacilitiesByDischargeType(tmpFacId, dischargeType, resultList);
			}
		}
	}	
	
	private void getUpstreamFacilities(String facilityId, 
			Collection dischargingFPList, 
			Collection dischargeReceiveHelperList){
		
		ArrayList columns = new ArrayList();
		columns.add("facilityByFacilityId.facilityId");
		columns.add("presentFlag"); 
		columns.add("projectedFlag");
		columns.add("presentFlowPortionPersent");
		columns.add("projectedFlowPortionPersent");
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		Collection results = searchDAO.getSearchList(FacilityDischarge.class, columns, scs);
		Iterator iter = results.iterator();
		while (iter.hasNext()){
			Object[] o = (Object[])iter.next();
			String tmpFacId = ((Long)o[0]).toString();

			//Pre-Order Traverse
			log.debug(" getUpstreamFacilities:visiting" + tmpFacId + "-" + facilityId + "\r\n");

   		    FacilityPopulationInformation DisschargingFP = getFPByFacilityID(tmpFacId, dischargingFPList);
		    FacilityPopulationInformation receivingFP = getFPByFacilityID(facilityId, dischargingFPList);

				if(DisschargingFP == null)
				{
					DisschargingFP = new FacilityPopulationInformation();
					
					DisschargingFP.setFacilityId(tmpFacId);
				    Facility disFacility = (Facility)searchDAO.getObject(Facility.class, Long.valueOf(tmpFacId));
					log.debug(" getUpstreamFacilities: " + "DisschargingFP Name: " + disFacility.getName() + "\r\n");			    
					DisschargingFP.setFacilityCwnsNbr(disFacility.getCwnsNbr());
					DisschargingFP.setFacilityName(disFacility.getName());
					DisschargingFP.setUpStreamFacilites(new ArrayList());
				    dischargingFPList.add(DisschargingFP);					
					log.debug(" getUpstreamFacilities: " + "Created new DisschargingFP " + tmpFacId + "\r\n");
				}
	
				if(receivingFP == null)
				{
					receivingFP = new FacilityPopulationInformation();
				    Facility rcvFacility = (Facility)searchDAO.getObject(Facility.class, Long.valueOf(facilityId));
				    receivingFP.setFacilityId(facilityId);
				    receivingFP.setFacilityCwnsNbr(rcvFacility.getCwnsNbr());
				    receivingFP.setFacilityName(rcvFacility.getName());
				    receivingFP.setPresentResPopulationPersant(1.0);
				    receivingFP.setPresentNonResPopulationPersant(1.0);
				    receivingFP.setProjectedResPopulationPersant(1.0);
				    receivingFP.setProjectedNonResPopulationPersant(1.0);				    
				    receivingFP.setUpStreamFacilites(new ArrayList());
					log.debug(" getUpstreamFacilities: " + "Created new receivingFP " + facilityId + "\r\n");			    
				}

				double presentResPopulationPersant=0.0,presentNonResPopulationPersant=0.0;
				
				DischargeReceiveHelper existingDrh = lookUpExistingDischargeReceiveHelper(tmpFacId, facilityId, dischargeReceiveHelperList);
				
				if(existingDrh != null)
				{
					//reset the percentage because the receiving facility already account for the aggregation
					DisschargingFP.setPresentNonResPopulationPersant(0.0);
					DisschargingFP.setPresentResPopulationPersant(0.0);
					DisschargingFP.setProjectedNonResPopulationPersant(0.0);
					DisschargingFP.setProjectedResPopulationPersant(0.0);					
				}

				log.debug(" getUpstreamFacilities: " + "o[1]:o[3] " + ((Character)o[1]) + ":" + ((Short)o[3]) + "\r\n");

				double pathTerminalPresentResPopulationPersant=0.0,pathTerminalPresentNonResPopulationPersant=0.0;
				double pathTerminalProjectedResPopulationPersant=0.0,pathTerminalProjectedNonResPopulationPersant=0.0;
				
				int presentResPopulation=0,presentNonResPopulation=0;
				if (((Character)o[1]).compareTo(new Character('Y'))==0){
					HashMap m = getPresentPopulationByPopIdAndFacId(tmpFacId, "1", "2");				
					if (m != null){
						if (m.containsKey("1"))
						{
							presentResPopulationPersant = ((Short)o[3]).doubleValue()/100.00;
							presentResPopulation = ((Integer)m.get("1")).intValue();
	}
						if (m.containsKey("2"))
						{
						   presentNonResPopulationPersant = ((Short)o[3]).doubleValue()/100.00;
						   presentNonResPopulation = ((Integer)m.get("2")).intValue();
						}
					}
				}

				log.debug(" getUpstreamFacilities: **PresentRes** " + DisschargingFP.getPresentResPopulationPersant() + "-" +
						presentResPopulationPersant + "-" +
						receivingFP.getPresentResPopulationPersant() + "\r\n");
				
				log.debug(" getUpstreamFacilities: " + "DisschargingFP-getPresentNonResPopulationPersant-" + DisschargingFP.getPresentNonResPopulationPersant() + "\r\n");						

				pathTerminalPresentResPopulationPersant=presentResPopulationPersant*receivingFP.getPresentResPopulationPersant();
				pathTerminalPresentNonResPopulationPersant=presentNonResPopulationPersant*receivingFP.getPresentNonResPopulationPersant();

				DisschargingFP.setPresentResPopulationPersant(DisschargingFP.getPresentResPopulationPersant() + pathTerminalPresentResPopulationPersant);
				DisschargingFP.setPresentNonResPopulationPersant(DisschargingFP.getPresentNonResPopulationPersant() + pathTerminalPresentNonResPopulationPersant);
				
				DisschargingFP.setActualPresentResPopulation(presentResPopulation);
				DisschargingFP.setActualPresentNonResPopulation(presentNonResPopulation);
	
				log.debug(" getUpstreamFacilities: " + "o[2]:o[4] " + ((Character)o[2]) + ":" + ((Short)o[4]) + "\r\n");

				double projectedResPopulationPersant=0.0,projectedNonResPopulationPersant=0.0;
				int projectedResPopulation=0,projectedNonResPopulation=0;
				if (((Character)o[2]).compareTo(new Character('Y'))==0){
					HashMap m = getProjectedPopulationByPopIdAndFacId(tmpFacId, "1", "2");
					if (m != null){
						if (m.containsKey("1")){
							
							projectedResPopulationPersant = ((Short)o[4]).doubleValue()/100.00;
							Object[] obj = (Object[])m.get("1");
							projectedResPopulation = ((Integer)obj[0]).intValue();
						}	
						if (m.containsKey("2")){
							
							projectedNonResPopulationPersant = ((Short)o[4]).doubleValue()/100.00;
							Object[] obj = (Object[])m.get("2");
							projectedNonResPopulation = ((Integer)obj[0]).intValue();
						}	
					}
				}
				
				pathTerminalProjectedResPopulationPersant=projectedResPopulationPersant*receivingFP.getProjectedResPopulationPersant();
				pathTerminalProjectedNonResPopulationPersant=projectedNonResPopulationPersant*receivingFP.getProjectedNonResPopulationPersant();
				DisschargingFP.setProjectedResPopulationPersant(DisschargingFP.getProjectedResPopulationPersant() + pathTerminalProjectedResPopulationPersant);
				DisschargingFP.setProjectedNonResPopulationPersant(DisschargingFP.getProjectedNonResPopulationPersant() + pathTerminalProjectedNonResPopulationPersant);
				log.debug(" getUpstreamFacilities: " + "DisschargingFP-projectedResPopulationPersant-" + projectedResPopulationPersant + "\r\n");
				log.debug(" getUpstreamFacilities: " + "DisschargingFP-projectedNonResPopulationPersant-" + projectedNonResPopulationPersant + "\r\n");						

				DisschargingFP.setActualProjectedResPopulation((projectedResPopulation));
				DisschargingFP.setActualProjectedNonResPopulation((projectedNonResPopulation));

				log.debug(" getUpstreamFacilities: " + "DisschargingFP-ActualpresentResPopulation-" + DisschargingFP.getActualPresentResPopulation() + "\r\n");
				log.debug(" getUpstreamFacilities: " + "DisschargingFP-ActualpresentNonResPopulation-" + DisschargingFP.getActualPresentNonResPopulation() + "\r\n");		
				log.debug(" getUpstreamFacilities: " + "DisschargingFP-ActualprojectedResPopulation-" + DisschargingFP.getActualProjectedResPopulation() + "\r\n");
				log.debug(" getUpstreamFacilities: " + "DisschargingFP-ActualprojectedNonResPopulation-" + DisschargingFP.getActualProjectedNonResPopulation() + "\r\n");						
						
			    if(existingDrh == null)		    
				{
				    receivingFP.getUpStreamFacilites().add(DisschargingFP);
				    DischargeReceiveHelper drHelper = new DischargeReceiveHelper();
				    drHelper.setDischargingFacility(DisschargingFP);
				    drHelper.setReceivingFacility(receivingFP);
				    drHelper.setPresentResPopulationPersant(pathTerminalPresentResPopulationPersant);
				    drHelper.setPresentNonResPopulationPersant(pathTerminalPresentNonResPopulationPersant);
				    drHelper.setProjectedResPopulationPersant(pathTerminalProjectedResPopulationPersant);
				    drHelper.setProjectedNonResPopulationPersant(pathTerminalProjectedNonResPopulationPersant);
				    drHelper.setCombinedSewer(hasCombinedSewer(tmpFacId)?"Y":"N");
				    drHelper.setOutOfState(isOutOfState(tmpFacId, facilityId)?"Y":"N");
				    
					log.debug(" getUpstreamFacilities: " + "drHelper-setPresentResPopulationPersant-" + pathTerminalPresentResPopulationPersant + "\r\n");
					log.debug(" getUpstreamFacilities: " + "drHelper-setPresentNonResPopulationPersant-" + pathTerminalPresentNonResPopulationPersant + "\r\n");		
					log.debug(" getUpstreamFacilities: " + "drHelper-setProjectedResPopulationPersant-" + pathTerminalProjectedResPopulationPersant + "\r\n");
					log.debug(" getUpstreamFacilities: " + "drHelper-setProjectedNonResPopulationPersant-" + pathTerminalProjectedNonResPopulationPersant + "\r\n");						
				    dischargeReceiveHelperList.add(drHelper);
				}
			    else
			    {
			    	existingDrh.setPresentResPopulationPersant(pathTerminalPresentResPopulationPersant);
			    	existingDrh.setPresentNonResPopulationPersant(pathTerminalPresentNonResPopulationPersant);
			    	existingDrh.setProjectedResPopulationPersant(pathTerminalProjectedResPopulationPersant);
			    	existingDrh.setProjectedNonResPopulationPersant(pathTerminalProjectedNonResPopulationPersant);	
					log.debug(" getUpstreamFacilities: " + "existingDrh-setPresentResPopulationPersant-" + pathTerminalPresentResPopulationPersant + "\r\n");
					log.debug(" getUpstreamFacilities: " + "existingDrh-setPresentNonResPopulationPersant-" + pathTerminalPresentNonResPopulationPersant + "\r\n");		
					log.debug(" getUpstreamFacilities: " + "existingDrh-setProjectedResPopulationPersant-" + pathTerminalProjectedResPopulationPersant + "\r\n");
					log.debug(" getUpstreamFacilities: " + "existingDrh-setProjectedNonResPopulationPersant-" + pathTerminalProjectedNonResPopulationPersant + "\r\n");									    	
			    }
				
				log.debug(" getUpstreamFacilities: " + "**********Done With This Unit**********\r\n");
	
			getUpstreamFacilities(tmpFacId, dischargingFPList, dischargeReceiveHelperList);
			
		} //while
	}
	
	private boolean hasCombinedSewer(String facilityId)
	{
		ArrayList columns = new ArrayList();
		columns.add("docPopulationCount");
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		Collection results = searchDAO.getSearchList(CombinedSewer.class, columns, scs1);
		Iterator iter = results.iterator();
		if(iter.hasNext()){
			return true;
		}
		return false;
	}
	
	public boolean isOutOfState(String dischargingFacilityId, String currentFacilityId)
	{

		Facility dischargingFacility = (Facility)searchDAO.getObject(Facility.class, new Long(dischargingFacilityId));
		Facility currentFacility = (Facility)searchDAO.getObject(Facility.class, new Long(currentFacilityId));
		
		if(dischargingFacility!=null && currentFacilityId != null 
				&& dischargingFacility.getLocationId().equalsIgnoreCase(currentFacility.getLocationId()))
			return false;
		return true;
	}
	
	public boolean isOnlyFacilityInSewershed(String facilityId) {
		ArrayList facilites = (ArrayList)getRelatedSewerShedFacilities(facilityId);
		return facilites.size() == 1 ? true : false;		
	}
	
	/**
	 * Given a Long object that represents a facilityId and discharge type return a Collection of 
	 * Long objects that represent the related sewer shed facilities. 
	 * @param facilityId
	 * @param dischargeType
	 * @return
	 */
	public Collection getRelatedSewerShedFacilitiesByDischargeType(Long facilityId, int dischargeType) {
		Collection relatedSewerShedFacilities = new ArrayList();
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		Collection facIds = getRelatedSewerShedFacilitiesByDischargeType(facilityId.toString(), dischargeType, f.getVersionCode());
		Iterator iter = facIds.iterator();
		while (iter.hasNext()) {
			String facId = (String) iter.next(); 
			Long sewerShedFacility = new Long(facId);
			relatedSewerShedFacilities.add(sewerShedFacility);
		}
		return relatedSewerShedFacilities;
	}
	
	public Collection getRelatedSewerShedFacilitiesByDischargeType(String facilityId, int dischargeType, char versionCode)
	{
		ArrayList tmpArrayList = new ArrayList();
		
		tmpArrayList.add(facilityId);
		
		return getRelatedSewerShedFacilitiesByDischargeType(facilityId, new ArrayList(), tmpArrayList, dischargeType, versionCode);
	}
	
	private Collection getRelatedSewerShedFacilitiesByDischargeType(String facilityId, Collection excludingList, 
			Collection relatedSewerShedFacilityList, int dischargeType, char versionCode)
	{
		
		log.debug(" getRelatedSewerShedFacilities: enter....  " + facilityId + "\r\n");
		
		ArrayList columns = new ArrayList();
		columns.add("facilityByFacilityId.facilityId");
		columns.add("facilityByFacilityIdDischargeTo.facilityId");	
		
		SearchConditions scs0 = null;
		
		Iterator excludedIter = excludingList.iterator();
		while (excludedIter.hasNext()){
			String excludedFacilityId = (String)excludedIter.next();
			
			log.debug(" getRelatedSewerShedFacilities: now excluding " + excludedFacilityId + "\r\n");

			if(scs0 == null)
			{
				scs0 = new SearchConditions();
			}
			
			
			
			scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
					SearchCondition.OPERATOR_NOT_EQ, new Long(excludedFacilityId)));
			scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facilityByFacilityId.facilityId", 
					SearchCondition.OPERATOR_NOT_EQ, new Long(excludedFacilityId)));
		}
		
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
																SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs1.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("facilityByFacilityId.facilityId", 
				SearchCondition.OPERATOR_EQ, new Long(facilityId)));

		SearchConditions scs2 = new SearchConditions(new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
				SearchCondition.OPERATOR_IS_NOT_NULL, null));	
		
		scs2.setCondition(SearchCondition.OPERATOR_AND, scs1);

		if(scs0!=null)
		{
			scs2.setCondition(SearchCondition.OPERATOR_AND, scs0);
		}
		SearchConditions scs3 = new SearchConditions();
		
		if(dischargeType == PopulationService.PRESENT_ONLY)
		{
			scs3.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PROJECTED_ONLY)
		{
			scs3.setCondition(new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PRESENT_AND_PROJECTED)
		{
			scs3.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
			scs3.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		
		scs2.setCondition(SearchCondition.OPERATOR_AND, scs3);  
		
		log.debug(" getRelatedSewerShedFacilities: ready to searchDAO.getSearchList" + "\r\n");
	
		Collection results = searchDAO.getSearchList(FacilityDischarge.class, columns, scs2);
		
		Iterator iter = results.iterator();
		while (iter.hasNext()){
			Object[] o = (Object[])iter.next();
			String upstreamFacId = ((Long)o[0]).toString();
			String downstreamFacId = ((Long)o[1]).toString();
			
			String newNodeFacilityId = upstreamFacId.equals(facilityId)?downstreamFacId:upstreamFacId;
			

			
			if(!excludingList.contains(facilityId))
			      excludingList.add(facilityId);
			
			getRelatedSewerShedFacilitiesByDischargeType(newNodeFacilityId, excludingList, relatedSewerShedFacilityList,dischargeType,versionCode);
			//Post-Order Traverse
			log.debug(" getRelatedSewerShedFacilities:visiting --> " + newNodeFacilityId + "\r\n");
			
			if(!relatedSewerShedFacilityList.contains(newNodeFacilityId))
				relatedSewerShedFacilityList.add(newNodeFacilityId);
						
		}

		return relatedSewerShedFacilityList;
	}
		
	/**
	 * Given a Long object that represents a facilityId return a Collection of 
	 * Long objects that represent the related sewer shed facilities. 
	 * @param facilityId
	 * @return
	 */
	public Collection getRelatedSewerShedFacilities(Long facilityId) {
		Collection relatedSewerShedFacilities = new ArrayList();
		
		Collection facIds = getRelatedSewerShedFacilities(facilityId.toString());
		Iterator iter = facIds.iterator();
		while (iter.hasNext()) {
			String facId = (String) iter.next(); 
			Long sewerShedFacility = new Long(facId);
			relatedSewerShedFacilities.add(sewerShedFacility);
		}
		return relatedSewerShedFacilities;
	}
	
	public Collection getRelatedSewerShedFacilitiesLocations(String facilityId){
		
		Collection facIds =  getRelatedSewerShedFacilities(facilityId);
		
		if(facIds.size()>999){
			log.error("sewershed with more than 999 facilites encountered");
			return null;
		}
		if(facIds.isEmpty())return null;
		
		Collection facilityIds = new ArrayList();	
		for (Iterator iter = facIds.iterator(); iter.hasNext();) {
			String facIdStr = (String) iter.next();
			facilityIds.add(new Long(facIdStr));
		}
			
		ArrayList columns = new ArrayList();
		columns.add("locationId");
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityId",SearchCondition.OPERATOR_IN, facilityIds));
		Collection results= searchDAO.getSearchList(Facility.class, columns, scs);
		
		Set locSet = new HashSet();
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			//Object[] o = (Object[])iterator.next();
			//String facLocationId = ((String)o[0]);
			String facLocationId = (String)iterator.next();
			locSet.add(facLocationId);
		}		
		return locSet;		
	}
	
	public Collection getRelatedSewerShedFacilities(String facilityId)
	{
		ArrayList tmpArrayList = new ArrayList();
		
		tmpArrayList.add(facilityId);
		
		return getRelatedSewerShedFacilities(facilityId, new ArrayList(), tmpArrayList);
	}
	
	private Collection getRelatedSewerShedFacilities(String facilityId, Collection excludingList, 
			Collection relatedSewerShedFacilityList)
	{
		
		log.debug(" getRelatedSewerShedFacilities: enter....  " + facilityId + "\r\n");
		
		ArrayList columns = new ArrayList();
		columns.add("facilityByFacilityId.facilityId");
		columns.add("facilityByFacilityIdDischargeTo.facilityId");	
		
		SearchConditions scs0 = null;
		
		Iterator excludedIter = excludingList.iterator();
		while (excludedIter.hasNext()){
			String excludedFacilityId = (String)excludedIter.next();
			
			log.debug(" getRelatedSewerShedFacilities: now excluding " + excludedFacilityId + "\r\n");

			if(scs0 == null)
			{
				scs0 = new SearchConditions();
			}
			scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
					SearchCondition.OPERATOR_NOT_EQ, new Long(excludedFacilityId)));
			scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facilityByFacilityId.facilityId", 
					SearchCondition.OPERATOR_NOT_EQ, new Long(excludedFacilityId)));
		}
		
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
																SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs1.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("facilityByFacilityId.facilityId", 
				SearchCondition.OPERATOR_EQ, new Long(facilityId)));

		SearchConditions scs2 = new SearchConditions(new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
				SearchCondition.OPERATOR_IS_NOT_NULL, null));	
		
		scs2.setCondition(SearchCondition.OPERATOR_AND, scs1);

		if(scs0!=null)
		{
			scs2.setCondition(SearchCondition.OPERATOR_AND, scs0);
		}

		log.debug(" getRelatedSewerShedFacilities: ready to searchDAO.getSearchList" + "\r\n");
	
		Collection results = searchDAO.getSearchList(FacilityDischarge.class, columns, scs2);
		Iterator iter = results.iterator();
		while (iter.hasNext()){
			Object[] o = (Object[])iter.next();
			String upstreamFacId = ((Long)o[0]).toString();
			String downstreamFacId = ((Long)o[1]).toString();
			
			String newNodeFacilityId = upstreamFacId.equals(facilityId)?downstreamFacId:upstreamFacId;
			
			if(!excludingList.contains(facilityId))
			      excludingList.add(facilityId);
			
			getRelatedSewerShedFacilities(newNodeFacilityId, excludingList, relatedSewerShedFacilityList);

			//Post-Order Traverse
			log.debug(" getRelatedSewerShedFacilities:visiting --> " + newNodeFacilityId + "\r\n");
			
			if(!relatedSewerShedFacilityList.contains(newNodeFacilityId))
				relatedSewerShedFacilityList.add(newNodeFacilityId);
		}

		return relatedSewerShedFacilityList;
	}
		
    public void savePopulationInfo(String sFacilityId, String user) {
		
		SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(sFacilityId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ,new Long(5)));
		FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
		
		if(facilityEntryStatus != null)
		{
			facilityEntryStatus.setLastUpdateTs(new Date());
			searchDAO.saveObject(facilityEntryStatus);
		}
		else
		{
			//throw serious error
		}
		
		Facility facility = (Facility)searchDAO.getObject(Facility.class, new Long(sFacilityId));
		
		if (facility != null){
		     if ("SAS".equalsIgnoreCase(facility.getReviewStatusRef().getReviewStatusId())){
		    	 FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
		    	 FacilityReviewStatusId id = new FacilityReviewStatusId(new Long(sFacilityId).longValue(), "SIP", new Date());
		    	 facilityreviewstatus.setId(id);
		    	 facilityreviewstatus.setLastUpdateUserid(user);
		    	 searchDAO.saveObject(facilityreviewstatus);
		    	 
		        ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
		        reviewStatusRef.setReviewStatusId("SIP");
		        facility.setReviewStatusRef(reviewStatusRef);
		     } 
		  
		  facility.setLastUpdateTs(new Date());
	      facility.setLastUpdateUserid(user);
		  searchDAO.saveObject(facility);  
		}
		
    }
    
    public int getFutureTotalResAndNonResPopulation(Long facilityId){
		HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"", 
				PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"");
		Object[] o = (Object[])m.get(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"");
		Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");
		Object[] no = (Object[])m.get(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"");
		Integer pop_non_res = (no!=null)?(Integer)no[0]: new Integer("0");
		int total = pop_res.intValue() +  pop_non_res.intValue();		
		return total;
    }
 
    public int getTotalPresentResidentialReceivingPopulationInSewershed(Long facilityId) {
		ArrayList sewerShedFacilities = (ArrayList) getRelatedSewerShedFacilities(facilityId.toString());
		Iterator iter0 = sewerShedFacilities.iterator();
		int total = 0;
		while (iter0.hasNext()){
			String facId = (String)iter0.next();
			total =  total + getPresentResidentialReceivingPopulation(new Long(facId));
		}
		return total;
    }   
    
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
    public int getProjectedResPopulation(Long facilityId) {
    	int total = 0; 
		total = getProjectedResidentialReceivingPopulation(facilityId)
		    + getProjectedResIndividualSewageDisposalSystemPopulation(facilityId)
			+ getProjectedResDecentralizedPopulation(facilityId)
			+ getProjectedUpstreamResidentialReceivingPopuliation(facilityId);   
    	return total;
    }
    
    public int getProjectedUpstreamNonResidentialReceivingPopuliation(Long facilityId) {
    	PopulationHelper ph = getUpStreamFacilitiesPopulationTotal(facilityId.toString());
    	return ph == null ? 0 : ph.getProjectedNonResPopulation();
    }
    
    public int getProjectedUpstreamResidentialReceivingPopuliation(Long facilityId) {
    	PopulationHelper ph = getUpStreamFacilitiesPopulationTotal(facilityId.toString());
    	return ph == null ? 0 : ph.getProjectedResPopulation();
    }
 
    //**********************Receiving Population*************************************
    //Resident-Present
    public int getPresentResidentialReceivingPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"", null);
		Object object = m.get(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"");
		if (object instanceof Integer)
			return ((Integer)object).intValue();
		else {
			Object[] o = (Object[])object;
			Integer pop_res_receiving = (o!=null)?(Integer)o[0]: new Integer("0");    	
			return pop_res_receiving.intValue();
		}	
    }  
    
    //Non Resident-Present
    public int getPresentNonResidentialReceivingPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION), null);
		Object[] o = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION));
		Integer pop_res_receiving = (o!=null)?(Integer)o[0]: new Integer("0");
    	
    	return pop_res_receiving.intValue();
    }    
    
    //Resident-Projected
    public int getProjectedResidentialReceivingPopulation(Long facilityId) {
    	HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"", null);
		Object[] o = (Object[])m.get(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"");
		Integer pop_res_receiving = (o!=null)?(Integer)o[0]: new Integer("0");   	
    	return pop_res_receiving.intValue();
    }
    
    //Non Resident-Projected
    public int getProjectedNonResidentialReceivingPopulation(Long facilityId) {
    	HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION), null);
		Object[] o = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION));
		Integer pop_res_receiving = (o!=null)?(Integer)o[0]: new Integer("0");
    	
    	return pop_res_receiving.intValue();
    }
    
    //  Total Present
    public int getTotalPresentReceivingCollectionPopulation(Long facilityId){
       	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"", PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"");	
       	Integer pop_res_receiving = (Integer)m.get(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"");
       	Integer pop_nonres_receiving = (Integer)m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION));
    	return (pop_res_receiving==null?0:pop_res_receiving.intValue()) + (pop_nonres_receiving==null?0:pop_nonres_receiving.intValue());
    }

    //  Total Projected
    public int getTotalProjectedReceivingCollectionPopulation(Long facilityId){
       	HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"", PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"");	
       	Object[] o = (Object[])m.get(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"");
		Integer pop_res_receiving = (o!=null)?(Integer)o[0]: new Integer("0");
		Object[] o2 = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION));
		Integer pop_nonres_receiving = (o2!=null)?(Integer)o2[0]: new Integer("0");		
    	return pop_res_receiving.intValue() + pop_nonres_receiving.intValue();
    }
    

    //**********************************Individual Sewage Disposal System Population*******************
    //  Residential -Present
    public int getPresentResIndividualSewageDisposalSystemPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER), null);
    	Integer pop_res = (Integer)m.get(String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER));  	
    	return pop_res==null?0:pop_res.intValue();
    }
    
    //  Non-Residential -Present
    public int getPresentNonResIndividualSewageDisposalSystemPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER), null);
    	Integer pop_res = (Integer)m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER));   	
    	return pop_res==null?0:pop_res.intValue();
    }     
    
    //  Residential -Projected
    public int getProjectedResIndividualSewageDisposalSystemPopulation(Long facilityId) {
    	HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER), null);
		Object[] o = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
		Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");    	
    	return pop_res.intValue();
    }   
    
    //  Non-Residential -Projected
    public int getProjectedNonResIndividualSewageDisposalSystemPopulation(Long facilityId) {
    	HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER), PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER+"");
		Object[] o = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
		Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");    	
    	return pop_res.intValue();
    }

    //  total present
    public int getTotalPresentIndividualSewageDisposalSystemPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER),PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER+"");
    	Integer pop_res =  (Integer)m.get(String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
    	Integer pop_non_res = (Integer)m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER)); 	
    	return (pop_res==null?0:pop_res.intValue()) + (pop_non_res==null?0:pop_non_res.intValue());
    }
    
    //total projected
    public int getTotalProjectedIndividualSewageDisposalSystemPopulation(Long facilityId) {
    	HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER), null);
		Object[] o = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
		Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");    	    	
		Object[] o2 = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
		Integer pop_non_res = (o2!=null)?(Integer)o2[0]: new Integer("0");    	
    	return pop_res.intValue() +pop_non_res.intValue();
    }

    
    
   //Clustered
    public int getPresentResidentialClusteredWastewaterHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
    }

    public int getPresentNonResidentialClusteredWastewaterHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER));
    } 

    public int getProjectedResidentialClusteredWastewaterHouses(Long facilityId) {
    	return getProjectedHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
    }

    public int getProjectedNonResidentialClusteredWastewaterHouses(Long facilityId) {
    	return getProjectedHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER));
    }
    
    //onsite
    public int getPresentResidentialOnsiteWastewaterHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
    }

    public int getPresentNonResidentialOnsiteWastewaterHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
    } 

    public int getProjectedResidentialOnsiteWastewaterHouses(Long facilityId) {
    	return getProjectedHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
    }

    public int getProjectedNonResidentialOnsiteWastewaterHouses(Long facilityId) {
    	return getProjectedHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_INDIVIDUAL_WASTEWATER));
    }

    /* -- Calculated X1: Residential Number of Unit
     * 				 Y1: Residential Population per household
     * 				 X2: Non-residential Number of Unit
     * 				 Y2: Non-residentail Population per household 
     * ------------- */ 
    
    // New Onsite Wastewater Treatment Systems -ALL
    public int getResidentialNewIndividualWastewaterTreatmentSystemAllHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }
    
    public float getResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemAll(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }    
    
    public int getProjectedResidentialNewIndividualWastewaterTreatmentSystemAllHouses(Long facilityId) {
    	return getProjectedHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }    

    public int getNonResidentialNewIndividualWastewaterTreatmentSystemAllHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }
    
    public float getNonResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemAll(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }    
    
    public int getProjectedNonResidentialNewIndividualWastewaterTreatmentSystemAllHouses(Long facilityId){
    	return getProjectedHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }

    // New Onsite Wastewater Treatment Systems - Innovative
    public int getResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
    }

    public float getResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemInnovative(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
    }        
    
    public int getNonResidentialNewIndividualWastewaterTreatmentSystemInnovativeHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
    }
    
    public float getNonResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemInnovative(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
    }    
    
    
    // New Onsite Wastewater Treatment Systems - Conventional    
    public int getResidentialNewIndividualWastewaterTreatmentSystemConventionalHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
    }
    
    public float getResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemConventional(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
    }            

    public int getNonResidentialNewIndividualWastewaterTreatmentSystemConventionalHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
    }
    
    public float getNonResidentialPopulationPerHouseholdNewOnsiteWastewaterTreatmentSystemConventional(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
    }        
    
    // New Cluster Wastewater Treatment Systems    
    public int getResidentialNewClusterWastewaterTreatmentSystemHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
    }
    
    public float getResidentialPopulationPerHouseholdNewClusterWastewaterTreatmentSystem(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
    }                
    
    public int getProjectedResidentialNewClusterWastewaterTreatmentSystemHouses(Long facilityId){
    	return getProjectedHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));    	
    }

    public int getNonResidentialNewClusterWastewaterTreatmentSystemHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
    }
    
    public int getProjectedNonResidentialNewClusterWastewaterTreatmentSystemHouses(Long facilityId){
    	return getProjectedHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));    	
    }
    
    public float getNonResidentialPopulationPerHouseholdNewClustereWastewaterTreatmentSystem(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_NEW_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
    }            

    // Rehab Wastewater Treatment Systems ALL
    public int getResidentialRehabIndividualWastewaterTreatmentSystemAllHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }

    public float getResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemAll(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }                    
    
    public int getNonResidentialRehabIndividualWastewaterTreatmentSystemAllHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }
    
    public float getNonResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemAll(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_ALL));
    }            
    
    
    // Rehab Onsite Wastewater Treatment Sytem - Innovative
    public int getResidentialRehabIndividualWastewaterTreatmentSystemInnovativeHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
    }

    public float getResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemInnovative(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
    }                        
    
    public int getNonResidentialRehabIndividualWastewaterTreatmentSystemInnovativeHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
    }        

    public float getNonResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemInnovative(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_INNOVATIVE));
    }            
    
    
    // Rehab Onsite Wastewater Treatment Sytem - Conventional    
    public int getResidentialRehabIndividualWastewaterTreatmentSystemConventionalHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
    }

    public float getResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemConventional(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
    }                            
    
    public int getNonResidentialRehabIndividualWastewaterTreatmentSystemConventionalHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
    }        

    public float getNonResidentialPopulationPerHouseholdRehabOnsiteWastewaterTreatmentSystemConventional(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_INDIVIDUAL_WASTEWATER_TREATMENT_SYSTEM_CONVENTIONAL));
    }            
    
    
    // Rehab Cluster Wastewater Treatment Sytem    
    public int getResidentialRehabClusterWastewaterTreatmentSystemHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
    }

    public float getResidentialPopulationPerHouseholdRehabClusterWastewaterTreatmentSystem(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
    }                                    

    public int getNonResidentialRehabClusterWastewaterTreatmentSystemHouses(Long facilityId) {
    	return getPresentHouseCountByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
    }        
    
    public float getNonResidentialPopulationPerHouseholdRehabClusterWastewaterTreatmentSystem(Long facilityId) {
    	return getPresentPopulationPerUnitByPopIdAndFacId(facilityId.toString(), 
    			String.valueOf(PopulationService.HOUSE_TYPE_NON_RESIDENTIAL_REHAB_CLUSTERED_WASTEWATER_TREATMENT_SYSTEM));
    }                
    
    public int getPresentNewSeparatSewerSystemPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_NEW_SEPARATE_SEWER_SYSTEM), null);
		Object object = m.get(String.valueOf(PopulationService.POPULATION_TYPE_NEW_SEPARATE_SEWER_SYSTEM));
		if (object instanceof Integer)
			return ((Integer)object).intValue();	
		
		Object[] o = (Object[])object;
		Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");
    	
    	return pop_res.intValue();
    }    
    
    public int getPresentRehabSeparateSewerSystemPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_REHAB_SEPARATE_SEWER_SYSTEM), null);
		Object object = m.get(String.valueOf(PopulationService.POPULATION_TYPE_REHAB_SEPARATE_SEWER_SYSTEM));
		if (object instanceof Integer)
			return ((Integer)object).intValue();	

		Object[] o = (Object[])object;
		Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");
    	
		return pop_res.intValue();
    }    

    
    //**********************DecentralizedPopulation
    //  Residential -Present
    public int getPresentResDecentralizedPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER), null);
		//Object[] o = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
		//Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");
    	Integer pop_res = (Integer)m.get(String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
		return (pop_res==null?0:pop_res.intValue());
    }    
    
    //  Non Residential -Present    
    public int getPresentNonResDecentralizedPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER), null);
    	Integer pop_res = (Integer)m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER));
    	return (pop_res==null?0:pop_res.intValue());
    } 
    
    //  Residential -Projected
    public int getProjectedResDecentralizedPopulation(Long facilityId) {
    	HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER), null);
		Object[] o = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
		Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");    	
    	return pop_res.intValue();
    }    
    
    //  Non Residential -Projected
    public int getProjectedNonResDecentralizedPopulation(Long facilityId) {
    	HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER), null);
		Object[] o = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER));
		Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");    	
    	return pop_res.intValue();
    } 
    
    //  total -present    
    public int getTotalPresentDecentralizedPopulation(Long facilityId) {
    	HashMap m = getPresentPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER), PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER+"");
    	Integer pop_res = (Integer)m.get(String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
    	Integer pop_non_res = (Integer)m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER));
    	return (pop_res==null?0:pop_res.intValue())+ (pop_non_res==null?0:pop_non_res.intValue());
    }
    
    //  total -present    
    public int getTotalProjectedDecentralizedPopulation(Long facilityId) {
    	HashMap m = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER), PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER+"");
		Object[] o = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_RESIDENTIAL_CLUSTERED_WASTERWATER));
		Integer pop_res = (o!=null)?(Integer)o[0]: new Integer("0");
		Object[] o2 = (Object[])m.get(String.valueOf(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_CLUSTERED_WASTEWATER));
		Integer pop_non_res = (o2!=null)?(Integer)o2[0]: new Integer("0");
    	return pop_res.intValue()+pop_non_res.intValue();
    }        
    
    //get receiving population (includes upstream)
    public double getTotalPresentReceivingPopulation (Long facilityId){
        PopulationHelper PH =getUpStreamFacilitiesPopulationTotal(facilityId.toString());
        int resRecPopCnt =0;
        int nonResRecPopCnt =0;
        int resUpstreamCnt = 0;
        int nonResUpstreamCnt = 0;
        double nonResMulti = ((BigDecimal)getCwnsInfoRef().getFlowRatioNonResMultiplier()).doubleValue();
        
        if (PH != null){
           resUpstreamCnt = PH.getPresentResPopulation();
           nonResUpstreamCnt = PH.getPresentNonResPopulation();
        }        
        HashMap recPopulationCount = getPresentPopulationByPopIdAndFacId(facilityId.toString(), PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"", PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"");
        if (recPopulationCount != null && !recPopulationCount.isEmpty()){
    	    if (recPopulationCount.containsKey(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"")){
    	    	resRecPopCnt =((Integer)recPopulationCount.get(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"")).intValue();
    	    }    
    	    if (recPopulationCount.containsKey(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"")){
    	    	nonResRecPopCnt = ((Integer)recPopulationCount.get(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"")).intValue();
    	    }    
    	}
        return resRecPopCnt+resUpstreamCnt+(nonResMulti * (nonResRecPopCnt + nonResUpstreamCnt));        
    }

    public double getTotalProjectedReceivingPopulation (Long facilityId){
        PopulationHelper PH =getUpStreamFacilitiesPopulationTotal(facilityId.toString());
        int resRecPopCnt =0;
        int nonResRecPopCnt =0;
        int resUpstreamCnt = 0;
        int nonResUpstreamCnt = 0;
        double nonResMulti = ((BigDecimal)getCwnsInfoRef().getFlowRatioNonResMultiplier()).doubleValue();
        
        if (PH != null){
           resUpstreamCnt = PH.getProjectedResPopulation();
           nonResUpstreamCnt = PH.getProjectedNonResPopulation();
        }        
        HashMap recPopulationCount = getProjectedPopulationByPopIdAndFacId(facilityId.toString(), PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"", PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"");
        if (recPopulationCount != null && !recPopulationCount.isEmpty()){
    	    if (recPopulationCount.containsKey(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"")){
    	    	Object[] o = (Object[])recPopulationCount.get(PopulationService.POPULATION_TYPE_RESIDENTIAL_RECEIVING_COLLECTION+"");
		        resRecPopCnt=((Integer)o[0]).intValue();
    	    }    
    	    if (recPopulationCount.containsKey(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"")){
    	    	Object[] o = (Object[])recPopulationCount.get(PopulationService.POPULATION_TYPE_NON_RESIDENTIAL_RECEIVING_COLLECTION+"");
    	    	nonResRecPopCnt=((Integer)o[0]).intValue(); 	    	
    	    }
    	}
        return resRecPopCnt+resUpstreamCnt+(nonResMulti * (nonResRecPopCnt + nonResUpstreamCnt));        
    }
    
    
	private CwnsInfoRef getCwnsInfoRef() {
		Collection result = searchDAO.getObjects(CwnsInfoRef.class);
		return (CwnsInfoRef)result.iterator().next();
	}
	
	public Collection getPopulationByFacilityId(Long facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection results = searchDAO.getSearchList(FacilityPopulation.class, scs);		
		return results;		
	}
	
	private CountyRef getFacilityPrimaryCounty(Long facilityId){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		SearchConditions scs =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("primaryFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		GeographicAreaCounty GAC = (GeographicAreaCounty)searchDAO.getSearchObject(GeographicAreaCounty.class, scs, aliasArray);
		if (GAC != null)
			return GAC.getCountyRef();
		else
		return null;
	}

	public float getNonResidentialPopulationPerHouse(Long facilityId) {
		
		SearchConditions scs0 = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(8)));
		scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		FacilityPopulationUnit object = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs0);
		if (object != null)
		  return object.getPopulationPerUnit().floatValue();
		else {
		CountyRef county = getFacilityPrimaryCounty(facilityId);
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, f.getLocationId()));
		StateRef state= (StateRef)searchDAO.getSearchObject(StateRef.class, scs);
		return county == null ? (state.getNonresPopulationPerUnit().floatValue()) : county.getNonresPopulationPerUnit().floatValue();
		}
	}

	public float getResidentialPopulationPerHouse(Long facilityId) {
		SearchConditions scs0 = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(7)));
		scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		FacilityPopulationUnit object = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs0);
		if (object != null)
		  return object.getPopulationPerUnit().floatValue();
		else {
		CountyRef county = getFacilityPrimaryCounty(facilityId);
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, f.getLocationId()));
		StateRef state= (StateRef)searchDAO.getSearchObject(StateRef.class, scs);
		return county == null ? (state.getResPopulationPerUnit().floatValue()) : county.getResPopulationPerUnit().floatValue();
		}
	}
		
	public void savePopulationUnitsByPopIdAndFacId(String facNum, String popId, Object[] values, String userId){
		SearchConditions scs = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popId)));
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs1.setCondition(SearchCondition.OPERATOR_AND, scs);
		FacilityPopulationUnit result = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs1);
		if (result != null){
			result.setPresentUnitsCount((Integer)values[0]);
			result.setProjectedUnitsCount((Integer)values[1]);
			result.setPopulationPerUnit((BigDecimal)values[2]);
			result.setLastUpdateTs(new Date());
		    result.setLastUpdateUserid(userId);
			
		}
		else{
			result = new FacilityPopulationUnit();
			SearchConditions scs2 = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
			Facility facility = (Facility)searchDAO.getSearchObject(Facility.class, scs2);
			result.setFacility(facility);
			SearchConditions scs3 = new SearchConditions(new SearchCondition("populationId", SearchCondition.OPERATOR_EQ, new Long(popId)));
			PopulationRef populationRef = (PopulationRef)searchDAO.getSearchObject(PopulationRef.class, scs3);
			FacilityPopulationUnitId id = new FacilityPopulationUnitId();
			id.setFacilityId(facility.getFacilityId());
			id.setPopulationId(populationRef.getPopulationId());
		    result.setId(id);
		    result.setLastUpdateTs(new Date());
		    result.setLastUpdateUserid(userId);
		    result.setPopulationRef(populationRef);
		    result.setPresentUnitsCount((Integer)values[0]);
		    result.setProjectedUnitsCount((Integer)values[1]);
		    result.setPopulationPerUnit((BigDecimal)values[2]);
		    //result.setProjectedYear(projectedYear);
		}
		searchDAO.saveObject(result);
		
	}
	
	public void updatePopulationPerUnits(String facNum, int[] popIds, float populationPerUnit, String userId){
		//SearchConditions scs = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popIds[0])));
		//scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(popIds[1])));
		//SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		//scs1.setCondition(SearchCondition.OPERATOR_AND, scs);
		//Collection results = searchDAO.getSearchList(FacilityPopulationUnit.class, scs1);
		
		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
														new FacilityPopulationUnitId(Long.parseLong(facNum), (long)popIds[0])));
		SearchConditions scs1 = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
				new FacilityPopulationUnitId(Long.parseLong(facNum), (long)popIds[1])));
		scs1.setCondition(SearchCondition.OPERATOR_OR, scs);
		Collection results = searchDAO.getSearchList(FacilityPopulationUnit.class, scs1);
		
		Iterator iter = results.iterator();
		  while(iter.hasNext()){
			  FacilityPopulationUnit obj = (FacilityPopulationUnit)iter.next();
			  obj.setPopulationPerUnit(new BigDecimal(populationPerUnit));
			  obj.setLastUpdateTs(new Date());
			  obj.setLastUpdateUserid(userId);
			  searchDAO.saveObject(obj);
		  }
		
	}

	public void deleteFacilityHouseIfExists(String facNum, Long popId) {
		SearchConditions scs = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, popId));
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs1.setCondition(SearchCondition.OPERATOR_AND, scs);
		FacilityPopulationUnit result = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs1);
		if (result != null)
			searchDAO.removeObject(result);
	}
	
	public void setUpCostCurvesForRerun(ArrayList costCurveList, Long facilityId){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("costCurveRef", "c", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("c.costCurveId", SearchCondition.OPERATOR_IN, costCurveList));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection objects = searchDAO.getSearchList(FacilityCostCurve.class, new ArrayList(), scs1, new ArrayList(), aliasArray);
		Iterator iter = objects.iterator();
		while (iter.hasNext()){
			FacilityCostCurve facilityCostCurve = (FacilityCostCurve)iter.next();
			facilityCostCurve.setCurveRerunFlag('Y');
			searchDAO.saveObject(facilityCostCurve);
		}
	}
		
	public String isClusteredSystemExists(String facNum) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("facilityTypeRef.facilityTypeId", SearchCondition.OPERATOR_EQ, new Long(PopulationService.FACILITY_TYPE_CLUSTERED_SYSTEM)));
		Collection results = searchDAO.getSearchList(FacilityType.class, scs);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}
	
	public String isOWTSystemExists(String facNum) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facNum)));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("facilityTypeRef.facilityTypeId", SearchCondition.OPERATOR_EQ, new Long(PopulationService.FACILITY_TYPE_ONSITE_WASTE_WATER_TREATMENT_SYSTEM)));
		Collection results = searchDAO.getSearchList(FacilityType.class, scs);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}

	public void updateFacilityPopulationCount(Long facilityId, ArrayList popIds, String userId) {
		SearchConditions scs = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_IN, popIds));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection results = searchDAO.getSearchList(FacilityPopulationUnit.class, scs);
		Iterator iter = results.iterator();
		  while(iter.hasNext()){
			  FacilityPopulationUnit obj = (FacilityPopulationUnit)iter.next();
			  float populationPerUnit = obj.getPopulationPerUnit().floatValue();
			  int presentUnits = obj.getPresentUnitsCount().intValue();
			  int populationCount = Math.round(populationPerUnit * presentUnits);
			  SearchConditions scs1 = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(obj.getPopulationRef().getPopulationId())));
			  scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
			  FacilityPopulation fp = (FacilityPopulation)searchDAO.getSearchObject(FacilityPopulation.class, scs1);
			  if (fp!=null){
			   fp.setPresentPopulationCount(new Integer(populationCount));
			   searchDAO.saveObject(fp);
			  }
		  }
	}

	public float getNonResidentialOWTSPopulationPerHouse(Long facilityId) {
		SearchConditions scs0 = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(6)));
		scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		FacilityPopulationUnit object = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs0);
		if (object != null)
		  return object.getPopulationPerUnit().floatValue();
		else {
		CountyRef county = getFacilityPrimaryCounty(facilityId);
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, f.getLocationId()));
		StateRef state= (StateRef)searchDAO.getSearchObject(StateRef.class, scs);
		return county == null ? (state.getResPopulationPerUnit().floatValue()) : county.getResPopulationPerUnit().floatValue();
		}
	}

	public float getResidentialOWTSPopulationPerHouse(Long facilityId) {
		SearchConditions scs0 = new SearchConditions(new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_EQ, new Long(5)));
		scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		FacilityPopulationUnit object = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs0);
		if (object != null)
		  return object.getPopulationPerUnit().floatValue();
		else {
		CountyRef county = getFacilityPrimaryCounty(facilityId);
		Facility f = (Facility)searchDAO.getObject(Facility.class, facilityId);
		SearchConditions scs = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, f.getLocationId()));
		StateRef state= (StateRef)searchDAO.getSearchObject(StateRef.class, scs);
		return county == null ? (state.getResPopulationPerUnit().floatValue()) : county.getResPopulationPerUnit().floatValue();
		}
	}
		
	/**
	 * Get the list of FACTILITY_DISCHARGE objects that are associated with the facilityId. 
	 * @param facilityId
	 * @return
	 */
	private Collection getFacilityDischarges(String facilityId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("facilityByFacilityId.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		Collection facilityDischarges = searchDAO.getSearchList(FacilityDischarge.class, new ArrayList(), scs);
        return  facilityDischarges;
	}
	
	private int getPresentFlowPercent(Collection discharges){
		int flowPercent =0;
		for (Iterator iter = discharges.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			int cflow = (fd.getPresentFlowPortionPersent()!=null?fd.getPresentFlowPortionPersent().intValue():0);
			flowPercent = flowPercent+ cflow;	
		}
		return flowPercent;		
	}
	
	private int getProjectedFlowPercent(Collection discharges){
		int flowPercent =0;
		for (Iterator iter = discharges.iterator(); iter.hasNext();) {
			FacilityDischarge fd = (FacilityDischarge) iter.next();
			int cflow = (fd.getProjectedFlowPortionPersent()!=null?fd.getProjectedFlowPortionPersent().intValue():0);
			flowPercent = flowPercent+ cflow;
		}
		return flowPercent;		
	}
	
	public boolean isFlowApportionmentCompleted(String facNum){
		Collection upstreamPresentfacilities = getUpstreamFacilitiesListByDischargeType(facNum, PopulationService.PRESENT_ONLY);
		Collection upstreamProjectedfacilities = getUpstreamFacilitiesListByDischargeType(facNum, PopulationService.PROJECTED_ONLY);
		Iterator iter1 = upstreamPresentfacilities.iterator();
		Iterator iter2 = upstreamProjectedfacilities.iterator();
		while (iter1.hasNext()){
			Collection discharges = getFacilityDischarges((String)iter1.next());
			int presentFlowPercent = getPresentFlowPercent(discharges);
			log.debug("presentflowpercent--"+presentFlowPercent);
			if(presentFlowPercent !=100){
				return false;
			}
		}
		while (iter2.hasNext()){
			Collection discharges = getFacilityDischarges((String)iter2.next());
			int projectedFlowPercent = getProjectedFlowPercent(discharges);
			log.debug("projectedflowpercent--"+projectedFlowPercent);
			if(projectedFlowPercent !=100){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Given a Long object that represents a facilityId and discharge type return a Collection of 
	 * Long objects that represent the related sewer shed facilities. 
	 * @param facilityId
	 * @param dischargeType
	 * @return
	 */
	public Collection getRelatedSewerShedFacilitiesForDisplay(Long facilityId, int dischargeType) {
		Collection relatedSewerShedFacilityList = new ArrayList();
		getRelatedSewerShedFacilitiesForDisplay(facilityId.toString(), new ArrayList(),relatedSewerShedFacilityList, dischargeType);
		
		return relatedSewerShedFacilityList;
	}
		
	
	private Collection getRelatedSewerShedFacilitiesForDisplay(String facilityId, Collection excludingList, Collection relatedSewerShedFacilityList, int dischargeType)
	{
		
		log.debug(" getRelatedSewerShedFacilities: enter....  " + facilityId + "\r\n");
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityByFacilityId", "disFacility", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("facilityByFacilityIdDischargeTo", "recFacility", AliasCriteria.JOIN_INNER));
		ArrayList columns = new ArrayList();
		columns.add("facilityByFacilityId");
		columns.add("facilityByFacilityIdDischargeTo");	
        SearchConditions scs0 = null;
		
		Iterator excludedIter = excludingList.iterator();
		while (excludedIter.hasNext()){
			String excludedFacilityId = (String)excludedIter.next();
			
			log.debug(" getRelatedSewerShedFacilities: now excluding " + excludedFacilityId + "\r\n");

			if(scs0 == null)
			{
				scs0 = new SearchConditions();
			}
			
			scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
					SearchCondition.OPERATOR_NOT_EQ, new Long(excludedFacilityId)));
			scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facilityByFacilityId.facilityId", 
					SearchCondition.OPERATOR_NOT_EQ, new Long(excludedFacilityId)));
		}
				
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
																SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs1.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("facilityByFacilityId.facilityId", 
				SearchCondition.OPERATOR_EQ, new Long(facilityId)));

		SearchConditions scs2 = new SearchConditions(new SearchCondition("facilityByFacilityIdDischargeTo.facilityId", 
				SearchCondition.OPERATOR_IS_NOT_NULL, null));	
		
		scs2.setCondition(SearchCondition.OPERATOR_AND, scs1);
		if(scs0!=null)
		{
			scs2.setCondition(SearchCondition.OPERATOR_AND, scs0);
		}
		
		SearchConditions scs3 = new SearchConditions();
		
		if(dischargeType == PopulationService.PRESENT_ONLY)
		{
			scs3.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PROJECTED_ONLY)
		{
			scs3.setCondition(new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		else if(dischargeType == PopulationService.PRESENT_AND_PROJECTED)
		{
			scs3.setCondition(new SearchCondition("presentFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
			scs3.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("projectedFlag", 
					SearchCondition.OPERATOR_EQ, "Y"));
		}
		
		scs2.setCondition(SearchCondition.OPERATOR_AND, scs3);
		SearchConditions scs4 = new SearchConditions();
		scs4.setCondition(new SearchCondition("disFacility.versionCode", 
					SearchCondition.OPERATOR_NOT_EQ, new Character('A')));
		scs4.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("recFacility.versionCode", 
					SearchCondition.OPERATOR_NOT_EQ, new Character('A')));
		
		if(scs4!=null)
		{
			scs2.setCondition(SearchCondition.OPERATOR_AND, scs4);
		}	
			
		log.debug(" getRelatedSewerShedFacilities: ready to searchDAO.getSearchList" + "\r\n");
	
		Collection results = searchDAO.getSearchList(FacilityDischarge.class, columns, scs2, new ArrayList(), aliasArray);
		//Collection results = searchDAO.getSearchList(FacilityDischarge.class, columns, scs2);
		Iterator iter = results.iterator();
		while (iter.hasNext()){
			Object[] o = (Object[])iter.next();
			String upstreamFacId = Long.toString(((Facility)o[0]).getFacilityId());
			String downstreamFacId = Long.toString(((Facility)o[1]).getFacilityId());
			PopulationHelper PH = new PopulationHelper();
			PH.setDisFacilityCwnsNbr(((Facility)o[0]).getCwnsNbr());
			PH.setDisFacilityName(((Facility)o[0]).getName());
			PH.setRecFacilityCwnsNbr(((Facility)o[1]).getCwnsNbr());
			PH.setRecFacilityName(((Facility)o[1]).getName());
			if(!isDuplicate(relatedSewerShedFacilityList,PH))
			   relatedSewerShedFacilityList.add(PH);
			String newNodeFacilityId = upstreamFacId.equals(facilityId)?downstreamFacId:upstreamFacId;
			if(!excludingList.contains(facilityId))
			      excludingList.add(facilityId);		
			getRelatedSewerShedFacilitiesForDisplay(newNodeFacilityId,excludingList,relatedSewerShedFacilityList,dischargeType);
			//Post-Order Traverse
			log.debug(" getRelatedSewerShedFacilities:visiting --> " + newNodeFacilityId + "\r\n");
					
						
		}

		return relatedSewerShedFacilityList;
	}
	
	private boolean isDuplicate(Collection sewershedList,PopulationHelper currentObject){
		Iterator iter = sewershedList.iterator();
		while(iter.hasNext()){
			PopulationHelper PH = (PopulationHelper)iter.next();
			if(PH.getDisFacilityCwnsNbr().equals(currentObject.getDisFacilityCwnsNbr()) &&
					PH.getRecFacilityCwnsNbr().equals(currentObject.getRecFacilityCwnsNbr()))
				return true;
		}
		
		return false;
		
		
	}
	
}
