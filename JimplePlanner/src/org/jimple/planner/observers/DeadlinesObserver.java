package org.jimple.planner.observers;
import org.jimple.planner.logic.*;

public class DeadlinesObserver extends myObserver{
	
	public DeadlinesObserver(Subject subject) {
		this.subject = subject;
		this.subject.attach(this);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		for (int i = 0; i < subject.getDeadlines().size(); i++) {
			System.out.println(subject.getDeadlines().get(0).getTitle());
		}
	}

}
