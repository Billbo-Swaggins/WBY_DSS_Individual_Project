package main.java.imganno.objects;

public class Model {
	
	//TODO: add selectImage button
	private ImageData currImage;
	private Annotation currAnnotation;
	
	public Model() {
		
	}
	
	private void createAnnotation() {
		//do stuff
	}
	
	/** Getters and Setters below **/

	public ImageData getCurrImage() {
		return currImage;
	}

	public void setCurrImage(ImageData currImage) {
		this.currImage = currImage;
	}

	public Annotation getCurrAnnotation() {
		return currAnnotation;
	}

	public void setCurrAnnotation(Annotation currAnnotation) {
		this.currAnnotation = currAnnotation;
	}
}
