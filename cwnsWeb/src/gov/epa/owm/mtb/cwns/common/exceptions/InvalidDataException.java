/*
 * @(#)InvalidDataException.java  May 27, 2005
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
 * This class represents the error which occurs when persistent data is
 * invalidly set (or missing).  It should only be used to identify cases
 * where the persistent data itself is incorrect (such as when an expected
 * field in a database entry is missing)
 *
 * @author Pramod Kudva
 * @version $Revision: 1.1 $
 */
public class InvalidDataException extends CWNSRuntimeException {
    /**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -4557932409267989833L;

	/**
     * Constructs a new runtime exception with <code>null</code> as its detail
     * message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public InvalidDataException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     *
     * @since 1.4
     */
    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new runtime exception with the specified detail message
     * and cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in this
     * runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent
     *                or unknown.)
     *
     * @since 1.4
     */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
