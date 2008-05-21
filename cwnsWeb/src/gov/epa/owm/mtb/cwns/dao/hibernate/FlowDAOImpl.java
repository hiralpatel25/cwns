package gov.epa.owm.mtb.cwns.dao.hibernate;

import gov.epa.owm.mtb.cwns.dao.FlowDAO;
import gov.epa.owm.mtb.cwns.model.FacilityFlow;

public class FlowDAOImpl extends BaseDAOHibernate implements FlowDAO {
	//FlowFacility_flow.present_flow_msr >= 10 where flow_id_fk = 5
	//boolean isPresentFlowGT10(){
		//get facility flow information
	//	FacilityFlow ff = new FacilityFlow();
	//	if(ff.getFlowRef().getFlowId()==5){
	//		if(ff.getPresentFlowMsr().intValue() >= 10){
	//			return true;
	//		}
	//	}
	//} 

}
