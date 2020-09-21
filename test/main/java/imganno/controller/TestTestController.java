package main.java.imganno.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.imganno.App;
import main.java.imganno.objects.Annotation;
import main.java.imganno.objects.ImageData;

public class TestTestController extends ApplicationTest {
	
	private final FXMLLoader loader = new FXMLLoader();
	private Parent sceneRoot;
	
	private Stage stage;
	private File f;

	@Mock FileChooser fc;
	@InjectMocks @Spy TestController tc;
	TestController spy;

	
	@Override
	public void start (Stage stage) throws Exception {
		this.stage = stage;
		
		sceneRoot = loader.load(getClass().getResourceAsStream("../views/Test.fxml"));
		Scene primaryScene = new Scene(sceneRoot);
		tc = loader.getController();
		
		tc.fc = Mockito.mock(FileChooser.class);
		spy = Mockito.spy(tc);
		
		App.setPrimaryStage(stage);
		
		stage.setScene(primaryScene);
		stage.show();
		stage.toFront();
	}
	
	@Before
	public void setUp() throws Exception {
		URL url = new URL("https://i.imgur.com/nM7h55H.jpg"); //permanent link to test file
		BufferedImage img = ImageIO.read(url);
		f = new File("downloaded.jpg");
		ImageIO.write(img, "jpg", f);
	}
	
	@Test
	public void testLoadImage() {
		when(tc.fc.showOpenDialog(any(Stage.class))).thenReturn(f);
		
		clickOn("#loadImageButton");
		WaitForAsyncUtils.waitForFxEvents();
		assertEquals(f, tc.imageFile);
	}

	@Test
	public void testInitialized() {
		assertEquals(tc.instructions.getText(), "Instructions:\n"+
							"Left click on an annotation to select it\n"+
							"To create an annotation, click 'Create Annotation'\n"+
							"Ctrl+Left Click : Spawn annotation squares\n"+
							"Ctrl+Left Click+Drag : Resize annotation square\n"+
							"Left Click+Drag : Move annotation square\n");
	}
	
	@Test
	public void testCreateAnnotation() {
		tc.createAnnotation();
		assertEquals(true, tc.getCreating());
		assertEquals(tc.createAnnoButton.isDisabled(), true);
		assertEquals(tc.saveAnnoButton.isDisabled(), false);
		assertEquals(tc.deleteAnnoButton.isDisabled(), false);
		assertEquals(tc.commentField.isEditable(), true);
	}
	
	@Test
	public void testSaveandLoadAnnotationstoDisk() throws Exception {
		ImageData data = new ImageData(f, new Image(new FileInputStream(f.getPath())));
		
		spy.imageFile = f;
		spy.imageData = data;
		Annotation test = new Annotation("test", 0, 0);
		spy.annotations.add(test);
		spy.imageData.annotations.add(test);
		
		spy.saveChanges();
		assertEquals(spy.annotations.size(), 1);
		
		tc.imageFile = f;
		tc.imageData = data;
		tc.deleteChangesButton.setDisable(false);
		clickOn("#deleteChangesButton");
		
		assertEquals(tc.annotations.size(), 1);
	}
	
	@Test
	public void testSaveandDeleteAnnotation() {
		Annotation test = new Annotation("test", 0, 0);
		spy.setCurrAnnotation(test);;
		spy.saveAnnotation();
		
		assertEquals(spy.annotations.size(), 1);
		
		spy.setCurrAnnotation(spy.annotations.get(0));
		spy.deleteAnnotation();
		
		assertEquals(spy.annotations.size(), 0);
	}
	
	@Test
	public void testAnnotationHandlers() throws FileNotFoundException {
		when(tc.fc.showOpenDialog(any(Stage.class))).thenReturn(f);
		
		clickOn("#loadImageButton");
		clickOn("#createAnnoButton");
		assertEquals(tc.getCreating(), true);
		
		press(KeyCode.CONTROL);
		moveTo(tc.getImagePane());
		press(MouseButton.PRIMARY).moveBy(1, 1).release(MouseButton.PRIMARY);
		release(KeyCode.CONTROL);
		
		Annotation test = tc.getCurrAnnotation();
		
		press(KeyCode.CONTROL);
		moveTo(test);
		press(MouseButton.PRIMARY).moveBy(2, 2).release(MouseButton.PRIMARY);
		release(KeyCode.CONTROL);
		
		press(KeyCode.CONTROL);
		moveTo(test);
		press(MouseButton.PRIMARY).moveBy(-4, -4).release(MouseButton.PRIMARY);
		release(KeyCode.CONTROL);
		
		moveTo(test);
		press(MouseButton.PRIMARY).moveBy(1, 1).release(MouseButton.PRIMARY);
		
		clickOn("#saveAnnoButton");
		
		assertEquals(tc.annotations.size(), 1);
	}
	
	@Test
	public void testExitHandler() {
		Platform.runLater(() -> {
			tc.exitHelper(new WindowEvent(stage, WindowEvent.ANY));
			WaitForAsyncUtils.waitForFxEvents();
			press(KeyCode.LEFT).release(KeyCode.LEFT);
			press(KeyCode.LEFT).release(KeyCode.LEFT);
			press(KeyCode.ENTER).release(KeyCode.ENTER);
		});
	}

}
