package org.mef.sprig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mef.sprig.json.Reader;
import org.mef.sprig.util.TSortNode;
import org.mef.sprig.util.TopologicalSort;

import play.Logger;

/** 
 * Main API class for Sprig.
 * Loads JSON into model objects and saves them to the database.
 *
 */
public class Sprig 
{
	protected static String seedDir = "conf/sprig";
	protected static Sprig theInstance;
	protected static Reader reader;
	
	public static String version()
	{
		return "v0.1.2";
	}
	
	/**
	 * Sets the directory to be used, when you want JSON files in some other directory
	 * than 'conf/sprig'.
	 * 
	 * Used internally for unit testing. 
	 * @param dir folder with sprig json files
	 */
	public static void setDir(String dir)
	{
		seedDir = dir;
	}
	
	public static void setReader(Reader r)
	{
		reader = r;
	}
	
	public static void enableDebugLogging(boolean b)
	{
		SprigLogger.debugLoggingEnabled = b;
	}
	
	/**
	 * Load the seed data for the given modes.  
	 * @param objs Each object is either a Class of a model to be loaded, or a loader that implements SprigLoader
	 * @return number objects
	 * @throws Exception something bad happened
	 */
	public static int load(Object... objs) throws Exception
	{
		return load(null, objs);
	}
	
	/**
	 * Load the seed data for the given modes.  
	 * @param subDir A sub-directory (under 'conf/sprig') in which to load JSON files.
	 * @param objs Each object is either a Class of a model to be loaded, or a loader that implements SprigLoader
	 * @return number of json files loaded
	 * @throws Exception something bad happened
	 */
	@SuppressWarnings("rawtypes")
	public static int load(String subDir, Object... objs) throws Exception
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
			
			Wrapper wrapper = new Wrapper(tmp, reader);
			L.add(wrapper);
		}
		
		int n = self.doLoad(subDir, L);
		
		//and close
		for(Wrapper wrapper : L)
		{
			wrapper.getLoader().close();
		}
		return n;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Object> getLoadedObjects(Class clazz) 
	{
		List<Object> L = theInstance.resultMap.get(clazz);
		return L;
	}
	
	//===============================
	protected int failCount;
	@SuppressWarnings("rawtypes")
	protected Map<Class, List<Object>> resultMap = new HashMap<Class, List<Object>>();
	protected Map<String, Wrapper> loaderMap = new HashMap<String, Wrapper>();
	protected List<ViaRef> viaL = new ArrayList<ViaRef>();

	protected Sprig()
	{
	}
	
	protected void log(String s)
	{
		SprigLogger.log(s);
	}
	
	@SuppressWarnings("rawtypes")
	protected int doLoad(String subDir, List<Wrapper> wrapperL) throws Exception
	{
		int numObjLoaded = 0;
		
		List<Object> loadedObjL = null;
		String dir = getDir(subDir);
		log("dir: " + dir);
		for(Wrapper wrapper : wrapperL)
		{
			loadedObjL = wrapper.load(dir, viaL);  //read the JSON
			
			addToResultMap(wrapper.getLoader().getClassBeingLoaded(), loadedObjL);
			numObjLoaded += loadedObjL.size();
			
			this.loaderMap.put(wrapper.getNameOfClassBeingLoaded(), wrapper);
		}
		
		//sort
		List<Wrapper> sortedL = tsort(wrapperL);

		log("and save..");
		List<Wrapper> soFarL = new ArrayList<Wrapper>();
		failCount = 0;
		for(Wrapper wrapper : sortedL)
		{
			SprigLoader loader = wrapper.getLoader();
			log(String.format("SEED saving %s..", wrapper.getNameOfClassBeingLoaded()));
			
			List<Object> L = this.resultMap.get(loader.getClassBeingLoaded());
			wrapper.save(L);

			soFarL.add(wrapper);
			doResolve(soFarL);
			if (failCount > 0)
			{
				throw new IllegalStateException("SEED resolve failed");
			}
		}
		return numObjLoaded;
	}
	
	private String getDir(String subDir) 
	{
		if (subDir == null)
		{
			return seedDir;
		}
		else
		{
			return seedDir + "/" + subDir;
		}
	}

	@SuppressWarnings("rawtypes")
	protected void addToResultMap(Class classBeingLoaded, List<Object> L) 
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

	protected List<Wrapper> tsort(List<Wrapper> wrapperL)
	{
		List<TSortNode> L = new ArrayList<TSortNode>();
		for(Wrapper wrapper : wrapperL)
		{
			TSortNode node = new TSortNode(wrapper);
			L.add(node);
		}
		
		for(ViaRef via : viaL)
		{
			Wrapper srcWrapper = findByClass(wrapperL, via.sourceClazz);
			Wrapper targetWrapper = findByClassName(wrapperL, via.targetClassName);
			
			TSortNode node1 = null;
			TSortNode node2 = null;
			for(TSortNode tmp : L)
			{
				if (tmp.obj == srcWrapper)
				{
					node1 = tmp;
				}
				else if (tmp.obj == targetWrapper)
				{
					node2 = tmp;
				}
			}

			node1.addDep(node2);
		}
		
		List<TSortNode> sortL = TopologicalSort.sort(L);
		
		List<Wrapper> sortedL = new ArrayList<Wrapper>();
		for(TSortNode node : sortL)
		{
			sortedL.add((Wrapper) node.obj);
		}
		return sortedL;
	}

	@SuppressWarnings("rawtypes")
	protected Wrapper findByClass(List<Wrapper> wrapperL, Class clazz) 
	{
		for(Wrapper wrapper : wrapperL) 
		{
			SprigLoader loader = wrapper.getLoader();
			if (loader.getClassBeingLoaded() == clazz)
			{
				return wrapper;
			}
		}
		return null;
	}
	protected Wrapper findByClassName(List<Wrapper> wrapperL, String className) 
	{
		for(Wrapper wrapper : wrapperL) 
		{
			if (wrapper.getNameOfClassBeingLoaded().equals(className))
			{
				return wrapper;
			}
		}
		return null;
	}

	protected boolean doResolve(List<Wrapper> soFarL)
	{
		while(doOneRound(soFarL))
		{
		}
		return (this.viaL.size() == 0);
	}

	protected boolean doOneRound(List<Wrapper> soFarL)
	{
		for(ViaRef vid : viaL)
		{
			for(Wrapper wrapper : soFarL)
			{
				//once a loaders objects have been saved to the db, we can resolve references to them
				String className = wrapper.getNameOfClassBeingLoaded();
				if (className.equals(vid.targetClassName))
				{
					//log("a " + className);
					if (resolveAsDeferredId(vid))
					{
						viaL.remove(vid);
						return true;
					}
				}
			}
		}

		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean resolveAsDeferredId(ViaRef ref)
	{
		if (ref.targetField.equals("sprig_id"))
		{
			Wrapper w = this.loaderMap.get(ref.targetClassName);
			Integer sprigId = Integer.parseInt(ref.targetVal);
			Object obj = w.sprigIdMap.get(sprigId);

			Wrapper sourceW = this.loaderMap.get(ref.sourceClazz.getSimpleName());
			SprigLoader sourceLoader = sourceW.getLoader();
			String fieldName = ref.sourceField; 
			
			//resolve
			try 
			{
				sourceLoader.resolve(ref.sourceObj, fieldName, obj);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				failCount++;
				log(String.format("SEED failed to resolve %s.%s to %s.%s ", ref.sourceClazz.getSimpleName(), 
						fieldName, ref.targetClassName, ref.targetField));
			}
			return true;
		}
		return false;
	}
}