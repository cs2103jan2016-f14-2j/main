package org.jimple.planner;

public class LogicDirectory {
	private String ERROR_DIRECTORY_PATH_FEEDBACK = "invalid directory path";
	private String DIRECTORY_PATH_CHANGED_FEEDBACK = "save directory path changed";
	
	
	protected String changeSaveDirectory(Storage store, String[] variableArray) {
		if (!isValidPath(store, variableArray)) {
			return ERROR_DIRECTORY_PATH_FEEDBACK;
		}
		return DIRECTORY_PATH_CHANGED_FEEDBACK;
	}

	private boolean isValidPath(Storage store, String[] variableArray) {
		if (store.setPath(variableArray[0])) {
			return true;
		}
		return false;
	}

}
