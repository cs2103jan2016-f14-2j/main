package org.jimple.planner.ui;

import org.jimple.planner.Task;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UiListCellSearch extends ListCell<Task> {
	@Override
	protected void updateItem(Task item, boolean bln) {
		super.updateItem(item, bln);
		if (item != null) {
			Text title = new Text(item.getTitle());
			title.getStyleClass().add("fancytext");
			VBox vBox = new VBox(title);
			HBox hBox = new HBox(new Text(String.format("#%d", item.getTaskId())), vBox);
			hBox.setSpacing(10);
			setGraphic(hBox);
		}
	}
}
