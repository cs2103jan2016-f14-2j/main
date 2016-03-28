package org.jimple.planner.ui;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.control.textfield.TextFields;
import org.jimple.planner.Constants;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UiController extends myObserver implements Initializable {
	
	protected UiPrompt prompt;
	protected UiListViewControl listViewControl;
	private LinkedList<String> cmdHistory;
	private int cmdHistoryPointer;
	protected static final Logger log= Logger.getLogger( UiController.class.getName() );
	Logic logic = new Logic();
	UiFormatter listFormatter = new UiFormatter();
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
	AnchorPane todayPane;
	
	@FXML
	AnchorPane upcomingPane;
	
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
		cmdHistory = new LinkedList<String>();
		cmdHistory.add("");
		prompt = new UiPrompt(this);
		listViewControl = new UiListViewControl(this);
		cmdHistoryPointer = 0;
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
		overlay.setVisible(false);
		assert !overlay.isVisible();
		log.log(Level.INFO,"initialising event listeners");
		commandBoxListener();
		tabPanesListener();
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
			cmdHistory.add(1, inputStr);
			clearCommandBox();
		}
	}	

	private void updateMessagePrompt(String output){
		messagePrompt.setText(output);
		fadeOut(5, messagePrompt);
	}

	/*======================================
	 * 
	 * LOADING/RELOADING TASK LIST:
	 * 
	========================================*/
	
	protected void loadDisplay() {
		loadMainTab();
		loadAgendaList();
		loadEventsList();
		loadDeadlinesList();
		loadTodoList();
		prompt = new UiPrompt(this);
		prompt.searchPrompt();
	}
	
	public void loadMainTab(){
//		listFormatter.fomatList(logic.getDeadlinesList(), logic.getEventsList());
		listFormatter.formatList(logic.getAgendaList(),Constants.TYPE_TODAY);
		todayPane.getChildren().clear();
//		todayPane.getChildren().add(listFormatter.getMainContent());
		ListView<Task> list = listFormatter.getFormattedList();
		list.setFocusTraversable(false);
		todayPane.getChildren().add(list);
	}
	
	public void loadAgendaList() {
		listFormatter.formatList(logic.getAgendaList(),Constants.TYPE_AGENDA);
		agendaContent.getChildren().clear();
		agendaContent.getChildren().add(listFormatter.getFormattedList());
	}

	public void loadEventsList() {
		listFormatter.formatList(logic.getEventsList(),Constants.TYPE_EVENT);
		eventsContent.getChildren().clear();
		eventsContent.getChildren().add(listFormatter.getFormattedList());
	}

	public void loadDeadlinesList() {
		listFormatter.formatList(logic.getDeadlinesList(),Constants.TYPE_DEADLINE);
		deadlinesContent.getChildren().clear();
		deadlinesContent.getChildren().add(listFormatter.getFormattedList());
	}

	public void loadTodoList()  {
		listFormatter.formatList(logic.getToDoList(),Constants.TYPE_TODO);
		todoContent.getChildren().clear();
		todoContent.getChildren().add(listFormatter.getFormattedList());
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
				switch(t.getCode()){
			    case ESCAPE:
					tabPanes.requestFocus();
					listViewControl.getActiveListView().requestFocus();
					if(overlay.isVisible())
						overlay.setVisible(false);
					break;
			    case UP:
					if(cmdHistoryPointer < cmdHistory.size() - 1)
						commandBox.setText(cmdHistory.get(++cmdHistoryPointer));
					commandBox.positionCaret(commandBox.getLength());
					t.consume();
					break;
			    case DOWN:
					if(cmdHistoryPointer > 0)
						commandBox.setText(cmdHistory.get(--cmdHistoryPointer));
					commandBox.positionCaret(commandBox.getLength());
					t.consume();
					break;
				default:
					cmdHistoryPointer = 0;
					break;
				}
			}
		});
	}

	public void taskSelectionListener(){
		listViewControl.getList(listViewControl.getCurrentTab()).setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.UP) {
					if (listViewControl.getCurrentTabItemIndex() == 0) {
						tabPanes.requestFocus();
						listViewControl.deselectTaskItem();
					}
				}
			}
		});
	}

	public void tabPanesListener() {
		tabPanes.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				listViewControl.deselectTaskItem();
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
					listViewControl.getActiveListView().requestFocus();
					if (listViewControl.isListViewSelectionEmpty())
						listViewControl.getActiveListView().getSelectionModel().select(0);
					break;
				case UP:
					taskSelectionListener();
					break;
				case LEFT:
					tabPanes.requestFocus();
					listViewControl.deselectTaskItem();
					break;
				case RIGHT:
					tabPanes.requestFocus();
					listViewControl.deselectTaskItem();
					break;
				case BACK_SPACE:
				case DELETE:
//					prompt.deletePrompt();
					listViewControl.deleteSelectedTask();
					break;
				default:
					taskSelectionListener();
					commandBox.requestFocus();
					commandBox.positionCaret(commandBox.getLength());
					break;
				}
			}
		});
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
	
	protected Node makeDraggable(final Node node) {
        final DragContext dragContext = new DragContext();
        final Group wrapGroup = new Group(node);
        
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
		case Constants.TYPE_EVENT:
			if(listViewControl.getCurrentTabName().equals("Jimple")){
				listViewControl.addAndReload(mainTab, index);
				break;
			}
			listViewControl.addAndReload(eventsTab,index);
			break;
		case Constants.TYPE_DEADLINE:
			if(listViewControl.getCurrentTabName().equals("Jimple")){
				listViewControl.addAndReload(mainTab, index);
				break;
			}
			listViewControl.addAndReload(deadlinesTab,index);
			break;
		case Constants.TYPE_TODO:
			listViewControl.addAndReload(todoTab,index);
			break;
		case Constants.TYPE_SEARCH:
			prompt.searchPrompt();
			overlay.setVisible(true);
			break;
		case Constants.TYPE_HELP:
			prompt.helpPrompt(feedback[0]);
			break;
		default:
			loadDisplay();
			break;
		}
		updateMessagePrompt(feedback[0]);
	}

}
