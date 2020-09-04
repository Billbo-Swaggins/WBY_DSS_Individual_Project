package main.java.imganno.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.java.imganno.App;
import main.java.imganno.objects.ImageData;

public class TestController {

	@FXML private AnchorPane pane;
	@FXML private AnchorPane hostPane;
	@FXML private ImageView imagePane;
	
	@FXML private TextField commentField;
	
	private FileChooser fc;
	
	private File imageFile;
	private ImageData imageData;
	
	public TestController() {
		this.fc = new FileChooser();
		fc.setTitle("Select image file");
		fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
	}
	
	@FXML private void saveChanges(ActionEvent ae) {
		
	}
	
	@FXML private void discardChanges(ActionEvent ae) {
		
	}
	
	@FXML private void deleteAnnotation(ActionEvent ae) {
		
	}
	
	@FXML private void loadImage(ActionEvent ae) throws FileNotFoundException {
		imageFile = fc.showOpenDialog(App.getPrimaryStage());
		imageData = new ImageData(new Image(new FileInputStream(imageFile.getPath())));
		
		//TODO: load annotations
		
		imagePane.setImage(imageData.getImage());
	}
}
