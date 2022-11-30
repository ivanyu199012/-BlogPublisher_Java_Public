package com.blog.publish.publisher.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Token {
	
	private static Properties prop;
	
	static {
		FileInputStream ip = null;
		try {
			ip = new FileInputStream("tokens.properties");
			prop = new Properties();
			prop.load(ip);
			ip.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getGithubToken()
	{
		return prop.getProperty("GITHUB_TOKEN");
	}
}
