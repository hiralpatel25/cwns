/**
 * 
 */
package gov.epa.owm.mtb.cwns.summary;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * @author jlaviset
 *
 */
public class SummaryForm extends ActionForm {
	
	private String summaryAct = "none";
	private String showCorrection = "N";
	private String check_box = "no";
	private Collection summHelpers = new ArrayList();
	private Collection facCommentHelpers = new ArrayList();
	private String[] dataAreaTypes = {};
	private String CWNSNbr;
	private String facilityId;
	private String showCostCurve = "";
	private String costCurve; 
	private String showChanged = "N";
	private String lastUpdatedUserName = "";
	private String lastUpdateTS = "";
	private String reviewerName = "";
	private String lastReviewedTS = "";
	private String isFacility = "N";
	private String isFacilityInSewershed ="N";
	
	public String getIsFacilityInSewershed() {
		return isFacilityInSewershed;
	}
	public void setIsFacilityInSewershed(String isFacilityInSewershed) {
		this.isFacilityInSewershed = isFacilityInSewershed;
	}
	public String getIsFacility() {
		return isFacility;
	}
	public void setIsFacility(String isFacility) {
		this.isFacility = isFacility;
	}
	public String getShowCorrection() {
		return showCorrection;
	}
	public void setShowCorrection(String showCorrection) {
		this.showCorrection = showCorrection;
	}
	public String getCheck_box() {
		return check_box;
	}
	public void setCheck_box(String check_box) {
		this.check_box = check_box;
	}
	public Collection getSummHelpers() {
		return summHelpers;
	}
	public void setSummHelpers(Collection summHelpers) {
		this.summHelpers = summHelpers;
	}
	public Collection getFacCommentHelpers() {
		return facCommentHelpers;
	}
	public void setFacCommentHelpers(Collection facCommentHelpers) {
		this.facCommentHelpers = facCommentHelpers;
	}
	public String[] getDataAreaTypes() {
		return dataAreaTypes;
	}
	public void setDataAreaTypes(String[] dt) {
		this.dataAreaTypes = dt;
	}
	public String getCWNSNbr() {
		return CWNSNbr;
	}
	public void setCWNSNbr(String CWNSNbr) {
		this.CWNSNbr = CWNSNbr;
	}
	public String getSummaryAct() {
		return summaryAct;
	}
	public void setSummaryAct(String summaryAct) {
		this.summaryAct = summaryAct;
	}
	public String getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	public String getShowCostCurve() {
		return showCostCurve;
	}
	public void setShowCostCurve(String showCostCurve) {
		this.showCostCurve = showCostCurve;
	}
	public String getCostCurve() {
		return costCurve;
	}
	public void setCostCurve(String costCurve) {
		this.costCurve = costCurve;
	}
	public String getShowChanged() {
		return showChanged;
	}
	public void setShowChanged(String showChanged) {
		this.showChanged = showChanged;
	}
	public String getLastUpdatedUserName() {
		return lastUpdatedUserName;
	}
	public void setLastUpdatedUserName(String lastUpdatedUserName) {
		this.lastUpdatedUserName = lastUpdatedUserName;
	}
	public String getLastUpdateTS() {
		return lastUpdateTS;
	}
	public void setlastUpdateTS(String lastUpdateTS) {
		this.lastUpdateTS = lastUpdateTS;
	}
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
	public String getLastReviewedTS() {
		return lastReviewedTS;
	}
	public void setLastReviewedTS(String lastReviewedTS) {
		this.lastReviewedTS = lastReviewedTS;
	}

}
