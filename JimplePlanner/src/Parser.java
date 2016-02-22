import java.util.HashMap;

public class Parser {
	
	private static HashMap<String, Integer> extendedCommandsAdd;
	private final static String[] AVAILABLE_EXTENDED_COMMANDS_ARRAY = {"at", "by", "from", "to"};
	private final static int[] AVAILABLE_EXTENDED_COMMANDS_INDEX_ARRAY = {2, 3, 2, 3};
	
	public Parser() {
		extendedCommandsAdd = new HashMap<String, Integer>();
		for (int i = 0; i < AVAILABLE_EXTENDED_COMMANDS_ARRAY.length; i++) {
			extendedCommandsAdd.put(AVAILABLE_EXTENDED_COMMANDS_ARRAY[i], AVAILABLE_EXTENDED_COMMANDS_INDEX_ARRAY[i]);
		}
	}
	
	public static InputStruct parseInput(String userInput) {
		String[] splitUserInput = userInput.split(" ");
		switch (getCommandString(splitUserInput)) {
			case "add" :
				return getAddStruct(splitUserInput);
			case "delete" :
				return null;
			case "edit" :
				return null;
			default :
				return null;
		}
	}
	
	private static InputStruct getAddStruct(String[] userInputStringArray) {
		InputStruct outputAddStruct = new InputStruct(userInputStringArray[0]);
		int currIndex = 0;
		String userInputString = "";
		for (int i = 1; i < userInputStringArray.length; i++) {
			String currString = userInputStringArray[i];
			if (!extendedCommandsAdd.containsKey(currString)) {
				userInputString += currString + " ";
			} else {
				outputAddStruct.addArray[currIndex] = userInputString.substring(0, userInputString.length()-1);
				currIndex = extendedCommandsAdd.get(currString);
				userInputString = "";
			}
		}
		outputAddStruct.addArray[currIndex] = userInputString.substring(0, userInputString.length()-1);
		return outputAddStruct;
	}
	
	public static String getCommandString(String[] userInputStringArray) {
		return userInputStringArray[0];		
	}
	
}

class InputStruct {
	
	public String commandString;
	
	public InputStruct(String inputCommandString) {
		commandString = inputCommandString;
		switch (commandString) {
			case "add" :
				addArray = new String[5];
				break;
			case "edit" :
				editArray = new String[6];
				break;
			default :
				break;
		}
	}
	
	/* --------------|
	 * ADD VARIABLES |
	 * --------------|
	 * Index 0: Event Name
	 * Index 1: Event Description
	 * Index 2: Event Time (From)
	 * Index 3: Event Time (To)
	 * Index 4: Event Category
	 * --------------------------- */
	public String[] addArray;
	
	/* --------------|
	 * EDIT VARIABLES|
	 * --------------|
	 * Index 0
	 * Index 1: Event Name
	 * Index 2: Event Description
	 * Index 3: Event Time (From)
	 * Index 4: Event Time (To)
	 * Index 5: Event Category
	 * --------------------------- */
	public String[] editArray;
}