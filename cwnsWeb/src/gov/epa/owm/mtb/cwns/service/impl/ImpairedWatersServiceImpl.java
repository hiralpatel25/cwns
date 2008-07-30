package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.impairedWatersInformation.ImpairedWatersListHelper;
import gov.epa.owm.mtb.cwns.impairedWatersSearch.ImpairedWatersSearchListHelper;
import gov.epa.owm.mtb.cwns.model.AbsoluteLocationPoint;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.model.DocumentTypeRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityDocument;
import gov.epa.owm.mtb.cwns.model.FacilityDocumentId;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityImpairedWater;
import gov.epa.owm.mtb.cwns.model.FacilityImpairedWaterId;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatusId;
import gov.epa.owm.mtb.cwns.model.ImpairedWater;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.ImpairedWatersService;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;

import waters9i_waters.CWNSSpatialServices_xsd.Waters9I_waters_PrgList;
import waters9i_waters.CWNSSpatialServices_xsd.Waters9I_waters_WaterApplicationUser;
import waters9i_waters.CWNSSpatialServices_xsd.Waters9I_waters_WaterDataUser;
import waters9i_waters.CWNSSpatialServices_xsd.Waters9I_waters_WaterEntities;
import waters9i_waters.CWNSSpatialServices_xsd.Waters9I_waters_WaterEntityUser;
import waters9i_waters.CWNSSpatialServices_xsd.Waters9I_waters_WaterHighlightUser;
import waters9i_waters.CWNSSpatialServices_xsd.Waters9I_waters_WaterUrlUser;

import com.induscorp.epadev.waters.wsdls.CWNSSpatialServices_wsdl.CWNSSpatialServices;
import com.induscorp.epadev.waters.wsdls.CWNSSpatialServices_wsdl.CWNSSpatialServicesLocator;
import com.induscorp.epadev.waters.wsdls.CWNSSpatialServices_wsdl.CWNSSpatialServicesPortType;

public class ImpairedWatersServiceImpl extends CWNSService implements ImpairedWatersService
{

	public static final String YYYY_MM_DD = "yyyy-MM-dd";	
	public static final String YYYY_MM = "yyyy-MM";	
	
	private SearchDAO searchDAO;
	private FacilityService facilityService;

	public void setFacilityService(FacilityService fs) {
		facilityService = fs;
	}

	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}

	public void deleteDocumentIfNoReferences(long facilityId, String waterBodyName, String listId)
	{
		String titleName = waterBodyName + " (" + listId + ")";

		ArrayList columns = new ArrayList();
		columns.add("f.facilityId");		
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("document", "d", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));
				
		SearchConditions scs0 = new SearchConditions(new SearchCondition("d.titleName", SearchCondition.OPERATOR_EQ, titleName));
		scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("f.facilityId", SearchCondition.OPERATOR_NOT_EQ, new Long(facilityId)));

	    List facilityDocumentList = searchDAO.getSearchList(FacilityDocument.class, columns, scs0, new ArrayList(), aliasArray);
	    
		int size = 0;
		
		if (facilityDocumentList != null)
		{
			//there are other facilityDocument still associtated with this D
			size = facilityDocumentList.size();
		}
		
		if(size == 0)
		{
			SearchConditions scs = new SearchConditions(new SearchCondition("titleName", SearchCondition.OPERATOR_EQ, titleName));
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("documentTypeRef.documentTypeId", SearchCondition.OPERATOR_EQ, "11"));
			Document document = (Document)searchDAO.getSearchObject(Document.class, scs);
			if (document != null){
			   searchDAO.removeObject(document);
			}	
		}
	}
	
	public void deleteFacilityDocumentIfExists(long facilityId, String waterBodyName, String listId)
	{
		String titleName = waterBodyName + " (" + listId + ")";

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("document", "d", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("d.documentTypeRef", "dtr", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("d.titleName", SearchCondition.OPERATOR_EQ, titleName));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("dtr.documentTypeId", SearchCondition.OPERATOR_EQ, "11"));
		FacilityDocument facilityDocument = (FacilityDocument)searchDAO.getSearchObject(FacilityDocument.class, scs, aliasArray);
		if (facilityDocument != null){
		   searchDAO.removeObject(facilityDocument);
		}		
	}
	
	public void deleteImpairedWaterIfNoReferences(long facilityId, String listId)
	{
		SearchConditions scs0 = new SearchConditions(new SearchCondition("impairedWater.listId", SearchCondition.OPERATOR_EQ, listId));
		scs0.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_NOT_EQ, new Long(facilityId)));
		List facilityIWList = searchDAO.getSearchList(FacilityImpairedWater.class, scs0);
		
		int size = 0;
		
		if (facilityIWList != null)
		{
			//there are other facilities still associtated with this IW
			size = facilityIWList.size();
		}
		
		if(size == 0)
		{
			SearchConditions scs = new SearchConditions(new SearchCondition("listId", SearchCondition.OPERATOR_EQ, listId));
			ImpairedWater impairedWater = (ImpairedWater)searchDAO.getSearchObject(ImpairedWater.class, scs);
			if (impairedWater != null){
			   searchDAO.removeObject(impairedWater);
			}			
		}
	}

	public void deleteFacilityImpairedWaterIfExists(long facilityId, String listId)
	{
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("impairedWater.listId", SearchCondition.OPERATOR_EQ, listId));
		FacilityImpairedWater facilityIW = (FacilityImpairedWater)searchDAO.getSearchObject(FacilityImpairedWater.class, scs);
		if (facilityIW != null){
		   searchDAO.removeObject(facilityIW);
		}		
	}

	public void delete(long facilityId, String waterBodyName, String listId, String userId)
	{
		deleteFacilityImpairedWaterIfExists(facilityId, listId);
		deleteImpairedWaterIfNoReferences(facilityId, listId);
		deleteFacilityDocumentIfExists(facilityId, waterBodyName, listId);
		deleteDocumentIfNoReferences(facilityId, waterBodyName, listId);
		long[] daIds = new long[]{10, 12};
		postSaveFacilityInfo(daIds, new Long(facilityId).toString(), userId);		
	}
	
	//endDate must be in yyyy-MON-dd format
	public long saveDocument(long facilityId, String waterBodyName, String ListId, String endDate, String userId)
	{
		String titleName = waterBodyName + " (" + ListId + ")";
		
		SimpleDateFormat yyyyMondd = new SimpleDateFormat(YYYY_MM_DD);	
		SimpleDateFormat yyyyMon = new SimpleDateFormat(YYYY_MM);	
				
		Date baseDesigndate = null, publishedDate = null;
		
		try
		{
			publishedDate = yyyyMondd.parse(endDate.substring(0, 4) + "-01-01");
			baseDesigndate = yyyyMon.parse(endDate.substring(0, 4) + "-01");	
		}
		catch (ParseException e) {
            // TODO: need to handle this exception			
			return -1;
        }    
		
		SearchConditions scs = new SearchConditions(new SearchCondition("titleName", 
																		SearchCondition.OPERATOR_EQ, 
																		titleName));
		Document document = (Document)searchDAO.getSearchObject(Document.class, scs);
		if (document != null){
			document.setLastUpdateTs(new Date());
			document.setLastUpdateUserid(userId);
			document.setPublishedDate(publishedDate);
			document.setBaseDesignDate(baseDesigndate);
			searchDAO.saveObject(document);
		}
		else {
			document = new Document();
			
			SearchConditions scs1 = new SearchConditions(new SearchCondition("documentTypeId", 
					SearchCondition.OPERATOR_EQ, 
					"11"));
			DocumentTypeRef documentTypeRef = (DocumentTypeRef)searchDAO.getSearchObject(DocumentTypeRef.class, scs1);
			document.setDocumentTypeRef(documentTypeRef);
			document.setTitleName(titleName);
			document.setAuthorName("Impaired Waters Database");
			document.setLastUpdateTs(new Date());
			document.setLastUpdateUserid(userId);
			document.setPublishedDate(publishedDate);
			document.setBaseDesignDate(baseDesigndate);
			
 	    	Facility facility = facilityService.findByFacilityId(new Long(facilityId).toString());
 	    	document.setLocationId(facility.getLocationId());
 	    	
			searchDAO.saveObject(document);
		}			
		
		return document.getDocumentId();		
	}
	

	public void saveFacilityDocument(long facilityId, long documentId, String userId)
	{
		// TODO Auto-generated method stub
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("document.documentId", SearchCondition.OPERATOR_EQ, new Long(documentId)));
		FacilityDocument facilityDocument = (FacilityDocument)searchDAO.getSearchObject(FacilityDocument.class, scs);
		if (facilityDocument != null){
			facilityDocument.setLastUpdateTs(new Date());
			facilityDocument.setLastUpdateUserid(userId);
			searchDAO.saveObject(facilityDocument);
		}
		else {
			facilityDocument = new FacilityDocument();
			FacilityDocumentId id = new FacilityDocumentId(documentId, facilityId);
			facilityDocument.setId(id);
			facilityDocument.setFeedbackDeleteFlag('N');
			facilityDocument.setLastUpdateTs(new Date());
			facilityDocument.setLastUpdateUserid(userId);
			searchDAO.saveObject(facilityDocument);
		}		
	}
	

	public String saveImpairedWater(String waterBodyName, String ListId, String TMDLReportUrl, String NTTSUrl, String waterBodyType, String userId)
	{
		// TODO Auto-generated method stub
		SearchConditions scs = new SearchConditions(new SearchCondition("listId", SearchCondition.OPERATOR_EQ, ListId));
		ImpairedWater IW = (ImpairedWater)searchDAO.getSearchObject(ImpairedWater.class, scs);
		if (IW != null){
			IW.setWaterBodyName(waterBodyName);
			IW.setTmdlUrl(TMDLReportUrl);
			IW.setImpairedWaterUrl(NTTSUrl);
			IW.setLastUpdateTs(new Date());
			IW.setLastUpdateUserid(userId);			
			IW.setSourceCode("303(d)".equals(waterBodyType.toLowerCase())?"303D":"305B");
			searchDAO.saveObject(IW);
		}
		else {
			IW = new ImpairedWater();
			IW.setListId(ListId);
			IW.setWaterBodyName(waterBodyName);
			IW.setSourceCode("303(d)".equals(waterBodyType.toLowerCase())?"303D":"305B");
			IW.setTmdlUrl(TMDLReportUrl);
			IW.setImpairedWaterUrl(NTTSUrl);
			IW.setLastUpdateTs(new Date());
			IW.setLastUpdateUserid(userId);
			searchDAO.saveObject(IW);
		}			
		
		return IW.getListId();
	}
	

	public void saveFacilityImpairedWater(long facilityId, String listId, String userId)
	{
		// TODO Auto-generated method stub
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("impairedWater.listId", SearchCondition.OPERATOR_EQ, listId));
		FacilityImpairedWater facilityIW = (FacilityImpairedWater)searchDAO.getSearchObject(FacilityImpairedWater.class, scs);
		if (facilityIW != null){
			facilityIW.setLastUpdateTs(new Date());
			facilityIW.setLastUpdateUserid(userId);
			searchDAO.saveObject(facilityIW);
		}
		else {
			facilityIW = new FacilityImpairedWater();
			FacilityImpairedWaterId id = new FacilityImpairedWaterId(facilityId, listId);
			facilityIW.setId(id);
			facilityIW.setFeedbackDeleteFlag('N');
			facilityIW.setLastUpdateTs(new Date());
			facilityIW.setLastUpdateUserid(userId);
			searchDAO.saveObject(facilityIW);
		}				
	}

	public boolean costExistsForFacilityDocument(long facilityId, String waterBodyName, String listId)
	{
		
		String titleName = waterBodyName + " (" + listId + ")";

		ArrayList columns = new ArrayList();
		columns.add("costId");		
		
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facilityDocument", "fd", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fd.facility", "f", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("fd.document", "d", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("d.documentTypeRef", "dtr", AliasCriteria.JOIN_INNER));
				
		SearchConditions scs = new SearchConditions(new SearchCondition("d.titleName", SearchCondition.OPERATOR_EQ, titleName));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("dtr.documentTypeId", SearchCondition.OPERATOR_EQ, "11"));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));

		
	    List costList = searchDAO.getSearchList(Cost.class, columns, scs, new ArrayList(), aliasArray);
	    
	    if(costList!=null && costList.size()>0)
	    {
	    	return true;
	    }
		
		return false;
	}
	
	public boolean costCurvesExistsForFacilityDocument(long facilityId, String waterBodyName, String listId){
		
		String titleName = waterBodyName + " (" + listId + ")";
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("document", "d", AliasCriteria.JOIN_INNER));
		aliasArray.add(new AliasCriteria("d.documentTypeRef", "dtr", AliasCriteria.JOIN_INNER));
		
		SearchConditions scs = new SearchConditions(new SearchCondition("d.titleName", SearchCondition.OPERATOR_EQ, titleName));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("dtr.documentTypeId", SearchCondition.OPERATOR_EQ, "11"));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
	    FacilityDocument facilityDocument = (FacilityDocument)searchDAO.getSearchObject(FacilityDocument.class, scs,aliasArray);
	    if(facilityDocument!=null && facilityDocument.getFacilityCostCurves()!=null && facilityDocument.getFacilityCostCurves().size()>0){
	    	return true;
	    }		
		return false;
	}
	
	
    public void postSaveFacilityInfo(long[] dataAreaIds, String sFacilityId, String user) {
    	//update facility_entry_status
    	if(dataAreaIds != null && dataAreaIds.length > 0)
    	{
    		for(int i=0; i<dataAreaIds.length; i++){
				SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(sFacilityId)));
				scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ,new Long(dataAreaIds[i])));
				FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
				
				if(facilityEntryStatus != null){
					facilityEntryStatus.setLastUpdateTs(new Date());
					facilityEntryStatus.setDataAreaLastUpdateTs(new Date());
					facilityEntryStatus.setLastUpdateUserid(user);
					searchDAO.saveObject(facilityEntryStatus);
				}else{
					//TODO: serious error: throw exception
				}
    		}
    	}
    	
		//update facility, facility_review_status table
		Facility facility = (Facility)searchDAO.getObject(Facility.class, new Long(sFacilityId));		
		if (facility != null){
		     if (facility.getVersionCode() == 'S' &&
		    		 "SAS".equalsIgnoreCase(facility.getReviewStatusRef().getReviewStatusId()))
		     {
		    	 FacilityReviewStatus facilityreviewstatus = new FacilityReviewStatus();
		    	 FacilityReviewStatusId id = new FacilityReviewStatusId(new Long(sFacilityId).longValue(), "SIP", new Date());
		    	 facilityreviewstatus.setId(id);
		    	 facilityreviewstatus.setLastUpdateUserid(user);
		    	 searchDAO.saveObject(facilityreviewstatus);
		    	 
		        ReviewStatusRef reviewStatusRef = new ReviewStatusRef();
		        reviewStatusRef.setReviewStatusId("SIP");
		        facility.setReviewStatusRef(reviewStatusRef);
		     } 
		  
		  facility.setLastUpdateTs(new Date());
	      facility.setLastUpdateUserid(user);
		  searchDAO.saveObject(facility);  
		}		
    }	
    
    public Collection getFacilityImpairedWaterList(long facilityId)
    {
    	ArrayList resultList = new ArrayList();

		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("impairedWater", "iw", AliasCriteria.JOIN_INNER));
		
		ArrayList columns = new ArrayList();
		columns.add("iw.listId");
		columns.add("iw.waterBodyName");		
		columns.add("iw.impairedWaterUrl");
		columns.add("iw.tmdlUrl");
		columns.add("iw.sourceCode");
		columns.add("feedbackDeleteFlag");		
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		Collection facilityIWList = searchDAO.getSearchList(FacilityImpairedWater.class, columns, scs, new ArrayList(), aliasArray);
		    	
		if(facilityIWList!=null)
		{
			Iterator iter = facilityIWList.iterator();
			while (iter.hasNext()){
				Object[] obj = (Object[])iter.next();
				
				ImpairedWatersListHelper IWHelper = new ImpairedWatersListHelper();
				
				IWHelper.setListId((String)obj[0]);
				IWHelper.setWaterBodyName((String)obj[1]);
				IWHelper.setListUrl((String)obj[2]);
				IWHelper.setWaterBodyDetailUrl((String)obj[3]);
				IWHelper.setHasCost((costExistsForFacilityDocument(facilityId, (String)obj[1], (String)obj[0]) ||
						                  costCurvesExistsForFacilityDocument(facilityId, (String)obj[1], (String)obj[0]))==true?"Y":"N");
				IWHelper.setWaterBodyType((String)obj[4]);
				IWHelper.setFeedbackDeleteFlag(((Character)obj[5]).toString());
				resultList.add(IWHelper);
			}			
		}
    	
    	return resultList;
    }
    
	public boolean isUpdatable(CurrentUser user, Long facilityId)
	{
		return true;
	}
	
	// return true when successfully inserted all impaired waters
	public boolean addImpairedWaters(long facilityId, Set listIds, Collection masterList, String user)
	{
		if(listIds == null || masterList == null || user == null)
			return false;
		
		log.debug("addImpairedWaters: entering addImpairedWaters");
				
		boolean recordsTouched = false;
		
		Iterator iterListIds = listIds.iterator();
		while ( iterListIds.hasNext() ) 
		{
			String listId = (String) iterListIds.next();
			
			log.debug("addImpairedWaters: ready to add (" + listId + ")");
			
			Iterator iterMaster = masterList.iterator();
			while ( iterMaster.hasNext() ) 
			{
				ImpairedWatersSearchListHelper IWHelper = (ImpairedWatersSearchListHelper) iterMaster.next();
				
				log.debug("addImpairedWaters: checking (" + IWHelper.getListId() + ")");
				
				if(IWHelper!=null && listId.equalsIgnoreCase(IWHelper.getListId()))
				{
					log.debug("addImpairedWaters: found and add (" + listId + ")");					
					log.debug("addImpairedWaters: " + IWHelper.getListId() + "(" + IWHelper.getListUrl() + ")");			
					saveImpairedWater(IWHelper.getWaterBodyName(), 
							IWHelper.getListId(), 
							IWHelper.getWaterBodyDetailUrl(),
							IWHelper.getListUrl(),IWHelper.getWaterType().toUpperCase(),
							user);
					saveFacilityImpairedWater(facilityId, IWHelper.getListId(), user);
					
					long docId = saveDocument(facilityId, 
							IWHelper.getWaterBodyName(), 
							IWHelper.getListId(), 
							IWHelper.getEndDate(), 
							user);
					
					saveFacilityDocument(facilityId, docId, user);
					
					recordsTouched = true;
					break;
					
				}
			}			
			
		}
		
		if(recordsTouched)
		{
			long[] daIds = new long[]{10, 12};
			postSaveFacilityInfo(daIds, new Long(facilityId).toString(), user);
		}

		return true;
		
	}
	
	public Collection getSearchDisplayList(Collection qualifiedImpairedWaterList, int startIndex)
	{
		ArrayList searchDisplayList	= new ArrayList();	
		
		int i = 0;
		
		if(qualifiedImpairedWaterList != null && startIndex >=0)
		{
	    	Iterator iter = qualifiedImpairedWaterList.iterator();
	    	while ( iter.hasNext() ) {
	    		ImpairedWatersSearchListHelper iwSearchHelper = (ImpairedWatersSearchListHelper) iter.next();
	    		
	    		i++;
	    		
	    		if(i>startIndex+MAX_RESULTS)
	    			break;
	    		else if(i>startIndex)
	    			searchDisplayList.add(iwSearchHelper);
	    	}
		}		
		return searchDisplayList;
	}
	
	// masterList is a collection of ImpairedWatersSearchListHelper
	public Collection getQualifiedImpairedWaterList(Collection masterList, long facilityId, int tmdlOption)
	{
		ArrayList qualifiedImpairedWaterList = null;

		ArrayList columns = new ArrayList();
		columns.add("impairedWater.listId");
		
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		Collection facilityIWList = searchDAO.getSearchList(FacilityImpairedWater.class, columns, scs);
		
		
			qualifiedImpairedWaterList = new ArrayList();
			
			HashSet facilityIWSet = new HashSet();
					
			if (facilityIWList != null && facilityIWList.size() >= 1)
			{
				facilityIWSet.addAll(facilityIWList);
			}					

			Iterator iter1 = masterList.iterator();
			
			while (iter1.hasNext()){
				
				ImpairedWatersSearchListHelper iwSearchHelper = (ImpairedWatersSearchListHelper) iter1.next();
				
				if(!facilityIWSet.contains(iwSearchHelper.getListId()))
				{			
					if(tmdlOption == ImpairedWatersService.TMDL_ALL || 
							(tmdlOption == ImpairedWatersService.TMDL_COMPLETED && iwSearchHelper.getTmdlStatus().equalsIgnoreCase(ImpairedWatersService.TEXT_TMDL_COMPLETED)) ||
							(tmdlOption == ImpairedWatersService.TMDL_NEEDED && iwSearchHelper.getTmdlStatus().equalsIgnoreCase(ImpairedWatersService.TEXT_TMDL_NEEDED)))
						{
							qualifiedImpairedWaterList.add(iwSearchHelper);
						}
				}

			}			
		
		
		return qualifiedImpairedWaterList;
	}
	
    public Collection getImpairedWaterEntities(long facilityId, 
    		int webServiceMethod, 
    		int radiusMile, 
    		String huc8, 
    		int tmdlOption,
    		String keyword) throws Exception
    {
    	ArrayList impairedWaterEntitiesList = new ArrayList();
    	ArrayList listIdList = new ArrayList();
    	
    	ImpairedWatersSearchListHelper IWSearchHelper = null;  	
		
    	BigDecimal latIW = null;
    	BigDecimal longIW = null;
    	    	
    	if(webServiceMethod == ImpairedWatersService.BY_LAT_LONG_RADIUS)
    	{
	    	// get Lat and Long for the facility    	
	    	ArrayList columns = new ArrayList();
			columns.add("latitudeDecimalDegree");
			columns.add("latitudeDirection");
			columns.add("longitudeDecimalDegree");
			columns.add("longitudeDirection");
			
			ArrayList aliasArray = new ArrayList();
			aliasArray.add(new AliasCriteria("geographicArea", "ga", AliasCriteria.JOIN_INNER));
			aliasArray.add(new AliasCriteria("ga.facility", "f", AliasCriteria.JOIN_INNER));
			SearchConditions scs1 = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
			Collection results = searchDAO.getSearchList(AbsoluteLocationPoint.class, columns, scs1, new ArrayList(), aliasArray);
			Iterator iter = results.iterator();
			Map map = new HashMap();
			while (iter.hasNext()){
				Object[] obj = (Object[])iter.next();

				if(obj[0] != null && obj[2] !=null)
				{
					latIW = (BigDecimal)obj[0];
					longIW = (BigDecimal)obj[2];
					
					if(obj[1] != null && ((Character)obj[1]).charValue() == 'S')
					{
						latIW = latIW.negate();
					}
					
					if(obj[3] == null || ((Character)obj[3]).charValue() == 'W')
					{
						longIW = longIW.negate();
					}				
				}
			
				break;
			}		
	
			if(latIW == null || longIW == null)
				return impairedWaterEntitiesList;
	
			log.debug("Lat: (" + latIW.toString() + ")  Long: (" + longIW.toString() + ")");
    	}
    	
		try {

	    	String webServicesEndPoint = CWNSProperties.getProperty("303d.webservices.endpoint");
	    				
	    	log.debug("webServicesEndPoint: (" + webServicesEndPoint + ")");
	    	
	    	CWNSSpatialServicesLocator factory = new CWNSSpatialServicesLocator();

	    	factory.setCWNSSpatialServicesPortEndpointAddress(webServicesEndPoint);

	    	CWNSSpatialServicesPortType spatialservices = (CWNSSpatialServicesPortType)((CWNSSpatialServices)factory).getPort(CWNSSpatialServicesPortType.class);

			Waters9I_waters_PrgList pl = new Waters9I_waters_PrgList();		
			
			String[] array = {"303D", "305B"};			
			
			pl.setArray(array);
			
			log.debug("Starting spatialservices call...");
			
			Waters9I_waters_WaterDataUser wdu = null;
			
			try{
			if(webServiceMethod == ImpairedWatersService.BY_LAT_LONG_RADIUS)
			{
				wdu = spatialservices.getEntitiesByLatLong(latIW, 
							                             longIW, 
							                             new BigDecimal(new Integer(radiusMile).toString()), 
							                             pl, keyword);
			}
			else if(webServiceMethod == ImpairedWatersService.BY_HUC8)
			{
				wdu = spatialservices.getEntitiesByHuc(huc8, pl, keyword);
			}
			else if(webServiceMethod == ImpairedWatersService.BY_KEYWORD)
			{
				wdu = spatialservices.getEntitiesByName(keyword, pl);
			}
			else
			{
				return impairedWaterEntitiesList;
			}
			}catch (RemoteException e) {
				if (e instanceof AxisFault){
					AxisFault fault = (AxisFault)e;
					if(fault.getFaultString().indexOf("returns more than")>=0 &&
						(fault.getFaultString().indexOf("1000")>=0 ||
								fault.getFaultString().indexOf("1,000")>=0)){
						throw new SQLException(fault.getFaultString());
					}
				}
			} catch (Exception e){
				throw e;
			}	

			log.debug("end spatialservices call...");
			
			
			
			if(wdu!=null && wdu.getWatersApplications()!=null)
			{
				Waters9I_waters_WaterApplicationUser[] wwwu = wdu.getWatersApplications().getArray();
				
				if(wwwu !=null && wwwu.length > 0)
				{
					for(int k=0; k<wwwu.length; k++)
					{
						Waters9I_waters_WaterApplicationUser obj303d = wwwu[k];
						
						Waters9I_waters_WaterEntities wwwe = obj303d.getWatersEntities();
						
						if(wwwe != null)
						{
							Waters9I_waters_WaterEntityUser[] wwweuArray = wwwe.getArray();
							
							if(wwweuArray != null && wwweuArray.length > 0)
							{
								if(wwweuArray[0].getEntityTypeDescription()==null) 
									continue;

								String entityType = wwweuArray[0].getEntityTypeDescription();

								String waterBodyType = "";
																
								if(entityType.indexOf("303") >= 0)
									waterBodyType = "303(d)";
								else if(entityType.indexOf("305") >= 0)
									waterBodyType = "305(b)";
								else
									continue;

								for (int i=0; i<wwweuArray.length; i++)
								{
									Waters9I_waters_WaterEntityUser wwweu = wwweuArray[i];
									
									if(wwweu!=null)
									{										
										//log.debug("wwweu[" + i + "]-getEntityId: " + wwweu.getEntityId());
										//log.debug("wwweu[" + i + "]-getEntityName: " + wwweu.getEntityName());
										//log.debug("wwweu[" + i + "]-getStartDateDescription: " + wwweu.getStartDateDescription());
										//log.debug("wwweu[" + i + "]-getEndDateDescription: " + wwweu.getEndDateDescription());								
										//log.debug("wwweu[" + i + "]-getEntityOrganization: " + wwweu.getEntityOrganization());
										//log.debug("wwweu[" + i + "]-getEntityTypeDescription: " + wwweu.getEntityTypeDescription());
										//log.debug("wwweu[" + i + "]-getStateLocationUspsCode: " + wwweu.getStateLocationUspsCode());
										IWSearchHelper = new ImpairedWatersSearchListHelper();
										
										IWSearchHelper.setWaterType(waterBodyType);
										IWSearchHelper.setListId(wwweu.getEntityId());
										IWSearchHelper.setWaterBodyName(wwweu.getEntityName());
										IWSearchHelper.setEndDate(wwweu.getEndDateDescription());
										
										if(wwweu.getWatersUrls()!=null && wwweu.getWatersUrls().getArray() !=null)
										{
											Waters9I_waters_WaterUrlUser[] wwwUrlArray = wwweu.getWatersUrls().getArray();
											
											for(int j=0; j<wwwUrlArray.length; j++)
											{
												Waters9I_waters_WaterUrlUser wwwUU = wwwUrlArray[j];
												
												if(wwwUU!=null)
												{
													//log.debug("---wwwUrl[" + i + "][" + j + "]-getUrlDescription: " + wwwUU.getUrlDescription());
													//log.debug("---wwwUrl[" + i + "][" + j + "]-getUrlAddress: " + wwwUU.getUrlAddress());
													if(wwwUU.getUrlDescription().equalsIgnoreCase("TMDL Report"))
														IWSearchHelper.setWaterBodyDetailUrl(wwwUU.getUrlAddress());
													else if(wwwUU.getUrlDescription().startsWith("National TMDL") || 
															wwwUU.getUrlDescription().startsWith("Water Quality Inventory Assessment "))
													IWSearchHelper.setListUrl(wwwUU.getUrlAddress());
													
												}
											}
	
										}										
										//log.debug("wwweu[" + i + "]-" + wwweu.getWatersHucs());
										//log.debug("wwweu[" + i + "]-" + wwweu.getWatersHightlights());
										
										if(wwweu.getWatersHightlights()!=null && wwweu.getWatersHightlights().getArray() !=null)
										{
											Waters9I_waters_WaterHighlightUser[] wwwHighlightArray = 
													wwweu.getWatersHightlights().getArray();
											
											for(int m=0; m<wwwHighlightArray.length; m++)
											{
												Waters9I_waters_WaterHighlightUser wwwUH = wwwHighlightArray[m];
												
												if(wwwUH!=null)
												{
													if(wwwUH.getHighlightName().equalsIgnoreCase("Water TMDL Status"))
													{
														IWSearchHelper.setTmdlStatus(wwwUH.getHighlightValue());	
													}
													else if(wwwUH.getHighlightName().equalsIgnoreCase("Miles to Search Location"))
													{
														double milesToLocation = -1.0;
														
														try
														{
															milesToLocation = Double.parseDouble(wwwUH.getHighlightValue());
														}
														catch(Exception e)
														{
															//TODO: log errors
														}
														IWSearchHelper.setMilesToLocation(milesToLocation);
													}
													else if(wwwUH.getHighlightName().equalsIgnoreCase("Integrated Reporting Category"))
													{
														if(wwwUH.getHighlightValue()!=null && wwwUH.getHighlightValue().indexOf("4")>=0)
															{
																	IWSearchHelper.setReportCategory(wwwUH.getHighlightValue());
															}
													}
												}
											} // for waterhighlists
										} // if(wwweu.getWatersHightlights()!=null && wwweu.getWatersHightlights().getArray() !=null)
										
									} // if(wwweu!=null)
									
									//log.debug("setTmdlStatus: " + IWSearchHelper.getTmdlStatus());
									
									if(waterBodyType.equals("305(b)") && IWSearchHelper.getReportCategory().indexOf("4") < 0)
										continue;
									
									if(!listIdList.contains(wwweu.getEntityId()))
									{
										listIdList.add(wwweu.getEntityId());
									    impairedWaterEntitiesList.add(IWSearchHelper);
									}
									
								} // for i -- all 303d and 305b
																
							} //if(wwweuArray != null && wwweuArray.length > 0)
						} // if(wwwe != null)
				  } // for k
				}}}
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				AxisFault fault = (AxisFault)e;
				if(fault.getFaultString().indexOf("returns more than")>=0 &&
						(fault.getFaultString().indexOf("1000")>=0 ||
								fault.getFaultString().indexOf("1,000")>=0)){
					throw new Exception(fault.getFaultString());					
				}
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}/*catch (Exception e){
				e.printStackTrace();
				throw e;
			}*/
		
		return impairedWaterEntitiesList;
	}

	public void softDeleteFacilityType(Long facilityId, String waterBodyName, String listId, CurrentUser user) {
		SearchConditions scs = new SearchConditions(new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, facilityId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("impairedWater.listId", SearchCondition.OPERATOR_EQ, listId));
		FacilityImpairedWater facilityIW = (FacilityImpairedWater)searchDAO.getSearchObject(FacilityImpairedWater.class, scs);
		if (facilityIW != null){
			char delflag=facilityIW.getFeedbackDeleteFlag()=='Y'?'N':'Y';
			facilityIW.setFeedbackDeleteFlag(delflag);
		    searchDAO.saveObject(facilityIW);
		}				
	}
}
