/*
* @(#)SearchDAO.java  Mar 13, 2005
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

import gov.epa.owm.mtb.cwns.common.search.SearchConditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;


/**
 * This SearchDAO interface declares the Generic methods for a Search
 *
 * @author Raj Lingam
 */

public interface SearchDAO extends DAO {
    
	/**
	 * Searches for an object based on class and search conditions  
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @return  an object based on search
	 */
	public Object getSearchObject(Class implClass, SearchConditions searchConditions);
	
	/**
	 * Searches for an object based on class and search conditions  
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @param aliasCriteria  alias
	 * @return  an object based on search
	 */
	public Object getSearchObject(Class implClass, SearchConditions searchConditions, Collection aliasCriteria);	
	
	/**
	 * Searches for a list of objects based on class and search conditions
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @return  list of objects based on search
	 */
	public List getSearchList(Class implClass, SearchConditions searchConditions);
	
	/**
	 * Searches for a list of objects based on class and search conditions and sorts the results 
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @return  list of objects based on search
	 */
    public List getSearchList(Class implClass, SearchConditions searchConditions, Collection sortCriteria);
	
	
	/**
	 * Searches for a list of objects based on class and search conditions and sorts the results. The results are restricted to max result size. 
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param startIndex start index of the result set
	 * @param maxResults  number of results from the start index
	 * @return list of objects based on search 
	 */
    public List getSearchList(Class implClass, SearchConditions searchConditions, Collection sortCriteria, int startIndex, int maxResults);

	/**
	 * Searches for a list of objects based on class and search conditions and sorts the results. The results are restricted to max result size. 
	 * @param implClass    search object class
	 * @param searchConditions  search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param aliasCriteria a collection of alias criteria
	 * @param startIndex start index of the result set
	 * @param maxResults  number of results from the start index
	 * @return list of objects based on search
	 */
	public List getSearchList(Class implClass, SearchConditions searchConditions, Collection sortCriteria, Collection aliasCriteria, int startIndex, int maxResults);
	
	/**
	 * searches for specified columns based on search conditions and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @return an object array of specified columns
	 */
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions);
	
	/**
	 * searches based on search conditions and sorts and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @param sortCriteria sort criteria
	 * @return an object array of specified columns
	 */
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions, Collection sortCriteria);
	
	/**
	 * 
	 * searches based on search conditions and sorts and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param aliasCriteria a collection of alias criteria 
	 * @return an object array of specified columns
	 */
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions, Collection sortCriteria, Collection aliasCriteria) ;
		
	
	/**
	 * searches based on search conditions and sorts and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param aliasCriteria a collection of alias criteria 
  	 * @param startIndex start index of the result set
	 * @param maxResults  number of results from the start index
	 * @return an object array of specified columns
	 */
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions, Collection sortCriteria, Collection aliasCriteria, int startIndex, int maxResults) ;
	
	/**
	 * searches based on search conditions and sorts and returns an object array of specified columns 
	 * @param implClass search object class
	 * @param columns a collection of columns
	 * @param searchConditions search conditions
	 * @param sortCriteria a collection of sort criteria
	 * @param aliasCriteria a collection of alias criteria 
  	 * @param startIndex start index of the result set
	 * @param maxResults  number of results from the start index
	 * @param isDistinct  whether the result set should be distinct; default false
	 * @return an object array of specified columns
	 */	
	public List getSearchList(Class implClass, Collection columns, SearchConditions searchConditions, Collection sortCriteria, Collection aliasCriteria, int startIndex, int maxResults, boolean isDistinct);
	
	/**
	 * gets a count of search results based on the searc condition 
	 * @param implClass search object class
	 * @param searchConditions search conditions
	 * @return a count of the results
	 */
	public int getCount(Class implClass, SearchConditions searchConditions);

	public Collection getNamedQueryList(String queryName, Collection queryParameters);

	public void deleteAll(Collection objects);
	
	public void flushAndClearCache();
	
	/**
	 * Used to flush and clear the Hibernate Session
	 * @return
	 */
	public Session getDAOSession();

}
