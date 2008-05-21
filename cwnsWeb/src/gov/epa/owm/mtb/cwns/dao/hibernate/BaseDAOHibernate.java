package gov.epa.owm.mtb.cwns.dao.hibernate;
/*
* @(#)BaseDAOHibernate.java  Mar 13, 2005
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

import gov.epa.owm.mtb.cwns.dao.DAO;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * This class implementes BaseDAO interface for Hibernate.
 *
 * @author Vidya Ramakrishnan
 * @version $Revision: 1.1 $
 */
public abstract class BaseDAOHibernate extends HibernateDaoSupport implements DAO {

    /**
     * Log for the current class.
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * Generic method to save an object - handles both update and insert.
     * @param o the object to save
     */
    public void saveObject(Object o) {
        getHibernateTemplate().saveOrUpdate(o);
    }

    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     * @param clazz model class to lookup
     * @param id the identifier (primary key) of the class
     * @return a populated object
     */
    public Object getObject(Class clazz, Number id) {
        Object o = getHibernateTemplate().load(clazz, id);
        return o;
    }

    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     * @param clazz the type of objects (a.k.a. while table) to get data from
     * @return List of populated objects
     */
    public List getObjects(Class clazz) {
        return getHibernateTemplate().loadAll(clazz);
    }

    /**
     * Generic method to delete an object based on class and id
     * @param clazz model class to lookup
     * @param id the identifier (primary key) of the class
     */
    public void removeObject(Class clazz, Number id) {
        Object o =  getObject(clazz, id);
        if (null != o){
            getHibernateTemplate().delete(o);
        }
    }
    
 
    /**
     * Generic method to delete an object 
     * @param o the object to delete
     */
    public void removeObject(Object o) {
        if (null != o){
            getHibernateTemplate().delete(o);
        }
    }
    
    /**
     * Generic method to delete a collection of objects 
     * @param Os the collection of objects to delete
     */
    public void removeObjects(Collection Os) {
        if (null != Os){
            getHibernateTemplate().deleteAll(Os);
        }
    }    
    
    /**
     * Gets DAO Session
     * @return Hibernate Session
     */
    public Session getDAOSession(){
        return SessionFactoryUtils.getSession(getSessionFactory(), false);
    }
}
