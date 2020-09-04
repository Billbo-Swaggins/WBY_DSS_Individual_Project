package main.java.imganno.objects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import javafx.scene.image.Image;

public class TestAppView {

	static Model a;
	
	@BeforeAll
	static void initialize() {
		a = new Model();
	}
	
	@Test
	void testCurrImage() {
		assertEquals(null, a.getCurrImage());
		
		ImageData id = new ImageData(new Image("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"));
		a.setCurrImage(id);
		assertEquals(id, a.getCurrImage());
	}
	
//	@Test
//	void testCurrAnnotation() {
//		assertEquals(null, a.getCurrAnnotation());
//		
//		Annotation anno = new Annotation("hi", 1, 1);
//		a.setCurrAnnotation(anno);
//		assertEquals(anno, a.getCurrAnnotation());
//	}
}
