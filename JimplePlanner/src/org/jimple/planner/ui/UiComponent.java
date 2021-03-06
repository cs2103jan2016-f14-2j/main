//@@author A0122498Y
package org.jimple.planner.ui;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UiComponent extends Application {
	double xOffset;
	double yOffset;
	protected static final Logger log= Logger.getLogger( UiComponent.class.getName() );
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("JimpleUI.fxml"));
			root.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                xOffset = primaryStage.getX() - event.getScreenX();
	                yOffset = primaryStage.getY() - event.getScreenY();
	            }
	        });
			
			root.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                primaryStage.setX(event.getScreenX() + xOffset);
	                primaryStage.setY(event.getScreenY() + yOffset);
	            }
	        });
			Scene scene = new Scene(root,1000,700);
			scene.setFill(Color.TRANSPARENT);
			scene.getStylesheets().add("application.css");
			primaryStage.getIcons().add(new Image("Jimple-Icon.png"));
            primaryStage.setTitle("Jimple Planner");
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.show();
		} catch(Exception e) {
			log.log(Level.WARNING,"Error setting UI stage");
		}
	}
	
	public void begin(){
		String[] args = null;
		launch(args);
	}
	
	public void begin(String[] args) {
		launch(args);
	}
}