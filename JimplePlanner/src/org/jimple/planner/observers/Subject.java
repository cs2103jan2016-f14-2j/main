package org.jimple.planner.observers;

import java.awt.List;
import java.util.ArrayList;
import java.util.Observer;

import org.jimple.planner.Task;
import org.jimple.planner.logic.Logic;

public class Subject {
	private ArrayList<myObserver> observers = new ArrayList<myObserver>();
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
	
	public void setState(ArrayList<Task> toDoState, ArrayList<Task> deadlineState, ArrayList<Task> eventState)	{
		logic.setToDoList(toDoState);
		logic.setDeadlinesList(deadlineState);
		logic.setEventsList(eventState);
		notifyAllObservers();
	}
	
	public void attach(myObserver observer)	{
		observers.add(observer);
	}
	
	public void notifyAllObservers()	{
		for (myObserver observer : observers)	{
			observer.update();
		}
	}
}
