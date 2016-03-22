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
	
	public static void main(String[] args) throws Exception {
		testParser = new Parser();
		InputStruct testOutput = testParser.parseInput("search 1");
		String[] testOutputArray = testOutput.getVariableArray();
		for (int i = 0; i < testOutputArray.length; i++) {
			System.out.println(testOutputArray[i]);
		}
	}
	
	/*@Test
	public void test() {
		testParser = new Parser();
		
		test1();
	}
	
	private void test1() {
		InputStruct testStruct = null;
		try {
			testStruct = testParser.parseInput("add Learn Double Dream Hands from 8AM 8am to 10AM");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[]variableArray = testStruct.getVariableArray();
		assertEquals("Learn Double Dream Hands", variableArray[0]);
		assertEquals(null, variableArray[1]);
		System.out.println(variableArray[2]);
		//assertEquals(year + "-" + month + "-" + date + "T" + minutes:00", variableArray[2]);
		//assertEquals("10AM", variableArray[3]);
		assertEquals(null, variableArray[4]);
	}*/

}
