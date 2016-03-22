package org.jimple.planner;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class ParserTest {
	
	private static Parser testParser;

	private Date d = Calendar.getInstance().getTime();
	private int year = d.getYear() + 1900;
	private String date = String.format("%02d", d.getDate());
	private String month = String.format("%02d", d.getMonth()+1);
	private String hours = String.format("%02d", d.getHours());
	private String minutes = String.format("%02d", d.getMinutes());
	
	@Test
	public void test() {
		testParser = new Parser();
		test1();
		test2();
		test3();
	}
	
	private void test1() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("add Learn Double Dream Hands from 12 may 8am to 13 may 12AM category DOWNDOG");
			String[] test = testStruct.getVariableArray();
			for (String i: test) {
				System.out.println(i);
			}
			assertEquals(assertArray(testStruct, "event", "Learn Double Dream Hands", null, "2016-05-12T08:00", "2016-05-13T00:00", "DOWNDOG"), true);
		} catch (Exception e) {}
	}
	
	private void test2() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("add !@#$%^&*() from 12 jun 0123 to 12 june 2030 description UPDOG");
			assertEquals(assertArray(testStruct, "event", "!@#$%^&*()", "UPDOG", "2016-06-12T01:23", "2016-06-12T20:30", null), true);
		} catch (Exception e) {}
	}
	
	private void test3() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("add");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Command Invalid. Type \"help\" to see the list of commands.");
		}
	}
	
	private boolean assertArray(InputStruct inputStruct, String... args) {
		if (inputStruct == null && args[0] == "exception") {
			return true;
		}
		String[] variableArray = inputStruct.getVariableArray();
		if (variableArray.length != args.length) {
			return false;
		}
		for (int i = 0; i < variableArray.length; i++) {
			if (!variableArray[i].equals(args[i])) {
				System.out.println(variableArray[i] + " " + args[i]);
				return false;
			}
		}
		return true;
	}

}
