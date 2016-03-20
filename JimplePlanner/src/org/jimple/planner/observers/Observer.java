package org.jimple.planner.observers;

public abstract class Observer {
	protected Subject subject;
	public abstract void update();
}
