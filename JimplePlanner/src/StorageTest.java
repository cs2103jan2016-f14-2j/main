import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class StorageTest {
	@Test
	public void testIsSaved() throws IOException {
		ArrayList<Event> events = new ArrayList<Event>();
		Event event1 = new Event("Makan here for chap goh mei");
		event1.setDescription("at counsin's place");
		event1.setCategory("family time");
		
		Event event2 = new Event("Go exercise, you fatty");
		event2.setCategory("Keep fit");
		
		Event event3 = new Event("Do 2100 assignment");
		event3.setDescription("due very soon");
		event3.setCategory("Homework");
		
		events.add(event1);
		events.add(event2);
		events.add(event3);
		
		assertTrue("this should return true if saved", Storage.isSaved(events));
	}
}
