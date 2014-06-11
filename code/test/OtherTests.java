import static org.junit.Assert.*;

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
	public void test() throws Exception 
	{
		int n = Sprig.load(User.class);
		assertEquals(0, n);
	}

}
