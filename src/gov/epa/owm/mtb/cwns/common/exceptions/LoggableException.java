package gov.epa.owm.mtb.cwns.common.exceptions;

/*
 * @(#)<filename> <date>
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

public interface LoggableException {

    void setLogged();

    boolean isLogged();
}
