package org.jimple.planner.ui;

import java.util.ArrayList;
import java.util.Iterator;

import org.jimple.planner.Task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ListViewFormatter {
	private static final String TYPE_STATIC = "static";
	private static final String TYPE_AGENDA = "agenda";
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

	public void formatList(ArrayList<Task> newList, String listType) {
		this.arrList = newList;
		formattedList.clear();

		if(arrList.isEmpty()){
			formatEmptyList();
			return;
		}
		formatType(listType);
	}
	
	public void fomatList(ArrayList<Task> deadlinesList, ArrayList<Task> eventsList) {
		int counter = 0;
		formattedList.clear();
		for(Task task : deadlinesList){
			formattedList.add(task);
			if(++counter >= 3)
				break;
		}
		
		while(formattedList.size() < 3)
			formattedList.add(new Task(""));
		
		counter = 0;
		
		for(Task task : eventsList){
			formattedList.add(task);
			if(++counter >= 3)
				break;
		}
		
		while(formattedList.size() < 6)
			formattedList.add(new Task(""));
	}

	public void formatType(String listType) {
		switch (listType) {
		case TYPE_AGENDA:
			formatAgendaList();
			break;
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
	
	public VBox getMainContent(){

		Text ID;
		Text date;
		Text time;
		Text title;
		VBox mainVbox = new VBox(10);
		HBox eventsSection = new HBox(10);
		mainVbox.getStyleClass().add("mainVbox");
		eventsSection.getStyleClass().add("hbox");
		VBox.setVgrow(eventsSection, Priority.ALWAYS);

		fitToAnchorPane(mainVbox);
		
		//adding deadlines
		for(int i=0; i<3; i++){
			ID = new Text();
			date = new Text();
			time = new Text();
			title = new Text();
			if(!formattedList.get(i).getTitle().equals("")){
			ID.setText(String.format("%d", formattedList.get(i).getTaskId()));
			date.setText(formattedList.get(i).getPrettyFromDate());
			time.setText(formattedList.get(i).getPrettyFromTime());
			title.setText(formattedList.get(i).getTitle());
			HBox hbox = new HBox(ID,date,time,title);
			hbox.setSpacing(10);
			hbox.getStyleClass().add("hboxDeadline");
			VBox.setVgrow(hbox, Priority.ALWAYS);
			mainVbox.getChildren().add(hbox);
			}
			else{
				System.out.println("empty deadline");
				mainVbox.getChildren().add(new HBox());
			}
		}
		
		//adding events
		for(int i=3; i<6; i++){
			date = new Text();
			time = new Text();
			title = new Text();
			if(!formattedList.get(i).getTitle().equals("")){
			title.setText(String.format("%d %s", formattedList.get(i).getTaskId(), formattedList.get(i).getTitle()));
			date.setText(String.format("FROM %s %s", formattedList.get(i).getPrettyFromDate(), formattedList.get(i).getPrettyFromTime()));
			time.setText(String.format("TO %s %s", formattedList.get(i).getPrettyToDate(), formattedList.get(i).getPrettyToTime()));
			VBox vbox = new VBox(title,date,time);
			vbox.setSpacing(10);
			vbox.getStyleClass().add("vboxEvent");
			HBox.setHgrow(vbox, Priority.ALWAYS);
			eventsSection.getChildren().add(vbox);
			}
			else{
				System.out.println("empty event");
				date = new Text();
				time = new Text();
				title = new Text();
				title.setText(String.format(" "));
				date.setText(String.format(" "));
				time.setText(String.format(" "));
				VBox vbox = new VBox(title,date,time);
				vbox.getStyleClass().add("vboxEvent");
				HBox.setHgrow(vbox, Priority.ALWAYS);
				eventsSection.getChildren().add(vbox);
			}
		}
		
		mainVbox.getChildren().add(eventsSection);
		mainVbox.setPadding(new Insets(10));
		return mainVbox;
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
		task.setType(TYPE_STATIC);
		return task;
	}

	private void addTasksToFormattedDateList() {
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
	}
	
	private void addTasksToFormattedList() {
		for (Task task : arrList) {
			formattedList.add(task);
		}		
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
	}
	
	private void formatEmptyList() {
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
	}
	
	private void formatAgendaList() {
		addTasksToFormattedDateList();
		agendaCellFormat();
	}

	private void formatDeadlinesList() {
		addTasksToFormattedDateList();
		deadlinesCellFormat();
	}

	private void formatEventsList() {
		addTasksToFormattedDateList();
		eventsCellFormat();
	}
	
	private void formatTodoList() {
		addTasksToFormattedList();
		todoCellFormat();
	}
	
	private void agendaCellFormat() {
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {

			@Override
			public ListCell<Task> call(ListView<Task> arg0) {
				return new ListCell<Task>() {

					@Override
					protected void updateItem(Task item, boolean bln) {
						super.updateItem(item, bln);
						if (item != null) {
							Text t = new Text(item.getTitle());
							Text ID = new Text();
							Text date = new Text();
							VBox vBox = new VBox();
							HBox hBox = new HBox();
							if (item.getType().equals(TYPE_STATIC)) {
								this.setId(TYPE_STATIC);
								this.setFocusTraversable(false);
								vBox = new VBox(t);
								hBox = new HBox(vBox);
								hBox.setSpacing(10);
								setGraphic(hBox);
							}
							else if (item.getType() == TYPE_DEADLINE) {
								this.setId(TYPE_DEADLINE);
								vBox = new VBox(t);
								ID.setText(String.format("  %d", item.getTaskId()));
								date.setText(String.format("%s", item.getPrettyFromTime()));
								hBox = new HBox(ID, vBox, date);
								t.setId(TYPE_DEADLINE);
								ID.setId(TYPE_DEADLINE);
								date.setId(TYPE_DEADLINE);
							} else {
								this.setId(TYPE_EVENT);
								vBox = new VBox(t,
										new Text(String.format("%s to %s", item.getPrettyFromTime(),
												item.getPrettyToTime())));
								hBox = new HBox(new Text(String.format("%d", item.getTaskId())),
										vBox);
							}
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
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

								if (item.getType().equals(TYPE_STATIC)) {
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
							if (item.getType().equals(TYPE_STATIC)) {
								this.setFocusTraversable(false);
								VBox vBox = new VBox(t);
								HBox hBox = new HBox(vBox);
								hBox.setSpacing(10);
								setGraphic(hBox);
							}

							else {
							VBox vBox = new VBox(t,
									new Text(String.format("by: %s %s", item.getPrettyFromDate(), item.getPrettyFromTime())));
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
}
