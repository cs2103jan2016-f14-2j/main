package org.jimple.planner;

public class DeadlinesObserver extends Observer{
	
	public DeadlinesObserver(Subject subject) {
		this.subject = subject;
		this.subject.attach((java.util.Observer) this);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		for (int i = 0; i < subject.getDeadlines().size(); i++) {
			System.out.println(subject.getDeadlines().get(0).getTitle());
		}
	}

}
