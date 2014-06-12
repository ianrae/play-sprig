package org.mef.sprig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.sprig.json.JsonFileLoader;

public class Wrapper 
{
	@SuppressWarnings("rawtypes")
	private SprigLoader loader;
	private JsonFileLoader fileLoader;

	public Wrapper(SprigLoader loader)
	{
		this.loader = loader;
	}
	
	public String getNameOfClassBeingLoaded()
	{
		Class clazz = loader.getClassBeingLoaded();
		return clazz.getSimpleName();
	}

	public List<Object> load(String dir) throws Exception
	{
		String className = getNameOfClassBeingLoaded();
		this.fileLoader = new JsonFileLoader(dir, className);
		
		List<Map<String,Object>> list = fileLoader.load();
		
		List<Object> L = doparseItems(list);

//		List<Object> storedL = resultMap.get(loader.getClassBeingLoaded());
//		if (storedL != null)
//		{
//			storedL.addAll(L);
//		}
//		else
//		{
//			List<Object> objL = (List<Object>)(List<?>)L;
//			resultMap.put(loader.getClassBeingLoaded(), objL);
//		}
		return L;
	}
	
	
	public List<Object> doparseItems(List<Map<String,Object>> inputList)
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
//			parseSprigId(params, obj);

			for(String key : params.keySet())
			{
				String val = (String) params.get(key).toString();
				if (false) //containsVia(val))
				{
////					String data2 = "{'type':'Shirt', 'items':[{'id':1,'color':'<% sprig_record(Color,2)%>'}]}";
//					
//					System.out.println(key);
//					val = val.replace("<%", "");
//					val = val.replace("%>", "");
////					String target = "sprig_record(";
//					int pos = val.indexOf('(');
//					int pos2 = val.indexOf(',', pos);
//					int pos3 = val.indexOf(')', pos);
//					String op = val.substring(0, pos).trim();
//					String namex = val.substring(pos + 1, pos2).trim();
//					String valx = val.substring(pos2 + 1, pos3).trim();
//					
//					//sourceclass,field,obj,target class,field,val,obj
//					ViaRef ref = new ViaRef();
//					ref.sourceClazz = this.classBeingLoaded;
//					ref.sourceField = key;
//					ref.sourceObj = obj;
//					ref.targetClassName = namex;
//					ref.targetField = "sprig_id";
//					ref.targetVal = valx;
//					observer.addViaRef(ref);
				}
				else
				{
					tmp.put(key, params.get(key));
				}
			}

			//parse all the non-sprig-record ones
			loader.parse(obj, tmp);				
		}
		
		return resultL;
	}

	public SprigLoader getLoader() 
	{
		return this.loader;
	}

	public void saveOrUpdate(List<Object> L) 
	{
		for(Object obj : L)
		{
			loader.save(obj);
		}
	}
}
