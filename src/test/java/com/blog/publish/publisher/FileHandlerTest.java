package com.blog.publish.publisher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.blog.publish.publisher.utils.FileHandler;
import com.blog.publish.publisher.utils.GistCodeHandler;
import com.blog.publish.publisher.utils.Token;

public class FileHandlerTest {

	private static Logger logger = LogManager.getLogger( FileHandlerTest.class );
	
	@Test
	public void test_readFile() throws IOException
	{
		String path = "C:\\Ivan\\Git\\17. EclipseWorkspace\\publisher\\temp\\5. Django_background_task.md";
		String content = FileHandler.readFile( path );
		assertTrue( content.contains( "# 1. Introduction & Proof-Of-Concept" ) );
	}
	
	@Test
	public void test_readFileToObject() throws ClassNotFoundException, IOException 
	{
		BlogInfo blogInfo = (BlogInfo) FileHandler.readFileToObject( "temp/blogInfo.txt" );
		assertEquals( blogInfo.getTitle(), "A simple approach for background task in Django" ); 
	}
}
