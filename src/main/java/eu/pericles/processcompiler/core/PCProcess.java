package eu.pericles.processcompiler.core;

import java.util.ArrayList;
import java.util.List;

import eu.pericles.processcompiler.bpmn.BPMNProcess;
import eu.pericles.processcompiler.ecosystem.InputSlot;
import eu.pericles.processcompiler.ecosystem.OutputSlot;
import eu.pericles.processcompiler.ecosystem.ProcessBase;
import eu.pericles.processcompiler.ecosystem.Slot;
import eu.pericles.processcompiler.exceptions.PCException;

public class PCProcess {

	private String id;
	private String name;
	private List<InputSlot> inputSlots = new ArrayList<>();;
	private List<OutputSlot> outputSlots = new ArrayList<>();;
	private BPMNProcess bpmnProcess;
	
	public PCProcess copy(ProcessBase process) {
		this.id = process.getId();
		this.name = process.getName();
		this.inputSlots = process.getInputSlots();
		this.outputSlots = process.getOutputSlots();
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<InputSlot> getInputSlots() {
		return inputSlots;
	}

	public void setInputSlots(List<InputSlot> inputSlots) {
		this.inputSlots = inputSlots;
	}

	public List<OutputSlot> getOutputSlots() {
		return outputSlots;
	}

	public void setOutputSlots(List<OutputSlot> outputSlots) {
		this.outputSlots = outputSlots;
	}

	public BPMNProcess getBpmnProcess() {
		return bpmnProcess;
	}

	public void setBpmnProcess(BPMNProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}
	
	public List<Slot> getSlots() {
		List<Slot> slots = new ArrayList<Slot>();
		slots.addAll(this.inputSlots);
		slots.addAll(this.outputSlots);
		return slots;
	}

	public Slot findSlotByID(String slotId) throws PCException {
		for (Slot slot : getSlots())
			if (slot.getId().equals(slotId))
				return slot;
		throw new PCException("Slot " + slotId + " doesn't exist");
	}

}
