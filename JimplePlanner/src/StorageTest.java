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
	
	@Test
	public void testGetEventFromLine() throws IOException{
		ArrayList<Event> events = new ArrayList<Event>();
		
		String line1 = "/Makan here for chap goh mei//:desc:at counsin's place/";
		String line2 = "/Go exercise, you fatty//:cat:Keep fit/";
		String line3 = "/Do 2100 assignment//:desc:due very soon/";
		events.add(Storage.testGetEventFromLine(line1));
		events.add(Storage.testGetEventFromLine(line2));
		events.add(Storage.testGetEventFromLine(line3));
		
		if(events.size()!=0){
			String title1 = events.get(0).getTitle();
			assertEquals("true if same", "Makan here for chap goh mei", title1);
			String desc1 = events.get(0).getDescription();
			assertEquals("true if same", "at counsin's place", desc1);
			String cat1 = events.get(0).getCategory();
			assertEquals("true if same", "family time", cat1);
			
			String title2 = events.get(0).getTitle();
			assertEquals("true if same", "Go exercise, you fatty", title2);
			String cat2 = events.get(0).getCategory();
			assertEquals("true if same", "Keep fit", cat2);
			
			String title3 = events.get(0).getTitle();
			assertEquals("true if same", "Do 2100 assignment", title3);
			String desc3 = events.get(0).getDescription();
			assertEquals("true if same", "due very soon", desc3);
			String cat3 = events.get(0).getCategory();
			assertEquals("true if same", "Homework", cat3);
			
		} else{
			assertTrue("just a short confirmation that it is truly empty", events.isEmpty());
		}
	}
}
