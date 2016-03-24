package org.jimple.planner.ui;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.control.textfield.TextFields;
import org.jimple.planner.Task;
import org.jimple.planner.logic.Logic;
import org.jimple.planner.observers.myObserver;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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

public class Controller extends myObserver implements Initializable {
	
	private static final String TYPE_HELP = "help";
	private static final String TYPE_SEARCH = "search";
	private static final String TYPE_TODO = "floating";
	private static final String TYPE_DEADLINE = "deadline";
	private static final String TYPE_EVENT = "event";
	private static final String TYPE_AGENDA = "agenda";
	
	private static final Logger log= Logger.getLogger( Controller.class.getName() );
	Logic logic = new Logic();
	ListViewFormatter listFormatter = new ListViewFormatter();
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
	Tab mainTab;
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
	AnchorPane mainContent;
	
	@FXML
	AnchorPane mainContainer;
	
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
	
	@FXML
	Label clock;
	
	@FXML
	ImageView closeButton;



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//asserts that FXML files initialises objects
		assert commandBox != null : "fx:id=\"commandBox\" was not injected: check your FXML file 'JimplUI.fxml'.";
		loadClock(); 
		System.out.println("initializing Jimple UI");
		logic.attach(this);
		TextFields.bindAutoCompletion(
                commandBox,
                "add ", "edit ", "delete ", "search ", "help", "changedir", "checkdir");
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				commandBox.requestFocus();
			}
		});
//		stackPane.setId("main");
		overlay.setVisible(false);
		assert !overlay.isVisible();
//		assert false;
		log.log(Level.INFO,"initialising event listeners");
		commandBoxListener();
		tabPanesListener();
//		loadDisplay();
		update();
	 }

	private void loadClock() {
		final DateFormat format = SimpleDateFormat.getInstance();  
		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {  
		     @Override  
		     public void handle(ActionEvent event) {  
		          final Calendar cal = Calendar.getInstance();  
		          clock.setText(format.format(cal.getTime()));  
		     }  
		}));  
		timeline.setCycleCount(Animation.INDEFINITE);  
		timeline.play();
	}
	
	public void enterTriggered() throws IOException {
		String inputStr = getInputCommand();
		if (!isEmpty(inputStr)) {
			logic.execute(inputStr);
			clearCommandBox();
		}
	}

	/*======================================
	 * 
	 * LOADING/RELOADING TASK LIST:
	 * 
	========================================*/
	
	private void loadDisplay() {
		loadMainTab();
		loadAgendaList();
		loadEventsList();
		loadDeadlinesList();
		loadTodoList();
		loadSearchList();
	}
	
	@SuppressWarnings("rawtypes")
	public void loadMainTab(){
		listFormatter.fomatList(logic.getDeadlinesList(), logic.getEventsList());
		mainContent.getChildren().clear();
		ListView emptyList = new ListView();
		emptyList.setPrefSize(0, 0);
		emptyList.setVisible(false);
		mainContent.getChildren().add(emptyList);
		mainContent.getChildren().add(listFormatter.getMainContent());
	}
	
	public void loadAgendaList() {
		listFormatter.formatList(logic.getAgendaList(),TYPE_AGENDA);
		agendaContent.getChildren().clear();
		agendaContent.getChildren().add(listFormatter.getFormattedList());
	}

	public void loadEventsList() {
		listFormatter.formatList(logic.getEventsList(),TYPE_EVENT);
		eventsContent.getChildren().clear();
		eventsContent.getChildren().add(listFormatter.getFormattedList());
	}

	public void loadDeadlinesList() {
		listFormatter.formatList(logic.getDeadlinesList(),TYPE_DEADLINE);
		deadlinesContent.getChildren().clear();
		deadlinesContent.getChildren().add(listFormatter.getFormattedList());
	}

	public void loadTodoList()  {
		listFormatter.formatList(logic.getToDoList(),TYPE_TODO);
		todoContent.getChildren().clear();
		todoContent.getChildren().add(listFormatter.getFormattedList());
	}

	/*======================================
	 * 
	 * TASK LIST DISPLAY CONTROLS:
	 * 
	========================================*/
	private void updateMessagePrompt(String output){
		messagePrompt.setText(output);
		fadeOut(5, messagePrompt);
	}
	
	private void selectTaskAtIndex(int index){
		for(int i=0; i<getActiveListView().getItems().size(); i++)
			if(getActiveListView().getItems().get(i).getTaskId() == index)
				selectIndex(i);
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
			log.log(Level.INFO, "attempting to delete selected task");
			int selectedIndex = getCurrentTabItemIndex();
			if (selectedIndex == -1)
				return;
			if(getSelectedListItem().getType().equals("static"))
				return;
			logic.execute("delete " + getSelectedListItem().getTaskId());
			updatePointer(selectedIndex);
		} catch (IOException e) {
			log.log(Level.WARNING, "IO exception. delete not successful", e);
		}
	}

	private Task getSelectedListItem() {
		return getActiveListView().getSelectionModel().getSelectedItem();
	}
	
	private void addAndReload(Tab tab, int index) {
		if(overlay.isVisible()){
			overlay.setVisible(false);
		}
		loadDisplay();
		tabPanes.getSelectionModel().select(tab);
		selectTaskAtIndex(index);
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
				if (t.getCode() == KeyCode.ESCAPE){
					tabPanes.requestFocus();
					
					if(overlay.isVisible()){
						overlay.setVisible(false);
					}
				}			
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
	
	private void loadSearchList() {
		listFormatter.formatList(logic.getSearchList(),TYPE_SEARCH);
		ListView<Task> listView = listFormatter.getFormattedList();
		listView.setPrefSize(stackPane.getWidth()/2, stackPane.getHeight()/2);
		//		fitToAnchorPane(listView);
		
		Pane popup = new Pane();
		VBox dialogVbox = new VBox(10);
		HBox dialogHbox = new HBox(10);
		Button closebtn = new Button("x");
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		closebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				popupLayer.getChildren().clear();
				overlay.setVisible(false);
			}
		});
		HBox closeBar = new HBox(new Text("Search Results"),spacer,closebtn);
		dialogVbox.getChildren().add(closeBar);
		dialogVbox.getChildren().add(listView);
		dialogVbox.getChildren().add(dialogHbox);
		dialogVbox.setPadding(new Insets(10));
		popup.getChildren().add(dialogVbox);
		popup.getStyleClass().add("popup");
		if(!popupLayer.getChildren().isEmpty())
			popupLayer.getChildren().clear();
		popupLayer.getChildren().add(makeDraggable(popup));
//		overlay.setVisible(true);
//		closebtn.requestFocus();
	}	
	
private void helpPrompt(String helpStrings) {
		Pane popup = new Pane();
		VBox dialogVbox = new VBox(10);
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Button closebtn = new Button("x");
		closebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				popupLayer.getChildren().clear();
				overlay.setVisible(false);
			}
		});
		HBox closeBar = new HBox(new Text("Help Sheet"),spacer,closebtn);
		
		dialogVbox.getChildren().add(closeBar);
		dialogVbox.getChildren().add(new Text(helpStrings));
		dialogVbox.setPadding(new Insets(10));
		popup.getChildren().add(dialogVbox);
		popup.getStyleClass().add("popup");
//		popupLayer.getChildren().add(popup);
		if(!popupLayer.getChildren().isEmpty())
			popupLayer.getChildren().clear();
		popupLayer.getChildren().add(makeDraggable(popup));
		overlay.setVisible(true);
//		closebtn.requestFocus();		
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
	@FXML
	private void closeButtonAction(){
	    // get a handle to the stage
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
	
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
	
	@Override
	public void update() {
		loadDisplay();
	}	

	@Override
	public void update(String[] feedback) {
		int index = 0;
		if(feedback[1].matches(".*\\d+.*")) //if string contains digits
		 index = Integer.parseInt(feedback[1].replaceAll("[^\\d.]", ""));
		String tab = feedback[1].replaceAll("[0-9]","");
		switch (tab) {
		case TYPE_EVENT:
			addAndReload(eventsTab,index);
			break;
		case TYPE_DEADLINE:
			addAndReload(deadlinesTab,index);
			break;
		case TYPE_TODO:
			addAndReload(todoTab,index);
			break;
		case TYPE_SEARCH:
			overlay.setVisible(true);
			loadSearchList();
			break;
		case TYPE_HELP:
			helpPrompt(feedback[0]);
			break;
		default:
			loadDisplay();
			break;
		}
		updateMessagePrompt(feedback[0]);
	}

}
