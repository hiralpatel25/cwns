package gov.epa.owm.mtb.cwns.permits;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.epa.owm.mtb.cwns.common.CWNSAction;
import gov.epa.owm.mtb.cwns.county.FacilityCountyForm;
import gov.epa.owm.mtb.cwns.service.FacilityPermitService;

public class FacilityPermitAddAction extends CWNSAction {

	protected ActionForward cwnsExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		FacilityPermitForm facilityPermitForm = (FacilityPermitForm)form;
		facilityPermitForm.setPermitNumber("");
		facilityPermitForm.setPermitTypeId(0);
		facilityPermitForm.setUseData('N');
		facilityPermitForm.setType_NPDES('N');
		Collection permitTypes = facilityPermitService.getPermitTypes(true);
		
		facilityPermitForm.setPermitTypes(permitTypes);
		facilityPermitForm.setMode("add");
        // save token
		saveToken(request);     
		return mapping.findForward("success");
	}
//  set the facility address service
    private FacilityPermitService facilityPermitService;
            
    public void setFacilityPermitService(FacilityPermitService facilityPermitService) {
		this.facilityPermitService = facilityPermitService;
	}

}
