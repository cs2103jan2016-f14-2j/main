//@@author A0122498Y
package org.jimple.planner.ui;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UiController extends myObserver implements Initializable {
	
	protected UiPrompt prompt;
	protected UiListViewControl listViewControl;
	protected LinkedList<String> cmdHistory;
	protected int cmdHistoryPointer;	
	public static boolean isSearch = false;
	public static boolean isConflictedShown = false;
	protected static final Logger log= Logger.getLogger( UiController.class.getName() );
	
	Logic logic;
	UiFormatter listFormatter = new UiFormatter();

	@FXML
	AnchorPane mainController, mainContainer,
	todayPane, nowPane, upcomingPane,
	mainContent, agendaContent, todoContent, archiveContent,
	popupLayer, searchBox, searchContent, helpBox, conflictedBox, searchList, conflictedList;
	@FXML
	ImageView closeButton, searchCloseButton, helpCloseButton;
	@FXML
	Label messagePrompt, clock,
	todayLabel, ongoingLabel, upcomingLabel,
	agendaLabel, todoLabel,
	searchLabel, conflictedLabel, archiveLabel;
	@FXML
	StackPane stackPane;
	@FXML
	Tab mainTab, agendaTab, todoTab, archiveTab;
	@FXML
	TabPane tabPanes;
	@FXML
	Text textArea, helpContent;
	@FXML
	TextField commandBox;

	@FXML
	VBox overlay, todayEmpty, ongoingEmpty, upcomingEmpty,
	agendaEmpty, todoEmpty, archiveEmpty, searchEmpty, conflictedEmpty;



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println(Constants.MESSAGE_INITIALIZE);
		logic = new Logic();
		
		//asserts that FXML files initialises JavaFX objects
		assert commandBox != null : Constants.ERROR_FXML_INITIALIZE;
		loadClock();
		
		//initialise command history
		cmdHistory = new LinkedList<String>();
		cmdHistory.add("");
		cmdHistoryPointer = 0;
		
		setFlavourText();		
		setTabIcons();
		
		prompt = new UiPrompt(this);
		listViewControl = new UiListViewControl(this);
		logic.attach(this);
		
		//Auto-complete strings for command box
		TextFields.bindAutoCompletion(
                commandBox,
                Constants.STRING_ADD, Constants.STRING_EDIT,
                Constants.STRING_DELETE, Constants.STRING_SEARCH,
                Constants.STRING_UNDOTASK, Constants.STRING_HELP,
                Constants.STRING_CHANGEDIR, Constants.STRING_CHECKDIR,
                Constants.STRING_CHECKCONFLICT, Constants.STRING_EDITLABEL);
		
		//Set focus to command box on start up
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				commandBox.requestFocus();
			}
		});
		
		overlay.setVisible(false);
		assert !overlay.isVisible();
		log.log(Level.INFO,"initialising event listeners");
		
		//initialise listeners
		commandBoxListener();
		tabPanesListener();
		
		//load content
		update();
	 }

	protected void setTabIcons() {
		ImageView icon;
		int iconheight = 20;

		icon = new ImageView(new Image(Constants.ICON_TAB_JIMPLE));
		icon.setFitHeight(iconheight);
		icon.setPreserveRatio(true);
		mainTab.setGraphic(icon);

		icon = new ImageView(new Image(Constants.ICON_TAB_AGENDA));
		icon.setFitHeight(iconheight);
		icon.setPreserveRatio(true);
		agendaTab.setGraphic(icon);

		icon = new ImageView(new Image(Constants.ICON_TAB_TODO));
		icon.setFitHeight(iconheight);
		icon.setPreserveRatio(true);
		todoTab.setGraphic(icon);

		icon = new ImageView(new Image(Constants.ICON_TAB_ARCHIVE));
		icon.setFitHeight(iconheight);
		icon.setPreserveRatio(true);
		archiveTab.setGraphic(icon);
	}

	protected void setFlavourText() {
		//@@author A0135775W
		searchLabel.setText("Um, we found nothing.");
		conflictedLabel.setText("Zero conflicts!");
		todayLabel.setText("A free day for you!");
		ongoingLabel.setText("Nothing happening right now.");
		upcomingLabel.setText("No deadlines to worry about!");
	    agendaLabel.setText("No more events and deadlines! Time to relax.");
	    todoLabel.setText("Nothing left on your to-do list! Grab yourself a drink.");
	    archiveLabel.setText("Your archive is empty! Oh look, tumbleweed!");
	}

	//@@author A0122498Y
	protected String getCurrentTime(){
		String currentTime = "";
		currentTime +=  LocalDateTime.now().getHour() + ":" +
		String.format("%02d", LocalDateTime.now().getMinute())  + " " + 
		LocalDateTime.now().getDayOfWeek() + ", " +
		LocalDateTime.now().getDayOfMonth() + " " +
		LocalDateTime.now().getMonth() + " " +
		LocalDateTime.now().getYear();
		return currentTime;
	}
	
	protected void loadClock() {
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

	protected void updateMessagePrompt(String output){
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
	
	protected void loadMainTab(){
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
	
	protected void loadAgendaList() {
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
	
	protected void loadArchiveList() {
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

	protected void loadTodoList()  {
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
	
	protected void fadeOut(float sec, Node item){
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
	
	protected void commandBoxListener() {
		commandBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				switch(t.getCode()){
			    case ESCAPE:
					tabPanes.requestFocus();
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

	protected void taskSelectionListener(){
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

	protected void tabPanesListener() {
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
	
	protected String getInputCommand() {
		if (commandBox == null || isEmpty(commandBox.getText())) {
			return "";
		}
		return commandBox.getText();
	}

	protected boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

	protected void clearCommandBox() {
		commandBox.setText(null);
	}
	
	/*======================================
	 * 
	 * UI INTERACTION:
	 * 
	========================================*/
	
	@FXML
	protected void closeButtonAction(){
	    Stage stage = (Stage) closeButton.getScene().getWindow();
	    stage.close();
	}

	@FXML
	protected void popupCloseButtonAction(){
		popupLayer.getChildren().clear();
		isSearch = false;
		isConflictedShown = false;
		overlay.setVisible(false);
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
		case Constants.TYPE_DEADLINE:
			listViewControl.updateAndReload(agendaTab,index);
			break;
		case Constants.TYPE_TODO:
			listViewControl.updateAndReload(todoTab,index);
			break;
		case Constants.TYPE_ARCHIVE:
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
