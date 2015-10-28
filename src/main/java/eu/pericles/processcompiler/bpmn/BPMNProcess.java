package eu.pericles.processcompiler.bpmn;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.model.TProcess;

public class BPMNProcess {
	
	private String targetNamespace;
	private TProcess process;
	private BPMNDiagram diagram;

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public TProcess getProcess() {
		return process;
	}

	public void setProcess(TProcess process) {
		this.process = process;
	}

	public BPMNDiagram getDiagram() {
		return diagram;
	}

	public void setDiagram(BPMNDiagram diagram) {
		this.diagram = diagram;
	}

}
