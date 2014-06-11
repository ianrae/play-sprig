package org.mef.sprig;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class DefaultSprigLoader implements SprigLoader
{
	private Class clazz;
	
	public DefaultSprigLoader(Class clazz)
	{
		this.clazz = clazz;
		
	}
	@Override
	public void parse(Object obj, Map map) {
		// TODO Auto-generated method stub
		
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
	public String getNameOfClassBeingLoaded() 
	{
		return clazz.getSimpleName();
	}

}
