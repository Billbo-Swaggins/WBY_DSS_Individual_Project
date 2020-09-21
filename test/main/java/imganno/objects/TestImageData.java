package main.java.imganno.objects;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import javafx.scene.image.Image;

public class TestImageData {
	
	@Test
	public void testConstructor() {
		File f = new File("temp");
		Image i = Mockito.mock(Image.class);
		ImageData id = Mockito.spy(new ImageData(f, i));
		
		when(id.getWidth()).thenReturn(10.0);
		when(id.getHeight()).thenReturn(20.0);
		
		assertEquals(id.getImage(), i);
		assertEquals(id.getWidth(), 10.0);
		assertEquals(id.getHeight(), 20.0);
	}

}
