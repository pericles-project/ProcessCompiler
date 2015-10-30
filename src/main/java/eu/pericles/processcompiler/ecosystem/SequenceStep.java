package eu.pericles.processcompiler.ecosystem;

import java.util.Map;

public class SequenceStep {
	
	private String processId;
	private Map<String,String> inputMap;
	private Map<String,String> outputMap;
	
	//--------------- GETTERS AND SETTERS ----------------//
	
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public Map<String, String> getInputMap() {
		return inputMap;
	}
	public void setInputMap(Map<String, String> inputMap) {
		this.inputMap = inputMap;
	}
	public Map<String, String> getOutputMap() {
		return outputMap;
	}
	public void setOutputMap(Map<String, String> outputMap) {
		this.outputMap = outputMap;
	}
}
