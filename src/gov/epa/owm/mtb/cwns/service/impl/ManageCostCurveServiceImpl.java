package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveException;
import gov.epa.owm.mtb.cwns.costcurve.CostCurveOutput;
import gov.epa.owm.mtb.cwns.costcurve.CostOutput;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.fes.FESManager;
import gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveErrorsHelper;
import gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveForm;
import gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveListHelper;
import gov.epa.owm.mtb.cwns.manageCostCurve.ManageCostCurveViewAllCatVListHelper;
import gov.epa.owm.mtb.cwns.model.CategoryRef;
import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.CostCurveCategoryRef;
import gov.epa.owm.mtb.cwns.model.CostCurveRef;
import gov.epa.owm.mtb.cwns.model.CostIndexRef;
import gov.epa.owm.mtb.cwns.model.DataAreaRef;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataArea;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataAreaId;
import gov.epa.owm.mtb.cwns.model.FacilityDataAreaMessage;
import gov.epa.owm.mtb.cwns.model.FacilityDocument;
import gov.epa.owm.mtb.cwns.model.FacilityDocumentId;
import gov.epa.owm.mtb.cwns.model.FacilityPopulation;
import gov.epa.owm.mtb.cwns.model.FacilityPopulationUnit;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChange;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChangeCcRef;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChangeRefId;
import gov.epa.owm.mtb.cwns.model.NeedTypeRef;
import gov.epa.owm.mtb.cwns.model.PopulationRef;
import gov.epa.owm.mtb.cwns.navigationTabs.NavigationTabsListHelper;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ManageCostCurveService;
import gov.epa.owm.mtb.cwns.service.NavigationTabsService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ManageCostCurveServiceImpl extends CWNSService implements ManageCostCurveService {
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	
	private FacilityService faclityService;
	public void setFacilityService(FacilityService fs) {
		faclityService = fs;
	}
	
	private CostCurveService costCurveService;

	public void setCostCurveService(CostCurveService ccs) {
		costCurveService = ccs;
	}
	
	private FESManager fesManager;
	public void setFesManager(FESManager fesManager) {
		this.fesManager = fesManager;
	}

	private PopulationService populationService;

	public void setPopulationService(PopulationService ps) {
		populationService = ps;
	}
	
	private NavigationTabsService navigationTabsService;

	public void setNavigationTabsService(NavigationTabsService ns) {
		navigationTabsService = ns;
	}	
	
	public Collection getCatVCostForFacilitiesInSewershed(long facilityId)
	{
		ArrayList catVList = new ArrayList();
		
		Collection allFacilitiesInSewershed = populationService.getRelatedSewerShedFacilities(new Long(facilityId).toString());

		Facility fac = faclityService.findByFacilityId(new Long(facilityId).toString());
		
		if(allFacilitiesInSewershed != null && allFacilitiesInSewershed.size() > 0 && fac != null)
		{
	    	Iterator iterFac = allFacilitiesInSewershed.iterator();
	      	
	    	while ( iterFac.hasNext() ) 
	    	{
	    		String facId = (String)iterFac.next();
	    		
	    		Facility objFac = faclityService.findByFacilityId(facId);
  		    		
	    		// checking CSO
	    		ArrayList aliasArray = new ArrayList();
	    		aliasArray.add(new AliasCriteria("combinedSewerStatusRef", "cssr", AliasCriteria.JOIN_INNER));	

	    		SearchConditions scs = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facId)));

	    		CombinedSewer cso = (CombinedSewer)searchDAO.getSearchObject(CombinedSewer.class, scs, aliasArray);
	    		
	    		if(cso==null || cso.getCombinedSewerStatusRef() == null || 
	    				cso.getCombinedSewerStatusRef().getCombinedSewerStatusId() == null ||
	    				!(cso.getCombinedSewerStatusRef().getCombinedSewerStatusId().equalsIgnoreCase("B") ||
	    						cso.getCombinedSewerStatusRef().getCombinedSewerStatusId().equalsIgnoreCase("C")))
	    		{
	    			continue;
	    		}
	    		
	    		ManageCostCurveViewAllCatVListHelper mcc = new ManageCostCurveViewAllCatVListHelper();
	    			    		
	    		mcc.setCwnsNumber(objFac.getCwnsNbr());
	    		mcc.setFacilityName(objFac.getName());

	    		ArrayList columns = new ArrayList();
	    		columns.add("adjustedAmount");		

	    		aliasArray = new ArrayList();
	    		aliasArray.add(new AliasCriteria("categoryRef", "cr", AliasCriteria.JOIN_INNER));	
	    		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));	
	    		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
	    		aliasArray.add(new AliasCriteria("fd.document", "d", AliasCriteria.JOIN_INNER));
	    		aliasArray.add(new AliasCriteria("d.documentTypeRef", "dtr", AliasCriteria.JOIN_INNER));

	    		scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facId)));
	    		scs.setCondition(SearchCondition.OPERATOR_AND, 
	    				new SearchCondition("dtr.documentTypeId", SearchCondition.OPERATOR_EQ, "98"));				        				
	    		scs.setCondition(SearchCondition.OPERATOR_AND, 
	    				new SearchCondition("cr.categoryId", SearchCondition.OPERATOR_EQ, "V"));				        				

	    		Collection docList = searchDAO.getSearchList(Cost.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);

	    		Iterator iter = docList.iterator();
	    		
	    		if ( iter.hasNext() )
	    		{
	        		Object obj = (Object)iter.next();
	        		
	        		mcc.setAdjustedAmount(((Long)obj).longValue());
	    		}
	    		
	    		catVList.add(mcc);
	    	} // while sewershed ids			
		}

		return catVList;
	}
	
	public void deleteCostCurve(long facilityId, long facilityCostCurveId, CurrentUser user)
	{

		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("facilityCostCurveId", SearchCondition.OPERATOR_EQ, new Long(facilityCostCurveId)));
		
	    FacilityCostCurve facilityCostCurve = (FacilityCostCurve)searchDAO.getSearchObject(FacilityCostCurve.class, scs4);
	    
   	    if(facilityCostCurve!=null)
   	    {
	        searchDAO.removeObject(facilityCostCurve);
	        faclityService.performPostSaveUpdates(new Long(facilityId), faclityService.DATA_AREA_NEEDS, user);
   	    }
   	    
		return;
	}
	
	public void saveOrUpdateCostCurve(ManageCostCurveForm form, CurrentUser user)
	{
		boolean dataDirtyFlag = false;
		
		HashSet newSet = convertArrayToHashSet(form.getAssignedFacilityCostCurveIds());
		HashSet originSet = convertArrayToHashSet(form.getUpdatedFacilityCostCurveIds());
		
		HashSet newSetCopy = convertArrayToHashSet(form.getAssignedFacilityCostCurveIds());
		HashSet originSetCopy = convertArrayToHashSet(form.getUpdatedFacilityCostCurveIds());
		
		if(newSet!=null && originSet!=null)
			newSet.removeAll(originSet);
		
		if(newSet!=null)
		{
			Iterator iter = newSet.iterator();
			
			while ( iter.hasNext() ) 
			{
	    		String id = (String)iter.next();	
	    		saveorUpdateFacilityCostCurve(form.getManageCostCurveFacilityId(), 
	    									  form.getDocumentId(), 
	    									  new Long(id).longValue(), 
	    									  'A',
	    									  'Y',
	    									  user);
	    		dataDirtyFlag = true;
			}	
		}
		
		// now removal
		if(originSetCopy!=null && newSetCopy!=null)
			originSetCopy.removeAll(newSetCopy);
		
		if(originSetCopy!=null)
		{
			Iterator iter = originSetCopy.iterator();
			
			while ( iter.hasNext() ) 
			{
	    		String id = (String)iter.next();	

	    		deleteFacilityCostCurve(new Long(id).longValue(), form.getManageCostCurveFacilityId(), form.getDocumentId(), user);
	    		
	    		dataDirtyFlag = true;
			}	
		}

		if(dataDirtyFlag)
		{
            faclityService.performPostSaveUpdates(new Long(form.getManageCostCurveFacilityId()), faclityService.DATA_AREA_NEEDS, user);	
            //runCostCurveByFacilityDocument(form.getManageCostCurveFacilityId(), form.getDocumentId(), user.getUserId());
		}
	}
	
	private HashSet convertArrayToHashSet(Object[] A)
	{
		if(A==null || A.length == 0)
			return null;
		
		HashSet hs = new HashSet();
		
		for (int i=0; i<A.length; i++)
		{
			if(A[i] !=null)
				hs.add(A[i]);
		}
		
		return hs;
	}
	
	public void runCostCurveByFacilityDocument(long facilityId, long documentId, String user)
	{
		ArrayList columns = new ArrayList();
		
		columns.add("facilityCostCurveId");
		columns.add("ccr.costCurveId");
		columns.add("ccr.code");
		
		ArrayList aliasArrayCat = new ArrayList();
		aliasArrayCat.add(new AliasCriteria("costCurveRef", "ccr", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityDocument.id", SearchCondition.OPERATOR_EQ, 
				new FacilityDocumentId(documentId, facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("curveRerunFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("ccr.costCurveId", SearchCondition.OPERATOR_NOT_EQ, new Long(7)));
		
		Collection ccList = (Collection)searchDAO.getSearchList(FacilityCostCurve.class, columns, scs, new ArrayList(), aliasArrayCat);
		
		Iterator iter = ccList.iterator();

    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    		
    		long fccId = ((Long)obj[0]).longValue();
    		String ccrCode = (String)obj[2];
    		
    		if(facilityCostCurveDataAreaNoError(fccId))
    		{
    			CostCurveOutput cco = null;
    			
    			try
    			{
    				cco = (CostCurveOutput)costCurveService.runCostCurve(ccrCode, new Long(facilityId));
    			}
    			catch(CostCurveException e)
    			{
    				// log error here
    				continue;
    			}
    			
    			List costOutputs = cco.getCostOutputs();
    			
    	   		Iterator iterCO = costOutputs.iterator();

            	while ( iterCO.hasNext() ) {
            		CostOutput co = (CostOutput)iterCO.next();
            		
            		String catId = co.getCategoryID();
            		long baseAmount = co.getCost();
            		
            		if(baseAmount == 0)
            			continue;
            		
            		//X = Cost_index_ref.monthly_index where index_month and index_year = document.base_design_date
            		//Y = Cost_index_ref.monthly_index where base_month_flag = Y
            		//-> cost_adjusted_amount = cost.base+amount * X / Y
            		
            		long adjustedAmount = 
            			(long)(baseAmount * getMonthlyCostIndexAmt((short)2008, (byte)1, 'Y') / getMonthlyCostIndexAmt((short)1998, (byte)1, 'N'));

            		saveOrUpdateCostByFCCandCostCat(facilityId, documentId, fccId, catId, 
            									baseAmount, adjustedAmount, user);
            		
            		FacilityCostCurve fcc1 = (FacilityCostCurve)searchDAO.getObject(FacilityCostCurve.class, new Long(fccId));
            		
            		fcc1.setAssignedOrRunCode('R');
            		fcc1.setCurveRerunFlag('N');
            		fcc1.setLastUpdateTs(new Date());
            		fcc1.setLastUpdateUserid(user);
            		
            		searchDAO.saveObject(fcc1);
            	}
    		}
    	}	
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
	
	private boolean facilityCostCurveDataAreaNoError(long fccId)
	{
		boolean result = true;
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityCostCurve.facilityCostCurveId", SearchCondition.OPERATOR_EQ, 
				new Long(fccId)));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("errorFlag", SearchCondition.OPERATOR_NOT_EQ, new Character('N')));				        				
		
		Collection fccdaList = searchDAO.getSearchList(FacilityCostCurveDataArea.class, scs);
		
		if(fccdaList!=null && fccdaList.size() > 0)
			result = false;
		
		return result;
	}
	
	private void deleteFacilityCostCurve(long costCurveId, long facilityId, long documentId, CurrentUser user)
	{
		long seachCCId = costCurveId;
		if(costCurveId==3) seachCCId=8;
		
		ArrayList pcolumns = new ArrayList();
		pcolumns.add("populationId");		

		ArrayList paliasArray = new ArrayList();
		paliasArray.add(new AliasCriteria("costCurveRef", "ccr", AliasCriteria.JOIN_INNER));	

		SearchConditions pscs = new SearchConditions(new SearchCondition("ccr.costCurveId", SearchCondition.OPERATOR_EQ, 
				new Long(seachCCId)));

		Collection pList = searchDAO.getSearchList(PopulationRef.class, pcolumns, pscs, new ArrayList(), paliasArray, 0, 0, true);
				
		if(pList!=null && pList.size()>0)
		{			
			boolean deleteFacilityPopulation = true;
			if(costCurveId==3 || costCurveId==8){
				long otherCostCurveId = costCurveId==3?8:3;
				SearchConditions cc_scs = new SearchConditions(new SearchCondition("facilityDocument.id.facilityId", SearchCondition.OPERATOR_EQ, 
						new Long(facilityId)));
				cc_scs.setCondition(new SearchCondition("costCurveRef.costCurveId", SearchCondition.OPERATOR_EQ, 
						new Long(otherCostCurveId)));
				Object otherCC = searchDAO.getSearchObject(FacilityCostCurve.class, cc_scs); 
				
				if (otherCC!=null){
					deleteFacilityPopulation = false;
				}
			}
		
		//delete facility_population_unit
		if (deleteFacilityPopulation) {			
			SearchConditions fp_scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, 
					new Long(facilityId)));

			fp_scs.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_IN, pList)); 		
			Collection fpList = searchDAO.getSearchList(FacilityPopulation.class, fp_scs);

			if(fpList!=null && fpList.size() > 0)
				searchDAO.deleteAll(fpList);
		}

		//delete facility_population_unit			
		SearchConditions fpu_scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		
		fpu_scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("populationRef.populationId", SearchCondition.OPERATOR_IN, pList)); 		
		Collection fpuList = searchDAO.getSearchList(FacilityPopulationUnit.class, fpu_scs);
		
		if(fpuList!=null && fpuList.size() > 0)
			searchDAO.deleteAll(fpuList);
		}
		
		//delete facilityCostCurveDataArea
    	ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("costCurveRef", "ccr", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("fd.id", SearchCondition.OPERATOR_EQ, 
				new FacilityDocumentId(documentId, facilityId)));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("ccr.costCurveId", SearchCondition.OPERATOR_EQ,new Long(costCurveId)));				        				
		
		FacilityCostCurve fcc = (FacilityCostCurve)searchDAO.getSearchObject(FacilityCostCurve.class, scs, aliasArray);
		
		if(fcc==null) // TODO: should throw exception for data error
			return;
		
		long fccId = fcc.getFacilityCostCurveId();
		
		scs = new SearchConditions(new SearchCondition("facilityCostCurve.facilityCostCurveId", SearchCondition.OPERATOR_EQ, 
				new Long(fccId)));
		
		Collection fccdaList = searchDAO.getSearchList(FacilityCostCurveDataArea.class, scs);

		if(fccdaList!=null && fccdaList.size() > 0)
			for (Iterator iter = fccdaList.iterator(); iter.hasNext();) {
				FacilityCostCurveDataArea fccda = (FacilityCostCurveDataArea) iter.next();
				deleteCostCurveMessages(fccda);
				searchDAO.removeObject(fccda);
			} 
		
		//delete cost
		scs = new SearchConditions(new SearchCondition("facilityDocument.id", SearchCondition.OPERATOR_EQ, 
				new FacilityDocumentId(documentId, facilityId)));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("facilityCostCurve.facilityCostCurveId", SearchCondition.OPERATOR_EQ,new Long(fccId)));				        				
		
		Collection costList = searchDAO.getSearchList(Cost.class, scs);
		
		if(costList!=null && costList.size() > 0)
			searchDAO.deleteAll(costList);
		
		//delete facilityCostCurve
		searchDAO.removeObject(fcc);
	}
	
	//delete ccv messages
	private void deleteCostCurveMessages(FacilityCostCurveDataArea fccda){
		long facilityId = fccda.getFacilityCostCurve().getFacilityDocument().getId().getFacilityId();
		long dataAreaId= fccda.getId().getDataAreaId();
		Collection ccvErrors = getFacilityDataAreaMessages(new Long(facilityId), new Long(dataAreaId));
		if(ccvErrors!=null && !ccvErrors.isEmpty()){
			searchDAO.deleteAll(ccvErrors);	
		}
	}
	
	public Collection getFacilityDataAreaMessages(Long facilityId, Long dataAreaId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		scs.setCondition(new SearchCondition ("sourceCode", SearchCondition.OPERATOR_EQ, "ccv"));		
		return searchDAO.getSearchList(FacilityDataAreaMessage.class, scs);
	}
	
	private void saveorUpdateFacilityCostCurve(long facilityId, long documentId, long costCurveId, 
												char assignOrRunCode, char rerunFlag, CurrentUser user)
	{

    	ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("costCurveRef", "ccr", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("fd.id", SearchCondition.OPERATOR_EQ, 
				new FacilityDocumentId(documentId, facilityId)));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("ccr.costCurveId", SearchCondition.OPERATOR_EQ,new Long(costCurveId)));				        				
		
		FacilityCostCurve fcc = (FacilityCostCurve)searchDAO.getSearchObject(FacilityCostCurve.class, scs, aliasArray);
		
		boolean isNew = false;
		
		if(fcc == null)
		{
			fcc = new FacilityCostCurve();
			
			SearchConditions scs1 = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
					new FacilityDocumentId(documentId, facilityId)));

			fcc.setCostCurveRef((CostCurveRef)searchDAO.getObject(CostCurveRef.class, new Long(costCurveId)));
			fcc.setFacilityDocument((FacilityDocument)searchDAO.getSearchObject(FacilityDocument.class, scs1));
			
			isNew = true;
		}
		
		fcc.setAssignedOrRunCode(assignOrRunCode);
		fcc.setCurveRerunFlag(rerunFlag);
		fcc.setLastUpdateTs(new Date());
		fcc.setLastUpdateUserid(user.getUserId());
		
		searchDAO.saveObject(fcc);
		
		if(isNew)
		{
			long fccId = fcc.getFacilityCostCurveId();
			Collection availableDataAreas = navigationTabsService.findDataAreaIdsByFacilityId(new Long(facilityId).toString());	
    		Iterator iterNT = availableDataAreas.iterator();

        	while ( iterNT.hasNext() ) {
        		Long daId = (Long)iterNT.next();
        		
        		FacilityCostCurveDataArea fccda = new FacilityCostCurveDataArea();
        		fccda.setId(new FacilityCostCurveDataAreaId(fccId, daId.longValue()));
        		fccda.setErrorFlag('N');
        		fccda.setFacilityCostCurve(fcc);
        		fccda.setDataAreaRef((DataAreaRef)searchDAO.getObject(DataAreaRef.class, daId));
        		fccda.setLastUpdateTs(new Date());
        		fccda.setLastUpdateUserid(user.getUserId());
        		
        		searchDAO.saveObject(fccda);
        	}
		}
	
	}
	
	private String getDocumentAprovedForNeeds(long documentId)
	{
		
		ArrayList columns = new ArrayList();
		columns.add("dtr.aprovedForNeedsFlag");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("documentTypeRef", "dtr", AliasCriteria.JOIN_INNER));	

		SearchConditions scs = new SearchConditions(new SearchCondition("documentId", SearchCondition.OPERATOR_EQ, 
				new Long(documentId)));

		Collection docList = searchDAO.getSearchList(Document.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);

		Iterator iter = docList.iterator();
		
		if ( iter.hasNext() )
		{
    		Object obj = (Object)iter.next();
    		
    		return obj==null?"":((Character)obj).toString();
		}

		return "";
	}
	
	private Collection getCostCategories(long facilityId)
	{
		ArrayList columns = new ArrayList();
		columns.add("cr.categoryId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("categoryRef", "cr", AliasCriteria.JOIN_INNER));	
		
		SearchConditions scs = new SearchConditions(new SearchCondition("fd.id.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("costMethodCode", SearchCondition.OPERATOR_EQ, new Character('D')));
		
		return searchDAO.getSearchList(Cost.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);	
	}

	
	private boolean costCurveCategoryConflict(long costCurveId, Collection categoryList)
	{
		if(categoryList == null || categoryList.size() == 0)
			return false;
		
		ArrayList columns = new ArrayList();
		columns.add("cr.categoryId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("categoryRef", "cr", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("costCurveRef", "ccr", AliasCriteria.JOIN_INNER));

		SearchConditions scs = new SearchConditions(new SearchCondition("ccr.costCurveId", SearchCondition.OPERATOR_EQ, new Long(costCurveId)));
		
		if(categoryList!=null && categoryList.size()>0)
		{
			scs.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("cr.categoryId", SearchCondition.OPERATOR_IN, categoryList));    			
		}
		Collection catList = searchDAO.getSearchList(CostCurveCategoryRef.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);
		
		if(catList == null || catList.size() == 0)
			return false;
		else
			return true;
		
	}
		
	
	// return a ManageCostCurveListHelper list
	public Collection getCostCurveList(long facilityId, long documentId) 
	{
		
		String approvedForNeeds = getDocumentAprovedForNeeds(documentId);
		
		Collection costCategoryList = getCostCategories(facilityId);
		
		ArrayList manageCostCurveList = new ArrayList();
		ArrayList errorList = null;
		
		ArrayList columns = new ArrayList();
		columns.add("ftr.facilityTypeId");		
		columns.add("ctr.changeTypeId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("changeTypeRef", "ctr", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("facilityType", "ft", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ft.facilityTypeRef", "ftr", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ft.facility", "f", AliasCriteria.JOIN_INNER));			

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));

		Collection facilityTypeIdList = searchDAO.getSearchList(FacilityTypeChange.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);

		boolean group124567assigned = false, hasThree = false, hasFour = false,
			cc11assigned = false, cc1213assigned = false, cc15assigned = false, cc1617assigned = false;		

		Iterator iter34 = facilityTypeIdList.iterator();
		
		while ( iter34.hasNext() ) 
		{
    		Object[] obj = (Object[])iter34.next();		
			
			if(obj[1] !=null && ((Long)obj[1]).longValue() == 3)
				hasThree = true;
			
			if(obj[1] !=null && ((Long)obj[1]).longValue() == 4)
				hasFour = true;	
		}

    	Iterator iter = facilityTypeIdList.iterator();
    	
    	ArrayList ccIdSet = new ArrayList();
  	
    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    
        	columns = new ArrayList();
    		columns.add("ccr.costCurveId");		
    		columns.add("ccr.code");		
    		columns.add("ccr.name");	
    		
        	aliasArray = new ArrayList();
    		aliasArray.add(new AliasCriteria("facilityTypeChangeRef", "ftcr", AliasCriteria.JOIN_INNER));	
    		aliasArray.add(new AliasCriteria("costCurveRef", "ccr", AliasCriteria.JOIN_INNER));
    		
    		scs = new SearchConditions(new SearchCondition("ftcr.id", SearchCondition.OPERATOR_EQ, 
    				new FacilityTypeChangeRefId(((Long)obj[0]).longValue(), ((Long)obj[1]).longValue())));
    		
    		if(!(hasThree && hasFour))
    		{
    			scs.setCondition(SearchCondition.OPERATOR_AND, 
    					new SearchCondition("ccr.costCurveId", SearchCondition.OPERATOR_NOT_EQ, new Long(4)));    			
    		}
    		
    		Collection costCurveList = searchDAO.getSearchList(FacilityTypeChangeCcRef.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);
    		
        	Iterator iterCC = costCurveList.iterator();
          	
        	while ( iterCC.hasNext() ) {
        		Object[] objCC = (Object[])iterCC.next();

        		if(ccIdSet.contains((Long)objCC[0]))
        			continue;
        			
	    		ManageCostCurveListHelper mccListHelper = new ManageCostCurveListHelper();
	    		mccListHelper.setCostCurveName((String)objCC[2]);
	    		mccListHelper.setCostCurveCode((String)objCC[1]);
	    		mccListHelper.setCostCurveId(((Long)objCC[0]).longValue());

	    		//set the default value which may be overwritten by the code below
	            mccListHelper.setCostAllocated("N");	
	            
	            mccListHelper.setDisableCostCurve("N");

            	columns = new ArrayList();
        		columns.add("facilityCostCurveId");			
        		columns.add("assignedOrRunCode");	
        		columns.add("curveRerunFlag");	        
        		columns.add("d.documentId");
        		
            	aliasArray = new ArrayList();
        		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));	
        		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));	
        		aliasArray.add(new AliasCriteria("fd.document", "d", AliasCriteria.JOIN_INNER));	
        		aliasArray.add(new AliasCriteria("costCurveRef", "ccr", AliasCriteria.JOIN_INNER));
        		
        		scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId))); 
        		
    			scs.setCondition(SearchCondition.OPERATOR_AND, 
    					new SearchCondition("ccr.costCurveId", SearchCondition.OPERATOR_EQ,(Long)objCC[0]));				        				
        		
        		Collection facilityCostCurveList = searchDAO.getSearchList(FacilityCostCurve.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);
        		
            	Iterator iterFCC = facilityCostCurveList.iterator();
              	
            	if( iterFCC.hasNext() ) {

            		Object[] objFCC = (Object[])iterFCC.next();
            		
            		mccListHelper.setFacilityCostCurveId(((Long)objFCC[0]).longValue());
            		mccListHelper.setAssignedOrRunCode(((Character)objFCC[1]).toString());
            		mccListHelper.setCurveRerunFlag(((Character)objFCC[2]).toString());
            		mccListHelper.setDocumentId(((Long)objFCC[3]).longValue());
            		
            		if(mccListHelper.getAssignedOrRunCode().equals("R"))
            			mccListHelper.setCostAllocated("Y");
            		else
        	            mccListHelper.setCostAllocated("N");
            		
            		//get all cost categories created by this cost curve
            		columns = new ArrayList();
            		columns.add("id.categoryId");            		
            		scs = new SearchConditions(new SearchCondition("id.costCurveId", SearchCondition.OPERATOR_EQ, (Long)objCC[0]));
            		Collection costCurveCategoryList = searchDAO.getSearchList(CostCurveCategoryRef.class, columns, scs);
            		
            		//get all 'D' costs of the facility where 'D' cost type in costCurveCategoryList
            		scs = new SearchConditions(new SearchCondition("facilityDocument.id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
            		scs.setCondition(SearchCondition.OPERATOR_AND, 
            				new SearchCondition("costMethodCode", SearchCondition.OPERATOR_EQ, new Character('D')));
            		scs.setCondition(SearchCondition.OPERATOR_AND, 
            				new SearchCondition("categoryRef.categoryId", SearchCondition.OPERATOR_IN, costCurveCategoryList));
            		Collection facilityDocCostList = searchDAO.getSearchList(Cost.class, scs);
            		
            		if((mccListHelper.getDocumentId() != documentId) ||
            		  	facilityDocCostList!=null && facilityDocCostList.size()>0)
            			mccListHelper.setDisableCostCurve("Y");
            		else
            			mccListHelper.setDisableCostCurve("N");
            		
            		long ccId = mccListHelper.getCostCurveId();
            		
            		if(ccId == 1 ||  ccId == 2 ||  ccId == 4 ||  ccId == 5 ||  ccId == 6 ||  ccId == 10)
            			group124567assigned = true;
            		else if(ccId == 11)
            			cc11assigned = true;
            		else if(ccId == 12 || ccId == 13)
            			cc1213assigned = true;
            		else if(ccId == 15)
            			cc15assigned = true;
            		else if(ccId == 16 || ccId == 17)
            			cc1617assigned = true;
            		
            		mccListHelper.setCostId(getCostId(facilityId, documentId, mccListHelper.getFacilityCostCurveId()));
            	}
            	else
            	{
            		// this is the costCurve that's yet to be assigned
            		if(costCurveCategoryConflict(mccListHelper.getCostCurveId(), costCategoryList))
            			mccListHelper.setDisableCostCurve("Y");
            		else
            			mccListHelper.setDisableCostCurve("N");
            	}
            	
            	if(!approvedForNeeds.equalsIgnoreCase("Y"))
            		mccListHelper.setDisableCostCurve("Y");

            	if(mccListHelper.getCostCurveId() == 7 && mccListHelper.getDisableCostCurve().equals("N") 
            			&& !isCsoCostCurveValid(facilityId, documentId))
            		mccListHelper.setDisableCostCurve("Y");
            	
            	if(mccListHelper.getCostCurveId() != 7 && mccListHelper.getDisableCostCurve().equals("N")
            			&& getDocumentType(documentId).equals("98"))
            		mccListHelper.setDisableCostCurve("Y");            	
            	
            	if(mccListHelper.getFacilityCostCurveId() > 0)
            		mccListHelper.setErrorDataAreaNames(getCCErrors(mccListHelper.getFacilityCostCurveId(), facilityId));	   
	    		
	    		manageCostCurveList.add(mccListHelper);
	    		
	    		ccIdSet.add((Long)objCC[0]);
        	}
    	}

    	manageCostCurveList = sortCostCurveList(manageCostCurveList, 
    											group124567assigned,
    											cc11assigned,
    											cc1213assigned,
    											cc15assigned,
    											cc1617assigned);
    	
		return manageCostCurveList;
	}
	
	private long getCostId(long facilityId, long documentId, long facCCId)
	{
		long costId = -999;
		
		ArrayList columns = new ArrayList();
		columns.add("costId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("facilityCostCurve", "fcc", AliasCriteria.JOIN_INNER));	

		SearchConditions scs = new SearchConditions(new SearchCondition("fd.id", SearchCondition.OPERATOR_EQ, 
				new FacilityDocumentId(documentId, facilityId)));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("fcc.facilityCostCurveId", SearchCondition.OPERATOR_EQ, 
				new Long(facCCId)));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("costMethodCode", SearchCondition.OPERATOR_EQ, new Character('C')));				        				

		Collection docList = searchDAO.getSearchList(Cost.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);

		Iterator iter = docList.iterator();

    	if( iter.hasNext() ) 
    	{
    		costId = ((Long)iter.next()).longValue();
    	}
		
    	return costId;
		
	}

	private Collection getCCErrors(long facCCId, long facilityId)
	{
		Collection availableDataAreas = navigationTabsService.findDataAreasByFacilityId(new Long(facilityId).toString(), "Needs", false, null);		
				
		ArrayList errorList = new ArrayList();
		
		ArrayList columns = new ArrayList();
		columns.add("da.name");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityCostCurve", "fcc", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("dataAreaRef", "da", AliasCriteria.JOIN_INNER));	
		
		SearchConditions scs = new SearchConditions(new SearchCondition("fcc.facilityCostCurveId", SearchCondition.OPERATOR_EQ, 
				new Long(facCCId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("errorFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));				        				

	    SortCriteria sortCriteriaCat = new SortCriteria("da.name", SortCriteria.ORDER_ASCENDING);	
	    ArrayList sortArrayCat = new ArrayList();
	    sortArrayCat.add(sortCriteriaCat);	  		
		
		Collection docList = searchDAO.getSearchList(FacilityCostCurveDataArea.class, columns, scs, sortArrayCat, aliasArray, 0, 0, true);

		Iterator iter = docList.iterator();

    	while ( iter.hasNext() ) {
    		String dataAreaName = (String)iter.next();
    		
    		Iterator iterNT = availableDataAreas.iterator();

        	while ( iterNT.hasNext() ) {
        		NavigationTabsListHelper nth = (NavigationTabsListHelper)iterNT.next();
        		
        		if(nth.getTabText().indexOf(dataAreaName) >=0)
        		{
            		ManageCostCurveErrorsHelper me = new ManageCostCurveErrorsHelper();
            		me.setErrorDataAreaName(dataAreaName); 				
        			me.setNavTabText(nth.getTabText());    
        			me.setActive(nth.getActive());
        			errorList.add(me);  
        			
        			break;
        		}
        	}  		
    	}
    	
		
		return errorList;
	}
	
	private boolean isCsoCostCurveValid(long facilityId, long documentId)
	{
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("combinedSewerStatusRef", "cssr", AliasCriteria.JOIN_INNER));	

		SearchConditions scs = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));

		CombinedSewer cso = (CombinedSewer)searchDAO.getSearchObject(CombinedSewer.class, scs, aliasArray);
		
		if(cso==null || cso.getCombinedSewerStatusRef() == null || 
				cso.getCombinedSewerStatusRef().getCombinedSewerStatusId() == null ||
				!(cso.getCombinedSewerStatusRef().getCombinedSewerStatusId().equalsIgnoreCase("B") ||
						cso.getCombinedSewerStatusRef().getCombinedSewerStatusId().equalsIgnoreCase("C")))
		{
			return false;
		}

		Collection allFacilitiesInSewershed = populationService.getRelatedSewerShedFacilities(new Long(facilityId).toString());

		Facility fac = faclityService.findByFacilityId(new Long(facilityId).toString());
		
		if(allFacilitiesInSewershed != null && allFacilitiesInSewershed.size() > 0 && fac != null)
		{
	    	Iterator iterFac = allFacilitiesInSewershed.iterator();
	      	
	    	while ( iterFac.hasNext() ) {
	    		String facId = (String)iterFac.next();
	    		
	    		Facility objFac = faclityService.findByFacilityId(facId);
	    		
	    		if(!objFac.getLocationId().equalsIgnoreCase(fac.getLocationId()))
	    		{
	    			return false;
	    		}
	    	}			
		}

		if(!getDocumentType(documentId).equals("98"))
			return false;
				
		return true;

	}
	
	private String getDocumentType(long documentId)
	{
		
		ArrayList columns = new ArrayList();
		columns.add("dtr.documentTypeId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("documentTypeRef", "dtr", AliasCriteria.JOIN_INNER));	

		SearchConditions scs = new SearchConditions(new SearchCondition("documentId", SearchCondition.OPERATOR_EQ, 
				new Long(documentId)));

		Collection docList = searchDAO.getSearchList(Document.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);

		Iterator iter = docList.iterator();
		
		if ( iter.hasNext() )
		{
    		Object obj = (Object)iter.next();
    		
    		return obj==null?"":(String)obj;
		}

		return "";
	}
		
	public String atLeastOneEnabled(Collection ccList)
	{
		if(ccList == null || ccList.size() == 0)
			return "N";
		
    	Iterator iterCC = ccList.iterator();
      	
    	while ( iterCC.hasNext() ) {
    		ManageCostCurveListHelper objCC = (ManageCostCurveListHelper)iterCC.next();
    		
    		if(objCC.getDisableCostCurve().equals("N"))
    			return "Y";
    	}
    		
    	return "N";
	}
	
	private ArrayList sortCostCurveList(ArrayList ccList, 
			boolean group124567assigned,
			boolean cc11assigned,
			boolean cc1213assigned,
			boolean cc15assigned,
			boolean cc1617assigned)
	{
		Object[] ccArray =  ccList.toArray();
		
	    boolean doMore = true;
	    while (doMore) {
		        doMore = false;  // assume this is last pass over array
		        for (int i=0; i<ccArray.length-1; i++) 
		        {
		            if (0 < ((ManageCostCurveListHelper)ccArray[i]).getCostCurveName().compareToIgnoreCase(((ManageCostCurveListHelper)ccArray[i+1]).getCostCurveName())) 
		            {
		               // exchange elements
		            	ManageCostCurveListHelper temp = (ManageCostCurveListHelper)ccArray[i];  
		            	ccArray[i] = ccArray[i+1];  
		            	ccArray[i+1] = temp;
		               
		               doMore = true;  // after an exchange, must look again 
		            }
		        }
	    }
	    
	    ArrayList returnList = new ArrayList();
	    
	    // this is the last chance to massage the list data	    
	    for(int i=0; i<ccArray.length; i++)
	    {
	    	ManageCostCurveListHelper temp1 = (ManageCostCurveListHelper)ccArray[i];
	    	
	    	if(group124567assigned
	    			&& temp1.getFacilityCostCurveId() < 0 
	    			&& (temp1.getCostCurveId() == 1 ||
	    				temp1.getCostCurveId() == 2 ||
	    				temp1.getCostCurveId() == 4 ||
	    				temp1.getCostCurveId() == 5 ||
	    				temp1.getCostCurveId() == 6 ||
	    				temp1.getCostCurveId() == 10
	    		))
	    		temp1.setDisableCostCurve("Y");
	    	
	    	if(cc11assigned && 
	    	   temp1.getFacilityCostCurveId() < 0 &&
	    	   (temp1.getCostCurveId() == 12 ||
	    				temp1.getCostCurveId() == 13))
	    	   temp1.setDisableCostCurve("Y");

	    	if(cc1213assigned && 
	 	    	   temp1.getFacilityCostCurveId() < 0 &&
	 	    	   temp1.getCostCurveId() == 11)
	 	       temp1.setDisableCostCurve("Y");
	    	
	    	if(cc15assigned && 
	 	    	   temp1.getFacilityCostCurveId() < 0 &&
	 	    	   (temp1.getCostCurveId() == 16 ||
	 	    				temp1.getCostCurveId() == 17))
	 	    	   temp1.setDisableCostCurve("Y");

	 	    if(cc1617assigned && 
	 	 	    	   temp1.getFacilityCostCurveId() < 0 &&
	 	 	    	   temp1.getCostCurveId() == 15)
	 	 	       temp1.setDisableCostCurve("Y");

	 	    returnList.add(temp1);
	    }
	    
	    return returnList;
	    
	}
	
	//return a list of String objects
	public Collection getAssignedCostCurveList(ArrayList ccList)
	{
		ArrayList assignedCostCurveList = new ArrayList();
		
		if(ccList == null || ccList.size() == 0)
			return assignedCostCurveList;
		
    	Iterator iterCC = ccList.iterator();
      	
    	while ( iterCC.hasNext() ) {
    		ManageCostCurveListHelper objCC = (ManageCostCurveListHelper)iterCC.next();
    		
    		if(objCC.getDocumentId() > 0)
    			assignedCostCurveList.add(objCC.getCostCurveId()+"");    			
    	}		

		return assignedCostCurveList;		
	}


}