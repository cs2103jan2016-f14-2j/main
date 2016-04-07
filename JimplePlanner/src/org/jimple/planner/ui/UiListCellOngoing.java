package org.jimple.planner.ui;
//@@author A0122498
import java.io.IOException;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class UiListCellOngoing extends ListCell<Task>{
	
	@FXML
	Label title, fromdate, todate, id, label, time, desc;
	@FXML
	AnchorPane anchorpane;
	@FXML
	ImageView icon;
	@FXML
	VBox vBox, idcolor;

	FXMLLoader fxmlLoader;	
	@Override
	protected void updateItem(Task item, boolean bln) {
		super.updateItem(item, bln);
		this.getStyleClass().add("listcell");
		this.setPrefWidth(0);
		if (item != null) {
			this.setId(Constants.TYPE_DEADLINE);
			loadFXMLLayout("ongoingCellLayout.fxml");
			
			setDeadlineColors(item);	
			setLabel(item);
			setDesc(item);
			setStyles();
			
			icon.setImage(new Image("eventIconGrey.png"));
			title.setText(item.getTitle());
			id.setText(""+item.getTaskId());
			fromdate.setText("from " + item.getPrettyFromDate() + " " +item.getPrettyFromTime());
			todate.setText("to " + item.getPrettyToDate() + " " +item.getPrettyToTime());
			setGraphic(anchorpane);
		}
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
		label.setText(item.getTaskLabel().getLabelName());
		label.getStyleClass().add("labelText");
		label.setId("color" + item.getTaskLabel().getColourId());
		if(item.getTaskLabel().getLabelName().equals(Constants.TASK_LABEL_NAME_DEFAULT)){
			if(vBox.getChildren().contains(label))
				vBox.getChildren().remove(label);
		}
	}

	private void setDesc(Task item) {
		desc.setText(item.getDescription());
		desc.setId("description");
		if(item.getDescription().equals("")){
			if(vBox.getChildren().contains(desc))
				vBox.getChildren().remove(desc);
		}
		idcolor.setId("color" + item.getTaskLabel().getColourId());
		id.setId("color" + item.getTaskLabel().getColourId());
	}
	
	private void setStyles(){
		title.getStyleClass().add("title");
		fromdate.getStyleClass().add("date");
		todate.getStyleClass().add("date");
	}
	
	private void setDeadlineColors(Task item) {
		// more than a day away
		if (UiFormatter.timeDifference(item.getFromTime()) >= 1440)
			this.setId("green");
		// less than a day
		else if (UiFormatter.timeDifference(item.getFromTime()) >= 180)
			this.setId("yellow");
		// less than an hour
		else if (UiFormatter.timeDifference(item.getFromTime()) >= 60)
			this.setId("orange");
		else if (UiFormatter.timeDifference(item.getFromTime()) >= 30)
			this.setId("red");
		else if (UiFormatter.timeDifference(item.getFromTime()) >= 0)
			this.setId("darkred");
		else
			this.setId("overdue");
	}
}
