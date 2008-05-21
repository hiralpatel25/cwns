package gov.epa.owm.mtb.cwns.facilityInformation;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorActionForm;

public class FacilityPORForm extends ValidatorActionForm {
	
	/**
	 * 
	 */
	public static final String NORTH = "N";
	public static final String SOUTH = "S";
	public static final String EAST = "E";
	public static final String WEST = "W";
	
	private static final long serialVersionUID = 1L;
	private Long facilityId;
	private String locationDescId="";
	private String locationDesc="";
	private long methodId;
	private String methodDesc="";
	private long datumId;
	private String datumDesc="";
	private float latitude;
	private float longitude;
	private String source;
	private String sourceDesc;
	private String measureDate;
	private String scale;
	private char latitudeDirec='N';
	private char longitudeDirec='W';
	private char isInTribalTerritory='N';
	private char isUpdatable = 'N';
	private String showWarningMessage = "N";
	private Collection locationDescs;
	private Collection methods;
	private Collection datums;
	private char confirm='N';
	private char confirmCheckbox;
	private char isTriTerritoryUpdatable='N';
	private char isMeasureDateUpdatable='N';
	private String sessionId="";
	private String  coordinateTypeCode="";
	
	public char getIsTriTerritoryUpdatable() {
		return isTriTerritoryUpdatable;
	}
	public void setIsTriTerritoryUpdatable(char isTriTerritoryUpdatable) {
		this.isTriTerritoryUpdatable = isTriTerritoryUpdatable;
	}
	public char getConfirmCheckbox() {
		return confirmCheckbox;
	}
	public void setConfirmCheckbox(char confirmCheckbox) {
		this.confirmCheckbox = confirmCheckbox;
	}
	public char getConfirm() {
		return confirm;
	}
	public void setConfirm(char confirm) {
		this.confirm = confirm;
	}
	public char getIsUpdatable() {
		return isUpdatable;
	}
	public void setIsUpdatable(char isUpdatable) {
		this.isUpdatable = isUpdatable;
	}
	public String getShowWarningMessage() {
		return showWarningMessage;
	}
	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}
	public char getIsInTribalTerritory() {
		return isInTribalTerritory;
	}
	public void setIsInTribalTerritory(char isInTribalTerritory) {
		this.isInTribalTerritory = isInTribalTerritory;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public char getLatitudeDirec() {
		return latitudeDirec;
	}
	public void setLatitudeDirec(char latitudeDirec) {
		this.latitudeDirec = latitudeDirec;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public char getLongitudeDirec() {
		return longitudeDirec;
	}
	public void setLongitudeDirec(char longitudeDirec) {
		this.longitudeDirec = longitudeDirec;
	}
	public String getMeasureDate() {
		return measureDate;
	}
	public void setMeasureDate(String measureDate) {
		this.measureDate = measureDate;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Long getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}
	public Collection getDatums() {
		return datums;
	}
	public void setDatums(Collection datums) {
		this.datums = datums;
	}
	public Collection getLocationDescs() {
		return locationDescs;
	}
	public void setLocationDescs(Collection locationDescs) {
		this.locationDescs = locationDescs;
	}
	public Collection getMethods() {
		return methods;
	}
	public void setMethods(Collection methods) {
		this.methods = methods;
	}
	public long getDatumId() {
		return datumId;
	}
	public void setDatumId(long datumId) {
		this.datumId = datumId;
	}
	public String getLocationDescId() {
		return locationDescId;
	}
	public void setLocationDescId(String locationDescId) {
		this.locationDescId = locationDescId;
	}
	public long getMethodId() {
		return methodId;
	}
	public void setMethodId(long methodId) {
		this.methodId = methodId;
	}
	/*
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		ActionErrors errors = new ActionErrors();
		if (methodId == 0 && (!"".equals(measureDate) || datumId > 0 || longitude > 0 || latitude >0)){
			errors.add("method", new ActionError("validation.error.method.miss"));
		}
		if (datumId == 0 && (!"".equals(measureDate) || methodId > 0 || longitude > 0 || latitude >0)){
			errors.add("datum", new ActionError("validation.error.datum.miss"));
		}
		if (longitude == 0 && (!"".equals(measureDate) || datumId > 0 || methodId > 0 || latitude >0)){
			errors.add("longitude", new ActionError("validation.error.longitude.miss"));
		}
		if (latitude == 0 && (!"".equals(measureDate) || datumId > 0 || longitude > 0 || methodId >0)){
			errors.add("latitude", new ActionError("validation.error.latitude.miss"));
		}
		if ("".equals(measureDate) && (methodId > 0 || datumId > 0 || longitude > 0 || latitude >0)){
			errors.add("measureDate", new ActionError("validation.error.measureDate.miss"));
		}
		if (longitude != 0 && (((new Float(longitude)).intValue()) < 0||(new Float(longitude)).intValue() > 180)){
			errors.add("longituderange", new ActionError("validation.error.longitude.range"));
		}
		if (latitude != 0 && (((new Float(latitude)).intValue()) < 0||(new Float(latitude)).intValue() > 90)){
			errors.add("latituderange", new ActionError("validation.error.latitude.range"));
		}
		return errors;
	}*/
	public String getDatumDesc() {
		return datumDesc;
	}
	public void setDatumDesc(String datumDesc) {
		this.datumDesc = datumDesc;
	}
	public String getLocationDesc() {
		return locationDesc;
	}
	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}
	public String getMethodDesc() {
		return methodDesc;
	}
	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}
	public String getSourceDesc() {
		return sourceDesc;
	}
	public char getIsMeasureDateUpdatable() {
		return isMeasureDateUpdatable;
	}
	public void setIsMeasureDateUpdatable(char isMeasureDateUpdatable) {
		this.isMeasureDateUpdatable = isMeasureDateUpdatable;
	}
	public String getCoordinateTypeCode() {
		return coordinateTypeCode;
	}
	public void setCoordinateTypeCode(String coordinateTypeCode) {
		this.coordinateTypeCode = coordinateTypeCode;
	}
    
}
