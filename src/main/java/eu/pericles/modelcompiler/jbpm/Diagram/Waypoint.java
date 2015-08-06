package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("di:waypoint")
public class Waypoint {
	@XStreamAlias("xsi:type")
	@XStreamAsAttribute
	private String type = "dc:Point";
	@XStreamAsAttribute
	private String x = "0.0";
	@XStreamAsAttribute
	private String y = "0.0";

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
