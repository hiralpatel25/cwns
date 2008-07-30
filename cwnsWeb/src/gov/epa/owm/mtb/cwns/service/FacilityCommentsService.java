package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.model.DataAreaRef;

import java.util.Collection;
import java.util.List;

public interface FacilityCommentsService {
	
    public List findFacilityCommentsByFacilityIdAndDataAreaName(long facilityId, String dataAreaName);
    public List findFacilityCommentsByFacilityIdAndDataAreaId(long facilityId, Long dataAreaId);
	public int countFacilityComments(long facilityId, String dataAreaName);
	public void addFacilityComments(long facilityId, String dataAreaName, String comments, String User);
	public void updateFacilityComments(long facilityCommentId, String comments, String User);
	public void deleteFacilityComments(long facilityId, String dataAreaName);
	public void deleteFacilityComments(long facilityCommentId);
	public boolean isFeedbackVersionValid(long facilityId, String dataAreaName);
	public DataAreaRef getDataAreaRefFromName(String dataAreaName);
	public boolean displayFeedbackFacilitySatisfied(long facilityId, long dataAreaId, CurrentUser user);	
}
