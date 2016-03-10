package org.jimple.planner;

import java.util.HashMap;

public class ListOfMonths {
	public HashMap<Integer, String> listOfMonths;

	public ListOfMonths() {
		listOfMonths = new HashMap<Integer, String>();
		listOfMonths.put(0, "01");
		listOfMonths.put(1, "02");
		listOfMonths.put(2, "03");
		listOfMonths.put(3, "04");
		listOfMonths.put(4, "05");
		listOfMonths.put(5, "06");
		listOfMonths.put(6, "07");
		listOfMonths.put(7, "08");
		listOfMonths.put(8, "09");
		listOfMonths.put(9, "10");
		listOfMonths.put(10, "11");
		listOfMonths.put(11, "12");
	}

	public String monthDigit(int month) {
		return listOfMonths.get(month);
	}
}