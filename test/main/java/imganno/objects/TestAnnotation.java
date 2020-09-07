package main.java.imganno.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestAnnotation {
	
	static Annotation a;
	
	@BeforeAll
	static void initialize() {
		a = new Annotation("comment", 1, 2);
		a.setWidth(3);
		a.setHeight(4);
	}
	
	@Test
	void testConstructor() {		
		assertEquals("comment", a.comment);
		assertEquals(1, a.getX());
		assertEquals(2, a.getY());
	}
	
	@Test
	void testWidth() {
		assertEquals(3, a.getWidth());
	}
	
	@Test
	void testHeight() {
		assertEquals(4, a.getHeight());
	}
}
