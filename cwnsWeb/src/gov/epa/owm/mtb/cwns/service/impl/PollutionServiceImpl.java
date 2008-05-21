package gov.epa.owm.mtb.cwns.service.impl;

import gov.epa.owm.mtb.cwns.common.CurrentUser;
import gov.epa.owm.mtb.cwns.common.search.AliasCriteria;
import gov.epa.owm.mtb.cwns.common.search.SearchCondition;
import gov.epa.owm.mtb.cwns.common.search.SearchConditions;
import gov.epa.owm.mtb.cwns.common.search.SortCriteria;
import gov.epa.owm.mtb.cwns.dao.SearchDAO;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityPollutionProblem;
import gov.epa.owm.mtb.cwns.model.FacilityPollutionProblemId;
import gov.epa.owm.mtb.cwns.model.PollutionCategoryGroupRef;
import gov.epa.owm.mtb.cwns.model.PollutionProblemRef;
import gov.epa.owm.mtb.cwns.pollution.PollutionCategoryGroupProblemListHelper;
import gov.epa.owm.mtb.cwns.pollution.PollutionGroupHelper;
import gov.epa.owm.mtb.cwns.pollution.PollutionProblemHelper;
import gov.epa.owm.mtb.cwns.service.FacilityService;
import gov.epa.owm.mtb.cwns.service.PollutionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class PollutionServiceImpl extends CWNSService implements PollutionService {
	
	private SearchDAO searchDAO;
	public void setSearchDAO(SearchDAO dao){
		searchDAO = dao;
	}	
	private FacilityService facilityService;

	public void setFacilityService(FacilityService fs) {
		facilityService = fs;
	}
	
	public Collection getPollutionGroupInfo(String facilityId)
	{
		ArrayList pgiList = new ArrayList();
		
		ArrayList columnsCat = new ArrayList();
		columnsCat.add("pollutionCategoryGroupId");
		columnsCat.add("name");
		
	    SortCriteria sortCriteriaCat = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
	    ArrayList sortArrayCat = new ArrayList();
	    sortArrayCat.add(sortCriteriaCat);	  		

		List CategoryList = searchDAO.getSearchList(PollutionCategoryGroupRef.class, columnsCat, new SearchConditions(), sortArrayCat);
    	Iterator iter = CategoryList.iterator();
		
    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    		PollutionGroupHelper pgh = new PollutionGroupHelper();
    		pgh.setPollutionGroupId(((Long)obj[0]).longValue());
    		pgh.setPollutionGroupName((String)obj[1]);
    		
    		pgiList.add(pgh);
    	}		
    	
		return pgiList;
	}
	
	public void updatePollutionRecords(String facilityId, String commaDelimitedString, CurrentUser user)
	{
		String userId = user.getUserId();
		
		ArrayList existingPPList = (ArrayList)getFacilityPollutionProblems(facilityId);

		boolean dataDirtyFlag = false;
		
		if(commaDelimitedString!=null)
		{
		     StringTokenizer tokenizer = new StringTokenizer(commaDelimitedString, ",");
		     
		     while (tokenizer.hasMoreTokens()) {

				String str = tokenizer.nextToken();
		    	
				Iterator iter = existingPPList.iterator();
				
				boolean matchExisting = false;
				
		    	while ( iter.hasNext() ) {
		    		
		    		PollutionProblemHelper pp = (PollutionProblemHelper)iter.next();
		    		
		    		if((new Long(str)).longValue() == pp.getPollutionProblemId())
		    		{
		    			//one of the existing - no touch
		    			matchExisting = true;
		    			existingPPList.remove(pp);
		    			break;
		    		}
		    	}				
		    	
		    	if(!matchExisting)
		    	{
		    		//insert this problemId
		    		insertFacilityPollution((new Long(facilityId)).longValue(), (new Long(str)).longValue(), userId);
		    		dataDirtyFlag = true;
		    	}
			}
		}
		
		
		// now the remaining ones in the existingPPList should be deleted

		Iterator iter = existingPPList.iterator();

    	while ( iter.hasNext() ) {	
    		PollutionProblemHelper pp = (PollutionProblemHelper)iter.next();
    		
    		// delete pp
    		deleteFacilityPollution((new Long(facilityId)).longValue(), pp.getPollutionProblemId());
    		dataDirtyFlag = true;
    	}		
		
    	if(dataDirtyFlag)
    	{
    		facilityService.performPostSaveUpdates(new Long(facilityId), facilityService.DATA_AREA_POLLUTION, user);
    	}
	}
	
	private void insertFacilityPollution(long facilityId, long pollutionId, String userId)
	{
		FacilityPollutionProblem fp = new  FacilityPollutionProblem();
		
		Facility objFac = facilityService.findByFacilityId(new Long(facilityId).toString());
		
		fp.setId(new FacilityPollutionProblemId(facilityId, pollutionId));
		fp.setFacility(objFac);
		fp.setPollutionProblemRef((PollutionProblemRef)searchDAO.getObject(PollutionProblemRef.class, new Long(pollutionId)));
		fp.setLastUpdateUserid(userId);
		fp.setLastUpdateTs(new Date());

		searchDAO.saveObject(fp);
	}
	
	private void deleteFacilityPollution(long facilityId, long pollutionId)
	{
		// checking CSO
		ArrayList aliasArray = new ArrayList();
		aliasArray.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));	
		aliasArray.add(new AliasCriteria("pollutionProblemRef", "ppr", AliasCriteria.JOIN_INNER));	

		SearchConditions scs = new SearchConditions(new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));
		scs.setCondition(SearchCondition.OPERATOR_AND, 
				new SearchCondition("ppr.pollutionProblemId", SearchCondition.OPERATOR_EQ, new Long(pollutionId)));				        				

		FacilityPollutionProblem fpp = (FacilityPollutionProblem)searchDAO.getSearchObject(FacilityPollutionProblem.class, scs, aliasArray);	
		
		searchDAO.removeObject(fpp);
	}
	
	public Collection getPollutionGroupProblemInfo(String facilityId)
	{
		ArrayList pgpiList = new ArrayList();
		
		ArrayList columnsCat = new ArrayList();
		columnsCat.add("pcg.pollutionCategoryGroupId");
		columnsCat.add("pcg.name");
		columnsCat.add("pollutionProblemId");
		columnsCat.add("name");

		ArrayList aliasArrayCat = new ArrayList();
		aliasArrayCat.add(new AliasCriteria("pollutionCategoryGroupRef", "pcg", AliasCriteria.JOIN_INNER));

	    ArrayList sortArrayCat = new ArrayList();		
	    //SortCriteria sortCriteriaCat = new SortCriteria("pcg.pollutionCategoryGroupId", SortCriteria.ORDER_ASCENDING);	
	    //sortArrayCat.add(sortCriteriaCat);
	    SortCriteria sortCriteriaCat = new SortCriteria();
	    sortCriteriaCat = new SortCriteria("pcg.pollutionCategoryGroupId", SortCriteria.ORDER_ASCENDING);	
	    sortArrayCat.add(sortCriteriaCat);
	    sortCriteriaCat = new SortCriteria();
	    sortCriteriaCat = new SortCriteria("name", SortCriteria.ORDER_ASCENDING);	
	    sortArrayCat.add(sortCriteriaCat);

		List CategoryList = searchDAO.getSearchList(PollutionProblemRef.class, columnsCat, new SearchConditions(), sortArrayCat, aliasArrayCat);
    	Iterator iter = CategoryList.iterator();
		
    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    		PollutionCategoryGroupProblemListHelper pcgp = new PollutionCategoryGroupProblemListHelper();
    		pcgp.setPollutionCategoryGroupId(((Long)obj[0]).longValue());
    		pcgp.setPollutionCategoryGroupName(((String)obj[1]));
    		pcgp.setPollutionProblemId(((Long)obj[2]).longValue());
    		pcgp.setPollutionProblemName(((String)obj[3]));    
    		
    		pgpiList.add(pcgp);
    	}		
		
		return pgpiList;
	}	
	
	public Collection getFacilityPollutionProblems(String facilityId)
	{
		ArrayList pgpiList = new ArrayList();
		
		ArrayList columnsCat = new ArrayList();
		columnsCat.add("ppr.pollutionProblemId");
		columnsCat.add("ppr.name");

		ArrayList aliasArrayCat = new ArrayList();
		aliasArrayCat.add(new AliasCriteria("pollutionProblemRef", "ppr", AliasCriteria.JOIN_INNER));
		aliasArrayCat.add(new AliasCriteria("facility", "f", AliasCriteria.JOIN_INNER));

	    ArrayList sortArrayCat = new ArrayList();		
	    SortCriteria sortCriteriaCat = new SortCriteria("ppr.name", SortCriteria.ORDER_ASCENDING);	
	    sortArrayCat.add(sortCriteriaCat);

		SearchConditions scs4 = new SearchConditions(
			    new SearchCondition("f.facilityId", SearchCondition.OPERATOR_EQ, new Long(facilityId)));	    
	    
		List CategoryList = searchDAO.getSearchList(FacilityPollutionProblem.class, columnsCat, scs4, sortArrayCat, aliasArrayCat);
    	Iterator iter = CategoryList.iterator();
		
    	while ( iter.hasNext() ) {
    		Object[] obj = (Object[])iter.next();
    		PollutionProblemHelper pph = new PollutionProblemHelper();
    		
    		pph.setPollutionProblemId(((Long)obj[0]).longValue());
    		pph.setPollutionProblemName((String)obj[1]);
    		
    		pgpiList.add(pph);
    	}		
		
		return pgpiList;		
	}
	
	public Collection getPollutionProblemInfo(Collection pollutionCategoryGroupProblemList, Collection facPolluProbList)
	{
		ArrayList ppiList = new ArrayList();

    	Iterator iter = pollutionCategoryGroupProblemList.iterator();
		
    	while ( iter.hasNext() ) {
    		PollutionCategoryGroupProblemListHelper pcgp = (PollutionCategoryGroupProblemListHelper)iter.next();

        	Iterator iter0 = facPolluProbList.iterator();
    		
        	boolean existingProb = false;
        	
        	while ( iter0.hasNext() ) {
        		PollutionProblemHelper pph0 = (PollutionProblemHelper)iter0.next();
        		if(pph0.getPollutionProblemId() == pcgp.getPollutionProblemId())
        		{
        			existingProb = true;
        			break;
        		}
        	}
    		
        	if(!existingProb)
        	{
	    		PollutionProblemHelper pph = new PollutionProblemHelper();
	    		
	    		pph.setPollutionProblemId(pcgp.getPollutionProblemId());
	    		pph.setPollutionProblemName(pcgp.getPollutionProblemName());
	    		
	    		ppiList.add(pph);
        	}
    	}	
    	
    	return ppiList;
	}
}