package com.blog.publish.publisher;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blog.publish.publisher.utils.FileHandler;
import com.blog.publish.publisher.utils.GistCodeHandler;
import com.blog.publish.publisher.utils.MediumUploader;

public class MediumUploaderTest
{
	private static InitialData initialData = null;
	private static final Logger logger = LogManager.getLogger( GistCodeHandlerTest.class );

	@BeforeClass
	public static void setUp() throws IOException, InterruptedException
	{
		String markdown = FileHandler.readFile( "temp\\5. Django_background_task.md" );
		Map< String, Object > resultMap = GistCodeHandler.convertBlogCodeToGist( "5_Django_background_task.md", markdown );
		String markdownText = ( String ) resultMap.get( GistCodeHandler.TEMP_MARKDOWN_KEY );
		Map< String, String > idToGistLinkMap = ( Map< String, String > ) resultMap.get( GistCodeHandler.ID_TO_GIST_LINK_MAP_KEY );
		initialData = new InitialData( markdownText, idToGistLinkMap );
	}

	@Test
	public void test_formatMarkdownText() throws IOException, InterruptedException
	{
		String formattedMarkdownText = MediumUploader.formatMarkdownText( initialData.getMarkdownTex(), initialData.getIdToGistLinkMap() );
		logger.info( "formattedMarkdownText = " + formattedMarkdownText );
		for ( Map.Entry<String,String> entry : initialData.getIdToGistLinkMap().entrySet() )
		{
			String id = entry.getKey();
			String gistLink = entry.getValue();
			assertTrue( ! formattedMarkdownText.contains( id ) );
			assertTrue( formattedMarkdownText.contains( gistLink ) );
			String gistCodeBlockId = gistLink.replace( "https://gist.github.com/", "" );
			GistCodeHandler.deleteGist( gistCodeBlockId );
		};
	}

	static class InitialData
	{
		String markdownTex;
		Map< String, String > idToGistLinkMap;

		public InitialData(String markdownTex, Map< String, String > idToGistLinkMap) {
			super();
			this.markdownTex = markdownTex;
			this.idToGistLinkMap = idToGistLinkMap;
		}

		public String getMarkdownTex()
		{
			return markdownTex;
		}

		public Map< String, String > getIdToGistLinkMap()
		{
			return idToGistLinkMap;
		}
	}
}
