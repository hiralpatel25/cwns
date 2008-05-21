package gov.epa.owm.mtb.cwns.facilityType;

import gov.epa.owm.mtb.cwns.common.Entity;

import java.util.ArrayList;
import java.util.Iterator;

public class DeleteDataAreaHelper {
	
	private ArrayList dataAreas;
	private Long facilityType;

	public DeleteDataAreaHelper() {
		super();
	}
	
	public DeleteDataAreaHelper(ArrayList messages, Long facilityType) {
		super();
		dataAreas = new ArrayList();
		if(messages!=null){
			int i=0;
			for (Iterator iter = messages.iterator(); iter.hasNext();) {
				String da = (String) iter.next();
				dataAreas.add(new Entity(i+"",da));
				i++;
			} 			
		}
		this.facilityType = facilityType;
	}

	public ArrayList getDataAreas() {
		return dataAreas;
	}
	
	public void setDataAreas(ArrayList da) {
		this.dataAreas = da;
	}
	
	public Long getFacilityType() {	
		return facilityType;
	}
	public void setFacilityType(Long facilityType) {
		this.facilityType = facilityType;
	}

}
