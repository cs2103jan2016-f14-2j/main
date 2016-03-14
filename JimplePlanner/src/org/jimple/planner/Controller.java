package org.jimple.planner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class Controller implements Initializable {
	Logic logic = new Logic();

	@FXML
	TextField commandBox;

	@FXML
	Text textArea;

	@FXML
	Label messagePrompt;

	@FXML
	AnchorPane mainController;

	@FXML
	Tab agendaTab;
	@FXML
	Tab eventsTab;
	@FXML
	Tab todoTab;
	@FXML
	Tab deadlinesTab;

	@FXML
	TabPane tabPanes;

	@FXML
	AnchorPane agendaContent;

	@FXML
	AnchorPane eventsContent;

	@FXML
	AnchorPane deadlinesContent;

	@FXML
	AnchorPane todoContent;

	@FXML
	StackPane stackPane;
	// @FXML
	// ListView<Task> listView;

	@FXML
	ListView<String> list;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		assert commandBox != null : "fx:id=\"synopsis\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
		System.out.println("initializing Jimple UI");

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				commandBox.requestFocus();
			}
		});

		commandBoxListener();
		tabPanesListener();
		initializeDisplay();
		taskSelectionListener();
	}

	private void initializeDisplay() {
		loadAgendaList();
		loadEventsList();
		loadDeadlinesList();
		loadTodoList();
	}

	public void loadAgendaList() {
		ArrayList<Task> taskList = new ArrayList<Task>(logic.display("deadlines"));
		taskList.addAll(logic.display("events"));
		ObservableList<Task> data = FXCollections.observableArrayList();
		data.addAll(taskList);
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
							if (item.getType() == "deadline") {
								t.setId("deadline");
								vBox = new VBox(t);
								hBox = new HBox(new Text(String.format("#%d", super.getIndex()+ logic.display("floating").size())), vBox, new Text(String.format("by %s", item.getPrettyToDate())));
							} else {
								t.setId("event");
								vBox = new VBox(t, new Text(String.format("from: %s", item.getPrettyFromDate())),
										new Text(String.format("to: %s", item.getPrettyToDate())));
								hBox = new HBox(new Text(String.format("#%d", super.getIndex()+ logic.display("floating").size())), vBox);
							}
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
		fitToAnchorPane(listView);
		agendaContent.getChildren().clear();
		agendaContent.getChildren().add(listView);
	}
	
	public void loadEventsList() {
		ArrayList<Task> taskList = new ArrayList<Task>(logic.display("events"));
		ObservableList<Task> data = FXCollections.observableArrayList();
		data.addAll(taskList);
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
							t.setId("fancytext");
							VBox vBox = new VBox(t, new Text(String.format("from: %s", item.getFromTime())),
									new Text(String.format("to: %s", item.getToTime())));
							HBox hBox = new HBox(new Text(String.format("#%d", super.getIndex()+ logic.display("floating").size()
									+ logic.display("deadlines").size())), vBox);
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
		fitToAnchorPane(listView);
		eventsContent.getChildren().clear();
		eventsContent.getChildren().add(listView);
	}

	public void loadDeadlinesList() {
		ArrayList<Task> taskList = new ArrayList<Task>(logic.display("deadlines"));
		ObservableList<Task> data = FXCollections.observableArrayList();
		data.addAll(taskList);
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
							t.setId("fancytext");
							VBox vBox = new VBox(t, new Text(String.format("from: %s", item.getFromTime())),
									new Text(String.format("to: %s", item.getToTime())));
							HBox hBox = new HBox(new Text(String.format("#%d", super.getIndex()+logic.display("floating").size())), vBox);
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
		fitToAnchorPane(listView);
		deadlinesContent.getChildren().clear();
		deadlinesContent.getChildren().add(listView);
	}

	public void loadTodoList() {
		ArrayList<Task> taskList = new ArrayList<Task>(logic.display("floating"));
		ObservableList<Task> data = FXCollections.observableArrayList();
		data.addAll(taskList);
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
							t.setId("fancytext");
							VBox vBox = new VBox(t);
							HBox hBox = new HBox(new Text(String.format("#%d", super.getIndex())), vBox);
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
		fitToAnchorPane(listView);
		todoContent.getChildren().clear();
		todoContent.getChildren().add(listView);
	}
	
	public void showSearch(String searchStr) {
		ArrayList<Task> taskList = new ArrayList<Task>(logic.searchWord(searchStr));
		ObservableList<Task> data = FXCollections.observableArrayList();
		data.addAll(taskList);
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
							t.setId("fancytext");
							VBox vBox = new VBox(t, new Text(String.format("from: %s", item.getFromTime())),
									new Text(String.format("to: %s", item.getToTime())));
							HBox hBox = new HBox(vBox);
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
		fitToAnchorPane(listView);
		((Pane) getCurrentTab().getContent()).getChildren().clear();
		((Pane) getCurrentTab().getContent()).getChildren().add(listView);
	}

	public String getCurrentTabName() {
		return tabPanes.getSelectionModel().getSelectedItem().getText();
	}

	public Tab getCurrentTab() {
		return tabPanes.getSelectionModel().getSelectedItem();
	}
	
	public void updatePointer(int num){
		System.out.println(getActiveListView().getItems().size());
		if(num >= getActiveListView().getItems().size())
			num -= 1;
		getActiveListView().requestFocus();
		getActiveListView().getSelectionModel().select(num);
		getActiveListView().scrollTo(getActiveListView().getSelectionModel().getSelectedIndex());
	}

	public int getCurrentTabItemIndex() {
		return getActiveListView().getSelectionModel().getSelectedIndex();
	}

	public void deselectTaskItem() {
		getActiveListView().getSelectionModel().clearSelection();
	}

	@SuppressWarnings("unchecked")
	private ListView<Task> getActiveListView() {
		return (ListView<Task>) ((Pane) tabPanes.getSelectionModel().getSelectedItem().getContent()).getChildren()
				.get(0);
	}

	public void fitToAnchorPane(Node node) {
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
	}

	public void enterTriggered() throws IOException {
		String inputStr = getInputCommand();
		if (!isEmpty(inputStr)) {
			String[] feedback = logic.execute(inputStr);
			messagePrompt.setText(feedback[0]);
			if (feedback[1] != null)
				System.out.println(feedback[1]);

			fadeOut(5, messagePrompt);

			clearCommandBox();

			String displayType = feedback[1];

//			reloadDisplay();
			switch (displayType) {
			case "event":
				addAndReload(eventsTab);
				break;
			case "deadline":
				addAndReload(deadlinesTab);
				break;
			case "todo":
				addAndReload(todoTab);
				break;
			case "search":
				showSearch(feedback[0]);
				break;
			default:
				reloadDisplay();
				break;
			}
		}
	}

	private void fadeOut(float sec, Node item) {
		FadeTransition ft = new FadeTransition(Duration.millis(3000), item);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setCycleCount(1);
		ft.play();
	}

	private void addAndReload(Tab tab) {
		initializeDisplay();
		tabPanes.getSelectionModel().select(tab);
		if (tab == todoTab) {
			getActiveListView().requestFocus();
			getActiveListView().getSelectionModel().selectLast();
			getActiveListView().scrollTo(getActiveListView().getSelectionModel().getSelectedIndex());
		}
	}

	private void reloadDisplay() {
		initializeDisplay();
	}

	@SuppressWarnings("unchecked")
	private ListView<Task> getList(Tab tab) {
		return (ListView<Task>) ((Pane) tab.getContent()).getChildren().get(0);
	}

	private void deleteSelectedTask() {
		try {
			System.out.println(getCurrentTabName());
			System.out.println(getCurrentTabItemIndex());
			int selectedIndex = getCurrentTabItemIndex();
			if(selectedIndex == -1)
				return;
			switch (getCurrentTabName()) {
			case "To-do":
				logic.execute("delete " + selectedIndex);
				break;

			case "Deadlines":
				logic.execute("delete " + (selectedIndex + logic.display("floating").size()));
				break;

			case "Events":
				System.out.println("deleting from events");
				logic.execute("delete " + (selectedIndex + logic.display("floating").size()
						+ logic.display("deadlines").size()));
				break;

			case "Agenda":
				System.out.println("deleting from agenda");
				logic.execute("delete " + (selectedIndex + logic.display("floating").size()));
				break;

			default:
				break;
			}
			reloadDisplay();
			updatePointer(selectedIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void commandBoxListener() {
		commandBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ESCAPE)
					tabPanes.requestFocus();
			}
		});
	}

	public void taskSelectionListener() {
		getList(getCurrentTab()).setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.UP) {
					if (getCurrentTabItemIndex() == 0) {
						tabPanes.requestFocus();
						// tabPanes.getSelectionModel().getSelectedItem().getContent().requestFocus();
						deselectTaskItem();
					}
				}

			}
		});
	}

	public void tabPanesListener() {
		tabPanes.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				deselectTaskItem();
				tabPanes.getSelectionModel().getSelectedItem().getContent().requestFocus();
			}
		});
		tabPanes.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode().isArrowKey())
					taskSelectionListener();

				switch (t.getCode()) {
				case DOWN:
					getActiveListView().requestFocus();
					if (isListViewSelectionEmpty())
						getActiveListView().getSelectionModel().select(0);
					break;
				case UP:
					taskSelectionListener();
					break;
				case LEFT:
					tabPanes.requestFocus();
					deselectTaskItem();
					break;
				case RIGHT:
					tabPanes.requestFocus();
					deselectTaskItem();
					break;
				case BACK_SPACE:
				case DELETE:
					// Pane popup = new Pane();
					// VBox dialogVbox = new VBox(20);
					// dialogVbox.getChildren().add(new Text("Confirm
					// delete?"));
					// popup.getChildren().add(dialogVbox);
					// stackPane.getChildren().add(popup);

					// final Stage dialog = new Stage();
					// dialog.initModality(Modality.APPLICATION_MODAL);
					//// dialog.initOwner(primaryStage);
					// Button btn = new Button();
					// btn.setText("delete");
					// btn.setOnAction(
					// new EventHandler<ActionEvent>() {
					// @Override
					// public void handle(ActionEvent event) {
					// deleteSelectedTask();
					// dialog.close();
					// }
					// });
					// VBox dialogVbox = new VBox(20);
					// dialogVbox.getChildren().add(new Text("Confirm
					// delete?"));
					// dialogVbox.getChildren().add(btn);
					// Scene dialogScene = new Scene(dialogVbox, 300, 200);
					// dialog.setScene(dialogScene);
					// dialog.show();
					deleteSelectedTask();
					break;
				default:
					taskSelectionListener();
					commandBox.requestFocus();
					commandBox.positionCaret(commandBox.getLength());
					break;
				}
			}

			private boolean isListViewSelectionEmpty() {
				return getActiveListView().getSelectionModel().isEmpty();
			}
		});
	}

	public String getInputCommand() {
		if (commandBox == null || isEmpty(commandBox.getText())) {
			return "";
		}
		return commandBox.getText();
	}

	private boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

	private void clearCommandBox() {
		commandBox.setText(null);
	}
}
