package gov.epa.owm.mtb.cwns.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.AnnouncementDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.Announcement;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.AnnouncementService;

public class AnnouncementServiceImpl extends CWNSService implements
		AnnouncementService {

	private AnnouncementDAO announcementDAO;
	private SearchDAO searchDAO;
	public static final String DATEFORMAT =  "dd-MMM-yy hh:mm a";
	
	public void setAnnouncementDAO(AnnouncementDAO dao){
		announcementDAO = dao;
	}	
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	
	public Collection findAnnouncementsByLocation(String locationId1,String locationId2,int startIndex,int maxResultSize) {
		
		SortCriteria sortCriteria = new SortCriteria("id.lastUpdateTs", SortCriteria.ORDER_DECENDING);	
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		ArrayList columns = new ArrayList();
		columns.add("id.lastUpdateTs");
		columns.add("description");
		columns.add("amt.description");
		AliasCriteria aliasCriteria = new AliasCriteria("administrativeMessageRef","amt",AliasCriteria.JOIN_INNER);
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(aliasCriteria);
		SearchConditions scs = getKeyWordSearchCondition(locationId1,locationId2);
		Collection results = searchDAO.getSearchList(Announcement.class, columns, scs, sortArray, aliasArray);
		
		return results;
	}
	
	public SearchConditions getKeyWordSearchCondition(String locationid1,String locationid2){
		
		Date d1 = new Date(); 
		Date d2 = new Date(d1.getYear()-1,d1.getMonth(),d1.getDate());
		ArrayList a = new ArrayList();
		a.add(d2);
		a.add(d1);
		SearchConditions scs =  new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationid1));
		scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("locationId", SearchCondition.OPERATOR_EQ, locationid2));
	    scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.lastUpdateTs", SearchCondition.OPERATOR_BETWEEN, a));	
		//and locationId condition
		//SearchConditions scs2 =  new SearchConditions(new SearchCondition("locationId", SearchCondition.OPERATOR_LIKE, "NJ"));
		//scs.setCondition(SearchCondition.OPERATOR_AND, scs2);
		return scs;
	}

	public Collection findAnnouncementsByLocationAndFormat(String locationId1,String locationId2,int startIndex,int maxResultSize) {
		Collection announcements = findAnnouncementsByLocation(locationId1,locationId2,startIndex,maxResultSize);
	    Collection ann = new ArrayList();
		Iterator iter = announcements.iterator();	
		SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			String value = (String)obj[1]==null?(String)obj[2]:(String)obj[1];
			ann.add(new Entity(df.format((Date)obj[0]),value));
		}	
		return ann;
	}
	
}
