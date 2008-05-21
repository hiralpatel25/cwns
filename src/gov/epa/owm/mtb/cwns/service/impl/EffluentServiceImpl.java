package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.costcurve.service.TreatmentPlantCostCurveService;
import gov.epa.owm.mtb.cwns.dao.FacilityDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.AdvancedTreatmentTypeRef;
import gov.epa.owm.mtb.cwns.model.EffluentTreatmentLevelRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityAdvancedTreatment;
import gov.epa.owm.mtb.cwns.model.FacilityAdvancedTreatmentId;
import gov.epa.owm.mtb.cwns.model.FacilityEffluent;
import gov.epa.owm.mtb.cwns.model.FacilityEffluentId;
import gov.epa.owm.mtb.cwns.service.EffluentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EffluentServiceImpl extends CWNSService implements EffluentService {
	
	public static final int ADVANCED_TREATMENT_TYPE_BOD = 1;
	public static final int ADVANCED_TREATMENT_TYPE_NITROGEN_REMOVAL = 2;
	public static final int ADVANCED_TREATMENT_TYPE_PHOSPHOROUS_REMOVAL = 3;
	public static final int ADVANCED_TREATMENT_TYPE_NUTRIENT_REMOVAL = 10;
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}	
	
	private FacilityDAO facilityDAO;
	public void setFacilityDAO(FacilityDAO fdao) {
		this.facilityDAO = fdao;
	}	
	
	public long getPresentFacilityEffluentLevel(Long facilityId){
		FacilityEffluent fe =  getFacilityEffluentLevel(facilityId, EffluentService.EFFLUENT_LEVEL_TYPE_PRESENT);
		long levelId =0;
		if(fe !=null && fe.getId()!=null){
			levelId = fe.getId().getEffluentTreatmentLevelId();
		}
		return levelId; 
	}
	
	public long getProjectedFacilityEffluentLevel(Long facilityId){
		FacilityEffluent fe = getFacilityEffluentLevel(facilityId, EffluentService.EFFLUENT_LEVEL_TYPE_PROJECTED);
		long levelId =0;
		if(fe !=null && fe.getId()!=null){
			levelId = fe.getId().getEffluentTreatmentLevelId();
		}
		return levelId; 
	}
	
	public boolean isPresentdFacilityDisinfected(Long facilityId){
		boolean isDisinfected = false;
		FacilityEffluent fe =  getFacilityEffluentLevel(facilityId, EffluentService.EFFLUENT_LEVEL_TYPE_PRESENT);
		char disinfectionFlag =0;
		if(fe !=null && fe.getId()!=null){
			disinfectionFlag = fe.getDisinfectionFlag();
		}
		
		isDisinfected = disinfectionFlag=='Y'?true:false;
		return isDisinfected;
	}
	
	public boolean isProjectedFacilityDisinfected(Long facilityId){
		boolean isDisinfected = false;
		FacilityEffluent fe =  getFacilityEffluentLevel(facilityId, EffluentService.EFFLUENT_LEVEL_TYPE_PROJECTED);
		char disinfectionFlag =0;
		if(fe !=null && fe.getId()!=null){
			disinfectionFlag = fe.getDisinfectionFlag();
		}
		
		isDisinfected = disinfectionFlag=='Y'?true:false;
		return isDisinfected;		
	}
	
	private FacilityEffluent getFacilityEffluentLevel(Long facilityId, char projectCode){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition("id.presentOrProjectedCode", SearchCondition.OPERATOR_EQ, new Character(projectCode)));
		FacilityEffluent fe = (FacilityEffluent)searchDAO.getSearchObject(FacilityEffluent.class, scs);
		return fe;		
	}
	

	public int getPresentProjectEffluentAdvancedTypeSubtype(Long facilityId, String projectCode) {
		return getProjectEffluentAdvancedTypeSubtype(facilityId, "P");
	}	
	
	public int getProjectedProjectEffluentAdvancedTypeSubtype(Long facilityId, String projectCode) {
		return getProjectEffluentAdvancedTypeSubtype(facilityId, "F");
	}
	
	private int getProjectEffluentAdvancedTypeSubtype(Long facilityId, String projectCode) {
		FacilityAdvancedTreatment advancedTreatmentBOD = getProjectAdvancedTreatmentByAdvanceTreatmentTypeAndCode(facilityId, 
				projectCode, ADVANCED_TREATMENT_TYPE_BOD);
		FacilityAdvancedTreatment advancedTreatmentNutrient_Removal = getProjectAdvancedTreatmentByAdvanceTreatmentTypeAndCode(facilityId, 
				projectCode, ADVANCED_TREATMENT_TYPE_NUTRIENT_REMOVAL);		
		List projectAdvancedTreatmentListExcludeBOD = getProjectAdvancedTreatmentListByProjectcodeExcludeBODNutrientRemoval(facilityId, 
				projectCode);
		int advancedTypeSubtype = TreatmentPlantCostCurveService.EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT;
		if (advancedTreatmentBOD == null) {  //WITHOUT BOD
			if (((projectAdvancedTreatmentListExcludeBOD == null || projectAdvancedTreatmentListExcludeBOD.size() ==0))
					&& (advancedTreatmentNutrient_Removal == null))
				advancedTypeSubtype = TreatmentPlantCostCurveService.EFFLUENT_ADVANCED_NO_BOD_AND_NO_OTHER_ADVANCED_TREATMENT;
			else if (projectAdvancedTreatmentListExcludeBOD.size() == 1 ) 
				advancedTypeSubtype = TreatmentPlantCostCurveService.EFFLUENT_ADVANCED_NO_BOD_AND_1_OTHER_ADVANCED_TREATMENT;
			else if (projectAdvancedTreatmentListExcludeBOD.size() >= 2) 
				advancedTypeSubtype = TreatmentPlantCostCurveService.EFFLUENT_ADVANCED_NO_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT; 
		} else { // WITH BOD
			if (((projectAdvancedTreatmentListExcludeBOD == null || projectAdvancedTreatmentListExcludeBOD.size() == 0))
					&& (advancedTreatmentNutrient_Removal == null))
				advancedTypeSubtype = TreatmentPlantCostCurveService.EFFLUENT_ADVANCED_WITH_BOD_AND_NO_OTHER_ADVANCED_TREATMENT;			
			else if (projectAdvancedTreatmentListExcludeBOD.size() == 1 ) 
				advancedTypeSubtype = TreatmentPlantCostCurveService.EFFLUENT_ADVANCED_WITH_BOD_AND_1_OTHER_ADVANCED_TREATMENT;
			else if (projectAdvancedTreatmentListExcludeBOD.size() >= 2) 
				advancedTypeSubtype = TreatmentPlantCostCurveService.EFFLUENT_ADVANCED_WITH_BOD_AND_AT_LEAST_2_OTHER_ADVANCED_TREATMENT; 			
		}
		return advancedTypeSubtype;
	}
	
	private FacilityAdvancedTreatment getProjectAdvancedTreatmentByAdvanceTreatmentTypeAndCode(Long facilityId, 
			String projectCode, int advanceTreatmentType) {
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition("id.presentOrProjectedCode", SearchCondition.OPERATOR_EQ, projectCode));		
		scs.setCondition(new SearchCondition("id.advancedTreatmentTypeId", SearchCondition.OPERATOR_EQ, new Long(advanceTreatmentType)));		
		FacilityAdvancedTreatment fe = (FacilityAdvancedTreatment)searchDAO.getSearchObject(FacilityAdvancedTreatment.class, scs);		
		return fe;
	}
	
	/**
	 * Exclude Advanced Treatment BOD and Nutrient Removal 
	 * @param facilityId
	 * @param projectCode
	 * @return
	 */
	private List getProjectAdvancedTreatmentListByProjectcodeExcludeBODNutrientRemoval(Long facilityId, String projectCode) {
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition("id.presentOrProjectedCode", SearchCondition.OPERATOR_EQ, projectCode));		
		scs.setCondition(new SearchCondition("id.advancedTreatmentTypeId", SearchCondition.OPERATOR_NOT_EQ, new Long(ADVANCED_TREATMENT_TYPE_BOD)));
		scs.setCondition(new SearchCondition("id.advancedTreatmentTypeId", SearchCondition.OPERATOR_NOT_EQ, new Long(ADVANCED_TREATMENT_TYPE_NUTRIENT_REMOVAL)));		
		List list = searchDAO.getSearchList(FacilityAdvancedTreatment.class, scs);		
		return list;
	}	
	
	public Collection getPresentAdvanceTreatmentTypes(Long facilityId){
		return getAdvanceTreatmentTypes(facilityId, EffluentService.ADVANCED_TREATMENT_TYPE_PRESENT);		
	}
	
	public Collection getProjectedAdvanceTreatmentTypes(Long facilityId){
		return getAdvanceTreatmentTypes(facilityId, EffluentService.ADVANCED_TREATMENT_TYPE_PROJECTED);		
	}
	
	private Collection getAdvanceTreatmentTypes(Long facilityId, char projectCode){
		SearchConditions scs = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition("id.presentOrProjectedCode", SearchCondition.OPERATOR_EQ, new Character(projectCode)));
		Collection c = searchDAO.getSearchList(FacilityAdvancedTreatment.class, scs);
		return c;
	}
	
	public Collection getAdvanceTreatmentTypes(){
		SortCriteria sortCriteria = new SortCriteria("advancedTreatmentTypeId", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		return searchDAO.getSearchList(AdvancedTreatmentTypeRef.class, new SearchConditions(),  sortArray);		
	}
	
	public Collection getEffluentTreatmentLevel(){
		SortCriteria sortCriteria = new SortCriteria("sortSequence", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		return searchDAO.getSearchList(EffluentTreatmentLevelRef.class, new SearchConditions(), sortArray);
	}
	
	public EffluentTreatmentLevelRef getEffluentTreatmentLevelById(long effluentLevelId){
		SearchConditions scs = new SearchConditions(new SearchCondition("effluentTreatmentLevelId", SearchCondition.OPERATOR_EQ, new Long(effluentLevelId)));
		return (EffluentTreatmentLevelRef)searchDAO.getSearchObject(EffluentTreatmentLevelRef.class, scs);
	}
	
	public void saveEffluent(Long facilityId, long effluentTreatmentLevelId, String[] advanceTreamentIndicators,
			char presentProjectedCode, char disinfectionFlag, String userId){
		FacilityEffluent fe = getFacilityEffluentLevel(facilityId, presentProjectedCode);
		
		if(fe!=null){
			//delete old stuff
			deleteEffluentInfo(fe);
		}
		
		fe = new FacilityEffluent();
		FacilityEffluentId fei = new FacilityEffluentId();
		fei.setFacilityId(facilityId.longValue());
		fei.setEffluentTreatmentLevelId(effluentTreatmentLevelId);
		fei.setPresentOrProjectedCode(presentProjectedCode);
		fe.setId(fei);
		fe.setDisinfectionFlag(disinfectionFlag);
		

		EffluentTreatmentLevelRef effluentTreatmentLevelRef = getEffluentTreatmentLevelById(effluentTreatmentLevelId);
		fe.setEffluentTreatmentLevelRef(effluentTreatmentLevelRef);
		
		//get facility
		Facility facility = facilityDAO.findByFacilityId(facilityId.longValue()+"");
		fe.setFacility(facility);
		
		//all the indicators are removed at this point
		fe.setFacilityAdvancedTreatments(new HashSet());
		fe.setLastUpdateTs(new Date());
		fe.setLastUpdateUserid(userId);
		searchDAO.saveObject(fe);
		
		//add new indicators if applicable
		if(EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID == effluentTreatmentLevelId){
			Set indicators = new HashSet();
			if(advanceTreamentIndicators!=null && advanceTreamentIndicators.length>0){
				for (int i = 0; i < advanceTreamentIndicators.length; i++) {
					 String idStr = advanceTreamentIndicators[i];
					 FacilityAdvancedTreatment fat = new FacilityAdvancedTreatment();
					 FacilityAdvancedTreatmentId fatId = new FacilityAdvancedTreatmentId();
					 fatId.setAdvancedTreatmentTypeId((new Long(idStr)).longValue());
					 fatId.setEffluentTreatmentLevelId(effluentTreatmentLevelId);
					 fatId.setFacilityId(facilityId.longValue());
					 fatId.setPresentOrProjectedCode(presentProjectedCode);
					 fat.setId(fatId);
					 fat.setFacilityEffluent(fe);
					 fat.setLastUpdateTs(new Date());
					 fat.setLastUpdateUserid(userId);
					 searchDAO.saveObject(fat);
					 indicators.add(fat);
				}
			}
			fe.setFacilityAdvancedTreatments(indicators);
			searchDAO.saveObject(fe);
		}		
	}
	
	public void deleteEffluentInfo(FacilityEffluent fe){
		Set treatmentIndicators = fe.getFacilityAdvancedTreatments();
		if(treatmentIndicators!=null && !treatmentIndicators.isEmpty()){
			searchDAO.deleteAll(treatmentIndicators);
		}
		searchDAO.removeObject(fe);
		searchDAO.flushAndClearCache();
	}	
	
	public int getTreatmentLevelOrder(long treatmentLevelId, Collection advanceIndicators){
		if(treatmentLevelId==EffluentService.EFFLUENT_TREATMENT_LEVEL_PRIMARY_ID || treatmentLevelId==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADAVANCE_PRIMARY_ID){
			return 1;
		}else if(treatmentLevelId==EffluentService.EFFLUENT_TREATMENT_LEVEL_SECONDARY_ID){
			return 2;			
		}else if(treatmentLevelId==EffluentService.EFFLUENT_TREATMENT_LEVEL_ADVANCE_ID){
			boolean isBOD = false;
			boolean isNitrogen = false;
			boolean isPhosphorous = false;			
			for (Iterator iter = advanceIndicators.iterator(); iter.hasNext();) {
				FacilityAdvancedTreatment fAttr = (FacilityAdvancedTreatment) iter.next();
				if(EffluentService.EFFLUENT_TREATMENT_INDICATOR_BOD == fAttr.getId().getAdvancedTreatmentTypeId()){
					isBOD=true;
				}else if(EffluentService.EFFLUENT_TREATMENT_INDICATOR_N == fAttr.getId().getAdvancedTreatmentTypeId()){
					isNitrogen=true;
				}else if(EffluentService.EFFLUENT_TREATMENT_INDICATOR_P == fAttr.getId().getAdvancedTreatmentTypeId()){
					isPhosphorous=true;
				}
			}
			if(!isBOD && !isNitrogen && !isPhosphorous){
				return 3;
			}else if(!isBOD && (isNitrogen && isPhosphorous)){
				return 5;
			}else if(!isBOD && (isNitrogen || isPhosphorous)){
				return 4;
			}else if(isBOD && !isNitrogen && !isPhosphorous){
				return 6;
			}else if(isBOD && (isNitrogen && isPhosphorous)){
				return 8;
			}else if(isBOD && (isNitrogen || isPhosphorous)){
				return 7;
			}
		}
		return 0;
	}	
}
