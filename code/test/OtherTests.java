import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.mef.sprig.Sprig;


public class OtherTests extends BaseTest
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
		private Address addr;
		public Address getAddr() {
			return addr;
		}
		public void setAddr(Address addr) {
			this.addr = addr;
		}
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
		assertNotNull(u.addr);
		assertEquals("King", u.addr.getStreet());

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

	@Test
	public void testT()
	{
		Method getNameMethod;
		User user = new User();
		Address addr = new Address();
		addr.setStreet("abc");

		try
		{
			getNameMethod = user.getClass().getMethod("setAddr", Address.class);
		}
		catch (SecurityException exception1)
		{
			getNameMethod = null;
		}
		catch (NoSuchMethodException exception1)
		{
			getNameMethod = null;
		}

		if (getNameMethod != null)
		{
			try
			{
				System.out.println(getNameMethod.invoke(user, new Object[]{addr}));
			}
			catch (IllegalArgumentException exception)
			{
				// TODO Implement this catch block.
			}
			catch (IllegalAccessException exception)
			{
				// TODO Implement this catch block.
			}
			catch (java.lang.reflect.InvocationTargetException exception)
			{
				// TODO Implement this catch block.
			}
		}
		else
		{
			System.out.print("Unexpected class: ");
			System.out.println(user.getClass());
		}		

		assertEquals("abc", user.addr.getStreet());
	}

}
