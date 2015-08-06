package eu.pericles.modelcompiler.jbpm.Diagram;

import eu.pericles.modelcompiler.common.BaseElement;

public class NodeElement extends BaseElement {
	private String bpmnElement;
	
	//---- Getters and setters ----// 

	public String getBpmnElement() {
		return bpmnElement;
	}

	public void setBpmnElement(String bpmnElement) {
		this.bpmnElement = bpmnElement;
	}
}
