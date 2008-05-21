package gov.epa.owm.mtb.cwns.fes;

import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.model.CategoryRef;
import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.CombinedSewerStatusRef;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataArea;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.FacilityTypeCategoryRef;
import gov.epa.owm.mtb.cwns.model.FacilityTypeChange;
import gov.epa.owm.mtb.cwns.needs.NeedsHelper;
import gov.epa.owm.mtb.cwns.service.CSOService;
import gov.epa.owm.mtb.cwns.service.CapitalCostService;
import gov.epa.owm.mtb.cwns.service.CostCurveService;
import gov.epa.owm.mtb.cwns.service.EffluentService;
import gov.epa.owm.mtb.cwns.service.FESService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.NeedsService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class NeedsValidator extends FESValidator {

	public NeedsValidator() {
		super(FacilityService.DATA_AREA_NEEDS);
	}
	
	public boolean isRequired(Long facilityId) {
		//if facility type with NO change noChange or abandonment and public or (private but a nps) needs are required
		if(!facilityTypeService.isFacilityNoChangeOrAbandoned(facilityId)){
			//facility is public or NPS
			Facility f = facilityService.findByFacilityId(facilityId.toString());
			String ownership = f.getOwnerCode();
			if(FacilityService.FACILITY_OWNER_PUBLIC.equals(ownership) || 
					(FacilityService.FACILITY_OWNER_PRIVATE.equals(ownership)&& !facilityService.isFacility(facilityId))){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEntered(Long facilityId) {
		Collection needs = needsService.getNeedsInfo(facilityId.toString());
		//for each need doc check if a capital cost exists if yes entered else false
		for (Iterator iter = needs.iterator(); iter.hasNext();) {
			NeedsHelper nh = (NeedsHelper) iter.next();
			Collection costs = capitalCostService.getCostList(facilityId.longValue(), nh.getDocumentId());
			if(costs.size()>0){
				return true;
			}
		}		
		return false;
	}

	public boolean isErrors(Long facilityId, Collection errors) {
		// parameters
		boolean isCostEntered = isEntered(facilityId);
		boolean isCostRequired = isRequired(facilityId);
		Facility f = facilityService.findByFacilityId(facilityId.toString());
		Collection cft = facilityTypeService.getFacityType(facilityId);
		String ownership = f.getOwnerCode();
		boolean isError = false;
		Collection needs = needsService.getNeedsInfo(facilityId.toString());
		Collection facilityCosts =capitalCostService.getFacilityCosts(facilityId);
		Collection fcc = costCurveService.getFacilityCostCurves(facilityId);
		
		//Validation
		
		//is required not entered
		if(isCostRequired && !isCostEntered){
			errors.add("error.needs.required");
			return true;
		}		
		
		//if a facility is privately owned and the facility Type is not eligible for private federal funds then error 
		if(FacilityService.FACILITY_OWNER_PRIVATE.equals(ownership) && cft!=null){
			for (Iterator iter = cft.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				if(ft.getFacilityTypeRef().getFederalNeedsForPrivateFlag()=='N' && isCostEntered){
					errors.add("error.needs.private.costs");
					return true;
				}	
			}
		}
		
		//if not required and entered is true 
		if(!isCostRequired && !FacilityService.FACILITY_OWNER_PRIVATE.equals(ownership) && isCostEntered){
			errors.add("error.needs.noChange.hasCosts");
			return true;
		}
		
		
		
		//For each facility type at least one category must have cost assigned
		if(isCostRequired){
			for (Iterator iter = cft.iterator(); iter.hasNext();) {
				FacilityType ft = (FacilityType) iter.next();
				if(!facilityTypeService.isFacilityTypeNoChangeOrAbandoned(facilityId, new Long(ft.getId().getFacilityTypeId()))){
					Set cats = ft.getFacilityTypeRef().getFacilityTypeCategoryRefs();
					if(!isCostAssignedToAtleastOneCategory(facilityId, cats)){
						errors.add("error.needs.costs.atleast.oneCat");
						isError = true;
						break;
					}					
				}
			}			
		}
		
		//For each cost category there must be at least on facility type change
		for (Iterator iter = facilityCosts.iterator(); iter.hasNext();) {
			Cost fCost = (Cost) iter.next();
			if(!isFacilityTypeCategoryValid(facilityId, fCost.getCategoryRef(), cft)){
				errors.add("error.needs.costs.atleast.oneType");
				isError = true;
				break;
			}
			
		}
		
		//Documented Category V cost should be specified for a CSO facility with no change or abandoned and status is  requires costs curve or no needs 
		if(!isCSOValid(facilityId)){
			errors.add("error.needs.csoCosts.document");
			isError = true;
		}
		
		
		//category valid for SSO but SSO indicator is not set
		if(isSSOError(facilityCosts)){
			errors.add("error.needs.invalid.SSO");
			isError = true;
		}
		
		
		//CSO cost curve is assigned but 
		CombinedSewer cs = csoService.getFacilityCSOInfo(facilityId);
		if(isCSOCCAssigned(fcc)){
			if(cs==null){
				errors.add("error.needs.CCAssigned.noCSOInfo");
				isError = true;				
			}else{
				if(!"C".equals(cs.getCombinedSewerStatusRef().getCombinedSewerStatusId()) &&
						!"B".equals(cs.getCombinedSewerStatusRef().getCombinedSewerStatusId())){
					errors.add("error.needs.CCAssigned.noValidCSOStatus");
					isError = true;
				}
			}
			
			//cso cc exist and this facility is in a sewershed that includes multiple states
			Collection facilityLocations =populationService.getRelatedSewerShedFacilitiesLocations(facilityId.toString());
			if(facilityLocations.size()>1){ //multi-state sewersheds
				errors.add("error.needs.CCCSOAssigned.multiState");
				isError = true;
				
			}
			
		}		
			
			//if costs assigned
		if(facilityCosts.size()>0){
			if(isTreamentPlantWithIncreasedLevelOfTreatement(cft)){
				long presentEfflentLevel = effluentService.getPresentFacilityEffluentLevel(facilityId);
				long projectedEfflentLevel = effluentService.getProjectedFacilityEffluentLevel(facilityId);
				if((presentEfflentLevel== EffluentService.EFFLUENT_TREATMENT_LEVEL_ADAVANCE_PRIMARY_ID||
						presentEfflentLevel== EffluentService.EFFLUENT_TREATMENT_LEVEL_PRIMARY_ID ||
						presentEfflentLevel== EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID) &&
						projectedEfflentLevel == EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
					//check if catII cost are specified otherwise error
					Collection ncs = capitalCostService.getCostsByCategory(facilityId, CapitalCostService.CATEGORY_ID_ADVANCE_TREATMENT);
					if(ncs.isEmpty()){						
						errors.add("error.needs.advance.treatment");
						isError = true;
					}			
				}
			}
		}
		
		
		//if a cost curve exists and no doc that is valid for needs exists
		if(isFederalCostsExists(facilityCosts) && !isDocumentValidForNeeds(needs)){
			errors.add("error.needs.federalCost.noValidDocument");
			isError = true;
		}
		
		//out dated engineers signature
		for (Iterator iter = needs.iterator(); iter.hasNext();) {
			NeedsHelper need = (NeedsHelper)iter.next();
			Document doc = needsService.getDocument(need.getDocumentId());
			if(doc.getOutdatedDocCertificatnFlag()!=null && doc.getOutdatedDocCertificatnFlag().charValue()=='Y' && 
					(doc.getDescription()==null || "".equals(doc.getDescription()))){
				errors.add("error.needs.outofDate.engineerSign");
				isError = true;
				break;
			}			
		}	
		
		//least 1 CC has run (i.e. assign_or_run_flag = R), AND has errors (i.e. at least one facility_cost_curve_data_area.error = Y for the CC)
				
		//A Cost Curve that has allocated Capital Cost now has a Cost Curve error. Please correct the error or unassign the Cost Curve.
		//get a list of cost curves assocaited with this facility
		//loop and check if any of them have re-run flag set to y and has errors
		if(fcc!=null & !fcc.isEmpty()){
			for (Iterator iter = fcc.iterator(); iter.hasNext();) {
				FacilityCostCurve fc = (FacilityCostCurve) iter.next();
				if(fc.getCurveRerunFlag()=='Y' && costCurveHasErrors(fc.getFacilityCostCurveDataAreas())){
					errors.add("error.needs.ccErrors");
					isError = true;
					break;
				}				
			}
		} 
		return isError;
	}
	
	private boolean costCurveHasErrors(Collection fccdas){
		if(fccdas!=null && !fccdas.isEmpty()){
			for (Iterator iterator = fccdas.iterator(); iterator.hasNext();) {
				FacilityCostCurveDataArea fccda = (FacilityCostCurveDataArea) iterator.next();
				if(fccda.getErrorFlag()=='Y'){
					return true;	
				}
			}	
		}
		return false;
	}
		
	private boolean isTreamentPlantWithIncreasedLevelOfTreatement(Collection facilityTypes){
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(ft.getId().getFacilityTypeId()==FacilityTypeService.FACILITY_TYPE_TREATMENT_PLANT.longValue()){
				if(ft.getPresentFlag()=='Y' && ft.getProjectedFlag()=='Y'){
					Collection changeType = ft.getFacilityTypeChanges();
					for (Iterator iterator = changeType.iterator(); iterator
							.hasNext();) {
						FacilityTypeChange ftc = (FacilityTypeChange) iterator.next();
						if(ftc.getId().getChangeTypeId()== FacilityTypeService.CHANGE_TYPE_INCREASE_LEVEL_OF_TREATMENT.longValue()){
							return true;
						}
						
					}
					
				}
				
			}
			
		}
		return false;
	}
	
	private boolean isCSOCCAssigned(Collection fcc) {
		for (Iterator iter = fcc.iterator(); iter.hasNext();) {
			FacilityCostCurve cc = (FacilityCostCurve) iter.next();
			if(cc.getCostCurveRef().getCostCurveId()==7){
				return true;
			}
		}
		return false;
	}

	private boolean isFacilityTypeCategoryValid(Long facilityId, CategoryRef cr, Collection facilityTypes) {
		for (Iterator iter = facilityTypes.iterator(); iter.hasNext();) {
			FacilityType ft = (FacilityType) iter.next();
			if(!isFacilityTypeNoChange(ft.getFacilityTypeChanges())){
				//is valid category for the facilityType
				if(isCatContained(ft.getFacilityTypeRef().getFacilityTypeCategoryRefs(), cr.getCategoryId())){
					return true;
				}	
			}
		}
		return false;
	}

	private boolean isCatContained(Collection cats, String catId){
		if(cats==null)return false;
		for (Iterator iter = cats.iterator(); iter.hasNext();) {
			FacilityTypeCategoryRef ftcr = (FacilityTypeCategoryRef) iter.next();
			if(ftcr.getId().getCategoryId().equals(catId)){
				return true;
			}
		} 
		return false;		
	}
	
	
	private boolean isFacilityTypeNoChange(Collection changes){
		for (Iterator iter = changes.iterator(); iter.hasNext();) {
			FacilityTypeChange ftc = (FacilityTypeChange) iter.next();
			if(ftc.getId().getChangeTypeId()== FacilityTypeService.CHANGE_TYPE_NO_CHANGE.longValue()){
				return true;
			}			
		}
		return false;
	}

	private boolean isCostAssignedToAtleastOneCategory(Long facilityId, Set cats){
		for (Iterator iterator = cats.iterator(); iterator.hasNext();) {
			FacilityTypeCategoryRef cr = (FacilityTypeCategoryRef) iterator.next();
			Collection costs = capitalCostService.getCostsByCategory(facilityId, cr.getId().getCategoryId());
			if(costs.size()>0){
				return true;
			}
		}		
		return false;
	}

	
	public boolean isCSOValid(Long facilityId) {
		if(facilityTypeService.isFacilityTypeValidForDataAreaAndHasChangeType(facilityId, FacilityService.DATA_AREA_CSO, FacilityTypeService.CHANGE_TYPE_NO_CHANGE) ||
		facilityTypeService.isFacilityTypeValidForDataAreaAndHasChangeType(facilityId, FacilityService.DATA_AREA_CSO, FacilityTypeService.CHANGE_TYPE_ABANDONED)){
			CombinedSewer cs = csoService.getFacilityCSOInfo(facilityId);
			if(cs!=null){
				CombinedSewerStatusRef  cssr = cs.getCombinedSewerStatusRef();
				if(("C".equals(cssr.getCombinedSewerStatusId()) || "N".equals(cssr.getCombinedSewerStatusId()))){
					Collection cat5Costs = capitalCostService.getCostsByCategory(facilityId, "V");
					if(cat5Costs!=null){
						for (Iterator iter = cat5Costs.iterator(); iter
								.hasNext();) {
							Cost c = (Cost) iter.next();
							if(c.getCostMethodCode()=='D'){
								return false;
							}
						}
					}
											
				}
			}
		}
		return true;
	}
	
	public boolean isDocumentValidForNeeds(Collection needs){
		for (Iterator iter = needs.iterator(); iter.hasNext();) {
			NeedsHelper need = (NeedsHelper)iter.next();
			Document doc = needsService.getDocument(need.getDocumentId());
			if(doc.getDocumentTypeRef()!=null && doc.getDocumentTypeRef().getAprovedForNeedsFlag()=='Y'){
				return true;
			}
		}
		return false;		
	}
	
	public boolean isFederalCostsExists(Collection costs){
		for (Iterator iter = costs.iterator(); iter.hasNext();) {
			Cost cost = (Cost)iter.next();
			if(cost.getNeedTypeRef()!=null){
				if("F".equals(cost.getNeedTypeRef().getNeedTypeId())){
					return true;
				}				
			}
		}
		return false;		
	}
	
	public boolean isSSOError(Collection costs){
		for (Iterator iter = costs.iterator(); iter.hasNext();) {
			Cost cost = (Cost)iter.next();
			if(cost.getCategoryRef().getValidForSsoFlag()=='Y' && cost.getSsoFlag()== null){
				return true;
			}				
		}
		return false;		
	}
	
	

	public void setFesService(FESService fes) {
		fesService = fes;
	}
	
	private FacilityTypeService facilityTypeService;
	public void setFacilityTypeService(FacilityTypeService facilityTypeService) {
		this.facilityTypeService = facilityTypeService;
	}
	
	private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
	private CapitalCostService capitalCostService;
	public void setCapitalCostService(CapitalCostService capitalCostService) {
		this.capitalCostService = capitalCostService;
	}

	private NeedsService needsService;	
	public void setNeedsService(NeedsService needsService) {
		this.needsService = needsService;
	}
	protected CSOService csoService;
	public void setCsoService(CSOService cso){
		csoService = cso;
	}
	
	protected PopulationService populationService;
	public void setPopulationService (PopulationService ps){
		populationService = ps;
	}
	
	protected EffluentService effluentService;
	public void setEffluentService(EffluentService effluentService) {
		this.effluentService = effluentService;
	}
	
	protected CostCurveService costCurveService;
	public void setCostCurveService(CostCurveService ccs) {
		costCurveService = ccs;
	}
	
}
