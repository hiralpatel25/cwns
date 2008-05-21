package gov.epa.owm.mtb.cwns.service;

import java.util.Collection;

import gov.epa.owm.mtb.cwns.model.CombinedSewer;
import gov.epa.owm.mtb.cwns.model.CombinedSewerStatusRef;
import gov.epa.owm.mtb.cwns.model.CsoImperviousness;


public interface CSOService {
	
	public static final char COMBINED_SEWER_STATUS_NONE='N';
	public static final char COMBINED_SEWER_STATUS_DOCUMENTED='D';
	public static final char COMBINED_SEWER_STATUS_REQUIRES_COST_CURVE='C';
	public static final char COMBINED_SEWER_STATUS_BOTH='B';
	
	
	// Get Facility CSO Info
	public CombinedSewer getFacilityCSOInfo(Long facilityId);
	
	//get status references
	public Collection getCSOStatusReference();
	
	public CsoImperviousness getCSOImperviousnessByFacilityId(Long facilityId);
	
	//	get status references by status ID
	public CombinedSewerStatusRef getCSOStatusReferenceById(String statusId);

	//	save combined sewer object
	public void saveCSO(CombinedSewer cs);
	
	//gets the total combined population
	public int getTotalCSOPopulation(Long facilityId);

	public void costCurveRerun(Long facilityId);

}
