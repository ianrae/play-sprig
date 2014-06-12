package org.mef.sprig;

import java.util.Map;

/**
 * Loader for objects of model class T. 
 * T is typically a play.model class but can be any
 * class (if you define a custom loader).
 * @param <T>
 */
public interface SprigLoader<T> 
{
	/**
	 * Extract property values from map and store in model object.
	 * @param obj
	 * @param map
	 */
	void parse(T obj, Map<String,Object> map);
	
	/**
	 * Persist obj to the database (or whatever storage is being used).
	 * @param obj
	 */
	void save(T obj);
	
	/**
	 * Set sourceObj.fieldName to targetObj.  This method is used when
	 * an object's JSON uses <% sprig_record(SomeClass,2)%> to represent
	 * a reference to the SomeClass object with matching "sprig_id".
	 * 
	 * @param sourceObj
	 * @param fieldName
	 * @param targetObj
	 */
	void resolve(T sourceObj, String fieldName, Object targetObj);
	
	/**
	 * The class of model class T.
	 * @return
	 */
	Class<T> getClassBeingLoaded();
}


