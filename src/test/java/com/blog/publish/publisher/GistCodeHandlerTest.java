package com.blog.publish.publisher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.blog.publish.publisher.utils.FileHandler;
import com.blog.publish.publisher.utils.GistCodeHandler;
import com.blog.publish.publisher.utils.GistCodeHandler.CodeBlockInfo;

public class GistCodeHandlerTest
{

	private static Logger logger = LogManager.getLogger( GistCodeHandlerTest.class );

	@Test
	public void test_uploadCodeBlockToGist() throws IOException, InterruptedException
	{
		String htmlUrl = GistCodeHandler.uploadCodeBlockToGist( "Hello World Description", "helloWorld.txt",
				"Hello World !!" );
		assertTrue( htmlUrl.contains( "https://gist.github.com/" ) );

		String id = htmlUrl.replace( "https://gist.github.com/", "" );
		logger.info( "id = " + id );

		GistCodeHandler.deleteGist( id );
	}

	@Test
	public void test_convertCodeBlockToId() throws IOException
	{
		String markdown = FileHandler.readFile( "temp\\5. Django_background_task.md" );
		Map< String, Object > resultMap = GistCodeHandler.convertCodeBlockToId( "5_Django_background_task.md",markdown );
		assertTrue( resultMap.containsKey( GistCodeHandler.TEMP_MARKDOWN_KEY ) );
		assertTrue( !( ( String ) resultMap.get( GistCodeHandler.TEMP_MARKDOWN_KEY ) ).contains( "```" ) );

		assertTrue( resultMap.containsKey( GistCodeHandler.ID_TO_CODE_BLOCK_INFO_MAP_KEY ) );
		assertTrue( ( ( Map< String, String > ) resultMap.get( GistCodeHandler.ID_TO_CODE_BLOCK_INFO_MAP_KEY ) ).size() == 6 );
		CodeBlockInfo codeBlockInfo = ( ( Map< String, CodeBlockInfo > ) resultMap.get( GistCodeHandler.ID_TO_CODE_BLOCK_INFO_MAP_KEY ) ).get( "_@_5_Django_background_task.md_code_5_@_" );
		assertTrue( ! codeBlockInfo.getCodeBlock().contains( "```" ) );
		assertEquals( codeBlockInfo.getLanguage(), "python" );
	}

	@Test
	public void test_convertBlogCodeToGist() throws IOException, InterruptedException
	{
		String markdown = FileHandler.readFile( "temp\\5. Django_background_task.md" );
		Map< String, Object > resultMap = GistCodeHandler.convertBlogCodeToGist( "5_Django_background_task.md", markdown );
		assertTrue( resultMap.containsKey( GistCodeHandler.TEMP_MARKDOWN_KEY ) );
		assertTrue( !( ( String ) resultMap.get( GistCodeHandler.TEMP_MARKDOWN_KEY ) ).contains( "```" ) );

		assertTrue( resultMap.containsKey( GistCodeHandler.ID_TO_GIST_LINK_MAP_KEY ) );
		Map< String, String > idToGistLinkMap = ( Map< String, String > ) resultMap.get( GistCodeHandler.ID_TO_GIST_LINK_MAP_KEY );
		assertTrue( idToGistLinkMap.size() == 6 );
		for ( Map.Entry<String,String> entry : idToGistLinkMap.entrySet() )
		{
			String gistLink = entry.getValue();
			assertTrue( gistLink.contains( "https://gist.github.com/" ) );
			String gistCodeBlockId = gistLink.replace( "https://gist.github.com/", "" );
			GistCodeHandler.deleteGist( gistCodeBlockId );
		};
	}
}
