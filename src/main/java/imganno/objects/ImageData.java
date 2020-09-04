package main.java.imganno.objects;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class ImageData {
	private final Image image;	
	private final int width;
	private final int height;
	
	public ArrayList<Annotation> annotations;
	
	public ImageData(Image image) {
		this.image = image;
		this.width = (int) image.getWidth();
		this.height = (int) image.getHeight();
		this.annotations = new ArrayList<>();
	}

	public Image getImage() {
		return image;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
