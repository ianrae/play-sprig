package org.mef.sprig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Sprig //implements LoaderObserver
{
	private static String seedDir = "conf/sprig";

	public static void setDir(String dir)
	{
		seedDir = dir;
	}
	@SuppressWarnings("rawtypes")
	public static int load(Object... objs) throws Exception
	{
		Sprig self = new Sprig();
		theInstance = self;
		List<Wrapper> L = new ArrayList<Wrapper>();
		
		for(Object obj : objs)
		{
			SprigLoader tmp;
			
			if (obj instanceof SprigLoader)
			{
				tmp = (SprigLoader)obj;
			}
			else if (obj instanceof Class)
			{
				tmp = new DefaultSprigLoader((Class)obj);
			}
			else
			{
				throw new IllegalArgumentException("parameter must be Class or a SprigLoader");
			}
			
			Wrapper wrapper = new Wrapper(tmp);
			L.add(wrapper);
		}
		
		int n = self.doLoad(L);
		return n;
	}
	
//	private static List<Object> mostRecentLoadedObjectsL;
	private int failCount;
	private static Sprig theInstance;
	
	private int doLoad(List<Wrapper> wrapperL) throws Exception
	{
		int numObjLoaded = 0;
		
		List<Object> loadedObjL = null;
		for(Wrapper wrapper : wrapperL)
		{
			loadedObjL = wrapper.load(seedDir);
			addToResultMap(wrapper.getLoader().getClassBeingLoaded(), loadedObjL);
			numObjLoaded += loadedObjL.size();
		}
		
//		return (loadedObjL == null) ? 0 : loadedObjL.size();
		log("and save..");
		
		List<SprigLoader> soFarL = new ArrayList<SprigLoader>();
		failCount = 0;
		for(Wrapper wrapper : wrapperL) //sortedL
		{
			SprigLoader loader = wrapper.getLoader();
			log(String.format("SEED saving %s..", wrapper.getNameOfClassBeingLoaded()));
			
			List<Object> L = this.resultMap.get(loader.getClassBeingLoaded());
			wrapper.saveOrUpdate(L);

			soFarL.add(loader);
			//doResolve(soFarL);
			if (failCount > 0)
			{
				throw new IllegalStateException("SEED resolve failed");
			}
		}
		return numObjLoaded;
	}
	
	private void addToResultMap(Class classBeingLoaded, List<Object> L) 
	{
		List<Object> storedL = resultMap.get(classBeingLoaded);
		if (storedL != null)
		{
			storedL.addAll(L);
		}
		else
		{
			resultMap.put(classBeingLoaded, L);
		}
	}
	private void log(String s)
	{
		System.out.println(s);
	}
	//===============================
//	@SuppressWarnings("rawtypes")
	private Map<Class, List<Object>> resultMap = new HashMap<Class, List<Object>>();
//	@SuppressWarnings("rawtypes")
//	private Map<String, SprigLoader> loaderMap = new HashMap<String, SprigLoader>();
//	private List<ViaRef> viaL = new ArrayList<ViaRef>();
//	private int failCount; //# errors

	public Sprig()
	{
	}
	public static List<Object> getLoadedObjects(Class clazz) 
	{
		List<Object> L = theInstance.resultMap.get(clazz);
		return L;
	}

//	public Map<Class, List<Object>> getResultMap()
//	{
//		return this.resultMap;
//	}
//	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private int doLoad(SprigLoader...loaders) throws Exception
//	{
//		int numObj = 0;
//		for(SprigLoader loader : loaders)
//		{
//			String className = loader.getNameOfClassBeingLoaded();
//			String path = className + ".json";
//			String json = ResourceReader.readSeedFile(path, seedDir);
//			if (json == null || json.isEmpty()) //fix later!!
//			{
//				log(String.format("SEED LOAD failed: %s", path));
//				return 0;
//			}
//
//			log(String.format("SEED %s loading..", path));
//			numObj += parseType(loader, json);
//			
//			this.loaderMap.put(className, loader);
//		}
//
//		List<SprigLoader> sortedL = tsort(loaders);
//
////		logDebug("and save..");
//		List<SprigLoader> soFarL = new ArrayList<SprigLoader>();
//		failCount = 0;
//		for(SprigLoader loader : sortedL)
//		{
//			log(String.format("SEED saving %s..", loader.getNameOfClassBeingLoaded()));
//			List<Object> L = this.resultMap.get(loader.getClassBeingLoaded());
//			loader.saveOrUpdate(L);
//
//			soFarL.add(loader);
//			doResolve(soFarL);
//			if (failCount > 0)
//			{
//				throw new IllegalStateException("SEED resolve failed");
//			}
//		}
//		
//		if (viaL.size() > 0)
//		{
//			log(String.format("SEED FAILED with %d unresolved sprig_id references", viaL.size()));
//		}
//
//		return numObj;
//	}
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public int parseType(SprigLoader loader, String inputJson) throws Exception
//	{
//		Map<String,Object> myMap = new HashMap<String, Object>();
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		String mapData = inputJson;
//		myMap = objectMapper.readValue(mapData, new TypeReference<HashMap<String,Object>>() {});
////		System.out.println("Map using TypeReference: "+myMap);
//
//		List<Map<String,Object>> myList = (List<Map<String, Object>>) myMap.get("records");
//
//		//                List<Object> L = (List<Flour>)(List<?>) loader.parseItems(inner, this);
//		List<Object> L = loader.doparseItems(this, myList);
//
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
//		return L.size();
//	}
//	
//	@SuppressWarnings("rawtypes")
//	private List<SprigLoader> tsort(SprigLoader[] loaders)
//	{
//		List<TSortNode> L = new ArrayList<TSortNode>();
//		for(SprigLoader loader : loaders)
//		{
//			TSortNode node = new TSortNode(loader);
//			L.add(node);
//		}
//		
//		
//		for(ViaRef via : viaL)
//		{
//			SprigLoader srcLoader = findByClass(loaders, via.sourceClazz);
//			SprigLoader targetLoader = findByClassName(loaders, via.targetClassName);
//			
//			TSortNode node1 = null;
//			TSortNode node2 = null;
//			for(TSortNode tmp : L)
//			{
//				if (tmp.obj == srcLoader)
//				{
//					node1 = tmp;
//				}
//				else if (tmp.obj == targetLoader)
//				{
//					node2 = tmp;
//				}
//			}
//
//			node1.addDep(node2);
//		}
//		
//		List<TSortNode> sortL = TopologicalSort.sort(L);
//		
//		List<SprigLoader> sortedL = new ArrayList<SprigLoader>();
//		for(TSortNode node : sortL)
//		{
//			sortedL.add((SprigLoader) node.obj);
//		}
//		return sortedL;
//	}
//
//	@SuppressWarnings("rawtypes")
//	private SprigLoader findByClass(SprigLoader[] loaders, Class clazz) 
//	{
//		for(SprigLoader loader : loaders)
//		{
//			if (loader.getClassBeingLoaded() == clazz)
//			{
//				return loader;
//			}
//		}
//		return null;
//	}
//	@SuppressWarnings("rawtypes")
//	private SprigLoader findByClassName(SprigLoader[] loaders, String className) 
//	{
//		for(SprigLoader loader : loaders)
//		{
//			if (loader.getNameOfClassBeingLoaded().equals(className))
//			{
//				return loader;
//			}
//		}
//		return null;
//	}
//	private void log(String s)
//	{
//		System.out.println(s);
//	}
//	@Override
//	public void addViaRef(ViaRef ref) 
//	{
//		viaL.add(ref);
//	}
//
//
//	@SuppressWarnings("rawtypes")
//	private boolean doResolve(List<SprigLoader> soFarL)
//	{
//		while(doOneRound(soFarL))
//		{
//		}
//		return (this.viaL.size() == 0);
//	}
//
//	@SuppressWarnings("rawtypes")
//	private boolean doOneRound(List<SprigLoader> soFarL)
//	{
//		for(ViaRef vid : viaL)
//		{
//			for(SprigLoader loader : soFarL)
//			{
//				//once a loaders objects have been saved to the db, we can resolve references to them
//				String className = loader.getNameOfClassBeingLoaded();
//				if (className.equals(vid.targetClassName))
//				{
//					log("a " + className);
//					if (resolveAsDeferredId(vid))
//					{
//						viaL.remove(vid);
//						return true;
//					}
//				}
//			}
//		}
//
//		return false;
//	}
//
//	@SuppressWarnings("rawtypes")
//	private boolean resolveAsDeferredId(ViaRef ref)
//	{
//		if (ref.targetField.equals("sprig_id"))
//		{
//			SprigLoader loader = this.loaderMap.get(ref.targetClassName);
//			Integer sprigId = Integer.parseInt(ref.targetVal);
//			Object obj = loader.getSprigIdMap().objMap.get(sprigId);
//
//			SprigLoader sourceLoader = this.loaderMap.get(ref.sourceClazz.getSimpleName());
//			String fieldName = ref.sourceField; 
//			if (! sourceLoader.resolve(ref.sourceObj, fieldName, obj))
//			{
//				failCount++;
//				log(String.format("SEED failed to resolve %s.%s to %s.%s ", ref.sourceClazz.getSimpleName(), 
//						fieldName, ref.targetClassName, ref.targetField));
//			}
//			return true;
//		}
//		return false;
//	}
}