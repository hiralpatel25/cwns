/**
 * @(#)ApplicationException.java  May 27, 2005
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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class describes a critical internal application failure.  Under
 * ordinary circumstances, it is not expected that this will be recoverable
 * (although callers are welcome to catch and process the error if need be.
 * <br><br>
 * Note that this while class supports wrapping exceptions as per the typical
 * JDK 1.4 paradigm, it also supports multiple messages in a single exception.
 * Thus, it is certainly possible (and encouraged) to wrap the triggering
 * exception itself, and just add additional messages as necessary as the
 * exception propogates up the call stack
 *
 * @author  Pramod Kudva
 * @version $Revision: 1.1 $
 */
public class ApplicationException extends CWNSRuntimeException {
    /**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 7016865507658461838L;
	private ArrayList messages = new ArrayList();

    /**
     * Constructs a new runtime exception with <code>null</code> as its detail
     * message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ApplicationException() {
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
    public ApplicationException(String message) {
        super();

        messages.add(message);
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
     */
    public ApplicationException(Throwable cause) {
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
    public ApplicationException(String message, Throwable cause) {
        super(cause);

        messages.add(message);
    }

    /**
     * Adds the given message to the collection of detail messages
     * associated with this exception
     *
     * @param message   The message to add
     */
    public void addMessage(String message) {
        messages.add(message);
    }

    /**
     * Returns the detail message string of this exception.  Note that it
     * will consist of each message added by addMessage(), in order
     *
     * @return  The detail message string of this instance
     */
    public String getMessage() {
        // TBD: Right now, this will return the messages in reverse calling
        //      order.  In other words, if A calls B which calls C, C throws
        //      an exception, and all three and a message and rethrow, we'll
        //      end up displaying in the order C, B, A.  Determine if this
        //      makes sense, or if we want to reverse the order first
        StringBuffer buf = new StringBuffer();
        Iterator iter = messages.iterator();
        while(iter.hasNext()) {
            String message = (String)iter.next();
            buf.append(message);
            buf.append("\n\t");
        }

        return buf.toString();
    }
}
