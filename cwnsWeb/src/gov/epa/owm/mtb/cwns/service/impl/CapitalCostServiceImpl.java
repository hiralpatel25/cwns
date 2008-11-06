package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.capitalCost.CapitalCostForm;
import gov.epa.owm.mtb.cwns.capitalCost.CapitalCostListHelper;
import gov.epa.owm.mtb.cwns.capitalCost.CategoryClassificationHelper;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.CategoryClassificationRef;
import gov.epa.owm.mtb.cwns.model.CategoryRef;
import gov.epa.owm.mtb.cwns.model.ClassificationRef;
import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.CostCurveCategoryRef;
import gov.epa.owm.mtb.cwns.model.CostIndexRef;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityDocument;
import gov.epa.owm.mtb.cwns.model.FacilityDocumentId;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeCategoryRef;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChange;
import gov.epa.owm.mtb.cwns.model.NeedTypeRef;
import gov.epa.owm.mtb.cwns.service.CapitalCostService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CapitalCostServiceImpl extends CWNSService implements CapitalCostService {
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	private FacilityService faclityService;

	public void setFacilityService(FacilityService fs) {
		faclityService = fs;
	}

	public boolean checkUniqueCombination(String combinedIds)
	{
		String [] ids = new String[20];
		
		int i=0;
		
		int position = -1;

		while(true)
		{
			position = combinedIds.indexOf("|");
			
			if(position < 0)
			{
				ids[i++] = combinedIds;
				break;
			}
			else if(position >= 0)
			{
				ids[i++] = combinedIds.substring(0, position);
				
				if(position + 1 < combinedIds.length())
				combinedIds = combinedIds.substring(position+1);
			}
		}

		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("categoryRef.categoryId", SearchCondition.OPERATOR_EQ, ids[0]));
		
		if(ids[1] == null || ids[1].trim().equals(""))
		{
			scs4.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("classificationRef.classificationId", SearchCondition.OPERATOR_IS_NULL, null));					
		}
		else
		{
			scs4.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("classificationRef.classificationId", SearchCondition.OPERATOR_EQ, ids[1]));			
		}
		
		scs4.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("needTypeRef.needTypeId", SearchCondition.OPERATOR_EQ, ids[2]));
		
		if(ids[3] == null || ids[3].trim().equals(""))
		{
			scs4.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("ssoFlag", SearchCondition.OPERATOR_IS_NULL, null));					
		}
		else
		{
			scs4.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("ssoFlag", SearchCondition.OPERATOR_EQ, ids[3]));			
		}
		
		scs4.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("facilityDocument.id", 
    					SearchCondition.OPERATOR_EQ, 
    					new FacilityDocumentId(new Long(ids[5]).longValue(), new Long(ids[4]).longValue())));		

		SearchConditions scs2 = new SearchConditions(new SearchCondition("categoryRef.categoryId", SearchCondition.OPERATOR_NOT_EQ, "XII"));
		scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("costMethodCode", SearchCondition.OPERATOR_NOT_EQ, new Character('C')));

		scs4.setCondition(SearchCondition.OPERATOR_AND, scs2);

		int count = searchDAO.getCount(Cost.class, scs4);
		
		if(count > 0)
			return false;
		else
			return true;
	}
	
	public Document getDocument(long documentId)
	{
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("documentTypeRef", "dt", AliasCriteria.JOIN_LEFT));			
		
		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("documentId", SearchCondition.OPERATOR_EQ, new Long(documentId)));
				
		Document d = (Document)searchDAO.getSearchObject(Document.class, scs4, aliasArray);		
		
		return d;
	}
	
	public String getFacilityPrivateAllowFederal(long facilityId)
	{
		ArrayList columns = new ArrayList();
		columns.add("f.ownerCode");	
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));	

		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs4.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("f.ownerCode", SearchCondition.OPERATOR_EQ, "PRI"));
		
		Collection ft = (Collection)searchDAO.getSearchList(FacilityType.class, columns, scs4, new ArrayList(), aliasArray);	
		
		if(ft!=null && ft.size() > 0)
		{
			columns.add("ftr.federalNeedsForPrivateFlag");	
			aliasArray.add(new AliasCriteria("facilityTypeRef", "ftr", AliasCriteria.JOIN_INNER));	
			scs4.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("ftr.federalNeedsForPrivateFlag", SearchCondition.OPERATOR_EQ, 
							new Character('Y')));
			ft = (Collection)searchDAO.getSearchList(FacilityType.class, columns, scs4, new ArrayList(), aliasArray);	
			
			if(ft==null || ft.size() == 0)
				return "N";
		}		
		return "Y";	
	}
	
	public String getMonthlyCostIndexDate(long documentId)
	{
		String costIndexDateStr = "";
		
		SearchConditions scs = null;
		Collection results = null;

		scs = new SearchConditions(new SearchCondition("baseMonthFlag", 
										SearchCondition.OPERATOR_EQ, new Character('Y')));

		// since baseMonthFlag is not unique in DB, assume a collection and use the first result 
		results = searchDAO.getSearchList(CostIndexRef.class, scs);
		
		if(results.iterator().hasNext())
		{
			int month = ((CostIndexRef)(results.iterator().next())).getIndexMonth();
			int year = ((CostIndexRef)(results.iterator().next())).getIndexYear();

			SimpleDateFormat dfMMYYYY = new SimpleDateFormat(gov.epa.owm.mtb.cwns.service.impl.NeedsServiceImpl.MONTHYEARFORMAT);		

			SimpleDateFormat dfMonthYYYY = new SimpleDateFormat("MMMMM yyyy");
			
			Date costIndexDate = null;
			
			try
			{
				costIndexDate = dfMMYYYY.parse(month + "/" + year);
			}
			catch (ParseException e) {
	            // TODO: need to handle this exception			
				return "";
	        }    
			
			costIndexDateStr =  dfMonthYYYY.format(costIndexDate);
		}
		else
		{
			// data setup error - throw error
			costIndexDateStr = "";
		}
				
		return costIndexDateStr;
	}
	
	public void updateDocumentedCosts(long facilityId, long documentId, String userId){		
		SearchConditions scsCost = null;
		Collection lstCost = null;
		
		scsCost = new SearchConditions(new SearchCondition("facilityDocument.id", SearchCondition.OPERATOR_EQ, 
														new FacilityDocumentId(documentId, facilityId)));
		scsCost.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("costMethodCode", SearchCondition.OPERATOR_EQ, 
														new Character('D')));
		lstCost = searchDAO.getSearchList(Cost.class, scsCost);

		if (lstCost!=null && lstCost.size()>0){
			Iterator costIter = lstCost.iterator();
			while (costIter.hasNext()){
				Cost cost = (Cost)costIter.next();
				long baseAmount = cost.getBaseAmount();
				double monthlyCostIndex = getMonthlyCostIndexAmt(0, 'Y');
				double monthlyBaseCostIndex = getMonthlyCostIndexAmt(documentId, 'N');
				if (monthlyCostIndex!=0){
					double result = baseAmount *  monthlyCostIndex/monthlyBaseCostIndex;
					cost.setAdjustedAmount(Math.round(result));
					cost.setLastUpdateTs(new Date());
					cost.setLastUpdateUserid(userId);
					searchDAO.saveObject(cost);
				}
			}
		}
	}
	
	public double getMonthlyCostIndexAmt(long documentId, char baseMonthFlag)
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

		SimpleDateFormat dfMMYYYY = new SimpleDateFormat(gov.epa.owm.mtb.cwns.service.impl.NeedsServiceImpl.MONTHYEARFORMAT);		
		
		Document document = (Document)searchDAO.getObject(Document.class, new Long(documentId));
		
		if(document == null)
		{
			//TODO: throw exception: serious error	
			return 1.0;
		}
		
		// not checking null because baseMonthYear is not null in DB		
		
		String baseMonthYearStr = dfMMYYYY.format(document.getBaseDesignDate());
		
		byte baseMonth = Byte.parseByte(baseMonthYearStr.substring(0, 2));
		short baseYear = Short.parseShort(baseMonthYearStr.substring(3, 7));

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
	
	public Cost prepareFacilityDocument(long costId)
	{
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("categoryRef", "cr", AliasCriteria.JOIN_LEFT));			
		
		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("costId", SearchCondition.OPERATOR_EQ, new Long(costId)));
				
		return (Cost)searchDAO.getSearchObject(Cost.class, scs4, aliasArray);
	}
	
	public void deleteCost(long facilityId, long costId, CurrentUser user)
	{
		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("costId", SearchCondition.OPERATOR_EQ, new Long(costId)));		
	    Cost cost = (Cost)searchDAO.getSearchObject(Cost.class, scs4);	    
   	    if(cost!=null)
   	    {
   	    	if (user.isNonLocalUser()){
   	    		searchDAO.removeObject(cost);
   	    	}else{
   	    		//if local user mark as deleted
   	    		if (cost.getFeedbackDeleteFlag()=='Y') {
   	    			cost.setFeedbackDeleteFlag('N');
   	    		}else{
   	    			cost.setFeedbackDeleteFlag('Y');
   	    		}   	    		
   	    		searchDAO.saveObject(cost);
   	    	}
    		faclityService.performPostSaveUpdates(new Long(facilityId), faclityService.DATA_AREA_NEEDS, user);
   	    }

		return;
	}
	public long saveOrUpdateCost(CapitalCostForm form, CurrentUser cUser)
	{
		String user = cUser.getUserId();
		
		long costId = form.getCapitalCostId();
		
		Cost cost = null;

        if(costId <= 0)
        {
        	// new cost
        	cost = new Cost();
        }
        else
        {
        	SearchConditions scs = new SearchConditions(new SearchCondition("costId", SearchCondition.OPERATOR_EQ, new Long(costId)));

        	cost = (Cost)searchDAO.getSearchObject(Cost.class, scs);
        
	        if (cost==null){
	
	        	// TODO: something seriously wrong - throw an exception
	        	// new document
	            cost = new Cost();	        	
	        }
        }
		
        cost.setAdjustedAmount(form.getAdjustedAmount());
        cost.setBaseAmount(form.getBaseAmount());
        cost.setCostMethodCode((form.getCostMethodCode()==null||form.getCostMethodCode().length()<1)?'D':form.getCostMethodCode().charAt(0));
        cost.setSrfEligiblePercentage(form.getSrfEligible().trim().length()>0?new Short(form.getSrfEligible()):null);
        
        // the front-end must make sure it is not null
        cost.setSsoFlag((form.getSso()==null || form.getSso().length() < 1)?null:new Character(form.getSso().charAt(0)));

    	SearchConditions scs0 = new SearchConditions(new SearchCondition("categoryId", SearchCondition.OPERATOR_EQ, form.getCostCategory()));
    	CategoryRef cr = (CategoryRef) searchDAO.getSearchObject(CategoryRef.class, scs0);	
        cost.setCategoryRef(cr);

        if(form.getClassification()==null || form.getClassification().length() < 1)
        {
        	cost.setClassificationRef(null);
        }
        else
        {
	    	SearchConditions scs1 = new SearchConditions(new SearchCondition("classificationId", SearchCondition.OPERATOR_EQ, form.getClassification()));
	    	ClassificationRef clr = (ClassificationRef) searchDAO.getSearchObject(ClassificationRef.class, scs1);	
	        cost.setClassificationRef(clr);
        }
        
        //  the front-end must make sure it is not null      
        if(form.getCostNeedType()==null || form.getCostNeedType().length() < 1)
        {
        	cost.setNeedTypeRef(null);
        }
        else
        {
	    	SearchConditions scs2 = new SearchConditions(new SearchCondition("needTypeId", SearchCondition.OPERATOR_EQ, form.getCostNeedType()));
	    	NeedTypeRef nr = (NeedTypeRef) searchDAO.getSearchObject(NeedTypeRef.class, scs2);	
	        cost.setNeedTypeRef(nr);
        }

        SearchConditions scs3 = new SearchConditions(
			    new SearchCondition("id", 
			    					SearchCondition.OPERATOR_EQ, 
			    					new FacilityDocumentId(form.getDocumentId(), form.getCapitalCostFacilityId())));
        
        log.debug("facilityId/documentId: " + form.getCapitalCostFacilityId() + "/" + form.getDocumentId());
        
        FacilityDocument fd = (FacilityDocument) searchDAO.getSearchObject(FacilityDocument.class, scs3);

        if (fd==null){
        	// something seriously wrong - throw exception
        }        
        
        cost.setFacilityDocument(fd);                
        
        cost.setFeedbackDeleteFlag('N');
        cost.setLastUpdateTs(new Date());
        cost.setLastUpdateUserid(user);
		
        searchDAO.saveObject(cost);
        
        faclityService.performPostSaveUpdates(new Long(form.getCapitalCostFacilityId()), faclityService.DATA_AREA_NEEDS, cUser);
        
		return cost.getCostId();
	}
	
	private Collection getCostCurveCategories(long facilityId)
	{
		Collection catList = null;		
		//get cost curve ids list where assign_or_run_code = 'R'
		ArrayList columnsFCC = new ArrayList();		
		columnsFCC.add("costCurveRef.costCurveId");
		SearchConditions scsFCC = new SearchConditions(new SearchCondition("facilityDocument.id.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scsFCC.setCondition(new SearchConditions(new SearchCondition("assignedOrRunCode", SearchCondition.OPERATOR_EQ, 
				new Character('R'))));		
		Collection fccList =  searchDAO.getSearchList(FacilityCostCurve.class, columnsFCC, scsFCC, new ArrayList(), new ArrayList());

		//get category list for all the cost curves where assign_or_run_code = 'R'		
		if (fccList!=null && fccList.size()>0){
			ArrayList columnsCat = new ArrayList();
			columnsCat.add("categoryRef.categoryId");
			SearchConditions scsCCR = new SearchConditions(new SearchCondition("costCurveRef.costCurveId", SearchCondition.OPERATOR_IN, 
					fccList));
			catList =  searchDAO.getSearchList(CostCurveCategoryRef.class, columnsCat, scsCCR, new ArrayList(), new ArrayList());
		}		
		return catList;
	}
	
	private boolean excludeCategoryV(long facilityId)
	{
		ArrayList columns = new ArrayList();
		columns.add("f.facilityId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("combinedSewerStatusRef", "cssr", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));			

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("cssr.combinedSewerStatusId", SearchCondition.OPERATOR_EQ, "N"));
				
		Collection csList = searchDAO.getSearchList(CombinedSewer.class, columns, scs, new ArrayList(), aliasArray);
		
		if(csList == null || csList.size() < 1)
		{
			return false;
		}

		aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityTypeRef", "ftr", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));			

		scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("ftr.validForCsoFlag", SearchCondition.OPERATOR_EQ, "Y"));
						
		Collection facilityTypeIdList = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);
		
		if(facilityTypeIdList !=null && facilityTypeIdList.size() > 0)
			return true;
		
		return false;
	}
	
	public Collection  getCategoryClassificationList(long facilityId, long docId, boolean isStateUser)
	{
		ArrayList costCategoryList = new ArrayList();
		
		ArrayList columns = new ArrayList();
		columns.add("ftr.facilityTypeId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityTypeRef", "ftr", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));			

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		
		Collection facilityTypeIdList = searchDAO.getSearchList(FacilityType.class, columns, scs, new ArrayList(), aliasArray);

		ArrayList columnsCat = new ArrayList();
		columnsCat.add("cr.categoryId");
		columnsCat.add("cr.name");
		columnsCat.add("cr.validForSsoFlag");
		
		ArrayList aliasArrayCat = new ArrayList();
		aliasArrayCat.add(new AliasCriteria("categoryRef", "cr", AliasCriteria.JOIN_INNER));
		aliasArrayCat.add(new AliasCriteria("facilityTypeRef", "ftr", AliasCriteria.JOIN_INNER));			

	    SortCriteria sortCriteriaCat = new SortCriteria("cr.categoryId", SortCriteria.ORDER_ASCENDING);	
	    ArrayList sortArrayCat = new ArrayList();
	    sortArrayCat.add(sortCriteriaCat);	  		
		
	    //State users only:
	    //On Add: If facility has a Facility Type that is valid for CSO, and a combined sewer with a 
	    //status of No Needs, Problem Solved, ignore Cat V for this facility
		SearchConditions scsCat = new SearchConditions(new SearchCondition("ftr.facilityTypeId", SearchCondition.OPERATOR_IN, 
				facilityTypeIdList));		
		if(isStateUser && excludeCategoryV(facilityId))
		{
			scsCat.setCondition(SearchCondition.OPERATOR_AND, 
					new SearchCondition("cr.categoryId", SearchCondition.OPERATOR_NOT_EQ, "V"));		
		}		
		
		//State users only:
		//On Add: Ignore Categories where a Cost Curve that produces the same Category of Cost has already been Run,
		//This Cost Curve may be assigned to ANY document assigned to the facility (i.e. check all Cost for facility, 
		//not just Cost assigned to the selected document)
		Collection costCurveCatList = getCostCurveCategories(facilityId);
		if((costCurveCatList!=null && costCurveCatList.size() > 0) && isStateUser){
			scsCat.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("cr.categoryId", SearchCondition.OPERATOR_NOT_IN, costCurveCatList));			
		}
		
		
		List CategoryList = searchDAO.getSearchList(FacilityTypeCategoryRef.class, columnsCat, scsCat, sortArrayCat, aliasArrayCat);
    	Iterator iter = CategoryList.iterator();
		
    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    		
    		String catId = (String)obj[0];
    		String catName = (String)obj[1];
    		String catValidForSSO = obj[2]==null?"":((Character)obj[2]).toString();    		    		
   		
    		//classfication
 		
    		
    		Collection ClassficationList = getClassificationList(catId);
    		
    		if(ClassficationList.size() < 1)
    		{
	    		CategoryClassificationHelper cch = new CategoryClassificationHelper();
	    	    
	    	    cch.setCostCategoryId(catId.trim());
	    	    cch.setCostCategoryName(catName.trim());
	    	    cch.setCategoryValidForSSO(catValidForSSO);
	    	    cch.setCategoryNoFacilityTypeChange(categoryNoFacilityTypeChange(facilityId, catId, isStateUser)?"Y":"N");
	    	    cch.setCategoryNoClassification("Y");
	    	    cch.setClassificationId((""));
	    	    cch.setClassificationName((""));	
	    	    
	    	    costCategoryList.add(cch);
    		}
    		else
    		{
	        	Iterator iterClassf = ClassficationList.iterator();
	    		
	        	while ( iterClassf.hasNext() ) {
	        		Object[] objClassf = (Object[])iterClassf.next();
	
		    		CategoryClassificationHelper cch = new CategoryClassificationHelper();
		    	    
		    	    cch.setCostCategoryId(catId.trim());
		    	    cch.setCostCategoryName(catName.trim());
		    	    cch.setCategoryValidForSSO(catValidForSSO);
		    	    cch.setCategoryNoFacilityTypeChange(categoryNoFacilityTypeChange(facilityId, catId, isStateUser)?"Y":"N");
		    	    cch.setClassificationId(((String)objClassf[0]).trim());
		    	    cch.setClassificationName(((String)objClassf[1]).trim());
		    	    cch.setCategoryNoClassification("N");
		    	    
		    	    costCategoryList.add(cch);
	        	}
    		}
    	}		
		
		return costCategoryList;
	}
	
	public Collection getClassificationList(String catId)
	{
		//classfication
		
		ArrayList columnsClassf = new ArrayList();
		columnsClassf.add("clr.classificationId");
		columnsClassf.add("clr.name");

		ArrayList aliasArrayClassf = new ArrayList();
		aliasArrayClassf.add(new AliasCriteria("categoryRef", "cr", AliasCriteria.JOIN_INNER));
		aliasArrayClassf.add(new AliasCriteria("classificationRef", "clr", AliasCriteria.JOIN_INNER));			

		SearchConditions scsClassf = new SearchConditions(new SearchCondition("cr.categoryId", SearchCondition.OPERATOR_EQ, 
				catId));

	    SortCriteria sortCriteriaClassf = new SortCriteria("clr.name", SortCriteria.ORDER_ASCENDING);	
	    ArrayList sortArrayClassf = new ArrayList();
	    sortArrayClassf.add(sortCriteriaClassf);	  		
		
		return searchDAO.getSearchList(CategoryClassificationRef.class, columnsClassf, 
									scsClassf, sortArrayClassf, aliasArrayClassf);		
	}
	
	public boolean categoryNoFacilityTypeChange(long facilityId, String categoryId, boolean isStateUser)
	{
		if (isStateUser && (categoryId.equalsIgnoreCase("viii") || categoryId.equalsIgnoreCase("ix") || categoryId.equalsIgnoreCase("xiii"))){
			return true;
		}
		
		ArrayList columns = new ArrayList();
		columns.add("ftr.facilityTypeId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("changeTypeRef", "ctr", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("facilityType", "ft", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ft.facilityTypeRef", "ftr", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ft.facility", "f", AliasCriteria.JOIN_INNER));			

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("ctr.changeTypeId", SearchCondition.OPERATOR_NOT_EQ, new Long(1)));	
		
		Collection facilityTypeIdList = searchDAO.getSearchList(FacilityTypeChange.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);
		
		ArrayList columnsCat = new ArrayList();
		columnsCat.add("cr.categoryId");
		
		ArrayList aliasArrayCat = new ArrayList();
		aliasArrayCat.add(new AliasCriteria("categoryRef", "cr", AliasCriteria.JOIN_INNER));
		aliasArrayCat.add(new AliasCriteria("facilityTypeRef", "ftr", AliasCriteria.JOIN_INNER));			

		SearchConditions scsCat = new SearchConditions(new SearchCondition("cr.categoryId", SearchCondition.OPERATOR_EQ, categoryId));
		if(facilityTypeIdList!=null && facilityTypeIdList.size() > 0)
		{
			scsCat.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("ftr.facilityTypeId", SearchCondition.OPERATOR_IN, 
						facilityTypeIdList));
			Collection CategoryList = searchDAO.getSearchList(FacilityTypeCategoryRef.class, columnsCat, scsCat, new ArrayList(), aliasArrayCat);
			if(CategoryList!=null && CategoryList.size() > 0)
				return false;
		}

		
		return true;
	}
	
	// return a CapitalCostListHelper list
	public Collection getCostList(long facilityId, long documentId) {
		
		ArrayList capitalCostList = new ArrayList();

		/*
			private String classification = "";
			private String sso = "N";
			private String canDelete  = "N";
		 */

		ArrayList columns = new ArrayList();
		columns.add("costId");		
		columns.add("baseAmount");		
		columns.add("adjustedAmount");		
		columns.add("srfEligiblePercentage");		
		columns.add("costMethodCode");
		columns.add("ntr.name");		
		columns.add("cr.categoryId");		
		columns.add("cr.name");		
		columns.add("clr.name");
		columns.add("ssoFlag");
		columns.add("cr.validForSsoFlag");
		columns.add("feedbackDeleteFlag");
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("needTypeRef", "ntr", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("categoryRef", "cr", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("classificationRef", "clr", AliasCriteria.JOIN_LEFT));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facilityDocument.id", SearchCondition.OPERATOR_EQ, 
			    					new FacilityDocumentId(documentId, facilityId)));
		
	    ArrayList sortArrayCat = new ArrayList();
	    sortArrayCat.add(new SortCriteria("cr.categoryId", SortCriteria.ORDER_ASCENDING));	  
	    sortArrayCat.add(new SortCriteria("ntr.name", SortCriteria.ORDER_ASCENDING));	  
	    sortArrayCat.add(new SortCriteria("clr.name", SortCriteria.ORDER_ASCENDING));	  
	    
	    List costList = searchDAO.getSearchList(Cost.class, columns, scs, sortArrayCat, aliasArray);
    	Iterator iter = costList.iterator();
  	
    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    	    
    	    CapitalCostListHelper ch = new CapitalCostListHelper();
    	    
    	    ch.setCapitalCostId(((Long)obj[0]).longValue());
    	    ch.setBaseAmount(((Long)obj[1]).longValue());
    	    ch.setAdjustedAmount(((Long)obj[2]).longValue());
    	    ch.setSrfEligible(obj[3]==null?"":((Short)obj[3]).toString());
    	    ch.setCostMethodCode(((Character)obj[4]).charValue()); 
  	        ch.setCostNeedType(obj[5]==null?"":(String)obj[5]);
  	        ch.setCategory((String)obj[6]);
  	        ch.setCategoryName((String)obj[7]);
  	        ch.setClassification((String)obj[8]);
  	        ch.setSso(obj[9]==null?"":((Character)obj[9]).toString());
  	        ch.setCategoryValidForSSO(obj[10]==null?"":((Character)obj[10]).toString());
  	        ch.setFeedbackDeleteFlag(obj[11]==null?"":((Character)obj[11]).toString());    	    
  	      capitalCostList.add(ch);
        	
    	} // while cost query
    	
		return capitalCostList;
	}
	
	public Collection getCategoryRefByFacilityType(Long facilityTypeId){
		ArrayList cats = new ArrayList();
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityTypeId", SearchCondition.OPERATOR_EQ, facilityTypeId));
		Collection c= searchDAO.getSearchList(FacilityTypeCategoryRef.class,scs);
		for (Iterator iter = c.iterator(); iter.hasNext();) {
			FacilityTypeCategoryRef ftc = (FacilityTypeCategoryRef) iter.next();
			cats.add(ftc.getCategoryRef());			
		}
		return cats;
	}

	public Collection getCostsByCategory(Long facilityId, String categoryId) {
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("categoryRef", "cr", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("cr.categoryId", SearchCondition.OPERATOR_EQ, categoryId));
		scs.setCondition(new SearchCondition("fd.id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));	
		
		List costList = searchDAO.getSearchList(Cost.class, scs, new ArrayList(), aliasArray,0,0);
		return costList;
	}

	public Collection getFacilityCosts(Long facilityId) {
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("fd.id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));	
		List costList = searchDAO.getSearchList(Cost.class, scs, new ArrayList(), aliasArray,0,0);
		return costList;
	}
	
	
	public CategoryRef getCategoryRef(String categoryId){
		return (CategoryRef)searchDAO.getSearchObject(CategoryRef.class, new SearchConditions(new SearchCondition("categoryId", SearchCondition.OPERATOR_EQ, categoryId)));		
	}
	
	public boolean canAddNewCostToDocument(Long documentId){
		boolean canAddCost = true;
		SearchConditions scs = new SearchConditions(new SearchCondition("documentId", SearchCondition.OPERATOR_EQ, documentId));
		Object objDoc = searchDAO.getSearchObject(Document.class, scs);
		
		if (objDoc!=null){
			Document doc = (Document)objDoc;
			canAddCost = doc.getDocumentTypeRef().getAprovedForCostsFlag()=='N'?false:true;
			
			if (doc.getDocumentTypeRef().getDocumentTypeId().equals("98")){
				canAddCost = false;
			}
		}		
		return canAddCost;
	}
	
	public boolean facilityHasFacilityTypesAssigned(Long facilityId){
		boolean hasFacilityTypes = false;
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection facilityTypes = searchDAO.getSearchList(FacilityType.class, scs);
		if (facilityTypes!=null && facilityTypes.size()>0)
			hasFacilityTypes = true;
		
		return hasFacilityTypes;
	}
	
	public boolean assignedTypesAreAllNoChange(Long facilityId){
		boolean noChange=false;
		ArrayList columns = new ArrayList();
		columns.add("ftr.facilityTypeId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("changeTypeRef", "ctr", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("facilityType", "ft", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("ft.facilityTypeRef", "ftr", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ft.facility", "f", AliasCriteria.JOIN_INNER));			

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ctr.changeTypeId", SearchCondition.OPERATOR_NOT_EQ, new Long(1)));	
		
		Collection facilityTypeIdList = searchDAO.getSearchList(FacilityTypeChange.class, columns, scs, new ArrayList(), aliasArray, 0, 0, true);
		if(facilityTypeIdList==null || facilityTypeIdList.size()<=0 ){
			noChange=true;
		}		
		return noChange;
	}
	
}