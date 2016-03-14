package org.jimple.planner;

import java.util.Date;
/* ------------------|
 * Author: A0135775W |
 * Name: Lee Lu Ke   |
 * ----------------- */

import java.util.Scanner;

public class TimeParserTest {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("Input an extended command: ");
			String testExtendedCommand = sc.nextLine();
			System.out.print("Input a time: ");
			String testInput = sc.nextLine();
			TimeParser tp = new TimeParser();
			String t = tp.timeParser(testExtendedCommand, testInput);
			if (t != null) {
				System.out.println("Time parsed to:");
				System.out.println(t);
			}
		}
	}
	
}
