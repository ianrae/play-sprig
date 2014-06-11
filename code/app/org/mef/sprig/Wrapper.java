package org.mef.sprig;

import java.util.List;
import java.util.Map;

import org.mef.sprig.json.JsonFileLoader;

public class Wrapper 
{
	private SprigLoader loader;
	private JsonFileLoader fileLoader;

	public Wrapper(SprigLoader loader)
	{
		this.loader = loader;
	}

	public void load(String dir) throws Exception
	{
		String className = loader.getNameOfClassBeingLoaded();
		this.fileLoader = new JsonFileLoader(dir, className);
		
		List<Map<String,Object>> list = fileLoader.load();
		
	}
}
