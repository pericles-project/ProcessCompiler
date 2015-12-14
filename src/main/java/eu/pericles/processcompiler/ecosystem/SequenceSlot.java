package eu.pericles.processcompiler.ecosystem;

public class SequenceSlot {
	
	private String processFlowStep;
	private String processSlotID;
	
	public SequenceSlot(String step, String slot) {
		processFlowStep = step;
		processSlotID = slot;
	}
	
	//--------------- GETTERS AND SETTERS ----------------//
	
	public String getProcessFlowStep() {
		return processFlowStep;
	}
	public void setProcessFlowStep(String processFlowStep) {
		this.processFlowStep = processFlowStep;
	}
	public String getProcessSlotID() {
		return processSlotID;
	}
	public void setProcessSlotID(String processSlotID) {
		this.processSlotID = processSlotID;
	}
	
	//--------------- HASHCODE AND EQUALS ----------------//
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((processFlowStep == null) ? 0 : processFlowStep.hashCode());
		result = prime * result + ((processSlotID == null) ? 0 : processSlotID.hashCode());
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
		SequenceSlot other = (SequenceSlot) obj;
		if (processFlowStep == null) {
			if (other.processFlowStep != null)
				return false;
		} else if (!processFlowStep.equals(other.processFlowStep))
			return false;
		if (processSlotID == null) {
			if (other.processSlotID != null)
				return false;
		} else if (!processSlotID.equals(other.processSlotID))
			return false;
		return true;
	}

}
