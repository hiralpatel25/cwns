package gov.epa.owm.mtb.cwns.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.service.DocumentService;
import gov.epa.owm.mtb.cwns.model.DocumentTypeRef;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;

public class DocumentServiceImpl extends CWNSService implements DocumentService{
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}
	
	public Collection getDocumentTypeRefs(){
		SortCriteria sortCriteria = new SortCriteria("name",
				SortCriteria.ORDER_ASCENDING);
		ArrayList sortArray = new ArrayList();
		sortArray.add(sortCriteria);
		SearchConditions scs = new SearchConditions();
		Collection documentTypeRefs = searchDAO.getSearchList(DocumentTypeRef.class,new ArrayList(), scs, sortArray);
		return documentTypeRefs;
	}
}
