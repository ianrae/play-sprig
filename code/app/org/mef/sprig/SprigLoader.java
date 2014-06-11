package org.mef.sprig;

import java.util.Map;


public interface SprigLoader<T> 
{
	void parse(T obj, Map<String,Object> map);
	void save(T obj);
	void resolve(Object sourceObj, String fieldName, Object targetObj);
	String getNameOfClassBeingLoaded();
}


