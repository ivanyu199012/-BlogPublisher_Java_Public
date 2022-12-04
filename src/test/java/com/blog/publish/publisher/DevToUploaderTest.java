package com.blog.publish.publisher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.blog.publish.publisher.utils.DevToUploader;
import com.blog.publish.publisher.utils.FileHandler;

public class DevToUploaderTest
{
	private static Logger logger = LogManager.getLogger( FileHandlerTest.class );

	@Test
	public void test_formatMarkdownText() throws IOException
	{
		String markdown = FileHandler
				.readFile( "C:\\Ivan\\Git\\17. EclipseWorkspace\\publisher\\temp\\5. Django_background_task.md" );
		markdown = DevToUploader.formatMarkdownText( markdown );
		assertTrue( markdown.contains( "## 1. Introduction & Proof-Of-Concept" ) );
	}

	@Test
	public void test_prepareReqDataDict() throws IOException, ClassNotFoundException
	{
		String markdown = FileHandler
				.readFile( "C:\\Ivan\\Git\\17. EclipseWorkspace\\publisher\\temp\\5. Django_background_task.md" );
		BlogInfo blogInfo = ( BlogInfo ) FileHandler.readFileToObject( "temp/blogInfo.txt" );
		Map<String, Object> articleMap = DevToUploader.prepareArticleMap( blogInfo, markdown );
		assertTrue( articleMap.containsKey( "article" ) );
		
		Map<String, Object> reqMap = ( Map<String, Object> ) articleMap.get( "article" );
		assertEquals( reqMap.get( "title" ), blogInfo.getTitle() );
		assertEquals( reqMap.get( "body_markdown" ), markdown );
		assertEquals( reqMap.get( "published" ), blogInfo.getPublishStatus().devToStatus );
		assertEquals( reqMap.get( "canonical_url" ), blogInfo.getCanonicalUrl() );
	}
	
	@Test
	public void test_postArticle() throws IOException, ClassNotFoundException, InterruptedException
	{
		String markdown = FileHandler
				.readFile( "C:\\Ivan\\Git\\17. EclipseWorkspace\\publisher\\temp\\5. Django_background_task.md" );
		markdown = DevToUploader.formatMarkdownText( markdown );
		
		BlogInfo blogInfo = ( BlogInfo ) FileHandler.readFileToObject( "temp/blogInfo.txt" );
		blogInfo.setTitle( blogInfo.getTitle() + "_v1" );
		
		Map<String, Object> reqMap = DevToUploader.prepareArticleMap( blogInfo, markdown + " TEST" );
		String postUrl = DevToUploader.postArticle( reqMap );
		String urlPattern = "^https:\\/\\/dev\\.to\\/ivanyu2021\\/.*\\/edit$";
		assertTrue( postUrl.matches( urlPattern ) );
	}
}
