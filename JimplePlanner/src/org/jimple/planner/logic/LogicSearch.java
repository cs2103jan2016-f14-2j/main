package org.jimple.planner.logic;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Storage;
import org.jimple.planner.Task;

public class LogicSearch implements LogicTaskModification{
	private static final String TYPE_DEADLINE = "deadline";
	private static final String TYPE_EVENT = "event";
	private static final String TYPE_TODO = "floating";
	private String WINDOW_CLOSED_FEEDBACK = "search window closed";

	
	protected ArrayList<Task> searchWord(String wordToBeSearched,  ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) {
		ArrayList<Task> searchWordResults = new ArrayList<Task>();
		if (todo.isEmpty() && deadlines.isEmpty() && events.isEmpty()) {
		} else {
			searchWordResults.addAll(searchFromOneTaskList(wordToBeSearched, todo));
			searchWordResults.addAll(searchFromOneTaskList(wordToBeSearched, deadlines));
			searchWordResults.addAll(searchFromOneTaskList(wordToBeSearched, events));
		}
		return searchWordResults;
	}
	
	private ArrayList<Task> searchFromOneTaskList(String wordToBeSearched, ArrayList<Task> list) {
		ArrayList<Task> searchWordResults;
		searchWordResults = getSearchedTasks(wordToBeSearched, list);
		if (searchWordResults.isEmpty()) {
			// searchWordResults.add(SEARCH_WORD_NOT_FOUND_FEEDBACK);
		}
		return searchWordResults;
	}
	
	private ArrayList<Task> getSearchedTasks(String wordToBeSearched, ArrayList<Task> list) {
		ArrayList<Task> objectOfTaskInstanceFound = new ArrayList<Task>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Task currentTask = list.get(i);
				if (isContainKeyword(currentTask, wordToBeSearched)) {
					objectOfTaskInstanceFound.add(list.get(i));
					i--;
				}
			}
		}
		return objectOfTaskInstanceFound;
	}
	
	private boolean isContainSubstring(String sourceString, String substring) {
		int substringLength = substring.length();
		if (substringLength == 0) {
			return true;
		}
		char subStringFirstLowerCaseChar = Character.toLowerCase(substring.charAt(0));
		char subStringFirstUpperCaseChar = Character.toUpperCase(substring.charAt(0));
		for (int i = sourceString.length() - substringLength; i >= 0; i--) {
			char sourceCharacterAt = sourceString.charAt(i);
			if (sourceCharacterAt != subStringFirstLowerCaseChar && sourceCharacterAt != subStringFirstUpperCaseChar) {
				continue;
			}
			if (sourceString.regionMatches(true, i, substring, 0, substringLength)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isContainKeyword(Task event, String keyword) {
		boolean isTitleSearched = isContainSubstring(event.getTitle(), keyword);
		boolean isDescSearched = isContainSubstring(event.getDescription(), keyword);
		boolean isCategorySearched = isContainSubstring(event.getCategory(), keyword);
		return (isTitleSearched || isDescSearched || isCategorySearched);
	}
	
	public String reInsertNewTasks(Storage store, ArrayList<Task> newList, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		for (Task aTask : newList) {
			if (aTask.getType().equals(TYPE_EVENT)) {
				events.add(aTask);
			} else if (aTask.getType().equals(TYPE_TODO)) {
				todo.add(aTask);
			} else if (aTask.getType().equals(TYPE_DEADLINE)) {
				deadlines.add(aTask);
			}
		}
		packageForSavingInFile(store, todo, deadlines, events);
		return WINDOW_CLOSED_FEEDBACK;
	}

	public boolean testIsContainKeyword(Task event, String keyword) {
		return isContainKeyword(event, keyword);
	}
	
	public ArrayList<Task> testSearchWord(String wordToBeSearched, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) {
		return searchWord(wordToBeSearched, todo, deadlines, events);
	}

}
