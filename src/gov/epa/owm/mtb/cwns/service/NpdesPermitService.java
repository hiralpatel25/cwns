package gov.epa.owm.mtb.cwns.service;

import java.util.Collection;
import java.util.Date;

public interface NpdesPermitService {

	public Collection getEfPipeScheds(Long facilityId);
	
	public Collection displayNpdesFlowData(String efNpdesPermitNumber, String efDischargeNum, String efReportDesig,
            Date startDate, Date endDate);

	public String getEfNpdesPermitNbr(Long facilityId);
	
	public float getPermitDesignFlowRate(String efNpdesPermitNbr);

	public boolean IsUnitOfMeasureSame(Collection npdesFlowData);
}
