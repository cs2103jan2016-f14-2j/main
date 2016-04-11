//@@author A0122498Y
package org.jimple.planner.ui;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class UiListCellUpcoming extends ListCell<Task> {
	protected static final Logger log= Logger.getLogger( UiComponent.class.getName() );
	@FXML
	Label title, date, id, label, time, desc;
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
			loadFXMLLayout("upcomingCellLayout.fxml");
			
			setDeadlineColors(item);	
			setLabel(item);
			setDesc(item);
			setStyles();
			
			icon.setImage(new Image("deadlineIconGrey.png"));
			title.setText(item.getTitle());
			id.setText(""+item.getTaskId());
			date.setText(item.getPrettierFromDate());
			time.setText(item.getPrettyFromTime());
				
			setGraphic(anchorpane);
		}
	}
	

	private void setDeadlineColors(Task item) {
		// more than a day away
		if (UiFormatter.timeDifference(item.getFromTime()) >= 1440)
			this.setId("");
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

	private void loadFXMLLayout(String file) {
		fxmlLoader = new FXMLLoader(getClass().getResource(file));
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			log.log(Level.WARNING,"Error loading FXML layout");
		}
	}

	private void setLabel(Task item) {
		if(item.getTaskLabel().getLabelName().equals(Constants.TASK_LABEL_NAME_DEFAULT))
			label.setVisible(false);
		label.setText(item.getTaskLabel().getLabelName());
		label.getStyleClass().add("labelText");
		label.setId("color" + item.getTaskLabel().getColourId());
	}
	
	private void setStyles(){
		title.getStyleClass().add("title");
		date.getStyleClass().add("date");
		time.getStyleClass().add("date");
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
}
