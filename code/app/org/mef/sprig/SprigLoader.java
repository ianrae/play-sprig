package org.mef.sprig;

import java.util.Map;

//http://stackoverflow.com/questions/356583/can-i-reflectively-instantiate-a-generic-type-in-java

public interface SprigLoader //<T> 
{
//	void parse(T obj, Map<String,Object> map);
//	void save(T obj);
	void parse(Object obj, Map<String,Object> map);
	void save(Object obj);
	void resolve(Object sourceObj, String fieldName, Object targetObj);
	Class getClassBeingLoaded();
}


