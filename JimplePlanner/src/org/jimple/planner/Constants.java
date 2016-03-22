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
	public static final String TYPE_STATIC = "static";
	public static final String EMPTY_STRING = "";
	
	/**
	 * Logic component
	 */
	public static final String STRING_SEARCH = "search";
	public static final String STRING_ADD = "add";
	public static final String STRING_DELETE = "delete";
	public static final String STRING_EDIT = "edit";
	public static final String STRING_CHANGEDIR = "changedir";
	public static final String STRING_UNDO = "undo";
	
	public static final String ADD_HELP_HEADER = "Add a new task:\n";
	public static final String EDIT_HELP_HEADER = "Edit a current task:\n";
	public static final String DISPLAY_HELP_HEADER = "Display all tasks:\n";
	public static final String DELETE_HELP_HEADER = "Delete a task:\n";
	
	public static final String ADD_COMMAND_BY = "type \"add\" + <your event> by <time>\n";
	public static final String ADD_COMMAND_AT = "type \"add\" + <your event> at <time>\n";
	public static final String ADD_COMMAND_FROMTO = "type \"add\" + <your event> from <time> to <time>\n";
	public static final String EDIT_COMMAND_ONE_TIMING = "type \"edit\" + <your event> to <time>\n";
	public static final String EDIT_COMMAND_TWO_TIMINGS = "type \"edit\" + <your event> from <time> to <time>\n";
	public static final String DISPLAY_COMMAND = "type \"display\"";
	public static final String DELETE_COMMAND = "type \"delete\" <event name>";
	
	public static final String ERROR_WRONG_INPUT_FEEDBACK = "wrong input format";
	public static final String ERROR_WRONG_COMMAND_FEEDBACK = "unknown command";
	
	public static final String ERROR_ADDED_FEEDBACK = "task could not be added";
	public static final String ADDED_FEEDBACK = "task added to planner";
	
	public static final String UNDO_FEEDBACK = "task undone";
	public static final String UNDO_FEEDBACK_ERROR = "no task to undo";
	public static final int DELETE_CACHE_LIMIT = 20;
	
	public static final String EDITED_FEEDBACK = "task edited in planner";
	public static final String ERROR_EDIT_FEEDBACK = "task could not be editted";
	
	public static final String ERROR_DIRECTORY_PATH_FEEDBACK = "invalid directory path";
	public static final String DIRECTORY_PATH_CHANGED_FEEDBACK = "save directory path changed";
	
	public static final String DELETED_FEEDBACK = "task deleted";
	public static final String ERROR_DELETED_FEEDBACK = "could not find task to be deleted";
	
	public static String WINDOW_CLOSED_FEEDBACK = "search window closed";
	
	private Constants(){
		throw new AssertionError();
	}
}
