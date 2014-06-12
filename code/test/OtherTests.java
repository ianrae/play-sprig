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

	public static class Address
	{
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public int getRegion() {
			return region;
		}
		public void setRegion(int region) {
			this.region = region;
		}
		private String street;
		private int region;
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
	public void testUser() throws Exception 
	{
		Class clazz = User.class;
		String s = clazz.getSimpleName();
		log(s);
		
		String path = this.getTestFile(""); 
		Sprig.setDir(path);

		int n = Sprig.load(User.class, Address.class);
		assertEquals(4, n);
		
		List<Object> L = Sprig.getLoadedObjects(User.class);
		assertEquals(2, L.size());
		User u = (User)L.get(0);
		assertEquals("bob", u.firstName);
		assertEquals("Smith", u.lastName);
		u = (User)L.get(1);
		assertEquals("sue", u.firstName);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testAddress() throws Exception 
	{
		String path = this.getTestFile(""); 
		Sprig.setDir(path);

		int n = Sprig.load(Address.class);
		assertEquals(2, n);
		
		List<Object> L = Sprig.getLoadedObjects(Address.class);
		assertEquals(2, L.size());
		Address u = (Address)L.get(0);
		assertEquals("Main", u.street);
		assertEquals(42, u.region);
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
