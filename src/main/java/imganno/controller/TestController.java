package main.java.imganno.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.java.imganno.App;
import main.java.imganno.objects.Annotation;
import main.java.imganno.objects.ImageData;

public class TestController {

	@FXML private AnchorPane pane;
	@FXML private AnchorPane hostPane;
	@FXML private ImageView imagePane;
	@FXML private TextField commentField;
		
	private FileChooser fc;
	
	private File imageFile;
	private ImageData imageData;
	
	private Annotation currAnnotation;
	private ArrayList<Annotation> annotations;
	
	private Rectangle clip;
		
	public TestController() {		
		this.annotations = new ArrayList<>();
		
		this.fc = new FileChooser();
		fc.setTitle("Select image file");
		fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
	}
	
	private void setCurrAnnotation(Annotation a) {
		if(currAnnotation != null) {
			currAnnotation.setStroke(Paint.valueOf("black"));
		}
		
		a.setStroke(Paint.valueOf("red"));
		commentField.setText(a.comment);
		currAnnotation = a;
	}
	
	EventHandler<MouseEvent> annotationHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			
			Annotation a = (Annotation) event.getSource();
			//System.out.println("moused pressed on annotation");
			if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				setCurrAnnotation(a);
				event.consume();
			}
			else if (!event.isControlDown()){
				
				a.setX(event.getSceneX()-a.getWidth()/2-a.getTranslateX());
				a.setY(event.getSceneY()-a.getHeight()/2-a.getTranslateY());
			
			}
			
		}
		
	};
	
	@FXML public void initialize() {
		
		pane.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				//only reason we should be clicking on this pane itself instead of a annotation is to create a new one
				if(event.isControlDown() && imageData != null) {
					if(!annotations.contains(currAnnotation)) {
						hostPane.getChildren().remove(currAnnotation);
					}
					Annotation a = new Annotation("", event.getSceneX(), event.getSceneY());
					hostPane.getChildren().add(a);
					a.toFront();
					a.setOnMousePressed(annotationHandler);
					a.setOnMouseDragged(annotationHandler);
//					a.setOnMouseEntered(e -> {
//						mouseInBounds = true;
//					});
//					a.setOnMouseExited(e -> {
//						mouseInBounds = false;
//					});
					setCurrAnnotation(a);
				}
			}
			
		});
				
		pane.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				//this should only ever happen after we created a new annotation and set it to currannotation, so resize that
				if(currAnnotation != null && event.isControlDown() && imageData != null) {
					double x = currAnnotation.getX();
					double y = currAnnotation.getY();
					
					double mouseX = event.getSceneX();
					double mouseY = event.getSceneY();
					
					double width = mouseX - x;
					double height = mouseY - y;
									
					//account for when width becomes negative
					if(width < 0) {
						currAnnotation.setTranslateX(width);
						currAnnotation.setWidth(-width);
					} else {
						currAnnotation.setWidth(width);
					}
					
					//account for when width and height becomes negative
					if(height < 0) {
						currAnnotation.setTranslateY(height);
						currAnnotation.setHeight(-height);
					} else {
						currAnnotation.setHeight(height);
					}
				}
			}
			
		});
	}
	
	@FXML private void saveChanges(ActionEvent ae) {
		//go through all annotations and save them as individual text files to internal directory
		for(Annotation a : annotations) {
			
		}
	}
	
	@FXML private void discardChanges(ActionEvent ae) {
		
	}
	
	@FXML private void createAnnotation(ActionEvent ae) {
		currAnnotation.comment = commentField.getText();
		annotations.add(currAnnotation);
	}
	
	@FXML private void deleteAnnotation(ActionEvent ae) {
		commentField.setText("");
		annotations.remove(currAnnotation);
		hostPane.getChildren().remove(currAnnotation);
		setCurrAnnotation(null);
	}
	
	@FXML private void loadImage(ActionEvent ae) throws FileNotFoundException {
		for(Annotation a : annotations) {
			hostPane.getChildren().remove(a);
		}
		annotations = new ArrayList<>();
		imageFile = fc.showOpenDialog(App.getPrimaryStage());
		imageData = new ImageData(new Image(new FileInputStream(imageFile.getPath())));
		
		//TODO: load annotations
		
		imagePane.setImage(imageData.getImage());
		
		clip = new Rectangle(imagePane.getFitWidth(), imagePane.getFitHeight());
		hostPane.setClip(clip);
	}
}
