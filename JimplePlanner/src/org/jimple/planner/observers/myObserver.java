package org.jimple.planner.observers;
import org.jimple.planner.logic.Logic;

//@@author A0124952E
public abstract class myObserver {
	protected Logic logic;
	public abstract void update();
	public abstract void update(String[] displayType);
}
