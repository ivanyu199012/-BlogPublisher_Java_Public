package com.blog.publish.publisher.utils;

import java.net.http.HttpResponse;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils
{
	private static Logger logger = LogManager.getLogger( Utils.class );

	public static boolean isNullOrEmpty( List<Object> l )
	{
		return l == null || l.size() == 0;
	}

	public static boolean isNullOrEmpty( Object[] arr )
	{
		return arr == null || arr.length == 0;
	}

	public static void logResponseError( HttpResponse<String> response )
	{
		logger.error( "response.statusCode() = " + response.statusCode() );
		logger.error( "response = " + response );
		logger.error( "response.body = " + response.body() );
	}
}
