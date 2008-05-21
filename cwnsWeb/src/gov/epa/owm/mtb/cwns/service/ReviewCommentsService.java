package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;

import java.util.Collection;

public interface ReviewCommentsService {
	
	/* Maximum number of results to return from a query */ 
	public final int	MAX_RESULTS = 4;
	public final int    SHORT_COMMENT_LENGTH = 50;
	
	public Collection findReviewCommentsByFacilityId(long facilityId, int startIndex, int maxResultSize);
	
    public long findFacilityIdByFacilityNumberAndVersionCode(String facilityNumber, String facilityVersionCode);	
	
	public int countReviewComments(long facilityId);
	
	public Collection getReviewCommentsDereferencedResults(long facilityId, int startIndex, int maxResultSize);

	public void addComments(long facilityId, String comments, String User);
	
	public long fetchFacilityIdByVersionCode(long facilityId, String srcVersionCode, String targetVersionCode);

	public boolean isUpdatable(CurrentUser user, Long facilityId);	
}
