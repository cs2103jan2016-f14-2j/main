//@@author A0122498Y
package org.jimple.planner.ui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jimple.planner.constants.Constants;
import org.jimple.planner.task.Task;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UiListCellDefault extends ListCell<Task> {
	protected static final Logger log= Logger.getLogger( UiComponent.class.getName() );	
	@FXML
	Label title, fromdate, todate, id, label, desc;
	@FXML
	CheckBox checkbox;
	@FXML
	AnchorPane anchorpane, marker;
	@FXML
	VBox vBox, date, idcolor;
	@FXML
	ImageView icon;
	@FXML
	HBox hBox;

	FXMLLoader fxmlLoader;	

	@Override
	protected void updateItem(Task item, boolean bln) {
		super.updateItem(item, bln);
		this.getStyleClass().add("listcell");
		this.setPrefWidth(0);
		
		if (item != null) {
			switch(item.getType()){
			
			//STATIC
			case Constants.TYPE_STATIC:
				this.setId(Constants.TYPE_STATIC);
				loadFXMLLayout("staticCellLayout.fxml");
				
				title.setText(item.getTitle());
				title.getStyleClass().add("staticDate");
				marker.getStyleClass().add(Constants.TYPE_STATIC + "Marker");
				break;
			
			//DEADLINE
			case Constants.TYPE_DEADLINE:
				this.setId(Constants.TYPE_DEADLINE);
				loadFXMLLayout("defaultCellLayout.fxml");
				
				setLabel(item);
				setDesc(item);
				setStyles();
				
				if(hBox.getChildren().contains(checkbox))
					hBox.getChildren().remove(checkbox);
				if(date.getChildren().contains(todate))
					date.getChildren().remove(todate);

				icon.setImage(new Image("deadlineIconGrey.png"));
				title.setText(item.getTitle());
				id.setText(""+item.getTaskId());
				fromdate.setText(item.getPrettyFromDate() + " " + item.getPrettyFromTime());
				setDeadlineColors(item);	
				break;
			
			//EVENT
			case Constants.TYPE_EVENT:
				this.setId(Constants.TYPE_EVENT);
				loadFXMLLayout("defaultCellLayout.fxml");
				
				setLabel(item);
				setDesc(item);
				setStyles();
				
				if(UiFormatter.timeDifference(item.getToTime()) < 0){
					this.setId("overdue");
				}
				else{
					this.setId("");
				}
				
				if(hBox.getChildren().contains(checkbox))
					hBox.getChildren().remove(checkbox);

				icon.setImage(new Image("eventIconGrey.png"));
				title.setText(item.getTitle());
				id.setText(""+item.getTaskId());
				fromdate.setText(item.getPrettyFromDate() + " " + item.getPrettyFromTime());
				todate.setText(item.getPrettyToDate() + " " + item.getPrettyToTime());
				break;
			
			//TO-DO
			case Constants.TYPE_TODO:
				this.setId(Constants.TYPE_TODO);
				loadFXMLLayout("defaultCellLayout.fxml");
				
				setLabel(item);
				setDesc(item);
				setStyles();
				
				if(hBox.getChildren().contains(date))
					hBox.getChildren().remove(date);

				icon.setImage(new Image("todoIconGrey.png"));
				title.setText(item.getTitle());
				id.setText(""+item.getTaskId());
				break;
			}
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
		else if (UiFormatter.timeDifference(item.getFromTime()) >= 30){
			this.setId("red");
			icon.setImage(new Image("deadlineIconWhite.png"));
		}
		else if (UiFormatter.timeDifference(item.getFromTime()) >= 0){
			this.setId("darkred");
			icon.setImage(new Image("deadlineIconWhite.png"));
		}
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
		todate.getStyleClass().add("date");
		fromdate.getStyleClass().add("date");
	}

}
