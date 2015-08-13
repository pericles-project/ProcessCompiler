package eu.pericles.modelcompiler.jbpm.Diagram;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import eu.pericles.modelcompiler.common.Element;

@XStreamAlias("bpmndi:BPMNDiagram")
public class Diagram implements Element {

	@XStreamAsAttribute
	@XStreamAlias("id")
	private String uid;
	@XStreamAlias("bpmndi:BPMNPlane")
	private Plane plane;

	// ---- Getters and setters ----//

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}
}
