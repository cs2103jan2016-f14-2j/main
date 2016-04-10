package org.jimple.planner.logic;

import java.util.ArrayList;

import org.jimple.planner.task.Task;

//@@author A0124952E
public class LogicSearch {
	protected static String mostRecentlySearchedWord = new String("");
	
	//@@author A0135808B
	protected ArrayList<Task> searchWord(String wordToBeSearched, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks) {
		ArrayList<Task> searchWordResults = new ArrayList<Task>();
		searchWordResults.clear();
		mostRecentlySearchedWord = wordToBeSearched;

		if (todo.isEmpty() && deadlines.isEmpty() && events.isEmpty()) {
		} else {
			searchWordResults.addAll(getSearchedTasks(wordToBeSearched, todo));
			searchWordResults.addAll(getSearchedTasks(wordToBeSearched, deadlines));
			searchWordResults.addAll(getSearchedTasks(wordToBeSearched, events));
			searchWordResults.addAll(getSearchedTasks(wordToBeSearched, archivedTasks));
		}
		return searchWordResults;
	}

	//@@author A0135808B
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
	
	//@@author A0135808B-unused
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

	//@@author A0124952E
	private boolean isContainSearchedWord(String sourceString, String searchedPhrase) {
		String[] dividedSearchWords = searchedPhrase.split(" ");
		String[] dividedSourceWords = sourceString.split(" ");
		
		if (dividedSearchWords.length == 0) {
			return false;
		}
		for (int i = 0; i < dividedSearchWords.length; i++) {
			for (int j=0; j< dividedSourceWords.length; j++)	{
				if (dividedSourceWords[j].toLowerCase().equals(dividedSearchWords[i].toLowerCase()))	{
					return true;
				}
			}
		}
		return false;
	}

	//@@author A0135808B
	private boolean isContainKeyword(Task event, String keyword) {
		boolean isTitleSearched = isContainSearchedWord(event.getTitle(), keyword);
		boolean isDescSearched = isContainSearchedWord(event.getDescription(), keyword);
		boolean isLabelSearched = isContainSearchedWord(event.getTaskLabel().getLabelName(), keyword);
		return (isTitleSearched || isDescSearched || isLabelSearched);
	}

	//@@author A0124952E
	public boolean testIsContainKeyword(Task event, String keyword) {
		return isContainKeyword(event, keyword);
	}

	//@@author A0124952E
	public ArrayList<Task> testSearchWord(String wordToBeSearched, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks) {
		return searchWord(wordToBeSearched, todo, deadlines, events, archivedTasks);
	}

	//@@author A0124952E
	public ArrayList<Task> testgetSearchedTasks(String wordToBeSearched, ArrayList<Task> list) {
		return getSearchedTasks(wordToBeSearched, list);
	}
}
