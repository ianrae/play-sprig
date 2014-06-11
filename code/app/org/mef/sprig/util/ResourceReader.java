package org.mef.sprig.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public class ResourceReader 
{
	public static String readSeedFile(String filename, String dir)
	{
		String path;
		if (! dir.startsWith("/"))
		{
			String currentDir = new File(".").getAbsolutePath();
			path = FilenameUtils.concat(currentDir, dir);
		}
		else
		{
			path = dir;
		}
		
		path = FilenameUtils.concat(path, filename);
		return readFile(path);
	}
	
	public static String readFile(String path) 
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		boolean succeeded = false;
		
	    try {
	    	br = new BufferedReader(new FileReader(path));
	        String line = br.readLine();

	        while (line != null) 
	        {
	            sb.append(line);
	            sb.append('\n');
	            line = br.readLine();
	        }
	        succeeded = true;	        
	    } 
	    catch (Exception e) 
	    {
			e.printStackTrace();
		}
	    finally 
	    {
	    	if (br != null)
	    	{
	    		try 
	    		{
					br.close();
				} 
	    		catch (IOException e) 
	    		{
//					e.printStackTrace();
				}
	    	}
	    }	
	    
	    if (! succeeded)
	    {
	    	return null;
	    }
	    
	    String everything = sb.toString();
	    return everything;
	}
}
