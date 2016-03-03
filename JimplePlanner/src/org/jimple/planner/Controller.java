package org.jimple.planner;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	}
	
	public void enterButtonClicked() throws IOException {
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
