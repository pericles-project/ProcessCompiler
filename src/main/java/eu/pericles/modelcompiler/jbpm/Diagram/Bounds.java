package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("dc:Bounds")
public class Bounds {
	@XStreamAsAttribute
	private String height = "0.0";
	@XStreamAsAttribute
	private String width = "0.0";
	@XStreamAsAttribute
	private String x = "0.0";
	@XStreamAsAttribute
	private String y = "0.0";

	//---- Getters and setters ----// 
	
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
}
