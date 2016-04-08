package org.jimple.planner.ui;
//@@author A0122498
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.control.textfield.TextFields;
import org.jimple.planner.constants.Constants;
import org.jimple.planner.logic.Logic;
import org.jimple.planner.observers.myObserver;
import org.jimple.planner.task.Task;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	public static boolean isSearch = false;
	public static boolean isConflictedShown = false;
	protected static final Logger log= Logger.getLogger( UiController.class.getName() );
	Logic logic;
	UiFormatter listFormatter = new UiFormatter();
	private final BooleanProperty dragModeActiveProperty =
            new SimpleBooleanProperty(this, "dragModeActive", true);

	@FXML
	TextField commandBox;

	@FXML
	Text textArea, helpContent;

	@FXML
	Label messagePrompt, clock,
	todayLabel, ongoingLabel, upcomingLabel,
	agendaLabel, todoLabel,
	searchLabel, conflictedLabel;
	
	@FXML
	VBox todayEmpty, ongoingEmpty, upcomingEmpty,
	agendaEmpty, todoEmpty, archiveEmpty, searchEmpty, conflictedEmpty;

	@FXML
	Tab mainTab, agendaTab, todoTab, archiveTab;

	@FXML
	TabPane tabPanes;
	
	@FXML
	AnchorPane mainController, mainContainer,
	todayPane, nowPane, upcomingPane,
	mainContent, agendaContent, todoContent, archiveContent,
	popupLayer, searchBox, searchContent, helpBox, conflictedBox, searchList, conflictedList;

	@FXML
	StackPane stackPane;

	@FXML
	ListView<String> list;

	@FXML
	VBox overlay;
	
	@FXML
	ImageView closeButton, searchCloseButton, helpCloseButton;



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		logic = new Logic();
		//asserts that FXML files initialises objects
		assert commandBox != null : "fx:id=\"commandBox\" was not injected: check your FXML file 'JimplUI.fxml'.";
		loadClock();
		cmdHistory = new LinkedList<String>();
		cmdHistory.add("");
		
		setEmptyListStrings();
		
		ImageView icon;
		int iconheight = 20;

		icon = new ImageView(new Image("jimpleTabIcon.png"));
		icon.setFitHeight(iconheight);
		icon.setPreserveRatio(true);
		mainTab.setGraphic(icon);
//		mainTab.setText("");
		icon = new ImageView(new Image("agendaTabIcon.png"));
		icon.setFitHeight(iconheight);
		icon.setPreserveRatio(true);
		agendaTab.setGraphic(icon);
//		agendaTab.setText("");
		icon = new ImageView(new Image("todoTabIcon.png"));
		icon.setFitHeight(iconheight);
		icon.setPreserveRatio(true);
		todoTab.setGraphic(icon);
//		todoTab.setText("");
		icon = new ImageView(new Image("archiveTabIcon.png"));
		icon.setFitHeight(iconheight);
		icon.setPreserveRatio(true);
		archiveTab.setGraphic(icon);
//		archiveTab.setText("");
		
		prompt = new UiPrompt(this);
		listViewControl = new UiListViewControl(this);
		cmdHistoryPointer = 0;
		System.out.println("initializing Jimple UI");
		logic.attach(this);
		
		TextFields.bindAutoCompletion(
                commandBox,
                Constants.STRING_ADD, Constants.STRING_EDIT,
                Constants.STRING_DELETE, Constants.STRING_SEARCH,
                Constants.STRING_UNDOTASK, Constants.STRING_HELP,
                Constants.STRING_CHANGEDIR, Constants.STRING_CHECKDIR);
		
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

	private void setEmptyListStrings() {
//		searchLabel.setText("nothing here");
	}

	private String getCurrentTime(){
		String currentTime = "";
		currentTime +=  LocalDateTime.now().getHour() + ":" +
		String.format("%02d", LocalDateTime.now().getMinute())  + " " + 
		LocalDateTime.now().getDayOfWeek() + ", " +
		LocalDateTime.now().getDayOfMonth() + " " +
		LocalDateTime.now().getMonth() + " " +
		LocalDateTime.now().getYear();
		return currentTime;
	}
	private void loadClock() {
		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {  
		     @Override  
		     public void handle(ActionEvent event) {  
		          clock.setText(getCurrentTime());
		          if(LocalDateTime.now().getSecond() == 0){
		        	  update();
		          }
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
		tabPanesListener();
		int i = listViewControl.getCurrentTabItemIndex();
		boolean cmb = commandBox.isFocused();
		int pos = commandBox.getCaretPosition();
		loadMainTab();
		loadAgendaList();
		loadTodoList();
		loadArchiveList();
		prompt = new UiPrompt(this);
		
		if(isSearch){
			prompt.searchPrompt();
		}
		
		if(isConflictedShown){
			prompt.conflictedPrompt();
		}
		
		listViewControl.selectIndex(i);
		listViewControl.getActiveListView().scrollTo(i);
		taskSelectionListener();
		if(cmb){
			commandBox.requestFocus();
			commandBox.positionCaret(pos);
		}
	}
	
	public void loadMainTab(){
		listFormatter.formatList(logic.getAgendaList(),Constants.TYPE_TODAY);
		ListView<Task> list = listFormatter.getFormattedList();
		todayPane.getChildren().clear();
		if(list != null){
			list.setFocusTraversable(false);
			todayPane.getChildren().add(list);
		}
		else {
			todayPane.getChildren().add(todayEmpty);
		}
		
		listFormatter.formatList(logic.getEventsList(),Constants.TYPE_NOW);
		list = listFormatter.getFormattedList();
		nowPane.getChildren().clear();
		if(list != null){
			list.setFocusTraversable(false);
			nowPane.getChildren().add(list);
		}
		else {
			nowPane.getChildren().add(ongoingEmpty);
		}
		
		listFormatter.formatList(logic.getDeadlinesList(),Constants.TYPE_UPCOMING);
		list = listFormatter.getFormattedList();
		upcomingPane.getChildren().clear();
		if(list != null){
			list.setFocusTraversable(false);
			upcomingPane.getChildren().add(list);
		}
		else {
			upcomingPane.getChildren().add(upcomingEmpty);
		}
	}
	
	public void loadAgendaList() {
		listFormatter.formatList(logic.getAgendaList(),Constants.TYPE_AGENDA);
		agendaContent.getChildren().clear();
		ListView<Task> list = listFormatter.getFormattedList();
		if(list != null){
			agendaContent.getChildren().add(list);
		}
		else {
			agendaContent.getChildren().add(agendaEmpty);
		}
	}
	
	public void loadArchiveList() {
		listFormatter.formatList(logic.getArchivedList(),Constants.TYPE_ARCHIVE);
		archiveContent.getChildren().clear();
		ListView<Task> list = listFormatter.getFormattedList();
		if(list != null){
			archiveContent.getChildren().add(list);
		}
		else {
			archiveContent.getChildren().add(archiveEmpty);
		}
	}


	public void loadTodoList()  {
		listFormatter.formatList(logic.getToDoList(),Constants.TYPE_TODO);
		todoContent.getChildren().clear();
		ListView<Task> list = listFormatter.getFormattedList();
		if(list != null){
			todoContent.getChildren().add(list);
		}
		else {
			todoContent.getChildren().add(todoEmpty);
		}
	}
	
	/*======================================
	 * 
	 * FXML LAYOUT RELATED:
	 * 
	========================================*/
	private void fadeOut(float sec, Node item){
		FadeTransition ft = new FadeTransition(Duration.millis(sec*500), item);
		FadeTransition ft2 = new FadeTransition(Duration.millis(sec*1000), item);
		ft.setFromValue(1.0);
		ft.setToValue(1.0);
		ft2.setFromValue(1.0);
		ft2.setToValue(0.0);
		ft.setCycleCount(1);
		ft.play();
		
		ft.setOnFinished(new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
		        ft2.play();
		    }
		});
	}
	
	public static void fitToAnchorPane(Node node) {
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
//					listViewControl.getActiveListView().requestFocus();
					if(overlay.isVisible()){
						overlay.setVisible(false);
						isSearch = false;
						isConflictedShown = false;
						commandBox.requestFocus();
						commandBox.positionCaret(commandBox.getLength());
					}
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
				switch(t.getCode()){
				case UP: 
					if (listViewControl.getCurrentTabItemIndex() == 0) {
						listViewControl.selectIndex(-1);
						tabPanes.requestFocus();
						listViewControl.deselectTaskItem();
						}
					break;
//				case LEFT:
//				case RIGHT:
//					break;
				case SPACE:
					if(agendaTab.isSelected())
						listViewControl.selectFirstIncompleteTask();
					break;
				case ESCAPE:
					tabPanes.requestFocus();
					listViewControl.deselectTaskItem();
					break;
				default:
					break;
				}
			}
		});
	}

	public void tabPanesListener() {
		tabPanes.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent t) {				
				if (t.getCode().isArrowKey()){
					taskSelectionListener();
				}

				switch (t.getCode()) {
				case DOWN:
					listViewControl.getActiveListView().requestFocus();
					if (listViewControl.isListViewSelectionEmpty())
						listViewControl.getActiveListView().getSelectionModel().select(0);
					break;
				case UP:
					listViewControl.getActiveListView().requestFocus();
					taskSelectionListener();
					t.consume();
					break;
				case LEFT:
				case RIGHT:
					tabPanes.requestFocus();
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

	
	@FXML
	private void searchCloseButtonAction(){
		popupLayer.getChildren().clear();
		isSearch = false;
		isConflictedShown = false;
		overlay.setVisible(false);
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
		System.out.println("feedback[1] = "+feedback[1]);
		switch (tab) {
		case Constants.TYPE_EVENT:
		case Constants.TYPE_DEADLINE:
			listViewControl.updateAndReload(agendaTab,index);
			break;
		case Constants.TYPE_TODO:
			listViewControl.updateAndReload(todoTab,index);
			break;
		case Constants.TYPE_ARCHIVE:
//			listViewControl.updateAndReload(archiveTab,index);
			loadDisplay();
			break;
		case Constants.TYPE_SEARCH:
			prompt.searchPrompt();
			break;
		case Constants.TYPE_CONFLICTED:
			prompt.conflictedPrompt();
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
