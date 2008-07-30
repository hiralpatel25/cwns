package gov.epa.owm.mtb.cwns.summary;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.service.SummaryService;

public class SaveAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		SummaryForm summaryForm = (SummaryForm) form;
		
		//	 debug statements --
		if (summaryForm.getDataAreaTypes()!=null) {
			String[] dataareatypes = summaryForm.getDataAreaTypes();
			log.debug("Length-----"+ dataareatypes.length);
			for (int i=0;i<dataareatypes.length;i++) {
				log.debug("type-----"+ dataareatypes[i]);
			}	
		}
		log.debug("CWNSNumber-----"+ summaryForm.getCWNSNbr());
				
		summaryService.save(summaryForm.getFacilityId(),summaryForm.getDataAreaTypes());
		
		return mapping.findForward("success");
	}
	
	private SummaryService summaryService;

	public void setSummaryService(SummaryService ss) {
		summaryService = ss;
	}

}
