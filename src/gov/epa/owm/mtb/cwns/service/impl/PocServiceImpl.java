package gov.epa.owm.mtb.cwns.service.impl;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.CountyRef;
import gov.epa.owm.mtb.cwns.model.FacilityPointOfContact;
import gov.epa.owm.mtb.cwns.model.FacilityPointOfContactId;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.model.PointOfContact;
import gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactForm;
import gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactHelper;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;
import gov.epa.owm.mtb.cwns.service.PocService;
import gov.epa.owm.mtb.cwns.service.PopulationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class PocServiceImpl extends CWNSService implements PocService {	


	/**
	 * Load the POC form for display.
	 * @param facilityId
	 * @param pocf
	 */
	public void loadPocForm(String facilityId, PointOfContactForm pocf) {
		
		FacilityPointOfContact fpoc = getFacilityPoc(facilityId,pocf.getEditPocId());		
		PointOfContact poc = fpoc.getPointOfContact();
		
		pocf.setAuthorityName(poc.getAuthorityName());
		pocf.setContactName(poc.getName());
		pocf.setTitle(poc.getRoleTitle());
		pocf.setPhone(poc.getPhone());
		pocf.setFax(poc.getFax());
		pocf.setAddress1(poc.getStreetAddress1());
		pocf.setAddress2(poc.getStreetAddress2());
		pocf.setCity(poc.getCity());
		pocf.setStateId(poc.getStateId());
		pocf.setCountyName(poc.getCounty());
		pocf.setZip(poc.getZip());
		pocf.setEmail(poc.getEmail());
		pocf.setResponsibleEntity(new Character (fpoc.getPrimaryFlag()).toString());
		pocf.setTribe(new Character (poc.getTribeFlag()).toString());
		pocf.setSourcedFromNpdes(new Character(poc.getSourcedFromNpdesFlag()).toString());
		pocf.setSuperfundPossible(isResponsiblePartyEnabled(facilityId));
		pocf.setSuperfundResponsibleParty(new Character(fpoc.getSuperfundRespPartyFlag()).toString());
	
	
	}	
	
	public boolean isResponsiblePartyEnabled(String facilityId){
        // Determine if "Superfund Responsible Party" should be enabled
		
		SearchConditions scs =  new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		Collection facilityTypes = searchDAO.getSearchList(FacilityType.class, scs);
		
		Iterator iter = facilityTypes.iterator();
		while (iter.hasNext()) {
			FacilityType facilityType = (FacilityType)iter.next();
			long facTypeRefId = facilityType.getFacilityTypeRef().getFacilityTypeId(); 
			if (FacilityTypeService.FACILITY_TYPE_URBAN.longValue() == facTypeRefId ||
				FacilityTypeService.FACILITY_TYPE_STORAGE_TANKS.longValue() == facTypeRefId ||
				FacilityTypeService.FACILITY_TYPE_SANITARY_LANDFILL.longValue() == facTypeRefId) {
								
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Return true if the county name exists within the state.
	 */
	public boolean doesCountyExist(String stateId,	String countyName) {
		boolean exists = false;
		
		SearchConditions scs  =  new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, stateId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("name",SearchCondition.OPERATOR_EQ, countyName));
		CountyRef countyRef = (CountyRef) searchDAO.getSearchObject(CountyRef.class, scs);		
		if (countyRef != null) {
			exists = true;
		}
		return exists;
	}
	/**
	 * Associate one or more Points of Contact to a facility
	 */
	public void associatePointOfContacts(String facilityId, long[] pocIds, String userId) {
		if (pocIds == null) {
			return;    // nothing to do
		}
		
		for (int i = 0; i < pocIds.length; i++) {
			associatePocToFacility(facilityId, new Long(pocIds[i]).toString(), userId);
		}
	}

	/**
	 * Assoicate a Point of Contact to a facility.
	 * @param facilityId
	 * @param pocId
	 * @param userId
	 */
	private void associatePocToFacility(String facilityId, String pocId,String userId) {

		Facility facility = facilityService.findByFacilityId(facilityId);
		PointOfContact poc = getPocById(pocId); 
		
		// Initialize the FacilityPointOfContactId object
		FacilityPointOfContactId fpocId = new FacilityPointOfContactId();
		fpocId.setFacilityId(new Long(facilityId).longValue());
		fpocId.setPointOfContactId(poc.getPointOfContactId());
		
		// Initialize the FacilityPointOfContact object 
		FacilityPointOfContact fpoc = new FacilityPointOfContact();
		updateFacilityPoc(facilityId, "N","N",fpoc,userId);

		// Initialize a few more fields in the FacilityPointOfContact object.
		fpoc.setFacility(facility);
		fpoc.setPointOfContact(poc);
		fpoc.setFeedbackDeleteFlag('N');
		fpoc.setId(fpocId);
		
		// Save the Facility POC object
		searchDAO.saveObject(fpoc);
	}
	
	
	/**
	 * 
	 */
	public List getPocSearchList(long facilityId, 
			   							  String locationId,
			   							  String keyword,
			   							  int startIndex, 
			   							  int maxResults) {

		SearchConditions scs = createPocSearchConditions(facilityId, locationId, keyword);

		// Sort order
		SortCriteria sortCriteria = new SortCriteria("authorityName", SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		
		return searchDAO.getSearchList(PointOfContact.class, scs, sortArray, new ArrayList(),startIndex, maxResults);
	}

	/**
	 * 
	 * @param facilityId
	 * @param locationId
	 * @param keyword
	 * @return
	 */
	public SearchConditions createPocSearchConditions(long facilityId, String locationId, String keyword) {
		
		// Get existing POCs for the facility
		String     facId  = new Long(facilityId).toString();
		List existingPocs = getPocListForFacility(facId);

		// create Collection of POC Ids to exclude
		Collection existingPocIds = new ArrayList();
		Iterator iter = existingPocs.iterator();
		while (iter.hasNext()) {
			PointOfContactHelper pocHelper = (PointOfContactHelper)iter.next();
			//PointOfContact poc = (PointOfContact) iter.next();
			existingPocIds.add(new Long( pocHelper.getPointOfContactId()));
		}
		
		// State Criteria
		SearchConditions scs  =  new SearchConditions(new SearchCondition("stateId", SearchCondition.OPERATOR_EQ, locationId));
		
		// Keyword Criteria
		if (keyword != null && keyword.trim().length() > 0) {
			SearchConditions scs2 = new SearchConditions(new SearchCondition("authorityName", SearchCondition.OPERATOR_LIKE, keyword));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("name", SearchCondition.OPERATOR_LIKE, keyword));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("county", SearchCondition.OPERATOR_LIKE, keyword));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("streetAddress1", SearchCondition.OPERATOR_LIKE, keyword));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("streetAddress2", SearchCondition.OPERATOR_LIKE, keyword));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("city", SearchCondition.OPERATOR_LIKE, keyword));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("zip", SearchCondition.OPERATOR_LIKE, keyword));
			scs.setCondition(SearchCondition.OPERATOR_AND, scs2);
		}
		
		// Exclude existing POCs Criteria
		if (!existingPocIds.isEmpty()) {
			SearchConditions scs3 = new SearchConditions(new SearchCondition("pointOfContactId", SearchCondition.OPERATOR_NOT_IN, existingPocIds));
			scs.setCondition(SearchCondition.OPERATOR_AND, scs3);
		}

		return scs;
	}
	
	public int getPocSearchListCount(long facilityId, 
				  String locationId,
				  String keyword) {

		SearchConditions scs = createPocSearchConditions(facilityId, locationId, keyword);
		return searchDAO.getCount(PointOfContact.class, scs);
	}
	
	public void deletePoc(PointOfContactForm pocf, CurrentUser currentUser) {

		String pocId      = new Long(pocf.getEditPocId()).toString();
		String facilityId = pocf.getFacilityId();

		FacilityPointOfContact fpoc = getFacilityPoc(facilityId,pocId);
		PointOfContact   poc = fpoc.getPointOfContact();
		
        // if local user mark as deleted
		if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(currentUser.getCurrentRole().getLocationTypeId())){
			if (fpoc.getFeedbackDeleteFlag()=='Y') {
				fpoc.setFeedbackDeleteFlag('N');
   		    }else{
   		    	fpoc.setFeedbackDeleteFlag('Y');
   		     }   	    		
   		  searchDAO.saveObject(fpoc);
		}
		else{
		   // How many FacilityPointOfContact objects are associated with the POC 
		   int pocCount = poc.getFacilityPointOfContacts().size();
		  // Delete the FacilityPointOfContact record
		   searchDAO.removeObject(fpoc);

		   // Delete the PointOfContact object if no other FacilityPointOfContact
		   // object is assocatied to it.
		   if (pocCount == 1) {
			 searchDAO.removeObject(poc);
		   }
		}	   
	}
	
	/**
	 * Update the POC information in the database. Both the PointOfContact and the 
	 * FacilityPointOfContact objects are updated.
	 * @param pocf
	 */
	public void updatePoc(PointOfContactForm pocf, String userId) {

		String pocId      = new Long(pocf.getEditPocId()).toString();
		String facilityId = pocf.getFacilityId();
		
		// Update the existing FacilityPointOfContact object 
		FacilityPointOfContact fpoc =  getFacilityPoc(facilityId,pocId);
		updateFacilityPoc(facilityId,
						  pocf.getResponsibleEntity(),
						  pocf.getSuperfundResponsibleParty(),
						  fpoc,
						  userId);		


		// Update the existing PointOfContact object
		PointOfContact poc = fpoc.getPointOfContact();
		updatePoc(pocf,poc,userId);
		
		// Save the POC and Facility POC
		searchDAO.saveObject(poc);
		searchDAO.saveObject(fpoc);
	}
	
	
	/**
	 * 
	 * @param stateId
	 * @param authorityName
	 * @return
	 */
	public boolean isAuthorityNameUniqueWithinState(String stateId, String authorityName, String city, String name) {
		boolean unique = true;
		
		SearchConditions scs  =  new SearchConditions(new SearchCondition("stateId", SearchCondition.OPERATOR_EQ, stateId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("authorityName",SearchCondition.OPERATOR_EQ,authorityName.trim()));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("city",SearchCondition.OPERATOR_EQ,city));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("name",SearchCondition.OPERATOR_EQ,name));

		Collection authorities = searchDAO.getSearchList(PointOfContact.class, scs);
		if (authorities.size() > 0 ) {
			unique = false;
		}
		return unique;
	}
	public void addNewPoc(PointOfContactForm pocf, String userId) {

		// Get the Facility object
		String facilityId = pocf.getFacilityId();
		
		// Initialize and save the PointOfContact object
		PointOfContact poc = new PointOfContact();
		updatePoc(pocf,poc,userId);
		searchDAO.saveObject(poc);
		
		// Initialize the FacilityPointOfContactId object
		FacilityPointOfContactId fpocId = new FacilityPointOfContactId();
		fpocId.setFacilityId(new Long(facilityId).longValue());
		fpocId.setPointOfContactId(poc.getPointOfContactId());
		
		// Initialize the FacilityPointOfContact object 
		FacilityPointOfContact fpoc = new FacilityPointOfContact();
		updateFacilityPoc(facilityId,
						  pocf.getResponsibleEntity(),
						  pocf.getSuperfundResponsibleParty(),
						  fpoc,
						  userId);

		// Initialize a few more fields in the FacilityPointOfContact object.
		Facility facility = facilityService.findByFacilityId(facilityId);
		fpoc.setFacility(facility);
		fpoc.setPointOfContact(poc);
		fpoc.setFeedbackDeleteFlag('N');
		fpoc.setId(fpocId);
		
		// Save the Facility POC object
		searchDAO.saveObject(fpoc);
	}

	/**
	 * 
	 * @param facilityId
	 * @param responsibleEntity
	 * @param superfundResponsibleParty
	 * @param fpoc
	 * @param userId
	 */
	private void updateFacilityPoc(String facilityId,
								   String responsibleEntity,
								   String superfundResponsibleParty,
								   FacilityPointOfContact fpoc, 
								   String userId) {


		// Responsible Entity
		boolean primaryFlag = (responsibleEntity != null && 
				  			  "Y".equals(responsibleEntity)) ? true : false;
		if (primaryFlag) {
			// Ensure only one primary Point of Contact
			long priPocId = getPirmaryPocIdForFacility(facilityId);
			if (priPocId > 0 ) {
				FacilityPointOfContact fPointOfContact = getFacilityPoc(facilityId,	new Long(priPocId).toString());
				fPointOfContact.setPrimaryFlag('N');
				searchDAO.saveObject(fPointOfContact);
			}
			fpoc.setPrimaryFlag('Y');			

		} else {
			fpoc.setPrimaryFlag('N');
		}
		
		if ("Y".equals(superfundResponsibleParty)) {
			// Ensure only one Superfund Responsible Party
			FacilityPointOfContact fPointOfContact = getSuperfundRespParty(facilityId);
			if (fPointOfContact != null) {
				fPointOfContact.setSuperfundRespPartyFlag('N');
				searchDAO.saveObject(fPointOfContact);
			}
			fpoc.setSuperfundRespPartyFlag('Y');
			
		} else {
			fpoc.setSuperfundRespPartyFlag('N');
		}
		
		fpoc.setLastUpdateTs(new Date());
		fpoc.setLastUpdateUserid(userId);
		
	}

	/**
	 * Update the PointOfContact object using the PointOfContactForm bean.
	 * @param pocf
	 * @param poc
	 */
	private void updatePoc(PointOfContactForm pocf, PointOfContact poc, String userId) {
		
		poc.setAuthorityName(pocf.getAuthorityName().trim());
		poc.setName(pocf.getContactName().trim());
		poc.setRoleTitle(pocf.getTitle());
		poc.setPhone(pocf.getPhone().trim());
		poc.setFax(pocf.getFax().trim());
		poc.setStreetAddress1(pocf.getAddress1().trim());
		poc.setStreetAddress2(pocf.getAddress2().trim());
		poc.setCity(pocf.getCity().trim());
		poc.setStateId(pocf.getStateId());
		poc.setCounty(pocf.getCountyName().trim()); 
		poc.setZip(pocf.getZip().trim());
		poc.setEmail(pocf.getEmail().trim());
		char tribeFlag = (pocf.getTribe() != null && 
						  "Y".equals(pocf.getTribe())) ? 'Y' : 'N';
		poc.setTribeFlag(tribeFlag);
		if("Y".equalsIgnoreCase(pocf.getSourcedFromNpdes()))
			poc.setSourcedFromNpdesFlag('Y');
		else
			poc.setSourcedFromNpdesFlag('N');
		poc.setLastUpdateTs(new Date());
		poc.setLastUpdateUserid(userId);
	
	}
	
	/**
	 * Given a facility Id return the associated Collection of PointOfContact objects.
	 * @param facilityId
	 * @return
	 */
	public List getPocListForFacility(String facilityId) {
		/*
		ArrayList aliasArray = new ArrayList();
		AliasCriteria alias = new AliasCriteria("facilityPointOfContacts", "fpocs", AliasCriteria.JOIN_INNER);		
		aliasArray.add(alias);
		
		SearchConditions scs  =  new SearchConditions(new SearchCondition("fpocs.id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		
		SortCriteria sortCriteria = new SortCriteria("authorityName", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);		
		*/
		ArrayList pointOfContactList = new ArrayList();
		ArrayList aliasArray = new ArrayList();
		AliasCriteria alias = new AliasCriteria("pointOfContact", "poc", AliasCriteria.JOIN_INNER);		
		aliasArray.add(alias);
		
		SearchConditions scs  =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		
		SortCriteria sortCriteria = new SortCriteria("poc.authorityName", SortCriteria.ORDER_ASCENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);	
		Collection pointOfContacts = searchDAO.getSearchList(FacilityPointOfContact.class, new ArrayList(), scs, sortArray, aliasArray);
		Iterator iter = pointOfContacts.iterator();
		while(iter.hasNext()){
			FacilityPointOfContact fpoc = (FacilityPointOfContact)iter.next();
			PointOfContact poc = fpoc.getPointOfContact();
			PointOfContactHelper pocHelper = new PointOfContactHelper();
			pocHelper.setAuthorityName(poc.getAuthorityName());
			pocHelper.setEmail(poc.getEmail());
			pocHelper.setFeedbackDeleteFlag(Character.toString(fpoc.getFeedbackDeleteFlag()));
			pocHelper.setName(poc.getName());
			pocHelper.setPhone(poc.getPhone());
			pocHelper.setPointOfContactId(poc.getPointOfContactId());
			pocHelper.setRoleTitle(poc.getRoleTitle());
			pocHelper.setSourcedFromNpdesFlag(poc.getSourcedFromNpdesFlag());
		  pointOfContactList.add(pocHelper);	
			
		}
		return pointOfContactList;
		
	}
	
	/**
	 * Given a facilityId return the FacilityPointOfContact for the Superfund Responsible Party.  
	 * If no Superfund Responsible Party exists return null.
	 * @param facilityId
	 * @return
	 */
	public FacilityPointOfContact getSuperfundRespParty(String facilityId) {
		
		SearchConditions scs  =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("superfundRespPartyFlag",SearchCondition.OPERATOR_EQ, "Y"));
		return (FacilityPointOfContact) searchDAO.getSearchObject(FacilityPointOfContact.class, scs);
	}
	
	/**
	 * Given a facilityId return the for the facility. If 
	 * no primary Point of Contact exists return zero.
	 * @param facilityId
	 * @return
	 */
	public long getPirmaryPocIdForFacility(String facilityId) {
		
		long pimaryPocId = 0;
		SearchConditions scs  =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("primaryFlag",SearchCondition.OPERATOR_EQ, "Y"));
		Collection fpocList   = searchDAO.getSearchList(FacilityPointOfContact.class, scs);
		if (fpocList.size() == 1) {
			Iterator iter = fpocList.iterator();
			FacilityPointOfContact fpoc = (FacilityPointOfContact)iter.next();
			pimaryPocId = fpoc.getId().getPointOfContactId();
		} else if (fpocList.size() > 1) {
			throw new ApplicationException("Multiple Primary Point of Contact records found for FacilityId = "+ facilityId);
		}
		return pimaryPocId;
	}
	
	/**
	 * Return the PointOfContact object associated with the Point of Contact Id passed in.
	 */
	public PointOfContact getPocById(String pointOfContactId) {
		
		SearchConditions scs  =  new SearchConditions(new SearchCondition("pointOfContactId", SearchCondition.OPERATOR_EQ, new Long(pointOfContactId)));
		PointOfContact poc = (PointOfContact)searchDAO.getSearchObject(PointOfContact.class, scs);
		return poc;
	}
	
	/**
	 * 
	 * @param stateId
	 * @param authorityName
	 * @param name
	 * @param city
	 * @return
	 */
	public PointOfContact getPointOfContact(String stateId, String authorityName, String name, String city) {
		PointOfContact poc = null;
		SearchConditions scs  =  new SearchConditions(new SearchCondition("authorityName",SearchCondition.OPERATOR_EQ,authorityName.trim()));
		scs.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("stateId", SearchCondition.OPERATOR_EQ, stateId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("name",SearchCondition.OPERATOR_EQ,name==null?"":name.trim()));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("city",SearchCondition.OPERATOR_EQ,city==null?"":city.trim()));
		poc = (PointOfContact)searchDAO.getSearchObject(PointOfContact.class, scs);
		return poc;
	}


	/**
	 * Given a Facility Id and a Point of Contact Id return a 
	 * FacilityPointOfContact object.
	 * @param facilityId
	 * @param pocId
	 * @return
	 */
	public FacilityPointOfContact getFacilityPoc(String facilityId,String pocId) {
		
		SearchConditions scs  =  new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.pointOfContactId",SearchCondition.OPERATOR_EQ, new Long(pocId)));
		FacilityPointOfContact fpoc = (FacilityPointOfContact)searchDAO.getSearchObject(FacilityPointOfContact.class, scs);
		return fpoc;
	}
	
	private static SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}


    /* set the facility service */
    private FacilityService facilityService;
    public void setFacilityService(FacilityService fs){
       facilityService = fs;    	
    }
 
}
