package eu.pericles.processcompiler.ecosystem;

import java.util.List;

public abstract class Process {
	
	private String id;
	private String version;
	private String name;
	private String description;
	private List<InputSlot> inputs;
	private List<OutputSlot> outputs;
	private Implementation implementation;
	
	//--------------- GETTERS AND SETTERS ----------------//
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<InputSlot> getInputs() {
		return inputs;
	}
	public void setInputs(List<InputSlot> inputs) {
		this.inputs = inputs;
	}
	public List<OutputSlot> getOutputs() {
		return outputs;
	}
	public void setOutputs(List<OutputSlot> outputs) {
		this.outputs = outputs;
	}
	public Implementation getImplementation() {
		return implementation;
	}
	public void setImplementation(Implementation implementation) {
		this.implementation = implementation;
	}
	
	

}
