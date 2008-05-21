package gov.epa.owm.mtb.cwns.dao;

import java.util.Collection;
import java.util.Date;

import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;

public interface ReviewStatusRefDAO extends DAO {
	public ReviewStatusRef getReviewStatusRefByCode(String statusCode);

	public Collection findFacilityCountByReviewStatusRef(Collection locationIds);

	public Collection getFacilityReviewStatus(String facilityId);

	public Collection getFacilityEntryStatusWithErrors(String facilityId);

	public Collection getFederalReviewStatusWithErrors(String facilityId);

	public Date getLatestReviewCommentTs(String facilityId);

	public Date getLatestReviewStatusTs(String facilityId);

	public Collection getFacilityFeedbackStatus(String cwnsNbr);

	public boolean hasFeedbackVersion(String cwnsNbr);

	public String getCWNSNbrByFacilityId(String facilityId);

	public Collection getAllReviewStatues();

	public Collection getFacilityDocumentByType(String facilityId);

	public Collection getFacilityReviewStatusHistory(String facilityId);
	
	public void createFeedbackFacility(String facilityId, String userId);
}
