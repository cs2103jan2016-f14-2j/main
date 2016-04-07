package org.jimple.planner.ui;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
//@@author A0122498
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
		controller.helpContent.setText(helpStrings);		
		controller.popupLayer.getChildren().clear();
		controller.popupLayer.getChildren().add(controller.helpBox);
		controller.overlay.setVisible(true);
	}
	
	protected void searchPrompt() {
		UiController.isSearch = true;
		controller.listFormatter.formatList(controller.logic.getSearchList(),Constants.TYPE_SEARCH);
		ListView<Task> listView = controller.listFormatter.getFormattedList();
//		listView.setPrefSize(controller.stackPane.getWidth()/2, controller.stackPane.getHeight()/2);

//		controller.searchContent.getChildren().clear();
//		controller.searchContent.getChildren().add(listView);
		controller.searchList.getItems().clear();
		controller.searchList.getItems().addAll(listView.getItems());
		if (controller.searchList != null) {
			controller.searchList.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
				@Override
				public ListCell<Task> call(ListView<Task> arg0) {
					return new UiListCellAgenda();
				}

			});
		}
		controller.popupLayer.getChildren().clear();
//		controller.popupLayer.getChildren().add(makeDraggable(controller.searchBox));
		controller.popupLayer.getChildren().add(controller.searchBox);
		controller.overlay.setVisible(true);
	}	
}
