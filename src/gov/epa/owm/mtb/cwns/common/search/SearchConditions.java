package gov.epa.owm.mtb.cwns.common.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class holds the search conditons and logical operators
 * @author Raj Lingam
 *
 */
public class SearchConditions implements Serializable{
	
	private List conditions;
	private Map logicalOperatorsMap;
	private Logger log=Logger.getLogger(getClass());

	/**
	 * Default Constructor
	 */
	public SearchConditions() {
		conditions = new ArrayList();	
		logicalOperatorsMap = new HashMap();
	}
	
	/**
	 * Construct that sets the initial search condition
	 * @param sc
	 */
	public SearchConditions(SearchCondition sc) {
		conditions = new ArrayList();
		logicalOperatorsMap = new HashMap();
		conditions.add(sc);
	}
	
	/**
	 * gets search conditions
	 * get the search condtions 
	 * @return a collection search condition(s) as objects
	 */
	public List getConditions() {
		return conditions;
	}
	
    /**
     * Sets the search condtion and logical operator
     * @param logicalOperator
     * @param searchCondition
     */	
	public void setCondition(String logicalOperator, Object searchCondition){
		if (isLogicalOperator(logicalOperator)) {			
			if (isSearchObject(searchCondition)) {
			    if (conditions.size() > 0) {
			    	logicalOperatorsMap.put(""+conditions.size(), logicalOperator);
			    }
				//set the condition
				conditions.add(searchCondition);
			} else {
				log.error("Error invalid searchCondition: " + searchCondition.getClass());
			}
		}else{
			log.error("Error invalid logical operator: " + logicalOperator);
		}	
	}
	
	public String getLogicalOperator(int index){		
		return (String)logicalOperatorsMap.get(""+index);
	}
	
    /**
     * sets the search conditions using AND as default logical operator
     * Sets the search condtion 
     * @param searchCondition
     */
	
	public void setCondition(Object searchCondition){
		setCondition(SearchCondition.OPERATOR_AND, searchCondition);
	}	
	
	/**
	 * Checks if an operator is a Logical Operator
	 * @param operator
	 * @return  true if the operator is logical else false
	 */
	private boolean isLogicalOperator(String operator) {
		if (operator.equals(SearchCondition.OPERATOR_AND)
				|| operator.equals(SearchCondition.OPERATOR_OR)
				|| operator.equals(SearchCondition.OPERATOR_NOT))
			return true;
		return false;
	}
	
	/**
	 * Checks if the specified object is an instance of search condition
	 * @param o
	 * @return if searchCondition returns true else false
	 */
	private boolean isSearchObject(Object o){
		if(o instanceof SearchCondition){
			return true;
		}
		
		if(o instanceof SearchConditions){
			return true;
		}
		return false;
	}

}
