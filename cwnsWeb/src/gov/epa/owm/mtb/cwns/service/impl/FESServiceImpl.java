package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.DataAreaFesRuleRef;
import gov.epa.owm.mtb.cwns.model.DataAreaRef;
import gov.epa.owm.mtb.cwns.model.FacilityDataAreaMessage;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityDataAreaMessageId;
import gov.epa.owm.mtb.cwns.service.FESService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

public class FESServiceImpl extends CWNSService implements FESService{
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	
	public void updateFes(boolean validateRequired, char requiredFlag, char enteredFlag, char errorFlag, ArrayList errors, Long facilityId, Long dataAreaId, String userId){
		//get fes record
		FacilityEntryStatus fes = getFacilityEntryStatus(facilityId, dataAreaId);		
		if(fes != null){
			if(validateRequired){
				fes.setRequiredFlag(requiredFlag);
			}
			fes.setEnteredFlag(enteredFlag);
			fes.setErrorFlag(errorFlag);
			//delete if any existing error messages
			Collection fesErrors= getFacilityDataAreaMessages(facilityId, dataAreaId);
			deleteFacilityEntryStatusErrors(fesErrors);
			//set any error messages
			if(errorFlag=='Y'){
				if(errors.size()>0){
					//Set newFESErrors=createFacilityEntryStatusErrors(fes, errors, userId);
					createFacilityEntryStatusErrors(fes, errors, userId);
					//fes.setFacilityDataAreaMessages(newFESErrors);
				}else{
					log.debug("FES found errors but, no error messages are defined");
				}
			}else{
				//fes.setFacilityDataAreaMessages(new HashSet());
			}			
			saveFacilityEntryStatus(fes, userId);
		}else{
			log.debug("No FES found for " + facilityId + " Data Area " + dataAreaId);
		}
	}
	

	private Collection getFacilityDataAreaMessages(Long facilityId, Long dataAreaId){		
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		scs.setCondition(new SearchCondition ("sourceCode", SearchCondition.OPERATOR_EQ, "fes"));		
		return searchDAO.getSearchList(FacilityDataAreaMessage.class, scs);
	}
	
	
	
	
	private void saveFacilityEntryStatus(FacilityEntryStatus fes, String userId) {
		fes.setLastUpdateUserid(userId);
		fes.setLastUpdateTs(new Date());
		searchDAO.saveObject(fes);
		//Set s = fes.getFacilityDataAreaMessages();
		//for (Iterator iter = s.iterator(); iter.hasNext();) {
		//	FacilityDataAreaMessage fesErr = (FacilityDataAreaMessage) iter.next();
		//	log.debug("saved " + fesErr.getId().getDataAreaId() + " fid-" + fesErr.getId().getFacilityId()+ " -" + fesErr.getId().getErrorMessageKey());	
		//}

	}

	public FacilityEntryStatus getFacilityEntryStatus(Long facilityId, Long dataAreaId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, dataAreaId));
		return (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs);	
	}


	public void createFacilityEntryStatusErrors(FacilityEntryStatus fes, ArrayList errors, String userId) {
		//Set fesErrSet = new HashSet();
		long facilityId =fes.getId().getFacilityId();
		long dataAreaId = fes.getId().getDataAreaId();
		
		for (Iterator iter = errors.iterator(); iter.hasNext();) {
			FacilityDataAreaMessage fesErr = new FacilityDataAreaMessage();
			String errorMessageKey = (String) iter.next();
			FacilityDataAreaMessageId fesErrId = new FacilityDataAreaMessageId(facilityId, dataAreaId, errorMessageKey);
			fesErr.setId(fesErrId);
			fesErr.setFacilityEntryStatus(fes);
			fesErr.setSourceCode("fes");
			Date current = new Date();
			fesErr.setLastUpdateTs(current);
			fesErr.setLastUpdateUserid(userId);
			searchDAO.saveObject(fesErr);
			//fesErrSet.add(fesErr);
		}
		//return fesErrSet;
	}


	public void deleteFacilityEntryStatusErrors(Collection fesErrors) {
		for (Iterator iter = fesErrors.iterator(); iter.hasNext();) {
			FacilityDataAreaMessage fesErr = (FacilityDataAreaMessage) iter.next();
			searchDAO.removeObject(fesErr);
			searchDAO.flushAndClearCache();
			log.debug("Deleted " + fesErr.getId().getDataAreaId() + " fid-" + fesErr.getId().getFacilityId()+ " -" + fesErr.getId().getErrorMessageKey());
		}
	}


	public Collection getDataAreaFESRule(Long currentDataAreaId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.dataAreaIdCurrent", SearchCondition.OPERATOR_EQ, currentDataAreaId));
		return searchDAO.getSearchList(DataAreaFesRuleRef.class, scs);	
	}
	
	
	public Collection getDataAreaFESErrors(long facilityId,  long dataAreaId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(new SearchCondition ("id.dataAreaId", SearchCondition.OPERATOR_EQ, new Long(dataAreaId)));
		return searchDAO.getSearchList(FacilityDataAreaMessage.class, scs);
	}
	
	public void deleteFacilityEntryStatus(FacilityEntryStatus fes){
		deleteFacilityEntryStatusErrors(getFacilityDataAreaMessages(new Long(fes.getId().getFacilityId()), new Long(fes.getId().getDataAreaId()) ));
		searchDAO.removeObject(fes);
	}
	
	public boolean isDataAreaApplicable(Long facilityId, Long dataAreaId){
		FacilityEntryStatus fes = getFacilityEntryStatus(facilityId, dataAreaId);
		if(fes ==null){
			return false;
		}
		return true;
	}
	

}
