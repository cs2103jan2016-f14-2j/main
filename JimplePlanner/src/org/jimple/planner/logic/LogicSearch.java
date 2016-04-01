package org.jimple.planner.logic;

import java.util.ArrayList;

import org.jimple.planner.Task;

public class LogicSearch {
	protected static String mostRecentlySearchedWord = new String("");
	
	protected ArrayList<Task> searchWord(String wordToBeSearched,  ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) {
		ArrayList<Task> searchWordResults = new ArrayList<Task>();
		searchWordResults.clear();
		mostRecentlySearchedWord = wordToBeSearched;
		
		if (todo.isEmpty() && deadlines.isEmpty() && events.isEmpty()) {
		} else {
			searchWordResults.addAll(getSearchedTasks(wordToBeSearched, todo));
			searchWordResults.addAll(getSearchedTasks(wordToBeSearched, deadlines));
			searchWordResults.addAll(getSearchedTasks(wordToBeSearched, events));
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
		boolean isLabelSearched = isContainSubstring(event.getTaskLabel().getLabelName(), keyword);
		return (isTitleSearched || isDescSearched || isLabelSearched);
	}

	public boolean testIsContainKeyword(Task event, String keyword) {
		return isContainKeyword(event, keyword);
	}
	
	public ArrayList<Task> testSearchWord(String wordToBeSearched, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events) {
		return searchWord(wordToBeSearched, todo, deadlines, events);
	}
	
	public ArrayList<Task> testgetSearchedTasks (String wordToBeSearched, ArrayList<Task> list)	{
		return getSearchedTasks(wordToBeSearched, list);
	}
}
