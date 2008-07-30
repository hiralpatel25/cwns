/*
 * @(#)ObjectNotFoundException.java  May 27, 2005
 *
 * Copyright 2005 Lockheed Martin
 * 1010 North Glebe Road, Arlington, Virginia, 22201, USA
 * All Rights Reserved
 *
 * This software is the confidential and propietary information of Lockheed
 * Martin ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with Lockheed Martin.
 */
package gov.epa.owm.mtb.cwns.common.exceptions;

/**
 * This class represents the condition where a selected object cannot be
 * found.  Note that it should only be used in cases where the object was
 * presumed to exist.
 * <br><br>
 * This class differs from the Spring
 * <code>ObjectRetrievalFailureException</code> in two fundamental ways:
 * <ul>
 * <li>
 *   The Spring exception only supports object failure when retrieval is done
 *   by id.  This class supports multiple data query methods
 * </li>
 * <li>
 *   The Spring exception implicitly assumes retrieval is done through a
 *   persistence layer.  This class can be used to indicate any sort of
 *   lookup failure.
 * </li>
 * </ul>
 *
 * @author Pramod Kudva
 * @version $Revision: 1.1 $
 */
public class ObjectNotFoundException extends CWNSRuntimeException {
    /**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -8809069351744108557L;
	private Class desiredClass;
    private Object queryField;
    private String queryFieldName;

    /**
     * Creates a new instance of this exception
     *
     * @param desiredClass  Class of the instance which we were attempting to
     *                      retrieve
     * @param id            The unique identifier representing the ID of the
     *                      requested object
     */
    public ObjectNotFoundException(Class desiredClass, Long id) {
        this(desiredClass, "ID", id);
    }

    /**
     * Creates a new instance of this exception
     *
     * @param desiredClass      Class of the instance which we were
     *                          attempting to retrieve
     * @param queryFieldName    The name of the field for which we were
     *                          querying
     * @param queryField        The value of the field for which we were
     *                          querying
     */
    public ObjectNotFoundException(Class desiredClass,
                                   String queryFieldName,
                                   Object queryField) {
        super();
        
        this.desiredClass = desiredClass;
        this.queryField = queryField;
        this.queryFieldName = queryFieldName;
    }

    /**
     * Returns the detail message string of this exception.
     *
     * @return  The detail message string of this instance
     */
    public String getMessage() {
        StringBuffer buf = new StringBuffer();
        buf.append("Could not retrieve ");
        buf.append(desiredClass.getName());
        buf.append("instance (");
        buf.append(queryFieldName);
        buf.append(": ");
        buf.append(queryField);

        return buf.toString();
    }
}
