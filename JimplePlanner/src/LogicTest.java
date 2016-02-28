import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LogicTest {
	Logic testLogic = new Logic();
	
	@Test
	public void AddShouldReturnFeedback() throws IOException {
		String originalInput = "add finish 2103 homework by tomorrow";
		String[] parsedInput = {"finish 2103 homework", "", "", "2007-12-03T00:00", ""};
		assertEquals("task is added to file", "task added to planner\n", testLogic.addToTaskList(parsedInput, originalInput));
	}
	
	@Test
	public void EditShouldReturnFeedback() throws IOException	{
		String[] parsedInput = {"1", "go school", "that means NUS", "2016-02-29T12:00", "", ""};
		assertEquals("task is edited", "task edited in planner\n", testLogic.editTask(parsedInput));
	}

}
