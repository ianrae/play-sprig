package org.mef.sprig.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.mef.sprig.SprigLogger;

import play.Application;
import play.Play;

//reads files in conf/ dir. Works in prod and dev mode
//The class is here because it needs Play.application(). I copy it to sprig
//once its tested here
public class MyResourceReader 
{
	public String read(String relPath)
	{
		String contents = null;
		SprigLogger.logDebug(String.format("%s.. ", relPath));
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
		Application app = getApp();
		if (app == null)
		{
			SprigLogger.logDebug("no play app. try as file");
			File f = new File(relPath);
			FileInputStream fstream = null;
			try {
				fstream = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return fstream;
		}

		InputStream in = app.resourceAsStream(relPath); 
		if (in != null)
		{
			SprigLogger.logDebug("found resource");
			return in;
		}
		else
		{
			SprigLogger.logDebug("try as app.getFile");
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

	private Application getApp()
	{
		Application app = null;
		try {
			app = Play.application(); //in unit test Play is not running, so just return null
		} catch (Exception e) {
		}
		return null;
	}

	private String readInputStream(InputStream input) throws IOException 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder sb = new StringBuilder();
		while (true) 
		{
			String line = reader.readLine();
			if (line == null) break;
			sb.append(line);
			sb.append('\n');
		}

		String everything = sb.toString();
		return everything;
	}



	//--helper--
	protected void log(String s)
	{
		SprigLogger.log(s);
	}
}
