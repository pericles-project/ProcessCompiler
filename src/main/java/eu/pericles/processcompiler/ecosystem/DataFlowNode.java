package eu.pericles.processcompiler.ecosystem;

public class DataFlowNode {
	
	private int sequenceStep;
	private String slot;
	
	public DataFlowNode(int step, String slot) {
		sequenceStep = step;
		this.slot = slot;
	}
	
	//--------------- GETTERS AND SETTERS ----------------//
	
	public int getSequenceStep() {
		return sequenceStep;
	}
	public void setSequenceStep(int processFlowStep) {
		this.sequenceStep = processFlowStep;
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String processSlotID) {
		this.slot = processSlotID;
	}
	
	//--------------- HASHCODE AND EQUALS ----------------//
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sequenceStep;
		result = prime * result + ((slot == null) ? 0 : slot.hashCode());
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
		DataFlowNode other = (DataFlowNode) obj;
		if (sequenceStep != other.sequenceStep)
			return false;
		if (slot == null) {
			if (other.slot != null)
				return false;
		} else if (!slot.equals(other.slot))
			return false;
		return true;
	}

}
