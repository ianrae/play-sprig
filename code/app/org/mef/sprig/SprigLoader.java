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
	 * @param obj model obj
	 * @param map values from json
	 */
	void parse(T obj, Map<String,Object> map);
	
	/**
	 * Persist obj to the database (or whatever storage is being used).
	 * @param obj model obj
	 */
	void save(T obj);
	
	/**
	 * Set sourceObj.fieldName to targetObj.  This method is used when
	 * an object's JSON uses  sprig_record(SomeClass,2) to represent
	 * a reference to the SomeClass object with matching "sprig_id".
	 * 
	 * @param sourceObj source object
	 * @param fieldName field name
	 * @param targetObj target object
	 */
	void resolve(T sourceObj, String fieldName, Object targetObj);
	
	/**
	 * The class of model class T.
	 * @return class being loaded
	 */
	Class<T> getClassBeingLoaded();
	
	/**
	 * Convenience method. It is the last method called on the loader.
	 * Use it for any cleanup code you need.
	 */
	void close();
}


