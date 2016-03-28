package org.jimple.planner;

import java.io.File;

public final class Constants {
	public static final String TYPE_EVENT = "event";
	public static final String TYPE_TODO = "floating";
	public static final String TYPE_DEADLINE = "deadline";
	public static final String TYPE_AGENDA = "agenda";
	public static final String TYPE_STATIC = "static";
	public static final String TYPE_SEARCH = "search";
	public static final String TYPE_HELP = "help";
	public static final String TYPE_POPUP = "popup";
	public static final String EMPTY_STRING = "";
	
	public static final int TASK_LABEL_DEFAULT = 0;
	public static final int TASK_LABEL_BLUE= 1;
	public static final int TASK_LABEL_GREEN = 2;
	public static final int TASK_LABEL_YELLOW = 3;
	public static final int TASK_LABEL_ORANGE = 4;
	public static final int TASK_LABEL_RED = 5;
	public static final int TASK_LABEL_DARK_RED = 6;
	
	public static final String ERROR_INVALID_TASK = "Task is invalid";
	
	/*
	 * Storage Component
	 */
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
	
	/**
	 * Logic component
	 */
	public static final String STRING_SEARCH = "search";
	public static final String STRING_ADD = "add";
	public static final String STRING_DELETE = "delete";
	public static final String STRING_EDIT = "edit";
	public static final String STRING_CHANGEDIR = "changedir";
	public static final String STRING_UNDO = "undo";
	public static final String STRING_HELP = "help";
	public static final String STRING_CHECKDIR = "checkdir";
	
	public static final String TIME_FORMAT_HELP_HEADER = "Time Format:\n";
	public static final String ADD_HELP_HEADER = "Add a new task:\n";
	public static final String EDIT_HELP_HEADER = "Edit a current task:\n";
	public static final String SEARCH_HELP_HEADER = "Search tasks:\n";
	public static final String DELETE_HELP_HEADER = "Delete a task:\n";
	public static final String UNDO_HELP_HEADER = "Undo a task:\n";
	public static final String CHANGEDIR_HELP_HEADER = "Change Jimple File Directory:\n";
	public static final String CHECKDIR_HELP_HEADER = "Check Jimple File Directory:\n";
	
	public static final String TIME_FORMAT = "<date in DAY MONTH> / <time in day of week> / <time in AM/PM> / <time in 24HR>\n\n";
	public static final String ADD_COMMAND_BY = "type \"add\" <your event name> by <time>\n";
	public static final String ADD_COMMAND_AT = "type \"add\" <your event name> at <time>\n";
	public static final String ADD_COMMAND_ON = "type \"add\" <your event name> on <time>\n";
	public static final String ADD_COMMAND_FROMTO = "type \"add\" <your event> from <time> to <time>\n\n";
	public static final String EDIT_COMMAND_ONE_TIMING = "type \"edit\" <taskID> \"name\" <your event title> \"time\" by <date/time>\n";
	public static final String EDIT_COMMAND_TWO_TIMINGS = "type \"edit\" <taskID> \"name\" <your event title> \"time\" from <date/time> to <date/time>\n\n";
	public static final String DELETE_COMMAND = "type \"delete\" <taskID>\n\n";
	public static final String SEARCH_COMMAND = "type \"search\" <task name>\n\n";
	public static final String UNDO_COMMAND = "type \"undo\"\n\n";
	public static final String CHANGEDIR_COMMAND = "type \"changedir\" <full path directory>\n\n";
	public static final String CHECKDIR_COMMAND = "type \"checkdir\"\n";
	
	public static final String ERROR_WRONG_INPUT_FEEDBACK = "wrong input format";
	public static final String ERROR_WRONG_COMMAND_FEEDBACK = "unknown command";
	
	public static final String ERROR_WRONG_TIME_FEEDBACK = "please input a valid \"from\" and \"to\" time";
	public static final String ADDED_FEEDBACK = " added to planner";
	
	public static final String UNDO_FEEDBACK = " undone";
	public static final String UNDO_FEEDBACK_ERROR = "no task to undo";
	public static final int DELETE_CACHE_LIMIT = 20;
	
	public static final String EDITED_FEEDBACK = " edited in planner";
	public static final String ERROR_EDIT_FEEDBACK = " could not be edited";
	
	public static final String ERROR_DIRECTORY_PATH_FEEDBACK = "invalid directory path";
	public static final String DIRECTORY_PATH_CHANGED_FEEDBACK = "save directory path changed to ";
	
	public static final String DELETED_FEEDBACK = " deleted";
	public static final String ERROR_DELETED_FEEDBACK = " could not be found";
	
	public static String WINDOW_CLOSED_FEEDBACK = "search window closed";
	
	private Constants(){
		throw new AssertionError();
	}
}
