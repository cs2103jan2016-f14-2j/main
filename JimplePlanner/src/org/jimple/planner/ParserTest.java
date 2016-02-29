package org.jimple.planner;
import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
	
	private Parser testParser;

	@Test
	public void test() {
		testParser = new Parser();
		
		test1();
	}
	
	private void test1() {
		InputStruct testStruct = testParser.parseInput("add Learn Double Dream Hands from 8AM to 10AM");
		assertEquals("Learn Double Dream Hands", testStruct.variableArray[0]);
		assertEquals(null, testStruct.variableArray[1]);
		assertEquals("8AM", testStruct.variableArray[2]);
		assertEquals("10AM", testStruct.variableArray[3]);
		assertEquals(null, testStruct.variableArray[4]);
	}

}
