package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.CountyRef;
import gov.epa.owm.mtb.cwns.model.EfPermit;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityAddress;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityPermit;
import gov.epa.owm.mtb.cwns.model.FacilityPermitId;
import gov.epa.owm.mtb.cwns.model.FacilityPointOfContact;
import gov.epa.owm.mtb.cwns.model.GeographicArea;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCounty;
import gov.epa.owm.mtb.cwns.model.GeographicAreaCountyId;
import gov.epa.owm.mtb.cwns.model.GeographicAreaTypeRef;
import gov.epa.owm.mtb.cwns.model.Permit;
import gov.epa.owm.mtb.cwns.model.PermitTypeRef;
import gov.epa.owm.mtb.cwns.model.PointOfContact;
import gov.epa.owm.mtb.cwns.model.StateRef;
import gov.epa.owm.mtb.cwns.permits.PermitsSearchListHelper;
import gov.epa.owm.mtb.cwns.service.FacilityAddressService;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PocService;
import gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactForm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.rpc.ServiceException;

import Indexing_Service.waters.rti.org.Waters_Indexing_Service;
import Indexing_Service.waters.rti.org.Waters_Indexing_ServiceLocator;
import Indexing_Service.waters.rti.org.Waters_Indexing_ServiceSoap;

public class FacilityPermitServiceImpl extends CWNSService implements
		FacilityPermitService {
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}
	
	private FacilityService facilityService;
	public void setFacilityService(FacilityService facilityService) {
		this.facilityService = facilityService;
	}
	
	private FacilityAddressService facilityAddressService;
	public void setFacilityAddressService(
			FacilityAddressService facilityAddressService) {
		this.facilityAddressService = facilityAddressService;
	}
	private PocService pocService;
	public void setPocService(PocService pocService) {
		this.pocService = pocService;
	}
	
	PointOfContactForm pocf;	
	public Collection getPermitsByFacilityId(Long facilityId){
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("permit", "p", AliasCriteria.JOIN_INNER));
		//aliasArray.add(new AliasCriteria("p.permitTypeRef", "c", AliasCriteria.JOIN_INNER));
		SortCriteria sortCriteria = new SortCriteria("p.permitNumber", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection c = searchDAO.getSearchList(FacilityPermit.class, new ArrayList(), scs, sortArray, aliasArray);
		return c;
	}

	public void deleteFacilityPermit(Long facilityId, Long permitId, CurrentUser user) {
				
		FacilityPermit facilityPermit = getFacilityPermitByPrimaryKey(permitId,facilityId);
		if (facilityPermit != null){
           // if local user mark as deleted
		   if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
				if (facilityPermit.getFeedbackDeleteFlag()=='Y') {
					facilityPermit.setFeedbackDeleteFlag('N');
				}else{
					facilityPermit.setFeedbackDeleteFlag('Y');
		   		     }   	    		
		   		  searchDAO.saveObject(facilityPermit);
			}
			else{
			    char usedflg = facilityPermit.getUsedForFacilityLocatnFlag();
			    searchDAO.removeObject(facilityPermit);
			    if (usedflg=='Y'){
				  updatePORAndFacilityAddress(facilityId, user);
			    }
			}    
		}	
		
	}

	public void updatePORAndFacilityAddress(Long facilityId, CurrentUser user) {
		FacilityAddress facAddress = null;
		SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		facAddress = (FacilityAddress)searchDAO.getSearchObject(FacilityAddress.class, scs1);
		if (facAddress != null){
			facAddress.setSourcedFromNpdesFlag('N');
			searchDAO.saveObject(facAddress);
		}
		AbsoluteLocationPoint obj = null;
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		obj = (AbsoluteLocationPoint)searchDAO.getSearchObject(AbsoluteLocationPoint.class, scs2, aliasArray);
		if (obj != null){
			obj.setSourceCd('M');
			searchDAO.saveObject(obj);
		}
		updateFacilityEntryStatus(facilityId, user, new Long(2));
	}

	private void updateFacilityEntryStatus(Long facilityId, CurrentUser user, Long dataAreaId){
		SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ, dataAreaId));
		FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
		if (facilityEntryStatus != null){
			facilityEntryStatus.setDataAreaLastUpdateTs(new Date());
			searchDAO.saveObject(facilityEntryStatus);
		}		
	}
	
	public FacilityPermit getFacilityPermitByPrimaryKey(Long permitId, Long facilityId) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("id.permitId", SearchCondition.OPERATOR_EQ, permitId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId",SearchCondition.OPERATOR_EQ,facilityId));
		FacilityPermit obj = (FacilityPermit)searchDAO.getSearchObject(FacilityPermit.class, scs);
		return obj;
	}
	
    public boolean isDuplicatePermit(Long facilityId, String permitNumber) {
		
		SearchConditions scs =  new SearchConditions(new SearchCondition("permitNumber", SearchCondition.OPERATOR_EQ, permitNumber));
        Permit permit = (Permit)searchDAO.getSearchObject(Permit.class, scs);
        if (permit!=null){
		log.debug("permitid in is duplicate--"+permit.getPermitId());
        SearchConditions scs1 =  new SearchConditions(new SearchCondition("id.permitId", SearchCondition.OPERATOR_EQ, new Long(permit.getPermitId())));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId",SearchCondition.OPERATOR_EQ,facilityId));
		Collection c = searchDAO.getSearchList(FacilityPermit.class, scs1);
		if (!c.isEmpty())
		  return true;
		else
			return false;
        }
        else
        	return false;
	}
	
	public Collection getPermitTypes(boolean excludePermitType){
		ArrayList columns = new ArrayList();
		columns.add("permitTypeId");
		columns.add("name");
		SearchConditions scs =  new SearchConditions(new SearchCondition("npdesFlag", SearchCondition.OPERATOR_EQ, new Character('N')));
		if (excludePermitType)
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("permitTypeId", SearchCondition.OPERATOR_NOT_EQ, new Long(40))); 
		SortCriteria sortCriteria = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		Collection results = searchDAO.getSearchList(PermitTypeRef.class, columns, scs, sortArray);
		return processEntitiesIdValue(results);	
    }
    
	private Collection processEntitiesIdValue(Collection results) {
		Collection col = new ArrayList();
		Iterator iter = results.iterator();	
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			col.add(new Entity(((Long)obj[0]).toString(),(String)obj[1]));
		}	
		return col;
	}
	
	public void addFacilityPermit(Long facilityId, long permitTypeId, String permitNumber, CurrentUser user) {
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("permitNumber", SearchCondition.OPERATOR_EQ, permitNumber));
        Permit permit = (Permit)searchDAO.getSearchObject(Permit.class, scs2);
				
		  SearchConditions scs3 =  new SearchConditions(new SearchCondition("permitTypeId", SearchCondition.OPERATOR_EQ, new Long(permitTypeId)));
		  PermitTypeRef permitTypeRef = (PermitTypeRef)searchDAO.getSearchObject(PermitTypeRef.class, scs3);
			if (permit!=null){
			  permit.setPermitTypeRef(permitTypeRef);
			}
			else{
				permit = new Permit();
			    permit.setPermitNumber(permitNumber);
			    permit.setPermitTypeRef(permitTypeRef);
			    permit.setLastUpdateTs(new Date());
			    permit.setLastUpdateUserid(user.getUserId());
			    searchDAO.saveObject(permit);
			}
		FacilityPermit facilityPermit = new FacilityPermit();
		SearchConditions scs4 =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
        Facility facility = (Facility)searchDAO.getSearchObject(Facility.class, scs4);
		FacilityPermitId id = new FacilityPermitId(facility.getFacilityId(),permit.getPermitId());
		facilityPermit.setId(id);
		facilityPermit.setFeedbackDeleteFlag('N');
		facilityPermit.setPermit(permit);
		facilityPermit.setFacility(facility);
		facilityPermit.setUsedForFacilityLocatnFlag('N');
		facilityPermit.setLastUpdateTs(new Date());
		facilityPermit.setLastUpdateUserid(user.getUserId());
		searchDAO.saveObject(facilityPermit);
	}
	
	// Other
	public void updateFacilityPermit(Long facilityId, long permitId, long permitTypeId, String permitNumber, 
			 CurrentUser user) {
				
	      SearchConditions scs2 =  new SearchConditions(new SearchCondition("permitNumber", SearchCondition.OPERATOR_EQ, permitNumber));
          Permit permit = (Permit)searchDAO.getSearchObject(Permit.class, scs2);
				
		  SearchConditions scs3 =  new SearchConditions(new SearchCondition("permitTypeId", SearchCondition.OPERATOR_EQ, new Long(permitTypeId)));
		  PermitTypeRef permitTypeRef = (PermitTypeRef)searchDAO.getSearchObject(PermitTypeRef.class, scs3);
			if (permit!=null){
			  permit.setPermitTypeRef(permitTypeRef);
			}
			else{
				permit = new Permit();
			    permit.setPermitNumber(permitNumber);
			    permit.setPermitTypeRef(permitTypeRef);
			    permit.setLastUpdateTs(new Date());
			    permit.setLastUpdateUserid(user.getUserId());
			    searchDAO.saveObject(permit);
			}
		   if (permitId != permit.getPermitId()){
			   FacilityPermit obj = getFacilityPermitByPrimaryKey(new Long(permitId), facilityId);
				if (obj != null){
					searchDAO.removeObject(obj);
				}	
		   }
		   FacilityPermit facilityPermit = getFacilityPermitByPrimaryKey(new Long(permitId), facilityId);
		   if (facilityPermit == null){
		      facilityPermit = new FacilityPermit();
		   }   
		   FacilityPermitId id = new FacilityPermitId(facilityId.longValue(),permit.getPermitId());
		   facilityPermit.setId(id);
		   facilityPermit.setFeedbackDeleteFlag('N');
		   facilityPermit.setPermit(permit);
		   facilityPermit.setUsedForFacilityLocatnFlag('N');
		   facilityPermit.setLastUpdateTs(new Date());
		   facilityPermit.setLastUpdateUserid(user.getUserId());
		   searchDAO.saveObject(facilityPermit);
				
	}
	// NPDES
	public void updateFacilityPermit(Long facilityId, long permitId, Character usedForPORFlag, CurrentUser user ) {
		FacilityPermit facilityPermit = getFacilityPermitByPrimaryKey(new Long(permitId), facilityId);
		if (facilityPermit != null){
		  SearchConditions scs1 =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		  scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.permitId",SearchCondition.OPERATOR_NOT_EQ,new Long(permitId)));
		  scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("usedForFacilityLocatnFlag",SearchCondition.OPERATOR_EQ,new Character('Y')));
		  FacilityPermit obj1 = (FacilityPermit)searchDAO.getSearchObject(FacilityPermit.class, scs1);
		  if ((usedForPORFlag.charValue() == 'Y') && (obj1 != null)){
				obj1.setUsedForFacilityLocatnFlag('N');
				obj1.setLastUpdateTs(new Date());
				obj1.setLastUpdateUserid(user.getUserId());
				searchDAO.saveObject(obj1);
			}
			
		  facilityPermit.setUsedForFacilityLocatnFlag(usedForPORFlag.charValue());
		  facilityPermit.setLastUpdateTs(new Date());
		  facilityPermit.setLastUpdateUserid(user.getUserId());
		  searchDAO.saveObject(facilityPermit);
		}		
	}

	public boolean isPermitAlreadyUsed(Long facilityId, String permitNumber) {
		
		SearchConditions scs =  new SearchConditions(new SearchCondition("permitNumber", SearchCondition.OPERATOR_EQ, permitNumber));
        Permit permit = (Permit)searchDAO.getSearchObject(Permit.class, scs);
        if (permit!=null){
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("id.permitId", SearchCondition.OPERATOR_EQ, new Long(permit.getPermitId())));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId",SearchCondition.OPERATOR_NOT_EQ,facilityId));
		Collection c = searchDAO.getSearchList(FacilityPermit.class, scs1);
		if (!c.isEmpty())
		  return true;
		else
			return false;
        }
        else
        	return false;
	}
	
	

	public void associatePermits(Long facilityId, String[] permitNbrs, String userId) {
		if (permitNbrs == null) {
			return;    // nothing to do
		}
		
		for (int i = 0; i < permitNbrs.length; i++) {
			associatePermitToFacility(facilityId, permitNbrs[i], userId);
		}
		
	}
	
	/**
	 * Assoicate a Permit to a facility.
	 * @param facilityId
	 * @param permitNbr
	 * @param userId
	 */
	private void associatePermitToFacility(Long facilityId, String permitNbr,String userId) {
		SearchConditions scs = new SearchConditions(new SearchCondition("efNpdesPermitNumber", SearchCondition.OPERATOR_EQ, permitNbr));
		EfPermit obj = (EfPermit)searchDAO.getSearchObject(EfPermit.class, scs);
		
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("permitNumber", SearchCondition.OPERATOR_EQ, permitNbr));
        Permit permit = (Permit)searchDAO.getSearchObject(Permit.class, scs2);
				
		  SearchConditions scs3 =  new SearchConditions(new SearchCondition("permitTypeId", SearchCondition.OPERATOR_EQ, new Long(obj.getPermitTypeRef().getPermitTypeId())));
		  PermitTypeRef permitTypeRef = (PermitTypeRef)searchDAO.getSearchObject(PermitTypeRef.class, scs3);
			if (permit!=null){
			  permit.setPermitTypeRef(permitTypeRef);
			  
			}
			else{
				permit = new Permit();
			    permit.setPermitNumber(permitNbr);
			    permit.setPermitTypeRef(permitTypeRef);
			    permit.setEfPermit(obj);
			    permit.setLastUpdateTs(new Date());
			    permit.setLastUpdateUserid(userId);
			    searchDAO.saveObject(permit);
			}
			FacilityPermit facilityPermit = new FacilityPermit();
			FacilityPermitId id = new FacilityPermitId(facilityId.longValue(),permit.getPermitId());
			facilityPermit.setId(id);
			facilityPermit.setFeedbackDeleteFlag('N');
			facilityPermit.setPermit(permit);
			facilityPermit.setUsedForFacilityLocatnFlag('N');
			facilityPermit.setLastUpdateTs(new Date());
			facilityPermit.setLastUpdateUserid(userId);
			searchDAO.saveObject(facilityPermit);
	}

	public List getPermitSearchList(Long facilityId, String locationId, String keyword, int startIndex, int maxResults) {
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("permitTypeRef", "pt", AliasCriteria.JOIN_INNER));
		SearchConditions scs = createPermitsSearchConditions(facilityId, locationId, keyword);
		//Collection c = searchDAO.getSearchList(EfPermit.class, scs, new ArrayList(), startIndex, maxResults);
		SortCriteria sortCriteria = new SortCriteria("efNpdesPermitNumber", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		Collection c = searchDAO.getSearchList(EfPermit.class, scs, sortArray, aliasArray, startIndex, maxResults);
		ArrayList permitSearchListHelpers = new ArrayList();
		Iterator iter = c.iterator();
		while(iter.hasNext()){
			EfPermit obj = (EfPermit)iter.next();
			PermitsSearchListHelper PSLH = new PermitsSearchListHelper();
			PSLH.setPermitNumber(obj.getEfNpdesPermitNumber());
			PSLH.setCountyName(obj.getCountyName());
			PSLH.setFacilityName(obj.getFacilityName());
			if (obj.getPermitTypeRef()!=null)
			   PSLH.setPermitType(obj.getPermitTypeRef().getName());
			if (isPermitAlreadyUsed(facilityId, obj.getEfNpdesPermitNumber())){
				PSLH.setAssWithAnotherFac("Y");
			}
			permitSearchListHelpers.add(PSLH);	
		}
		return permitSearchListHelpers;
	}
	
	/**
	 * 
	 * @param facilityId
	 * @param locationId
	 * @param keyword
	 * @return
	 */
	public SearchConditions createPermitsSearchConditions(Long facilityId, String locationId, String keyword) {
		
		// Get existing NPDES Permits for the facility
		//String facId  = facilityId.toString();
		List existingPermits = getNPDESPermitsForFacility(facilityId);

		// create Collection of NPDES Permit Numbers to exclude
		Collection existingPermitNbrs = new ArrayList();
		Iterator iter = existingPermits.iterator();
		while (iter.hasNext()) {
			FacilityPermit facilityPermit = (FacilityPermit) iter.next();
			existingPermitNbrs.add(facilityPermit.getPermit().getPermitNumber());
		}
		
		// State Criteria
		
		String stateId = getFacilityStateId(locationId);
		SearchConditions scs  =  new SearchConditions(new SearchCondition("facilityLocationStateId", SearchCondition.OPERATOR_EQ, stateId));
		log.debug("keyword length---"+keyword.trim().length());
		// Keyword Criteria
		if (keyword != null && keyword.trim().length() > 0) {
			SearchConditions scs2 = new SearchConditions(new SearchCondition("efNpdesPermitNumber", SearchCondition.OPERATOR_LIKE, keyword));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("pt.name", SearchCondition.OPERATOR_LIKE, keyword));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("facilityName", SearchCondition.OPERATOR_LIKE, keyword));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("countyName", SearchCondition.OPERATOR_LIKE, keyword));
			scs.setCondition(SearchCondition.OPERATOR_AND, scs2);
		}
		
		// Exclude existing NPDES Permits Criteria
		if (!existingPermitNbrs.isEmpty()) {
			log.debug("is not empty");
			SearchConditions scs3 = new SearchConditions(new SearchCondition("efNpdesPermitNumber", SearchCondition.OPERATOR_NOT_IN, existingPermitNbrs));
			scs.setCondition(SearchCondition.OPERATOR_AND, scs3);
		}

		return scs;
	}
	
	/**
	 * Given a facility Id return the associated Collection of FacilityPermits where permit type is NPDES.
	 * @param facilityId
	 * @return
	 */
	public List getNPDESPermitsForFacility(Long facilityId) {
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("permit", "p", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("p.permitTypeRef", "pt", AliasCriteria.JOIN_INNER));
		SortCriteria sortCriteria = new SortCriteria("p.permitNumber", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("pt.npdesFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		return searchDAO.getSearchList(FacilityPermit.class, new ArrayList(), scs, sortArray, aliasArray);
	}
	
	public int getPermitSearchListCount(Long facilityId, String locationId, String keyword) {
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("permitTypeRef", "pt", AliasCriteria.JOIN_INNER));
		SearchConditions scs = createPermitsSearchConditions(facilityId, locationId, keyword);
		return (searchDAO.getSearchList(EfPermit.class, new ArrayList(), scs, new ArrayList(), aliasArray)).size();
		//return searchDAO.getCount(EfPermit.class, scs);
	}

	public Collection facilitiesAssWithPermit(String permitNbr, Long facilityId) {
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("permit", "p", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));
		SortCriteria sortCriteria = new SortCriteria("f.cwnsNbr", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_NOT_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("f.versionCode", SearchCondition.OPERATOR_EQ, new Character('S')));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("p.permitNumber", SearchCondition.OPERATOR_EQ, permitNbr));
		return searchDAO.getSearchList(FacilityPermit.class, new ArrayList(), scs, sortArray, aliasArray);
		
	}

	public EfPermit getNPDESPermitDetails(String permitNbr) {
		SearchConditions scs =  new SearchConditions(new SearchCondition("efNpdesPermitNumber", SearchCondition.OPERATOR_EQ, permitNbr));
		return (EfPermit)searchDAO.getSearchObject(EfPermit.class, scs);
	}
	
	public void createOrUpdateInfo(Long facilityId, CurrentUser user, long permitId){
		createOrUpdateInfo(facilityId, user, permitId, true);
	}
	public void updateUseDataNPDESPermitInfo(CurrentUser user, int start, int max){
		SortCriteria sortCriteria = new SortCriteria("id.facilityId", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs =  new SearchConditions(new SearchCondition("usedForFacilityLocatnFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		Collection facilityPermits = searchDAO.getSearchList(FacilityPermit.class, new ArrayList(), scs, sortArray, new ArrayList(), start, max);
		if(facilityPermits!=null)
			for(Iterator iter = facilityPermits.iterator();iter.hasNext();){
				FacilityPermit facilityPermit = (FacilityPermit)iter.next();
				long facilityId = facilityPermit.getFacility().getFacilityId();
				long permitId = facilityPermit.getPermit().getPermitId();
				System.out.println("Processing facility permit" + facilityId);
				createOrUpdateInfo(new Long(facilityId), user, permitId, false);
				searchDAO.flushAndClearCache();
			}
		
	}
	
	public void updateUseDataNPDESPermitInfo(CurrentUser user, Long facilityId){
		SearchConditions scs =  new SearchConditions(new SearchCondition("usedForFacilityLocatnFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		scs.setCondition(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Collection facilityPermits = searchDAO.getSearchList(FacilityPermit.class, scs);
		if(facilityPermits!=null)
			for(Iterator iter = facilityPermits.iterator();iter.hasNext();){
				FacilityPermit facilityPermit = (FacilityPermit)iter.next();
				long permitId = facilityPermit.getPermit().getPermitId();
				System.out.println("Processing facility permit" + facilityId);
				createOrUpdateInfo(facilityId, user, permitId, false);
				searchDAO.flushAndClearCache();
			}	
	}
	
	private void createOrUpdateInfo(Long facilityId, CurrentUser user, long permitId, boolean updateWLV){
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("permitId", SearchCondition.OPERATOR_EQ, new Long(permitId)));
		Permit p = (Permit)searchDAO.getSearchObject(Permit.class, scs2);
		String permitNbr = p.getPermitNumber();
		SearchConditions scs =  new SearchConditions(new SearchCondition("efNpdesPermitNumber", SearchCondition.OPERATOR_EQ, permitNbr));
		EfPermit efPermit = (EfPermit)searchDAO.getSearchObject(EfPermit.class, scs);
		if (efPermit!=null){
		   addFacilityAddress(facilityId,user,efPermit);
		   createOrUpdatePOR(facilityId,user,efPermit, updateWLV);
		   updateFacilityEntryStatus(facilityId, user, new Long(2));
		 if (efPermit.getCountyName()!=null){
			 SearchConditions scs1 =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
			 Facility f = (Facility)searchDAO.getSearchObject(Facility.class, scs1);
			 assCountyIfNotAssociated(facilityId,user,f==null?"":f.getLocationId(),efPermit.getCountyName());
		 }
		 String stateId=getFacilityLocationId(efPermit.getPointOfContactStateId());
		 if (efPermit.getPointOfContactAuthorityNm()!=null && stateId!=null){			 			 
			 PointOfContact poc = pocService.getPointOfContact(stateId, efPermit.getPointOfContactAuthorityNm(), 
					 efPermit.getPointOfContactName(), efPermit.getPointOfContactCity());
			 if (poc == null){
				   pocf = new PointOfContactForm();
				   pocf.setFacilityId(facilityId.toString());
				   pocf.setAuthorityName(efPermit.getPointOfContactAuthorityNm());
				   pocf.setStateId(stateId);
				   pocf.setAddress1(efPermit.getPointOfContactStreet1()==null?"":efPermit.getPointOfContactStreet1());
				   pocf.setAddress2(efPermit.getPointOfContactStreet2()==null?"":efPermit.getPointOfContactStreet2());
			       pocf.setCity(efPermit.getPointOfContactCity()==null?"":efPermit.getPointOfContactCity());   
			       pocf.setContactName(efPermit.getPointOfContactName()==null?"":efPermit.getPointOfContactName());
			       pocf.setCountyName(efPermit.getCountyName()==null?"":efPermit.getCountyName());  
			       pocf.setPhone(efPermit.getPointOfContactPhoneNumber()==null?"":efPermit.getPointOfContactPhoneNumber());
			       pocf.setZip(efPermit.getPointOfContactZip()==null?"":efPermit.getPointOfContactZip());
			       pocf.setSourcedFromNpdes("Y"); 
			       pocService.addNewPoc(pocf, user.getUserId());
			 }
			 else{
				 long[] pocIds = {poc.getPointOfContactId()};
				 FacilityPointOfContact facilityPOC = pocService.getFacilityPoc(facilityId.toString(), (new Long(pocIds[0])).toString());
				 if (facilityPOC == null)
				   pocService.associatePointOfContacts(facilityId.toString(), pocIds, user.getUserId());
			 }
			 updateFacilityEntryStatus(facilityId, user, new Long(3));  
		 }
		} 
	}
	
	private void createOrUpdatePOR(Long facilityId, CurrentUser user, EfPermit efPermit, boolean updateWLV){
		AbsoluteLocationPoint POR = facilityAddressService.getFacilityCoordinates(facilityId);				
		/*
		AbsoluteLocationPoint POR = null;
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		POR = (AbsoluteLocationPoint)searchDAO.getSearchObject(AbsoluteLocationPoint.class, scs1, aliasArray);
		*/
		if (POR != null){
			facilityAddressService.removeFeatures(facilityId);
			searchDAO.removeObject(POR);
		}
		POR =  new AbsoluteLocationPoint();
        // Get Geographic Area object
		GeographicArea geographicArea = getGeographicArea(facilityId,user);
		POR.setGeographicArea(geographicArea);
		POR.setHorizontalCllctnMethodRef(efPermit.getHorizontalCllctnMethodRef());
		POR.setHorizontalCoordntDatumRef(efPermit.getHorizontalCoordntDatumRef());
		if (efPermit.getLatitudeDecimalDegree()!=null){
			BigDecimal latitude = efPermit.getLatitudeDecimalDegree();
		  POR.setLatitudeDecimalDegree(latitude);
		}
		POR.setLatitudeDirection(efPermit.getLatitudeDirection());
		if (efPermit.getLongitudeDecimalDegree()!=null){
			BigDecimal longitude = efPermit.getLongitudeDecimalDegree();
		    POR.setLongitudeDecimalDegree(longitude);
		}
		POR.setLongitudeDirection(efPermit.getLongitudeDirection());
		POR.setScale(efPermit.getScale());
		POR.setSourceCd('N');
		POR.setCoordinateTypeCode(new Character('S'));
		POR.setLocationDescriptionRef(efPermit.getLocationDescriptionRef());
		POR.setLastUpdateTs(new Date());
		POR.setLastUpdateUserid(user.getUserId());
		searchDAO.saveObject(POR);
		if(updateWLV){
			if (efPermit.getLatitudeDecimalDegree()!=null && efPermit.getLongitudeDecimalDegree()!=null){
				String latitude="";
				String longitude="";
				if(efPermit.getLatitudeDirection()!=null && efPermit.getLatitudeDirection().charValue()=='N'){
					latitude = "+"+efPermit.getLatitudeDecimalDegree();
				}
				else
					latitude = "-"+efPermit.getLatitudeDecimalDegree();
				if(efPermit.getLongitudeDirection()!=null && efPermit.getLongitudeDirection().charValue()=='E'){
					longitude = "+"+efPermit.getLongitudeDecimalDegree();
				}
				else
					longitude = "-"+efPermit.getLongitudeDecimalDegree();
				facilityAddressService.saveWebRITSession(latitude, longitude, facilityId);
			}			
		}
	}
	
	private void addFacilityAddress(Long facilityId, CurrentUser user, EfPermit efPermit){
		FacilityAddress facilityAddress = facilityAddressService.getFacilityAddress(facilityId);
		if (facilityAddress == null)
			facilityAddress = new FacilityAddress();
		String state = "";
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		Facility f = (Facility)searchDAO.getSearchObject(Facility.class, scs2);
		facilityAddress.setFacility(f);
		facilityAddress.setFacilityId(f.getFacilityId());
		facilityAddress.setSourcedFromNpdesFlag('Y');
		facilityAddress.setLastUpdateTs(new Date());
		facilityAddress.setLastUpdateUserid(user.getUserId());
		facilityAddress.setCity(efPermit.getFacilityLocationCity());
		if (efPermit.getFacilityLocationStateId()!=null){
			state = getFacilityLocationId(efPermit.getFacilityLocationStateId());
		}
		facilityAddress.setStateId(state.trim());
		facilityAddress.setStreetAddress1(efPermit.getFacilityLocationStreet1());
		facilityAddress.setStreetAddress2(efPermit.getFacilityLocationStreet2());
		facilityAddress.setZip(efPermit.getFacilityLocationZip());
		searchDAO.saveObject(facilityAddress);
	}
	
	private String getFacilityLocationId(String facilityLocationStateId) {
		SearchConditions scs1 = new SearchConditions(new SearchCondition("stateId", SearchCondition.OPERATOR_EQ, facilityLocationStateId));
		StateRef state = (StateRef)searchDAO.getSearchObject(StateRef.class, scs1);
		if (state!=null)
		  return state.getLocationId();
		else
		return null;
	}
    
	private String getFacilityStateId(String locationId) {
		SearchConditions scs1 = new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		StateRef state = (StateRef)searchDAO.getSearchObject(StateRef.class, scs1);
		if (state!=null)
		  return state.getStateId();
		else
		return "";
	}
	
	private void assCountyIfNotAssociated(Long facilityId, CurrentUser user, String locationId, String countyName){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("countyRef", "c", AliasCriteria.JOIN_INNER));
		SearchConditions scs1 =  new SearchConditions(new SearchCondition("ga.facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("ga.geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		scs1.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("c.name",SearchCondition.OPERATOR_EQ,countyName));
		GeographicAreaCounty GAC = (GeographicAreaCounty)searchDAO.getSearchObject(GeographicAreaCounty.class, scs1, aliasArray);
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("name", SearchCondition.OPERATOR_EQ, countyName));
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationId));
		CountyRef county = (CountyRef)searchDAO.getSearchObject(CountyRef.class, scs2);
		if (GAC == null && county!=null){
			GAC = new GeographicAreaCounty();
			GAC.setCountyRef(county);
			GAC.setFeedbackDeleteFlag('N');
			GeographicArea geographicArea = getGeographicArea(facilityId,user);
			GAC.setGeographicArea(geographicArea);
			GeographicAreaCountyId id = new GeographicAreaCountyId(geographicArea.getGeographicAreaId(),county.getCountyId());
			GAC.setId(id);
			GAC.setPrimaryFlag('N');
			GAC.setLastUpdateTs(new Date());
			GAC.setLastUpdateUserid(user.getUserId());
			searchDAO.saveObject(GAC);
		}
	}
	
	private GeographicArea getGeographicArea(Long facilityId, CurrentUser user) {
        // Get Geographic Area object
		SearchConditions scs2 =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("geographicAreaTypeRef.geographicAreaTypeId",SearchCondition.OPERATOR_EQ,new Long(7)));
		GeographicArea geographicArea = (GeographicArea)searchDAO.getSearchObject(GeographicArea.class, scs2);
		if (geographicArea == null){
			geographicArea = new GeographicArea();
			SearchConditions scs3 =  new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, facilityId));
			Facility f = (Facility)searchDAO.getSearchObject(Facility.class, scs3);
			SearchConditions scs4 =  new SearchConditions(new SearchCondition("geographicAreaTypeId", SearchCondition.OPERATOR_EQ, new Long(7)));
			GeographicAreaTypeRef geographicAreaTypeRef = (GeographicAreaTypeRef)searchDAO.getSearchObject(GeographicAreaTypeRef.class, scs4);
			geographicArea.setFacility(f);
			geographicArea.setGeographicAreaTypeRef(geographicAreaTypeRef);
			geographicArea.setLastUpdateUserid(user.getUserId());
		    geographicArea.setLastUpdateTs(new Date());
		    geographicArea.setTribeFlag(new Character('N'));
		    searchDAO.saveObject(geographicArea);
		}    
		return geographicArea;
	}
	/*
    private void saveWebRITSession(String latitude, String longitude, Long facilityId){
	   try { 
		   
			String registrationKey = CWNSProperties.getProperty("webrit.registration.key");	
			Waters_Indexing_ServiceSoap waterIndexingService=getWaterIndexingService(registrationKey);
			String sessionId = facilityAddressService.getWebritSession();
			String xmlRes = waterIndexingService.savePointToSession(sessionId, latitude+",-"+longitude, registrationKey);
			log.debug("Reponse of save point to session:"+ xmlRes);
			String cwnsNbr = facilityService.findByFacilityId(facilityId.toString()).getCwnsNbr();
			log.debug("sessionId:"+ sessionId);
			log.debug("CWNSNBR:"+ cwnsNbr);
			log.debug("registration:" + registrationKey );			
	    	String status =waterIndexingService.savePendingIndexFeatures(sessionId,cwnsNbr,registrationKey);
	    	log.debug("status:"+ status);
	    	//save new features
	    	boolean success = facilityAddressService.saveFeatureIds(status, facilityId);	    	
			log.debug("End water Indexing Services saving session call..." + status);
		}catch (Exception e) {
			log.error("Error Calling water index service saving session", e);
			throw new ApplicationException("Error Calling water index service saving  session", e);
		}	
	   
    }*/
	    	
}
