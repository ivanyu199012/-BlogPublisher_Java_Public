package com.blog.publish.publisher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import com.blog.publish.publisher.BlogInfo.SITE;
import com.blog.publish.publisher.utils.FileHandler;

/**
 * Unit test for simple App.
 */
public class AppTest
{
	@Test
	public void test_getCommandLineBy() throws ParseException
	{
		String[] args = {
				"temp\\sample.md",
				"--title",
				"Sample Title",
				"--canonicalUrl",
				"https://ivanyu2021.hashnode.dev/correct-steps-to-setup-basic-environment-for-python",
				"--series",
				"Python",
				"--sites",
				"DEVTO,MEDIUM",
				"--tags",
				"Python",
				"--imageUrl",
				"https://ivanyu2021.hashnode.dev/_next/image?url=https%3A%2F%2Fcdn.hashnode.com%2Fres%2Fhashnode%2Fimage%2Fupload%2Fv1626249332587%2FK6qFAif_l.png%3Fw%3D1600%26h%3D840%26fit%3Dcrop%26crop%3Dentropy%26auto%3Dcompress%2Cformat%26format%3Dwebp&w=1920&q=75",
				"--subtitle",
				"Sample Subtitle"
				};
		CommandLine cmd = App.getCommandLineBy( args );
		assertEquals( cmd.getArgs()[ 0 ], args[ 0 ] );
		assertEquals( cmd.getOptionValue( "title" ), args[2] );
		assertEquals( cmd.getOptionValue( "canonicalUrl" ), args[4] );
		assertEquals( cmd.getOptionValue( "series" ), args[6] );
		assertEquals( cmd.getOptionValue( "sites" ), args[8] );
		assertEquals( cmd.getOptionValue( "tags" ), args[10] );
		assertEquals( cmd.getOptionValue( "imageUrl" ), args[12] );
	}

	@Test
	public void test_getBlogInfoFrom() throws ParseException, IOException
	{
		String[] args = {
				"temp\\sample.md",
				"--title",
				"Sample Title",
				"--canonicalUrl",
				"https://ivanyu2021.hashnode.dev/correct-steps-to-setup-basic-environment-for-python",
				"--series",
				"Python",
				"--sites",
				"DEVTO,MEDIUM",
				"--tags",
				"Python",
				"--imageUrl",
				"https://ivanyu2021.hashnode.dev/_next/image?url=https%3A%2F%2Fcdn.hashnode.com%2Fres%2Fhashnode%2Fimage%2Fupload%2Fv1626249332587%2FK6qFAif_l.png%3Fw%3D1600%26h%3D840%26fit%3Dcrop%26crop%3Dentropy%26auto%3Dcompress%2Cformat%26format%3Dwebp&w=1920&q=75",
				"--subtitle",
				"Sample Subtitle"
				};
		CommandLine cmd = App.getCommandLineBy( args );
		BlogInfo blogInfo = App.getBlogInfoFrom( cmd );
		assertEquals( blogInfo.getFilepath(), cmd.getArgs()[ 0 ] );
		assertEquals( blogInfo.getTitle(), cmd.getOptionValue( "title" ) );
		assertEquals( blogInfo.getCanonicalUrl(), cmd.getOptionValue( "canonicalUrl" ) );
		assertEquals( blogInfo.getSeries(), cmd.getOptionValue( "series" ) );
		SITE[] sites = { SITE.DEVTO, SITE.MEDIUM };
		assertTrue( Arrays.asList( blogInfo.getSites() ).containsAll( Arrays.asList( sites ) ) );
		assertTrue( Arrays.asList( blogInfo.getTags() ).containsAll( Arrays.asList( cmd.getOptionValue( "tags" ).split( "," ) ) ) );
		assertEquals( blogInfo.getImageUrl(), cmd.getOptionValue( "imageUrl" ) );

		FileHandler.writeObjectToFile( blogInfo, "temp/blogInfo.txt" );
	}

	@Ignore
	@Test
	public void test_main() throws ParseException, IOException, InterruptedException
	{
		String[] args = {
				"temp\\sample.md",
				"--title",
				"Sample Title",
				"--canonicalUrl",
				"https://ivanyu2021.hashnode.dev/correct-steps-to-setup-basic-environment-for-python",
				"--series",
				"Python",
				"--sites",
				"DEVTO,MEDIUM",
				"--tags",
				"Python",
				"--imageUrl",
				"https://ivanyu2021.hashnode.dev/_next/image?url=https%3A%2F%2Fcdn.hashnode.com%2Fres%2Fhashnode%2Fimage%2Fupload%2Fv1626249332587%2FK6qFAif_l.png%3Fw%3D1600%26h%3D840%26fit%3Dcrop%26crop%3Dentropy%26auto%3Dcompress%2Cformat%26format%3Dwebp&w=1920&q=75",
				"--subtitle",
				"Sample Subtitle"
				};
		App.main( args );
	}
}
