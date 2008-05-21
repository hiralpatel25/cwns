package gov.epa.owm.mtb.cwns.boundary;

public class BoundaryListHelper {
	
	private long boundaryId;
	private String boundaryType;
	private String boundaryName;
	
	public long getBoundaryId() {
		return boundaryId;
	}
	public void setBoundaryId(long boundaryId) {
		this.boundaryId = boundaryId;
	}
	public String getBoundaryName() {
		return boundaryName;
	}
	public void setBoundaryName(String boundaryName) {
		this.boundaryName = boundaryName;
	}
	public String getBoundaryType() {
		return boundaryType;
	}
	public void setBoundaryType(String boundaryType) {
		this.boundaryType = boundaryType;
	}
	public BoundaryListHelper(long boundaryId, String boundaryType, String boundaryName) {
		super();
		this.boundaryId = boundaryId;
		this.boundaryType = boundaryType;
		this.boundaryName = boundaryName;
	}
	public BoundaryListHelper() {
		super();
	}

}
