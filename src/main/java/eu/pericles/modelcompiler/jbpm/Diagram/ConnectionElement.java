package eu.pericles.modelcompiler.jbpm.Diagram;

import eu.pericles.modelcompiler.common.BaseElement;

public class ConnectionElement extends BaseElement {
	
	private String bpmnElement;
	private String sourceElement;
	private String targetElement;
	
	//---- Getters and setters ----// 
	
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
	
	

}
