package gov.epa.owm.mtb.cwns.dao;

import gov.epa.owm.mtb.cwns.model.DataAreaRef;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public interface FacilityCommentsDAO extends DAO {
    public List findFacilityCommentsByFacilityIdAndDataAreaName(long facilityId, String dataAreaName);
	public int countFacilityComments(long facilityId, String dataAreaName);
	public void addFacilityComments(long facilityId, String dataAreaName, String comments, String User);
	public void deleteFacilityComments(long facilityId, String dataAreaName);
	public void deleteFacilityComments(long facilityCommentId);
	public void updateFacilityComments(long facilityCommentId, String comments, String User);
	public boolean isFeedbackVersionValid(long facilityId, String dataAreaName);
	public List findFacilityCommentsByFacilityIdAndDataAreaName(long facilityId, Long dataAreaId);
	public DataAreaRef getDataAreaRefFromName(String dataAreaName);
	public FacilityReviewStatus getLatestFacilityReviewStatus(String facilityId);
}
