package org.jimple.planner;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class Controller implements Initializable{
	
	@FXML
    TextField commandBox;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		assert commandBox != null : "fx:id=\"synopsis\" was not injected: check your FXML file 'IssueTrackingLite.fxml'.";
		
		System.out.println("initializing Jimple UI");
	}
	
	public void enterButtonClicked(){
		String inputStr = getInputCommand();
		if(!isEmpty(inputStr)){
			System.out.println(inputStr);
			clearCommandBox();
		}
	}

	
	public String getInputCommand(){
        if (commandBox == null || isEmpty(commandBox.getText())) {
            return "";
        }
        return commandBox.getText();
	}
	
	private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
	
	private void clearCommandBox(){
		commandBox.setText(null);
	}
}
