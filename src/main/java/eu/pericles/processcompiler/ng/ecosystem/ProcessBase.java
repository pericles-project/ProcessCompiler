package eu.pericles.processcompiler.ng.ecosystem;

import java.util.ArrayList;
import java.util.List;

public class ProcessBase {
	
	private String id;
	private String version;
	private String name;
	private String description;
	private List<InputSlot> inputSlots;
	private List<OutputSlot> outputSlots;
	private Implementation implementation;
	
	public ProcessBase() {
		super();
	}
	
	public ProcessBase(ProcessBase process) {
		setId(process.getId());
		setVersion(process.getVersion());
		setName(process.getName());
		setDescription(process.getDescription());
		setInputSlots(process.getInputSlots());
		setOutputSlots(process.getOutputSlots());
		setImplementation(process.getImplementation());
	}
	
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
	public List<InputSlot> getInputSlots() {
		return inputSlots;
	}
	public void setInputSlots(List<InputSlot> inputs) {
		this.inputSlots = inputs;
	}
	public List<OutputSlot> getOutputSlots() {
		return outputSlots;
	}
	public void setOutputSlots(List<OutputSlot> outputs) {
		this.outputSlots = outputs;
	}
	public List<Slot> getSlots() {
		List<Slot> slots = new ArrayList<Slot>();
		slots.addAll(inputSlots);
		slots.addAll(outputSlots);
		return slots;
	}
	public Implementation getImplementation() {
		return implementation;
	}
	public void setImplementation(Implementation implementation) {
		this.implementation = implementation;
	}
	
	//--------------- HASHCODE AND EQUALS ----------------//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((implementation == null) ? 0 : implementation.hashCode());
		result = prime * result + ((inputSlots == null) ? 0 : inputSlots.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((outputSlots == null) ? 0 : outputSlots.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessBase other = (ProcessBase) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (implementation == null) {
			if (other.implementation != null)
				return false;
		} else if (!implementation.equals(other.implementation))
			return false;
		if (inputSlots == null) {
			if (other.inputSlots != null)
				return false;
		} else if (!inputSlots.equals(other.inputSlots))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (outputSlots == null) {
			if (other.outputSlots != null)
				return false;
		} else if (!outputSlots.equals(other.outputSlots))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
}
