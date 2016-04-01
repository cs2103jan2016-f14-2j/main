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
import javafx.scene.text.Text;

public class UiListCellAgenda extends ListCell<Task> {
	
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
			switch(item.getType()){
			
			//STATIC
			case Constants.TYPE_STATIC:
				this.setId(Constants.TYPE_STATIC);
				loadFXMLLayout("staticCellLayout.fxml");
				
				title.setText(item.getTitle());
				title.setId(Constants.TYPE_STATIC);
				break;
			
			//DEADLINE
			case Constants.TYPE_DEADLINE:
				this.setId(Constants.TYPE_DEADLINE);
				loadFXMLLayout("deadlineCellLayout.fxml");
				
				setDeadlineColors(item);	
				setLabel(item);

				title.setText(item.getTitle());
				id.setText(String.format("  %d", item.getTaskId()));
				date.setText(String.format("%s", item.getPrettyFromTime()));
				title.setId(Constants.TYPE_DEADLINE+"Text");
				id.setId(Constants.TYPE_DEADLINE+"Text");
				date.setId(Constants.TYPE_DEADLINE+"Text");
				break;
			
			//EVENT
			case Constants.TYPE_EVENT:
				this.setId(Constants.TYPE_EVENT);
				loadFXMLLayout("eventCellLayout.fxml");
				
				setLabel(item);
				
				title.setText(item.getTitle());
				id.setText(String.format("%d", item.getTaskId()));
				date.setText(String.format("%s %s to %s %s",
						item.getPrettyFromDate(),
						item.getPrettyFromTime(),
						item.getPrettyToDate(),
						item.getPrettyToTime()));
				title.setId(Constants.TYPE_EVENT+"Text");
				id.setId(Constants.TYPE_EVENT+"Text");
				date.setId(Constants.TYPE_EVENT+"Text");
				break;
			
			//TO-DO
			case Constants.TYPE_TODO:
				this.setId(Constants.TYPE_TODO);
				loadFXMLLayout("todoCellLayout.fxml");
				
				setLabel(item);
				
				title.setText(item.getTitle());
				title.setWrapText(true);
				id.setText(String.format("%d", item.getTaskId()));
				label.setText(item.getTaskLabel().getLabelName());
				label.setId("color" + item.getTaskLabel().getColourId());
				break;
			}
			HBox hbox = new HBox(new Text("hello world"));
//			hbox.setPrefHeight(30);
//			setGraphic(new Text("hello world"));
			setGraphic(anchorpane);	
		}
	}

	private void setDeadlineColors(Task item) {
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
	}

	private void loadFXMLLayout(String file) {
		fxmlLoader = new FXMLLoader(getClass().getResource(file));
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void setLabel(Task item) {
		if(item.getTaskLabel().getLabelName().equals(Constants.TASK_LABEL_NAME_DEFAULT))
			label.setVisible(false);
		label.setText(item.getTaskLabel().getLabelName());
		label.getStyleClass().add("labelText");
		label.setId("color" + item.getTaskLabel().getColourId());
	}

}
