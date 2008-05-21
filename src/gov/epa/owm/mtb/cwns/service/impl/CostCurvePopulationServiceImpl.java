package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.costCurvePopulation.CostCurvePopulationForm;
import gov.epa.owm.mtb.cwns.costCurvePopulation.CostCurvePopulationLineItemHelper;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityPopulation;
import gov.epa.owm.mtb.cwns.model.FacilityPopulationId;
import gov.epa.owm.mtb.cwns.model.FacilityPopulationUnit;
import gov.epa.owm.mtb.cwns.model.FacilityPopulationUnitId;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.model.PopulationRef;
import gov.epa.owm.mtb.cwns.service.CostCurvePopulationService;
import gov.epa.owm.mtb.cwns.service.FacilityService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class CostCurvePopulationServiceImpl extends CWNSService implements CostCurvePopulationService {
	
	public static final int NEW = 0;
	public static final int REHAB = 1;
	
	public static final int RESIDENTIAL=0;
	public static final int NONRESIDENTIAL=1;
	
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	private FacilityService faclityService;

	public void setFacilityService(FacilityService fs) {
		faclityService = fs;
	}

	public void saveOrUpdateCostCurvePopulation(CostCurvePopulationForm form, CurrentUser cUser)
	{
		boolean isDataDirty = false;
		
		isDataDirty = saveUpdateResNonResSSS(form.getCostCurvePopulationFacilityId(), 
				form.getNewSeparateSewerSystemPopulation(), 
				27, 3, cUser) || isDataDirty;
		
		isDataDirty = saveUpdateResNonResSSS(form.getCostCurvePopulationFacilityId(), 
												form.getNewSeparateSewerSystemPopulation(), 
				27, 8, cUser) || isDataDirty;

		isDataDirty = saveUpdateResNonResSSS(form.getCostCurvePopulationFacilityId(), 
				form.getRehabReplaceSeparateSewerSystemPopulation(), 
				28, 9, cUser) || isDataDirty;
		
		isDataDirty = saveUpdateResNonRes(form.getCostCurvePopulationFacilityId(), form.getNewOWTSAllResidentHouses(), form.getNewOWTSAllNonResidentHouses(), 
				11, 12, 11, cUser) || isDataDirty;

		isDataDirty = saveUpdateResNonRes(form.getCostCurvePopulationFacilityId(), form.getNewOWTSInnovResidentHouses(), form.getNewOWTSInnovNonResidentHouses(), 
				13, 14, 12, cUser) || isDataDirty;

		isDataDirty = saveUpdateResNonRes(form.getCostCurvePopulationFacilityId(), form.getNewOWTSConvenResidentHouses(), form.getNewOWTSConvenNonResidentHouses(), 
				15, 16, 13, cUser) || isDataDirty;

		isDataDirty = saveUpdateResNonRes(form.getCostCurvePopulationFacilityId(), form.getNewClusteredSystemResidentHouses(), form.getNewClusteredSystemNonResidentHouses(), 
				17, 18, 14, cUser) || isDataDirty;

		isDataDirty = saveUpdateResNonRes(form.getCostCurvePopulationFacilityId(), form.getRehabOWTSAllResidentHouses(), form.getRehabOWTSAllNonResidentHouses(), 
				19, 20, 15, cUser) || isDataDirty;

		isDataDirty = saveUpdateResNonRes(form.getCostCurvePopulationFacilityId(), form.getRehabOWTSInnovResidentHouses(), form.getRehabOWTSInnovNonResidentHouses(), 
				21, 22, 16, cUser) || isDataDirty;

		isDataDirty = saveUpdateResNonRes(form.getCostCurvePopulationFacilityId(), form.getRehabOWTSConvenResidentHouses(), form.getRehabOWTSConvenNonResidentHouses(), 
				23, 24, 17, cUser) || isDataDirty;

		isDataDirty = saveUpdateResNonRes(form.getCostCurvePopulationFacilityId(), form.getRehabClusteredSystemResidentHouses(), form.getRehabClusteredSystemNonResidentHouses(), 
				25, 26, 18, cUser) || isDataDirty;		
		
		if(isDataDirty)
		{
			//postSave
			faclityService.performPostSaveUpdates(new Long(form.getCostCurvePopulationFacilityId()), faclityService.DATA_AREA_POPULATION, cUser);			
		}
		
		return;
	}
	
	private boolean saveUpdateResNonResSSS(long facilityId, int resCount, 
			long resPopId, int costCurveId, CurrentUser cUser)
	{
		if(!existFacilityCostCurve(facilityId, costCurveId))
			return false;
		
		boolean changedForCostCurve = false;
		
		//first get fph object, and check to see if it has changed
		FacilityPopulationId fphIdRes = new FacilityPopulationId(facilityId, resPopId);

		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
				fphIdRes));
		FacilityPopulation fphObjRes = (FacilityPopulation)searchDAO.getSearchObject(FacilityPopulation.class, scs);

		if((fphObjRes==null && resCount!=CostCurvePopulationForm.INITIAL_INT_VALUE)) //changed
		{
			changedForCostCurve = true;
			
			fphObjRes = new FacilityPopulation();
			fphObjRes.setFacility(faclityService.findByFacilityId(new Long(facilityId).toString()));
			fphObjRes.setId(fphIdRes);
			fphObjRes.setPopulationRef((PopulationRef)searchDAO.getObject(PopulationRef.class, new Long(resPopId)));
			fphObjRes.setPresentPopulationCount(new Integer(resCount));
			fphObjRes.setLastUpdateTs(new Date());
			fphObjRes.setLastUpdateUserid(cUser.getUserId());
			searchDAO.saveObject(fphObjRes);
		}
		else if(fphObjRes!=null)
		{
			Integer fphResInt = fphObjRes.getPresentPopulationCount();
			
			if((fphResInt==null && resCount!=CostCurvePopulationForm.INITIAL_INT_VALUE) ||
					(fphResInt!=null && resCount!=fphResInt.intValue()))
			{
				//changed 
				changedForCostCurve = true;
				fphObjRes.setPresentPopulationCount(new Integer(resCount));
				fphObjRes.setLastUpdateTs(new Date());
				fphObjRes.setLastUpdateUserid(cUser.getUserId());
				searchDAO.saveObject(fphObjRes);
				
			}
			else if(fphResInt!=null && resCount==CostCurvePopulationForm.INITIAL_INT_VALUE)
			{
				changedForCostCurve = true;
				fphObjRes.setPresentPopulationCount(null);
				fphObjRes.setLastUpdateTs(new Date());
				fphObjRes.setLastUpdateUserid(cUser.getUserId());	
				searchDAO.saveObject(fphObjRes);
				
			}		
		}
				
		if( changedForCostCurve)
		{
			updateFacilityCostCurveRerunFlag(facilityId, costCurveId, 'Y', cUser);	
			return true;
		}
		
		return false;
	}
	
	private boolean saveUpdateResNonRes(long facilityId, int resCount, int nonResCount, 
					long resPopId, long nonResPopId,
					int costCurveId, CurrentUser cUser)
	{
		double resPopPerUnit = getPopulationPerHouse(facilityId, CostCurvePopulationServiceImpl.RESIDENTIAL);
		boolean resUpdated = updateFacilityPopulationHouses(facilityId, resCount,
				resPopId, resPopPerUnit, cUser);
		
		double nonResPopPerUnit = getPopulationPerHouse(facilityId, CostCurvePopulationServiceImpl.NONRESIDENTIAL);;
		boolean nonResUpdated = updateFacilityPopulationHouses(facilityId, nonResCount,
				nonResPopId, nonResPopPerUnit, cUser);	
		
		if(resUpdated || nonResUpdated)
		{
			// if so, update costCurve
			updateFacilityCostCurveRerunFlag(facilityId, costCurveId, 'Y', cUser);
			
			return true;
			
		}//if

		return false;
		
	}

	private void updateFacilityCostCurveRerunFlag(long facilityId, int costCurveId, char yesOrNo, CurrentUser cUser)
	{
		Collection upList = getFacilityCostCurve(facilityId, costCurveId);
		
		Iterator iter = upList.iterator();
		
		while ( iter.hasNext() ) 
		{
			FacilityCostCurve fcc = (FacilityCostCurve)iter.next();
			
			if(fcc!=null)
			{
				fcc.setCurveRerunFlag(yesOrNo);
				fcc.setLastUpdateTs(new Date());
				fcc.setLastUpdateUserid(cUser.getUserId());
				
				searchDAO.saveObject(fcc);
			}
		}		
		
	}
	
	private boolean updateFacilityPopulationHouses(long facilityId, int resCount,
			long resPopId, double popPerUnit, CurrentUser cUser)
	{
		boolean changedForCostCurve = false;
		
		//first get fph object, and check to see if it has changed
		FacilityPopulationUnitId fphIdRes = new FacilityPopulationUnitId(facilityId, resPopId);

		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
				fphIdRes));
		FacilityPopulationUnit fphObjRes = (FacilityPopulationUnit)searchDAO.getSearchObject(FacilityPopulationUnit.class, scs);

		if((fphObjRes==null && resCount!=CostCurvePopulationForm.INITIAL_INT_VALUE)) //changed
		{
			changedForCostCurve = true;
			
			fphObjRes = new FacilityPopulationUnit();
			fphObjRes.setFacility(faclityService.findByFacilityId(new Long(facilityId).toString()));
			fphObjRes.setId(fphIdRes);
			fphObjRes.setPopulationRef((PopulationRef)searchDAO.getObject(PopulationRef.class, new Long(resPopId)));
			fphObjRes.setPresentUnitsCount(new Integer(resCount));
			fphObjRes.setPopulationPerUnit(new BigDecimal(popPerUnit));
			fphObjRes.setLastUpdateTs(new Date());
			fphObjRes.setLastUpdateUserid(cUser.getUserId());
			searchDAO.saveObject(fphObjRes);

		}
		else if(fphObjRes!=null)
		{
			Integer fphResInt = fphObjRes.getPresentUnitsCount();
			
			if((fphResInt==null && resCount!=CostCurvePopulationForm.INITIAL_INT_VALUE) ||
					(fphResInt!=null && resCount!=fphResInt.intValue()))
			{
				//changed 
				changedForCostCurve = true;
				fphObjRes.setPresentUnitsCount(new Integer(resCount));
				fphObjRes.setLastUpdateTs(new Date());
				fphObjRes.setLastUpdateUserid(cUser.getUserId());
				searchDAO.saveObject(fphObjRes);
				
			}
			else if(fphResInt!=null && resCount==CostCurvePopulationForm.INITIAL_INT_VALUE)
			{
				changedForCostCurve = true;
				fphObjRes.setPresentUnitsCount(null);
				fphObjRes.setLastUpdateTs(new Date());
				fphObjRes.setLastUpdateUserid(cUser.getUserId());				
				searchDAO.saveObject(fphObjRes);
			}		
		}
				
		return changedForCostCurve;
	}
	public double getPopulationPerHouse(long facilityId, int resOrNonRes)
	{
		ArrayList columns = new ArrayList();
		
		double result = 0;
		
		columns.add("c.resPopulationPerUnit");		
		columns.add("c.nonresPopulationPerUnit");		
		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("countyRef", "c", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("ga.geographicAreaTypeRef", "gatr", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("ga.facility", "f", AliasCriteria.JOIN_INNER));

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("gatr.geographicAreaTypeId", SearchCondition.OPERATOR_EQ, new Long(7)));    			
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("primaryFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));    			

		Collection upList = searchDAO.getSearchList(GeographicAreaCounty.class, columns, scs, 
											new ArrayList(), aliasArray, 0, 0, true);

		Iterator iter = upList.iterator();
		
		if ( iter.hasNext() ) 
		{
    		Object[] obj = (Object[])iter.next();
    		result = (obj[resOrNonRes]==null?0:((BigDecimal)obj[resOrNonRes]).doubleValue());
		}
		else
		{
			// no primary county, then check state
			result = 2.8;
		}
		
		return result;
	}
	
	public String getPopulationPerHouseDisplayOnly(long facilityId)
	{
		ArrayList columns = new ArrayList();

		columns.add("facilityCostCurveId");		

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("costCurveRef", "c", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		
		ArrayList costCurveIdList = new ArrayList();
		costCurveIdList.add(new Long(11));
		costCurveIdList.add(new Long(12));
		costCurveIdList.add(new Long(13));
		costCurveIdList.add(new Long(14));
		costCurveIdList.add(new Long(15));
		costCurveIdList.add(new Long(16));
		costCurveIdList.add(new Long(17));
		costCurveIdList.add(new Long(18));

		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("c.costCurveId", SearchCondition.OPERATOR_IN, costCurveIdList));    			
		
		Collection upList = searchDAO.getSearchList(FacilityCostCurve.class, columns, scs, 
											new ArrayList(), aliasArray, 0, 0, true);

		Iterator iter = upList.iterator();
		
		if ( iter.hasNext() ) 
		{
			//exist, display
			return "Y";
		}
		
		return "N";
	}
	
	private void setItemHelper(CostCurvePopulationLineItemHelper obj, 
			int housePresentPopId, 
			int costCurveId, 
			int validationPopId, 
			long facilityId,
			int newOrRehab,
			boolean isWarning)
	{	
		obj.setValue(getFacilityPopulationHousesPresentPopulation(facilityId, housePresentPopId));

		if(existFacilityCostCurve(facilityId, costCurveId))
		  obj.setDisplayOnly("Y");
				
		obj.setToValidate("Y");
		
		if(isWarning)
		{
			obj.setIsWarning("Y");
			
			if(getFacilityPopulationHousesProjectedPopulation(facilityId, validationPopId) > 
				getFacilityPopulationHousesPresentPopulation(facilityId, validationPopId))
				obj.setProjectedGreaterThanPresent("Y");
			else
				obj.setProjectedGreaterThanPresent("N");
		}
		else
		{
			obj.setIsWarning("N");
			obj.setProjectedGreaterThanPresent("Y");
		}
		
		if(newOrRehab == NEW)
		{
			obj.setValidationValue(getFacilityPopulationHousesProjectedPopulation(facilityId, validationPopId) - getFacilityPopulationHousesPresentPopulation(facilityId, validationPopId));
			
		}
		else if(newOrRehab == REHAB)
		{
			obj.setValidationValue(getFacilityPopulationHousesPresentPopulation(facilityId, validationPopId));			
		}
	}

	private void setSSSItemHelper(CostCurvePopulationLineItemHelper obj, 
			int presentPopId, 
			int costCurveId, 
			//int validationPopId, // this should be 1 & 2
			long facilityId,
			int newOrRehab)
	{
		obj.setValue(getFacilityPopulationPresentPopulation(facilityId, presentPopId));

		if(existFacilityCostCurve(facilityId, costCurveId))
		   obj.setDisplayOnly("Y");
				
		if(newOrRehab == NEW)
		{
			obj.setValidationValue(getFacilityPopulationProjectedPopulation(facilityId, 1)
								- getFacilityPopulationPresentPopulation(facilityId, 1)
								+ getFacilityPopulationProjectedPopulation(facilityId, 2)
								- getFacilityPopulationPresentPopulation(facilityId, 2));
		}
		else if(newOrRehab == REHAB)
		{
			obj.setValidationValue(getFacilityPopulationPresentPopulation(facilityId, 1)
					+ getFacilityPopulationPresentPopulation(facilityId, 2));
		}		
	}
	
	private void validateSSS(CostCurvePopulationForm form, 
			int presentPopId, 
			int costCurveId, 
			//int validationPopId, // this should be 1 & 2
			long facilityId,
			int newOrRehab,
			List errorsList)
	{
		if(!existFacilityCostCurve(facilityId, costCurveId))
			return;

				
		int validationValue = 0;
		
		if(newOrRehab == NEW)
		{
			validationValue = getFacilityPopulationProjectedPopulation(facilityId, 1)
								- getFacilityPopulationPresentPopulation(facilityId, 1)
								+ getFacilityPopulationProjectedPopulation(facilityId, 2)
								- getFacilityPopulationPresentPopulation(facilityId, 2);
			if(form.getNewSeparateSewerSystemPopulation()>validationValue)
			{
				errorsList.add("New Separate Sewer System Population must be less or equal to the Facility’s Projected – Present Receiving Collection Population");
			}
			
			if(form.getNewSeparateSewerSystemPopulation()>15000)
			{
				errorsList.add("New Separate Sewer System Population must be less or equal to 15,000");
			}
		}
		else if(newOrRehab == REHAB)
		{
			validationValue = getFacilityPopulationPresentPopulation(facilityId, 1)
					+ getFacilityPopulationPresentPopulation(facilityId, 2);
			
			if(form.getRehabReplaceSeparateSewerSystemPopulation()>validationValue)
			{
				errorsList.add("Rehabilitate/Replace Separate Sewer System Population must be less or equal to the Facility’s Present Receiving Collection Population");
			}

			if(form.getRehabReplaceSeparateSewerSystemPopulation()>15000)
			{
				errorsList.add("Rehabilitate/Replace Separate Sewer System Population must be less or equal to 15,000");
			}
			
		}		
	}	
	
	private boolean existFacilityCostCurve(long facilityId, long costCurveId)
	{
		List list = getFacilityCostCurve(facilityId, costCurveId);

		if(list==null || list.size() == 0)
		  return false;	
		
		return true;
	}
		
	public void validateFormValues(CostCurvePopulationForm form, List errorsList, List warningsList)
	{
		validateSSS(form, 27, 3, form.getCostCurvePopulationFacilityId(), NEW, errorsList);
		validateSSS(form, 27, 8, form.getCostCurvePopulationFacilityId(), NEW, errorsList);
		validateSSS(form, 28, 9, form.getCostCurvePopulationFacilityId(), REHAB, errorsList);
		
		//rehab all
		if(existFacilityCostCurve(form.getCostCurvePopulationFacilityId(), 15))
		{
			if(form.getRehabOWTSAllResidentHouses() >
				getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 5))
		      errorsList.add("The residential Number of Units for Rehab Onsite Wastewater Treatment System – all must be less or equal to the Facility’s Residential Present Number of Onsite Wastewater Treatment System Houses");		

			if(form.getRehabOWTSAllNonResidentHouses() >
			getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 6))
	          errorsList.add("The non-residential Number of Units for Rehab Onsite Wastewater Treatment System – all must be less or equal to the Facility’s non-residential Present Number of Onsite Wastewater Treatment System Houses");		
		}
		
		//rehab cluster
		if(existFacilityCostCurve(form.getCostCurvePopulationFacilityId(), 18))
		{
			if(form.getRehabClusteredSystemResidentHouses() >
				getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 7))
		      errorsList.add("The residential Number of Units for Rehab Cluster System must be less or equal to the Facility’s Residential Present Number of Cluster System Houses");		

			if(form.getRehabClusteredSystemNonResidentHouses() >
			getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 8))
	          errorsList.add("The non-residential Number of Units for Rehab Cluster System must be less or equal to the Facility’s non-residential Present Number of Cluster System Houses");		
		}
		
		//rehab inno + conven / res
		boolean fcc16 = existFacilityCostCurve(form.getCostCurvePopulationFacilityId(), 16);
		boolean fcc17 = existFacilityCostCurve(form.getCostCurvePopulationFacilityId(), 17);
		
		if(fcc16 || fcc17)
		{
			int sum_16_17 = 0;
			
			if(fcc16)
				sum_16_17 += form.getRehabOWTSInnovResidentHouses();

			if(fcc17)
				sum_16_17 += form.getRehabOWTSConvenResidentHouses();

			if(sum_16_17 >
				getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 5))
		      errorsList.add("The sum of the residential Number of Units for Rehab Onsite Wastewater Treatment System – Innovative and Conventional must be less or equal to the Facility’s residential  Present Number of Onsite Wastewater Treatment System Units");
		}		

		//rehab inno + conven / nonres

		if(fcc16 || fcc17)
		{
			int sum_16_17 = 0;
			
			if(fcc16)
				sum_16_17 += form.getRehabOWTSInnovNonResidentHouses();

			if(fcc17)
				sum_16_17 += form.getRehabOWTSConvenNonResidentHouses();

			if(sum_16_17 >
				getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 6))
		      errorsList.add("The sum of the non-residential Number of Units for Rehab Onsite Wastewater Treatment System – Innovative and Conventional must be less or equal to the Facility’s non-residential  Present Number of Onsite Wastewater Treatment System Units");
		}		

		if(errorsList.size() > 0)
			return;
		
		// -------------  now handle warning ------------ 
		
		int projectedMinusPresent5 = 
			getFacilityPopulationHousesProjectedPopulation(form.getCostCurvePopulationFacilityId(), 5) - 
				getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 5);

		int projectedMinusPresent6 = 
			getFacilityPopulationHousesProjectedPopulation(form.getCostCurvePopulationFacilityId(), 6) - 
				getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 6);

		int projectedMinusPresent7 = 
			getFacilityPopulationHousesProjectedPopulation(form.getCostCurvePopulationFacilityId(), 7) - 
				getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 7);

		int projectedMinusPresent8 = 
			getFacilityPopulationHousesProjectedPopulation(form.getCostCurvePopulationFacilityId(), 8) - 
				getFacilityPopulationHousesPresentPopulation(form.getCostCurvePopulationFacilityId(), 8);

		// new all
		if(existFacilityCostCurve(form.getCostCurvePopulationFacilityId(), 11))
		{
			if(projectedMinusPresent5 > 0 &&
				form.getNewOWTSAllResidentHouses() > projectedMinusPresent5)
		      warningsList.add("The residential Number of Units for New Onsite Wastewater Treatment System – all is more than the Facility’s Residential Projected - Present Number of Onsite Wastewater Treatment System Houses -  please provide a Population Comment.");		

			if(projectedMinusPresent6 > 0 &&
					form.getNewOWTSAllNonResidentHouses() > projectedMinusPresent6)
			      warningsList.add("The non-residential Number of Units for New Onsite Wastewater Treatment System – all is more than the Facility’s non-residential Projected - Present Number of Onsite Wastewater Treatment System Houses - please provide a Population Comment.");		
		}
		
		// new cluster
		if(existFacilityCostCurve(form.getCostCurvePopulationFacilityId(), 14))
		{
			if(projectedMinusPresent7 > 0 &&
				form.getNewClusteredSystemResidentHouses() > projectedMinusPresent7)
		      warningsList.add("The residential Number of Units for New Cluster System is more than the Facility’s residential Projected - Present Number of Cluster System Houses -  please provide a Population Comment.");		

			if(projectedMinusPresent8 > 0 &&
					form.getNewClusteredSystemNonResidentHouses() > projectedMinusPresent8)
			  warningsList.add("The non-residential Number of Units for New Cluster System is more than the Facility’s non-residential Projected - Present Number of Cluster System Houses -  please provide a Population Comment.");		
		}
		
		//new inno + conven
		boolean fcc12 = existFacilityCostCurve(form.getCostCurvePopulationFacilityId(), 12);
		boolean fcc13 = existFacilityCostCurve(form.getCostCurvePopulationFacilityId(), 13);
		
		if(fcc13 || fcc12)
		{
			int sum_12_13 = 0;
			
			if(fcc12)
				sum_12_13 += form.getNewOWTSInnovResidentHouses();

			if(fcc13)
				sum_12_13 += form.getNewOWTSConvenResidentHouses();

			if(projectedMinusPresent5 > 0 &&
					sum_12_13 > projectedMinusPresent5)
			      warningsList.add("The sum of the residential Number of Units for New Onsite Wastewater Treatment System – Innovative and Conventional is more than the Facility’s residential  Projected - Present Number of Onsite Wastewater Treatment System Units - please provide a Population Comment.");		
		
			sum_12_13 = 0;
			
			if(fcc12)
				sum_12_13 += form.getNewOWTSInnovNonResidentHouses();

			if(fcc13)
				sum_12_13 += form.getNewOWTSConvenNonResidentHouses();

			if(projectedMinusPresent6 > 0 &&
					sum_12_13 > projectedMinusPresent6)
			      warningsList.add("The sum of the non-residential Number of Units for New Onsite Wastewater Treatment System – Innovative and Conventional is more than the Facility’s non-residential  Projected - Present Number of Onsite Wastewater Treatment System Units - please provide a Population Comment.");

		}		

	}

 	  public void setDisplayObjectsInRequest(HttpServletRequest req, long facilityId,
 			 CostCurvePopulationForm costCurvePopulationForm, boolean resetDisplayValues)
	  {
 	 	CostCurvePopulationLineItemHelper NewSeparateSewerSystemInterceptorObj = new CostCurvePopulationLineItemHelper();
 	 	setSSSItemHelper(NewSeparateSewerSystemInterceptorObj, 27, 3, facilityId, NEW);
 	 	if(resetDisplayValues)
 	 		NewSeparateSewerSystemInterceptorObj.setValue(costCurvePopulationForm.getNewSeparateSewerSystemPopulation());
 	 	req.setAttribute("NewSeparateSewerSystemInterceptorObj", NewSeparateSewerSystemInterceptorObj);
 		  
 		CostCurvePopulationLineItemHelper NewSeparateSewerSystemPopulationObj = new CostCurvePopulationLineItemHelper();
 		setSSSItemHelper(NewSeparateSewerSystemPopulationObj, 27, 8, facilityId, NEW);
 		if(resetDisplayValues)
 			NewSeparateSewerSystemPopulationObj.setValue(costCurvePopulationForm.getNewSeparateSewerSystemPopulation());
 		req.setAttribute("NewSeparateSewerSystemPopulationObj", NewSeparateSewerSystemPopulationObj);

 		CostCurvePopulationLineItemHelper RehabReplaceSeparateSewerSystemPopulationObj = new CostCurvePopulationLineItemHelper();
 		setSSSItemHelper(RehabReplaceSeparateSewerSystemPopulationObj, 28, 9, facilityId, REHAB);
 		if(resetDisplayValues)
 			RehabReplaceSeparateSewerSystemPopulationObj.setValue(costCurvePopulationForm.getRehabReplaceSeparateSewerSystemPopulation());
 		req.setAttribute("RehabReplaceSeparateSewerSystemPopulationObj", RehabReplaceSeparateSewerSystemPopulationObj);


 		// mass reproduction of the same logic
 		CostCurvePopulationLineItemHelper NewOWTSAllResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(NewOWTSAllResidentHousesObj, 11, 11, 5, facilityId, NEW, true);
 		if(resetDisplayValues)
 			NewOWTSAllResidentHousesObj.setValue(costCurvePopulationForm.getNewOWTSAllResidentHouses());
 		req.setAttribute("NewOWTSAllResidentHousesObj", NewOWTSAllResidentHousesObj);
 		
 		
 		CostCurvePopulationLineItemHelper NewOWTSAllNonResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(NewOWTSAllNonResidentHousesObj, 12, 11, 6, facilityId, NEW, true);
 		if(resetDisplayValues)
 			NewOWTSAllNonResidentHousesObj.setValue(costCurvePopulationForm.getNewOWTSAllNonResidentHouses());
 		req.setAttribute("NewOWTSAllNonResidentHousesObj", NewOWTSAllNonResidentHousesObj);


 		CostCurvePopulationLineItemHelper NewOWTSInnovResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(NewOWTSInnovResidentHousesObj, 13, 12, 5, facilityId, NEW, false);
 		NewOWTSInnovResidentHousesObj.setToValidate("N");
 		if(resetDisplayValues)
 			NewOWTSInnovResidentHousesObj.setValue(costCurvePopulationForm.getNewOWTSInnovResidentHouses());
 		req.setAttribute("NewOWTSInnovResidentHousesObj", NewOWTSInnovResidentHousesObj);


 		CostCurvePopulationLineItemHelper NewOWTSInnovNonResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(NewOWTSInnovNonResidentHousesObj, 14, 12, 6, facilityId, NEW, true);
 		NewOWTSInnovNonResidentHousesObj.setToValidate("N");
 		if(resetDisplayValues)
 			NewOWTSInnovNonResidentHousesObj.setValue(costCurvePopulationForm.getNewOWTSInnovNonResidentHouses());
 		req.setAttribute("NewOWTSInnovNonResidentHousesObj", NewOWTSInnovNonResidentHousesObj);


 		CostCurvePopulationLineItemHelper NewOWTSConvenResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(NewOWTSConvenResidentHousesObj, 15, 13, 5, facilityId, NEW, true);
 		if(resetDisplayValues)
 			NewOWTSConvenResidentHousesObj.setValue(costCurvePopulationForm.getNewOWTSConvenResidentHouses());
 		req.setAttribute("NewOWTSConvenResidentHousesObj", NewOWTSConvenResidentHousesObj);

 		CostCurvePopulationLineItemHelper NewOWTSConvenNonResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(NewOWTSConvenNonResidentHousesObj, 16, 13, 6, facilityId, NEW, true);
 		if(resetDisplayValues)
 			NewOWTSConvenNonResidentHousesObj.setValue(costCurvePopulationForm.getNewOWTSConvenNonResidentHouses());
 		req.setAttribute("NewOWTSConvenNonResidentHousesObj", NewOWTSConvenNonResidentHousesObj);

 		CostCurvePopulationLineItemHelper NewClusteredSystemResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(NewClusteredSystemResidentHousesObj, 17, 14, 7, facilityId, NEW, true);
 		if(resetDisplayValues)
 			NewClusteredSystemResidentHousesObj.setValue(costCurvePopulationForm.getNewClusteredSystemResidentHouses());
 		req.setAttribute("NewClusteredSystemResidentHousesObj", NewClusteredSystemResidentHousesObj);

 		CostCurvePopulationLineItemHelper NewClusteredSystemNonResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(NewClusteredSystemNonResidentHousesObj, 18, 14, 8, facilityId, NEW, true);
 		if(resetDisplayValues)
 			NewClusteredSystemNonResidentHousesObj.setValue(costCurvePopulationForm.getNewClusteredSystemNonResidentHouses());
 		req.setAttribute("NewClusteredSystemNonResidentHousesObj", NewClusteredSystemNonResidentHousesObj);


 		CostCurvePopulationLineItemHelper RehabOWTSAllResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(RehabOWTSAllResidentHousesObj, 19, 15, 5, facilityId, REHAB, false);
 		if(resetDisplayValues)
 			RehabOWTSAllResidentHousesObj.setValue(costCurvePopulationForm.getRehabOWTSAllResidentHouses());
 		req.setAttribute("RehabOWTSAllResidentHousesObj", RehabOWTSAllResidentHousesObj);


 		CostCurvePopulationLineItemHelper RehabOWTSAllNonResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(RehabOWTSAllNonResidentHousesObj, 20, 15, 6, facilityId, REHAB, false);
 		if(resetDisplayValues)
 			RehabOWTSAllNonResidentHousesObj.setValue(costCurvePopulationForm.getRehabOWTSAllNonResidentHouses());
 		req.setAttribute("RehabOWTSAllNonResidentHousesObj", RehabOWTSAllNonResidentHousesObj);

 		CostCurvePopulationLineItemHelper RehabOWTSInnovResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(RehabOWTSInnovResidentHousesObj, 21, 16, 5, facilityId, REHAB, false);
 		RehabOWTSInnovResidentHousesObj.setToValidate("N");
 		if(resetDisplayValues)
 			RehabOWTSInnovResidentHousesObj.setValue(costCurvePopulationForm.getRehabOWTSInnovResidentHouses());
 		req.setAttribute("RehabOWTSInnovResidentHousesObj", RehabOWTSInnovResidentHousesObj);


 		CostCurvePopulationLineItemHelper RehabOWTSInnovNonResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(RehabOWTSInnovNonResidentHousesObj, 22, 16, 6, facilityId, REHAB, false);
 		RehabOWTSInnovNonResidentHousesObj.setToValidate("N");
 		if(resetDisplayValues)
 			RehabOWTSInnovNonResidentHousesObj.setValue(costCurvePopulationForm.getRehabOWTSInnovNonResidentHouses());
 		req.setAttribute("RehabOWTSInnovNonResidentHousesObj", RehabOWTSInnovNonResidentHousesObj);


 		CostCurvePopulationLineItemHelper RehabOWTSConvenResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(RehabOWTSConvenResidentHousesObj, 23, 17, 5, facilityId, REHAB, false);
 		if(resetDisplayValues)
 			RehabOWTSConvenResidentHousesObj.setValue(costCurvePopulationForm.getRehabOWTSConvenResidentHouses());
 		req.setAttribute("RehabOWTSConvenResidentHousesObj", RehabOWTSConvenResidentHousesObj);


 		CostCurvePopulationLineItemHelper RehabOWTSConvenNonResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(RehabOWTSConvenNonResidentHousesObj, 24, 17, 6, facilityId, REHAB, false);
 		if(resetDisplayValues)
 			RehabOWTSConvenNonResidentHousesObj.setValue(costCurvePopulationForm.getRehabOWTSConvenNonResidentHouses());
 		req.setAttribute("RehabOWTSConvenNonResidentHousesObj", RehabOWTSConvenNonResidentHousesObj);


 		CostCurvePopulationLineItemHelper RehabClusteredSystemResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(RehabClusteredSystemResidentHousesObj, 25, 18, 7, facilityId, REHAB, false);
 		if(resetDisplayValues)
 			RehabClusteredSystemResidentHousesObj.setValue(costCurvePopulationForm.getRehabClusteredSystemResidentHouses());
 		req.setAttribute("RehabClusteredSystemResidentHousesObj", RehabClusteredSystemResidentHousesObj);


 		CostCurvePopulationLineItemHelper RehabClusteredSystemNonResidentHousesObj = new CostCurvePopulationLineItemHelper();
 		setItemHelper(RehabClusteredSystemNonResidentHousesObj, 26, 18, 8, facilityId, REHAB, false);
 		if(resetDisplayValues)
 			RehabClusteredSystemNonResidentHousesObj.setValue(costCurvePopulationForm.getRehabClusteredSystemNonResidentHouses());
 		req.setAttribute("RehabClusteredSystemNonResidentHousesObj", RehabClusteredSystemNonResidentHousesObj);

	  }
	/*
NewSeparateSewerSystemPopulationObj
RehabReplaceSeparateSewerSystemPopulationObj
NewOWTSAllResidentHousesObj
NewOWTSAllNonResidentHousesObj
NewOWTSInnovResidentHousesObj
NewOWTSInnovNonResidentHousesObj
NewOWTSConvenResidentHousesObj
NewOWTSConvenNonResidentHousesObj
NewClusteredSystemResidentHousesObj
NewClusteredSystemNonResidentHousesObj
RehabOWTSAllResidentHousesObj
RehabOWTSAllNonResidentHousesObj
RehabOWTSInnovResidentHousesObj
RehabOWTSInnovNonResidentHousesObj
RehabOWTSConvenResidentHousesObj
RehabOWTSConvenNonResidentHousesObj
RehabClusteredSystemResidentHousesObj
RehabClusteredSystemNonResidentHousesObj
	 */
	public int getFacilityPopulationPresentPopulation(long facilityId, long populationId)
	{
		FacilityPopulationId fpId = new FacilityPopulationId(facilityId, populationId);
		return getTableColumnValue(FacilityPopulation.class, fpId, "presentPopulationCount");		
	}
	
	public int getFacilityPopulationProjectedPopulation(long facilityId, long populationId)
	{
		FacilityPopulationId fpId = new FacilityPopulationId(facilityId, populationId);
		return getTableColumnValue(FacilityPopulation.class, fpId, "projectedPopulationCount");				
	}
	public int getFacilityPopulationHousesPresentPopulation(long facilityId, long populationId)
	{
		FacilityPopulationUnitId fphId = new FacilityPopulationUnitId(facilityId, populationId);
		return getTableColumnValue(FacilityPopulationUnit.class, fphId, "presentUnitsCount");		
	}
	
	public int getFacilityPopulationHousesProjectedPopulation(long facilityId, long populationId)
	{
		FacilityPopulationUnitId fphId = new FacilityPopulationUnitId(facilityId, populationId);
		return getTableColumnValue(FacilityPopulationUnit.class, fphId, "projectedUnitsCount");
	}

	private int getTableColumnValue(Class clz, Object id, String columnName)
	{
		ArrayList columns = new ArrayList();
		columns.add(columnName);

		SearchConditions scs = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, 
				id));

		Collection tcList = searchDAO.getSearchList(clz, columns, scs);

		Iterator iter = tcList.iterator();
		
		if ( iter.hasNext() ) 
		{
    		Object obj = (Object)iter.next();
    		
    		if(obj!=null)
    			return ((Integer)obj).intValue();
		}
		
		return 0;
		
	}
	
	public List getFacilityCostCurve(long facilityId, long costCurveId)
	{
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("costCurveRef", "c", AliasCriteria.JOIN_INNER));			
		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, 
				new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("c.costCurveId", SearchCondition.OPERATOR_EQ, new Long(costCurveId)));    			
		
		return searchDAO.getSearchList(FacilityCostCurve.class, scs, new ArrayList(), aliasArray, 0, 0);
	}
	
	public void setFacilityCostCurveRerun(long facilityId, long costCurveId, char yesOrNo)
	{
		List fccList = getFacilityCostCurve(facilityId,  costCurveId);

		Iterator iter = fccList.iterator();

		while ( iter.hasNext() ) 
		{
			FacilityCostCurve obj = (FacilityCostCurve)iter.next();
			obj.setCurveRerunFlag(yesOrNo);
			searchDAO.saveObject(obj);
		}
	}
	
	
	public void setFacilityPopulationPresentPopulation(long facilityId, long populationId, long number)
	{
		
	}
	
	public void setFacilityPopulationProjectedPopulation(long facilityId, long populationId, long number)
	{
		
	}
	
	public void setFacilityPopulationHousesPresentPopulation(long facilityId, long populationId, long number)
	{
		
	}
	
	public void setFacilityPopulationHousesProjectedPopulation(long facilityId, long populationId, long number)
	{
		
	}
	
}