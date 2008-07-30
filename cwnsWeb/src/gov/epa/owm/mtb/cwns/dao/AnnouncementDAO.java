package gov.epa.owm.mtb.cwns.dao;

import java.util.Collection;

public interface AnnouncementDAO extends DAO {

	Collection findAnnouncementsByLocation(String locationId);

}
