package main.java.imganno.objects;

import java.util.ArrayList;

import javafx.scene.image.Image;

public class ImageData {
	private final Image image;	
	
	public ArrayList<Annotation> annotations;
	
	public ImageData(Image image) {
		this.image = image;
		this.annotations = new ArrayList<>();
	}

	public Image getImage() {
		return image;
	}

	public int getWidth() {
		return (int) image.getWidth();
	}

	public int getHeight() {
		return (int) image.getHeight();
	}
}
