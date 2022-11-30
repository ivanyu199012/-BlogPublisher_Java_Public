package com.blog.publish.publisher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.blog.publish.publisher.utils.GistCodeHandler;

/**
 * Unit test for simple App.
 */
public class AppTest {
	/**
	 * Rigorous Test :-)
	 */
	@Test
	public void shouldAnswerWithTrue() {
		assertTrue(true);
	}
	
	@Test
	public void test_GistCodeHandler_uploadCodeBlockToGist() throws IOException, InterruptedException
	{
		String htmlUrl = GistCodeHandler.uploadCodeBlockToGist( "Hello World Description", "helloWorld.txt", "Hello World !!" );
		assertTrue( htmlUrl.contains( "https://gist.github.com/" ) );
	}

}
