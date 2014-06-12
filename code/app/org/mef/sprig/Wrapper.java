package org.mef.sprig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.sprig.json.JsonFileReader;

public class Wrapper 
{
	@SuppressWarnings("rawtypes")
	private SprigLoader loader;
	private JsonFileReader fileLoader;
	public Map<Integer,Object> sprigIdMap = new HashMap<Integer, Object>();

	public static final String SPRIG_ID_NAME = "sprig_id";

	
	public Wrapper(SprigLoader loader)
	{
		this.loader = loader;
	}
	
	@SuppressWarnings("rawtypes")
	public String getNameOfClassBeingLoaded()
	{
		Class clazz = loader.getClassBeingLoaded();
		return clazz.getSimpleName();
	}

	public List<Object> load(String dir, List<ViaRef> viaL) throws Exception
	{
		String className = getNameOfClassBeingLoaded();
		this.fileLoader = new JsonFileReader(dir, className);
		
		List<Map<String,Object>> list = fileLoader.load();
		List<Object> L = parseObjects(list, viaL);
		return L;
	}
	
	
	@SuppressWarnings("rawtypes")
	public List<Object> parseObjects(List<Map<String,Object>> inputList, List<ViaRef> viaL)
	{
		List<Object> resultL = new ArrayList<Object>();

		Map<String,Object> tmp = new HashMap<String,Object>();
		for(Map<String,Object> params : inputList)
		{
			Object obj = null;
			try {
				Class clazz = loader.getClassBeingLoaded();
				obj = clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			resultL.add(obj);
			parseSprigId(params, obj);

			for(String key : params.keySet())
			{
				String val = (String) params.get(key).toString();
				if (containsVia(val))
				{
//					String data2 = "{'type':'Shirt', 'items':[{'id':1,'color':'<% sprig_record(Color,2)%>'}]}";
					System.out.println(key);
					val = val.replace("<%", "");
					val = val.replace("%>", "");
//					String target = "sprig_record(";
					int pos = val.indexOf('(');
					int pos2 = val.indexOf(',', pos);
					int pos3 = val.indexOf(')', pos);
//					String op = val.substring(0, pos).trim();
					String namex = val.substring(pos + 1, pos2).trim();
					String valx = val.substring(pos2 + 1, pos3).trim();
					
					//sourceclass,field,obj,target class,field,val,obj
					ViaRef ref = new ViaRef();
					ref.sourceClazz = loader.getClassBeingLoaded();
					ref.sourceField = key;
					ref.sourceObj = obj;
					ref.targetClassName = namex;
					ref.targetField = "sprig_id";
					ref.targetVal = valx;
					viaL.add(ref);
				}
				else if (! key.equals(SPRIG_ID_NAME))
				{
					tmp.put(key, params.get(key));
				}
			}

			//parse all the non-sprig-record ones
			loader.parse(obj, tmp);				
		}
		
		return resultL;
	}

	private boolean containsVia(String key) 
	{
		return (key.startsWith("<%") && key.endsWith("%>"));
	}
	
	private void parseSprigId(Map<String, Object> map, Object obj) 
	{
		String idName = SPRIG_ID_NAME;
		if (map.containsKey(idName))
		{
			Integer id = (Integer)map.get(idName); //support String later!!!!!!!!1111
			this.sprigIdMap.put(id, obj);
		}
	}

	public SprigLoader getLoader() 
	{
		return this.loader;
	}

	public void save(List<Object> L) 
	{
		for(Object obj : L)
		{
			loader.save(obj);
		}
	}
}
