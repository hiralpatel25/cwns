package gov.epa.owm.mtb.cwns.dao;

import gov.epa.owm.mtb.cwns.model.CwnsInfoRef;
import gov.epa.owm.mtb.cwns.needs.NeedsHelper;

import java.util.Collection;

public interface NeedsDAO extends DAO {
	public Collection getDocumentAndType(String facNum);
	public Collection getAdjustedAmounts(String facilityNumber, Long documentId);	
	public CwnsInfoRef getCwnsInfoRef();
	public Collection getDocumentGroupInfo();
	public Collection getDocumentGroupTypeInfo(String facilityId, String documentId);
	public Collection getDistinctDocumentTypeInfo(String facilityId, String documentId);
	public Collection getDocFacByRES(String documentId);	
	public boolean existDuplicateCostCategory(String facilityId, String documentType);
	public Collection getDocumentByQuery(String facilityNumber, String facilityLocation, NeedsHelper nh, int first, int max);
	public int countDocumentByQuery(String facilityNumber, String facilityLocation, NeedsHelper nh);
}
