import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.mef.sprig.Sprig;



public class DirTests extends BaseTest
{

	@Test
	public void test() throws Exception 
	{
		String path = this.getTestFile(""); 
		Sprig.setDir(path);

		String dir = "prod";
		int n = Sprig.load(dir, OtherTests.Address.class);
		assertEquals(1, n);

		List<Object> L = Sprig.getLoadedObjects(OtherTests.Address.class);
		assertEquals(1, L.size());
		OtherTests.Address addr = (OtherTests.Address) L.get(0);
		assertEquals("Queen", addr.getStreet());
	}

}
