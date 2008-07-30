package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CwnsInfoRef;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityFlow;
import gov.epa.owm.mtb.cwns.model.FlowRef;
import gov.epa.owm.mtb.cwns.model.FacilityFlowId;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.service.FlowService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

public class FlowServiceImpl extends CWNSService implements FlowService {
	
	//private FlowDAO flowDAO;
	private SearchDAO searchDAO;
	/*
	public void setFlowDAO(FlowDAO dao){
		flowDAO = dao;
	}*/
			
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	
	private PopulationService populationService;
	public void setPopulationService(PopulationService ps){
		populationService =ps;
	}
	
	public Collection getFlowRefs(){
		SortCriteria sortCriteria = new SortCriteria("name",
				SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);

		SearchConditions scs = new SearchConditions();
		Collection flowRefs = searchDAO.getSearchList(FlowRef.class,new ArrayList(), scs, sortArray);
		return flowRefs;
	}
	
	public String isPreFlowUpdatable(Long facilityId) {
		ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.validForFlowFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("presentFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}
	
    public String isProFlowUpdatable(Long facilityId) {
    	ArrayList columns = new ArrayList();
		columns.add("ftref.facilityTypeId");
		AliasCriteria alias = new AliasCriteria("facilityTypeRef", "ftref", AliasCriteria.JOIN_INNER);		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(alias);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("ftref.validForFlowFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("projectedFlag", SearchCondition.OPERATOR_LIKE, "Y"));
		Collection results = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);
		if(results.iterator().hasNext())
		   return "Y";
		else
			return "N";
	}

	public HashMap getIndividualFlowValues(Long facilityId){
		HashMap map = null;
		ArrayList columns = new ArrayList();
		columns.add("flowRef.flowId");
		columns.add("existingFlowMsr");
		columns.add("presentFlowMsr");
		columns.add("projectedFlowMsr");
		SearchConditions scs = new SearchConditions(new SearchCondition("flowRef.flowId", SearchCondition.OPERATOR_EQ, new Long(1)));
		scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("flowRef.flowId", SearchCondition.OPERATOR_EQ, new Long(2)));
		scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("flowRef.flowId", SearchCondition.OPERATOR_EQ, new Long(3)));
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs1.setCondition(SearchCondition.OPERATOR_AND, scs);
		Collection results = searchDAO.getSearchList(FacilityFlow.class, columns, scs1);
		Iterator iter = results.iterator();
		map = new HashMap();
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			Object[] value = new Object[3];
			value[0] = obj[1];
			value[1] = obj[2];
			value[2] = obj[3];
			map.put(((Long)obj[0]).toString(), value);
		}		
		        
		return map;		
	}
	
	public HashMap getTotalFlowValues(Long facilityId){
		HashMap map = null;
		ArrayList columns = new ArrayList();
		columns.add("existingFlowMsr");
		columns.add("presentFlowMsr");
		columns.add("projectedFlowMsr");
		SearchConditions scs = new SearchConditions(new SearchCondition("flowRef.flowId", SearchCondition.OPERATOR_EQ, new Long(5)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection results = searchDAO.getSearchList(FacilityFlow.class, columns, scs);
		Iterator iter = results.iterator();
		map = new HashMap();
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			map.put("Existingflow", obj[0]);
			map.put("Presentflow", obj[1]);
			map.put("Projectedflow", obj[2]);
		}				        
		return map;						
	}

	public float getMunicipalPresentFlowRate(Long facilityId) {
		FacilityFlow facilityFlow = getFacilityFlow(facilityId, new Long(1));
		return (facilityFlow == null || facilityFlow.getPresentFlowMsr() == null) ? (float)0.0 : facilityFlow.getPresentFlowMsr().floatValue();
	}
	
	public float getMunicipalProjectedFlowRate(Long facilityId) {
		FacilityFlow facilityFlow = getFacilityFlow(facilityId, new Long(1));
		return (facilityFlow == null || facilityFlow.getProjectedFlowMsr() == null) ? (float)0.0 : facilityFlow.getProjectedFlowMsr().floatValue();
	}	
	
	public CwnsInfoRef getCwnsInfoRef() {
		Collection result = searchDAO.getObjects(CwnsInfoRef.class);
		return (CwnsInfoRef)result.iterator().next();
	}

	public void saveIndividualFlowInfo(String surveyFacilityId, HashMap map, String userId) {
		// Calling post save from facility service
		/*
		SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(surveyFacilityId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ,new Long(6)));
		FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
		if (facilityEntryStatus!=null){
		  facilityEntryStatus.setLastUpdateTs(new Date());
		  facilityEntryStatus.setDataAreaLastUpdateTs(new Date());
		  facilityEntryStatus.setLastUpdateUserid(userId);
		  searchDAO.saveObject(facilityEntryStatus);
		}  
		Facility facility = (Facility)searchDAO.getObject(Facility.class, new Long(surveyFacilityId));
		if (facility != null){
		     if ("SAS".equalsIgnoreCase(facility.getReviewStatusRef().getReviewStatusId())){
		    	 FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
		    	 FacilityReviewStatusId id = new FacilityReviewStatusId(new Long(surveyFacilityId).longValue(), "SIP", new Date());
		    	 facilityreviewstatus.setId(id);
		    	 facilityreviewstatus.setLastUpdateUserid(userId);
		    	 searchDAO.saveObject(facilityreviewstatus);
		    	 
		        ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
		        reviewStatusRef.setReviewStatusId("SIP");
		        facility.setReviewStatusRef(reviewStatusRef);
		     } 
		  
		  facility.setLastUpdateTs(new Date());
	      facility.setLastUpdateUserid(userId);
		  searchDAO.saveObject(facility);  
		}
		*/
		/*Not Required
		if (!("".equals(feedBackFacilityId))){
			Facility fFacility = (Facility)searchDAO.getObject(Facility.class, new Long(feedBackFacilityId));
			if ("LAS".equalsIgnoreCase(fFacility.getReviewStatusRef().getReviewStatusId())){
		    	 FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
		    	 FacilityReviewStatusId id = new FacilityReviewStatusId(fFacility.getFacilityId(),"LIP", new Date());
		    	 facilityreviewstatus.setId(id);
		    	 facilityreviewstatus.setLastUpdateUserid(userId);
		    	 searchDAO.saveObject(facilityreviewstatus);
		     ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
		     reviewStatusRef.setReviewStatusId("LIP");
		     fFacility.setReviewStatusRef(reviewStatusRef);
		     fFacility.setLastUpdateTs(new Date());
		     fFacility.setLastUpdateUserid(userId);
		     searchDAO.saveObject(fFacility);
		    
		   } 
				
	  }*/
		if (map.containsKey("1"))
			saveOrUpdateFlowInfo(surveyFacilityId, (double[])map.get("1"), new Long(1),userId);
		else
			deleteFlowInfoIfExists(surveyFacilityId, new Long(1));
		if (map.containsKey("2"))
			saveOrUpdateFlowInfo(surveyFacilityId,(double[]) map.get("2"), new Long(2),userId);
		else
			deleteFlowInfoIfExists(surveyFacilityId, new Long(2));
		if (map.containsKey("3"))
			saveOrUpdateFlowInfo(surveyFacilityId, (double[])map.get("3"), new Long(3),userId);
		else
			deleteFlowInfoIfExists(surveyFacilityId, new Long(3));
		if (map.containsKey("5"))
			saveOrUpdateFlowInfo(surveyFacilityId, (double[])map.get("5"), new Long(5),userId);
		else
		    deleteFlowInfoIfExists(surveyFacilityId, new Long(5));
	}

	private void deleteFlowInfoIfExists(String surveyFacilityId, Long flowId) {
		// TODO Auto-generated method stub
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(surveyFacilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("flowRef.flowId", SearchCondition.OPERATOR_EQ, flowId));
		FacilityFlow facilityFlow = (FacilityFlow)searchDAO.getSearchObject(FacilityFlow.class, scs);
		if (facilityFlow != null){
		   searchDAO.removeObject(facilityFlow);
		}
	}

	private void saveOrUpdateFlowInfo(String surveyFacilityId, double[] values, Long flowId, String userId) {
		// TODO Auto-generated method stub
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(surveyFacilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("flowRef.flowId", SearchCondition.OPERATOR_EQ, flowId));
		FacilityFlow facilityFlow = (FacilityFlow)searchDAO.getSearchObject(FacilityFlow.class, scs);
		if (facilityFlow!=null){
			facilityFlow.setExistingFlowMsr(new BigDecimal(values[0]));
			facilityFlow.setPresentFlowMsr(new BigDecimal(values[1]));
			facilityFlow.setProjectedFlowMsr(new BigDecimal(values[2]));
			facilityFlow.setLastUpdateTs(new Date());
			facilityFlow.setLastUpdateUserid(userId);
			searchDAO.saveObject(facilityFlow);
		}
		else {
			facilityFlow = new FacilityFlow();
			FacilityFlowId id = new FacilityFlowId(new Long(surveyFacilityId).longValue(), flowId.longValue());
			//Facility facility = (Facility)searchDAO.getObject(Facility.class, new Long(surveyFacilityId));
			//FlowRef flowRef = (FlowRef)searchDAO.getObject(FlowRef.class, flowId);
			//facilityFlow.setFacility(facility);
			//facilityFlow.setFlowRef(flowRef);
			facilityFlow.setId(id);
			facilityFlow.setExistingFlowMsr(new BigDecimal(values[0]));
			facilityFlow.setPresentFlowMsr(new BigDecimal(values[1]));
			facilityFlow.setProjectedFlowMsr(new BigDecimal(values[2]));
			facilityFlow.setLastUpdateTs(new Date());
			facilityFlow.setLastUpdateUserid(userId);
			searchDAO.saveObject(facilityFlow);
		}
		
	}

	public void saveTotalFlowInfo(String surveyFacilityId, HashMap map, String userId) {
		// Calling post save from facility service
		/*
		SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(surveyFacilityId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ,new Long(6)));
		FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
		if (facilityEntryStatus!=null){
		  facilityEntryStatus.setLastUpdateTs(new Date());
		  searchDAO.saveObject(facilityEntryStatus);
		}  
		Facility facility = (Facility)searchDAO.getObject(Facility.class, new Long(surveyFacilityId));
		if (facility != null){
		     if ("SAS".equalsIgnoreCase(facility.getReviewStatusRef().getReviewStatusId())){
		    	 FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
		    	 FacilityReviewStatusId id = new FacilityReviewStatusId(new Long(surveyFacilityId).longValue(), "SIP", new Date());
		    	 facilityreviewstatus.setId(id);
		    	 facilityreviewstatus.setLastUpdateUserid(userId);
		    	 searchDAO.saveObject(facilityreviewstatus);
		    	 
		        ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
		        reviewStatusRef.setReviewStatusId("SIP");
		        facility.setReviewStatusRef(reviewStatusRef);
		     } 
		  
		  facility.setLastUpdateTs(new Date());
	      facility.setLastUpdateUserid(userId);
		  searchDAO.saveObject(facility);  
		}
		*/
		double[] values = (double[])map.get("5");
		if (values[0]==0 && values[1]==0 && values[2]==0)
			deleteFlowInfoIfExists(surveyFacilityId, new Long(5));
		else
			saveOrUpdateFlowInfo(surveyFacilityId, (double[])map.get("5"), new Long(5),userId);
		deleteFlowInfoIfExists(surveyFacilityId, new Long(1));
		deleteFlowInfoIfExists(surveyFacilityId, new Long(2));
		deleteFlowInfoIfExists(surveyFacilityId, new Long(3));
	}

	public boolean isPreDesignFlowUpdatable(String facilityId) {
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
		SearchConditions scs1 = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs1.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("costCurveRef.costCurveId",SearchCondition.OPERATOR_EQ,new Long(7)));
		scs1.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("assignedOrRunCode",SearchCondition.OPERATOR_EQ,new Character('R')));
		SearchConditions scs2 = new SearchConditions(new SearchCondition("f.reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_LIKE,"FA"));
		scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("f.reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_LIKE,"FRR"));
		scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("f.reviewStatusRef.reviewStatusId", SearchCondition.OPERATOR_LIKE,"FRC"));
		scs1.setCondition(SearchCondition.OPERATOR_AND, scs2);
		Collection results = searchDAO.getSearchList(FacilityCostCurve.class, new ArrayList(), scs1, new ArrayList(), aliasArray);
		if (results!=null && !(results.isEmpty()))
		   return true;
		else 
		   return false;
	}

	public void saveWWPFlow(String surveyFacilityId, double[] wwpFlow, String userId) {
		if (wwpFlow[0]==0 && wwpFlow[1]==0 && wwpFlow[2]==0)
			deleteFlowInfoIfExists(surveyFacilityId, new Long(4));
		else
		    saveOrUpdateFlowInfo(surveyFacilityId, wwpFlow, new Long(4),userId);
		
	}

	public HashMap getWWPFlowValues(Long facilityId) {
		HashMap map = null;
		ArrayList columns = new ArrayList();
		columns.add("existingFlowMsr");
		columns.add("presentFlowMsr");
		columns.add("projectedFlowMsr");
		SearchConditions scs = new SearchConditions(new SearchCondition("flowRef.flowId", SearchCondition.OPERATOR_EQ, FLOW_TYPE_WET_WEATHER_PEAK));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection results = searchDAO.getSearchList(FacilityFlow.class, columns, scs);
		Iterator iter = results.iterator();
		map = new HashMap();
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			map.put("Existingflow", obj[0]);
			map.put("Presentflow", obj[1]);
			map.put("Projectedflow", obj[2]);
		}		
		        
		return map;	
	}
	
	public boolean isFlowExist(Long facilityId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection results = searchDAO.getSearchList(FacilityFlow.class, scs);
		if(results!=null && !results.isEmpty()){
			return true;
		}else{
			return false;
		}
	}	
	
	public FacilityFlow getFacilityFlow(Long facilityId, Long flowTypeId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.flowTypeId", SearchCondition.OPERATOR_EQ, flowTypeId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		return (FacilityFlow)searchDAO.getSearchObject(FacilityFlow.class, scs);
	}	
	
	public double getExistingFlowToPropulation(Long facilityId){
		double f2pRatio=-99.0;
		FacilityFlow ff = getFacilityFlow(facilityId, FlowService.FLOW_TYPE_TOTAL);
		if(ff.getExistingFlowMsr()!=null){
			double flow = ff.getExistingFlowMsr().floatValue();
			double pcount= populationService.getTotalPresentReceivingPopulation(facilityId);
			if(pcount>0.0){
				f2pRatio = flow*1000000/pcount;	
			}			
		}
		return f2pRatio;
	}
	
	public double getPresentFlowToPropulation(Long facilityId){
		double f2pRatio=-99.0;
		FacilityFlow ff = getFacilityFlow(facilityId, FlowService.FLOW_TYPE_TOTAL);
		if(ff.getPresentFlowMsr()!=null){
			double flow = ff.getPresentFlowMsr().floatValue();
			double pcount= populationService.getTotalPresentReceivingPopulation(facilityId);
			if(pcount>0.0){
				f2pRatio = flow*1000000/pcount;
			}	
		}
		return f2pRatio;
	}

	
	public double getPresentTotalFlow(Long facilityId) {
		FacilityFlow ff = getFacilityFlow(facilityId, FlowService.FLOW_TYPE_TOTAL);
		if (ff == null) return 0.0;
		if(ff.getPresentFlowMsr()!=null){
			return ff.getPresentFlowMsr().floatValue();
		}
		return 0.0;
	}
	
	public double getProjectedFlowToPropulation(Long facilityId){
		double f2pRatio=-99.0;
		FacilityFlow ff = getFacilityFlow(facilityId, FlowService.FLOW_TYPE_TOTAL);
		if(ff.getProjectedFlowMsr()!=null){
			double flow = ff.getProjectedFlowMsr().floatValue();
			double pcount= populationService.getTotalProjectedReceivingPopulation(facilityId);
			if(pcount > 0.0){
				f2pRatio = flow*1000000/pcount;
			}	
		}
		return f2pRatio;
	}
	
	public boolean isFlowToRationValid(Long facilityId){
		short lowRatio = getCwnsInfoRef().getFlowLowRatio();
		short hiRatio = getCwnsInfoRef().getFlowHighRatio();

		double existingRatio = getExistingFlowToPropulation(facilityId);
		if(existingRatio>=0 &&(existingRatio<lowRatio || existingRatio> hiRatio)){
			return false;
		}
		
		double presentRatio = getPresentFlowToPropulation(facilityId);
		if(presentRatio>=0 && (presentRatio<lowRatio || presentRatio> hiRatio)){
			return false;
		}
		
		double projectedRatio = getProjectedFlowToPropulation(facilityId);
		if(projectedRatio>=0 && (projectedRatio<lowRatio || projectedRatio> hiRatio)){
			return false;
		}		
		return true;
	}
	
	public boolean isProMunicipalFlowUpdatable(Long facilityId){
		ArrayList costCurveIdList = new ArrayList();
		costCurveIdList.add(new Long(1));
		//costCurveIdList.add(new Long(2));
		costCurveIdList.add(new Long(4));
		costCurveIdList.add(new Long(5));
		costCurveIdList.add(new Long(6));
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("costCurveRef", "c", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("c.costCurveId", SearchCondition.OPERATOR_IN, costCurveIdList));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("assignedOrRunCode", SearchCondition.OPERATOR_EQ, new Character('R')));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection objects = searchDAO.getSearchList(FacilityCostCurve.class, new ArrayList(), scs1, new ArrayList(), aliasArray);
		if (objects!=null && objects.size()>0)
		  return false;
		else
			return true;
	}

  public boolean isTotalFlowUpdatable(Long facilityId){
	ArrayList costCurveIdList = new ArrayList();
	costCurveIdList.add(new Long(1));
	costCurveIdList.add(new Long(2));
	costCurveIdList.add(new Long(4));
	costCurveIdList.add(new Long(5));
	costCurveIdList.add(new Long(6));
	
	ArrayList aliasArray = new ArrayList();
	aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));			
	aliasArray.add(new AliasCriteria("costCurveRef", "c", AliasCriteria.JOIN_INNER));			
	aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
	SearchConditions scs1 =  new SearchConditions(new SearchCondition("c.costCurveId", SearchCondition.OPERATOR_IN, costCurveIdList));
	scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("assignedOrRunCode", SearchCondition.OPERATOR_EQ, new Character('R')));
	scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
	Collection objects = searchDAO.getSearchList(FacilityCostCurve.class, new ArrayList(), scs1, new ArrayList(), aliasArray);
	if (objects!=null && objects.size()>0)
	  return false;
	else
		return true;
 }
}  

