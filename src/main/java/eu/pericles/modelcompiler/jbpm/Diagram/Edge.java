package eu.pericles.modelcompiler.jbpm.Diagram;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("bpmndi:BPMNEdge")
public class Edge {
	
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String bpmnElement;
	@XStreamAsAttribute
	private String sourceElement;
	@XStreamAsAttribute
	private String targetElement;
	@XStreamImplicit
	private List<Waypoint> points;

	//---- Getters and setters ----// 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBpmnElement() {
		return bpmnElement;
	}
	public void setBpmnElement(String bpmnElement) {
		this.bpmnElement = bpmnElement;
	}
	public String getSourceElement() {
		return sourceElement;
	}
	public void setSourceElement(String sourceElement) {
		this.sourceElement = sourceElement;
	}
	public String getTargetElement() {
		return targetElement;
	}
	public void setTargetElement(String targetElement) {
		this.targetElement = targetElement;
	}
	public List<Waypoint> getPoints() {
		return points;
	}
	public void setPoints(List<Waypoint> points) {
		this.points = points;
	}
}
