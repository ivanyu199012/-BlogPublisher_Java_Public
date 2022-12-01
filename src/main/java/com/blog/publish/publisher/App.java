package com.blog.publish.publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blog.publish.publisher.BlogInfo.SITE;

/**
 * Hello world!
 *
 */
public class App
{
	private static Logger logger = LogManager.getLogger( App.class );

	public static void main( String[] args ) throws ParseException
	{
		Options options = new Options();
		Option title = Option.builder( "t" ).longOpt( "title" ).hasArg().required( true ).desc( "title of post" )
				.build();
		options.addOption( title );

		DefaultParser clParse = new DefaultParser();
		CommandLine cmd = clParse.parse( options, args );

		logger.info( "title = " + cmd.getOptionValue( "title" ) );
		logger.info( "filepath = " + cmd.getArgs()[0] );
	}

	public static CommandLine getCommandLineBy( String[] args ) throws ParseException
	{
		Options options = new Options();
		Option title = Option.builder( "t" ).longOpt( "title" ).hasArg().required( true ).desc( "title of post" )
				.build();
		options.addOption( title );

		Option canonicalUrl = Option.builder( "cUrl" ).longOpt( "canonicalUrl" ).hasArg().required( true )
				.desc( "Canonical Url" ).build();
		options.addOption( canonicalUrl );

		Option series = Option.builder().longOpt( "series" ).hasArg().required( true ).desc( "Series" ).build();
		options.addOption( series );

		Option sites = Option.builder().longOpt( "sites" ).hasArg().required( true )
				.desc( "sites, Published to [DEVTO, MEDIUM], separated by ," ).build();
		options.addOption( sites );

		Option tags = Option.builder( "a" ).longOpt( "tags" ).hasArg().required( false ).desc( "tags, separated by ," )
				.build();
		options.addOption( tags );

		Option publishStatus = Option.builder( "p" ).longOpt( "pub" ).hasArg().required( false )
				.desc( "publish status, one of draft/unlisted/public, defaults to draft" ).build();
		options.addOption( publishStatus );

		Option imageUrl = Option.builder().longOpt( "imageUrl" ).hasArg().required( false ).desc( "Image URL" ).build();
		options.addOption( imageUrl );

		Option subtitle = Option.builder().longOpt( "subtitle" ).hasArg().required( false ).desc( "Subtitle" ).build();
		options.addOption( subtitle );

		DefaultParser clParse = new DefaultParser();
		CommandLine cmd = clParse.parse( options, args );

		return cmd;
	}

	public static BlogInfo getBlogInfoFrom( CommandLine cmd )
	{
		BlogInfo blogInfo = new BlogInfo();
		blogInfo.setFilepath( cmd.getArgs()[0] );
		blogInfo.setTitle( cmd.getOptionValue( "title" ) );
		blogInfo.setCanonicalUrl( cmd.getOptionValue( "canonicalUrl" ) );
		blogInfo.setSeries( cmd.getOptionValue( "series" ) );

		String sitesStr = cmd.getOptionValue( "sites" );
		String[] sites = sitesStr.split( "," );
		List<SITE> siteList = new ArrayList<SITE>();
		Arrays.stream( sites ).forEach( site -> siteList.add( SITE.valueOf( site.trim() ) ) );
		blogInfo.setSites( siteList.toArray( new SITE[0] ) );

		if ( cmd.hasOption( "tags" ) ) {
			String tagsStr = cmd.getOptionValue( "tags" );
			String[] tags = tagsStr.split( "," );
			tags = Arrays.stream( tags ).map( String::trim ).toArray( String[]::new );
			blogInfo.setTags( tags );
		}
		
		if( cmd.hasOption( "imageUrl" ) ) 
		{
			blogInfo.setImageUrl( cmd.getOptionValue( "imageUrl" ) );
		}

		return blogInfo;
	}
}
