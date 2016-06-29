package eu.pericles.processcompiler.ecosystem;

public class DataConnection {
	
	private DataFlowNode resourceNode;
	private DataFlowNode slotNode;
	
	public DataConnection() {
	}
	
	public DataConnection(DataFlowNode slotNode, DataFlowNode resourceNode) {
		this.slotNode = slotNode;
		this.resourceNode = resourceNode;
	}
	
	public String getSlot() {
		return getSlotNode().getProcessSlot();
	}
	
	public String getResource() {
		return getResourceNode().getProcessSlot();
	}
	
	public int getSequenceStep() {
		return getSlotNode().getSequenceStep();
	}
	
	//--------------- GETTERS AND SETTERS ----------------//

	public DataFlowNode getSlotNode() {
		return slotNode;
	}

	public void setSlotNode(DataFlowNode entrySlot) {
		this.slotNode = entrySlot;
	}

	public DataFlowNode getResourceNode() {
		return resourceNode;
	}

	public void setResourceNode(DataFlowNode resourceSlot) {
		this.resourceNode = resourceSlot;
	}
	
	//--------------- HASHCODE AND EQUALS ----------------//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((slotNode == null) ? 0 : slotNode.hashCode());
		result = prime * result + ((resourceNode == null) ? 0 : resourceNode.hashCode());
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
		DataConnection other = (DataConnection) obj;
		if (slotNode == null) {
			if (other.slotNode != null)
				return false;
		} else if (!slotNode.equals(other.slotNode))
			return false;
		if (resourceNode == null) {
			if (other.resourceNode != null)
				return false;
		} else if (!resourceNode.equals(other.resourceNode))
			return false;
		return true;
	}

}
