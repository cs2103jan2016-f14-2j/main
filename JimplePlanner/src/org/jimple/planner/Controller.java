package org.jimple.planner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
	TabPane tabPanes;
	
	@FXML
	AnchorPane agendaContent;
	
	@FXML
	AnchorPane eventsContent;
	
	@FXML
	AnchorPane deadlinesContent;
	
	@FXML
	AnchorPane todoContent;
	
//	@FXML
//	ListView<Task> listView;
	
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
		updateDisplay();
	}

	private void updateDisplay() {
		loadAgendaList();
		loadEventsList();
		loadDeadlinesList();
		loadTodoList();
	}

    public void loadAgendaList() {
    	
    	//SAMPLE DATA=======================================================
//    	ArrayList<Task> taskList = new ArrayList<Task>();
//    	Task task = new Task("Finish 2103T homework");
//    	task.setToDate("2007-12-03T10:15:30");
//    	taskList.add(task);
//    	taskList.add(new Task("eat food"));
//    	taskList.add(new Task("make food"));
//    	taskList.add(new Task("buy food"));
//    	taskList.add(new Task("take food"));
    	//==================================================================
    	ArrayList<Task> taskList = logic.display("events");
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
                            		new Text(String.format("from: %s", item.getFromTime())),
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
        agendaContent.getChildren().add(listView);
    }

    public void loadEventsList() {
    	ArrayList<Task> taskList = logic.display("events");
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
                            		new Text(String.format("from: %s", item.getFromTime())),
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
    	eventsContent.getChildren().add(listView);
    }

    public void loadDeadlinesList() {
    	ArrayList<Task> taskList = logic.display("deadlines");
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
                            		new Text(String.format("from: %s", item.getFromTime())),
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
    	deadlinesContent.getChildren().add(listView);
    }
    
    public void loadTodoList() {
    	ArrayList<Task> taskList = logic.display("floating");
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
                            HBox hBox = new HBox(new Text(String.format("#%d",super.getIndex())),vBox);
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                        }
                    }
                };
            }

        });
        fitToAnchorPane(listView);
    	todoContent.getChildren().add(listView);
    }
    
    public void fitToAnchorPane(Node node){
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
    }
    
	public void enterTriggered() throws IOException {
		String inputStr = getInputCommand();
		if (!isEmpty(inputStr)) {
			// System.out.println(inputStr);
			String[] feedbackArr = logic.execute(inputStr);
			String messageOutput = feedbackArr[0];
			messagePrompt.setText(messageOutput);
			
			FadeTransition ft = new FadeTransition(Duration.millis(3000), messagePrompt);
			ft.setFromValue(1.0);
			ft.setToValue(0.0);
			ft.setCycleCount(1);
			ft.play();

			clearCommandBox();
			updateDisplay();
		}
	}
	
	public void commandBoxListener(){		
		commandBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
		        @Override
		        public void handle(KeyEvent t) {
		            if(t.getCode() == KeyCode.ESCAPE)
		            	tabPanes.requestFocus();
		        }
		    });		
	}
	
	public void tabPanesListener(){		
		tabPanes.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
		        @Override
		        public void handle(KeyEvent t) {
		        	if(!t.getCode().isArrowKey()){
		        		commandBox.requestFocus();
		        		commandBox.positionCaret(commandBox.getLength());
		        	}
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
