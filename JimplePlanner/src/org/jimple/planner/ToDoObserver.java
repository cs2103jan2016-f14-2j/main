package org.jimple.planner;

public class ToDoObserver extends Observer {

	public ToDoObserver(Subject subject) {
		this.subject = subject;
		this.subject.attach((java.util.Observer) this);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for (int i = 0; i < subject.getToDo().size(); i++) {
			System.out.println(subject.getToDo().get(0).getTitle());
		}
	}
}
