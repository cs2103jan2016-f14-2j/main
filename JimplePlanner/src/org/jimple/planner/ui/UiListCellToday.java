package org.jimple.planner.ui;

import java.io.IOException;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;

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

public class UiListCellToday extends ListCell<Task> {
	
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

			// DEADLINE
			if (item.getType().equals(Constants.TYPE_DEADLINE)) {

				Text title = new Text(item.getTitle());
				Text ID = new Text();
				Text date = new Text();
				VBox vBox = new VBox();
				HBox hBox = new HBox();
				Text desc = new Text(item.getDescription());
				Region spacer = new Region();
				VBox.setVgrow(spacer, Priority.ALWAYS);
				HBox.setHgrow(spacer, Priority.ALWAYS);
//				this.getStyleClass().clear();
				this.setId(Constants.TYPE_DEADLINE);

				// more than a day away
				if (UiFormatter.timeDifference(item.getFromTime()) > 1440)
					this.setId("green");
				// less than a day
				else if (UiFormatter.timeDifference(item.getFromTime()) > 120)
					this.setId("yellow");
				// less than an hour
				else if (UiFormatter.timeDifference(item.getFromTime()) > 60)
					this.setId("orange");
				else if (UiFormatter.timeDifference(item.getFromTime()) > 30)
					this.setId("red");
				else if (UiFormatter.timeDifference(item.getFromTime()) > 0)
					this.setId("darkred");
				else
					this.setId("overdue");

				vBox = new VBox(title);
				if (!item.getDescription().equals("")) {
					vBox = new VBox(title, desc);
				}
				ID.setText(String.format("%d", item.getTaskId()));
				date.setText(String.format("%s", item.getPrettyFromTime()));
				hBox = new HBox(date, vBox, spacer, ID);
				title.setId(Constants.TYPE_DEADLINE);
				ID.setId(Constants.TYPE_DEADLINE);
				date.setId(Constants.TYPE_DEADLINE);
				hBox.setSpacing(10);
				setGraphic(hBox);
			}

			// EVENT
			else {
//				this.getStyleClass().clear();
				this.setId(Constants.TYPE_EVENT);
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
				label.setId("color" + item.getTaskLabel().getColourId());
				date.setText(String.format("%s %s to %s %s",
						item.getPrettyFromDate(),
						item.getPrettyFromTime(),
						item.getPrettyToDate(),
						item.getPrettyToTime()));
				title.setId("event");
				id.setId("event");
				date.setId("event");
				setGraphic(anchorpane);
			}
		}
	}

}
