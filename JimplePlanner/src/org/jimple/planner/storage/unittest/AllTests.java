package org.jimple.planner.storage.unittest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	StorageTestGetFromLine.class,
	StorageTestSaveLoadMechanics.class,
	TestWriteTask.class })

public class AllTests {

}
