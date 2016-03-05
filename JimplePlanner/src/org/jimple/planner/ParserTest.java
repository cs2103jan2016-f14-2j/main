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
		String[]variableArray = testStruct.getVariableArray();
		assertEquals("Learn Double Dream Hands", variableArray[0]);
		assertEquals(null, variableArray[1]);
		assertEquals("8AM", variableArray[2]);
		assertEquals("10AM", variableArray[3]);
		assertEquals(null, variableArray[4]);
	}

}
