package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import eu.pericles.modelcompiler.common.Element;

@XStreamAlias("bpmndi:BPMNShape")
public class Shape implements Element {

	@XStreamAsAttribute
	@XStreamAlias("id")
	private String uid;
	@XStreamAsAttribute
	private String bpmnElement;
	@XStreamAlias("dc:Bounds")
	private Bounds bounds;

	// ---- Getters and setters ----//

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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
