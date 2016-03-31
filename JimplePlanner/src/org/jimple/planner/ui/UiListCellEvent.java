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

public class UiListCellEvent extends ListCell<Task>{
	
	@FXML
	Label title, date, id, label;
	@FXML
	HBox hBox;					
	@FXML
	VBox vbox;					
	@FXML
	Region spacer;
	@FXML
	AnchorPane anchorpane;

	FXMLLoader fxmlLoader;	
	@Override
	protected void updateItem(Task item, boolean bln) {
		super.updateItem(item, bln);
		if (item != null) {
			// STATIC
			if (item.getType().equals(Constants.TYPE_STATIC)) {
				this.getStyleClass().clear();
				this.getStyleClass().add(Constants.TYPE_STATIC);

				fxmlLoader = new FXMLLoader(getClass().getResource("staticCellLayout.fxml"));
				fxmlLoader.setController(this);
				try {
					fxmlLoader.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				title.setText(item.getTitle());
			}

			// EVENT
			else {
				this.getStyleClass().clear();
				this.getStyleClass().add(Constants.TYPE_EVENT);
				fxmlLoader = new FXMLLoader(getClass().getResource("eventCellLayout.fxml"));
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
				label.getStyleClass().add("color" + item.getTaskLabel().getColourId());
				date.setText(String.format("%s %s to %s %s",
						item.getPrettyFromDate(),
						item.getPrettyFromTime(),
						item.getPrettyToDate(),
						item.getPrettyToTime()));
			}
			setGraphic(anchorpane);
		}
	}

}
