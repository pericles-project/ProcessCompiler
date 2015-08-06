package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmndi:BPMNDiagram")
public class Diagram {
	
	@XStreamAsAttribute
	private String id = "Diagram";
	@XStreamAlias("bpmndi:BPMNPlane")
	private Plane plane;
	
	//---- Getters and setters ----// 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Plane getPlane() {
		return plane;
	}
	public void setPlane(Plane plane) {
		this.plane = plane;
	}
}
