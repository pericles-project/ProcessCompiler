package eu.pericles.modelcompiler.jbpm.Diagram;

import eu.pericles.modelcompiler.common.Element;

public class ConnectionElement implements Element {
	
	private String uid;
	private String bpmnElement;
	private String sourceElement;
	private String targetElement;
	
	//---- Getters and setters ----// 

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
