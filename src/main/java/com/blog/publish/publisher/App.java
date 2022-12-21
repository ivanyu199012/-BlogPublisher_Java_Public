package com.blog.publish.publisher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blog.publish.publisher.BlogInfo.SITE;
import com.blog.publish.publisher.utils.DevToUploader;
import com.blog.publish.publisher.utils.FileHandler;
import com.blog.publish.publisher.utils.GistCodeHandler;
import com.blog.publish.publisher.utils.MediumUploader;
import com.blog.publish.publisher.utils.Utils;

/**
 * Hello world!
 *
 */
public class App
{
	public static final String ID_TO_GIST_LINK_MAP_PATH = "temp\\idToGistLinkMap_mediumUploadArticle.txt";
	private static Logger logger = LogManager.getLogger( App.class );
	private static interface UploadArticle
	{
		public void exec( BlogInfo blogInfo, String markdown ) throws IOException, InterruptedException;
	}

	private static UploadArticle devToUploadArticle = new UploadArticle() {

		@Override
		public void exec( BlogInfo blogInfo, String markdown ) throws IOException, InterruptedException
		{
			String formattedmarkdown = DevToUploader.formatMarkdownText( markdown );
			Map<String, Object> articleMap = DevToUploader.prepareArticleMap( blogInfo, formattedmarkdown );
			DevToUploader.postArticle( articleMap );
		}
	};

	private static UploadArticle mediumUploadArticle = new UploadArticle() {

		@Override
		public void exec( BlogInfo blogInfo, String markdown ) throws IOException, InterruptedException
		{
			String filename = Paths.get( blogInfo.getFilepath() ).getFileName().toString();
			Map< String, Object > resultMap = GistCodeHandler.convertBlogCodeToGist( filename, markdown );
			String markdownText = ( String ) resultMap.get( GistCodeHandler.TEMP_MARKDOWN_KEY );
			Map< String, String > idToGistLinkMap = ( Map< String, String > ) resultMap.get( GistCodeHandler.ID_TO_GIST_LINK_MAP_KEY );

			String formattedMarkdownText = MediumUploader.formatMarkdownText(
					markdownText,
					idToGistLinkMap,
					blogInfo );
			Map<String, Object> reqDataMap = MediumUploader.prepReqDataMap( blogInfo, formattedMarkdownText );
			MediumUploader.postArticle( reqDataMap );

			FileHandler.writeMapToFile( idToGistLinkMap, ID_TO_GIST_LINK_MAP_PATH );
		}
	};

	private static Map< SITE, UploadArticle > siteToUploadArticleMap = Map.ofEntries(
		Map.entry( SITE.DEVTO, devToUploadArticle ),
		Map.entry( SITE.MEDIUM, mediumUploadArticle)
	);


	public static void main( String[] args ) throws ParseException, IOException, InterruptedException
	{
		CommandLine cmd = App.getCommandLineBy( args );
		BlogInfo blogInfo = App.getBlogInfoFrom( cmd );
		String markdown = FileHandler.readFile( blogInfo.getFilepath() );

		if( ! Utils.isNullOrEmpty( blogInfo.getSites() ) )
		{
			for( SITE site : blogInfo.getSites() )
			{
				try
				{
					logger.info( "Started Uploading to " + site.name() );
					siteToUploadArticleMap.get( site ).exec( blogInfo, markdown );
					logger.info( "Finished Uploading to " + site.name() );
				}
				catch (Exception e)
				{
					logger.error( "Upload article to the site " + site.toString(), e );
				}
			}
		}
	}

	public static CommandLine getCommandLineBy( String[] args ) throws ParseException
	{
		Options options = new Options();
		Option title = Option.builder( "t" ).longOpt( "title" ).hasArg().required( true ).desc( "title of post" )
				.build();
		options.addOption( title );

		Option canonicalUrl = Option.builder( "cUrl" ).longOpt( "canonicalUrl" ).hasArg().required( false )
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

		if( cmd.hasOption( "subtitle" ) )
		{
			blogInfo.setSubtitle( cmd.getOptionValue( "subtitle" ) );
		}

		return blogInfo;
	}
}
