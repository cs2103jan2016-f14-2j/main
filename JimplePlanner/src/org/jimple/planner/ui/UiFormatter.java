package org.jimple.planner.ui;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import org.jimple.planner.Constants;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

		if(arrList.isEmpty()){
			formatEmptyList();
			return;
		}
		formatType(listType);
	}
	
	//Agenda formatter
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
		case Constants.TYPE_TODAY:
			addTodayTasksToFormattedDateList();
			agendaCellFormat();
			break;
			
		case Constants.TYPE_NOW:
			addNowTasksToFormattedDateList();
			agendaCellFormat();
			break;
		case Constants.TYPE_UPCOMING:
			break;
		case Constants.TYPE_AGENDA:
			addTasksToFormattedDateList();
			agendaCellFormat();
			break;
		case Constants.TYPE_DEADLINE:
			addTasksToFormattedDateList();
			deadlinesCellFormat();
			break;
		case Constants.TYPE_EVENT:
			addTasksToFormattedDateList();
			eventsCellFormat();
			break;
		case Constants.TYPE_TODO:
			addTasksToFormattedList();
			todoCellFormat();
			break;
		case Constants.TYPE_SEARCH:
			addTasksToFormattedList();
			searchCellFormat();
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
				ID.setText("");
				date.setText("");
				time.setText("");
				title.setText("NO MORE DEADLINES!");
				HBox hbox = new HBox(ID,date,time,title);
				hbox.setSpacing(10);
				hbox.getStyleClass().add("hboxDeadline");
				VBox.setVgrow(hbox, Priority.ALWAYS);
				mainVbox.getChildren().add(hbox);
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
				date = new Text();
				time = new Text();
				title = new Text();
				title.setText(String.format("NO MORE EVENTS!"));
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
		if(listView == null)
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
	
	private Task staticTask(String title) {
		Task task = new Task(title);
		task.setType(Constants.TYPE_STATIC);
		task.setIsOverDue(false);
		return task;
	}

	private void addTodayTasksToFormattedDateList() {
		for (Task task : arrList) {
			if (LocalDateTime.now().getDayOfYear() == task.getFromTime().getDayOfYear()) {
				formattedList.add(task);
			}
		}
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
		if(formattedList.isEmpty())
			listView = null;
	}
	
	private void addNowTasksToFormattedDateList() {
		for (Task task : arrList) {
			if (LocalDateTime.now().getDayOfYear() == task.getFromTime().getDayOfYear()) {
				if(task.getType().equals(Constants.TYPE_EVENT) && timeDifference(task.getFromTime()) < 1 && !task.getIsOverDue())
					formattedList.add(task);
			}
		}
		data = FXCollections.observableArrayList(formattedList);
		listView = new ListView<Task>(data);
		if(formattedList.isEmpty())
			listView = null;
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
	

	
	private void agendaCellFormat() {
		if(listView != null){
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {

			@Override
			public ListCell<Task> call(ListView<Task> arg0) {
				return new ListCell<Task>() {

					@Override
					protected void updateItem(Task item, boolean bln) {
						super.updateItem(item, bln);
						if (item != null) {
							Text title = new Text(item.getTitle());
							Text ID = new Text();
							Text date = new Text();
							VBox vBox = new VBox();
							HBox hBox = new HBox();
							Region spacer = new Region();
							VBox.setVgrow(spacer, Priority.ALWAYS);
							HBox.setHgrow(spacer, Priority.ALWAYS);
							
							//STATIC
							if (item.getType().equals(Constants.TYPE_STATIC)) {
								this.getStyleClass().clear();
								this.getStyleClass().add(Constants.TYPE_STATIC);
								this.setFocusTraversable(false);
								vBox = new VBox(title);
								hBox = new HBox(vBox);
							}
							
							//DEADLINE
							else if (item.getType().equals(Constants.TYPE_DEADLINE)) {
								this.getStyleClass().clear();
								this.getStyleClass().add(Constants.TYPE_DEADLINE);
								//more than a day away
								if(timeDifference(item.getFromTime())>=1440)
									this.getStyleClass().add("green");
								//less than a day
								else if(timeDifference(item.getFromTime())>=60)
									this.getStyleClass().add("yellow");
								//less than an hour
								else if(timeDifference(item.getFromTime())>=30)
									this.getStyleClass().add("orange");
								else if(timeDifference(item.getFromTime())>=10)
									this.getStyleClass().add("red");
								else if(timeDifference(item.getFromTime())>=0)
									this.getStyleClass().add("darkred");
								else
									this.getStyleClass().add("overdue");
								vBox = new VBox(title);
								ID.setText(String.format("%d", item.getTaskId()));
								date.setText(String.format("%s", item.getPrettyFromTime()));
								hBox = new HBox(date, vBox, spacer, ID);
								title.getStyleClass().add(Constants.TYPE_DEADLINE);
								ID.getStyleClass().add(Constants.TYPE_DEADLINE);
								date.getStyleClass().add(Constants.TYPE_DEADLINE);
							}
							
							//EVENT
							else {
								this.getStyleClass().clear();
								this.getStyleClass().add(Constants.TYPE_EVENT);
								if(item.getIsOverDue()){
									this.getStyleClass().add("overdue");
								}
								ID.setText(String.format("%d", item.getTaskId()));
								date.setText(String.format("%s %s to %s %s",
										item.getPrettyFromDate(),
										item.getPrettyFromTime(),
										item.getPrettyToDate(),
										item.getPrettyToTime()));
								vBox = new VBox(title,date);
								hBox = new HBox(vBox, spacer, ID);
							}
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
		}
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
								Text title = new Text(item.getTitle());
								Text ID = new Text();
								Text date = new Text();
								VBox vBox = new VBox();
								HBox hBox = new HBox();

								Region spacer = new Region();
								VBox.setVgrow(spacer, Priority.ALWAYS);
								HBox.setHgrow(spacer, Priority.ALWAYS);
								
								//STATIC
								if (item.getType().equals(Constants.TYPE_STATIC)) {
									this.getStyleClass().add(Constants.TYPE_STATIC);
									this.setFocusTraversable(false);
									vBox = new VBox(title);
									hBox = new HBox(vBox);
								}

								//EVENT
								else {
									this.getStyleClass().add(Constants.TYPE_EVENT);
									ID.setText(String.format("%d", item.getTaskId()));
									date.setText(String.format("%s %s to %s %s",
											item.getPrettyFromDate(),
											item.getPrettyFromTime(),
											item.getPrettyToDate(),
											item.getPrettyToTime()));
									vBox = new VBox(title,date);
									hBox = new HBox(vBox, spacer, ID);
								}
								hBox.setSpacing(10);
								setGraphic(hBox);
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
							Text title = new Text(item.getTitle());
							Text ID = new Text();
							Text date = new Text();
							VBox vBox = new VBox();
							HBox hBox = new HBox();
							Region spacer = new Region();
							VBox.setVgrow(spacer, Priority.ALWAYS);
							HBox.setHgrow(spacer, Priority.ALWAYS);
							
							//STATIC
							if (item.getType().equals(Constants.TYPE_STATIC)) {
								double value = (double)LocalDateTime.now().getSecond()/60.0 * 255.0;
								String color = "-fx-background-color: #ffff" + Integer.toHexString((int)value).toString();
								setStyle(color);
								this.setFocusTraversable(false);
								vBox = new VBox(title);
								hBox = new HBox(vBox);
							}

							//DEADLINE
							else {
								this.getStyleClass().clear();
								this.getStyleClass().add(Constants.TYPE_DEADLINE);
								//more than a day away
								if(timeDifference(item.getFromTime())>=1440)
									this.getStyleClass().add("green");
								//less than a day
								else if(timeDifference(item.getFromTime())>=60)
									this.getStyleClass().add("yellow");
								//less than an hour
								else if(timeDifference(item.getFromTime())>=30)
									this.getStyleClass().add("orange");
								else if(timeDifference(item.getFromTime())>=10)
									this.getStyleClass().add("red");
								else if(timeDifference(item.getFromTime())>=0)
									this.getStyleClass().add("darkred");
								else
									this.getStyleClass().add("overdue");
								
								vBox = new VBox(title);
								ID.setText(String.format("  %d", item.getTaskId()));
								date.setText(String.format("%s", item.getPrettyFromTime()));
								hBox = new HBox(date, vBox, spacer, ID);
								title.getStyleClass().add(Constants.TYPE_DEADLINE);
								ID.getStyleClass().add(Constants.TYPE_DEADLINE);
								date.getStyleClass().add(Constants.TYPE_DEADLINE);
							}
							hBox.setSpacing(10);
							setGraphic(hBox);
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
							Text title = new Text(item.getTitle());
							Text ID = new Text();
							Region spacer = new Region();
							VBox.setVgrow(spacer, Priority.ALWAYS);
							HBox.setHgrow(spacer, Priority.ALWAYS);
							ID.setText(String.format("%d", item.getTaskId()));
							
							title.getStyleClass().add(Constants.TYPE_TODO);
							ID.getStyleClass().add(Constants.TYPE_TODO);
							this.getStyleClass().add(Constants.TYPE_TODO);
							
							VBox vBox = new VBox(title);
							HBox hBox = new HBox(vBox, spacer, ID);
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
	}
	
	private void searchCellFormat(){
		listView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {

			@Override
			public ListCell<Task> call(ListView<Task> arg0) {
				return new ListCell<Task>() {

					@Override
					protected void updateItem(Task item, boolean bln) {
						super.updateItem(item, bln);
						if (item != null) {
							Text title = new Text(item.getTitle());
							title.getStyleClass().add("fancytext");
							VBox vBox = new VBox(title);
							HBox hBox = new HBox(new Text(String.format("#%d", item.getTaskId())), vBox);
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
	}
	
	private double timeDifference(LocalDateTime reference){
		double minutes = Duration.between(LocalDateTime.now(),reference).toMillis() / 6000.0;
		return minutes;
	}
	
}
