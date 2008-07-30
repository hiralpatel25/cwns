package gov.epa.owm.mtb.cwns.dao;

import java.util.List;

public interface ReviewCommentsDAO extends DAO {

	public List findReviewCommentsByFacilityId(long facilityId, int firstResult, int maxResults);
    public long findFacilityIdByFacilityNumberAndVersionCode(String facilityNumber, String facilityVersionCode);	
	public int countReviewComments(long facilityId);
	public void addComments(long facilityId, String comments, String User);
	public long fetchFacilityIdByVersionCode(long facilityId, String srcVersionCode, String targetVersionCode);
	public List getReviewCommentsByFacilityId(String facilityId);
	
}
