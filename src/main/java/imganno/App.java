package main.java.imganno;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;


public class App extends Application {

	private static Stage primaryStage;
	private static Scene primaryScene;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryScene = new Scene(new AnchorPane());
		
		
		//src/main/resources/Test.fxml
		//src/main/java/App.java
		FXMLLoader fxmlLoader = new FXMLLoader();
		
		Parent root = fxmlLoader.load(getClass().getResourceAsStream("views/Test.fxml"));
		
		App.primaryStage = primaryStage;
		App.primaryStage.setResizable(false);
		
		primaryScene.setRoot(root);
		
		primaryStage.setScene(primaryScene);
		primaryStage.show();
	}
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

}
