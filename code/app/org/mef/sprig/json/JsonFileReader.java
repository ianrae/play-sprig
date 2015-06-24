package org.mef.sprig.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.sprig.SprigLogger;
import org.mef.sprig.util.ResourceReader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFileReader implements Reader
{
	private String dir;
	private String className;

	public JsonFileReader(String dir, String className)
	{
		this.dir = dir;
		this.className = className;
	}
	
	public List<Map<String,Object>> load() throws Exception
	{
		String path = className + ".json";
		String json = ResourceReader.readSeedFile(path, this.dir);
		if (json == null || json.isEmpty()) //fix later!!
		{
			log(String.format("SEED LOAD failed: %s", path));
			return null;
		}

		log(String.format("SEED %s loading..", path));
		return parseType(json);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,Object>> parseType(String inputJson) throws Exception
	{
		Map<String,Object> myMap = new HashMap<String, Object>();

		ObjectMapper objectMapper = new ObjectMapper();
		String mapData = inputJson;
		myMap = objectMapper.readValue(mapData, new TypeReference<HashMap<String,Object>>() {});
//		System.out.println("Map using TypeReference: "+myMap);

		List<Map<String,Object>> myList = (List<Map<String, Object>>) myMap.get("records");


		return myList;
	}
	
	private void log(String s)
	{
		SprigLogger.log(s);
	}
}
