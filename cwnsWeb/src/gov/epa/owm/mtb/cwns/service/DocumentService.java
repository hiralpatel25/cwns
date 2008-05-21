package gov.epa.owm.mtb.cwns.service;

import java.util.Collection;

public interface DocumentService {	
	public static final String DOCUMENT_TYPE_INTENDED_USE_PLAN = "01";
	public static final String DOCUMENT_TYPE_CAPITAL_IMPROVEMENT_PLAN = "20";
	public static final String DOCUMENT_TYPE_FACILITY_PLAN = "21";	
	
	public static String DOCUMENT_TYPE_CSO_COST_CURVE_NEEDS="98";
	public Collection getDocumentTypeRefs();

}
