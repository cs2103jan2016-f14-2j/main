# A0135775Wunused
###### \src\org\jimple\planner\parser\TimeParser.java
``` java
	private boolean isAfterCurrentDateAndTime() throws InvalidDateTimeFieldException {
		if (!isToday(getField(FIELD_DAY), getField(FIELD_MONTH)) && !isAfterCurrentDate(getField(FIELD_DAY), getField(FIELD_MONTH))) {
			return false;
		} else if (isToday(getField(FIELD_DAY), getField(FIELD_MONTH)) && !isAfterCurrentTime(getField(FIELD_HOUR), getField(FIELD_MINUTE))) {
			return false;
		}
		return true;
	}
	
```
