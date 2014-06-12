import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.mef.sprig.Sprig;


public class OtherTests 
{
	public static class User
	{
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		private String firstName;
		private String lastName;
	}

	@Test
	public void testFile()
	{
		String path = getTestFile("User.json");
		log(path);
		File f = new File(path);
		assertTrue(f.exists());
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws Exception 
	{
		Class clazz = User.class;
		String s = clazz.getSimpleName();
		log(s);
		
		String path = this.getTestFile(""); 
		Sprig.setDir(path);

		int n = Sprig.load(User.class);
		assertEquals(2, n);
		
		List<Object> L = Sprig.getLoadedObjects();
		assertEquals(2, L.size());
		User u = (User)L.get(0);
		assertEquals("bob", u.firstName);
		assertEquals("Smith", u.lastName);
		u = (User)L.get(1);
		assertEquals("sue", u.firstName);
	}

	//--helpers--
	private void log(String s) 
	{
		System.out.println(s);

	}

	private String getTestFile(String filename)
	{
		String path = new File(".").getAbsolutePath();
		if (path.endsWith("."))
		{
			path = path.substring(0, path.length() - 1);
		}
		path += "test\\testfiles\\" + filename;
		return path;
	}

}
