package eu.pericles.processcompiler.ecosystem;

public class DataFlowNode {
	
	private int sequenceStep;
	private String processSlot;
	
	public DataFlowNode(int step, String slot) {
		sequenceStep = step;
		this.processSlot = slot;
	}
	
	//--------------- GETTERS AND SETTERS ----------------//
	
	public int getSequenceStep() {
		return sequenceStep;
	}
	public void setSequenceStep(int processFlowStep) {
		this.sequenceStep = processFlowStep;
	}
	public String getProcessSlot() {
		return processSlot;
	}
	public void setProcessSlot(String processSlotID) {
		this.processSlot = processSlotID;
	}
	
	//--------------- HASHCODE AND EQUALS ----------------//
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sequenceStep;
		result = prime * result + ((processSlot == null) ? 0 : processSlot.hashCode());
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
		if (processSlot == null) {
			if (other.processSlot != null)
				return false;
		} else if (!processSlot.equals(other.processSlot))
			return false;
		return true;
	}

}
