package gov.epa.owm.mtb.cwns.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import gov.epa.owm.mtb.cwns.common.Entity;
import gov.epa.owm.mtb.cwns.dao.SummaryDAO;
import gov.epa.owm.mtb.cwns.facility.FacilityListHelper;
import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.service.SummaryService;
import gov.epa.owm.mtb.cwns.summary.FacilityCommentHelper;
import gov.epa.owm.mtb.cwns.summary.SummaryHelper;

public class SummaryServiceImpl extends CWNSService implements SummaryService {
	
	private SummaryDAO summaryDAO;
		
	public void setSummaryDAO(SummaryDAO dao){
		summaryDAO = dao;
	}	
	    
	public String findReviewStatusRefByFacilityIdAndVersionCode(String facNum, String facilityVersionCode) {
		return summaryDAO.findReviewStatusRefByFacilityIdAndVersionCode(facNum,facilityVersionCode);
	}

	public Collection findSummaryByFacilityId(String facNum, String reviewstatustype, String showChanged) {
		//if (showcostcurveerrors != null && showcostcurveerrors.equals("Y")){
			//return findCostCurveErrorsByFacilityId(facNum,reviewstatustype);
		//}
		//String reviewstatustype = findReviewStatusRefByFacilityId(facNum);
        Collection summary = summaryDAO.findSummaryByFacilityId(facNum);
		Collection costcurves = summaryDAO.findFacilityCostcurveByFacilityId(facNum);
        
        Date FRS_lastupdated;
        Date FES_lastupdated;
        String correction = "";
		Collection summaryHelpers = new ArrayList();
		Iterator iter1 = summary.iterator();
		Object[] s = null;
		while (iter1.hasNext()) {
			s = (Object[]) iter1.next(); 
			SummaryHelper SH = new SummaryHelper(
					                        (String)s[0],
					                        ((Character)s[1]).toString(),
					                        ((Character)s[2]).toString(),
					                        ((Character)s[3]).toString());
			
			if (reviewstatustype.equalsIgnoreCase("FRR") || reviewstatustype.equalsIgnoreCase("FRC") || 
					reviewstatustype.equalsIgnoreCase("SCR") || reviewstatustype.equalsIgnoreCase("FA")) { 
				correction = summaryDAO.findCorrectionsByFacilityId(facNum,(String)s[0]);
			    SH.setCorrection(correction);
			}
		// if user has Federal Review Access level and review status type is FRC 
		//find values for 'changed' column
			if ("Y".equalsIgnoreCase(showChanged)){
				FRS_lastupdated = summaryDAO.findLatestFacilityReviewStatus(facNum,reviewstatustype);
				FES_lastupdated = summaryDAO.findFacilityEntryStatusLastupdatedts(facNum,(String)s[0]);
				if (FRS_lastupdated!=null && FES_lastupdated!=null && FRS_lastupdated.before(FES_lastupdated))
					SH.setError("Y");
				else
					SH.setError("N");
			}
			
		summaryHelpers.add(SH);
		}
		if (costcurves!=null&&costcurves.size()>0)
			assignCostCurveErrors(summaryHelpers,facNum);
		return summaryHelpers;
	}

	private void assignCostCurveErrors(Collection summaryHelpers, String facNum) {
		Collection ccerrors = summaryDAO.findCostCurveErrorsByFacilityId(facNum);
		Iterator iter1 = summaryHelpers.iterator();
		Iterator iter2 = ccerrors.iterator();
		while (iter1.hasNext()){
			SummaryHelper SH = (SummaryHelper)iter1.next();
			Object[] s = (Object[]) iter2.next();
			SH.setCcerror(((Character)s[1]).toString());
		}
	}

	public Collection findCostCurveErrorsByFacilityId(String facNum, String reviewstatustype) {
        /*
        Collection summary = summaryDAO.findCostCurveErrorsByFacilityId(facNum);
		Collection costcurves = this.findFacilityCostcurveByFacilityId(facNum);
        
		Collection summaryHelpers = new ArrayList();
		Iterator iter = summary.iterator();
		
		if (costcurves.size()>1){
			while (iter.hasNext()) {
			     Object[] s1 = (Object[]) iter.next();
			     Object[] s2 = (Object[]) iter.next();
			     String name = "";
			     String errorflag = "";
			     if (s1[0].equals(s2[0])){
			    	 name = (String)s1[0];
			    	 if (s1[1].equals(s2[1])){
			    		 
			    		 errorflag = ((Character)s1[1]).toString();
			    	 }
			    	 else
			    		 if ((((Character)s1[1]).toString()).equals("Y"))
			    			 errorflag = ((Character)s1[1]).toString();
			    		 else
			    			 errorflag = ((Character)s2[1]).toString();
			     }
			    	 
			     SummaryHelper SH = new SummaryHelper(name,"","",errorflag);
				 summaryHelpers.add(SH);
		      }
		}
		else{
		      while (iter.hasNext()) {
			     Object[] s = (Object[]) iter.next();
			     SummaryHelper SH = new SummaryHelper(
					                        (String)s[0],"","",
					                        ((Character)s[1]).toString());
		         summaryHelpers.add(SH);
		      }
		}
		*/
		Collection summary = summaryDAO.findCostCurveErrorsByFacilityId(facNum);
		/*
		Collection summaryHelpers = new ArrayList();
		Iterator iter = summary.iterator();
		while (iter.hasNext()) {
			Object[] s = (Object[]) iter.next();
		     SummaryHelper SH = new SummaryHelper(
				                        (String)s[0],"","",
				                        ((Character)s[1]).toString());
	         summaryHelpers.add(SH);
		}
		*/
		return summary;
		
	}

	public void save(String facId, String[] dataAreaTypes) {
		summaryDAO.save(facId, dataAreaTypes);
		
	}
    
	/*
	public long findFacilityIdByCWNSNumber(String cwnsNbr) {
		return summaryDAO.findFacilityIdByCWNSNumber(cwnsNbr);
	}*/

	public Collection findCommentsByFacilityId(String facilityId) {
		
		Collection comments = summaryDAO.findCommentsByFacilityId(facilityId);
		Collection facilityCommentHelpers = new ArrayList();
		Iterator iter = comments.iterator();	
		SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			FacilityCommentHelper FCH = new FacilityCommentHelper (
					                                       (String)obj[0],
					                                       df.format((Date)obj[1]),
		                                                   (String)obj[2]);	
			facilityCommentHelpers.add(FCH); 
			
		}	
		return facilityCommentHelpers;
		
	}

	public Collection findFacilityCostcurveByFacilityId(String facilityId) {
		Collection faccostcurves = summaryDAO.findFacilityCostcurveByFacilityId(facilityId);
		return faccostcurves;
	}

	public HashMap findLastUpdatedUserIdAndDate(String facilityId){
		Collection userIdAndDate = summaryDAO.findLastUpdatedUserIdAndDate(facilityId);
		SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
		Iterator iter = userIdAndDate.iterator();
		HashMap map = new HashMap();
		while (iter.hasNext()){
			Object[] obj = (Object[])iter.next();
			map.put("lastUpdateUserid",(String)obj[0]);
			map.put("lastUpdateTs", df.format((Date)obj[1]));
		}
		return map;
	}

	public HashMap findLastReviewedUserIdAndDate(String facilityId) {
		Object[] userIdAndDate = summaryDAO.findLastReviewedUserIdAndDate(facilityId);
		SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
		HashMap map = new HashMap();
		if (!(userIdAndDate==null)){
			map.put("lastUpdateUserid",(String)userIdAndDate[0]);
			map.put("lastUpdateTs", df.format((Date)userIdAndDate[1]));
		}
		return map;
	}
}
