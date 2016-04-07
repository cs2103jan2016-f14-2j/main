package org.jimple.planner.ui;
//@@author A0122498
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class UiFormatter {

	private ArrayList<Task> formattedList;
	private ArrayList<Task> arrList;
	private ObservableList<Task> data;
	private ListView<Task> listView;
	
	public UiFormatter() {
		formattedList = new ArrayList<Task>();
	}

	public void formatList(ArrayList<Task> newList, String listType) {
		this.arrList = newList;
		formattedList.clear();

		formatType(listType);
	}

	public void formatType(String listType) {
		switch (listType) {
		case Constants.TYPE_TODAY:
			addTodayTasksToFormattedDateList();
			agendaCellFormat();
			break;
		case Constants.TYPE_NOW:
			addNowTasksToFormattedDateList();
			ongoingCellFormat();
			break;
		case Constants.TYPE_UPCOMING:
			addUpcomingTasksToFormattedDateList();
			upcomingCellFormat();
			break;
		case Constants.TYPE_AGENDA:
			addTasksToFormattedDateList();
			agendaCellFormat();
			break;
		case Constants.TYPE_ARCHIVE:
			addTasksToFormattedList();
			defaultCellFormat();
			break;
		case Constants.TYPE_DEADLINE:
			addTasksToFormattedDateList();
			agendaCellFormat();
			break;
		case Constants.TYPE_EVENT:
			addTasksToFormattedDateList();
			agendaCellFormat();
			break;
		case Constants.TYPE_TODO:
			addTasksToFormattedList();
			agendaCellFormat();
			break;
		case Constants.TYPE_SEARCH:
			addTasksToFormattedList();
			agendaCellFormat();
			break;
		default:
			formatEmptyList();
			break;
		}
	}

	private void formatEmptyList() {
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
	}

	public ListView<Task> getFormattedList() {
		if (listView == null)
			return null;
		fitToAnchorPane(listView);
		return listView;
	}

	public void fitToAnchorPane(Node node) {
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
	}

	private Task staticTask(String title, LocalDateTime fromtime) {
		Task task = new Task(title);
		task.setType(Constants.TYPE_STATIC);
		task.setIsOverDue(false);
		task.setToDate(fromtime.toString());
		return task;
	}

	private void addTodayTasksToFormattedDateList() {
		for (Task task : arrList) {
			if(task.getType().equals(Constants.TYPE_EVENT)
					&& timeDifference(task.getFromTime()) < 0
					&& timeDifference(task.getToTime()) > 0
					&& !task.getIsOverDue()){
				formattedList.add(task);
			}
			else if (LocalDateTime.now().getDayOfYear() == task.getFromTime().getDayOfYear()
					&& !task.getIsOverDue()) {
				formattedList.add(task);
			}
		}
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
		if (formattedList.isEmpty())
			listView = null;
	}

	private void addNowTasksToFormattedDateList() {
		for (Task task : arrList) {
			if(timeDifference(task.getFromTime()) < 0
					&& timeDifference(task.getToTime()) > 0){
				formattedList.add(task);
			}
			else if (LocalDateTime.now().getDayOfYear() == task.getFromTime().getDayOfYear()) {
				if (timeDifference(task.getFromTime()) < 0 && !task.getIsOverDue())
					formattedList.add(task);
			}
		}
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
		if (formattedList.isEmpty())
			listView = null;
	}

	private void addUpcomingTasksToFormattedDateList() {
		for (Task task : arrList) {
			if (timeDifference(task.getFromTime()) > 0) {
				formattedList.add(task);
				break;
			}
		}
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
		if (formattedList.isEmpty())
			listView = null;
	}

	private void addTasksToFormattedDateList() {
		String dateCounter = "";

		for (Task task : arrList) {
			if (!dateCounter.equals(task.getPrettierFromDate())) {
				dateCounter = task.getPrettierFromDate();
				formattedList.add(staticTask(dateCounter,task.getFromTime()));
			}

			formattedList.add(task);
		}
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
	}

	private void addTasksToFormattedList() {
		for (Task task : arrList) {
			formattedList.add(task);
		}
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
	}

	private void defaultCellFormat() {
		if (listView != null) {
			listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
				@Override
				public ListCell<Task> call(ListView<Task> arg0) {
					return new UiListCellDefault();
				}

			});
		}
	}

	private void agendaCellFormat() {
		if (listView != null) {
			listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
				@Override
				public ListCell<Task> call(ListView<Task> arg0) {
					return new UiListCellAgenda();
				}

			});
		}
	}

	private void upcomingCellFormat() {
		if (listView != null) {
			listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
	
				@Override
				public ListCell<Task> call(ListView<Task> arg0) {
					return new UiListCellUpcoming();
				}
	
			});
		}
	}

	private void ongoingCellFormat() {
		if (listView != null) {		
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
			@Override
			public ListCell<Task> call(ListView<Task> arg0) {
				return new UiListCellOngoing();
			}
		});
		}
	}
	
	protected static double timeDifference(LocalDateTime reference) {
		double minutes = Duration.between(LocalDateTime.now(), reference).toMillis() / 60000.0;
		return minutes;
	}
}
