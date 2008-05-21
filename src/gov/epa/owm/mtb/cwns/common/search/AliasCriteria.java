package gov.epa.owm.mtb.cwns.common.search;

public class AliasCriteria {

	private String childObjectName;
	private String alias;
	private String join;
	
	public static final String JOIN_INNER = "INNER";
	public static final String JOIN_FULL = "FULL";
	public static final String JOIN_LEFT = "LEFT";

	public AliasCriteria(String childObjectName, String alias) {
		super();
		this.childObjectName = childObjectName;
		this.alias = alias;
		this.join=JOIN_LEFT;
	}

	public AliasCriteria(String childObjectName, String alias, String join) {
		super();
		this.childObjectName = childObjectName;
		this.alias = alias;
		this.join = join;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public String getChildObjectName() {
		return childObjectName;
	}

	public void setChildObjectName(String name) {
		this.childObjectName = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String value) {
		this.alias = value;
	}	
}
