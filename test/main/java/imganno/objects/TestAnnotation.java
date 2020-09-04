package main.java.imganno.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestAnnotation {
	
	static Annotation a;
	
	@BeforeAll
	static void initialize() {
		a = new Annotation("comment", 1, 2);
		a.width = 3;
		a.height = 4;
	}
	
	@Test
	void testConstructor() {		
		assertEquals("comment", a.comment);
		assertEquals(1, a.xcoord);
		assertEquals(2, a.ycoord);
	}
	
	@Test
	void testWidth() {
		assertEquals(3, a.width);
	}
	
	@Test
	void testHeight() {
		assertEquals(4, a.height);
	}
}
