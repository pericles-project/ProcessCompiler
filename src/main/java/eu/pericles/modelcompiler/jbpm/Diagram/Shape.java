package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("bpmndi:BPMNShape")
public class Shape {

	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String bpmnElement;
	@XStreamAlias("dc:Bounds")
	private Bounds bounds;

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
	public Bounds getBounds() {
		return bounds;
	}
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}
}
