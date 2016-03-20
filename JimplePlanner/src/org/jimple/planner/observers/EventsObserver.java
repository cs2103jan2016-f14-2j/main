package org.jimple.planner.observers;

public class EventsObserver extends Observer{
	
	public EventsObserver(Subject subject) {
		this.subject = subject;
		this.subject.attach((java.util.Observer) this);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		for (int i = 0; i < subject.getEvents().size(); i++) {
			System.out.println(subject.getEvents().get(0).getTitle());
		}
	}
	
}
