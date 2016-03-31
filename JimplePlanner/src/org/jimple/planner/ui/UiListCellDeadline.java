package org.jimple.planner.ui;

import java.io.IOException;
import java.time.LocalDateTime;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UiListCellDeadline extends ListCell<Task> {
	
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
			// STATIC
			if (item.getType().equals(Constants.TYPE_STATIC)) {
				this.setId(Constants.TYPE_STATIC);

				fxmlLoader = new FXMLLoader(getClass().getResource("staticCellLayout.fxml"));
				fxmlLoader.setController(this);
				try {
					fxmlLoader.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				title.setText(item.getTitle());
			}

			// DEADLINE
			else {
				this.setId(Constants.TYPE_DEADLINE);

				fxmlLoader = new FXMLLoader(getClass().getResource("deadlineCellLayout.fxml"));
				fxmlLoader.setController(this);
				try {
					fxmlLoader.load();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				// more than a day away
				if (UiFormatter.timeDifference(item.getFromTime()) >= 1440)
					this.setId("green");
				// less than a day
				else if (UiFormatter.timeDifference(item.getFromTime()) >= 60)
					this.setId("yellow");
				// less than an hour
				else if (UiFormatter.timeDifference(item.getFromTime()) >= 30)
					this.setId("orange");
				else if (UiFormatter.timeDifference(item.getFromTime()) >= 10)
					this.setId("red");
				else if (UiFormatter.timeDifference(item.getFromTime()) >= 0)
					this.setId("darkred");
				else
					this.setId("overdue");
				
				if(item.getTaskLabel().getLabelName().equals(Constants.TASK_LABEL_NAME_DEFAULT))
					label.setVisible(false);
				label.setText(item.getTaskLabel().getLabelName());
				label.setId("color" + item.getTaskLabel().getColourId());

				title.setText(item.getTitle());
				id.setText(String.format("  %d", item.getTaskId()));
				date.setText(String.format("%s", item.getPrettyFromTime()));
				title.setId(Constants.TYPE_DEADLINE);
				id.setId(Constants.TYPE_DEADLINE);
				date.setId(Constants.TYPE_DEADLINE);
			}
			setGraphic(anchorpane);
		}
	}

}
