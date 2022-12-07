package com.blog.publish.publisher.utils;

import java.util.HashMap;
import java.util.Map;

public class MediumUploader
{
	private static Map< String, String > headerMap = null;

	static
	{
		headerMap = new HashMap<>();
		headerMap.put( "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" );
		headerMap.put( "Accept-Encoding", "gzip, deflate, br" );
		headerMap.put( "Accept-Language", "en-US,en;q=0.5" );
		headerMap.put( "Connection", "keep-alive" );
		headerMap.put( "Host", "api.medium.com" );
		headerMap.put( "Authorization", "Bearer " + Token.getMediumToken() );
		headerMap.put( "Upgrade-Insecure-Requests", "1" );
		headerMap.put( "User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0" );
	}

	public static String formatMarkdownText( String markdownText, Map<String, String> idToGistLinkMap )
	{
		String formattedMarkdownText = markdownText;
		for ( Map.Entry<String,String> entry : idToGistLinkMap.entrySet() )
		{
			String id = entry.getKey();
			String gistLink = entry.getValue();
			formattedMarkdownText = formattedMarkdownText.replace( id, "\n" + gistLink + "\n" );
		};

		return formattedMarkdownText;
	}
}
