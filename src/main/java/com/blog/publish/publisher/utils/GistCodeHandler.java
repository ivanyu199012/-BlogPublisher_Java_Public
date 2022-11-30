package com.blog.publish.publisher.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blog.publish.publisher.utils.data.GistCodeData;
import com.fasterxml.jackson.databind.ObjectMapper;


public class GistCodeHandler {
	
	private static Logger logger = LogManager.getLogger( GistCodeHandler.class );

	public static String uploadCodeBlockToGist( String description, String filename, String codeBlock )
			throws IOException, InterruptedException {

		GistCodeData gistCodData = new GistCodeData();
		gistCodData.setDescription( description );
		gistCodData.setPublic( true );
		Map<String, String> contentMap = Map.of( "content", codeBlock );
		Map<String, Map<String, String>> filename2ContentMap = Map.of( filename, contentMap );
		gistCodData.setFiles( filename2ContentMap );

		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString( gistCodData );

		HttpRequest request = HttpRequest.newBuilder( URI.create( "https://api.github.com/gists" ) )
				.header( "Accept", "application/vnd.github+json" )
				.header( "Authorization", "Bearer " + Token.getGithubToken() )
				.POST( HttpRequest.BodyPublishers.ofString( jsonStr ) ).build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send( request, HttpResponse.BodyHandlers.ofString() );
		
		if ( response.statusCode() != 200 && response.statusCode() != 201 ) 
		{
			logger.error( "response.statusCode() = " + response.statusCode() );
			logger.error( response.body() );
			return null;
		}
		
		Map<String, String> responseMap = mapper.readValue( response.body(), Map.class );
		logger.info( "html_url = " + responseMap.get( "html_url" ) );

		return responseMap.get( "html_url" );
	}

}
