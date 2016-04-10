# generated
###### \src\org\jimple\planner\logic\Logic.java
``` java
	public ArrayList<Task> getToDoList() {
		return todo;
	}

```
###### \src\org\jimple\planner\logic\Logic.java
``` java
	public ArrayList<Task> getDeadlinesList() {
		checkOverCurrentTime(deadlines, events);
		return deadlines;
	}

```
###### \src\org\jimple\planner\logic\Logic.java
``` java
	public ArrayList<Task> getEventsDividedList() {
		checkOverCurrentTime(deadlines, events);
		ArrayList<Task> dividedTasks = getDividedTasks(events);
		return dividedTasks;
	}

```
###### \src\org\jimple\planner\logic\Logic.java
``` java
	public ArrayList<Task> getEventsList() {
		checkOverCurrentTime(deadlines, events);
		return events;
	}

```
###### \src\org\jimple\planner\logic\Logic.java
``` java
	public ArrayList<Task> getArchivedList() {
		return archivedTasks;
	}
	
```
###### \src\org\jimple\planner\logic\Logic.java
``` java
	public ArrayList<Task> getConflictedTasks() {
		conflictedTasks = conflictChecker.getConflictedTasks(LogicConflict.mostRecentlyCheckedConflict, deadlines,
				events);
		return conflictedTasks;
	}

```
###### \src\org\jimple\planner\logic\Logic.java
``` java
	public ArrayList<TaskLabel> getTaskLabels() {
		return taskLabels;
	}

```
###### \src\org\jimple\planner\logic\LogicAdd.java
``` java
	public String testAddToTaskList(String[] parsedInput, ArrayList<Task> tempHistory, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<TaskLabel> taskLabels,
			LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash)
					throws IOException, InvalidFromAndToTimeException {
		return addToTaskList(parsedInput, tempHistory, todo, deadlines, events, taskLabels, undoTasks, idHash);
	}
}
```
###### \src\org\jimple\planner\logic\LogicArchive.java
``` java
	public String testMarkTaskAsDone(String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks,
			ArrayList<Task> tempHistory, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			ArrayList<Task> archivedTasks, ArrayList<TaskLabel> taskLabels) throws IOException {
		return markTaskAsDone(parsedInput, undoTasks, tempHistory, todo, deadlines, events, archivedTasks, taskLabels);
	}
	
```
###### \src\org\jimple\planner\logic\LogicArchive.java
``` java
	public String testMarkTaskAsUndone(String[] parsedInput, LinkedList<LogicPreviousTask> undoTasks,
			ArrayList<Task> tempHistory, ArrayList<Task> todo, ArrayList<Task> deadlines, ArrayList<Task> events,
			ArrayList<Task> archivedTasks, ArrayList<TaskLabel> taskLabels) throws IOException {
		return markTaskAsUndone(parsedInput, undoTasks, tempHistory, todo, deadlines, events, archivedTasks,
				taskLabels);
	}

}
```
###### \src\org\jimple\planner\logic\LogicDelete.java
``` java
	public String testDeleteTask(String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks, LinkedList<LogicPreviousTask> undoTasks,
			HashMap<Integer, Boolean> idHash) throws IOException {
		return deleteTask(variableArray, todo, deadlines, events, archivedTasks, undoTasks, idHash);
	}

```
###### \src\org\jimple\planner\logic\LogicDelete.java
``` java
	public boolean testFindTaskToDelete(String[] variableArray, ArrayList<Task> list,
			LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash) throws IOException {
		return findTaskToDelete(variableArray, list, undoTasks, idHash);
	}

}
```
###### \src\org\jimple\planner\logic\LogicEdit.java
``` java
	public boolean testFindTaskToEdit(String[] variableArray, ArrayList<Task> list, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> tempHistory,
			ArrayList<TaskLabel> taskLabels, LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash)
					throws IOException, InvalidFromAndToTimeException {
		return findTaskToEdit(variableArray, list, todo, deadlines, events, tempHistory, taskLabels, undoTasks, idHash);
	}

```
###### \src\org\jimple\planner\logic\LogicEdit.java
``` java
	public String testEditTask(String[] variableArray, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> tempHistory, ArrayList<TaskLabel> taskLabels,
			LinkedList<LogicPreviousTask> undoTasks, HashMap<Integer, Boolean> idHash)
					throws IOException, InvalidFromAndToTimeException {
		return editTask(variableArray, todo, deadlines, events, tempHistory, taskLabels, undoTasks, idHash);
	}

}
```
###### \src\org\jimple\planner\logic\LogicLabel.java
``` java
	public String testChangeLabel(String[] variableArray, ArrayList<TaskLabel> taskLabels, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events) throws IOException {
		return changeLabel(variableArray, taskLabels, todo, deadlines, events);
	}
}
```
###### \src\org\jimple\planner\logic\LogicSearch.java
``` java
	public boolean testIsContainKeyword(Task event, String keyword) {
		return isContainKeyword(event, keyword);
	}

```
###### \src\org\jimple\planner\logic\LogicSearch.java
``` java
	public ArrayList<Task> testSearchWord(String wordToBeSearched, ArrayList<Task> todo, ArrayList<Task> deadlines,
			ArrayList<Task> events, ArrayList<Task> archivedTasks) {
		return searchWord(wordToBeSearched, todo, deadlines, events, archivedTasks);
	}

```
###### \src\org\jimple\planner\logic\LogicSearch.java
``` java
	public ArrayList<Task> testgetSearchedTasks(String wordToBeSearched, ArrayList<Task> list) {
		return getSearchedTasks(wordToBeSearched, list);
	}
}
```
###### \src\org\jimple\planner\logic\LogicUndo.java
``` java
	public String testUndoPreviousChange(LinkedList<LogicPreviousTask> undoTasks, ArrayList<Task> todo,
			ArrayList<Task> deadlines, ArrayList<Task> events, ArrayList<Task> archivedTasks,
			ArrayList<Task> tempHistory, ArrayList<TaskLabel> taskLabels, HashMap<Integer, Boolean> idHash) throws IOException {
		return undoPreviousChange(undoTasks, todo, deadlines, events, archivedTasks, tempHistory, taskLabels,
				idHash);
	}
}
```
###### \src\org\jimple\planner\task\Task.java
``` java
	@Override
	public int hashCode() {
		final int prime = 97;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fromDateTime == null) ? 0 : fromDateTime.hashCode());
		result = prime * result + (isDone ? 1231 : 1237);
		result = prime * result + taskId;
		result = prime * result + ((taskLabel == null) ? 0 : taskLabel.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((toDateTime == null) ? 0 : toDateTime.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Task))
			return false;
		Task other = (Task) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fromDateTime == null) {
			if (other.fromDateTime != null)
				return false;
		} else if (!fromDateTime.equals(other.fromDateTime))
			return false;
		if (isDone != other.isDone)
			return false;
		if (taskId != other.taskId)
			return false;
		if (taskLabel == null) {
			if (other.taskLabel != null)
				return false;
		} else if (!taskLabel.equals(other.taskLabel))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (toDateTime == null) {
			if (other.toDateTime != null)
				return false;
		} else if (!toDateTime.equals(other.toDateTime))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
```
###### \src\org\jimple\planner\task\TaskLabel.java
``` java
	@Override
	public int hashCode() {
		final int prime = 101;
		int result = 1;
		result = prime * result + colourId;
		result = prime * result + labelId;
		result = prime * result + ((labelName == null) ? 0 : labelName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskLabel other = (TaskLabel) obj;
		if (colourId != other.colourId)
			return false;
		if (labelId != other.labelId)
			return false;
		if (labelName == null) {
			if (other.labelName != null)
				return false;
		} else if (!labelName.equals(other.labelName))
			return false;
		return true;
	}
	
	
}
```
