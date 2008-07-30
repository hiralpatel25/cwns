/**
 * 
 */
package gov.epa.owm.mtb.cwns.discharge;

import gov.epa.owm.mtb.cwns.model.Facility;
import gov.epa.owm.mtb.cwns.model.FacilityDischarge;
import gov.epa.owm.mtb.cwns.model.FacilityType;
import gov.epa.owm.mtb.cwns.service.DischargeService;
import gov.epa.owm.mtb.cwns.service.FacilityTypeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts.action.ActionForm;

/**
 * @author Matt Connors
 *
 */
public class DischargeForm extends ActionForm {
	
	public static final String PRESENT_AND_PROJECTED = "presentAndProjected";
	public static final String PRESENT               = "present";
	public static final String PROJECTED			 = "projected";
	
	
	private String action ="";

	private long   facilityId;
	private long   dischargeId;
	
	private String methodId="xx";
	private String methodName="";

	private String displayDetails="";
	private String status="";
	
	private String cwnsNumber="";
	private String dischargeLocationId="";
	private String facilityDischargeDescription="";
	
	private String name="";
	private String description="";
	private String facilityLocationId="";

	private String presentFlow;
	private String projectedFlow;

	private boolean updateable;
	
	private String isLocalUser = "";
	
    private boolean isPresentFlowUpdatable;
    private boolean isProjectedFlowUpdatable;
	
	/**  
	 * Clear the object of data.
	 *
	 */
	public void clear() {
		
		facilityId = -1;
		dischargeId = -1;
		displayDetails="";
		status="";
		
		methodId="xx";
		methodName="";
		cwnsNumber="";
		dischargeLocationId="";
		
		name="";
		description="";
		facilityLocationId="";
		presentFlow="";
		projectedFlow="";
		updateable = true;
	}
	
	/**
	 * Initialize the form bean for editing using the Facility DDischarge object.
	 * @param fd
	 */
	public void loadForEdit(FacilityDischarge fd) {

		clear();
		setDisplayDetails("Y");
		
		setFacilityId(fd.getFacilityByFacilityId().getFacilityId());
		setDischargeId(fd.getFacilityDischargeId());
		String id = new Long(fd.getDischargeMethodRef().getDischargeMethodId()).toString();
		setMethodId(id);
		setMethodName(fd.getDischargeMethodRef().getName());
		
		if (fd.getPresentFlowPortionPersent() != null) {
			setPresentFlow(fd.getPresentFlowPortionPersent().toString());
		} else {
			setPresentFlow("");
		}
		
		if (fd.getProjectedFlowPortionPersent() != null) {
			setProjectedFlow(fd.getProjectedFlowPortionPersent().toString());
		} else {
			setProjectedFlow("");		}

		// Discharge location info
		if (fd.getFacilityByFacilityIdDischargeTo() != null) {
			setDischargeLocationId(fd.getFacilityByFacilityIdDischargeTo().getLocationId());
			setCwnsNumber(fd.getFacilityByFacilityIdDischargeTo().getCwnsNbr());
			setName(fd.getFacilityByFacilityIdDischargeTo().getName());
			setDescription(fd.getFacilityByFacilityId().getDescription());
		}
		
		setFacilityLocationId(fd.getFacilityByFacilityId().getLocationId());
		
		// Status
		if (fd.getPresentFlag() == 'Y' &&
		    fd.getProjectedFlag() == 'Y') {
			setStatus(PRESENT_AND_PROJECTED);
		} else if (fd.getPresentFlag() == 'Y') {
			setStatus(PRESENT);
		} else if (fd.getProjectedFlag() == 'Y') {
			setStatus(PROJECTED);
		} else {
			setStatus("");
		}

	}
	
	public void loadForAdd(Collection facilityTypes,
						   Collection facilityDischarges) {

		clear();
		setDisplayDetails("Y");
		
		boolean          present   		  	= false;
		boolean          projected         	= false;
		FacilityType     facilityType 		= null;
		FacilityDischarge facilityDischarge 	= null;
		
		// Default Discharge Method
		setMethodId(DischargeService.SELECT_DISCHARGE_METHOD);
		
		// Has Discharge To Another Facility already been assigned?
		boolean disToAnotherFacFound = false;
		Iterator fdIter = facilityDischarges.iterator();
		while (fdIter.hasNext()) {
			facilityDischarge = (FacilityDischarge)fdIter.next();
			if (facilityDischarge.getDischargeMethodRef().getDischargeMethodId() ==
				DischargeService.DISCHARGE_TO_ANOTHER_FAC) {
				disToAnotherFacFound = true;
			}
		}
		
		Iterator iter = facilityTypes.iterator();
		while (iter.hasNext()) {
			facilityType = (FacilityType)iter.next();
			
			// If Discharge To Another Facility has not been assigned  and the 
			// facility has a facility type of Seperate Sewers the default discharge 
			// method should be Discharge To Another Facility.
			if (!disToAnotherFacFound &&
				facilityType.getFacilityTypeRef().getFacilityTypeId() == 
				FacilityTypeService.FACILITY_TYPE_SEPERATE_SEWER.longValue()) {

				setMethodId(DischargeService.DISCHARGE_TO_ANOTHER_FACILITY);
			}
			
			// Identify if Facilty Type includes present and/or projected

			if (facilityType.getPresentFlag() == 'Y') {
				present = true;
			}
			
			if (facilityType.getProjectedFlag() == 'Y') {
				projected = true;
			}
		}
		
		setFacilityLocationId(facilityType.getFacility().getLocationId());
		setFacilityId(facilityType.getFacility().getFacilityId());
		
		if (present && projected) {
			setStatus(PRESENT_AND_PROJECTED);
		} else if(present) {
			setStatus(PRESENT);
		} else if (projected) {
			setStatus(PROJECTED);
		}
	}
	
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getCwnsNumber() {
		return cwnsNumber;
	}

	public void setCwnsNumber(String cwnsNumber) {
		this.cwnsNumber = cwnsNumber;
	}

	public String getDisplayDetails() {
		return displayDetails;
	}

	public void setDisplayDetails(String displayDetails) {
		this.displayDetails = displayDetails;
	}


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isUpdateable() {
		return updateable;
	}

	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}

	public long getDischargeId() {
		return dischargeId;
	}

	public void setDischargeId(long dischargeId) {
		this.dischargeId = dischargeId;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getPresentFlow() {
		return presentFlow;
	}

	public void setPresentFlow(String presentFlow) {
		this.presentFlow = presentFlow;
	}

	public String getProjectedFlow() {
		return projectedFlow;
	}

	public void setProjectedFlow(String projectedFlow) {
		this.projectedFlow = projectedFlow;
	}

	public String getMethodId() {
		return methodId;
	}

	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}

	public String getFacilityLocationId() {
		return facilityLocationId;
	}

	public void setFacilityLocationId(String facilityLocationId) {
		this.facilityLocationId = facilityLocationId;
	}

	public long getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(long facilityId) {
		this.facilityId = facilityId;
	}

	public String getDischargeLocationId() {
		return dischargeLocationId;
	}

	public void setDischargeLocationId(String dischargeLocationId) {
		this.dischargeLocationId = dischargeLocationId;
	}

	public String getFacilityDischargeDescription() {
		return facilityDischargeDescription;
	}

	public void setFacilityDischargeDescription(String facilityDischargeDescription) {
		this.facilityDischargeDescription = facilityDischargeDescription;
	}

	public String getIsLocalUser() {
		return isLocalUser;
	}

	public void setIsLocalUser(String isLocalUser) {
		this.isLocalUser = isLocalUser;
	}

	public boolean isPresentFlowUpdatable() {
		return isPresentFlowUpdatable;
	}

	public void setPresentFlowUpdatable(boolean isPresentFlowUpdatable) {
		this.isPresentFlowUpdatable = isPresentFlowUpdatable;
	}

	public boolean isProjectedFlowUpdatable() {
		return isProjectedFlowUpdatable;
	}

	public void setProjectedFlowUpdatable(boolean isProjectedFlowUpdatable) {
		this.isProjectedFlowUpdatable = isProjectedFlowUpdatable;
	}


}
