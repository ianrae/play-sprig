package org.mef.sprig;

import play.Logger;

public class SprigLogger 
{
	public static boolean debugLoggingEnabled = false;
	
	//--helper--
	public static void log(String s)
	{
		Logger.info("[Sprig] " + s);
	}
	public static void logDebug(String s)
	{
		Logger.info("[Sprig] " + s);
	}
}
