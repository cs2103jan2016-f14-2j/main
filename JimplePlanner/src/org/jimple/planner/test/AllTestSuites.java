package org.jimple.planner.test;

import org.jimple.planner.test.storage.StorageTestSuite;
import org.jimple.planner.test.parser.ParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//@@author A0135808B
@RunWith(Suite.class)
@SuiteClasses({ 
	StorageTestSuite.class,
	ParserTest.class
})

public class AllTestSuites {

}