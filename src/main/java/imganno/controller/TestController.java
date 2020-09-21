package main.java.imganno.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.WindowEvent;
import main.java.imganno.App;
import main.java.imganno.objects.Annotation;
import main.java.imganno.objects.ImageData;

public class TestController {

	@FXML private AnchorPane pane;
	@FXML private AnchorPane hostPane;
	@FXML private ImageView imagePane;
	@FXML TextField commentField;
	
	@FXML Label instructions;
	
	@FXML Button createAnnoButton;
	@FXML Button saveAnnoButton;
	@FXML Button deleteAnnoButton;
	@FXML Button saveChangesButton;
	@FXML Button deleteChangesButton;
	@FXML Button loadImageButton;
		
	FileChooser fc;
	
	File imageFile;
	ImageData imageData;
	
	private Annotation currAnnotation;
	ArrayList<Annotation> annotations;
	
	private Rectangle clip;
	
	private boolean first;
	private boolean creating;
	
	boolean getCreating() {
		return creating;
	}
	
	AnchorPane getHostPane() {
		return hostPane;
	}
	
	ImageView getImagePane() {
		return imagePane;
	}
	
	Annotation getCurrAnnotation() {
		return currAnnotation;
	}
		
	public TestController() {	
		this.annotations = new ArrayList<>();
		
		this.fc = new FileChooser();
		fc.setTitle("Select image file");
		fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
		
		first = true;
		creating = false;
	}
	
	private void toggleButtons(boolean bool) {
		saveAnnoButton.setDisable(!bool);
		deleteAnnoButton.setDisable(!bool);
		
		//commentField should only be editable when these are enabled and vice versa
		commentField.setEditable(bool);
	}
	
	void setCurrAnnotation(Annotation a) {
		if(currAnnotation != null) {
			currAnnotation.setStroke(Paint.valueOf("black"));
			commentField.setText("");
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
				deleteAnnoButton.setDisable(false);
				event.consume();
			}
			else if (!event.isControlDown() && creating){	
				a.setX(event.getSceneX()-a.getWidth()/2-a.getTranslateX());
				a.setY(event.getSceneY()-a.getHeight()/2-a.getTranslateY());
			}
			
		}
		
	};
	
	EventHandler<WindowEvent> exitHandler = e -> exitHelper(e);
	
	void exitHelper(WindowEvent event) {
		event.consume();
		Alert pu = new Alert(AlertType.CONFIRMATION);
		pu.setTitle("Exit");
		pu.setHeaderText("Do you wish to save before exiting?");
		
		ButtonType saveClose = new ButtonType("Save");
		ButtonType nosaveClose = new ButtonType("Don't Save");
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		
		pu.getButtonTypes().setAll(saveClose, nosaveClose, cancel);
		
		ButtonType result = pu.showAndWait().orElse(ButtonType.CANCEL);
		if (result == saveClose) {
			saveAnnotationstoDisk();
			Platform.exit();
		}
		else if (result == nosaveClose) {
			Platform.exit();
		}
		else {
			pu.close();
		}
	}

	
	@FXML public void initialize() {
	
		instructions.setText("Instructions:\n"+
							"Left click on an annotation to select it\n"+
							"To create an annotation, click 'Create Annotation'\n"+
							"Ctrl+Left Click : Spawn annotation squares\n"+
							"Ctrl+Left Click+Drag : Resize annotation square\n"+
							"Left Click+Drag : Move annotation square\n");
		
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
		
		toggleButtons(false);
		createAnnoButton.setDisable(true);
		saveChangesButton.setDisable(true);
		deleteChangesButton.setDisable(true);
	}
	
	void saveAnnotationstoDisk() {
		//file name will be imagefilename_creationdate.csv
		if(imageData != null && imageFile != null) {
			
			Path imagePath = Paths.get(imageFile.getAbsolutePath());
			BasicFileAttributes attr;
			try {
				attr = Files.readAttributes(imagePath, BasicFileAttributes.class);
				File dir = new File("image_data/");
				dir.mkdir();
				File f = new File(String.format("image_data/%s_%s.csv", imageFile.getName(), attr.creationTime()));
				//System.out.println(f.getAbsolutePath());
				
				FileWriter fw = new FileWriter(f);
				for (Annotation a : annotations) {
					fw.write(String.format("%s,%s,%s,%s,%s,%s,%s\n",
							a.comment, a.getX(), a.getY(), a.getWidth(), a.getHeight(),
							a.getTranslateX(), a.getTranslateY()));
				}
				fw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	void loadAnnotationsfromDisk() {
		Path imagePath = Paths.get(imageFile.getAbsolutePath());
		BasicFileAttributes attr;
		try {
			attr = Files.readAttributes(imagePath, BasicFileAttributes.class);
			File f = new File(String.format("image_data/%s_%s.csv", imageFile.getName(), attr.creationTime()));

			if(f.exists()) {
				Scanner s = new Scanner(f);
				while(s.hasNextLine()) {
					String data[] = s.nextLine().split(",");
					Annotation a = new Annotation(data[0],
							Double.valueOf(data[1]),
							Double.valueOf(data[2]));
					
					a.setWidth(Double.valueOf(data[3]));
					a.setHeight(Double.valueOf(data[4]));
					a.setTranslateX(Double.valueOf(data[5]));
					a.setTranslateY(Double.valueOf(data[6]));
					
					hostPane.getChildren().add(a);
					a.toFront();
					a.setOnMousePressed(annotationHandler);
					a.setOnMouseDragged(annotationHandler);
					
					annotations.add(a);
				}
			} else {
				System.out.println("File not found");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//removes the contents of alist from the annotations list
	void clearCurrData(Annotation[] alist) {
		for(Annotation a : alist) {
			hostPane.getChildren().remove(a);
			annotations.remove(a);
		}
		setCurrAnnotation(null);
		commentField.setText("");
	}
	
	@FXML void saveChanges() {
		//go through all annotations and save them as individual text files to internal directory
		saveAnnotationstoDisk();
	}
	
	@FXML void discardChanges() {
		Annotation[] alist = new Annotation[annotations.size()];
		alist = annotations.toArray(alist);
		clearCurrData(alist);
		loadAnnotationsfromDisk();
	}
	
	@FXML void createAnnotation() {
		createAnnoButton.setDisable(true);
		toggleButtons(true);
		setCurrAnnotation(null);
		creating = true;
	}
	
	@FXML void saveAnnotation() {
		createAnnoButton.setDisable(false);
		if(currAnnotation != null) {
			currAnnotation.comment = commentField.getText();
			annotations.add(currAnnotation);
		}
		creating = false;
		toggleButtons(false);
		deleteAnnoButton.setDisable(false);
	}
	
	@FXML void deleteAnnotation() {
		createAnnoButton.setDisable(false);
		commentField.setText("");
		Annotation[] alist = new Annotation[0];
		if(currAnnotation != null) {
			alist = new Annotation[] {currAnnotation};
		}
		clearCurrData(alist);
		creating = false;
		toggleButtons(false);
	}
	
	@FXML void loadImage() throws FileNotFoundException {
		
		if(first) {
			first = false;
			App.getPrimaryStage().setOnCloseRequest(exitHandler);
			
			createAnnoButton.setDisable(false);
			saveChangesButton.setDisable(false);
			deleteChangesButton.setDisable(false);
		}
		
		Annotation[] alist = new Annotation[annotations.size()];
		alist = annotations.toArray(alist);
		clearCurrData(alist);
		
		imageFile = fc.showOpenDialog(App.getPrimaryStage());
		
		if(imageFile != null) {
			imageData = new ImageData(imageFile, new Image(new FileInputStream(imageFile.getPath())));
			
			loadAnnotationsfromDisk();
			
			imagePane.setImage(imageData.getImage());
			
			clip = new Rectangle(imagePane.getFitWidth(), imagePane.getFitHeight());
			hostPane.setClip(clip);
		} else {
			App.getPrimaryStage().setOnCloseRequest(null);
		}
	}
	
}
