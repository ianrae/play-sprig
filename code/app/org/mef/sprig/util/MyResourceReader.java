package org.mef.sprig.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import play.Application;
import play.Logger;
import play.Play;

//reads files in conf/ dir. Works in prod and dev mode
//The class is here because it needs Play.application(). I copy it to sprig
//once its tested here
public class MyResourceReader 
{
	public String read(String relPath)
	{
		String contents = null;
		Logger.info("rr: read: " + relPath);
		InputStream in = this.getStream(relPath);
		if (in != null)
		{
			try {
				contents = readInputStream(in);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		return contents;
	}
	
	InputStream getStream(String relPath)
	{
		Application app = Play.application();
		InputStream in = app.resourceAsStream(relPath); 
		if (in != null)
		{
			Logger.info("rr: found resource");
			return in;
		}
		else
		{
			Logger.info("rr: try as file");
			File f = app.getFile(relPath);
			FileInputStream fstream = null;
			try {
				fstream = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return fstream;
		}
		
	}
	private String readInputStream(InputStream input) throws IOException 
	{
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder sb = new StringBuilder();
        while (true) 
        {
            String line = reader.readLine();
            if (line == null) break;
            //System.out.println("    " + line);
            sb.append(line);
            sb.append('\n');
        }
        
	    String everything = sb.toString();
	    return everything;
    }
}
