package org.jimple.planner;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
	}
	
	public void enterTriggered() throws IOException {
		String inputStr = getInputCommand();
		if (!isEmpty(inputStr)) {
			// System.out.println(inputStr);
			String messageOutput = logic.execute(inputStr);
			System.out.println(messageOutput);
			messagePrompt.setText(messageOutput);
			
			FadeTransition ft = new FadeTransition(Duration.millis(3000), messagePrompt);
			ft.setFromValue(1.0);
			ft.setToValue(0.0);
			ft.setCycleCount(1);
			ft.play();

			clearCommandBox();
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
