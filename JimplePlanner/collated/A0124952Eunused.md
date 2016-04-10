# A0124952Eunused
###### ./src/org/jimple/planner/depracated/ListOfMonths.java
``` java
@Deprecated
public class ListOfMonths {
	public HashMap<String, String> listOfMonths;
	public HashMap<Integer, String> listOfNewMonths;

	public ListOfMonths() {
		listOfMonths = new HashMap<String, String>();
		listOfNewMonths = new HashMap<Integer, String>();
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
		listOfNewMonths.put(0, "01");
		listOfNewMonths.put(1, "02");
		listOfNewMonths.put(2, "03");
		listOfNewMonths.put(3, "04");
		listOfNewMonths.put(4, "05");
		listOfNewMonths.put(5, "06");
		listOfNewMonths.put(6, "07");
		listOfNewMonths.put(7, "08");
		listOfNewMonths.put(8, "09");
		listOfNewMonths.put(9, "10");
		listOfNewMonths.put(10, "11");
		listOfNewMonths.put(11, "12");
	}

	public boolean contain(String month) {
		return listOfMonths.containsKey(month.toLowerCase());
	}

	public String monthDigit(String month) {
		return listOfMonths.get(month.toLowerCase());
	}
	
	public String newMonthDigit(int month) {
		return listOfNewMonths.get(month);
	}
}
```
