package org.jimple.planner.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskSorter {
	private static Comparator<Task> getFromDateTimeComparator(){
		return new Comparator<Task>(){
			public int compare(Task task1, Task task2){
				return task1.getFromTime().compareTo(task2.getFromTime());
			}
		};
	}
	
	private static Comparator<Task> getFromDateComparator(){
		return new Comparator<Task>(){
			public int compare(Task task1, Task task2){
				return task1.getFromTime().toLocalDate().compareTo(task2.getFromTime().toLocalDate());
			}
		};
	}
	
	private static Comparator<Task> getTaskIdComparator(){
		return new Comparator<Task>(){
			public int compare(Task task1, Task task2){
				int task1id = task1.getTaskId();
				int task2id = task2.getTaskId();
				int result;
				if(task1id<task2id){
					result = -1;
				} else if (task1id==task2id){
					result = 0;
				} else {
					result = 1;
				}
				return result;
			}
		};
	}
	
	private static void sortDeadlines(ArrayList<Task> deadlineList){
		Comparator<Task> fromDateComparator = getFromDateTimeComparator();
		Collections.sort(deadlineList, fromDateComparator);
	}
	
	private static void sortEvents(ArrayList<Task> eventList){
		Comparator<Task> fromDateComparator = getFromDateTimeComparator();
		Collections.sort(eventList, fromDateComparator);
	}
	
	private static void sortById(ArrayList<ArrayList<Task>> allTaskLists){
		Comparator<Task> taskIdComparator = getTaskIdComparator();
		for(ArrayList<Task> taskList: allTaskLists){
			Collections.sort(taskList, taskIdComparator);
		}
	}
	
	public void sortTasks(ArrayList<ArrayList<Task>> allTaskLists){
		assert allTaskLists.size() == 4;
		sortById(allTaskLists);
		sortDeadlines(allTaskLists.get(1));
		sortEvents(allTaskLists.get(2));
	}
	
	public void sortTasksByTime(ArrayList<Task> list){
		Collections.sort(list, getFromDateComparator());
	}
}
