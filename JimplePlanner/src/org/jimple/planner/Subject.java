package org.jimple.planner;

import java.awt.List;
import java.util.ArrayList;
import java.util.Observer;

public class Subject {
	private ArrayList<Observer>	observers = new ArrayList<Observer>();
	
	public int getState()	{
		
		return 1;
	}
	
	public void setState(int state)	{
		
	}
	
	public void attach(Observer observer)	{
		
	}
	
	public void notifyAllObservers()	{
		for (Observer observer : observers)	{
			observer.update();
		}
	}
}
