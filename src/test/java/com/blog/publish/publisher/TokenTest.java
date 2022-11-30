package com.blog.publish.publisher;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.blog.publish.publisher.utils.Token;

public class TokenTest {
	
	@Test
	public void test_getGithubToken()
	{
		String token = Token.getGithubToken();
		assertTrue(token.contains("ghp_"));
	}
}
