package org.jimple.planner;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ListViewFormatter {
	private static final String TYPE_FLOATING = "floating";
	private static final String TYPE_DEADLINE = "deadline";
	private static final String TYPE_EVENT = "event";
	private ArrayList<Task> formattedList;
	private ArrayList<Task> arrList;
	private ObservableList<Task> data;
	private ListView<Task> listView;

	public ListViewFormatter() {
		formattedList = new ArrayList<Task>();
	}

	public ListViewFormatter(ArrayList<Task> newList) {
		formatList(newList);
	}

	public void formatList(ArrayList<Task> newList) {
		this.arrList = newList;
		formattedList.clear();

		if(arrList.isEmpty()){
			formatEmptyList();
			return;
		}
		switch (arrList.get(0).getType()) {
		case TYPE_DEADLINE:
			formatDeadlinesList();
			break;
		case TYPE_EVENT:
			formatEventsList();
			break;
		case TYPE_FLOATING:
			formatTodoList();
			break;
		default: //AGENDA LIST
			formatEmptyList();
			break;
		}
	}

	private void formatAgendaList() {
		// TODO Auto-generated method stub
		
	}

	public ListView<Task> getFormattedList() {
		fitToAnchorPane(listView);
		return listView;
	}

	public void fitToAnchorPane(Node node) {
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
	}

	private Task staticTask(String title) {
		Task task = new Task(title);
		task.setType("static");
		return task;
	}

	private void formatDeadlinesList() {
		String dateCounter = "";

		for (Task task : arrList) {
			if (!dateCounter.equals(task.getPrettyToDate())) {
				dateCounter = task.getPrettyToDate();
				formattedList.add(staticTask(dateCounter));
			}

			formattedList.add(task);
		}
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
		deadlinesCellFormat();
	}
	
	private void formatEmptyList() {
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
	}

	private void formatEventsList() {
		String dateCounter = "";

		for (Task task : arrList) {
			if (!dateCounter.equals(task.getPrettyFromDate())) {
				dateCounter = task.getPrettyFromDate();
				formattedList.add(staticTask(dateCounter));
			}

			formattedList.add(task);
		}
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
		eventsCellFormat();
	}
	
	private void formatTodoList() {
		for (Task task : arrList) {
			formattedList.add(task);
		}
		
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
		todoCellFormat();
	}

	private void eventsCellFormat() {
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
				@Override
				public ListCell<Task> call(ListView<Task> arg0) {
					return new ListCell<Task>() {
						@Override
						protected void updateItem(Task item, boolean bln) {
							super.updateItem(item, bln);
							if (item != null) {
								Text t = new Text(item.getTitle());
								t.setId("fancytext");

								if (item.getType().equals("static")) {
//									this.setDisabled(true);
									this.setFocusTraversable(false);
									VBox vBox = new VBox(t);
									HBox hBox = new HBox(vBox);
									hBox.setSpacing(10);
									setGraphic(hBox);
								}

								else {
									VBox vBox = new VBox(t,
											new Text(String.format("from: %s %s", item.getPrettyFromDate(),
													item.getPrettyFromTime())),
											new Text(String.format("to: %s %s", item.getPrettyToDate(),
													item.getPrettyToTime())));
									HBox hBox = new HBox(new Text(String.format("#%d", item.getTaskId())), vBox);
									hBox.setSpacing(10);
									setGraphic(hBox);
								}
							}
						}
					};
				}

			});
	}
	
	private void deadlinesCellFormat(){
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {

			@Override
			public ListCell<Task> call(ListView<Task> arg0) {
				return new ListCell<Task>() {

					@Override
					protected void updateItem(Task item, boolean bln) {
						super.updateItem(item, bln);
						if (item != null) {
							Text t = new Text(item.getTitle());
							t.setId("fancytext");
							if (item.getType().equals("static")) {
//								this.setDisabled(true);
								this.setFocusTraversable(false);
								VBox vBox = new VBox(t);
								HBox hBox = new HBox(vBox);
								hBox.setSpacing(10);
								setGraphic(hBox);
							}

							else {
							VBox vBox = new VBox(t,
									new Text(String.format("by: %s %s", item.getPrettyToDate(), item.getPrettyToTime())));
							HBox hBox = new HBox(
									new Text(String.format("#%d", item.getTaskId())),
									vBox);
							hBox.setSpacing(10);
							setGraphic(hBox);
							}
						}
					}
				};
			}

		});
	}
	
	private void todoCellFormat(){
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {

			@Override
			public ListCell<Task> call(ListView<Task> arg0) {
				return new ListCell<Task>() {

					@Override
					protected void updateItem(Task item, boolean bln) {
						super.updateItem(item, bln);
						if (item != null) {
							Text t = new Text(item.getTitle());
							t.setId("fancytext");

							VBox vBox = new VBox(t);
							HBox hBox = new HBox(new Text(String.format("#%d", item.getTaskId())), vBox);
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
	}
	
	public ListView<Task> getFormattedAgendaList() {
		data = FXCollections.observableArrayList();
		data.addAll(formattedList);
		ListView<Task> listView = new ListView<Task>(data);
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {

			@Override
			public ListCell<Task> call(ListView<Task> arg0) {
				return new ListCell<Task>() {

					@Override
					protected void updateItem(Task item, boolean bln) {
						super.updateItem(item, bln);
						if (item != null) {
							Text t = new Text(item.getTitle());
							VBox vBox = new VBox();
							HBox hBox = new HBox();
							if (item.getType() == TYPE_DEADLINE) {
								t.setId(TYPE_DEADLINE);
								vBox = new VBox(t);
								hBox = new HBox(new Text(String.format("#%d", super.getIndex() + arrList.size())), vBox,
										new Text(String.format("by %s %s", item.getPrettyToDate(),
												item.getPrettyToTime())));
							} else {
								t.setId(TYPE_EVENT);
								vBox = new VBox(t,
										new Text(String.format("from: %s %s", item.getPrettyFromDate(),
												item.getPrettyFromTime())),
										new Text(String.format("to: %s %s", item.getPrettyToDate(),
												item.getPrettyToTime())));
								hBox = new HBox(new Text(String.format("#%d", super.getIndex() + arrList.size())),
										vBox);
							}
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
		return null;
	}

	public String getListType() {

		return null;
	}
}
