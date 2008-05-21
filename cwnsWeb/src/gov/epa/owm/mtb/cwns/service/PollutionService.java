package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;

import java.util.Collection;

public interface PollutionService {
	public Collection getPollutionGroupInfo(String facilityId);
	public Collection getPollutionProblemInfo(Collection pollutionCategoryGroupProblemList, Collection facPolluProbList);
	public Collection getPollutionGroupProblemInfo(String facilityId);
	public Collection getFacilityPollutionProblems(String facilityId);	
	public void updatePollutionRecords(String facilityId, String commaDelimitedString, CurrentUser user);
	
}
