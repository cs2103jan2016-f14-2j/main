package org.jimple.planner.ui;

import java.io.IOException;

import org.jimple.planner.Constants;
import org.jimple.planner.Task;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UiListCellAgenda extends ListCell<Task> {
	
	@FXML
	Label title, fromdate, todate, id, label, desc;
	@FXML
	CheckBox checkbox;
	@FXML
	AnchorPane anchorpane, marker, idcolor;
	@FXML
	VBox vBox, date;
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
				loadFXMLLayout("todoCellLayout.fxml");
				
				setDeadlineColors(item);	
				setLabel(item);
				setDesc(item);
				if(hBox.getChildren().contains(checkbox))
					hBox.getChildren().remove(checkbox);
				if(date.getChildren().contains(todate))
					date.getChildren().remove(todate);

				title.setText(item.getTitle());
				id.setText(""+item.getTaskId());
				fromdate.setText(item.getPrettyFromTime());
//				title.setId(Constants.TYPE_DEADLINE+"Text");
//				id.setId(Constants.TYPE_DEADLINE+"Text");
//				date.setId(Constants.TYPE_DEADLINE+"Text");
				break;
			
			//EVENT
			case Constants.TYPE_EVENT:
				this.setId(Constants.TYPE_EVENT);
				loadFXMLLayout("todoCellLayout.fxml");
				
				setLabel(item);
				setDesc(item);
				if(hBox.getChildren().contains(checkbox))
					hBox.getChildren().remove(checkbox);
				
				title.setText(item.getTitle());
				id.setText(""+item.getTaskId());
				fromdate.setText(item.getPrettyFromTime());
				todate.setText(item.getPrettyToTime());
				break;
			
			//TO-DO
			case Constants.TYPE_TODO:
				this.setId(Constants.TYPE_TODO);
				loadFXMLLayout("todoCellLayout.fxml");
				
				setLabel(item);
				setDesc(item);
				if(hBox.getChildren().contains(date))
					hBox.getChildren().remove(date);

				title.setText(item.getTitle());
				id.setText(""+item.getTaskId());
				break;
			}
			setGraphic(anchorpane);	
		}
	}

	private void remove(Node node) throws NullPointerException{
		((Pane) node.getParent()).getChildren().remove(node);
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
		title.getStyleClass().add("title");
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

}
