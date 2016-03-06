package org.jimple.planner;
 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("JimpleUI.fxml"));
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add("application.css");
            primaryStage.setTitle("Jimple Planner");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void begin(String[] args) {
		launch(args);
	}
}