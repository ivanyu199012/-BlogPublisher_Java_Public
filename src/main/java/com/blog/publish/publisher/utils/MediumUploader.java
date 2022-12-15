package com.blog.publish.publisher.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blog.publish.publisher.BlogInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MediumUploader
{
	private static final Logger logger = LogManager.getLogger( MediumUploader.class );
	private static Map< String, String > headerMap = Map.ofEntries(
			Map.entry( "Authorization", "Bearer " + Token.getMediumToken() ),
			Map.entry( "Accept", "application/json" ),
			Map.entry( "Accept-Charset", "utf-8" ),
			Map.entry( "Content-Type", "application/json" ),
			Map.entry( "Upgrade-Insecure-Requests", "1" ),
			Map.entry( "User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0" )
		);

	public static String formatMarkdownText( String markdownText, Map<String, String> idToGistLinkMap, BlogInfo blogInfo )
	{
		String formattedMarkdownText = markdownText;
		for ( Map.Entry<String,String> entry : idToGistLinkMap.entrySet() )
		{
			String id = entry.getKey();
			String gistLink = entry.getValue();
			formattedMarkdownText = formattedMarkdownText.replace( id, "\n" + gistLink + "\n" );
		};

		StringBuilder sb = new StringBuilder();
		sb.append( "\n![image.png](" + blogInfo.getImageUrl() + ")\n"  );
		sb.append( "\n# " + blogInfo.getTitle() + "\n"  );
		if ( ! StringUtils.isBlank( blogInfo.getSubtitle() ) )
		{
			sb.append( "\n# " + blogInfo.getSubtitle() + "\n"  );
		}
		sb.append( formattedMarkdownText  );
		return sb.toString();
	}

	public static Map<String, Object> prepReqDataMap( BlogInfo blogInfo, String markdown  )
	{
		Map<String, Object> reqDataMap = new HashMap<>();
		reqDataMap.put( "title", blogInfo.getTitle() );
		reqDataMap.put( "content", markdown );
		reqDataMap.put( "contentFormat", "markdown" );
		reqDataMap.put( "canonicalUrl", blogInfo.getCanonicalUrl() );
		reqDataMap.put( "publishStatus", blogInfo.getPublishStatus().mediumStatus );

		if ( ! Utils.isNullOrEmpty( blogInfo.getTags() ) )
		{
			String[] tags = Arrays.stream( blogInfo.getTags() ).map( String :: trim ).toArray( String[]::new );
			reqDataMap.put( "tags", tags );
		}
		return reqDataMap;
	}

	public static String postArticle( Map<String, Object> reqDataMap ) throws IOException, InterruptedException
	{
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString( reqDataMap );

		String authorId = getAuthorId();
		HttpRequest request = getCommonHeaderBuilder()
				.uri( URI.create( "https://api.medium.com/v1/users/" + authorId + "/posts" ) )
				.POST( HttpRequest.BodyPublishers.ofString( jsonStr ) )
				.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send( request, HttpResponse.BodyHandlers.ofString() );

		if ( response.statusCode() != 200 && response.statusCode() != 201 ) {
			logger.error( "response.statusCode() = " + response.statusCode() );
			logger.error( response.body() );
			return null;
		}

		Map<String, Object> responseMap = mapper.readValue( response.body(), Map.class );
		String postUrl = ( String ) ( ( Map<String, Object> ) responseMap.get( "data" ) ).get( "url" );
		logger.info( "Medium postUrl = " + postUrl );

		Matcher postIdMatcher = Pattern.compile( "https:\\/\\/medium\\.com\\/.*\\/(.*)" ).matcher( postUrl );
		String editPostUrl = null;
		if( ! postIdMatcher.find() )
		{
			logger.error( "postId is not found" );
			return null;
		}

		editPostUrl = "https://medium.com/p/" + postIdMatcher.group( 1 ) + "/edit";
		logger.info( "editPostUrl = " + editPostUrl );
		return editPostUrl;
	}

	private static String getAuthorId() throws IOException, InterruptedException
	{
		HttpRequest request = getCommonHeaderBuilder().uri( URI.create( "https://api.medium.com/v1/me" ) )
			.GET()
			.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send( request, HttpResponse.BodyHandlers.ofString() );

		if ( response.statusCode() != 200 && response.statusCode() != 201 ) {
			Utils.logResponseError( response );
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> responseMap = mapper.readValue( response.body(), Map.class );
		String autherId = ( String ) ( ( Map<String, Object> ) responseMap.get( "data" ) ).get( "id" );
		logger.info( "autherId = " + autherId );

		return autherId;
	}

	private static Builder getCommonHeaderBuilder()
	{
		Builder builder =  HttpRequest.newBuilder();
		for ( Map.Entry<String,String> entry : headerMap.entrySet() )
		{
			String key = entry.getKey();
			String val = entry.getValue();
			builder.header( key, val );
		}
		return builder;
	}

}
