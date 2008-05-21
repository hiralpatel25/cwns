package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.dao.CwnsUserSettingDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.CwnsUserSetting;
import gov.epa.owm.mtb.cwns.model.ReviewComment;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

public class CwnsUserSettingDAOImpl extends BaseDAOHibernate implements CwnsUserSettingDAO {

	public static final String SELECTED_LIST_TYPE = "Selected"; 
	public static final String IMPORT_LIST_TYPE   = "import"; 

	/**
	 * Update the Set of facilityIds for the user.
	 * @param userAndRole
	 * 			Identifies the user
	 * @param masterSelectedList
	 * 			Set of facilityIds
	 */
	public void updateCwnsUserSetting(String userAndRole, Set masterSelectedList){
		
	   	try {

	   		// remove existing "selected" records for this userAndRole
	   	 	Session session = this.getDAOSession();	
	   	 	List cwnsUserSettingRecs = session.createCriteria(CwnsUserSetting.class)
	   	 	            		.add(Expression.eq("userAndRole", userAndRole))
	   	 	            		.add(Expression.eq("listType", SELECTED_LIST_TYPE))
	   	 	            		.list();
	   	 	
			Iterator iter = cwnsUserSettingRecs.iterator();
			while (iter.hasNext()) {
				CwnsUserSetting cwnsUserSettingRec = (CwnsUserSetting) iter.next();
				session.delete(cwnsUserSettingRec);
			}
	   		
	   		
			// add a new record for each facilityId in masterSelectedList
			iter = masterSelectedList.iterator();
			while (iter.hasNext()) {
				String facilityId = (String) iter.next();

				CwnsUserSetting cwnsUserSetting = new CwnsUserSetting();
				cwnsUserSetting.setFacilityId(new Long(facilityId).longValue());
				cwnsUserSetting.setUserAndRole(userAndRole);
				cwnsUserSetting.setLastUpdateTs(new Date());
				cwnsUserSetting.setLastUpdateUserid(userAndRole);
				cwnsUserSetting.setListType(SELECTED_LIST_TYPE);
				saveObject(cwnsUserSetting);
			}
		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
			
		
	}
	

	/**
	 * Get the user's Set of facilityIds from the database.
	 * @param userAndRole
	 * 			Identifies the user
	 * @return
	 * 			Set of facilityIds
	 */
	public Set getCwnsUserSetting(String userAndRole, String listType){
		Set facilityIds = new HashSet();
		
	   	try {

	   		// Get existing records for this userAndRole
	   	 	Session session = this.getDAOSession();	
	   	 	List cwnsUserSettingRecs = session.createCriteria(CwnsUserSetting.class)
	   	 	            		.add(Expression.eq("userAndRole", userAndRole))
	   	 	            		.add(Expression.eq("listType", listType))
	   	 	            		.list();
	   	 	
			Iterator iter = cwnsUserSettingRecs.iterator();
			while (iter.hasNext()) {
				CwnsUserSetting cwnsUserSettingRec = (CwnsUserSetting) iter.next();
				Long facId = new Long(cwnsUserSettingRec.getFacilityId());
//				facilityIds.add(facId.toString());
				facilityIds.add(facId);
			}
		} catch (HibernateException he) {
			throw getHibernateTemplate().convertHibernateAccessException(he);
		}
		
		return facilityIds;
		
	}	
}