package main.java.imganno.objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Annotation extends Rectangle {
	
	/**
	 * When storing, have format like:
	 * 
	 * Filename: image filename.txt
	 * comment,absX,absY,width,height,transX,transY
	 * comment,absX,absY,width,height,transX,transY
	 * ...
	 * 
	 * Filename: image2 filename.txt
	 * ...
	 */

	public String comment;
	
	public Annotation(String comment, double x, double y) {
		this.comment = comment;
		this.setX(x);
		this.setY(y);
		this.setWidth(0);
		this.setHeight(0);
		
		this.setStroke(Paint.valueOf("black"));
		this.setFill(Color.TRANSPARENT);
	}
}
