package gov.epa.owm.mtb.cwns.discharge;

public class DischargeMethodHelper {
	
	String name="";
	String id="";
	String present="N";
	String projected="N";

	public DischargeMethodHelper(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPresent() {
		return present;
	}
	public void setPresent(String present) {
		this.present = present;
	}
	public String getProjected() {
		return projected;
	}
	public void setProjected(String projected) {
		this.projected = projected;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
