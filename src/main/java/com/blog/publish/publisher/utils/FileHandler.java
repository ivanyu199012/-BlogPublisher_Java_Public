package com.blog.publish.publisher.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

public class FileHandler {
	
	public static String readFile( String path ) throws IOException {
		
		return FileUtils.readFileToString( new File( path ), StandardCharsets.UTF_8.name() );
	}
}
