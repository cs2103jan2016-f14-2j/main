package org.jimple.planner;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class TestTimeParser {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Lai, try input a time:");
			String testInput = sc.nextLine();
			TimeParser tp = new TimeParser();
			Calendar t = tp.timeParser(testInput);
			if (t != null) {
				System.out.println("Time Parsed:");
				System.out.println(t);
			}
		}
	}
	
}
