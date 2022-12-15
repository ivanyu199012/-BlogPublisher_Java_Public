package com.blog.publish.publisher.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blog.publish.publisher.utils.data.GistCodeData;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GistCodeHandler
{

	private static final Logger logger = LogManager.getLogger( GistCodeHandler.class );
	public static final String DELIMITER = "_@_";
	public static final Map<String, String> LANG_2_EXT_MAP = Map.ofEntries(
		new AbstractMap.SimpleEntry<String, String>( "python", "py" ),
		new AbstractMap.SimpleEntry<String, String>( "javascript", "js" ),
		new AbstractMap.SimpleEntry<String, String>( "typescript", "ts" )
	);
	public static final String TEMP_MARKDOWN_KEY = "TEMP_MARKDOWN_KEY";
	public static final String ID_TO_CODE_BLOCK_INFO_MAP_KEY = "ID_TO_CODE_BLOCK_INFO_MAP_KEY";
	public static final String ID_TO_GIST_LINK_MAP_KEY = "ID_TO_CODE_BLOCK_MAP_KEY";

	public static Map<String, Object> convertBlogCodeToGist( String fileBaseName, String markdownText ) throws IOException, InterruptedException
	{

		Map< String, Object > resultMap = GistCodeHandler.convertCodeBlockToId( fileBaseName,markdownText );
		String tempMarkdownText = ( String ) resultMap.get( TEMP_MARKDOWN_KEY );
		Map< String, CodeBlockInfo > idToCodeBlockMap = ( Map< String, CodeBlockInfo > ) resultMap.get( ID_TO_CODE_BLOCK_INFO_MAP_KEY );

		Map< String, String > idToGistLinkMap = new HashMap<>();
		for ( Map.Entry<String,CodeBlockInfo> entry : idToCodeBlockMap.entrySet() )
		{
			String id = entry.getKey();
			CodeBlockInfo codeBlockInfo = entry.getValue();

			String filename = id.replaceAll( DELIMITER, "" ).replaceAll( ".md", "" );
			String ext = "";
			if ( codeBlockInfo.getLanguage() != null && LANG_2_EXT_MAP.containsKey( codeBlockInfo.getLanguage() ) )
			{
				ext = "." + LANG_2_EXT_MAP.get( codeBlockInfo.getLanguage() );
			}

			String filenameWithExt = filename + ext;
			String htmlUrl = uploadCodeBlockToGist( filename, filenameWithExt, codeBlockInfo.getCodeBlock() );
			idToGistLinkMap.put( id, htmlUrl );
		}

		resultMap = new HashMap<>();
		resultMap.put( TEMP_MARKDOWN_KEY, tempMarkdownText );
		resultMap.put( ID_TO_GIST_LINK_MAP_KEY, idToGistLinkMap );
		return resultMap;
	}

	public static Map<String, Object> convertCodeBlockToId( String fileBaseName, String markdownText )
	{
		String[] codeBlockArr = Pattern.compile( "```[\\s\\S][^```]+```" ).matcher( markdownText ).results().map( MatchResult::group ).toArray(String[]::new);

		String tempMarkdownText = markdownText;
		Map<String, CodeBlockInfo> idToCodeBlockInfoMap = new HashMap<>();
		for( int index = 0; index < codeBlockArr.length; index++ )
		{
			String codeBlock = codeBlockArr[ index ];
			String id = DELIMITER + fileBaseName + "_code_" + index + DELIMITER;
			tempMarkdownText = tempMarkdownText.replace( codeBlock, id );

			String language = null;
			Matcher codeBlockPrefixMatcher = Pattern.compile( "```(.*)\\R" ).matcher( codeBlock );
			if( codeBlockPrefixMatcher.find() )
			{
				language = codeBlockPrefixMatcher.group( 1 ).trim().toLowerCase();
				logger.info( "Code Block #" + id +" = " + language );
			}
			else
			{
				logger.info( "Code Block #" + id + " has no language" );
			}

			codeBlock = codeBlock.replaceAll( "```.*\\R", "" ).replaceAll( "```", "" );
			CodeBlockInfo codeBlockInfo = new CodeBlockInfo( language, codeBlock );
			idToCodeBlockInfoMap.put( id, codeBlockInfo );
		}

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put( TEMP_MARKDOWN_KEY, tempMarkdownText );
		resultMap.put( ID_TO_CODE_BLOCK_INFO_MAP_KEY, idToCodeBlockInfoMap );
		return resultMap;
	}

	public static String uploadCodeBlockToGist( String description, String filename, String codeBlock )
			throws IOException, InterruptedException
	{

		GistCodeData gistCodData = new GistCodeData();
		gistCodData.setDescription( description );
		gistCodData.setPublic( true );
		Map<String, String> contentMap = Map.of( "content", codeBlock );
		Map<String, Map<String, String>> filename2ContentMap = Map.of( filename, contentMap );
		gistCodData.setFiles( filename2ContentMap );

		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString( gistCodData );

		HttpRequest request = getCommonHeaderRequestBuilder().uri( URI.create( "https://api.github.com/gists" ) )
				.POST( HttpRequest.BodyPublishers.ofString( jsonStr ) ).build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send( request, HttpResponse.BodyHandlers.ofString() );

		if ( response.statusCode() != 200 && response.statusCode() != 201 ) {
			Utils.logResponseError( response );
			return null;
		}

		Map<String, String> responseMap = mapper.readValue( response.body(), Map.class );
		logger.info( "html_url = " + responseMap.get( "html_url" ) );

		return responseMap.get( "html_url" );
	}

	public static void deleteGist( String id ) throws IOException, InterruptedException
	{
		HttpRequest request = getCommonHeaderRequestBuilder().uri( URI.create( "https://api.github.com/gists/" + id ) )
				.DELETE().build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send( request, HttpResponse.BodyHandlers.ofString() );

		if ( response.statusCode() != 204 ) {
			Utils.logResponseError( response );
		}

		logger.info( "Gist with id #" + id + " has been deleted successfully" );
	}

	private static Builder getCommonHeaderRequestBuilder()
	{
		return HttpRequest.newBuilder().header( "Accept", "application/vnd.github+json" ).header( "Authorization",
				"Bearer " + Token.getGithubToken() );
	}

	public static class CodeBlockInfo
	{
		public String language;
		public String codeBlock;

		public CodeBlockInfo(String language, String codeBlock) {
			super();
			this.language = language;
			this.codeBlock = codeBlock;
		}

		public String getLanguage()
		{
			return language;
		}
		public void setLanguage( String language )
		{
			this.language = language;
		}
		public String getCodeBlock()
		{
			return codeBlock;
		}
		public void setCodeBlock( String codeBlock )
		{
			this.codeBlock = codeBlock;
		}
	}
}
