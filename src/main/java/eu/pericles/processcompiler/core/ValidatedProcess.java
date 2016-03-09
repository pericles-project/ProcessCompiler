package eu.pericles.processcompiler.core;

import java.util.HashMap;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;

public class ValidatedProcess {
	private BPMNProcess bpmnProcess;
	private HashMap<InputSlot, Object> inputConnections;
	private HashMap<OutputSlot, Object> outputConnections;
	
	public ValidatedProcess(BPMNProcess bpmnProcess, HashMap<InputSlot, Object> inputConnections, HashMap<OutputSlot, Object> outputConnections) {
		this.bpmnProcess = bpmnProcess;
		this.inputConnections = inputConnections;
		this.outputConnections = outputConnections;
	}

	public BPMNProcess getBpmnProcess() {
		return bpmnProcess;
	}
	public void setBpmnProcess(BPMNProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}
	public HashMap<InputSlot, Object> getInputConnections() {
		return inputConnections;
	}
	public void setInputConnections(HashMap<InputSlot, Object> inputConnections) {
		this.inputConnections = inputConnections;
	}
	public HashMap<OutputSlot, Object> getOutputConnections() {
		return outputConnections;
	}
	public void setOutputConnections(HashMap<OutputSlot, Object> outputConnections) {
		this.outputConnections = outputConnections;
	}

}
