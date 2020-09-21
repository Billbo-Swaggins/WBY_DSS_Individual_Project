package main.java.imganno.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class ImageData {
	private final Image image;	
	private final File file;
	
	public ArrayList<Annotation> annotations;
	
	public ImageData(File file, Image image) {
		this.image = image;
		this.file = file;
		this.annotations = new ArrayList<>();
	}

	public Image getImage() {
		return image;
	}

	public double getWidth() {
		return image.getWidth();
	}

	public double getHeight() {
		return image.getHeight();
	}
}
