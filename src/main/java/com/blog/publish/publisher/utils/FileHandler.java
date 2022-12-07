package com.blog.publish.publisher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileHandler
{

	public static String readFile( String path ) throws IOException
	{

		return FileUtils.readFileToString( new File( path ), StandardCharsets.UTF_8.name() );
	}

	public static String writeObjectToFile( Serializable object, String path ) throws IOException
	{
		FileOutputStream f = new FileOutputStream( path );
		ObjectOutputStream o = new ObjectOutputStream( f );

		o.writeObject( object );

		o.close();
		f.close();

		return path;
	}

	public static String writeMapToFile( Map<String, String> map, String path ) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		FileUtils.writeStringToFile( new File( path ), jsonResult, StandardCharsets.UTF_8 );

		return path;
	}

	public static Map<String, String> readFileToMap( String path ) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = FileUtils.readFileToString( new File( path ), StandardCharsets.UTF_8 );

		return ( Map<String, String> ) mapper.readValue( jsonStr, Map.class );
	}

	public static Object readFileToObject( String path ) throws IOException, ClassNotFoundException
	{
		FileInputStream fi = new FileInputStream(new File( path ));
		ObjectInputStream oi = new ObjectInputStream(fi);

		Object object = oi.readObject();

		oi.close();
		fi.close();

		return object;
	}
}
