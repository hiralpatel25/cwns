/*
* @(#)DAO.java  Mar 13, 2005
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
package gov.epa.owm.mtb.cwns.dao;

import java.util.List;

/**
 * This DAO interface declares the Generic methods for a Model Object
 *
 * @author Vidya Ramakrishnan
 * @version $Revision: 1.1 $
 */

public interface DAO {
    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     * @param clazz the type of objects (a.k.a. while table) to get data from
     * @return List of populated objects
     */
    List getObjects(Class clazz);

    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param clazz model class to lookup
     * @param id the identifier (primary key) of the class
     * @return a populated object
     */
    Object getObject(Class clazz, Number id);

    /**
     * Generic method to save an object - handles both update and insert.
     * @param o the object to save
     */
    void saveObject(Object o);

    /**
     * Generic method to delete an object based on class and id
     * @param clazz model class to lookup
     * @param id the identifier (primary key) of the class
     */
    void removeObject(Class clazz, Number id);

    
    /**
     * Generic method to delete an object 
     * @param o the object to delete
     */
    void removeObject(Object o);

}
