package com.blog.publish.publisher;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.blog.publish.publisher.utils.Token;

public class TokenTest {
	
	private static Logger logger = LogManager.getLogger(TokenTest.class);
	
	@Test
	public void test_getGithubToken()
	{
		String token = Token.getGithubToken();
		logger.info("token = " + token);
		assertTrue(token.contains("ghp_"));
	}
}
