package eu.pericles.processcompiler.ecosystem;

public class SlotConnection {
	
	private SequenceSlot inputSlot;
	private SequenceSlot resourceSlot;
	
	public SlotConnection(SequenceSlot inputSlot, SequenceSlot resourceSlot) {
		this.inputSlot = inputSlot;
		this.resourceSlot = resourceSlot;
	}
	
	//--------------- GETTERS AND SETTERS ----------------//

	public SequenceSlot getInputSlot() {
		return inputSlot;
	}

	public void setInputSlot(SequenceSlot inputSlot) {
		this.inputSlot = inputSlot;
	}

	public SequenceSlot getResourceSlot() {
		return resourceSlot;
	}

	public void setResourceSlot(SequenceSlot resourceSlot) {
		this.resourceSlot = resourceSlot;
	}
	
	//--------------- HASHCODE AND EQUALS ----------------//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inputSlot == null) ? 0 : inputSlot.hashCode());
		result = prime * result + ((resourceSlot == null) ? 0 : resourceSlot.hashCode());
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
		SlotConnection other = (SlotConnection) obj;
		if (inputSlot == null) {
			if (other.inputSlot != null)
				return false;
		} else if (!inputSlot.equals(other.inputSlot))
			return false;
		if (resourceSlot == null) {
			if (other.resourceSlot != null)
				return false;
		} else if (!resourceSlot.equals(other.resourceSlot))
			return false;
		return true;
	}

}
