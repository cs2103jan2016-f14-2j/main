package org.jimple.planner;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class ParserTest {
	
	private static Parser testParser;
	
	@Test
	public void test() {
		testParser = new Parser();
		test0();
		test1();
		test2();
		test3();
		test4();
		test5();
		test6();
		test7();
		test8();
		test9();
		test10();
		test11();
		test12();
		test13();
		test14();
	}
	
	// Miscellaneous test - for my usage.
	private void test0() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("DONE 4");
			String[] test = testStruct.getVariableArray();
			for (int i = 0; i < test.length; i++) {
				System.out.println(test[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Command: Add
	 * Input: Name, From (DMT), To (DMT), Label
	 * Expected: {event, Name, null, FromTime, ToTime, Label}
	 */
	private void test1() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("ADD Learn Double Dream Hands FROM 12 may 8am TO 13 may 12AM LABEL DOWNDOG");
			assertEquals(assertArray(testStruct, "event", "Learn Double Dream Hands", null, "2016-05-12T08:00", "2016-05-13T00:00", "DOWNDOG"), true);
		} catch (Exception e) {}
	}
	
	/* Command: ADD
	 * Input: Name (Symbols), From (DMT), To (DMT), Description
	 * Expected: {event, Name, Description, FromTime, ToTime, null}
	 */
	private void test2() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("ADD !@#$%^&*() FROM 12 jun 0123 TO 12 june 2030 DESCRIPTION UPDOG");
			assertEquals(assertArray(testStruct, "event", "!@#$%^&*()", "UPDOG", "2016-06-12T01:23", "2016-06-12T20:30", null), true);
		} catch (Exception e) {}
	}
	
	/* Command: ADD
	 * Input: nil
	 * Expected: Exception
	 */
	private void test3() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("ADD");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Command: \"ADD\" requires a Task Name. \"");
		}
	}
	
	/* Command: DELETE
	 * Input: Index
	 * Expected: {Index}
	 */
	private void test4() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("DELETE 2");
			assertEquals(assertArray(testStruct, "2"), true);
		} catch (Exception e) {}
	}
	
	/* Command: SEARCH
	 * Input: StringToSearch
	 * Expected: {StringToSearch}
	 */
	private void test5() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("SEARCH submit");
			assertEquals(assertArray(testStruct, "submit"), true);
		} catch (Exception e) {}
	}
	
	/* Command: ADD
	 * Input: Name ("ADD")
	 * Expected: {todo, "ADD", null, null, null, null}
	 */
	private void test6() {
		InputStruct testStruct;
		try {
			testStruct = testParser.parseInput("ADD ADD");
			assertEquals(assertArray(testStruct, "todo", "ADD", null, null, null, null), true);
		} catch (Exception e) {}
	}
	
	/* Command: ADD
	 * Input: Name, On (DM)
	 * Expected: {event, Name, null, OnDate (00:00), OnDate (23:59), null}
	 */
	private void test7() {
		InputStruct testStruct;
		try {
			testStruct = testParser.parseInput("ADD test7 ON 13 may");
			assertEquals(assertArray(testStruct, "event", "test7", null, "2016-05-13T00:00", "2016-05-13T23:59", null), true);
		} catch (Exception e) {}
	}
	
	/* Command: Invalid Command (asdf1234)
	 * Input: nil
	 * Expected: Exception
	 */
	private void test8() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("asdf1234");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Command: \"asdf1234\" not recognised.");
		}
	}
	
	/* Command: ADD
	 * Input: Name, At (DM)
	 * Expected: {event, Name, null, AtDate (00:00), AtDate (00:00 + 1hr), null}
	 */
	private void test9() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("ADD test9 AT 12 may");
			assertEquals(assertArray(testStruct, "event", "test9", null, "2016-05-12T00:00", "2016-05-12T01:00", null), true);
		} catch (Exception e) {}
	}
	
	/* Command: ADD
	 * Input: Name, At (D/M) T(hh:mm)
	 * Expected: {event, Name, null, AtDate&Time, AtDate&Time + 1hr, null}
	 */
	private void test10() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("ADD test10 AT 12/5 06:00");
			assertEquals(assertArray(testStruct, "event", "test10", null, "2016-05-12T06:00", "2016-05-12T07:00", null), true);
		} catch (Exception e) {}
	}
	
	/* Command: ADD
	 * Input: Name, By (D/M/Y)
	 * Expected: {event, Name, null, ByDateAndTime, null, null}
	 */
	private void test11() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("ADD test11 BY 12/5/17");
			assertEquals(assertArray(testStruct, "deadline", "test11", null, "2017-05-12T23:59", null, null), true);
		} catch (Exception e) {}
	}
	
	/* Command: EDITLABEL
	 * Input: Name, NameToChange, Colour
	 * Expected: {Name, NameToChange,Colour}
	 */
	private void test12() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("EDITLABEL asdf NAME qwerty COLOUR red");
			assertEquals(assertArray(testStruct, "asdf", "qwerty", "red"), true);
		} catch (Exception e) {}
	}
	
	/* Command: EDITLABEL
	 * Input: Name, Colour
	 * Expected: {Name, null, Colour}
	 */
	private void test13() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("EDITLABEL asdf NAME qwerty COLOUR orange");
			assertEquals(assertArray(testStruct, "asdf", "qwerty", "orange"), true);
		} catch (Exception e) {}
	}
	
	/* Command: EDITLABEL
	 * Input: Name, NameToChange, Colour (Invalid)
	 * Expected: Exception
	 */
	private void test14() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("EDITLABEL asdf NAME qwerty COLOUR notacolour");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Label Colour: \"notacolour\" invalid.");
			
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
			if (variableArray[i] == null && args[i] == null) {
				continue;
			} else if (!variableArray[i].equals(args[i])) {
				System.out.println(variableArray[i] + " " + args[i]);
				return false;
			}
		}
		return true;
	}

}
