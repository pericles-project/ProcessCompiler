package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("di:waypoint")
public class Waypoint {
	@XStreamAlias("xsi:type")
	@XStreamAsAttribute
	private String type;
	@XStreamAsAttribute
	private String x;
	@XStreamAsAttribute
	private String y;

	//---- Getters and setters ----// 
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
