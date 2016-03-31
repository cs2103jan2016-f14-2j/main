package org.jimple.planner.ui;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UiListCellTodo extends ListCell<Task> {

	@Override
	protected void updateItem(Task item, boolean bln) {
		super.updateItem(item, bln);
		if (item != null) {
			Text title = new Text(item.getTitle());
			Text ID = new Text();
			Region spacer = new Region();
			VBox.setVgrow(spacer, Priority.ALWAYS);
			HBox.setHgrow(spacer, Priority.ALWAYS);
			ID.setText(String.format("%d", item.getTaskId()));

			title.getStyleClass().add(Constants.TYPE_TODO);
			ID.getStyleClass().add(Constants.TYPE_TODO);
			this.getStyleClass().add(Constants.TYPE_TODO);

			VBox vBox = new VBox(title);
			HBox hBox = new HBox(vBox, spacer, ID);
			hBox.setSpacing(10);
			setGraphic(hBox);
		}
	}

}
