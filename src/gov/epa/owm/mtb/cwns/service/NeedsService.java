package gov.epa.owm.mtb.cwns.service;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.documentSearch.DocumentSearchForm;
import gov.epa.owm.mtb.cwns.model.Document;
import gov.epa.owm.mtb.cwns.needs.NeedsForm;

import java.util.Collection;

public interface NeedsService {
	
	public Collection getNeedsInfo(String facNum);

	public void saveNeedsInfo(String sFacilityId, String user);

	public void deleteFacilityDocument(String facilityId, String documentId, CurrentUser user);

	public void saveOrUpdateNeedsInfo(String facilityId, NeedsForm form, String userId);
	
	public boolean costExists(Long facilityId, Long documentId);
	
	public void prepareFacilityDocument(String facilityId, String documentId, NeedsForm form);

	public Collection getDocumentGroupInfo();
	
	public Collection getDocumentGroupTypeInfo(String facilityId, String documentId);
	
	public boolean isBaseMonthYearValid(String monthYear);
	
	public boolean datesUpdatable(String documentId);
	
	public boolean uploadDocument(String documentId, String filename, byte[] file);
	
	public Collection findDocumentsByKeywords(DocumentSearchForm form, String locationId, int first, int max);
	
	public int countDocumentsByKeywords(DocumentSearchForm form, String locationId);
	
	public boolean associateDocumentWithFacility(String facilityId, String[] docids, CurrentUser user);

	public Document getDocument(long documentId);

	public void setFeedBackDeleteFlag(String facNum, String documentId, char feedBackDeleteFlg);
	
	public boolean existDuplicateCostCategory(String facilityId, String documentType);

	public boolean uploadFaxDocument(String cwnsNbr, byte[] fileByteArray, String userName, String dateReceivedAsString);
	
	public boolean documentedCostExists(Long facilityId, Long documentId, Collection costCatList);
	
	public boolean facilityHasDocumentType(Long facilityId, String documentTypeId);
	
	public boolean containsCSOCostCurveDocType(Collection documentIdList);
	
	public int getExpiredNeedsDocumentsSize(Long facilityId);
}