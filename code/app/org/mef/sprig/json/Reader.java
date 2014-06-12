package org.mef.sprig.json;

import java.util.List;
import java.util.Map;


public interface Reader 
{
	List<Map<String,Object>> load() throws Exception;
	List<Map<String,Object>> parseType(String inputJson) throws Exception;
}
