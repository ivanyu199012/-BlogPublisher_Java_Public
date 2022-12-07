package com.blog.publish.publisher.utils;

import java.util.List;

public class Utils
{
	public static boolean isNullOrEmpty( List<Object> l )
	{
		return l == null || l.size() == 0;
	}

	public static boolean isNullOrEmpty( Object[] arr )
	{
		return arr == null || arr.length == 0;
	}
}
