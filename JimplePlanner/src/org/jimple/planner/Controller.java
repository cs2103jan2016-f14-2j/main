package org.jimple.planner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.jimple.planner.logic.Logic;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class Controller implements Initializable {
	Logic logic = new Logic();
	private final BooleanProperty dragModeActiveProperty =
            new SimpleBooleanProperty(this, "dragModeActive", true);

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

	@FXML
	ListView<String> list;

	@FXML
	AnchorPane popupLayer;

	@FXML
	VBox overlay;

	
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
		overlay.setVisible(false);
		commandBoxListener();
		tabPanesListener();
		loadDisplay();
		//taskSelectionListener();
	}
	
	public void enterTriggered() throws IOException {
		String inputStr = getInputCommand();
		if (!isEmpty(inputStr)) {
			String[] feedback = logic.execute(inputStr);
			messagePrompt.setText(feedback[0]);
			fadeOut(5, messagePrompt);
			
			clearCommandBox();

			String displayType = feedback[1];

			switch (displayType) {
			case "event":
				addAndReload(eventsTab);
				break;
			case "deadline":
				addAndReload(deadlinesTab);
				break;
			case "floating":
				addAndReload(todoTab);
				break;
			case "search":
//				showSearch(feedback[0]);
				searchPrompt(feedback[0]);
				break;
			default:
				loadDisplay();
				break;
			}
		}
	}

	/*======================================
	 * 
	 * LOADING/RELOADING TASK LIST:
	 * 
	========================================*/
	
	private void loadDisplay() {
		loadAgendaList();
		loadEventsList();
		loadDeadlinesList();
		loadTodoList();
	}

	public void loadAgendaList() {
		ArrayList<Task> taskList = new ArrayList<Task>(logic.display("deadline"));
		taskList.addAll(logic.display("event"));
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
								hBox = new HBox(
										new Text(String.format("#%d",
												super.getIndex() + logic.display("floating").size())),
										vBox, new Text(String.format("by %s %s", item.getPrettyToDate(), item.getPrettyToTime())));
							} else {
								t.setId("event");
								vBox = new VBox(t, new Text(String.format("from: %s %s", item.getPrettyFromDate(), item.getPrettyFromTime())),
										new Text(String.format("to: %s %s", item.getPrettyToDate(), item.getPrettyToTime())));
								hBox = new HBox(new Text(
										String.format("#%d", super.getIndex() + logic.display("floating").size())),
										vBox);
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
//		Task staticDate = new Task("Today");
//		staticDate.setType("static");
//		taskList.add(staticDate);
		ArrayList<Task> taskList = new ArrayList<Task>();
		taskList.addAll(logic.display("event"));

		ObservableList<Task> data = FXCollections.observableArrayList();
		data.addAll(taskList);
		// data.add();
		ListView<Task> listView = new ListView<Task>(data);
		listView.setItems(data);
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
								this.setDisable(true);
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
								HBox hBox = new HBox(new Text(String.format("#%d", super.getIndex()
										+ logic.display("floating").size() + logic.display("deadline").size())), vBox);
								hBox.setSpacing(10);
								setGraphic(hBox);
							}
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
		ArrayList<Task> taskList = new ArrayList<Task>(logic.display("deadline"));
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
							VBox vBox = new VBox(t,
									new Text(String.format("by: %s %s", item.getPrettyToDate(), item.getPrettyToTime())));
							HBox hBox = new HBox(
									new Text(String.format("#%d", super.getIndex() + logic.display("floating").size())),
									vBox);
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

	/*======================================
	 * 
	 * TASK LIST DISPLAY CONTROLS:
	 * 
	========================================*/
	
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

	public void updatePointer(int num) {
		System.out.println(getActiveListView().getItems().size());
		if (num >= getActiveListView().getItems().size())
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

	private void deleteSelectedTask() {
		try {
			System.out.println(getCurrentTabName());
			System.out.println(getCurrentTabItemIndex());
			int selectedIndex = getCurrentTabItemIndex();
			if (selectedIndex == -1)
				return;
			if(getActiveListView().getSelectionModel().getSelectedItem().getType().equals("static"))
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
						+ logic.display("deadline").size()));
				break;

			case "Agenda":
				System.out.println("deleting from agenda");
				logic.execute("delete " + (selectedIndex + logic.display("floating").size()));
				break;

			default:
				break;
			}
			loadDisplay();
			updatePointer(selectedIndex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addAndReload(Tab tab) {
		loadDisplay();
		tabPanes.getSelectionModel().select(tab);
		switch (tab.getText()) {
		case "Agenda":
		case "Events":
		case "Deadlines":
			// selectIndex(num);
			// implementation for Brandon: just search for input
			break;
		case "To-do":
			selectLastItem();
			break;
		default:
			break;
		}

	}

	private void selectIndex(int num) {
		getActiveListView().requestFocus();
		getActiveListView().getSelectionModel().select(num);
		getActiveListView().scrollTo(getActiveListView().getSelectionModel().getSelectedIndex());
	}

	private void selectLastItem() {
		getActiveListView().requestFocus();
		getActiveListView().getSelectionModel().selectLast();
		getActiveListView().scrollTo(getActiveListView().getSelectionModel().getSelectedIndex());
	}

	@SuppressWarnings("unchecked")
	private ListView<Task> getList(Tab tab) {
		return (ListView<Task>) ((Pane) tab.getContent()).getChildren().get(0);
	}

	@SuppressWarnings("unchecked")
	private ListView<Task> getActiveListView() {
		return (ListView<Task>) ((Pane) tabPanes.getSelectionModel().getSelectedItem().getContent()).getChildren()
				.get(0);
	}

	
	
	/*======================================
	 * 
	 * FXML LAYOUT RELATED:
	 * 
	========================================*/
	private void fadeOut(float sec, Node item) {
		FadeTransition ft = new FadeTransition(Duration.millis(sec*1000), item);
		ft.setFromValue(1.0);
		ft.setToValue(0);
		ft.setCycleCount(1);
		ft.play();
	}
	
	public void fitToAnchorPane(Node node) {
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
	}
	
	/*======================================
	 * 
	 * KEYBOARD LISTENERS:
	 * 
	========================================*/
	
	public void commandBoxListener() {
		commandBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ESCAPE)
					tabPanes.requestFocus();
			}
		});
	}

	public void taskSelectionListener(){
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
//					deletePrompt();
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

	
	/*======================================
	 * 
	 * POPUP DIALOGUE BOXES:
	 * 
	========================================*/

	private void deletePrompt() {
		Pane popup = new Pane();
		VBox dialogVbox = new VBox(10);
		HBox dialogHbox = new HBox(10);
		Button deletebtn = new Button();
		Button closebtn = new Button();
		deletebtn.setText("delete");
		deletebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				deleteSelectedTask();
				popupLayer.getChildren().clear();
				overlay.setVisible(false);
			}
		});
		closebtn.setText("cancel");
		closebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				popupLayer.getChildren().clear();
				overlay.setVisible(false);
			}
		});
		dialogVbox.getChildren().add(new Text("Confirm delete?"));
		dialogHbox.getChildren().add(deletebtn);
		dialogHbox.getChildren().add(closebtn);
		dialogVbox.getChildren().add(dialogHbox);
		dialogVbox.setPadding(new Insets(10));
		popup.getChildren().add(dialogVbox);
		popup.getStyleClass().add("popup");
		popupLayer.getChildren().add(popup);
		overlay.setVisible(true);
		deletebtn.requestFocus();
	}
	
	private void searchPrompt(String searchStr) {

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
							VBox vBox = new VBox(t);
							HBox hBox = new HBox(new Text(String.format("#%d", super.getIndex())), vBox);
							hBox.setSpacing(10);
							setGraphic(hBox);
						}
					}
				};
			}

		});
		listView.setPrefSize(stackPane.getWidth()/2, stackPane.getHeight()/2);
		//		fitToAnchorPane(listView);
		
		Pane popup = new Pane();
		VBox dialogVbox = new VBox(10);
		HBox dialogHbox = new HBox(10);
		Button deletebtn = new Button();
		Button closebtn = new Button();
		deletebtn.setText("delete");
		deletebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				deleteSelectedTask();
				popupLayer.getChildren().clear();
				overlay.setVisible(false);
			}
		});
		closebtn.setText("cancel");
		closebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				popupLayer.getChildren().clear();
				overlay.setVisible(false);
			}
		});
		dialogVbox.getChildren().add(new Text("Search Results"));
		dialogHbox.getChildren().add(deletebtn);
		dialogHbox.getChildren().add(closebtn);
		dialogVbox.getChildren().add(listView);
		dialogVbox.getChildren().add(dialogHbox);
		dialogVbox.setPadding(new Insets(10));
		popup.getChildren().add(dialogVbox);
		popup.getStyleClass().add("popup");
//		popup.setPrefSize(300, 500);
		popupLayer.getChildren().add(popup);
//		popupLayer.getChildren().add(makeDraggable(popup));
		overlay.setVisible(true);
		deletebtn.requestFocus();
		
		
//		popup.getChildren().clear();
//		popup.getChildren().add(listView);
	}	
	
	/*======================================
	 * 
	 * COMMAND BOX RELATED:
	 * 
	========================================*/
	
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
	
	/*======================================
	 * 
	 * UI INTERACTION:
	 * 
	========================================*/
	
	private static final class DragContext {
        public double mouseAnchorX;
        public double mouseAnchorY;
        public double initialTranslateX;
        public double initialTranslateY;
    }
	
	private Node makeDraggable(final Node node) {
        final DragContext dragContext = new DragContext();
        final Group wrapGroup = new Group(node);

//        wrapGroup.addEventFilter(
//                MouseEvent.ANY,
//                new EventHandler<MouseEvent>() {
//                    public void handle(final MouseEvent mouseEvent) {
//                        if (dragModeActiveProperty.get()) {
//                            // disable mouse events for all children
//                            mouseEvent.consume();
//                        }
//                    }
//                });
//
        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {
                        if (dragModeActiveProperty.get()) {
                            // remember initial mouse cursor coordinates
                            // and node position
                            dragContext.mouseAnchorX = mouseEvent.getX();
                            dragContext.mouseAnchorY = mouseEvent.getY();
                            dragContext.initialTranslateX =
                                    node.getTranslateX();
                            dragContext.initialTranslateY =
                                    node.getTranslateY();
                        }
                    }
                });

        wrapGroup.addEventFilter(
                MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent mouseEvent) {
                        if (dragModeActiveProperty.get()) {
                            // shift node from its initial position by delta
                            // calculated from mouse cursor movement
                            node.setTranslateX(
                                    dragContext.initialTranslateX
                                        + mouseEvent.getX()
                                        - dragContext.mouseAnchorX);
                            node.setTranslateY(
                                    dragContext.initialTranslateY
                                        + mouseEvent.getY()
                                        - dragContext.mouseAnchorY);
                        }
                    }
                });
                
        return wrapGroup;
    }	
}
