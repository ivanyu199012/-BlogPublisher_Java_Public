package com.blog.publish.publisher;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.blog.publish.publisher.utils.GistCodeHandler;

public class GistCodeHandlerTest {

	private static Logger logger = LogManager.getLogger( GistCodeHandlerTest.class );
	
	@Test
	public void test_uploadCodeBlockToGist() throws IOException, InterruptedException
	{
		String htmlUrl = GistCodeHandler.uploadCodeBlockToGist( "Hello World Description", "helloWorld.txt", "Hello World !!" );
		assertTrue( htmlUrl.contains( "https://gist.github.com/" ) );
		
		String id = htmlUrl.replace( "https://gist.github.com/", "" );
		logger.info( "id = " + id );
		
		GistCodeHandler.deleteGist( id );
	}
}
