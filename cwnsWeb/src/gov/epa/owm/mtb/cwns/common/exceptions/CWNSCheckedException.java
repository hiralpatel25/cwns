/*
 * @(#)CWNSCheckedException.java  May 27, 2005
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
 * This class represents the abstract base of the GMS checked exception
 * heirarchy.  All exceptions which can be expected to be handled by callers
 * should subclass this class (or its subclasses)
 *
 * @author Pramod Kudva
 * @version $Revision: 1.1 $
 */
public class CWNSCheckedException extends Exception implements LoggableException {

    /**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -7901105832583334831L;
	private boolean logged = false;

    /**
     * Constructs a new exception with <code>null</code> as its detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public CWNSCheckedException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by a call
     * to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CWNSCheckedException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     *
     * @since 1.4
     */
    public CWNSCheckedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *  <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
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
    public CWNSCheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged() {
        logged = true;
    }

}
