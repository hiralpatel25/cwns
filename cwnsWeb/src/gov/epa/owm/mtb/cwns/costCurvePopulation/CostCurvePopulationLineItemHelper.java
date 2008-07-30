package gov.epa.owm.mtb.cwns.costCurvePopulation;

public class CostCurvePopulationLineItemHelper {
	
	private String displayOnly = "N";

	private long value = 0;
	
	private long validationValue = -1;
	
	private String operator = "le"; // <=
	
	private String projectedGreaterThanPresent = "Y";
	
	private String isWarning = "N";
	
	private String toValidate = "Y";

	public String getToValidate() {
		return toValidate;
	}

	public void setToValidate(String toValidate) {
		this.toValidate = toValidate;
	}

	public String getProjectedGreaterThanPresent() {
		return projectedGreaterThanPresent;
	}

	public void setProjectedGreaterThanPresent(String projectedGreaterThanPresent) {
		this.projectedGreaterThanPresent = projectedGreaterThanPresent;
	}

	public String getDisplayOnly() {
		return displayOnly;
	}

	public void setDisplayOnly(String displayOnly) {
		this.displayOnly = displayOnly;
	}

	public String getIsWarning() {
		return isWarning;
	}

	public void setIsWarning(String isWarning) {
		this.isWarning = isWarning;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public long getValidationValue() {
		return validationValue;
	}

	public void setValidationValue(long validationValue) {
		this.validationValue = validationValue;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
	
}
