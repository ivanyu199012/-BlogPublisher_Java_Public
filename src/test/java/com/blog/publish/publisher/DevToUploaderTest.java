package com.blog.publish.publisher;

import java.io.IOException;

import com.blog.publish.publisher.utils.DevToUploader;
import com.blog.publish.publisher.utils.FileHandler;

public class DevToUploaderTest
{
	public void test_formatMarkdownText() throws IOException
	{
		String markdown = FileHandler.readFile( "C:\\MyFiles\\Git\\3. Blog\\blog\\5. Django_background_task.md" );
		markdown = DevToUploader.formatMarkdownText( markdown );
	}
}
