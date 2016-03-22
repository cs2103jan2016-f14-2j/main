package org.jimple.planner.storage.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.jimple.planner.Task;
import org.jimple.planner.storage.StorageSave;
import org.junit.Test;

public class StorageTestIsSaved {
	private StorageSave saveUnit = new StorageSave();
	
	@Test
	public void testIsSaved() throws IOException {
		ArrayList<ArrayList<Task>> tasks = ExampleTasks.getExampleTasks();
		boolean saveState = saveUnit.isSavedTest(tasks);
		assertTrue("this should return true if saved", saveState);
	}
}
