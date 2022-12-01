package com.blog.publish.publisher.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blog.publish.publisher.BlogInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DevToUploader
{
	private static Logger logger = LogManager.getLogger( DevToUploader.class );
	
	public static String formatMarkdownText( String markdown ) 
	{
		return markdown.replace( "# ", "## " );
	}
	
	public static Map<String, Object> prepareReqDataDict( BlogInfo blogInfo, String markdown )
	{
		Map< String, Object > reqDataMap = new HashMap<>();
		reqDataMap.put( "title", blogInfo.getTitle() );
		reqDataMap.put( "body_markdown", markdown );
		reqDataMap.put( "published", blogInfo.getPublishStatus().devToStatus );
		reqDataMap.put( "canonical_url", blogInfo.getCanonicalUrl() );
		
		if ( ! StringUtils.isBlank( blogInfo.getSeries() ) )
			reqDataMap.put( "series", blogInfo.getSeries() );
		
		if ( ! StringUtils.isBlank( blogInfo.getImageUrl() ))
			reqDataMap.put( "main_image", blogInfo.getImageUrl() );
		
		if ( blogInfo.getTags() != null && blogInfo.getTags().length > 0 ) 
		{
			String[] tags = Arrays.stream( blogInfo.getTags() ).map( tag -> tag.replace( " ", "" ) ).toArray( String[]::new );
			reqDataMap.put( markdown, tags );
		}
		
		return reqDataMap;
	}
	
	public static String postArticle(Map<String, Object> reqDataMap) throws IOException, InterruptedException
	{
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString( reqDataMap );
		
		HttpRequest request = HttpRequest.newBuilder( URI.create( "https://dev.to/api/articles" ) )
			.header( "Content-Type", "application/json" )
			.header( "api-key", Token.getDevToApiKey() )
			.POST( HttpRequest.BodyPublishers.ofString( jsonStr ) )
			.build();
		
		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send( request, HttpResponse.BodyHandlers.ofString() );
		
		if ( response.statusCode() != 201 ) 
		{
			logger.error( "response.statusCode() = " + response.statusCode() );
			logger.error( response.body() );
			return null;
		}
		
		Map<String, String> responseMap = mapper.readValue( response.body(), Map.class );
		String postUrl = responseMap.get( "url" ) + "/edit";
		logger.info( "postUrl = " + postUrl );
		
		return postUrl;
	}
}
