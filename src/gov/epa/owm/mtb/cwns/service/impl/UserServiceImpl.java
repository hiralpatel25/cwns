package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.iamfw.client.IAMLocator;
import gov.epa.iamfw.webservices.auth.AuthMgr;
import gov.epa.iamfw.webservices.common.AuthMethod;
import gov.epa.iamfw.webservices.common.Error;
import gov.epa.iamfw.webservices.delegatedadministration.DelegatedAdministrationService;
import gov.epa.iamfw.webservices.group.GroupRole;
import gov.epa.iamfw.webservices.selfservice.SelfService;

import gov.epa.iamfw.webservices.user.AccountInfo;
import gov.epa.iamfw.webservices.user.ContactInfo;
import gov.epa.iamfw.webservices.user.User;
import gov.epa.iamfw.webservices.user.UserMgr;
import gov.epa.iamfw.webservices.userservice.searchuser.localschema.Request;
import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.UserRole;
import gov.epa.owm.mtb.cwns.common.exceptions.ApplicationException;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.Search;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.AccessLevelRefDAO;
import gov.epa.owm.mtb.cwns.dao.CwnsUserDAO;
import gov.epa.owm.mtb.cwns.dao.CwnsUserLocationDAO;
import gov.epa.owm.mtb.cwns.dao.CwnsUserLocatnAccessLevelDAO;
import gov.epa.owm.mtb.cwns.dao.CwnsUserStatusRefDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.AccessLevelAppliesToRef;
import gov.epa.owm.mtb.cwns.model.AccessLevelAssignedByRef;
import gov.epa.owm.mtb.cwns.model.AccessLevelRef;
import gov.epa.owm.mtb.cwns.model.CwnsUser;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocation;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocationFacility;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocationFacilityId;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocationId;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocatnAccessLevel;
import gov.epa.owm.mtb.cwns.model.CwnsUserLocatnAccessLevelId;
import gov.epa.owm.mtb.cwns.model.CwnsUserStatusRef;
import gov.epa.owm.mtb.cwns.model.LocationRef;
import gov.epa.owm.mtb.cwns.model.LocationTypeRef;
import gov.epa.owm.mtb.cwns.model.RoleRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.pollution.PollutionProblemHelper;
import gov.epa.owm.mtb.cwns.service.UserService;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsAction;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsForm;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsHelper;
import gov.epa.owm.mtb.cwns.userdetails.UserDetailsRoleHelper;
import gov.epa.owm.mtb.cwns.userlist.UserListAction;
import gov.epa.owm.mtb.cwns.userlist.UserListHelper;
import gov.epa.owm.mtb.cwns.userregistration.IAMResponse;
import gov.epa.owm.mtb.cwns.userregistration.RegistrationForm;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

public class UserServiceImpl extends CWNSService implements UserService {

	public Search getUserSearch(CurrentUser adminUser, Map queryProperties,
			SortCriteria sortCriteria, int startIndex, int maxResults) {
		Search s = new Search();

		s.setName(UserListAction.QUERY_TYPE_SEARCH);
		s.setDescription(UserListAction.QUERY_TYPE_SEARCH_DESC);

		if (sortCriteria != null) {
			ArrayList sortArray = new ArrayList();
			sortArray.add(sortCriteria);
			s.setSortCriteria(sortArray);
		}

		s.setStartIndex(startIndex);
		s.setMaxResults(maxResults);

		SearchConditions scs = new SearchConditions();

		UserRole userRole = adminUser.getCurrentRole();
		String adminLocationTypeId = userRole.getLocationTypeId();
		String adminLocationId = userRole.getLocationId();

		// Get query parameters
		String locationId = (String) queryProperties.get("locationId");
		String userKeyword = (String) queryProperties.get("userKeyword");
		String locationTypeId = (String) queryProperties.get("locationTypeId");
		String userStatus = (String) queryProperties.get("userStatus");
		String[] accessLevels = (String[]) queryProperties.get("accessLevels");

		// Set up the aliases

		ArrayList aliasArray = new ArrayList();
		AliasCriteria alias = new AliasCriteria("cwnsUserLocations", "cul",
				AliasCriteria.JOIN_INNER);
		aliasArray.add(alias);

		alias = new AliasCriteria("cul.roleRef", "rr", AliasCriteria.JOIN_INNER);
		aliasArray.add(alias);

		alias = new AliasCriteria("rr.locationTypeRef", "lref",
				AliasCriteria.JOIN_INNER);
		aliasArray.add(alias);

		/* Filter on Location Type */

		if (locationTypeId.length() > 0) {
			// User specified the location Type
			scs.setCondition(new SearchCondition("lref.locationTypeId",
					SearchCondition.OPERATOR_EQ, locationTypeId));

		} else {
			// Determine Location Type based on the Admin User's Location Type
			SearchConditions scs2 = new SearchConditions(new SearchCondition(
					"lref.locationTypeId", SearchCondition.OPERATOR_EQ,
					LOCATION_TYPE_ID_LOCAL));

			if (adminLocationTypeId.equals(LOCATION_TYPE_ID_STATE)) {
				scs2.setCondition(SearchCondition.OPERATOR_OR,
						new SearchCondition("lref.locationTypeId",
								SearchCondition.OPERATOR_EQ,
								LOCATION_TYPE_ID_STATE));

			} else if (adminLocationTypeId.equals(LOCATION_TYPE_ID_FEDERAL)) {
				scs2.setCondition(SearchCondition.OPERATOR_OR,
						new SearchCondition("lref.locationTypeId",
								SearchCondition.OPERATOR_EQ,
								LOCATION_TYPE_ID_STATE));
				scs2.setCondition(SearchCondition.OPERATOR_OR,
						new SearchCondition("lref.locationTypeId",
								SearchCondition.OPERATOR_EQ,
								LOCATION_TYPE_ID_REGIONAL));
				scs2.setCondition(SearchCondition.OPERATOR_OR,
						new SearchCondition("lref.locationTypeId",
								SearchCondition.OPERATOR_EQ,
								LOCATION_TYPE_ID_FEDERAL));
			}
			scs.setCondition(SearchCondition.OPERATOR_AND, scs2);
		}

		/* Filter on Location */

		if (locationId.length() > 0 && !locationId.equalsIgnoreCase("all")) {
			// User specified the location
			scs.setCondition(new SearchCondition("cul.id.locationId",
					SearchCondition.OPERATOR_EQ, locationId));
		} else {
			if (!adminLocationTypeId.equals(LOCATION_TYPE_ID_FEDERAL)) {
				// State or Local user
				scs.setCondition(new SearchCondition("cul.id.locationId",
						SearchCondition.OPERATOR_EQ, adminLocationId));
			}
		}

		/* Filter on keyword (name) */
		if (userKeyword != null && userKeyword.length() > 0) {
			SearchConditions scs3 = new SearchConditions(new SearchCondition(
					"firstName", SearchCondition.OPERATOR_LIKE, userKeyword));
			scs3.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition(
					"lastName", SearchCondition.OPERATOR_LIKE, userKeyword));
			scs3.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition(
					"cwnsUserId", SearchCondition.OPERATOR_LIKE, userKeyword));
			scs3.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition(
					"oidUserid", SearchCondition.OPERATOR_LIKE, userKeyword));
			scs.setCondition(SearchCondition.OPERATOR_AND, scs3);
		}

		/* Filter on Status */
		if (userStatus.length() > 0 && !userStatus.equalsIgnoreCase("all")) {

			alias = new AliasCriteria("cwnsUserStatusRef", "cusr",
					AliasCriteria.JOIN_INNER);
			aliasArray.add(alias);
			scs.setCondition(new SearchCondition("cusr.cwnsUserStatusId",
					SearchCondition.OPERATOR_EQ, userStatus));
		}

		/* Filter on Access Levels */
		if (accessLevels != null && accessLevels.length > 0
				&& accessLevels[0].length() > 0) {

			boolean foundAtLeastOne = false;
			SearchConditions scs4 = new SearchConditions();

			for (int i = 0; i < accessLevels.length; i++) {
				String accessLevelId = accessLevels[i];
				if (!accessLevelId.equalsIgnoreCase("all")) {
					scs4.setCondition(SearchCondition.OPERATOR_OR,
							new SearchCondition("alf.accessLevelId",
									SearchCondition.OPERATOR_EQ, new Long(
											accessLevelId)));
					foundAtLeastOne = true;
				}
			}

			if (foundAtLeastOne) {

				alias = new AliasCriteria("cul.cwnsUserLocatnAccessLevels",
						"culal", AliasCriteria.JOIN_INNER);
				aliasArray.add(alias);

				alias = new AliasCriteria("culal.accessLevelRef", "alf",
						AliasCriteria.JOIN_INNER);
				aliasArray.add(alias);

				scs.setCondition(SearchCondition.OPERATOR_AND, scs4);
			}

		}

		s.setAlias(aliasArray);
		s.setSearchConditions(scs);
		return s;
	}

	/**
	 * Switch the user's Role in the CurrentUser object and update the database
	 * to indicate this is the user's Primary Role.
	 * 
	 * @param currentUser
	 * @param locationTypeId
	 * @param locationId
	 */
	public void switchRole(CurrentUser currentUser, String locationTypeId,
			String locationId) {
		
		IAMResponse iamResponse = null;
		
		// Save the original role.
		UserRole origRole = currentUser.getCurrentRole();
		
		// Update the current Role
		Collection roles = currentUser.getRoles();
		Iterator roleIter = roles.iterator();

		while (roleIter.hasNext()) {
			UserRole role = (UserRole) roleIter.next();
			if (role.getLocationTypeId().equals(locationTypeId)
					&& role.getLocationId().equals(locationId)) {
				currentUser.setCurrentRole(role);
			}
		}

		// Make this the user's Primary Role.
		setPrimaryRole(currentUser.getUserId(), locationTypeId, locationId);
		
		UserRole newRole = currentUser.getCurrentRole();
		if (!origRole.getLocationTypeId().equals(newRole.getLocationTypeId())) {
			// The Location Type Id changed so change the Portal User Group.
			// TODO: Change the Portal Group this user is 
			//       associated with. This will entail removing them from the 
			//       existing group and adding them to the new group.
			//
			//       The method regAddUserToGroup(cwnsUserId) should be modified to take
			//       and additional argument, portalGroup, that will identify the 
			// 		 Portal group the user should be added to.
			// 
			//       A second method should be created called regRemoveUserFromGroup(). It 
			//       should take 2 arguments - cwnsUserId and portalGroup.
			
			//			 Get the IAM locator object
			
			try{
				setCWNSPrimaryOidGroup(currentUser.getUserId(),newRole.getLocationTypeId());
			}catch(Exception e){
				log.error("ERROR: Attempt to swith OID Group failed.\nException:" + e);
			}
		}
	}

	/**
	 * Return a Search object base on the user entered search criteria.
	 */
	public Search getDefaultSearch(CurrentUser adminUser,
			SortCriteria sortCriteria, int startIndex, int maxResults) {
		Search s = new Search();

		s.setName(UserListAction.QUERY_TYPE_DEFAULT);

		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		s.setSortCriteria(sortArray);

		s.setStartIndex(startIndex);
		s.setMaxResults(maxResults);
		
		UserRole adminRole = adminUser.getCurrentRole();

		String adminLocationTypeId = adminUser.getCurrentRole()
				.getLocationTypeId();
		String adminLocationId = adminUser.getCurrentRole().getLocationId();

		// Set up the aliases

		ArrayList aliasArray = new ArrayList();
		AliasCriteria alias = new AliasCriteria("cwnsUserLocations", "cul",
				AliasCriteria.JOIN_INNER);
		aliasArray.add(alias);
		
		if(adminRole.isLimited()){
			alias = new AliasCriteria("cul.cwnsUserLocationFacilities", "culf", AliasCriteria.JOIN_LEFT);
			aliasArray.add(alias);
		}

		alias = new AliasCriteria("cul.roleRef", "rr", AliasCriteria.JOIN_INNER);
		aliasArray.add(alias);

		alias = new AliasCriteria("rr.locationTypeRef", "lref",
				AliasCriteria.JOIN_INNER);
		aliasArray.add(alias);

		alias = new AliasCriteria("cwnsUserStatusRef", "cusr",
				AliasCriteria.JOIN_INNER);
		aliasArray.add(alias);

		s.setAlias(aliasArray);

		SearchConditions scs = new SearchConditions();
		int pendingUsers = regGetNumberOfPendingUsers(adminUser);
		if (pendingUsers > 0) {
			// Since there are Pending users for this adminUser display the
			// Pending Users by default
			scs.setCondition(new SearchCondition("cusr.cwnsUserStatusId",
					SearchCondition.OPERATOR_EQ, "P"));
			s.setDescription(UserListAction.QUERY_TYPE_PENDING_DESC);
		} else {
			// Otherwise just filter out the Inactive users
			scs.setCondition(new SearchCondition("cusr.cwnsUserStatusId",
					SearchCondition.OPERATOR_NOT_EQ, "I"));
			s.setDescription(UserListAction.QUERY_TYPE_DEFAULT_DESC);
		}

		// Determine Location Type based on the Admin User's Location Type
		SearchConditions scs2 = new SearchConditions(new SearchCondition(
				"lref.locationTypeId", SearchCondition.OPERATOR_EQ,
				LOCATION_TYPE_ID_LOCAL));

		if (adminLocationTypeId.equals(LOCATION_TYPE_ID_STATE)) {
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition(
					"lref.locationTypeId", SearchCondition.OPERATOR_EQ,
					LOCATION_TYPE_ID_STATE));

		} else if (adminLocationTypeId.equals(LOCATION_TYPE_ID_FEDERAL)) {
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition(
					"lref.locationTypeId", SearchCondition.OPERATOR_EQ,
					LOCATION_TYPE_ID_STATE));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition(
					"lref.locationTypeId", SearchCondition.OPERATOR_EQ,
					LOCATION_TYPE_ID_REGIONAL));
			scs2.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition(
					"lref.locationTypeId", SearchCondition.OPERATOR_EQ,
					LOCATION_TYPE_ID_FEDERAL));
		}
		scs.setCondition(SearchCondition.OPERATOR_AND, scs2);

		/* Filter based on Location */
		if (adminLocationTypeId.equals(LOCATION_TYPE_ID_STATE)
				|| adminLocationTypeId.equals(LOCATION_TYPE_ID_LOCAL)) {
			// State or Local user
			scs.setCondition(new SearchCondition("cul.id.locationId",
					SearchCondition.OPERATOR_EQ, adminLocationId));
		}
		
		// if admin is limited by facilitites make sure that the users have atleast one common facilitity 
		
		if(adminRole.isLimited() && pendingUsers<=0){
			HashMap m = adminRole.getFacilities();
			scs.setCondition(new SearchCondition("culf.id.facilityId",SearchCondition.OPERATOR_IN, m.keySet()));			
		}

		s.setSearchConditions(scs);
		return s;
	}

	/**
	 * 
	 * @param search
	 * @return
	 */
	public Set getUserIds(Search search) {

		ArrayList columns = new ArrayList();
		columns.add("cwnsUserId");
		List userIds = searchDAO.getSearchList(CwnsUser.class, columns, search
				.getSearchConditions(), new ArrayList(), search.getAlias(), 0,
				0);
		Set uniqueUserIds = new HashSet();
		uniqueUserIds.addAll(userIds);
		return uniqueUserIds;
	}

	public Collection getUserListHelpers(Collection userList, Search search) {
		Collection users = cwnsUserDAO.getUserObjects(userList, search);
		return initializeUserListHelpers(users);
	}

	public Collection initializeUserListHelpers(Collection users) {
		Collection userListHelpers = new ArrayList();
		Iterator iter = users.iterator();
		while (iter.hasNext()) {
			CwnsUser cwnsUser = (CwnsUser) iter.next();
			UserListHelper ULH = new UserListHelper(cwnsUser.getLastName()
					+ ", " + cwnsUser.getFirstName(), cwnsUser.getCwnsUserId(),
					cwnsUser.getOidUserid(), cwnsUser.getCwnsUserStatusRef()
							.getName(), cwnsUser.getLastUpdateTs());

			userListHelpers.add(ULH);
		}
		return userListHelpers;
	}

	// default search condition
	public SearchConditions getLocationSearchCondition(String locationId) {
		SearchConditions scs3 = new SearchConditions(new SearchCondition(
				"locationId", SearchCondition.OPERATOR_LIKE, locationId));
		scs3.setCondition(new SearchCondition("versionCode",
				SearchCondition.OPERATOR_EQ, "S"));
		return scs3;
	}

	/**
	 * Return a collection of locationTypeRef objects base on a user's userType.
	 * If a null value is passed in for the user type a complete list of
	 * LocationTypes is returned.
	 */
	public Collection getLocationTypeRefs(String userType) {

		SortCriteria sortCriteria = new SortCriteria("locationTypeId",
				SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);

		Collection locationTypeRefs = searchDAO.getSearchList(
				LocationTypeRef.class, new SearchConditions(), sortArray);

		Collection locTypeRefs = new ArrayList();
		Iterator iter = locationTypeRefs.iterator();

		if (LOCATION_TYPE_ID_LOCAL.equals(userType)) {
			while (iter.hasNext()) {
				LocationTypeRef ltr = (LocationTypeRef) iter.next();
				if (ltr.getLocationTypeId().trim().equals(
						LOCATION_TYPE_ID_LOCAL)) {
					locTypeRefs.add(ltr);
				}
			}
		} else if (LOCATION_TYPE_ID_STATE.equals(userType)) {
			while (iter.hasNext()) {
				LocationTypeRef ltr = (LocationTypeRef) iter.next();
				if (ltr.getLocationTypeId().trim().equals(
						LOCATION_TYPE_ID_LOCAL.trim())
						|| ltr.getLocationTypeId().trim().equals(
								LOCATION_TYPE_ID_STATE.trim())) {
					locTypeRefs.add(ltr);
				}
			}

		} else if (LOCATION_TYPE_ID_FEDERAL.equals(userType)) { // Federal
			
			while (iter.hasNext()) {
				LocationTypeRef ltr = (LocationTypeRef) iter.next();
				if (!ltr.getLocationTypeId().trim().equals(LOCATION_TYPE_ID_LOCAL.trim())) {
					locTypeRefs.add(ltr);
				}
			}
		} else{ // ALL
			locTypeRefs.addAll(locationTypeRefs);
		}
		return locTypeRefs;
	}

	/**
	 * Return the complete Collection of LocationRef objects.
	 */
	public Collection getLocationRefs() {

		SortCriteria sortCriteria = new SortCriteria("name",
				SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);

		SearchConditions scs = new SearchConditions();
		Collection locationRefs = searchDAO.getSearchList(LocationRef.class,
				new ArrayList(), scs, sortArray);
		return locationRefs;
	}

	/**
	 * Return a list of user LocationRef objects based on the Location and
	 * LocationType of the CurrentUser object passed in.
	 */
	public Collection getLocationRefs(CurrentUser adminUser) {

		String userType = adminUser.getCurrentRole().getLocationTypeId();
		String userLocationId = adminUser.getCurrentRole().getLocationId();

		SortCriteria sortCriteria = new SortCriteria("name",
				SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);

		SearchConditions scs = null;
		if ("Local".equals(userType.trim()) || "State".equals(userType.trim())) {
			scs = new SearchConditions(new SearchCondition("locationId",
					SearchCondition.OPERATOR_EQ, userLocationId));

		} else { // Federal
			scs = new SearchConditions();
		}

		Collection locationRefs = searchDAO.getSearchList(LocationRef.class,
				new ArrayList(), scs, sortArray);

		return locationRefs;
	}

	/**
	 * Create a user object for the Portal User name passed in.
	 * 
	 * This method is used for testing purposes. It will be replaced with actual
	 * code once the Identity Management issues (CDX vs. IAM) have been
	 * clarified.
	 */
	public CurrentUser createUserObject(String portalUsername)
			throws ApplicationException {
		
		/* Find an Active user based on the Portal username */ 
		SearchConditions scs =  new SearchConditions(new SearchCondition("oidUserid", SearchCondition.OPERATOR_EQ, portalUsername));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("cwnsUserStatusRef.cwnsUserStatusId", SearchCondition.OPERATOR_EQ, "A"));
		Collection users = searchDAO.getSearchList(CwnsUser.class,  scs);
		if (users.size() < 1) {
			log.error("Unable to find cwns user for portal userId "+portalUsername);
			return null;
		} else if (users.size() > 1) {
			log.error(""+users.size()+" active cwns users found for portal userId "+portalUsername);
			return null;
		}
		CwnsUser cwnsUser = (CwnsUser) users.iterator().next();

		CurrentUser userObj = new CurrentUser();
		UserRole userRole = null;
		/* Set user profile info */
		userObj.setFirstName(cwnsUser.getFirstName());
		userObj.setLastName(cwnsUser.getLastName());
		userObj.setFullName(userObj.getFirstName() + " "
				+ userObj.getLastName());

		userObj.setUserId(cwnsUser.getCwnsUserId());
		userObj.setOidUserId(portalUsername);
		userObj.setPhoneNumber(cwnsUser.getPhone());

		// Get the CwnsUserLocation objects (Roles) for this user
		Collection cwnsUserLocations = getCwnsUserLocations(cwnsUser
				.getCwnsUserId());

		// loop through the CwnsUserLocation objects
		Iterator iter = cwnsUserLocations.iterator();
		while (iter.hasNext()) {
			CwnsUserLocation cul = (CwnsUserLocation) iter.next();
			String locationId = cul.getId().getLocationId();
			HashMap facilityIds = new HashMap();

			// Is this the Primary role?
			boolean primaryRole = false;
			String priFlag = new Character(cul.getPrimaryFlag()).toString();
			if ("Y".equals(priFlag)) {
				primaryRole = true;
			}

			// limited Facilities
			String limitedFacilities = new Character(cul
					.getLimitedFacilitiesFlag()).toString();
			boolean limited = false;
			if (limitedFacilities.equals("Y")) {
				limited=true;
				Set facilities = cul.getCwnsUserLocationFacilities();
				if(facilities!=null){
					for (Iterator iterator = facilities.iterator(); iterator.hasNext();) {
						CwnsUserLocationFacility culf = (CwnsUserLocationFacility) iterator.next();
						facilityIds.put(new Long(culf.getId().getFacilityId()), culf.getFacility());
					}
				}
			}

			// Location Type
			String locationTypeId = cul.getRoleRef().getId()
					.getLocationTypeId();

			// Role
			// String roleName = locationType.trim()+"/"+locationId.trim();

			// Get the CwnsUserLocatnAccessLevel objects for this user and
			// locationId
			AliasCriteria alias = new AliasCriteria("accessLevelRef", "alf",
					AliasCriteria.JOIN_LEFT);
			ArrayList aliasArray = new ArrayList();
			aliasArray.add(alias);

			scs = new SearchConditions(new SearchCondition("id.cwnsUserId",
					SearchCondition.OPERATOR_EQ, cwnsUser.getCwnsUserId()));
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition(
					"id.locationId", SearchCondition.OPERATOR_EQ, locationId));
			scs.setCondition(new SearchCondition("id.locationTypeId",
					SearchCondition.OPERATOR_EQ, locationTypeId));
			Collection accessLevels = searchDAO.getSearchList(
					CwnsUserLocatnAccessLevel.class, new ArrayList(), scs,
					new ArrayList(), aliasArray);

			// For each CwnsUserLocatnAccessLevel object get the accessLevelName
			Iterator aLevelsIter = accessLevels.iterator();
			HashMap accessLevs = new HashMap();
			while (aLevelsIter.hasNext()) {
				CwnsUserLocatnAccessLevel culal = (CwnsUserLocatnAccessLevel) aLevelsIter
						.next();
				accessLevs.put(new Long(culal.getAccessLevelRef()
						.getAccessLevelId()), culal.getAccessLevelRef()
						.getName());
			}

			userRole = new UserRole(locationTypeId, locationId, primaryRole,
					accessLevs, facilityIds, limited);
			userObj.getRoles().add(userRole);
			if (primaryRole) {
				userObj.setCurrentRole(userRole);
			}

		}

		// If the user status is not Pending ensure there is a primary role.
		// If no Primary role is identified arbitrarily pick on.
		if (userObj.getCurrentRole() == null) {
			if (userRole == null) {
				if (!userObj.getStatus().equals("P")) {
					throw new ApplicationException(
							"No user role (CWNS_USER_LOCATION) found for cwnsUserId "
									+ userObj.getUserId());
				}
			} else {
				userObj.setCurrentRole(userRole);
				log.error("ERROR: Arbitrarily choosing primary role for cwnsUserId "
								+ userObj.getUserId());
			}
		}

		// Update the last login time
		cwnsUser.setLastLogonTs(new Date());
		searchDAO.getDAOSession().saveOrUpdate(cwnsUser);

		return userObj;
	}

	public String findUserNameByUserId(String userId) {
		String name = "Not Found";
		ArrayList columns = new ArrayList();
		columns.add("firstName");
		columns.add("lastName");
		SearchConditions scs = getKeyWordSearchCondition(userId);
		Collection results = searchDAO.getSearchList(CwnsUser.class, columns,
				scs);
		Iterator it = results.iterator();
		if (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			name = (String) obj[0] + " " + (String) obj[1];
		}

		return name;
	}

	public CwnsUser findUserByUserId(String cwnsUserId) {
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"cwnsUserId", SearchCondition.OPERATOR_EQ, cwnsUserId
						.toUpperCase()));
		return (CwnsUser) searchDAO.getSearchObject(CwnsUser.class, scs);
	}

	public SearchConditions getKeyWordSearchCondition(String userId) {
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"cwnsUserId", SearchCondition.OPERATOR_EQ, userId));
		return scs;
	}

	/**
	 * Return a list of CwnsUserStatus objects
	 */
	public Collection getCwnsUserStatusRefs() {
		return cwnsUserStatusRefDAO.getCwnsUserStatusRefs();
	}

	/**
	 * For a given user return all the access level refs they have the security
	 * privleges to assign.
	 */
	public Collection getAssignedByAccessLevelRefs(CurrentUser adminUser) {

		String userLocationTypeId = adminUser.getCurrentRole()
				.getLocationTypeId().trim();
	
		// Get all the ACCESS_LEVEL_ASSIGN_BY_REFs for this type of use
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.locationTypeId", SearchCondition.OPERATOR_LIKE, userLocationTypeId));
		Collection accessLevelAssignedByRefs = searchDAO.getSearchList(AccessLevelAssignedByRef.class,scs);
		
		// Return the associated ACCESS_LEVEL_REFs
		Collection result = new ArrayList();
		Iterator iter = accessLevelAssignedByRefs.iterator();
		while (iter.hasNext()) {
			AccessLevelAssignedByRef alabr = (AccessLevelAssignedByRef) iter.next();
			result.add(alabr.getAccessLevelRef());
		}
		return result;
	}

	/**
	 * Return a Collection of AccessLevelRefs objects based on the
	 * locationTypeId passed in. This method utilizes the table
	 * ACCESS_LEVEL_APPLIES_TO_REF.
	 * 
	 * @param locationTypeId
	 * @return
	 */
	public Collection getAccessLevelRefs(String locationTypeId) {

		Collection result = new ArrayList();
		// Collection accessLevelAssignedByRefs =
		// searchDAO.getSearchList(AccessLevelAssignedByRef.class, new
		// SearchConditions());
		Collection accessLevelAppliesToRef = searchDAO.getSearchList(
				AccessLevelAppliesToRef.class, new SearchConditions());

		Iterator iter = accessLevelAppliesToRef.iterator();
		while (iter.hasNext()) {
			AccessLevelAppliesToRef alabr = (AccessLevelAppliesToRef) iter
					.next();
			String locId = alabr.getLocationTypeRef().getLocationTypeId()
					.trim();
			if (locationTypeId.equals(locId)) {
				result.add(alabr.getAccessLevelRef());
			}
		}
		return result;

	}

	/**
	 * Return a Collection of LocationRef objects based on the locationTypeId
	 * passed in
	 * 
	 * @param locationTypeId
	 * @return
	 */
	public Collection getLocationRefs(String locationTypeId) {

		Collection result = new ArrayList();

		// get the RoleRef objects for the specified location type
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.locationTypeId", SearchCondition.OPERATOR_EQ,
				locationTypeId));
		Collection roleRefs = searchDAO.getSearchList(RoleRef.class, scs);

		// iterate through the RoleRefs and get the associated LocationRef
		// objects
		Iterator roleRefIter = roleRefs.iterator();
		while (roleRefIter.hasNext()) {
			RoleRef roleRef = (RoleRef) roleRefIter.next();
			result.add(roleRef.getLocationRef());
		}

		return result;
	}

	/**
	 * Update a UserDetailsForm obj based on the cwnsUserId found in the form
	 * passed in.
	 */
	public UserDetailsForm getUserData(UserDetailsForm udForm, CurrentUser currentUser) {

		// Flush and clear the Hibernate cache otherwise newly added
		// User Roles may cause a null pointer exception.
		searchDAO.flushAndClearCache();

		// get the user object
		CwnsUser cwnsUser = findUserByUserId(udForm.getCwnsUserId());

		// If the user selected a role make it the current role
		if (udForm.getSelectedRoleInfoId() != null
				&& udForm.getSelectedRoleInfoId().length() > 0) {

			udForm.setCurrentLocId(udForm.getSelectedRoleLocId());
			udForm.setCurrentLocationTypeId(udForm.getSelectedRoleLocTypeId());
		}

		// initialize some form bean attributes
		String name = cwnsUser.getLastName() + ", " + cwnsUser.getFirstName();
		udForm.setName(name);
		udForm.setComments(cwnsUser.getDescription());
		udForm.setPhone(formatPhoneNumber(cwnsUser.getPhone()));
		udForm.setEmail(cwnsUser.getEmailAddress());
		udForm.setOidUserId(cwnsUser.getOidUserid());
		String statusId = cwnsUser.getCwnsUserStatusRef().getCwnsUserStatusId();
		udForm.setStatus(statusId);
		udForm.setRegType(cwnsUser.getRegType());
		udForm.setAffiliation(cwnsUser.getAffiliation());
		udForm.setTitle(cwnsUser.getTitle());
		udForm.setFacilityList(cwnsUser.getFacilityList());
		udForm.setTownAndCountyList(cwnsUser.getTownAndCountyList());

		// Get all the CwnsUserLocation objects for this user
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.cwnsUserId", SearchCondition.OPERATOR_EQ, udForm
						.getCwnsUserId()));
		Collection locationIds = searchDAO.getSearchList(
				CwnsUserLocation.class, new ArrayList(), scs);

		// For each CwnsUserLocation object get the information we need
		udForm.setHelpers(new ArrayList());
		Iterator iter = locationIds.iterator();
		while (iter.hasNext()) {

			CwnsUserLocation cul = (CwnsUserLocation) iter.next();
			String locationId = cul.getId().getLocationId();
			String locationTypeId = cul.getId().getLocationTypeId();

			// Location Type
			String locationType = cul.getRoleRef().getId().getLocationTypeId();

			// Role
			String role = locationType.trim() + ":" + locationId.trim();

			// Is this the Primary role?
			String priFlag = new Character(cul.getPrimaryFlag()).toString();
			if ("Y".equals(priFlag)) {
				udForm.setPrimaryRoleInfo(role);
			}

			// limited Facilities
			String limitedFacilities = String.valueOf(cul.getLimitedFacilitiesFlag());
			
			if (udForm.getSelectedRoleInfoId() != null
					&& udForm.getSelectedRoleInfoId().length() > 0) {

				if(locationId.equals(udForm.getSelectedRoleLocId()) && locationTypeId.equals(udForm.getSelectedRoleLocTypeId())){
					udForm.setAllFacilities(("Y".equals(limitedFacilities))?"N":"Y");
				}
			}

			// Get the CwnsUserLocatnAccessLevel objects for this user and
			// locationId
			AliasCriteria alias = new AliasCriteria("accessLevelRef", "alf",
					AliasCriteria.JOIN_LEFT);
			ArrayList aliasArray = new ArrayList();
			aliasArray.add(alias);

			scs = new SearchConditions(new SearchCondition("id.cwnsUserId",
					SearchCondition.OPERATOR_EQ, udForm.getCwnsUserId()));
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition(
					"id.locationId", SearchCondition.OPERATOR_EQ, locationId));
			scs.setCondition(new SearchCondition("id.locationTypeId",
					SearchCondition.OPERATOR_EQ, locationTypeId));

			Collection accessLevels = searchDAO.getSearchList(
					CwnsUserLocatnAccessLevel.class, new ArrayList(), scs,
					new ArrayList(), aliasArray);

			// For each CwnsUserLocatnAccessLevel object get the accessLevelName
			Iterator aLevelsIter = accessLevels.iterator();
			Collection accessLevs = new ArrayList();
			while (aLevelsIter.hasNext()) {
				CwnsUserLocatnAccessLevel culal = (CwnsUserLocatnAccessLevel) aLevelsIter
						.next();
				accessLevs.add(culal.getAccessLevelRef().getName());
			}

			UserDetailsHelper udh = new UserDetailsHelper(locationId, role,
					accessLevs, limitedFacilities);
			udForm.getHelpers().add(udh);
		}

		// Determine if the "Add Role" link should be enabled
		if (enableAddRole(udForm.getHelpers(),currentUser)) {
			udForm.setEnableAddRoleLink(true);
		} else {
			udForm.setEnableAddRoleLink(false);
		}
		return udForm;

	}

	/**
	 * Determine if the "Add Role" link should be enabled in the User Details screen
	 * @param userDetailsHelpers
	 * @param currentUser
	 * @return True if the link should be displayed, and false otherwise.
	 */
	private boolean enableAddRole (Collection userDetailsHelpers, CurrentUser currentUser ) {
		boolean enable = true;
		
		String currentUserLocationTypeId = currentUser.getCurrentRole().getLocationTypeId();
		String currentUserLocationId     = currentUser.getCurrentRole().getLocationId();

		if ("State".equals(currentUserLocationTypeId)){
			
			int found = 0;
			Iterator iter = userDetailsHelpers.iterator();
			while(iter.hasNext()) {
				UserDetailsHelper udh = (UserDetailsHelper) iter.next();
				String [] role = (udh.getRole()).split(":");
				if (currentUserLocationId.equals(role[1])) {
					found++;
				}
			}
			
			if (found > 1) {
				// There is at least two roles for this particular state already
				enable = false;
			}
		}
			
		if ("Local".equals(currentUserLocationTypeId)) {
			int found = 0;
			Iterator iter = userDetailsHelpers.iterator();
			while(iter.hasNext()) {
				UserDetailsHelper udh = (UserDetailsHelper) iter.next();
				String [] role = (udh.getRole()).split(":");
				if (currentUserLocationId.equals(role[1]) &&
					currentUserLocationTypeId.equals(role[0])) {
					found++;
				}
			}

			if (found > 0) {
				// There is at least one Local role for this particular state already 
				enable = false;
			}
		}
			
		return enable;
	}

	/**
	 * Given a cwnsUserId, locationTypeId & a locationId return the associated
	 * CwnsUserObject.
	 * 
	 * @param cwnsUserId
	 * @param locationTypeId
	 * @param locationId
	 * @return The CwnsUserLocation object if found, otherwise null
	 */
	public CwnsUserLocation getCwnsUserLocation(String cwnsUserId,
			String locationTypeId, String locationId) {
		CwnsUserLocation cwnsUserLocation = null;

		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.locationId", SearchCondition.OPERATOR_EQ, locationId));
		scs.setCondition(new SearchCondition("id.locationTypeId",
				SearchCondition.OPERATOR_EQ, locationTypeId));
		scs.setCondition(new SearchCondition("id.cwnsUserId",
				SearchCondition.OPERATOR_EQ, cwnsUserId));
		Collection cwnsUserLocations = searchDAO.getSearchList(
				CwnsUserLocation.class, scs);

		Iterator iter = cwnsUserLocations.iterator();
		if (iter.hasNext()) {
			cwnsUserLocation = (CwnsUserLocation) iter.next();
		}

		return cwnsUserLocation;
	}

	/**
	 * Return the Collection of CwnsUserLocation objects associated with the
	 * given cwnsUserId
	 * 
	 * @param cwnsUserId
	 * @return A Collection of CwnsUserLocation objects associated with the
	 *         cwnsUserId. If none are found return an empty Collection.
	 */
	public Collection getCwnsUserLocations(String cwnsUserId) {
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.cwnsUserId", SearchCondition.OPERATOR_EQ, cwnsUserId));
		Collection cwnsUserLocations = searchDAO.getSearchList(
				CwnsUserLocation.class, scs);
		return cwnsUserLocations;
	}

	/**
	 * Under the certain circumstances we automatically change a user's status
	 * to active. Those circumstances are: 1.) The user has only 1 Role 2.) That
	 * Role has only 1 Access Level and that Access Level is "View"
	 * 
	 * @param cwnsUserId
	 * @return
	 */
	public void addRole(UserDetailsForm udForm, String adminUserId)
			throws ApplicationException {

		// Obtain the CwnsUser object
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"cwnsUserId", SearchCondition.OPERATOR_EQ, udForm
						.getCwnsUserId()));
		CwnsUser cwnsUser = (CwnsUser) searchDAO.getSearchObject(
				CwnsUser.class, scs);
		String allfacilities = udForm.getAllFacilities();
		char lff = 'N';
		if (allfacilities.compareToIgnoreCase("N") == 0) {
			lff = 'Y';
		}

		// Obtain the RoleRef object
		scs = new SearchConditions(new SearchCondition("id.locationId",
				SearchCondition.OPERATOR_EQ, udForm.getCurrentLocId()));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition(
				"id.locationTypeId", SearchCondition.OPERATOR_EQ, udForm
						.getCurrentLocationTypeId()));
		RoleRef roleRef = (RoleRef) searchDAO.getSearchObject(RoleRef.class,
				scs);

		// create the CwnsUserLocation object
		CwnsUserLocation cwnsUserLocation = new CwnsUserLocation();
		cwnsUserLocation.setCwnsUser(cwnsUser);
		cwnsUserLocation.setRoleRef(roleRef);
		cwnsUserLocation.setPrimaryFlag('N');
		cwnsUserLocation.setLimitedFacilitiesFlag(lff);
		cwnsUserLocation.setLastUpdateTs(new Date());
		cwnsUserLocation.setLastUpdateUserid(adminUserId);

		CwnsUserLocationId cwnsUserLocationId = new CwnsUserLocationId(udForm
				.getCwnsUserId(), udForm.getCurrentLocationTypeId(), udForm
				.getCurrentLocId());
		cwnsUserLocation.setId(cwnsUserLocationId);
		cwnsUserLocationDAO.save(cwnsUserLocation);

		/* create cwnsUserLocationFacility */
		updateSelectedFacilities(udForm, cwnsUserLocation, adminUserId);

		/* create one or more CwnsUserLocatnAccessLevel objects */
		String[] accessLevelIds = udForm.getAccessLevelIds();
		if (accessLevelIds != null) {
			for (int i = 0; i < accessLevelIds.length; i++) {
				String accessLevelId = accessLevelIds[i];

				CwnsUserLocatnAccessLevel ulAccessLevel = new CwnsUserLocatnAccessLevel();
				ulAccessLevel.setCwnsUserLocation(cwnsUserLocation);
				ulAccessLevel.setLastUpdateTs(new Date());
				ulAccessLevel.setLastUpdateUserid(adminUserId);

				CwnsUserLocatnAccessLevelId id = new CwnsUserLocatnAccessLevelId();
				id.setCwnsUserId(udForm.getCwnsUserId());
				id.setAccessLevelId(new Long(accessLevelId).longValue());
				id.setLocationId(cwnsUserLocation.getId().getLocationId());
				id.setLocationTypeId(cwnsUserLocation.getId()
						.getLocationTypeId());
				ulAccessLevel.setId(id);
				// save the CwnsUserLocatnAccessLevel object
				cwnsUserLocatnAccessLevelDAO.save(ulAccessLevel);
			}
		}
			
		// All users get the View access privilege - add it now
		CwnsUserLocatnAccessLevel ulAccessLevel = new CwnsUserLocatnAccessLevel();
		ulAccessLevel.setCwnsUserLocation(cwnsUserLocation);
		ulAccessLevel.setLastUpdateTs(new Date());
		ulAccessLevel.setLastUpdateUserid(adminUserId);

		CwnsUserLocatnAccessLevelId id = new CwnsUserLocatnAccessLevelId();
		id.setCwnsUserId(udForm.getCwnsUserId());
		id.setAccessLevelId(12);
		id.setLocationId(cwnsUserLocation.getId().getLocationId());
		id.setLocationTypeId(cwnsUserLocation.getId()
				.getLocationTypeId());
		ulAccessLevel.setId(id);
		// save the CwnsUserLocatnAccessLevel object
		cwnsUserLocatnAccessLevelDAO.save(ulAccessLevel);
	}

	/**
	 * Get the access levels associated with this User's Role and update the
	 * UserDetailsForm.accessLevelIds attribute. Updating this attribute will
	 * cause the access levels to be "selected" when displayed in the JSP.
	 */
	public void getAccessLevelsForRole(UserDetailsForm udForm) {

		// Create a UserDetailsRoleHelper object
		UserDetailsRoleHelper udrHelper = new UserDetailsRoleHelper();
		udForm.setUdrHelper(udrHelper);

		/*
		 * // Get the CwnsUserLocation object CwnsUserLocation cwnsUserLocation =
		 * getCwnsUserLocation(udForm.getCwnsUserId(),
		 * udForm.getSelectedRoleLocTypeId(), udForm.getSelectedRoleLocId());
		 *  // Set the selected location value in the UserDetailsForm String
		 * limitedFacilities = new
		 * Character(cwnsUserLocation.getLimitedFacilitiesFlag()).toString();
		 * udrHelper.setLimitedFacilities(limitedFacilities); if
		 * ("N".equalsIgnoreCase(limitedFacilities)) { // Obtain the
		 * CwnsUserLocationFacility objects SearchConditions scs = new
		 * SearchConditions(new SearchCondition("id.locationId",
		 * SearchCondition.OPERATOR_EQ, udForm.getCurrentLocId()));
		 * scs.setCondition(new SearchCondition("id.cwnsUserId",
		 * SearchCondition.OPERATOR_EQ, udForm.getCwnsUserId()));
		 * scs.setCondition(new SearchCondition("id.locationTypeId",
		 * SearchCondition.OPERATOR_EQ, udForm.getSelectedRoleLocTypeId()));
		 * Collection cwnsUserLocationFacs =
		 * searchDAO.getSearchList(CwnsUserLocationFacility.class, scs );
		 * 
		 * udrHelper.setFacilities(new ArrayList()); Iterator
		 * cwnsUserLocationFacsIter = cwnsUserLocationFacs.iterator(); while
		 * (cwnsUserLocationFacsIter.hasNext()) { CwnsUserLocationFacility
		 * CwnsUserLocationFacility = (CwnsUserLocationFacility)
		 * cwnsUserLocationFacsIter.next(); Long culfId = new Long
		 * (CwnsUserLocationFacility.getFacility().getFacilityId()); Entity
		 * entity = new Entity(culfId.toString(),
		 * CwnsUserLocationFacility.getFacility().getName());
		 * udrHelper.getFacilities().add(entity); } }
		 */

		// Obtain the CwnsUserLocatnAccessLevel objects
		SearchConditions scs3 = new SearchConditions(new SearchCondition(
				"id.locationId", SearchCondition.OPERATOR_EQ, udForm
						.getCurrentLocId()));
		scs3.setCondition(new SearchCondition("id.cwnsUserId",
				SearchCondition.OPERATOR_EQ, udForm.getCwnsUserId()));
		scs3
				.setCondition(new SearchCondition("id.locationTypeId",
						SearchCondition.OPERATOR_EQ, udForm
								.getCurrentLocationTypeId()));
		Collection cwnsUserLocatnAccessLevels = searchDAO.getSearchList(
				CwnsUserLocatnAccessLevel.class, scs3);

		Collection culalAccessLevels = new ArrayList();
		Iterator culalIter = cwnsUserLocatnAccessLevels.iterator();
		while (culalIter.hasNext()) {
			CwnsUserLocatnAccessLevel culalAccessLevel = (CwnsUserLocatnAccessLevel) culalIter
					.next();
			Long culalId = new Long(culalAccessLevel.getId().getAccessLevelId());
			culalAccessLevels.add(culalId.toString());
		}
		udForm.setAccessLevelIds((String[]) culalAccessLevels
				.toArray(new String[culalAccessLevels.size()]));
		udForm.setCurrentLocationTypeId(udForm.getSelectedRoleLocTypeId());
	}

	public ArrayList getFacilitiesByIds(String cwnsUserId,
			String locationTypeId, String locationId) {
		ArrayList facilities = new ArrayList();

		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.cwnsUserId", SearchCondition.OPERATOR_EQ, cwnsUserId));
		scs.setCondition(new SearchCondition("id.locationId",
				SearchCondition.OPERATOR_EQ, locationId));
		scs.setCondition(new SearchCondition("id.locationTypeId",
				SearchCondition.OPERATOR_EQ, locationTypeId));

		Collection cuslfs = searchDAO.getSearchList(
				CwnsUserLocationFacility.class, scs);
		Iterator it = cuslfs.iterator();

		while (it.hasNext()) {
			CwnsUserLocationFacility temp = (CwnsUserLocationFacility) it
					.next();
			Facility f = temp.getFacility();
			facilities.add(f);
		}

		return facilities;
	}

	public Collection getFacilitiesForRole(UserDetailsForm udForm) {

		Collection facilities = new ArrayList();

		// Get the CwnsUserLocation object
		CwnsUserLocation cwnsUserLocation = getCwnsUserLocation(udForm
				.getCwnsUserId(), udForm.getSelectedRoleLocTypeId(), udForm
				.getSelectedRoleLocId());

		// Examine the Limited Facilities Flag
		String limitedFacilities = new Character(cwnsUserLocation
				.getLimitedFacilitiesFlag()).toString();

		/*
		 * WHY DOES THE FOLLOWING CAUSE A CLASS CAST EXCEPTION ??
		 * 
		 * if ("N".equalsIgnoreCase(limitedFacilities)) {
		 *  // obtain the facility objects ArrayList aliasArray = new
		 * ArrayList(); AliasCriteria alias = new
		 * AliasCriteria("cwnsUserLocationFacilities", "culf",
		 * AliasCriteria.JOIN_INNER); aliasArray.add(alias);
		 * 
		 * SearchConditions scs = new SearchConditions(new
		 * SearchCondition("facilityId", SearchCondition.OPERATOR_EQ,
		 * "culf.id.facilityId")); scs.setCondition(new
		 * SearchCondition("culf.id.cwnsUserId", SearchCondition.OPERATOR_EQ,
		 * udForm.getCwnsUserId())); // scs.setCondition(new
		 * SearchCondition("culf.id.locationTypeId",
		 * SearchCondition.OPERATOR_EQ, udForm.getSelectedRoleLocTypeId())); //
		 * scs.setCondition(new SearchCondition("culf.id.locationId",
		 * SearchCondition.OPERATOR_EQ, udForm.getCwnsUserId()));
		 * 
		 * ArrayList sortArray = new ArrayList(); // SortCriteria sortCriteria =
		 * new SortCriteria("name", SortCriteria.ORDER_ASCENDING); //
		 * sortArray.add(sortCriteria);
		 * 
		 * facilities = searchDAO.getSearchList(Facility.class, new ArrayList(),
		 * scs, sortArray, aliasArray); }
		 */

		if ("N".equalsIgnoreCase(limitedFacilities)) {
			// Obtain the CwnsUserLocationFacility objects
			SearchConditions scs2 = new SearchConditions(new SearchCondition(
					"id.locationId", SearchCondition.OPERATOR_EQ, udForm
							.getCurrentLocId()));
			scs2.setCondition(new SearchCondition("id.cwnsUserId",
					SearchCondition.OPERATOR_EQ, udForm.getCwnsUserId()));
			scs2.setCondition(new SearchCondition("id.locationTypeId",
					SearchCondition.OPERATOR_EQ, udForm
							.getCurrentLocationTypeId()));
			Collection cwnsUserLocationFacs = searchDAO.getSearchList(
					CwnsUserLocationFacility.class, scs2);

			// For each CwnsLocationFacility object get the associated Facility
			// object
			Iterator cwnsUserLocationFacsIter = cwnsUserLocationFacs.iterator();
			while (cwnsUserLocationFacsIter.hasNext()) {
				CwnsUserLocationFacility CwnsUserLocationFacility = (CwnsUserLocationFacility) cwnsUserLocationFacsIter
						.next();
				facilities.add(CwnsUserLocationFacility.getFacility());
			}
		}

		return facilities;
	}

	// update user information based on the form bean passed in
	public void updateUserData(UserDetailsForm udForm) {
		
		// Get the CwnsUser object
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"cwnsUserId", SearchCondition.OPERATOR_EQ, udForm
						.getCwnsUserId()));
		CwnsUser cwnsUser = (CwnsUser) searchDAO.getSearchObject(
				CwnsUser.class, scs);
		
		
		String oldStatus = cwnsUser.getCwnsUserStatusRef().getCwnsUserStatusId();
		
		
		
		// Get the CwnsUserstatusRef object
		CwnsUserStatusRef cwnsUserStatusRef = getCwnsUserStatusRef(udForm
				.getStatus());
		
		cwnsUser.setCwnsUserStatusRef(cwnsUserStatusRef);
		cwnsUser.setOidUserid(udForm.getOidUserId().trim());

		// Save the user self registration comments
		String comments = udForm.getComments();
		if (comments.length() > 4000) {
			// limit comments to 4000 characters
			comments = comments.substring(0, 4000);
		}
		cwnsUser.setDescription(comments);

		// Save the registration email notification comments
		String regNotificationComments = udForm.getRegNotificationComments();
		if (regNotificationComments.length() > 4000) {
			// limit comments to 4000 characters
			regNotificationComments = regNotificationComments
					.substring(0, 4000);
		}
		cwnsUser.setRegNotificationComments(regNotificationComments);

		cwnsUser.setPhone(formatPhoneNumber(udForm.getPhone()));

		cwnsUser.setEmailAddress(udForm.getEmail());

		// Set the user's primary Role
		String roleInfo = udForm.getPrimaryRoleInfo();
		String[] roleData = roleInfo.split(":");
		String locationTypeId = roleData[0];
		String locationId = roleData[1];

		setPrimaryRole(udForm.getCwnsUserId(), locationTypeId, locationId);
		
		// Save the CwnsUser object
		searchDAO.saveObject(cwnsUser);
		
		//if the use status has been updated, check to see if it I or A
		//if I remove from all CWNS groups
		//if A add into CWNS group
		try{
			if(!udForm.getStatus().equalsIgnoreCase(oldStatus)){
				if(udForm.getStatus().equalsIgnoreCase("I")){
					removeUserFromCWNSGroups(udForm.getCwnsUserId());
				}else if(udForm.getStatus().equalsIgnoreCase("A")){
					System.out.println("Current Location type = " + locationTypeId);
					addUserToCWNSGroups(udForm.getCwnsUserId(), locationTypeId);
				}
			}
			
		}catch(Exception e){
			log.error("Error trying to perform status control for CWNS groups with user " + udForm.getCwnsUserId(), e);
		}
	}

	/**
	 * Set the primary role of a user.
	 * 
	 * @param cwnsUserId
	 * @param locationTypeId
	 * @param locationId
	 */
	private void setPrimaryRole(String cwnsUserId, String locationTypeId,
			String locationId) {

		// Get a list of the user's roles (CwnsUserLocation objects)
		SearchConditions scs1 = new SearchConditions(new SearchCondition(
				"id.cwnsUserId", SearchCondition.OPERATOR_EQ, cwnsUserId));
		Collection cwnsUserLocations = searchDAO.getSearchList(
				CwnsUserLocation.class, scs1);

		if (cwnsUserLocations.size() > 0) {

			// Loop through the CwnsUserLocation objects update the primaryFlag
			// for each
			// role.
			Iterator cwnsUserLocationIter = cwnsUserLocations.iterator();
			while (cwnsUserLocationIter.hasNext()) {
				CwnsUserLocation cul = (CwnsUserLocation) cwnsUserLocationIter
						.next();
				if (cul.getId().getLocationId().equals(locationId)
						&& cul.getId().getLocationTypeId().equals(
								locationTypeId)) {
					cul.setPrimaryFlag('Y');
				} else {
					cul.setPrimaryFlag('N');
				}
				cwnsUserLocationDAO.save(cul);
			}
		}
	}

	/**
	 * Update a user role
	 */
	public void updateRole(UserDetailsForm udForm, CurrentUser currentUser) {

		updateSelectedFacIds(udForm, currentUser.getUserId());
		updateAccessLevels(udForm, currentUser.getUserId());
		updateUserData(udForm);
		getUserData(udForm, currentUser);
	}

	// update selected facilities
	public void updateSelectedFacIds(UserDetailsForm udForm, String adminUserId) {
		// delete existing CwnsUserLocationFacility object for thiw
		// CwnsUserLocation object
		SearchConditions scs2 = new SearchConditions(new SearchCondition(
				"id.locationId", SearchCondition.OPERATOR_EQ, udForm
						.getCurrentLocId()));
		scs2.setCondition(new SearchCondition("id.cwnsUserId",
				SearchCondition.OPERATOR_EQ, udForm.getCwnsUserId()));
		scs2
				.setCondition(new SearchCondition("id.locationTypeId",
						SearchCondition.OPERATOR_EQ, udForm
								.getCurrentLocationTypeId()));
		Collection cwnsFacilities = searchDAO.getSearchList(
				CwnsUserLocationFacility.class, scs2);
		searchDAO.deleteAll(cwnsFacilities);

		// Flush and clear the Hibernate Session cache
		searchDAO.flushAndClearCache();

		// Get the CwnsUserLocation object for this user
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.cwnsUserId", SearchCondition.OPERATOR_EQ, udForm
						.getCwnsUserId()));
		scs.setCondition(new SearchCondition("id.locationId",
				SearchCondition.OPERATOR_EQ, udForm.getCurrentLocId()));
		scs
				.setCondition(new SearchCondition("id.locationTypeId",
						SearchCondition.OPERATOR_EQ, udForm
								.getCurrentLocationTypeId()));
		Collection CwnsUserLocations = searchDAO.getSearchList(
				CwnsUserLocation.class, scs);
		Iterator culIter = CwnsUserLocations.iterator();
		CwnsUserLocation cwnsUserLocation = (CwnsUserLocation) culIter.next();
		
		
		
		if ("Y".equals(udForm.getAllFacilities())) {
			cwnsUserLocation.setLimitedFacilitiesFlag('N');
		} else {
			
			cwnsUserLocation.setLimitedFacilitiesFlag('Y');
			updateSelectedFacilities(udForm, cwnsUserLocation, adminUserId);
		}
	}

	private void updateSelectedFacilities(UserDetailsForm udForm,
			CwnsUserLocation cwnsUserLocation, String updateId) {
		
		//cwnsUserLocation.setLimitedFacilitiesFlag('Y');
		
		String strfacilitiesSelected = udForm.getStrFacilityIds();
		ArrayList list = udForm.getFacilityIds();
		getItemFromStr(strfacilitiesSelected, list);
		if (list.isEmpty() != true) {
			for (int i = 0; i < list.size(); i++) {
				String facilityId = (String) list.get(i);
				long fid = Long.parseLong(facilityId);
				CwnsUserLocationFacility cf = new CwnsUserLocationFacility();
				cf.setCwnsUserLocation(cwnsUserLocation);

				CwnsUserLocationFacilityId cflid = new CwnsUserLocationFacilityId();
				cflid.setCwnsUserId(udForm.getCwnsUserId());
				cflid.setFacilityId(fid);
				cflid.setLocationId(cwnsUserLocation.getId().getLocationId());
				cflid.setLocationTypeId(cwnsUserLocation.getId()
						.getLocationTypeId());

				cf.setId(cflid);
				cf.setLastUpdateTs(new Date());
				cf.setLastUpdateUserid(updateId);

				// save the CwnsUserLocationFacility
				searchDAO.saveObject(cf);

			}
		}

	}

	// Update Access Levels
	public void updateAccessLevels(UserDetailsForm udForm, String adminUserId) {

		// Delete the existing CwnsUserLocatnAccessLevel objects for this
		// CwnsuserLocation object
		SearchConditions scs2 = new SearchConditions(new SearchCondition(
				"id.locationId", SearchCondition.OPERATOR_EQ, udForm
						.getCurrentLocId()));
		scs2.setCondition(new SearchCondition("id.cwnsUserId",
				SearchCondition.OPERATOR_EQ, udForm.getCwnsUserId()));
		scs2
				.setCondition(new SearchCondition("id.locationTypeId",
						SearchCondition.OPERATOR_EQ, udForm
								.getCurrentLocationTypeId()));
		Collection cwnsUserLocatnAccessLevels = searchDAO.getSearchList(
				CwnsUserLocatnAccessLevel.class, scs2);
		cwnsUserLocatnAccessLevelDAO.deleteObjects(cwnsUserLocatnAccessLevels);

		// Flush and clear the Hibernate Session cache
		searchDAO.flushAndClearCache();

		// Get the CwnsUserLocation object 
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.cwnsUserId", SearchCondition.OPERATOR_EQ, udForm
						.getCwnsUserId()));
		scs.setCondition(new SearchCondition("id.locationId",
				SearchCondition.OPERATOR_EQ, udForm.getCurrentLocId()));
		scs
				.setCondition(new SearchCondition("id.locationTypeId",
						SearchCondition.OPERATOR_EQ, udForm
								.getCurrentLocationTypeId()));
		Collection CwnsUserLocations = searchDAO.getSearchList(
				CwnsUserLocation.class, scs);
		Iterator culIter = CwnsUserLocations.iterator();
		CwnsUserLocation cwnsUserLocation = (CwnsUserLocation) culIter.next();

		// Create a collection of Access Level Ids
		String[] accessLevelpartIds = udForm.getAccessLevelIds();
		ArrayList accessLevelIds = new ArrayList();
		if(accessLevelpartIds!=null&&accessLevelpartIds.length>0)
		{
			for(int i = 0; i < accessLevelpartIds.length;i++)
			{
				accessLevelIds.add(accessLevelpartIds[i]);
			}
		}
		// All users get the View access privilege - add it now
		accessLevelIds.add("12");
	
		
		// Create a Collection of AccessLevelRef objects
		Collection newAccessLevelRefs = new ArrayList();
		if (accessLevelIds != null && accessLevelIds.size() > 0) {
			for (int i = 0; i < accessLevelIds.size(); i++) {
				Long accLevId = new Long(Long.parseLong((String)accessLevelIds.get(i)));
				SearchConditions scs3 = new SearchConditions(
						new SearchCondition("accessLevelId",
								SearchCondition.OPERATOR_EQ, accLevId));
				Collection accessLevelRefs = searchDAO.getSearchList(
						AccessLevelRef.class, scs3);
				Iterator accessLevelRefsIter = accessLevelRefs.iterator();
				AccessLevelRef accessLevelRef = (AccessLevelRef) accessLevelRefsIter
						.next();
				newAccessLevelRefs.add(accessLevelRef);
			}

			// Create a Collection of CwnsUserLocatnAccessLevel objects 
			Set newCwnsUserLocatnAccessLevels = new HashSet();
			Iterator nalrs = newAccessLevelRefs.iterator();
			while (nalrs.hasNext()) {
				AccessLevelRef alf = (AccessLevelRef) nalrs.next();

				CwnsUserLocatnAccessLevelId id = new CwnsUserLocatnAccessLevelId();
				id.setAccessLevelId(alf.getAccessLevelId());
				id.setCwnsUserId(udForm.getCwnsUserId());
				id.setLocationId(cwnsUserLocation.getId().getLocationId());
				id.setLocationTypeId(cwnsUserLocation.getId()
						.getLocationTypeId());

				CwnsUserLocatnAccessLevel newCulal = new CwnsUserLocatnAccessLevel();
				newCulal.setAccessLevelRef(alf);
				newCulal.setCwnsUserLocation(cwnsUserLocation);
				newCulal.setLastUpdateTs(new Date());
				newCulal.setLastUpdateUserid(adminUserId);
				newCulal.setId(id);

				newCwnsUserLocatnAccessLevels.add(newCulal);

				// save the CwnsUserLocatnAccessLevel object
				cwnsUserLocatnAccessLevelDAO.saveObject(newCulal);
			}
			
		}

		// Save the CwnsUserLocation object
		cwnsUserLocationDAO.saveObject(cwnsUserLocation);

	}

	public String formatPhoneNumber(String phone) {

		String nPhone = ""; // Phone # with non-numerics removed

		if (phone == null) {
			// Nothing to do
			return phone;
		}

		phone = phone.trim();

		// strip all non numeric characters

		for (int i = 0; i < phone.length(); i++) {
			char ch = phone.charAt(i);
			if (Character.isDigit(ch)) {
				nPhone += Character.toString(ch);
			}
		}

		if (nPhone.length() != 10) {
			// Invalid length, don't even try to format
			return phone;
		}

		// reformat
		String areaCode = nPhone.substring(0, 3);
		String exch = nPhone.substring(3, 6);
		String num = nPhone.substring(6, 10);

		return "(" + areaCode + ") " + exch + "-" + num;
	}

	/**
	 * Return true if the selected role is the user's primary role and false
	 * otherwise.
	 * 
	 * @param udForm
	 * @return
	 */
	public boolean isPrimaryRole(UserDetailsForm udForm) {
		boolean primary = false;

		// Get the CwnsUserLocation object
		CwnsUserLocation cwnsUserLocation = getCwnsUserLocation(udForm
				.getCwnsUserId(), udForm.getSelectedRoleLocTypeId(), udForm
				.getSelectedRoleLocId());

		if (cwnsUserLocation != null
				&& cwnsUserLocation.getPrimaryFlag() == 'Y') {
			primary = true;
		}

		return primary;
	}

	// Delete a user Role
	public void deleteRole(UserDetailsForm udForm) {

		// Get the CwnsUserLocation object
		CwnsUserLocation cwnsUserLocation = getCwnsUserLocation(udForm
				.getCwnsUserId(), udForm.getSelectedRoleLocTypeId(), udForm
				.getSelectedRoleLocId());

		// Obtain the CwnsUserLocatnAccessLevel objects
		SearchConditions scs3 = new SearchConditions(new SearchCondition(
				"id.locationId", SearchCondition.OPERATOR_EQ, cwnsUserLocation
						.getId().getLocationId()));
		scs3.setCondition(new SearchCondition("id.locationTypeId",
				SearchCondition.OPERATOR_EQ, cwnsUserLocation.getId()
						.getLocationTypeId()));
		scs3.setCondition(new SearchCondition("id.cwnsUserId",
				SearchCondition.OPERATOR_EQ, udForm.getCwnsUserId()));
		Collection cwnsUserLocatnAccessLevels = searchDAO.getSearchList(
				CwnsUserLocatnAccessLevel.class, scs3);

		// Delete the CwnsUserLocatnAccessLevel objects
		cwnsUserLocatnAccessLevelDAO.deleteObjects(cwnsUserLocatnAccessLevels);

		// Delete the CwnsUserLocationFacility
		SearchConditions scs4 = new SearchConditions(new SearchCondition(
				"id.locationId", SearchCondition.OPERATOR_EQ, cwnsUserLocation
						.getId().getLocationId()));
		scs4.setCondition(new SearchCondition("id.locationTypeId",
				SearchCondition.OPERATOR_EQ, cwnsUserLocation.getId()
						.getLocationTypeId()));
		scs4.setCondition(new SearchCondition("id.cwnsUserId",
				SearchCondition.OPERATOR_EQ, udForm.getCwnsUserId()));
		Collection cwnsUserLocationFacilities = searchDAO.getSearchList(
				CwnsUserLocationFacility.class, scs4);
		searchDAO.deleteAll(cwnsUserLocationFacilities);

		// Delete the CwnsUserLocation object
		cwnsUserLocationDAO.deleteObject(cwnsUserLocation);
	}

	/**
	 * Given a portalUserId return the corresponding Portal User object.
	 * 
	 * @param oidUserName
	 * @return The Portal User object if found; null otherwise.
	 */
	public User regGetPortalUserObject(String oidUserName) {

		User user = null;
		String token = null;
		UserMgr userMgr = null;

		try {

			// Get the IAM locator object
			IAMLocator iamLocator = regGetIAMLocator();
			token = regGetAuthToken(iamLocator);
			userMgr = iamLocator.getUserManager();

		} catch (MalformedURLException me) {
			log
					.error(
							"Malformed URL Exception while obtaining Portal User object",
							me);
			throw new ApplicationException(me);
		} catch (ServiceException se) {
			log.error("Service Exception while obtaining Portal User object",
					se);
			throw new ApplicationException(se);
		}

		try {
			// Get the Portal User ojbect
			user = userMgr.getUser(token, oidUserName);

		} catch (RemoteException re) {
			log.error("Remote Exception in call userMgr.getUser()");
			throw new ApplicationException(re);
		}

		return user;
	}

	public IAMLocator regGetIAMLocator() throws MalformedURLException {

		// Get the web services end point
		String webServicesEndPoint = CWNSProperties
				.getProperty("iam.webservices.endpoint");
		// Return the IAM Locator
		return IAMLocator.getInstance(webServicesEndPoint);
	}

	/**
	 * Obtain a security token to use in IAM requests.
	 * 
	 * @param iamLocator
	 * @return
	 */
	private String regGetAuthToken(IAMLocator iamLocator) {
		String token = "";
		try {
			AuthMgr authMgr = iamLocator.getAuthMgr();

			// Get the username/password to use with IAM
			String adminPassword = CWNSProperties
					.getProperty("iam.webservices.password");
			String adminUsername = CWNSProperties
					.getProperty("iam.webservices.username");

			// Obtain an Authorization token
			token = authMgr.authenticate(adminUsername, adminPassword,
					AuthMethod.password);
		} catch (ServiceException se) {
			log
					.error(
							"Service Exception while obtaining IAM Authorization Token",
							se);
			throw new ApplicationException(se);
		} catch (RemoteException re) {
			log.error(
					"Remote Exception while obtaining IAM Authorization Token",
					re);
			throw new ApplicationException(re);
		}
		return token;
	}

	/**
	 * Determines if the user name passed in exists in the CWNS_USER.OID_USERID
	 * column.
	 * 
	 * @param oidUserName
	 * @return True if the user exists, false otherwise.
	 */
	public boolean portalUserExistsInCwns(String oidUserName) {
		boolean found = false;

		SearchConditions scs = new SearchConditions(new SearchCondition(
				"oidUserid", SearchCondition.OPERATOR_EQ, oidUserName));
		Collection cwnsUsers = searchDAO.getSearchList(CwnsUser.class, scs);
		if (cwnsUsers.size() > 0) {
			found = true;
		}
		return found;
	}

	/**
	 * Generate a new CWNS UserId.
	 * 
	 * @return A String representation of the next value in the CWNS_USER_SEQ
	 *         sequence.
	 */
	private String generateNewCwnsUserId() {
		BigDecimal seq = (BigDecimal) searchDAO.getDAOSession().createSQLQuery(
				"select cwns_user_seq.nextval from dual").list().get(0);
		return seq.toString();
	}

	/**
	 * Given a Portal User object create, and persist, a new CwnsUser object.
	 * 
	 * @param user
	 *            Portal User object
	 * @param adminUserId
	 *            The administrator's userId
	 * @return The new CwnsUser object
	 */
	public CwnsUser regCreateCWNSUserFromPortalProfile(User user,
			String adminUserId) {
		// Get user information from the Portal
		CwnsUser cwnsUser = null;
		try {
			cwnsUser = regGetCWNSDataFromPortal(user);

		} catch (Exception e) {
			return cwnsUser;
		}

		cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("P"));
		cwnsUser.setCwnsUserId(generateNewCwnsUserId());

		saveCwnsUser(cwnsUser, adminUserId);
		return cwnsUser;
	}

	// public void regUpdateComments(CwnsUser cwnsUser,IAMResponse iamResponse)
	// {
	// cwnsUser.setDescription("Initial Password: "+iamResponse.getPassword());
	// searchDAO.saveObject(cwnsUser);
	// }

	/**
	 * Create, and persist, a CWNS_USER object from the information found in the
	 * RegistrationForm. By default give the user a view access level for the
	 * user type and user location they entered.
	 * 
	 * @param rForm
	 *            Registration form bean
	 * @return The new CwnsUser object
	 */
	public CwnsUser regCreateCwnsUserFromForm(RegistrationForm rForm,
			String currentUserId) {

		// Create the CwnsUser object.
		CwnsUser cwnsUser = new CwnsUser();
		cwnsUser.setEmailAddress(rForm.getEmailAddress());
		cwnsUser.setFirstName(rForm.getFirstName());
		cwnsUser.setLastName(rForm.getLastName());
		cwnsUser.setPhone(rForm.getPhone());
		cwnsUser.setOidUserid(rForm.getOidUserId());
		cwnsUser.setDescription(rForm.getComments());
		cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("P"));
		cwnsUser.setPortalRequestId(rForm.getRequestId());
		//new fields
		cwnsUser.setAffiliation(rForm.getAffiliation());
		cwnsUser.setTitle(rForm.getTitle());
		cwnsUser.setFacilityList(rForm.getFacilityList());
		cwnsUser.setTownAndCountyList(rForm.getTownAndCountyList());

		cwnsUser.setCwnsUserId(generateNewCwnsUserId());
		saveCwnsUser(cwnsUser, currentUserId);

		// Obtain the RoleRef object
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.locationId", SearchCondition.OPERATOR_EQ, rForm
						.getFacilityStateId()));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition(
				"id.locationTypeId", SearchCondition.OPERATOR_EQ, rForm
						.getUserTypeId()));
		RoleRef roleRef = (RoleRef) searchDAO.getSearchObject(RoleRef.class,
				scs);

		// Create the CwnsUserLocation object
		CwnsUserLocation cwnsUserLocation = new CwnsUserLocation();
		cwnsUserLocation.setCwnsUser(cwnsUser);
		cwnsUserLocation.setRoleRef(roleRef);
		cwnsUserLocation.setPrimaryFlag('Y');
		//if local set limited=Y or limited=N
		if(UserService.LOCATION_TYPE_ID_LOCAL.equals(rForm.getUserTypeId())){
		   cwnsUserLocation.setLimitedFacilitiesFlag('Y');
		}else{
		   cwnsUserLocation.setLimitedFacilitiesFlag('N');
		}  
		cwnsUserLocation.setLastUpdateTs(new Date());
		cwnsUserLocation.setLastUpdateUserid(currentUserId);

		// Create the CwnsUserLocationId object
		CwnsUserLocationId cwnsUserLocationId = new CwnsUserLocationId(cwnsUser
				.getCwnsUserId(), rForm.getUserTypeId(), rForm.getFacilityStateId());
		cwnsUserLocation.setId(cwnsUserLocationId);

		// Save the CwnsUserLocation object
		cwnsUserLocationDAO.save(cwnsUserLocation);

		/*
		// Obtain the "View" AccessLevelRef
		scs = new SearchConditions();
		scs.setCondition(new SearchCondition("name",
				SearchCondition.OPERATOR_EQ, "View"));
		AccessLevelRef alr = (AccessLevelRef) searchDAO.getSearchObject(
				AccessLevelRef.class, scs);
	

		// Create the CwnsUserLocatnAccessLevel object
		CwnsUserLocatnAccessLevel ulAccessLevel = new CwnsUserLocatnAccessLevel();
		ulAccessLevel.setCwnsUserLocation(cwnsUserLocation);
		ulAccessLevel.setLastUpdateTs(new Date());
		ulAccessLevel.setLastUpdateUserid(currentUserId);

		// Create the CwnsUserLocatnAccessLevelId object
		CwnsUserLocatnAccessLevelId id = new CwnsUserLocatnAccessLevelId();
		id.setCwnsUserId(cwnsUser.getCwnsUserId());
		id.setAccessLevelId(alr.getAccessLevelId());
		id.setLocationId(cwnsUserLocation.getId().getLocationId());
		id.setLocationTypeId(cwnsUserLocation.getId().getLocationTypeId());
		ulAccessLevel.setId(id);
		*/
		CwnsUserLocatnAccessLevel ulAccessLevel = createCwnsUserLocatnAccessLevel(CurrentUser.VIEW, cwnsUserLocation, currentUserId);		
		// save the CwnsUserLocatnAccessLevel object
		cwnsUserLocatnAccessLevelDAO.save(ulAccessLevel);
		
		//if the user Type is local then check
		//if the user has requested for any of the access previlages then add them
		if(UserService.LOCATION_TYPE_ID_LOCAL.equals(rForm.getUserTypeId())){

			if(rForm.getSurveyFeedback()!=null && rForm.getSurveyFeedback().booleanValue()){
				//add and save survey feedback access level
				ulAccessLevel = createCwnsUserLocatnAccessLevel(CurrentUser.FACILITY_FEEDBACK, cwnsUserLocation, currentUserId);
				cwnsUserLocatnAccessLevelDAO.save(ulAccessLevel);
			}
			
			if(rForm.getUtilFeedback()!=null && rForm.getUtilFeedback().booleanValue()){
				//add partial feed back access level  FACILITY_UPDATE_PARTIAL
				ulAccessLevel = createCwnsUserLocatnAccessLevel(CurrentUser.FACILITY_UPDATE_PARTIAL, cwnsUserLocation, currentUserId);
				cwnsUserLocatnAccessLevelDAO.save(ulAccessLevel);
			}
			
			if(rForm.getUserManagement()!=null && rForm.getUserManagement().booleanValue()){
				//add user management role
				ulAccessLevel = createCwnsUserLocatnAccessLevel(CurrentUser.LOCAL_USER_MANAGEMENT, cwnsUserLocation, currentUserId);
				cwnsUserLocatnAccessLevelDAO.save(ulAccessLevel);
			}			
		}
		
		// Store the CWNS userId in the form
		rForm.setCwnsUserId(cwnsUser.getCwnsUserId());
		return cwnsUser;
	}
	
	
	private CwnsUserLocatnAccessLevel createCwnsUserLocatnAccessLevel(Long accessLevelId, CwnsUserLocation cwnsUserLocation, String currentUserId){
		CwnsUserLocatnAccessLevel ulAccessLevel = new CwnsUserLocatnAccessLevel();
		
		ulAccessLevel.setCwnsUserLocation(cwnsUserLocation);
		ulAccessLevel.setLastUpdateTs(new Date());
		ulAccessLevel.setLastUpdateUserid(currentUserId);

		// Create the CwnsUserLocatnAccessLevelId object
		CwnsUserLocatnAccessLevelId id = new CwnsUserLocatnAccessLevelId();
		
		id.setCwnsUserId(cwnsUserLocation.getId().getCwnsUserId());
		id.setAccessLevelId(accessLevelId.longValue());
		id.setLocationId(cwnsUserLocation.getId().getLocationId());
		id.setLocationTypeId(cwnsUserLocation.getId().getLocationTypeId());
		ulAccessLevel.setId(id);
		return ulAccessLevel;		
	}

	/**
	 * Given a CWNS userId and portal group, add the user to the CWNS Portal Community group
	 * 
	 * @param portalUserId
	 * @param portalGroup
	 * @return IAMResponse object
	 */
	public IAMResponse regAddUserToGroup(String cwnsUserId, String portalGroup) {
		
		IAMResponse iamResponse = new IAMResponse();
		Boolean no = new Boolean(false);

		gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Request request = new gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Request();
		gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Response response;
		gov.epa.iamfw.webservices.common.Error errors[];

		// Initialize the Notify object
		gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Notify notify = new gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Notify(
				no, "", no, "");

		//String groupCN = CWNSProperties.getProperty("cwns.groupName");
		
		try {
			IAMLocator iamLocator = regGetIAMLocator();
			String token = regGetAuthToken(iamLocator);
			
//			 Initialize the request object
			
			CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
			request.setGroupName(portalGroup);
			request.setToken(token);
			request.setUserId(cwnsUser.getOidUserid());
			request.setNotify(notify);


			DelegatedAdministrationService delegatedAdministrationService = iamLocator
					.getDelegatedAdministrationService();

			response = delegatedAdministrationService.addGroupMember(request);
			
			if (response.getErrors() == null) {
				iamResponse.setSuccess(true);
			} else {
				// Some type of error occurred
				errors = response.getErrors();
				for (int i = 0; i < errors.length; i++) {
					iamResponse.getMessages().add(errors[i].getErrorMessage());
					
				}

				// If the error is just that the user is already part of
				// the group return set success to true.
				if (regIsUserAlreadyPartOfGroup(iamResponse)) {
					iamResponse.setSuccess(true);
				}
			}
		} catch (Exception e) {
			new ApplicationException(
					"Exception Thrown trying to add a user to the group: ", e);
		}

		return iamResponse;

	}
	
	/**
	 * Given a CWNS userId and portal group, remove the user from the CWNS Portal Community group
	 * 
	 * @param portalUserId
	 * @param portalGroup
	 * @return IAMResponse object
	 */
	public IAMResponse regRemoveUserFromGroup(String cwnsUserId, String portalGroup) {
		
		IAMResponse iamResponse = new IAMResponse();
		Boolean no = new Boolean(false);

		gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Request request = new gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Request();
		gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Response response;
		gov.epa.iamfw.webservices.common.Error errors[];

		// Initialize the Notify object
		gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Notify notify = new gov.epa.iamfw.webservices.delegatedadministration.groupmember.localschema.Notify(
				no, "", no, "");

		//String groupCN = CWNSProperties.getProperty("cwns.groupName");

		try {
			IAMLocator iamLocator = regGetIAMLocator();
			String token = regGetAuthToken(iamLocator);
			
			
			
			// Initialize the request object
			CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
			request.setGroupName(portalGroup);
			request.setToken(token);
			request.setUserId(cwnsUser.getOidUserid());
			request.setNotify(notify);
			
			DelegatedAdministrationService delegatedAdministrationService = iamLocator
					.getDelegatedAdministrationService();

			response = delegatedAdministrationService.removeGroupMember(request);
			
			if (response.getErrors() == null) {
				iamResponse.setSuccess(true);
			} else {
				// Some type of error occurred
				errors = response.getErrors();
				for (int i = 0; i < errors.length; i++) {
					iamResponse.getMessages().add(errors[i].getErrorMessage());
				}

				// If the error is just that the user is already part of
				// the group return set success to true.
				if (regIsUserAlreadyPartOfGroup(iamResponse)) {
					iamResponse.setSuccess(true);
				}
			}
		} catch (Exception e) {
			new ApplicationException(
					"Exception Thrown trying to remove a user from the group: ", e);
		}

		return iamResponse;

	}

	/**
	 * Examine the IAMResponse object for errors. If the only error is the user
	 * is already part of the Portal group return true. If any other error is
	 * found return false.
	 * 
	 * @param iamResponse
	 * @return true if the only error found in the IAM Response object is that
	 *         the user is already part of the group. If any other error is
	 *         found return false.
	 */
	private boolean regIsUserAlreadyPartOfGroup(IAMResponse iamResponse) {

		boolean error = true;
		String groupMemberMsg = CWNSProperties
				.getProperty("iam.webservices.already_group_member");

		Iterator iter = iamResponse.getMessages().iterator();
		while (iter.hasNext()) {
			String msg = (String) iter.next();
			if (msg.indexOf(groupMemberMsg) < 0) {
				error = false;
			}
		}
		return error;
	}

	/**
	 * Approve the request to add the portal user to the group.
	 * 
	 * @param portalUserId
	 * @return
	 */
	/*
	 * private IAMResponse regApproveUserToGroup(String portalUserId) {
	 * IAMResponse iamResponse = new IAMResponse();
	 * iamResponse.setSuccess(false);
	 * 
	 * 
	 * return iamResponse;
	 *  }
	 */
	/**
	 * Retrieve Portal information for this user and initialize the cwnsUser
	 * object.
	 * 
	 * @param user
	 *            Portal User object
	 * @return CwnsUser object initialized with Portal Profile information.
	 */
	private CwnsUser regGetCWNSDataFromPortal(User user)
			throws MalformedURLException {

		// Get ContactInfo and AccountInfo objects
		ContactInfo contactInfo = user.getContactInfo();
		AccountInfo accountInfo = user.getAccountInfo();

		// Initialize the CwnsUser object.
		CwnsUser cwnsUser = new CwnsUser();
		cwnsUser.setEmailAddress(contactInfo.getMail());
		cwnsUser.setFirstName(accountInfo.getFirstName()[0]);
		cwnsUser.setLastName(accountInfo.getLastName()[0]);
		cwnsUser.setPhone(contactInfo.getTelephoneNumber());
		cwnsUser.setOidUserid(user.getUserId());
		cwnsUser.setDescription("");

		return cwnsUser;
	}

	/**
	 * Given a completed RegistrationForm bean request a Portal User account. If
	 * the Portal account request was successfull return an IAMResponse object
	 * that contains the Portal Request ID. If the Portal account was not
	 * created successfully return null and generate a log a message.
	 */
	public ActionErrors regRequestNewPortalAccount(RegistrationForm rForm) {
		ActionErrors errors = new ActionErrors();
		Boolean no = new Boolean(false);

		gov.epa.iamfw.webservices.selfservice.register.localschema.Response response;
		gov.epa.iamfw.webservices.selfservice.register.localschema.Confirmation confirmation;
		gov.epa.iamfw.webservices.selfservice.register.localschema.Notify notify = new gov.epa.iamfw.webservices.selfservice.register.localschema.Notify(
				no, "", no, "", no, "");

		// Initialize the Basic User Profile
		gov.epa.iamfw.webservices.selfservice.register.localschema.BasicUserProfile basicUserProfile = new gov.epa.iamfw.webservices.selfservice.register.localschema.BasicUserProfile();
		basicUserProfile.setAcceptEpaPrivacyStatment(new Boolean(true));

		basicUserProfile.setCity(rForm.getCity().trim());
		basicUserProfile.setEmailAddress(rForm.getEmailAddress().trim());

		String epaContactEmail = CWNSProperties
				.getProperty("iam.webservices.epa.contact.email");
		basicUserProfile.setEpaContactEmail(epaContactEmail);

		basicUserProfile.setEpaUserPrimaryCommunity("cwns");
		basicUserProfile.setFirstName(rForm.getFirstName().trim());
		basicUserProfile.setLastName(rForm.getLastName().trim());
		basicUserProfile.setPostalCode(rForm.getZip().trim());
		basicUserProfile.setState(rForm.getStateId());
		basicUserProfile.setCountry("US");
		basicUserProfile.setStreetAddess(rForm.getStreet().trim());
		String phone = formatPhoneNumber(rForm.getPhone().trim());
		basicUserProfile.setTelephoneNumber(phone);

		// Initialize the Request object

		gov.epa.iamfw.webservices.selfservice.register.localschema.Request request = new gov.epa.iamfw.webservices.selfservice.register.localschema.Request(
				new Boolean(true), notify, basicUserProfile);
		try {
			// Send the request
			IAMLocator iamLocator = regGetIAMLocator();
			SelfService selfService = iamLocator.getSelfService();
			response = selfService.register(request);
			confirmation = response.getConfirmation();
			if (confirmation != null) {
				String portalRequestId = confirmation
						.getRegisteredPendingUser().getRequestId();
				// update the CWNS_USER record with the Portal RequestId
				/*if (rForm.getCwnsUserId().length() > 0) {
					CwnsUser cwnsUser = findUserByUserId(rForm.getCwnsUserId());
					if (cwnsUser != null) {
						cwnsUser.setPortalRequestId(portalRequestId);
					}
				}*/
				rForm.setRequestId(portalRequestId);
			} else {
				// Something went wrong, generate an error message.
				for (int i = 0; i < response.getErrors().length; i++) {
					ActionError error = new ActionError(
							"error.registrationportal.requestaccount", response
									.getErrors()[i].getErrorMessage());
					errors.add(ActionErrors.GLOBAL_ERROR, error);
				}
			}
		} catch (Exception e) {
			log.error("Error trying to create a Portal account.", e);
			throw new ApplicationException(e);
		}
		return errors;
	}
	
	/**
	 * The user successfully registered for CWNS. Determine the registration type, update the cwns user
	 * to Pending and update the user obj description field
	 */
	public void regSuccessfulRegistration(RegistrationForm rForm, String currentUserId, HttpServletRequest request) {
		CwnsUser cwnsUser = findUserByUserId(rForm.getCwnsUserId());
				
		if (isPortalRegistration(request)) {
			cwnsUser.setRegType(PORTAL_REGISTRATION);
		} else if (isPublicUser(request)) {
			cwnsUser.setRegType(SELF_SERVICE_REGISTRATION);
		} else {
			cwnsUser.setRegType(ADMINISTRATOR_MANAGED_REGISTRATION);
		}
		
		cwnsUser.setDescription(rForm.getComments());
		cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("P"));
		saveCwnsUser(cwnsUser, currentUserId);
	}
	
	public void regSuccessfulRegistration(RegistrationForm rForm, String currentUserId, String type){
		CwnsUser cwnsUser = findUserByUserId(rForm.getCwnsUserId());
		if(type.equalsIgnoreCase(GROUP_SUBSCRIPTION)){
			cwnsUser.setRegType(GROUP_SUBSCRIPTION);
		}else if(type.equalsIgnoreCase(ACCOUNT_UPDATE)){
			cwnsUser.setRegType(ACCOUNT_UPDATE);
		}else{
			cwnsUser.setRegType(SELF_SERVICE_REGISTRATION);
		}
		
		cwnsUser.setDescription(rForm.getComments());
		cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("P"));
		saveCwnsUser(cwnsUser, currentUserId);
	}

	/**
	 * Given and array of IAM Error objects take the error messages and copy
	 * them to a IAMResponse object.
	 * 
	 * @param errors
	 * @return
	 */
	private IAMResponse regProcessIAMErrors(Error[] errors) {
		IAMResponse iamResponse = new IAMResponse();
		for (int i = 0; i < errors.length; i++) {
			iamResponse.getMessages().add(errors[i].getErrorMessage());
		}
		iamResponse.setSuccess(false);
		return iamResponse;
	}

	/**
	 * Approve a request for a Portal account.
	 */
	public IAMResponse regApprovePortalAccount(String requestId) {
		return regDispositionPortalAccountRequest(requestId, true);
	}

	/**
	 * Deny a request for a Portal account.
	 */
	public IAMResponse regDenyPortalAccount(String requestId) {
		return regDispositionPortalAccountRequest(requestId, false);
	}

	/**
	 * Approve or deny a request for a Portal account.
	 */
	public IAMResponse regDispositionPortalAccountRequest(String requestId,
			boolean approve) {
		System.out.println("Disposition " + requestId + " " + approve);
		Boolean no = new Boolean(false);
		Boolean yes = new Boolean(true);
		IAMResponse iamResponse = new IAMResponse();
		iamResponse.setSuccess(false);

		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Response pendingResponse;
		gov.epa.iamfw.webservices.delegatedadministration.processselfregistrationpendingrequests.localschema.Request request = new gov.epa.iamfw.webservices.delegatedadministration.processselfregistrationpendingrequests.localschema.Request();
		gov.epa.iamfw.webservices.delegatedadministration.processselfregistrationpendingrequests.localschema.Response response = null;
		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request pendingRequest = new gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request();
		gov.epa.iamfw.webservices.dataobjects.Subscriber[] subscribers;
		gov.epa.iamfw.webservices.dataobjects.Subscriber theSubscriber = null;

		// Initialize the Notify object
		gov.epa.iamfw.webservices.delegatedadministration.processselfregistrationpendingrequests.localschema.Notify notify = new gov.epa.iamfw.webservices.delegatedadministration.processselfregistrationpendingrequests.localschema.Notify(
				no, "");

		String token;

		try {
			// Get the IAM locator object and Auth Token
			IAMLocator iamLocator = regGetIAMLocator();
			token = regGetAuthToken(iamLocator);

			// Approve the user
			request.setRequestId(requestId);
			request.setNotify(notify);
			request.setToken(token);

			DelegatedAdministrationService delegatedAdministrationService = iamLocator
					.getDelegatedAdministrationService();
			if (approve) {
				// Approve the request
				response = delegatedAdministrationService
						.approveSelfRegistrationPendingRequests(request);
			} else {
				// Deny the request
				response = delegatedAdministrationService
						.rejectSelfRegistrationPendingRequests(request);
			}

			if (response.getErrors() != null) {
				// An error occurred
				return regProcessIAMErrors(response.getErrors());
			}

			if (approve) {
				String portalUserId = response.getConfirmation().getUserId();
				String password = response.getConfirmation().getPassword();
				iamResponse.setPortalUserId(portalUserId);
				iamResponse.setPassword(password);
			}

		} catch (Exception e) {
			throw new ApplicationException("Error in approving Portal user", e);
		}

		iamResponse.setSuccess(true);
		return iamResponse;
	}

	/**
	 * Get the list of pending Portal account requests
	 * 
	 * @return
	 */
	public IAMResponse regGetPendingPortalAccounts() {

		Boolean no = new Boolean(false);
		Boolean yes = new Boolean(true);
		IAMResponse iamResponse = new IAMResponse();
		iamResponse.setSuccess(false);

		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Response pendingResponse;
		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request pendingRequest = new gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request();
		gov.epa.iamfw.webservices.dataobjects.Subscriber[] subscribers;
		gov.epa.iamfw.webservices.dataobjects.Subscriber theSubscriber = null;

		// Initialize the Notify object
		gov.epa.iamfw.webservices.delegatedadministration.processselfregistrationpendingrequests.localschema.Notify notify = new gov.epa.iamfw.webservices.delegatedadministration.processselfregistrationpendingrequests.localschema.Notify(
				no, "");

		String token;

		try {
			// Get the IAM locator object and Auth Token
			IAMLocator iamLocator = regGetIAMLocator();
			token = regGetAuthToken(iamLocator);

			// Initialize the Request object used to obtain the pending requests
			pendingRequest.setGroupName("cwns");
			pendingRequest.setNoOfRequests(new Integer(0));
			pendingRequest.setOffSet(new Integer(0));
			pendingRequest.setToken(token);

			// Get a list of the pending requests
			DelegatedAdministrationService delegatedAdministrationService = iamLocator
					.getDelegatedAdministrationService();
			pendingResponse = delegatedAdministrationService
					.getSelfRegistrationPendingRequests(pendingRequest);

			if (pendingResponse.getErrors() != null) {
				return regProcessIAMErrors(pendingResponse.getErrors());
			}

			// get the subscriber list
			subscribers = pendingResponse.getSubscriberList();

			// Loop through the subscriber list and locate the
			// subscriber object associated with the request ID.

			// Get from the subscriber list all the info necessary to display in
			// the portal and
			// to approve or deny the user.

			// for (int i = 0; i < subscribers.length; i++) {
			// if (requestId.equals(subscribers[i].getRequestId())) {
			// theSubscriber = subscribers[i];
			// }
			// }

		} catch (Exception e) {
			throw new ApplicationException("Error in approving Portal user", e);
		}

		iamResponse.setSuccess(true);
		return iamResponse;
	}
	
	
	
	/**
	 * Filter the Collection passed in so only Access Levels that are applicable
	 * to the locationTypeId passed in.
	 * 
	 * @param userAccessLevels
	 * @param locationTypeId
	 * @return
	 */
	public Collection filterAccessLevelsRefs(Collection userAccessLevels,
			String locationTypeId) {

		Collection results = new ArrayList();

		// Get a Collection of Access Levels base on the locationTypeId
		Collection locationTypeAccessLevels = getAccessLevelRefs(locationTypeId);

		// Find the intersection of the two collections
		Iterator ualIter = userAccessLevels.iterator();
		while (ualIter.hasNext()) {
			AccessLevelRef ual = (AccessLevelRef) ualIter.next();
			Iterator ltalIter = locationTypeAccessLevels.iterator();
			while (ltalIter.hasNext()) {
				AccessLevelRef ltal = (AccessLevelRef) ltalIter.next();
				if (ual.getAccessLevelId() == ltal.getAccessLevelId()) {
					results.add(ual);
				}
			}
		}

		return results;
	}

	public Collection filterLocationRefs(Collection locationRefs,
			String locationTypeId) {

		Collection results = new ArrayList();

		// Get a Collection of LocationRefs base on the locationTypeId
		Collection filteredLocationRefs = getLocationRefs(locationTypeId);

		// Find the intersection of the two collections
		Iterator flIter = filteredLocationRefs.iterator();
		while (flIter.hasNext()) {
			LocationRef filteredLocationRef = (LocationRef) flIter.next();
			Iterator lIter = locationRefs.iterator();
			while (lIter.hasNext()) {
				LocationRef locationRef = (LocationRef) lIter.next();
				if (locationRef.getLocationId().equals(
						filteredLocationRef.getLocationId())) {
					results.add(locationRef);
				}
			}
		}

		return results;

	}

	/**
	 * Logically delete the CwnsUser object by setting the status to Inactive.
	 * 
	 * @param rForm
	 * @param currentUser
	 */
	public void regDeleteUser(RegistrationForm rForm, String currentUser) {

		String cwnsUserId = rForm.getCwnsUserId();
		CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
		cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("I"));
		saveCwnsUser(cwnsUser, currentUser);
//		rForm.setCwnsUserId("");
	}

	/**
	 * Return the CwnsUserStatusRef object for the given Id.
	 * 
	 * @param cwnsUserStatusId
	 * @return
	 */
	public CwnsUserStatusRef getCwnsUserStatusRef(String cwnsUserStatusId) {
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"cwnsUserStatusId", SearchCondition.OPERATOR_EQ,
				cwnsUserStatusId));
		Collection cwnsUserStatusRefs = searchDAO.getSearchList(
				CwnsUserStatusRef.class, scs);
		if (cwnsUserStatusRefs.size() != 1) {
			throw new ApplicationException("" + cwnsUserStatusRefs.size()
					+ " CwnsUserStatusRefs found for cwnsUserStatusId = "
					+ cwnsUserStatusId);
		}
		Iterator cwnsUserStatusIter = cwnsUserStatusRefs.iterator();
		return (CwnsUserStatusRef) cwnsUserStatusIter.next();
	}

	
	/**
	 * Return true if the user is performing Self Registration via the Portal 
	 * 
	 * @param request
	 * @return
	 */
	public boolean isPortalRegistration(HttpServletRequest request) {
/*     TODO: what is the criteria ????		
		String publicUser = (String)request.getSession().getAttribute(RegistrationForm.PUBLIC_USER);
		if (publicUser != null &&	"yes".equalsIgnoreCase(publicUser)) {
			return true;
		}
*/		
		return false;
	}
	
	/**
	 * Return true if CWNS Custom Self Registration 
	 * 
	 * @param request
	 * @return
	 */
	public boolean isPublicUser(HttpServletRequest request) {

		String publicUser = (String) request.getSession().getAttribute(
														RegistrationForm.PUBLIC_USER);
		
		CurrentUser currentUser = (CurrentUser) request.getSession().getAttribute(CurrentUser.CWNS_USER); 
		
		if (publicUser != null && "yes".equalsIgnoreCase(publicUser)) {
			return true;
			
		} else if (currentUser == null) {
			// No user obj in the session so assume it is a public user
			return true;
		}
		return false;
	}

	/**
	 * Given a location Id and a location type Id return true if the location Id
	 * is valid for the given location type Id. If not, return false
	 * 
	 * @param userTypeId
	 * @param userLocationId
	 * @return
	 */
	public boolean isLocationValid(String locationTypeId, String locationId) {

		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.locationTypeId", SearchCondition.OPERATOR_EQ,
				locationTypeId));
		scs.setCondition(new SearchCondition("id.locationId",
				SearchCondition.OPERATOR_EQ, locationId));
		Collection roleRefs = searchDAO.getSearchList(RoleRef.class, scs);

		if (roleRefs.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Update the Registration form using the Portal registration request id.
	 * @param rForm
	 * @param portalRegId
	 * @return
	 */
	public ActionErrors regUpdateFormFromPortalRegistrationId	(RegistrationForm rForm, String portalRegId) {
		
        ActionErrors errors    = new ActionErrors();
        User portalUserObj = null;
        
		try {
//			portalUserObj = regGetPortalUserObject(oidUserId);
			//TODO: Get API Call from Tuan
    	} catch (Exception e) {
			ActionError error = new ActionError("error.userdetails.unabletoaccessiam");
			errors.add(ActionErrors.GLOBAL_ERROR,error);
    	}
    	if (portalUserObj == null) {
			ActionError error = new ActionError("error.userdetails.unabletoaccessiam");
			errors.add(ActionErrors.GLOBAL_ERROR,error);
    	} else {
    		// Populate the Registration form from the Portal Profile Obj

    		// Get ContactInfo and AccountInfo objects
    		ContactInfo contactInfo = portalUserObj.getContactInfo();
    		AccountInfo accountInfo = portalUserObj.getAccountInfo();
    		
    		rForm.setPortalRegId(portalRegId);

    		if (accountInfo.getFirstName() != null) {
        		rForm.setFirstName(accountInfo.getFirstName()[0]);
    		}
    		if (accountInfo.getLastName() != null) {
        		rForm.setLastName(accountInfo.getLastName()[0]);
    		}

    		rForm.setPhone(contactInfo.getTelephoneNumber());
    		rForm.setEmailAddress(contactInfo.getMail());
    		if (contactInfo.getStreet() != null ) {
    			rForm.setStreet(contactInfo.getStreet()[0]);
    		}
    		if (contactInfo.getCity() != null) {
    			rForm.setCity(contactInfo.getCity()[0]);
    		}
    		if (contactInfo.getState() != null) {
    			rForm.setStateId(contactInfo.getState()[0]);
    		}
    		if (contactInfo.getPostalCode() != null) {
    			rForm.setZip(contactInfo.getPostalCode()[0]);
    		}
			rForm.setPhone(contactInfo.getTelephoneNumber());
    	}				
    	return errors;
	}

	/**
	 * Update the RegistrationForm bean with data from the Portal User object.
	 * 
	 * @param rForm
	 * @param oidUserId
	 * @return
	 */
	public ActionErrors regUpdateFormWithPortalUserInfo(RegistrationForm rForm,
			String oidUserId) {

		ActionErrors errors = new ActionErrors();
		//User portalUserObj = null;
		gov.epa.iamfw.webservices.userservice.getuser.localschema.UserProfile portalUserObj = null;
		User userObj = null;
		
		try {
			
			portalUserObj = regSearchOidUser(oidUserId);
    	} catch (Exception e) {
			ActionError error = new ActionError("error.userdetails.unabletoaccessiam");
			errors.add(ActionErrors.GLOBAL_ERROR,error);
    	}
    	if (portalUserObj == null) {
			ActionError error = new ActionError("error.userdetails.unabletoaccessiam");
			errors.add(ActionErrors.GLOBAL_ERROR,error);
    	} else {
    		// Populate the Registration form from the Portal User Obj data

    		// Get ContactInfo and AccountInfo objects
    		//ContactInfo contactInfo = portalUserObj.getContactInfo();
    		//AccountInfo accountInfo = portalUserObj.getAccountInfo();
    		
    		
			// Initialize the CwnsUser object.
			rForm.setOidUserId(oidUserId);
			
			if (portalUserObj.getFirstName() != null) {
				rForm.setFirstName(portalUserObj.getFirstName()[0]);
			}
			if (portalUserObj.getLastName() != null) {
				rForm.setLastName(portalUserObj.getLastName()[0]);
			}

			if (portalUserObj.getTelephoneNumber() != null){
				rForm.setPhone(portalUserObj.getTelephoneNumber()[0]);
			}
			if	(portalUserObj.getEpaForwardEmailAddress() != null){
				rForm.setEmailAddress(portalUserObj.getEpaForwardEmailAddress()[0]);
			}
			if	(portalUserObj.getEpaForwardEmailAddress() == null){
				//Look for the mail address attribute
				userObj = regGetPortalUserObject(oidUserId);
				if(userObj.getContactInfo().getMail() != null){
					rForm.setEmailAddress(userObj.getContactInfo().getMail());
				}
			}
			if (portalUserObj.getStreetAddress() != null) {
				rForm.setStreet(portalUserObj.getStreetAddress()[0]);
			}
			if (portalUserObj.getCity() != null) {
				rForm.setCity(portalUserObj.getCity()[0]);
			}
			if (portalUserObj.getState() != null) {
				rForm.setStateId(portalUserObj.getState()[0]);
			}
			if (portalUserObj.getPostalCode() != null) {
				rForm.setZip(portalUserObj.getPostalCode()[0]);
			}
			
		}
		return errors;
	}

	/**
	 * Update the RegistrationForm based on CwnsUser information.
	 */

	public ActionErrors regUpdateFormWithCwnsUserInfo(RegistrationForm rForm,
			String cwnsUserId) {
		ActionErrors errors = new ActionErrors();
		CwnsUser cwnsUser = findUserByUserId(cwnsUserId);

		if (cwnsUser != null) {
			rForm.setComments(cwnsUser.getDescription());
			rForm.setCwnsUserId(cwnsUser.getCwnsUserId());
			rForm.setEmailAddress(cwnsUser.getEmailAddress());
			rForm.setFirstName(cwnsUser.getFirstName());
			rForm.setLastName(cwnsUser.getLastName());
			rForm.setOidUserId(cwnsUser.getOidUserid());
			rForm.setPhone(formatPhoneNumber(cwnsUser.getPhone()));
		} else {
			ActionError error = new ActionError(
					"error.registration.cwnsUserNotFound");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
		}
		return errors;
	}

	/**
	 * Approve the user's account request and change the CwnsUser status to
	 * active.
	 * 
	 * @param cwnsUserId
	 * @param currentUserId
	 * @return
	 */
	public ActionErrors regApprovePortalUserAccount(String cwnsUserId,
			String currentUserId) {

		ActionErrors errors = new ActionErrors();
		CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
		String iamRequestId = cwnsUser.getPortalRequestId();

		if (cwnsUser.getPortalRequestId() != null
				|| cwnsUser.getPortalRequestId().length() > 0) {

			IAMResponse iamResponse = regApprovePortalAccount(iamRequestId);

			if (iamResponse.isSuccess()) {
				cwnsUser.setInitialPassword(iamResponse.getPassword());
				cwnsUser.setOidUserid(iamResponse.getPortalUserId());
				cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("A"));
			} else {
				errors = processErrors(iamResponse, errors);
			}
		}

		// Update the CwnsUser object
		saveCwnsUser(cwnsUser, currentUserId);

		return errors;
	}

	/**
	 * Deny the user's account request and update the CwnsUser user object.
	 * 
	 * @param cwnsUserId
	 * @param currentUserId
	 * @return
	 */
	public ActionErrors regDenyUserAccount(String cwnsUserId,
			String currentUserId) {

		ActionErrors errors = new ActionErrors();
		CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
		String iamRequestId = cwnsUser.getPortalRequestId();

		if (cwnsUser.getPortalRequestId() != null
				&& cwnsUser.getPortalRequestId().trim().length() > 0) {

			IAMResponse iamResponse = regDenyPortalAccount(iamRequestId);

			if (!iamResponse.isSuccess()) {
				errors = processErrors(iamResponse, errors);
			}
		}

		if (errors.isEmpty()) {
			// Update the CwnsUser object
			cwnsUser = findUserByUserId(cwnsUserId);
			String description = cwnsUser.getDescription()
					+ "CWNS Administrator " + currentUserId
					+ " denied this user access to CWNS";
			cwnsUser.setDescription(description);
			cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("I"));
			saveCwnsUser(cwnsUser, currentUserId);
		}

		return errors;
	}

	/**
	 * Return the number of pending users for this location and locationType.
	 * 
	 * @param currentUser
	 * @return The number of pending users
	 */
	public int regGetNumberOfPendingUsers(CurrentUser currentUser) {
		
		// Construct the hashmap
		Map queryProperties = new HashMap();

		// Location
		String locationId = currentUser.getCurrentRole().getLocationId();
		queryProperties.put("locationId", locationId);
		
		// Location Type
		String locationTypeId = currentUser.getCurrentRole()
				.getLocationTypeId();
		queryProperties.put("locationTypeId", locationTypeId);
		

		// Status
		queryProperties.put("userStatus", "P");
		
		
		if(locationTypeId.equalsIgnoreCase(LOCATION_TYPE_ID_STATE)){
			queryProperties.put("locationTypeId", "");
		}else if(locationTypeId.equalsIgnoreCase(LOCATION_TYPE_ID_FEDERAL)){
			queryProperties.put("locationId", "all");
			queryProperties.put("locationTypeId", "");
		}
		
		Search search = getUserSearch(currentUser, queryProperties, null, 0,
				50000);
		search.setStartIndex(0);
		
		/* Execute the search to obtain a list of Pening userIds */
		Collection pendingUserIds = getUserIds(search);

		return pendingUserIds.size();
	}
	
	public int adminGetNumberOfPendingUsers(CurrentUser currentUser){
		//System.out.println("---adminGetNumberOfPendingUsers---");
		// Construct the hashmap
		Map queryProperties = new HashMap();

		// Location
		String locationId = currentUser.getCurrentRole().getLocationId();
		queryProperties.put("locationId", locationId);
		
		// Location Type
		String locationTypeId = currentUser.getCurrentRole()
				.getLocationTypeId();
		queryProperties.put("locationTypeId", locationTypeId);
		

		// Status
		queryProperties.put("userStatus", "P");
		
		if(locationTypeId.equalsIgnoreCase(LOCATION_TYPE_ID_STATE)){
			queryProperties.put("locationTypeId", "");
		}else if(locationTypeId.equalsIgnoreCase(LOCATION_TYPE_ID_FEDERAL)){
			queryProperties.put("locationId", "all");
			queryProperties.put("locationTypeId", "");
		}
		
		System.out.println("locationId = " + (String)queryProperties.get("locationId"));
		System.out.println("locationTypeId = " + (String)queryProperties.get("locationTypeId"));
		
		Search search = getUserSearch(currentUser, queryProperties, null, 0,
				50000);
		search.setStartIndex(0);
		
		

		/* Execute the search to obtain a list of Pening userIds */
		Collection pendingUserIds = getUserIds(search);

		return pendingUserIds.size();
	}

	public Collection getUserLocationRestrictedFacilitiesId(Long facilityId,
			String cwnsUserId, String locationTypeId, String locationId) {
		ArrayList facilityIds = new ArrayList();
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.locationId", SearchCondition.OPERATOR_EQ, locationId));
		scs.setCondition(new SearchCondition("id.locationTypeId",
				SearchCondition.OPERATOR_EQ, locationTypeId));
		scs.setCondition(new SearchCondition("id.cwnsUserId",
				SearchCondition.OPERATOR_EQ, cwnsUserId));
		scs.setCondition(new SearchCondition("id.facilityId",
				SearchCondition.OPERATOR_EQ, facilityId));
		Collection cwnsUserLocations = searchDAO.getSearchList(
				CwnsUserLocationFacility.class, scs);
		for (Iterator iter = cwnsUserLocations.iterator(); iter.hasNext();) {
			CwnsUserLocationFacility utf = (CwnsUserLocationFacility) iter
					.next();
			facilityIds.add(new Long(utf.getId().getFacilityId()));
		}
		return facilityIds;
	}

	/**
	 * Update a CWNS user object with a Portal userId .
	 * 
	 * @param cwnsUserId
	 * @param portalUserId
	 * @param currentUser
	 */
	public void regUpdateCwnsObjWithPortalId(String cwnsUserId,
			String portalUserId, String currentUser) {

		CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
		cwnsUser.setOidUserid(portalUserId);
		cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("P"));
		saveCwnsUser(cwnsUser, currentUser);
	}


	/**
	 * Update a CWNS user object with data from the Registration Form.
	 * 
	 * @param cwnsUserId
	 * @param rForm
	 */
	public void regUpdateCwnsObjFromForm(String cwnsUserId,	RegistrationForm rForm, String currentUserId) {

		CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
		
		cwnsUser.setFirstName(rForm.getFirstName());
		cwnsUser.setLastName(rForm.getLastName());
		cwnsUser.setEmailAddress(rForm.getEmailAddress());
		cwnsUser.setPhone(rForm.getPhone());
		cwnsUser.setPortalRequestId(rForm.getRequestId());
		saveCwnsUser(cwnsUser, currentUserId);
	}
	
	/**
	 * Save the cwnsUser object.
	 * 
	 * @param cwnsUser
	 * @param userId
	 */
	public void saveCwnsUser(CwnsUser cwnsUser, String userId) {
		cwnsUser.setLastUpdateTs(new Date());
		cwnsUser.setLastUpdateUserid(userId);
		searchDAO.getDAOSession().saveOrUpdate(cwnsUser);
	}

	/**
	 * Convert IAMResponse messages to Action Errors
	 */
	public ActionErrors processErrors(IAMResponse portalResponseReq,
			ActionErrors errors) {
		Collection messages = portalResponseReq.getMessages();
		Iterator iter = messages.iterator();
		while (iter.hasNext()) {
			String message = (String) iter.next();
			ActionError error = new ActionError(
					"error.registrationportal.newuser", message);
			errors.add(ActionErrors.GLOBAL_ERROR, error);
		}
		return errors;
	}

	private static SearchDAO searchDAO;

	public void setSearchDAO(SearchDAO dao) {
		searchDAO = dao;
	}

	private CwnsUserDAO cwnsUserDAO;

	public void setCwnsUserDAO(CwnsUserDAO dao) {
		cwnsUserDAO = dao;
	}

	private CwnsUserStatusRefDAO cwnsUserStatusRefDAO;

	public void setCwnsUserStatusRefDAO(CwnsUserStatusRefDAO dao) {
		cwnsUserStatusRefDAO = dao;
	}

	private CwnsUserLocationDAO cwnsUserLocationDAO;

	public void setCwnsUserLocationDAO(CwnsUserLocationDAO dao) {
		cwnsUserLocationDAO = dao;
	}

	private CwnsUserLocatnAccessLevelDAO cwnsUserLocatnAccessLevelDAO;

	public void setCwnsUserLocatnAccessLevelDAO(CwnsUserLocatnAccessLevelDAO dao) {
		cwnsUserLocatnAccessLevelDAO = dao;
	}

	private AccessLevelRefDAO accessLevelRefDAO;

	public void setAccessLevelRefDAO(AccessLevelRefDAO dao) {
		accessLevelRefDAO = dao;
	}

	public void getItemFromStr(String commaDelimitedString, ArrayList list) {
		if (commaDelimitedString != null) {
			StringTokenizer tokenizer = new StringTokenizer(
					commaDelimitedString, ",");
			int k = 0;
			while (tokenizer.hasMoreTokens()) {
				String str = tokenizer.nextToken();
				list.add(str);
				k++;
			}
		}
	}

	public Collection getSelAccessLevels(UserDetailsForm udForm) {
		SearchConditions scs = new SearchConditions(new SearchCondition(
				"id.cwnsUserId", SearchCondition.OPERATOR_EQ, udForm
						.getCwnsUserId()));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition(
				"id.locationId", SearchCondition.OPERATOR_EQ, udForm
						.getCurrentLocId()));
		scs
				.setCondition(new SearchCondition("id.locationTypeId",
						SearchCondition.OPERATOR_EQ, udForm
								.getCurrentLocationTypeId()));

		Collection accessLevels = searchDAO.getSearchList(
				CwnsUserLocatnAccessLevel.class, scs);
		return accessLevels;

	}
	
	public void setCWNSPrimaryOidGroup(String cwnsUserId, String userType){
		System.out.println("setCWNSPrimaryOidGroup, " + cwnsUserId + ", " + userType);
		
		try{
			//		remove user from all CWNS subgroups, but not from CWNS
		regRemoveUserFromGroup(cwnsUserId, CWNSProperties.getProperty("cwns.Federal.groupName"));
		regRemoveUserFromGroup(cwnsUserId, CWNSProperties.getProperty("cwns.Regional.groupName"));
		regRemoveUserFromGroup(cwnsUserId, CWNSProperties.getProperty("cwns.State.groupName"));
		regRemoveUserFromGroup(cwnsUserId, CWNSProperties.getProperty("cwns.Local.groupName"));
		
		//Add user to selected group
		regAddUserToGroup(cwnsUserId, CWNSProperties.getProperty("cwns."+userType+".groupName"));
		}catch(Exception e){
			log.error("Error trying to set CWNS Primary Oid Group for user " + cwnsUserId, e);
		}
	}
	
	public void regUpdateFormWithRequestId(RegistrationForm rForm, String requestId){
		try{
			ArrayList selfRegRequests = new ArrayList();
			gov.epa.iamfw.webservices.dataobjects.Subscriber subscriber;
			
			IAMResponse iamResponse = regGetSelfRegistrationPendingRequest(selfRegRequests);
				
			if(iamResponse.isSuccess()){
				//
				for (int I = selfRegRequests.size() - 1; I >= 0; I--){
					//System.out.println(I);
					subscriber = (gov.epa.iamfw.webservices.dataobjects.Subscriber) selfRegRequests.get(I);
					if(subscriber.getRequestId().equalsIgnoreCase(requestId)){
						//assuming that this request is from iam worlflow, nothing should be null
						//if(subscriber.getFirstName() != null){
						rForm.setFirstName(subscriber.getFirstName());
						//}
						//if(subscriber.getLastName() != null){
						rForm.setLastName(subscriber.getLastName());
						//}
						//if(subscriber.getTelephoneNumber() != null){
						rForm.setPhone(subscriber.getTelephoneNumber());
						//}
						//if(subscriber.getEpaForwardEmailAddress() != null){
						rForm.setEmailAddress(subscriber.getEpaForwardEmailAddress());
						//}
						//if(subscriber.getStreetAddress() != null){
						rForm.setStreet(subscriber.getStreetAddress());
						//}
						//if(subscriber.getCity() != null){
						rForm.setCity(subscriber.getCity());
						//}
						//if(subscriber.getState() != null){
						rForm.setStateId(subscriber.getState());
						//}
						//if(subscriber.getPostalCode() != null){
						rForm.setZip(subscriber.getPostalCode());
						//}
						return;
					}
				}
			}
		}catch(Exception e){
			log.error("Error Unable to Update Form with RequestId.", e);
		}
	}
	
	public  IAMResponse regGetSelfRegistrationPendingRequest(ArrayList selfRegRequests){
		
		IAMResponse iamResponse = new IAMResponse();
		iamResponse.setSuccess(false);
		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Response pendingResponse;
		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request pendingRequest = new gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request();
		gov.epa.iamfw.webservices.dataobjects.Subscriber[] subscribers;
		String token;

		try {
			// Get the IAM locator object and Auth Token
			IAMLocator iamLocator = regGetIAMLocator();
			token = regGetAuthToken(iamLocator);

			// Initialize the Request object used to obtain the pending requests
			pendingRequest.setGroupName("cwns");
			pendingRequest.setNoOfRequests(new Integer(0));
			pendingRequest.setOffSet(new Integer(0));
			pendingRequest.setToken(token);

			// Get a list of the pending requests
			DelegatedAdministrationService delegatedAdministrationService = iamLocator
					.getDelegatedAdministrationService();
			pendingResponse = delegatedAdministrationService.getSelfRegistrationPendingRequests(pendingRequest);
					
			
			if (pendingResponse.getErrors() != null) {
				return regProcessIAMErrors(pendingResponse.getErrors());
			}

			// get the subscriber list
			subscribers = pendingResponse.getSubscriberList();
			
			for(int I = 0; I < subscribers.length; I++){
				//System.out.println(subscribers[I].getRequestId());
				//System.out.println(subscribers[I].getEpaUserPrimaryCommunity());
				//System.out.println(subscribers[I].getRequestType());
				
				selfRegRequests.add(subscribers[I]);
			}

			

		} catch (Exception e) {
			throw new ApplicationException("Error Failed to get SelfRegistration Pending Requests.", e);
		}
		
		iamResponse.setSuccess(true);
		return iamResponse;
	}
	
	public  IAMResponse regGetGroupSubscriptionPendingRequest(ArrayList groupSubRequests){
		IAMResponse iamResponse = new IAMResponse();
		iamResponse.setSuccess(false);
		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Response pendingResponse;
		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request pendingRequest = new gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request();
		gov.epa.iamfw.webservices.dataobjects.Subscriber[] subscribers;
		String token;

		try {
			// Get the IAM locator object and Auth Token
			IAMLocator iamLocator = regGetIAMLocator();
			token = regGetAuthToken(iamLocator);

			// Initialize the Request object used to obtain the pending requests
			pendingRequest.setGroupName("cwns");
			pendingRequest.setNoOfRequests(new Integer(0));
			pendingRequest.setOffSet(new Integer(0));
			pendingRequest.setToken(token);

			// Get a list of the pending requests
			DelegatedAdministrationService delegatedAdministrationService = iamLocator
					.getDelegatedAdministrationService();
			pendingResponse = delegatedAdministrationService.getSubscribeToGroupPendingRequests(pendingRequest);
					
			
			if (pendingResponse.getErrors() != null) {
				return regProcessIAMErrors(pendingResponse.getErrors());
			}

			// get the subscriber list
			subscribers = pendingResponse.getSubscriberList();
			
			for(int I = 0; I < subscribers.length; I++){
				//System.out.println(subscribers[I].getRequestId());
				//System.out.println(subscribers[I].getRequestedFor());
				//System.out.println(subscribers[I].getRequestType());
				
				groupSubRequests.add(subscribers[I]);
			}

			

		} catch (Exception e) {
			throw new ApplicationException("Error Failed to get SelfRegistration Pending Requests.", e);
		}
		
		iamResponse.setSuccess(true);
		return iamResponse;
	}
	
	public void regUpdateCwnsUserAccountRequestId(String requestId,String cwnsUserId){
		CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
		cwnsUser.setPortalRequestId(requestId);
	}
	
	public IAMResponse regHandleGroupSubscriptionRequest(String requestId, boolean approve){
		//System.out.println("Handle requestId = " + requestId + " " + approve);
		Boolean no = new Boolean(false);
		Boolean yes = new Boolean(true);
		IAMResponse iamResponse = new IAMResponse();
		iamResponse.setSuccess(false);

		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Response pendingResponse;
		gov.epa.iamfw.webservices.delegatedadministration.processsubscribetogrouppendingrequests.localschema.Request request = new gov.epa.iamfw.webservices.delegatedadministration.processsubscribetogrouppendingrequests.localschema.Request();
		gov.epa.iamfw.webservices.delegatedadministration.processsubscribetogrouppendingrequests.localschema.Response response = null;
		gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request pendingRequest = new gov.epa.iamfw.webservices.delegatedadministration.getpendingrequests.localschema.Request();
		gov.epa.iamfw.webservices.dataobjects.Subscriber[] subscribers;
		gov.epa.iamfw.webservices.dataobjects.Subscriber theSubscriber = null;

		// Initialize the Notify object
		gov.epa.iamfw.webservices.delegatedadministration.processsubscribetogrouppendingrequests.localschema.Notify notify = new gov.epa.iamfw.webservices.delegatedadministration.processsubscribetogrouppendingrequests.localschema.Notify(
				no, "");

		String token;

		try {
			// Get the IAM locator object and Auth Token
			IAMLocator iamLocator = regGetIAMLocator();
			token = regGetAuthToken(iamLocator);

			// Approve the user
			request.setRequestId(requestId);
			request.setNotify(notify);
			request.setToken(token);

			DelegatedAdministrationService delegatedAdministrationService = iamLocator
					.getDelegatedAdministrationService();
			if (approve) {
				// Approve the request
				response = delegatedAdministrationService.approveSubscribeToGroupPendingRequests(request);
				
			} else {
				// Deny the request
				//System.out.println("reject requestid");
				response = delegatedAdministrationService.rejectSubscribeToGroupPendingRequests(request);
			}

			if (response.getErrors() != null) {
				// An error occurred
				return regProcessIAMErrors(response.getErrors());
			}

		} catch (Exception e) {
			throw new ApplicationException("Error in approving Portal user Group subscription", e);
		}

		iamResponse.setSuccess(true);
		return iamResponse;
	}
	
	public ActionErrors regApproveGroupSubscriptionRequest(String cwnsUserId, String currentUserId){
		ActionErrors errors = new ActionErrors();
		CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
		String iamRequestId = cwnsUser.getPortalRequestId();

		if (cwnsUser.getPortalRequestId() != null
				|| cwnsUser.getPortalRequestId().length() > 0) {
			try{
			IAMResponse iamResponse = regHandleGroupSubscriptionRequest(iamRequestId, true);

			if (iamResponse.isSuccess()) {
				//cwnsUser.setInitialPassword(iamResponse.getPassword());
				//cwnsUser.setOidUserid(iamResponse.getPortalUserId());
				cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("A"));
			} else {
				errors = processErrors(iamResponse, errors);
			}
			}catch(Exception e){
				log.debug("ERROR, unable to complete regHandleGroupSubscriptionRequest for approval", e);
			}
		}

		// Update the CwnsUser object
		saveCwnsUser(cwnsUser, currentUserId);

		return errors;
	}
	
	public ActionErrors regDenyGroupSubscriptionRequest(String cwnsUserId, String currentUserId){
		//System.out.println("regDenyGroupSubscriptionRequest");
		ActionErrors errors = new ActionErrors();
		CwnsUser cwnsUser = findUserByUserId(cwnsUserId);
		String iamRequestId = cwnsUser.getPortalRequestId();
		
		
		
		if (cwnsUser.getPortalRequestId() != null
				&& cwnsUser.getPortalRequestId().trim().length() > 0) {
			try{
				IAMResponse iamResponse = regHandleGroupSubscriptionRequest(iamRequestId, false);
				if (!iamResponse.isSuccess()) {
					errors = processErrors(iamResponse, errors);
				}
			}catch(Exception e){
				log.debug("ERROR, unable to complete regHandleGroupSubscriptionRequest for denial", e);
			}
		}

		if (errors.isEmpty()) {
			// Update the CwnsUser object
			cwnsUser = findUserByUserId(cwnsUserId);
			String description = cwnsUser.getDescription()
					+ "CWNS Administrator " + currentUserId
					+ " denied this user access to CWNS";
			cwnsUser.setDescription(description);
			cwnsUser.setCwnsUserStatusRef(getCwnsUserStatusRef("I"));
			saveCwnsUser(cwnsUser, currentUserId);
		}

		return errors;
	}
	
	//Performance is lost using these function
	/*public boolean isRequestSelfRegistration(String requestId){
		boolean isSelfReg = false;
		ArrayList selfRegPendingRequests = new ArrayList();
		IAMResponse iamResponse = regGetSelfRegistrationPendingRequest(selfRegPendingRequests);
		gov.epa.iamfw.webservices.dataobjects.Subscriber subscriber;
		if(iamResponse.isSuccess()){
			for(int I = 0; I < selfRegPendingRequests.size(); I++){
				subscriber = (gov.epa.iamfw.webservices.dataobjects.Subscriber)selfRegPendingRequests.get(I);
				if(requestId.equalsIgnoreCase(subscriber.getRequestId())){
					if(subscriber.getRequestType() != null && subscriber.getRequestType().equalsIgnoreCase("Create User")){
						isSelfReg = true;
						return isSelfReg;
					}
				}
			}
		}else{
			log.error("Error unable to determine Self Registration request.");
		}
		return isSelfReg;
	}*/
	
	//not to be use until fix by IAM team
	//RequestType is null
	/*public boolean isRequestGroupSubscription(String requestId){
		boolean isGroupSub = false;
		ArrayList groupSubPendingRequests = new ArrayList();
		IAMResponse iamResponse = regGetGroupSubscriptionPendingRequest(groupSubPendingRequests);
		gov.epa.iamfw.webservices.dataobjects.Subscriber subscriber;
		if(iamResponse.isSuccess()){
			for(int I = 0; I < groupSubPendingRequests .size(); I++){
				subscriber = (gov.epa.iamfw.webservices.dataobjects.Subscriber)groupSubPendingRequests .get(I);
				if(requestId.equalsIgnoreCase(subscriber.getRequestId())){
					if(subscriber.getRequestType() == null){
						isGroupSub = true;
					}
				}
			}
		}else{
			log.error("Error unable to determine Group Subscription request.");
		}
		return isGroupSub;
	}*/
	
	public void removeUserFromCWNSGroups(String cwnsUserId){
		try{
			//		remove user from all CWNS subgroups, and from CWNS
		regRemoveUserFromGroup(cwnsUserId, CWNSProperties.getProperty("cwns.Federal.groupName"));
		regRemoveUserFromGroup(cwnsUserId, CWNSProperties.getProperty("cwns.Regional.groupName"));
		regRemoveUserFromGroup(cwnsUserId, CWNSProperties.getProperty("cwns.State.groupName"));
		regRemoveUserFromGroup(cwnsUserId, CWNSProperties.getProperty("cwns.Local.groupName"));
		regRemoveUserFromGroup(cwnsUserId, CWNSProperties.getProperty("cwns.groupName"));
		
		}catch(Exception e){
			log.error("Error trying to remove " + cwnsUserId + " from CWNS Groups.", e);
		}
	}
	
	public void addUserToCWNSGroups(String cwnsUserId, String userType){
		try{
			regAddUserToGroup(cwnsUserId,CWNSProperties.getProperty("cwns.groupName"));
			setCWNSPrimaryOidGroup(cwnsUserId, userType);
		}catch(Exception e){
			log.error("Error trying to add " + cwnsUserId + " from CWNS Groups.", e);
		}
	}
	
	public boolean isPortalUserMemberOfCWNSGroup(String oidUserId){
		try{
			//System.out.println("cwns group name = " + CWNSProperties.getProperty("cwns.groupName"));
			ArrayList memberGroups = new ArrayList();
			getPortalUserGroupMembership(memberGroups, oidUserId);
			String group = "";
			for(int I = 0; I < memberGroups.size(); I++){
				group = (String)memberGroups.get(I);
				System.out.println("group = " + group);
				if(CWNSProperties.getProperty("cwns.groupName").equalsIgnoreCase(group)){
					return true;
				}
			}
		}catch(Exception e){
			log.error("Error trying to get group Membership for oidUserId = " + oidUserId, e);
		}
		return false;
	}
	
	public void getPortalUserGroupMembership(ArrayList memberGroups, String oidUserId){
		try{
			IAMLocator iamLocator = regGetIAMLocator();
			gov.epa.iamfw.webservices.group.GroupMgr groupMgr = iamLocator.getGroupManager();
			
			
			String token = regGetAuthToken(iamLocator);
			
			String groups[] = groupMgr.getUserGroups(token, oidUserId, gov.epa.iamfw.webservices.group.GroupRole.member);
			
			for(int I = 0; I < groups.length; I++){
				//System.out.println("member of groups = " + groups[I]);
				memberGroups.add(groups[I]);
			}
			
		}catch(Exception e){
			log.error("Error trying to get group Membership for oidUserId = " + oidUserId, e);
		}
	}
	
	public  gov.epa.iamfw.webservices.userservice.getuser.localschema.UserProfile regSearchOidUser(String userId){
		try{
		IAMLocator iamLocator = regGetIAMLocator();
		String token = regGetAuthToken(iamLocator);
		
		gov.epa.iamfw.webservices.userservice.UserService userService = iamLocator.getUserService();
		gov.epa.iamfw.webservices.userservice.getuser.localschema.Request request = new gov.epa.iamfw.webservices.userservice.getuser.localschema.Request ();
		gov.epa.iamfw.webservices.userservice.getuser.localschema.Response  response = new gov.epa.iamfw.webservices.userservice.getuser.localschema.Response (); 
		
		request.setToken(token);
		request.setUserId(userId);
		response = userService.getUser(request);
		
		return response.getUserProfile();
		
		}catch(Exception e){
			log.error("Error trying to search for oidUserId = " + userId, e);
		}
		
		return null;
	}
}
