package eu.pericles.modelcompiler.jbpm.Diagram;

import eu.pericles.modelcompiler.common.Element;

public class NodeElement implements Element {

	private String uid;
	private String bpmnElement;

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
}
