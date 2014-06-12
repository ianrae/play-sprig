package org.mef.sprig;

import java.util.Map;

import org.springframework.beans.BeanWrapperImpl;

@SuppressWarnings("rawtypes")
public class DefaultSprigLoader implements SprigLoader
{
	private Class clazz;
	
	public DefaultSprigLoader(Class clazz)
	{
		this.clazz = clazz;
		
	}
	@Override
	public void parse(Object obj, Map<String,Object> map) 
	{
		log("A");
		BeanWrapperImpl w = new BeanWrapperImpl(obj);
		
		for(String propName : map.keySet())
		{
			Object val = map.get(propName);
			w.setPropertyValue(propName, val);
		}
	}

	@Override
	public void save(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolve(Object sourceObj, String fieldName, Object targetObj) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Class getClassBeingLoaded() 
	{
		return clazz;
	}

	//--helper--
	private void log(String s)
	{
		System.out.println(s);
	}
}
