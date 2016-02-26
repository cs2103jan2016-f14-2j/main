import java.util.HashMap;

public class Parser {
	
	/* ----------------------------|
	 * EXTENDED COMMANDS VARIABLES |
	 * ----------------------------|
	 * String[]: Stores the possible extended command strings for each command.
	 * int[]: Stores the index on InputStruct which each extended command string affects.
	 */
	private final String[] EXTENDED_COMMANDS_ADD = {"at", "from", "by", "to"};
	private final int[] EXTENDED_COMMANDS_ADD_INDEX = {2, 2, 3, 3};
	private final String[] EXTENDED_COMMANDS_EDIT = {"name", "desc", "from", "to", "cat"};
	private final int[] EXTENDED_COMMANDS_EDIT_INDEX = {1, 2, 3, 4, 5};
	
	/* ---------|
	 * HASHMAPS |
	 * ---------|
	 * Stores the extended command strings with the index for easy access.
	 */
	private HashMap<String, Integer> extendedCommandsAdd;
	private HashMap<String, Integer> extendedCommandsEdit;
	
	/* 
	 *  Stores the extended commands variables into the respective hashmaps.
	 */
	public Parser() {
		extendedCommandsAdd = new HashMap<String, Integer>();
		extendedCommandsEdit = new HashMap<String, Integer>();
		
		for (int i = 0; i < EXTENDED_COMMANDS_ADD.length; i++) {
			extendedCommandsAdd.put(EXTENDED_COMMANDS_ADD[i], EXTENDED_COMMANDS_ADD_INDEX[i]);
		}
		for (int i = 0; i < EXTENDED_COMMANDS_EDIT.length; i++) {
			extendedCommandsEdit.put(EXTENDED_COMMANDS_EDIT[i], EXTENDED_COMMANDS_EDIT_INDEX[i]);
		}
	}
	
	/*
	 * The main method that other components use.
	 * Returns an InputStruct containing the variables of the user input.
	 */
	public InputStruct parseInput(String userInput) {
		String[] splitUserInput = userInput.split(" ");
		switch (getCommandString(splitUserInput)) {
			case "add" :
				return getStruct(splitUserInput, extendedCommandsAdd);
			case "delete" :
				return null;
			case "edit" :
				return getStruct(splitUserInput, extendedCommandsEdit);
			default :
				return null;
		}
	}
	
	/*
	 * Detects and stores the variables in the user input.
	 */
	private InputStruct getStruct(String[] userInputStringArray, HashMap<String, Integer> inputExtendedCommandsHashMap) {
		
		// Creates the InputStruct to be returned.
		InputStruct outputAddStruct = new InputStruct(getCommandString(userInputStringArray));
		
		// currIndex is the index on the InputStruct that the strings currently being read affects.
		int currIndex = 0;
		
		// userInputString is the string currently being read. Updates while the next extended command is not found.
		String userInputString = "";
		
		for (int i = 1; i < userInputStringArray.length; i++) {
			
			//Updates userInputString if word being read is not an extended command.
			String currString = userInputStringArray[i];
			if (!inputExtendedCommandsHashMap.containsKey(currString)) {
				userInputString += currString + " ";
			} else {
				
				//When word being read is an extended command, stores the "userInputString" into the index in the InputStruct specified by "currIndex".
				outputAddStruct.currStringArray[currIndex] = userInputString.substring(0, userInputString.length()-1);
				
				//Updates the "currIndex" to the index related to the extended command.
				currIndex = inputExtendedCommandsHashMap.get(currString);
				
				//Resets the "userInputString".
				userInputString = "";
			}
		}
		outputAddStruct.currStringArray[currIndex] = userInputString.substring(0, userInputString.length()-1);
		return outputAddStruct;
	}
	
	public String getCommandString(String[] userInputStringArray) {
		return userInputStringArray[0];		
	}
	
}

/* ------------|
 * INPUTSTRUCT |
 * ------------|
 * This class is the output of the Parser. It contains the command string and all possible variables for all commands in the Jimple Planner.
 * Stores the variables detected in the user input.
 */
class InputStruct {
	
	public String commandString;
	
	// The string array being used, according to commandString.
	public String[] currStringArray;
	
	public InputStruct(String inputCommandString) {
		commandString = inputCommandString;
		
		// Inits the corresponding array according to the inputCommandString. Possible to use a single array, but it's neater this way.
		switch (commandString) {
			case "add" :
				addArray = new String[5];
				currStringArray = addArray;
				break;
			case "edit" :
				editArray = new String[6];
				currStringArray = editArray;
				break;
			case "delete" :
				deleteArray = new String[1];
				currStringArray = deleteArray;
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
	 */
	public String[] addArray;
	
	/* --------------|
	 * EDIT VARIABLES|
	 * --------------|
	 * Index 0: Event Index
	 * Index 1: Event Name
	 * Index 2: Event Description
	 * Index 3: Event Time (From)
	 * Index 4: Event Time (To)
	 * Index 5: Event Category
	 */
	public String[] editArray;
	
	/* ----------------|
	 * DELETE VARIABLE |
	 * ----------------|
	 * Index 0: Event Index
	 */
	public String[] deleteArray;
}
