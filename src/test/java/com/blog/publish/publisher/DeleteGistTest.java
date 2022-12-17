package com.blog.publish.publisher;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import com.blog.publish.publisher.utils.FileHandler;
import com.blog.publish.publisher.utils.GistCodeHandler;

public class DeleteGistTest
{
	@Test
	public void test_deleteGists() throws ParseException, IOException, InterruptedException
	{
		String path = "temp/idToGistLinkMap.txt";
		if ( new File( path ).exists() ) {
			Map< String, String > idToGistLinkMap = FileHandler.readFileToMap( path );
			for (Map.Entry< String, String > entry : idToGistLinkMap.entrySet()) {
				String gistLink = entry.getValue();
				String gistCodeBlockId = gistLink.replace( "https://gist.github.com/", "" );
				GistCodeHandler.deleteGist( gistCodeBlockId );
			}
		}
	}

	@Test
	public void test_deleteGistsTargetFolder() throws ParseException, IOException, InterruptedException
	{
		String path = "target/" + App.ID_TO_GIST_LINK_MAP_PATH;
		if ( new File( path ).exists() ) {
			if ( new File( path ).exists() ) {
				Map< String, String > idToGistLinkMap = FileHandler.readFileToMap( path );
				for (Map.Entry< String, String > entry : idToGistLinkMap.entrySet()) {
					String gistLink = entry.getValue();
					String gistCodeBlockId = gistLink.replace( "https://gist.github.com/", "" );
					GistCodeHandler.deleteGist( gistCodeBlockId );
				}
			}
		}
	}
}
