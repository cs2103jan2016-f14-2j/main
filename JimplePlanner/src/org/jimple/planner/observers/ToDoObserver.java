package org.jimple.planner.observers;

public class ToDoObserver extends myObserver {

	public ToDoObserver(Subject subject) {
		this.subject = subject;
		this.subject.attach(this);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for (int i = 0; i < subject.getToDo().size(); i++) {
			System.out.println(subject.getToDo().get(0).getTitle());
		}
	}
}
