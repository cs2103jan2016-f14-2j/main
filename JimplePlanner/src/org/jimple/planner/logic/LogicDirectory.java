package org.jimple.planner.logic;

import org.jimple.planner.storage.*;
import org.jimple.planner.Constants;

public class LogicDirectory {
	
	protected String changeSaveDirectory(Storage store, String[] variableArray) {
		if (!isValidPath(store, variableArray)) {
			return Constants.ERROR_DIRECTORY_PATH_FEEDBACK;
		}
		return Constants.DIRECTORY_PATH_CHANGED_FEEDBACK + "\"" + variableArray[0] +  "\"";
	}

	private boolean isValidPath(Storage store, String[] variableArray) {
		if (store.setPath(variableArray[0])) {
			return true;
		}
		return false;
	}

}
