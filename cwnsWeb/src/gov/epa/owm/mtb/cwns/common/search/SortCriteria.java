package gov.epa.owm.mtb.cwns.common.search;

/*
 * @ Author Raj Lingam
 * 
 */

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


public class SortCriteria implements Serializable{
    private static final long serialVersionUID = 3257004358615839545L;
    public static final String ID = "sortDTO";
    public static final int ORDER_ASCENDING=0;
    public static final int ORDER_DECENDING=1;
    
    public SortCriteria() {
        // Default Constructor
    }
    
    public SortCriteria(String column, int order) {
        this.m_Column = column;
        this.m_Order = order;
    }
    
    /**
     * Retrieves the column which should be used when sorting.  The exact
     * meaning of this will be determined by the implementing service
     *
     * @return  The sort column
     */
    public String getColumn() {
        return m_Column;
    }
    
    /**
     * Retrieves the order (ascending, descending) to be used when sorting
     *
     * @return  The sort order
     */
    public int getOrder() {
        return m_Order;
    }
    
    /**
     * Sets the sort column to the given value
     *
     * @param column    The new sort column
     */
    public void setColumn(String column) {
        m_Column = column;
    }
    
    /**
     * Sets the sort order to the given value
     *
     * @param order     The new sort order
     */
    public void setOrder(int order) {
        m_Order = order;
    }
    
    /**
     * Change the order of the sort. If ascending change to decending,
     * if decending change to ascending.
     *
     */
    public void changeOrder() {
    	if (m_Order == ORDER_ASCENDING) {
    		m_Order = ORDER_DECENDING;
    	} else {
    		m_Order = ORDER_ASCENDING;
    	}
    }
    
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }
    
    // Member attributes
    private String m_Column = "id";
    private int m_Order = ORDER_ASCENDING;
}

