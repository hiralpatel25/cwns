package gov.epa.owm.mtb.cwns.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.epa.iamfw.webservices.user.User;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocation;
import gov.epa.owm.mtb.cwns.model.FacilityPointOfContact;
import gov.epa.owm.mtb.cwns.model.PointOfContact;
import gov.epa.owm.mtb.cwns.pointofcontact.PointOfContactForm;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsForm;
import gov.epa.owm.mtb.cwns.userregistration.IAMResponse;
import gov.epa.owm.mtb.cwns.userregistration.RegistrationForm;

/**
 * This class provides business functionality related to the Point of Contact. 
 * @author Matt Connors
 *
 */
public interface PocService {	

	public final int MAX_RESULTS = 5;
	
	public PointOfContact getPocById(String pocId);
	
	public List getPocListForFacility(String facilityId);

	public long getPirmaryPocIdForFacility(String facilityId);

	public FacilityPointOfContact getFacilityPoc(String facilityId,String pocId);
	
	public void updatePoc(PointOfContactForm pocf, String userId);
	
	public void addNewPoc(PointOfContactForm pocf, String userId);	
	
	public void deletePoc(PointOfContactForm pocf, CurrentUser currentUser);
	
	public boolean isAuthorityNameUniqueWithinState(String stateId, String authorityName, String city, String name);
	
	public List getPocSearchList(
				long facilityId, 
				String locationId,
				String keyword,
				int startIndex, 
				int maxResults);
	
	public int getPocSearchListCount(long facilityId, 
			  String locationId,
			  String keyword);

	public void associatePointOfContacts(String facilityId, long[] pocIds, String userId);
	
	public boolean doesCountyExist(String stateId,	String county);
	
	public PointOfContact getPointOfContact(String stateId, String authorityName, String name, String city);
	
	public void loadPocForm(String facilityId, PointOfContactForm pocf);

	public boolean isResponsiblePartyEnabled(String facilityId);
}
