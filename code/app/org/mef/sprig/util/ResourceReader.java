package org.mef.sprig.util;

import org.apache.commons.io.FilenameUtils;

public class ResourceReader 
{
	public static String readSeedFile(String filename, String dir)
	{
		MyResourceReader r = new MyResourceReader();
		String relPath = FilenameUtils.concat(dir, filename);
		String results = r.read(relPath);
		return results;
	}
	
//	public static String readFile(String path) 
//	{
//		StringBuilder sb = new StringBuilder();
//		BufferedReader br = null;
//		boolean succeeded = false;
//		
//	    try {
//	    	br = new BufferedReader(new FileReader(path));
//	        String line = br.readLine();
//
//	        while (line != null) 
//	        {
//	            sb.append(line);
//	            sb.append('\n');
//	            line = br.readLine();
//	        }
//	        succeeded = true;	        
//	    } 
//	    catch (Exception e) 
//	    {
//			e.printStackTrace();
//		}
//	    finally 
//	    {
//	    	if (br != null)
//	    	{
//	    		try 
//	    		{
//					br.close();
//				} 
//	    		catch (IOException e) 
//	    		{
////					e.printStackTrace();
//				}
//	    	}
//	    }	
//	    
//	    if (! succeeded)
//	    {
//	    	return null;
//	    }
//	    
//	    String everything = sb.toString();
//	    return everything;
//	}
}
