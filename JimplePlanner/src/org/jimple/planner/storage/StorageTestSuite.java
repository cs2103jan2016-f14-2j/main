package org.jimple.planner.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	StorageTestGetFromLine.class,
	StorageTestSaveLoadMechanics.class,
	StorageTestWriteTask.class })

public class StorageTestSuite {

}
