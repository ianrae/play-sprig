package org.mef.sprig;

import java.util.Map;

import org.springframework.beans.BeanWrapperImpl;

import play.Logger;
import play.db.ebean.Model;

/**
 * Loads Play models or other Java beans. Uses Spring's BeanWrapperImpl
 * to set property values in the model object.
 *
 * Sprig uses this loader if a custom loader is not provided.
 */
@SuppressWarnings("rawtypes")
public class DefaultSprigLoader implements SprigLoader<Object>
{
	private Class clazz;
	
	public DefaultSprigLoader(Class clazz)
	{
		this.clazz = clazz;
		
	}
	@Override
	public void parse(Object obj, Map<String,Object> map) 
	{
		//log("A");
		BeanWrapperImpl w = new BeanWrapperImpl(obj);
		
		for(String propName : map.keySet())
		{
			Object val = map.get(propName);
			w.setPropertyValue(propName, val);
		}
	}
	

	@Override
	public void save(Object obj) 
	{
		if (obj instanceof Model)
		{
			Model m = (Model)obj;
			m.save();
		}
		else
		{
			log("not an play.Model object. you must use a custom loader.");
		}
	}

	@Override
	public void resolve(Object obj, String fieldName, Object targetObj) 
	{
		log(fieldName);
		BeanWrapperImpl w = new BeanWrapperImpl(obj);
		w.setPropertyValue(fieldName, targetObj);
	}
	
	@Override
	public Class getClassBeingLoaded() 
	{
		return clazz;
	}

	@Override
	public void close() 
	{
	}
	
	//--helper--
	protected void log(String s)
	{
		Logger.info("[Sprig] " + s);
	}
	
}
