import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mef.sprig.Sprig;
import org.mef.sprig.SprigLoader;


public class CustomLoaderTests extends BaseTest
{
	public static class Airport
	{
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private String name;
		
	}
	
	public static class AirportLoader implements SprigLoader<Airport>
	{
		@Override
		public void parse(Airport obj, Map<String, Object> map) 
		{
			if (map.containsKey("name"))
			{
				String name = (String) map.get("name");
				obj.setName(name);
			}
		}

		@Override
		public void save(Airport obj) 
		{
			System.out.println("saving..");
		}

		@Override
		public void resolve(Airport sourceObj, String fieldName,
				Object targetObj) 
		{
		}

		@Override
		public Class getClassBeingLoaded() 
		{
			return Airport.class;
		}

		@Override
		public void close() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Test
	public void test() throws Exception 
	{
		String path = this.getTestFile(""); 
		Sprig.setDir(path);

		AirportLoader customLoader = new AirportLoader();
		int n = Sprig.load(customLoader);
		assertEquals(1, n);
		
		List<Object> L = Sprig.getLoadedObjects(Airport.class);
		assertEquals(1, L.size());
		Airport airport = (Airport) L.get(0);
		assertEquals("Schiphol", airport.getName());
	}

}
