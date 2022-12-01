package com.blog.publish.publisher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

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
