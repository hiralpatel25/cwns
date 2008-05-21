package gov.epa.owm.mtb.cwns.common.search;

/**
* This class holds a search condition
*
* @author Raj Lingam
* 
*/
public class SearchCondition {
		
	public static final String OPERATOR_EQ = "EQ";
	public static final String OPERATOR_NOT_EQ = "NOT_EQ";
	public static final String OPERATOR_LT = "LT";
	public static final String OPERATOR_GT = "GT";
	public static final String OPERATOR_IN = "IN";
	public static final String OPERATOR_BETWEEN = "BETWEEN";
	public static final String OPERATOR_LIKE = "LIKE";
	public static final String OPERATOR_IS_NULL = "IS_NULL";
	public static final String OPERATOR_IS_NOT_NULL = "IS_NOT_NULL";	
	// Logical Operators
	public static final String OPERATOR_AND = "AND";
	public static final String OPERATOR_OR = "OR";
	public static final String OPERATOR_NOT = "NOT";
	public static final String OPERATOR_NOT_IN = "NOT_IN";	
	
	/**
	 * Default Constructor
	 *
	 */
	public SearchCondition() {
		super();
	}
	
	/**
	 * Constructor  based on name operator and value 
	 * @param name      column name
	 * @param operator  Operator gt,lt,=, etc 
	 * @param value     value Object 
	 */
	public SearchCondition(String name, String operator, Object value) {
		super();
		this.name = name;
		this.operator = operator;
		this.value = value;
	}
	
	/**
	 * Constructor based on name and value and the operator 
	 * @param name
	 * @param value
	 */
	public SearchCondition(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
		this.operator= OPERATOR_EQ;
	}
	
	
	/** 
	 * name of the column
	 */ 	
	private String name;
	
	/** 
	 * Operator gt, lt, eq, etc 
	 */
	private String operator;
	
	/** 
	 * value  
	 */	
	private Object value;	

    /**
     * gets the column name
     * @return column name
     */
	public String getName() {
		return name;
	}
	/**
	 * sets the column name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * gets the operator
	 * @return operator
	 */
	public String getOperator() {
		return operator;
	}
	
	/**
	 * sets the Operator
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	/**
	 * Value gatter
	 * @return values
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Value Setter
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}		
}
