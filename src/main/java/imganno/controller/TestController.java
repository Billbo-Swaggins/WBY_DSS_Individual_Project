package main.java.imganno.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
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
	
	private boolean first;
	private boolean creating;
		
	public TestController() {		
		this.annotations = new ArrayList<>();
		
		this.fc = new FileChooser();
		fc.setTitle("Select image file");
		fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
		
		first = true;
		creating = false;
	}
	
	private void setCurrAnnotation(Annotation a) {
		if(currAnnotation != null) {
			currAnnotation.setStroke(Paint.valueOf("black"));
		}
		if(a != null) {
			a.setStroke(Paint.valueOf("red"));
			commentField.setText(a.comment);
		}
		
		currAnnotation = a;
	}
	
	EventHandler<MouseEvent> annotationHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			Annotation a = (Annotation) event.getSource();
			//System.out.println("moused pressed on annotation");
			if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				if(!creating) setCurrAnnotation(a);
				event.consume();
			}
			else if (!event.isControlDown() && creating){	
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
				if(event.isControlDown() && imageData != null && creating) {
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
				if(currAnnotation != null && event.isControlDown() && imageData != null && creating) {
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
	}
	
	@FXML private void discardChanges(ActionEvent ae) {
		
	}
	
	@FXML private void createAnnotation(ActionEvent ae) {
		setCurrAnnotation(null);
		creating = true;
	}
	
	@FXML private void saveAnnotation(ActionEvent ae) {
		if(currAnnotation != null) {
			currAnnotation.comment = commentField.getText();
			annotations.add(currAnnotation);
		}
		creating = false;
		setCurrAnnotation(null);
	}
	
	@FXML private void deleteAnnotation(ActionEvent ae) {
		commentField.setText("");
		if(currAnnotation != null) {
			annotations.remove(currAnnotation);
			hostPane.getChildren().remove(currAnnotation);
		}
		setCurrAnnotation(null);
		creating = false;
	}
	
	@FXML private void loadImage(ActionEvent ae) throws FileNotFoundException {
		
		if(first) {
			first = false;
			App.getPrimaryStage().setOnCloseRequest(event -> {
				event.consume();
				Alert pu = new Alert(AlertType.CONFIRMATION);
				pu.setTitle("Exit");
				pu.setHeaderText("Do you wish to save before exiting?");
				
				ButtonType saveClose = new ButtonType("Save");
				ButtonType nosaveClose = new ButtonType("Don't Save");
				ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
				
//				((Button) pu.getDialogPane().lookupButton(saveClose)).setPrefWidth(Region.USE_COMPUTED_SIZE);
//				((Button) pu.getDialogPane().lookupButton(nosaveClose)).setPrefWidth(Region.USE_COMPUTED_SIZE);

				
				pu.getButtonTypes().setAll(saveClose, nosaveClose, cancel);
				
				ButtonType result = pu.showAndWait().get();
				if (result == saveClose) {
					//save all annotations to disk
					Platform.exit();
				}
				else if (result == nosaveClose) {
					Platform.exit();
				}
				else {
					pu.hide();
				}
				
			});
		}
		
		for(Annotation a : annotations) {
			hostPane.getChildren().remove(a);
		}
		annotations = new ArrayList<>();
		imageFile = fc.showOpenDialog(App.getPrimaryStage());
		
		if(imageFile != null) {
			imageData = new ImageData(new Image(new FileInputStream(imageFile.getPath())));
			
			//TODO: load annotations
			
			imagePane.setImage(imageData.getImage());
			
			clip = new Rectangle(imagePane.getFitWidth(), imagePane.getFitHeight());
			hostPane.setClip(clip);
		}
	}
}
