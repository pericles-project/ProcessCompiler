package eu.pericles.processcompiler.ecosystem;

public class DataConnection {
	
	private DataFlowNode resourceNode;
	private DataFlowNode entryNode;
	
	public DataConnection(DataFlowNode entryNode, DataFlowNode resourceNode) {
		this.entryNode = entryNode;
		this.resourceNode = resourceNode;
	}
	
	//--------------- GETTERS AND SETTERS ----------------//

	public DataFlowNode getEntryNode() {
		return entryNode;
	}

	public void setEntryNode(DataFlowNode entrySlot) {
		this.entryNode = entrySlot;
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
		result = prime * result + ((entryNode == null) ? 0 : entryNode.hashCode());
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
		if (entryNode == null) {
			if (other.entryNode != null)
				return false;
		} else if (!entryNode.equals(other.entryNode))
			return false;
		if (resourceNode == null) {
			if (other.resourceNode != null)
				return false;
		} else if (!resourceNode.equals(other.resourceNode))
			return false;
		return true;
	}

}
