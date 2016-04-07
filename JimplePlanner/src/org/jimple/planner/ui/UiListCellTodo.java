package org.jimple.planner.ui;
//@@author A0122498
import java.io.IOException;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

public class UiListCellTodo extends ListCell<Task> {
	
	@FXML
	Label title, id, label;
	@FXML
	AnchorPane anchorpane;
	
	FXMLLoader fxmlLoader;
	
	@Override
	protected void updateItem(Task item, boolean bln) {
		super.updateItem(item, bln);
		this.getStyleClass().add("listcell");
		if (item != null) {
			fxmlLoader = new FXMLLoader(getClass().getResource("todoCellLayout.fxml"));
			fxmlLoader.setController(this);
			try {
				fxmlLoader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if(item.getTaskLabel().getLabelName().equals(Constants.TASK_LABEL_NAME_DEFAULT))
				label.setVisible(false);
			title.setText(item.getTitle());
			id.setText(String.format("%d", item.getTaskId()));
			label.setText(item.getTaskLabel().getLabelName());
			label.setId("color" + item.getTaskLabel().getColourId());
			setGraphic(anchorpane);
		}
	}

}
