package org.jimple.planner.ui;

import java.io.IOException;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UiPrompt extends UiController{
	@FXML
	AnchorPane searchBox, searchContent;	
	FXMLLoader fxmlLoader;
	
	protected UiController controller;
	
	
	public UiPrompt(UiController controller){
		this.controller = controller;
	}
	
	public void setController(UiController controller){
		this.controller = controller;
	}
	
	public void deletePrompt() {
		Pane popup = new Pane();
		VBox dialogVbox = new VBox(10);
		HBox dialogHbox = new HBox(10);
		Button deletebtn = new Button();
		Button closebtn = new Button();
		deletebtn.setText("delete");
		deletebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				listViewControl.deleteSelectedTask();
				controller.popupLayer.getChildren().clear();
				controller.overlay.setVisible(false);
			}
		});
		closebtn.setText("cancel");
		closebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.popupLayer.getChildren().clear();
				controller.overlay.setVisible(false);
			}
		});
		dialogVbox.getChildren().add(new Text("Confirm delete?"));
		dialogHbox.getChildren().add(deletebtn);
		dialogHbox.getChildren().add(closebtn);
		dialogVbox.getChildren().add(dialogHbox);
		dialogVbox.setPadding(new Insets(10));
		popup.getChildren().add(dialogVbox);
		popup.getStyleClass().add(Constants.TYPE_POPUP);
		controller.popupLayer.getChildren().clear();
		controller.popupLayer.getChildren().add(popup);
		controller.overlay.setVisible(true);
		deletebtn.requestFocus();
	}
	

	protected void helpPrompt(String helpStrings) {
		Pane popup = new Pane();
		VBox dialogVbox = new VBox(10);
		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Button closebtn = new Button("x");
		closebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				controller.popupLayer.getChildren().clear();
				controller.overlay.setVisible(false);
			}
		});
		HBox closeBar = new HBox(new Text("Help Sheet"),spacer,closebtn);
		
		dialogVbox.getChildren().add(closeBar);
		dialogVbox.getChildren().add(new Text(helpStrings));
		dialogVbox.setPadding(new Insets(10));
		popup.getChildren().add(dialogVbox);
		popup.getStyleClass().add(Constants.TYPE_POPUP);
//		popupLayer.getChildren().add(popup);
		controller.popupLayer.getChildren().clear();
		controller.popupLayer.getChildren().add(makeDraggable(popup));
		controller.overlay.setVisible(true);
//		closebtn.requestFocus();		
	}
	
	protected void searchPrompt() {
		controller.listFormatter.formatList(controller.logic.getSearchList(),Constants.TYPE_SEARCH);
		ListView<Task> listView = controller.listFormatter.getFormattedList();
		listView.setPrefSize(controller.stackPane.getWidth()/2, controller.stackPane.getHeight()/2);

		controller.searchContent.getChildren().clear();
		controller.searchContent.getChildren().add(listView);
		controller.popupLayer.getChildren().clear();
		controller.popupLayer.getChildren().add(makeDraggable(controller.searchBox));
//		controller.popupLayer.getChildren().add(makeDraggable(searchBox));
//		overlay.setVisible(true);
//		closebtn.requestFocus();
	}	
}
