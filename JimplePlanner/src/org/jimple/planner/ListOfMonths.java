package org.jimple.planner;

import java.util.HashMap;

public class ListOfMonths {
	public HashMap<String, String> listOfMonths;

	public ListOfMonths() {
		listOfMonths = new HashMap<String, String>();
		listOfMonths.put("january", "01");
		listOfMonths.put("february", "02");
		listOfMonths.put("march", "03");
		listOfMonths.put("april", "04");
		listOfMonths.put("may", "05");
		listOfMonths.put("june", "06");
		listOfMonths.put("july", "07");
		listOfMonths.put("august", "08");
		listOfMonths.put("september", "09");
		listOfMonths.put("october", "10");
		listOfMonths.put("november", "11");
		listOfMonths.put("december", "12");
	}

	public boolean contain(String month) {
		return listOfMonths.containsKey(month.toLowerCase());
	}

	public String monthDigit(String month) {
		return listOfMonths.get(month.toLowerCase());
	}
}