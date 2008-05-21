package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.costcurve.algorithm.CostCurveAlgorithm;
import gov.epa.owm.mtb.cwns.costcurve.algorithm.CostCurveAlgorithmFactory;
import gov.epa.owm.mtb.cwns.costcurve.input.CostCurveInput;
import gov.epa.owm.mtb.cwns.costcurve.input.processor.CostCurveInputProcessor;
import gov.epa.owm.mtb.cwns.costcurve.input.processor.CostCurveInputProcessorFactory;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.model.CategoryRef;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.CostCurveCategoryRef;
import gov.epa.owm.mtb.cwns.model.CostCurveRef;
import gov.epa.owm.mtb.cwns.model.CostIndexRef;
import gov.epa.owm.mtb.cwns.model.DataAreaRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataArea;
import gov.epa.owm.mtb.cwns.model.FacilityDataAreaMessage;
import gov.epa.owm.mtb.cwns.model.FacilityDataAreaMessageId;
import gov.epa.owm.mtb.cwns.model.FacilityDocument;
import gov.epa.owm.mtb.cwns.model.FacilityDocumentId;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityFlow;
import gov.epa.owm.mtb.cwns.model.FacilityFlowId;
import gov.epa.owm.mtb.cwns.model.NeedTypeRef;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FlowService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CostCurveServiceImpl implements CostCurveService {
	
	public CostCurveOutput runCostCurve(String costCurveCode, Long facilityId) throws CostCurveException {

		// 1. get cost curve processor
		CostCurveInputProcessor ccip = costCurveInputProcessorFactory.getCostCurveInputProcessor(costCurveCode);
		
		// 2. get cost curve input from the cost curve processor
		CostCurveInput cci = ccip.getCostInputData(facilityId);
		
		// 3. get cost curve algorithm
		CostCurveAlgorithm costCurveAlgorithm = costCurveAlgorithmFactory.getCostCurveAlgorithm(costCurveCode);
		
		// 4. calculate the cost curve
		CostCurveOutput cost = costCurveAlgorithm.execute(cci);
		
		return cost;
		
	}
	
	/**
	 * Gets facility costcurves accross all the facilities in the current facility location that are in SIP, SAS, SCR 
	 * status and have rerun flag set to Y and no errors 
	 * @param facilityId
	 * @return facility costcurve objects
	 */
	
	public Collection getFacilityCostCurvesWithRerunAndNoErrors(Long facilityId){
		Facility f = facilityDAO.findByFacilityId(facilityId.toString());
		ArrayList aliasArray = new ArrayList();
		ArrayList revArr = new ArrayList();
		revArr.add("SAS");
		revArr.add("SIP");
		revArr.add("SCR");		
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("f.reviewStatusRef", "r", AliasCriteria.JOIN_INNER));
		SearchConditions scs = new SearchConditions(new SearchCondition("f.locationId", SearchCondition.OPERATOR_EQ, f.getLocationId()));
		scs.setCondition(new SearchCondition("r.reviewStatusId", SearchCondition.OPERATOR_IN, revArr));
		scs.setCondition(new SearchCondition("curveRerunFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		Collection rerunFacilityCostCurves = searchDAO.getSearchList(FacilityCostCurve.class, scs, new ArrayList(), aliasArray,0,0);
		//check if each cost curve check if any error is set to Y  -- need to be optimized
		ArrayList fccArr = new ArrayList();
		for (Iterator iter = rerunFacilityCostCurves.iterator(); iter.hasNext();) {
			FacilityCostCurve fcc = (FacilityCostCurve) iter.next();
			Set fccdas = fcc.getFacilityCostCurveDataAreas();
			boolean skip=false;
			for (Iterator iterator = fccdas.iterator(); iterator.hasNext();) {
				FacilityCostCurveDataArea  fccda= (FacilityCostCurveDataArea) iterator.next();
				if(fccda.getErrorFlag()=='Y'){
				 skip=true;
				 break;
				}
			}
			if(!skip){
				fccArr.add(fcc);
			}
		}
		return fccArr;
	}
	
	
	/**
	 * Gets a list of cost curves assigned to a facility 
	 * @param facility Id
	 * @return collection of FacilityCostCurve objects
	 */
	public Collection getFacilityCostCurves(Long facilityId){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		SearchConditions scs = new SearchConditions(new SearchCondition("fd.id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		return searchDAO.getSearchList(FacilityCostCurve.class, scs, new ArrayList(), aliasArray,0,0); 
	}

	public Collection getCostCurveValidationDataAreas(Long facilityId) {
		ArrayList arr = new ArrayList();
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection c = searchDAO.getSearchList(FacilityEntryStatus.class, scs);
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			FacilityEntryStatus fes = (FacilityEntryStatus) iter.next();
			arr.add(fes.getDataAreaRef());
		}
		return arr;
	}

	public void updateFacilityCostCurveDataArea(char errorFlag, Set errors, long facilityCostCurveId, Long dataAreaId, String userId) {
		//get the object
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityCostCurveId", SearchCondition.OPERATOR_EQ, new Long(facilityCostCurveId)));
		scs.setCondition(new SearchCondition("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		FacilityCostCurveDataArea fccda = (FacilityCostCurveDataArea)searchDAO.getSearchObject(FacilityCostCurveDataArea.class, scs);
		if(fccda !=null){
			//get and remove any data area message
			long facilityId = fccda.getFacilityCostCurve().getFacilityDocument().getId().getFacilityId();
			Collection ccvErrors = getFacilityDataAreaMessages(new Long(facilityId), dataAreaId);
			deleteFacilityDataAreaMessages(ccvErrors);
			fccda.setErrorFlag(errorFlag);
			fccda.setLastUpdateUserid(userId);
			Date today = new Date();
			fccda.setLastUpdateTs(today);
			searchDAO.saveObject(fccda);
			//add messages
			createFacilityDataAreaMessages(facilityId, dataAreaId.longValue(), errors, userId);
		}else{
			throw new ApplicationException("unable to find the facility cost curve object for" + facilityCostCurveId + " and data area" + dataAreaId.toString());
		}		
	}	
	
	public Collection getFacilityDataAreaMessages(Long facilityId, Long dataAreaId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		scs.setCondition(new SearchCondition ("sourceCode", SearchCondition.OPERATOR_EQ, "ccv"));		
		return searchDAO.getSearchList(FacilityDataAreaMessage.class, scs);
	}
	
	public void createFacilityDataAreaMessages(long facilityId, long dataAreaId, Set errors, String userId) {
		for (Iterator iter = errors.iterator(); iter.hasNext();) {
			FacilityDataAreaMessage ccvErr = new FacilityDataAreaMessage();
			String errorMessageKey = (String) iter.next();
			FacilityDataAreaMessageId fesErrId = new FacilityDataAreaMessageId(facilityId, dataAreaId, errorMessageKey);
			ccvErr.setId(fesErrId);
			ccvErr.setSourceCode("ccv");
			Date current = new Date();
			ccvErr.setLastUpdateTs(current);
			ccvErr.setLastUpdateUserid(userId);
			searchDAO.saveObject(ccvErr);
		}
	}
	
	public void deleteFacilityDataAreaMessages(Collection ccvErrors) {
		for (Iterator iter = ccvErrors.iterator(); iter.hasNext();) {
			FacilityDataAreaMessage ccvErr = (FacilityDataAreaMessage) iter.next();
			searchDAO.removeObject(ccvErr);
			searchDAO.flushAndClearCache();
		}
	}
	
	//returns cost categories produced by a given cost curve
	public Collection getCostCategories(long costCurveId){		
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("id.costCurveId", SearchCondition.OPERATOR_EQ, new Long(costCurveId)));
		Collection costCategoryList = searchDAO.getSearchList(CostCurveCategoryRef.class, scs2);
		return costCategoryList;
	}
	
	public long getCostCurveId(String costCurveCode){
		long costCurveId = -1;
		SearchConditions scs =  new SearchConditions(new SearchCondition("code", SearchCondition.OPERATOR_EQ, costCurveCode));
		Object ccRefObj = searchDAO.getSearchObject(CostCurveRef.class, scs);
		if (ccRefObj!=null){
			CostCurveRef ccRef = (CostCurveRef)ccRefObj;
			costCurveId = ccRef.getCostCurveId();
		}
		return costCurveId;
	}

	private CostCurveInputProcessorFactory costCurveInputProcessorFactory;  
	public void setCostCurveInputProcessorFactory(
			CostCurveInputProcessorFactory ccipf) {
		costCurveInputProcessorFactory = ccipf;
	}
	
	private CostCurveAlgorithmFactory costCurveAlgorithmFactory;
	public void setCostCurveAlgorithmFactory(
			CostCurveAlgorithmFactory costCurveAlgorithmFactory) {
		this.costCurveAlgorithmFactory = costCurveAlgorithmFactory;
	}  
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	
	private FacilityDAO facilityDAO;
	public void setFacilityDAO(FacilityDAO dao){
		facilityDAO = dao;
	}
	
	//***
    private FESManager fesManager;
    public void setFesManager(FESManager fesm){
    	fesManager=fesm;
    } 
	

	
	public void runCostCurve(Long facilityId, String userId) throws CostCurveException {		
		//1. get all the facility costcurve wheres cost curve rerun flag is set to Y and and all the dat datea errors set to N
		Collection facilityCostCurves=getFacilityCostCurvesWithRerunAndNoErrors(facilityId);
		
		for (Iterator iter = facilityCostCurves.iterator(); iter.hasNext();) {
			FacilityCostCurve fcc = (FacilityCostCurve) iter.next();					
			String costCurveCode = fcc.getCostCurveRef().getCode();
			Long facId = new Long(fcc.getFacilityDocument().getFacility().getFacilityId());
			long fccId = fcc.getFacilityCostCurveId();
			long documentId = fcc.getFacilityDocument().getDocument().getDocumentId();
			//System.out.println("facilityId=" + facId.longValue() + " faccId="+fccId);


			//run the cost curve
			CostCurveOutput cco = null;
			try{				
				cco = (CostCurveOutput)runCostCurve(costCurveCode, facId);
			}
			catch(CostCurveException e){
				throw new ApplicationException("Error running costcurves");
			}
			
			//iterate over cost output and update needs
			List costOutputs = cco.getCostOutputs();			
	   		Iterator iterCO = costOutputs.iterator();
	    	while ( iterCO.hasNext() ) {
	    		CostOutput co = (CostOutput)iterCO.next();
	    		
	    		//update the needs
	    		String catId = co.getCategoryID();
	    		long baseAmount = co.getCost();
	    		
	    		if(baseAmount == 0)
	    			continue;
	    		//X = Cost_index_ref.monthly_index where index_month and index_year = costCurveRef.base_design_date
	    		//Y = Cost_index_ref.monthly_index where base_month_flag = Y
	    		//-> cost_adjusted_amount = cost.base+amount * X / Y
	    		
	    		long adjustedAmount = 0;
	    		double baseMonthlyIndex = (double)getBaseMonthlyIndex();
	    		double ccMonthlyIndex = (double)getCostCurveMonthlyIndex(costCurveCode);
	    		
	    		if(ccMonthlyIndex!=0 && !costCurveCode.equals("NSI") && !costCurveCode.equals("NSS")){    			
	    			adjustedAmount = Math.round((double)baseAmount * baseMonthlyIndex / ccMonthlyIndex);
	    		}
	    		
	    		if(baseMonthlyIndex!=0 && (costCurveCode.equals("NSI") || costCurveCode.equals("NSS"))){    			
	    			adjustedAmount = Math.round((double)baseAmount * ccMonthlyIndex / baseMonthlyIndex);
	    		}	    		
	    		saveOrUpdateCostByFCCandCostCat(facId.intValue(), documentId, fccId, catId, 
	    									baseAmount, adjustedAmount, userId);
	    	}
	    	//update facility flow
	    	float flowRate = cco.getFutureMunicipalFlowRate();
	    	if(flowRate!=0){
		    	updateMunicipalflow(facId, flowRate, userId);	
	    	}
	    	
    		//update facility costcurve
    		FacilityCostCurve fcc1 = (FacilityCostCurve)searchDAO.getObject(FacilityCostCurve.class, new Long(fccId));    		
    		fcc1.setAssignedOrRunCode('R');
    		fcc1.setCurveRerunFlag('N');
    		fcc1.setLastUpdateTs(new Date());
    		fcc1.setLastUpdateUserid(userId);
    		searchDAO.saveObject(fcc1);
    		
    		//update facility Time Stamp
    		Facility f = facilityDAO.findByFacilityId(facId.toString());
    		if(f!=null){
    			f.setLastUpdateTs(new Date());
    			f.setLastUpdateUserid(userId);
    			searchDAO.saveObject(f);
    		}    		
    		fesManager.runValidation(facId, FacilityService.DATA_AREA_NEEDS, userId);
		}
	}
	
	private short getCostCurveMonthlyIndex(String costCurveCode){		
		int docMonthlyIndex = 0;
		int ccDesignMonth = 0;
		int ccDesignYear = 0;
		SearchConditions scs = new SearchConditions(new SearchCondition("code", SearchCondition.OPERATOR_EQ, costCurveCode));
		Object objCostCurveRef = searchDAO.getSearchObject(CostCurveRef.class, scs);
		if(objCostCurveRef!=null){
			CostCurveRef costCurveRef = (CostCurveRef)objCostCurveRef;
			Date baseDesignDate = costCurveRef.getBaseDesignDate();
			if (baseDesignDate!=null){
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(baseDesignDate);
				ccDesignMonth = calendar.get(Calendar.MONTH) + 1;
				ccDesignYear = calendar.get(Calendar.YEAR);
			}
		}
		
		SearchConditions scs2 = new SearchConditions(new SearchCondition("indexMonth", SearchCondition.OPERATOR_EQ, new Byte(new Integer(ccDesignMonth).byteValue())));
		scs2.setCondition(new SearchCondition("indexYear", SearchCondition.OPERATOR_EQ, new Short(new Integer(ccDesignYear).shortValue())));
		Object objCostIndexRef = searchDAO.getSearchObject(CostIndexRef.class, scs2);
		if (objCostIndexRef!=null){
			docMonthlyIndex = ((CostIndexRef)objCostIndexRef).getMonthlyIndex();
		}	
		return (short)docMonthlyIndex;		
	}
	
	private short getBaseMonthlyIndex(){
		int monthlyIndex = 0;
		SearchConditions scs = new SearchConditions(new SearchCondition("baseMonthFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		Object objCostIndexRef = searchDAO.getSearchObject(CostIndexRef.class, scs);
		if (objCostIndexRef!=null){
			monthlyIndex = ((CostIndexRef)objCostIndexRef).getMonthlyIndex();
		}		
		return (short)monthlyIndex;		
	}
	
	private void updateMunicipalflow(Long facilityId, float flowRate, String userId){
		FacilityFlow municipalFlow =  getFlow(facilityId, FlowService.FLOW_TYPE_MUNICIPAL);
		float currentMunicipalFlowRate =0;
		if(municipalFlow !=null){
			if(municipalFlow.getProjectedFlowMsr()!=null){
				currentMunicipalFlowRate = municipalFlow.getProjectedFlowMsr().floatValue();
			}	
		}else{
			municipalFlow= createFacilityFlow(facilityId.longValue(), FlowService.FLOW_TYPE_MUNICIPAL.longValue(), flowRate, userId);
		}	
		municipalFlow.setProjectedFlowMsr(new BigDecimal(flowRate));
		municipalFlow.setLastUpdateTs(new Date());
		municipalFlow.setLastUpdateUserid(userId);
		searchDAO.saveObject(municipalFlow);
		UpdateTotalFlow(facilityId, flowRate, currentMunicipalFlowRate, userId);
	}
	
	private void UpdateTotalFlow(Long facilityId, float flowRate, float currentMunicipalFlowRate, String userId){
		FacilityFlow totalFlow =getFlow(facilityId, FlowService.FLOW_TYPE_TOTAL);
		float currentTotalFlowRate=0;
		if(totalFlow!=null){
			if(totalFlow.getProjectedFlowMsr()!=null){
				currentTotalFlowRate=totalFlow.getProjectedFlowMsr().floatValue();
				totalFlow.setProjectedFlowMsr(new BigDecimal(currentTotalFlowRate-currentMunicipalFlowRate+flowRate));
			}else{
				totalFlow.setProjectedFlowMsr(new BigDecimal(flowRate));
			}
		}else{
			totalFlow= createFacilityFlow(facilityId.longValue(), FlowService.FLOW_TYPE_TOTAL.longValue(), flowRate, userId);
		}
		totalFlow.setLastUpdateTs(new Date());
		totalFlow.setLastUpdateUserid(userId);
		searchDAO.saveObject(totalFlow);
	}
	
	
	private FacilityFlow createFacilityFlow(long facilityId, long flowId, float flowRate, String userId){
		FacilityFlow facilityFlow = new FacilityFlow();
		FacilityFlowId id = new FacilityFlowId(facilityId, flowId);
		facilityFlow.setId(id);
		facilityFlow.setProjectedFlowMsr(new BigDecimal(flowRate));
		return facilityFlow;
	}
	
	private FacilityFlow getFlow(Long facilityId, Long flowTypeId){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.flowTypeId", SearchCondition.OPERATOR_EQ, flowTypeId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		return (FacilityFlow)searchDAO.getSearchObject(FacilityFlow.class, scs);		
	}
	
	private double getMonthlyCostIndexAmt(short year, byte month, char baseMonthFlag)
	{
		double result = 1.0;
		
		SearchConditions scs = null;
		Collection results = null;
		
		if(baseMonthFlag=='Y')
		{
			scs = new SearchConditions(new SearchCondition("baseMonthFlag", 
											SearchCondition.OPERATOR_EQ, new Character(baseMonthFlag)));
	
			// since baseMonthFlag is not unique in DB, assume a collection and use the first result 
			results = searchDAO.getSearchList(CostIndexRef.class, scs);
			
			if(results.iterator().hasNext())
			{
				return (double)((CostIndexRef)(results.iterator().next())).getMonthlyIndex();
			}
			else
			{
				// data setup error - throw error
			}
		}

		byte baseMonth = month;
		short baseYear = year;

		scs = new SearchConditions(new SearchCondition("indexMonth", 
										SearchCondition.OPERATOR_EQ, new Byte(baseMonth)));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, 
							new SearchCondition("indexYear", SearchCondition.OPERATOR_EQ, 
									new Short(baseYear)));

		// since indexMonth + indexYear is not unique in DB, assume a collection and use the first result 
		results = searchDAO.getSearchList(CostIndexRef.class, scs);
		
		if(results.iterator().hasNext())
		{
			result = (double)((CostIndexRef)(results.iterator().next())).getMonthlyIndex();
		}
		
		return result;
	}
	
	private void saveOrUpdateCostByFCCandCostCat(long facilityId, long documentId, long fccId, String costCatId, long baseAmt, long adjustedAmt, String user)
	{
		SearchConditions scs = new SearchConditions(
			    new SearchCondition("categoryRef.categoryId", SearchCondition.OPERATOR_EQ, costCatId));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("facilityCostCurve.facilityCostCurveId", SearchCondition.OPERATOR_EQ, new Long(fccId)));

		// assuming multiple records found, only update the first one
		Collection costList = searchDAO.getSearchList(Cost.class, scs);
		
		Cost cost = null;

   		Iterator iterCost = costList.iterator();

    	if ( iterCost.hasNext() ) {
    		cost = (Cost)iterCost.next();
    	}
    	else
    	{
    		// new Cost
    		cost = new Cost();

            SearchConditions scs3 = new SearchConditions(
    			    new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
    			    					new FacilityDocumentId(documentId, facilityId)));

            FacilityDocument fd = (FacilityDocument) searchDAO.getSearchObject(FacilityDocument.class, scs3);

            if (fd==null){
            	// something seriously wrong - throw exception
            }        
            
            cost.setFacilityDocument(fd);                

          }
    	
    	SearchConditions scs0 = new SearchConditions(new SearchCondition("categoryId", SearchCondition.OPERATOR_EQ, costCatId));
    	CategoryRef cr = (CategoryRef) searchDAO.getSearchObject(CategoryRef.class, scs0);	
        cost.setCategoryRef(cr);

    	SearchConditions scs1 = new SearchConditions(new SearchCondition("needTypeId", SearchCondition.OPERATOR_EQ, "F"));
    	NeedTypeRef ntr = (NeedTypeRef) searchDAO.getSearchObject(NeedTypeRef.class, scs1);	
        cost.setNeedTypeRef(ntr);
        
        cost.setFacilityCostCurve((FacilityCostCurve)searchDAO.getObject(FacilityCostCurve.class, new Long(fccId)));
        
        cost.setAdjustedAmount(adjustedAmt);
        cost.setBaseAmount(baseAmt);
        cost.setCostMethodCode('C');

        cost.setFeedbackDeleteFlag('N');
        cost.setLastUpdateTs(new Date());
        cost.setLastUpdateUserid(user);

        searchDAO.saveObject(cost);    	

	}
}
