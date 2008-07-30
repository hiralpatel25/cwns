package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CWNSProperties;
import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.exceptions.CWNSCheckedException;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.dao.NeedsDAO;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.documentSearch.DocumentSearchForm;
import gov.epa.owm.mtb.cwns.model.AdministrativeMessageRef;
import gov.epa.owm.mtb.cwns.model.Announcement;
import gov.epa.owm.mtb.cwns.model.AnnouncementId;
import gov.epa.owm.mtb.cwns.model.Cost;
import gov.epa.owm.mtb.cwns.model.CostIndexRef;
import gov.epa.owm.mtb.cwns.model.CwnsInfoRef;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.model.DocumentTypeRef;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurve;
import gov.epa.owm.mtb.cwns.model.FacilityCostCurveDataArea;
import gov.epa.owm.mtb.cwns.model.FacilityDocument;
import gov.epa.owm.mtb.cwns.model.FacilityDocumentId;
import gov.epa.owm.mtb.cwns.model.FacilityEntryStatus;
import gov.epa.owm.mtb.cwns.model.FacilityImpairedWater;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatus;
import gov.epa.owm.mtb.cwns.model.FacilityReviewStatusId;
import gov.epa.owm.mtb.cwns.model.ImpairedWater;
import gov.epa.owm.mtb.cwns.model.ReviewStatusRef;
import gov.epa.owm.mtb.cwns.needs.NeedsDocumentGroupHelper;
import gov.epa.owm.mtb.cwns.needs.NeedsDocumentGroupTypeHelper;
import gov.epa.owm.mtb.cwns.needs.NeedsForm;
import gov.epa.owm.mtb.cwns.needs.NeedsHelper;
import gov.epa.owm.mtb.cwns.service.AnnouncementService;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.NeedsService;
import gov.epa.owm.mtb.edms.DocumentAttribute;
import gov.epa.owm.mtb.edms.EdmsDocumentServicesImpl;
import gov.epa.owm.mtb.edms.EdmsDocumentServicesImplService;
import gov.epa.owm.mtb.edms.EdmsDocumentServicesImplServiceLocator;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

public class NeedsServiceImpl extends CWNSService implements NeedsService {

	public static final String DATEFORMAT =  "MM/dd/yyyy";
	public static final String MONTHYEARFORMAT = "MM/yyyy";
	
	private NeedsDAO needsDAO;
	private SearchDAO searchDAO;
	private FacilityService facilityService;

	public void setFacilityService(FacilityService fs) {
		facilityService = fs;
	}
	
	public void setNeedsDAO(NeedsDAO dao){
		needsDAO = dao;
	}
	
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	
	public boolean isBaseMonthYearValid(String monthYear)
	{
		int indexOfSlash = monthYear.indexOf("/");
		
		if(indexOfSlash < 1)
			return false;
		
		SearchConditions scs =  
			new SearchConditions(new SearchCondition("indexMonth", 
										SearchCondition.OPERATOR_EQ, new Byte(monthYear.substring(0, indexOfSlash))));
		
		scs.setCondition(SearchCondition.OPERATOR_AND, 
							new SearchCondition("indexYear", SearchCondition.OPERATOR_EQ, 
									new Short(monthYear.substring(indexOfSlash+1, monthYear.length()))));

		Collection results = searchDAO.getSearchList(CostIndexRef.class, scs);
		if(results.iterator().hasNext())
		   return true;
		else
			return false;
	}
	
	public Collection getDocumentGroupInfo()
	{
		ArrayList docGroupList = new ArrayList();
		
		Collection docGroupInfoList = needsDAO.getDocumentGroupInfo();
		
    	Iterator iter = docGroupInfoList.iterator();
    	
    	while ( iter.hasNext() ) {
    	    Map map = (Map) iter.next();
    	    
    		NeedsDocumentGroupHelper ndgh = new NeedsDocumentGroupHelper();
    	    
    		ndgh.setGroupId(  ((Long)map.get("documentGroupId")).longValue()  );
    		ndgh.setGroupDesc(  (String)map.get("name")  );

    	    docGroupList.add(ndgh);
    	}
		
		return docGroupList;
	}
	
	public Collection getDocumentGroupTypeInfo(String facilityId, String documentId)
	{
		boolean hasCSODoc = facilityHasDocumentType(new Long(facilityId), "98");
		ArrayList docGroupTypeList = new ArrayList();		

		// first: construct a list consist of all docymentTypes
		Collection distinctDocTypeInfoList = needsDAO.getDistinctDocumentTypeInfo(facilityId, documentId);		
    	Iterator iter0 = distinctDocTypeInfoList.iterator();
    	int i=0;
    	
    	while ( iter0.hasNext() ) {
    	    Map map = (Map) iter0.next();
    	    
    	    String docTypeId = (String)map.get("documentTypeId");
    	    
    	    if((documentId.equals("-9999") && !docTypeId.equals("11") && !(hasCSODoc && docTypeId.equals("98"))) 
    	    		|| (!documentId.equals("-9999") && !docTypeId.equals("11") && !docTypeId.equals("98")))
    	    {
	    		NeedsDocumentGroupTypeHelper ndgth = new NeedsDocumentGroupTypeHelper();
	    	    
	    		ndgth.setGroupId(0);
	    		ndgth.setGroupTypeDesc(  (String)map.get("documentTypeName")  );
	    		ndgth.setGroupTypeId(  (String)map.get("documentTypeId")  );
	    		ndgth.setGroupIndex(i++);
	    		
	    		docGroupTypeList.add(ndgth);
    	    }
    	}

    	// second: construct a list by group ids    	
		Collection docGroupTypeInfoList = needsDAO.getDocumentGroupTypeInfo(facilityId, documentId);
		
    	Iterator iter = docGroupTypeInfoList.iterator();
    	
    	i=0;
    	long oldGroupId = -999;
    	
    	while ( iter.hasNext() ) {
    	    Map map = (Map) iter.next();

    	    String docTypeId = (String)map.get("documentTypeId");
    	    
    	    if((documentId.equals("-9999") && !docTypeId.equals("11") && !(hasCSODoc && docTypeId.equals("98"))) 
    	    		|| (!documentId.equals("-9999") && !docTypeId.equals("11") && !docTypeId.equals("98")))
    	    {
	    		NeedsDocumentGroupTypeHelper ndgth = new NeedsDocumentGroupTypeHelper();
	    	    
	    		ndgth.setGroupId(  ((Long)map.get("documentGroupId")).longValue() );
	    		if(oldGroupId != ndgth.getGroupId())
	    		{
	    			i = 0;
	    		}
	    		
	    		ndgth.setGroupTypeDesc(  (String)map.get("documentTypeName")  );
	    		ndgth.setGroupTypeId(  (String)map.get("documentTypeId")  );
	    		ndgth.setGroupIndex(i++);
	    		
	    		oldGroupId = ndgth.getGroupId();
	    		
	    		docGroupTypeList.add(ndgth);
    	    }
    	}

		return docGroupTypeList;
	}
	
	public boolean datesUpdatable(String documentId)
	{
		Collection results = needsDAO.getDocFacByRES(documentId);
		
		if(results != null && results.size() > 0)
			return false;
		
		return true;
	}
	
	public Collection getNeedsInfo(String facNum) {
				
		ArrayList needsDocList = new ArrayList();
		
		SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
		
		CwnsInfoRef cwnsInfo = needsDAO.getCwnsInfoRef();
		
		Date submitDocNNpsYear = null, submitDocNpsYear = null,
			 submitDocNeedsNNpsYear = null, submitDocNeedsNpsYear = null;
		
		try
		{
			submitDocNNpsYear = df.parse("01/01/" + Integer.toString(cwnsInfo.getSubmitDocNNpsYear()));
			submitDocNpsYear = df.parse("01/01/" + Integer.toString(cwnsInfo.getSubmitDocNpsYear()));
			submitDocNeedsNNpsYear = df.parse("01/01/" + Integer.toString(cwnsInfo.getSubmitDocNeedsNNpsYear()));
			submitDocNeedsNpsYear = df.parse("01/01/" + Integer.toString(cwnsInfo.getSubmitDocNeedsNpsYear()));
			
		}
		catch (ParseException e) {
            // TODO: need to handle this exception			
			return null;
        }    

		boolean isFacility = facilityService.isFacility(new Long(facNum));
		
		Collection needInfoList = needsDAO.getDocumentAndType(facNum);
        		
    	Iterator iter = needInfoList.iterator();
    	
    	while ( iter.hasNext() ) {
    	    Map map = (Map) iter.next();
    	    
    		NeedsHelper nh = new NeedsHelper();
    	    
    	    Date published_date = (Date) map.get("d_publishedDate");
    	    String dbOutdatedDocCertificatnFlag = map.get("d_outdatedDocCertificatnFlag")==null?"N":((Character)map.get("d_outdatedDocCertificatnFlag")).toString();
    	    String dbAllowFootnoteFlag = ((Character)map.get("dt_allowFootnoteFlag")).toString();
    	    
    	    log.debug("d_documentId: " + (Long)map.get("d_documentId"));
    	    log.debug("d_titleName: " + (String)map.get("d_titleName"));
    	    log.debug("d_authorName: " + (String)map.get("d_authorName"));
    	    log.debug("d_publishedDate: " + df.format(published_date));
    	    log.debug("d_outdatedDocCertificatnFlag: " + dbOutdatedDocCertificatnFlag);
    	    log.debug("dt_documentTypeRefId: " + map.get("dt_documentTypeRefId"));
    	    log.debug("dt_documentTypeRefName: " + (String)map.get("dt_documentTypeRefName")); 
    	    log.debug("dt_allowFootnoteFlag: " + dbAllowFootnoteFlag); 
    	    Collection facDocuments = needsDAO.getDocFacByRES(((Long)map.get("d_documentId")).toString());
    	    if (facDocuments!=null&&facDocuments.size()>0)
    	    	nh.setIsDocumentReload("N");
    	    else
    	    	nh.setIsDocumentReload("Y");
    	    nh.setDocumentId(((Long)map.get("d_documentId")).longValue());
    	    nh.setDocumentTitle((String)map.get("d_titleName"));
    	    nh.setDocumentTypeId((String)map.get("dt_documentTypeRefId"));
    	    nh.setDocumentTypeDesc((String)map.get("dt_documentTypeRefName"));
    	    nh.setPublishedDate(df.format(published_date));
    	    nh.setAuthorName((String)map.get("d_authorName"));
    	    nh.setRepositoryId((String)map.get("d_repositoryId"));
    	    nh.setOutdatedDocCertificatnFlag(dbOutdatedDocCertificatnFlag);
    	    Character dbFeedbackDeleteFlag = (Character)map.get("feedbackDeleteFlag");
    	    if (dbFeedbackDeleteFlag!=null)
    	       nh.setFeedbackDeleteFlag(dbFeedbackDeleteFlag.toString());
    	    if(nh.getDocumentTypeId().equals("11") || nh.getDocumentTypeId().equals("98"))
    	    {
    	    	nh.setIsDocumentUpdatable("N");
    	    }
    	    else
    	    {
    	    	nh.setIsDocumentUpdatable("Y");
    	    }
    	    
    	    nh.setHasCost(costExists(new Long(facNum), (Long)map.get("d_documentId"))?"Y":"N");
    	    
    	    Collection adjustAmountList = needsDAO.getAdjustedAmounts(facNum, (Long)map.get("d_documentId"));
    	    
        	Iterator iterAdjustedAmt = adjustAmountList.iterator();

        	boolean submitFlag = false, footnoteableFlag = false;
        	
        	while ( iterAdjustedAmt.hasNext() ) {
        	    Map mapAdjustedAmt = (Map) iterAdjustedAmt.next();
        	    
        	    long adjustedAmount = ((Long)mapAdjustedAmt.get("adjust_amount_sum")).longValue();
        	    String needTypeId = (String)mapAdjustedAmt.get("needTypeId");
        	    
        	    log.debug("adjust_amount_sum: " + adjustedAmount);
        	    log.debug("needTypeId: " + needTypeId);   
        	            	    
        	    if(needTypeId.equals("S")==true && adjustedAmount > 0)
        	    {
            	    nh.setHasSSEAmount("Y");
            	    nh.setSseAmount(adjustedAmount);        	    	
        	    }
        	    
        	    if(needTypeId.equals("F")==true && adjustedAmount > 0)
        	    {
        	    	
            	    nh.setHasFederalAmount("Y");
            	    nh.setFederalAmount(adjustedAmount);
        	    	
        	    	if(dbOutdatedDocCertificatnFlag.equalsIgnoreCase("N")
        	    			&& dbAllowFootnoteFlag.equalsIgnoreCase("Y"))
        	    	{
        	    		footnoteableFlag = true;
        	    	}
        	    	
        	    	if(adjustedAmount > cwnsInfo.getSubmitDocNeedsAmount())
        	    	{
	        	    	if(!isFacility)
	        	    	{
	        	    		if(published_date.before(submitDocNeedsNpsYear))
	        	    		{
	        	    			submitFlag = true;
	        	    		}
	        	    	}
	        	    	else
	        	    	{
	        	    		if(published_date.before(submitDocNeedsNNpsYear))
	        	    		{
	        	    			submitFlag = true;
	        	    		}	        	    		
	        	    	}
        	    	}
        	    	else
        	    	{
	        	    	if(!isFacility)
	        	    	{
	        	    		if(published_date.before(submitDocNpsYear))
	        	    		{
	        	    			submitFlag = true;
	        	    		}
	        	    	}
	        	    	else
	        	    	{
	        	    		if(published_date.before(submitDocNNpsYear))
	        	    		{
	        	    			submitFlag = true;
	        	    		}	        	    		
	        	    	}
        	    	} // else (adjustedAmount > cwnsInfo.getSubmitDocNeedsAmount())
        	    }
        	} // while amounts
        	
    	    nh.setSubmitFlag(submitFlag?"Y":"N");
    	    nh.setFootnoteableFlag(footnoteableFlag?"Y":"N");
        	
        	needsDocList.add(nh);
        	
    	} // while document query
    	
		return needsDocList;
	}

	public void prepareFacilityDocument(String facilityId, String documentId, NeedsForm form)
	{
		SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
		SimpleDateFormat dfMMYYYY = new SimpleDateFormat(MONTHYEARFORMAT);		
		
		Document document = (Document)searchDAO.getObject(Document.class, new Long(documentId));
		
		if(document == null)
		{
			//TODO: throw exception: serious error	
			return;
		}
		
		form.setAuthor(document.getAuthorName());
		form.setBaseMonthYear(dfMMYYYY.format(document.getBaseDesignDate()));
		form.setDocumentId(documentId);
		form.setDocumentType(document.getDocumentTypeRef().getDocumentTypeId());
		form.setDocumentTypeDesc(document.getDocumentTypeRef().getName());
		form.setNarrativeText(document.getDescription());
		form.setNeedsStartYear(document.getNeedStartYear()==null?"":document.getNeedStartYear().toString());
		form.setOngoingDialogCheckbox(document.getOutdatedDocCertificatnFlag()==null?"N":document.getOutdatedDocCertificatnFlag().toString());
		form.setOthersCheckbox(document.getAvailableToPublicFlag()==null?"N":document.getAvailableToPublicFlag().toString());
		form.setPublishedDate(df.format(document.getPublishedDate()));
		form.setTargetDesignYear(document.getTargetDesignYear()==null?"":document.getTargetDesignYear().toString());
		form.setTitle(document.getTitleName());
	}
	/*
	 * (non-Javadoc)
	 * @see gov.epa.owm.mtb.cwns.service.NeedsService#saveOrUpdateNeedsInfo(java.lang.String, gov.epa.owm.mtb.cwns.needs.NeedsForm, java.lang.String)
	 * @param form - contains ket ids such as documentId as well as other doc detail info
	 * 			   - if form.documentId is null, it is a new document
	 */
	public void saveOrUpdateNeedsInfo(String facilityId, NeedsForm form, String userId)
	{
		String docId = form.getDocumentId();
		
		Document document = null;
		
		SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
		SimpleDateFormat dfMMYYYY = new SimpleDateFormat(MONTHYEARFORMAT);		
		
		Date baseDesigndate = null, publishedDate = null;
		
		try
		{
			baseDesigndate = dfMMYYYY.parse(form.getBaseMonthYear());
			publishedDate = df.parse(form.getPublishedDate());	
		}
		catch (ParseException e) {
            // TODO: need to handle this exception			
			return;
        }    

        if(docId == null || docId.trim().equals(""))
        {
        	// new document
            document = new Document();
        }
        else
        {
        	SearchConditions scs = new SearchConditions(new SearchCondition("documentId", SearchCondition.OPERATOR_EQ, new Long(docId)));

        	document = (Document)searchDAO.getSearchObject(Document.class, scs);
        
	        if (document==null){
	
	        	// TODO: something seriously wrong - throw an exception
	        	// new document
	            document = new Document();	        	
	        }
        }
        
        document.setAuthorName(form.getAuthor());
        document.setAvailableToPublicFlag((form.getOthersCheckbox()==null || form.getOthersCheckbox().length() < 1)?
        						new Character('N'):new Character(form.getOthersCheckbox().charAt(0)));
        document.setDescription(form.getNarrativeText());
        document.setOutdatedDocCertificatnFlag((form.getOngoingDialogCheckbox()==null || form.getOngoingDialogCheckbox().length() < 1)?
        						new Character('N'):new Character(form.getOngoingDialogCheckbox().charAt(0)));
        document.setTitleName(form.getTitle());
        document.setPublishedDate(publishedDate);     
        document.setBaseDesignDate(baseDesigndate);
        document.setLastUpdateTs(new Date());
        document.setLastUpdateUserid(userId);        
        document.setLocationId(form.getLocationId());
        document.setNeedStartYear(form.getNeedsStartYear().equals("")?null:new Short(form.getNeedsStartYear()));
        document.setTargetDesignYear(form.getTargetDesignYear().equals("")?null:new Short(form.getTargetDesignYear()));

    	SearchConditions scs0 = new SearchConditions(new SearchCondition("documentTypeId", SearchCondition.OPERATOR_EQ, form.getDocumentType()));
    	DocumentTypeRef dt = (DocumentTypeRef) searchDAO.getSearchObject(DocumentTypeRef.class, scs0);	

        document.setDocumentTypeRef(dt);  
        
        document.setFacilityDocuments(new HashSet());

        searchDAO.saveObject(document);
        
    	Document savedDocument = document;

        log.debug("saved documentId = " + savedDocument.getDocumentId());
        	
    	SearchConditions scs1 = new SearchConditions(new SearchCondition("facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
    	Facility f = (Facility) searchDAO.getSearchObject(Facility.class, scs1);	
        
        FacilityDocumentId fdId = new FacilityDocumentId(savedDocument.getDocumentId(), f.getFacilityId());
               
    	SearchConditions scs2 = new SearchConditions(new SearchCondition("document.documentId", SearchCondition.OPERATOR_EQ, new Long(savedDocument.getDocumentId())));
        scs2.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
    	FacilityDocument fd = (FacilityDocument) searchDAO.getSearchObject(FacilityDocument.class, scs2);

        if (fd==null){
        	fd = new FacilityDocument();
        	fd.setId(fdId);
        }

        fd.setFacility(f);
        fd.setDocument(savedDocument);
        fd.setLastUpdateTs(new Date());
        fd.setLastUpdateUserid(userId);
        fd.setFeedbackDeleteFlag('N'); //TODO: what's the default value? 
        savedDocument.getFacilityDocuments().add(fd);
        
        searchDAO.saveObject(fd);
        
        saveNeedsInfo(facilityId, userId);
        
        String docTypeId = savedDocument.getDocumentTypeRef().getDocumentTypeId();
        
        form.setDocumentTypeDesc(dt.getName());
        
        form.setDocumentId(new Long(savedDocument.getDocumentId()).toString());
        /*
        if(docTypeId.equals("20") && 
        		needsDAO.existDuplicateCostCategory(facilityId, docTypeId))
        {
        	form.setShowCIPWarning("Y");
        }
        else if(docTypeId.equals("01") && 
        		needsDAO.existDuplicateCostCategory(facilityId, docTypeId))
        {
        	form.setShowIUPWarning("Y");
        }
        else if(docTypeId.equals("21") && 
        		needsDAO.existDuplicateCostCategory(facilityId, docTypeId))
        {
        	form.setShowFPWarning("Y");
        }
        */
	}
	
	public void deleteFacilityDocument(String facilityId, String documentId, CurrentUser user)
	{
	  FacilityDocumentId fdId = new FacilityDocumentId(new Long(documentId).longValue(), 
					 new Long(facilityId).longValue());
        SearchConditions scs8 = new SearchConditions(new SearchCondition("id", SearchCondition.OPERATOR_EQ, fdId));
        FacilityDocument fd = (FacilityDocument) searchDAO.getSearchObject(FacilityDocument.class, scs8);
	
        // if local user mark as deleted
	  if(UserServiceImpl.LOCATION_TYPE_ID_LOCAL.equals(user.getCurrentRole().getLocationTypeId())){
			if (fd.getFeedbackDeleteFlag()=='Y') {
				fd.setFeedbackDeleteFlag('N');
   		    }else{
   		    	fd.setFeedbackDeleteFlag('Y');
   		     }   	    		
   		  searchDAO.saveObject(fd);
	  }
	  else{
		
		//remove cost and costclasification
		
		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("facilityDocument.id", 
			    					SearchCondition.OPERATOR_EQ, 
			    					new FacilityDocumentId(new Long(documentId).longValue(), 
			    										   new Long(facilityId).longValue())));
		
	    Collection costList = searchDAO.getSearchList(Cost.class, scs4);
	    
    	Iterator iterCost = costList.iterator();

    	while ( iterCost.hasNext() ) 
    	{
    	    Cost cost = (Cost) iterCost.next();

        	searchDAO.removeObject(cost);
    	}

    	// remove Facility_Cost_curve & Facility_cost_curve_data_area

		SearchConditions scs6 = new SearchConditions(
			    new SearchCondition("facilityDocument.id", 
			    					SearchCondition.OPERATOR_EQ, 
			    					new FacilityDocumentId(new Long(documentId).longValue(), 
			    										   new Long(facilityId).longValue())));
		
	    Collection costCurveList = searchDAO.getSearchList(FacilityCostCurve.class, scs6);
	    
    	Iterator iterCostCurve = costCurveList.iterator();

    	while ( iterCostCurve.hasNext() ) 
    	{
    	    FacilityCostCurve costCurve = (FacilityCostCurve) iterCostCurve.next();
    	    
    		SearchConditions scs7 = new SearchConditions(
    			    new SearchCondition("facilityCostCurve.facilityCostCurveId", 
    			    					SearchCondition.OPERATOR_EQ, new Long(costCurve.getFacilityCostCurveId())));
    		
    		//scs7.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("dataAreaRef.dataAreaId",SearchCondition.OPERATOR_EQ, new Long(10)));
    		
    	    Collection facilityCostCurveDataAreaList = searchDAO.getSearchList(FacilityCostCurveDataArea.class, scs7);
    	    
        	Iterator iterFacilityCostCurveDataArea = facilityCostCurveDataAreaList.iterator();

        	while ( iterFacilityCostCurveDataArea.hasNext() ) 
        	{
        		FacilityCostCurveDataArea facilityCostCurveDataArea = (FacilityCostCurveDataArea) iterFacilityCostCurveDataArea.next();
        		
        		searchDAO.removeObject(facilityCostCurveDataArea);
        	}
    	    
        	searchDAO.removeObject(costCurve);
    	}    	
    	
		// remove FacilityDocument
    	    	
    	searchDAO.removeObject(fd);
    	
    	// delete Document if it is 303d document
    	
    	SearchConditions scs9 = new SearchConditions(
                new SearchCondition("documentId", 
                		            SearchCondition.OPERATOR_EQ, 
                		            new Long(documentId)));

		Document doc = (Document) searchDAO.getSearchObject(Document.class, scs9);
		
		if(doc == null)
		{
			//TODO: throw exception: serious error
			// rollback
			return;
		}
		
		if(doc.getDocumentTypeRef().getDocumentTypeId().equals("11"))
		{
			//delete facility_impaired_water record where list_id_fk is in 
			
			String docTitleName = doc.getTitleName();

	    	SearchConditions scsA = new SearchConditions(
	                new SearchCondition("facility.facilityId", 
	                		            SearchCondition.OPERATOR_EQ, 
	                		            new Long(facilityId)));

			Collection facilityImpairedWaterList = (Collection) searchDAO.getSearchList(FacilityImpairedWater.class, scsA);
			
	    	Iterator iterFacilityImpairedWater = facilityImpairedWaterList.iterator();

	    	while ( iterFacilityImpairedWater.hasNext() ) 
	    	{
	    		FacilityImpairedWater facilityImpairedWater = (FacilityImpairedWater) iterFacilityImpairedWater.next();
	    		
	    		if(facilityImpairedWater!=null && docTitleName.indexOf(facilityImpairedWater.getId().getListId()) >= 0)
	    		{
	    			ImpairedWater impairedWater = facilityImpairedWater.getImpairedWater();
	    			searchDAO.removeObject(facilityImpairedWater);
	    			if(impairedWater!=null){
	    			  SearchConditions scs = new SearchConditions(new SearchCondition("impairedWater.listId",SearchCondition.OPERATOR_EQ,impairedWater.getListId()));
	    			  scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId",SearchCondition.OPERATOR_NOT_EQ,new Long(facilityId)));
	    			  Collection fiw = searchDAO.getSearchList(FacilityImpairedWater.class, scs);
	    			  if(fiw==null || fiw.size()==0)
	    				  searchDAO.removeObject(impairedWater);
	    			}  
	    		}
	    	}						
            
	    	// delete document if it's not linked to another facility
			ArrayList aliasArray = new ArrayList();
			aliasArray.add(new AliasCriteria("document", "d", AliasCriteria.JOIN_INNER));
			SearchConditions scs = new SearchConditions(new SearchCondition("d.documentTypeRef.documentTypeId",SearchCondition.OPERATOR_EQ, "11"));
			scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("facility.facilityId",SearchCondition.OPERATOR_NOT_EQ,new Long(facilityId)));
			Collection fds = searchDAO.getSearchList(FacilityDocument.class, new ArrayList(), scs, new ArrayList(), aliasArray);
			if (fds==null || fds.size()==0){
				searchDAO.removeObject(doc);
			}	
		}
    	    	
    	// save facility wide info    	
        saveNeedsInfo(facilityId, user.getUserId());
	  }  
	}

	public boolean costExists(Long facilityId, Long documentId)
	{
		boolean bExist = false;
				
		SearchConditions scs3 = new SearchConditions(
				    new SearchCondition("facilityDocument.id", 
				    					SearchCondition.OPERATOR_EQ, 
				    					new FacilityDocumentId(documentId.longValue(), 
				    										   facilityId.longValue())));
		int costCount = searchDAO.getCount(Cost.class, scs3);
		
		if(costCount > 0)
		{
			bExist = true;
		}
		
		return bExist;
	}
	
	public boolean documentedCostExists(Long facilityId, Long documentId, Collection costCatList)
	{		
		boolean bExist = false;				
		SearchConditions scs = new SearchConditions(
				    new SearchCondition("facilityDocument.id", 
				    					SearchCondition.OPERATOR_EQ, 
				    					new FacilityDocumentId(documentId.longValue(), 
				    										   facilityId.longValue())));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("categoryRef.categoryId", 
				    					SearchCondition.OPERATOR_IN, costCatList));
		
		List costList = searchDAO.getSearchList(Cost.class, scs);		
		if(costList != null && costList.size()>0){ 
			Iterator costIter = costList.iterator();
			while(costIter.hasNext()){
				Cost cost = (Cost)costIter.next();
				if (cost.getCostMethodCode()=='D'){
					bExist=true;
				}
			}				
		}		
		return bExist;
	}
	
    public void saveNeedsInfo(String sFacilityId, String user) {
		
		SearchConditions scs2 = new SearchConditions(new SearchCondition("id.facilityId", SearchCondition.OPERATOR_EQ, new Long(sFacilityId)));
		scs2.setCondition(SearchCondition.OPERATOR_AND,new SearchCondition("id.dataAreaId",SearchCondition.OPERATOR_EQ,new Long(10)));
		FacilityEntryStatus facilityEntryStatus = (FacilityEntryStatus)searchDAO.getSearchObject(FacilityEntryStatus.class, scs2);
		
		if(facilityEntryStatus != null)
		{
			facilityEntryStatus.setLastUpdateTs(new Date());
			searchDAO.saveObject(facilityEntryStatus);
		}
		else
		{
			//TODO: serious error: throw exception
		}
		
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
    public boolean uploadDocument(String documentId, String filename, byte[] file){
    	
    	
    	Document document = (Document)searchDAO.getObject(Document.class, new Long(documentId));
    	boolean result = false;
		
		if(document == null)
		{
			//TODO: throw exception: serious error	
			return false;
		}
    	
    	
		DocumentAttribute da = new DocumentAttribute();  
		
		DocumentAttribute [] array = new DocumentAttribute[7];
		
		da = new DocumentAttribute();
		da.setKey("name");
		da.setValue(setString(filename));		
		array[0] = da;
		
		da = new DocumentAttribute();
		da.setKey("title");
		da.setValue(setString(document.getTitleName()));		
		array[1] = da;
		
		da = new DocumentAttribute();
		da.setKey("subject");		
		da.setValue(setString(document.getDescription()));
		array[2] = da;
		
		da = new DocumentAttribute();
		da.setKey("author");
		da.setValue(setString(document.getAuthorName()));		
		array[3] = da;	
		
		da = new DocumentAttribute();
		da.setKey("cwns_doc_id");
		da.setValue(setString(String.valueOf(document.getDocumentId())));		
		array[4] = da;
    	
    	try {
//    		String webServicesEndPoint = CWNSProperties.getProperty("edms.webservices.endpoint");    		
//    		EdmsDocumentServiceLocator factory = new EdmsDocumentServiceLocator();			
//			factory.setEdmsDocumentServicesPortEndpointAddress(webServicesEndPoint);			
//			EdmsDocumentServices edms = ((EdmsDocumentService)factory).getEdmsDocumentServicesPort();				

			URL serviceURL = new URL(CWNSProperties.getProperty("edms.webservices.endpoint"));    		
			EdmsDocumentServicesImplService service = new EdmsDocumentServicesImplServiceLocator();
			EdmsDocumentServicesImpl edms = service.getEdmsDocumentServicesImpl(serviceURL);						    		
    		String r_object_id = edms.uploadDocument(file, array);
			
			log.debug("r_object_id = " + r_object_id);
			
			document.setRepositoryId(r_object_id);
			
			searchDAO.saveObject(document);
			
			result = true;
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     	
    	
    	return result;
    }
public Collection findDocumentsByKeywords(DocumentSearchForm form, String facilityLocation, int first, int max){
    	
    	SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);    	
    	    	
    	NeedsHelper nh = populateNeedHelper(form);    	
    	
    	Collection list = needsDAO.getDocumentByQuery(form.getFacilityId(), facilityLocation, nh, first, max);
    	
    	List result = new ArrayList();	
    	
    	Iterator iter = list.iterator();
    	
    	while ( iter.hasNext() ) {
    	    Map map = (Map) iter.next();
    	    
    		nh = new NeedsHelper();    		
    		
    		Date published_date = (Date) map.get("d_publishedDate");
    	    String dbOutdatedDocCertificatnFlag = map.get("d_outdatedDocCertificatnFlag")==null?"N":((Character)map.get("d_outdatedDocCertificatnFlag")).toString();
    	        		
    		nh.setDocumentId(((Long)map.get("d_documentId")).longValue());
    	    nh.setDocumentTitle((String)map.get("d_titleName"));
    	    nh.setDocumentTypeId((String)map.get("dt_documentTypeRefId"));
    	    nh.setDocumentTypeDesc((String)map.get("dt_documentTypeRefName"));
    	    nh.setPublishedDate(df.format(published_date));
    	    nh.setAuthorName((String)map.get("d_authorName"));
    	    nh.setRepositoryId((String)map.get("d_repositoryId"));
    	    nh.setOutdatedDocCertificatnFlag(dbOutdatedDocCertificatnFlag);
    	    
    	    result.add(nh);
    		
    	}
    	
    	return result;
    }  
    public int countDocumentsByKeywords(DocumentSearchForm form, String facilityLocation){
    	
    	NeedsHelper nh = populateNeedHelper(form);
    	
    	return needsDAO.countDocumentByQuery(form.getFacilityId(), facilityLocation, nh);
    }
    
    public boolean associateDocumentWithFacility(String facilityId, String[] docids, CurrentUser user){    	
    	if(docids != null){
    		
//    		List list = new ArrayList();
//    		Collections.addAll(list, docids);
    		List list = Arrays.asList(docids);
    		
    		Iterator iter = list.iterator();
    		
    		while(iter.hasNext()){
    			
    			String docid = (String) iter.next();
    			
    			FacilityDocumentId fdId = new FacilityDocumentId(new Long(docid).longValue(), new Long(facilityId).longValue());
	    		
        		FacilityDocument fd = new FacilityDocument();    		
        		
        		fd.setId(fdId);	
        		//fd.setFacility(f);
                //fd.setDocument(savedDocument);
                fd.setLastUpdateTs(new Date());
                fd.setLastUpdateUserid(user.getUserId());
                fd.setFeedbackDeleteFlag('N'); //TODO: what's the default value?     		
                
                searchDAO.saveObject(fd);
    			
    		}    		
    		
    	}
    	else return false;
    	
    	return true;
    	
    }
    private String setString(String str){
    	
    	if(str == null) return "";
    	
    	return str;    	
    }
    
    private NeedsHelper populateNeedHelper(DocumentSearchForm form){    	
    	NeedsHelper nh = new NeedsHelper();    	
    	nh.setDocumentTypeId(form.getDocType());
    	nh.setKeywords(form.getKeywords());
    	
    	//set start date and end date if only one of them is specified
    	if (form.getDateFrom().length()==0){
    		nh.setPublishDateFrom("01/01/1900");
    	}else{
    		nh.setPublishDateFrom(form.getDateFrom());
    	}    	
    	
    	if (form.getDateTo().length()==0){
    		Calendar calendar = Calendar.getInstance(); 
    		String strToday = (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + 
    								calendar.get(Calendar.YEAR);
    		nh.setPublishDateTo(strToday);
    	}else{
    		nh.setPublishDateTo(form.getDateTo());
    	}    	
    	return nh;
    }

	public Document getDocument(long documentId) {	
		return (Document)searchDAO.getSearchObject(Document.class, new SearchConditions(new SearchCondition("documentId",SearchCondition.OPERATOR_EQ,new Long(documentId))));
	}

	public void setFeedBackDeleteFlag(String facNum, String documentId, char feedBackDeleteFlg) {
        // Get the FacilityDischarge object
		FacilityDocumentId fdId = new FacilityDocumentId(new Long(documentId).longValue(), 
					 new Long(facNum).longValue());

        SearchConditions scs8 = new SearchConditions(new SearchCondition("id", 
		            SearchCondition.OPERATOR_EQ, fdId));

        FacilityDocument fd = (FacilityDocument) searchDAO.getSearchObject(FacilityDocument.class, scs8);
		if (fd != null){
			fd.setFeedbackDeleteFlag(feedBackDeleteFlg);
			searchDAO.saveObject(fd);
		}
		
	}

	public boolean existDuplicateCostCategory(String facilityId, String documentType) {
		// TODO Auto-generated method stub
		return needsDAO.existDuplicateCostCategory(facilityId, documentType);
	}
	
	public boolean uploadFaxDocument(String cwnsNbr, byte[] fileByteArray, String userName, String dateReceivedAsString) {
		Facility facility = facilityService.getFacilityByCwnsNbr(cwnsNbr);
		if(facility==null)return false;
		long documentId =0;
		try {
			documentId = saveDocument(facility.getFacilityId(), userName, dateReceivedAsString);
			saveFacilityDocument(facility.getFacilityId(), documentId, "fax");	
			return uploadDocument(String.valueOf(documentId), cwnsNbr+"Fax.pdf", fileByteArray);
		} catch (CWNSCheckedException e) {
			log.error("Error occured while creating a fax document" + e.getMessage());
			return false;
		} 
	}	
	
	private void saveFacilityDocument(long facilityId, long documentId, String userId){
		FacilityDocument facilityDocument = new FacilityDocument();
		FacilityDocumentId id = new FacilityDocumentId(documentId, facilityId);
		facilityDocument.setId(id);
		facilityDocument.setFeedbackDeleteFlag('N');
		facilityDocument.setLastUpdateTs(new Date());
		facilityDocument.setLastUpdateUserid(userId);
		searchDAO.saveObject(facilityDocument);
		//add an anncouncement
		Announcement a = new Announcement();
		Date now = new Date();
		AnnouncementId aId = new AnnouncementId(AnnouncementService.ADMIN_MESSAGE_ID,now);
		Facility f = facilityService.findByFacilityId(String.valueOf(facilityId));
		a.setId(aId);
		a.setLastUpdateUserid(userId);
		a.setLocationId(f.getLocationId());
		a.setDescription("A fax has been received for facility " + f.getCwnsNbr());
		AdministrativeMessageRef administrativeMessageRef = (AdministrativeMessageRef) searchDAO.getObject(AdministrativeMessageRef.class, new Long(AnnouncementService.ADMIN_MESSAGE_ID));
		a.setAdministrativeMessageRef(administrativeMessageRef);
		searchDAO.saveObject(a);		
	}
	
	private long saveDocument(long facilityId, String faxSender, String faxDate)throws CWNSCheckedException
	{
		Facility facility = facilityService.findByFacilityId(new Long(facilityId).toString());
		
		String titleName = "Small Community Form -" + facility.getName();
		
		SimpleDateFormat yyyyMondd = new SimpleDateFormat(DATEFORMAT);	
		SimpleDateFormat yyyyMon = new SimpleDateFormat(MONTHYEARFORMAT);	
				
		Date baseDesigndate = null, publishedDate = null;
		Date today = new Date();
		try
		{
			publishedDate = yyyyMondd.parse(yyyyMondd.format(today));
			baseDesigndate = yyyyMon.parse(yyyyMon.format(today));	
		}
		catch (ParseException e) {			
			throw new CWNSCheckedException("Error occured parsing dates for a fax document");
        }    
		
		Document document = new Document();
		SearchConditions scs1 = new SearchConditions(new SearchCondition("documentTypeId", SearchCondition.OPERATOR_EQ, "71"));
		DocumentTypeRef documentTypeRef = (DocumentTypeRef)searchDAO.getSearchObject(DocumentTypeRef.class, scs1);
		document.setDocumentTypeRef(documentTypeRef);
		document.setTitleName(titleName);
		document.setAuthorName(faxSender);
		document.setLastUpdateTs(new Date());
		document.setLastUpdateUserid("fax");
		document.setPublishedDate(publishedDate);
		document.setBaseDesignDate(baseDesigndate);	
		document.setLocationId(facility.getLocationId());
		searchDAO.saveObject(document);			
		return document.getDocumentId();		
	}
	
	public boolean facilityHasDocumentType(Long facilityId, String documentTypeId){
		boolean hasDocType = false;
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("document", "d", AliasCriteria.JOIN_INNER));		
		SearchConditions scs =  new SearchConditions(new SearchCondition("d.documentTypeRef.documentTypeId", SearchCondition.OPERATOR_EQ, documentTypeId));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId",SearchCondition.OPERATOR_EQ, facilityId));
		Collection lstFacilityDocument = searchDAO.getSearchList(FacilityDocument.class, new ArrayList(), scs, new ArrayList(), aliasArray);			
		
		if (lstFacilityDocument!=null && lstFacilityDocument.size()>0){
			hasDocType = true;
		}
		return hasDocType;		
	}
	
	public boolean containsCSOCostCurveDocType(Collection documentIdList){
		boolean containsCSODocType = false;
		SearchConditions scs = new SearchConditions(new SearchCondition("documentTypeRef.documentTypeId", SearchCondition.OPERATOR_EQ, "98"));
		scs.setCondition(new SearchCondition("documentId", SearchCondition.OPERATOR_IN, documentIdList));
		Collection lstDocType98 = searchDAO.getSearchList(Document.class, scs);
		if (lstDocType98!=null && lstDocType98.size()>0) containsCSODocType = true;
		return containsCSODocType;
	}
	
	public int getExpiredNeedsDocumentsSize(Long facilityId){
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("document", "d", AliasCriteria.JOIN_INNER));		
		SearchConditions scs =  new SearchConditions(new SearchCondition("d.outdatedDocCertificatnFlag", SearchCondition.OPERATOR_EQ, new Character('Y')));
		scs.setCondition(SearchCondition.OPERATOR_OR, new SearchCondition("d.outdatedDocCertificatnFlag",SearchCondition.OPERATOR_IS_NULL));
		scs.setCondition(SearchCondition.OPERATOR_AND, new SearchCondition("id.facilityId",SearchCondition.OPERATOR_EQ, facilityId));
		Collection lstFacilityDocument = searchDAO.getSearchList(FacilityDocument.class, new ArrayList(), scs, new ArrayList(), aliasArray);
		if(lstFacilityDocument==null)
			return 0;
		else return lstFacilityDocument.size();
	}
}
