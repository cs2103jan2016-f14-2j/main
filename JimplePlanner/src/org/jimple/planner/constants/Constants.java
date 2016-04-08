package org.jimple.planner.constants;

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
	public static final String TYPE_TODAY = "today";
	public static final String TYPE_NOW = "now";
	public static final String TYPE_UPCOMING = "upcoming";
	public static final String TYPE_ARCHIVE = "archive";
	public static final String EMPTY_STRING = "";
	public static final String NEWLINE = "\n";
	
	/*
	 * Task Label Class
	 */
	public static final String STRING_LABELNAME = "LABEL";
	public static final String STRING_LABELCOLOUR = "COLOUR";
	public static final int TASK_LABEL_COLOUR_DEFAULT_0 = 0;
	public static final int TASK_LABEL_COLOUR_BLUE_1 = 1;
	public static final int TASK_LABEL_COLOUR_GREEN_2 = 2;
	public static final int TASK_LABEL_COLOUR_YELLOW_3 = 3;
	public static final int TASK_LABEL_COLOUR_ORANGE_4 = 4;
	public static final int TASK_LABEL_COLOUR_RED_5 = 5;
	public static final int TASK_LABEL_COLOUR_PURPLE_6 = 6;
	public static final String TASK_LABEL_NAME_DEFAULT = "default";
	
	public static final String ERROR_INVALID_TASK = "Task is invalid";
	
	/*
	 * Storage Component
	 */
	
	public static final String DEFAULT_FILE_DIRECTORY = "jimpleFiles"+File.separator;
	public static final String DEFAULT_FILE_NAME = "planner.jim";
	public static final String DEFAULT_TEMP_FILE_NAME = "templanner.jim";
	public static final String TEST_FILE_NAME = "testplanner.jim";
	public static final String TEST_TEMP_FILE_NAME = "testtempplanner.jim";
	
	public static final String PROPERTIES_CONFIG_FILE_NAME = "jimpleConfig.properties";
	public static final String PROPERTIES_COMMENT_HEADER = "PATH SETTINGS";
	public static final String PROPERTIES_SAVEPATH_TO_CWD = "origin";
	
	public static final String PROPERTIES_KEY_CURRENT_SAVEPATH = "savepath";
	public static final String PROPERTIES_KEY_PREV_SAVEPATH = "prevsavepath";
	
	public static final String PROPERTIES_VALUE_COLOUR_BLUE_1 = "blue";
	public static final String PROPERTIES_VALUE_COLOUR_GREEN_2 = "green";
	public static final String PROPERTIES_VALUE_COLOUR_YELLOW_3 = "yellow";
	public static final String PROPERTIES_VALUE_COLOUR_ORANGE_4 = "orange";
	public static final String PROPERTIES_VALUE_COLOUR_RED_5 = "red";
	public static final String PROPERTIES_VALUE_COLOUR_PURPLE_6 = "purple";
	
	public static final String FILEPATH_DEFAULT = DEFAULT_FILE_DIRECTORY + DEFAULT_FILE_NAME;
	public static final String FILEPATH_DEFAULT_TEMP = DEFAULT_FILE_DIRECTORY + DEFAULT_TEMP_FILE_NAME;
	public static final String FILEPATH_TEST = DEFAULT_FILE_DIRECTORY + TEST_FILE_NAME;
	public static final String FILEPATH_TEST_TEMP = DEFAULT_FILE_DIRECTORY + TEST_TEMP_FILE_NAME;
	public static final String FILEPATH_CONFIG = DEFAULT_FILE_DIRECTORY+PROPERTIES_CONFIG_FILE_NAME;
	
	public static final String TAGS_LABEL = ":label:";
	public static final String TAGS_DESCRIPTION = ":desc:";
	public static final String TAGS_FROM_TIME = ":from:";
	public static final String TAGS_TO_TIME = ":to:";
	public static final String TAGS_TITLE = ":title:";
	public static final String TAGS_ISDONE = ":isDone:";
	public static final String TAGS_LINE_FIELD_SEPARATOR = "/s/";
	public static final String TAGS_LABEL_FIELD_SEPARATOR = "/tl/";
	
	//@@author A0135775W
	/* -----------------|
	 * Parser Component |
	 * -----------------|
	 */
	// Extended commands for all the main commands.
	public static final String STRING_NAME = "NAME";
	public static final String STRING_DESC = "DESC";
	public static final String STRING_AT = "AT";
	public static final String STRING_FROM = "FROM";
	public static final String STRING_TO = "TO";
	public static final String STRING_ON = "ON";
	public static final String STRING_BY = "BY";
	public static final String STRING_LABEL = "LABEL";
	public static final String STRING_TIME = "TIME";
	public static final String STRING_COLOUR = "COLOUR";
	
	// Supported colours for labels.
	public static final String STRING_RED = "red";
	public static final String STRING_ORANGE = "orange";
	public static final String STRING_YELLOW = "yellow";
	public static final String STRING_GREEN = "green";
	public static final String STRING_BLUE = "blue";
	public static final String STRING_PURPLE = "purple";
	
	/* Stores the index for the user input directly after the main command.
	 *  - "ADD": Index of the task name.
	 *  - "EDIT": Index of the task ID.
	 *  - "DELETE": Index of the task ID.
	 *  - "SEARCH": Index of the query string.
	 *  - "CHANGEDIR": Index of the directory path.
	 *  - "EDITLABEL": Index of the label name.
	 */
	public static final int INDEX_BASE = 0;
	
	// Stores the indexes for task fields. Used by "ADD" and "EDIT".
	public static final int INDEX_NAME = 1;
	public static final int INDEX_DESCRIPTION = 2;
	public static final int INDEX_FROM = 3;
	public static final int INDEX_TO = 4;
	public static final int INDEX_LABEL = 5;
	
	// Stores the indexes for task fields. Used by "EDITLABEL".
	public static final int INDEX_EDITLABEL_NAME = 1;
	public static final int INDEX_EDITLABEL_COLOUR = 2;

	// Stores the task type strings for "ADD".
	public static final String TASK_TYPE_TODO = "todo";
	public static final String TASK_TYPE_DEADLINE = "deadline";
	public static final String TASK_TYPE_EVENT = "event";
	
	/**
	 * Logic component
	 */
	//@@author A0124952E
	public static final String STRING_SEARCH = "SEARCH";
	public static final String STRING_ADD = "ADD";
	public static final String STRING_DELETE = "DELETE";
	public static final String STRING_EDIT = "EDIT";
	public static final String STRING_CHANGEDIR = "CHANGEDIR";
	public static final String STRING_UNDOTASK = "UNDOTASK";
	public static final String STRING_HELP = "HELP";
	public static final String STRING_CHECKDIR = "CHECKDIR";
	public static final String STRING_EDITLABEL = "EDITLABEL";
	public static final String STRING_DELETELABEL = "DELETELABEL";
	public static final String STRING_DONE = "DONE";
	public static final String STRING_RETURN = "RETURN";
	public static final String STRING_CHECKCONFLICT = "CHECKCONFLICT";
	
	public static final String TIME_FORMAT_HELP_HEADER = "Time Format:\n";
	public static final String ADD_HELP_HEADER = "Add a new task:\n";
	public static final String EDIT_HELP_HEADER = "Edit a current task:\n";
	public static final String SEARCH_HELP_HEADER = "Search tasks:\n";
	public static final String DELETE_HELP_HEADER = "Delete a task:\n";
	public static final String UNDOTASK_HELP_HEADER = "Undo a task:\n";
	public static final String CHANGEDIR_HELP_HEADER = "Change Jimple File Directory:\n";
	public static final String CHECKDIR_HELP_HEADER = "Check Jimple File Directory:\n";
	public static final String EDITLABEL_HELP_HEADER = "Edit a label:\n";
	public static final String DELETELABEL_HELP_HEADER = "Delete a label:\n";
	public static final String ARCHIVE_HELP_HEADER = "Archive completed tasks\n";
	public static final String UNARCHIVE_HELP_HEADER = "Return completed tasks\n";
	
	public static final String TIME_FORMAT = "<date in DAY MONTH> / <time in day of week> / <time in AM/PM> / <time in 24HR>\n\n";
	
	public static final String ADD_COMMAND_BY = "type \"ADD\" <your event name> BY <time>\n";
	public static final String ADD_COMMAND_AT = "type \"ADD\" <your event name> AT <time>\n";
	public static final String ADD_COMMAND_ON = "type \"ADD\" <your event name> ON <time>\n";
	public static final String ADD_COMMAND_FROMTO = "type \"ADD\" <your event> FROM <time> TO <time>\n\n";
	
	public static final String EDIT_COMMAND_NAME = "type \"EDIT\" <taskID> \"NAME\" <your event title>\n";
	public static final String EDIT_COMMAND_BY_TIME = "type \"EDIT\" <taskID> \"TIME\" BY <date/time>\n";
	public static final String EDIT_COMMAND_FROMTO_TIME = "type \"EDIT\" <taskID> \"TIME\" FROM <date/time> TO <date/time>\n";
	public static final String EDIT_COMMAND_LABEL = "type \"EDIT\" <taskID> \"LABEL\" <new name/\"default\" to remove label>\n\n";
	
	public static final String DELETE_COMMAND = "type \"DELETE\" <taskID>\n\n";
	
	public static final String SEARCH_COMMAND = "type \"SEARCH\" <task name>\n\n";
	
	public static final String UNDOTASK_COMMAND = "type \"UNDOTASK\"\n\n";
	
	public static final String CHANGEDIR_COMMAND = "type \"CHANGEDIR\" <full path directory>\n\n";
	public static final String CHECKDIR_COMMAND = "type \"CHECKDIR\"\n\n";
	
	public static final String EDITLABEL_COMMAND = "type \"EDITLABEL\" <old label name/colour> NAME <new label name> COLOUR <new label colour>\n\n";
	public static final String DELETELABEL_COMMAND = "type \"DELETELABEL\" <label name>\n\n";
	
	public static final String ARCHIVE_COMMAND = "type \"DONE\" <taskID>\n\n";
	public static final String UNARCHIVE_COMMAND = "type \"RETURN\" <taskID>\n";
	
	public static final String ERROR_WRONG_INPUT_FEEDBACK = "wrong input format";
	public static final String ERROR_WRONG_COMMAND_FEEDBACK = "unknown command";
	
	public static final String ERROR_WRONG_TIME_FEEDBACK = "please input a valid \"from\" and \"to\" time";
	public static final String ADDED_FEEDBACK = " added to planner";
	
	public static final String UNDO_FEEDBACK = " undone";
	public static final String UNDO_FEEDBACK_ERROR = "no task to undo";
	
	public static final String EDITED_FEEDBACK = " edited in planner";
	public static final String ERROR_EDIT_FEEDBACK = " could not be edited";
	
	public static final String ERROR_DIRECTORY_PATH_FEEDBACK = "invalid directory path";
	public static final String DIRECTORY_PATH_CHANGED_FEEDBACK = "save directory path changed to ";
	
	public static final String DELETED_FEEDBACK = " deleted";
	public static final String ERROR_DELETED_FEEDBACK = " could not be found";
	
	public static final String LABEL_FEEDBACK = " label changed";
	public static final String ERROR_LABEL_FEEDBACK = "label could not be changed";
	
	public static final String LABEL_DELETED_FEEDBACK = "label is deleted";
	public static final String ERROR_LABEL_DELETED_FEEDBACK = "label could not be deleted";
	
	public static final String DONE_FEEDBACK = " is now archived";
	public static final String ERROR_DONE_FEEDBACK = " does not exist and could not be archived";
	
	public static final String UNDONE_FEEDBACK = " has returned to your list";
	public static final String ERROR_UNDONE_FEEDBACK = " could not be returned to your list";
	
	public static final String ERROR_NO_CONFLICT_FOUND = "no conflicts found";

	public static final int MAX_ID = 10000;
	public static final int DELETE_CACHE_LIMIT = 20;
	
	public static String WINDOW_CLOSED_FEEDBACK = "search window closed";
	
	private Constants(){
		throw new AssertionError();
	}
}
