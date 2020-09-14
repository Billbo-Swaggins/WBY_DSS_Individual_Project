package main.java.imganno.objects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestAnnotation {

	@Test
	public void testConstructor() {
		Annotation a = new Annotation("comment", 1.0, 2.0);
		assertEquals("comment", a.comment);
		assertEquals(1.0, a.getX());
		assertEquals(2.0, a.getY());
	}
	
	@Test
	public void testWidth() {
		Annotation a = new Annotation("comment", 1.0, 2.0);
		a.setWidth(3.0);
		assertEquals(3.0, a.getWidth());
	}
	
	@Test
	public void testHeight() {
		Annotation a = new Annotation("comment", 1.0, 2.0);
		a.setHeight(4.0);
		assertEquals(4.0, a.getHeight());
	}

}
