package gov.epa.owm.mtb.cwns.service;

import java.util.Collection;

public interface AnnouncementService {
	
	public static final long ADMIN_MESSAGE_ID = 16;
	
	public Collection findAnnouncementsByLocation(String locationId1,String locationId2,int startIndex,int maxResultSize);
	
	public Collection findAnnouncementsByLocationAndFormat(String locationId1,String locationId2,int startIndex,int maxResultSize);
}
