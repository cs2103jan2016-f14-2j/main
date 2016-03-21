package org.jimple.planner;

import java.io.File;

public final class Constants {
	public static final int ALL_ARRAY_SIZE = 3;
	
	public static final String DEFAULT_FILE_DIRECTORY = "jimpleFiles"+File.separator;
	public static final String DEFAULT_FILE_NAME = "planner.jim";
	public static final String DEFAULT_TEMP_FILE_NAME = "templanner.jim";
	public static final String TEST_FILE_NAME = "testplanner.jim";
	public static final String TEST_TEMP_FILE_NAME = "testtempplanner.jim";
	
	public static final String PROPERTIES_CONFIG_FILE_NAME = "jimpleConfig.properties";
	public static final String PROPERTIES_SAVEPATH_KEY_NAME = "savepath";
	public static final String PROPERTIES_SAVEPATH_PREVIOUS_KEY_NAME = "prevsavepath";
	public static final String PROPERTIES_COMMENT_HEADER = "PATH SETTINGS";
	public static final String PROPERTIES_SAVEPATH_TO_CWD = "origin";
	
	public static final String FILEPATH_DEFAULT = DEFAULT_FILE_DIRECTORY + DEFAULT_FILE_NAME;
	public static final String FILEPATH_DEFAULT_TEMP = DEFAULT_FILE_DIRECTORY + DEFAULT_TEMP_FILE_NAME;
	public static final String FILEPATH_TEST = DEFAULT_FILE_DIRECTORY + TEST_FILE_NAME;
	public static final String FILEPATH_TEST_TEMP = DEFAULT_FILE_DIRECTORY + TEST_TEMP_FILE_NAME;
	public static final String FILEPATH_CONFIG = DEFAULT_FILE_DIRECTORY+PROPERTIES_CONFIG_FILE_NAME;
	
	public static final String TAGS_CATEGORY = ":cat:";
	public static final String TAGS_DESCRIPTION = ":desc:";
	public static final String TAGS_FROM_TIME = ":from:";
	public static final String TAGS_TO_TIME = ":to:";
	public static final String TAGS_TITLE = ":title:";
	public static final String TAGS_LINE_FIELD_SEPARATOR = "/";
	
	public static final String TYPE_EVENT = "event";
	public static final String TYPE_TODO = "floating";
	public static final String TYPE_DEADLINE = "deadline";
	public static final String EMPTY_STRING = "";
	
	private Constants(){
		throw new AssertionError();
	}
}
