package org.jimple.planner.observers;

import java.awt.List;
import java.util.ArrayList;
import java.util.Observer;

import org.jimple.planner.Task;
import org.jimple.planner.logic.Logic;

public class Subject {
	private ArrayList<Observer>	observers = new ArrayList<Observer>();
	Logic logic = new Logic();
	
	public ArrayList<Task> getToDo()	{
		return logic.getToDoList();
	}
	
	public ArrayList<Task> getDeadlines()	{
		return logic.getDeadlinesList();
	}
	
	public ArrayList<Task> getEvents()	{
		return logic.getEventsList();
	}
	
	public void setTodo(ArrayList<Task> toDoState)	{
		logic.setToDoList(toDoState);
	}
	
	public void setDeadlines(ArrayList<Task> deadlineState)	{
		logic.setDeadlinesList(deadlineState);
	}
	
	public void setEvents(ArrayList<Task> eventState)	{
		logic.setEventsList(eventState);
	}
	
	public void attach(Observer observer)	{
		observers.add(observer);
	}
	
	public void notifyAllObservers()	{
		for (Observer observer : observers)	{
			observer.update();
		}
	}
}
