import java.io.File;


public class BaseTest {

	//--helpers--
	protected void log(String s) 
	{
		System.out.println(s);

	}

	protected String getTestFile(String filename)
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
