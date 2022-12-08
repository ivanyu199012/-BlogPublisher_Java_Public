package com.blog.publish.publisher;

import static org.junit.Assert.assertTrue;

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
		Map< String, String > idToGistLinkMap = FileHandler.readFileToMap( App.ID_TO_GIST_LINK_MAP_PATH );
		for ( Map.Entry<String,String> entry : idToGistLinkMap.entrySet() )
		{
			String gistLink = entry.getValue();
			String gistCodeBlockId = gistLink.replace( "https://gist.github.com/", "" );
			GistCodeHandler.deleteGist( gistCodeBlockId );
		};
	}
}
