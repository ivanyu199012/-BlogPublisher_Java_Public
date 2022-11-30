package com.blog.publish.publisher.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.blog.publish.publisher.utils.data.GistCodeData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GistCodeHandler {
	
	public static String uploadCodeBlockToGist(String codeBlock) throws IOException, InterruptedException
	{
		String htmlUrl = null;
		
		HttpClient client = HttpClient.newHttpClient();
		
		GistCodeData gistCodData = new GistCodeData();
		gistCodData.setDescription("Hello World Description");
		gistCodData.setPublic(true);
		Map<String, String> contentMap = Map.of( "content", "Hello World!!!" );
		Map< String, Map< String, String > > filename2ContentMap = Map.of("helloWorld.txt", contentMap);
		gistCodData.setFiles(filename2ContentMap);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(gistCodData);
		
		HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.github.com/gists"))
			.header("Accept", "application/vnd.github+json")
			.header("Authorization", "Bearer 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")
			.POST(HttpRequest.BodyPublishers.ofString( jsonStr ))
			.build();
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println("response status code = " + response.statusCode() );
		System.out.println("response body = " + response.body() );
		
		return htmlUrl;
	}

}
