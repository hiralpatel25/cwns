package gov.epa.owm.mtb.cwns.common.exceptions;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 *
 * Copyright (c)2003 Lockheed Martin
 * 1010 North Glebe Road, Arlington, Virginia, 22201, U.S.A.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Lockheed
 * Martin ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Lockheed Martin.
 */

public class ExceptionHandler implements MethodInterceptor {

    Log log = LogFactory.getLog(ExceptionHandler.class);

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (Throwable throwable) {
            log.info("Test: Caught exception");
            logException(throwable, methodInvocation);
            if ((throwable instanceof RuntimeException) && !(throwable instanceof CWNSRuntimeException)) {
                throwable = new CWNSRuntimeException(throwable);

            } else if ((throwable instanceof Exception) && !(throwable instanceof CWNSCheckedException)) {
                throwable = new CWNSCheckedException(throwable);
            }
            throw throwable;
        }
    }

    private void logException(Throwable t, MethodInvocation method) {
        Log log = LogFactory.getLog(method.getClass());
        String methodDetails = getMethodInformation(method);
        if(t instanceof LoggableException) {
            LoggableException loggableException = (LoggableException) t;
            if (!loggableException.isLogged()) {
                loggableException.setLogged();
                log.error(methodDetails, t);
            } else {
                log.error(methodDetails);
            }
        } else {
            log.error(methodDetails, t);
        }
    }

    String getMethodInformation(MethodInvocation method) {
        StringBuffer buf = new StringBuffer("Method: ");
        buf.append(method.getMethod().getName());
        Object[] params = method.getArguments();
        if (params != null && params.length > 0) {
            buf.append("\nParameters passed in:\n");
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                buf.append(getObjectDetails(param));
                buf.append("\n");
            }
        }
        return buf.toString();
    }

    String getObjectDetails(Object obj) {
        String details;
        if (obj instanceof HttpServletRequest) {
            details = "Request parameters:\n";
            details += getMapDetails(((HttpServletRequest) obj).getParameterMap());
        } else {
            details = obj.toString();
        }
        return details;
    }

    String getMapDetails(Map map) {
        StringBuffer buf = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
            Entry entry = (Entry)iterator.next();
        	String key = (String) entry.getKey();
        	Object value = entry.getValue();
            buf.append(key);
            buf.append(": ");
            buf.append(getObjectDetails(value));
        }
        return buf.toString();
    }
}
