package com.blog.publish.publisher.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Token {
	
	private static Logger logger = LogManager.getLogger( Token.class );
	
	private static Properties prop;
	
	static {
		FileInputStream ip = null;
		String path = "tokens.properties";
		try {
			ip = new FileInputStream(path);
			prop = new Properties();
			prop.load(ip);
			ip.close();
		}
		catch (IOException e)
		{
			logger.error( "failed to read " + path, e );
		}
	}
	
	public static String getGithubToken()
	{
		return prop.getProperty("GITHUB_TOKEN");
	}
	
	public static String getDevToApiKey()
	{
		return prop.getProperty("DEV_TO_TOKEN");
	}
}
