package org.jimple.planner.ui;

import java.time.LocalDateTime;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UiListCellDeadline extends ListCell<Task> {

	@Override
	protected void updateItem(Task item, boolean bln) {
		super.updateItem(item, bln);
		if (item != null) {
			Text title = new Text(item.getTitle());
			Text ID = new Text();
			Text date = new Text();
			VBox vBox = new VBox();
			HBox hBox = new HBox();
			Region spacer = new Region();
			VBox.setVgrow(spacer, Priority.ALWAYS);
			HBox.setHgrow(spacer, Priority.ALWAYS);

			// STATIC
			if (item.getType().equals(Constants.TYPE_STATIC)) {
//				double value = (double) LocalDateTime.now().getSecond() / 60.0 * 255.0;
//				String color = "-fx-background-color: #ffff"
//						+ Integer.toHexString((int) value).toString();
//				setStyle(color);
				this.getStyleClass().clear();
				this.getStyleClass().add(Constants.TYPE_STATIC);
				this.setFocusTraversable(false);
				vBox = new VBox(title);
				hBox = new HBox(vBox);
			}

			// DEADLINE
			else {
				this.getStyleClass().clear();
				this.getStyleClass().add(Constants.TYPE_DEADLINE);
				// more than a day away
				if (UiFormatter.timeDifference(item.getFromTime()) >= 1440)
					this.getStyleClass().add("green");
				// less than a day
				else if (UiFormatter.timeDifference(item.getFromTime()) >= 60)
					this.getStyleClass().add("yellow");
				// less than an hour
				else if (UiFormatter.timeDifference(item.getFromTime()) >= 30)
					this.getStyleClass().add("orange");
				else if (UiFormatter.timeDifference(item.getFromTime()) >= 10)
					this.getStyleClass().add("red");
				else if (UiFormatter.timeDifference(item.getFromTime()) >= 0)
					this.getStyleClass().add("darkred");
				else
					this.getStyleClass().add("overdue");

				vBox = new VBox(title);
				ID.setText(String.format("  %d", item.getTaskId()));
				date.setText(String.format("%s", item.getPrettyFromTime()));
				hBox = new HBox(date, vBox, spacer, ID);
				title.getStyleClass().add(Constants.TYPE_DEADLINE);
				ID.getStyleClass().add(Constants.TYPE_DEADLINE);
				date.getStyleClass().add(Constants.TYPE_DEADLINE);
			}
			hBox.setSpacing(10);
			setGraphic(hBox);
		}
	}

}
