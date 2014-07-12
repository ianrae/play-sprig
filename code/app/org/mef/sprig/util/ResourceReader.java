package org.mef.sprig.util;


public class ResourceReader 
{
	public static String readSeedFile(String filename, String dir)
	{
		MyResourceReader r = new MyResourceReader();
		if (! dir.isEmpty() && ! dir.endsWith("/"))
		{
			dir += "/";
		}
//		String relPath = FilenameUtils.concat(dir, filename);
		String relPath = dir + filename;
		String results = r.read(relPath);
		return results;
	}
	
}
