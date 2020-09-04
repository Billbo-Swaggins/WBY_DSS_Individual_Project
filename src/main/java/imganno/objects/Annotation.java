package main.java.imganno.objects;

public class Annotation {

	public String comment;
	public int xcoord;
	public int ycoord;
	public int width;
	public int height;
	
	public Annotation(String comment, int x, int y) {
		this.comment = comment;
		this.xcoord = x;
		this.ycoord = y;
	}
}
