package org.jimple.planner.ui;

import java.io.IOException;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UiListCellOngoing extends ListCell<Task>{
	
	@FXML
	Label title, date, id, label;
	@FXML
	AnchorPane anchorpane;

	FXMLLoader fxmlLoader;	
	@Override
	protected void updateItem(Task item, boolean bln) {
		super.updateItem(item, bln);
		this.getStyleClass().add("listcell");
		if (item != null) {
			this.setId("ongoing");
			fxmlLoader = new FXMLLoader(getClass().getResource("ongoingCellLayout.fxml"));
			fxmlLoader.setController(this);
			try {
				fxmlLoader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if(item.getTaskLabel().getLabelName().equals(Constants.TASK_LABEL_NAME_DEFAULT))
				label.setVisible(false);
			label.setText(item.getTaskLabel().getLabelName());
			label.setId("color" + item.getTaskLabel().getColourId());
			title.setText(item.getTitle());
			id.setText(String.format("%d", item.getTaskId()));
			date.setText(String.format("FROM %s %s\r\nTO %s %s",
					item.getPrettyFromDate(),
					item.getPrettyFromTime(),
					item.getPrettyToDate(),
					item.getPrettyToTime()));
			title.setId("ongoingText");
			id.setId("ongoingText");
			date.setId("ongoingText");
			setGraphic(anchorpane);
		}
	}

}
