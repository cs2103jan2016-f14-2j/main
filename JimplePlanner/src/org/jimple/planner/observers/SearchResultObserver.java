package org.jimple.planner.observers;

public class SearchResultObserver extends myObserver{
	
	public SearchResultObserver(Subject subject)	{
		this.subject = subject;
		this.subject.attach(this);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		for (int i = 0; i < subject.getEvents().size(); i++) {
			System.out.println(subject.getEvents().get(0).getTitle());
		}
	}
}
